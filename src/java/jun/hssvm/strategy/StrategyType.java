package jun.hssvm.strategy;


public enum StrategyType {
	MIN_C, MIN_GAMMA, MIN_GAMMA_C, MIN_C_MAX_GAMMA, MIN_GAMMA_MAX_C, MAX_C_GAMMA, 
	UNKNOWN;
	
	public String toString() {
		switch(this) {
			case MIN_C: {
				return "Minimal C";
			}
			case MIN_GAMMA: {
				return "Minimal gamma";
			}
			case MIN_GAMMA_C: {
				return "Both C and gamma are minimal";
			}
			case MIN_C_MAX_GAMMA: {
				return "Minimal C and maximal gamma";
			}
			case MIN_GAMMA_MAX_C: {
				return "Maximal C and minimal gamma";
			}
			case MAX_C_GAMMA : {
				return "Both C and gamma are maximal";
			}
			default: return "Unknown strategy type";
			}
	}
	
	public static StrategyType getEnum(int type) {
		switch(type) {
	    	case 0: {
	    		return MIN_C;
	    	}
	    	case 1: {
	    		return MIN_GAMMA;
	    	}
	    	case 2: {
	    		return MIN_GAMMA_C;
	    	}
	    	case 3: {
	    		return MIN_C_MAX_GAMMA;
	    	}
	    	case 4: {
	    		return MIN_GAMMA_MAX_C;
	    	}
	    	case 5: {
	    		return MAX_C_GAMMA;
	    	}
	    	default: {
	    		return UNKNOWN;
	    	}
		}
	}
}
