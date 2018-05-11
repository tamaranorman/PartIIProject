package PrologInterpreter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

import com.igormaznitsa.prologparser.exceptions.PrologParserException;

import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.Program;

public class Comparison {
	public static void main(String[] args) throws IOException{
		Interpreter interpreter1 = new SingleThreadedInterpreter();
		Interpreter interpreter2 = new CopyWhenSpanningInterpreterForkJoin();
		Interpreter interpreter3 = new CopyWhenSpanningInterpreterThreadPool();
		Interpreter interpreter4 = new CopyWhenSpanningInterpreterSequential();
		Interpreter interpreter5 = new CopyWhenSpanningInterpreter();
		
		Parser parser = new BasicParser();
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Welcome to my prolog interpreter comparison.");
		System.out.println("Please enter rules and queries prefixing queries with \"?-\", all results putput in ms.");
		System.out.println("Enter \"exit.\" to stop running.");
		
		Program prog = null;
		HashMap<String, Integer> progDict = new HashMap<String, Integer>();
		boolean end = false;
		while (!end && scanner.hasNext()){
			String input = scanner.nextLine();
			if (input.equals("exit.")) {
				end = true;
			}
			else if (!input.isEmpty()) {
				if (input.startsWith("?-")){
					try {
						String[] inputs = input.split("-");
						GoalMappingPair goal;
						StatsContainer s1 = new StatsContainer();
						StatsContainer s2 = new StatsContainer();
						StatsContainer s3 = new StatsContainer();
						StatsContainer s4 = new StatsContainer();
						StatsContainer s5 = new StatsContainer();
						
						//long[][] results = new long[3][100];

						long start;
						long finish;
						for (int i = 0; i < 10; i++){
							goal = parser.parseGoal(inputs[1]);
							s1.setThreads(interpreter1.executeQuery(goal, prog, progDict).getThreads());
							

							goal = parser.parseGoal(inputs[1]);
							s2.setThreads(interpreter2.executeQuery(goal, prog, progDict).getThreads());

							goal = parser.parseGoal(inputs[1]);
							s3.setThreads(interpreter3.executeQuery(goal, prog, progDict).getThreads());
							
							goal = parser.parseGoal(inputs[1]);
							s4.setThreads(interpreter4.executeQuery(goal, prog, progDict).getThreads());
							
							goal = parser.parseGoal(inputs[1]);
							s5.setThreads(interpreter5.executeQuery(goal, prog, progDict).getThreads());
						}

						for (int i = 0; i < 100; i++){
							goal = parser.parseGoal(inputs[1]);
							start = System.nanoTime();
							interpreter1.executeQuery(goal, prog, progDict);
							finish = System.nanoTime();
							s1.update(finish-start);
							//System.out.println(finish-start);
						
							goal = parser.parseGoal(inputs[1]);
							start = System.nanoTime();
							interpreter2.executeQuery(goal, prog, progDict);
							finish = System.nanoTime();
							s2.update(finish-start);
							//System.out.println(finish-start);

							goal = parser.parseGoal(inputs[1]);
							start = System.nanoTime();
							interpreter3.executeQuery(goal, prog, progDict);
							finish = System.nanoTime();
							s3.update(finish-start);
							
							goal = parser.parseGoal(inputs[1]);
							start = System.nanoTime();
							interpreter4.executeQuery(goal, prog, progDict);
							finish = System.nanoTime();
							s4.update(finish-start);
							
							goal = parser.parseGoal(inputs[1]);
							start = System.nanoTime();
							interpreter5.executeQuery(goal, prog, progDict);
							finish = System.nanoTime();
							s5.update(finish-start);
						}
						
						System.out.println("Sequential Backtracking Interpreter:");
						s1.printResults();
						System.out.println("Multi-Threaded Thread Pool Copying Interpreter:");
						s2.printResults();
						System.out.println("Multi-Threaded Unoptimised Copying Interpreter:");
						s3.printResults();
						System.out.println("Sequential Copying Interpreter:");
						s4.printResults();
						System.out.println("Multi-Threaded Threads Copying Interpreter:");
						s5.printResults();
						 
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
		}
		scanner.close();
	}
}
