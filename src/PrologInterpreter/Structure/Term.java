package PrologInterpreter.Structure;

public abstract class Term {
	public abstract boolean unify(Term t);
	public abstract Term copy();
	public abstract Term spawnCopy();
	public abstract String print();
}
