package jun.hssvm;

/**
 * default format is no index
 * @author jun
 */
public class Format {
    
    protected int type;

    public Format(){
        type = SVMConst.NO_INDEX_FORMAT;
    }

    public int intValue(){
        return type;
    }
    
    public String get(int index, double x){
        return ""+x;
    }
    
    public boolean equals(Object o){
        if(o instanceof Format){
            if(((Format)o).type == type){
                return true;
            } 
        }
        
        return false;
    }
    
    public String toString(){
        return "non-index format";
    }
}
