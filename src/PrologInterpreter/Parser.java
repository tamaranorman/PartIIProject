package PrologInterpreter;

import java.io.IOException;

import com.igormaznitsa.prologparser.PrologParser;
import com.igormaznitsa.prologparser.exceptions.PrologParserException;
import com.igormaznitsa.prologparser.terms.PrologStructure;

public class Parser {
	final PrologParser parser;
	
	public Parser(){
		parser = new PrologParser(null);
	}
	
	public PrologStructure parseContent(String input) throws IOException, PrologParserException{
			PrologStructure structure;
			structure = (PrologStructure) parser.nextSentence(input);
			return structure;
	}
}