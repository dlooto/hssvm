
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import jun.hssvm.Problem;
import jun.util.File;
import jun.util.Util;


/**
 * @author jun
 */
public class TxbApp {
	public static void main(String[] args) throws Exception{
        new TxbApp().run(args);
    }

    private void run(String[] args) throws IOException {
    	if(args.length == 0) {
    		Util.exit("No process file");
    	}
    	Problem prob = readProblem(args[0]);
        if(prob == null){
            System.exit(1);
        }
        
        prob.writeToFile(args[0] + "_new", null);
        Util.outln("Processing completed.");
    }
    
    private Problem processNoIndexData(BufferedReader buffReader) throws IOException {
        if(buffReader == null){
            return null;
        }
        
        ArrayList<Integer> vy = new ArrayList<Integer>();
        ArrayList<double[]> vx = new ArrayList<double[]>();
        
        int lineCounts = 0;
        while(true) {
            String line = buffReader.readLine();
            lineCounts++;
            if(line == null) {
            	break;
            }
            
            String illegalItem = getIllegalData(line); 
            if(illegalItem != null) {
            	Util.outln("illegal data: " + illegalItem + " line " + lineCounts);
            	continue;
            }
            
            StringTokenizer st = new StringTokenizer(line, " \t\n\r\f");
            
            //add the class label
            vy.add(Util.toInt(st.nextToken()));           
            
            //add one sample data 
            double[] oneSample = new double[st.countTokens()];  
            for(int j=0; j<oneSample.length; j++) {
                oneSample[j] = Util.toDouble(st.nextToken());
            }
            vx.add(oneSample);       
        }

        Problem prob = new Problem(vy.size(), vx.get(0).length);
        for(int i=0;i<prob.getLen();i++) {
            prob.setYi(i, vy.get(i));
            prob.setXi(i, vx.get(i));
        }
        return prob;
    }
    
    private String getIllegalData(String line) {
    	if(line.indexOf("NaN") != -1) {
    		return "NaN";
    	}
    	StringTokenizer st = new StringTokenizer(line, " \t\n\r\f");
    	while(st.hasMoreTokens()) {
    		String temp = st.nextToken();
			if(!Util.isNumber(temp)) {
				return temp;
			}
    	}
    	
    	return null;
    }
    
    private Problem readProblem(String fileName) {
        BufferedReader buffReader = null;
        try {
            buffReader = File.createBufferedReader(fileName);
            return processNoIndexData(buffReader);
        } catch (IOException ioe){
            Util.errln("Read problem exception!");
            return null;
        } finally {
            if(buffReader != null){
                try {
                    buffReader.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}

