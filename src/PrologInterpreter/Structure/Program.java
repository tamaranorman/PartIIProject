package PrologInterpreter.Structure;

public class Program {
	private final Clause head;
	private final Program tail;
	
	public Program(Clause h, Program t){
		head = h;
		tail = t;
	}

	public Clause getHead() {
		return head;
	}

	public Program getTail() {
		return tail;
	}
}
