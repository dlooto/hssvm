package jun.util;

import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/**
 * Tool class
 * 
 * @author xjBean
 */
public class Util {
	
    /**
     * Normalize the array elements using formula : 
     *     y = (H-L)*(x-xmin)/(xmax-xmin) + L, 
     *  and if xmin == xmax, y = L; xmin= min(a[i]), xmax=max(a[i])
     *     
     * @param a the array to be normalized
     * @param L  lower bound 
     * @param H  upper bound
     */
    public static void scale(double[] a, double L, double H){
        double xmin = Util.getMin(a);
        double xmax = Util.getMax(a);
        if(xmin == xmax){
            for(int i=0; i<a.length; i++){
                a[i] = L;
           }
           return;
        }
        
        for(int i=0; i<a.length; i++){
            a[i] = (H-L)*(a[i]-xmin)/(xmax-xmin) + L;
        }
    }
    
    /**
     * Get m integer numbers from 0 to n(exclude n), every number is 
     * not equal to each other
     * 
     * @return a array which store m numbers
     */
    public static int[] randArray(int n, int m){
        if(n < m || m == 0 || n == 0){
            throw new RuntimeException("Can't get " + m +" numbers from " + n +" !");
        }
        
        int[] result = new int[m];
        
        //initialize assistant array
        LinkedList<Integer> a =  new LinkedList<Integer>();
        for(int i=0; i<n; i++){
            a.add(i);
        }
        
        //generate result array
        for(int i=0; i<m; i++){
            int ri = rand(a.size());
            result[i] = a.get(ri);
            a.remove(ri);
        }
        
        return result;
    }
    
    /**
     * get the max value from an array
     */
    public static double getMax(double[] x) {
        int max = 0;
        for(int i=0; i<x.length; i++){
            if(x[i] > x[max]){
                max = i;
            }
        }
        
        return x[max];
    }
    
    /**
     * get the min value from an array
     */
    public static double getMin(double[] x) {
        int min = 0;
        for(int i=0; i<x.length; i++){
            if(x[i] < x[min]){
                min = i;
            }
        }
        
        return x[min];
    }
    
    /**
     * Generate random integer,between 0 and scope(exclude scope)
     * @param scope the upper bound of random integer 
     */
    public static int rand(int scope){
        Random r = new Random();
        return r.nextInt(scope);
    }
    
    /**
     * convert string of boolean value
     * @param s y: true n: false
     */ 
    public static boolean toBool(String s) {
        if(s.equals("y") || s.equals("yes") || s.equals("true")){
            return true;
        } else if(s.equals("n") || s.equals("no") || s.equals("false")){
            return false;
        }
        
        return false;
    }
    
    /**
     * convert string of ASCII-character to double value
     */ 
    public static Double toDouble(String s) {
        return Double.valueOf(s).doubleValue();
    }

    /**
     * convert string of ASCII-character to integer value
     */
    public static int toInt(String s) {
        return Integer.parseInt(s);
    }
	
    /**
     * convert string to integer or double value
     * @param s  converted string
     * @param num  Double or Integer class type
     */
    public static Number toNumber(String s, Class num) {
        try {
            if(num.getName().equals("java.lang.Double")){
                return Double.valueOf(s);
            } else if(num.getName().equals("java.lang.Integer")) {
                return Integer.valueOf(s);
            } 
        } catch(Exception e){
            if(e instanceof NumberFormatException){
                Util.errln("Number format error: " + s);
                return null;
            } 
        }
        
        Util.errln("Unknown number type: " + s );
        return null;
    }
	
    public static boolean isNumber(String s){
        try {
            Double.valueOf(s);
        } catch(NumberFormatException e){
            return false;
        }
        
        return true;
    }
    
	/**
	 * print array elements to console
	 */
    public static <T> void printArray(T[] ar){
        System.out.println();
        for(int i = 0; i < ar.length; i++){
            System.out.print(ar[i] + " ");
        }
        
        System.out.println();
    }
    
    public static void outln(Object o){
            System.out.println(o);
    }
    
    public static void out(Object o){
        System.out.print(o);    
    }
    
    public static void err(Object o) {
        System.err.print(o);
    }
    
    public static void errln(Object o) {
        System.err.println(o);
    }
    
    public static void exit(int status) {
    	System.exit(status);
    }
    
    public static void exit(String errMsg) {
    	System.err.println(errMsg);
    	System.exit(1);
    }
    
    /**
     *  Set initial values for array elements 
     */
    public static <T> void initArray(T[] ar, T v){
        for(int i = 0; i < ar.length; i++){
            ar[i] = v;
        }
    }
    
    public static void initArray(int[] ar, int v){
        for(int i = 0; i < ar.length; i++){
            ar[i] = v;
        }
    }
    
    /**
     * @return the total amount of memory currently available for current and 
     * future objects, measured in MBytes.
     */
    public static int currentMemory(){
        int mb = 1024 * 1024;
        return (int)Runtime.getRuntime().totalMemory()/mb;
    }
    
    /**
     * Returns the maximum amount of memory that the Java virtual machine 
     * will attempt to use, measured in MBytes
     */
    public static int maxHeapSize(){
        int mb = 1024 * 1024;
        return (int)(Runtime.getRuntime().maxMemory()/mb);
    }
    
    /**
     * make sure user input
     * @return if user input "y" return null, else return "Operation aborted"
     */
    public static String confirm() {
    	Util.out("Sure to do?(y/n)\n");
		Scanner sc = new Scanner(System.in);
        String in = sc.next();
        if(!in.equals("y")) {
        	return("Operation aborted");
        } else {
        	return null;
        }
	}
}
