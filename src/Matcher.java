import java.io.*;
import java.util.*;

public class Matcher {

  /** shows debug-traces if true */
  boolean DEBUG=false;
  /** General bindings before matching happens */
  public Hashtable generalBindings;
  /** Bindings that will be found during the matching*/
  public Hashtable bindings;
  /** Indicates if the matching was successful or not*/
  public boolean   fail;
  /* Last position of the pattern during matching*/
  private int matchPosP;
  /* Last position of the input during matching*/
  private int matchPosI;
  
  /** Constructor */
    public Matcher() {
        fail = false;
  this.generalBindings = new Hashtable();
  this.bindings = new Hashtable();
         
  }

    
    /** Constructor
     * 
     * @param generalBindings bindings for variables 
     */
    public Matcher(Hashtable generalBindings) {
        this();
  this.generalBindings = generalBindings;
  }

    /** Returns the value of variable <code>var</code> in
     *  the hashtables <code>generalBindings</code> and 
     *  <code>bindings</code> 
     *  @return value of variable, null if unbound.
     */

    public String getBinding(Variable var) {
      String res = (String) generalBindings.get(var.name);
      if (res == null)
         res= (String) bindings.get(var.name);
      return res;
    }

    /** Adds <code>var</code> to bindings.
     *  @return <code>true</code> if consistent possible, 
     *          <code>false</code> and sets <code>fail=false</code> otherwise.
     */

    private boolean addBinding(Variable var) {
      String binding = getBinding(var);
      if (binding == null) {
   // Variable hinzufügen 
         bindings.put(var.name,var.value);
         return true;
      }
      // Wert von var und binding sind gleich
      else if (binding.equalsIgnoreCase(var.value)) {
              return true;
           }
      // Wert von var und binding sind gleich
      else {
              fail = true;
              return false;
      }
    }
        

    /** Print current bindings. */
    
    public String[][] printBindings() {
    	ArrayList<String> s= new  ArrayList<String>();
    	ArrayList<String> a= new  ArrayList<String>();
       Enumeration e = bindings.keys();      
       while (e.hasMoreElements()) {
         String var = (String)e.nextElement();
         s.add(var);
         String abc = (String) bindings.get(var);
         a.add(abc);
       }
       String[][] strings = new String[s.size()][2];
       
       for (int i = 0; i < s.size(); i++) {
    	  
			  strings[i][0]=s.get(i);
			  strings[i][1]=a.get(i);

       }
       
       return strings;
    }
    

    public boolean match(String patternS, String inputS) {
       fail = false;
       this.bindings = new Hashtable();
       
       // Sonderzeichen entfernen, wie z.B. :,!.
       inputS = StringUtil.removeSpecialCharacter(inputS);
       
       String[] pattern = StringUtil.stringToArgs(patternS);
       String[] input = StringUtil.stringToArgs(inputS);

       if (DEBUG)System.out.println(StringUtil.toString(pattern));
       if (DEBUG)System.out.println(StringUtil.toString(input));
             
       doMatch(pattern,input,0,0);
       
       return !fail;
    }
    

     /**
      *       
      * @param pattern
      * @param input
      * @param posP position pattern
      * @param posI position input
      */     
 
    private void doMatch(String[] pattern, String[] input,int posP, int posI) {
         // Fehler aufgetreten?
         if (fail){ matchPosP=posP; matchPosI=posI; return; }
         // Alle Pattern-Positionen abgearbeitet?
         if (posP>pattern.length-1) {
             // Gibt es noch unbearbeitete inputs?
             if (posI>input.length-1) {
               matchPosP=posP; matchPosI=posI; return;
             } else {
               fail=true;matchPosP=posP; matchPosI=posI; return;  
             }
         }
         
         int type = Variable.typ(pattern[posP]);
 
         // Aktuelle Patternposition ist eine Konstante, z.B. Peter
         if (type==Variable.constant)
         {
           // Ist posI ist größer als die Anzahl im Eingabefeld, dann ...
           // z.B. pattern={"a","b"} ; input={"a"} ; posP=1 ; posI=1
           // *TODO* Code ergänzen: if (...) {...}
        	 if(posI>input.length){
        		 fail=true;
        		 return;
        	 }
           // Vergleiche Konstanten an aktueller Position posP und posI
           // z.B. pattern={"a","b"} ; input={"a","b"} ; posP=0 ; posI=0
           if (pattern[posP].equalsIgnoreCase(input[posI]))
           {
            doMatch(pattern,input, posP+1, posI+1);    
           } else {
              //*TODO* Hier Code einfügen
        	   
        	   fail=true;
      		 return;
           }           
         // Aktuelle Patternposition ist eine Variable, z.B. ?name
         } else if (type==Variable.single) {
            boolean bindingOk = addBinding(new Variable(pattern[posP],input[posI]));
            if (bindingOk) {
                doMatch(pattern,input,posP+1,posI+1);
            } else {
                matchPosP=posP; matchPosI=posI; return;
            }
         // Aktuelle Patternposition ist eine Seqment-Variable, z.B. ?*anfang
         } else if (type==Variable.segment) {
            // pattern[posP] ist letztes Element? Dann matche alle
            if (posP == pattern.length - 1) {
                // Hat input noch elemente?
                if (posI > input.length - 1) {
                    // Nein
                    addBinding(new Variable(pattern[posP], ""));
                } else {
                    //ja
                    addBinding(new Variable(pattern[posP],
                            StringUtil.argsToString(input, posI, input.length - 1)));
                }
                matchPosP = posP;
                matchPosI = posI;
                return;
            }

            // Vorausschau: Nächste Pattern-Position untersuchen
            int nextPatternType = Variable.typ(pattern[posP + 1]);

            // ?*x ?*y -> nicht erlaubt
            if (nextPatternType == Variable.segment) {
                fail = true;
                System.out.println("Fehler: 2 Segmente folgen aufeinander in Pattern:'" +
                        StringUtil.argsToString(pattern) + "'");
                matchPosP = posP;
                matchPosI = posI;
                return;
            } else {
                // Matche mit Nachfolgern
                boolean findNextMatch = false;
                int nextMatchPosI = posI;
                while ((findNextMatch == false) && (nextMatchPosI < input.length)) {
                    Matcher m1 = new Matcher(bindings);
                    findNextMatch = m1.match(StringUtil.argsToString(pattern, posP + 1, pattern.length - 1),
                            StringUtil.argsToString(input, nextMatchPosI, input.length - 1));
                    if (!findNextMatch) {
                        nextMatchPosI++;
                    }
                    System.out.println("findNextMatch:" + findNextMatch);
                }
                if (!findNextMatch) {
                    // Match war nicht möglich
                    fail = true;
                    return;
                } else {
                    // Match war möglich, belege Segment
                    // Fälle: 
                    // 1. "?*x ?y c" ; "a b c"
                    // 2. "?*x c" ; "a b c"

                    // ist "?*x" leer? z.B. "?*x c" ; "c" 
                    if (nextMatchPosI == posI) {
                        // ja
                        addBinding(new Variable(pattern[posP], ""));
                    } else {
                        // nein, binde ?*x an alle Vorgänger
                        addBinding(new Variable(pattern[posP],
                                StringUtil.argsToString(input, posI, nextMatchPosI - 1)));
                    }
                    doMatch(pattern, input, posI + 1, nextMatchPosI);
                }
            }
         }
     }
                    
    /** Instantiate <code>pattern</code> according to the bindings of the 
     *  matcher. The bindings has to determinied before a 
     * invocation of the<code>match</code> or <code>addBinding</code> method.
     *
     *  E.g.: "My name is ?name" ; ?name = "Peter" -> "My name is Peter"
     *  @return the instantiated pattern. 
     */

    public String instantiate (String pattern) {
       String res = pattern;

       Enumeration e = bindings.keys();
       while (e.hasMoreElements()) {
         String var   = (String)e.nextElement();
         String value = (String)bindings.get(var);
         res = StringUtil.replace(res,var, value);
       }
       return res;
    }

  
    /** Transforms the bindings to <code>String[]</code>: 
     *  @return <code>{"var-name1 value1",...,"var-nameN valueN"}</code>
     */

    public String[] bindingsToArray() {
       String[] res = new String[bindings.size()];

       Enumeration e = bindings.keys();

       int i=0;
       while (e.hasMoreElements()) {
         String var = (String)e.nextElement(); 
         res[i++] = var + " " + bindings.get(var);
       }
      return res;
    }


    /** Transforms the bindings to <code>String</code>: 
     *  @return <code>"var-name1 |value1| ... var-nameN |valueN|}</code>
     */

    public String bindingsToString() {
       String res = "";

       Enumeration e = bindings.keys();

       while (e.hasMoreElements()) {
         String var = (String)e.nextElement(); 
         res += var + " |" + bindings.get(var) +"| ";
       }
      return res;
    }

    /** Test Method */
    public static void main(String[] args) {
     Matcher m1 = new Matcher();
     boolean match = m1.match("ich bin ?name",
                               "ich bin Norbert");
     System.out.println("Match: "+match);
     m1.printBindings();


    }
    
} // Matcher 

