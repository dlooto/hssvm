package jun.hssvm;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import jun.util.File;
import jun.util.Util;

/**
 * store predicted result. If put objects of this class into collection, it's 
 * better to use Set rather than list to get non-duplicate elements
 * @author jun
 */  
public class Outcome {
    
    private double C;
    private double gamma;
    private int correct;                // the number of samples predicted correctly  
    private int total;                  // the total number of samples 
    
    private ArrayList<Integer> realLabels;      //real labels 
    private ArrayList<Integer> predictLabels;   //predicted labels 

    
    public Outcome() {}
    
    public Outcome(double c, double gamma){
        this.C = c;
        this.gamma = gamma;
        realLabels = new ArrayList<Integer>();
        predictLabels = new ArrayList<Integer>();
    }

    public double getC() {
        return C;
    }

    public double getGamma() {
        return gamma;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrectNum(int correct) {
        this.correct = correct;
    }

    public double getAccuracy(){
        return (double)correct/getTotal();
    }
    
    public int getTotal() {
        return total;
    }

    public void setTotalNum(int total) {
        this.total = total;
    }
    
    public ArrayList<Integer> getPredictLabels() {
        return predictLabels;
    }

    public ArrayList<Integer> getRealLabels() {
        return realLabels;
    }
    
    public void addRealLabel(int label){
        realLabels.add(label);
    }
    
    public void addPredictLabel(int label){
        predictLabels.add(label);
    }

    public void writeToFile(String fileName){
        DataOutputStream output = null;
        try {
            output = File.createDataOutputStream(fileName);
                    
            for(int i=0; i<predictLabels.size(); i++) {
                 if(realLabels.get(i).equals(predictLabels.get(i))) {
                     output.writeBytes(realLabels.get(i)+"\t"+predictLabels.get(i) + "\n");
                 } else {
                     output.writeBytes(realLabels.get(i)+"\t"+predictLabels.get(i)+
                             "\t" + SVMConst.ERR_MARK+"\n");
                 }
            }
            output.writeBytes("\n" + this);
        } catch(Exception e) {
            if(e instanceof FileNotFoundException) {
                Util.errln("File not found: " + fileName);
            } else {
                Util.errln("Write prediction result to file exception!");
            }
            return;
        } finally {
            try{
                if(output != null){
                    output.close();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public String toString(){
        return ("C=" + C + "  gamma=" + gamma + "  accuracy=" + getAccuracy()*100 +
                 "%(" + correct + "/" + total + ")");
    }
    
    public String forFile(){
        return (C + "\t" + gamma + "\t" + getAccuracy());
    }
    
    /**
     * override this method to put Outcome object into Set list that contains 
     * no duplicate elements)
     */
    @Override
    public boolean equals(Object obj) {
    	if(this == obj) {
    		return true;
    	}
    	if(obj == null) {
    		return false;
    	}
    	if(obj instanceof Outcome) {
    		final Outcome o = (Outcome)obj; 
        	if(o.hasEqualC(this) && o.hasEqualGamma(this) && o.hasEqualAccuracy(this)) {
        		return true;
        	}
    	}
    	
    	return false;
    }
    
    /**
     * for set that contains non-duplicate elements
     */
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int)new Double(C).hashCode();
		result = prime * result + (int)new Double(gamma).hashCode();
		result = prime * result + correct + total;
		return result;
	}
    
    private boolean hasEqualC(Outcome o) {
    	if(Math.abs(o.C-this.C) <= 0.0001) {
    		return true;
    	}
    	return false;
    }
    
    private boolean hasEqualGamma(Outcome o) {
    	if(Math.abs(o.gamma-this.gamma) <= 0.0001) {
    		return true;
    	}
    	return false;
    }
    
    private boolean hasEqualAccuracy(Outcome o) {
    	if((o.correct == this.correct) && (o.total == this.total)) {
    		return true;
    	}
    	return false;
    }
}
