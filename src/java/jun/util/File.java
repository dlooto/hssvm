package jun.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * Used for file operation
 * @author jun
 *
 */
public class File {

	public static String getAbsolutePath(String fileName) {
		return new java.io.File(fileName).getAbsolutePath();
	}
	
    public static DataOutputStream createDataOutputStream(String outFile){
        try {
            return new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)));
        } catch(IOException ioe){
            if(ioe instanceof FileNotFoundException){
                Util.errln("File not found: " + outFile);
                return null;
            } else {
                ioe.printStackTrace();    
            }
        }
        
        return null;
    }
    
    public static BufferedReader createBufferedReader(String inFile){
        try {
            return new BufferedReader(new FileReader(inFile));
        } catch(IOException ioe){
            if(ioe instanceof FileNotFoundException){
                Util.errln("File not found: " + inFile);
                return null;
            } else {
                ioe.printStackTrace();    
            }
        }
        
        return null;
    }
    
    public static ObjectInputStream createObjectInputStream(String inFile){
        try {
            return new ObjectInputStream(new FileInputStream(inFile));
        } catch(IOException ioe){
            if(ioe instanceof FileNotFoundException){
                Util.errln("File not found: " + inFile);
                return null;
            } else {
                ioe.printStackTrace();    
            }
        }
        return null;
    }
    
    public static ObjectOutputStream createObjectOutputStream(String outFile){
        try {
            return new ObjectOutputStream(new FileOutputStream(outFile));
        } catch(IOException ioe){
                Util.errln(ioe);
                return null;
        }
    }
}
