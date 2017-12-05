package PrologInterpreter.Structure;

public abstract class Term {
	public abstract boolean unify(Term t);
	public abstract Term copy();
}
