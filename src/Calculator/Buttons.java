package Calculator;

import javax.swing.*;
import java.util.ArrayList;

/*
    this class holds all the components of the calculator view like buttons, text pane, also contains the child frame
    reference, which originates due to menu related event of this frame
*/
class Buttons{

    protected JButton numbers[];            //stores number buttons in a simple array
    protected ArrayList<JButton> symbols;   //stores the symbol buttons in an arraylist
    protected JButton clear,ans;
    protected JTextField txtpane;
    HistoryFrame child_frame;  //child frame reference
    MainEventHandler data;      //reference to the main event handler

    /*
        constructor to initialise components and assign event handlers
    */

    Buttons()
    {
        symbols=new ArrayList<>();
        numbers=new JButton[10];
        clear=new JButton("AC");       //clears the textpane

        //passes reference to the current instance of the components being used, this is used to access the
        //individual components in the handler class
        data=new MainEventHandler(this);

        ans=new JButton("Ans");
        ans.setActionCommand("0");          //if no evaluation has been done, initialised to zero, else stores last result
        ans.addActionListener(data);

        clear.addActionListener(data);
        ans.setEnabled(false);              //initially disabled
        clear.setEnabled(false);
        for(Integer i=0;i<10;i++)           //add number buttons the arraylist and disable them
        {
            numbers[i]=new JButton(i.toString());
            numbers[i].setEnabled(false);
            numbers[i].addActionListener(data);
        }


        symbols.add(new JButton("+"));  //create buttons for different operatorx
        symbols.add(new JButton("-"));
        symbols.add(new JButton("*"));
        symbols.add(new JButton("/"));
        symbols.add(new JButton("%"));
        symbols.add(new JButton("\u232b"));
        symbols.add(new JButton("!"));
        symbols.add(new JButton("sin"));        //7
        symbols.get(7).setActionCommand("sin(");
        symbols.add(new JButton("e"));          //8
        symbols.add(new JButton("cos"));        //9
        symbols.get(9).setActionCommand("cos(");
        symbols.add(new JButton("^"));
        symbols.add(new JButton("."));
        symbols.add(new JButton("("));
        symbols.add(new JButton("tan"));
        symbols.get(13).setActionCommand("tan(");
        symbols.add(new JButton("log"));
        symbols.get(14).setActionCommand("log(");
        symbols.add(new JButton("\u221A"));
        symbols.add(new JButton("ln"));
        symbols.get(16).setActionCommand("ln(");
        symbols.add(new JButton(")"));
        symbols.add(new JButton("="));

        for(JButton e:symbols)
        {                                       //attatching the event handler
            e.addActionListener(data);
            e.setEnabled(false);
        }

        txtpane=new JTextField(23);     //creating text pane, initially disabled
        txtpane.setEditable(false);
    }
}
