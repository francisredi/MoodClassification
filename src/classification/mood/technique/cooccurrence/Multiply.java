package src.classification.mood.technique.cooccurrence;


/**
 * Multiply two matrices.
 * This program recieve 4 initial matrices, then construct 4 transpose matrices, and finaly multiply them into cooccurances
 * matrices.
 * */
import java.io.*;
import java.text.*;

/**
 * Multiply class is responsible for multiplying documents-terms matrix
 *  and creating an co-occurance triangle matrix. 
 * @param m1 a documents-terms matrix.
 * @param String a file name where future constructed co-occurence matrix will be stored.
 * @author Vasily Nelyudov
 */

public class Multiply
{
	 public static void multiply(int[][] m1, String filename)
	 //public static int[][] multiply(int[][] m1, String filename)
	 {
	   // call transpose function to construct transpose version of initial matrix m1

 	   int m1_transp[][] = Transpose.build_transpose(m1);

	   int N = m1_transp[0].length;
  
	   // inticialize co-occurence triangle matrix
	   int[][] result = new int[N][];
	   for (int n = 0; n < N; n++)
	     result[n] = new int[n+1];

		 // creating triangle matrix and wtiring it into a file
		try {
			 PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
			 DecimalFormat df = new DecimalFormat("0000");
			 
			 // create triangle matrix by multiplying intial and transpose matrices
				for (int i=0; i<N-1; i++)
				{
				   for (int j=0; j<i+1; j++)
				   {
					  for (int k=0; k<m1_transp.length; k++)
						  result[i][j] += m1[i+1][k] * m1_transp[k][j];
					  out.print(df.format(result[i][j]) + " ");
				   }
				}
			 out.close();
			 }
		 catch (IOException e) {        }
	
		//return result;
     }
}