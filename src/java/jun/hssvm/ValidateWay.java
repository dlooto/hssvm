package jun.hssvm;

/**
 * 
 * @author jun
 *
 */
public enum ValidateWay {
	RAND, K_FOLD_CROSS, K_FOLD_CROSS_RAND, UNKNOWN;
	
	public String toString() {
		switch(this) {
			case RAND: {
				return "Random";
			}
			case K_FOLD_CROSS: {
				return "k-fold cross";
			}
			case K_FOLD_CROSS_RAND: {
				return "k-fold cross and random";
			}
			default: return "Unkown validation way";
		}
	}
	
	public static ValidateWay getEnum(int validateWay) {
		switch(validateWay) {
	    	case 0: {
	    		return RAND;
	    	}
	    	case 1: {
	    		return K_FOLD_CROSS;
	    	}
	    	case 2: {
	    		return K_FOLD_CROSS_RAND;
	    	}
	    	default: {
	    		return UNKNOWN;
	    	}
    	}
	}
}
