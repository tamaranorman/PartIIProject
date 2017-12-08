package PrologInterpreter.Structure.StructureCreators;

import java.util.HashMap;

import com.igormaznitsa.prologparser.terms.AbstractPrologTerm;
import com.igormaznitsa.prologparser.terms.PrologVariable;

import PrologInterpreter.Structure.Term;
import PrologInterpreter.Structure.TermCons;
import PrologInterpreter.Structure.TermVar;

public class TermCreator {
	private TermVarCreator varCreator;
	private TermConsCreator consCreator;

	public TermCreator(HashMap<String, TermVar> m){
		varCreator = new TermVarCreator(m);
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
