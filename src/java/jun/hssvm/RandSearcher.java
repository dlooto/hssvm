package jun.hssvm;


public class RandSearcher extends Searcher {

    public RandSearcher(){
        super();
    }
    
    protected void search(){
        for(int i=0; i<types.size(); i++){
            Type[] tp = types.get(i).randPick(subLens[i]);
            trainTypes.add(tp[0]);
            predictTypes.add(tp[1]);
        }
        
        searchOneTrip(trainTypes, predictTypes);
    }
    
    public String toString(){
        return "random cross-validation";
    }
}
