package PrologInterpreter;

import PrologInterpreter.Structure.Clause;
import PrologInterpreter.Structure.Goal;
import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.Program;
import PrologInterpreter.Structure.TermVarMapping;

public class StructureSharingInterpreter implements Interpreter{

	@Override
	public void executeQuery(GoalMappingPair query, Program rules) {
		solve(query.getGoal(), rules, query.getMap());
	}
	
	private void solve (Goal goal, Program program, TermVarMapping map){
		Program q = program;
		while (q != null){
			Clause c = q.getHead().deepCopy();
			final Goal g = goal.copy();
			if(g.getHead().unifySharing(c.getHead())){
				Goal h = Goal.append(c.getBody(), goal.getTail());
				if(h == null) {
					map.showAnswer();
				}
				else{
					Thread worker = new Thread() {
						@Override 
						public void run(){
							System.out.println("New thread reached");
							solve(h, program, map);
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
