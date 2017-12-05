package PrologInterpreter.Structure;

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
			return ((TermVar)t).unify(this);
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
	
	protected TermCons copyCons() {
		Term[] argsCopy = new Term[arity];
		for (int i = 0; i < arity; i ++){
			argsCopy[i] = args[i].copy();
		}
		return new TermCons(atom, arity, argsCopy);
	}

	@Override
	public String print() {
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
}
