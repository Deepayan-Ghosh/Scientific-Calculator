package Calculator;

import java.util.HashMap;

/*
    This class accepts the raw string and converts it into postfix form.
    The HashMap keeps track of the priorities by mapping each operator to a value.
    Sine and others are represented by single characters.
    finalexp is used by postfix evaluator so it is not private. Others are not required anywhere else so they are private
*/
public class PostfixConverter {
    private StringBuffer str;
    String finalexp="";
    private HashMap<String,Integer> symbols;
    PostfixConverter(String str)
    {
        this.str=new StringBuffer(str);

        //adding different operator(key) and their priorities(value)
        symbols=new HashMap<>();
        symbols.put("(",-1);
        symbols.put("+",0);
        symbols.put("-",0);
        symbols.put("*",1);
        symbols.put("/",2);
        symbols.put("%",3);
        symbols.put("^",4);

        //the rest are all unary operators hence they have highest priority
        symbols.put("s",5);symbols.put("S",5);
        symbols.put("c",5);symbols.put("C",5);
        symbols.put("t",5);symbols.put("T",5);
        symbols.put("l",5);symbols.put("L",5);
        symbols.put("p",5);symbols.put("P",5);
        symbols.put("!",5);symbols.put("r",5);

    }
    //method to convert expression into postfix
    protected void convert()
    {
        int i=0;
        char ch;
        String word="",finalwrd="";

        while(i<str.length())
        {
            ch=str.charAt(i);

            //extracts numbers from the expression, includes decimal point to accomodate floating point literals
            if(ch>=48 && ch<=57 || ch=='.')
            {
                word+=str.charAt(i);
            }
            else
            {
                //if the number is more than one digit lone, surround them in single quotes for easier future extraction
                if(word.length()>1)
                    finalwrd=finalwrd+"\'"+word+"\'"+ch;
                else
                    finalwrd=finalwrd+word+ch;
                word="";
            }
            i++;
        }

        //looop exits before the last value gets added
        finalwrd=finalwrd+((word.length()>1)?"\'"+word+"\'":word)+")";


        //stack used to convert into postfix form, uses general logic
        Stack operator_stack=new Stack(finalwrd.length());
        operator_stack.push("(");
        int k=0;
        while(k<finalwrd.length())
        {
            ch=finalwrd.charAt(k);
            if(ch>=48 && ch<=57  || ch=='\'' || ch=='.')
            {
                finalexp+=ch;
            }
            else if(ch==')')
            {
                while(symbols.get(operator_stack.peek())!=-1)
                {
                    finalexp+=operator_stack.pop();
                }
                operator_stack.pop();
            }

            else if(ch=='(')
                operator_stack.push(Character.toString(ch));


            else
                {
                while (symbols.get(operator_stack.peek()) >= symbols.get(Character.toString(ch))) {
                    finalexp+=operator_stack.pop();
                }
                operator_stack.push(Character.toString(ch));
            }
            k++;
        }
    }
}
