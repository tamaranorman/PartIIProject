package PrologInterpreter;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import PrologInterpreter.Structure.Clause;
import PrologInterpreter.Structure.Goal;
import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.Program;
import PrologInterpreter.Structure.ReturnStructure;
import PrologInterpreter.Structure.TermVarMapping;
import PrologInterpreter.Structure.UnificationListHolder;
import PrologInterpreter.Utilities.Literals;

public class StructureSharingInterpreterThreadPool implements Interpreter{
	private static Queue<String[]> results;
	private static ThreadPoolExecutor executor;
	int count;

	@Override
	public ReturnStructure executeQuery(GoalMappingPair query, Program rules, HashMap<String, Integer> progDict) {
		results = new LinkedBlockingQueue<String[]>();
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
		count = 0;
		solve(query.getGoal(), rules, query.getMap(), new UnificationListHolder(), progDict);
		while (count > executor.getCompletedTaskCount()) {
			
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
										count++;
										executor.submit(() -> { solve(g, program, map, l, progDict);
																return null;});
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