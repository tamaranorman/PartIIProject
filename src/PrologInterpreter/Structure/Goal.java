package PrologInterpreter.Structure;

public class Goal {
	private final TermCons head;
	private final Goal tail;
	
	public Goal(TermCons h, Goal t){
		head = h;
		tail = t;
	}
	
	public Goal copy(){
		return new Goal(head.copyCons(), tail == null ? null : tail.copy());
	}
	
	
	public static Goal append(Goal l, Goal m){
		if (l == null){
			return m;
		}
		else {
			return new Goal(l.head, append(l.tail, m));
		}
	}
	
	public void solve(Program p, TermVarMapping map){
		Program q = p;
		while (q != null){
			Trail t = Trail.note();
			Clause c = q.getHead().copy();
			//undo to last point to get new solutions
			//not sure need this
			Trail.Undo(t);
			if(head.unify(c.getHead())){
				Goal g = append(c.getBody(), tail);
				if(g == null) {
					map.showAnswer();
				}
				else{
					solve(p, map);
				}
			}
			Trail.Undo(t);
			
			q = q.getTail();
		}
	}
}
