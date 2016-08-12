

import java.io.*;

import jun.hssvm.Param;
import jun.hssvm.Problem;
import jun.hssvm.SVM;
import jun.hssvm.SVMConst;
import jun.hssvm.kernel.KernelType;
import jun.util.Util;
import jun.util.Time;

/**
 * @author jun
 *
 */
public class Trainer {

    private String trainFile;        
	private String modelFile;		 
    
    private int loopVar = 0;    //Only for command line parsing
    
    
	public static void main(String[] args) throws IOException {
		new Trainer().run(args);
	}

    private void run(String[] args) throws IOException {
        if(args.length == 0){
            Util.outln(help());
            return;
        }
        Param param = parseCmdLine(args);
        if(param == null) {
            return;
        }
		param.check();
        
        Problem prob = SVM.readProblem(trainFile, null);  
        if(prob == null){
            return;
        }
        
        long startTime = Time.get();
        
        SVM.init(param);
        SVM.saveModel(modelFile, SVM.train(prob));
        
        Util.outln("Training completed");
        Util.outln("Total time: " + Time.show(Time.get()-startTime));
    }

    private Param parseCmdLine(String[] args) {
        Param param = new Param();
        
        for(loopVar=0; loopVar<args.length; loopVar++){
            if(args[loopVar].equals("?")){
                Util.outln(help());
                return null;
            }
            
            if(args[loopVar].charAt(0) == '-'){//is option 
                if(args[loopVar].length() != 2){
                    Util.errln("Error option: " + args[loopVar]);
                    return null;
                }
                
                switch(args[loopVar].charAt(1)) {
                    case 'k': {//kernel type
                        Integer item = (Integer)processOption(args, Integer.class);
                        if(item == null){
                            return null;
                        }
                        param.kernelType = KernelType.getEnum(item.intValue());
                        break;
                    }
                    case 'e': {
                        Double item = (Double)processOption(args, Double.class);
                        if(item == null){
                            return null;
                        }
                        param.eps = item;
                        break;
                    }
                    case 'C': { 
                        Double item = (Double)processOption(args, Double.class);
                        if(item == null){
                            return null;
                        }
                        param.C = item;
                        break;
                    }
                    case 'g': {
                        Double item = (Double)processOption(args, Double.class);
                        if(item == null){
                            return null;
                        }
                        param.gamma = item;
                        break;
                    }
                    case 'm': {
                        Double item = (Double)processOption(args, Double.class);
                        if(item == null){
                            return null;
                        }
                        param.cacheFactor = item;
                        break;
                    }
                    default: {
                        Util.errln("Unknown option: " + args[loopVar]);
                        return null;
                    }
                }
            } else {// is argument
                if(trainFile == null || trainFile.equals("")){
                    trainFile = args[loopVar];
                } else {
                    modelFile = args[loopVar];
                }
            }
        }
        
        if(trainFile == null) {
            Util.errln("Require training file!");
            return null;
        } else if(modelFile == null) {
            modelFile = trainFile + SVMConst.MODEL_FILE_SUF;
        }
        
        return param;
    }
    
    //check if the option is correct TODO inline it
    private Number processOption(String[] args, Class clzz) {
        Number item = null;
        if(loopVar+1 < args.length){//if has item after the option
            item = Util.toNumber(args[loopVar+1], clzz);
        } else {
            Util.errln("No any items after Option: " + args[loopVar]);
        }
        
        if(item != null){
            loopVar++;
        } 
        
        return item;
    }
    
    private static String help() {
        return Usage.TRAINING;
    }
}
