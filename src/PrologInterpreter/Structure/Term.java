package PrologInterpreter.Structure;

public abstract class Term {
	abstract boolean unify(Term t);
	abstract boolean unifyCons(TermCons t);
	abstract Term copy();
}
