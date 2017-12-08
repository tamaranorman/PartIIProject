package PrologInterpreter.Structure;

import java.util.HashMap;

public class GoalMappingPair {
	private final Goal goal;
	private final HashMap<String, TermVar> map;
	
	public GoalMappingPair (Goal g, HashMap<String, TermVar> m){
		goal = g;
		map = m;
	}

	public Goal getGoal() {
		return goal;
	}

	public TermVarMapping getMap() {
		int size = map.size();
		String[] values = map.keySet().toArray(new String[size]);
		TermVar[] vars = new TermVar[values.length];
		for(int i = 0; i < size; i++){
			vars[i] = map.get(values[i]);
		}
		return new TermVarMapping(vars, values, values.length);
	}
}
