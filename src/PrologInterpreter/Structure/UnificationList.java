package PrologInterpreter.Structure;

public class UnificationList {
	private final TermVar var;
	private final Term value;
	private final UnificationList prev;
	
	public UnificationList (TermVar v1, Term v2, UnificationList p){
		var = v1;
		value = v2;
		prev = p;
	}
	
	public UnificationList (){
		var = null;
		value = null;
		prev = null;
	}

	public TermVar getVar() {
		return var;
	}

	public Term getValue() {
		return value;
	}

	public UnificationList getPrev() {
		return prev;
	}
}
