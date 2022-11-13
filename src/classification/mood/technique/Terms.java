package src.classification.mood.technique;

import java.util.*;
import java.io.*;

/**
 * This file is used to create array of Term object.
 * This class is used as a tool for constructing array of Term object out of given data file. 
 * For each data file it extracts terms and builds Term object and adds them into array.
 * Overaly class constructs 6 Term arrays. 
 * 
 * @author Enkhbold Nyamsuren
 */
public class Terms{
	
	/**
	 * stores the list of terms extracted of data file representing happy mood 
	 */
	protected ArrayList<Term> happyTerms;
	
	/**
	 * stores the list of terms extracted of data file representing unhappy mood 
	 */
	protected ArrayList<Term> unhappyTerms;
	
	/**
	 * stores the list of terms extracted of data file representing angry mood 
	 */
	protected ArrayList<Term> angryTerms;
	
	/**
	 * stores the list of terms extracted of data file representing fear mood 
	 */
	protected ArrayList<Term> fearTerms;
	
	/**
	 * stores the list of terms extracted of data file representing sad mood 
	 */
	protected ArrayList<Term> sadTerms;
	
	/**
	 * stores the list of terms extracted of data file that do not explicitely represent any particular mood 
	 */
	protected ArrayList<Term> allTerms;
	
	/**
	 * contains the path to data file within the package the value is "..\\classes\\classification\\mood\\technique\\data\\"
	 */
	private String directory = "..\\classes\\classification\\mood\\technique\\data\\";
	
	/**
	 * Default constructor is used to intialize resources and read data files into a memory.
	 * Call 6 othe methods:
	 * initHappy, initUnhappy, initSad, initAngry, initFear, initAllTerms
	 */
	public Terms(){
		initHappy();
		initUnhappy();
		initSad();
		initAngry();
		initFear();
		initAllTerms();
	}
	
	/**
	 * This method is used to read data file containing general terms
	 *
	 */
	private void initAllTerms(){
		allTerms = new ArrayList<Term>();
		readTermsIntoMemory(directory+"allTerms.dat", allTerms);
	}
	
	/**
	 * This method is used to read data file containing terms that represent Happy mood
	 *
	 */
	private void initHappy(){
		happyTerms = new ArrayList<Term>();
		readTermsIntoMemory(directory+"happyTerms.dat", happyTerms);
	}
	
	/**
	 * This method is used to read data file containing terms that represent Unhappy mood
	 *
	 */
	private void initUnhappy(){
		unhappyTerms = new ArrayList<Term>();
		readTermsIntoMemory(directory+"unhappyTerms.dat", unhappyTerms);
	}
	
	/**
	 * This method is used to read data file containing terms that represent Sad mood
	 *
	 */
	private void initSad(){
		sadTerms = new ArrayList<Term>();
		readTermsIntoMemory(directory+"sadTerms.dat", sadTerms);
	}
	
	/**
	 * This method is used to read data file containing terms that represent Angry mood
	 *
	 */
	private void initAngry(){
		angryTerms = new ArrayList<Term>();
		readTermsIntoMemory(directory+"angryTerms.dat", angryTerms);
	}
	
	/**
	 * This method is used to read data file containing terms that represent Fear mood
	 *
	 */
	private void initFear(){
		fearTerms = new ArrayList<Term>();
		readTermsIntoMemory(directory+"fearTerms.dat", fearTerms);
	}
	
	/**
	 * this method is used to get array containing objects of Term class with Happy terms
	 * @return	ArrayList<Term>
	 */
	public ArrayList<Term> getHappyTerms(){
		return happyTerms;
	}
	
	/**
	 * this method is used to get array containing objects of Term class with Unhappy terms
	 * @return	ArrayList<Term>
	 */
	public ArrayList<Term> getUnhappyTerms(){
		return unhappyTerms;
	}
	
	/**
	 * this method is used to get array containing objects of Term class with Angry terms
	 * @return	ArrayList<Term>
	 */
	public ArrayList<Term> getAngryTerms(){
		return angryTerms;
	}
	
	/**
	 * this method is used to get array containing objects of Term class with Sad terms
	 * @return	ArrayList<Term>
	 */
	public ArrayList<Term> getSadTerms(){
		return sadTerms;
	}
	
	/**
	 * this method is used to get array containing objects of Term class with Fear terms
	 * @return	ArrayList<Term>
	 */
	public ArrayList<Term> getFearTerms(){
		return fearTerms;
	}
	
	/**
	 * this method is used to get array containing objects of Term class with general terms
	 * @return	ArrayList<Term>
	 */
	public ArrayList<Term> getAllTerms(){
		return allTerms;
	}
	
	/**
	 * This is the method that actually parses the data files and extracts the information about terms
	 * As it extract information it stores them in Term object and add object into array.
	 * 
	 * @param fileName	name of data file to parse
	 * @param termArray	name of array where to store Term object
	 * @return	String error message if file doesn't exist or cannot be read.
	 */
	public String readTermsIntoMemory(String fileName, ArrayList<Term> termArray){
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
			String line = br.readLine();
			while(line != null){
				// parse the read line, load information into Anew data structure
				StringTokenizer st = new StringTokenizer(line);
				
				Term term = new Term();
				term.description =  (String)st.nextElement();
				term.valence = Float.valueOf((String) st.nextElement());        // don't care about column 2 - word no.
				term.arousal = Float.valueOf((String) st.nextElement());   // column 3 - valence mean
				term.dominance = Float.valueOf((String) st.nextElement());
				term.happy = Float.valueOf((String) st.nextElement());
				term.angry = Float.valueOf((String) st.nextElement());
				term.fear = Float.valueOf((String) st.nextElement());
				term.sad = Float.valueOf((String) st.nextElement());

				termArray.add(term);
				
				line = br.readLine(); // get the next line
			}
			br.close();
		}
		catch(FileNotFoundException fnfe){
			return "The Term file "+fileName+" does not exist!";
		}
		catch(IOException ioe){
			return "An I/O exception occured while reading term file!";
		}
		return null;
	}
};