package Calculator;

/*
    a simple stack data structure with the basic functionalities: pop,push,peek
 */
public class Stack {
    String stack[];
    int top,size;

    //initialises the stack with the number of elements received as argument
    Stack(int length)
    {
        top=-1;size=length;
        stack=new String[length];
    }
    public void push(String element)
    {
        top=top+1;
        stack[top]=element;
    }
    public String  pop()
    {
        if(top<=-1)
            return null;
        String temp=stack[top];
        top--;
        return temp;
    }
    public String peek()
    {
        String temp=stack[top];
        return temp;
    }
}
