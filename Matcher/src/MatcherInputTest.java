import javax.swing.JOptionPane;

public class MatcherInputTest {
	private String[] pattern ={"Hallo","Tschüss","Wie geht es dir","Ich bin ?name"};
	private String[] answers={"Hallo","Einen schönen Tag noch.","Mir geht es gut und dir?","Hallo ?name" };
	private Matcher m = new Matcher();
	private String input;
	private boolean verstanden= false;
	private String[][] bindings;
	private String variable;
	private boolean antwort_gegeben=false;
	public MatcherInputTest(){
		matchen();
	}
	
	public void matchen(){
		while(true){
			verstanden=false;
			antwort_gegeben=false;
			input =JOptionPane.showInputDialog(null);
			for (int i = 0; i < pattern.length; i++) {
				if(input.equals("")){
					break;
				}
				if(m.match(pattern[i], input)){
					bindings=m.printBindings();
					for (int j = 0; j < bindings.length; j++) {
						if (pattern[i].contains(bindings[j][0])) {
							variable=bindings[j][1];
						}	
					}
					for (int j = 0; j < bindings.length; j++) {
						if (answers[i].contains(bindings[j][0])) {
							String s = answers[i];
							String a =s.replace(bindings[j][0],variable);
							JOptionPane.showMessageDialog(null, a);
							antwort_gegeben=true;
						}	
					}
					if(!antwort_gegeben){
					JOptionPane.showMessageDialog(null, answers[i]);
					}
					verstanden=true;
				}
			}
			
			
			
			
			
			
			
			
			if(input.equalsIgnoreCase("exit")){
				break;
			}
			if(!verstanden){
				double i = Math.random()*10 +1;
				if(i<5){
					JOptionPane.showMessageDialog(null, "Ich habe dich nicht verstanden.");
				}else{
					JOptionPane.showMessageDialog(null, "Andere Eingabe.");
				}
				
			}
			
			
		}
	}
	
	public static void main(String[] args) {
		new MatcherInputTest();
	}
}
