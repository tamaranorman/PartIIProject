package PrologInterpreter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import PrologInterpreter.Structure.Clause;
import PrologInterpreter.Structure.Goal;
import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.Program;
import PrologInterpreter.Structure.Term;
import PrologInterpreter.Structure.TermVarMapping;

public class CopyWhenSpanningInterpreter implements Interpreter {
	private List<Thread> threads = new LinkedList<Thread>();
	
	@Override
	public void executeQuery(GoalMappingPair query, Program rules) {
		threads = new LinkedList<Thread>();
		solve(query.getGoal(), rules, query.getMap());
		try {
			for (int i = 0; i < threads.size(); i++){
				threads.get(i).join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void solve(Goal goal, final Program program, TermVarMapping map) {
		Program q = program;
		while (q != null){
			if (goal.getHead().canUnify(q.getHead().getHead())){
				Clause c = q.getHead().deepCopy();
				final TermVarMapping m;
				final Goal g;
				if (q.getTail() != null){
					HashMap<Term, Term> pairs = new HashMap<>();
					m = new TermVarMapping(map, pairs);
					g = goal.deepCopy(pairs);
				}
				else
				{
					m = map;
					g = goal;
				}
				if(g.getHead().unify(c.getHead())){
					final Goal h = Goal.append(c.getBody(), g.getTail());
					if(h == null) {
						m.showAnswer();
					}
					else{
						Thread worker = new Thread() {
							@Override 
							public void run(){
								solve(h, program, m);
							}
						};
						worker.start();
						threads.add(worker);
					}
				}
			}
			else{
				System.out.println("false.");
			}
			
			q = q.getTail();
		}
	
	}

}
