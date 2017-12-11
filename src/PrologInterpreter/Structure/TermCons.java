package PrologInterpreter.Structure;

import PrologInterpreter.Utilities.Literals;

public class TermCons extends Term{
	private final int arity;
	private final Atom atom;
	private final Term[] args;
	
	public TermCons(Atom a, int arity, Term[] args){
		atom = a;
		this.arity = arity;
		this.args = args;
	}

	@Override
	public boolean unify(Term t) {
		if (t instanceof TermCons){
			return unifyCons((TermCons) t);
		}
		else if (t instanceof TermVar){
			return (t.unify(this));
		}
		return false;
	}

	private boolean unifyCons(TermCons t) {
		if (t.arity == arity && t.atom.equals(atom)){
			for(int i = 0; i < arity; i ++){
				if(!args[i].unify(t.args[i])){
					return false;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public Term copy() {
		return copyCons();
	}
	
	@Override
	public Term spawnCopy(TermVarMapping m) {
		return spawnCopyCons(m);
	}
	
	protected TermCons copyCons() {
		Term[] argsCopy = new Term[arity];
		for (int i = 0; i < arity; i ++){
			argsCopy[i] = args[i].copy();
		}
		return new TermCons(atom, arity, argsCopy);
	}
	
	protected TermCons spawnCopyCons(TermVarMapping m) {
		Term[] argsCopy = new Term[arity];
		for (int i = 0; i < arity; i ++){
			argsCopy[i] = args[i].spawnCopy(m);
		}
		return new TermCons(atom, arity, argsCopy);
	}

	@Override
	public String print() {
		if (atom == Literals.consAtom){
			return "[" + printList();
		}
		String p = atom.getAtomName();
		if (arity > 0){
			p += "(";
			for (int i = 0; i < arity; i++){
				p += args[i].print();
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
}
