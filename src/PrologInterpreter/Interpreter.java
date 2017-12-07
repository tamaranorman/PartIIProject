package PrologInterpreter;

import java.util.HashMap;

import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.Program;
import PrologInterpreter.Structure.TermVar;
import PrologInterpreter.Structure.TermVarMapping;

public class Interpreter {
	public void executeQuery(GoalMappingPair query, Program rules){
		HashMap<String, TermVar> m = query.getMap();
		int size = m.size();
		String[] values = m.keySet().toArray(new String[size]);
		TermVar[] vars = new TermVar[values.length];
		for(int i = 0; i < size; i++){
			vars[i] = m.get(values[i]);
		}
		query.getGoal().solve(rules, new TermVarMapping(vars, values, values.length));
		System.out.println("Interpreter reached " + query.toString());
	}
}
