package PrologInterpreter;

import java.io.IOException;

import com.igormaznitsa.prologparser.PrologParser;
import com.igormaznitsa.prologparser.exceptions.PrologParserException;
import com.igormaznitsa.prologparser.terms.PrologStructure;

import PrologInterpreter.Structure.Clause;
import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.PrologStructureToStructure;

public class Parser {
	final PrologParser parser;
	
	public Parser(){
		parser = new PrologParser(null);
	}
	
	public Clause parseClause(String input) throws IOException, PrologParserException{
		PrologStructure structure = (PrologStructure) parser.nextSentence(input);
		return PrologStructureToStructure.createClause(structure); 
	}
	
	public GoalMappingPair parseGoal(String input) throws IOException, PrologParserException{
		PrologStructure structure = (PrologStructure) parser.nextSentence(input);
		return PrologStructureToStructure.createGoalMapping(structure);
	}
	
	
}