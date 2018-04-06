package PrologInterpreter;

import java.util.HashMap;

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
	private final static boolean seq = true;
	
	@Override
	public void executeQuery(GoalMappingPair query, Program rules, HashMap<String, Integer> progDict){
		solve(query.getGoal(), rules, query.getMap(), progDict);
	}
	
	private void solve (Goal goal, Program program, TermVarMapping map, HashMap<String, Integer> progDict){
		boolean repeat;
		do {
			repeat = false;
			String goalAtomName = goal.getHead().getAtom().getAtomName();
			if (Literals.MY_SET.contains(goalAtomName)){
				boolean unifies;
				switch (goalAtomName){
					case "is": 
						unifies = goal.getHead().unifyIs(seq);
						break;
					case "=":
						unifies = goal.getHead().unifyEquals(seq);
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
						goal = g;
						repeat = true;
					}
				}
			}
			else {
				Program q = program;
				String name = goal.getHead().getAtom().getAtomName();
				if (progDict.containsKey(name)) {
					int i = progDict.get(name);
					while (q != null && i > 0){
						Trail t = Trail.note();
						Clause c = q.getHead().copy();
						if (c.getHead().getAtom().getAtomName().equals(name)) {
							i--;
						}
						Trail.undo(t);
						if(goal.getHead().unify(c.getHead(), seq)){
							Goal g = Goal.append(c.getBody(), goal.getTail());
							if(g == null) {
								map.showAnswer();
							}
							else{
								if (q.getTail() == null){
									goal = g;
									repeat = true;
								}
								else {
									solve(g, program, map, progDict);
								}
							}
						}
						if (!repeat){
							Trail.undo(t);
						}
						q = q.getTail();
					}
				}
			}
		} while (repeat);
	}
}
