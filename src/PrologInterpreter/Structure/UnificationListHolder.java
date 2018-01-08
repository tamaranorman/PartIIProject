package PrologInterpreter.Structure;

public class UnificationListHolder {
	private UnificationList list;
	
	public UnificationListHolder(){
		list = new UnificationList();
	}
	
	public UnificationListHolder(UnificationList l){
		list = l;
	}

	public UnificationList getList() {
		return list;
	}
	
	public void addToList(TermVar var, Term value){
		list = new UnificationList(var, value, list);
	}

}
