package src.classification.mood.technique;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.*;
import java.io.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.*;

/**
 * The goal of this class is exactly opposite to the usage of the XMLDBhandler.
 * With given terms' frequency matrix represented as 2D array, this class converts 
 * the whole matrix into its XML representation and then store it in a given XML file.
 * 
 * @author Enkhbold Nyamsuren
 * @see javax.xml.parsers
 * @see org.w3c.dom
 * @see import javax.xml.transform
 */

public class FrequencyMatrixBuilder{
	
	private String xmldb;
	private int wordMatrix[][];
	
	/**
	 * Name of the document
	 */
	private Document dc;
	/**
	 * Size of a row
	 */
	int rowSize;
	/**
	 * Size of a column
	 */
	int colSize;
	
	/**
	 * Class constructor initilizes all resources required for creating XML file.
	 * 
	 * @param xmldb	the path containg location and name of the XML file where data should be stored
	 * @param wordMatrix	frequency matrix as a 2D array that should be converted into XML file
	 * @param rowSize	number of rows in frequency matrix
	 * @param colSize	number of columns in frequency matrix
	 */
	public FrequencyMatrixBuilder(String xmldb, int wordMatrix[][], int rowSize, int colSize){
		this.xmldb = xmldb;
		this.wordMatrix = wordMatrix;
		this.rowSize = rowSize;
		this.colSize = colSize;
		if(wordMatrix != null) countSize();
	}
	
	/**
	 * not implmented
	 *
	 */
	public void countSize(){
	}
	
	/**
	 * Read given XML file into a memory. Notice that XML is used as a framework for 
	 * adding frequency values.
	 * 
	 * @throws Exception	throws Exception if DocumentBuilder cannot be created
	 */
	public void loadXMLdb() throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	   	DocumentBuilder builder = factory.newDocumentBuilder();
	    dc = builder.parse( new File(xmldb));
	    dc.getDocumentElement().normalize();
	}	
	
	/**
	 * This function converts the frequency matrix into its XML representation 
	 * by reading matrix row by row.
	 *
	 */
	public void writeMatrix(){
		Node tempElem;
		
		if(dc == null){
			try{
				loadXMLdb();
			}catch(Exception e){
				System.out.println("XML database reading error! Details:\n" + e.toString());
				return;
			}
		}	
		for(int i=0; i<rowSize; i++){
			tempElem = getElementByID(i);
			for(int j=0; j<colSize; j++){
				if(wordMatrix[i][j] != 0){
					Element child = dc.createElement("d");
					child.setAttribute("id", ""+j);
					child.appendChild(dc.createTextNode(""+wordMatrix[i][j]));
					tempElem.appendChild(child);
				}
			}
		}
		writeIntoXMLFile();		
	}
	
	/**
	 * After XML structure has bees created, it is written into file using current method,
	 *
	 */
	public void writeIntoXMLFile(){
		try{
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			
			DOMSource source = new DOMSource(dc);
		    
			StreamResult result = new StreamResult(new File(xmldb));
		    
		    transformer.transform(source, result);
		} catch(Exception e){}
	}
	
	/**
	 * not implemented
	 * @param name
	 * @return
	 */	
	public Element getElementByDescription(String name){
		return null;
	}
	
	/**
	 * Returns the element in the XML hierarchy by given ID
	 * 
	 * @param id	id of the XML element to retrieve
	 * @return Element	the XML element that was requested
	 */
	public Element getElementByID(int id){
		Element tempElem;
		NodeList nodes =  dc.getDocumentElement().getChildNodes();
		
		for(int i=0; i<nodes.getLength(); i++){
			tempElem = (Element)nodes.item(i);
			if(tempElem.getAttribute("id").equals(""+id)){ 
				return tempElem;
			}
		}		
		return null;
	}
}