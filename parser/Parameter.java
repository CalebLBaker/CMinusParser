/**
* This is the class that defines parameters for the c- parser
*
* @author Caleb Baker, Faith Trautmann
* @version 1.0
* File: Parameter.java
* Created: Spring 2018
* (C)Copyright Cedarville University, its Computer Science faculty, and the
* authors. All rights reserved.
*
* This is the definition for the parameter class. It does not inherit from any 
* classes nor is it expected to be inherited from.
*
*/

package parser;
import lowlevel.Data;
import lowlevel.FuncParam;

public class Parameter {
    // name of the parameter 
    private String name;
    
    // true if the parameter is an array, false otherwise
    private boolean isArray;

    /**
     * Parameter constructor
     * @param n the name of the parameter.
     * @param iA is the parameter an array.
     */
    Parameter(String n, boolean iA) {
        name = n;
        isArray = iA;
    }

    /**
     * Print this node of the tree
     * @param tab the current level of indentation
     */
    public void print(String tab) {
    	String n = name;
    	if (isArray) {
    		n += "[]";
    	}
    	System.out.println(tab + n);
    }

	/**
	 * Generates Low-level code for a function parameter.
	 * @return the low-level code for the parameter.
	 */
    public FuncParam genCode() {
        return new FuncParam(Data.TYPE_INT, name, isArray);
    }

    public String getName() {
        return name;
    }
}