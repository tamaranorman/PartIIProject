package PrologInterpreter.Structure;

public class Trail {
	private TermVar head;
	private Trail tail;
	private static Trail soFar;
	
	private Trail(TermVar h, Trail t){
		head = h;
		tail = t;
	}
	
	public static void Push(TermVar x){
		soFar = new Trail(x, soFar);
	}
	
	public static void Undo(Trail whereTo){
		while(soFar != whereTo){
			soFar = soFar.tail;
			soFar.head.reset();
		}
	}
}
