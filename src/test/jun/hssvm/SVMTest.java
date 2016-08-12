package jun.hssvm;

import java.util.ArrayList;
import java.util.Map;

import junit.framework.TestCase;

import jun.hssvm.kernel.Kernel;
import jun.hssvm.kernel.KernelType;
import jun.hssvm.kernel.RBFKernel;
import jun.util.File;
import jun.util.LRUCache;
import jun.util.Util;
import jun.util.Time;

/**
 * 
 * @author jun
 *
 */
public class SVMTest extends TestCase {
	/* the dir of data files */
	private String fullpath = File.getAbsolutePath("") + "/data/";
    
    private Param param;
    private Problem prob;
    private ArrayList<Type> typeList;
    
    protected void setUp() throws Exception {
    	String fileName = fullpath + "winetest";
        param = parseCmdLine();
        prob = SVM.readProblem(fileName, null);
        typeList = SVM.groupClasses(prob);
    }
    
    public void testEnum() {
    	assertEquals(KernelType.RBF, KernelType.getEnum(2));
    	assertEquals(KernelType.POLY, KernelType.getEnum(1));
    	assertEquals(KernelType.UNKNOWN, KernelType.getEnum(0));
    	assertEquals(KernelType.UNKNOWN, KernelType.getEnum(4));
    }
    
    public void testFilePath() {
    	String name1 = "TestStudy.java";
    	String name2 = "/home/jun/Work/workspace/hssvm/src/test/jun/hssvm/TestStudy.java";
    	
//    	Util.outln("name1 relativePath: " + File.getFileName(name1));
//    	Util.outln("name1 absolutePath: " + File.getAbsolutePath(name1));
//    	Util.outln("name2 relativePath: " + File.getFileName(name2));
//    	Util.outln("name2 absolutePath: " + File.getAbsolutePath(name2));
//    	
//    	assertEquals("TestStudy.java", File.getFileName(name1));
//    	//assertEquals(name2, File.getAbsolutePath(name1));
//    	assertEquals("TestStudy.java", File.getFileName(name2));
    	//assertEquals(name2, File.getAbsolutePath(name1));
    }
    
    public void testCache(){
        LRUCache<Integer, Double[]> cache = new LRUCache<Integer,Double[]>(3);
        cache.put(1, new Double[]{1.0, 1.0, 1.0});
        cache.put(2, new Double[]{2.0, 2.0, 2.0});
        cache.put(3, new Double[]{3.0, 3.0, 3.0});
        
        cache.put(4, new Double[]{4.0, 4.0, 4.0});
        assertEquals(3, cache.usedEntries());
        
        assertNull(cache.get(1));
        
        cache.put(5, new Double[]{5.0, 5.0});
        assertNull(cache.get(2));
        
        assertNotNull(cache.get(4));
        
        for (Map.Entry<Integer,Double[]> e : cache.getAll()){
            System.out.print (e.getKey() + " : " );
            Util.printArray(e.getValue());
         }
        int i=0;
    }
    
    public void testNumberFormat(){
        assertTrue(Util.isNumber("1.0"));
        
        assertTrue(Util.isNumber("1d"));
        assertFalse(Util.isNumber("-d"));
        assertFalse(Util.isNumber("dff"));
    }
    
    public void testType(){
        Type type = new Type(1000);
        for(int i=0; i<20; i++){
            type.addSample(new double[]{i+1,i+1,i+1});
        }
        
        Type sub1 = type.subType(0, 5);
        Type sub11 = type.exclude(0, 5);
        assertEquals(5, sub1.getLen());
        assertEquals(15, sub11.getLen());
        
        Type sub2 = type.subType(5, 10);
        Type sub22 = type.exclude(5, 10);
        assertEquals(5, sub2.getLen());
        assertEquals(15, sub22.getLen());
        
        Type sub3 = type.subType(15, 20);
        Type sub33 = type.exclude(15, 20);
        assertEquals(5, sub3.getLen());
        assertEquals(15, sub33.getLen());
        
        Type sub0 = type.subType(3,5);
        assertEquals(2, sub0.getLen());
    }
    
    public void testInteger(){
        Integer a = 10;
        Integer b = 10;
        assertTrue(a == b);
        assertTrue(a.equals(b));
        
        Integer a3 = 10;
        Integer b3 = 15;
        assertTrue(a3 != b3);
        assertTrue(!a3.equals(b3));
        
        Integer a2 = 129;
        Integer b2 = 129;
        assertTrue(a2 != b2);
        assertTrue(a2.equals(b2));
        
        Integer a1 = 129;
        Integer b1 = 200;
        assertTrue(a1 != b1);
        assertTrue(!a1.equals(b1));
    }
    
    public void testTypeConvert(){
        Double d = (Double)Util.toNumber("123", Double.class);
        Integer i = (Integer)Util.toNumber("100", Integer.class);
        Integer dd = (Integer)Util.toNumber("dfd", Integer.class);
        int k = 0;
    }
    
    public void testGeneric(){
        TestStudy t = new TestStudy();
        ArrayList<String> as = new ArrayList<String>();
        as.add("ni");
        as.add("hao");
        t.printCollection(as);
        
        Integer[] ia = new Integer[]{1, 3 ,4, 5};
        Util.printArray(ia);
        
        Double[] da = new Double[]{new Double(6), new Double(8) ,new Double(10), new Double(0)};
        Util.printArray(da);
        int[] a = new int[]{1,2,3};
        Util.initArray(a, 0);
    }
    
    public void testTime(){
        long t1 = Time.get();
        Time.show(t1);
    }

    public void testPickType(){
        Type t = new Type(1);
        t.addSample(new double[]{1, 2, 3, 4});
        t.addSample(new double[]{2, 1, 5, 3});
        //t.addSample(new double[]{4, 7, 6, 0});
        //t.addSample(new double[]{14, 8, 6, 10});
        //t.addSample(new double[]{1.2, 3.5, 6, 0.1});
        
        Type tType = new Type(t.getLabel());
        Type pType = new Type(t.getLabel());
        //t.pick(SVMConst.DEFAULT_FOLD, tType, pType);
        int k=0;
    }
    
    public void testGetRandArray(){
        int[] t = Util.randArray(10, 4);
        for(int i=0; i<t.length; i++){
            for(int j=i+1; j<t.length; j++){
                if (t[i] == t[j]){
                    throw new RuntimeException("Error equals");
                }
            }
        }
        int i = t[t.length-1];
    }
    
    public void testMaxAndMin(){
        double[] x = new double[]{1, 2, 7, 3, 9};
        assertEquals((double)9, Util.getMax(x));
        assertEquals((double)1, Util.getMin(x));
    }
    
//    public void testMatlabJar(){
//        MatlabQPSolver.doSolve(null);
//    }
    
    public void testScaleData(){
        Problem prob = new Problem(5, 4);
        for(int i=0; i<prob.getLen(); i++){
            double[] xi = new double[]{Util.rand(10), Util.rand(10), Util.rand(10), Util.rand(10)};
            prob.setXi(i, xi);
        }
        
        prob.scaleData(new Scope(-1, 1));
        int k=0;
    }
    
    public void testSolver(){
        
        //initialize problem
        String filePath = fullpath + "zootrain";
        Problem prob = null;
        try{
            prob = SVM.readProblem((filePath), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        param = parseCmdLine();
        typeList = SVM.groupClasses(prob);
        assertEquals(5, typeList.get(5).getLen());
        
//        HSSolver solver = new HSSolver(param, typeList.get(5)); 
        /*
        double[] utm = solver.getUtm();
        assertEquals(15,utm.length);
        
        for(int i=0; i<typeList.get(5).getLen(); i++){
            for(int j=i; j<typeList.get(5).getLen(); j++) {
                utm[solver.calUtmIndex(i, j)] = solver.calUtmIndex(i, j) + 1;
            }
        }
        
        //test calculate UTM-matrix index
        assertEquals((double)1, utm[solver.calUtmIndex(0, 0)]);
        assertEquals((double)5, utm[solver.calUtmIndex(0, 4)]);
        assertEquals(0, solver.calUtmIndex(0, 0));
        assertEquals(1, solver.calUtmIndex(0, 1));
        assertEquals(2, solver.calUtmIndex(0, 2));
        assertEquals(5, solver.calUtmIndex(1, 1));
        assertEquals(6, solver.calUtmIndex(1, 2));
        assertEquals(9, solver.calUtmIndex(2, 2));
        
        assertEquals((double)8, utm[solver.calUtmIndex(1, 3)]);
        assertEquals((double)9, utm[solver.calUtmIndex(1, 4)]);
        assertEquals((double)10, utm[solver.calUtmIndex(2, 2)]);
        assertEquals((double)15, utm[solver.calUtmIndex(4, 4)]);
        */
        
    }
    
    public void testGroupClass(){
        //local problem
        String fileName = fullpath + "zootrain";
        Problem prob = null;
        try{
            prob = SVM.readProblem(fileName, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //tes problem scale
        assertEquals(87, prob.getLen());
        assertEquals(16, prob.getDims());
        
        typeList = SVM.groupClasses(prob);
        assertEquals(7, typeList.size());

        //test get samples length
        assertEquals(37, typeList.get(0).getLen());
        assertEquals(11, typeList.get(3).getLen());
        assertEquals(9, typeList.get(6).getLen());
        
        //assert get some dimention value
        assertEquals(5, typeList.get(4).getLabel());
        assertEquals((double)3, typeList.get(4).getValue(2, 9));
        assertEquals((double)0, typeList.get(4).getValue(0, 0));
        assertEquals((double)1, typeList.get(4).getValue(2, 15));
        
        //assert add and get a sample
        Type t = typeList.get(4);
        assertEquals(3, t.getLen());
        double[] s = new double[prob.getDims()];
        for(int i=0; i<s.length; i++){
            s[i] = i+1;
        }
        t.addSample(s);
        assertEquals(4, t.getLen());
        assertArrayEquals(s, t.getSample(3));
    }
    
    public void testKernelFunc(){
        Kernel kf = new RBFKernel(param.gamma);
        
        //test dot product
        double[] x = new double[]{2, 1, 3};
        double[] y = new double[]{4, 5, 7}; 
        assertEquals((double)14, kf.dot(x, x));
        assertEquals((double)90, kf.dot(y, y));
        assertEquals((double)34, kf.dot(x, y));
        
        //test ||x - y||^2 = x.x - 2*x.y + y.y
        assertEquals((double)(14+90-2*34), kf.calDotDist(x, y));   
        
        //How to test the calKernelDist();  ?
    }
    
    protected void tearDown() throws Exception {
    }

    private Param parseCmdLine() {
        return new Param();
    }
    
    //compare two double[] array
    private void assertArrayEquals(double[] r1, double[] r2){
        assertEquals(r1.length, r2.length);
        for(int i = 0; i < r1.length; i++){
            assertEquals(r1[i], r2[i]);
        }
    }
}
