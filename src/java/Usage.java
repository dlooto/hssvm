
import jun.hssvm.SVMConst;
import jun.hssvm.ValidateWay;
import jun.hssvm.strategy.StrategyType;

/**
 * store all usage information 
 * @author jun
 *
 */
class Usage {

	private static final String CACHE_USAGE = 
    	"   -m factor: which determines storing how many rows of kernel matrix in cache.\n" +
        "           It's available only if processing large amount samples(default " + 
        SVMConst.DEFAULT_CACHE_FACTOR + ")";
    
    private static final String KERNEL_USAGE =
    	"   -k kernel_type : set type of kernel function (default RBF)\n" +
        "        1 -- polynomial(Unsupported temporaryly) \n" +
        "        2 -- RBF(radial basis function) ";
    
    private static final String EPS_USAGE = 
    	"   -e epsilon : set tolerance of termination criterion (default 0.001)";
	
    private static final String VALIDATE_WAY = 
    	"   -v way: the way of cross-validation(default " + ValidateWay.K_FOLD_CROSS + ")\n" +
        "        0 -- " + ValidateWay.RAND + "\n" +
        "        1 -- " + ValidateWay.K_FOLD_CROSS + "\n" +
        "        2 -- " + ValidateWay.K_FOLD_CROSS_RAND ;
    
    private static final String STRATEGY_TYPE = 
    	"   -t stype: determines how to choose optimal gamma-C pair (default " + 
    				SVMConst.DEFAULT_BEST_STRATEGY + ")\n" +
        "        0 -- " + StrategyType.MIN_C + "\n" +
        "        1 -- " + StrategyType.MIN_GAMMA + "\n" +
        "        2 -- " + StrategyType.MIN_GAMMA_C ;
    
    
    /*
     * =========================================================================
     */
    public static final String TRAINING = 
            "Usage: Trainer [options] training_file [model_file]\n\n" +
            "Options:\n" +
            	CACHE_USAGE + "\n" +
            	KERNEL_USAGE + "\n" +
            	EPS_USAGE + "\n" +
            	"   -C cost : set the penalty factor C (default 1)\n" +
            	"   -g gamma : set gamma in kernel function (default 1)\n" ;
    
    public static final String PREDICTION = 
            "usage: Predictor test_file model_file [output_file]";
    
    public static final String DATA_TRANSFER = 
            "Usage: DataTransfer [option] src_file  [dest_file] \n" +
            "   -i : convert non-index format data to index format data(by default,\n" +
            "        convert the index format to non-index format)";
    
    public static final String NORMALIZATION = 
        "Usage: Normalizer  [option] train_file  test_file \n" +
        "  option: -s lower upper(default" + SVMConst.DEFAULT_SCALE_AREA + ")\n";
    
    public static final String PARAM_OPTIMIZATION = 
            "Usage: ParamOptimizer  [options] train_file \n" +
            "Options: \n" +
            	CACHE_USAGE + "\n" +
        		KERNEL_USAGE + "\n" +
        		EPS_USAGE + "\n" +
        		VALIDATE_WAY + "\n" +
        		STRATEGY_TYPE + "\n" +
            "   -f  fold: the fold number in cross-validation(default " + SVMConst.DEFAULT_FOLD + ")\n" +
            "   -C  lower upper step :  set the scope of C(default " + SVMConst.DEFAULT_C_AREA + ")\n" +
            "   -g  lower upper step :  set the scope of gamma(default " + SVMConst.DEFAULT_GAMMA_AREA + ")\n" +
            "   -w  : write parameters searching result to file(no this option, not write)\n"; 
    
    public static final String RUNALL = "Usage: runall conf, " +
    		"please review readme file to get more details";

}
