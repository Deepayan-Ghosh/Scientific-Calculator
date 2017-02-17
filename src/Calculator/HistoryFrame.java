package Calculator;

/*
    this class encapsulates the history frame and its components, listens to events from its components, and sends
    read requests to database handler, retrieves results, shows them in table, clears history (deletes data in table)
    and so on.
 */

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;

class HistoryFrame implements ActionListener {

        //components of this frame, generally private as they are not accessed outside this frame.
        //table is not private as it needs to be accessed by the event handler to get the selected item in table

        private MainEventHandler data;
        private JFrame history;
        JTable table;
        private JLabel empty;
        private JScrollPane jsp;
        private DataBaseManager fetch_data;
        private Vector colheads,rows,Data;
        private JPanel table_panel,button_panel;
        private JButton refresh,reevaluate,close,clear,delete;

        //constructor to initialise and attatch event handlers, set Layout and add components

        HistoryFrame(String str, MainEventHandler data)
        {
            history=new JFrame(str);
            history.setVisible(false);
            history.setResizable(false);

            //panel containing the table
            table_panel=button_panel=new JPanel();
            table_panel.setLayout(new FlowLayout(FlowLayout.LEFT));
            button_panel.setLayout(new FlowLayout(FlowLayout.CENTER,30,20));

            //when the window is closing remove the contents, so as to avoid overlapping when next time panel is added
            history.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        history.setVisible(false);
                        table_panel.remove(jsp);
                        history.dispose();
                    }
                });

            //database handler
            fetch_data=new DataBaseManager();
            empty=new JLabel("\n");

            //buttons
            refresh=new JButton("Refresh");
            reevaluate=new JButton("Re-evaluate");
            close=new JButton("Close");
            clear=new JButton("Clear History");
            delete=new JButton("Delete");
            button_panel.add(refresh);
            refresh.addActionListener(this);
            button_panel.add(reevaluate);
            reevaluate.addActionListener(data);
            button_panel.add(clear);
            clear.addActionListener(this);
            button_panel.add(delete);
            delete.addActionListener(this);
            button_panel.add(close);
            close.addActionListener(this);
            history.add(button_panel,BorderLayout.SOUTH);
        }


        @Override
        public void actionPerformed(ActionEvent e) {

            //if event related to show option in menu of main view is triggered
            if(e.getActionCommand()=="show")
            {
                //helper method to populate table with database data
                showDataInTable();

                jsp=new JScrollPane(table);
                table_panel.add(jsp,FlowLayout.LEFT);
                jsp.setPreferredSize(new Dimension(700,300));
                jsp.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                history.add(empty,BorderLayout.NORTH);
                history.add(table_panel, BorderLayout.CENTER);
                history.setVisible(true);
                history.setSize(800,450);
            }

            //if clear menu is triggered from main frame, clear the data from database.
            else if(e.getActionCommand().equals("clear"))
            {
                fetch_data.clear_history();
            }

            //if clear history occurs in child frame, delete data and show the new table
            else if(e.getActionCommand().equals("Clear History"))
            {
                fetch_data.clear_history();
                showDataInTable();
            }

            //refreshes the contents of table
            else if(e.getActionCommand().equals("Refresh"))
                showDataInTable();

            //deletes one or more rows
            else if(e.getActionCommand().equals("Delete"))
            {
                Integer i=table.getSelectedRow();
                i++;
                fetch_data.deleteData(i.toString());
                showDataInTable();
            }

            //closes the frame
            else if(e.getActionCommand().equals("Close"))
            {
                history.setVisible(false);
                table_panel.remove(jsp);
                history.dispose();
            }
        }

        //main function which creates the table and populates it with database data
        public void showDataInTable()
        {
            try
            {
                ResultSet r=fetch_data.readData();
                ResultSetMetaData rsmt=r.getMetaData();
                int c=rsmt.getColumnCount();
                colheads=new Vector(c);
                for(int i=1;i<=c;i++)
                    colheads.add(rsmt.getColumnName(i));
                rows=new Vector();
                while(r.next())
                {
                    Data=new Vector(c);
                    for(int i=1;i<=c;i++)
                        Data.add(r.getString(i));
                    rows.add(Data);
                }

                //if table is null that is no table exists at the moment, create new table and populate
                //else, change the model of table
                if(table==null)
                {
                    table=new JTable(rows,colheads);
                    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    TableColumnModel cmodel=table.getColumnModel();
                    cmodel.getColumn(0).setMaxWidth(50);
                    table.setColumnModel(cmodel);
                }
                else
                {
                    DefaultTableModel model=new DefaultTableModel(rows,colheads);   //obtain new model from new resultset
                    table.setModel(model);                                        //set the new model on the table
                    TableColumnModel cmodel=table.getColumnModel();       //obtain column model to set the width of column
                    cmodel.getColumn(0).setMaxWidth(50);
                    table.setColumnModel(cmodel);
                }
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(null,"Error\n"+e);
            }
        }

    }

