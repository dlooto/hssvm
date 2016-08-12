package jun.hssvm;

import jun.hssvm.kernel.Kernel;
import jun.util.LRUCache;

/**
 * 
 * @author jun
 *
 */
public class CacheMatrix extends KMatrix {

    private LRUCache<Integer, double[]> cache;
    private double[] diaVector;                 //store diagonal K(i, i)
    private int cacheSize;                      //how many rows in cache
    
    public CacheMatrix() { super(); }
    
    public CacheMatrix(Param param, Type type, Kernel kernel) {
        super(param, type, kernel);
    }
    
    //called from constructor
    protected void buildMatrix(){
        // calculate K(i,i)
        diaVector = new double[type.getLen()];
        for(int j=0; j<diaVector.length; j++){
            diaVector[j] = kernel.K(type.getSample(j), type.getSample(j));
        }
        
        // initialize cache
        
        cacheSize = (int)(type.getLen() * param.cacheFactor);
        if(cacheSize < 2) {
            cacheSize = 2;
        }
        
        cache = new LRUCache<Integer, double[]>(cacheSize);
        int prefetchRows = (int)(cacheSize * SVMConst.PREFETCH_FACTOR);
        for(int i=0; i<prefetchRows; i++){
            cache.put(i, buildRowData(i));
        }
    }

    public double getKii(int i) {
        return diaVector[i];
    }

    public double[] getRow(int i) {
        double[] rowData = cache.get(i);
        if(rowData != null) {
            return rowData;
        } else {
            cache.put(i, buildRowData(i));
            return cache.get(i);
        } 
    }

    private double[] buildRowData(int i) {
        double[] rowData = new double[type.getLen()];
        for(int j=0; j<rowData.length; j++){
            rowData[j] = kernel.K(type.getSample(i), type.getSample(j));
        }
        return rowData;
    }
    
    //Required memory = 8*len*len*factor*/(1024*1024) + margin
    protected int calRequiredMem() {
        return (int)(type.getLen()*type.getLen()*param.cacheFactor*8/(1024*1024)+ 
                SVMConst.MARGIN);
    }
    
    public String toString(){
        return "Cache-type matrix";
    }
}
