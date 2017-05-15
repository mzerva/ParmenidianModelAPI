package model;

import java.util.ArrayList;
import java.util.Map;

import parmenidianEnumerations.Metric_Enums;

public class Manager implements ManagerInterface{
	private Parser parser;
	
	public void initialize(String sqlFiles, String xmlFile){
		parser= new Parser(sqlFiles,xmlFile);
	}
	
	public ArrayList<DBVersion> getLifetime(){
		return parser.getLifetime();
	}
	
	public ArrayList<Map<String,Integer>> getTransitions(){
		return parser.getTransitions();
	}
	
	public void getMetricReport(String targetFolder, Metric_Enums metric, String sqlFiles, String xmlFile) throws Exception{
		DiachronicGraph dg = new DiachronicGraph(sqlFiles, xmlFile);
		ReportFactory factory = new ReportFactory();
		MetricsReportEngine engine = factory.getMetricsReportEngine(targetFolder, metric, dg);
		engine.generateMetricsReport();
	}
}
