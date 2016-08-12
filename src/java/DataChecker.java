
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;

import jun.util.File;
import jun.util.Util;


/**
 * @author jun
 */
public class DataChecker {
	public static void main(String[] args) throws Exception{
        new DataChecker().run(args);
    }

    private void run(String[] args) throws IOException {
    	if(args.length == 0) {
    		Util.exit("No checked file");
    	}
    	check(args[0]);
    }
    
    private void doCheck(BufferedReader buffReader) throws IOException {
        if(buffReader == null){
            Util.exit(1);
        }
        
        int lineCounts = 0;
        int illegalLines = 0;
        while(true) {
            String line = buffReader.readLine();
            lineCounts++;
            if(line == null) {
            	break;
            }
            
            String illegalItem = getIllegalData(line); 
            
            if(illegalItem != null) {
            	illegalLines++;
            	Util.outln("error line " + lineCounts + ": " + illegalItem );
            	continue;
            }
        }
        
        if(illegalLines == 0) {
        	Util.outln("No errors detected in data.");
        } else {
        	Util.outln("Total error lines: " + illegalLines);
        }
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
    
    private void check(String fileName) {
        BufferedReader buffReader = null;
        try {
            buffReader = File.createBufferedReader(fileName);
            doCheck(buffReader);
        } catch (IOException ioe){
            Util.exit("Read file exception!");
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

