package jun.hssvm;


/**
 * 
 * @author jun
 *
 */
public class Scaler {

    private Scope scaleArea;
    
    
    public Scaler(Scope scaleArea){
    	this.scaleArea = scaleArea; 
    }
    
    public Problem doScale(String trainFile){
        Problem p = SVM.readProblem(trainFile, null);
        if(p == null){
            return null;
        }
        p.scaleData(scaleArea);
        return p;
    }
    
    public Problem[] doScale(String trainFile, String testFile){
        Problem trainP = SVM.readProblem(trainFile, null);
        Problem testP = SVM.readProblem(testFile, null);
        if(trainP == null || testP == null){
            return null;
        }
        
        Problem prob = trainP.mergeWith(testP);
        prob.scaleData(scaleArea);
        prob.decompose(trainP.getLen(), trainP, testP);  
        
        return new Problem[]{trainP, testP};
    }
    
}
