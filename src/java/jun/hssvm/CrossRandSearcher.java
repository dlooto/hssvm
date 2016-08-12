package jun.hssvm;


/**
 * every time, choose one-fold samples orderly from the max-type, 
 * and choose one-fold samples randomly from other types 
 * @author jun
 *
 */
public class CrossRandSearcher extends Searcher {

    public CrossRandSearcher(){
        super();
    }
    
    protected void search() {
        int max = getMaxTypeIndex();
        
        for(int i=0; i<fold; i++){
            predictTypes.add(types.get(max).subType(subLens[max]*i, subLens[max]*(i+1)));
            trainTypes.add(types.get(max).exclude(subLens[max]*i, subLens[max]*(i+1)));
            
            for(int j=0; j<types.size(); j++){
                if(j != max){
                    Type[] tp = types.get(j).randPick(subLens[j]);
                    trainTypes.add(tp[0]);
                    predictTypes.add(tp[1]);
                }
            }
            
            searchOneTrip(trainTypes, predictTypes);
            
            trainTypes.clear();
            predictTypes.clear();
        }
    }
    
    //remove the type that has max samples length, and return the max-type 
    private int getMaxTypeIndex(){
        int max = 0;
        for(int i=0; i<types.size(); i++){
            if(types.get(i).getLen() > types.get(max).getLen()){
                max = i;
            }
        }
        
       return max;
    }

    public String toString(){
        return "k-fold random cross-validation";
    }
}
