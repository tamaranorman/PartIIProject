package PrologInterpreter;

import java.io.IOException;
import java.util.HashMap;

import com.igormaznitsa.prologparser.exceptions.PrologParserException;

import PrologInterpreter.Structure.Clause;
import PrologInterpreter.Structure.GoalMappingPair;

public interface Parser {

	Clause parseClause(String input, HashMap<String, Integer> progDict) throws IOException, PrologParserException;

	GoalMappingPair parseGoal(String input) throws IOException, PrologParserException;

}