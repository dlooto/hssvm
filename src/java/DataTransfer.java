

import java.io.*;
import java.util.*;

import jun.util.Util;
import jun.util.File;
import jun.hssvm.Format;
import jun.hssvm.IndexFormat;
import jun.hssvm.Problem;
import jun.hssvm.SVM;
import jun.hssvm.SVMConst;


/**
 * Convert the sample data with index to non-index format
 * 
 * @author jun
 */
public class DataTransfer {
    
    private String srcFile;
    private String destFile;
    private Format destFormat = new Format();
    
    public static void main(String[] args) throws IOException {
        new DataTransfer().run(args);
    }
    
    private void run(String[] args) throws IOException {
        String errMsg = parseCmdLine(args);
        if(errMsg != null){
            Util.outln(errMsg);
            return;
        }
        
        Format srcFormat = getDataFormat(srcFile);
        if(srcFormat == null){
            return;
        }
        if(srcFormat.equals(destFormat)){
            Util.outln("The file is already " + srcFormat);
            return;
        }
        
        Problem prob = SVM.readProblem(srcFile, srcFormat);
        if(prob != null) {
            prob.writeToFile(destFile, destFormat);
            Util.outln("Data conversion completed.");
        }
    }
    
    private Format getDataFormat(String fileName){
        BufferedReader fp = null;
        try {
            fp = File.createBufferedReader(fileName);
            if(fp == null){
                return null;
            }
            String line = fp.readLine();
            if(line == null || line.equals("")) {
                Util.errln("No any data in file: " + fileName);
                return null;
            }
            
            StringTokenizer st = new StringTokenizer(line, SVMConst.NOINDEX_DELIMIT_SYMBOL);
            st.nextToken();//label
            String item = st.nextToken();//first data item
            for(int i=0; i<item.length(); i++){
                if(item.charAt(i) == ':'){
                    return new IndexFormat();
                }
            }
            
            return new Format();
        } catch(IOException ioe){
            if(ioe instanceof FileNotFoundException){
                Util.errln("File not found: " + fileName);  
            } else {
                Util.errln("Read file "+ fileName + " exception!");
            }
            return null;
        } finally{
            if(fp != null){
                try {
                    fp.close();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    
    private String parseCmdLine(String[] args){
        if(args.length < 1 || args.length > 3) {
            return help();
        }
        
        String defaultSuf = SVMConst.NOINDEX_DATA_SUF;
        for(int j=0; j<args.length; j++){
            if(args[j].equals("-i")){
                destFormat = new IndexFormat();
                defaultSuf = SVMConst.INDEX_DATA_SUF;
            } else if(srcFile == null){
                srcFile = args[j];
            } else if(destFile == null){
                destFile = args[j];
            }
        }
        
        if(args.length == 3){
            if(destFormat.intValue() != SVMConst.INDEX_FORMAT){
                Util.errln("Command error");
                return help();
            }
        }
        
        if(srcFile == null){
            return "No source file";
        }
        
        if(destFile == null){
            destFile = srcFile + defaultSuf;
        }
        
        return null;
    }
    
    private String help(){
        return Usage.DATA_TRANSFER; 
    }
}