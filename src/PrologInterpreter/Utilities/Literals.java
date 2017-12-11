package PrologInterpreter.Utilities;

import PrologInterpreter.Structure.Atom;
import PrologInterpreter.Structure.TermCons;

public class Literals {
	
	public static String nilString = "nil";
	
	public static Atom nilAtom = new Atom(nilString);
	public static Atom consAtom = new Atom("cons");
	
	public static TermCons nilCons = new TermCons(nilAtom, 0, null);
	
	public static String ifSeperator = ":-";
	public static String commaSeperator = ",";
}
