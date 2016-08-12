package jun.hssvm.strategy;

import java.util.Set;

import jun.hssvm.Outcome;

/**
 * choose the outcome holding minimal C from accuracy-highest outcome list 
 * @author jun
 *
 */
public class MinC extends ParamStrategy {

	public MinC() {
		super();
	}
	
	@Override
	protected Set<Outcome> getOutcomesByStrategy(Set<Outcome> outcomes) {
		return getOutcomesByMinC(outcomes);
	}

}
