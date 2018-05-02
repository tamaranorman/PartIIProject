package PrologInterpreter.Structure;

import java.util.HashMap;

import PrologInterpreter.Utilities.Literals;

public class TermCons extends Term{
	private final int arity;
	private final Atom atom;
	private final Term[] args;
	
	public TermCons(Atom a, int arity, Term[] args, boolean cVar){
		super(cVar);
		atom = a;
		this.arity = arity;
		this.args = args;
	}

	@Override
	public boolean unify(Term t, boolean seq) {
		if (t instanceof TermCons){
			return unifyCons((TermCons) t, seq);
		}
		else if (t instanceof TermVar){
			return (t.unify(this, seq));
		}
		return false;
	}

	private boolean unifyCons(TermCons t, boolean seq) {
		if (t.arity == arity && t.atom.equals(atom)){
			for(int i = 0; i < arity; i ++){
				if(!args[i].unify(t.args[i], seq)){
					return false;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean unifyIs(boolean seq) {
		if(args[1] instanceof TermVar){
			if (!((TermVar)args[1]).isUnifiedCons()){
				return false;
			}
		}
		try {
			TermCons lhs = new TermCons(new Atom(String.valueOf(args[1].evaluate())), 0, null, false);
			return args[0].unify(lhs, seq);
		}
		catch(NumberFormatException e){
			return false;
		}
	}
	
	public boolean unifyEquals(boolean seq) {
		return args[0].unify(args[1], seq);
	}

	public boolean unifyNotEqual() {
		return args[0].evaluate() != args[1].evaluate();
	}
	
	public boolean unifyGreaterThan() {
		return args[0].evaluate() > args[1].evaluate();
	}
	
	@Override
	public int evaluate() throws NumberFormatException{
		if (atom.getAtomName().equals("+")){
			int a = args[0].evaluate();
			int b = args[1].evaluate();
			return a + b;
		}
		if (atom.getAtomName().equals("*")){
			int a = args[0].evaluate();
			int b = args[1].evaluate();
			return a * b;
		}
		if (atom.getAtomName().equals("-")){
			int a = args[0].evaluate();
			int b = args[1].evaluate();
			return a - b;
		}
		if (atom.getAtomName().equals("/")){
			int a = args[0].evaluate();
			int b = args[1].evaluate();
			return a / b;
		}
		else {
			return Integer.parseInt(atom.getAtomName());
		}
	}

	@Override
	public boolean unifySharing (Term t, UnificationListHolder holder, UnificationList list) {
		if (t instanceof TermCons){
			return unifyConsSharing((TermCons) t, holder, list);
		}
		else if (t instanceof TermVar){
			return t.unifySharing(this, holder, list);
		}
		return false;
	}
	
	public boolean unifyConsSharing(TermCons t, UnificationListHolder holder, UnificationList list) {
		if (t.arity == arity && t.atom.equals(atom)){
			for(int i = 0; i < arity; i ++){
				if(!args[i].unifySharing(t.args[i], holder, holder.getList())){
					return false;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean unifyIsSharing(UnificationListHolder holder) {
		if(args[1] instanceof TermVar){
			if (!((TermVar)args[1]).isUnifiedSharingCons(holder, holder.getList())){
				return false;
			}
		}
		try {
			TermCons lhs = new TermCons(new Atom(String.valueOf(args[1].evaluateSharing(holder, holder.getList()))), 0, null, false);
			return args[0].unifySharing(lhs, holder, holder.getList());
		}
		catch(NumberFormatException e){
			return false;
		}
	}
	
	public boolean unifyEqualsSharing(UnificationListHolder holder) {
		return args[0].unifySharing(args[1], holder, holder.getList());
	}

	public boolean unifyNotEqualSharing(UnificationListHolder holder) {
		return args[0].evaluateSharing(holder, holder.getList()) != args[1].evaluateSharing(holder, holder.getList());
	}
	
	public boolean unifyGreaterThanSharing(UnificationListHolder holder) {
		return args[0].evaluateSharing(holder, holder.getList()) > args[1].evaluateSharing(holder, holder.getList());
	}
	

	@Override
	public int evaluateSharing(UnificationListHolder holder, UnificationList list) {
		if (atom.getAtomName().equals("+")){
			int a = args[0].evaluateSharing(holder, holder.getList());
			int b = args[1].evaluateSharing(holder, holder.getList());
			return a + b;
		}
		if (atom.getAtomName().equals("*")){
			int a = args[0].evaluateSharing(holder, holder.getList());
			int b = args[1].evaluateSharing(holder, holder.getList());
			return a * b;
		}
		if (atom.getAtomName().equals("-")){
			int a = args[0].evaluateSharing(holder, holder.getList());
			int b = args[1].evaluateSharing(holder, holder.getList());
			return a - b;
		}
		if (atom.getAtomName().equals("/")){
			int a = args[0].evaluateSharing(holder, holder.getList());
			int b = args[1].evaluateSharing(holder, holder.getList());
			return a / b;
		}
		else {
			return Integer.parseInt(atom.getAtomName());
		}
	}

	@Override
	public Term copy() {
		return copyCons();
	}
	
	@Override
	public Term deepCopy(HashMap<Term, Term> map) {
		return deepCopyCons(map);
	}
	
	public TermCons copyCons() {
		if (!containsVar){
			return this;
		}
		Term[] argsCopy = new Term[arity];
		for (int i = 0; i < arity; i ++){
			argsCopy[i] = args[i].copy();
		}
		return new TermCons(atom, arity, argsCopy, containsVar);
	}
	
	public TermCons deepCopyCons(HashMap<Term, Term> map) {
		if (!containsVar){
			return this;
		}
		Term[] argsCopy = new Term[arity];
		//boolean seenVar = false;
		for (int i = 0; i < arity; i ++){
			//if (args[i].containsVar) {
				argsCopy[i] = args[i].deepCopy(map);
				//seenVar = true;
			//}
			/*else {
				argsCopy[i] = args[i];
			}*/
		}
		//if (seenVar) {
			return new TermCons(atom, arity, argsCopy, containsVar);
		//}
		//containsVar = false;
		//return this;
	}

	@Override
	public String print() {
		if (atom.equals(Literals.consAtom)){
			return "[" + printList();
		}
		String p = atom.getAtomName();
		if (arity > 0){
			p += "(" + args[0].print();
			for (int i = 1; i < arity; i++){
				p += ", " + args[i].print();
			}
			p += ")";
		}
		return p;
	}


	@Override
	public String print(UnificationList list, UnificationListHolder fixedList) {
		if (atom == Literals.consAtom){
			return "[" + printList(list, fixedList);
		}
		String p = atom.getAtomName();
		if (arity > 0){
			p += "(" + args[0].print(list, fixedList);
			for (int i = 1; i < arity; i++){
				p += ", " + args[i].print(list, fixedList);
			}
			p += ")";
		}
		return p;
	}
	
	private String printList() {
		if (isNilTerm(args[1])){
			return args[0].print() + "]";
		}
		else {
			if (args[1] instanceof TermCons){
				return args[0].print() + "," + ((TermCons)args[1]).printList();
			}
			else {
				if (args[1].print().startsWith("[")){
					return args[0].print() + "," + args[1].print().substring(1);
				}
				return args[0].print() + "|" + args[1].print() + "]";
			}
		}
	}
	
	private String printList(UnificationList list, UnificationListHolder fixedList) {
		if (isNilTerm(args[1], list, fixedList)){
			return args[0].print(list, fixedList) + "]";
		}
		else {
			if (args[1] instanceof TermCons){
				return args[0].print(list, fixedList) + "," + ((TermCons)args[1]).printList(list, fixedList);
			}
			else {
				String arg1 = args[1].print(list, fixedList);
				if (arg1.startsWith("[")){
					return args[0].print(list, fixedList) + "," + arg1.substring(1);
				}
				return args[0].print(list, fixedList) + "|" + arg1 + "]";
			}
		}
	}

	private boolean isNilTerm(Term term) {
		if (term instanceof TermVar){
			if(term.print().equals(Literals.nilString)){
				return true;
			}
		}
		if (term instanceof TermCons){
			if (term.print().equals(Literals.nilString)){
				return true;
			}
		}
		return false;
	}
	
	private boolean isNilTerm(Term term, UnificationList list, UnificationListHolder fixedList) {
		if (term instanceof TermVar){
			if(term.print(list, fixedList).equals(Literals.nilString)){
				return true;
			}
		}
		if (term instanceof TermCons){
			if (term.print(list, fixedList).equals(Literals.nilString)){
				return true;
			}
		}
		return false;
	}

	public Term[] getArgs() {
		return args;
	}
	
	public Atom getAtom() {
		return atom;
	}

	public boolean canUnify(TermCons head) {
		if (atom.equals(head.atom) && arity == head.arity){
			  return true;
		}
		return false;
	}
}
