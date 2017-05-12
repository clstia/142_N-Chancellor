/*
* Interface.java
* Serves as the main method and the window that retrieves the desired board size
*/

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JFileChooser;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Interface extends JFrame
{
    // declare variable for parent frame. need this to hide the main frame later
    JFrame parent;

    // constructor for the intial interface
    public Interface ()
    {
        // set parent variable as this frame
        this.parent = this;
        // create content pane
        setContentPane (createContentPane ());
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setTitle ("N Chancellors - Set Board Size");
        pack ();
        setVisible (true);
    }

    // creates a panel that serves as the main container
    private JPanel createContentPane ()
    {
        JPanel panel = new JPanel ();
        panel.setLayout (new GridLayout (3, 1));

        JPanel inputPanel = new JPanel ();
        inputPanel.setLayout (new GridLayout (2, 1));

        // create the form that will take the desired board size
        JTextField size = new JTextField ();
        // on button click, will generate a game with board of size N
        JButton confirmSize = new JButton ("Generate Board");
        confirmSize.addActionListener (new ActionListener ()
        {
            public void actionPerformed (ActionEvent ae)
            {
                // how to hide parent
                new Game (Integer.parseInt (size.getText ()), parent);
            }
        });

        JButton findPreset = new JButton ("Create Board From Preset");
        findPreset.addActionListener (new ActionListener ()
        {
            public void actionPerformed (ActionEvent ae)
            {
                JFileChooser chooseFile = new JFileChooser ();
                int option = chooseFile.showOpenDialog (Interface.this);
                if (option == JFileChooser.APPROVE_OPTION)
                {
                    OpenPreset op = new OpenPreset (chooseFile.getSelectedFile ());
                    new Game (op.getBoard (), op.getSize (), parent);
                }
            }
        });

        // assemble the components
        inputPanel.add (new JLabel ("Enter board size: "));
        inputPanel.add (size);

        panel.add (inputPanel);
        panel.add (confirmSize);
        panel.add (findPreset);

        return panel;
    }

    public static void main (String[] args)
    {
        new Interface ();
    }
}
