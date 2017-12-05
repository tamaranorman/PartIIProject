package PrologInterpreter;

import java.util.LinkedList;

import com.igormaznitsa.prologparser.terms.PrologStructure;

public class Interpreter {
	public void executeQuery(PrologStructure query, LinkedList<PrologStructure> rules){
		//TODO complete interpreter
		System.out.println("Interpreter reached " + query.toString());
	}

}
