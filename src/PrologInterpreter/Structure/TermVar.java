package PrologInterpreter.Structure;

public class TermVar extends Term {
	static int timeStamp;
	final int varNo;
	Term instance;
	
	public TermVar(){
		instance = this;
		varNo = timeStamp++;
	}

	@Override
	boolean unify(Term t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	Term copy() {
		// TODO Auto-generated method stub
		return null;
	}
	
	Term reset(){
		instance = this;
		return this;
	}
}
