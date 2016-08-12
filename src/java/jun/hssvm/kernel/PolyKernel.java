package jun.hssvm.kernel;

/**
 * @author jun
 */
public class PolyKernel extends Kernel implements java.io.Serializable {

    private static final long serialVersionUID = 10000012;
    
    private double coef0;
    private int degree;
    
    public PolyKernel(double gamma, double coef0, int degree){
        super(gamma);
        this.coef0 = coef0;
        this.degree = degree;
    }
    
    public double K(double[] x, double[] y) {
        return Math.pow(gamma*dot(x, y) + coef0, degree);
    }
    
    public void updateParam(double gamma, double coef0, int degree){
        updateGamma(gamma);
        this.coef0 = coef0;
        this.degree = degree;
    }
}
