package PrologInterpreter;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import PrologInterpreter.Structure.Clause;
import PrologInterpreter.Structure.Goal;
import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.Program;
import PrologInterpreter.Structure.TermVarMapping;
import PrologInterpreter.Structure.UnificationListHolder;
import PrologInterpreter.Utilities.Literals;

public class StructureSharingInterpreter implements Interpreter{
	private BlockingQueue<Thread> threads;
	private static Queue<String[]> results;

	@Override
	public Queue<String[]> executeQuery(GoalMappingPair query, Program rules, HashMap<String, Integer> progDict) {
		threads = new LinkedBlockingQueue<Thread>();
		results = new LinkedBlockingQueue<String[]>();
		solve(query.getGoal(), rules, query.getMap(), new UnificationListHolder(), progDict);
		try {
			while(threads.size() != 0) {
				threads.take().join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return results.size() != 0 ? results : Literals.falseQuery;
	}
	
	private void solve (Goal goal, Program program, TermVarMapping map, UnificationListHolder list, HashMap<String, Integer> progDict){
		boolean repeat = false;
		do{
			repeat = false;
			String goalAtomName = goal.getHead().getAtom().getAtomName();
			if (Literals.MY_SET.contains(goalAtomName)){
				boolean unifies;
				switch (goalAtomName){
					case "is": 
						unifies = goal.getHead().unifyIsSharing(list);
						break;
					case "=":
						unifies = goal.getHead().unifyEqualsSharing(list);
						break;
					case "=\\=":
						unifies = goal.getHead().unifyNotEqualSharing(list);
						break;
					case ">":
						unifies = goal.getHead().unifyGreaterThanSharing(list);
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
			else {
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
							UnificationListHolder l = new UnificationListHolder(list.getList());
							if(goal.getHead().unifySharing(c.getHead(), l, l.getList())){
								Goal g = Goal.append(c.getBody(), goal.getTail());
								if(g == null) {
									results.add(map.showAnswer(l));
								}
								else{
									if (q.getTail() == null || i == 0){
										goal = g;
										list = l;
										repeat = true;
									}
									else {
										Thread worker = new Thread() {
											@Override 
											public void run(){
												solve(g, program, map, l, progDict);
											}
										};
										worker.start();
										threads.add(worker);
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