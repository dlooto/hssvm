

import java.io.IOException;

import jun.hssvm.Problem;
import jun.hssvm.SVMConst;
import jun.hssvm.Scaler;
import jun.hssvm.Scope;
import jun.util.Util;

/**
 * Normalize the sample data
 * @author jun
 */
public class Normalizer {
    
    private String trainFile = "";
    private String testFile = "";
    private Scope sArea = SVMConst.DEFAULT_SCALE_AREA;

    public static void main(String[] args) throws Exception{
        new Normalizer().run(args);
    }

    private void run(String[] args) throws IOException {
        String errMsg = parseCmdLine(args);
        if(errMsg != null){
            Util.errln(errMsg);
            return;
        }
        
        Scaler scaler = new Scaler(sArea);
        
        //Only normalize training file
        if(testFile.equals("")){
            scaler.doScale(trainFile).writeToFile(trainFile + SVMConst.SEARCH_SCALE_SUF, null);
            Util.outln("Normalizing data completed.");
            return;
        }
        
        //normalize training and testing files
        Problem[] probs = scaler.doScale(trainFile, testFile);
        
        //write to files
        probs[0].writeToFile(trainFile + SVMConst.CLASSIFY_SCALE_SUF, null);
        probs[1].writeToFile(testFile + SVMConst.CLASSIFY_SCALE_SUF, null);
        Util.outln("Normalizing data completed.");
    }
    
    private String parseCmdLine(String[] args){
        if(args.length < 1 || args.length > 5) {
            return Usage.NORMALIZATION;
        } 
        
        for(int i=0; i<args.length; i++) {
			if(args[i].equals("")) {
				continue;
			}
			
            if(args[i].charAt(0) == '-') {// if options
            	if(args[i].length() != 2) {
            		return("Error option: " + args[i]);
            	}
            	
                if(args[i].charAt(1) == 's') {
                	if(i+2 < args.length) {
                		sArea = new Scope(Util.toDouble(args[i+1]), Util.toDouble(args[i+2]));
                        i += 2;
                    } else {
                    	return("Error option: " + args[i]);
                    }
                } else {
                	return("Unknown option: " + args[i]);
                }
            } else {// if file names
            	if(trainFile.equals("")) {
            		trainFile = args[i];
            	} else if(testFile.equals("")) {
                    testFile = args[i];
            	} else {
            		return("Error argument: " + args[i]);
            	}
            }
		}
                
		return null;
        
    }
}
