package PrologInterpreter;

import java.io.IOException;
import java.util.Scanner;

import com.igormaznitsa.prologparser.exceptions.PrologParserException;

import PrologInterpreter.Structure.Program;

public class UI {

	public static void main(String[] args) throws IOException {
		Parser parser = new Parser();
		Interpreter interpreter = new Interpreter();
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Welcome to my prolog interpreter:");
		System.out.println("Please enter rules and queries prefixing queries with \"?-\"");
		
		Program prog = null;
		Program last = null;
		while (scanner.hasNext()){
			String input = scanner.nextLine();
			if (input.startsWith("?-")){
				try {
					String[] inputs = input.split("-");
					interpreter.executeQuery(parser.parseGoal(inputs[1]), prog);
				} catch (PrologParserException e) {
					System.out.println("Query couldn't be executed " + e.getMessage());
				}
			}
			else {
				try {
					Program c = new Program(parser.parseClause(input), null);
					if (prog == null){
						prog = c;
						last = c;
					}
					else {
						last.setTail(c);
						last = c;
					}
				}
				catch(PrologParserException e) {
					System.out.println("Line " + input + " couldn't be added. " + e.getMessage());
				}
			}
		}
		scanner.close();
		System.out.println("Scanner is closed");
	}

}
