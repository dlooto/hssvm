package jun.hssvm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;


import jun.hssvm.strategy.ParamStrategy;

/**
 * 
 * @author jun
 *
 */
abstract public class Searcher {

    //input arguments
    protected ArrayList<Type> types;
    protected Param param;
    protected int fold;
    protected Scope cArea;
    protected Scope gammaArea;
    protected ParamStrategy strategy;
    
    //assistant arrays
    protected ArrayList<Type> trainTypes;
    protected ArrayList<Type> predictTypes;
    protected int[] subLens;
    
    // searching results 
    protected Set<Outcome> results;
    protected Set<Outcome> topOutcomes;
    
    
    protected Searcher() {
    	trainTypes 	 = new ArrayList<Type>();
		predictTypes = new ArrayList<Type>();
		results = new HashSet<Outcome>();
    }
    
    public void setParams(ArrayList<Type> types, Param param, int fold, 
    		Scope cArea, Scope gammaArea, ParamStrategy strategy) {
    	this.types = types;
		this.param = param;
		this.fold = fold;
		this.cArea = cArea;
		this.gammaArea = gammaArea;
		this.strategy = strategy;
    }
    
    public static Searcher getInstance(ValidateWay validateWay){
        switch(validateWay){
            case RAND: {
                return new RandSearcher();
            }
            case K_FOLD_CROSS: {
                return new CrossSearcher();
            }
            case K_FOLD_CROSS_RAND: {
                return new CrossRandSearcher();
            }
            default:{
                return new CrossSearcher();
            }
        }
    }
    
    public void doSearch(){
        reviseFolds();
        
        //calculate the length about one-fold samples of every class
        subLens = new int[types.size()];
        for(int i=0; i<subLens.length; i++) {
            subLens[i] = types.get(i).getLen()/fold;
        }
        
        search();
    }

    public Set<Outcome> getTopOutcomes() {
    	if(topOutcomes == null) {
    		topOutcomes = strategy.getTopOutcomes(results);
    	}
        return topOutcomes;
    }
    
    public Set<Outcome> getOutcomes() {
        return Collections.unmodifiableSet(results);
    }
    
    /**
     * get outcomes whose accuracies are greater or equal than specified 
     * lowest accuracy rate  
     * @param accuracy
     * @return Set<Outcome>
     */
    public Set<Outcome> getOutcomesBeyond(double accuracy) {
    	if(accuracy <= 0) {
    		return getOutcomes();
    	}
    	
        Set<Outcome> rs = new HashSet<Outcome>();
        for(Outcome o: results) {
        	if(o.getAccuracy() >= accuracy) {
        		rs.add(o);
        	}
        }
        return rs;
    }
    
    public int getRealFold(){
        return fold;
    }
    
    abstract protected void search();
    
    //according to specified training samples set and prediction samples set ,
    //cover all values of C and gamma to train and predict 
    protected void searchOneTrip(ArrayList<Type> trainTypes, ArrayList<Type> predictTypes) {
        for(double c=cArea.getLower(); c<=cArea.getUpper(); c+=cArea.getStep()) {
            param.C = c;
            for(double g=gammaArea.getLower(); g<=gammaArea.getUpper(); g+=gammaArea.getStep()) {
                param.gamma = g;
                SVM.init(param);
                results.add(SVM.predict(predictTypes, SVM.train(trainTypes)));
            }
        }
    }

    //modify fold, fold=min(fold, min(type_i.size))
    private void reviseFolds() {
        int min = 0;
        for(int i=0; i<types.size(); i++){
            if(types.get(i).getLen() < types.get(min).getLen()){
                min = i;
            }
        }
        
        Type minType = types.get(min);
        if(minType.getLen() < SVMConst.DEFAULT_MIN_FOLD){
            throw new RuntimeException("The samples number is too small: " +
                    minType);
        }
        if(minType.getLen() < fold){
            fold = minType.getLen();
        }
    }
    
    public String toString(){
       return "";
   }
}
