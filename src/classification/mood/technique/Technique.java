package src.classification.mood.technique;

import java.io.File;

/**
 * This is the root interface class for a general classification technique.
 * 
 * @author Francis
 */
public interface Technique {
	
	/**
	 * This method runs a main program for a particular classification technique
	 * @param blogFile  the path and filename of the blog document
	 */
	// written by Francisco Rojas
	public void runTechnique(File blogFile);	//****** modified parameter by Egi

}