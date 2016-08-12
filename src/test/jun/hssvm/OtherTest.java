package jun.hssvm;

import jun.util.Util;
import jun.util.XMLParser;

import org.junit.Test;
import static org.junit.Assert.*;

public class OtherTest {
	private String ROOT = "../";
	
	
	public void testNaN() {
		assertTrue(Util.isNumber("NaN"));
		System.out.println(Util.toDouble("NaN"));
		assertFalse(Util.isNumber("naN"));
		assertTrue(Util.isNumber("100"));
	}

	@Test
	public void testXMLHandler() {
		String xmlpath = ROOT + "hssvm-config.xml";
		String rootNode = "hssvm-config";
		XMLParser parser = XMLParser.getInstance(xmlpath, rootNode);
		
		Util.outln("max heap: " + parser.getValue("java-heap-max"));
		Util.outln("train file: " + parser.getValue("train-file"));
		Util.outln("write-search-result: " + parser.getValue("write-search-result"));
		//Util.outln("test=" + parser.getValue("test"));
		Util.outln("gamma-l:" + parser.getValue("glower"));
	}
	
}
