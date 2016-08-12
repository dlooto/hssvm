package jun.util;

import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * To parse specified XML file and get arguments set
 * @author jun
 *
 */
public class XMLParser {

	private static XMLParser parser = null;
	
	//private Document doc;
	private Node root;
	
	
	private XMLParser() {}
	
	private XMLParser(String xmlpath, String rootnode) {
		this();
        init(xmlpath, rootnode);
	}
	
	private void init(String xmlpath, String rootnode) {
		try {
        	root = new SAXReader().read(xmlpath).selectSingleNode(rootnode);
        } catch (DocumentException de){
        	Util.errln("Initialize xml parser failed !\n" + de);
        } catch (Exception e){
        	e.printStackTrace();
        }
	}
	
	/**
	 * 
	 * @param path the path of xml file needed to be handled 
	 * @param root the root node name within xml file 
	 * @return a single instance of XMLParser
	 */
	public static XMLParser getInstance(String path, String root) {
		if(parser == null) {
			parser = new XMLParser(path, root); 
		}
		return parser;
	}
	
	/**
	 * get attribute value by name
	 */
	public String getValue(String name) {
		try {
			//TODO what's the difference between methods getStringValue and getText() ? 
			return root.selectSingleNode(name).getText();
		} catch(Exception e) {
			Util.errln(e + "\nUnknown element name: " + name);
			return null;
		}
	}
}
