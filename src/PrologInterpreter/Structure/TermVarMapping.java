package PrologInterpreter.Structure;

import java.util.HashMap;
import java.util.Set;

public class TermVarMapping {

	private HashMap<String, TermVar> map;
	
	public TermVarMapping(){
		map = new HashMap<String, TermVar>();
	}
	
	public TermVarMapping(TermVarMapping m) {
		map = new HashMap<String, TermVar>();
		String[] values = m.keySet().toArray(new String[m.size()]);
		for(String v : values){
			map.put(v, m.get(v));
		}
	}

	private int size() {
		return map.size();
	}

	private Set<String> keySet() {
		return map.keySet();
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

	public void replace(TermVar termVar, TermVar t) {
		int size = map.size();
		String[] values = map.keySet().toArray(new String[size]);
		for(String v : values){
			if(map.get(v) == termVar){
				map.replace(v, t);
			}
		}
	}

	public boolean containsValue(TermVar termVar) {
		int size = map.size();
		String[] values = map.keySet().toArray(new String[size]);
		for(String v : values){
			if(map.get(v) == termVar){
				return true;
			}
		}
		return false;
	}
}
