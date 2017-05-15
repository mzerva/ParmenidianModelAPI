package model;



import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;



public class Parser {
	private ArrayList<DBVersion> lifetime= new ArrayList<DBVersion>();
	private ArrayList<Map<String,Integer>> transitions = new ArrayList<Map<String,Integer>>();
	private HecateManager worker = new HecateManager();


	public Parser(String sqlFiles, String xmlFile){
		lifetime=worker.parseSql(sqlFiles);
		
		transitions=worker.parseXml(xmlFile);
	}
	
	public ArrayList<DBVersion> getLifetime(){
		return lifetime;
	}
	
	public ArrayList<Map<String,Integer>> getTransitions(){
		return transitions;
	}


}
