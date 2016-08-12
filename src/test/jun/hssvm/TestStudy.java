package jun.hssvm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jun.util.File;
import jun.util.Util;

/**
 *  used for testing
 *  @author jun
 */
public class TestStudy {

    public static void main(String[] args) {
    	
        // Get the jvm heap size.
        long heapSize = Runtime.getRuntime().totalMemory();
 
        //Print the jvm heap size.
        System.out.println("Heap Size = " + heapSize);
        
        LinkedHashMap<Integer, Double[]> map = new LinkedHashMap<Integer, Double[]>();
        map.put(1, new Double[]{1.0, 1.0, 1.0});
        map.put(2, new Double[]{2.0, 2.0, 2.0});
        map.put(3, new Double[]{3.0, 3.0, 3.0});
        
        for (Iterator<Double[]> iterator = map.values().iterator(); iterator.hasNext();) {  
            Double[] value =  iterator.next();  
            Util.printArray(value);  
        }  
        
        Math.ceil(2.0);
        
        //use
        map.get(2);
        map.put(4, new Double[]{4.0,4.0,4.0});
        map.get(3);
        
        for (Iterator<Double[]> iterator = map.values().iterator(); iterator.hasNext();) {  
            Double[] value =  iterator.next();  
            Util.printArray(value);  
        }  
        
        //hashMap
        HashMap<Integer, Double[]> map1 = new HashMap<Integer, Double[]>();
        map1.put(1, new Double[]{1.0, 1.0, 1.0});
        map1.put(2, new Double[]{2.0, 2.0, 2.0});
        map1.put(3, new Double[]{3.0, 3.0, 3.0});
        
        Util.outln("HashMap: ");
        for (Iterator<Double[]> iterator = map1.values().iterator(); iterator.hasNext();) {  
            Double[] value =  iterator.next();  
            Util.printArray(value);  
        }  
    }
    
    // generic type
    public void printCollection (Collection<?> c){
        for(Object o : c) {
            System.out.println(o);
        }
    }
    
    public void method(){
        List<?> list = new ArrayList<String>();   // compile-time error!
        list.add(null);//right
        
        //list.add("Hello");  //compile error
        list.get(0);
    }
    
    public <T> void addArray(T[] a, Collection<T> c) { // c-type depends on a-type  
        for(T o : a){
            c.add(o);
        }
    }
 
//=======below code will get compile error=====================================
//    void foo(List<String> ls) {  
//        System.out.print("foo(List<String> ls)");
//    }
//    
//    void foo(List<Object> lo) {
//        System.out.print("foo(List<Object> lo)");
//    }
//    
//    <T> void foo(List<T> l) {    
//        System.out.print("foo(List<T> l)");
//    }
    
    
//    //1
    void foo(String s) {
        System.out.println("foo(String s)");
    }
//
//    //2
//    void foo(Object s) {
//        System.out.println("foo(Object s)");
//    }
//    
//    //3
    <T> void foo(T t) {
        System.out.println("foo(T t)");
    } 
    
    //2 and 3 can't be togethor
    
}
        
