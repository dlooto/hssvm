package jun.hssvm;

import java.io.*;
import java.util.*;

import jun.hssvm.kernel.Kernel;
import jun.hssvm.kernel.RBFKernel;
import jun.util.Util;
import jun.util.File;

/**
 * @author jun
 *
 */
public class SVM {
    
    private static Param param;         //Only used by training process
    private static Kernel kernel;       //Chosed kernel function   
    
    
    //
    // Interface functions
    //
    
    //  initialize static globel variables
    public static void init(Param _param){
        param = _param;
        if(kernel == null){
            kernel = createKernel(param);
        } else {
            kernel.updateGamma(param.gamma);
        }
    }
    
    public static HSModel train(Problem prob) {
        return train(groupSamples(prob));
    }
    
    //Used for parameter optimum
    public static HSModel train(ArrayList<Type> typeList){
        if(typeList.size() <2){
            throw new RuntimeException("The number of classes < 2!");
        }
        
        Sphere[] hsa = new Sphere[typeList.size()];
        for(int i=0;i<hsa.length;i++) {
            hsa[i] = trainOneClass(typeList.get(i));
        }
        
        // Output spheres 
        Util.printArray(hsa);
        
        return new HSModel(param, hsa);
    }
    
    public static Outcome predict(Problem prob, HSModel model) {
        Param param = model.getParam();
        Outcome result = new Outcome(param.C, param.gamma);
        
        int correct = 0;
        for(int i=0; i<prob.getLen(); i++) {
            int predictedLabel = predictOneSample(model.getSpheres(), prob.getXi(i));
            if(prob.getYi(i) == predictedLabel){
                correct++;
            }
            
            result.addRealLabel(prob.getYi(i));
            result.addPredictLabel(predictedLabel);
        }
        
        result.setCorrectNum(correct);
        result.setTotalNum(prob.getLen());
        
        return result;
    }
    
    //Used for parameter optimum
    public static Outcome predict(ArrayList<Type> typeList, HSModel model) {
        Param p = model.getParam();
        Outcome result = new Outcome(p.C, p.gamma);
        
        int correct = 0;
        int total = 0;
        for(int i=0; i<typeList.size(); i++) {
            Type type = typeList.get(i);
            for(int j=0; j<type.getLen(); j++) {
                int predictedLabel = predictOneSample(model.getSpheres(), 
                        type.getSample(j));
                if(predictedLabel == type.getLabel()) {
                    correct++;
                }
                total++;
            }
        }
        
        result.setCorrectNum(correct);
        result.setTotalNum(total);
        
        return result;
    }
    
    //By default, read the no-index format file  
    public static Problem readProblem(String fileName, Format format){
        if(format == null) {
            format = new Format();
        }
        
        BufferedReader buffReader = null;
        try {
            buffReader = File.createBufferedReader(fileName);
            if(format instanceof IndexFormat){
                return processIndexData(buffReader);
            } else {
                return processNoIndexData(buffReader);
            }
        } catch (IOException ioe){
            Util.errln("Read problem exception!");
            return null;
        } finally {
            if(buffReader != null){
                try {
                    buffReader.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void saveModel(String modelFile, HSModel model) {
        try{
            ObjectOutputStream oos = File.createObjectOutputStream(modelFile);
            if(oos == null){
                return;
            }
            oos.writeObject(model);
            oos.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public static HSModel loadModel(String modelFile){
        ObjectInputStream ois = null;       
        try {
            ois = File.createObjectInputStream(modelFile);
            return (HSModel)ois.readObject();
        } catch(Exception e) {
            Util.errln("Load model from file " + modelFile+ " exception!");
            return null;
        } finally {
            try {
                if(ois != null){
                    ois.close();    
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    
    //process data file that has "index:value" pair 
    private static Problem processIndexData(BufferedReader fp) throws IOException {
        if(fp == null){
            return null;
        }
        
        ArrayList<Integer> vy = new ArrayList<Integer>();
        ArrayList<DimNode[]> ivx = new ArrayList<DimNode[]>();
        
        int maxIndex = 1;
        while(true) {
            String line = fp.readLine();
            if(line == null) break;
            StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");
            
            //add the class label
            //vy.add((int)Util.atof(st.nextToken()));
            vy.add((Integer)Util.toNumber(st.nextToken(), Integer.class));
            
            int m = st.countTokens()/2;
            DimNode[] s = new DimNode[m];
            for(int j=0; j<m; j++) {
                //int index = Util.atoi(st.nextToken());
                //double value = Util.atof(st.nextToken());
                int index = (Integer)Util.toNumber(st.nextToken(), Integer.class);
                double value = (Double)Util.toNumber(st.nextToken(), Double.class);
                
                s[j] = new DimNode(index, value);
            }
            if(m > 0) {
                maxIndex = Math.max(maxIndex, s[m-1].getIndex());
            }
           ivx.add(s);
        }
        
        //
        //build problem
        //
        Problem prob = new Problem(vy.size(), maxIndex);
        for(int i=0;i<prob.getLen();i++) {
            prob.setYi(i, vy.get(i));
            
            //Add one sample data
            int si = 0;
            DimNode[] s = ivx.get(i);
            double[] oneSample = new double[prob.getDims()];
            for(int j=0; j<prob.getDims(); j++){
                if(s[si].getIndex() > j+1) { //lack option, pad 0
                    oneSample[j] = 0;
                } else {//curren index=j+1 
                    oneSample[j] = s[si].getValue();
                    if(si < s.length-1) {//if has next index
                        si++;
                    } else {
                        for(int k=j+1; k<prob.getDims(); k++) {
                            oneSample[k] = 0;
                        }
                        break;
                    }
                }
            }
            prob.setXi(i, oneSample);
        }
        
        return prob;
    }
    
    //Process no-index format data file
    private static Problem processNoIndexData(BufferedReader fp) throws IOException {
        if(fp == null){
            return null;
        }
        
        ArrayList<Integer> vy = new ArrayList<Integer>();
        ArrayList<double[]> vx = new ArrayList<double[]>();
        
        while(true) {
            String line = fp.readLine();
            if(line == null) break;
            StringTokenizer st = new StringTokenizer(line, " \t\n\r\f");
            
            //add the class label
            vy.add(Util.toInt(st.nextToken()));           
            
            //add one sample data 
            double[] oneSample = new double[st.countTokens()];  
            for(int j=0; j<oneSample.length; j++) {
                oneSample[j] = Util.toDouble(st.nextToken());
            }
            vx.add(oneSample);       
        }

        Problem prob = new Problem(vy.size(), vx.get(0).length);
        for(int i=0;i<prob.getLen();i++) {
            prob.setYi(i, vy.get(i));
            prob.setXi(i, vx.get(i));
        }
        return prob;
    }
    
    private static Sphere trainOneClass(Type type) {
        double[] alpha = new HSSolver(param, type, kernel).doSolve();
        
        // Get the support vectors 
        ArrayList<double[]> sVectors = new ArrayList<double[]>();  //SVs
        for(int i=0; i<alpha.length; i++) {
            if(alpha[i] > 0){ 
                sVectors.add(type.getSample(i));
            }
        }
        
        Sphere hs = new Sphere(type.getLabel(), alpha, sVectors);
        hs.calRs(kernel);
        
        return hs;
    }
    
    private static int predictOneSample(Sphere[] sa, double[] sample) {
        int drop = 0;       //the number of spheres that the sample drop into 
        int which = -1;     //it's available only when the sample falls into only one sphere
        
        int min = 0;    
        double minValue = Double.MAX_VALUE;
        
        //Decide which sphere the sample belongs to  
        for(int i=0; i<sa.length; i++) {
            double ds = sa[i].getDistanceToCenter(sample, kernel);
            if(ds-sa[i].getRs() <= 0) {
                drop++;
                which = i;
            } // else, drop out of the sphere
            
            double currentValue = Math.abs((ds - sa[i].getRs()) / sa[i].getRs());
            if(currentValue < minValue) {
                minValue = currentValue;
                min = i;
            }
        }
        
        if(drop == 1) {
            return sa[which].getLabel();
        } 
        
        return sa[min].getLabel();  
    }
    
    /**
     * Group training data, based on class label
     * @param prob
     * @return
     * @deprecated use the method groupSamples() to take place, which use hashMap way 
     */
    public static ArrayList<Type> groupClasses(Problem prob) {
        ArrayList<Type> typeList = new ArrayList<Type>();
        
        /* the number of classes, which have been scanned currently */
        int cl = 0;
        
        for(int i=0; i<prob.getLen(); i++) {
            //at first, suppose the next sample belong to unscanned class
            boolean unshowed = true;      
            
            for(int j=0; j<cl; j++) {   
                //if true,the sample will be added into a found class type
                if(prob.getYi(i) == typeList.get(j).getLabel()) {
                    typeList.get(j).addSample(prob.getXi(i));  
                    unshowed = false;
                    break;
                }
            }
            
            //if true, a new class type must have been scanned,
            //that is , the "for statement" will not be executed above 
            if(unshowed) { 
                Type t = new Type(prob.getYi(i));
                t.addSample(prob.getXi(i));
                typeList.add(t);
                cl++;
            }
        }
              
        return typeList;
    }
    
    // use hash-map way to group sample data 
    public static ArrayList<Type> groupSamples(Problem prob) {
        Map<Integer, Type> typeMap = new HashMap<Integer, Type>();

        for(int i = 0; i < prob.getLen(); i++) {
        	Integer label = prob.getYi(i);
        	if(typeMap.containsKey(label)) {
        		typeMap.get(label).addSample(prob.getXi(i));
        	} else {
        		Type t = new Type(label);
        		t.addSample(prob.getXi(i));
        		typeMap.put(label, t);
        	}
        }
        
        //convert map to list 
        ArrayList<Type> result = new ArrayList<Type>();
        for(Type t: typeMap.values()) {
        	result.add(t);
        }
        
        return result;
    }
    
    private static Kernel createKernel(Param param){
        switch(param.kernelType) {
            case POLY: {
            	throw new RuntimeException("Not implemented temporarily");
            	//return new PolyKernel(param.gamma, param.coef0, param.degree);
            }
            case RBF: {
                return new RBFKernel(param.gamma);
            }
            //other kernel type...
            default:
                return new RBFKernel(param.gamma);
        }
    }
    
    //////only used for processing index:value-format data file///////
    static class DimNode {
        int index;
        double value;
        
        DimNode() {}
        
        DimNode(int index, double value) {
            this.index = index;
            this.value = value;
        }
        
        int getIndex(){
            return index;
        }
        
        double getValue() {
            return value;
        }
    }
}
