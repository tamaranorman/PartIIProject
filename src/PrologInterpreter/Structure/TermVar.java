package PrologInterpreter.Structure;

public class TermVar extends Term {
	private static int timeStamp;
	private final int varNo;
	private Term instance;
	
	public TermVar(){
		instance = this;
		varNo = timeStamp++;
	}

	@Override
	public
	boolean unify(Term t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public
	Term copy() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Term reset(){
		instance = this;
		return this;
	}
}
