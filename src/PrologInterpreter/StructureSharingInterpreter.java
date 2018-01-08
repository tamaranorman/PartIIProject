package PrologInterpreter;

import PrologInterpreter.Structure.Clause;
import PrologInterpreter.Structure.Goal;
import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.Program;
import PrologInterpreter.Structure.TermVarMapping;
import PrologInterpreter.Structure.UnificationList;

public class StructureSharingInterpreter implements Interpreter{

	@Override
	public void executeQuery(GoalMappingPair query, Program rules) {
		solve(query.getGoal(), rules, query.getMap(), new UnificationList());
	}
	
	private void solve (Goal goal, Program program, TermVarMapping map, UnificationList list){
		Program q = program;
		while (q != null){
			Clause c = q.getHead().deepCopy();
			final Goal g = goal.copy();
			UnificationList extendedList = g.getHead().unifySharing(c.getHead(), list);
			if(extendedList != list){
				Goal h = Goal.append(c.getBody(), goal.getTail());
				if(h == null) {
					map.showAnswer(extendedList);
				}
				else{
					Thread worker = new Thread() {
						@Override 
						public void run(){
							System.out.println("New thread reached");
							solve(h, program, map, extendedList);
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
