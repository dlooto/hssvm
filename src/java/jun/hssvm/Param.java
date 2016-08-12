package jun.hssvm;

import jun.hssvm.kernel.KernelType;
import jun.util.Util;

/**
 * store training arguments
 * @author jun
 */
public class Param implements Cloneable,java.io.Serializable {
	
    private static final long serialVersionUID = 10000003;
    
    /* this factor determine storing how many rows of kernel matrix in cache */
    public double cacheFactor = SVMConst.DEFAULT_CACHE_FACTOR;
   
	public KernelType kernelType = KernelType.RBF;
	public double C = 1.0;
	public double gamma = 1 ;	           
	public double eps = 1e-3;
    
	/* the format of sample data file */
    public int dataFormat = SVMConst.NO_INDEX_FORMAT;
    
    
    public void check() {
    	 if(cacheFactor <= 0) {
             Util.exit("cache factor <= 0");
         }
         if(kernelType == KernelType.UNKNOWN) {
        	 Util.exit("Unsupported kernel type");
         }
         if(C <= 0){
             Util.exit("The penalty factor C <= 0 !"); 
         }
         if(gamma < 0) {
        	 Util.exit("Gamma <= 0");
         }
         if(eps <= 0) {
        	 Util.exit("eps <= 0");
         }
    }
    
    public String toString() {
    	return ("	 kernel type: " + kernelType + "\n" +	
    			"	 cache factor: " + cacheFactor + "\n" +
    			"	 C: " + C + "\n" +
    			"	 gamma: " + gamma + "\n" +
    			"	 eps: " + eps 
    			);
    			
    }
}
