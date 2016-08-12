package jun.hssvm.kernel;

public enum KernelType {
	POLY, RBF, UNKNOWN;
	
	public String toString() {
		switch(this) {
			case POLY: {
				return "Poly";
			}
			case RBF: {
				return "RBF";
			}
			default: return "Unknown kernel type";
		}
	}
	
	public static KernelType getEnum(int kernelType) {
		switch(kernelType) {
	    	case 1: {
	    		return POLY;
	    	}
	    	case 2: {
	    		return RBF;
	    	}
	    	default: {
	    		return UNKNOWN;
	    	}
		}
	}
}
