package PrologInterpreter;

import java.util.LinkedList;
import java.util.List;

import PrologInterpreter.Structure.Clause;
import PrologInterpreter.Structure.Goal;
import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.Program;
import PrologInterpreter.Structure.TermVarMapping;
import PrologInterpreter.Structure.UnificationListHolder;

public class StructureSharingInterpreter implements Interpreter{
	private List<Thread> threads = new LinkedList<Thread>();

	@Override
	public void executeQuery(GoalMappingPair query, Program rules) {
		threads = new LinkedList<Thread>();
		solve(query.getGoal(), rules, query.getMap(), new UnificationListHolder());
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
	
	private void solve (Goal goal, Program program, TermVarMapping map, UnificationListHolder list){
		Program q = program;
		while (q != null){
			Clause c = q.getHead().deepCopy();
			final Goal g = goal.shallowCopy();
			UnificationListHolder l = new UnificationListHolder(list.getList());
			if(g.getHead().unifySharing(c.getHead(), l, l.getList())){
				Goal h = Goal.append(c.getBody(), goal.getTail());
				if(h == null) {
					map.showAnswer(l);
				}
				else{
					Thread worker = new Thread() {
						@Override 
						public void run(){
							System.out.println("New thread reached");
							solve(h, program, map, l);
						}
					};
					worker.start();//add all to list
					threads.add(worker);
				}
			}
			else{
				System.out.println("false.");
			}
			
			q = q.getTail();
		}
	}

}
