package jun.hssvm.strategy;

import java.util.Set;

import jun.hssvm.Outcome;

public class MinGamma extends ParamStrategy {

	public MinGamma() {
		super();
	}
	
	@Override
	protected Set<Outcome> getOutcomesByStrategy(Set<Outcome> outcomes) {
		return getOutcomesByMinGamma(outcomes);
	}
}
