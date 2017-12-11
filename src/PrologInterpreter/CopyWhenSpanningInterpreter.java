package PrologInterpreter;

import PrologInterpreter.Structure.Clause;
import PrologInterpreter.Structure.Goal;
import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.Program;
import PrologInterpreter.Structure.TermVarMapping;

public class CopyWhenSpanningInterpreter implements Interpreter {

	@Override
	public void executeQuery(GoalMappingPair query, Program rules) {
		solve(query.getGoal(), rules, query.getMap());
	}

	private void solve(Goal goal, Program program, TermVarMapping map) {
		Program q = program;
		while (q != null){
			Clause c = q.getHead().copy();
			TermVarMapping m = new TermVarMapping(map);
			final Goal g = goal.spawnCopy(m);
			//need to now edit map
			if(g.getHead().unify(c.getHead())){
				final Goal h = Goal.append(c.getBody(), g.getTail());
				if(h == null) {
					m.showAnswer();
				}
				else{
					Thread worker = new Thread() {
						@Override 
						public void run(){
							System.out.println("New thread reached");
							solve(h, program, m);
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
