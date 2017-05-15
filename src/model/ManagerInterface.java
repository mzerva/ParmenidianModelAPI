package model;

import java.util.ArrayList;
import java.util.Map;

import parmenidianEnumerations.Metric_Enums;

public interface ManagerInterface {
	public void initialize(String sqlFiles, String xmlFile);
	
	public ArrayList<DBVersion> getLifetime();
	
	public ArrayList<Map<String,Integer>> getTransitions();
	
	public void getMetricReport(String targetFolder, Metric_Enums metric, String sqlFiles, String xmlFile) throws Exception;

}
