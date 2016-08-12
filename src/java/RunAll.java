
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jun.hssvm.HSModel;
import jun.hssvm.Outcome;
import jun.hssvm.Problem;
import jun.hssvm.SVM;
import jun.hssvm.SVMConst;
import jun.util.File;
import jun.util.Time;
import static jun.util.Util.*;

/**
 * 
 * @author jun
 *
 */
public class RunAll extends BaseOptimizer {

    // assistant list
    List<String> 	trainTimes 	  	= new ArrayList<String>();
    List<String> 	predictTimes   	= new ArrayList<String>();
    List<Outcome> 	classifyResults = new ArrayList<Outcome>();

    
	public RunAll() {
		super();
	}
	
	public static void main(String[] args) {
		new RunAll().run(args);
	}
	
	protected String getHelp() {
		return Usage.RUNALL;
	}
	
	@Override
	protected String checkExtraArgs() {
		if(testFile.equals("")) {
			return ( "Require testing file");
		}
		return null;
	}

	@Override
	protected String confirmExtraArgs() {
		return( "   File used in training:   " + trainFile + "\n" +
		    	"   File used in prediction: " + testFile + "\n" +
		    	"   Normalize data when classifing: " + forScaleShow(ifClassifyScale) + "\n" +
				"   Required accuracy: " + requiredAccuracy
			  );
	}

	private Problem[] getClassifyData() {
	    Problem trainProb = SVM.readProblem(trainFile, null);
	    Problem testProb = SVM.readProblem(testFile, null);
	    if(trainProb == null || testProb == null){
	        exit(1);
	    }
	    
	    if(ifClassifyScale) {
	    	Problem prob = trainProb.mergeWith(testProb);
	        prob.scaleData(scaleArea);
	        prob.decompose(trainProb.getLen(), trainProb, testProb);  
	    }
	    
	    return new Problem[] {trainProb, testProb};
	}
	
	@Override
	protected void executeExtra() {
		Problem[] probs = getClassifyData();
        
		String prompt = "";
		Set<Outcome> requiredOutcomes = searcher.getOutcomesBeyond(requiredAccuracy);
		
		/* 
		 * if the number of searching results satisfying the required accuracy  
		 * is equals 0, then we use all results with highest accuracy 
		 */
        if(requiredOutcomes.size() == 0) {
        	prompt = getEmptyResultsPrompt();
            requiredOutcomes = searcher.getTopOutcomes();
        }
        
        // train and predict for every required gamma-C pair
        for(Outcome o : requiredOutcomes) {
    		classify(probs, o);
    	}
        
        writeClassifyResults();
        writeFinalResults(prompt);
        writeAllReport();
	}
	
	private void writeFinalResults(String prompt) {
		Set<Outcome> finalResults = strategy.getTopOutcomes(listToSet(classifyResults));
		
		//write final results to output files
		int i = 0;
		for(Outcome o: finalResults) {
			o.writeToFile(testFile + SVMConst.PREDICT_OUT_SUF + i);
			i++;
        }
		
		//print final results
		outln(prompt);
		outln("Classification results: ");
        for(Outcome o: finalResults) {
        	outln("  " + o);
        }
	}
	
	private String getEmptyResultsPrompt() {
		return ("Cause no any results in param-searching meets the accuracy: >=" + 
				requiredAccuracy + ",\n the best results below have been used to " +
				"classify: \n" + searcher.getTopOutcomes());
	}

	private void writeAllReport() {
		DataOutputStream output = null;
        try {
            output = File.createDataOutputStream(getPrefix(trainFile) + ".report");
            
            // write arguments
            output.writeBytes("\nThe following parameters are used : \n" + showArgs() + "\n");
            
			// write searching results
			output.writeBytes("\nBest results in searching: \n");
			for(Outcome o: searcher.getTopOutcomes()) {
				output.writeBytes("  " + o + "\n");
			}
			
			// write classification results
			output.writeBytes("\nBest results in classification: \n");
			Set<Outcome> topSet = strategy.getTopOutcomes(listToSet(classifyResults));
			for(Outcome o: topSet) {
				output.writeBytes("  " + o + "\n");
			}
			
            output.close();
        } catch (IOException ioe) {
            errln("Write run-all report exception!");
            return;
        }
	}

    private void writeClassifyResults() {
        DataOutputStream output = null;
        try {
            output = File.createDataOutputStream(getPrefix(testFile) + ".runall-classify");
            
            for(int i=0; i<classifyResults.size(); i++) {
            	output.writeBytes(classifyResults.get(i) + "\t" + trainTimes.get(i) + "\t" +
            			predictTimes.get(i) + "\n");
            }
            
            output.close();
        } catch (IOException ioe) {
            errln("Write the classifying result exception!");
            return;
        }
    }

	private String getPrefix(String fileName) {
		return fileName;
	}
	
	private void classify(Problem[] probs, Outcome newParam) {
		// update Param object
		param.C = newParam.getC();
		param.gamma = newParam.getGamma();
		SVM.init(param);
		
		//train
		Time.start();
		HSModel model = SVM.train(probs[0]);
		trainTimes.add(Time.show());
		
		//predict
		Time.start();
		classifyResults.add(SVM.predict(probs[1], model));
		predictTimes.add(Time.show());
	}
	
	private Set<Outcome> listToSet(List<Outcome> list) {
		Set<Outcome> resultSet = new HashSet<Outcome>();
		for(Outcome o: list) {
			resultSet.add(o);
		}
		
		return resultSet;
	}
}
