package jun.hssvm.strategy;

import java.util.Set;

import jun.hssvm.Outcome;

/**
 * choose the outcome holding minimal C and gamma from accuracy-highest 
 * outcome list 
 * @author jun
 *
 */
public class MinGammaC extends ParamStrategy {

	public MinGammaC() {
		super();
	}
	
	@Override
	protected Set<Outcome> getOutcomesByStrategy(Set<Outcome> outcomes) {
		return getOutcomesByMinC(getOutcomesByMinGamma(outcomes));
	}

}
