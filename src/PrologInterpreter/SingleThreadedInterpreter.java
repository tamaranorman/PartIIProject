package PrologInterpreter;

import PrologInterpreter.Structure.Clause;
import PrologInterpreter.Structure.Goal;
import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.Program;
import PrologInterpreter.Structure.TermVarMapping;
import PrologInterpreter.Structure.Trail;
import PrologInterpreter.Utilities.Literals;

public class SingleThreadedInterpreter implements Interpreter {
	/* (non-Javadoc)
	 * @see PrologInterpreter.Interpreter#executeQuery(PrologInterpreter.Structure.GoalMappingPair, PrologInterpreter.Structure.Program)
	 */
	@Override
	public void executeQuery(GoalMappingPair query, Program rules){
		solve(query.getGoal(), rules, query.getMap());
	}
	
	private void solve (Goal goal, Program program, TermVarMapping map){
		String goalAtomName = goal.getHead().getAtom().getAtomName();
		if (Literals.MY_SET.contains(goalAtomName)){
			boolean unifies;
			switch (goalAtomName){
				case "is": 
					unifies = goal.getHead().unifyIs();
					break;
				case "=":
					unifies = goal.getHead().unifyEquals();
					break;
				case "=\\=":
					unifies = goal.getHead().unifyNotEqual();
					break;
				case ">":
					unifies = goal.getHead().unifyGreaterThan();
					break;
				default:
					unifies = false;
					break;
			}
			if (unifies){
				Goal g = goal.getTail();
				if(g == null) {
					map.showAnswer();  
				}
				else{
					solve(g, program, map);
				}
			}
		}
		else {
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
					//System.out.println("false.");
				}
				Trail.Undo(t);
				q = q.getTail();
			}
		}
	}
}
