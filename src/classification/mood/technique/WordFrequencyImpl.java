package src.classification.mood.technique;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import src.classification.mood.MainWindow;
import src.classification.mood.Anew;

/**
 * This class is the implementation of the Word Frequency method. All calculations and 
 * functions that are related to Word Frequency method is done within this class. 
 * 
 * @author Enkhbold Nyamsuren
 */
public class WordFrequencyImpl extends TechniqueImpl implements WordFrequency{
	
	/**
	 * contains the sorted list of ANEW words found in the blog text
	 */
	protected ArrayList<Anew> anewArray;
	
	/**
	 * reference to application's main window
	 */
	private MainWindow windowHandler;
	
	/**
	 * contain the number of total blog texts with happy mood
	 */
	int happy = 0;
	
	/**
	 * contain the number of total blog texts with angry mood
	 */
	int angry = 0;
	
	/**
	 * contain the number of total blog texts with sad mood
	 */
	int sad = 0;
	
	/**
	 * contain the number of total blog texts with fear mood
	 */
	int fear = 0;
	
	/**
	 * contain the number of total blog texts with unknown mood
	 */
	int unknown = 0;
	
	/**
	 * contains the list of ANEW terms representing unhappy mood
	 * the list is retrieved from data file within the application package
	 */
	ArrayList<Term> unhappyList;
	
	/**
	 * contains the list of ANEW terms representing fear mood
	 * the list is retrieved from data file within the application package
	 */
	ArrayList<Term> fearList;
	
	/**
	 * contains the list of ANEW terms representing sad mood
	 * the list is retrieved from data file within the application package
	 */
	ArrayList<Term> sadList;
	
	/**
	 * contains the list of ANEW terms representing angry mood
	 * the list is retrieved from data file within the application package
	 */
	ArrayList<Term> angryList;
	
	/**
	 * contains the list of ANEW terms representing happy mood
	 * the list is retrieved from data file within the application package
	 */
	ArrayList<Term> happyList;
	
	/**
	 * contains the list of ANEW terms that do not explicitely represent any particular mood
	 * the list is retrieved from data file within the application package
	 */
	ArrayList<Term> allList;
	
	/**
	 * Default constructor for class.
	 * Calls constructor of super class to create object of document parser
	 * @param handler	reference tp the MainWindow object which calls this method
	 * @see TechniqueImpl
	 */
	public WordFrequencyImpl(MainWindow handler){
		super();
		windowHandler = handler;
		anewArray = new ArrayList<Anew>();
	}
	
	/**
	 * This method is used to initialize the required resources such as loading 
	 * data files into the memory, loading blog file and parsing it. If there is need 
	 * for several blogs to be classified then this method is resposible for
	 * sequetial classification of the files. To actual classification it calls the classify method.
	 * After classification it prints output in the windows' output section.
	 *  
	 * @see Terms
	 * @see Term
	 * @see Anew
	 * @see Parser
	 */
	public void runTechnique(File blogFile) {
		
		Terms myterms = new Terms();
		unhappyList = myterms.getUnhappyTerms();
		fearList = myterms.getFearTerms();
		sadList = myterms.getSadTerms();
		angryList = myterms.getAngryTerms();
		happyList = myterms.getHappyTerms();
		allList = myterms.getAllTerms();	//list of general terms]
		
		if(blogFile.isDirectory()){
			String[] children;
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return !name.startsWith(".");
				}
			};
			
			windowHandler.printOutput("Classifying files in directory " + blogFile.getPath() + " with Word Frequency method:\n");
			children = blogFile.list(filter);			
			for (int l=0; l<children.length; l++) {
				System.out.println(children[l] + ", processing... ");
				classify(new File(blogFile.getPath() + "\\" + children[l]));				
			}
		} else{
			windowHandler.printOutput("Classifying document " + blogFile.getName() + "\n");
			classify(blogFile);
		}
		windowHandler.printOutput("Result set (number of documents for each class):\n");
		windowHandler.printOutput("Method used: Word Frequency");
		windowHandler.printOutput("	Happy: " + happy);
		windowHandler.printOutput("	Sad: " + sad);
		windowHandler.printOutput("	Angry: " + angry);
		windowHandler.printOutput("	Fear: " + fear);
		windowHandler.printOutput("	Unknown: " + unknown + "\n\n");	
	}
	
	/**
	 * this method is actual implementation of the WordFrequency method. It parses 
	 * the blog file and retrieves corresponding Anew words from anew list. This part was done
	 * by code implemented by Francis. 
	 * For the next part it reads all data files and searches for matches of anew words found
	 * in file to terms found in data files. For matching terms it retrieves their weights
	 * from data files.
	 * Finally depending on the weight of each class it assigns document to certain class.s 
	 * @param blogFile
	 */
	public void classify(File blogFile){

		StringBuffer sb = new StringBuffer();

		// read the blog text from file into memory
		String error = parser.readBlogIntoMemory(sb, blogFile, emoticonsArray);	//****** modified by Egi
		
		// correct any misspellings to blogtext here!
		
		// handle negation constructs here!
		System.out.println(sb.toString());
		parser.handleNegationInSentences(sb);
		System.out.println(sb.toString());

		// load anew vocab and corresponding values into an array in memory
		if (error == null){
			anewArray = new ArrayList<Anew>();
			error = parser.readANEWIntoMemory(sb, anewtext, anewArray);
		}

		if (error != null) { // if get a string back, there was an error
			System.out.println(error);
			return;
		}

		float happyIndex = 0;
		float sadIndex = 0;
		float fearIndex = 0;
		float angryIndex = 0;
		float unhappyIndex = 0;

		for(int i=0; i<anewArray.size(); i++){
			Anew anew = anewArray.get(i);
			boolean found = false;
			
			for(int j=0; j<happyList.size(); j++){
				Term term = happyList.get(j);
				if(anew.description.equals(term.description)){
					happyIndex += (term.happy/(term.angry + term.sad + term.fear));
					found = true;
					break;
				}
			}
			
			if(!found)
			for(int j=0; j<angryList.size(); j++){
				Term term = angryList.get(j);
				if(anew.description.equals(term.description)){
					angryIndex += (term.angry/(term.happy + term.sad + term.fear));
					found = true;
					break;
				}
			}
			
			if(!found)
			for(int j=0; j<fearList.size(); j++){
				Term term = fearList.get(j);
				if(anew.description.equals(term.description)){
					fearIndex += (term.fear/(term.happy + term.angry + term.sad));
					found = true;
					break;
				}
			}
			
			if(!found)
			for(int j=0; j<sadList.size(); j++){
				Term term = sadList.get(j);
				if(anew.description.equals(term.description)){
					sadIndex += (term.sad/(term.happy + term.angry + term.fear));
					found = true;
					break;
				}
			}

			if(!found)
			for(int j=0; j<unhappyList.size(); j++){
				Term term = unhappyList.get(j);
				if(anew.description.equals(term.description)){
					unhappyIndex += ((term.angry + term.sad + term.fear)/(term.happy + term.angry + term.sad + term.fear));
					found = true;
					break;
				}
			}

			if(!found)
			for(int j=0; j<allList.size(); j++){
				Term term = allList.get(j);
				if(anew.description.equals(term.description)){
					happyIndex += term.happy;
					angryIndex += term.angry;
					sadIndex += term.sad;
					fearIndex += term.fear;
					break;
				}
			}
		}

		angryIndex += unhappyIndex;
		fearIndex += unhappyIndex;
		sadIndex += unhappyIndex;

		if(happyIndex > sadIndex && happyIndex > fearIndex && happyIndex > angryIndex){ 
			happy++;
		} else if(angryIndex > sadIndex && angryIndex > fearIndex && angryIndex > happyIndex){
			angry++;
		} else if(fearIndex > sadIndex && fearIndex > angryIndex && fearIndex > happyIndex){
			fear++;
		} else if(sadIndex > angryIndex && sadIndex > fearIndex && sadIndex > happyIndex){
			sad++;
		} else {
			unknown++;
		}
	}
}