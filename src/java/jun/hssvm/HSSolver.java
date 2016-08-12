package jun.hssvm;

import jun.hssvm.kernel.Kernel;
import jun.util.Time;
import jun.util.Util;

/**
 * Used for solving convex quadratic programming of hyper-sphere
 * 
 * @author jun
 */ 
public class HSSolver {
    
    private static final double MAX = Double.MAX_VALUE;
    
    private Param param;
    private Type type;
    private Kernel kernel;
    
    private double[] alpha;     // The result of solver processing
    private double[] u;         // Gradient of objective function
    
    private KMatrix km;
    
    
    public  HSSolver(Param param, Type type, Kernel kernel){
        this.param = param;
        this.type = type;
        this.kernel = kernel;
        init();
    }

    private void init() {
        
        /* 
         * initialize alpha,
         * based on: s.t. alpha[0]+alpha[1]+...+alpha[i]+...+alpha[len]=1
         */
        alpha = new double[type.getLen()];
        alpha[0] = 1.0;
        for(int i=1; i<alpha.length; i++) {
            alpha[i] = 0;
        }
        
        long buildStart = Time.get();
        km = createKernelMatrix();
        
        Util.outln("Building time: " + Time.show(Time.get()-buildStart));
        
        /*
         * initialize gradients ui,
         * u[i]=2*alpha[0]*Q(0,i)-Q(i,i), alpha[i]=0(if i != 0)
         */
        u = new double[type.getLen()];
        double[] r0 = km.getRow(0);
        for(int i=0; i<u.length; i++){
            u[i] = 2*r0[i] - km.getKii(i);   
        }
    }

	private KMatrix createKernelMatrix() {
		if(calUTMRequiredMemory() > SVMConst.UTM_PERMITTED_SIZE){
            return new CacheMatrix(param, type, kernel);
        } else {
            return new UTMatrix(type, kernel);
        }
	}

    //calculate memory size which UTM matrix require
    private int calUTMRequiredMemory(){
    	/*
    	 * the value of len*len may be very big, so use long cast
    	 */
        return (int)((long)4*type.getLen()*(type.getLen()+1)/(1024*1024));
    }
    
    /**
     * @SMO algorithm
     * @return a double array, to store optimal solution multipliers
     */
    public double[] doSolve() {
    	long startTime = Time.get();
    	
        if(alpha.length == 1) {
            return alpha;
        }
        
        Util.out("Iterating...\n");
        
        int iterations = 0;  
        while(true) {
            if(isSatisfiedKKT()) {
                break;
            }
            
            int[] B = selectWorkingSet();
            if(B[1] == -1) {
                throw new RuntimeException("Working set selection error !");
            }
            int i = B[0];
            int j = B[1];
            
            iterations++; 
            if(iterations%1000 == 0) {//show progress when iterating
                Util.out(".");
            }

            /*
             *  update alpha[i] and alpha[j]
             */
            double oldAlpha_i = alpha[i];
            double oldAlpha_j = alpha[j];
            double[] ri = km.getRow(i);
            double[] rj = km.getRow(j);
            double d = ri[i] + rj[j] - ri[j];
            if(d <= 0){
                throw new RuntimeException("The second derivative must be greater " +
                        "than 0 ! Please check the type of kernel function!");
            }
            
            double tempAlpha_i = oldAlpha_i + (u[j]-u[i])/(2*d);   
            
            /* correct alpha[i} */
            double L = Math.max(0, oldAlpha_i + oldAlpha_j - param.C); 
            double H = Math.min(oldAlpha_i + oldAlpha_j, param.C); 
            if(tempAlpha_i > H){
                alpha[i] = H;
            } else if(tempAlpha_i < L) {
                alpha[i] = L;
            } else {
                alpha[i] = tempAlpha_i;
            }
            alpha[j] = oldAlpha_i + oldAlpha_j - alpha[i]; 
            
            /* update all gradient u[i] */
            for(int k=0; k<u.length; k++) {
                u[k] += 2*( (alpha[i] - oldAlpha_i) * ri[k] + (alpha[j] - 
                		oldAlpha_j) * rj[k] );    
            }
        }

        Util.out("Time: " + Time.show(Time.get()-startTime) + ",   " +
                "iterations: " + iterations + "\n\n");
        
        return alpha;
    }

    /*
     * Working set selection via the "maximal violating pair" 
     * @will be used for a optional selecting way 
     */
    private int[] selectWorkingSetPro(){ 
        return getMaxPair();
    }
    
    //select working set using Lin-method from Taiwan
    private int[] selectWorkingSet(){
        int[] B = new int[]{-1, -1};
        
        //select i which belongs to I(up)
        double minUt = MAX;
        for(int t=0; t<alpha.length; t++){
            if(alpha[t] != param.C) {        //I(up)
                if(u[t] < minUt){
                    minUt = u[t];
                    B[0] = t;
                }
            }
        }
        
        //select j which belongs to I(low)
        double maxObj = -MAX;
        double[] ri = km.getRow(B[0]);
        for(int t=0; t<alpha.length; t++){ 
            if(t != B[0]){  // (i != j) is required
                if(alpha[t] != 0){  //I(low)
                    double bit = -u[B[0]]+u[t];
                    if (bit > 0) {	//violating pair
                        double obj = bit*bit / (km.getKii(B[0])+km.getKii(t)-2*ri[t]);
                        if(obj > maxObj){
                            maxObj = obj;
                            B[1] = t;
                        } 
                    }
                }
            }
        }
        
        return B;
    }
    
    //Get maximal violating pair
    private int[] getMaxPair(){
        int max = -1;
        int min = -1;
        double maxUt = -MAX;
        double minUt = MAX;
        for(int t=0; t<alpha.length; t++){
            if(alpha[t] != param.C) {        //I(up)
                if(-u[t] > maxUt){
                    maxUt = -u[t];
                    max = t;
                }
            }
            if(alpha[t] != 0){               //I(low)
                if(-u[t] < minUt){ 
                    minUt = -u[t];
                    min = t;
                }
            }
        }
        
        //if true, satisfy the stopping criterion,
        if( (maxUt - minUt) <= param.eps){
            return null;
        }
        
        return new int[]{max, min};
    }
    
    private boolean isSatisfiedKKT(){
        double minUt = MAX;
        double maxUt = -MAX;
        for(int t=0; t<alpha.length; t++) {
            if(alpha[t] != param.C) {        //I(up)
                if(u[t] < minUt){
                    minUt = u[t];
                }
            }
            if(alpha[t] != 0){               //I(low)
                if(u[t] > maxUt){ 
                    maxUt = u[t];
                }
            }
        }
        
        //if true, satisfy the stopping criterion,
        if(maxUt - minUt <= param.eps){ 
            return true;
        }
        
        return false;
    }
}
