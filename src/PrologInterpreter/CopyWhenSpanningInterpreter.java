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
import PrologInterpreter.Utilities.Literals;

public class CopyWhenSpanningInterpreter implements Interpreter {
	private List<Thread> threads = new LinkedList<Thread>();
	
	@Override
	public void executeQuery(GoalMappingPair query, Program rules, HashMap<String, Integer> progDict) {
		threads = new LinkedList<Thread>();
		solve(query.getGoal(), rules, query.getMap(), progDict);
		try {
			for (int i = 0; i < threads.size(); i++){
				threads.get(i).join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void solve(Goal goal, final Program program, TermVarMapping map, HashMap<String, Integer> progDict) {
		boolean repeat = false;
		do {
			repeat = false;
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
						goal = g;
						repeat = true;
					}
				}
			}
			else{
				Program q = program;
				String name = goal.getHead().getAtom().getAtomName();
				if (progDict.containsKey(name)) {
					int i = progDict.get(name);
					while (q != null){
						if (q.getHead().getHead().getAtom().getAtomName().equals(name)) {
							i--;
						}
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
						else{
							//System.out.println("false.");
						}

						q = q.getTail();
					}
				}
			}
		}while(repeat);
	}
}
