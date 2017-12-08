package PrologInterpreter.Structure.StructureCreators;

import java.util.HashMap;

import com.igormaznitsa.prologparser.terms.PrologVariable;

import PrologInterpreter.Structure.TermVar;

public class TermVarCreator {
	private HashMap<String, TermVar> varMapping;
	
	public TermVarCreator(HashMap<String, TermVar> m){
		varMapping = m;
	}
	
	public TermVar createVar(PrologVariable v){
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
