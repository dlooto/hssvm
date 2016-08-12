package jun.hssvm;

import jun.hssvm.strategy.StrategyType;

/**
 * all constants about this application are stored into this class
 * @author jun
 *
 */
public interface SVMConst {

    /* svm_type, now only supports classification */
    public static final int C_SVC = 0;

    /* sample data file format */   
    public static final int INDEX_FORMAT = 1;
    public static final int NO_INDEX_FORMAT = 0;  
    
    /* Default suffix of training model file */
    public static final String MODEL_FILE_SUF = ".model";
    
    /* the default suffix of prediction result output */
    public static final String  PREDICT_OUT_SUF = ".out";
    
    /* The mark of error classification */
    public static final String ERR_MARK = "x";
    
    /* default parameters of normalizing */
    public static final String CLASSIFY_SCALE_SUF = ".s";
    public static final String SEARCH_SCALE_SUF = ".ss";
    
    public static final Scope DEFAULT_SCALE_AREA = new Scope(-1.0, 1.0);
    
    /* Default values for parameter optimization */		
    public static final Scope DEFAULT_C_AREA = new Scope(1, 5, 1); 
    public static final Scope DEFAULT_GAMMA_AREA = new Scope(1, 5, 1);
    
    /* the default folds in k-fold cross-validation */
    public static final int DEFAULT_FOLD = 5;
    public static final int DEFAULT_MIN_FOLD = 3;
    
    /* default suffix of result file in parameter optimization */
    public static final String  SEARCH_FILE_SUF = ".po";
    
    /* result report file suffix when running all from build.xml */
    public static final String RUN_ALL_REPORT_SUF = ".run-all.report";
    
    /* 
     * the maximal permissible size of result file in parameter optimization, 
     * this variable indicates the number of data rows in result file
     */
    public static final int LIMITED_FILE_SIZE = 9000000;
    
    /* the delimit symbol of data format in original data file  */
    public static final String NOINDEX_DELIMIT_SYMBOL = " \t\n\r\f";
    public static final String INDEX_DELIMIT_SYMBOL   = " \t\n\r\f:";
    
    /* Default file suffix generated in processing of data format conversion */
    public static final String NOINDEX_DATA_SUF = ".n";
    public static final String INDEX_DATA_SUF   = ".i";
    
    
    /* set aside memory for other data storing in our application */
    public static final int MARGIN = 15; 
    
    /* 
     * the permitted max size of UTM matrix , measured in MBytes
     * when 24M, the UTM can store kernel matrix of 2500 samples approximately
     */
    public static final int UTM_PERMITTED_SIZE = 24; 

    /* 
     * the default number of cache pre-fetch rows of kernel matrix
     * when initializing cache
     */
    public static final double PREFETCH_FACTOR = 0.1;
    
    /* 
     * the amount of stored rows of kernel matrix in cache,which
     * equals to (all-rows * factor) 
     */
    public static final double DEFAULT_CACHE_FACTOR = 0.05;
    
    /* the default lowest accuracy required */
    public static final double DEFAULT_LOWEST_ACCURACY = 0.85;
    
    /* default best gamma-C pair strategy */
    public static final StrategyType DEFAULT_BEST_STRATEGY = StrategyType.MIN_C;
    
}
