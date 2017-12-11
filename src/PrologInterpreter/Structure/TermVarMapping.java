package PrologInterpreter.Structure;

import java.util.HashMap;

public class TermVarMapping {

	private HashMap<String, TermVar> map;
	
	public TermVarMapping(){
		map = new HashMap<String, TermVar>();
	}
	
	public void showAnswer(){
		int size = map.size();
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

	public void clear() {
		map.clear();
	}

	public boolean containsKey(String text) {
		return map.containsKey(text);
	}

	public TermVar get(String text) {
		return map.get(text);
	}

	public void put(String text, TermVar var) {
		map.put(text, var);
	}
}
