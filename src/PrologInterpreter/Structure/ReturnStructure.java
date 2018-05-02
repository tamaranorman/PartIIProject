package PrologInterpreter.Structure;

import java.util.Queue;

public class ReturnStructure {
	private final Queue<String[]> values;
	private final int threads;
	
	public ReturnStructure(Queue<String[]> v, int t) {
		values = v;
		threads = t;
	}

	public Queue<String[]> getValues() {
		return values;
	}

	public int getThreads() {
		return threads;
	}

}
