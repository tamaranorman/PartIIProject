package PrologInterpreter.Structure;

public class TermVar extends Term {
	private static int timeStamp;
	private final int varNo;
	private Term instance;
	
	public TermVar(){
		instance = this;
		varNo = timeStamp++;
	}
	
	public TermVar(int n){
		instance = this;
		varNo = n;
	}
	
	public TermVar(Term i, int n) {
		instance = i;
		varNo = n;
	}

	@Override
	public boolean unify(Term t) {
		//if already unified
		if (instance != this){
			return instance.unify(t);
		}
		else {
			Trail.Push(this);
			instance = t;
			return true;
		}
	}

	@Override
	public Term copy() {
		if(instance == this){
			Trail.Push(this);
			instance = new TermVar();
		}
		return instance;
	}

	@Override
	public Term spawnCopy() {
		if (instance == this){
			return new TermVar(varNo);
		}
		return new TermVar(instance.spawnCopy(), varNo);
	}
	
	public Term reset(){
		instance = this;
		return this;
	}

	public int getVarNo() {
		return varNo;
	}

	@Override
	public String print() {
		if (instance != this){
			return instance.print();
		}
		else{
			return "_" + varNo;
		}
	}
}
