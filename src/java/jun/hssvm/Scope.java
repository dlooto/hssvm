package jun.hssvm;

import jun.util.Util;

public class Scope {
	
	private double lower;   	// lower bound
	private double upper;		// upper bound
	private double step;		// step length
	
	private boolean hasStep;
	
	
	// in this case, the variable step is no used 
	public Scope(double lower, double upper) {
		hasStep = false;
		this.lower = lower;
		this.upper = upper;
		check();
	}
	
	public Scope(double lower, double upper, double step) {
		hasStep = true;
		this.lower = lower;
		this.upper = upper;
		this.step = step;
		check();
	}
	
	public double getLower() {
		return lower;
	}
	
	public double getUpper() {
		return upper;
	}
	
	public double getStep() {
		return step;
	}
	
	private void check() {
		if(lower > upper) {
           Util.exit("Exception: lower bound > upper bound" + this);
        }
        if(hasStep) {
        	if(step <= 0) {
        		Util.exit("Exception: step length <= 0, greater than 0 is required" +
        				step);
        	}
            
        	if(step > (upper-lower)) {
                Util.exit("Exception: step length is too big " + this);
            }   
        }
	}
	
	public String toString() {
		return (hasStep)?
				"[" + lower + " " + upper + "]  step=" + step :
				"[" + lower + " " + upper + "]";
	}
}
