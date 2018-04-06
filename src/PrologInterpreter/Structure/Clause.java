package PrologInterpreter.Structure;

import java.util.HashMap;

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
	
	/*public String print() {
		String s = head.print();
		if (body == null){
			s += " :- true";
		}
		else {
			s += " :- " + body.print();
		}
		return s;
	}*/

	public Clause deepCopy() {
		HashMap<Term, Term> map = new HashMap<>();
		return new Clause(head.deepCopyCons(map), body == null ? null : body.deepCopy(map));
	}
}
