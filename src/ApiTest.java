import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import static com.jayway.restassured.RestAssured.given;

import org.apache.log4j.Logger;
import org.apache.xerces.impl.dv.util.Base64;

public class ApiTest {
	//static Hashtable<String, String> apitest;//Added to load and store api xls #Sagar
	public static String apitestXls=null;//Added to load and store api xls #Sagar
	 public static String [][] arrTestCaseBuildApi=new String [500][500],arrTestCasesMainApi=new String [500][500],arrTestCaseApi=new String [500][500];
	 public static String [][] arrTestCaseVarApi=new String [500][500];
	 public static String [] request=new String [500],response=new String [500],responseParam=new String [500],data=new String [500];
	 static int rowToExecute;
	static int reqParamCount=0;
	static int respParamCount=0;
	 static String requestType;
	 static int iColumnRunFlag,iColumnTestCaseID,iColumnExecuteAs,iColumnAuthorization_Credentials,iColumnClient_ID;
	 public static String varPath;
	 public static String reqJson=null,token=null,username=null,password=null,clientID=null;
	 public static String API_Base_URL=null,Proxy_URL=null,OS_AccessTokenURL=null,FS_AccessTokenURL=null,currReqURL=null,OS_AccessToken=null,FS_AccessToken=null,reqHeader=null;
	 static int Proxy_Port=0,k=1;
	 static Response apiResponse;
	 public static String superAdminUname=null,superAdminPwd=null;
	 //Report Parameters
	 public static String resultFileName;
	 public static String currTestURL,currTestBody,currTestResponse,currTestStatus,currTestFinalStatus="Not Found";
	 static int tcFail=0,tcPass=0,reqPass=0,reqFail=0,respPass=0,respFail=0;
	 static Logger logger = Logger.getLogger(ApiTest.class);
	 
	 //Mobile App Parameters
	 static boolean isFs=false;
	 public static String appKey=null,utcTimestamp=null,utcTimestampSignature=null,createAppKeyURL=null,appKeyPackage=null,appKeyFingerprint=null;	
	 
	 public static boolean main() throws Exception
	 {
		 try {
				varPath = FileOperations.getTemplateProjectFolder();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		    String timeStamp = SaveResult.dateTimeStamp();
			resultFileName = TestSuitRunner.arrTestCaseBuild[1][1]+"_"+TestSuitRunner.arrTestCaseBuild[3][1]+"_"+timeStamp;
			SaveApiResult.Result(); // copy result template and create new result file
	 apitestXls=varPath+"//APITestCases_New.xls";	//Added to load and store api xls #Sagar
	 logger.info("Using "+apitestXls+" as api repository file");
	 arrTestCaseBuildApi=ReadExcel.main(apitestXls,"API_Config");
	 arrTestCasesMainApi = ReadExcel.main(apitestXls,"Main");// contains list of tests cases
	 arrTestCaseVarApi= ReadExcel.main(apitestXls,"Variables");// contains list of tests Variables
	 API_Base_URL=arrTestCaseBuildApi[1][1];
	 OS_AccessTokenURL=arrTestCaseBuildApi[6][1];
	 FS_AccessTokenURL=arrTestCaseBuildApi[7][1];
	 createAppKeyURL=arrTestCaseBuildApi[8][1];
	 appKeyPackage=arrTestCaseBuildApi[9][1];
	 appKeyFingerprint=arrTestCaseBuildApi[10][1];
	 superAdminUname=arrTestCaseBuildApi[11][1];
	 superAdminPwd=arrTestCaseBuildApi[12][1];
	 String appKeyJson="{\"appPackageName\": \""+appKeyPackage+"\",\"appCertificateFingerprint\": \""+appKeyFingerprint+"\"}";
	  
	 
	 if(arrTestCaseBuildApi[2][1].trim().equalsIgnoreCase("TRUE"))
	 {
		 Proxy_URL=arrTestCaseBuildApi[3][1].trim();
		 Proxy_Port=Integer.parseInt(arrTestCaseBuildApi[4][1].trim());
		 System.out.println("Using Proxy_URL "+Proxy_URL+" and Proxy_Port "+Proxy_Port);
		 
	 }
	 if(arrTestCaseBuildApi[5][1].trim().equalsIgnoreCase("TRUE"))
	 {
		 RestAssured.useRelaxedHTTPSValidation();
		 System.out.println("RestAssured.useRelaxedHTTPSValidation() is true");
	 }
	 
	 if(API_Base_URL==null)
		 System.out.println("API URL can't be empty");
	 if(Proxy_URL==null||Proxy_Port==0)
		 System.out.println("Proxy URL/Port can't be empty");
	 else
		 RestAssured.proxy(Proxy_URL, Proxy_Port); 
	
	 
	 try
		{
	// Loop to iterate through all test cases 
				for (int i = 0; i < arrTestCasesMainApi.length; i++)
				{
					// Loop to find empty values 
					for (int j = 1; j < arrTestCasesMainApi[0].length; j++)
					{
						if (arrTestCasesMainApi[i][j]==""){
							arrTestCasesMainApi[i][j]="Empty";
						}
					}
				}
				//Loop to iterate main sheet and pick test cases with runflag as 'TRUE'
				
				for (int iRow = 1; iRow < arrTestCasesMainApi.length; iRow++)
				{
					iColumnRunFlag = TestSuitRunner.GetColumnIndex(arrTestCasesMainApi, "RunFlag");
					iColumnTestCaseID = TestSuitRunner.GetColumnIndex(arrTestCasesMainApi, "Test_Case_ID");
					iColumnExecuteAs = TestSuitRunner.GetColumnIndex(arrTestCasesMainApi, "ExecuteAs");
					iColumnAuthorization_Credentials = TestSuitRunner.GetColumnIndex(arrTestCasesMainApi, "Authorization_Credentials");
					iColumnClient_ID = TestSuitRunner.GetColumnIndex(arrTestCasesMainApi, "Client_ID");
					//Make all the arrays null
					Arrays.fill(request, null);
					Arrays.fill(response, null);
					Arrays.fill(responseParam, null);
					Arrays.fill(data, null);
					String [] creds = arrTestCasesMainApi[iRow][iColumnAuthorization_Credentials].trim().split(",");
					username=creds[0];
					password=creds[1];
					clientID=arrTestCasesMainApi[iRow][iColumnClient_ID].trim();
					if (arrTestCasesMainApi[iRow][iColumnRunFlag].toUpperCase().equalsIgnoreCase("TRUE")) 
					{
						if(arrTestCasesMainApi[iRow][iColumnExecuteAs].trim().equalsIgnoreCase("OS"))
						{
							createAccessTokenOS(username,password);
							token=OS_AccessToken;
							reqHeader="\"Authorization\",\"Bearer "+token+"\"";
							System.out.println("OS access token created User ==> "+username+" Password ==> "+password+" Token ==> "+token);
						}
						else
						{
							if(arrTestCasesMainApi[iRow][iColumnExecuteAs].trim().equalsIgnoreCase("FS"))
							{
								isFs=true;
								if(appKey==null)
								{
									createAccessTokenOS(superAdminUname,superAdminPwd);
									token=OS_AccessToken;
									createAppKey(appKeyJson);
								}
									
								createAccessTokenFS();
								token=FS_AccessToken;
								System.out.println("FS access token created"+token);
							}
							else
								System.out.println("Invalid ExecuteAs ,Please provide OS or FS");
						}
										
						String myval = arrTestCasesMainApi[iRow][iColumnTestCaseID];
						String [] myCol = myval.split(",");
						//System.out.println(myCol[0]);
						//System.out.println(myCol[1]);
						String [] reqType=myCol[0].trim().split("->");
						//System.out.println(reqType[0]);
						//System.out.println(reqType[1]);
						requestType=reqType[0].trim();
						arrTestCaseApi= ReadExcel.main(apitestXls,myCol[0].trim());// contains worksheet of test case to be executed
						
						//Loop to iterate and execute the selected step values  
						CurrentTestCase : for (int tcRow = 1; tcRow<arrTestCaseApi.length; tcRow++)
						{
							
							int tsIDColumn = GetColumnIndex(arrTestCaseApi, "TSID");// get column for TSID
							if(arrTestCaseApi[tcRow][tsIDColumn].equalsIgnoreCase(myCol[1])||myCol[1].equalsIgnoreCase("*"))
							{
								rowToExecute=tcRow;
								executeAPI(myval, rowToExecute);
								//System.out.println(rowToExecute);
								if(!myCol[1].equalsIgnoreCase("*"))
								break CurrentTestCase;
							}
						}
											
					}
					
				}
				
				}
		catch(Exception e)
		{
			logger.error(e);
		}
	      	
	 return(true);//Dummy Implementation
	 }
	 
	 private static void executeAPI(String myval,int rowToExecute)
	 {
		 Arrays.fill(request, null);
			Arrays.fill(response, null);
			Arrays.fill(responseParam, null);
			Arrays.fill(data, null);
		    reqParamCount=0;
			respParamCount=0;
			currReqURL=getCurrentURL(arrTestCaseApi[0][0].trim());
			k=1;
			//If request is of type get start from the 3rd column as 1st column is TSID and second Column is ID
			if(requestType.equalsIgnoreCase("Get")||requestType.equalsIgnoreCase("Delete"))
			{	
			k=2;
			//currReqURL=currReqURL+arrTestCaseApi[rowToExecute][1].trim();
			currReqURL=currReqURL.replace("$ID",arrTestCaseApi[rowToExecute][1].trim());
			System.out.println(currReqURL);
			}
			
			
			//Read the entire row to form the request
			for (;k<arrTestCaseApi[1].length;k++)
			{
				switch(requestType)
				{
				case "Post" :
					try
					{
						if(arrTestCaseApi[rowToExecute][k].equalsIgnoreCase("IGNORE"))
						{
							data=arrTestCaseApi[1][k].trim().split("->");
							request[reqParamCount]=data[1];
							reqParamCount++;
						}
						else
						{
						if(!arrTestCaseApi[rowToExecute][k].equalsIgnoreCase("NA"))
						{
							//check for request or response data and add it to corresponding array
							data=arrTestCaseApi[1][k].trim().split("->");
							if(data[0].equalsIgnoreCase("req"))
							{
								myval=arrTestCaseApi[rowToExecute][k];
								request[reqParamCount]=data[1]+":"+myval;
								reqParamCount++;
							}
							else
							{
								if(data[0].equalsIgnoreCase("resp"))
								{
									myval=arrTestCaseApi[rowToExecute][k];
									if(data[1].equalsIgnoreCase("links.href"))
									{	
									myval="["+API_Base_URL+myval+"]";
									System.out.println(myval);
									}
									response[respParamCount]=myval;
									responseParam[respParamCount]=data[1];
									respParamCount++;
								}
								else
								{
									logger.error("Invalid parameter in column header");
								}
							}
						}
					}
					}		
					catch(Exception e)
					{
						
					}
					break;
				case "Put" :
					try
					{
						if(!arrTestCaseApi[rowToExecute][k].equalsIgnoreCase("NA"))
						{
							//check for request or response data and add it to corresponding array
							data=arrTestCaseApi[1][k].trim().split("->");
							if(data[0].equalsIgnoreCase("req"))
							{
								myval=arrTestCaseApi[rowToExecute][k];
								request[reqParamCount]=data[1]+":"+myval;
								reqParamCount++;
							}
							else
							{
								if(data[0].equalsIgnoreCase("resp"))
								{
									myval=arrTestCaseApi[rowToExecute][k];
									response[respParamCount]=myval;
									responseParam[respParamCount]=data[1];
									respParamCount++;
								}
								else
								{
									logger.error("Invalid parameter in column header");
								}
							}
						}
					}
					catch(Exception e)
					{
						
					}
					break;
				case "Get" :
					try
					{
						if(!arrTestCaseApi[rowToExecute][k].equalsIgnoreCase("NA"))
						{
							data=arrTestCaseApi[1][k].trim().split("->");
							if(data[0].equalsIgnoreCase("resp"))
							{
								myval=arrTestCaseApi[rowToExecute][k];
								response[respParamCount]=myval;
								responseParam[respParamCount]=data[1];
								respParamCount++;
							}
							else
							{
								logger.error("Invalid parameter in column header");
							}
						}
					}
					catch(Exception e)
					{
						
					}
					break;
				case "Delete" :
					try
					{
						if(!arrTestCaseApi[rowToExecute][k].equalsIgnoreCase("NA"))
						{
							data=arrTestCaseApi[1][k].trim().split("->");
							if(data[0].equalsIgnoreCase("resp"))
							{
								myval=arrTestCaseApi[rowToExecute][k];
								response[respParamCount]=myval;
								responseParam[respParamCount]=data[1];
								respParamCount++;
							}
							else
							{
								logger.error("Invalid parameter in column header");
							}
						}
					}
					catch(Exception e)
					{
						
					}
					break;
				}

			}
			int s;
			//Loop to replace variable in request
			
			for(s=0;s<reqParamCount;s++)
			{
				for (int ivar=1; ivar<arrTestCaseVarApi.length;ivar++)
				{
					//System.out.println("ivar ==> "+ivar+" TestObject ==> "+testObject);
					if (request[s].contains("$"+arrTestCaseVarApi[ivar][0]))
					{
						request[s] = request[s].replace("$"+arrTestCaseVarApi[ivar][0], arrTestCaseVarApi[ivar][1]);
						}
					
				}
			}
			for(s=0;s<respParamCount;s++)
			{
				for (int ivar=1; ivar<arrTestCaseVarApi.length;ivar++)
				{
					//System.out.println("ivar ==> "+ivar+" TestObject ==> "+testObject);
					if (response[s].contains("$"+arrTestCaseVarApi[ivar][0]))
					{
						response[s] = response[s].replace("$"+arrTestCaseVarApi[ivar][0], arrTestCaseVarApi[ivar][1]);
						}
					
				}
			}
			
			//Switch to execute request based on request type
			switch(requestType)
			{
			case "Post" :
				try
				{
					reqJson=postJson(request,reqParamCount);
					//createAccessTokenOS();
					postRequest(reqJson);
					currTestBody=reqJson.replace(",",",\n");
					boolean test=verifyResponse();
					if(test)
					{
						currTestStatus=currTestStatus+"\n All response parameters matches the expected response parameters \n Status : PASS";
						tcPass++;
						currTestFinalStatus="Pass";
					}
					else
					{
						currTestStatus=currTestStatus+"\n All response parameters doesn't match the expected response parameters \n Status : FAIL";
						tcFail++;
						currTestFinalStatus="Fail";
					}
				}
				catch(Exception e)
				{
					
				}
				break;
			case "Put" :
				try
				{
					reqJson=postJson(request,reqParamCount);
					//createAccessTokenOS();
					putRequest(reqJson);
					currTestBody=reqJson.replace(",",",\n");
					boolean test=verifyResponse();
					if(test)
					{
						currTestStatus=currTestStatus+"\n All response parameters matches the expected response \n Status : PASS";
						tcPass++;
						currTestFinalStatus="Pass";
					}
					else
					{
						currTestStatus=currTestStatus+"\n All response parameters matches the expected response \n Status : FAIL";
						tcFail++;
						currTestFinalStatus="Fail";
					}
				}
				catch(Exception e)
				{
					
				}
				break;	
			case "Get" :
				try
				{
					//createAccessTokenOS();
					currTestBody="NA";
					getRequest();
					boolean test=verifyResponse();
					if(test)
					{
						currTestStatus=currTestStatus+"\n All response parameters matches the expected response \n Status : PASS";
						tcPass++;
						currTestFinalStatus="Pass";
					}
					else
					{
						currTestStatus=currTestStatus+"\n All response parameters does not matches the expected response \n Status : FAIL";
						tcFail++;
						currTestFinalStatus="Fail";
					}
				}
				catch(Exception e)
				{
					
				}
				break;
			case "Delete" :
				try
				{
					//createAccessTokenOS();
					currTestBody="NA";
					deleteRequest();
					boolean test=verifyResponse();
					if(test)
					{
						currTestStatus=currTestStatus+"\n All response parameters matches the expected response \n Status : PASS";
						tcPass++;
						currTestFinalStatus="Pass";
					}
					else
					{
						currTestStatus=currTestStatus+"\n All response parameters matches the expected response \n Status : FAIL";
						tcFail++;
						currTestFinalStatus="Fail";
					}
				}
				catch(Exception e)
				{
					
				}
				break;
			}
			
			currTestURL=API_Base_URL+currReqURL+"?access_token="+OS_AccessToken+" using credentials username : "+username+" Password : "+password;
			
			//currTestBody=reqJson.replace(",",",\n");
			//currTestResponse
			SaveApiResult.writeTestCaseNameRow(ApiTest.currTestURL,ApiTest.currTestBody,ApiTest.currTestResponse,ApiTest.currTestStatus,ApiTest.currTestFinalStatus);
			
	
	 
	 }
	 private static String getCurrentURL(String urlVariable) {
		 String url=null;
		 try {
			 
		 for (int iRow = 1; iRow < arrTestCaseBuildApi.length; iRow++)
			{
				
					int iColumnParameter = TestSuitRunner.GetColumnIndex(arrTestCaseBuildApi, "Parameter");
					int iColumnValue = TestSuitRunner.GetColumnIndex(arrTestCaseBuildApi, "Value");
					if (arrTestCaseBuildApi[iRow][iColumnParameter].trim().equalsIgnoreCase(urlVariable.trim())){
						
						url=arrTestCaseBuildApi[iRow][iColumnValue].trim();
						return(url);
					}
					
				
			}
		 
		 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
			 System.out.println("URL_Is_Empty_Please_Verify_Variable_Name");
			 return("URL_Is_Empty_Please_Verify_Variable_Name");
		 
		 
	 }
	private static String postJson(String[] request,int count) {
		
		// TODO Auto-generated method stub
		String createJson;
		int i;
		createJson="{";
		for (i=0;i<count;i++)
		{
			createJson=createJson.concat(request[i]);
		}
		createJson=createJson.concat("}");
		System.out.println(createJson);
		System.out.println(reqParamCount);
		System.out.println(respParamCount);
		return(createJson);
		
	}
	
private static void createAccessTokenOS(String username,String password) {
		try
		{
		  Response resp1 = given().auth().preemptive().basic(username,password).get(API_Base_URL+OS_AccessTokenURL+clientID);
		  System.out.println(resp1.getStatusCode());
		  System.out.println(resp1.asString());	
		  OS_AccessToken = resp1.jsonPath().getString("access_token");
		  
		}
		catch(Exception e)
		{
			currTestStatus="Unable to generate access token for Office Staff : "+e;
			
		}
		}
//Implementation to get FS access token is pending

private static void createAccessTokenFS() {
	try
	{
	  long millis = System.currentTimeMillis();
	  utcTimestamp=String.valueOf(millis);
	  utcTimestampSignature=getSHA256WithKey(utcTimestamp,appKeyFingerprint);
	    
	  Response resp1 = given().auth().preemptive().basic(username,password).and().header("utcTimestamp", utcTimestamp,"utcTimestampSignature",utcTimestampSignature,"appkey",appKey).put(API_Base_URL+FS_AccessTokenURL+clientID);;
			  //get(API_Base_URL+FS_AccessTokenURL+clientID);
	  System.out.println(resp1.getStatusCode());
	  System.out.println(resp1.asString());	
	  FS_AccessToken = resp1.jsonPath().getString("access_token");
	  
	}
	catch(Exception e)
	{
		currTestStatus="Unable to generate access token for Office Staff : "+e;
		
	}
	}

private static void postRequest(String requestJson) {
	try
	{
	  /*Response resp1 = given().auth().preemptive().basic(username,password).get(API_Base_URL+OS_AccessTokenURL+clientID);
	  System.out.println(resp1.getStatusCode());
	  System.out.println(resp1.asString());	
	  String token1 = resp1.jsonPath().getString("access_token");
	  System.out.println(API_Base_URL+currReqURL+"?access_token="+OS_AccessToken);*/
		if(isFs==true)
		{
		long millis = System.currentTimeMillis();
		utcTimestamp=String.valueOf(millis);
	    utcTimestampSignature=getSHA256WithKey(utcTimestamp,appKeyFingerprint);
		apiResponse= given().auth().preemptive().basic(username,password).and().and().header("utcTimestamp", utcTimestamp,"utcTimestampSignature",utcTimestampSignature,"appkey",appKey).contentType("application/json").and().body(requestJson).post(API_Base_URL+currReqURL+"?access_token="+token);
		}
		else
		apiResponse= given().auth().preemptive().basic(username,password).and().contentType("application/json").and().body(requestJson).post(API_Base_URL+currReqURL+"?access_token="+token);
	  System.out.println(apiResponse.asString());
	  currTestResponse=apiResponse.asString().replace(",",",\n");;
	  currTestStatus="Post Request Successfull";
	  reqPass++;
	}
	catch(Exception e)
	{
		currTestStatus="Post Request Failed with error : "+e;
		reqFail++;
	}
	}
private static void putRequest(String requestJson) {
	try
	{
	  
		if(isFs==true)
		{
		long millis = System.currentTimeMillis();
		utcTimestamp=String.valueOf(millis);
	    utcTimestampSignature=getSHA256WithKey(utcTimestamp,appKeyFingerprint);
		apiResponse= given().auth().preemptive().basic(username,password).and().and().header("utcTimestamp", utcTimestamp,"utcTimestampSignature",utcTimestampSignature,"appkey",appKey).contentType("application/json").and().body(requestJson).put(API_Base_URL+currReqURL+"?access_token="+token);
		}
		else	
	  apiResponse= given().auth().preemptive().basic(username,password).and().contentType("application/json").and().body(requestJson).put(API_Base_URL+currReqURL+"?access_token="+token);
	  System.out.println(apiResponse.asString());
	  currTestResponse=apiResponse.asString().replace(",",",\n");;
	  currTestStatus="Post Request Successfull";
	  reqPass++;
	}
	catch(Exception e)
	{
		currTestStatus="Post Request Failed with error : "+e;
		reqFail++;
	}
	}
private static void getRequest() {
	try
	{
		if(isFs==true)
		{
		long millis = System.currentTimeMillis();
		utcTimestamp=String.valueOf(millis);
	    utcTimestampSignature=getSHA256WithKey(utcTimestamp,appKeyFingerprint);
		apiResponse= given().auth().preemptive().basic(username,password).and().and().header("utcTimestamp", utcTimestamp,"utcTimestampSignature",utcTimestampSignature,"appkey",appKey).contentType("application/json").get(API_Base_URL+currReqURL+"?access_token="+token);
		}
		else
	  apiResponse = given().auth().preemptive().basic(username,password).get(API_Base_URL+currReqURL+"?access_token="+token);
	  //apiResponse = given().head(reqHeader).then().get(API_Base_URL+currReqURL+"?access_token="+token);
	  System.out.println(apiResponse.getStatusCode());
	  System.out.println(apiResponse.asString());	
	  currTestResponse=apiResponse.asString().replace(",",",\n");;
	  currTestStatus="Get Request Successfull";
	  reqPass++;
	}
	catch(Exception e)
	{
		currTestStatus="Get Request Failed with error : "+e+"\n";
		reqFail++;
	}
	}
private static void deleteRequest() {
	try
	{
		if(isFs==true)
		{
		long millis = System.currentTimeMillis();
		utcTimestamp=String.valueOf(millis);
	    utcTimestampSignature=getSHA256WithKey(utcTimestamp,appKeyFingerprint);
		apiResponse= given().auth().preemptive().basic(username,password).and().and().header("utcTimestamp", utcTimestamp,"utcTimestampSignature",utcTimestampSignature,"appkey",appKey).contentType("application/json").delete(API_Base_URL+currReqURL+"?access_token="+token);
		}
		else
	  apiResponse = given().auth().preemptive().basic(username,password).delete(API_Base_URL+currReqURL+"?access_token="+token);
	  System.out.println(apiResponse.getStatusCode());
	  System.out.println(apiResponse.asString());	
	  System.out.println(apiResponse.asString());
	  currTestResponse=apiResponse.asString().replace(",",",\n");;
	  currTestStatus="Delete Request Successfull";
	  reqPass++;
	}
	catch(Exception e)
	{
		currTestStatus="Delete Request Failed with error : "+e+"\n";
		reqFail++;
	}
	}
private static boolean verifyResponse() {
	int verifyCount=0;
	//boolean isHref=false;
	for(int i=0;i<respParamCount;i++)
	{
		try
		{
		String param=apiResponse.jsonPath().getString(responseParam[i]);
				
		if(param.equalsIgnoreCase(response[i]))
		{
			System.out.println(param+" matches "+response[i]);
			currTestResponse=currTestResponse+"\n"+responseParam[i]+" : "+param+" matches "+response[i]+"\n";
			verifyCount++;
		}
		else
		{
			
		System.out.println(param+" doesn't matches "+response[i]);
		currTestResponse=currTestResponse+"\n"+responseParam[i]+" : "+param+" doesn't matches matches "+response[i]+"\n";
		}
		}
		catch(Exception e)
		{
			System.out.println("Requested Param ==> "+responseParam[i]+" not received as part of response");
		}
	 }
	System.out.println("Total Parameters : "+respParamCount+" Parameters Passed : "+verifyCount+" Parameters Failed : "+(respParamCount-verifyCount));
	currTestResponse=currTestResponse+"\nTotal Parameters : "+respParamCount+" \nParameters Passed : "+verifyCount+" \nParameters Failed : "+(respParamCount-verifyCount);
	if(verifyCount==respParamCount)
	{	
	respPass++;	
	return(true);
	}
	else
	{
	respFail++;	
	return(false);
	}
		
	}

	public static int GetColumnIndex(String arrExcelData[][], String columnName) throws IOException
	{
		int columnIndex = -1;
				
		CurrentLoop: for (int iColumn=0; iColumn<arrExcelData[0].length; iColumn++)
		{
			if (arrExcelData[1][iColumn].equals(columnName)) 
				{
					columnIndex = iColumn;
					break CurrentLoop;
				}
		}
		
		return columnIndex;
	}
	public static void createAppKey(String requestJson)
	{
		  String token1=null;
		  System.out.println(requestJson);
		  Response resp1 = given().auth().preemptive().basic("admin@acc1","admin@123").and().contentType("application/json").and().body(requestJson).post(API_Base_URL+createAppKeyURL+"?access_token="+OS_AccessToken);
		  System.out.println(resp1.asString());	
		  if(resp1.jsonPath().getString("data.appKey")!=null)
		  {
		  token1 = resp1.jsonPath().getString("data.appKey");
		  appKey=token1;
		  System.out.println(appKey);
		  }
		  else
		  {
			  System.out.println("appKey not generated");
		  }
		  
	}
	private static String getSHA256WithKey(String data, String keyString) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException
	   {
	      SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacSHA256");
	      Mac mac = Mac.getInstance("HmacSHA256");
	      mac.init(key);

	      byte[] bytes = mac.doFinal(data.getBytes("UTF-8"));

	      String hash = Base64.encode(bytes);
	      hash = hash.replace('+', '-');
	      hash = hash.replace('/', '_');
	      
	      return hash;
	   } 

	
}

