package jun.hssvm;

import java.util.List;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import jun.util.File;


public class GroupSamplesTest {
	
	private String path = File.getAbsolutePath("") + "/data/";
	private String filepath = "";
	private Problem prob;
	
	@Before
	public void setUp() {
        filepath = path + "winetrain";
        prob = SVM.readProblem(filepath, null);
	}
	
	@Test
    public void groupByHashMap() {
		List<Type> list = SVM.groupSamples(prob);
		System.out.println("\nhashMap: ");
        for(Type t: list) {
        	System.out.println(t.getLabel() + " " + t.getLen());
        }
	}
	
	@Test
    public void compareTwoGroupingWay(){
        // test two grouping method equal
		List<Type> list1 = SVM.groupClasses(prob);
        List<Type> list2 = SVM.groupSamples(prob);
        assertEquals(list1.size(), list2.size());

        //test get samples length
        System.out.println("\noriginal way: ");
        for(Type t: list1) {
        	System.out.println(t.getLabel() + " " + t.getLen());
        }
        
        System.out.println("\nIn hashMap grouping: ");
        for(Type t: list2) {
        	System.out.println(t.getLabel() + " " + t.getLen());
        }
    }
}
