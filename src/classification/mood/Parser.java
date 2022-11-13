package src.classification.mood;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * The parser is responsible for the parsing of various files:
 * <ul>
 * <li> The file containing the ANEW terms and corresponding values.
 * <li> The file containing the emoticons and their English word equivalents.
 * <li> The file containing the blog text.
 * <ul>
 * <p>
 * Depending on the situation, data parsed from the files are inserted into data structures which are then added into an array list,
 * or term frequencies are calculated and inserted into a matrix. The blog text is prepared for classification handling. See method
 * descriptions for details.
 * 
 * @author Francisco Rojas
 */
public class Parser{
	
	/**
	 * The negation handling of sentences in a blog text (part of the 4th stage of the mood classification task).
	 * @param sb  the string buffer which contains the blog text
	 */
	// written by Francisco Rojas
	public void handleNegationInSentences(StringBuffer sb){
		String blogText = sb.toString();
		blogText = blogText.replaceAll("n't[\\W]"," not "); // replace contraction using 'not' to entire word
		blogText = blogText.replaceAll("[\\W]not[\\W][A-Za-z]+[\\W]"," ");
		sb.replace(0,sb.length()-1,"");
		sb.append(blogText);
	}

	/**
	 * After reading the blog into memory, this method collects the term frequency for each ANEW term in a particular document (blog file) and puts it in the initial matrix used for term co-occurrences.
	 * @param file            the blog file to be used for scanning ANEW terms and their term frequencies
	 * @param emoticonsArray  the emoticons array which will replace emoticons in blog text to English text form
	 * @param anewArray       the ANEW vocabulary used to retrieve the desired terms and their term frequencies in the blog text
	 * @param large_m         the initial matrix to be used for term co-occurrences algorithm (dimensions: Terms x Document Ids, slots are term frequencies)
	 * @param docNum          the current column being processed in the matrix which corresponds to the document (blog file) being used
	 */
	// written by Francisco Rojas
	public void getTermFrequenciesForFile(File file,ArrayList<Replacer> emoticonsArray,ArrayList<String> anewArray,int large_m[][],int docNum){
		StringBuffer sb = new StringBuffer();
		// First read the blog into memory
		String error = readBlogIntoMemory(sb,file,emoticonsArray);
		
		int size = anewArray.size();
		// get the term frequencies for each term (the row) in the matrix for a particular column docNum in the matrix using the blog file
		for(int i = 0; i < size; i++){
			String term = anewArray.get(i);
			String blog = sb.toString();
			String[] blogsplit = blog.split("[\\W]"+term+"[\\W]"); // non-word character before and after to make sure count term correctly
			large_m[i][docNum] = blogsplit.length - 1; // assign term frequency in this slot
		}
		
		if(error!=null){ // if get a string back, there was an error
			System.out.println(error);
			return;
		}
	}
	
	/**
	 * Reads the emoticons and their English word equivalents into an array.
	 * @param fileName        the path and filename in which the emoticons and English word equivalents are located
	 * @param emoticonsArray  initially an empty array of emoticons and their corresponding English word equivalents (represented in a container <code>Replacer</code>)
	 * @return null or String (if error occured)
	 */
	// written by Francisco Rojas
	public String readEmoticonsIntoMemory(String fileName, ArrayList<Replacer> emoticonsArray){
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
			String line = br.readLine();
			while(line != null){
				// parse the read line, load information into Replacer data structure
				StringTokenizer st = new StringTokenizer(line,"\t");
				Replacer replacer = new Replacer();
				replacer.original = (String) st.nextElement(); // column 1 - emoticon
				replacer.replacement = (String) st.nextElement(); // column 2 - English word
				emoticonsArray.add(replacer);
				line = br.readLine(); // get the next line
			}
			br.close();
		}
		catch(FileNotFoundException fnfe){
			return "The emoticons file "+fileName+" does not exist!";
		}
		catch(IOException ioe){
			return "An I/O exception occured while reading the emoticons file!";
		}
		return null;
	}
	
	/**
	 * Reads all the ANEW terms into an array.
	 * @param fileName   the path and filename in which the ANEW terms are located
	 * @param anewArray  the array which will contain the ANEW terms
	 * @return null or String (if error occured)
	 */
	// written by Francisco Rojas
	public String readAllANEWWordsIntoMemory(String fileName, ArrayList<String> anewArray){
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
			String line = br.readLine();
			while(line != null){
				// parse the read line, load information into Anew data structure
				StringTokenizer st = new StringTokenizer(line);
				String description = (String) st.nextElement(); // column 1 - description
				anewArray.add(description);
				line = br.readLine(); // get the next line
			}
			br.close();
		}
		catch(FileNotFoundException fnfe){
			return "The ANEW file "+fileName+" does not exist!";
		}
		catch(IOException ioe){
			return "An I/O exception occured while reading ANEW file!";
		}
		return null;
	}

	/**
	 * Store only the ANEW terms found in the blog in the ANEW arraylist with their corresponding valence, arousal, and dominance values.
	 * @param sb         will contain the blog text
	 * @param fileName   the path and filename of the blog file
	 * @param anewArray  the array which will hold the ANEW information
	 * @return null or String (if error occured)
	 */
    // written by Francisco Rojas
	public String readANEWIntoMemory(StringBuffer sb, String fileName, ArrayList<Anew> anewArray){
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
			String line = br.readLine();
			while(line != null){
				// parse the read line, load information into Anew data structure
				StringTokenizer st = new StringTokenizer(line);
				String description = (String) st.nextElement(); // column 1 - description
				sb.toString().split("[\\W]"+description+"[\\W]");
				// is this ANEW word in the blog text? If so, add to array.
				if(sb.toString().split("[\\W]"+description+"[\\W]").length > 1){
					Anew anew = new Anew();
					anew.description =  description;
					st.nextElement();                             // don't care about column 2 - word no.
					anew.valence = Float.valueOf((String) st.nextElement());   // column 3 - valence mean
					st.nextElement();                             // don't care about column 4 - valence mean SD
					anew.arousal = Float.valueOf((String) st.nextElement());   // column 5 - arousal mean
					st.nextElement();                             // don't care about column 6 - arousal mean SD
					anew.dominance = Float.valueOf((String) st.nextElement()); // column 7 - dominance mean
					/*st.nextElement();                             // don't care about column 8 - dominance mean SD
					anew.frequency = Integer.valueOf((String) st.nextElement()); // column 9 - word frequency*/
					anewArray.add(anew);
				}
				line = br.readLine(); // get the next line
			}
			br.close();
		}
		catch(FileNotFoundException fnfe){
			return "The ANEW file "+fileName+" does not exist!";
		}
		catch(IOException ioe){
			return "An I/O exception occured while reading ANEW file!";
		}
		return null;
	}

	/**
	 * Reads the blog text from the blog file and puts it in a string buffer.
	 * It also prepares the blog text by converting emoticons into English words and lower-casing all the letters.
	 * @param sb              will hold the blog text
	 * @param file            the blog file
	 * @param emoticonsArray  the array of emoticons and their English equivalents
	 * @return null or String (if error occured)
	 */
    // written by Francisco Rojas
	public String readBlogIntoMemory(StringBuffer sb, File file, ArrayList<Replacer> emoticonsArray){
		
		try{
			sb.append(" ");
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = br.readLine();
			while(line != null){
				sb.append(line+" ");
				line = br.readLine(); // get the next line
			}
			br.close();
			sb.append(" ");

			String blogText = sb.toString();
			// replace emoticons with words (if any)
			int size = emoticonsArray.size();
			for(int i = 0; i < size; i++){
				Replacer rep = (Replacer) emoticonsArray.get(i);
				blogText = blogText.replace(rep.original,rep.replacement);
			}
			//blogText = blogText.replaceAll("[^A-Za-z'\\s]", "").toLowerCase(); // remove punctuation, lower case
			// NOTE: THE PUNCTUATION IS NEEDED SO TERM FREQUENCY IS COUNTED CORRECTLY!
			blogText = blogText.toLowerCase(); // lower case
			sb.replace(0,sb.length()-1,"");
			sb.append(blogText);
		}
		catch(FileNotFoundException fnfe){
			return "The blog file "+file.getName()+" does not exist!";
		}
		catch(IOException ioe){
			return "An I/O exception occured while reading blog file!";
		}
		return null;
	}

}