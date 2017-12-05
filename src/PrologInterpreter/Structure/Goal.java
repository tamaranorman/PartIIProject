package PrologInterpreter.Structure;

public class Goal {
	private final TermCons head;
	private final Goal tail;
	
	public Goal(TermCons h, Goal g){
		head = h;
		tail = g;
	}
	
	
	public static Goal append(Goal l, Goal m){
		if (l == null){
			return m;
		}
		else {
			return new Goal(l.head, append(l.tail, m));
		}
	}
	
	//TODO
	//solve
}
