package Calculator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Handels most of the events from basic calculator view that is View class and its child frame "HistoryFrame"
   It appends the action command of buttons to form the displayed expression. Actually the action commands are so set
   that they represent different operators and numbers required. Then it is passed to convert it to postfix and then
   evaluate the expression using postfix expression evaluation.
   For representation sine is replaced with s, cos with c, tan with t, log with l, ln with p
                      asin with S, acos with C, and atan with T

*/
class MainEventHandler implements ActionListener
{
    //displayed_expression is the expression displayed to the user in JTextPane
    //expression is the equivalent expression which is to be used for evaluation
    //it stores certain changes like replace 'sin" with 's' and so on for use in postfix evaluator

    protected StringBuffer expression,displayed_expression;

    //'reference' is a reference to the current instance of 'Buttons' used in the calculator.

    protected Buttons reference;

    //flag_evaluated is a status flag which turns true when an expression is evaluated

    protected int flag_evaluated=0;

    private DataBaseManager db_writer;  //writes the final expression and result to database for history

    //constructor to initialize

    MainEventHandler(Buttons reference)
    {
        expression=new StringBuffer("");
        displayed_expression=new StringBuffer("");

        this.reference=reference;       //reference received from Buttons

        try{
            db_writer=new DataBaseManager();
        }
        //catches exception and display them as errors in JOptionPane

        catch (Exception e){
            JOptionPane.showMessageDialog(null,"Error:\n"+e);
        }
    }


    //uses regex to find trigonometric ratios and substitute them with single characters for easier evaluation
    //in postfix

    public StringBuffer t_ratio_substitute(StringBuffer expr)
    {
        StringBuffer exp=new StringBuffer(expr);
        int i=1;
        char prev=exp.charAt(0);
        char present;

        //here prev and present, work in coordination to put multiplication operator where the user does not specify
        //e.g 2sin(45) -->2*sin(45)

        while(i<exp.length())
        {
            present=exp.charAt(i);
            if((present=='a' || present=='s' || present=='c' || present=='t' || present=='l' || present=='e') && (prev>=48 && prev<=57))
            {
                exp.insert(i,'*');
            }
            i++;
            prev=present;
        }
        //required regex

        Pattern pat=Pattern.compile("[sincotanlg]+");
        Matcher mat=pat.matcher(exp);
        String rtext="";int end=0;      //string with which to replace
        String txt="";                  //holds the found matching
        while(mat.find(end))
        {
            txt=mat.group();
            if(mat.group().equals("sin"))
                rtext="s";
            else if(mat.group().equals("cos"))
                rtext="c";
            else if(mat.group().equals("tan"))
                rtext="t";
            else if(mat.group().equals("ln"))
                rtext="p";
            else if(mat.group().equals("log"))
                rtext="l";
            else if(mat.group().equals("asin"))
                rtext="S";
            else if(mat.group().equals("acos"))
                rtext="C";
            else if(mat.group().equals("atan"))
                rtext="T";
            else{}
            exp.replace(mat.start(),mat.end(),rtext);
            end=mat.end()-txt.length()+1;
        }

        //replaces e with exponent value

        int index_exp=exp.toString().indexOf('e');
        while(index_exp!=-1)
        {
            exp.replace(index_exp,index_exp+1,Double.toString(Math.E));
            index_exp=exp.toString().indexOf('e');
        }

        //replaces sqrt sign with 'r', r means sqrt. Postfix evaluator recognises and performs desired square root
        int index_sqrt=exp.toString().indexOf('\u221A');
        while(index_sqrt!=-1)
        {
            exp.replace(index_sqrt,index_sqrt+1,"r");
            index_sqrt=exp.toString().indexOf('\u221A');
        }
        return exp;
    }

    //main event handling function
    //handles events from current Buttons instance being used and also from its child frame which shows history

    public void actionPerformed(ActionEvent e)
    {
        try {

            String command=e.getActionCommand();    //stores the action command

            if(command=="AC")                       //clears the JTextPane, or resets the expression
            {
                expression = new StringBuffer("");
                displayed_expression = new StringBuffer("");
                flag_evaluated=0;
            }

            // \u232b is backspace, deletes the last element of the displayed expression

            else if(command=="\u232b")
            {
                displayed_expression.deleteCharAt(displayed_expression.length() - 1);
            }

            //if the user presses equals to, then evaluate
            //pass the formed expression to postfix evaluator

            /* turns the flag_evaluated to true. It is used to track whether a calculation occurs just after another
               calculation. If it is true and an operator is pressed the previous evaluated result is used.
            */

            else if(command=="=")
            {
                //if equals is pressed when no expression has been provided

                if(reference.txtpane.getText().length()==0)
                    displayed_expression.append("0.0");

                else
                {
                    expression=t_ratio_substitute(displayed_expression);
                    PostfixEvaluation result = new PostfixEvaluation(expression.toString());

                    //holds the expression to be written to database
                    String hold_exp=displayed_expression.toString();

                    //the evaluated result is again stored in expression variable so that it can be reused
                    // for future calculation
                    expression = new StringBuffer(result.evaluate());
                    displayed_expression = new StringBuffer(expression);

                    //holds evaluated result to be written to database
                    String hold_res=displayed_expression.toString();

                    //sets textpane
                    reference.txtpane.setText(expression.toString());
                    reference.ans.setActionCommand(expression.toString());
                    flag_evaluated = 1;

                    //passes data to be written to database mananging java class
                    db_writer.writeData(hold_exp,hold_res);
                    return;
                }
            }

            //if number button is pressed, append it to expression
            else if(command.charAt(command.length()-1)>=48 && command.charAt(command.length()-1)<=57)
            {
                //if current calculation succeeds another, and the pressed button is number then reset all the params
                if(flag_evaluated==1)
                {
                    expression = new StringBuffer("");
                    displayed_expression = new StringBuffer("");
                    flag_evaluated= 0;
                }
                displayed_expression.append(command);
            }

            /*
            this event results from the child frame "history". Since the selected expression from the table is to be
            re evaluated, thus showed in main view frame. Thus it is a cross frame event, and handled by the main frame
            as the child frame sends data to parent frame to be displayed.
            takes the selected expression from table in history frame and makes it the new expression
             */
            else if(command.equals("Re-evaluate"))
            {
                displayed_expression=new StringBuffer(reference.child_frame.table.getValueAt(reference.child_frame.table.getSelectedRow(),2).toString());
            }

            else
            {
                //if current calc succeeds another(checked by flag_evaluated) then reset the expressions
                if(flag_evaluated==1 && (command.equals("sin(") || command.equals("cos(") || command.equals("tan(") ||
                        command.equals("asin(") || command.equals("acos(") || command.equals("atan(") ||
                        command.equals("log(") || command.equals("ln(") || command.equals("e") || command.equals("(") || command.equals(")")))
                {
                    expression = new StringBuffer("");
                    displayed_expression = new StringBuffer("");
                    flag_evaluated= 0;
                }
                //whatever may be the situation, append the resul
                displayed_expression.append((command.equals("10^x"))?"10^":command);
                flag_evaluated=0;
            }
            //sets the contents of the JTextPane to the displayed expression
            reference.txtpane.setText(displayed_expression.toString());
        }

        //if any exception occurs shows error message
        catch (Exception excep)
        {
            JOptionPane.showMessageDialog(null,"Error:\n"+excep);
        }
    }
}
