package PrologInterpreter.Structure;

import java.util.HashMap;

public class TermVar extends Term {
	private static int timeStamp;
	private final int varNo;
	private Term instance;
	
	public TermVar(){
		super(true);
		instance = this;
		varNo = timeStamp++;
	}
	
	public TermVar(int n){
		super(true);
		instance = this;
		varNo = n;
	}
	
	public TermVar(Term i, int n) {
		super(true);
		instance = i;
		varNo = n;
	}

	@Override
	public boolean unify(Term t, boolean seq) {
		//if already unified
		if (instance != this){
			return instance.unify(t, seq);
		}
		else {
			if (seq == false && !t.containsVar) {
				this.containsVar = false;
			}
			if (seq) {
				Trail.push(this);
			}
			instance = t;
			return true;
		}
	}
	
	@Override
	public boolean unifySharing(Term t, UnificationListHolder holder, UnificationList list) {
		while(list.getPrev() != null) {
			if(this.equalsVar(list.getVar())){
				return list.getValue().unifySharing(t, holder, holder.getList());
			}
			list = list.getPrev();
		}
		holder.addToList(this, t);
		return true;
	}

	@Override
	public Term copy() {
		if(instance == this){
			Trail.push(this);
			instance = new TermVar();
		}
		return instance;
	}

	@Override
	public Term deepCopy(HashMap<Term, Term> map) {
		if (!this.containsVar) {
			return this;
		}
		if (map.containsKey(this)){
			return map.get(this);
		}
		if (instance == this){
			TermVar n = new TermVar();
			map.put(this, n);
			return n;
		}
		if (!instance.containsVar) {
			this.containsVar = false;
			return instance;
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
	
	@Override
	public String print(UnificationList list, UnificationListHolder fixedList) {
		if(list.getVar() == null){
			return "_" + varNo;
		}
		if (list.getVar().getVarNo() == varNo){
			return list.getValue().print(fixedList.getList(), fixedList);
		}
		else{
			return print(list.getPrev(), fixedList);
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
	
	public boolean isUnifiedCons() {
		if (instance != this){
			if (instance instanceof TermCons){
				return true;
			}
			else{
				return ((TermVar) instance).isUnifiedCons();
			}
		}
		return false;
	}
	
	public boolean isUnifiedSharingCons(UnificationListHolder holder, UnificationList list) {
		if (list.getVar() != null){
			if (list.getVar() == this){
				if (list.getValue() instanceof TermCons){
					return true;
				}
				((TermVar)list.getValue()).isUnifiedSharingCons(holder, holder.getList());
			}
		}
		else if (list.getPrev() != null){
			return isUnifiedSharingCons(holder,  list.getPrev());
		}
		return false;
	}

	@Override
	public void replace(TermVar termVar, TermVar newTerm) {
		if (instance != this){
			instance.replace(termVar, newTerm);
		}
	}

	@Override
	public int evaluate() {
		return instance.evaluate();
	}

	@Override
	public int evaluateSharing(UnificationListHolder holder, UnificationList list) {
		if (list.getVar() != null && list.getVar().getVarNo() == varNo){
			return list.getValue().evaluateSharing(holder, holder.getList());
		}
		else if (list.getPrev() != null){
			return evaluateSharing(holder, list.getPrev());
		}
		System.out.println("Nothing in list");
		return 0;
	}
}
