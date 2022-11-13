package src.classification.mood.technique;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import src.classification.mood.MainWindow;
import src.classification.mood.technique.cooccurrence.Multiply;

/**
 * This class implement co-occurrence technique
 */
public class TermCooccurrencesImpl extends TechniqueImpl implements TermCooccurrences{
	/**
	 * Array contains all words from ANEW list
	 */
	protected ArrayList<String> anewArray;
	/**
	 * This matrix contain all tf values for the doc of angry corpus. Rows are terms, columns are documents.
	 */
	protected int angry_m[][] = null;
	/**
	 * This matrix contain all tf values for the doc of fear corpus. Rows are terms, columns are documents.
	 */
	protected int fear_m[][] = null;
	/**
	 * This matrix contain all tf values for the doc of happy corpus. Rows are terms, columns are documents.
	 */
	protected int happy_m[][] = null;
	/**
	 * This matrix contain all tf values for the doc of sad corpus. Rows are terms, columns are documents.
	 */
	protected int sad_m[][] = null;	
	/**
	 * this array contains ids of the terms which are present in the blog
	 */
	private int[] terms_id; // this array contains ids of the terms which are present in the blog
	/**
	 * reference to main window
	 */
	private MainWindow windowHandler;
	/**
	 * reference to main window
	 */
	public TermCooccurrencesImpl(MainWindow handler){
		super();
		windowHandler = handler;
		anewArray = new ArrayList<String>();
	}
	
	/**
     * Find ANEW words indide analyzed document
     * @param sb		containing analyzed document
     */
	
	public void setupArrayOfAnewTermIds(StringBuffer sb){
		int size = anewArray.size();
		String blogText = sb.toString();
		int terms_idCurr = 0;
		for(int i = 0; i < size; i++){
			if(blogText.split("[\\W]"+anewArray.get(i)+"[\\W]").length > 1){ // if ANEW word is present in blog text
				terms_id[terms_idCurr] = i; // add term id to terms_id array
				terms_idCurr++;
			}
		}
	}
	/**
     * Scan directory with training documents using half of them. While looking througth the folder call parser.getTermFrequenciesForFile
     * @param directory		the path where training document are stores;
     * @param fileFilter	using for filtering docunents inside particular folder
     * @return constructed matrix
     * @see parser
     */
	
	public int scanDirectory(String directory, FileFilter fileFilter, char matrix){
		File dir = new File(directory);
		
		File[] files = dir.listFiles(fileFilter);
		int numDocs = 250;//files.length / 2; // sample data
		final int numTerms = anewArray.size();
		if(numDocs == 0){
			// directory does not exist or is not a directory
		}else{
			int large_m[][] = new int[numTerms][numDocs]; // create matrix to hold term frequencies
			for(int docNum = 0; docNum < numDocs; docNum++){
				parser.getTermFrequenciesForFile(files[docNum],emoticonsArray,anewArray,large_m,docNum);
			}
			switch(matrix){
				case 'a':
					angry_m = large_m;
					break;
				case 'f':
					fear_m = large_m;
					break;
				case 'h':
					happy_m = large_m;
					break;
				default:
					sad_m = large_m;
			}
		}
		return numDocs;
	}

	/**
	 * This method runs a main program for term co-occurences technique
	 */
	
	public void runTechnique(File blogFile) {
		
		// locations of corpuses
		/** TODO: DO NOT FORGET THIS */
		String basePath ="d:/MoodCorpus/";
		String[] pathArray = new String[4];
		pathArray[0] = "angry";
		pathArray[1] = "happy";
		pathArray[2] = "sad";
		pathArray[3] = "fear";
		
		// first read all the emoticons and their English word equivalents into memory just once
		String error = parser.readEmoticonsIntoMemory(emoticonstext,emoticonsArray);

		// read all anew vocab into an array in memory
		if(error==null) error = parser.readAllANEWWordsIntoMemory(anewtext,anewArray);
		
		if(error != null){
			System.out.println(error);
			return;
		}
		
		// This filter only returns non-directories
	    FileFilter fileFilter = new FileFilter() {
	        public boolean accept(File file) {
	            return !file.isDirectory();
	        }
	    };

	    final int numTerms = anewArray.size();
	    XMLCreator creator = new XMLCreator();
	    
	    System.out.println("Processing angry lists...");
	    creator.createXML(pathArray[0] + "Matrix.xml", anewArray);	    
	    final int numAngryDocs = scanDirectory(basePath + pathArray[0],fileFilter,'a'); // 23 seconds time
	    FrequencyMatrixBuilder myhandler = new FrequencyMatrixBuilder(pathArray[0] + "Matrix.xml", angry_m, numTerms, numAngryDocs);
	    myhandler.writeMatrix();
	    
	    System.out.println("Finished.\n\nProcessing fear lists...");
	    creator.createXML(pathArray[3] + "Matrix.xml", anewArray);	 
	    final int numFearDocs = scanDirectory(basePath + pathArray[3], fileFilter,'f');
	    myhandler = new FrequencyMatrixBuilder(pathArray[3] + "Matrix.xml", fear_m, numTerms, numFearDocs);
	    myhandler.writeMatrix();
	    
	    System.out.println("Finished.\n\nProcessing happy lists...");
	    creator.createXML(pathArray[1] + "Matrix.xml", anewArray);	 
	    final int numHappyDocs = scanDirectory(basePath + pathArray[1], fileFilter,'h');
	    myhandler = new FrequencyMatrixBuilder(pathArray[1] + "Matrix.xml", happy_m, numTerms, numHappyDocs);
	    myhandler.writeMatrix();
	    
	    System.out.println("Finished.\n\nProcessing sad lists...");
	    creator.createXML(pathArray[2] + "Matrix.xml", anewArray);	 
	    final int numSadDocs = scanDirectory(basePath + pathArray[2], fileFilter,'s');
	    myhandler = new FrequencyMatrixBuilder(pathArray[2] + "Matrix.xml", sad_m, numTerms, numSadDocs);
	    myhandler.writeMatrix();
	    myhandler = null;
	    
	    System.out.println("Finished");
	    
	    // Creating training data
	    
	    try{
	    	// Vasily's training code. This code should be stated one time.
	    	// how to use only one large_m[][] matrix instead of 4 different ones?
	    	// create angry cooccurrence matrix
	    	System.out.println("Creating coocurrence matrix of angry corpus training data...");
	    	int large_m_angry[][] = createMatrix(pathArray[0] + "Matrix.xml");
		    Multiply.multiply(large_m_angry,pathArray[0] + "CooccurrenceMatrix.txt");
		    System.out.println("Finished.\n\n Creating coocurrence matrix of happy corpus training data...");
		    // create happy cooccurrence matrix
		    int large_m_happy[][] = createMatrix(pathArray[1] + "Matrix.xml");
		    Multiply.multiply(large_m_happy,pathArray[1] + "CooccurrenceMatrix.txt");
		    System.out.println("Finished.\n\n Creating coocurrence matrix of sad corpus training data...");
		    // create sad cooccurrence matrix
		    int large_m_sad[][] = createMatrix(pathArray[2] + "Matrix.xml");
		    Multiply.multiply(large_m_sad,pathArray[2] + "CooccurrenceMatrix.txt");
		    System.out.println("Finished.\n\n Creating coocurrence matrix of fear corpus training data...");
		    // create fear cooccurrence matrix
		    int large_m_fear[][] = createMatrix(pathArray[3] + "Matrix.xml");
		    Multiply.multiply(large_m_fear,pathArray[3] + "CooccurrenceMatrix.txt");
		    System.out.println("Finished.");
	    } catch(Exception e){}
	}
	
		// File analysis
		/*
				
				
		
		 Ectract_TF(terms_id);
		*/
	/**
     * Create initial matrix and put it into xml file
     * @param path		the path and filename in which the co-occurrence matrix is located
     * @return constructed matrix
     */
	
	private int[][] createMatrix(String path) throws Exception{
		XMLDBhandler myhandler = new XMLDBhandler();
		
		myhandler.setXMLDBPath(path);
		return myhandler.createMatrix();
	}

}