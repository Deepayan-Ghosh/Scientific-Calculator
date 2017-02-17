package Calculator;

/*
    This is mainly a database handler which writes and reads data and returns the resulting dataset to the caller
    which requested for it. Most of the elements are private because outside class access is not desired.
    SQLite is used as it is an embedded database, and the database here will not have much traffic so SQLite is preferrable,
    as it does not require setting up server
*/


import javax.swing.*;
import java.sql.*;

public class DataBaseManager {

    private Connection con;
    private PreparedStatement stm;
    private Statement statement;
    private String sql;

    DataBaseManager() {
        con = null;
        stm = null;
        statement = null;
        sql = "INSERT INTO History(Expression, Result) VALUES (?,?)";   //main query which can be hard coded. No other query is possible
                                                                        //overwritten as needed
    }

    private void getConnection() throws Exception {
        Class.forName("org.sqlite.JDBC");

        //the connection url is relative. Please set it to the path where your database is.
        //the jar is configured to look for the .db file in the current location where the jar is
         con = DriverManager.getConnection("jdbc:sqlite:history.db");
    }

    //public method to get the data contained in database
    public ResultSet readData() throws Exception {
        getConnection();
        statement = con.createStatement();
        ResultSet result = statement.executeQuery("Select * from History");

        return result;
    }

    /*
            utility function to create the table.
            Not used anywhere else in this application.
     */
    public void createTable()
    {
        String str="CREATE TABLE History("+
                "Id INTEGER,"+
                "Date DATETIME DEFAULT (DATETIME(CURRENT_TIMESTAMP,'LOCALTIME')),"+
                "Expression VARCHAR(50),"+
                "Result VARCHAR(25)," +
                "PRIMARY KEY(Id)"+
                ")";
        try
        {
            getConnection();
            statement=con.createStatement();
            statement.execute(str);
            con.close();
        }
        catch (Exception e)
        {}
    }

    //function used to write the data to database file. Expression and result
    public void writeData(String exp, String res) {
        try {
            getConnection();                //open the connection
            stm = con.prepareStatement(sql);
            stm.setString(1, exp);
            stm.setString(2, res);
            stm.execute();
            con.close();
        } catch (Exception e)               //show error in JOptionPanel
        {
            JOptionPane.showMessageDialog(null,"error\n"+e);
        }
    }
    //function to clear history or to delete from table
    public void clear_history() {
        try {
            getConnection();
            statement=con.createStatement();
            boolean b=statement.execute("DELETE FROM History");
            statement=con.createStatement();
            statement.execute("VACUUM");
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"error\n"+e);
        }
    }
    //delete function to delete single data
    public void deleteData(String id)
    {
        try
        {
            getConnection();
            sql="Delete from History where Id=?";
            stm=con.prepareStatement(sql);
            stm.setString(1,id);
            stm.execute();
            con.close();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"error\n"+e);
        }
    }

    /*public static void main(String[] args) {
        XMLHandler x=new XMLHandler();
        x.createTable();
    }*/
}
