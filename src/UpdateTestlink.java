import java.util.Arrays;

import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.api.client.TestLinkAPIException;
import org.dbfacade.testlink.api.client.TestLinkAPIResults;


public class UpdateTestlink {
	//public static String DEVKEY="2f404203b306bd8dd811a7f824c194d0";
	//public static String URL="http://localhost/testlink/lib/api/xmlrpc/v1/xmlrpc.php";
	//public static String DEVKEY="b17ed03a04717c02f59baf96bf3468d4";
	//public static String URL="http://10.44.188.185/testlink/lib/api/xmlrpc/v1/xmlrpc.php";
	//public static final String SERVER_URL = "http://localhost:8600/testlink-1.8.5b/lib/api/xmlrpc.php";
	static String DEVKEY=TestSuitRunner.arrTestSuiteBuild[22][1];
	static String URL=TestSuitRunner.arrTestSuiteBuild[18][1];
	static String testProject=TestSuitRunner.arrTestSuiteBuild[19][1];
	static String testPlan=TestSuitRunner.arrTestSuiteBuild[20][1];
	static String build=TestSuitRunner.arrTestSuiteBuild[21][1];
	static String buildName;
	
	
	public static int i=0;
	//This function checks if the latest build name same as build name in master build details sheet 
	//If build name in master build details sheet is "Nightly" creates a build with MWM_DateTimestamp
	//If both of teh above fails creates a new build with name provided in the  master build details sheet
	public static boolean createBuild()
	{
		TestLinkAPIResults latBuild;
		TestLinkAPIClient api1=new TestLinkAPIClient(DEVKEY.trim(), URL);
		try {
			latBuild=api1.getLatestBuildForTestPlan(testProject, testPlan);
			//latBuild.getValueByName(0, "name");
			System.out.println("Latest Build"+latBuild.toString());
			System.out.println("Latest Build"+latBuild.getValueByName(0, "name").toString());
			if(latBuild.getValueByName(0, "name").toString().equalsIgnoreCase(build))
			{
				System.out.println("Build Exist,Executing on current build");
				buildName=latBuild.getValueByName(0, "name").toString();
				return true;
			}
			else
			{
				String buildNotes;
				if(build.equalsIgnoreCase("Nightly"))
				buildName="MWM_"+SaveResult.dateTimeStamp();
				else
					buildName=build;
				buildNotes="Create build "+buildName+" by automation suite on "+SaveResult.currentDate();
				
				api1.createBuild(testProject, testPlan, buildName, buildNotes);
				System.out.println("Created build"+buildName);
				return true;
			}
			
		} catch (TestLinkAPIException e) {
			System.out.println("Failed to create Build");
			e.printStackTrace();
			return false;
			
		}
	}
	public static boolean reportResult() {
		
		String DEVKEY=TestSuitRunner.arrTestCaseBuild[22][1];
		String URL=TestSuitRunner.arrTestCaseBuild[18][1];
		String testProject=TestSuitRunner.arrTestCaseBuild[19][1];
		String testPlan=TestSuitRunner.arrTestCaseBuild[20][1];
		String build=buildName;
		//String testCases=TestSuitRunner.currTestLinkID;
		TestLinkAPIResults test=null;
		String result=null;
		//String testcase[] = testCases.split(",");
		String [] testLinkID,testLinkStatus,testLinkNotes;
		
		testLinkID=TestSuitRunner.testLinkID;
		testLinkStatus=TestSuitRunner.testLinkStatus;
		testLinkNotes=TestSuitRunner.testLinkNotes;
		System.out.println(DEVKEY);
		System.out.println(URL);
		System.out.println(testProject);
		System.out.println(testPlan);
		System.out.println(build);
		TestLinkAPIClient api=new TestLinkAPIClient(DEVKEY.trim(), URL);
		//Tes
		System.out.println(DEVKEY);
		System.out.println(URL);
		System.out.println(testProject);
		System.out.println(testPlan);
		System.out.println(build);
		
		for (;i<testLinkID.length;i++){
		if (testLinkStatus[i].matches("Pass")){
			result= TestLinkAPIResults.TEST_PASSED;
		}
		else if(testLinkStatus[i].matches("Fail")){
			result=TestLinkAPIResults.TEST_FAILED;
		}
		else {
			System.out.println("Invalid Testlink Status"+testLinkStatus[i]);
		}
		//byte []test1;
		//test1=testLinkID[i];
		//String value;
		//Charset.forName("UTF-8").encode(testLinkID[i]);
		System.out.println("======================================================================================================================");
		System.out.println(i);
		System.out.println(testLinkID[i]);
		System.out.println(testLinkNotes[i]);
		System.out.println(result);
		//test=api.getBuildsForTestPlan(testProject, testPlan);
		//System.out.println(test);
		//test=api.getCasesForTestPlan(testProject, testPlan);
		//System.out.println(test);
		/*value=api.connectErrorMsg;
		System.out.println(value);
		test=api.about();
		System.out.println(test);
		test=api.getTestCaseIDByName(testLinkID[i], testProject, "MWM 2.1");
		System.out.println(test);
		test=api.about();
		System.out.println(test);
		test=api.getLastExecutionResult(testProject, testPlan, testLinkID[i]);
		System.out.println(test);
		test=api.getTestCaseIDByName(testLinkID[i], testProject, "MWM 2.1");
		//test=api.getTestCaseIDByName(testLinkID[i]);
		//api.
		//api.get
		System.out.println(test);*/
		if(result!=null){
			
			try
			{
			System.out.println("test project [" + testProject + "], test plan: [" + testPlan + "], test link id: [" + testLinkID[i] + "], build: [" + build + "], notes: [" + testLinkNotes[i] + "], result: [" + result + "]");
			 api.reportTestCaseResult(testProject, testPlan, testLinkID[i], build, testLinkNotes[i], result);
			 System.out.println("api executed");
			}
			catch(Exception e)
			{
				
				System.out.println("api not executed,Test Link update failed Exception details :\n"+e);
				return false;
			}
			 //System.out.println("===>" + test);
			// api.reportTestCaseResult(testPlan, test, build, testLinkNotes[i], result)
			 //api.reportTestCaseResult(testProject, testPlan, testLinkID[i], build, testLinkNotes[i], result);
			}
		}
		System.out.println("*************************************************************************************************************");
		Arrays.fill(testLinkID, null);
		Arrays.fill(testLinkStatus, null);
		Arrays.fill(testLinkNotes, null);
		return true;
	}
	/*public static void doIt(String DEV_KEY,int testProjectID, int testPlanID, String testCaseExternalID, int version)
	{
		try 
		{
			XmlRpcClient rpcClient;
			XmlRpcClientConfigImpl config;
			
			config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(SERVER_URL));
			rpcClient = new XmlRpcClient();
			rpcClient.setConfig(config);		
			
			ArrayList<Object> params = new ArrayList<Object>();
			Hashtable<String, Object> methodData = new Hashtable<String, Object>();				
			methodData.put("devKey", DEV_KEY);
			methodData.put("testprojectid", testProjectID);
			methodData.put("testplanid", testPlanID);
			methodData.put("testcaseexternalid", testCaseExternalID);
			methodData.put("version", version);
			params.add(methodData);
			
			Object result = rpcClient.execute("tl.addTestCaseToTestPlan", params);
			// Typically you'd want to validate the result here and probably do something more useful with it
			System.out.println("Result was:\n");				
			Map item = (Map)result;
			System.out.println("Keys: " + item.keySet().toString() + " values: " + item.values().toString());
			
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (XmlRpcException e)
		{
			e.printStackTrace();
		}
	}*/
}