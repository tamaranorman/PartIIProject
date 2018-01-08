package PrologInterpreter;

import PrologInterpreter.Structure.Clause;
import PrologInterpreter.Structure.Goal;
import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.Program;
import PrologInterpreter.Structure.TermVarMapping;
import PrologInterpreter.Structure.UnificationListHolder;

public class StructureSharingInterpreter implements Interpreter{

	@Override
	public void executeQuery(GoalMappingPair query, Program rules) {
		solve(query.getGoal(), rules, query.getMap(), new UnificationListHolder());
	}
	
	private void solve (Goal goal, Program program, TermVarMapping map, UnificationListHolder list){
		Program q = program;
		while (q != null){
			Clause c = q.getHead().deepCopy();
			final Goal g = goal.shallowCopy();
			UnificationListHolder l = new UnificationListHolder(list.getList());
			if(g.getHead().unifySharing(c.getHead(), l, l.getList())){
				Goal h = Goal.append(c.getBody(), goal.getTail());
				if(h == null) {
					map.showAnswer(l);
				}
				else{
					Thread worker = new Thread() {
						@Override 
						public void run(){
							System.out.println("New thread reached");
							solve(h, program, map, l);
						}
					};
					worker.start();
				}
			}
			else{
				System.out.println("false.");
			}
			
			q = q.getTail();
		}
	}

}
