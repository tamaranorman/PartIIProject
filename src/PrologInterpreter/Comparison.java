package PrologInterpreter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import com.igormaznitsa.prologparser.exceptions.PrologParserException;

import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.Program;

public class Comparison {
	public static void main(String[] args) throws IOException{
		Interpreter interpreter1 = new SingleThreadedInterpreter();
		Interpreter interpreter2 = new CopyWhenSpanningInterpreter();
		Interpreter interpreter3 = new StructureSharingInterpreter();
		
		Parser parser = new Parser();
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Welcome to my prolog interpreter:");
		System.out.println("Please enter rules and queries prefixing queries with \"?-\"");
		
		Program prog = null;
		Program last = null;
		HashMap<String, Integer> progDict = new HashMap<String, Integer>();
		while (scanner.hasNext()){
			String input = scanner.nextLine();
			if (input.startsWith("?-")){
				try {
					String[] inputs = input.split("-");
					GoalMappingPair goal;
					long v1 = 0;
					long v2 = 0;
					long v3 = 0;
					for (int i = 0; i < 10; i++){
						goal = parser.parseGoal(inputs[1]);
						long s1 = System.nanoTime();
						interpreter1.executeQuery(goal, prog);
						long e1 = System.nanoTime();
						v1 += e1 - s1;
						
						goal = parser.parseGoal(inputs[1]);
						long s2 = System.nanoTime();
						interpreter2.executeQuery(goal, prog);
						long e2 = System.nanoTime();
						v2 += e2 - s2;
						
						goal = parser.parseGoal(inputs[1]);
						long s3 = System.nanoTime();
						interpreter3.executeQuery(goal, prog);
						long e3 = System.nanoTime();
						v3 += e3 - s3;
					}
					System.out.println(v1/10);	
					System.out.println(v2/10);
					System.out.println(v3/10);
				} 
				catch (PrologParserException e) {
					System.out.println("Query couldn't be executed " + e.getMessage());
				}
			}
			else {
				try {
					Program c = new Program(parser.parseClause(input, progDict), null);
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
