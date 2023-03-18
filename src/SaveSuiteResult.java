import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
public class SaveSuiteResult {
	public static String Result;
	public static String tsresultFolderPath,tsresultFilePath,tlresultFilePath;
	static String testSuiteName,tcNewStatus;
	static String tableids = "";
	static String linkids = "";
	static String tsName;
	
	public static void Result() throws IOException
	{
		File directory = new File (".");
		//String currPath = directory.getCanonicalPath();
		//String[] subDirs = currPath.split(Pattern.quote(File.separator));
		String currDate = SaveResult.dateTimeStamp();
		//tsresultFolderPath = directory.getCanonicalPath()+"//Results//"+currDate;
		
		tsresultFolderPath =SaveResult.resultFolderPath;
		File theDir = new File(tsresultFolderPath);
		if (!theDir.exists()){
			System.out.println("creating directory: " + tsresultFolderPath);
		    boolean result = theDir.mkdir();  

		     if(result) {    
		       System.out.println("DIR created");  
		     }

		}
		tsresultFilePath = tsresultFolderPath+"//"+TestSuitRunner.tsresultFileName+".html";
		tlresultFilePath=tsresultFolderPath+"//Testlink_"+TestSuitRunner.tsresultFileName+".html";
		copyTemplate(TestSuitRunner.tsresultFileName);
		copyTestLinkTemplate(tlresultFilePath);
		writeHeaderProjectDetails();
	}
	public static void copyTemplate(String fileName) throws IOException{
		String varPath = FileOperations.getTemplateProjectFolder();
		String resultPath = varPath+"//Resources//TestSuite_Report_Template.html";
		
		Result = FileOperations.read(resultPath);
		
		FileOperations.write(tsresultFilePath, Result);
	}
	public static void copyTestLinkTemplate(String fileName) throws IOException{
		String varPath = FileOperations.getTemplateProjectFolder();
		String resultPath = varPath+"//Resources//TestLink.html";
		
		Result = FileOperations.read(resultPath);
		
		FileOperations.write(tlresultFilePath, Result);
	}
	public static void writeHeaderProjectDetails(){
		String header = FileOperations.read(tsresultFilePath);
		header = header.replace("ProjectNameVariable", TestSuitRunner.arrTestCaseBuild[1][1]);
		header = header.replace("ReleaseNameVariable",TestSuitRunner.arrTestCaseBuild[2][1]);
		header = header.replace("BuildVariable",TestSuitRunner.arrTestCaseBuild[3][1]);
		header = header.replace("PlatformVariable", TestSuitRunner.arrTestCaseBuild[4][1]);
		header = header.replace("ExecutedByVariable", TestSuitRunner.arrTestCaseBuild[1][1]);
		header = header.replace("StartTimeVariable", SaveResult.currentDate()+", "+SaveResult.currentTime());
		FileOperations.write(tsresultFilePath, header);
		
	}
	
	public static void writeTSResultRow(String currTestSuite,String currSuiteDesc){
		String [] arrResultRow;
		arrResultRow = new String[7];
		arrResultRow[0]= "<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 100%;\"><tr><td style=\"text-align: center;width: 25%;\"><a href = \""+SaveResult.resultFilePath.replace("//", "\\")+"\">\n"+currTestSuite+"</a></td>"+"\n";
		arrResultRow[1]= "<td style=\"text-align: center;width: 25%;\">"+"\n"+currSuiteDesc+"</td>"+"\n";
		arrResultRow[2]= "<td style=\"text-align: center;width: 10%;\">TotalTC</td>"+"\n";
		//arrResultRow[2]= "<td style=\"text-align: center;\">"+"\n"+"-"+"</td>"+"\n";
		arrResultRow[3]= "<td style=\"text-align: center;width: 10%;\">PassTC</td>"+"\n";
		arrResultRow[4]= "<td style=\"text-align: center;width: 10%;\">FailTC</td>"+"\n";
		arrResultRow[5]= "<td style=\"text-align: center;width: 10%;\">NotRunTC</td>"+"\n";
		arrResultRow[6]= "<td style=\"text-align: center;width: 10%;\">PerTC</td></tr></table>"+"\n";
		
		//Check for null or empty in any value and replace with -
		
	    if (currTestSuite == "" || currTestSuite== null )
	    	arrResultRow[0]= "<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 100%;\"><tr><td style=\"text-align: center;width: 25%;\"><a href = \""+SaveResult.resultFilePath.replace("//", "\\")+"\">"+"\n"+"-"+"</a></td>";
	    if (currSuiteDesc == "" || currSuiteDesc== null)
	    	arrResultRow[1]= "<td style=\"text-align: center;width: 25%;\">"+"\n"+"-"+"</td>";
	   
		String newResultRow = "\n"+arrResultRow[0]+"\n"+arrResultRow[1]+"\n"+arrResultRow[2]+"\n"+arrResultRow[3]+"\n"+arrResultRow[4]+"\n"+arrResultRow[5]+"\n"+arrResultRow[6]+"\n"+"</tr>"+"\n";
		
			
		String Result = FileOperations.read(tsresultFilePath);
		
		String newResult = Result + newResultRow;
				
		FileOperations.write(tsresultFilePath, newResult);
	}
	/*
	public static void writeTestSuiteNameRow(String testSuiteName, String testSuiteDesc){
		String testSuiteNameRow = FileOperations.read(tsresultFilePath);
		String tsNamerow;
		int linkval=1;
		String tsName = "<td colspan=\"2\" style=\"width: 35%; text-align: center;background-color: rgb(210, 205, 205);\">"+"\n <strong><a href=\""+SaveResult.resultFolderPath+"//"+TestSuitRunner.resultFileName+".html"+"\">"+testSuiteName+"</a></strong> \n"+"</td>";
		String tsDesc = "<td colspan=\"1\" style=\"width: 20%; text-align: center;background-color: rgb(210, 205, 205);\">"+"\n"+"<strong>"+"\n"+testSuiteDesc+"</strong> \n"+"</td>";
		String tsTotal ="<td colspan=\"1\" style=\"width: 20%; text-align: center;background-color: rgb(210, 205, 205);\">"+"\n"+"<strong>TotalTC</strong> \n"+"</td>";
		String tsPass ="<td colspan=\"1\" style=\"width: 20%; text-align: center;background-color: rgb(210, 205, 205);\">"+"\n"+"<strong>PassTC</strong> \n"+"</td>";
		String tsFail ="<td colspan=\"1\" style=\"width: 20%; text-align: center;background-color: rgb(210, 205, 205);\">"+"\n"+"<strong>FailTC</strong> \n"+"</td>";
		String tsNotRun ="<td colspan=\"1\" style=\"width: 20%; text-align: center;background-color: rgb(210, 205, 205);\">"+"\n"+"<strong>NotRunTC</strong> \n"+"</td>";
		String tsPassPer ="<td colspan=\"1\" style=\"width: 20%; text-align: center;background-color: rgb(210, 205, 205);\">"+"\n"+"<strong>PerTC</strong> \n"+"</td>";
		String testcaseLink = "<td colspan=\"1\" style=\"width: 15%; text-align: center;background-color: rgb(210, 205, 205);\"><input id=\"lnk"+linkval+"\" type=\"button\" value=\"[+] Expand\" onclick=\"toggle_visibility('tbl"+linkval+"','lnk"+linkval+"');\"></td>";
		
		tableids = tableids+",#tbl"+ linkval;
		linkids = linkids+",#lnk"+ linkval;
		
		if (TestSuitRunner.isfirsttestsuite==true)
		{
		//String testCaseName = "<tr>"+"\n"+"<td colspan=\"7\" style=\"text-align: center;background-color: rgb(210, 205, 205);\">"+"\n"+testName+"\n"+"</tr>";
			tsNamerow = "\n"+"<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 100%;\">"+"\n"+"<tr>"+"\n"+tsName+"\n"+tsDesc+"\n"+tsTotal+"\n"+tsPass+"\n"+tsFail+"\n"+tsNotRun+"\n"+tsPassPer+"\n"+testcaseLink+"\n"+"</tr></table>"+"\n";
		}
		else 
		{
			//tsNamerow = "</table></tr>"+"\n"+"<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 100%;\">"+"\n"+"<tr><a href=\""+SaveResult.resultFolderPath+"//"+TestSuitRunner.resultFileName+".html"+"\">"+"\n"+tsName+"</a>\n"+tsDesc+"\n"+tsTotal+"\n"+tsPass+"\n"+tsFail+"\n"+tsPassPer+"\n"+testcaseLink+"\n"+"</tr>"+"\n";
			tsNamerow = "\n"+"<tr>"+"\n"+tsName+"\n"+tsDesc+"\n"+tsTotal+"\n"+tsPass+"\n"+tsFail+"\n"+tsPassPer+"\n"+testcaseLink+"\n"+"</tr></table>"+"\n";
    	}
		testSuiteNameRow = testSuiteNameRow + tsNamerow;
		FileOperations.write(tsresultFilePath, testSuiteNameRow);
		/*String testCaseHeader = "<tr>"+"\n"+"\n"+"<table width=\"100%\" border=\"1\" cellpadding=\"4\" cellspacing=\"0\" id=\"tbl"+linkval+"\">"+"\n"+"<tr>"+"\n"+"<td style=\"width: 10%; text-align: center;background-color:#98AFC7;\"><strong>Test Step Id</strong></td>"+"\n"+"<td style=\"width: 25%; text-align: center;background-color:#98AFC7;\"><strong>Step Description</strong></td>"+"\n"+"<td style=\"width: 20%; text-align: center;background-color:#98AFC7;\"><strong>Test Keyword</strong></td>"+"\n"+"<td style=\"width: 20%; text-align: center;background-color:#98AFC7;\"><strong>Test Parameter</strong></td>"+"\n"+"<td style=\"width: 10%; text-align: center;background-color:#98AFC7;\"><strong>Test Link Id</strong></td>"+"\n"+"<td style=\"width: 10%; text-align: center;background-color:#98AFC7;\"><strong>Status</strong></td>"+"\n"+"<td style=\"width: 15%; text-align: center;background-color:#98AFC7;\"><strong>Comments</strong></td>"+"\n"+"</tr>";
		
		linkval++;
		String testCaseNameRow1 = FileOperations.read(tsresultFilePath);
		testCaseNameRow1 = testCaseNameRow1 + testCaseHeader;
		FileOperations.write(tsresultFilePath, testCaseNameRow1);*/
		/*
	}*/
	public static void writeIndividualResults(Double total,Double passed, Double passRate, Double failed, Double notRun) throws IOException{
		String resultRow = FileOperations.read(tsresultFilePath);
		
		String myTotal = ""+total.shortValue();
		String myPassed = ""+passed.shortValue();
		String myPassRate = ""+passRate.shortValue();
		String myFailed = ""+failed.shortValue();
		String InvalidVariable = ""+notRun.shortValue();
		
		resultRow = resultRow.replace("TotalTC", myTotal);
		resultRow = resultRow.replace("PassTC", myPassed);
		resultRow = resultRow.replace("FailTC", myFailed);
		resultRow = resultRow.replace("NotRunTC", InvalidVariable);
		resultRow = resultRow.replace("PerTC", myPassRate);
		String breakRow="\n<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 100%;\"><tr><td style=\"width: 100%; text-align: center;background-color:#98AFC7;\"></tr></table>";
		resultRow=resultRow+breakRow;
		/*
		String varProjPath = FileOperations.getTemplateProjectFolder();
		String resultPath = varProjPath+"//Resources//Footer.html";
		String footer = FileOperations.read(resultPath);
		resultRow = resultRow + footer;
		*/
		FileOperations.write(tsresultFilePath, resultRow);
	}
	public static void writeHeaderTestSuiteRunDetails(Double total,Double passed, Double passRate, Double failed, Double notRun) throws IOException{
		String resultRow = FileOperations.read(tsresultFilePath);
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
		resultRow = resultRow.replace("EndTimeVariable",  SaveResult.currentDate()+", "+SaveResult.currentTime());
		
		//variable for graph generation
		resultRow = resultRow.replace("PassCount", myPassed);
		resultRow = resultRow.replace("FailCount", myFailed);
		resultRow = resultRow.replace("NotRunCount", InvalidVariable);
		//resultRow = resultRow.replace("tableids", tableids);
		//resultRow = resultRow.replace("linkids", linkids);
		String varProjPath = FileOperations.getTemplateProjectFolder();
		String resultPath = varProjPath+"//Resources//Footer.html";
		String footer = FileOperations.read(resultPath);
		resultRow = resultRow + footer;
		
		FileOperations.write(tsresultFilePath, resultRow);
	}
	public static void writeTestLinkDetails(String TestSuite,String tot,String pass,String fail,String passPer){
		String resultRow = FileOperations.read(tlresultFilePath);
		
		resultRow=resultRow.replace("['TestSuite',  TotTlink,      PassTlink,         FailTlink,             PassPerTlink],","['TestSuite',  TotTlink,      PassTlink,         FailTlink,             PassPerTlink],['TestSuite',  TotTlink,      PassTlink,         FailTlink,             PassPerTlink],");
		resultRow=resultRow.replaceFirst("TotTlink", tot);
		resultRow=resultRow.replaceFirst("PassTlink",pass);
		resultRow=resultRow.replaceFirst("FailTlink",fail);
		resultRow=resultRow.replaceFirst("PassPerTlink",passPer);
		resultRow=resultRow.replaceFirst("TestSuite",TestSuite);
		FileOperations.write(tlresultFilePath, resultRow);
		replaceTestSuiteDetails(tot,pass,fail,passPer);
		}
	public static void removeTestLinkDetails(String tot,String pass,String fail,String passPer){
		String resultRow = FileOperations.read(tlresultFilePath);
		
		resultRow=resultRow.replace("['TestSuite',  TotTlink,      PassTlink,         FailTlink,             PassPerTlink],","['TestSuite',  TotTlink,      PassTlink,         FailTlink,             PassPerTlink]");
		resultRow=resultRow.replaceFirst("TotTlink", tot);
		resultRow=resultRow.replaceFirst("PassTlink",pass);
		resultRow=resultRow.replaceFirst("FailTlink",fail);
		resultRow=resultRow.replaceFirst("PassPerTlink",passPer);
		resultRow=resultRow.replaceFirst("TestSuite","Total");
		resultRow=resultRow.replaceFirst("TotalTestlinkTC", tot);
		resultRow=resultRow.replaceFirst("PassTestlinkTC",pass);
		resultRow=resultRow.replaceFirst("FailTestlinkTC",fail);
		resultRow=resultRow.replaceFirst("PassPercentage",passPer);
		resultRow = resultRow.replace("tableids", tableids);
		resultRow = resultRow.replace("linkids", linkids);
		FileOperations.write(tlresultFilePath, resultRow);
		}
	static int linkval = 1;
	public static void writeTestSuiteNameRow(String testName, String tot,String pass,String fail,String passPer){
		String testSuiteNameRow = FileOperations.read(tlresultFilePath);
		String tsName = "<td colspan=\"2\" style=\"width: 50%; text-align: center;background-color: rgb(210, 205, 205);\">"+"\n <strong>"+testName+"</strong> \n"+"</td>";
		String tsTotal = "<td colspan=\"1\" style=\"width: 10%; text-align: center;background-color: rgb(210, 205, 205);\">"+"\n"+"<strong>"+"\n"+tot+"</strong> \n"+"</td>";
		String tsPass = "<td colspan=\"1\" style=\"width: 10%; text-align: center;background-color: rgb(210, 205, 205);\">"+"\n"+"<strong>"+"\n"+pass+"</strong> \n"+"</td>";
		String tsFail ="<td colspan=\"1\" style=\"width: 10%; text-align: center;background-color: rgb(210, 205, 205);\">"+"\n"+"<strong>"+"\n"+fail+"</strong> \n"+"</td>";
		String Percent="<td colspan=\"1\" style=\"width: 10%; text-align: center;background-color: rgb(210, 205, 205);\">"+"\n"+"<strong>"+"\n"+passPer+"</strong> \n"+"</td>";
		String testcaseLink = "<td colspan=\"1\" style=\"width: 10%; text-align: center;background-color: rgb(210, 205, 205);\"><input id=\"lnk"+linkval+"\" type=\"button\" value=\"[+] Expand\" onclick=\"toggle_visibility('tbl"+linkval+"','lnk"+linkval+"');\"></td>";
		
		tableids = tableids+",#tbl"+ linkval;
		linkids = linkids+",#lnk"+ linkval;
		
		if (TestSuitRunner.isfirsttestsuite==true)
		{
		//String testCaseName = "<tr>"+"\n"+"<td colspan=\"7\" style=\"text-align: center;background-color: rgb(210, 205, 205);\">"+"\n"+testName+"\n"+"</tr>";
			testSuiteName = "\n"+"<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 100%;\">"+"\n"+"<tr>"+"\n"+tsName+"\n"+tsTotal+"\n"+tsPass+"\n"+tsFail+"\n"+Percent+"\n"+testcaseLink+"\n"+"</tr>"+"\n";
		}
		else 
		{
			testSuiteName = "</table></tr>"+"\n"+"<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 100%;\">"+"\n"+"<tr>"+"\n"+tsName+"\n"+tsTotal+"\n"+tsPass+"\n"+tsFail+"\n"+Percent+"\n"+testcaseLink+"\n"+"</tr>"+"\n";
		}
		testSuiteNameRow = testSuiteNameRow + testSuiteName;
		FileOperations.write(tlresultFilePath, testSuiteNameRow);
		String testLinkHeader = "<tr>"+"\n"+"\n"+"<table width=\"100%\" border=\"1\" cellpadding=\"4\" cellspacing=\"0\" id=\"tbl"+linkval+"\">"+"\n"+"<tr>"+"\n"+"<td style=\"width: 10%; text-align: center;background-color:#98AFC7;\"><strong>Test Case</strong></td>"+"\n"+"<td style=\"width: 25%; text-align: center;background-color:#98AFC7;\"><strong>Total</strong></td>"+"\n"+"<td style=\"width: 20%; text-align: center;background-color:#98AFC7;\"><strong>Pass</strong></td>"+"\n"+"<td style=\"width: 20%; text-align: center;background-color:#98AFC7;\"><strong>Fail</strong></td>"+"\n"+"<td style=\"width: 10%; text-align: center;background-color:#98AFC7;\"><strong>Pass Percentage</strong></td>"+"\n"+"\n"+"</tr>";
		linkval++;
		String testSuiteNameRow1 = FileOperations.read(tlresultFilePath);
		testSuiteNameRow1 = testSuiteNameRow1 + testLinkHeader;
		FileOperations.write(tlresultFilePath, testSuiteNameRow1);
	}
	public static void replaceTestSuiteDetails(String tot,String pass,String fail,String passPer){
String resultRow = FileOperations.read(tlresultFilePath);
		
		resultRow=resultRow.replaceFirst("STotTlink", tot);
		resultRow=resultRow.replaceFirst("SPassTlink",pass);
		resultRow=resultRow.replaceFirst("SFailTlink",fail);
		resultRow=resultRow.replaceFirst("SPassPerTlink",passPer);
		FileOperations.write(tlresultFilePath, resultRow);
	}
	public static void writeResultRow(String currTestCase,String tlTotalString,String tlPassString,String tlFailString,String PassPercentage){
		String [] arrResultRow;
		arrResultRow = new String[7];
		arrResultRow[0]= "<td style=\"text-align: center;\">"+"\n"+currTestCase+"</td>"+"\n";
		arrResultRow[1]= "<td style=\"text-align: center;\">"+"\n"+tlTotalString+"</td>"+"\n";
		arrResultRow[2]= "<td style=\"text-align: center; color: rgb(34,177,76) \">"+"\n"+tlPassString+"</td>"+"\n";
		//arrResultRow[2]= "<td style=\"text-align: center;\">"+"\n"+"-"+"</td>"+"\n";
		arrResultRow[3]= "<td style=\"text-align: center; color: rgb(251,0,0) \">"+"\n"+tlFailString+"</td>"+"\n";
		arrResultRow[4]= "<td style=\"text-align: center;\">"+"\n"+PassPercentage+"</td>"+"\n";

		
		//Check for null or empty in any value and replace with -
		
	    if (currTestCase == "" || currTestCase== null)
	    	arrResultRow[0]= "<td style=\"text-align: center;\">"+"\n"+"-"+"</td>";
	    if (tlTotalString == "" || tlTotalString== null)
	    	arrResultRow[1]= "<td style=\"text-align: center;\">"+"\n"+"-"+"</td>";
	    if (tlPassString == "" || tlPassString== null)
	    	arrResultRow[2]= "<td style=\"text-align: center;\">"+"\n"+"-"+"</td>";
	    if (tlFailString == "" || tlFailString== null)
	    	arrResultRow[3]= "<td style=\"text-align: center;\">"+"\n"+"-"+"</td>";
	    if (PassPercentage == "" || PassPercentage== null)
	    	arrResultRow[4]= "<td style=\"text-align: center;\">"+"\n"+"-"+"</td>";
	    
	    String newResultRow = "\n"+"<tr>"+"\n"+arrResultRow[0]+"\n"+arrResultRow[1]+"\n"+arrResultRow[2]+"\n"+arrResultRow[3]+"\n"+arrResultRow[4]+"\n"+"</tr>"+"\n";
		
			
		String Result = FileOperations.read(tlresultFilePath);
		
		String newResult = Result + newResultRow;
				
		FileOperations.write(tlresultFilePath, newResult);
	}
	
}
