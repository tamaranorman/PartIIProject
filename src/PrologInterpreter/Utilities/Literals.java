package PrologInterpreter.Utilities;

import PrologInterpreter.Structure.Atom;
import PrologInterpreter.Structure.TermCons;

public class Literals {
	
	public static Atom nilAtom = new Atom("nil");
	public static Atom consAtom = new Atom("cons");
	
	public static TermCons nilCons = new TermCons(nilAtom, 0, null);
	
	public static String ifSeperator = ":-";
	public static String commaSeperator = ",";
}
