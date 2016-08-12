

import java.io.*;

import jun.hssvm.HSModel;
import jun.hssvm.Outcome;
import jun.hssvm.Problem;
import jun.hssvm.SVM;
import jun.hssvm.SVMConst;
import jun.util.Time;
import jun.util.Util;
import jun.util.File;


/**
 * 
 * @author jun
 *
 */
public class Predictor {

	private double requiredAccuracy = SVMConst.DEFAULT_LOWEST_ACCURACY;
	
	
    public static void main(String[] args) throws IOException {
        new Predictor().run(args);
    }
    
    private void run(String[] args){
        if(args.length < 2 || args.length > 3) {
            Util.outln(help());
            return;
        }
        
        Problem prob = SVM.readProblem(args[0], null);
        if(prob == null){
            return;
        }
        
        HSModel model = SVM.loadModel(args[1]);
        if(model == null) {
            return;
        }
        
        long startTime = Time.get();
        
        SVM.init(model.getParam());
        Outcome re = SVM.predict(prob, model);
        
        Util.outln(re);
        if(re.getAccuracy() < requiredAccuracy){
            Util.outln("The accuracy is less than " + requiredAccuracy*100 +
                    "%, suggest optimizing parameters");
        }
        
        // output to file
        if(args.length == 2){
        	re.writeToFile(args[0] + SVMConst.PREDICT_OUT_SUF);
        } else {
            re.writeToFile(args[2]);
        }
        
        Util.outln("Prediction completed");
        Util.outln("Total time: " + Time.show(Time.get()-startTime));
    }
    
    private static String help() {
        return Usage.PREDICTION;
    }
}
