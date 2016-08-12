package jun.hssvm;

import java.util.ArrayList;

import jun.hssvm.kernel.Kernel;
import jun.util.Util;

/**
 * @author jun
 *
 */
public class Sphere implements java.io.Serializable {
    private static final long serialVersionUID = 10000001;

    private int label;
    private ArrayList<double[]> SVs;        //support vectors
    private double rs;                      //square of radius
    private double[] alpha;
    
    //assistant VARs
    private double mainValue;               //the value of alpha^T * Q * alpha
    private ArrayList<Double> nonZeroAlpha;  // non-zero lagrange multipliers
	
    
    public Sphere(){}
    
    public Sphere(int label, double[] alpha, ArrayList<double[]> SVs){
        this.label = label;
        this.alpha = alpha;
        this.SVs = SVs;
    }
    
    public void calRs(Kernel kernel){
        getNonzeroAlpha();
        calMainValue(kernel);
        
        /*
         * when calculating the value of rs, using different SV will show some
         * small deviations, so we calculate the mean value 
         */
        double rsSum = 0;
        for(int i=0; i<SVs.size(); i++){
            rsSum += getDistanceToCenter(SVs.get(i), kernel);
        }
        rs = rsSum/SVs.size();
    }
    
    /**
     * Calculate the square-distance from some point z to sphere center
     * D^2 = K(x,x)-2*sum + mainValue  
     * sum = alpha[0]*K(x,x0)+...+alpha[j]*K(x,xj)+...+alpha[n]*K(x,xn)
     */
    public double getDistanceToCenter(double[] x, Kernel kernel){
        double sum = 0;
        for(int j=0; j<nonZeroAlpha.size(); j++){
            sum += nonZeroAlpha.get(j) * kernel.K(x, SVs.get(j));
        }
        
        return kernel.K(x, x) - 2*sum + mainValue;
    }
    
	public int getLabel(){
		return label;
	}
	
	public double getRs(){
		return rs;
	}
    
    //  Calculate the value of alpha^T * Q * alpha
    //  Q is the kernel matrix
    private void calMainValue(Kernel kernel) {
        for(int i=0; i<nonZeroAlpha.size(); i++) {
            double sum = 0;
            double[] xi = SVs.get(i);
            for(int j=0; j<nonZeroAlpha.size(); j++){
                sum += nonZeroAlpha.get(j) * kernel.K(xi, SVs.get(j));
            }
            mainValue += nonZeroAlpha.get(i) * sum;
        }
    }

    private void getNonzeroAlpha() {
        nonZeroAlpha = new ArrayList<Double>();
        for(int i=0; i<alpha.length; i++) {
            if(alpha[i] > 0) {
                nonZeroAlpha.add(alpha[i]);
            }
        }
    }
    
    //output 
    public String toString(){
        StringBuffer result = new StringBuffer(80);
        result.append("Sphere " + label + "\n");
        result.append("The number of SVs: " + SVs.size() +"(total " + alpha.length + ")\n");
        result.append("Square of Radius: " + rs +"\n\n");
        
        return result.toString();
    }
}
