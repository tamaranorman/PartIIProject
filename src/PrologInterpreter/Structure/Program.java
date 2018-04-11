package PrologInterpreter.Structure;

public class Program {
	private final Clause head;
	private Program tail;
	private static Program last = null;
	
	public Program(Clause h){
		head = h;
		if (last != null) {
			last.tail = this;
		}
		last = this;
	}

	public Clause getHead() {
		return head;
	}

	public Program getTail() {
		return tail;
	}
	
	public static Program getLast() {
		return last;
	}
}
