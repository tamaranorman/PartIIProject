package PrologInterpreter.Structure;

import java.util.HashMap;

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
	
	public Goal deepCopy(HashMap<Term, Term> map) {
		return new Goal(head.deepCopyCons(map), tail == null ? null : tail.deepCopy(map));
	}
	
	public static Goal append(Goal l, Goal m){
		if (l == null){
			return m;
		}
		else {
			return new Goal(l.head, append(l.tail, m));
		}
	}
	
	public TermCons getHead() {
		return head;
	}

	public Goal getTail() {
		return tail;
	}
}
