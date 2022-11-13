package src.classification.mood;

/**
 * Container data structure for one term and its corresponding attribute values
 * 
 * @author Francisco Rojas
 */
public class Anew {
	/**
	 * contains the description of the term
	 */
	public String description;
	/**
	 * contains valence value of the term
	 */
	public float valence;
	/**
	 * contains arousal value of the term
	 */
	public float arousal;
	/**
	 * contains dominance value of the term
	 */
	public float dominance;
	/**
	 * contains frequency value of the term which is given in ANEW list
	 */
	public int frequency;
	/*public void println(){
		System.out.printf(description+'\t'+valence+'\t'+arousal+'\t'+dominance+'\t'+frequency+'\n');
	}*/
}
