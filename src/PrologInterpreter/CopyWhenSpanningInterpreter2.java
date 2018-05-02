package PrologInterpreter;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import PrologInterpreter.Structure.Clause;
import PrologInterpreter.Structure.Goal;
import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.Program;
import PrologInterpreter.Structure.ReturnStructure;
import PrologInterpreter.Structure.Term;
import PrologInterpreter.Structure.TermVarMapping;
import PrologInterpreter.Utilities.Literals;

public class CopyWhenSpanningInterpreter2 implements Interpreter {
	private final static boolean seq = false;
	private BlockingQueue<Thread> threads;
	private static Queue<String[]> results;
	
	@Override
	public ReturnStructure executeQuery(GoalMappingPair query, Program rules, HashMap<String, Integer> progDict) {
		threads = new LinkedBlockingQueue<Thread>();
		results = new LinkedBlockingQueue<String[]>();
		solve(query.getGoal(), rules, query.getMap());
		int threadCount = 0;
		try {
			while(threads.size() != 0) {
				threads.take().join();
				threadCount++;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new ReturnStructure(results.size() != 0 ? results : Literals.falseQuery, threadCount);
	}

	private void solve(Goal goal, final Program program, TermVarMapping map) {
		Program q = program;
		while (q != null){
			Clause c = q.getHead().deepCopy();
			final TermVarMapping m;
			HashMap<Term, Term> pairs = new HashMap<>();
			m = new TermVarMapping(map, pairs);
			final Goal g = goal.deepCopy(pairs);
			if(g.getHead().unify(c.getHead(), seq)){
				final Goal h = Goal.append(c.getBody(), g.getTail());
				if(h == null) {
					results.add(m.showAnswer());
				}
				else{
					Thread worker = new Thread() {
						@Override 
						public void run(){
							solve(h, program, m);
						}
					};
					threads.add(worker);
					worker.start();
				}
			}
			q = q.getTail();
		}
	}
}

