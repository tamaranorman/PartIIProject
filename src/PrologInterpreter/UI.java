package PrologInterpreter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

import com.igormaznitsa.prologparser.exceptions.PrologParserException;
import com.igormaznitsa.prologparser.terms.PrologStructure;

public class UI {

	public static void main(String[] args) throws IOException {
		Parser parser = new Parser();
		Interpreter interpreter = new Interpreter();
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Welcome to my prolog interpreter:");
		System.out.println("Please enter rules and queries prefixing queries with \"?-\"");
		
		LinkedList<PrologStructure> syntax = new LinkedList<PrologStructure>();
		while (scanner.hasNext()){
			String input = scanner.nextLine();
			if (input.startsWith("?-")){
				try {
					String[] inputs = input.split("-");
					interpreter.executeQuery(parser.parseContent(inputs[1]), syntax);
				} catch (PrologParserException e) {
					System.out.println("Query couldn't be executed " + e.getMessage());
				}
			}
			else {
				try {
					syntax.add(parser.parseContent(input));
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
