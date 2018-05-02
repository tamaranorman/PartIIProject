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

public class CopyWhenSpanningInterpreter implements Interpreter {
	private final static boolean seq = false;
	private BlockingQueue<Thread> threads;
	private static Queue<String[]> results;
	
	@Override
	public ReturnStructure executeQuery(GoalMappingPair query, Program rules, HashMap<String, Integer> progDict) {
		threads = new LinkedBlockingQueue<Thread>();
		results = new LinkedBlockingQueue<String[]>();
		solve(query.getGoal(), rules, query.getMap(), progDict);
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

	private void solve(Goal goal, final Program program, TermVarMapping map, HashMap<String, Integer> progDict) {
		boolean repeat = false;
		do {
			repeat = false;
			String goalAtomName = goal.getHead().getAtom().getAtomName();
			if (Literals.arithmetic_operators.contains(goalAtomName)){
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
						results.add(map.showAnswer());  
					}
					else{
						goal = g;
						repeat = true;
					}
				}
			}
			else{
				Program q = program;
				if (progDict.containsKey(goalAtomName)) {
					int i = progDict.get(goalAtomName);
					while (q != null && !repeat){
						if (q.getHead().getHead().getAtom().getAtomName().equals(goalAtomName)) {
							i--;
						}
						if (goal.getHead().canUnify(q.getHead().getHead())){
							Clause c = q.getHead().deepCopy();
							final TermVarMapping m;
							final Goal g;
							if (q.getTail() != null && i != 0){
								HashMap<Term, Term> pairs = new HashMap<>();
								m = new TermVarMapping(map, pairs);
								g = goal.deepCopy(pairs);
							}
							else
							{
								m = map;
								g = goal;
							}
							if(c.getHead().unify(g.getHead(), seq)){
								final Goal h = Goal.append(c.getBody(), g.getTail());
								if(h == null) {
									results.add(m.showAnswer());
								}
								else{
									if (q.getTail() == null || i == 0){
										goal = h;
										map = m;
										repeat = true;
									}
									else {  
										Thread worker = new Thread() {
											@Override 
											public void run(){
												solve(h, program, m, progDict);
											}
										};
										threads.add(worker);
										worker.start();
									}
								}
							}
						}
						q = q.getTail();
					}
				}
			}
		}while(repeat);
	}
}