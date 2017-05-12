import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class OpenPreset
{
    int [][] board;
    int N;

    public OpenPreset (File file)
    {
        try
        {
            // open file stream
            BufferedReader br = new BufferedReader (new FileReader (file));

            N = Integer.parseInt (br.readLine ()); // get N
            this.board = new int [N][N]; // since size is already available, initialize the 2d array

            // traverse the remainder of the file
            for (int i = 0; i < N; i++)
            {
                // tokenize the line
                String[] breakLine = br.readLine ().split (" ");
                for (int j = 0; j < N; j++)
                {
                    // add to board
                    this.board [i][j] = Integer.parseInt (breakLine [j]);
                }
            }

            // close file stream
            br.close ();
        }
        // catch IOException
        catch (IOException ioe)
        {
            ioe.printStackTrace ();
        }
    }

    // return size
    public int getSize ()
    {
        return this.N;
    }

    // return the board
    public int [][] getBoard ()
    {
        return this.board;
    }
}
