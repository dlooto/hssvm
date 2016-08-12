package jun.hssvm;

import jun.hssvm.kernel.Kernel;

/**
 * when the number of samples is small enough, using this structure
 * @author jun
 *
 */
public class UTMatrix extends KMatrix {

    private double[] utm;       // Store kernel matrix(UTM style)
    
    
    public UTMatrix(){ super(); }
    
    public UTMatrix(Type type, Kernel kernel){
        super(type, kernel);
    }
    
    protected void buildMatrix(){
        utm = new double[calUtmLen(type.getLen())];
        for(int i=0; i<type.getLen(); i++){
            for(int j=i; j<type.getLen(); j++){  
                utm[calIndexForUTM(i,j)] = kernel.K(type.getSample(i), 
                        type.getSample(j));
            }
        }
    }
    
    public double getKii(int i) {
        return utm[calIndexForUTM(i, i)];
    }

    // in UTM, the column index j must be >= the row index i
    public double[] getRow(int i) {
        double[] rowData = new double[type.getLen()];
        for(int j=0; j<rowData.length; j++) {
            if(i <= j) {
                rowData[j] = utm[calIndexForUTM(i, j)];
            } else {
                rowData[j] = utm[calIndexForUTM(j, i)];
            }
        }
        
        return rowData;
    }
    
    protected int calRequiredMem(){
        return (4*type.getLen()*(type.getLen()+1)/(1024*1024)) + SVMConst.MARGIN;
    }
    
    //Get the element index in utm-matrix from the ith row and the jth col 
    private int calIndexForUTM(int i, int j) {
        return (int)(0.5*(2*type.getLen()-i-1)*i+j);
    }

    //Get the length of UTM
    private int calUtmLen(int l){
        return (int)(0.5*l*(l + 1));
    }
    
    public String toString(){
        return "Upper triangular matrix";
    }
}
