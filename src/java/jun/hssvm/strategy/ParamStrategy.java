package jun.hssvm.strategy;

import java.util.Set;
import java.util.HashSet;

import jun.hssvm.Outcome;

abstract public class ParamStrategy {
	
	private double accuracyEpsilon = 1e-4;
	private double gammaCEpsilon = 1e-2;
	
	protected ParamStrategy() {}
	
	public Set<Outcome> getTopOutcomes(Set<Outcome> outcomes) {
		return getOutcomesByStrategy(getOutcomesByTopAccuracy(outcomes));
	}
	
	abstract protected Set<Outcome> getOutcomesByStrategy(Set<Outcome> outcomes);
	
	public static ParamStrategy getInstance(StrategyType type) {
		switch(type) {
			case MIN_C: {
				return new MinC();
			}
			case MIN_GAMMA: {
				return new MinGamma();
			}
			case MIN_GAMMA_C: {
				return new MinGammaC();
			}
			case MIN_C_MAX_GAMMA: {
				throw new RuntimeException("Unsupported temporaryly");
			}
			case MIN_GAMMA_MAX_C: {
				throw new RuntimeException("Unsupported temporaryly");
			}
			case MAX_C_GAMMA : {
				throw new RuntimeException("Unsupported temporaryly");
			}
			default: return null;
		}
	}
	
	// get outcome list by minimal C  
	protected Set<Outcome> getOutcomesByMinC(Set<Outcome> outcomes) {
		Set<Outcome> results = new HashSet<Outcome>();
		
		double minC = Double.MAX_VALUE;
		for(Outcome o : outcomes) {
			if(o.getC() < minC) {
				minC = o.getC();
			}
		}
		
		for(Outcome o : outcomes) {
			if(Math.abs(o.getC()-minC) <= gammaCEpsilon) {
				results.add(o);
			}
		}
		
		return results;
	}
	
	// get outcome list by minimal gamma 
	protected Set<Outcome> getOutcomesByMinGamma(Set<Outcome> outcomes) {
		Set<Outcome> results = new HashSet<Outcome>();
		
		double minGamma = Double.MAX_VALUE;
		for(Outcome o : outcomes) {
			if(o.getGamma() < minGamma) {
				minGamma = o.getGamma();
			}
		}
		
		for(Outcome o : outcomes) {
			if(Math.abs(o.getGamma()-minGamma) <= gammaCEpsilon) {
				results.add(o);
			}
		}
		
		return results;
	}
	
	// get outcome list by highest accuracy
	private Set<Outcome> getOutcomesByTopAccuracy(Set<Outcome> outcomes) {
		Set<Outcome> results = new HashSet<Outcome>();
		
		// get highest accuracy
		double peakAccuracy = 0;
	    for(Outcome o : outcomes) {
	    	if(o.getAccuracy() > peakAccuracy) {
	        	peakAccuracy = o.getAccuracy();
	        }
	    }
	    
	    // get outcomes having highest accuracy
	    for(Outcome o : outcomes) {
	    	if(Math.abs(o.getAccuracy()-peakAccuracy) <= accuracyEpsilon) {
	    		results.add(o);
	    	}
	    }
	    
	    return results;
	}
	
}
