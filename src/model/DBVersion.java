package model;





import java.util.ArrayList;
import java.util.Map;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;


/**
 * 
 * @author libathos
 *h klash Table einai ths Ekaths kai afora thn sql anaparastash twn table
 *h dikia m klash table edw einai model.table gia na apofigoume ta conflicts
 *kai periexei plirofories sxetika me thn anaparastash tou table ws komvo
 *
 */
public class DBVersion  {
	
	private String versionName;
	private ArrayList<Table> tablesWithin=new ArrayList<Table>();
	private ArrayList<ForeignKey> versionForeignKeys= new ArrayList<ForeignKey>();
	private GraphMetrics graphMetricsOfDBVersion;

	public DBVersion(ArrayList<Table> tablesWithin,ArrayList<ForeignKey> versionForeignKeys,String vn){
		
		//1 set VersionName				
		String[] array =vn.split(".sql",2);
		versionName=array[0];
		
		//2 set all the tables of the current version		
		this.tablesWithin=tablesWithin;

		
		//3 set all the FK dependencies of the current version
		 this.versionForeignKeys=versionForeignKeys;

		 graphMetricsOfDBVersion = new GraphMetrics(tablesWithin,versionForeignKeys);
		
	}

	public ArrayList<Table> getTables() {
		
		return tablesWithin;
	}



	public ArrayList<ForeignKey> getVersionForeignKeys() {
		return versionForeignKeys;
	}


	public ArrayList<Table> getNodes() {
		
		return getTables();
		
	}


	public ArrayList<ForeignKey> getEdges() {
		
		return getVersionForeignKeys();

	}


	public String getVersion() {
		
		return this.versionName;
	}
	
	public Graph getGraph(){
		
		return graphMetricsOfDBVersion.getGraph();
		
	}
	
	public String generateVertexDegree(String vertex){
		
		vertex=vertex.replace(",","");
		
		
		
		for(int i=0;i<tablesWithin.size();++i){
			if(vertex.equals(tablesWithin.get(i).getKey())){
				vertex=vertex+",";
				return graphMetricsOfDBVersion.generateVertexDegree(vertex);
			}
		}
		
		return "*,";
//		return -1+",";
		
		
		
	}
	
	public String generateVertexInDegree(String vertex){
		
		vertex=vertex.replace(",","");
		
		
		
		for(int i=0;i<tablesWithin.size();++i){
			if(vertex.equals(tablesWithin.get(i).getKey())){
				vertex=vertex+",";
				return graphMetricsOfDBVersion.generateVertexInDegree(vertex);
			}
		}
		
		return "*,";
//		return -1+",";
		
		
		
	}
	
	public String generateVertexOutDegree(String vertex){
		
		vertex=vertex.replace(",","");
		
		
		
		for(int i=0;i<tablesWithin.size();++i){
			if(vertex.equals(tablesWithin.get(i).getKey())){
				vertex=vertex+",";
				return graphMetricsOfDBVersion.generateVertexOutDegree(vertex);
			}
		}
		
		return "*,";
//		return -1+",";
		
		
		
	}
	
	public String generateVertexBetweenness(String vertex){
		
		vertex=vertex.replace(",","");
		
		
		
		for(int i=0;i<tablesWithin.size();++i){
			if(vertex.equals(tablesWithin.get(i).getKey())){
				vertex=vertex+",";
				return graphMetricsOfDBVersion.generateVertexBetweenness(vertex);
			}
		}
		
		return "*,";
//		return -1+",";
		
		
		
	}

	public String generateEdgeBetweenness(String edge) {
		
		edge=edge.replace(",","");
		
		
		
		for(int i=0;i<versionForeignKeys.size();++i){
			if(edge.equals(versionForeignKeys.get(i).getSourceTable()+"|"+versionForeignKeys.get(i).getTargetTable())){
				edge=edge+",";
				return graphMetricsOfDBVersion.generateEdgeBetweenness(edge);
			}
		}
		
		return "*,";
//		return -1+",";
		
		
		
	}
	
	public String getGraphDiameter(){
		
		
		return graphMetricsOfDBVersion.getGraphDiameter();
		
		
	}
	
	public String getVertexCount(){
		
		return graphMetricsOfDBVersion.getVertexCount();
		
	}
	
	public String getVertexCountForGcc() {
		
		return graphMetricsOfDBVersion.getVertexCountForGcc();
		
	}
	
	public String getEdgeCount(){
		
		
		return graphMetricsOfDBVersion.getEdgeCount();		
		
	}
	
	public String getEdgeCountForGCC(){
		
		return graphMetricsOfDBVersion.getEdgeCountForGcc();		
		
	}
	
	public String generateConnectedComponentsCountReport(){
		
		return graphMetricsOfDBVersion.getNumberOfConnectedComponents();
		
	}
	
	public Map<String,Double> getClusteringCoefficient(){
		
		return graphMetricsOfDBVersion.getClusteringCoefficient();
		
	}
	
}
