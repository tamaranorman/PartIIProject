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
		
		return new TermVarMapping(map);
	}
}
