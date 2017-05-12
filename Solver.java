import java.util.LinkedList;

public class Solver
{
    private int [][] inputBoard;
    private static LinkedList <int [][]> blankSolutionSet, currSolutionSet;

    public Solver (int [][] inputBoard, int N)
    {
        blankSolutionSet = new LinkedList <> ();
        currSolutionSet = new LinkedList <> ();

        this.inputBoard = new int [N][N];

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                this.inputBoard [i][j] = inputBoard [i][j];

        permutation (N);

        System.out.println ("Total solutions: " + blankSolutionSet.size () + " Actual Solutions: " + currSolutionSet.size ());
    }

    public LinkedList <int [][]> getBlankSolutionSet ()
    {
        return this.blankSolutionSet;
    }

    public LinkedList <int [][]> getActualSolutionSet ()
    {
        return this.currSolutionSet;
    }

    private void permutation (int N)
    {
        int start, move;
        int [] nopts = new int [N+2];
        int [][] option = new int [N+2][N+2];
        int i, j, candidate;

        for (i = 0; i < N+2; i++)
            nopts [i] = 0;

        for (i = 0; i < N+2; i++)
            for (j = 0; j < N+2; j++)
                option [i][j] = 0;

        move = start = 0;
        nopts [start] = 1;

        while (nopts [start] > 0)
        {
            if (nopts [move] > 0)
            {
                move ++;
                nopts [move] = 0;

                if (move == N+1)
                {
                    LinkedList <Integer> arrAnswer = new LinkedList <> ();
                    for (i = 0; i < move; i++)
                        arrAnswer.add (option [i][nopts [i]]);
                    solveChancellor (arrAnswer, N);
                }
                else if (move == 1)
                {
                    for (candidate = N; candidate >= 1; candidate --)
                    {
                        nopts [move] ++;
                        option [move][nopts [move]] = candidate;
                    }
                }
                else
                {
                    for (candidate = N; candidate >= 1; candidate --)
                    {
                        for (i = move - 1; i >= 1; i--)
                            if (candidate == option [i][nopts [i]])
                                break;

                        if (!(i >= 1))
                            option [move][++nopts [move]] = candidate;
                    }
                }
            }
            else
            {
                move --;
                nopts [move] --;
            }
        }
    }

    private void solveChancellor (LinkedList <Integer> arrAnswer, int N)
    {
        int[][] board = new int [N+2][N+2];

        for (int i = 0; i < N+2; i++)
            for (int j = 0; j < N+2; j++)
                board [i][j] = 0;

        for (int i = 1; i < N+1; i++)
            board [arrAnswer.indexOf (arrAnswer.get (i))][arrAnswer.get (i)] = 1;

        boolean aSolution = true;

        for (int i = 1; i < N+1; i++)
        {
            for (int j = 1; j < N+1; j++)
            {
                if (board [i][j] == 1)
                {
                    if ((1 <= (i-2) && (i-2) <= N+1) && (1 <= (j-1) && (j-1) <= N+1))
                    {
                        if (board [i-2][j-1] == 1)
                        {
                            aSolution = false;
                            break;
                        }
                    }

                    if ((1 <= (i+2) && (i+2) <= N+1) && (1 <= (j+1) && (j+1) <= N+1))
                    {
                        if (board [i+2][j+1] == 1)
                        {
                            aSolution = false;
                            break;
                        }
                    }

                    if ((1 <= (i+1) && (i+1) <= N+1) && (1 <= (j+2) && (j+2) <= N+1))
                    {
                        if (board [i-1][j+2] == 1)
                        {
                            aSolution = false;
                            break;
                        }
                    }

                    if ((1 <= (i+1) && (i+1) <= N+1) && (1 <= (j-2) && (j-2) <= N+1))
                    {
                        if (board [i+1][j-2] == 1)
                        {
                            aSolution = false;
                            break;
                        }
                    }

                    if ((1 <= (i-2) && (i-2) <= N+1) && (1 <= (j+1) && (j+1) <= N+1))
                    {
                        if (board [i-2][j+1] == 1)
                        {
                            aSolution = false;
                            break;
                        }
                    }

                    if ((1 <= (i+2) && (i+2) <= N+1) && (1 <= (j-1) && (j-1) <= N+1))
                    {
                        if (board [i+2][j-1] == 1)
                        {
                            aSolution = false;
                            break;
                        }
                    }

                    if ((1 <= (i+1) && (i+1) <= N+1) && (1 <= (j+2) && (j+2) <= N+1))
                    {
                        if (board [i+1][j+2] == 1)
                        {
                            aSolution = false;
                            break;
                        }
                    }

                    if ((1 <= (i-1) && (i-1) <= N+1) && (1 <= (j-2) && (j-2) <= N+1))
                    {
                        if (board [i-1][j-2] == 1)
                        {
                            aSolution = false;
                            break;
                        }
                    }
                }

                if (!aSolution)
                    break;
            }
        }

        if (aSolution)
        {
            // add solution to blank board
            blankSolutionSet.add (board);

            // check if this is a solution to the actual input board
            int [][] solutionToInput = new int [N+2][N+2];

            for (int i = 0; i < N+2; i++)
                for (int j = 0; j < N+2; j++)
                    solutionToInput [i][j] = 0;

            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    if (this.inputBoard [i][j] == 1)
                        solutionToInput [i+1][j+1] = 1;

            boolean bFoundSolutionWithGiven = false, bShouldStop = false;

            for (int i = 0; i < N+2; i++)
            {
                for (int j = 0; j < N+2; j++)
                {
                    if (solutionToInput [i][j] == 1)
                    {
                        if (board [i][j] == 1)
                        {
                            bFoundSolutionWithGiven = true;
                        }
                        else
                        {
                            bFoundSolutionWithGiven = false;
                            bShouldStop = true;
                            break;
                        }
                    }
                }
                if (bShouldStop)
                    break;
            }

            if (bFoundSolutionWithGiven)
            {
                currSolutionSet.add (board);
            }

        }
    }
}
