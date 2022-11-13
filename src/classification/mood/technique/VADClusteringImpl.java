package src.classification.mood.technique;

import java.io.*;
import java.util.ArrayList;

import src.classification.mood.MainWindow;
import src.classification.mood.Anew;

/**
 * This class is the implementation of the VAD Clustering method. All calculations and 
 * functions that are related to VAD Clustering method is done within this class. 
 * 
 * @author Enkhbold Nyamsuren
 */
public class VADClusteringImpl extends TechniqueImpl implements VADClustering {
	
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
	private int mhappy = 0;
	
	/**
	 * contain the number of total blog texts with angry mood
	 */
	private int mangry = 0;
	
	/**
	 * contain the number of total blog texts with fear mood
	 */
	private int mfear = 0;
	
	/**
	 * contain the number of total blog texts with sad mood
	 */
	private int msad = 0;
	
	/**
	 * Default constructor for class.
	 * Calls constructor of super class to create object of document parser
	 * @param handler	reference tp the MainWindow object which calls this method
	 * @see TechniqueImpl
	 */
	public VADClusteringImpl(MainWindow handler) {
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
	 * @param blogFile 	object of a File to read 
	 * @see Anew
	 * @see Parser
	 */
	public void runTechnique(File blogFile) {

		if(blogFile.isDirectory()){
			String[] children;
			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return !name.startsWith(".");
				}
			};
			
			windowHandler.printOutput("Classifying files in directory " + blogFile.getPath() + " with VAD Clustering method:\n");
			children = blogFile.list(filter);

			for (int l=0; l<children.length; l++){
				System.out.println(children[l] + ", processing... ");
				classify(new File(blogFile.getPath() + "\\" + children[l]));				
			}
		} else{
			windowHandler.printOutput("Classifying document " + blogFile.getName() + "\n");
			classify(blogFile);			
		}
		
		windowHandler.printOutput("Result set (number of documents for each class):\n");
		windowHandler.printOutput("Method used: VAD Clustering");
		windowHandler.printOutput("	Happy: " + mhappy);
		windowHandler.printOutput("	Sad: " + msad);
		windowHandler.printOutput("	Angry: " + mangry);
		windowHandler.printOutput("	Fear: " + mfear + "\n\n");
	}
	
	/**
	 * this method is actual implementation of the VADClustering method. It parses 
	 * the blog file and retrieves corresponding Anew words from anew list. This part was done
	 * by code implemented by Francis. 
	 * For the next part it retrieves certain anew words that satisfy condition to 
	 * express the class of current blog.
	 * Finding those terms it calculates their weight and assigns blog into certain class
	 * depending on the value of the weight
	 *  
	 * @param blogFile	object of a File to read 
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
	
		int neg = 0;
		int pos = 0;
		float weight = 0;
		for(int i=0; i<anewArray.size(); i++){
			Anew curTerm = ((Anew)anewArray.get(i));
			
			if(curTerm.valence < 5)	++neg;
			else if(curTerm.valence > 5 && curTerm.arousal > 5 && curTerm.dominance > 5) ++pos;
		}
		if(pos != 0){
			weight = (float)neg/pos;
		} else weight = neg;

		int calm = 0;
		int arousal = 0;
		float aweight = 0;
		for(int i=0; i<anewArray.size(); i++){
			Anew curTerm = ((Anew)anewArray.get(i));
			
			if(curTerm.valence < 5){ 
				if(curTerm.arousal < 5)	++calm;
				else if(curTerm.arousal > 5) ++arousal;
			}
		}
		if(arousal != 0){
			aweight = (float)calm/arousal;
		} else aweight = calm;

		int fear = 0;
		int anger = 0;
		float faweight = 0;
		for(int i=0; i<anewArray.size(); i++){
			Anew curTerm = ((Anew)anewArray.get(i));
			
			if(curTerm.valence < 5 && curTerm.arousal > 5){ 
				if(curTerm.dominance < 5)	++fear;
				else if(curTerm.dominance > 5) ++anger;
			}
		}
		if(fear != 0){
			faweight = (float)anger/fear;
		} else faweight = anger;		
		
		if(weight < 0.4){
			mhappy++;
		} else{
			if(aweight >= 1){
				msad++;
			} else{
				if(faweight >= 0.5){
					mangry++;
				} else {
					mfear++;
				} 
			}
		} 
	}
}