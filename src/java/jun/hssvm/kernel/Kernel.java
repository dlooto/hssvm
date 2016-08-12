package jun.hssvm.kernel;

/**
 * @author jun
 */

abstract public class Kernel {
   
    protected double gamma;
    
    protected Kernel() {}
    
    protected Kernel(double gamma) {
        this.gamma = gamma;
    }
    
    /**
     * use kernel function to calculate K(x,y)  
     */
    abstract public double K(double[] x, double[] y);
      
    /**
     * calculate the distance's square of two vectors using kernel way
     * such as :  ||x-y||^2 = K(x,x) - 2*K(x,y) + K(y,y)
     */
    public double calKernelDist(double[] x, double[] y){
        if(x.length != y.length) {
            throw new RuntimeException("The vector dimention error !");
        }
        
        return ( K(x,x) - 2*K(x,y) + K(y,y) );
    }
    
    /**
     * Calculate square of distance about two vectors by dot-product way only
     * like ||x - y||^2 = x.x - 2*x.y + y.y
     */
    public double calDotDist(double[] x, double[] y){        
        return ( dot(x,x) - 2*dot(x,y) + dot(y,y) );
    }
    
    public double dot(double[] x, double[] y) {
        double sum = 0;
        for(int i=0; i<x.length; i++) {
            sum += x[i]*y[i];
        }
        
        return sum;
    }
    
    public void updateGamma(double gamma) {
        this.gamma = gamma;
    }
}
