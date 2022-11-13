package src.classification.mood.technique;

/**
 * This class is used by Word Frequency method to temprorary
 * store information about term read from data files. The information stored:
 *  - the description of the term
 *  - VAD values
 *  - Document frequencies of the term in each of the corpuses
 * 
 * @author Enkhbold Nyamsuren
 */
public class Term {
	
	/**
	 * description of the term
	 */
	public String description;
	
	/**
	 * valence value of the term
	 */
	public float valence;
	
	/**
	 * arousal value of the term
	 */
	public float arousal;
	
	/**
	 * dominance value of the term
	 */
	public float dominance;
	
	/**
	 * contains the weight of the term for happy class
	 */
	public float happy;
	
	/**
	 * contains the weight of the term for angry class
	 */
	public float angry;
	
	/**
	 * contains the weight of the term for fear class
	 */
	public float fear;
	
	/**
	 * contains the weight of the term for sad class
	 */
	public float sad;
	
	/**
	 * default constructor
	 *
	 */
	public Term(){}
	
	/**
	 * Constructor used to initialize all values in a class
	 * @param descr	description of the class
	 * @param val	valence value
	 * @param ar	arousal value
	 * @param domin	dominance value
	 * @param happy	document frequency in happy corpus
	 * @param angry	document frequency in angry corpus
	 * @param fear	document frequency in fear corpus
	 * @param sad	document frequency in sad corpus
	 */
	public Term(String descr, float val, float ar, float domin, float happy, float angry, float fear, float sad){
		description = descr;
		valence = val;
		arousal = ar;
		dominance = domin;

		this.happy = happy;
		this.angry = angry;
		this.fear = fear;
		this.sad = sad;
	}
	
	/**
	 * this method prints out the description of the term and its VAD values into the console
	 *
	 */
	public void println(){
		System.out.println(description+'\t'+valence+'\t'+arousal+'\t'+dominance);
	}
}
