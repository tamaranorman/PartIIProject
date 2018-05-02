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
		boolean exception = false;
		
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
						Queue<String[]> result1 = interpreter1.executeQuery(goal, prog, progDict).getValues();
						results.addAll(result1);

						goal = parser.parseGoal(inputs[1]);
						Queue<String[]> result2 = interpreter2.executeQuery(goal, prog, progDict).getValues();

						goal = parser.parseGoal(inputs[1]);
						Queue<String[]> result3 = interpreter3.executeQuery(goal, prog, progDict).getValues();

						assertTrue(checkEquality(result1, result2, result3));

					} catch (PrologParserException e) {
						exception = true;
					}
				} else {
					try {
						Program c = new Program(parser.parseClause(input, progDict));
						if (prog == null) {
							prog = c;
						}
					} catch (PrologParserException e) {
						exception = true;
					}
				}
			}
		}
		bufferedReader.close();
		assertFalse(exception);
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
		int sizeVariables = result1.peek().length;
		String[][] v1 = result1.toArray(new String[0][0]);
		String[][] v2 = result2.toArray(new String[0][0]);
		String[][] v3 = result3.toArray(new String[0][0]);
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
	
	private Comparator<String[]> c = new Comparator<String[]>() {
		@Override
		public int compare(String[] o1, String[] o2) {
			String quantityOne = o1[0];
			String quantityTwo = o2[0];
			return quantityOne.compareTo(quantityTwo);
		}};

}
