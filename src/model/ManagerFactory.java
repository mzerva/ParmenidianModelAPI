package model;

public class ManagerFactory {
	private ManagerInterface manager = new Manager();
	
	public ManagerInterface getManager(){
		return manager;
	}
}
