package PrologInterpreter.Structure;

public class GoalMappingPair {
	private final Goal goal;
	private final TermVarMapping map;
	
	public GoalMappingPair (Goal g, TermVarMapping m){
		goal = g;
		map = m;
	}

	public Goal getGoal() {
		return goal;
	}

	public TermVarMapping getMap() {
		return map;
	}
}
