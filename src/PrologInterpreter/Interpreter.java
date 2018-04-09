package PrologInterpreter;

import java.util.HashMap;
import java.util.Queue;

import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.Program;

public interface Interpreter {

	Queue<String[]> executeQuery(GoalMappingPair query, Program rules, HashMap<String, Integer> progDict);

}