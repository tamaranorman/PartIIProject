package PrologInterpreter.Structure;

public class Clause {
	private final TermCons head;
	private final Goal body;
	
	public Clause(TermCons h, Goal b){
		head = h;
		body = b;
	}
	
	public Clause copy(){
		return new Clause(head.copyCons(), body == null ? null : body.copy());
	}
	
	public TermCons getHead(){
		return head;
	}
	
	public Goal getBody() {
		return body;
	}
}
