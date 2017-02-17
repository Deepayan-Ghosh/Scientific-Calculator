package Calculator;

/*
    This class extends the button class. This class sets the main view with the components defined in Buttons class.
    It defines the layout and other factors.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
class View  extends  Buttons{
    private JPanel panel_top,panel_center,MenuHolder,textpanel,buttonholder;
    private JFrame frm;
    private JLabel empty_label;
    View() {
        super();

        //empty label to create gap
        empty_label=new JLabel("\n");
        Font f_label=new Font("Arial",Font.PLAIN,45);
        empty_label.setFont(f_label);


        //create frame to hold component
        frm = new JFrame("Calculator");
        frm.setSize(400, 500);
        frm.setVisible(true);
        frm.setResizable(false);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //panel_center is the center panel
        panel_center = new JPanel();
        panel_center.setBackground(Color.DARK_GRAY);
        panel_top = new JPanel();
        MenuHolder=new JPanel();MenuHolder.setLayout(new FlowLayout());
        textpanel=new JPanel();
        buttonholder=new JPanel();buttonholder.setLayout(new FlowLayout(FlowLayout.CENTER,40,10));
        panel_top.add(empty_label);


        //set layout of central panel
        panel_center.setLayout(new GridLayout(6,4,6,6));
        //set border of central panel to create gap on four sides and the buttons
        panel_center.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY,20));
        panel_center.add(ans);

        //adding buttons from button class to central panel in grid view
        int p=1,q=0;
        panel_center.add(symbols.get(q++));
        panel_center.add(symbols.get(q++));
        panel_center.add(symbols.get(q++));
        panel_center.add(symbols.get(q++));
        for(int i=1;i<=12;i++) {
            if(i%4==0)
            {
                panel_center.add(symbols.get(q++));
                panel_center.add(symbols.get(q++));
            }
            else
            {
                panel_center.add(numbers[p++]);
            }
        }
        panel_center.add(numbers[0]);
        for(int i=q;i<symbols.size();i++)
            panel_center.add(symbols.get(i));


        //adding the text field to textpanel
        Font f=new Font("Times New Roman",Font.BOLD,14);
        txtpane.setHorizontalAlignment(SwingConstants.RIGHT);
        txtpane.setFont(f);
        txtpane.setPreferredSize(new Dimension(0,40));
        textpanel.add(txtpane);


        //a togglebutton to activate and deactivate the calculator, when set to on, all components are enabled
        JToggleButton on_off=new JToggleButton("Off");


        //inv is  a togglebutton when pressed activates the inverse mode like asin, acos, atan, etc
        JToggleButton inv=new JToggleButton("Inverse");
        inv.setEnabled(false);


        //add the listener which enables all the components when on_off button is set to on

        on_off.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(on_off.isSelected())
                {
                    on_off.setLabel("On");
                    ans.setEnabled(true);clear.setEnabled(true);inv.setEnabled(true);
                    for(JButton btn:numbers)
                        btn.setEnabled(true);
                    for(JButton btn:symbols)
                        btn.setEnabled(true);
                    txtpane.setEditable(true);
                }
                else
                {
                    on_off.setLabel("Off");
                    ans.setEnabled(false);clear.setEnabled(false);inv.setEnabled(false);
                    for(JButton btn:numbers)
                        btn.setEnabled(false);
                    for(JButton btn:symbols)
                        btn.setEnabled(false);
                    txtpane.setEditable(false);
                }
            }
        });


        //inverse button's event when triggered changes sin button to asin, cos to acos, etc and accordingly
        //set the action command of each button
        inv.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(inv.isSelected())
                {
                    symbols.get(7).setLabel("asin");symbols.get(7).setActionCommand("asin(");
                    symbols.get(9).setLabel("acos");symbols.get(9).setActionCommand("acos(");
                    symbols.get(13).setLabel("atan");symbols.get(13).setActionCommand("atan(");
                    symbols.get(14).setLabel("10^x");symbols.get(14).setActionCommand("10^x");
                }
                else {
                    symbols.get(7).setLabel("sin");symbols.get(7).setActionCommand("sin(");
                    symbols.get(9).setLabel("cos");symbols.get(9).setActionCommand("cos(");
                    symbols.get(13).setLabel("tan");symbols.get(13).setActionCommand("tan(");
                    symbols.get(14).setLabel("log");symbols.get(14).setActionCommand("log(");
                }
            }
        });

        //add the buttons to the buttonholder panel
        buttonholder.add(on_off);buttonholder.add(clear);buttonholder.add(inv);


        //create a menu which contains mainly options to clear and show history
        JMenuBar jmb=new JMenuBar();
        jmb.setPreferredSize(new Dimension(400,25));
        JMenu history=new JMenu("History");
        JMenuItem clear=new JMenuItem("clear");
        clear.setPreferredSize(new Dimension(history.getWidth(),30));
        JMenuItem show=new JMenuItem("show");


        //here, the resulting child frame is created
        child_frame=new HistoryFrame("history",data);


        //when show option is clicked, the frame is made visible. The event is handled by the HistoryFrame class
        show.addActionListener(child_frame);
        clear.addActionListener(child_frame);


        //add the options to the menu option
        history.add(clear);history.add(show);
        jmb.add(history);

        //set the menu bar to the frame
        frm.setJMenuBar(jmb);

        //adding panels to frame
        panel_top.setLayout(new BorderLayout());
        panel_top.add(MenuHolder,BorderLayout.NORTH);
        panel_top.add(textpanel,BorderLayout.CENTER);
        panel_top.add(buttonholder,BorderLayout.SOUTH);
        frm.add(panel_top, BorderLayout.NORTH);
        frm.add(panel_center, BorderLayout.CENTER);
    }
}