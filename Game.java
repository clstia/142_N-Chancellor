/*
* Game.java
*   Represents a current game state. It is the whole playable interface with basic checkers for incorrectly placed chancellors.
*   Functions such as solve, clear board, and exit are included here.
*/

// import dependencies here
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

// opted to extend JFrame since it will be the main container and it will be easier to access certain variables
public class Game extends JFrame implements WindowListener
{
    // variables needed
    private JButton[][] buttonArray; // represents the board in the interface
    private int[][] boardArray; // represents the board in the backend
    private JFrame self, parent; // needed to hide some frames

    // constructor for blank board
    public Game (int size, JFrame parent)
    {
        this.parent = parent; // add a reference to parent
        this.self = this; // add a reference to self
        parent.setVisible (false); // hide parent
        setContentPane (createBlankBoard (size)); // create the board given a certain size
        setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE); // allow window listener to handle closing operation
        addWindowListener (this); // add a window listener
        setTitle (new String ("N Chancellors - Board Size " + size)); // set title. add the current size of the board for clarity
        pack (); // compress the elements on the frame
        setVisible (true); // display frame
    }

    // constructor for preset game
    public Game (int [][] board, int size, JFrame parent)
    {
        this.parent = parent;
        this.self = this;
        parent.setVisible (false);
        setContentPane (createFromPreset (size, board));
        setDefaultCloseOperation (JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener (this);
        setTitle (new String ("N Chancellors - Board Size " + size));
        pack ();
        setVisible (true);
    }

    private JPanel createButtonArray (int size, int [][] board)
    {
        JPanel buttonPanel = new JPanel (); // create panel for 2d jbutton array
        buttonPanel.setLayout (new GridLayout (size, size)); // set layout manager as grid layout

        buttonArray = new JButton [size][size]; // initialize 2d array of jbuttons
        // initialize the 2d array of jbuttons
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                JButton button = new JButton (); // create a jbutton object
                button.setPreferredSize (new Dimension (60, 60)); // set size to 60px x 60px
                button.setBackground (Color.WHITE); // set background color as white
                // add an action listener to every button
                button.addActionListener (new ActionListener ()
                {
                    public void actionPerformed (ActionEvent ae)
                    {
                        for (int i = 0; i < size; i++)
                        {
                            for (int j = 0; j < size; j++)
                            {
                                // typecast event source as jbutton then check it's location in the 2d array
                                if (((JButton) ae.getSource ()) == buttonArray [i][j])
                                {
                                    // if backend array == 1 (has chancellor), replace text with empty text on the interface and 0 on the backend
                                    if (boardArray [i][j] == 1)
                                    {
                                        buttonArray [i][j].setText (" ");
                                        boardArray [i][j] = 0;
                                    }
                                    // else, place indicator for chancellor (1 for both the board and backend)
                                    else
                                    {
                                        buttonArray [i][j].setText ("1");
                                        boardArray [i][j] = 1;
                                    }

                                    // checks for misplaced chancellor
                                    // if there is a misplaced chancellor
                                    if (!checkPosition (boardArray, size, i, j))
                                    {
                                        // if current color is white, set to red. else, revert to white
                                      if (buttonArray [i][j].getBackground () == Color.WHITE)
                                        buttonArray[i][j].setBackground (Color.RED);
                                      else
                                        buttonArray[i][j].setBackground (Color.WHITE);
                                    }
                                    else
                                    {
                                        buttonArray[i][j].setBackground (Color.WHITE);
                                    }

                                    // if the current board is already solved, show dialog box
                                    if (compareToSolution (boardArray, size))
                                      JOptionPane.showMessageDialog (null, "You solved it!", "Alert", JOptionPane.PLAIN_MESSAGE);
                                }
                            }
                        }
                    }
                });
                // set intial text as blank
                if (board [i][j] == 0)
                    button.setText (" ");
                else
                    button.setText ("1");
                // add to 2d array of buttns
                buttonArray [i][j] = button;
                // add to the jpanel
                buttonPanel.add (buttonArray [i][j]);
            }
        }

        return buttonPanel;
    }

    private JPanel createFromPreset (int size, int [][] board)
    {
        JPanel panel = new JPanel ();
        panel.setLayout (new BorderLayout ()); // set layout manager to border layout

        // initialize backend board 2d array
        boardArray = new int [size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                boardArray [i][j] = board [i][j];

        // create a panel for the other buttons on the game interface
        JPanel otherButton = new JPanel ();

        // set layout manager to 1 row , 2 cols
        otherButton.setLayout (new GridLayout (1, 2));

        // create a solver button
        JButton solveButton = new JButton ("Solve");

        // add action listener to the button
        solveButton.addActionListener (new ActionListener ()
        {
            public void actionPerformed (ActionEvent ae)
            {
                // create an instance of a solver
                Solver solver = new Solver (boardArray, size);
                System.out.println ("Blank solns : " + solver.getBlankSolutionSet ().size () + " Filled solns : " + solver.getActualSolutionSet ().size ());
                // if there is only one solution
                if (solver.getActualSolutionSet ().size () == 1)
                {
                    // compare the current board with the solution. if it matches, then the player has solved the board.
                    // else, it can be inferred that the player is almost done in solving the board
                    if (compareBoard (boardArray, solver.getActualSolutionSet ().getFirst (), size))
                    {
                      JOptionPane.showMessageDialog (null, "You solved it!", "Alert", JOptionPane.PLAIN_MESSAGE);
                    }
                    else
                    {
                      JOptionPane.showMessageDialog (null, "You're almost there!", "Alert", JOptionPane.PLAIN_MESSAGE);
                    }
                }
                // if the player pressed solve on a blank board
                else if (blankPuzzle (boardArray, size))
                {
                  JOptionPane.showMessageDialog (null, "Here are some solutions", "Alert", JOptionPane.PLAIN_MESSAGE);
                  new WriteSolution (solver.getBlankSolutionSet (), size);
                  new SolutionSet (solver.getBlankSolutionSet (), size);

                }
                // if the player pressed solver on a partially filled board
                else if (!solver.getActualSolutionSet ().isEmpty ())
                {
                  JOptionPane.showMessageDialog (null, "Here are some solutions", "Alert", JOptionPane.PLAIN_MESSAGE);
                  new WriteSolution (solver.getActualSolutionSet (), size);
                  new SolutionSet (solver.getActualSolutionSet (), size);
                }
                // if the current board has no existing solutions
                else
                {
                  JOptionPane.showMessageDialog (null, "No solution exists!", "Alert", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        // create an exit button
        JButton exitButton = new JButton ("Exit");
        exitButton.addActionListener (new ActionListener ()
        {
            public void actionPerformed (ActionEvent ae)
            {
                parent.setVisible (true); // shows parent window
                self.dispose (); // disposes current window
            }
        });

        // add them to the panel
        otherButton.add (solveButton);
        otherButton.add (exitButton);

        // add the other panels to the main panel
        panel.add (createButtonArray (size, boardArray), BorderLayout.CENTER);
        panel.add (otherButton, BorderLayout.SOUTH);


        return panel;
    }

    // create a jpanel that serves as the main content pane
    private JPanel createBlankBoard (int size)
    {
        JPanel panel = new JPanel (); // initialize a jpanel that serves as the main panel
        boardArray = new int [size][size]; // initialize 2d array int for board representation

        panel.setLayout (new BorderLayout ()); // set layout manager to border layout

        // initialize the backend board
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                boardArray [i][j] = 0;

        // create a panel for the other buttons on the game interface
        JPanel otherButton = new JPanel ();

        // set layout manager to 1 row , 2 cols
        otherButton.setLayout (new GridLayout (1, 2));

        // create a solver button
        JButton solveButton = new JButton ("Solve");

        // add action listener to the button
        solveButton.addActionListener (new ActionListener ()
        {
            public void actionPerformed (ActionEvent ae)
            {
                // create an instance of a solver
                Solver solver = new Solver (boardArray, size);
                System.out.println ("Blank solns : " + solver.getBlankSolutionSet ().size () + " Filled solns : " + solver.getActualSolutionSet ().size ());
                // if there is only one solution
                if (solver.getActualSolutionSet ().size () == 1)
                {
                    // compare the current board with the solution. if it matches, then the player has solved the board.
                    // else, it can be inferred that the player is almost done in solving the board
                    if (compareBoard (boardArray, solver.getActualSolutionSet ().getFirst (), size))
                    {
                      JOptionPane.showMessageDialog (null, "You solved it!", "Alert", JOptionPane.PLAIN_MESSAGE);
                    }
                    else
                    {
                      JOptionPane.showMessageDialog (null, "You're almost there!", "Alert", JOptionPane.PLAIN_MESSAGE);
                    }
                }
                // if the player pressed solve on a blank board
                else if (blankPuzzle (boardArray, size))
                {
                  JOptionPane.showMessageDialog (null, "Here are some solutions", "Alert", JOptionPane.PLAIN_MESSAGE);
                  new WriteSolution (solver.getBlankSolutionSet (), size);
                  new SolutionSet (solver.getBlankSolutionSet (), size);

                }
                // if the player pressed solver on a partially filled board
                else if (!solver.getActualSolutionSet ().isEmpty ())
                {
                  JOptionPane.showMessageDialog (null, "Here are some solutions", "Alert", JOptionPane.PLAIN_MESSAGE);
                  new WriteSolution (solver.getActualSolutionSet (), size);
                  new SolutionSet (solver.getActualSolutionSet (), size);
                }
                // if the current board has no existing solutions
                else
                {
                  JOptionPane.showMessageDialog (null, "No solution exists!", "Alert", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        // create an exit button
        JButton exitButton = new JButton ("Exit");
        exitButton.addActionListener (new ActionListener ()
        {
            public void actionPerformed (ActionEvent ae)
            {
                parent.setVisible (true); // shows parent window
                self.dispose (); // disposes current window
            }
        });

        // add them to the panel
        otherButton.add (solveButton);
        otherButton.add (exitButton);

        // add the other panels to the main panel
        panel.add (createButtonArray (size, boardArray), BorderLayout.CENTER);
        panel.add (otherButton, BorderLayout.SOUTH);

        // return the main panel
        return panel;
    }

    // compares the current board with the generated solution set
    private boolean compareToSolution (int [][] input, int size)
    {
      Solver solver = new Solver (input, size); // create a solver instance for the current board
      // if the current board is equal to a solution, return true.
      // else, return false
      for (int [][] solution : solver.getActualSolutionSet ())
        if (compareBoard (input, solution, size))
          return true;
      return false;
    }

    // checks the position of a recently placed chancellor.
    // returns true if the position is not in conflict with another piece
    // returns false if the position is wrong
    private boolean checkPosition (int [][] input, int N, int x , int y)
    {
      // check row
      for (int i = 0; i < N; i++)
        if (y != i)
          if (input [x][i] != 0)
            return false;

      // check col
      for (int i = 0; i < N; i++)
        if (x != i)
          if (input [i][y] != 0)
            return false;

      // L-Shaped
      // down
      if ((x + 2) < N)
      {
          if ((y + 1) < N)
          {
              if (input [x+2][y+1] != 0)
              {
                  return false;
              }
          }
          if ((y - 1) >= 0)
          {
              if (input [x+2][y-1] != 0)
              {
                  return false;
              }
          }
      }

      // up
      if ((x - 2) >= 0)
      {
          if ((y + 1) < N)
          {
              if (input [x-2][y+1] != 0)
              {
                  return false;
              }
          }
          if ((y - 1) >= 0)
          {
              if (input [x-2][y-1] != 0)
              {
                  return false;
              }
          }
      }

      // left
      if ((y - 2) >= 0)
      {
          if ((x + 1) < N)
          {
              if (input [x+1][y-2] != 0)
              {
                  return false;
              }
          }
          if ((x - 1) >= 0)
          {
              if (input [x-1][y-2] != 0)
              {
                  return false;
              }
          }
      }

      // right
      if ((y + 2) < N)
      {
          if ((x + 1) < N)
          {
              if (input [x+1][y+2] != 0)
              {
                  return false;
              }
          }
          if ((x - 1) >= 0)
          {
              if (input [x-1][y+2] != 0)
              {
                  return false;
              }
          }
      }

      return true;
    }

    // returns true if the current puzzle is empty
    // returns false if the current puzzle is not empty
    private boolean blankPuzzle (int [][] input, int N)
    {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (input [i][j] != 0)
                    return false;
        return true;
    }

    // compares two boards - the current board and a solution board
    // returns true if the two boards are equal
    // returns false if the two boards does not match
    private boolean compareBoard (int [][] input, int [][] solution, int N)
    {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (input [i][j] != solution [i+1][j+1])
                    return false;

        return true;
    }

    // required methods to be implemented for the window listener
    // only windowClosing has an action since this is the method fired when a window is disposed on click
    // windowClosed is another one but we only needed one action to be triggered on close
    public void windowClosing (WindowEvent we)
    {
        parent.setVisible (true); // show parent
        self.dispose (); // dispose self
    }
    public void windowOpened (WindowEvent we){}
    public void windowClosed (WindowEvent we){}
    public void windowIconified (WindowEvent we){}
    public void windowDeiconified (WindowEvent we){}
    public void windowActivated (WindowEvent we){}
    public void windowDeactivated (WindowEvent we){}
}
