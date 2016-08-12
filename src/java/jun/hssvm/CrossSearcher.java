package jun.hssvm;


public class CrossSearcher extends Searcher {

    public CrossSearcher(){
        super();
    }
    
    protected void search(){
        for(int i=0; i<fold; i++){
            // generate trainTypes list and predictTypes list
            for(int j=0; j<types.size(); j++){
                int begin = i * subLens[j];
                int end = begin + subLens[j];
                predictTypes.add(types.get(j).subType(begin, end));
                trainTypes.add(types.get(j).exclude(begin, end));
            }
            
            searchOneTrip(trainTypes, predictTypes);
            
            trainTypes.clear();
            predictTypes.clear();
        }
    }
    
    public String toString(){
        return "k-fold cross-validation";
    }
}
