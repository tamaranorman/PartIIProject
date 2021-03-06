package PrologInterpreter.Utilities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import PrologInterpreter.Structure.Atom;
import PrologInterpreter.Structure.TermCons;

public class Literals {
	
	public static String nilString = "nil";
	
	public static Atom nilAtom = new Atom(nilString);
	public static Atom consAtom = new Atom("cons");
	
	public static TermCons nilCons = new TermCons(nilAtom, 0, null, false);
	
	public static String ifSeperator = ":-";
	public static String commaSeperator = ",";
	
	public static String is = "is";
	public static String equals = "=";
	public static String notEquals = "=\\=";
	public static String greaterThan = ">";
	
	public static final String[] SET_VALUES = new String[] { is, equals, notEquals, greaterThan };
	public static final Set<String> arithmetic_operators = new HashSet<>(Arrays.asList(SET_VALUES));
	
	private static final String[] falseStringArray = new String[] {"false."};
	public static final Queue<String[]> falseQuery = new LinkedList<String[]>() {
	{
		add(falseStringArray);
	}};
}
