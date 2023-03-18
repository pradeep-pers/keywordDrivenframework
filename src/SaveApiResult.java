

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;


public class SaveApiResult {
	public static String Result;
	public static String resultFolderPath,resultFilePath,screenshotFolderPath;
	static String testCaseName,tcNewStatus;
	static String tableids = "";
	static String linkids = "";
	
	public static void Result() throws IOException
	{
		File directory = new File (".");
		//String currPath = directory.getCanonicalPath();
		//String[] subDirs = currPath.split(Pattern.quote(File.separator));
		resultFolderPath = directory.getCanonicalPath()+"//ApiResults";
		File theDir = new File(resultFolderPath);
		if (!theDir.exists()){
			System.out.println("creating directory: " + resultFolderPath);
		    boolean result = theDir.mkdir();  

		     if(result) {    
		       System.out.println("DIR created");  
		     }

		}
		resultFilePath = resultFolderPath+"//"+ApiTest.resultFileName+".html";
		copyTemplate(ApiTest.resultFileName);
		writeHeaderProjectDetails();
	}

	public static void writeResultRow(String currTestCase,String currTestStep,String currTestStepDescription,String testkeyword,String testParameter,String testStatus){
		String [] arrResultRow;
		arrResultRow = new String[6];
		arrResultRow[0]= "<td style=\"text-align: center;\">"+"\n"+currTestStep+"</td>"+"\n";
		arrResultRow[1]= "<td>"+"\n"+currTestStepDescription+"</td>"+"\n";
		arrResultRow[2]= "<td style=\"text-align: center;\">"+"\n"+testkeyword+"</td>"+"\n";
		//arrResultRow[2]= "<td style=\"text-align: center;\">"+"\n"+"-"+"</td>"+"\n";
		arrResultRow[3]= "<td style=\"text-align: center;\">"+"\n"+testParameter+"</td>"+"\n";
		arrResultRow[4]= "<td style=\"text-align: center;\">"+"\n"+testStatus+"</td>"+"\n";
		arrResultRow[5]= "<td style=\"text-align: center;\">"+"\n"+keywordLibrary.testStepError+"</td>"+"\n";
		
		//Check for null or empty in any value and replace with -
		
	    if (currTestStep == "" || currTestStep== null)
	    	arrResultRow[0]= "<td style=\"text-align: center;\">"+"\n"+"-"+"</td>";
	    if (currTestStepDescription == "" || currTestStepDescription== null)
	    	arrResultRow[1]= "<td style=\"text-align: center;\">"+"\n"+"-"+"</td>";
	    if (testkeyword == "" || testkeyword== null)
	    	arrResultRow[2]= "<td style=\"text-align: center;\">"+"\n"+"-"+"</td>";
	    if (testParameter == "" || testParameter== null)
	    	arrResultRow[3]= "<td style=\"text-align: center;\">"+"\n"+"-"+"</td>";
	    if (testStatus == "" || testStatus== null)
	    	arrResultRow[4]= "<td style=\"text-align: center;\">"+"\n"+"-"+"</td>";
	    if (keywordLibrary.testStepError == "" || keywordLibrary.testStepError== null)
	    	arrResultRow[5]= "<td style=\"text-align: center;\">"+"\n"+"-"+"</td>";

		if (testStatus=="Fail")
			arrResultRow[4]= "<td style=\"text-align: center; color: rgb(251,0,0) \">"+"\n"+"<strong>Fail</strong>"+"\n"+"</td>"+"\n";	
		
		if (testStatus=="Pass")
			arrResultRow[4]= "<td style=\"text-align: center; color: rgb(34,177,76) \">"+"\n"+"<strong>Pass</strong>"+"\n"+"</td>"+"\n";
		
		String newResultRow = "\n"+"<tr>"+"\n"+arrResultRow[0]+"\n"+arrResultRow[1]+"\n"+arrResultRow[2]+"\n"+arrResultRow[3]+"\n"+arrResultRow[4]+"\n"+arrResultRow[5]+"\n"+"</tr>"+"\n";
		
			
		String Result = FileOperations.read(resultFilePath);
		
		String newResult = Result + newResultRow;
				
		FileOperations.write(resultFilePath, newResult);
	}
	
	static int linkval = 1;
	public static void writeTestCaseNameRow(String ApiUrl, String reqBody, String respJson,String Status,String tcStatus){
		String testCaseNameRow = FileOperations.read(resultFilePath);
		String testCaseName1 = "<td colspan=\"2\" style=\"width: 20%; text-align: center;background-color: rgb(210, 205, 205);\">"+"\n <strong>"+ApiUrl+"</strong> \n"+"</td>";
		String testCaseST = "<td colspan=\"1\" style=\"width: 20%; text-align: center;background-color: rgb(210, 205, 205);\">"+"\n"+"<strong>"+"\n"+reqBody+"</strong> \n"+"</td>";
		String testCaseET = "<td colspan=\"1\" style=\"width: 30%; text-align: center;background-color: rgb(210, 205, 205);\">"+"\n"+"<strong>"+"\n"+respJson+"</strong> \n"+"</td>";
		String testCaseStatus = "<td colspan=\"1\" style=\"width: 20%; text-align: center;background-color: rgb(210, 205, 205);\">"+"\n"+"<strong>"+"\n"+Status+"</strong> \n"+"</td>";
		//String testcaseLink = "<td colspan=\"1\" style=\"width: 15%; text-align: center;background-color: rgb(210, 205, 205);\"><input id=\"lnk"+linkval+"\" type=\"button\" value=\"[+] Expand\" onclick=\"toggle_visibility('tbl"+linkval+"','lnk"+linkval+"');\"></td>";
		
		tableids = tableids+",#tbl"+ linkval;
		linkids = linkids+",#lnk"+ linkval;
		/*
		if (TestSuitRunner.isfirsttestcase==true)
		{
		//String testCaseName = "<tr>"+"\n"+"<td colspan=\"7\" style=\"text-align: center;background-color: rgb(210, 205, 205);\">"+"\n"+testName+"\n"+"</tr>";
			testCaseName = "\n"+"<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 100%;\">"+"\n"+"<tr>"+"\n"+testCaseName1+"\n"+testCaseST+"\n"+testCaseET+"\n"+testCaseStatus+"\n"+testcaseLink+"\n"+"</tr>"+"\n";
		}
		else 
		{
			testCaseName = "</table></tr>"+"\n"+"</table>"+"<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 100%;\">"+"\n"+"<tr>"+"\n"+testCaseName1+"\n"+testCaseST+"\n"+testCaseET+"\n"+testCaseStatus+"\n"+testcaseLink+"\n"+"</tr>"+"\n";
		}*/
		if (tcStatus=="Pass")
		{
			tcNewStatus="<td colspan=\"1\" style=\"width: 10%; text-align: center; color: rgb(34,177,76);background-color: rgb(210, 205, 205);\"><strong>PASS</strong></td>";
		}else
		{
			tcNewStatus="<td colspan=\"1\" style=\"width: 10%; text-align: center; color: rgb(251,0,0);background-color: rgb(210, 205, 205);\"><strong>FAIL</strong></td>";
		}
		testCaseName = "\n"+"<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 100%;\">"+"\n"+"<tr>"+"\n"+testCaseName1+"\n"+testCaseST+"\n"+testCaseET+"\n"+testCaseStatus+"\n"+tcNewStatus+"\n"+"</tr>"+"\n";
		testCaseNameRow = testCaseNameRow + testCaseName;
		FileOperations.write(resultFilePath, testCaseNameRow);
		//String testCaseHeader = "<tr>"+"\n"+"\n"+"<table width=\"100%\" border=\"1\" cellpadding=\"4\" cellspacing=\"0\" id=\"tbl"+linkval+"\">"+"\n"+"<tr>"+"\n"+"<td style=\"width: 10%; text-align: center;background-color:#98AFC7;\"><strong>Test Step Id</strong></td>"+"\n"+"<td style=\"width: 25%; text-align: center;background-color:#98AFC7;\"><strong>Step Description</strong></td>"+"\n"+"<td style=\"width: 20%; text-align: center;background-color:#98AFC7;\"><strong>Test Keyword</strong></td>"+"\n"+"<td style=\"width: 20%; text-align: center;background-color:#98AFC7;\"><strong>Test Parameter</strong></td>"+"\n"+"<td style=\"width: 10%; text-align: center;background-color:#98AFC7;\"><strong>Status</strong></td>"+"\n"+"<td style=\"width: 15%; text-align: center;background-color:#98AFC7;\"><strong>Comments</strong></td>"+"\n"+"</tr>";
		linkval++;
		String testCaseNameRow1 = FileOperations.read(resultFilePath);
		//testCaseNameRow1 = testCaseNameRow1 + testCaseHeader;
		FileOperations.write(resultFilePath, testCaseNameRow1);
	}
	
	public static void copyTemplate(String fileName) throws IOException{
		String varPath = FileOperations.getTemplateProjectFolder();
		String resultPath = varPath+"//Resources//API_Test_Report_Template.html";
		
		Result = FileOperations.read(resultPath);
		
		FileOperations.write(resultFilePath, Result);
	}
	
	public static void writeHeaderProjectDetails(){
		String header = FileOperations.read(resultFilePath);
		header = header.replace("ProjectNameVariable", TestSuitRunner.arrTestCaseBuild[1][1]);
		header = header.replace("ReleaseNameVariable",TestSuitRunner.arrTestCaseBuild[2][1]);
		header = header.replace("BuildVariable",TestSuitRunner.arrTestCaseBuild[3][1]);
		header = header.replace("PlatformVariable", TestSuitRunner.arrTestCaseBuild[4][1]);
		header = header.replace("ExecutedByVariable", TestSuitRunner.arrTestCaseBuild[1][1]);
		header = header.replace("StartTimeVariable", currentDate()+", "+currentTime());
		FileOperations.write(resultFilePath, header);
		
	}
	
	public static void updateTestCaseTimeDetails() throws IOException{
		String resultRow = FileOperations.read(resultFilePath);
		//resultRow = resultRow.replace("TCStartTimeVariable",  currentDate()+", "+currentTime());
		resultRow = resultRow.replace("TCTimeVariable",  currentDate()+", "+currentTime());
		FileOperations.write(resultFilePath, resultRow);
	}
	
	public static void updateTestCaseStatus(String tcStatus) throws IOException{
		String resultRow = FileOperations.read(resultFilePath);
		//System.out.println("Test Case status is :- "+ tcStatus);
		
		if (tcStatus=="Pass")
		{
			tcNewStatus="<td colspan=\"1\" style=\"width: 10%; text-align: center; color: rgb(34,177,76);background-color: rgb(210, 205, 205);\"><strong>PASS</strong></td>";
		}else
		{
			tcNewStatus="<td colspan=\"1\" style=\"width: 10%; text-align: center; color: rgb(251,0,0);background-color: rgb(210, 205, 205);\"><strong>FAIL</strong></td>";
		}
		
		resultRow = resultRow.replace("tcStatus",  tcNewStatus);
		FileOperations.write(resultFilePath, resultRow);
	}
	
	public static void writeHeaderTestRunDetails(Double total,Double passed, Double passRate, Double failed, Double notRun) throws IOException{
		writeNotRunTests();
		String resultRow = FileOperations.read(resultFilePath);
		String myTotal = ""+total.shortValue();
		String myPassed = ""+passed.shortValue();
		String myPassRate = ""+passRate.shortValue();
		String myFailed = ""+failed.shortValue();
		String InvalidVariable = ""+notRun.shortValue();
		resultRow = resultRow.replace("TotalTestCasesVariable", myTotal);
		resultRow = resultRow.replace("PassedVariable", myPassed);
		resultRow = resultRow.replace("PassRateVariable", myPassRate+"%");
		resultRow = resultRow.replace("FailedVariable", myFailed);
		resultRow = resultRow.replace("InvalidVariable", InvalidVariable);
		resultRow = resultRow.replace("EndTimeVariable",  currentDate()+", "+currentTime());
		
		//variable for graph generation
		resultRow = resultRow.replace("PassCount", myPassed);
		resultRow = resultRow.replace("FailCount", myFailed);
		resultRow = resultRow.replace("NotRunCount", InvalidVariable);
		resultRow = resultRow.replace("tableids", tableids);
		resultRow = resultRow.replace("linkids", linkids);
		String varProjPath = FileOperations.getTemplateProjectFolder();
		String resultPath = varProjPath+"//Resources//Footer.html";
		String footer = FileOperations.read(resultPath);
		resultRow = resultRow + footer;
		
		FileOperations.write(resultFilePath, resultRow);
	}
	
	public static void writeNotRunTests() throws IOException{
		String notRunTests="",NATests="",blockedTests="";
		for (int i=1; i<TestSuitRunner.arrTestCasesMain.length; i++){
			if (TestSuitRunner.arrTestCasesMain[i][TestSuitRunner.GetColumnIndex(TestSuitRunner.arrTestCasesMain, "RunFlag")].equalsIgnoreCase("FALSE"))
				notRunTests= notRunTests+"\n"+"<tr>"+"\n"+"<td colspan=\"2\" style=\"text-align: left;background-color: #FFFFFF;\">"+"\n"+TestSuitRunner.arrTestCasesMain[i][0]+" - "+TestSuitRunner.arrTestCasesMain[i][1]+"\n"+"</td>"+"\n"+"</tr>";
			if (TestSuitRunner.arrTestCasesMain[i][TestSuitRunner.GetColumnIndex(TestSuitRunner.arrTestCasesMain, "RunFlag")].equalsIgnoreCase("NA"))
				NATests= NATests+"\n"+"<tr>"+"\n"+"<td colspan=\"2\" style=\"text-align: left;background-color: #FFFFFF;\">"+"\n"+TestSuitRunner.arrTestCasesMain[i][0]+" - "+TestSuitRunner.arrTestCasesMain[i][1]+"\n"+"</td>"+"\n"+"</tr>";
			if (TestSuitRunner.arrTestCasesMain[i][TestSuitRunner.GetColumnIndex(TestSuitRunner.arrTestCasesMain, "RunFlag")].equalsIgnoreCase("BLOCKED"))
				blockedTests= blockedTests+"\n"+"<tr>"+"\n"+"<td colspan=\"2\" style=\"text-align: left;\">"+"\n"+TestSuitRunner.arrTestCasesMain[i][0]+" - "+TestSuitRunner.arrTestCasesMain[i][1]+"\n"+"</td>"+"\n"+"</tr>";
		}
		String testResult = FileOperations.read(resultFilePath);
		testResult= testResult+"\n"+"</table>"+"\n"+"\n"+"</tr>"+"\n"+"</table>"+"\n"+"<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 100%;\">"+"\n"+"<tr>"+"\n"+"<td colspan=\"1\" style=\"text-align: center;background-color: rgb(210, 205, 205);\"><strong>"+"Tests Not Run"+"</strong>"+"\n"+"</td>"+"\n"+"<td colspan=\"1\" style=\"width: 15%; text-align: center;background-color: rgb(210, 205, 205);\"><input id=\"lnks\" type=\"button\" value=\"[+] Expand\" onclick=\"toggle_visibility('tbls','lnks');\"></td>"+"\n"+"</tr>"+"\n"+"<tr colspan=\"1\"><table width=\"100%\" border=\"1\" cellpadding=\"4\" cellspacing=\"0\" id=\"tbls\">"+notRunTests;
		testResult= testResult+"\n"+"</table>"+"\n"+"</tr>"+"\n"+"</table>"+"\n"+"<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 100%;\">"+"\n"+"<tr>"+"\n"+"<td colspan=\"1\" style=\"text-align: center;background-color: rgb(210, 205, 205);\"><strong>"+"Tests Not Applicable"+"</strong>"+"\n"+"</td>"+"\n"+"<td colspan=\"1\" style=\"width: 15%; text-align: center;background-color: rgb(210, 205, 205);\"><input id=\"lnkd\" type=\"button\" value=\"[+] Expand\" onclick=\"toggle_visibility('tbld','lnkd');\"></td>"+"\n"+"</tr>"+"\n"+"<tr colspan=\"1\"><table width=\"100%\" border=\"1\" cellpadding=\"4\" cellspacing=\"0\" id=\"tbld\">"+NATests;
		testResult= testResult+"\n"+"</table>"+"\n"+"</tr>"+"\n"+"</table>"+"\n"+"<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 100%;\">"+"\n"+"<tr>"+"\n"+"<td colspan=\"1\" style=\"text-align: center;background-color: rgb(210, 205, 205);\"><strong>"+"Blocked Tests"+"</strong>"+"\n"+"</td>"+"\n"+"<td colspan=\"1\" style=\"width: 15%; text-align: center;background-color: rgb(210, 205, 205);\"><input id=\"lnkf\" type=\"button\" value=\"[+] Expand\" onclick=\"toggle_visibility('tblf','lnkf');\"></td>"+"\n"+"</tr>"+"\n"+"<tr colspan=\"1\"><table width=\"100%\" border=\"1\" cellpadding=\"4\" cellspacing=\"0\" id=\"tblf\">"+blockedTests;
		FileOperations.write(resultFilePath, testResult);
	}
	
	public static String dateTimeStamp()
	{
	      Date date = new Date();  
	      SimpleDateFormat sdf;  
	      sdf = new SimpleDateFormat("dd_MMM_yyyy_hh_mm");  
	      String currentDate = sdf.format(date);
	      return currentDate;
	}
	
	public static String currentDate()
	{
	      Date date = new Date();  
	      SimpleDateFormat sdf;  
	      sdf = new SimpleDateFormat("dd MMM yyyy");  
	      String currentDate = sdf.format(date);
	      return currentDate;
	}
	
	
	public static String currentTime()
	{
	      Date date = new Date();  
	      SimpleDateFormat sdf;  
	      sdf = new SimpleDateFormat("hh:mm:ss");  
	      String currentTimeStamp = sdf.format(date);
	      return currentTimeStamp;
	}
	
	public static String currentTimeZone()
	{
	      Date date = new Date();  
	      SimpleDateFormat sdf;  
	      sdf = new SimpleDateFormat("zzz");  
	      String currentDate = sdf.format(date);
	      return currentDate;
	}
	
	public static void copyFile(){
		  // Date dNow = new Date(0);
				String myTimeStamp = dateTimeStamp();
			      try {
			    	  SaveApiResult.copyDirectory(new File(TestSuitRunner.varResultPath),new File(resultFolderPath+myTimeStamp));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	public static void copyDirectory(File sourceLocation , File targetLocation)
		    throws IOException {
		        
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
               // targetLocation.mkdir();
            
            
            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {
            
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);
            
            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        	}
        }
	}
}
	    


