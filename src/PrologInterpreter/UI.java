package PrologInterpreter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Queue;
import java.util.Scanner;

import com.igormaznitsa.prologparser.exceptions.PrologParserException;

import PrologInterpreter.Structure.Program;

public class UI {

	public static void main(String[] args) throws IOException {
		Parser parser = new BasicParser();
		Interpreter interpreter = new CopyWhenSpanningInterpreterThreadPool();
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Welcome to my prolog interpreter:");
		System.out.println("Please enter rules and queries prefixing queries with \"?-\"");
		
		Program prog = null;
		HashMap<String, Integer> progDict = new HashMap<String, Integer>();
		while (scanner.hasNext()){
			String input = scanner.nextLine();
			if (!input.isEmpty()) {
				if (input.startsWith("?-")){
					try {
						String[] inputs = input.split("-");
						Queue<String[]> results = interpreter.executeQuery(parser.parseGoal(inputs[1]), prog, progDict).getValues();
						while(!results.isEmpty()) {
							String[] values = results.remove();
							for(String v : values) {
								System.out.println(v);
							}
						}
					} catch (PrologParserException e) {
						System.out.println("Query couldn't be executed " + e.getMessage());
					}
				}
				else {
					try {
						Program c = new Program(parser.parseClause(input, progDict));
						if (prog == null){
							prog = c;
						}
					}
					catch(PrologParserException e) {
						System.out.println("Line " + input + " couldn't be added. " + e.getMessage());
					}
				}
			}
		}
		scanner.close();
		System.out.println("Scanner is closed");
	}

}
