package PrologInterpreter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Queue;
import java.util.Scanner;

import com.igormaznitsa.prologparser.exceptions.PrologParserException;

import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.Program;

public class ComparisonCorrectness {

	public static void main(String[] args) throws IOException{
		Interpreter interpreter1 = new SingleThreadedInterpreter();
		Interpreter interpreter2 = new CopyWhenSpanningInterpreter();
		Interpreter interpreter3 = new StructureSharingInterpreter();
		
		Parser parser = new BasicParser();
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Welcome to my prolog interpreter:");
		System.out.println("Please enter rules and queries prefixing queries with \"?-\"");
		
		Program prog = null;
		HashMap<String, Integer> progDict = new HashMap<String, Integer>();
		while (scanner.hasNext()){
			String input = scanner.nextLine();
			if (input.startsWith("?-")){
				try {
					String[] inputs = input.split("-");
					GoalMappingPair goal;
					
					goal = parser.parseGoal(inputs[1]);
					Queue<String[]> result1 = interpreter1.executeQuery(goal, prog, progDict);
						
					goal = parser.parseGoal(inputs[1]);
					Queue<String[]> result2 = interpreter2.executeQuery(goal, prog, progDict);
						
					goal = parser.parseGoal(inputs[1]);
					Queue<String[]> result3 = interpreter3.executeQuery(goal, prog, progDict);
					
					System.out.print(checkEquality(result1, result2, result3));
					
				} 
				catch (PrologParserException e) {
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
		scanner.close();
		System.out.println("Scanner is closed");
	}

	private static boolean checkEquality(Queue<String[]> result1, Queue<String[]> result2, Queue<String[]> result3) {
		if(result1.size() != result2.size() || result1.size() != result3.size()) {
			return false;
		}
		int sizeResults = result1.size();
		if (sizeResults == 0) {
			return true;
		}
		else if (sizeResults == 1) {
			String[] r1 = result1.remove();
			String[] r2 = result2.remove();
			String[] r3 = result3.remove();
			for(int i = 0; i < r1.length; i++) {
				if(!r1[i].equals(r2[i]) || !r1[i].equals(r3[i])) {
					return false;
				}
			}
			return true;
		}
		else {
			int sizeVariables = result1.peek().length;
			String[][] v1 = new String[sizeVariables][sizeResults];
			String[][] v2 = new String[sizeVariables][sizeResults];
			String[][] v3 = new String[sizeVariables][sizeResults];
			for(int j = 0; j < sizeResults; j++) {
				String[] r1 = result1.remove();
				String[] r2 = result2.remove();
				String[] r3 = result3.remove();
				for(int i = 0; i < sizeVariables; i++) {
					v1[i][j] = r1[i];
					v2[i][j] = r2[i];
					v3[i][j] = r3[i];
				}
			}
			for(int j = 0; j < sizeVariables; j ++) {
				Arrays.sort(v1[j]);
				Arrays.sort(v2[j]);
				Arrays.sort(v3[j]);
				for(int i = 0; i < sizeResults; i++) {
					if(!v1[j][i].equals(v2[j][i]) || !v1[j][i].equals(v3[j][i])) {
						return false;
					}
				}
			}
			
			return true;
		}
	}
}
