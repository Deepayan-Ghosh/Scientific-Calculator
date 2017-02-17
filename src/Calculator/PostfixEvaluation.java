package Calculator;

/*
    This class extends from Postfix converter. It takes the finalword from the converter class and evaluates it
       according to postfix evaluation, except that it is extended to sin, cos, factorial, etc.
 */
class PostfixEvaluation extends PostfixConverter{
    private StringBuffer postfix;

    //initialise the super class variables
    PostfixEvaluation(String expression)
    {
        super(expression);
    }

    public String evaluate()throws Exception
    {
        convert();                              //calls convert subroutine to convert the expression into postfix form
        postfix=new StringBuffer(finalexp);     //finalexp is from converter class

        int k=0;char ch;
        String number="";

        //stack to hold the operands
        Stack operand_stack=new Stack(postfix.length());
        while(k<postfix.length())
        {
            ch=postfix.charAt(k);

            //extract a number with more than one digit
            if(ch=='\'')
            {
                k++;
                ch=postfix.charAt(k);
                while(ch!='\'')
                {
                    number+=ch;
                    k++;
                    ch=postfix.charAt(k);
                }
            }
            //else if it is only a digit
            else if(ch>=48 && ch<=57)
                number+=ch;

            //else if it is an operator, and operator also means sin,cos,tan,log,ln,asin,acos,atan,10^x,factorial
            else
            {
                String str1=null;
                String str2=null;
                Double operand2=0d;

                //if operator is unary, then pop one operand only, else pop two
                if(ch=='s' || ch=='S' || ch=='c' || ch=='C' || ch=='t' || ch=='T' ||ch=='l'||ch=='p' || ch=='!' || ch=='r')
                    str1=operand_stack.pop();
                else
                {
                    str1=operand_stack.pop();
                    str2=operand_stack.pop();

                    //operand2 is not necessary all the time
                    operand2=Double.parseDouble(str2);
                }

                //operand1 is guaranteed to exist
                Double operand1=Double.parseDouble(str1);
                Double result=0d;

                //main calculation
                switch(ch)
                {
                    case '+':   result=operand2+operand1;
                                break;
                    case '-':   result=operand2-operand1;
                                break;
                    case '*':   result=operand2*operand1;
                                break;
                    case '/':   result=operand2/operand1;
                                break;
                    case '%':   result=operand2%operand1;
                                break;
                    case '^':   result=Math.pow(operand2,operand1);
                                break;
                    case 's':   result=Math.sin(Math.toRadians(operand1));
                                break;
                    case 'S':   result=Math.toDegrees(Math.asin(operand1));
                                break;
                    case 'c':   result=Math.cos(Math.toRadians(operand1));
                                break;
                    case 'C':   result=Math.toDegrees(Math.acos(operand1));
                                break;
                    case 't':   result=Math.tan(Math.toRadians(operand1));
                                break;
                    case 'T':   result=Math.toDegrees(Math.atan(operand1));
                                break;
                    case 'l':   result=Math.log10(operand1);
                                break;
                    case 'p':   result=Math.log(operand1);
                                break;
                    case '!':   result=1d;
                                for(long i=1;i<=operand1;i++)
                                    result*=i;
                                break;
                    case 'r':   result=Math.sqrt(operand1);
                                break;
                }

                //if value is very small, of order <-15, print zero
                if(Math.getExponent(result)<-15)
                    operand_stack.push("0.0");
                else {
                    operand_stack.push(result.toString());
                }
                k++;
                continue;
            }
            operand_stack.push(number);
            k++;
            number="";
        }
        if(operand_stack.top==1)
        {
            Double d1=Double.parseDouble(operand_stack.pop());
            Double d2=Double.parseDouble(operand_stack.pop());
            operand_stack.push(Double.toString(d1.doubleValue()*d2.doubleValue()));
        }
       return operand_stack.peek();
    }

    /*public static void main(String[] args) throws IOException{
        Scanner sc=new Scanner(System.in);
        String input="";
        input=sc.nextLine();
        PostfixEvaluation ob=new PostfixEvaluation(input);
        ob.evaluate();
        System.out.println(ob.postfix);
    }*/
}
