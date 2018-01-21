package PrologInterpreter;

import java.io.IOException;
import java.util.Scanner;

import com.igormaznitsa.prologparser.exceptions.PrologParserException;

import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.Program;

public class Comparison {
	public static void main(String[] args) throws IOException{
		Interpreter interpreter1 = new SingleThreadedInterpreter();
		Interpreter interpreter2 = new CopyWhenSpanningInterpreter();
		Interpreter interpreter3 = new CopyWhenSpanningInterpreter2();
		
		Parser parser = new Parser();
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
					GoalMappingPair goal = parser.parseGoal(inputs[1]);
					long s1 = System.nanoTime();
					interpreter1.executeQuery(goal, prog);
					long e1 = System.nanoTime();
					goal = parser.parseGoal(inputs[1]);
					long s2 = System.nanoTime();
					interpreter2.executeQuery(goal, prog);
					long e2 = System.nanoTime();
					goal = parser.parseGoal(inputs[1]);
					long s3 = System.nanoTime();
					interpreter3.executeQuery(goal, prog);
					long e3 = System.nanoTime();
					
					System.out.println(e1-s1);
					System.out.println(e2-s2);
					System.out.println(e3-s3);				
				} 
				catch (PrologParserException e) {
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
