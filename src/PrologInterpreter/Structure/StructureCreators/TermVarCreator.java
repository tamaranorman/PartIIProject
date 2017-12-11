package PrologInterpreter.Structure.StructureCreators;

import com.igormaznitsa.prologparser.terms.PrologVariable;

import PrologInterpreter.Structure.TermVar;
import PrologInterpreter.Structure.TermVarMapping;

public class TermVarCreator {
	private TermVarMapping varMapping;
	
	public TermVarCreator(TermVarMapping v){
		varMapping = v;
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
