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
	
	public TermVar(Term i) {
		super(true);
		instance = i;
		varNo = timeStamp++;
	}

	@Override
	public boolean unify(Term t, boolean seq) {
		//if already unified
		if (instance != this){
			return instance.unify(t, seq);
		}
		else {
			/*if (seq == false && !t.containsVar) {
				this.containsVar = false;
			}*/
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
			if(this.varNo == list.getVar().varNo){
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
		/*if (!this.containsVar) {
			return this;
		}*/
		if (map.containsKey(this)){
			return map.get(this);
		}
		TermVar n;
		if (instance == this){
			n = new TermVar();
			map.put(this, n);
			return n;
		}
		else 
		{
			n = new TermVar(instance.deepCopy(map));
			return n;
		}
		/*
		if (!instance.containsVar) {
			this.containsVar = false;
			return this;
		}*/
		//map.put(this, n);
		//return n;
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
			if (list.getVar().varNo == this.varNo){
				if (list.getValue() instanceof TermCons){
					return true;
				}
				return ((TermVar)list.getValue()).isUnifiedSharingCons(holder, holder.getList());
			}
			return isUnifiedSharingCons(holder,  list.getPrev());
		}
		return false;
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
