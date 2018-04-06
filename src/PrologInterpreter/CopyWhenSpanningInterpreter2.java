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

public class CopyWhenSpanningInterpreter2 implements Interpreter {
	private final static boolean seq = false;
	private List<Thread> threads = new LinkedList<Thread>();
	
	@Override
	public void executeQuery(GoalMappingPair query, Program rules, HashMap<String, Integer> progDict) {
		threads = new LinkedList<Thread>();
		solve(query.getGoal(), rules, query.getMap());
		try {
			for (int i = 0; i < threads.size(); i++){
				threads.get(i).join();
			}
			System.out.println("All joined");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void solve(Goal goal, final Program program, TermVarMapping map) {
		Program q = program;
		while (q != null){
			if (goal.getHead().canUnify(q.getHead().getHead())){
				Clause clause = q.getHead();
				Thread worker = new Thread(){
					@Override
					public void run(){
						Clause c = clause.deepCopy();
						final TermVarMapping m;
						final Goal g;
						if (true){
							HashMap<Term, Term> pairs = new HashMap<>();
							m = new TermVarMapping(map, pairs);
							g = goal.deepCopy(pairs);
						}
						/*else
						{
							m = map;
							g = goal;
						}*/
						if(g.getHead().unify(c.getHead(), seq)){
							final Goal h = Goal.append(c.getBody(), g.getTail());
							if(h == null) {
								m.showAnswer();
							}
							else{
								solve(h, program, m);
							}
						}
					}
				};
				worker.start();
				threads.add(worker);
			}
			q = q.getTail();
		}
	
	}

}
