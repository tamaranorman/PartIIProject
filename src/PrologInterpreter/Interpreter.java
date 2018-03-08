package PrologInterpreter;

import java.util.HashMap;

import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.Program;

public interface Interpreter {

	void executeQuery(GoalMappingPair query, Program rules, HashMap<String, Integer> progDict);

}