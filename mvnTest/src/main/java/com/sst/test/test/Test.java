package com.sst.test.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;
 
public class Test {
 
  
   public static void main(String args[]) throws Exception{
	   
	   ArrayList<String> dsas = new ArrayList<String>();
	  
	   
	   FileWriter sd = new FileWriter(new File("D:\\Files\\asb.json") );
	   
	  // String string = "testCaseKey, status \n  TES-T1, Pass  \n  TES-T2, Pass \n  TES-T3, Pass  \n  TES-T4, Pass " ; 
			  
	   String string1 = String.join(" ", jenkinsStatus());
			   
			  //System.out.println(string);
			  
			  String string = string1.substring(0, string1.length() - 2);
			  
			  System.out.println(string);
			  
			 JSONArray result = CDL.toJSONArray("testCaseKey, status \n  "+string);
			 
			 for (int i = 0; i < result.length(); i++) {
				 
				 System.out.println(result.get(i));
				 dsas.add(result.get(i).toString());
				
			}
			 
			 sd.write("[");
			 String j = ",";
			 for (int i = 0; i < dsas.size(); i++) {
				 
				 sd.write(dsas.get(i));
				 if (i < dsas.size()-1) {
					 
					 sd.write(j) ;
					
				}
				
			}
			 sd.write("]");
			 sd.close();
   }
   
   //****************************************************************************************************************
   public static ArrayList<String> jenkinsStatus() throws Exception{
   	
		getJenkinsTestResults("http://localhost:8080/job/Sel_Pr5/lastBuild/testReport/api/json?pretty=true","admin","admin" );
		
	    File file = new File("D:\\Files\\sample3.json");
	    String content = FileUtils.readFileToString(file, "utf-8");
	   // System.out.println("Reading JSON file from Java program");
	    JSONObject json1 = new JSONObject(content);
	    
	    Object as1 = json1.getJSONArray("childReports").get(0);
	    JSONObject json3 = new JSONObject(as1.toString());
	    Object as5 = json3.getJSONObject("result").getJSONArray("suites").get(0);
	    JSONObject json2 = new JSONObject(as5.toString());
	       
	  //  System.out.println(json2.getJSONArray("cases").length());
	      
		ArrayList<String> al = new ArrayList<String>();
		String testKey1 = "";
		String testStatus = "";
		String ActResult = "";
	    int flag1 = 0;
	    
	    
	    
	    for (int i = 0; i < json2.getJSONArray("cases").length(); i++) {
	    	    
	    	String vb = json2.getJSONArray("cases").getJSONObject(i).get("className").toString();
	    	String[] firstString =		vb.split("[.]");
			String[] scndString = firstString[3].split("_");
			String testKey = scndString[0]+"-"+scndString[1];
		
			//Formatting values 
			String result = json2.getJSONArray("cases").getJSONObject(i).get("status").toString();
			String testStatus1 = "";
			
			if (result.equalsIgnoreCase("FAILED") || result.equalsIgnoreCase("SKIPPED")) {
				testStatus1 = "Fail";
				flag1 = 1;
				
			} 
			else if (result.equalsIgnoreCase("PASSED") ) {
				testStatus1 = "Pass";
				flag1 = 1;
			} 
			
			
			if (i == 0) {
				testStatus = testStatus1;
			}
			

			if(!testKey1.equalsIgnoreCase(testKey) && testStatus.equalsIgnoreCase("Pass") ){
				
				ActResult = "Pass";
				flag1 = 1;
			}
			
			if(!testKey1.equalsIgnoreCase(testKey) && testStatus.equalsIgnoreCase("Fail") ){
				
				ActResult = "Fail";
				flag1 = 1;
			}
			

			if(testKey1.equalsIgnoreCase(testKey) && testStatus.equalsIgnoreCase("Pass") && testStatus1.equalsIgnoreCase("Fail") ){
				
				ActResult = "Fail";
			}
			
			if(!testKey1.equalsIgnoreCase(testKey) && testStatus.equalsIgnoreCase("Fail") && testStatus1.equalsIgnoreCase("Pass") ){
				
				ActResult = "Fail";
			}
			
			
			
			
		/*	if(testKey1.equalsIgnoreCase(testKey) && !testStatus.equalsIgnoreCase("Pass") )
			{
				if (result.equalsIgnoreCase("FAILED") || result.equalsIgnoreCase("SKIPPED")) {
					testStatus1 = "Fail";
					flag1 = 1;
				} else if(testStatus1.equalsIgnoreCase("Pass") )
				{
					testStatus1 = "Fail";
					flag1 = 1;
				}
			}
			
			
			if(testKey1.equalsIgnoreCase(testKey) && testStatus.equalsIgnoreCase("Pass") && flag1 == 1)
			{
				if (result.equalsIgnoreCase("PASSED")) {
					testStatus1 = "Pass";
					flag1 = 1;
				} else{testStatus1 = "Fail";
				flag1 = 1;}
				
			}
				*/
			
	/*		if (testKey1.equalsIgnoreCase(testKey)) {
				if (result.equalsIgnoreCase("FAILED") || result.equalsIgnoreCase("SKIPPED")) {
					testStatus1 = "Fail";
					flag1 = 1;
					
				} 
				else if (result.equalsIgnoreCase("PASSED") ) {
					testStatus1 = "Pass";
					flag1 = 1;
				} 
				
			}
			*/
			testStatus = testStatus1;
			
			if (flag1 == 1 ) {
				String sasa = testKey+","+ActResult+" \n" ;
		    	al.add(sasa);
			}
			
	    	
			testKey1 = testKey;
		}
	    
	    for (int i = 0; i < al.size(); i++) {
	    	//System.out.println(al.get(i));
		}

	  	  return al;
	  	  
	    }
   
	 public static String getJenkinsTestResults(String jenkinsURL, String uName, String pswd ) throws IOException{
		 
		 String JsnString = "";
		 
	 try {
	      URL url = new URL (jenkinsURL); // Jenkins URL localhost:8080, job named 'test'
	      String user = uName; // username
	      String pass = pswd; // password or API token----------------------------------------------------------------
	      String authStr = user +":"+  pass;
	      String encoding = DatatypeConverter.printBase64Binary(authStr.getBytes("utf-8"));

	      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	      connection.setRequestMethod("GET");
	      connection.setDoOutput(true);
	      connection.setRequestProperty("Authorization", "Basic " + encoding);
	      InputStream content = connection.getInputStream();
	      BufferedReader in   = new BufferedReader (new InputStreamReader (content));
	      String line;
	      while ((line = in.readLine()) != null) {
	    
	        JsnString = JsnString +line +"\n";
	        
	      }
	    } catch(Exception e) {
	      e.printStackTrace();
	    }
	 
	 
	 
	  FileWriter file = new FileWriter("D:\\Files\\sample3.json");

           file.write(JsnString);
           file.flush();



	return JsnString;
	 }
 
}