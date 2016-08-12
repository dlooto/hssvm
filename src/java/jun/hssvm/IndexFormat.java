package jun.hssvm;

/**
 * index format data 
 * @author jun
 */
public class IndexFormat extends Format {
    
    public IndexFormat(){
        type = SVMConst.INDEX_FORMAT;
    }
    
    public String get(int index, double x){
        return (index+1) + ":" + x;
    }
    
    public String toString(){
        return "index format";
    }
}
