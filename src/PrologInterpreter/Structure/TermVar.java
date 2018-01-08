package PrologInterpreter.Structure;

import java.util.HashMap;

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
	public Term spawnCopy(TermVarMapping m) {
		TermVar t;
		if (instance == this){
			t = new TermVar(varNo);
		}
		else{
			t = new TermVar(instance.spawnCopy(m), varNo);
		}
		m.replace(this, t);
		return t;
	}

	@Override
	public Term deepCopy(HashMap<Term, Term> map) {
		if (map.containsKey(this)){
			return map.get(this);
		}
		if (instance == this){
			TermVar n = new TermVar();
			map.put(this, n);
			return n;
		}
		return new TermVar(instance.deepCopy(map), timeStamp++);
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

	public boolean equalsVar(TermVar termVar) {
		if (this == termVar){
			return true;
		}
		if (instance == this && termVar.instance == termVar){
			return (this == termVar);
		}
		if (instance == this && termVar.instance instanceof TermCons){
			return ((TermCons)(termVar.instance)).contains((TermVar)instance);
		}
		if (instance == this){
			return (termVar.equalsVar(this));
		}
		if (instance instanceof TermCons && termVar.instance instanceof TermCons){
			return (instance == termVar.instance);
		}
		if (instance instanceof TermCons){
			return termVar.equalsVar(this);
		}
		if (instance instanceof TermVar){
			return ((TermVar)instance).equalsVar(termVar);
		}
		return false;
	}

	public boolean isUnunified() {
		return (instance == this);
	}

	@Override
	public void replace(TermVar termVar, TermVar newTerm) {
		if (instance != this){
			instance.replace(termVar, newTerm);
		}
	}
}
