package PrologInterpreter;

import java.util.HashMap;

import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.Program;
import PrologInterpreter.Structure.ReturnStructure;

public interface Interpreter {

	ReturnStructure executeQuery(GoalMappingPair query, Program rules, HashMap<String, Integer> progDict);
}