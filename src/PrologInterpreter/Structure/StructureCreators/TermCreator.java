package PrologInterpreter.Structure.StructureCreators;

import com.igormaznitsa.prologparser.terms.AbstractPrologTerm;
import com.igormaznitsa.prologparser.terms.PrologVariable;

import PrologInterpreter.Structure.Term;
import PrologInterpreter.Structure.TermCons;
import PrologInterpreter.Structure.TermVar;
import PrologInterpreter.Structure.TermVarMapping;

public class TermCreator {
	private TermVarCreator varCreator;
	private TermConsCreator consCreator;

	public TermCreator(TermVarMapping varMapping){
		varCreator = new TermVarCreator(varMapping);
		consCreator = new TermConsCreator(this);
	}
	
	public Term createTerm(AbstractPrologTerm t){
		if (t instanceof PrologVariable){
			return varCreator.createVar((PrologVariable) t);
		}
		else {
			return consCreator.createTermCons(t);
		}
	}
	
	public TermCons createTermCons(AbstractPrologTerm t){
		return consCreator.createTermCons(t);
	}
	
	public TermVar createVar(AbstractPrologTerm t){
		return varCreator.createVar((PrologVariable) t);
	}
}
