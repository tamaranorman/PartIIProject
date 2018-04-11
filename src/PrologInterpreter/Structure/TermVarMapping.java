package PrologInterpreter.Structure;

import java.util.HashMap;
import java.util.Set;

public class TermVarMapping {

	private HashMap<String, TermVar> map;
	
	public TermVarMapping(){
		map = new HashMap<String, TermVar>();
	}
	
	public TermVarMapping(TermVarMapping m, HashMap<Term, Term> pairs) {
		map = new HashMap<String, TermVar>();
		String[] values = m.keySet().toArray(new String[m.size()]);
		for(String v : values){
			TermVar value = m.get(v);
			map.put(v,((TermVar) value.deepCopy(pairs)));
		}
	}

	private int size() {
		return map.size();
	}

	private Set<String> keySet() {
		return map.keySet();
	}

	public String[] showAnswer(){
		int size = map.size();
		String[] result;
		if (size == 0){
			result = new String[] {"true."};
		}
		else {
			String[] values = map.keySet().toArray(new String[size]);
			result = new String[size];
			for(int i = 0; i < size-1; i++){
				result[i] = values[i] + " = " + map.get(values[i]).print() + ",";
			}
			result[size-1] = values[size -1] + " = " + map.get(values[size -1]).print() + ";";
		}
		return result;
	}
	
	public String[] showAnswer(UnificationListHolder holder) {
		int size = map.size();
		String[] result;
		if (size == 0){
			result = new String[] {"true."};
		}
		else {
			String[] values = map.keySet().toArray(new String[size]);
			result = new String[size];
			for(int i = 0; i < size-1; i++){
				result[i] = values[i] + " = " + map.get(values[i]).print(holder.getList(), holder) + ",";
			}
			result[size-1] = values[size -1] + " = " + map.get(values[size -1]).print(holder.getList(), holder) + ";";
		}
		return result;
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
