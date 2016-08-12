package jun.hssvm.kernel;

/**
 * @author jun
 *
 */
public class RBFKernel extends Kernel implements java.io.Serializable {
    private static final long serialVersionUID = 10000011;
    

    public RBFKernel(double gamma){
        super(gamma);
    }
    
    public double K(double[] x, double[] y) {
            return Math.exp(-gamma*(dot(x,x) - 2*dot(x,y) + dot(y,y)));
    }
}
