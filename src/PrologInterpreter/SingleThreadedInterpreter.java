package PrologInterpreter;

import java.util.HashMap;

import PrologInterpreter.Structure.Clause;
import PrologInterpreter.Structure.Goal;
import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.Program;
import PrologInterpreter.Structure.TermVar;
import PrologInterpreter.Structure.TermVarMapping;
import PrologInterpreter.Structure.Trail;

public class SingleThreadedInterpreter implements Interpreter {
	/* (non-Javadoc)
	 * @see PrologInterpreter.Interpreter#executeQuery(PrologInterpreter.Structure.GoalMappingPair, PrologInterpreter.Structure.Program)
	 */
	@Override
	public void executeQuery(GoalMappingPair query, Program rules){
		HashMap<String, TermVar> m = query.getMap();
		int size = m.size();
		String[] values = m.keySet().toArray(new String[size]);
		TermVar[] vars = new TermVar[values.length];
		for(int i = 0; i < size; i++){
			vars[i] = m.get(values[i]);
		}
		solve(query.getGoal(), rules, new TermVarMapping(vars, values, values.length));
	}
	
	private void solve (Goal goal, Program program, TermVarMapping map){
		Program q = program;
		while (q != null){
			Trail t = Trail.note();
			Clause c = q.getHead().copy();
			Trail.Undo(t);
			if(goal.getHead().unify(c.getHead())){
				Goal g = Goal.append(c.getBody(), goal.getTail());
				if(g == null) {
					map.showAnswer();
				}
				else{
					solve(g, program, map);
				}
			}
			else{
				System.out.println("false.");
			}
			Trail.Undo(t);
			
			q = q.getTail();
		}
	}
}
