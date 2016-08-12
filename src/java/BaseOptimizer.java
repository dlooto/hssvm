
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import jun.hssvm.*;
import jun.hssvm.kernel.KernelType;
import jun.hssvm.strategy.ParamStrategy;
import jun.hssvm.strategy.StrategyType;
import jun.util.File;
import jun.util.Time;
import jun.util.XMLParser;
import static jun.util.Util.*;


/**
 * Base class of parameter optimization
 * @author jun
 */
abstract class BaseOptimizer {
    
	protected static XMLParser xp;
	
	//read all arguments from config file
	protected static final String CONFIG_OPT = "conf";
	protected static final String CONFIG_FILE = "hssvm-config.xml";
	
	protected Searcher searcher;
	
	//protected String searchFile = "";
	protected String trainFile = "";
	protected String testFile  = "";
	
	// the required lowest accuracy
	protected double requiredAccuracy = SVMConst.DEFAULT_LOWEST_ACCURACY;
    
	protected int fold 			= SVMConst.DEFAULT_FOLD;
    protected Scope cArea 	  	= SVMConst.DEFAULT_C_AREA;
    protected Scope gammaArea 	= SVMConst.DEFAULT_GAMMA_AREA;
    protected Scope scaleArea   = SVMConst.DEFAULT_SCALE_AREA;
    protected final Param param = new Param();
    
    protected boolean shouldWriteResult = false;
    protected boolean ifClassifyScale = false;
    protected boolean ifSearchScale = false;
    
    protected ValidateWay validateWay 	= ValidateWay.K_FOLD_CROSS;
    
    /* this argument determines how to choose optimal gamma-C pair */
    protected StrategyType strategyType = SVMConst.DEFAULT_BEST_STRATEGY;
    protected ParamStrategy strategy 	= ParamStrategy.getInstance(strategyType);
    
    
    protected BaseOptimizer() { 
    	//outln(System.getenv("HSSVM_HOME"));
    	xp = XMLParser.getInstance(System.getenv("HSSVM_HOME") + "/" + CONFIG_FILE, 
    			"hssvm-config");
    }
    
    protected void run(String[] args) {
    	long startTime = Time.get();
    	
        if(hasError(parseArgs(args))) { return;};
        if(hasError(checkArgs())) { return;};
        
        execute();
        
        outln("Total time for all: " + Time.show(Time.get()-startTime));
    }

	private boolean hasError(String errMsg) {
		if(errMsg != null) {
    		errln(errMsg);
    		return true;
    	}
		return false;
	}
    
    private void execute() {
		search();
        executeExtra();
    }

	private void search() {
		Time.start();
		
		searcher = Searcher.getInstance(validateWay); 
		searcher.setParams(getData(), param, fold, cArea, gammaArea, strategy);
        searcher.doSearch();
        
        writeSearchResult();
        printSearchResult();
        
    	outln("Searching time: " + Time.show());
	}
	
	private ArrayList<Type> getData() {
		Problem prob = SVM.readProblem(trainFile, null);
        if(prob == null) {
        	exit(1);
        }
        
        if(ifSearchScale) {
        	prob.scaleData(scaleArea);
        }
        
        return SVM.groupSamples(prob);
	}
	
	private void writeSearchResult() {
		if(shouldWriteResult) {
			// requiredAccuracy=0, write all results to file
			Set<Outcome> results = searcher.getOutcomesBeyond(0);
					
	    	if(results.size() > SVMConst.LIMITED_FILE_SIZE){
	            errln("The result of parameters optimization is too big: " + 
	                    results.size() + "Results not written");
	            return;
	    	}
	    	
	    	writeOutcomesToFile(results, trainFile + SVMConst.SEARCH_FILE_SUF);
        }
	}

	private void writeOutcomesToFile(Set<Outcome> outcomes, String outfile) {
		DataOutputStream output = null;
        try {
        	outln("Writing searching result to file...");
            output = File.createDataOutputStream(outfile);
            
            for(Outcome o : outcomes) {
            	output.writeBytes(o.forFile() + "\n");
            }
            
            output.close();
        } catch (IOException ioe) {
            errln("Write the result of argument searching exception!");
            return;
        }
	}
	
	private void printSearchResult() {
		outln("The real broken number: " + searcher.getRealFold());
		outln("Best results in searching: ");
		for(Outcome o: searcher.getTopOutcomes()) {
			outln("  " + o);
		}
		outln("\nSearching completed");
	}
	
	//get all arguments from config file
	private void parseConfigFile(String configFile) {
		trainFile = xp.getValue("train-file");
		testFile = xp.getValue("test-file");
		param.kernelType = KernelType.getEnum(toInt(xp.getValue("kernel")));
		param.cacheFactor = toDouble(xp.getValue("cache-factor"));
		param.eps = toDouble(xp.getValue("eps"));
		validateWay = ValidateWay.getEnum(toInt(xp.getValue("cross-validate-way")));
		fold = toInt(xp.getValue("fold"));
		
		ifSearchScale = toBool(xp.getValue("if-search-scale"));
		ifClassifyScale = toBool(xp.getValue("if-classify-scale"));
		scaleArea = new Scope(toDouble(xp.getValue("scale-lower")),
							  toDouble(xp.getValue("scale-upper")));
		
		cArea = new Scope(toDouble(xp.getValue("C-lower")),
						  toDouble(xp.getValue("C-upper")),
						  toDouble(xp.getValue("C-step")));
		
		gammaArea = new Scope(toDouble(xp.getValue("gamma-lower")),
							  toDouble(xp.getValue("gamma-upper")),
						  	  toDouble(xp.getValue("gamma-step")));
		
		shouldWriteResult = toBool(xp.getValue("if-write-search-result"));
		
		strategyType = StrategyType.getEnum(toInt(xp.getValue("best-param-strategy")));
		strategy = ParamStrategy.getInstance(strategyType);
		
		requiredAccuracy = toDouble(xp.getValue("required-accuracy"));
		
	}
	
	//TODO, forget scaling option obtain...
	private String parseArgs(String[] args) {
		if(args.length == 0 || args[0].equals("?")){
        	return getHelp();
        }
		
		if(args.length == 1 && args[0].equals(CONFIG_OPT)) {
			parseConfigFile(CONFIG_FILE); 
			return null;
		} 
		
		//else parse command-line arguments
		for(int i = 0; i < args.length; i++) {
			if(args[i].equals("")) {
				i++;
				continue;
			}
			
            if(args[i].charAt(0) == '-') {
				if(args[i].length() != 2) {
            		return("Error option: " + args[i]);
            	}

                switch (args[i].charAt(1)) {
                    case 'm': {// cache factor
                        if(i+1 < args.length) {
                            param.cacheFactor = toDouble(args[i+1]);
                            i++;
                        } else {
                        	return ("Error option: " + args[i]);
                        }
                        break;
                    }
					case 'f': {// broken number
                        if(i+1 < args.length){
                            fold = toInt(args[i+1]);
                            i++;
                        } else {
                        	return("Error option: " + args[i]);
                        }
                        break;
                    }
                    case 'v': {//the way of cross validation 
                        if(i+1 < args.length){
                        	validateWay = ValidateWay.getEnum(toInt(args[i+1]));
                            i++;
                        } else {
                        	return("Error option: " + args[i]);
                        }
                        break;
                    }
                    case 'k': {// kernel type
                        if(i+1 < args.length){
                        	param.kernelType = KernelType.getEnum(toInt(args[i+1]));
                            i++;
                        } else {
                        	return("Error option: " + args[i]);
                        }
                        break;
                    }
                    case 'C': {//C bound
                        if(i+3 < args.length){
                            cArea = new Scope(toDouble(args[i+1]), 
                            				  toDouble(args[i+2]),
                            				  toDouble(args[i+3]));
                            i += 3;
                        } else {
                        	return("Error option: " + args[i]);
                        }
                        break;
                    }
                    case 'g': { //gamma bound
                        if(i+3 < args.length){
                        	gammaArea = new Scope(toDouble(args[i+1]), 
                  				  				  toDouble(args[i+2]),
                  				  				  toDouble(args[i+3]));
                        	i += 3;
                        } else {
                        	return("Error option: " + args[i]);
                        }
                        break;
                    }
                    case 'e': {// epsilon
                        if(i+1 < args.length) {
                            param.eps = toDouble(args[i+1]);
                            i++;
                        } else {
                        	return("Error option: " + args[i]);
                        }
                        break;
                    }
                    case 'w': {// if write searching result
                    	shouldWriteResult = true;
                        break;
                    }
                    case 't': {//optimal gamma-C pair strategy
                    	if(i+1 < args.length) {
                    		strategyType = StrategyType.getEnum(toInt(args[i+1]));
                    		strategy = ParamStrategy.getInstance(strategyType);
                            i++;
                        } else {
                        	return("Error option: " + args[i]);
                        }
                        break;
                    }
                    case 'a': { 
	                	if(i+1 < args.length) {
	                        requiredAccuracy = toDouble(args[i+1]);
	                        i++;
	                    } else {
	                    	return("Error option: " + args[i]);
	                    }
	                    break;
	                }
                    default:{
                    	return("Unknown option: " + args[i]);
                    }
                }
            } else {// if file name
            	if(trainFile.equals("")) {
            		trainFile = args[i];
                } else if(testFile.equals("")) {
                	testFile = args[i];
                } else {
            		return("Error argument" + args[i]);
            	}
            }
		}
		
		return null;
	}
    
	private String checkArgs() {
		if(trainFile.equals("")) {
        	return("Require training file");
        }
		if(fold < SVMConst.DEFAULT_MIN_FOLD){ 
            return("Broken number too small: " + fold);
        }
        if(validateWay == ValidateWay.UNKNOWN){
        	return("Unsupported cross-validation way");
        } 
        if(strategyType == StrategyType.UNKNOWN) {
        	return ("" + strategyType);
        }
        
        param.check();
        
        String errMsg = checkExtraArgs();
        if(errMsg != null) {
        	return errMsg;
        }
        
        return confirmArgs();
	}

	private String confirmArgs() {
		out("Use following parameters: \n" + showArgs());
        return confirm();
	}
	
	protected String showArgs() {
		return ("   File used in Searching: " + trainFile + "\n" +
                "   Cache factor: " + param.cacheFactor + "\n" +
                "   Kernel function type: " + param.kernelType + "\n" +
                "   Epsilon: " + param.eps + "\n" +
                "   The way of cross validation: " + validateWay + "\n" +
                "   Broken number: " + fold + "\n" +
                "   Optimal gamma-C pair strategy: " + strategyType + "\n" +
                "   C :      " + cArea + "\n" +
                "   Gamma:   " + gammaArea + "\n" +
                "   Write searching result to file: " + shouldWriteResult +"\n"+
                "   Normalize data when searching: " + forScaleShow(ifSearchScale) + "\n" +
                	confirmExtraArgs() + "\n" 
				);
	}
	
	protected String forScaleShow(boolean ifScale) {
		return ifScale + (ifScale?",  scale-scope:" + scaleArea : "");
	}
	
    //
    //// abstract methods for subclass overriding
    //
	abstract protected String getHelp();
	
	abstract protected String checkExtraArgs();
	
	abstract protected void executeExtra();
	
	abstract protected String confirmExtraArgs();
}
