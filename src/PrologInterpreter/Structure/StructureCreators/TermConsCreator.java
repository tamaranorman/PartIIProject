package PrologInterpreter.Structure.StructureCreators;

import com.igormaznitsa.prologparser.terms.AbstractPrologNumericTerm;
import com.igormaznitsa.prologparser.terms.AbstractPrologTerm;
import com.igormaznitsa.prologparser.terms.PrologAtom;
import com.igormaznitsa.prologparser.terms.PrologFloatNumber;
import com.igormaznitsa.prologparser.terms.PrologIntegerNumber;
import com.igormaznitsa.prologparser.terms.PrologList;
import com.igormaznitsa.prologparser.terms.PrologStructure;
import com.igormaznitsa.prologparser.terms.PrologVariable;

import PrologInterpreter.Structure.Atom;
import PrologInterpreter.Structure.Term;
import PrologInterpreter.Structure.TermCons;
import PrologInterpreter.Utilities.Literals;

public class TermConsCreator {
	
	private TermCreator termCreator;

	public TermConsCreator(TermCreator creator) {
		termCreator = creator;
	}

	public TermCons createTermCons(AbstractPrologTerm element) {
		if (element instanceof PrologList) return createTermCons((PrologList) element);
		if (element instanceof PrologStructure) return createTermCons((PrologStructure) element);
		if (element instanceof PrologAtom) return createTermCons((PrologAtom) element);
		if (element instanceof AbstractPrologNumericTerm)return createTermCons((AbstractPrologNumericTerm) element);
		System.out.println("No matching method found for " + element.getType().name());
		return null;
	}
	
	private TermCons createTermCons(PrologStructure s)
	{
		boolean containsVar = false;
		Atom a = new Atom(s.getText());
		int arity = s.getArity();
		Term[] args = new Term[arity];
		for(int i = 0; i < arity; i++){
			AbstractPrologTerm t = s.getElement(i);
			if (t instanceof PrologVariable){
				args[i] = termCreator.createVar((PrologVariable) t);
				containsVar = true;
			}
			else {
				TermCons tc = createTermCons(t);
				args[i] = tc;
				containsVar = containsVar | tc.getContainsVar();
			}
		}
		return new TermCons(a, arity, args, containsVar);
	}
	
	private TermCons createTermCons(PrologAtom a){
		return new TermCons(new Atom(a.getText()), 0, null, false);
	}
	
	private TermCons createTermCons(AbstractPrologNumericTerm t){
		if (t instanceof PrologFloatNumber) return new TermCons(new Atom(((PrologFloatNumber)t).getValue().toPlainString()), 0, null, false);
		if (t instanceof PrologIntegerNumber) return new TermCons(new Atom(((PrologIntegerNumber)t).getValue().toString()), 0, null, false);
		return null;
	}
	
	private TermCons createTermCons(PrologList l){
		if(l.isNullList()){
			return Literals.nilCons;
		}
		else if(l.getTail().equals(null)){
			Term t = termCreator.createTerm(l.getHead());
			return new TermCons(Literals.consAtom, 2, new Term[]{t, Literals.nilCons}, t.getContainsVar());
		}
		else {
			Term t1 = termCreator.createTerm(l.getHead());
			Term t2 = termCreator.createTerm(l.getTail());
			return new TermCons(Literals.consAtom, 2, new Term[]{t1, t2}, t1.getContainsVar()|t2.getContainsVar());
		}
	}
}
