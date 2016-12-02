import java.io.*;
import java.util.*;

/**
 * Variable.java
 *
 *
 * Created: Wed Aug  2 09:57:17 2000
 *
 * @author Stephan Baldes
 */

public class Variable {
    public String name;
    public String value;    

    public static final int constant = 0;
    public static final int single   = 1;
    public static final int segment  = 2;

    private static String singlePrefix = "?";
    private static String segmentPrefix = "?*";


    public static int typ (String name) {
      if (name.startsWith(segmentPrefix)) return segment;
      else if (name.startsWith(singlePrefix)) return single;
      else return constant;   
    }

    public Variable() {
	this.name = "";
	this.value = "";
	}   


    public Variable(String name) {
        this();
	this.name = name;
	}

    public Variable(String name, String value) {
        this(name);
	this.value = value;
	}


    public Variable(Variable var) {
	this.name = new String(var.name); 
	this.value = new String(var.value);
	}
    
    public String toString() {
       return this.name + " " + this.value;
    }


    public static void main(String[] args) {
	Variable var1 = new Variable("?x", "hallo");
        System.out.println(var1);
	}
    
    } // Variable
