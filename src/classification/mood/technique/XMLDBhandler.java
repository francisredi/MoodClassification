package src.classification.mood.technique;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

import java.io.*;

/**
 * The class is used to construct the frequency matrix of terms 
 * from a given XML database file.
 * Frequency matrix is 2 dimensional matrix where row represents term 
 * and column represents the document where the term occures. Each cell
 * contains the frequency of the term in particular document. 
 * 
 * @author Enkhbold Nyamsuren
 * @see javax.xml.parsers
 * @see org.w3c.dom
 */

public class XMLDBhandler{
	
	/**
	 * Contains the absolute path to XML database file
	 */
	private String XMLDBpath;
	
	/**
	 * 2D comlex array for storing generated frequency matrix
	 */
	private int matrix[][];
	
	/**
	 * number of columns in frequency matrix, it has static value of 250
	 */
	private static int colSize = 250;
	
	/**
	 * contains the object document of XML database file
	 */
	private Document dc;
	
	/**
	 * Default constructor of the class.
	 *
	 */
	public XMLDBhandler(){}
	
	/**
	 * setXMLPath method is used to set path to XML file that is used as
	 * a database containing data about term frequencies in each document. 
	 * 
	 * @param path
	 */
	public void setXMLDBPath(String path){
		XMLDBpath = path;
		matrix = null;
	}
	
	/**
	 * This method is used to start the matrix generation process.
	 * If data from XML file is not loaded than it automatically calls 
	 * loadXMLdb method to read XML file, and then call the parseXML method
	 * to start parsing of XML file.
	 * 
	 * @return int[][]	return 2D integer array representing terms' frequency matrix
	 * @throws Exception	Excetion is thrown if XML file cannot be found or read
	 * @see loadXMLdb
	 * @see parseXML
	 */
	public int[][] createMatrix() throws Exception{
		if(XMLDBpath != null){
			
			loadXMLdb();
			
			if(dc != null){
				parseXML();
				return matrix;
			} else
				throw new Exception("Cannot parse XML database!");
			
		} else {
			throw new Exception("Path to XML database is not specified");
		}
	}
	
	/**
	 * This method loads the XML file into a memory
	 * 
	 * @throws Exception	Exception is thrown if XML file cannot be found
	 */
	public void loadXMLdb() throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	   	DocumentBuilder builder = factory.newDocumentBuilder();
	    dc = builder.parse( new File(XMLDBpath));
	    dc.getDocumentElement().normalize();
	}
	
	/**
	 * This method is used to parse XML nodes and construct corresponding frequency matrix 
	 * represented as a 2D array. 
	 *
	 */
	public void parseXML(){
		Element elem = dc.getDocumentElement();
		NodeList termNodes = elem.getChildNodes();
		
		matrix = new int[termNodes.getLength()][colSize];

		for(int i=0; i<termNodes.getLength(); i++){
			Element termElem = (Element)termNodes.item(i);

			if(termElem.hasChildNodes()){
				NodeList childs = termElem.getChildNodes();
				
				/*int nextInt = 0;
				for(int j=0; j<childs.getLength(); j++){
					Element docElem = (Element)childs.item(j);
					int id = Integer.parseInt(docElem.getAttribute("id"));
					
					if(id > nextInt)
						for(int m=nextInt; m<id; m++)
							matrix[i][m] = 0;

					matrix[i][id] = Integer.parseInt(docElem.getTextContent());
					nextInt = id + 1;
				}*/

				for(int j=0; j<childs.getLength(); j++){
					Element docElem = (Element)childs.item(j);
					int id = Integer.parseInt(docElem.getAttribute("id"));
					
					matrix[i][id] = Integer.parseInt(docElem.getTextContent());
				}
			} else{
				/*for(int j=0; j<colSize; j++){
					matrix[i][j] = 0;
				}	*/
			}
		}
	}

};