package PrologInterpreter.Structure;

import java.util.HashMap;

public class TermVarMapping {

	private HashMap<String, TermVar> map;

	private int size;
	
	public TermVarMapping(HashMap<String, TermVar> m){
		map = m;
		size = m.size();
	}
	
	public void showAnswer(){
		String[] values = map.keySet().toArray(new String[size]);
		if (size == 0){
			System.out.println("yes");
		}
		else {
			for(int i = 0; i < size-1; i++){
				System.out.println(values[i] + " = " + map.get(values[i]).print() + ",");
			}
			System.out.println(values[size -1] + " = " + map.get(values[size -1]).print() + ";");
		}
	}
}
