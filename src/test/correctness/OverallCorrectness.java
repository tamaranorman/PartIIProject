package test.correctness;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.jupiter.api.Test;

import com.igormaznitsa.prologparser.exceptions.PrologParserException;

import PrologInterpreter.BasicParser;
import PrologInterpreter.CopyWhenSpanningInterpreter;
import PrologInterpreter.Interpreter;
import PrologInterpreter.Parser;
import PrologInterpreter.SingleThreadedInterpreter;
import PrologInterpreter.StructureSharingInterpreter;
import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.Program;

class OverallCorrectness {
	@Test
	void comparison() throws IOException{
		Interpreter interpreter1 = new SingleThreadedInterpreter();
		Interpreter interpreter2 = new CopyWhenSpanningInterpreter();
		Interpreter interpreter3 = new StructureSharingInterpreter();
		
		Parser parser = new BasicParser();
		FileReader f = new FileReader("correctnessTestsInput.txt");
		BufferedReader bufferedReader = new BufferedReader(f);
		
		Program prog = null;
		HashMap<String, Integer> progDict = new HashMap<String, Integer>();
		String input = null;
		LinkedList<String[]> results = new LinkedList<String[]>();
		while ((input = bufferedReader.readLine()) != null) {
			if (!input.isEmpty()) {
				if (input.startsWith("?-")) {
					try {
						String[] inputs = input.split("-");
						GoalMappingPair goal;

						goal = parser.parseGoal(inputs[1]);
						Queue<String[]> result1 = interpreter1.executeQuery(goal, prog, progDict);
						results.addAll(result1);

						goal = parser.parseGoal(inputs[1]);
						Queue<String[]> result2 = interpreter2.executeQuery(goal, prog, progDict);

						goal = parser.parseGoal(inputs[1]);
						Queue<String[]> result3 = interpreter3.executeQuery(goal, prog, progDict);

						assertTrue(checkEquality(result1, result2, result3));

					} catch (PrologParserException e) {
						System.out.println("Query couldn't be executed " + e.getMessage());
					}
				} else {
					try {
						Program c = new Program(parser.parseClause(input, progDict));
						if (prog == null) {
							prog = c;
						}
					} catch (PrologParserException e) {
						System.out.println("Line " + input + " couldn't be added. " + e.getMessage());
					}
				}
			}
		}
		bufferedReader.close();
		assertTrue(checkMatches(results));
	}

	private boolean checkEquality(Queue<String[]> result1, Queue<String[]> result2, Queue<String[]> result3) {
		if(result1.size() != result2.size() || result1.size() != result3.size()) {
			return false;
		}
		if (result1 == result2 && result1 == result3) {
			return true;
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
			String[][] v1 = new String[sizeResults][sizeVariables];
			String[][] v2 = new String[sizeResults][sizeVariables];
			String[][] v3 = new String[sizeResults][sizeVariables];
			for(int i = 0; i < sizeResults; i++) {
				v1[i] = result1.remove();
				v2[i] = result2.remove();
				v3[i] = result3.remove();
			}
			Comparator<String[]> c = new Comparator<String[]>() {
				@Override
				public int compare(String[] o1, String[] o2) {
					String quantityOne = o1[0];
					String quantityTwo = o2[0];
					return quantityOne.compareTo(quantityTwo);
				}};
			Arrays.sort(v1, c);
			Arrays.sort(v2, c);
			Arrays.sort(v3, c);
			for(int j = 0; j < sizeResults; j ++) {
				for(int i = 0; i < sizeVariables; i++) {
					if(!v1[j][i].equals(v2[j][i]) || !v1[j][i].equals(v3[j][i])) {
						return false;
					}
				}
			}
			return true;
		}
	}
	
	private boolean checkMatches(LinkedList<String[]> results) throws IOException {
		FileReader f = new FileReader("correctnessTestsOutput.txt");
		BufferedReader bufferedReader = new BufferedReader(f);
		String line = null;
		String[] current = results.removeFirst();
		int i = 0;
		while ((line = bufferedReader.readLine()) != null) {
			if(line.isEmpty()) {
				current = results.removeFirst();
				i = 0;
			}
			else if (line.equals(current[i])) {
				bufferedReader.close();
				return false;
			}
		}
		bufferedReader.close();
		return true;
	}

}
