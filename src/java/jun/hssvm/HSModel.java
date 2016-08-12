package jun.hssvm;

/**
 * This HSModel object obtained from training process will be save in file  
 * @author jun
 */

public class HSModel implements java.io.Serializable {
    private static final long serialVersionUID = 10000002;
    
    private Param param;    
    private Sphere[] hsa;  //Hyper-sphere array
    
    	
    public HSModel() {}
    
    public HSModel(Param param, Sphere[] hsa){
        this.param = param;
        this.hsa = hsa;
    }

    public Sphere[] getSpheres() {
        return hsa;
    }
    
    public Param getParam() {
        return param;
    }

}
