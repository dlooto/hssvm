package jun.hssvm;

import jun.hssvm.kernel.Kernel;
import jun.util.Util;

abstract public class KMatrix {
    
    private static final int maxHeapSize = Util.maxHeapSize();
    private int requiredMem;      //the max memory needed
    
    protected Param param = new Param();
    protected Type type;
    protected Kernel kernel;
   
    
    protected KMatrix(){}
    
    protected KMatrix(Type type, Kernel kernel){
        this();
        this.type = type;
        this.kernel = kernel;
        checkMemory();
        
        Util.outln(getRequiredMem() + " M memory will be used. Building " + this + "...");
        buildMatrix();
    }
    
    //TODO duplicate code
    protected KMatrix(Param param, Type type, Kernel kernel){
        this();
        this.param = param;
        this.type = type;
        this.kernel = kernel;
        checkMemory();
        
        Util.outln(getRequiredMem() + " M memory will be used. Building " + this + "...");
        buildMatrix();
    }
    
    /**
     * get one row from kernel matrix,
     * @param i the row index
     * @return double type array
     */
    abstract public double[] getRow(int i);
    
    /**
     * get K(i, i) 
     * @param i the index of sample
     * @return
     */
    abstract public double getKii(int i);
    
    /**
     * get required memory,measured in MBytes
     */
    public int getRequiredMem(){
        return requiredMem;
    }
    
    public String toString() {
        return "";
    }
    
    abstract protected int calRequiredMem();
    
    abstract protected void buildMatrix();
    
    private void checkMemory() {
        requiredMem = calRequiredMem();
        if(requiredMem >= maxHeapSize) {
            throw new RuntimeException("\nBuilding " + this + " require memory " + 
                    requiredMem + "M, \nbut current max java heap size is: " + 
                    maxHeapSize + "M");
                    
        }
    }
}
