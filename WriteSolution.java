/*
*   WriteSolution.java
*       writes a solution set in a text file
*/

// import dependencies
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class WriteSolution
{
    // constructor
    public WriteSolution (LinkedList <int [][]> solutionSet, int N)
    {
        // buffered writer needs to catch IOException
        try
        {
            // creates an output stream using buffered writer
            BufferedWriter bw = new BufferedWriter (new FileWriter (new File ("output.txt")));

            // variable for solution counter
            int counter = 0;

            // for each solution board
            for (int [][] solution : solutionSet)
            {
                // increment counter
                counter ++;

                // write string
                bw.write (new String ("Solution # " + counter + "\n"));

                // traverse the board to properly print the contents of the board
                for (int i = 1; i < N+1; i++)
                {
                    for (int j = 1; j < N+1; j++)
                    {
                        bw.write (solution [i][j] + " ");
                    }
                    bw.write ("\n");
                }
                bw.write ("===========\n");
            }

            // close the output stream
            bw.close ();
        }
        catch (IOException ioe)
        {
            // print the stack trace if error is encountered
            ioe.printStackTrace ();
        }
    }
}
