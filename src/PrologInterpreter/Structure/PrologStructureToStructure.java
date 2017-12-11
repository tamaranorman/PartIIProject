package PrologInterpreter.Structure;

import PrologInterpreter.Utilities.Literals;
import PrologInterpreter.Structure.StructureCreators.TermCreator;

import com.igormaznitsa.prologparser.exceptions.PrologParserException;
import com.igormaznitsa.prologparser.terms.AbstractPrologTerm;
import com.igormaznitsa.prologparser.terms.PrologStructure;

public class PrologStructureToStructure {

	private static TermVarMapping varMapping = new TermVarMapping();
	private static TermCreator termCreator = new TermCreator(varMapping);
	
	public static Clause createClause(PrologStructure s) throws PrologParserException{
		varMapping.clear();
		if (s.getText().equals(Literals.ifSeperator)){
			AbstractPrologTerm t = s.getElement(0);
			if (s.getElement(0) instanceof PrologStructure){
				TermCons lhs = termCreator.createTermCons((PrologStructure) t);
				Goal rhs = createGoal(s.getElement(1));
				return new Clause(lhs, rhs);
			}
			else {
				throw new PrologParserException("Doesn't start with cons term", 0, 0);
			}
		}
		//Singleton
		else {
			TermCons lhs = termCreator.createTermCons(s);
			return new Clause(lhs, null);
		}
	}
	
	public static GoalMappingPair createGoalMapping(AbstractPrologTerm t){
		varMapping.clear();
		return new GoalMappingPair(createGoal(t), varMapping);
	}
	
	private static Goal createGoal(AbstractPrologTerm t){
		if (t.getText().equals(Literals.commaSeperator)){
			PrologStructure s = (PrologStructure) t;
			TermCons head = termCreator.createTermCons(s.getElement(0));
			Goal tail = createGoal(s.getElement(1));
			return new Goal(head, tail);
		}
		else {
			TermCons head = termCreator.createTermCons(t);
			return new Goal(head, null);
		}
	}
}
