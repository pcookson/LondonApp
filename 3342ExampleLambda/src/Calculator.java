
/**
 * 
 * @author Oracle (TM)
 * This Calculator class was taken from http://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html
 * on January 28, 2014. It is being used as an example of lambda expressions in Java 8.
 * All comments added by Patrick Cookson
 *
 */
public class Calculator {
  
	/**
	 * Create functional interface. Interface must include at at least one abstract method/
	 * Additioanlly, although not shown here, the functional interface can have any number of non abstract
	 * methods implemented.
	 */
    interface IntegerMath {
        int operation(int a, int b);   
    }
  
    /**
     * This is a method of Calculator that uses the InterMath functional interface as an input
     * This simulates what can be done in other functional languages: passing a function as input to 
     * another function.
     * 
     */
    public int operateBinary(int a, int b, IntegerMath op) {
        return op.operation(a, b);
    }
 
    public static void main(String... args) {
    	//create new Calculator object
        Calculator myApp = new Calculator();
        
        /*
         * Instantiates an instance of the functional interface IntegerMath and uses lambda expressions to define the 
         * operation that will be implemented. In this case, IntegerMath takes two int inputs and performs
         * addition
         */
        IntegerMath addition = (a, b) -> a + b;
        
        /*
         * Just like 'addition' above, subtraction is a new instance of the functional interface IntegerMath.
         * Here, lambda expressions define a new instance of IntegerMath called subtraction which takes
         * two int input and performs subtraction
         */
        
        IntegerMath subtraction = (a, b) -> a - b;
        
        /*
         * Passes the two integer values, and the 'function' addition to the operateBinary method of Calculator.
         * Although addition is, in Java, a inner class, it is being used as a function input
         */
        System.out.println("40 + 2 = " +
            myApp.operateBinary(40, 2, addition));
        
        /*
         * Likewise for the 'subtraction' instance
         */
        System.out.println("20 - 10 = " +
            myApp.operateBinary(20, 10, subtraction));    
    }//end main
}//end Calculator
