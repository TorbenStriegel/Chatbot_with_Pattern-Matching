import java.util.*;
import java.io.*;


/** This class contains a set of methods that are useful for string related issues, 
*    e.g. tokenization, concatenation, etc.
*
*/

public class StringUtil 
{

    /** Specifies that matching pairs of this character delimit 
    * string constants in this tokenizer.
    */

   public final static int QUOTE_CHAR =  '\u0022'; // '\"'

    /** Specifies that matching pairs of this character delimit 
    * string constants in this tokenizer. 
    */

    public final static int ELEMENT_SEPARATOR = '\u007c'; //'|'

     
    /** Breaks the specified line into tokens using a StreamTokenizer
     * <br><pre>
     * "Hello \"this is the second\"  \"and the third argument\""
     * ->
     * {"Hello","this is the second","and the third argument"}
     * </pre>
     * .
    * If a string quote character is encountered, then a string is recognized, 
    * consisting of all characters after (but not including) the string quote character, 
    * up to (but not including) the next occurrence of that same string quote character, 
    * or a line terminator, or end of file.
    * @param line the line to be processed
    * @return the arguments in sequential order or <code>null</code> if there aren't any
    * @exception IOException if an I/O error occurs.
    * @see #argsToString
    * @see java.io.StreamTokenizer
    */
    public static String[] stringToArgs(String line) 
    {
        try {
       	StreamTokenizer st = new StreamTokenizer(new StringReader(line));

        int char1 = Math.min(QUOTE_CHAR,ELEMENT_SEPARATOR);
        int char2 = Math.max(QUOTE_CHAR,ELEMENT_SEPARATOR);

        st.whitespaceChars(0,32);
        st.wordChars(33,char1-1); // 34 = "
        st.wordChars(char1+1,char2-1); //124 = |
        st.wordChars(char2+1,255);

        st.quoteChar(QUOTE_CHAR); 

	int ttype = st.nextToken();
        if (ttype == StreamTokenizer.TT_EOF) return null;
        Vector l = new Vector();
	    while(ttype != StreamTokenizer.TT_EOF)
	    {
	        switch (ttype)
	        {
	            case StreamTokenizer.TT_WORD: 
	            {
	                l.addElement(st.sval);
	                break;
	            }
	            case StreamTokenizer.TT_NUMBER:
	            {
	                if ((st.nval % 1) == 0) 
	                {
	                    l.addElement(String.valueOf((long)st.nval));
	                } else 
	                {
	                    l.addElement(String.valueOf(st.nval));
	                }
	                break;
	            }
	            case StreamTokenizer.TT_EOL:
	            {
	                break;
	            }
		case QUOTE_CHAR: 
	            {
	                l.addElement(st.sval);
	            }
	        } 
	        ttype = st.nextToken();
	    }
	    //String[] args = (String[])l.toArray(new String[l.size()]);
            String[] args = toArray(l);
        return args;
     } catch (Exception e) {e.printStackTrace(); return null;}
 
    }

    /** Concatenates a subset of the specified arguments using a delimiter 
     *  <pre>"</pre>
     *  <br><pre>{"0","1","2","3"},0,2 -> "0 1 2"</pre>
    *
    * @param args a list of arguments
    * @param beginIndex the beginning index, inclusive, starts with 0
    * @param endIndex the ending index, inclusive
    * @return the concatenation of the specified arguments
    * @throws IllegalArgumentException if the begin index is equal to or greater 
    *            than the end index
    * @see #stringToArgs
    */
    public static String argsToString(String[] args, int beginIndex, int endIndex) 
    {return argsToString(args,beginIndex,endIndex," ");
    }

    /** Concatenates the specified arguments using a delimiter 
     *  <pre>"</pre>
     *  <br><pre>{"0","1","2","3"} -> "0 1 2 3"</pre>
    *
    * @param args a list of arguments
    * @param beginIndex the beginning index, inclusive, starts with 0
    * @param endIndex the ending index, inclusive
    * @return the concatenation of the specified arguments
    * @throws IllegalArgumentException if the begin index is equal to or greater 
    *            than the end index
    * @see stringToArgs
    */
    public static String argsToString(String[] args) 
    {return argsToString(args,0,args.length-1," ");
    }

    /** Concatenates a subset of the specified arguments using a delimiter
     *  <br><pre>{"0","1","2","3"},0,2,"-" -> "0-1-2"</pre>
    *
    * @param args a list of arguments
    * @param beginIndex the beginning index, inclusive, starts with 0
    * @param endIndex the ending index, inclusive
    * @param delim the delimiter to be used
    * @return the concatenation of the specified arguments
    * @throws IllegalArgumentException if the begin index is equal to or greater 
    *            than the end index
    */
    public static String argsToString(String[] args, int beginIndex, int endIndex, String delim) 
    {
        if (beginIndex > endIndex) {
            throw new IllegalArgumentException("begin index >= end index");
        }
        StringBuffer arg = new StringBuffer(args[beginIndex]);
        for (int i=++beginIndex; i<=endIndex; i++) 
        {
            arg.append(delim).append(args[i]);
        }
        return arg.toString();
    }



  public static String toString (String[] s) {
   if ((s==null) || (s.length == 0)) return null;
   else{
     String res;
     int i; 
     res = "{";
     for(i=0; i<s.length-1; i++) res += s[i]+",";
     res += s[i];
     res += "}";
     return res;
   }
  }

  /**	
   * Converts <code>Enumeration</code> to <code>String</code>
   * A <code>\n</code> is used a separator.
   */
  
  public static String  toString(Enumeration e) {
    String res = "";
    while (e.hasMoreElements()) 
      res += "\n"+"  Element: "+(String)e.nextElement();
    return res;
  }

  
  /**	
   * Converts <code>Vector</code> to <code>String</code>
   * A <code>\n</code> is used a separator.
   */

  public static String toString(Vector v) {
   if ((v==null) || (v.size()==0)) return null;
    String res = "";
    for(int i=0; i<v.size(); i++) {
      res += "\n"+"  Element: ";
      if (v.elementAt(i) instanceof String) 
         res +=(String)v.elementAt(i);
      else if (v.elementAt(i) instanceof String[]) 
         res += toString((String[])v.elementAt(i));
    }
    return res;
  }


  /**	
   * converts <code>int[]</code> to <code>String</code>
   * A <code>,</code> is used a separator
   *  <br><pre>{0,1,2,3}  -> "{0,1,2,3}"</pre>
   */

  public static String toString (int[] s) {
   if ((s==null) || (s.length == 0)) return null;
   else{
     String res;
     int i; 
     res = "{";
     for(i=0; i<s.length-1; i++) res += s[i]+",";
     res += s[i];
     res += "}";
     return res;
   }
  }

  /**	
   * converts <code>Vector</code> to <code>String[]</code>
   * <br>
   */

  public static String[] toArray (Vector s) {
   if (s == null) return null;
   String[] res = new String[s.size()];
   for(int i=0; i<s.size(); i++) res[i] = (String)s.elementAt(i);
   return res;
  }

  /**
   * replace <code>Strings</code> in a <code>String</code>.
   *     <br><pre>
   *     "Hello you, where are you?","you","we" 
   *     ->
   *     "Hello we, where are we?"
   *     </pre>
   */


  public static String replace(String original, String oldString, String newString) {
	String result = original;
	StringBuffer sb = new StringBuffer();
	int fromIndex = 0;
	while(true) {
	  int index = result.indexOf(oldString, fromIndex);
	  if (index == -1) break;
	  sb.setLength(0);
	  sb.append(result.substring(0, index));
	  sb.append(newString);
	  fromIndex = sb.length();
	  sb.append(result.substring(index + oldString.length()));
	  result = sb.toString();
	}
	return result;
  }
  
  /** Removes all special characters from a String.
   *  E.g.: !,.$%^&*; <br/>
   *  ASCII: 33-47, 58-64
   * 
   * @param text
   * @return
   */
  public static String removeSpecialCharacter(String text) {
      StringBuffer result = new StringBuffer();
      for (int i = 0; i < text.length(); i++) {
          char currentChar = text.charAt(i);
          if ((currentChar>=33 && currentChar<=47) ||
              (currentChar>=58 && currentChar<=64)) {
              continue;
          } else {
              result.append(currentChar);
          }
                 
      }
      return result.toString();
  }

}

