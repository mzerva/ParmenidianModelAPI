package model;

import java.io.File;
import parmenidianEnumerations.Metric_Enums;
import java.io.PrintWriter;
import java.util.ArrayList;

public abstract class MetricsReportEngine{
		
	protected String targetFolder;
	private File reportFile;
	protected ArrayList<DBVersion> versions;
	protected GraphMetrics graphMetricsOfDiachronicGraph;
	protected int lines;
	protected int columns;
	protected ArrayList<Table>  vertices;
	protected ArrayList<ForeignKey> edges;
	protected Metric_Enums metric;
	protected String[][] report; 
	private PrintWriter writer;
	
	//Template method: standardizes steps to create metrics reports
	public void generateMetricsReport(){
		
				
		createCsvFile();//create a csv report file
			
		populateArray(); //populate array
		
		printArrayIntoFile(); //print array into report file
		
		
	}
	
	
	//defined here
	public void createCsvFile(){
		
		try{
			
			reportFile = new File(targetFolder+"\\Report of "+metric.name().replace("_", " ").toLowerCase()+".csv");
		
		}catch (Exception e){
			
			System.out.println(e.getClass());
			System.out.println("Problem opening the report file.");
		
		}
			
	}
	
	
	//defined in subclasses ArrayPopulationForGraphMetrics and ArrayPopulationForVertexMetrics
	public abstract void populateArray();
			
	//defined here
	public void printArrayIntoFile(){
		
		try{
			
			writer = new PrintWriter(reportFile);
			
		} catch (Exception e) {
			
			 System.out.println(e.getClass());
			 System.out.println("Problem writing into the report file.");
		}
		
		for(int i=0;i<lines;i++){
			for(int j=0;j<columns;j++)
				writer.print(report[i][j]);
			writer.print("\n");
		}
				
		writer.close();
	}
	
	public String[][] getReport(){
		return report;
	}

}