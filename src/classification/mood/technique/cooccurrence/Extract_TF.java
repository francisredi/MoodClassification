package src.classification.mood.technique.cooccurrence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Extract_TF class is responsible for extracting particular TF values of the ANEW terms 
 * which have been found in the blog. 
 * @param term_id is an array which contains terms id in the ANEW list.
 * @return assumed mood value of blog. 
 * @author Vasily Nelyudov
 */


public class Extract_TF {
	public void classifyDocByTermCooccurences(int[]term_id){
				
		String line = null;
		
		// Fist allocate judgment_table which will store all tf value from different mood corpuses for a blog
				
		int rows = ((term_id.length - 1) * term_id.length) / 2; // number of rows depends of a number of term pairs (combination without repetition)
		
		int[][] judgment_table = new int[rows][6];		

		String[] file = {
				"D:\\Eclipse_workspace\\mood\\deploy\\angryCooccurrenceMatrix.txt",
                "D:\\Eclipse_workspace\\mood\\deploy\\happyCooccurrenceMatrix.txt",
				"D:\\Eclipse_workspace\\mood\\deploy\\sadCooccurrenceMatrix.txt",
				"D:\\Eclipse_workspace\\mood\\deploy\\fearCooccurrenceMatrix.txt"
			};

		// read all matrix into the memory
		try {	
			BufferedReader br;
			
			int sum, N;
				for (int i = 0; i < file.length ; i++)    // do for each co-occurence matrix
				{
					int u = -1;
					br = new BufferedReader(new FileReader(file[i]));
					line = br.readLine();
					
					// 			
					for (int h = 0; h < term_id.length ; h++ )
						for (int b = h + 1; b < term_id.length ; b++ )
							{								
								// first element should be with the highest index number, it's made due to the triangle matrix structure.
								if (term_id[h] > term_id[b]){
									term_id[b] = term_id[b] - term_id[h];
									term_id[h] = term_id[b] + term_id[h];
									term_id[b] = term_id[h] - term_id[b];
								}	
								sum = 0; // this value is used to found out a paricular tf in the text file  
								N = 0;  // index of the first tf value in the txt file 

								for (int j = 0; j < term_id[b] ;j++)            // calculation of tf value position in the co-occurence matrix file
									sum += j;
								N = (sum + term_id[h]) * 5; 

								String string = line.substring(N,N+4);
								
								int value = Integer.parseInt(string,10);         // conver string into integer for future comparing
																
								u += 1;   // this is a row sweatcher
								
								judgment_table[u][0] = term_id[b];
								judgment_table[u][1] = term_id[h];
								judgment_table[u][i + 2] = value;								
							}
				}
			}	
			catch (IOException ioe){
			System.err.println(ioe);
			}
			
			// Print results
			
			System.out.println("		      Term Y	  Term X	  Angry       Happy        Sad        Fear");
			System.out.println("              ----------------------------------------------------------------");

			int count = 0;
			for (int h = 0; h < judgment_table.length ; h++)
			{
				count += 1;
				System.out.print("	" + count + ")");
				for (int  b = 0; b < judgment_table[0].length ; b++ )
					System.out.print("	  	    " +  judgment_table[h][b]);
				System.out.println();
			}
			System.out.println("              ----------------------------------------------------------------");
			
			
			
	}

}
