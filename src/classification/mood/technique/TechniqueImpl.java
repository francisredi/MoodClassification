package src.classification.mood.technique;

import java.io.File;
import java.util.ArrayList;

import src.classification.mood.Parser;
import src.classification.mood.Replacer;

/**
 * This class implements the interface <code>Technique</code>. It provides a common method for all techniques to run their own
 * separate programs. It also prevents cloning. Creating an instance using this class is not allowed.
 * 
 * The class contains an emoticons array and a parser which is common to all techniques for all interfaces.
 * Information about the location of the ANEW list and emoticons files are also provided.
 * 
 * @author Francis
 */
public abstract class TechniqueImpl implements Technique{
	
	/**
	 * contains list of emoticons that should be replaced if found in blog text
	 */
	protected static ArrayList<Replacer> emoticonsArray; // contains emoticons and their English word equivalents
	
	/**
	 * reference to object of Parser class
	 */
	protected static Parser parser;
	
	/**
	 * contains the path to textual file containing ANEW list
	 */
	protected static String anewtext = "..\\classes\\classification\\mood\\anew-androgynous.txt";
	
	/**
	 * contains the path to textual file containing list of emoticons
	 */
	protected static String emoticonstext = "..\\classes\\classification\\mood\\emoticons.txt";
	
	/**
	 * The constructor for the class which allocates memory for a single Parser and a single emoticons replacer array.
	 */
	// written by Francisco Rojas
	public TechniqueImpl(){
		if(parser == null){
			parser = new Parser(); // instantiate a parser
		}
		// first read all the emoticons and their English word equivalents into memory just once
		if(emoticonsArray == null){
			emoticonsArray = new ArrayList<Replacer>();
			String error = parser.readEmoticonsIntoMemory(emoticonstext,emoticonsArray);
			if (error != null) { // if get a string back, there was an error
				System.out.println(error);
				return;
			}
		}
	}

	/**
	 * The real main function (routine) for the mood classification using a certain technique which is selected polymorphically.
	 */
	// written by Francisco Rojas
	public abstract void runTechnique(File blogFile);
	
	/**
	 * Prevent cloning
	 * @throws CloneNotSupportedException
	 */
	// written by Francisco Rojas
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}

}