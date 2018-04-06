package PrologInterpreter.Structure;

public class Trail {
	private TermVar head;
	private Trail tail;
	private static Trail soFar;
	
	private Trail(TermVar h, Trail t){
		head = h;
		tail = t;
	}
	
	public static void push(TermVar x){
		soFar = new Trail(x, soFar);
	}
	
	public static void undo(Trail whereTo){
		while(soFar != whereTo){
			soFar.head.reset();
			soFar = soFar.tail;
		}
	}
	
	public static Trail note (){
		return soFar;
	}
}
