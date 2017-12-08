package PrologInterpreter.Structure;

import PrologInterpreter.Utilities.Literals;

import java.util.HashMap;

import com.igormaznitsa.prologparser.exceptions.PrologParserException;
import com.igormaznitsa.prologparser.terms.AbstractPrologNumericTerm;
import com.igormaznitsa.prologparser.terms.AbstractPrologTerm;
import com.igormaznitsa.prologparser.terms.PrologAtom;
import com.igormaznitsa.prologparser.terms.PrologFloatNumber;
import com.igormaznitsa.prologparser.terms.PrologIntegerNumber;
import com.igormaznitsa.prologparser.terms.PrologList;
import com.igormaznitsa.prologparser.terms.PrologStructure;
import com.igormaznitsa.prologparser.terms.PrologVariable;

public class PrologStructureToStructure {

	private static HashMap<String, TermVar> varMapping = new HashMap<String, TermVar>();
	
	public static Clause createClause(PrologStructure s) throws PrologParserException{
		varMapping.clear();
		if (s.getText().equals(":-")){
			AbstractPrologTerm t = s.getElement(0);
			if (s.getElement(0) instanceof PrologStructure){
				TermCons lhs = createTermCons((PrologStructure) t);
				Goal rhs = createGoal(s.getElement(1));
				return new Clause(lhs, rhs);
			}
			else {
				throw new PrologParserException("Doesn't start with cons term", 0, 0);
			}
		}
		//Singleton
		else {
			TermCons lhs = createTermCons(s);
			return new Clause(lhs, null);
		}
	}
	
	public static GoalMappingPair createGoalMapping(AbstractPrologTerm t){
		varMapping.clear();
		return new GoalMappingPair(createGoal(t), varMapping);
	}
	
	private static Goal createGoal(AbstractPrologTerm t){
		if (t.getText().equals(",")){
			PrologStructure s = (PrologStructure) t;
			TermCons head = createTermCons(s.getElement(0));
			Goal tail = createGoal(s.getElement(1));
			return new Goal(head, tail);
		}
		else {
			TermCons head = createTermCons(t);
			return new Goal(head, null);
		}
	}
	
	private static Term createTerm(AbstractPrologTerm t){
		if (t instanceof PrologVariable){
			return createVar((PrologVariable) t);
		}
		else {
			return createTermCons(t);
		}
	}
	
	private static TermCons createTermCons(PrologStructure s){

		Atom a = new Atom(s.getText());
		int arity = s.getArity();
		Term[] args = new Term[arity];
		for(int i = 0; i < arity; i++){
			AbstractPrologTerm t = s.getElement(i);
			if (t instanceof PrologVariable){
				args[i] = createVar((PrologVariable) t);
			}
			else {
				args[i] = createTermCons(t);
			}
		}
		return new TermCons(a, arity, args);
	}
	
	private static TermCons createTermCons(PrologAtom a){
		return new TermCons(new Atom(a.getText()), 0, null);
	}
	
	private static TermCons createTermCons(AbstractPrologNumericTerm t){
		if (t instanceof PrologFloatNumber) return new TermCons(new Atom(((PrologFloatNumber)t).getValue().toPlainString()), 0, null);
		if (t instanceof PrologIntegerNumber) return new TermCons(new Atom(((PrologIntegerNumber)t).getValue().toString()), 0, null);
		return null;
	}
	
	private static TermCons createTermCons(PrologList l){
		if(l.isNullList()){
			return Literals.nilCons;
		}
		else if(l.getTail().equals(null)){
			return new TermCons(Literals.consAtom, 2, new Term[]{createTerm(l.getHead()), 
																 Literals.nilCons});
		}
		else {
			return new TermCons(Literals.consAtom, 2, new Term[]{createTerm(l.getHead()), 
																createTerm(l.getTail())});
		}
	}
	
	private static TermCons createTermCons(AbstractPrologTerm element) {
		if (element instanceof PrologList) return createTermCons((PrologList) element);
		if (element instanceof PrologStructure) return createTermCons((PrologStructure) element);
		if (element instanceof PrologAtom) return createTermCons((PrologAtom) element);
		if (element instanceof AbstractPrologNumericTerm)return createTermCons((AbstractPrologNumericTerm) element);
		System.out.println("No matching method found for " + element.getType().name());
		return null;
	}
	
	private static TermVar createVar(PrologVariable v){
		if (varMapping.containsKey(v.getText())){
			return varMapping.get(v.getText());
		}
		else{
			TermVar var = new TermVar();
			varMapping.put(v.getText(), var);
			return var;
		}
	}
}
