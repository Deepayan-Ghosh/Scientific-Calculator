##Scientific Calculator

It is a scientific calculator which can solve arithmetic expressions, sine-cosine related expressions, nested sin-cos-tan or log-ln like sin(sin(45)) etc. It also keeps track of your calculations by recording your history, mainly, date of calculation, expression, result. You can choose to perform the calculation again, by pressing re-evaluate button. 

It uses simple stack to convert an arithmetic expression into postfix form, and then evaluate the postfix expression. The postfix evaluation algorithm has been extended to accomodate sin, cos, factorial operations. Each of the special operations are represented by single character operators, like 's' for 'sin', 'c' for 'cos', etc so that they can be treated in the same way as +,-,*,/ are treated during postfix evaluation. It also uses SQLite as an embedded database to keep the track of history of calculations.

##The JAR File
Download the 'Calculator' directory and run the .jar file from within the folder. To use the jar somewhere else, copy the folder and run it from within the folder. The main point is that the database file must be at the same directory level as the jar file. 
