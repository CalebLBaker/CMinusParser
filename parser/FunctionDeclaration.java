/**
* Defines a function declaration class for the c- parser
*
* @author Caleb Baker, Faith Trautmann
* @version 1.0
* File: FunctionDeclaration.java
* Created: Spring 2018
* (C)Copyright Cedarville University, its Computer Science faculty, and the
* authors. All rights reserved.
*
* This class defines a function declaration class for use in the c- parser. It 
* inherits from Declaration and is not expected to inherit from any classes.
*/

package parser;
import java.util.ArrayList;
import lowlevel.CodeItem;

import scanner.Token;
import lowlevel.FuncParam;
import lowlevel.Data;
import lowlevel.Function;
import lowlevel.BasicBlock;
import java.util.HashMap;

public class FunctionDeclaration extends Declaration {
    // name of the function 
    private String name;
    
    //return type of the function
    private Token.TokenType returnType;
    
    // parameters of the function
    private ArrayList<Parameter> parameters;
    
    // compound statement the makes up the function body
    private CompoundStatement statement;

    /**
     * Function declaration constructor
     * @param t the return type of the function
     * @param n the name of the array.
     * @param p the function parameters
     * @param s the compound statement the makes up the function body
     */
    FunctionDeclaration( Token.TokenType t, String n, ArrayList<Parameter> p, CompoundStatement s) {
        returnType = t;
        name = n;
        parameters = p;
        statement = s;
    }

    /**
     * Prints the node
     * @param tab current level of indentation
     */
    public void print(String tab) {
        System.out.println(tab + Token.toString(returnType) + " " + name + "(");
        if (parameters != null) {
            String newTab = tab + "    ";
            for (Parameter p : parameters) {
                p.print(newTab);
            }
        }
        System.out.println(tab + ")");
        statement.print(tab);
    }


    /**
	 * Generates Low-level code for a function declaration.
	 * @param tab the symbol table for global scope.
	 * @throws CodeGenerationException if an undeclared variable is used.
     * @return a CodeItem representing the low-level code.
	 */
	public CodeItem genCode(SymbolTable tab) throws CodeGenerationException {

        // Create linked-list of parameters.
        FuncParam firstParam = null;
        if (parameters != null) {
            firstParam = parameters.get(0).genCode();
            FuncParam par = firstParam;
            int len = parameters.size();
            for (int i = 1; i < len; i++) {
                par.setNextParam(parameters.get(i).genCode());
                par = par.getNextParam();
            }
        }

        // Get return type
        int type;
        if (returnType == Token.TokenType.INT) {
            type = Data.TYPE_INT;
        }
        else {
            type = Data.TYPE_VOID;
        }

        // Create function
        Function func = new Function(type, name, firstParam);

        // Put parameters into the function's hash map
        if (parameters != null) {
            HashMap paramTable = func.getTable();
            for (int i = 0; i < parameters.size(); i++) {
                paramTable.put(parameters.get(i).getName(), func.getNewRegNum());
            }
        }

        // Create first two blocks.
        func.createBlock0();
        BasicBlock blockOne = new BasicBlock(func);
        func.appendBlock(blockOne);
        func.setCurrBlock(blockOne);

        // Generate code for the function body.
        statement.genCode(func, tab, true);

        // Append return block.
        func.appendBlock(func.getReturnBlock());

        // Append unconnected chain.
        BasicBlock unconnected = func.getFirstUnconnectedBlock();
        if (unconnected != null) {
            func.appendBlock(unconnected);
        }

		return func;
	}
}
