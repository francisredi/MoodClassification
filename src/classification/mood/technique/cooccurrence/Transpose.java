package src.classification.mood.technique.cooccurrence;

/*
  Constructing of transpose matrix using initial
*/
/**
 * Transpose class is responsible for constructing transpose matrix from initional one.
 * @param initial_m a documents-terms initial matrix.
 * @author Vasily Nelyudov
 */

public class Transpose
{
	/**
	 * Constructing transpose matrix from initional one.
	 * @param initial_m - a documents-terms initial matrix.
	 * @return build_transpose - a constructed transpose matrix.
	 */
	
public static int[][] build_transpose(int[][] initial_m)
{
int tr_col = initial_m.length;
int tr_row = initial_m[0].length;
int[][] transpose_m = new int [tr_row][tr_col];

// start transpose process
  for (tr_row = 0; tr_row < initial_m[0].length; tr_row++)
         {
             for (tr_col = 0; tr_col < initial_m.length; tr_col++)
                transpose_m[tr_row][tr_col] = initial_m[tr_col][tr_row];   
         }
   return transpose_m;
}
}