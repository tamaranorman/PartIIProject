package PrologInterpreter.Structure;

public class TermVarMapping {

	private TermVar[] vVar;
	private String[] vText;
	private int size;
	
	public TermVarMapping(TermVar[] v, String[] t, int s){
		vVar = v;
		vText = t;
		size = s;
	}
	
	public void showAnswer(){
		if (size == 0){
			System.out.println("yes");
		}
		else {
			for(int i = 0; i < size-1; i++){
				System.out.println(vText[i] + " = " + vVar[i].print() + ",");
			}
			System.out.println(vText[size -1] + " = " + vVar[size -1].print() + ";");
		}
	}
}
