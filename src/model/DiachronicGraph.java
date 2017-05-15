package model;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
//import java.io.FileWriter; KD ON 09/04/17
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import parmenidianEnumerations.Status;
//import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality; KD ON 09/04/17
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;

@SuppressWarnings({ "rawtypes", "unused" })
public class DiachronicGraph{

	private ArrayList<DBVersion> versions = new ArrayList<DBVersion>();
	private ArrayList<Map<String,Integer>> transitions = new ArrayList<Map<String,Integer>>();//auxiliary class for creating DiachronicGraph
	
	private static ConcurrentHashMap<String, Table> graph = new ConcurrentHashMap<String, Table>();//union of tables
	private ConcurrentHashMap<String, ForeignKey> graphEdges = new ConcurrentHashMap<String, ForeignKey>();//union of edges
	
	private ArrayList<Table>  vertices= new ArrayList<Table>();//union of tables
	private ArrayList<ForeignKey> edges= new ArrayList<ForeignKey>();//union of edges

	private GraphMetrics graphMetricsOfDiachronicGraph;
	
	public DiachronicGraph(String sql,String xml) throws Exception {
		
		Parser myParser;

		myParser = new Parser(sql,xml);
		versions=myParser.getLifetime();
		transitions=myParser.getTransitions();
		updateLifetimeWithTransitions();
		
		createDiachronicGraph();
		graphMetricsOfDiachronicGraph = new GraphMetrics(vertices,edges);
		
	}
	
	private void updateLifetimeWithTransitions(){
		
		for(int i=0;i<versions.size();++i)
			if(i==0)
				setFirstVersion(versions.get(i));
			else if(i==versions.size()-1)
				setFinalVersion(versions.get(i),i);
			else
				setIntermediateVersion(versions.get(i),i);
	}
	
	
	/**
	 * Trexw thn prwth version me to prwto Dictionary kai checkarw n dw an sthn
	 * 2h version exei svistei kapoios pinakas.Me endiaferei mono to deletion
	 * An kapoioi exoun ginei updated tha tous vapsw sthn 2h ekdosh,oxi edw
	 * @param fversion :firstVersion
	 */
	private void setFirstVersion(DBVersion fversion){
		
		for(int i=0;i<fversion.getTables().size();++i)
			if(transitions.get(0).containsKey(fversion.getTables().get(i).getKey())
			&& transitions.get(0).get(fversion.getTables().get(i).getKey())==Status.DELETION.getValue())
				fversion.getTables().get(i).setTableStatus(Status.DELETION.getValue());		
		
	}
	
	/**
	 * Trexw thn teleutaia version mou me to teleutaio dictionary mou,h thesh tou
	 * teleutaiou dictionary mou einai mia prin apo thn thesh ths teleutaias version mou.
	 * Psaxnw gia tables pou periexontai st dictionary mou KAI DEN einai deletions,einai 
	 * dhladh mono newTable kai UpdateTable kai tous vafw analoga me thn timh pou exei to
	 * dictionary mou.
	 * @param fversion :finalVersion
	 * @param k :H thesh ths teleutaias Version mou sthn Lista
	 */
	private void setFinalVersion(DBVersion fversion,int k){
		
		for(int i=0;i<fversion.getTables().size();++i)
			if(transitions.get(k-1).containsKey(fversion.getTables().get(i).getKey())
			&& transitions.get(k-1).get(fversion.getTables().get(i).getKey())!=Status.DELETION.getValue())
				fversion.getTables().get(i).setTableStatus(transitions.get(k-1).get(fversion.getTables().get(i).getKey()));
		
	}
	
	private void setIntermediateVersion(DBVersion version,int k){
		
		for(int i=0;i<version.getTables().size();++i){
			//koitaw to mellontiko m dictionary
			if(transitions.get(k).containsKey(version.getTables().get(i).getKey())
			&& transitions.get(k).get(version.getTables().get(i).getKey())==Status.DELETION.getValue())
				version.getTables().get(i).setTableStatus(Status.DELETION.getValue());
			
			//koitaw to palho m dictionary
			if(transitions.get(k-1).containsKey(version.getTables().get(i).getKey())
			&& transitions.get(k-1).get(version.getTables().get(i).getKey())!=Status.DELETION.getValue())
				version.getTables().get(i).setTableStatus(transitions.get(k-1).get(version.getTables().get(i).getKey()));
		
				
		}
	}

	//se periptwsh pou o UniversalGraph kataskeuazetai apto graphml
	//ektos apo tis listes exw enhmerwmeno kai to graph gia thn grhgorh
	//prospelash twn komvwn
	private void fixGraph() {
		
		for(int i=0;i<vertices.size();++i){
			graph.put(vertices.get(i).getKey(),vertices.get(i));
		}		
	}

	private void createDiachronicGraph() {
		
		for(int i=0;i<versions.size();++i){
			
			for(Table mt : versions.get(i).getTables())
				graph.putIfAbsent(mt.getKey(), mt);//union of tables
			
			for(ForeignKey fk : versions.get(i).getVersionForeignKeys())
				graphEdges.putIfAbsent(fk.getKey(), fk);//union of FK
		}
		
		transformNodes();
		transformEdges();
		
	}

	private void transformEdges() {

		//metatroph tou hashmap se ArrayList 
		Iterator i=graphEdges.entrySet().iterator();
		
		while(i.hasNext()){
			
			  Map.Entry entry = (Map.Entry) i.next();

			  String key = (String)entry.getKey();

			  ForeignKey value = (ForeignKey)entry.getValue();
			  
			  edges.add(value);
		}
		
		
	}

	private void transformNodes() {
		
		
		//metatroph tou hashmap se ArrayList 
		Iterator i=graph.entrySet().iterator();		
		while(i.hasNext()){
			
			  Map.Entry entry = (Map.Entry) i.next();

			  String key = (String)entry.getKey();

			  Table value = (Table)entry.getValue();
			  
			  vertices.add(value);

		}
		
		
		
	}	

	public ArrayList<Table> getNodes() {

		return vertices;
		
	}

	public ArrayList<ForeignKey> getEdges() {
		
		return edges;

	}

	public ConcurrentHashMap<String, Table> getDictionaryOfGraph() {
		return graph;
	}

	public String getVersion() {
		
		return "Universal Graph";
		
	}
	
	public Graph getGraph(){
		
		return graphMetricsOfDiachronicGraph.getGraph();
		
	}
	
//created by KD on 13/04/17	
	public ArrayList<DBVersion> getVersions(){
		
		
		return versions;
		

	}
	
//created by KD on 13/04/17	
		public GraphMetrics getGraphMetrics(){
			
			
			return graphMetricsOfDiachronicGraph;
			

		}

}
