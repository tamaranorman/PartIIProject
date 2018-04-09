 package PrologInterpreter;

import java.io.IOException;
import java.util.HashMap;

import com.igormaznitsa.prologparser.PrologParser;
import com.igormaznitsa.prologparser.exceptions.PrologParserException;
import com.igormaznitsa.prologparser.terms.PrologStructure;

import PrologInterpreter.Structure.Clause;
import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.PrologStructureToStructure;

public class BasicParser implements Parser {
	final PrologParser parser;
	
	public BasicParser(PrologParser p){
		parser = p;
	}
	
	/* (non-Javadoc)
	 * @see PrologInterpreter.IParser#parseClause(java.lang.String, java.util.HashMap)
	 */
	@Override
	public Clause parseClause(String input, HashMap<String, Integer> progDict) throws IOException, PrologParserException{
		PrologStructure structure = (PrologStructure) parser.nextSentence(input);
		return PrologStructureToStructure.createClause(structure, progDict); 
	}
	
	/* (non-Javadoc)
	 * @see PrologInterpreter.IParser#parseGoal(java.lang.String)
	 */
	@Override
	public GoalMappingPair parseGoal(String input) throws IOException, PrologParserException{
		PrologStructure structure = (PrologStructure) parser.nextSentence(input);
		return PrologStructureToStructure.createGoalMapping(structure);
	}
	
	
}