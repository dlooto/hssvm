package jun.hssvm;

import java.io.DataOutputStream;
import java.io.IOException;

import jun.util.File;
import jun.util.Util;

/**
 * The original sample data will be read from data file and be stored in this 
 * class, it will contain the label of sample and the information of all samples 
 * @author jun
 *
 */

public class Problem {
	private int l;           // the number of all samples
    private int d;           // the dimentions of single sample , 
    
	private int[] y;     	 // labels
    private double[][] x;    // sample data
    
    public Problem() {}
    
    public Problem(int l, int d){
        this.l = l;
        this.d = d;
        
        y = new int[l];
        x = new double[l][d];
    }
    
    /** set label value for the ith sample */  
    public void setYi(int i, int value) {
        y[i] = value;
    }
    
    /** set a double array for the ith sample */
    public void setXi(int i, double[] s){
        this.x[i] = s;
    }
    
    /** get the label of the ith sample */
    public int getYi(int i) {
        return y[i];
    }
    
    /** get the ith sample data */ 
    public double[] getXi(int i) {
        return x[i];
    }
    
    public int getDims() {
        return d;
    }
    
    public int getLen(){
        return l;
    }
    
    /**
     * scale data by Mean-variance method
     * xi-->(xi-v)/var(X)
     * X=(x1,x2,...,xm)
     * v = mean(X)=(x1+x2+...+xm)/m
     * var(X) = ((x1-v)^2 + (x2-v)^2 +...+ (xm-v)^2)/m
     * @noused
     */
    public void scaleData(){
        Util.outln("Scaling data...");
        
        for(int j=0; j<d; j++){ // for every dimendion...
            
            // Get mean value
            double mean = 0;
            for(int i=0; i<l; i++){
                mean += x[i][j];
            }
            mean = mean/l;
            
            // Get variance 
            double variance = 0;
            for(int i=0; i<l; i++){
                variance += (x[i][j]-mean) * (x[i][j]-mean);
            }
            variance = variance / (l-1);
            
            // scale data
            for(int i=0; i<l; i++){
                x[i][j] = (x[i][j]-mean) / variance;
            }
        }
    }
    
    public void scaleData(Scope scaleScope){
        for(int j=0; j<d; j++) { // For every dimension...
            // initialize
            double[] a = new double[l];//assistant array
            for(int i=0; i<a.length; i++){
                a[i] = x[i][j]; 
            }
            
            Util.scale(a, scaleScope.getLower(), scaleScope.getUpper());
            
            // update x[][j]
            for(int i=0; i<a.length; i++){
                x[i][j] = a[i];
            }
        }
    }
    
    /**
     * Merge with another problem to form a bigger problem
     * @param p2 this problem will be merged with current problem
     * @return merged problem
     */
    public Problem mergeWith(Problem p2){
        if(d != p2.getDims()){
            throw new RuntimeException("Can't merge the two problem, the dimemtions" +
                    "of two files are not equal!");
        }
        
        Problem result = new Problem(l+p2.getLen(), d);
        for(int i=0; i<l; i++){
            result.setYi(i, y[i]);
            result.setXi(i, x[i]);
        }
        for(int i=0; i<p2.l; i++){
            result.setYi(i+l, p2.getYi(i));
            result.setXi(i+l, p2.getXi(i));
        }
        
        return result;
    }

    /**
     * divide a problem into two sub-problem 
     * @param len  length of the first sub problem,  
     */
    public void decompose(int len, Problem sub1, Problem sub2){
        if(len >= l){
            throw new RuntimeException("The length of sub-problem is too long.");
        }
        
        for(int i=0; i<sub1.l; i++){
            sub1.setYi(i, y[i]);
            sub1.setXi(i, x[i]);
        }
        
        for(int i=0; i<sub2.l; i++){
            sub2.setYi(i, y[i+sub1.l]);
            sub2.setXi(i, x[i+sub1.l]);
        }
    }
    
    /**
     * write problem to file, the written format is same as original sample data
     * @param outFile the output file name  
     * @param format 
     */
    public void writeToFile(String outFile, Format format){
        // by default, using no-index format
        if(format == null) {
            format = new Format();
        }
        
        DataOutputStream output = null;
        try {
            output = File.createDataOutputStream(outFile);
            
            for(int i=0; i<l; i++) {
                output.writeBytes(y[i] + "    ");
                for(int j=0; j<d; j++){
                    output.writeBytes(format.get(j, x[i][j]) + "   ");
                }
                output.writeBytes("\n");
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if(output != null){
                try {
                    output.close();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
