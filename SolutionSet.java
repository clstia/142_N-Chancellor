/*
*   SolutionSet.java
*       Represents a window that contains the solution set to a certain board
*/

// import dependencies
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.util.LinkedList;

public class SolutionSet extends JFrame
{
    JFrame self; // reference to self
    int counter = 0; // solution counter
    // buttons
    JButton nextButton;
    JButton prevButton;
    JButton closeButton;
    JButton [] options;

    public SolutionSet (LinkedList <int [][]> solutionSet, int N)
    {
        self = this; // create reference to self
        setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE); // dispose self on close
        setContentPane (createContentPane (solutionSet, N, self)); // create a content pane
        setTitle (new String ("Total number of Solutions: " + solutionSet.size ())); // set title that reflects the total number of solutions
        pack (); // compress everything into the frame
        setVisible (true); // show frame
    }

    // returns a jpanel that contains every interface element
    private JPanel createContentPane (LinkedList <int [][]> solutionSet, int N, JFrame self)
    {
        JPanel panel = new JPanel (); // create a blank panel
        panel.setLayout (new BorderLayout ()); // set layout manager as border layout

        JPanel buttonPanel = new JPanel (); // create a panel that will hold the action buttons
        buttonPanel.setLayout (new GridLayout (1, 3)); // set layout manager as grid layout with 1 row, 3 cols
        // create the action buttons
        JButton nextButton = new JButton ("Next Solution");
        JButton prevButton = new JButton ("Previous Solution");
        JButton closeButton = new JButton ("Close Window");

        // create a label for the current solution frame
        JLabel solutionLabel = new JLabel ("");

        // create a 2d array of jbuttons that represents a solution
        JButton [][] solutionButtons = new JButton [N][N];

        // create a panel for the solution
        JPanel solutionPanel = new JPanel ();
        // set layout manager as n x n grid
        solutionPanel.setLayout (new GridLayout (N, N));
        // get the first solution
        int [][] board = solutionSet.getFirst ();

        // traverse through the board
        for (int i = 1; i <= N; i++)
        {
            for (int j = 1; j <= N; j++)
            {
                // create a button object
                JButton button = new JButton (" ");
                // if the current coordinate has a chancellor, set text as 1
                if (board [i][j] == 1)
                    button.setText (Integer.toString (board [i][j]));
                // disable the said button
                button.setEnabled (false);
                // set the background as white
                button.setBackground (Color.WHITE);
                // set size as 60px x 60px
                button.setPreferredSize (new Dimension (60, 60));
                // add to the ui interface
                solutionPanel.add (button);
                // add to the backend representation
                solutionButtons [i-1][j-1] = button;
            }
        }

        // create an array of buttons for the action buttons
        options = new JButton [3];
        // create an instance of a private class that implements an action listener
        Traverse tl = new Traverse (solutionSet, solutionButtons, self, solutionLabel, N);

        // add action listener to the button
        prevButton.addActionListener (tl);
        // disable prev button since there are no previous solutions other that the first
        prevButton.setEnabled (false); // since counter will start at 0;
        buttonPanel.add (prevButton);
        options[0] = (prevButton);

        // add action listener to the button
        closeButton.addActionListener (tl);
        // add to interface
        buttonPanel.add (closeButton);
        // add to backend representation
        options[1] = (closeButton);

        // add action listener to the button
        nextButton.addActionListener (tl);
        // add to interface
        buttonPanel.add (nextButton);
        // add to backend representation
        options[2] = (nextButton);

        // add to the main panel.
        panel.add (solutionPanel, BorderLayout.CENTER); // solution panel is at the center
        panel.add (buttonPanel, BorderLayout.SOUTH); // action buttons at the lower part
        panel.add (solutionLabel, BorderLayout.NORTH); // solution label at the top

        // return the content pane
        return panel;
    }

    // a private class that serves as the action listener of the action buttons
    // allows the frame to switch between solution boards in a responsive and elegant way
    private class Traverse implements ActionListener
    {
        int counter, N; // solution counter and size variable
        JFrame self; // reference to solution set window
        JLabel label; // solution label reference
        LinkedList <int [][]> solutionSet; // reference to the solution set
        JButton [][] solutionButtons; // reference to the 2d array of buttons that will show the current solution

        // constructor
        public Traverse (LinkedList <int [][]> solutionSet, JButton [][] solutionButtons, JFrame self, JLabel label, int N)
        {
            this.self = self; // reference to solution set window
            this.label = label; // reference to the solution label
            this.counter = 0; // set counter as 0
            this.N = N; // reference to the board size
            this.solutionSet = solutionSet; // reference to the solution set
            this.solutionButtons = solutionButtons; // reference to the 2d array of jbuttons
            label.setText ("Solution # 1"); // set label as 1st solution
        }

        // action listener responsible for the responsive and elegant presentation of the solutions
        public void actionPerformed (ActionEvent ae)
        {
            // typecast source as a jbutton
            JButton source = (JButton) ae.getSource ();

            // if the source is the prevButton
            if (source == options [0])
            {
                counter --; // decrement counter
                // clear text is needed in order to imitate a repaint function
                label.setText (""); // clear text
                label.setText (new String ("Solution # " + (counter+1))); // set new label
                clearButtons (); // clear all the labels on the buttons
                setSolution (counter); // re-initialize all the labels on the buttons that represents the solution by getting the previous solution board
            }
            // if the source is the closeButton
            else if (source == options [1])
            {
                self.dispose (); // dispose self
            }
            // if the source is the nextButton
            else
            {
                counter ++; // increment
                // clear text is needed in order to imitate a repaint function
                label.setText (""); // clear text
                label.setText (new String ("Solution # " + (counter+1))); // set new label
                clearButtons (); // clear all the labels on the buttons
                setSolution (counter); // re-initialize all the labels on the buttons that represents the solution by getting the next solution board
            }

            // disable the prevButton once 0 is reached
            if (counter == 0)
                options [0].setEnabled (false);
            else
                options [0].setEnabled (true);

            // disable the nextButton once last solution is rendered
            if (counter == solutionSet.size ()-1)
                options [2].setEnabled (false);
            else
                options [2].setEnabled (true);
        }

        // plots the solution to the array of jbuttons
        private void setSolution (int index)
        {
            // get the target solution board
            int [][] selectedSolution = solutionSet.get (index);

            // plot the solution on the 2d jbutton array
            for (int i = 1; i <= N; i++)
                for (int j = 1; j <= N; j++)
                    if (selectedSolution [i][j] == 1)
                        solutionButtons [i-1][j-1].setText (Integer.toString (selectedSolution [i][j])); // since the solution board is int, use Integer.toString () to convert it to string properly
        }

        // clears all the labels on the 2d jbutton array
        private void clearButtons ()
        {
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    solutionButtons [i][j].setText (" ");
        }
    }
}
