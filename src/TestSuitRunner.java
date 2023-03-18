

import java.awt.AWTException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;

public class TestSuitRunner extends Thread {		
	
	//declare all the variables
	private static int driversNum = 5;
	public static List<WebDriver> driver = new ArrayList<WebDriver>(driversNum);
	public static WebDriver ff_driver;
	public static WebDriver android_driver; 
	public static String varResultPath;
	static Hashtable<String, String> objRep;
	public static String keyWordXls=null,ile,resultFileName;
	public static String objRepXls=null;
	public static String currTestCase;
	public static String testParameter;// test parameter  
	public static String testkeyword, testObjectName; // test keyword 
	static String testObject; // test object property  
	static String testStatus;
	public static double tcCount;
	public static double tcFailCount=0;
	public static double ncCount;
	public static TestSuitRunner runTestCases;
    public static String [][] arrTestCaseBuild,arrTestCasesMain,arrTestCase,arrTestData;
	public static String currTestStep;
	public static String currTestDescription,currTestLinkID;
	public static String flagTestCaseStatus = "Pass";
	public static String varPath;
	public static long longTestSuiteStartTimeMilli;
	public static String strTestSuiteStartTime;
	public static int tcRow, iColObjectValue;
	public static double totalTc;
	public static double totalFailedTc;
	public static double totalPassedTc;
	public static double totalNotRunTc;
	public static double passRate;
	static boolean testStepFlag,tlUpdateFlag ;
	boolean colFlag;
	public static boolean driverFlag;
	public static int i=0;
	static int intProceedOnFailColumn;
	static String currDataColumn;
	static String currTestLinkUpdateFlagColumn;
	static String dataColumn;
	public static boolean isfirsttestcase=true;
	public static String [] testLinkID= new String[500],testLinkStatus= new String[500],testLinkNotes=new String[500]; //Added to create map for testlink results
	public static int teslinkCount=0,teslinkCountPass=0,teslinkCountFail=0,testlinkCountTotal=0;//Added to maintain count of testlink results
	public static int steslinkCount=0,steslinkCountPass=0,steslinkCountFail=0,stestlinkCountTotal=0;
	//Multiple test cases
	public static String [] testSuiteName= new String[500],testSuiteStatus= new String[500];
	public static String [][] arrTestSuite,arrTestSuiteBuild;
	public static String TestSuiteXls=null,currtsName,currtsDesc,currtsFile,currtsBuild;
	int iTSRunFlag,iTSName,iTSDesc,iTSFile,iTSBuildFlag;
	public static double stotalTc=0;
	public static double stotalFailedTc=0;
	public static double stotalPassedTc=0;
	public static double stotalNotRunTc=0;
	public static double spassRate=0;
	public static boolean isfirsttestsuite=false;
	public static String tsresultFileName;
	public static boolean masterBuild;
	
	public static int currtctlCount=0;
	public static int currtctlPass=0;
	public static int currtctlFail=0;
	public static String[] testLinkIDs;
	//public static String val;
	static Scanner in = new Scanner(System.in);
	
	//public static String testObjectName;
	static Logger logger = Logger.getLogger(TestSuitRunner.class);
	
	static
	{
		for(int counter=0; counter < driversNum; counter++ )
		{
			driver.add(null);
		}
	}
	
	public static void main(String[] args) throws Exception{
		//System.out.println("testarg1"+args[0]);
		//System.out.println("testarg2"+args[1]);
		TestSuitRunner t1 = new TestSuitRunner();
		t1.start();
	}
	
	public void run() {		
		
		PropertyConfigurator.configure("Resources//log4j.properties");
		
		logger.info("Starting execution of test suit");
		
		try {
			varPath = FileOperations.getTemplateProjectFolder();
			System.out.println(FileOperations.getTestcaseFiles());
		} catch (Exception e) {
			e.printStackTrace();
		}
		TestSuiteXls = varPath+"//TestSuite.xls";
		try
		{
		arrTestSuite = ReadExcel.main(TestSuiteXls,"Main");// contains list of tests cases
		iTSRunFlag = GetColumnIndex(arrTestSuite, "RunFlag");
		iTSName = GetColumnIndex(arrTestSuite, "TestSuiteName");
		iTSDesc = GetColumnIndex(arrTestSuite, "TestSuiteDescription");
		iTSFile = GetColumnIndex(arrTestSuite, "TestSuiteFileName");
		iTSBuildFlag = GetColumnIndex(arrTestSuite, "UseMasterBuildDetails");
		}catch(Exception e)
		{
			System.out.println("RunFlag/TestSuiteName/TestSuiteDescription/TestSuiteFileName column/s not present in the test suite file,Pleaser verify the column count nad names");
		}
		
		arrTestSuiteBuild = ReadExcel.main(TestSuiteXls,"Build_Details");
		String tsprojectName = arrTestSuiteBuild[1][1];
		String tsprojectBuild = arrTestSuiteBuild[3][1];
		if (arrTestSuiteBuild[28][1].toUpperCase().equals("TRUE"))
		UpdateTestlink.createBuild();
		String timeStamp = SaveResult.dateTimeStamp();
		tsresultFileName=tsprojectName+"_"+tsprojectBuild+"_ExecutionReport_"+timeStamp;
		

		try
		{
		SaveResult.setFolderPath();
		
		SaveSuiteResult.Result();
		}catch(Exception e)
		{
			logger.info("Exception creating reports file/folder"+e);
		}
		/*
		//arrTestSuiteBuild = ReadExcel.main(TestSuiteXls,"Build_Details");
		arrTestCaseBuild = ReadExcel.main(TestSuiteXls,"Build_Details");
		String tsprojectName = arrTestCaseBuild[1][1];
		String tsprojectBuild = arrTestCaseBuild[3][1];
			
		String timeStamp = SaveResult.dateTimeStamp();
		resultFileName = tsprojectName+"_"+tsprojectBuild+"_"+timeStamp;
		try {
			SaveResult.Result();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // copy result template and create new result file
		*/
		
		// Loop to iterate through all test Suites 
					for (int i = 0; i < arrTestSuite.length; i++)
					{
						// Loop to find empty values 
						for (int j = 1; j < arrTestSuite[0].length; j++)
						{
							if (arrTestSuite[i][j]==""){
								arrTestSuite[i][j]="Empty";
							}
						}
					}
					
					arrTestSuiteBuild = ReadExcel.main(TestSuiteXls,"Build_Details");
					
					for (int iRow = 1; iRow < arrTestSuite.length; iRow++)//Loop to execute all test suites
					{
						
						
						if (arrTestSuite[iRow][iTSRunFlag].toUpperCase().equalsIgnoreCase("TRUE")) 
						{
							if (isfirsttestsuite=true)
							{
								isfirsttestsuite=false;
							}
							else
								isfirsttestsuite=true;
		keyWordXls = varPath+"//TestCases//"+arrTestSuite[iRow][iTSFile];
		objRepXls = varPath+"//TestCases//ObjectRepository.xls";
		currtsName=arrTestSuite[iRow][iTSName];
		currtsDesc=arrTestSuite[iRow][iTSDesc];
		currtsFile=arrTestSuite[iRow][iTSFile];
		currtsBuild=arrTestSuite[iRow][iTSBuildFlag];
		System.out.println(currtsFile);
		String Filename=currtsFile.replace(".","_");
		System.out.println(Filename);
		resultFileName = Filename+"_"+tsprojectName+"_"+tsprojectBuild+"_"+timeStamp;
		
		
		//SaveResult.writeTestSuiteNameRow(currtsName,currtsDesc,currtsFile);
		/*System.out.println("Enter path to TestCases.xls [If in current directory press Enter]");
			keyWordXls = in.nextLine();
		
		System.out.println("Enter path to ObjectRepository.xls [If in current directory press Enter]");
			objRepXls = in.nextLine();
		keyWordXls = varPath+"//TestCases.xls";
		objRepXls = varPath+"//ObjectRepository.xls";
		
		if (keyWordXls.equals(null) || keyWordXls.isEmpty()){
			keyWordXls = varPath+"//TestCases.xls";
		}
		
		if (objRepXls.equals(null) || objRepXls.isEmpty()){
				objRepXls = varPath+"//ObjectRepository.xls";
		}*/
		
		/*keyWordXls = RunnerUI.textField.getText();
		objRepXls = RunnerUI.textField_1.getText();*/
				
		
		logger.info("Using "+keyWordXls+" as testcase file");
		logger.info("Using "+objRepXls+" as object repository file");
		if(currtsBuild.trim().equalsIgnoreCase("FALSE"))
		{
		arrTestCaseBuild = ReadExcel.main(keyWordXls,"Build_Details");
		masterBuild=false;
		}
		else
		{
			if(masterBuild==false)
			{
		arrTestCaseBuild = ReadExcel.main(TestSuiteXls,"Build_Details");
		masterBuild=true;
			}
		}
		try {
			SaveResult.Result(); // copy result template and create new result file
			SaveSuiteResult.writeTSResultRow(currtsName,currtsDesc);
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			SaveSuiteResult.writeTestSuiteNameRow(currtsName+"-"+currtsDesc,"STotTlink","SPassTlink","SFailTlink","SPassPerTlink");
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			runSuit(keyWordXls, objRepXls);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		// add test case summary
		totalTc = TestSuitRunner.tcCount;
		totalFailedTc = TestSuitRunner.tcFailCount;
		totalNotRunTc = TestSuitRunner.ncCount;
		totalPassedTc = totalTc - totalFailedTc - totalNotRunTc;
		passRate = ((totalPassedTc/totalTc)*100);
		
		//Add testlink summary
		String tlTotal=""+testlinkCountTotal;
		String tlPass=""+teslinkCountPass;
		String tlFail=""+teslinkCountFail;
		String tlPassPer;
		if(testlinkCountTotal !=0)
			tlPassPer=""+((teslinkCountPass/testlinkCountTotal)*100);
		else
			tlPassPer="";
		String currentTime = SaveResult.currentTime();
		long longSuiteEndTimeMilli=System.currentTimeMillis();
		long longTestDuration = longSuiteEndTimeMilli - longTestSuiteStartTimeMilli;
		int hours = (int) (longTestDuration/(1000*60*60));
		int minutes = (int) (longTestDuration/(1000*60))%60;
		int seconds = (int) (longTestDuration/1000)%60;
		try {
			SaveResult.writeHeaderTestRunDetails(totalTc, totalPassedTc, passRate, totalFailedTc, totalNotRunTc);
			SaveSuiteResult.writeIndividualResults(totalTc, totalPassedTc, passRate, totalFailedTc, totalNotRunTc);
			SaveSuiteResult.writeTestLinkDetails(currtsFile, tlTotal, tlPass, tlFail,tlPassPer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String projectName = arrTestCaseBuild[1][1];
		String projectRelease = arrTestCaseBuild[2][1];
		String projectBuild = arrTestCaseBuild[3][1];
		String projectPlatform = arrTestCaseBuild[4][1];
		String flagUpdateMail = arrTestCaseBuild[5][1];
				
		if (flagUpdateMail.toUpperCase().contentEquals("YES")){
				String mailbody = "TEST RUN SUMMARY"+"\n"+"\n"+"Project			: "+projectName+"\n"+"Release		: "+projectRelease+"\n"+"Build			: "+projectBuild+"\n"+"Platform		: "+projectPlatform+"\n"+"Test suite start time	: "+strTestSuiteStartTime+"\n"+"Test suite end time	: "+currentTime+"\n"+"Test suite duration	: "+hours+" Hours "+minutes+" Minutes "+seconds+" Seconds"+"\n"+"Total test cases		: "+(int)totalTc+"\n"+"Passed test cases	: "+(int)totalPassedTc+"\n"+"Failed test cases	: "+(int)totalFailedTc+"\n"+"Suite pass rate		: " +passRate+"%";		
				String mailFooter = "\n"+"\n"+"\n"+"This is an auto generated mail. You can reply back using 'Reply' or 'Reply to all' option which will send a mail to the respective owners of this test automation run."+"\n"+"\n"+"This email and any attachments are confidential and may also be privileged. If you are not the addressee, do not disclose, copy, circulate or in any other way use or rely on the information contained in this email or any attachments. If received in error, notify the sender immediately and delete this email and any attachments from your system.";
				mailResults.content = mailbody+mailFooter;
				try {
					mailResults.sendMail();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		
		System.out.println("======Test Run Report======");
		System.out.println("Test Suite Sheet Name : "+keyWordXls);
		System.out.println("Total test cases	: "+(int)totalTc+"\n"+"Passed test cases	: "+(int)totalPassedTc+"\n"+"Failed test cases	: "+(int)totalFailedTc);
		System.out.println("===========================");
		/*if (driverFlag==true){
			for (int j=driversNum;j>=0;j--){
				if (driver.get(j)!=null){
					driver.get(j).quit();
				}
			}
		}*/
		//Moved outside the loop to avoid exception 
		
		stotalTc=stotalTc+(int)totalTc;
		stotalPassedTc=stotalPassedTc+(int)totalPassedTc;
		stotalFailedTc=stotalFailedTc+(int)totalFailedTc;
		stotalNotRunTc=stotalNotRunTc+(int)totalNotRunTc;
		stestlinkCountTotal=stestlinkCountTotal+testlinkCountTotal;
		steslinkCountPass=steslinkCountPass+teslinkCountPass;
		steslinkCountFail=steslinkCountFail+teslinkCountFail;
		totalTc=0;
		totalPassedTc=0;
		totalFailedTc=0;
		totalNotRunTc=0;
		testlinkCountTotal=0;
		teslinkCountPass=0;
		teslinkCountFail=0;
		TestSuitRunner.tcCount=0;
		TestSuitRunner.tcFailCount=0;
		TestSuitRunner.ncCount=0;
		
	}//End of If for test file selection
	
	
}//End of for loop of Test Suite	
					
					String stlTotal=""+stestlinkCountTotal;
					String stlPass=""+steslinkCountPass;
					String stlFail=""+steslinkCountFail;
					String stlPassPer;
					if(stestlinkCountTotal!=0)
						stlPassPer=""+((steslinkCountPass/stestlinkCountTotal)*100);
					else
						stlPassPer="";
					SaveSuiteResult.removeTestLinkDetails(stlTotal,stlPass,stlFail,stlPassPer);
					spassRate = ((stotalPassedTc/stotalTc)*100);
					try {
						SaveSuiteResult.writeHeaderProjectDetails();
						SaveSuiteResult.writeHeaderTestSuiteRunDetails(stotalTc, stotalPassedTc, spassRate, stotalFailedTc, stotalNotRunTc);
						} catch (Exception e) {
						e.printStackTrace();
					}
					if (driverFlag==true){
						for (int j=driversNum;j>=0;j--){
							if (driver.get(j)!=null){
								driver.get(j).quit();
							}
						}
					}
					System.exit(0);
	}
	
	
	public static void runSuit(String keyWordXls, String objRepXls) throws Exception {

		String projectName = arrTestCaseBuild[1][1];
		String projectBuild = arrTestCaseBuild[3][1];
		String smscRequired = arrTestCaseBuild[26][1];
		String smscPort = arrTestCaseBuild[27][1];
		
		/*String timeStamp = SaveResult.dateTimeStamp();
		resultFileName = currtsFile+"_"+projectName+"_"+projectBuild+"_"+timeStamp;

		SaveResult.Result(); // copy result template and create new result file*/
		
		boolean smscflag;
		
		if (smscRequired.equalsIgnoreCase("true")){
			Simulator.start(smscPort);
			int timer=1;
			do {
				smscflag = Simulator.listClients();
				if (timer==6){
					System.out.println("===========================Unable to connect SMSC exiting test suit===========================");
					System.exit(0);
				}
				Thread.sleep(10000);
				timer++;
			}while(smscflag==false);
		}


		driverFlag=false;
		strTestSuiteStartTime = SaveResult.currentTime();
		longTestSuiteStartTimeMilli = System.currentTimeMillis();
		tcCount=0;
		objRep = loadObjectRepository(objRepXls, "Repository");// Load the object Repository
		//Read test cases 
		
		arrTestCasesMain = ReadExcel.main(keyWordXls,"Main");// contains list of tests cases 
		
		// Loop to iterate through all test cases 
			for (int i = 0; i < arrTestCasesMain.length; i++)
			{
				// Loop to find empty values 
				for (int j = 1; j < arrTestCasesMain[0].length; j++)
				{
					if (arrTestCasesMain[i][j]==""){
						arrTestCasesMain[i][j]="Empty";
					}
				}
			}
			
			//Loop to pick up the test case 
			for (int iRow = 1; iRow < arrTestCasesMain.length; iRow++)
			{
				if (iRow != 1)
				{
					isfirsttestcase=false;
				}
				int iColumnRunFlag = GetColumnIndex(arrTestCasesMain, "RunFlag");
				if (arrTestCasesMain[iRow][iColumnRunFlag].toUpperCase().equalsIgnoreCase("TRUE")) 
				{
					int iColumnTestCaseId = GetColumnIndex(arrTestCasesMain, "TestCaseId");//gets the column 
					currTestCase = arrTestCasesMain[iRow][iColumnTestCaseId];// contains the current test case 
					int iColumnTestCaseDesc = GetColumnIndex(arrTestCasesMain, "Description");//gets the column 
					currTestDescription = arrTestCasesMain[iRow][iColumnTestCaseDesc];// contains the current test case
					int iColumnTestLinkID = GetColumnIndex(arrTestCasesMain, "TestLink-TC_ID");//gets the column 
					currTestLinkID = arrTestCasesMain[iRow][iColumnTestLinkID];// contains the current test case
					int intDataColumn = GetColumnIndex(arrTestCasesMain, "DataColumnName");// get column index
					currDataColumn = arrTestCasesMain[iRow][intDataColumn].trim();
					int iColumnTestlinkUpdate = GetColumnIndex(arrTestCasesMain, "TestLink-Update-EachTestData");// get column index
					currTestLinkUpdateFlagColumn = arrTestCasesMain[iRow][iColumnTestlinkUpdate].trim();
					String [] myCol = currDataColumn.split(",");
					teslinkCount=0;
					//Loop for multiple data column
					if (myCol.length>0)
					{
						for (int i=0; i<myCol.length; i++)
						{
							dataColumn =  myCol[i];
							if (dataColumn.contentEquals("Empty") ||  dataColumn.contentEquals("") )
							{
								tcCount++;
								ncCount++;
								if (myCol.length<2)
								{
									keywordLibrary.testStepError = keywordLibrary.testStepError + "\n"+"Please Provide Data Column Name in Main Sheet for test case : "+currTestCase;
									System.out.println(keywordLibrary.testStepError + "\n"+"Please Provide Data Column Name in Main Sheet for test case : "+currTestCase);
									break;
								}
								else
								{
									logger.error("Please Provide Data Column Name in Main Sheet for test case : "+currTestCase);
								}
							}
							else
							{
								SaveResult.writeTestCaseNameRow(TestSuitRunner.currTestCase+" - "+TestSuitRunner.currTestDescription,SaveResult.currentDate()+", "+SaveResult.currentTime(),"TCTimeVariable");
								tcCount++;
								arrTestCase = ReadExcel.main(keyWordXls,currTestCase);
								logger.info("Executing test case :- " + currTestCase);
								flagTestCaseStatus = "Pass"; // set flag to true before executing each test case
								
								System.out.println("Iteration : "+i);
								currtctlCount=0;
								currtctlPass=0;
								currtctlFail=0;
								
								//Loop to read the test step values  
								CurrentTestCase : for (tcRow = 1; tcRow<arrTestCase.length; tcRow++)
								{
									iColObjectValue = GetColumnIndex(arrTestCase, "OutputValues");
									int iObjectColumn = GetColumnIndex(arrTestCase, "ObjectName");// get object name
									testObjectName = arrTestCase[tcRow][iObjectColumn];
									if (!testObjectName.isEmpty()){
										
										
										try{
											if(arrTestCaseBuild[36][1].toUpperCase().equals("TRUE"))
											{
												testObject = dbOperations.getObjectFromDb("select obj_value from obj_repo where obj_name ='"+testObjectName+"'");
												System.out.println(testObject);
											}
											else
											{
											testObject = objRep.get(testObjectName);// test object
											}
											//System.out.println(testObjectName);
											
											
											
										}
										catch (Exception e){
											logger.error("Unable to find xpath in object repository for object"+testObjectName);
										}
									}
									int iKeywordColumn = GetColumnIndex(arrTestCase, "Keyword");// get column of keyword
									testkeyword = arrTestCase[tcRow][iKeywordColumn].trim(); // test keyword  
									int iTestStepColumn = GetColumnIndex(arrTestCase, "TSID");// get column index
									int iTestLinkIDColumn = GetColumnIndex(arrTestCase, "TestLinkID");// get TestLinkID column index
									currTestStep = arrTestCase[tcRow][iTestStepColumn];
									int iTestStepDescription = GetColumnIndex(arrTestCase, "Description");// get column index
									String currTestStepDescription = arrTestCase[tcRow][iTestStepDescription]; 
									int intDataValue = GetColumnIndex(arrTestCase, dataColumn);// get column index
									if (intDataValue==-1)
									{
										keywordLibrary.testStepError=keywordLibrary.testStepError + "\n"+("Data Column Name not found for Test Case "+currTestCase+ " in test case sheet.");
										logger.error(keywordLibrary.testStepError);
										break CurrentTestCase;
									}
									else
									{	
										String currDataValue = arrTestCase[tcRow][intDataValue].trim();
										if (currDataValue.contentEquals(""))
										{
											testParameter="";											testStepFlag = executeTcStep(testkeyword,testParameter, testObject, testObjectName);

										}
										else
										{
											testParameter = currDataValue;
											testStepFlag = executeTcStep(testkeyword,testParameter, testObject, testObjectName);
										}
										String id=null;
										testLinkIDs=null;
										if(arrTestCase[tcRow][iTestLinkIDColumn].trim()!=null)
										{
										testLinkIDs =arrTestCase[tcRow][iTestLinkIDColumn].trim().split(",");
										
										if(i==0)
										id=testLinkIDs[i];
										else
										{
											if(currTestLinkUpdateFlagColumn.toUpperCase().equals("FALSE"))
											id=testLinkIDs[0];
											else
												{
												if(testLinkIDs.length>i)
													id=testLinkIDs[i];
												else
												id="";
											
												}
											}
										System.out.println("testLinkIDs.length"+testLinkIDs.length);
										}
										
										if (testStepFlag==false)
										{
											testStatus = "Fail";
											SaveResult.writeResultRow(currTestCase,currTestStep,id,currTestStepDescription,testkeyword,testParameter,testStatus);
											
											intProceedOnFailColumn = GetColumnIndex(arrTestCase, "ProceedOnFail"); //get column index
												if(flagTestCaseStatus.toLowerCase().equals("pass"))
												{
													tcFailCount++; //increment failed test cases count
												}
												flagTestCaseStatus = "Fail"; //mark test case as failed
											if (arrTestCase[tcRow][intProceedOnFailColumn].toUpperCase().equals("NO"))
											{
												testStatus = "Fail";
												keywordLibrary.testStepError = keywordLibrary.testStepError + "\n"+"Test case "+currTestCase+ " failed at step "+currTestStep+". Aborting test case...";
												logger.error(keywordLibrary.testStepError);
												break CurrentTestCase;
											}
											
										}
										else{
											testStatus = "Pass";
											SaveResult.writeResultRow(currTestCase,currTestStep,id,currTestStepDescription,testkeyword,testParameter,testStatus);
										}
										testLinkIDs =arrTestCase[tcRow][iTestLinkIDColumn].trim().split(",");
										String testNotes="";
										if(i==0)
										{
										id=testLinkIDs[i];
										testNotes=testParameter;
										}
										
										else
										{
											if(testLinkIDs.length>i)
											{
												id=testLinkIDs[i];
												if(currTestLinkUpdateFlagColumn.toUpperCase().equals("FALSE"))
													testNotes=testNotes+":"+testParameter;
													else
														testNotes=testParameter;											}
											else
											id="";
										}
										if(currTestLinkUpdateFlagColumn.toUpperCase().equals("FALSE"))
										{
											id=testLinkIDs[0];
											testNotes=testNotes+":"+testParameter;
										}
										//String[] val=val1.split(",");
										//Update Teslink ID and status in array
										//if(arrTestCase[tcRow][iTestLinkIDColumn]!= "")
										//System.out.println("testLinkIDs.length"+testLinkIDs.length);
										//id=testLinkIDs[i];
										if(!id.isEmpty())
										{
											//String[] val =arrTestCase[tcRow][iTestLinkIDColumn].split(",");
											if(currTestLinkUpdateFlagColumn.toUpperCase().equals("FALSE"))
												testLinkID[teslinkCount]=testLinkIDs[0];
											else
											testLinkID[teslinkCount]=testLinkIDs[i];
											
											//testLinkStatus[teslinkCount]=testStatus;
											if(testStepFlag==true)
											{	
												testLinkStatus[teslinkCount]="Pass";
												testLinkNotes[teslinkCount]="Executed successfully on date : "+SaveResult.currentDate()+" time : "+SaveResult.currentTime()+" for data "+testNotes;
												teslinkCountPass++;
												currtctlPass++;
											}
											else
											{
												testLinkStatus[teslinkCount]="Fail";
												testLinkNotes[teslinkCount]="Executed successfully on date : "+SaveResult.currentDate()+" time : "+SaveResult.currentTime()+" for data "+testNotes;
												teslinkCountFail++;
												currtctlFail++;
											}
											currtctlCount++;
											teslinkCount++;
											testlinkCountTotal++;
											if(currTestLinkUpdateFlagColumn.toUpperCase().equals("FALSE"))
												System.out.println("testLinkID[teslinkCount]"+testLinkIDs[0]);	
											else
											System.out.println("testLinkID[teslinkCount]"+testLinkIDs[i]);
											System.out.println("i"+i);
											System.out.println("testLinkStatus[teslinkCount]=testStatus"+testStatus);
											System.out.println("teslinkCountPass"+teslinkCountPass);
											System.out.println("teslinkCountFail"+teslinkCountFail);
											System.out.println("testlinkCountTotal"+testlinkCountTotal);
											
										}
										testkeyword = "";
										testParameter = "";
										testObject = "";
										testObjectName = "";
																			}
									
								}//End CurrentTestCase
								try
								{
									SaveResult.updateTestCaseStatus(flagTestCaseStatus);
									SaveResult.updateTestCaseTimeDetails();
									
									
								if (arrTestCaseBuild[28][1].toUpperCase().equals("TRUE") && currTestLinkUpdateFlagColumn.toUpperCase().equals("TRUE")) {
									tlUpdateFlag=UpdateTestlink.reportResult();
									System.out.println("Test Link Update Status "+tlUpdateFlag);
									}
								}catch(Exception e){
									logger.error("Testlink Update failed \n"+e);
									tlUpdateFlag=false;
								}
							}//Test run end
							String tlTotal=""+currtctlCount,tlPass=""+currtctlPass,tlFail=""+currtctlFail,tlPassPer;
							if(currtctlCount!=0)
								tlPassPer=""+((currtctlPass/currtctlCount)*100);
							else
								tlPassPer="";
							/*for (int j=0;j<testLinkID.length;j++){
								if (testLinkStatus[j].matches("Pass")){
									tlPass= tlPass+" , "+testLinkID[j];
								}
								else if(testLinkStatus[i].matches("Fail")){
									tlFail= tlFail+","+testLinkID[j];
								}
								tlTotal=tlTotal+","+testLinkID[j];
							}*/
							for (int j=0;j<currtctlCount;j++){
								String currTLID;
								if (currTestLinkUpdateFlagColumn.toUpperCase().equals("FALSE")) {
									currTLID=testLinkIDs[0];
								}
								else
									currTLID=testLinkID[j];
								if (testLinkStatus[j].matches("Pass")){
									tlPass= tlPass +" , "+currTLID;
								}
								else if(testLinkStatus[i].matches("Fail")){
									tlFail= tlFail +","+currTLID;
								}
								tlTotal=tlTotal +","+currTLID;
							}
							SaveSuiteResult.writeResultRow(currTestCase+" - TestData :"+(i+1),tlTotal,tlPass,tlFail,tlPassPer);
							
						}//End loop for multiple data column
						
						try
						{
													
						if (arrTestCaseBuild[28][1].toUpperCase().equals("TRUE") && currTestLinkUpdateFlagColumn.toUpperCase().equals("FALSE")) {
							    System.out.println("Updating testlink post complete execution");
							    tlUpdateFlag=UpdateTestlink.reportResult();
							}
						}catch(Exception e){
							logger.error("Testlink Update failed post complete execution\n"+e);
							tlUpdateFlag=false;
						}
					}//End IF for multiple data column
					else
					{
						tcCount++;
						ncCount++;
						keywordLibrary.testStepError=keywordLibrary.testStepError + "\n"+("Please Provide Data Column Name in Main Sheet for test case"+currTestCase);
					}
					
					Arrays.fill(testLinkID, null);
					Arrays.fill(testLinkStatus, null);
					Arrays.fill(testLinkNotes, null);
					
					
				}
			}
	}
		
	private static boolean executeTcStep(String testkeyword, String testParameter, String testObject, String testObjectName) throws AWTException, IOException {
		keywordLibrary keyWordLib = new keywordLibrary();
		boolean stepResult = keyWordLib.actions(testkeyword, testParameter, testObject, testObjectName);
		return stepResult;
	}
	
	public static int GetColumnIndex(String arrExcelData[][], String columnName) throws IOException
	{
		int columnIndex = -1;
				
		CurrentLoop: for (int iColumn=0; iColumn<arrExcelData[0].length; iColumn++)
		{
			if (arrExcelData[0][iColumn].equals(columnName)) 
				{
					columnIndex = iColumn;
					break CurrentLoop;
				}
		}
		
		return columnIndex;
	}
	
	public static Hashtable<String, String> loadObjectRepository(String xlsName, String wrkShtName) throws Exception {
		 
		    Hashtable<String, String> dict= new Hashtable<String, String>();		
		    
		    try {
		    	String [][]objRep = ReadExcel.main(xlsName, "Repository");
		    	for (int iRow=1;iRow<objRep.length;iRow++){
		    		dict.put(objRep[iRow][0], objRep[iRow][1]);
		    	}
					  return dict;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	

}