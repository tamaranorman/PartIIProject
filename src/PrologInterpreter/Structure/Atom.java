package PrologInterpreter.Structure;

public class Atom {
	final String atomName;
	
	public Atom(String s){
		atomName = s;
	}
	
	@Override
	public boolean equals(Object o){
		if (! (o instanceof Atom)) {
			return false;
		}
		Atom a = (Atom) o;
		if (a.atomName.equals(atomName)){
			return true;
		}
		else {
			return false;
		}
	}
	
	public String getAtomName (){
		return atomName;
	}

}
