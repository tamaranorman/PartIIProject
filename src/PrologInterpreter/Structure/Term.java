package PrologInterpreter.Structure;

public abstract class Term {
	abstract boolean unify(Term t);
	abstract Term copy();
}
