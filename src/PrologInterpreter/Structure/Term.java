package PrologInterpreter.Structure;

import java.util.HashMap;

public abstract class Term {
	protected boolean containsVar;
	public abstract boolean unify(Term t, boolean seq);
	public abstract Term copy();
	public abstract String print();
	public abstract Term deepCopy(HashMap<Term, Term> map);
	public abstract String print(UnificationList list, UnificationListHolder fixedList);
	public abstract boolean unifySharing(Term t, UnificationListHolder holder, UnificationList list);
	public abstract int evaluate();
	public abstract int evaluateSharing(UnificationListHolder holder, UnificationList list);
	
	public Term(boolean b){
		containsVar = b;
	}
	
	public boolean getContainsVar() {
		return containsVar;
	}
}
