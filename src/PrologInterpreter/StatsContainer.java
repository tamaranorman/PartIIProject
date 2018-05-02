package PrologInterpreter;

import java.text.DecimalFormat;

public class StatsContainer {
	private long mean;
	private long variance;
	private long min;
	private long max;
	private int n;
	private int threads;
	
	private DecimalFormat f = new DecimalFormat("#0.000");
	
	public StatsContainer() {
		mean = 0;
		variance = 0;
		min = Long.MAX_VALUE;
		max = Long.MIN_VALUE;
	}
	
	public void update(long nextResult) {
		n++;
		long oldMean = mean;
		mean = mean + (nextResult - mean)/n;
		variance = variance + (nextResult - oldMean) * (nextResult - mean);
		if (nextResult < min) {
			min = nextResult;
		}
		if (nextResult > max) {
			max = nextResult;
		}
	}
	
	public void printResults() {
		long nanoToMilli = 1000000;
		System.out.println("Mean: " + f.format((double)mean/nanoToMilli));
		variance = variance/nanoToMilli;
		System.out.println("Variance: " + f.format((double)variance/nanoToMilli));
		System.out.println("Minimum: " + f.format((double)min/nanoToMilli));
		System.out.println("Maximum: " + f.format((double)max/nanoToMilli));
		System.out.println("Threads used: " + threads);
	}
	
	public void printResults2() {
		long nanoToMilli = 1000000;
		System.out.println(f.format((double)mean/nanoToMilli));
		variance = variance/nanoToMilli;
		System.out.println(f.format((double)variance/nanoToMilli));
		System.out.println(f.format((double)min/nanoToMilli));
		System.out.println(f.format((double)max/nanoToMilli));
		System.out.println(threads);
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

}
