package PrologInterpreter.Structure;

import java.util.HashMap;

public abstract class Term {
	protected final boolean containsVar;
	public abstract boolean unify(Term t);
	public abstract Term copy();
	public abstract Term spawnCopy(TermVarMapping m);
	public abstract String print();
	public abstract Term deepCopy(HashMap<Term, Term> map);
	public abstract void replace(TermVar termVar, TermVar newTerm);
	public abstract String print(UnificationList list, UnificationListHolder fixedList);
	public abstract boolean unifySharing(Term t, UnificationListHolder holder, UnificationList list);
	
	public Term(boolean b){
		containsVar = b;
	}
	
	public boolean getContainsVar() {
		return containsVar;
	}
}
