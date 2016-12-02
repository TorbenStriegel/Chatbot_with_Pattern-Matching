/** Test methods for the class Matcher.java
 * 
 * @author Stephan Baldes
 */
public class MatcherTest {

    public MatcherTest() {
      matchKonstante1();
      matchKonstante2();
      matchKonstante3();
      matchKonstante4();
      matchKonstante5();
      
      matchSegment1();
      matchSegment2();
      matchSegment3();
      matchSegment4();
      matchSegment5();
      matchSegment6();

      matchVariable1();
      matchVariable2();
      matchVariable3();      

      matchSonstiges1();
      matchSonstiges2();
      matchSonstiges3();
      matchSonstiges4();
      matchSonstiges5();
      
    }

    
    public void matchKonstante1() {

     // Eine Konstante   
        
     Matcher m1 = new Matcher();
     boolean match = m1.match("Hallo",
                              "Hallo");
     assertTrue("matchKonstante1",match==true);
    }


    public void matchKonstante2() {

     // Mehrere Konstanten
       
     Matcher m1 = new Matcher();
     boolean match = m1.match("Hallo ich bin Merlin",
                      "Hallo ich bin Merlin");
     assertTrue("matchKonstante2_1",match==true);

      // Mit Sonderzeichen.
      match = m1.match("Hallo ich bin Merlin",
                      "Hallo, ich bin Merlin");
     assertTrue("matchKonstante2_2",match==true);

    }

    public void matchKonstante3() {

     // Mehrere Konstanten, falschen Match erkennen
       
     Matcher m1 = new Matcher();
     boolean match = m1.match("Hallo ich bin Merlin",
                              "Hallo ich bin Berlin");
     assertTrue("matchKonstante3",match==false);
    }


    public void matchKonstante4() {

     // Groß- und Kleinschreibung wird nicht berücksichtigt.
        
     Matcher m1 = new Matcher();
     boolean match = m1.match("Ich heiße Peter",
                              "Ich heiße Peter Pan");
     assertTrue("matchKonstante4",match==false);
    }

    public void matchKonstante5() {

     // Groß- und Kleinschreibung wird nicht berücksichtigt.
        
     Matcher m1 = new Matcher();
     boolean match = m1.match("Hallo",
                              "hallo");
     assertTrue("matchKonstante5",match==true);
    }

    public void matchVariable1() {

     // Eine Variable
        
     Matcher m1 = new Matcher();
     boolean match = m1.match("?name",
                              "Merlin");
     assertTrue("matchVariable1_1",match==true);
     assertTrue("matchVariable1_2",m1.getBinding(new Variable("?name")).equals("Merlin"));
    }

    public void matchVariable2() {

     // Mehrere Variablen
        
     Matcher m1 = new Matcher();
     boolean match = m1.match("?gruss ich bin ?name",
                               "Hallo ich bin Merlin");
     assertTrue("matchVariable2_1",match==true);
     assertTrue("matchVariable2_2",m1.getBinding(new Variable("?gruss")).equals("Hallo"));
     assertTrue("matchVariable2_3",m1.getBinding(new Variable("?name")).equals("Merlin"));
    }

    public void matchVariable3() {

     // Zwei Variablen aufeinanderfolgend
        
     Matcher m1 = new Matcher();
     boolean match = m1.match("?gruss ?wer bin ?name",
                               "Hallo ich bin Merlin");
     assertTrue("matchVariable3_1",match==true);
     assertTrue("matchVariable3_2",m1.getBinding(new Variable("?gruss")).equals("Hallo"));
     assertTrue("matchVariable3_3",m1.getBinding(new Variable("?wer")).equals("ich"));
     assertTrue("matchVariable3_4",m1.getBinding(new Variable("?name")).equals("Merlin"));
    }
    
    public void matchSegment1() {

     // Segment, ganzer String
     
     Matcher m1 = new Matcher();
     boolean match = m1.match("?*x",
                      "Hallo ich bin Merlin");
     assertTrue("matchSegment1_1",match==true);
     assertTrue("matchSegment1_2",m1.getBinding(new Variable("?*x")).equals("Hallo ich bin Merlin"));
    }

    public void matchSegment2() {

     // Segment
     
     Matcher m1 = new Matcher();
     boolean match = m1.match("?*x bin Merlin",
                      "Hallo ich bin Merlin");
     assertTrue("matchSegment2_1",match==true);
     assertTrue("matchSegment2_2",m1.getBinding(new Variable("?*x")).equals("Hallo ich"));
    }
    
    public void matchSegment3() {

     // Segment, leerer Match
     
     Matcher m1 = new Matcher();
     boolean match = m1.match("?*x Ich bin Merlin",
                      "Ich bin Merlin");
     assertTrue("matchSegment3_1",match==true);
     assertTrue("matchSegment3_2",m1.getBinding(new Variable("?*x")).equals(""));     
    }

    public void matchSegment4() {

     // Segment, zwei aufeinander folgende Segmente sind nicht erlaubt!
     
     Matcher m1 = new Matcher();
     boolean match = m1.match("?*y ?*x Ich bin Merlin",
                      "Ich bin Merlin");
     assertTrue("matchSegment4_1",match==false);     
    }
    
    public void matchSegment5() {

     // 2 Segmente
     
     Matcher m1 = new Matcher();
     boolean match = m1.match("?*x bin ?*y",
                      "Ich bin Merlin");
     assertTrue("matchSegment5_1",match==true);     
     assertTrue("matchSegment5_2",m1.getBinding(new Variable("?*x")).equals("Ich"));     
     assertTrue("matchSegment5_3",m1.getBinding(new Variable("?*y")).equals("Merlin"));          
    }

    public void matchSegment6() {
    
     // Segment und Variable
        
     Matcher m1 = new Matcher();
     boolean match = m1.match("?*gruss ?x ?*name",
                               "Hallo ich bin Merlin");
     assertTrue("matchSegment6_1",match==true);
     assertTrue("matchSegment6_2",m1.getBinding(new Variable("?*gruss")).equals(""));
     assertTrue("matchSegment6_3",m1.getBinding(new Variable("?x")).equals("Hallo"));
     assertTrue("matchSegment6_4",m1.getBinding(new Variable("?*name")).equals("ich bin Merlin"));
    }

    public void matchSonstiges1() {
    
      Matcher m1 = new Matcher();
      boolean match = m1.match(
                               "?*alles falsch",
                               "Ich heiße Merlin und bin jetzt ganz schön aufgeregt!");        
     assertTrue("matchSonstiges1",match==false);
    }

    public void matchSonstiges2() {
    
      Matcher m1 = new Matcher();
      boolean match = m1.match(
                               "?*alles aufgeregt",
                               "Ich heiße Merlin und bin jetzt ganz schön aufgeregt!");        
     assertTrue("matchSonstiges2_1",match==true);
     assertTrue("matchSonstiges2_2",m1.getBinding(new Variable("?*alles")).equals("Ich heiße Merlin und bin jetzt ganz schön"));
    }

    public void matchSonstiges3() {
    
      Matcher m1 = new Matcher();
      boolean match = m1.match(
                               "?*pre ich heiße ?name",
                               "ich heiße Merlin");         
     assertTrue("matchSonstiges3_1",match==true);
     assertTrue("matchSonstiges3_2",m1.getBinding(new Variable("?name")).equals("Merlin"));
    }
   
    public void matchSonstiges4() {
    
     // Variable, Ende erkennen
        
     Matcher m1 = new Matcher();
     boolean match = m1.match("ich bin ?name",
                               "ich bin Merlin wie heisst Du");
     assertTrue("matchSonstiges4",match==false);     
    }
   
    public void matchSonstiges5() {
    
     // Variable, Ende erkennen
        
     Matcher m1 = new Matcher();
     boolean match = m1.match("ich bin Merlin",
                               "ich bin Merlin wie heisst Du");
     assertTrue("matchSonstiges5",match==false);     
    }

  public void assertTrue(String testName, boolean value) {
    System.out.println("====================================================");
    if (!value) {
        throw new RuntimeException("Der Test "+testName+" ist fehlgeschlagen!");
    }
    else {System.out.println("Test "+testName+" erfolgreich!");}
    System.out.println("====================================================");
  }   
  
  public static void main(String[] args) {
    new MatcherTest();
  }       
  }
