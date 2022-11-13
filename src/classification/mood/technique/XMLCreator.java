package src.classification.mood.technique;

import java.io.*;
import java.util.*;

/**
 * This class is used to create XML framework file out of AnewList.
 * This XML frameowrk is supposed to be used to store frequency matrix.
 * 
 * @author Enkhbold Nyamsuren
 */
public class XMLCreator{
	/**
	 * output writer to the XML file
	 */	
	static BufferedWriter fileer;
	
	/**
	 * default contsructor
	 *
	 */
	public XMLCreator(){}
	
	/**
	 * Creates new XML file with given name and array of anew words.
	 * @param xmlfile	name for the XML file
	 * @param anewArray	array of Anew words
	 */
	public void createXML(String xmlfile, ArrayList<String> anewArray){
		try{
			fileer = new BufferedWriter(new FileWriter(xmlfile, false));
		} catch(Exception e){}

		writer("<?xml version='1.0' encoding='UTF-8'?>");
		writer("<AnewWordList>");


		for(int i=0; i<anewArray.size(); i++){
			writer("<w desc='" + anewArray.get(i) + "' id='" + i + "'/>");
		}
		writer("</AnewWordList>");	
	}
	
	/**
	 * This function is used to write string of output into the XML file
	 * @param msg	the string output to be written into XML file
	 */
	static public void writer(String msg){
		try{ 
			fileer.write(msg);
			fileer.flush();
		} catch(Exception e){}
	}
}