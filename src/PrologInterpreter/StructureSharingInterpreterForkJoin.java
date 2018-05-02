package PrologInterpreter;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.LinkedBlockingQueue;

import PrologInterpreter.Structure.Clause;
import PrologInterpreter.Structure.Goal;
import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.Program;
import PrologInterpreter.Structure.ReturnStructure;
import PrologInterpreter.Structure.TermVarMapping;
import PrologInterpreter.Structure.UnificationListHolder;
import PrologInterpreter.Utilities.Literals;

public class StructureSharingInterpreterForkJoin implements Interpreter{
	private BlockingQueue<ForkJoinTask<?>> tasks;
	private static Queue<String[]> results;
	private static ForkJoinPool executor;

	@Override
	public ReturnStructure executeQuery(GoalMappingPair query, Program rules, HashMap<String, Integer> progDict) {
		tasks = new LinkedBlockingQueue<ForkJoinTask<?>>();
		results = new LinkedBlockingQueue<String[]>();
		executor = new ForkJoinPool();
		solve(query.getGoal(), rules, query.getMap(), new UnificationListHolder(), progDict);
		
		int count = 0;
		try {
			while(tasks.size() != 0) {
				tasks.take().join();
				count++;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executor.shutdown();
		return new ReturnStructure(results.size() != 0 ? results : Literals.falseQuery, count);
	}
	
	private void solve (Goal goal, Program program, TermVarMapping map, UnificationListHolder list, HashMap<String, Integer> progDict){
		boolean repeat = false;
		do{
			repeat = false;
			String goalAtomName = goal.getHead().getAtom().getAtomName();
			if (Literals.arithmetic_operators.contains(goalAtomName)){
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
						results.add(map.showAnswer(list));  
					}
					else{
						goal = g;
						repeat = true;
					}
				}
			}
			else {
				Program q = program;
				if (progDict.containsKey(goalAtomName)) {
					int i = progDict.get(goalAtomName);
					while (q != null && !repeat){
						if (q.getHead().getHead().getAtom().getAtomName().equals(goalAtomName)) {
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
										tasks.add(executor.submit(() -> { solve(g, program, map, l, progDict);
										return null;}));
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