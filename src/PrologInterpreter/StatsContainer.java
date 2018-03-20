package PrologInterpreter;

public class StatsContainer {
	private long mean;
	private long standDev;
	private long min;
	private long max;
	private int n;
	
	public StatsContainer() {
		mean = 0;
		standDev = 0;
		min = Long.MAX_VALUE;
		max = Long.MIN_VALUE;
	}
	
	public void update(long nextResult) {
		n++;
		long oldMean = mean;
		mean = mean + (nextResult - mean)/n;
		standDev = standDev + (nextResult - oldMean) * (nextResult - mean);
		if (nextResult < min) {
			min = nextResult;
		}
		if (nextResult > max) {
			max = nextResult;
		}
	}
	
	public void printResults() {
		System.out.println("Mean: " + mean);
		System.out.println("Standard Deviation: " + standDev);
		System.out.println("Minimum: " + min);
		System.out.println("Maximum: " + max);
	}
	
	public long getMean() {
		return mean;
	}
	public long getStandDev() {
		return standDev;
	}
	public long getMin() {
		return min;
	}
	public long getMax() {
		return max;
	}

}
