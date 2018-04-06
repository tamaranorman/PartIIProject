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
	
	/*public UnificationListHolder getPrevious(){
		return new UnificationListHolder(list.getPrev());
	}*/

	/*public boolean isNull() {
		return list.getPrev() == null && list.getValue() == null && list.getVar() == null;
	}*/
}
