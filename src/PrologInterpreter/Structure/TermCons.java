package PrologInterpreter.Structure;

public class TermCons extends Term{
	final int arity;
	final Atom atom;
	final Term[] args;
	
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
	
	private TermCons copyCons() {
		Term[] argsCopy = new Term[arity];
		for (int i = 0; i < arity; i ++){
			argsCopy[i] = args[i].copy();
		}
		return new TermCons(atom, arity, argsCopy);
	}
}
