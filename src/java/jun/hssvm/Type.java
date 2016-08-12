package jun.hssvm;

import java.util.ArrayList;

import jun.util.Util;

/**
 * The information of all samples having the same class type is recorded in 
 * this type object
 * 
 * @author jun
 * 
 */ 
 
public class Type {  

	private int len;            //the number of all samples owned by current type
	private int label;          //class label
    
    private ArrayList<double[]> sampleList;
    
    
    public Type() {}
    
    public Type(int label) {
        this.label = label;
        this.sampleList = new ArrayList<double[]>();
    }
    
    public void addSample(double[] s) {
        sampleList.add(s);
        len++;
    }
    
    //get the ith sample 
    public double[] getSample(int i) {
        return sampleList.get(i);
    }
    
    public int getLen() {
        return len;
    }
    
    public int getLabel() {
        return label;
    }

    /**
     * Get the jth dimention value of the ith sample, 
     */
    public double getValue(int i, int j) {
        return sampleList.get(i)[j];
    }
    
    /**
     *  randomly select subLen samples as predict-type, and the rest is as 
     *  train-type  
     * @param subLen  the samples length of sub predict-type 
     * @return Type[]  only has two elements, the first is trainType, second is predictType 
     */
    public Type[] randPick(int subLen){
        //the first is train type, the second is predict type 
        Type[] result = new Type[]{new Type(label), new Type(label)};
        
        // create assistant array
        int[] assist = new int[len];    
        Util.initArray(assist, 0);
        
        //get predicted type-object
        int[] pr = Util.randArray(len, subLen);
        for(int i=0; i<pr.length; i++){
            result[1].addSample(this.sampleList.get(pr[i]));
            assist[pr[i]] = 1; 
        }
        
        // get training type-object
        for(int i=0; i<len; i++){
            if(assist[i] == 0){
                result[0].addSample(this.sampleList.get(i));
            }
        }
        
        if((result[0].len + result[1].len) != len){
            throw new RuntimeException("Pick type error!");
        }
        
        return result;
    }
    
    /**
     * return a sub type object including the samples between beginIndex and endIndex
     * @param beginIndex include the sample with beginIndex
     * @param endIndex exclude the sample with endIndex
     */
    public Type subType(int beginIndex, int endIndex){
        if (beginIndex < 0) {
            throw new RuntimeException(indexOutOfBounds() + beginIndex);
        }
        if (endIndex > len) {
            throw new RuntimeException(indexOutOfBounds() + endIndex);
        }
        if (beginIndex > endIndex) {
            throw new RuntimeException(indexOutOfBounds() + (endIndex - beginIndex));
        }
        
        Type result = new Type(label);
        for(int i=beginIndex; i<endIndex; i++){
            result.addSample(sampleList.get(i));
        }
        
        return result;
    }
    
    /**
     * return a sub type object including samples from beginIndex to end
     * @param beginIndex include the sample with beginIndex
     * @param endIndex exclude the sample with endIndex
     */
    public Type subType(int beginIndex){
        if (beginIndex < 0 || beginIndex >= len) {
            throw new RuntimeException(indexOutOfBounds() + beginIndex);
        }
        
        Type result = new Type(label);
        for(int i=beginIndex; i<len; i++){
            result.addSample(sampleList.get(i));
        }
        
        return result;
    }
    
    /**
     * Get a sub type object that exclude the samples between beginIndex and endIndex
     * @param beginIndex include the sample with beginIndex
     * @param endIndex exclude the sample with endIndex
     */
    public Type exclude(int beginIndex, int endIndex){
        if (beginIndex < 0) {
            throw new RuntimeException(indexOutOfBounds() + beginIndex);
        }
        if (endIndex > len) {
            throw new RuntimeException(indexOutOfBounds() + endIndex);
        }
        if (beginIndex > endIndex) {
            throw new RuntimeException(indexOutOfBounds() + (endIndex - beginIndex));
        }
        
        Type result = new Type(label);
        for(int i=0; i<beginIndex; i++) {
            result.addSample(sampleList.get(i));
        }
        for(int i=endIndex; i<len; i++) {
            result.addSample(sampleList.get(i));
        }
        
        return result;
    }
    
    private String indexOutOfBounds(){
        return "Type index out of range: ";
    }
    
    public String toString(){
        return "Label: " + label + ", " + "samples size: " + len;
    }
}
