import java.awt.AWTException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.selendroid.SelendroidCapabilities;
import io.selendroid.SelendroidConfiguration;
import io.selendroid.SelendroidDriver;
import io.selendroid.SelendroidLauncher;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import static com.jayway.restassured.RestAssured.given;


public class keywordLibrary {
	public static String testStepError;
	public String replacedText, getData1, screenshotFolderPath;
	Boolean flagAction;
	String strFlag;
	String[] getData;
	List<WebDriver> driver = new ArrayList<WebDriver>(5);
	WebDriver android_driver;
	WebDriver ff_driver;
	public WebDriverWait wait;
	int i=0;
	AndroidDriver driver1;
	String DB_CONNECTION_STRING = null;
	String DB_USER = null;
	String DB_PASSWORD = null;
	String fence = null;
	private boolean acceptNextAlert = true;
	String elementType="xpath";
	String ffBinaryPath = null;
	String useFFBinary = "FALSE";
	
	public JSONObject method1(String var) throws JSONException{
		JSONObject jsonObject = new JSONObject(var);
		try{
   		
   		//if (jsonObject.has("data")){
   		if(jsonObject.has("data")){
   			System.out.println("***********************"+jsonObject.getString("data"));
          JSONArray jsonArray = jsonObject.getJSONArray("data");
          
          System.out.println(jsonArray.length());
          //System.out.println(jsonArray.getString(0));
          if(jsonArray.length()>1 && !jsonArray.getString(0).contains("conversation")) {
          jsonObject = jsonArray.getJSONObject(1).getJSONObject("data");
          }
          else{
        	  jsonObject = jsonArray.getJSONObject(0).getJSONObject("data");
          }
         
   		}else {
   			System.out.println("-------------------------");
   			String myxpath = ".//*[@id='messages']/pre[4]";
   			System.out.println(myxpath);
    		WebElement Response = ElementLocator.elementExists(elementType,myxpath);
    		System.out.println("this is response" + Response );
    		//System.out.println("@@@@@@@@@@@@=========="+testObject+"==============");
    		var=Response.getText();
    		System.out.println("$$$$$$$$$$$$$$$$"+var);
    		jsonObject = new JSONObject(var);
    		JSONArray jsonArray = jsonObject.getJSONArray("data");
            
            System.out.println(jsonArray.length());
            System.out.println(jsonObject);
           
            if(jsonArray.length()>1 && !jsonArray.getString(0).contains("conversation")){
            jsonObject = jsonArray.getJSONObject(1).getJSONObject("data");
            }
            else{
          	  jsonObject = jsonArray.getJSONObject(0).getJSONObject("data");
            }
   		}
            
   		}catch(Exception e){
   			e.printStackTrace();
   		}
		return jsonObject;
	
	}	
	
	
	public boolean actions(String testkeyword, String testParameter, String testObject, String testObjectName) throws AWTException, FailingHttpStatusCodeException, MalformedURLException, IOException{
			
		testStepError = "";
		useFFBinary = TestSuitRunner.arrTestCaseBuild[34][1].toUpperCase();
			
		// Trim the keyword
			if (testkeyword!=null)
			{
				testkeyword = testkeyword.toUpperCase().trim();
			}
		//Trim the parameter		
			if (testParameter!=null)
			{
				testParameter = testParameter.trim();
			}
			
			for (int ivar=1; ivar<TestSuitRunner.arrTestCaseBuild.length;ivar++)
			{
				//System.out.println("ivar ==> "+ivar+" TestObject ==> "+testObject);
				if (testParameter.equalsIgnoreCase(TestSuitRunner.arrTestCaseBuild[ivar][0]))
				{
					testParameter = TestSuitRunner.arrTestCaseBuild[ivar][1];
					}
				if(testParameter.contains(TestSuitRunner.arrTestCaseBuild[ivar][0]))
				{
					testParameter =testParameter.replace(TestSuitRunner.arrTestCaseBuild[ivar][0], TestSuitRunner.arrTestCaseBuild[ivar][1]);
					}
			/*	if(testObject!=null && testObject.contains(TestSuitRunner.arrTestCaseBuild[ivar][0]))
				{
					testObject =testObject.replace(TestSuitRunner.arrTestCaseBuild[ivar][0], TestSuitRunner.arrTestCaseBuild[ivar][1]);
					}*/
				
				if(testObject.contains("$"+TestSuitRunner.arrTestCaseBuild[ivar][0]))
				{
					testObject = testObject.replace("$"+TestSuitRunner.arrTestCaseBuild[ivar][0], TestSuitRunner.arrTestCaseBuild[ivar][1]);
					System.out.println( "$"+TestSuitRunner.arrTestCaseBuild[ivar][0] + "Replaced with ====> "+ TestSuitRunner.arrTestCaseBuild[ivar][1]);
					}
			}
			
	    //Trim the test object value		
			if (testObject!=null)
			{
				testObject = testObject.trim();
			}
			
			
			
			try {
				if (StringUtils.isEmpty(testObject)==false) {
					if (!testkeyword.equals("LAUNCH") && !testkeyword.equals("VERIFY_OBJECT_NOT")){
						
						ElementLocator.waitElementExists(elementType, testObject);
					}
				}
			}
			catch (Exception e){
				testStepError = "Initial object not found" ;
				TestSuitRunner.logger.error(testStepError+" TC ID:- "+TestSuitRunner.currTestCase+" TS ID:- "+TestSuitRunner.tcRow,e);
				strFlag = "false";
			}
				
			
			
			switch (testkeyword){
			
			case "CLOSE_ALERT" :
				try {
				      Alert alert = TestSuitRunner.driver.get(TestSuitRunner.i).switchTo().alert();
				      //String alertText = alert.getText();
				      if (acceptNextAlert) {
				        alert.accept();
				        strFlag = "true";
				      } else {
				        alert.dismiss();
				        strFlag = "true";
				      }
				      //return alertText;
				    } 
				catch(Exception e){
					testStepError = "Alert not displayed";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
					
					}
				finally {
				      acceptNextAlert = true;
				      strFlag = "true";
				    }
				break;  
				
			case "SENDKEY_ESCAPE":
				try{
					Actions builder_Enter = new Actions(TestSuitRunner.driver.get(TestSuitRunner.i));
					builder_Enter.sendKeys(Keys.ESCAPE).build().perform();
					strFlag = "true";
				}
				
				catch(Exception e){
					testStepError = "Unable to send key escape";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			case "SENDKEY_ENTER":
				try{
					Actions builder_Enter = new Actions(TestSuitRunner.driver.get(TestSuitRunner.i));
					builder_Enter.sendKeys(Keys.ENTER).build().perform();
					strFlag = "true";
				}
				catch(Exception e){
					testStepError = "Unable to send key enter";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			case "SENDKEY_TAB":
				try{
					Actions builder_Tab = new Actions(TestSuitRunner.driver.get(TestSuitRunner.i));
					builder_Tab.sendKeys(Keys.TAB).build().perform();
					strFlag = "true";
				}
				catch(Exception e){
						testStepError = "Unable to send key tab";
						TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
						strFlag = "false";
					}
				break;
				
			case "SENDKEY_SHIFT_TAB":
				try{
					Actions builder_Shift_Tab = new Actions(TestSuitRunner.driver.get(TestSuitRunner.i));
					builder_Shift_Tab.sendKeys(Keys.SHIFT, Keys.TAB).build().perform(); 
					strFlag = "true";
				}
				catch(Exception e){
						testStepError = "Unable to send key shift+tab";
						TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
						strFlag = "false";
					}
				break;
				
			case "SENDKEYS":
				try{
					Actions builder_keys = new Actions(TestSuitRunner.driver.get(TestSuitRunner.i));
					builder_keys.sendKeys(testParameter).build().perform();
					strFlag = "true";
				}
				catch(Exception e){
						testStepError = "Unable to send key "+testParameter;
						TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
						strFlag = "false";
					}
				break;
				//Adding CTRL + Up arrow key to solve whitespace issue
			case "SENDKEYS_CTRL_UP":
				try{
					//String selectAll = Keys.chord(Keys.CONTROL, Keys.ARROW_UP);
					Actions builder_keys = new Actions(TestSuitRunner.driver.get(TestSuitRunner.i));
					builder_keys.moveByOffset(0,250).click();
					//builder_keys.moveToElement(TestSuitRunner.driver.get(TestSuitRunner.i).findElement(By.xpath("//div[contains(text(),'Displaying 1 -')]"))).click();
					//builder_keys.moveByOffset(0,250).click();
					builder_keys.sendKeys(Keys.CONTROL, Keys.ARROW_UP).build().perform();
					strFlag = "true";
				}
				catch(Exception e){
						testStepError = "Unable to send key "+testParameter;
						TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
						strFlag = "false";
					}
				break;
				
			case "SENDMSG":
				try{
						String[] parameter = testParameter.split(",");
						if(parameter.length<2){
							Simulator.sendMessage(parameter[0],parameter[1]);
							Thread.sleep(15000);
							strFlag = "true";
							
						}else
						{
							testStepError = "Unable to send key "+testParameter;
							TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow);
							strFlag = "false";  
						}
				}
				catch(Exception e){
						testStepError = "Unable to send key "+testParameter;
						TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
						strFlag = "false";
					}
				break;
				
			case "VERIFYMSG":
				try{
					
					String[] parameter = testParameter.split(",");
					if(parameter.length==2){
					boolean verifyflag = Simulator.verifymsg(parameter[0], parameter[1]);
						if (verifyflag==true)
						{
							testStepError = "Message verified successfully";
							TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow);
							strFlag = "true";
						}
						else {
							testStepError = "Message not received";
							TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow);
							strFlag = "false";
						}
					}
				}catch(Exception e){
					testStepError = "Unable to verify msg";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			case "VERIFY_VALUE" :
                try{
                    WebElement text = ElementLocator.elementExists(elementType,testObject);
                    if (testParameter.equals(text.getAttribute("value")))
                    {
                        strFlag = "true";
                        testStepError = "Text Validated Successfully";
                        TestSuitRunner.logger.error(testStepError);
                    }
                    else
                    {
                        testStepError = "Text value does not match. Text displayed is " +text.getAttribute("value") +".";
                        TestSuitRunner.logger.error(testStepError);
                        strFlag = "false";
                    }
                }
                catch (NoSuchElementException e) {
                    testStepError = "Test Object "+testObject+" not found to verify value";
                    TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
                    strFlag = "false";
                }
                catch (Exception e) {
                    testStepError = "Test Object "+testObject+" not found to verify value";
                    TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
                    strFlag = "false";
                }
				break;
							
			case "VERIFY_IS_EMPTY" :
                try{
                	WebElement text = ElementLocator.elementExists(elementType,testObject);
                    if (text.getAttribute("value").isEmpty())
                    {
                    strFlag = "true";
                    testStepError = "Input field is empty";
                    }
                    else
                    {
                       testStepError = "Input field is not empty";
                       TestSuitRunner.logger.error(testStepError);
                       strFlag = "false";
                    }
                }
                catch (NoSuchElementException e) {
                       testStepError = "Test Object "+testObject+" not found to verify value";
                       TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
                       strFlag = "false";
                }
                catch (Exception e) {
                    testStepError = "Test Object "+testObject+" not found to verify value";
                    TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
                    strFlag = "false";
             }
				break;
				
            //Verify disabled               
			case "VERIFY_DISABLED" :
                try{
                    WebElement text = ElementLocator.elementExists(elementType,testObject);
                    if (text.getAttribute("disabled")=="TRUE")
                    {
                    strFlag = "true";
                    testStepError = "Dropdown disabled";
                    TestSuitRunner.logger.info(testStepError);
                    }
                    else
                    {
                       testStepError = "Dropdown is enabled";
                       TestSuitRunner.logger.info(testStepError);
                       strFlag = "false";
                    }
                }
                catch (NoSuchElementException e) {
                       testStepError = "Test Object "+testObject+" not found to verify disabled";
                       TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
                       strFlag = "false";
                }
                catch (Exception e) {
                    testStepError = "Test Object "+testObject+" not found to verify disabled";
                    TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
                    strFlag = "false";
                }
				break;
				
            //Keyword to Click back browser button               
			case "CLICK_BACK":
				try{
					TestSuitRunner.driver.get(TestSuitRunner.i).navigate().back();
					strFlag = "true";
				}
				catch(Exception e){
					testStepError = "Unable to click browser back";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
				//Keyword to Click forward browser button               
			case "CLICK_FORWARD":
				try{
					TestSuitRunner.driver.get(TestSuitRunner.i).navigate().forward();
					strFlag = "true";
				}
				catch(Exception e){
						testStepError = "Unable to click browser forward";
						TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
						strFlag = "false";
				}
				break;
				
			case "LGE_VALIDATE_RESPONSE":
            	try{
            		//String myxpath = ".//*[@id='messages']/pre[4]";
            		WebElement Response = ElementLocator.elementExists(elementType,testObject);
            		System.out.println("this is response" + Response );
            		//System.out.println("@@@@@@@@@@@@=========="+testObject+"==============");
            		String var=Response.getText();
            		//System.out.println("this is var"+ var);
            		//System.out.println("my response is:" + var);
            		
           		//JSONObject jsonObject = new JSONObject(var);
           		//if (jsonObject.has("data")){
                keywordLibrary key = new keywordLibrary();
           		JSONObject json = key.method1(var);
           		
           		/*if(jsonObject.has("data")){
                  JSONArray jsonArray = jsonObject.getJSONArray("data");
                  
                  System.out.println(jsonArray.length());
                  if(jsonArray.length()>1){
                  jsonObject = jsonArray.getJSONObject(1).getJSONObject("data");
                  }
                  else{
                	  jsonObject = jsonArray.getJSONObject(0).getJSONObject("data");
                  }*/
                 
                 String  result =(String) (json.get("text"));
                    
                   System.out.println("my result is:" + result);
                    
                 if(result.contains(testParameter)==true)
                     {                	 
                	 testStepError = "validated "+testParameter+" against actual result"+result;
            		 strFlag = "true";
                     }
           		
           		
           		
           		
            	}catch(Exception e)
             	    {
             		TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
             		testStepError = "Unable find "+testParameter+" to validate";
             		strFlag = "false";
             	    }
                   	break;
				
	/*####################################### FOR MRM ONLY ##################################################################*/
			case "CREATE_FENCE":
				try{
					//URL of Oracle database server
					for (int ivar=1; ivar<TestSuitRunner.arrTestCaseBuild.length;ivar++)
					{
						if (TestSuitRunner.arrTestCaseBuild[ivar][0].equals("DB_CONNECTION_STRING")){
							DB_CONNECTION_STRING = TestSuitRunner.arrTestCaseBuild[ivar][1];
						}
						if (TestSuitRunner.arrTestCaseBuild[ivar][0].equals("DB_USER")){
							DB_USER = TestSuitRunner.arrTestCaseBuild[ivar][1];
						}
						if (TestSuitRunner.arrTestCaseBuild[ivar][0].equals("DB_PASSWORD")){
							DB_PASSWORD = TestSuitRunner.arrTestCaseBuild[ivar][1];
						}
					}
					
			        
					String url = "jdbc:oracle:thin:@"+DB_CONNECTION_STRING;
			        Properties props = new Properties();
			        props.setProperty("user",DB_USER);
			        props.setProperty("password",DB_PASSWORD);
			      
			        //creating connection to Oracle database using JDBC
			        Connection conn = DriverManager.getConnection(url,props);
			
			        //creating PreparedStatement object to execute query
			        PreparedStatement preStatement = conn.prepareStatement(testParameter);
					ResultSet result = preStatement.executeQuery();
					if (result!=null){
						strFlag = "true";
					}else{
						strFlag = "false";
					}				
				}
				catch(Exception e){
					testStepError = "Unable to create geo-fence";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			//Created by Sagar to update user password to admin@123	
			case "UPDATE_PASSWORD":
				try{
					//URL of Oracle database server
					for (int ivar=1; ivar<TestSuitRunner.arrTestCaseBuild.length;ivar++)
					{
						if (TestSuitRunner.arrTestCaseBuild[ivar][0].equals("DB_CONNECTION_STRING")){
							DB_CONNECTION_STRING = TestSuitRunner.arrTestCaseBuild[ivar][1];
						}
						if (TestSuitRunner.arrTestCaseBuild[ivar][0].equals("DB_USER")){
							DB_USER = TestSuitRunner.arrTestCaseBuild[ivar][1];
						}
						if (TestSuitRunner.arrTestCaseBuild[ivar][0].equals("DB_PASSWORD")){
							DB_PASSWORD = TestSuitRunner.arrTestCaseBuild[ivar][1];
						}
					}
					
					//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
					String url = "jdbc:oracle:thin:@"+DB_CONNECTION_STRING;
			        Properties props = new Properties();
			        props.setProperty("user",DB_USER);
			        props.setProperty("password",DB_PASSWORD);
			      
			        //creating connection to Oracle database using JDBC
			        Connection conn = DriverManager.getConnection(url,props);
			
			        String sql1 ="UPDATE APP_USER SET PASSWORD = 'ba7daac61b65386d2e0a47de73a97e211a0af2c04152ec56781dab4c80d493fc', UNIQ_ID = '[B@94fe42', FIRST_LOGIN = 'F', PASSWORD_UPDATED_DATE= sysdate WHERE USERNAME = \'"+testParameter+"\'";
			
			        //creating PreparedStatement object to execute query
			        PreparedStatement preStatement = conn.prepareStatement(sql1);
					ResultSet fence = preStatement.executeQuery();
					System.out.println("Executed Sql ==============================================");
					if (fence!=null){
						strFlag = "true";
					}else{
						strFlag = "false";
					}
				}
				catch(Exception e){
					testStepError = "Unable to update password for resource "+testParameter;
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}	
				break;
				
			case "UPDATE_RESOURCE":
				try{
					//URL of Oracle database server
					for (int ivar=1; ivar<TestSuitRunner.arrTestCaseBuild.length;ivar++)
					{
						if (TestSuitRunner.arrTestCaseBuild[ivar][0].equals("DB_CONNECTION_STRING")){
							DB_CONNECTION_STRING = TestSuitRunner.arrTestCaseBuild[ivar][1];
						}
						if (TestSuitRunner.arrTestCaseBuild[ivar][0].equals("DB_USER")){
							DB_USER = TestSuitRunner.arrTestCaseBuild[ivar][1];
						}
						if (TestSuitRunner.arrTestCaseBuild[ivar][0].equals("DB_PASSWORD")){
							DB_PASSWORD = TestSuitRunner.arrTestCaseBuild[ivar][1];
						}
					}
					
			        
					String url = "jdbc:oracle:thin:@"+DB_CONNECTION_STRING;
			        Properties props = new Properties();
			        props.setProperty("user",DB_USER);
			        props.setProperty("password",DB_PASSWORD);
			      
			        //creating connection to Oracle database using JDBC
			        Connection conn = DriverManager.getConnection(url,props);
			
			        String sql1 ="UPDATE RESOURCES SET STATUS = 'T', CONSENT_STATUS = 'ACCEPTED', CHECKIN_CHECKOUT_STATUS = 'CHECKED_IN', RESOURCE_STATE = 'SUCCESS_LOCATION_RETRIVABLE' WHERE IDENTIFICATION_NAME = \'"+testParameter+"\'";
			        String sql2 ="update resource_tracking set track_status='T' where resource_id in (Select ID from resources where IDENTIFICATION_NAME = \'"+testParameter+"\')";
			
			        //creating PreparedStatement object to execute query
			        PreparedStatement preStatement = conn.prepareStatement(sql1);
					ResultSet fence = preStatement.executeQuery();
					PreparedStatement preStatement1 = conn.prepareStatement(sql2);
					ResultSet fence1 = preStatement1.executeQuery();
					
					if (fence!=null && fence1!=null){
						strFlag = "true";
					}else{
						strFlag = "false";
					}
				}
				catch(Exception e){
					testStepError = "Unable to update resource";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
			
			case "CLEAR_PASSWORD_HISTORY":
				try{
					//URL of Oracle database server
					String [] mytxt = testParameter.split("@");
					//String myvariable = "//*[contains(text(),'"+mytxt[0]+"')]";
					//String myvariable1 = "//*[contains(text(),'"+mytxt[1]+"')]";
					
					for (int ivar=1; ivar<TestSuitRunner.arrTestCaseBuild.length;ivar++)
					{
						if (TestSuitRunner.arrTestCaseBuild[ivar][0].equals("DB_CONNECTION_STRING")){
							DB_CONNECTION_STRING = TestSuitRunner.arrTestCaseBuild[ivar][1];
						}
						if (TestSuitRunner.arrTestCaseBuild[ivar][0].equals("DB_USER")){
							DB_USER = TestSuitRunner.arrTestCaseBuild[ivar][1];
						}
						if (TestSuitRunner.arrTestCaseBuild[ivar][0].equals("DB_PASSWORD")){
							DB_PASSWORD = TestSuitRunner.arrTestCaseBuild[ivar][1];
						}
					}
					
			        
					String url = "jdbc:oracle:thin:@"+DB_CONNECTION_STRING;
			        Properties props = new Properties();
			        props.setProperty("user",DB_USER);
			        props.setProperty("password",DB_PASSWORD);
			      
			        //creating connection to Oracle database using JDBC
			        Connection conn = DriverManager.getConnection(url,props);
			        
			
			        String sql ="DELETE FROM password_history where USER_ID=(SELECT ID FROM APP_USER where username = \'"+mytxt[0]+"\' and enterprise_id=(select id from enterprise where ENTERPRISE_CODE=\'"+mytxt[1]+"\'))";
			        
			        
			
			        //creating PreparedStatement object to execute query
			        PreparedStatement preStatement = conn.prepareStatement(sql);
					ResultSet delete = preStatement.executeQuery();
					
					if (delete!=null){
						strFlag = "true";
					}else{
						strFlag = "false";
					}
				}
				catch(Exception e){
					testStepError = "Unable to delete password history";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "true";
				}
				break;
				
			case "CUSTOM_SET_ALERT":
				try{
					String [] mytxt = testParameter.split(",");
					String myxpath = "//*[contains(text(),'"+mytxt[0]+"')]";
					String myxpath1 = "//*[contains(text(),'"+mytxt[1]+"')]";
					
					ElementLocator.waitElementExists("xpath", myxpath);
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true && ElementLocator.elementExists("xpath",myxpath+"//..//.."+myxpath1).isDisplayed()==true){
						if (mytxt[2].equals("Entering")){
							WebElement checkbox2 = ElementLocator.elementExists("xpath",myxpath+"//..//..//*[@id='enteringFence']/input");
							if (checkbox2.isSelected()==false){
								checkbox2.click();
							}
						}
						if (mytxt[3].equals("Leaving")){
							WebElement checkbox3 = ElementLocator.elementExists("xpath",myxpath+"//..//..//*[@id='leavingFence']/input");
							if (checkbox3.isSelected()==false){
								checkbox3.click();
							}
						}
						if (Integer.parseInt(mytxt[4]) > 0 && mytxt[4] != "" && mytxt[4] != null && mytxt[4] != " "){
							WebElement myval = ElementLocator.elementExists("xpath",myxpath+"//..//..//*[@id='exceddigLimit']/input");
							myval.clear();
							myval.sendKeys(mytxt[4]);
						}
						if (mytxt[5] != "00:00" && mytxt[5] != "" && mytxt[5] != null && mytxt[5] != " " ){
							WebElement myselectdropdown = ElementLocator.elementExists("xpath",myxpath+"//..//..//*[@id='startTimeField']/input");
							myselectdropdown.click();
							String selectValue = "//*[contains(@id,'x-auto')]/div[text()='"+mytxt[5]+"']";
							WebElement dropDownElement = ElementLocator.elementExists("xpath",selectValue);
							dropDownElement.click();
						}
						if (mytxt[6] != "23:59" && mytxt[6] != "" && mytxt[6] != null && mytxt[6] != " "){
							WebElement myselectdropdown = ElementLocator.elementExists("xpath",myxpath+"//..//..//*[@id='endTimeField']/input");
							myselectdropdown.click();
							String selectValue = "//*[contains(@id,'x-auto')]/div[text()='"+mytxt[6]+"']";
							WebElement dropDownElement = ElementLocator.elementExists("xpath",selectValue);
							dropDownElement.click();
						}
						WebElement myselectdropdown = ElementLocator.elementExists("xpath","//button[text()='Enable Alerts']");
						myselectdropdown.click();
						strFlag = "true";
					} else {
						testStepError = "Geo-Fence "+mytxt[0]+","+mytxt[1]+" is not displayed"; 
						TestSuitRunner.logger.info(testStepError);
						strFlag = "false";
					}
				}
				catch(Exception e){
					testStepError = "Unable to set alert for resource";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			case "CUSTOM_VIEW_INFO":
				try{
					String myxpath = "//*[contains(text(),'"+testParameter+"')]";
					
					ElementLocator.waitElementExists("xpath", myxpath);
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						WebElement deleteTab = ElementLocator.elementExists("xpath",myxpath+"//..//..//*[contains(@id,'View')]");
						deleteTab.click();
						strFlag = "true";
					}else {
						testStepError = testParameter+" is not displayed";
						TestSuitRunner.logger.info(testStepError);
						strFlag = "false";
					}
				}
				catch(Exception e){
					testStepError = "Unable to find resource to view resource information";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;				
			
			case "DELETE_RESOURCE_FROM_DB" :
				try{
					PreparedStatement preStatement;
					ResultSet fence;
					//URL of Oracle database server
					for (int ivar=1; ivar<TestSuitRunner.arrTestCaseBuild.length;ivar++)
					{
						if (TestSuitRunner.arrTestCaseBuild[ivar][0].equals("DB_CONNECTION_STRING")){
							DB_CONNECTION_STRING = TestSuitRunner.arrTestCaseBuild[ivar][1];
						}
						if (TestSuitRunner.arrTestCaseBuild[ivar][0].equals("DB_USER")){
							DB_USER = TestSuitRunner.arrTestCaseBuild[ivar][1];
						}
						if (TestSuitRunner.arrTestCaseBuild[ivar][0].equals("DB_PASSWORD")){
							DB_PASSWORD = TestSuitRunner.arrTestCaseBuild[ivar][1];
						}
					}
					
			        
					String url = "jdbc:oracle:thin:@"+DB_CONNECTION_STRING;
			        Properties props = new Properties();
			        props.setProperty("user",DB_USER);
			        props.setProperty("password",DB_PASSWORD);
			      
			        //creating connection to Oracle database using JDBC
			        Connection conn = DriverManager.getConnection(url,props);
			
			        String sql1 ="delete from billing_tracking where resource_id=(select id from resources_ert where identification_name= \'"+testParameter+"\')";
			        String sql2 ="delete from resource_checkinout_status where resource_id=(select id from resources_ert where identification_name= \'"+testParameter+"\')";
			        String sql3 ="delete from resource_skill where resource_id=(select id from resources_ert where identification_name= \'"+testParameter+"\')";
			        String sql4 ="delete from resource_status where resource_id=(select id from resources_ert where identification_name= \'"+testParameter+"\')";
			        String sql5 ="delete from sms_devices where loginid = \'"+testParameter+"\'";
			        String sql6 ="delete from resources_ert where identification_name= \'"+testParameter+"\'";
			        //creating PreparedStatement object to execute query
			        preStatement = conn.prepareStatement(sql1);
					fence = preStatement.executeQuery();
					preStatement = conn.prepareStatement(sql2);
					fence = preStatement.executeQuery();
					preStatement = conn.prepareStatement(sql3);
					fence = preStatement.executeQuery();
					preStatement = conn.prepareStatement(sql4);
					fence = preStatement.executeQuery();
					preStatement = conn.prepareStatement(sql5);
					fence = preStatement.executeQuery();
					preStatement = conn.prepareStatement(sql6);
					fence = preStatement.executeQuery();
					if (fence!=null){
						strFlag = "true";
					}else{
						strFlag = "false";
					}
				}
				catch(Exception e){
					testStepError = "Unable to delete resource "+testParameter;
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;				
				
			case "CUSTOM_EDIT":
				try{
					String myxpath = "//*[text()='"+testParameter+"']";
					
					ElementLocator.waitElementExists("xpath", myxpath);
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						WebElement editTab = ElementLocator.elementExists("xpath",myxpath+"//..//..//*[contains(@id,'Edit') and contains(@class,'btnEdit')]");
						editTab.click();
						strFlag = "true";
					}else {
						testStepError = testParameter+" is not displayed";
						TestSuitRunner.logger.info(testStepError);
						strFlag = "false";
					}
				}
				catch(Exception e){
					testStepError = "Unable to find element "+testParameter+" to edit";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			case "CUSTOM_DELETE":
				try{
					String myxpath = "//*[text()='"+testParameter+"']";
					
					ElementLocator.waitElementExists("xpath", myxpath);
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						//WebElement deleteTab = ElementLocator.elementExists("xpath",myxpath+"//..//..//*[contains(@id,'Delete')]");
						WebElement deleteTab = ElementLocator.elementExists("xpath",myxpath+"//..//..//*[starts-with(@data-qtip,'Click to delete the role')]" );
						deleteTab.click();
						strFlag = "true";
					}else {
						testStepError = testParameter+" is not displayed";
						TestSuitRunner.logger.info(testStepError);
						strFlag = "false";
					}
				}
				catch(Exception e){
					testStepError = "Unable to find element "+testParameter+" to delete";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
				//*****************Added for Task Dispatcher************//
            case "RECOMMEND_FIELD_STAFF":
				try{
					String myxpath = "//*[contains(text(),'"+testParameter+"')]";
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						WebElement selectTask = ElementLocator.elementExists("xpath",myxpath+"//..//..//*[@class='x-btn gridButton btnRecommend  x-btn-noicon']");
						selectTask.click();
						strFlag = "true";
					}else {
						testStepError = testParameter+" is not displayed";
						TestSuitRunner.logger.info(testStepError);
						strFlag = "false";
					}
				}
				catch(Exception e){
					testStepError = "Unable to find element "+testParameter+" to delete";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
            
            case "ASSIGN_TASK":
				try{
					String myxpath = "//*[contains(text(),'"+testParameter+"')]";
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						WebElement selectFieldStaff = ElementLocator.elementExists("xpath",myxpath+"//..//..//*[@class='x-btn gridButton btnCheck  x-btn-noicon']");
						selectFieldStaff.click();
						strFlag = "true";
					}else {
						testStepError = testParameter+" is not displayed";
						TestSuitRunner.logger.info(testStepError);
						strFlag = "false";
					}
				}
				catch(Exception e){
					testStepError = "Unable to find element "+testParameter+" to delete";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
			  //***************END******************
	
				
			case "CUSTOM_MANAGE_ALERTS":
				try{
					String myxpath = "//*[contains(text(),'"+testParameter+"')]";
					
					ElementLocator.waitElementExists("xpath", myxpath);
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						WebElement deleteTab = ElementLocator.elementExists("xpath","("+myxpath+"//..//..//*[contains(@id,'Edit')])[1]");
						deleteTab.click();
						strFlag = "true";
					}else {
						testStepError = testParameter+" is not displayed"; 
						strFlag = "false";
					}
				}
				catch(Exception e){
					testStepError = "Unable to find element "+testParameter+" to manage alert";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			case "CUSTOM_VERIFY_ELEMENT_PRESENT":
				try{
					String myxpath = "//*[contains(text(),'"+testParameter+"')]";
					System.out.println("xpath is " +myxpath);
					ElementLocator.waitElementExists("xpath", myxpath);
					// Line code is changed to "equalsIgnoreCase" where it was "==" operator was used 
					if(ElementLocator.elementExists("xpath",myxpath).getText().equalsIgnoreCase(testParameter)){
						testStepError = testParameter+" is displayed";
							strFlag = "true";
					}else {
						testStepError = testParameter+" is not displayed"; 
						strFlag = "false";
					}
				}
				catch(Exception e){
					testStepError = "Unable to verify is elemet present";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;

			case "CUSTOM_VERIFY_ELEMENT_NOT_PRESENT":
				try{
					String myxpath = "//*[contains(text(),'"+testParameter+"')]";
					if (ElementLocator.elementExists("xpath",myxpath)==null){
						testStepError = testParameter+" is not displayed";
						strFlag = "true";
					}else {
						
						testStepError = testParameter+" is not displayed";
						strFlag = "true";
					}
				}
				catch(Exception e){
					testStepError = "Unable to verify elemeny not present";
					//TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			case "CUSTOM_DYNAMIC_LOCATION":
				try{
					String myxpath = "//*[contains(text(),'"+testParameter+"')]//..//..//*[starts-with(@id,'dynamicLocButton')]";
					
					ElementLocator.waitElementExists("xpath", myxpath);
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						ElementLocator.elementExists("xpath",myxpath).click();
						testStepError = testObjectName+" is displayed";
						strFlag = "true";
					}else {
						testStepError = testObjectName+" is not displayed";
						strFlag = "false";
					}
				}
				catch(Exception e){
					testStepError = "Unable to find dynamic location tab";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			case "CUSTOM_HISTORICAL_LOCATION":
				try{
					String myxpath = "//*[contains(text(),'"+testParameter+"')]//..//..//*[starts-with(@id,'historicalLocButton')]";
					
					ElementLocator.waitElementExists("xpath", myxpath);
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						ElementLocator.elementExists("xpath",myxpath).click();
						testStepError = testParameter+" is displayed";
						strFlag = "true";
					}else {
						testStepError = testParameter+" is not displayed";
						strFlag = "false";
					}
				}
				catch(Exception e){
					testStepError = "Unable to find historical location tab";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			case "CUSTOM_GENERATE_REPORT":
				try{
					String myxpath = "//*[contains(text(),'"+testParameter+"')]//..//..//*[starts-with(@id,'generateReportButton')]";
					
					ElementLocator.waitElementExists("xpath", myxpath);
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						ElementLocator.elementExists("xpath",myxpath).click();
						testStepError = testParameter+" is displayed";
						strFlag = "true";
					}else {
						testStepError = testParameter+" is not displayed";
						strFlag = "false";
					}
				}
				catch(Exception e){
					testStepError = "Unable to find generate report tab";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
		//Keyword to click on help
			 case "CLICK_HELP":
				try{
					String myxpath = "//div[@id='"+testParameter+"']//button[contains(text(),'Help')]";
					
					ElementLocator.waitElementExists("xpath", myxpath);
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						WebElement HelpBttn = ElementLocator.elementExists("xpath",myxpath);
						HelpBttn.click();
						testStepError = TestSuitRunner.testParameter+"is displayed";
						strFlag = "true";
					}else {
						testStepError = TestSuitRunner.testParameter+" is not displayed"; 
						strFlag = "false";
					}
				}
				catch(Exception e){
					testStepError = "Unable to click on help tab";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			 case "VERIFY_HELP_TEXT":
				try{
					Thread.sleep(5000);
					
					ElementLocator.waitElementExists(elementType, "//div[@class=' help-panel']");
					String text = ElementLocator.elementExists("xpath","//div[@class=' help-panel']").getText();
					if (testParameter.equals(text))
					{
					strFlag = "true";
					testStepError = "Text Validated Successfully";
					}
					else
					{
						testStepError = "Text value does not match. Text displayed is " +text+".";
						strFlag = "false";
					}
				}
				catch(Exception e){
					testStepError = "Unable to find help panel to verify text";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
					
			 case "VERIFY_DROPDOWN_VALUES":
				try{
					String[] expected = {"GRAM", "OUNCE", "POUND", "MILLIMETER", "TSP", "TBSP", "FLUID_OUNCE"};
					WebElement select = ElementLocator.elementExists("xpath","(//input[@type='text'])[3]");
					WebElement text = ElementLocator.elementExists("xpath","(//input[@type='text'])[3]");
					List<WebElement> allOptions = select.findElements(By.tagName("option"));
					// make sure you found the right number of elements
					if (expected.length != allOptions.size()) {
						System.out.println("fail, wrong number of elements found");
					}
					// make sure that the value of every <option> element equals the expected value
					for (int i = 0; i < expected.length; i++) {
						String optionValue = allOptions.get(TestSuitRunner.i).getAttribute("value");
						if (optionValue.equals(expected[i])) {
							System.out.println("passed on: " + optionValue);
						} else {
							System.out.println("failed on: " + optionValue);
						}
					}
					System.out.println(text);
					if (testParameter.equals(text))
					{
					strFlag = "true";
					testStepError = "Text Validated Successfully";
					}
					else
					{
						testStepError = "Text value does not match. Text displayed is " +text+".";
						strFlag = "false";
					}
				}
				catch(Exception e){
					testStepError = "Unable to find dropdown to verify values";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
				
	/*#######################################################################################################################*/
				
			case "COMPARE_INNER_HTML":
				try{
				//String myxpath = "//*[contains(text(),'"+testParameter+"')]";
					String myxpath = "//*[text()='"+testParameter+"']";
				
				ElementLocator.waitElementExists("xpath", myxpath);
				
				WebElement mygraph = ElementLocator.elementExists("xpath",myxpath);
				String innerhtml = (String)((JavascriptExecutor)TestSuitRunner.driver.get(TestSuitRunner.i)).executeScript("return arguments[0].innerHTML;", mygraph); 
				if (innerhtml.contains(testParameter)==true)
					{
					testStepError = testParameter+" is displayed";
						strFlag = "true";
					}
				else
					{
						testStepError = testParameter+" is not displayed";
						strFlag = "false";
					}
				}
				catch(NoSuchElementException e) {
				 testStepError = "Unable to find element to verify inner html";
				 TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
				 strFlag = "false";
				}
				catch(Exception e) {
					 testStepError = "Unable to find element to verify inner html";
					 TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					 strFlag = "false";
				}
				break;
				
			case "COMPARE_INNER_HTML_CONTAINS":
				try{
				String myxpath = "//*[contains(text(),'"+testParameter+"')]";
					//String myxpath = "//*[text()='"+testParameter+"']";
				
				ElementLocator.waitElementExists("xpath", myxpath);
				
				WebElement mygraph = ElementLocator.elementExists(elementType,testObject);
				String innerhtml = (String)((JavascriptExecutor)TestSuitRunner.driver.get(TestSuitRunner.i)).executeScript("return arguments[0].innerHTML;", mygraph); 
				if (innerhtml.contains(testParameter)==true)
					{
					testStepError = testParameter+" is displayed";
						strFlag = "true";
					}
				else
					{
						testStepError = testParameter+" is not displayed";
						strFlag = "false";
					}
				}
				catch(NoSuchElementException e) {
				 testStepError = "Unable to find element to verify inner html";
				 TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
				 strFlag = "false";
				}
				catch(Exception e) {
					 testStepError = "Unable to find element to verify inner html";
					 TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					 strFlag = "false";
				}
				break;
			
			case "COMPARE_INNER_HTML_DOES_NOT_CONTAINS":
				try{
					WebElement mygraph = ElementLocator.elementExists(elementType,testObject);
					String innerhtml = (String)((JavascriptExecutor)TestSuitRunner.driver.get(TestSuitRunner.i)).executeScript("return arguments[0].innerHTML;", mygraph); 
					if (innerhtml.contains(testParameter)==true)
						{
						testStepError = testParameter+" is displayed";
						strFlag = "false";
					}
					else
					{
						testStepError = testParameter+" is not displayed";
						strFlag = "true";
					}
				}
				catch(NoSuchElementException e) {
					testStepError = "Unable to find element to verify inner html";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				catch(Exception e) {
					testStepError = "Unable to find element to verify inner html";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			case "GET_TEXT_COUNT":
				try{
					WebElement body = TestSuitRunner.driver.get(TestSuitRunner.i).findElement(By.tagName("body"));
					String bodyText = body.getText();
					int count = 0;
					
					// search for the String within the text
					while (bodyText.contains(testParameter)){

					    // when match is found, increment the count
					    count++;

					    // continue searching from where you left off
					    bodyText = bodyText.substring(bodyText.indexOf("VIM LIQUID MARATHI") + "VIM LIQUID MARATHI".length());
					}
					
					System.out.println("Number of data displayed : "+count );
				}
				catch(NoSuchElementException e) {
					 testStepError = "Unable to find text to get count";
					 TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					 strFlag = "false";
				}
				catch(Exception e) {
					 testStepError = "Unable to find text to get count";
					 TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					 strFlag = "false";
				}
				break;
				
			case "CUSTOM_SEARCH" :
				try{
	
					WebElement textbox = ElementLocator.elementExists(elementType,testObject);
					textbox.clear();
					textbox.sendKeys(testParameter);

					String myxpath = "//*[@class='ellipse-text' and @data-qtip='"+testParameter+"']";
				WebElement dropdown = ElementLocator.elementExists(elementType,myxpath);
				dropdown.click();	
				strFlag = "true";
				}
				catch (NoSuchElementException e) {
					testStepError = "Unable to find element to input text";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				catch (Exception e) {
					testStepError = "Unable to find element to input text";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				
			case "INPUT_TEXT" :
				try{
					//WebElement textbox = TestSuitRunner.driver.get(TestSuitRunner.i).findElement(By.xpath(testObject));
					WebElement textbox = ElementLocator.elementExists(elementType,testObject);
					textbox.clear();
					textbox.sendKeys(testParameter);
					strFlag = "true";
				}
				catch (NoSuchElementException e) {
					testStepError = "Unable to find element to input text";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				catch (Exception e) {
					testStepError = "Unable to find element to input text";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}	
				break;
				
			case "MOUSE_HOVER" :
				try{
				    WebElement theTestObject = ElementLocator.elementExists(elementType,testObject);
				    Action hover = new Actions(TestSuitRunner.driver.get(TestSuitRunner.i)).moveToElement(theTestObject).build();
				    hover.perform();
				    strFlag = "true";
				}
				catch (Exception e) {
				    testStepError = "Unable to find elemetnt to perform mouse hover";
				    TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
				    strFlag = "false";
				}
				break;
				
			case "OK_POPUP" :
				try {
					wait = new WebDriverWait(TestSuitRunner.driver.get(TestSuitRunner.i),20);
					ElementLocator.waitElementExists(elementType, "//button[text()='ok' or text()='Ok' or text()='OK' or text()='oK']");
					int xpathCount = TestSuitRunner.driver.get(TestSuitRunner.i).findElements(By.xpath("//button[text()='ok' or text()='Ok' or text()='OK' or text()='oK']")).size();
					
					for (i=xpathCount; i>0; i--){
						ElementLocator.elementExists("xpath","(//button[text()='ok' or text()='Ok' or text()='OK' or text()='oK'])["+i+"]").click();
					}
					strFlag = "true";
				}catch(Exception e){
					testStepError = "No popup displayed";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			case "CLICK" :
				try{
					//WebElement link = TestSuitRunner.driver.get(TestSuitRunner.i).findElement(By.xpath(testObject));
					WebElement link =  ElementLocator.elementExists(elementType,testObject);
					link.click();
					strFlag = "true";
				}
				catch (NoSuchElementException e) {
					testStepError = "Unable to find element to click on";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				catch (Exception e){
					testStepError = "Unable to find element to click on";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			case "CLICK_WAIT" :
				try{
					//WebElement link = TestSuitRunner.driver.get(TestSuitRunner.i).findElement(By.xpath(testObject));
					WebElement link =  ElementLocator.elementExists(elementType,testObject);
					link.click();
					int time=7;
					/*if(testParameter!=null)
					time=Integer.parseInt(testParameter);
           		 //TimeUnit.SECONDS.sleep(time);*/
           		System.out.println("wait for "+time+" seconds started");
           		TimeUnit.SECONDS.sleep(time);
           		System.out.println("wait for "+time+" seconds ended");
					strFlag = "true";
				}
				catch (NoSuchElementException e) {
					testStepError = "Unable to find element to click on";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				catch (Exception e){
					testStepError = "Unable to find element to click on";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
			
			case "DOUBLE_CLICK" :
			    try{
					WebElement object = ElementLocator.elementExists(elementType,testObject);
					Actions doubleclk = new Actions(TestSuitRunner.driver.get(TestSuitRunner.i));
					doubleclk.doubleClick(object);
					doubleclk.perform();
					strFlag = "true";
				}
				catch (NoSuchElementException e) {
					testStepError = e.getMessage();
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
			    catch (Exception e) {
					testStepError = e.getMessage();
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				} 
				break;
			   
	             //Added for toast messages
            case "VERIFY_TOAST":
				try{
					ElementLocator.waitElementExists("xpath","//*[contains(text(),'"+testParameter+"')]");
					WebElement myobj = ElementLocator.elementExists("xpath","//*[contains(text(),'"+testParameter+"')]");
					if (myobj.isDisplayed()==true)
					{
						testStepError = "Toast "+testParameter+" is displayed";
						strFlag = "true";
					}
					else
					{
						testStepError = "Toast "+testParameter+" is not displayed";
						strFlag = "false";
					}
				}
				catch (NoSuchElementException e) {
					testStepError = "Unable to verify Toast message";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				catch (Exception e) {
					testStepError = "Unable to verify Toast message";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;

			case "LAUNCH":
				try{
				if (testObject!=null)
 				{
					testObject = testObject.toUpperCase();
					if (testObject.equals("IEXPLORE") || testObject.equals("IE") || testObject.equals("INTERNET EXPLORER") || testObject.equals("IEXPLORER")){
                        testObject="IE";
					}
				}
				
				switch (testObject){
				
		            case "FIREFOX" :
		            	/*File pathToBinary = new File("C://Program Files//Mozilla Firefox15//Firefox.exe");
		            	FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);*/
		            	//FirefoxBinary binary = new FirefoxBinary(new File("D://Firefox//Firefox44//firefox.exe"));
		            	FirefoxProfile profile = new FirefoxProfile();
		            	//FirefoxProfile profile = new FirefoxProfile();
		            	profile.setEnableNativeEvents(true);
		            	profile.setPreference("security.mixed_content.block_active_content",false);
		            	profile.setPreference("security.mixed_content.block_display_content",false);
		            	if(useFFBinary.equals("TRUE"))
		            	{
		            		ffBinaryPath = TestSuitRunner.arrTestCaseBuild[35][1];
		            		FirefoxBinary binary = new FirefoxBinary(new File(ffBinaryPath));
		            		TestSuitRunner.ff_driver=new FirefoxDriver(binary,profile);
		            	}
		            	else
		            		TestSuitRunner.ff_driver=new FirefoxDriver(profile);
		            	//TestSuitRunner.ff_driver=new FirefoxDriver(ffBinary, profile);
		            	TestSuitRunner.driver.add(0,TestSuitRunner.ff_driver);
		            	TestSuitRunner.i=0;
		            	TestSuitRunner.driver.get(0).manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		            	TestSuitRunner.driver.get(0).manage().window().maximize();
		            	TestSuitRunner.driver.get(0).get(testParameter);
		            	TestSuitRunner.driverFlag=true;
		                break;
		                
		            case "SELENDROID" :
		            	SelendroidConfiguration config = new SelendroidConfiguration();
		            	config.addSupportedApp("D://MWM_Automation_Latest//Resources//mwmapp-release_62.apk");
		            	SelendroidLauncher selendroidServer = new SelendroidLauncher(config);
		                selendroidServer.launchSelendroid();

		                SelendroidCapabilities caps1 = new SelendroidCapabilities("com.psl.location.cdpapp:2.2");
		                //caps1.setEmulator(false);
		                //TestSuitRunner.driver.get(TestSuitRunner.i).get(2) = new SelendroidDriver(caps1);
		                TestSuitRunner.android_driver = new SelendroidDriver(caps1);
		                TestSuitRunner.driver.add(1,TestSuitRunner.android_driver );
		                TestSuitRunner.i=1;
		                TestSuitRunner.driver.get(1).switchTo().window("WEBVIEW");
		                //driver.add(1,TestSuitRunner.android_driver );
		                TestSuitRunner.driver.get(1).manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		                //TestSuitRunner.driver.get(TestSuitRunner.i).get(1).switchTo().window("WEBVIEW");
		                TestSuitRunner.driverFlag=true;
		                break;
		
		            case "IE" :
		
		            	String ieBinary = "Resources/IEDriverServer.exe";
		            	System.setProperty("webdriver.get(TestSuitRunner.i).ie.driver.get(TestSuitRunner.i)", ieBinary);
		                try {
		                DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
		                caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);  
		                caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);  
		                caps.setCapability("ignoreZoomSetting", true);
		                //TestSuitRunner.driver.get(TestSuitRunner.i).get(3) = new InternetExplorerDriver(caps);
		                TestSuitRunner.driver.add(2, new InternetExplorerDriver(caps));
		                TestSuitRunner.i=2;
		                }
		                catch(Exception e){
		                		e.printStackTrace();
		                }
		                TestSuitRunner.driver.get(2).manage().window().maximize();
		                TestSuitRunner.driver.get(2).manage().deleteAllCookies();
		                TestSuitRunner.driver.get(2).get(testParameter);
		                wait = new WebDriverWait(TestSuitRunner.driver.get(2), 60);
		                TestSuitRunner.driverFlag=true;
		                break;
		                
		            case "APPIUM" :
		            	
		            	  File app = new File("C:\\Users\\ankush_paunikar\\Desktop\\ContactManager.apk");
		                  DesiredCapabilities capabilities = new DesiredCapabilities();
		                  capabilities.setCapability("deviceName","Android Emulator");
		                  capabilities.setCapability("platformName", "Android");
		                  capabilities.setCapability("platformVersion", "4.4");
		                  capabilities.setCapability("app", app);
		                  capabilities.setCapability("appPackage", "com.example.android.contactmanager");
		                  capabilities.setCapability("appActivity", ".ContactManager");
		                  android_driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		                TestSuitRunner.driverFlag=true;
		                break;
		                
		            case "CHROME" :
		
		                DesiredCapabilities chromeCapabilities = DesiredCapabilities.chrome();
		                String chromeBinary = System.getProperty(" ");
		                if (chromeBinary == null || chromeBinary.equals("")) {
		
		                String os = System.getProperty("os.name").toLowerCase().substring(0, 3);
		
		                chromeBinary = "Resources/chromedriver-" + os + (os.equals("win") ? ".exe" : "");
		
		                System.setProperty("webdriver.chrome.driver", chromeBinary);
		
		             }
		
		                TestSuitRunner.driver.add(3,new ChromeDriver(chromeCapabilities));
		                TestSuitRunner.i=3;
		                wait = new WebDriverWait(TestSuitRunner.driver.get(3), 5);
		                TestSuitRunner.driver.get(3).manage().window().maximize();
		                TestSuitRunner.driver.get(3).get(testParameter);
		                TestSuitRunner.driverFlag=true;
		                break;
		                
		            default :
		            	System.out.println("ERROR : Please Enter Proper platform Name vaild names are firefox, selendroid, IE, Chrome");
		            	TestSuitRunner.logger.fatal("Unable to launch the browser");
		            	System.exit(0);
		            	break;
				}
				
				strFlag = "true";
				}
				catch (Exception e) {
					testStepError = e.getMessage(); 
					// e.printStackTrace();
					 TestSuitRunner.logger.fatal("Unable to launch the browser",e);
					 strFlag = "false";
				}
				break;
				
			case "SWITCH_ANDRID":
				try{
					//TestSuitRunner.driver.get(TestSuitRunner.i).get(TestSuitRunner.i)=TestSuitRunner.android_driver;
					TestSuitRunner.i=1;
					TestSuitRunner.driver.get(1).switchTo().window("WEBVIEW");
					strFlag = "true";
					//TestSuitRunner.driver.get(TestSuitRunner.i).quit();
				}
				catch (Exception e) {
					testStepError = "Unable to switch to android driver";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
					// TestSuitRunner.driver.get(TestSuitRunner.i).quit();
				}
				break;
				
			case "SWITCH_FIREFOX":
				try{
					TestSuitRunner.i=0;
					 strFlag = "true";
				}
				catch (Exception e) {
					testStepError = "Unable to switch to firefix driver";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}	
				break;
				
			case "SWITCH_IE":
				try{
					TestSuitRunner.i=2;
					strFlag = "true";
				}
				 catch (Exception e) {
					testStepError = "Unable to switch to chrome driver";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			case "SWITCH_CHROME":
				try{
					TestSuitRunner.i=3;
					strFlag = "true";
				}
				 catch (Exception e) {
					testStepError = "Unable to switch to chrome driver";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}	
				break;
				
			case "OPEN_URL":
				try{
					TestSuitRunner.driver.get(TestSuitRunner.i).get(testParameter);
					strFlag = "true";
				}
				catch (Exception e) {
					testStepError = "Unable to open the URL";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			case "ASSERT_TITLE":
				try{
					String strCurrentPageTitle= TestSuitRunner.driver.get(TestSuitRunner.i).getTitle();
					if (testParameter.equals(strCurrentPageTitle)) {
						testStepError = "Page title validated successfully";
					strFlag = "true";
					}
					else {
						testStepError = "Page Title does not match. Title displayed is " +strCurrentPageTitle+".";
						strFlag = "false";
					}
				}
				catch (NoSuchElementException e) {
					testStepError = "Unable to verify title";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				catch (Exception e) {
					testStepError = "Unable to verify title";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			case "CUSTOM_SELECT_DROPDOWN" :
				try {
					WebElement link = ElementLocator.elementExists(elementType,testObject);
					link.click();
					// String selectValue = "//*[contains(@id,'x-auto') and contains(@class,'x-combo-list-inner')]/div[text()='"+testParameter+"']";  is changed so
					// String SelectValue="//*[text()='role_1'] "; is replaced.
					String selectValue = "//*[text()='role_1'] ";
					WebElement dropDownElement = ElementLocator.waitElementExists("xpath", selectValue);
					dropDownElement.click();
					testStepError = "Dropdown value "+testParameter+" selected successfully";
					strFlag = "true";
				}
				catch (NoSuchElementException e) {
					 testStepError = "Unable to select dropdown value";
					 TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					 strFlag = "false";
				}
				catch (Exception e) {
					 testStepError = "Unable to select dropdown value";
					 TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					 strFlag = "false";
				}
				break;
				
			case "SELECT_DROPDOWN_CHECKBOX" :
				try {
					WebElement link = ElementLocator.elementExists(elementType,testObject);
					link.click();
					//String selectValue = "//*[contains(@id,'x-auto')]/div[text()='"+testParameter+"']";
					String[] val = testParameter.split(",");
					for (int i=0;i<val.length;i++){
						String selectValue = "//*[@class='x-window-bwrap']//*[text()='"+val[i].toUpperCase()+"']//..//td//input";
						
						WebElement dropDownElement = ElementLocator.waitElementExists("xpath", selectValue);
						dropDownElement.click();
					}
					testStepError = "Dropdown value "+testParameter+" selected successfully";
					strFlag = "true";
				}
				catch (NoSuchElementException e) {
					testStepError = "Unable to select dropdown value";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				catch (Exception e) {
					testStepError = "Unable to select dropdown value";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			case "CUSTOM_VERIFY_DROPDOWN" :
				try {
					WebElement link = ElementLocator.elementExists(elementType,testObject);
					link.click();
					String selectValue = "//*[contains(@id,'x-auto')]/div[text()='"+testParameter+"']";
					WebElement dropDownElement = ElementLocator.elementExists("xpath",selectValue);
					if(dropDownElement.isDisplayed())
					{		
						testStepError = "Dropdown value "+testParameter+" verifeded successfully";
						strFlag = "true";
						link.click();
					}
					else
					{
						testStepError = "Dropdown value "+testParameter+" not verifeded successfully";
						strFlag = "false";
						link.click();
					}
				}
				catch (NoSuchElementException e) {
					 testStepError = "Unable to verify dropdown";
					 TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					 strFlag = "false";
				}
				catch (Exception e) {
					 testStepError = "Unable to verify dropdown";
					 TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					 strFlag = "false";
				}
				break;
				
			case "CUSTOM_VERIFY_NOT_DROPDOWN" :
				try {
					WebElement link = ElementLocator.elementExists(elementType,testObject);
					link.click();
					String selectValue = "//*[contains(@id,'x-auto')]/div[text()='"+testParameter+"']";
					WebElement dropDownElement = ElementLocator.elementExists("xpath",selectValue);
					if(dropDownElement.isDisplayed()==false)
					{		
						testStepError = "Dropdown value "+testParameter+" not present";
						strFlag = "true";
						link.click();
					}
					else
					{
						testStepError = "Dropdown value "+testParameter+" is present";
						strFlag = "false";
						link.click();
					}
				}
				catch (NoSuchElementException e) {
					 testStepError = "Unable to verify not a dropdown";
					 TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					 strFlag = "false";
				}
				catch (Exception e) {
					 testStepError = "Unable to verify not a dropdown";
					 TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					 strFlag = "false";
				}
				break;
				
				
			case "VERIFY_TEXT" :
				try{
				    WebElement text = ElementLocator.elementExists(elementType,testObject);
				    if (testParameter.equals(text.getText()))
				    {
				    strFlag = "true";
				    testStepError = "Text Validated Successfully";
				    }
				    else
				    {
				    	testStepError = "Text value does not match. Text displayed is " +text.getText() +".";
				    	strFlag = "false";
				    }
				}
				catch (NoSuchElementException e) {
					testStepError = "Unable to verify text";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				catch (Exception e) {
					testStepError = "Unable to verify text";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			case "VERIFY_TEXT_CONTAINS" :
				try{
				    WebElement text = ElementLocator.elementExists(elementType,testObject);
				    if ((text.getText().contains(testParameter)))
				    {
						strFlag = "true";
						testStepError = "Text Validated Successfully";
				    }
				    else
				    {
				    	testStepError = "Text value does not contain expected string. Text displayed is " +text.getText() +".";
				    	strFlag = "false";
				    }
				}
				catch (NoSuchElementException e) {
					testStepError = "Unable to verify contains text";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				catch (Exception e) {
					testStepError = "Unable to verify contains text";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				    
			case "VERIFY_OBJECT":
				try{
					WebElement myobj = ElementLocator.elementExists(elementType,testObject);
					if (myobj.isDisplayed()==true)
					{
						testStepError = "Object with property "+testObjectName+" is displayed";
						strFlag = "true";
					}
					else
					{
						testStepError = "Object with property "+testObjectName+" is not displayed";
						strFlag = "false";
					}
				}
				catch (NoSuchElementException e) {
					testStepError = "Unable to verify object";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				catch (Exception e) {
					testStepError = "Unable to verify object";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
				//Created by Sagar to verify object not present
			case "VERIFY_OBJECT_NOT":
				try{
					if (ElementLocator.elementExists(elementType,testObject)==null)
					{
						testStepError = "Object with property "+testObjectName+" is not displayed";
						strFlag = "true";
					}
					else{
						testStepError = "Object with property "+testObjectName+" is displayed";
						strFlag = "false";
					}
				}
				catch (NoSuchElementException e ) {
					testStepError = "Object with property "+testObjectName+" is not displayed";
					strFlag = "true";
				}
				catch (Exception e ) {
					testStepError = "Object with property "+testObjectName+" is not displayed";
					strFlag = "true";
				}
				break;
				
		    case "CLOSE_BROWSER" :
		    	try {
					TestSuitRunner.driver.get(TestSuitRunner.i).close();
					strFlag = "true";
		    	}
		    	catch (Exception e){
		    		testStepError = "Unable to close browser";
		    		TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
		    	}
                break;
                   
            case "BROWSER_BACK" :
            	try {
					TestSuitRunner.driver.get(TestSuitRunner.i).navigate().back();
					strFlag = "true";
            	}
            	catch (Exception e) {
            		testStepError = "Unable to navigate back in browser";
            		TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
            	}
                break;
                   
            case "BROWSER_FORWARD" :
            	try {
                   TestSuitRunner.driver.get(TestSuitRunner.i).navigate().forward();
                   strFlag = "true";
            	}
            	catch (Exception e){
            		testStepError = "Unable to navigate forward in browser";
            		TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
            	}
				break;
                   
            case "BROWSER_REFRESH" :
            	try {
                   TestSuitRunner.driver.get(TestSuitRunner.i).navigate().refresh();
                   strFlag = "true";
            	}
            	catch (Exception e){
            		testStepError = "Unable to refresh browser";
            		TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
            	}
				break;
                   
            case "SELECT_CHILD_WINDOW" :
            	try{
					for(String winHandle : TestSuitRunner.driver.get(TestSuitRunner.i).getWindowHandles()){
						TestSuitRunner.driver.get(TestSuitRunner.i).switchTo().window(winHandle);
					}                   
					strFlag = "true";
            	}
            	catch (NoSuchElementException e) {
            		testStepError = "Unable to navigate to child window";
            		TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
            	catch (Exception e) {
            		testStepError = "Unable to navigate to child window";
            		TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
            	break;
				
            case "SELECT_DROPDOWN" :
            	try{
					strFlag = "true";
					WebElement dropDownListBox = ElementLocator.elementExists(elementType,testObject);
					Select clickThis = new Select(dropDownListBox);
					clickThis.selectByVisibleText(testParameter);
					testStepError = "Dropdown value "+testParameter+" select successfully";
            	}
            	catch (Exception e) {
					testStepError = "Unable to select dropdown";
            		TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
		
			case "CHECK_CHECKBOX_RADIO_BTN":
                try{
                    WebElement chkbox = ElementLocator.elementExists(elementType,testObject);
                    if (chkbox.isSelected()== true)
                    {
                       testStepError = "Checkbox is already selected" +testObjectName;
                    }
                    else
                    {
                        chkbox.click();
                        if (chkbox.isSelected()== true)
                        {
							testStepError = "Check box is selected"+testObjectName+" successfully";
                        }
                    }
                    strFlag = "true";
                }
                catch (NoSuchElementException e) {
                    testStepError = "unable to check checkbox/radio button";
                    TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
                    strFlag = "false";
                }     
                catch (Exception e) {
                    testStepError = "unable to check checkbox/radio button";
                    TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
                    strFlag = "false";
                } 
                break;
                       
			case "UNCHECK_CHECKBOX":
                try{
                    WebElement chkbox = ElementLocator.elementExists(elementType,testObject);
                       
                    if (chkbox.isSelected()!= true)
                    {
						testStepError = "Checkbox is already Unchecked" +testObjectName;
                    }
                    else
                    {
                        chkbox.click();
                        if (chkbox.isSelected()!= true)
						{
							testStepError = "Check box is Unchecked"+testObjectName+" successfully";
                        }
                    }
                    strFlag = "true";
                }
                catch (NoSuchElementException e) {
                    testStepError = "Unable to uncheck checkbox";
                    TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
                    strFlag = "false";
                }
                catch (Exception e) {
                    testStepError = "Unable to uncheck checkbox";
                    TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
                    strFlag = "false";
                }
                break;
                       
			case "VERIFY_IS_SELECTED_CHECKBOX_RADIO_BTN":
                try{
				    WebElement chkbox = ElementLocator.elementExists(elementType,testObject);
					if (chkbox.isSelected()== true)
					{
						testStepError = "Checkbox is selected" +testObjectName;
						strFlag = "true";
					}
					else
					{
						testStepError = "Check box is Not selected"+testObjectName;
						strFlag = "false";
					}
				}
				catch (NoSuchElementException e) {
					testStepError = "Unable to verify is checkbox/radio button selected";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
                catch (Exception e) {
					testStepError = "Unable to verify is checkbox/radio button selected";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
                       
			case "VERIFY_IS_NOT_SELECTED_CHECKBOX_RADIO_BTN":
                try{
					WebElement chkbox = ElementLocator.elementExists(elementType,testObject);
                       
					if (chkbox.isSelected()== false)
					{
						testStepError = "Checkbox is not  selected" +testObjectName;
						strFlag = "true";
					}
					else if (chkbox.isSelected()== true)
					{
						testStepError = "Check box is selected"+testObjectName;
						strFlag = "false";
					}
				}
				catch (NoSuchElementException e) {
					testStepError = "unable to verify checkbox/radio button is not selected";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
                }
                catch (Exception e) {
					testStepError = "unable to verify checkbox/radio button is not selected";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
                }
				break;
                
          	case "COMPARE_IMAGE" : 
				try{ 
					WebElement image = ElementLocator.elementExists(elementType,testObject); 
					String src = ((JavascriptExecutor)TestSuitRunner.driver.get(TestSuitRunner.i)).executeScript("return arguments[0].attributes['src'].value;", image).toString(); 
				
					if (src.contains("http"))
					{
						boolean temp = ImageComaprision.processImg(src,testParameter);
						if (temp==true) 
						{ 
							strFlag = "true"; 
							testStepError = "Image matches successfully";
						} 
						else 
						{ 
							strFlag = "false"; 
							testStepError = "Images does not match";
						} 
					}
					else {
						String[] var = TestSuitRunner.driver.get(TestSuitRunner.i).getCurrentUrl().split("/");
						String src1= var[0]+"//"+var[2]+src;
						boolean temp1 = ImageComaprision.processImg(src1,testParameter);
						if (temp1==true) 
						{ 
							strFlag = "true"; 
							testStepError = "Image matches successfully";
						} 
						else 
						{ 
							strFlag = "false"; 
							testStepError = "Images does not match";
						}
					}
				}
				catch (Exception e) { 
					testStepError = "Unable to compare the image";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false"; 
				}
				break;
				
			case "EXECUTE_SQL_QUERY" : 
				try{ 
					
					boolean temp = mySQLConnector.connector(testParameter); 
					if (temp==true) 
					{ 
						strFlag = "true"; 
						testStepError = "Sql query executed successfully";
					} 
					else 
					{ 
						strFlag = "false"; 
						testStepError = "Sql query was unsuccessful";
					} 
				} 
				catch (Exception e) { 
					testStepError = "Unable to execute sql query";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			case "SLEEPTIME_IN_SECONDS" :
            	try {
					TimeUnit.SECONDS.sleep(Integer.parseInt(testParameter));
					strFlag = "true";	
            	}
            	catch (Exception e){
            		testStepError = "Unable to set sleeptime";
            		TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
            	}
				break;

			case "GET_TEXT" :
				try{
					WebElement get_text = ElementLocator.elementExists(elementType,testObject);
					getData1 = get_text.getText();
					WriteExcel.writeDataToExcelFile(TestSuitRunner.keyWordXls,TestSuitRunner.currTestCase,TestSuitRunner.tcRow,TestSuitRunner.iColObjectValue,getData1);
					strFlag = "true";
				}
				catch (NoSuchElementException e){
					testStepError = "Unable to get text";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				catch (Exception e){
					testStepError = "Unable to get text";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
                
			case "GET_SPLITTED_TEXT" :
				try{
					WebElement get_text = ElementLocator.elementExists(elementType,testObject);
					String [] mytxt = testParameter.split(",");
					getData = get_text.getText().split(mytxt[0]);
					int myVal = Integer.parseInt(mytxt[1]);
					getData1 = getData[myVal].trim();
					WriteExcel.writeDataToExcelFile(TestSuitRunner.keyWordXls,TestSuitRunner.currTestCase,TestSuitRunner.tcRow,TestSuitRunner.iColObjectValue,getData1);
					strFlag = "true";
				}
				catch (NoSuchElementException e){
					testStepError = "Unable to get splitted text";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				catch (Exception e){
					testStepError = "Unable to get splitted text";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			case "USE_TEXT" :
				try{
					WebElement get_text = ElementLocator.elementExists(elementType,testObject);
					get_text.clear();
					get_text.sendKeys(getData[1].trim());
					strFlag = "true";
				}
				catch (Exception e){
					testStepError = "Unable to use text";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
				}
				break;
				
			case "SELECT_FRAME" :
                try{
                       TestSuitRunner.driver.get(TestSuitRunner.i).switchTo().defaultContent();
                       TestSuitRunner.driver.get(TestSuitRunner.i).switchTo().frame(TestSuitRunner.driver.get(TestSuitRunner.i).findElements(By.tagName("iframe")).get(0));
                       testStepError = "Frame"+testObject+" selected successfully";
                       strFlag = "true";
                }
                catch (NoSuchElementException e) {
                       testStepError = "Unable to select frame";
                       TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
                       strFlag = "false";
                }
                catch (Exception e) {
                    testStepError = "Unable to select frame";
                    TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
                    strFlag = "false";
                }
				break;
				
			case "SELECT_FRAME_XPATH" :
                try{
                       TestSuitRunner.driver.get(TestSuitRunner.i).switchTo().defaultContent();
                       TestSuitRunner.driver.get(TestSuitRunner.i).switchTo().frame(TestSuitRunner.driver.get(TestSuitRunner.i).findElements(By.xpath(testObject)).get(0));
                       testStepError = "Frame"+testObject+" selected successfully";
                       strFlag = "true";
                }
                catch (NoSuchElementException e) {
                       testStepError = "Unable to select frame";
                       TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
                       strFlag = "false";
                }
                catch (Exception e) {
                    testStepError = "Unable to select frame";
                    TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
                    strFlag = "false";
                }
				break;
			case "EXTJS_SELECT_DROPDOWN" :
                try {
					WebElement textbox = ElementLocator.elementExists(elementType,testObject);
					textbox.click();
					textbox.clear();
					textbox.sendKeys(testParameter);
					String selectValue = "//li[text()='"+testParameter+"']";
				   
					
					ElementLocator.waitElementExists("xpath", selectValue);
				   
					WebElement dropDownElement = ElementLocator.elementExists("xpath",selectValue);
					dropDownElement.click();
					testStepError = "Dropdown value "+testParameter+" selected successfully";
					strFlag = "true";
                }
                catch (NoSuchElementException e) {
                    testStepError = "Unable to select dropdown value";
                    TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
                    strFlag = "false";
                }
                catch (Exception e) {
                    testStepError = "Unable to select dropdown value";
                    TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
                    strFlag = "false";
                }
				break;

			case "CUSTOM_EXTJS_SELECT_DROPDOWN" :
                try {
                    WebElement textbox = ElementLocator.elementExists(elementType,testObject);
                    textbox.click();
                    String selectValue = "//li[text()='"+testParameter+"']";
                     
                    
                    ElementLocator.waitElementExists("xpath", selectValue);
                    
                    WebElement dropDownElement = ElementLocator.elementExists("xpath",selectValue);
                    dropDownElement.click();
                    testStepError = "Dropdown value "+testParameter+" selected successfully";
                    strFlag = "true";
                }
                catch (NoSuchElementException e) {
                    testStepError = "Unable to select dropdown value";
                    TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
                    strFlag = "false";
                }
                catch (Exception e) {
                    testStepError = "Unable to select dropdown value";
                    TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
                    strFlag = "false";
                }
				break;
				//**********Added for verifying comments[Field Staff,Mobile Number,Comments] text on Mobile App************//
            case "VERIFY_COMMENTS_APP":
				try{
					String [] mytxt = testParameter.split(",");
					String fieldStaff = "//div[@class='ui-block-a mrmTaskCommentsCommentAuthorNameStyle ui-mrm-plain-roboto-label-font' and contains(text(),'"+mytxt[0]+"')]";
					String fsMobile = "//div[@class='ui-block-b ui-btn ui-btn-sub ui-btn-icon-left ui-icon-mrmCallIcon ui-nodisc-icon ui-mrm-roboto-light-label-font mrmTaskCommentsPhoneNumberStyle' and contains(text(),'"+mytxt[1]+"')]";
					String comments="//div[@class='ui-grid-solo ui-mrm-plain-roboto-label-font mrmTaskCommentsCommentStyle' and contains(text(),'"+mytxt[2]+"')]";
					if (ElementLocator.elementExists("xpath",fieldStaff).isDisplayed()==true && ElementLocator.elementExists("xpath",fsMobile).isDisplayed()==true && ElementLocator.elementExists("xpath",comments).isDisplayed()==true){
						strFlag = "true";
					}else {
						testStepError = "fieldStaff => "+fieldStaff+" or fsMobile ==>"+fsMobile+"or comments ==>"+comments+"not displayed";
						TestSuitRunner.logger.info(testStepError);
						strFlag = "false";
					}
				}
                catch(Exception e){
					testStepError = "Unable to verify Comments "+testParameter;
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
                }
				break;
                  //****************END***********************   
				
				//**********Added for verifying comments[both creator and comment text] on Webapp************//
            case "VERIFY_COMMENTS_WEB":
				try{
					String [] mytxt = testParameter.split(",");
					String creator = "//span[@class='activity-author']//b[contains(text(),'"+mytxt[0]+"')]";
					String comments="//div[@class='activity-new' and contains(text(),'"+mytxt[1]+"')]";
					if (ElementLocator.elementExists("xpath",creator).isDisplayed()==true && ElementLocator.elementExists("xpath",comments).isDisplayed()==true){
						strFlag = "true";
					}else {
						testStepError = "Creator => "+creator+" or Comments ==>"+comments+" not displayed";
						TestSuitRunner.logger.info(testStepError);
						strFlag = "false";
					}
				}
                catch(Exception e){
					testStepError = "Unable to verify Comments "+testParameter;
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
                }
				break;
                  //****************END*********************** 
				
				//**********Added for selecting customer from customer list on Webapp************//
            case "SELECT_CUSTOMER":
				try{
					String myxpath = "//div[contains(text(),'"+testParameter+"')]";
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						WebElement selectCustomer = ElementLocator.elementExists("xpath",myxpath);
						Actions action = new Actions(TestSuitRunner.driver.get(TestSuitRunner.i));
						//Double click
						action.doubleClick(selectCustomer).perform();
						//selectCustomer.click();
						strFlag = "true";
					}else {
						testStepError = testParameter+" customer is not displayed";
						TestSuitRunner.logger.info(testStepError);
						strFlag = "false";
					}
				}
                catch(Exception e){
					testStepError = "Unable to select "+testParameter+" customer";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
                }
				break;
                  //****************END***********************    
				
              //**********Added for selecting task from task list on App************//
            case "MOBILE_SELECT_TASK":
				try{
					String myxpath = "//*[contains(text(),'"+testParameter+"')]";
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						WebElement selectTask = ElementLocator.elementExists("xpath",myxpath);
						selectTask.click();
						strFlag = "true";
					}else {
						testStepError = testParameter+" is not displayed";
						TestSuitRunner.logger.info(testStepError);
						strFlag = "false";
					}
				}
                catch(Exception e){
					testStepError = "Unable to find element "+testParameter+" to delete";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
                }
				break;
                  //****************END***********************
            case "TEST_API":
				try{
					boolean test=true;
					test=ApiTest.main();
					if(!test)
					{
						strFlag = "false";
						testStepError = "Unable to execute all API's";
					}
					else
					{
						strFlag = "true";
						testStepError = "Successfully executed all API's";
						
					}
				}
				catch(Exception e){
					testStepError = "Unable to find element "+testParameter+" to delete";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
                }
				break;
                  //****************END***********************
				
				 //**********Added for geting Access Token for office staff************//
            case "GET_OS_ACCESS_TOKEN":
				try{
					/*int comma=0;
					for(int z=0;z<testParameter.length();z++)
					{
							
						if(testParameter.charAt(z)=='"')
						{
							if(comma==0 || (comma%2==0))
							{
							testParameter.replace('"','\"');
							comma++;
						    }
							else
							{
								testParameter.replace(","\);
								comma++;
							}
						}
					}*/
					String[] parameter= testParameter.split("NaN");
					System.out.println(parameter[0]);
					System.out.println(parameter[1]);
					
					RestAssured.proxy("ptproxy.persistent.co.in", 8080);   
					RestAssured.useRelaxedHTTPSValidation();
				  //Response resp1 = given().auth().basic("admin@acc1", "admin@123").then().get("http://10.221.1.107:9999/mwm/auth/v1/officestaffs/login?grant_type=password&client_id=test");
				  Response resp1 = given().auth().preemptive().basic("admin@acc1","admin@123").get("https://121.243.26.37:41442/mwm/auth/v1/officestaffs/login?grant_type=password&client_id=test");
				  System.out.println(resp1.getStatusCode());
				  System.out.println(resp1.asString());	
				  //JSONArray jsonResponse = new JSONArray(resp1.asString());
				  //String token = jsonResponse.getJSONObject(0).getString("access_token");
				  String token = resp1.jsonPath().getString("access_token");
				  System.out.println(token);
				  Response resp = given().auth().basic("admin@acc1", "admin@123").then().get("https://121.243.26.37:41442/mwm/v1/enterprises/acc1?access_token="+token);
				  System.out.println(resp.getStatusCode());
				  System.out.println(resp.asString());
				  //Response resp2 = given().auth().preemptive().basic("admin@acc1", "admin@123").and().contentType("application/json").and().body("{\"name\":\"TestAPI2\",\"enterpriseCode\":\"api2\",\"email\":\"DummyEmail@ABC.COM\",\"mobileNo\":\"1234567890\",\"mapType\":\"GOOGLE\",\"consentTextForSms\":\"Enterprise TestAPI is requesting your consent for enabling tracking of your mobile device.\",\"locationDeterminationInterval\":30,\"accountType\":\"PREMIUM\",\"areaOfOperation\":\"ROTN\"}").post("https://121.243.26.37:41442/mwm/v1/enterprises?access_token="+token);
				  Response resp2 = given().auth().preemptive().basic("admin@acc1", "admin@123").and().contentType("application/json").and().body(parameter[0]).post("https://121.243.26.37:41442/mwm/v1/enterprises?access_token="+token);
				  System.out.println(resp2.getStatusCode());
				  System.out.println(resp2.asString());
				  if(parameter[1].equals(resp2.asString()))
				  {
					  System.out.println("Hurray");
				  }
				  strFlag = "true";
				  /*
				  if(resp2.jsonPath().getString("links.href")!=null)
				  {	  
				  String href = resp2.jsonPath().getString("links.href");
				  System.out.println(href);
				  strFlag = "true";
				  }
				  else
				  {
					  strFlag = "false"; 
					  System.out.println("HREF tag not found");
				  }
				  //Response resp = given().auth().basic("admin@acc1", "admin@123").get("https://10.222.115.172:9009/mwm/auth/v1/officestaffs/login?grant_type=password&client_id=acc1");
				  //Response resp = get("http://restcountries.eu/rest/v1/name/norway");
				  //JSONArray jsonResponse = new JSONArray(resp);
				  //JSONArray jsonResponse = new JSONArray(resp.asString());
				  //String token = path("","access_token");
				  //String token = jsonResponse.getJSONObject(0).getString("capital");
				  //resp.getStatusCode();
				  //System.out.println(token);
				  //System.out.println(resp.getStatusCode());
				 // strFlag = "true";
					/*if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						WebElement selectTask = ElementLocator.elementExists("xpath",myxpath);
						selectTask.click();
						strFlag = "true";
					}else {
						testStepError = testParameter+" is not displayed";
						TestSuitRunner.logger.info(testStepError);
						strFlag = "false";
					}*/
					
				}
                catch(Exception e){
					testStepError = "Unable to find element "+testParameter+" to delete";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
                }
				break;
                  //****************END***********************

              //Keywords for verifying Enterprise
            case "VERIFY_ENTERPRISE_NAME":
            	try{
					//String myxpath = "//*[contains(text(),'"+testParameter+"')]";
					String myxpath = "//div[contains(@class,'enterpriseDynamicGrid')//contains(text(),'"+testParameter+"')]";
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						strFlag = "true";
					}else {
						testStepError = myxpath+" is not displayed";
						TestSuitRunner.logger.info(testStepError);
						strFlag = "false";
					}
				}
                catch(Exception e){
					testStepError = "Unable to find enterprise name "+testParameter+" to verify";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
                }
				break;
				
				//Keyword for Verifying the Enterprise ID
				
				
				
            case "VERIFY_ENTERPRISE_ID":
            	try{
					//String myxpath = "//*[contains(text(),'"+testParameter+"')]";
					String myxpath = "//div[contains(@class,'-enterpriseCode') and contains(text(),'"+testParameter+"')]";
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						strFlag = "true";
					}else {
						testStepError = myxpath+" is not displayed";
						TestSuitRunner.logger.info(testStepError);
						strFlag = "false";
					}
				}
                catch(Exception e){
					testStepError = "Unable to find enterprise Id "+testParameter+" to verify";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
                }
				break;
				
				
//Keyword for Verifying the Enterprise Contact Number
				
				
				
            case "VERIFY_ENTERPRISE_CONTACTNUMBER":
            	try{
					//String myxpath = "//*[contains(text(),'"+testParameter+"')]";
					String myxpath = "//div[contains(@class,'-enterprisePhone') and contains(text(),'"+testParameter+"')]";
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						strFlag = "true";
					}else {
						testStepError = myxpath+" is not displayed";
						TestSuitRunner.logger.info(testStepError);
						strFlag = "false";
					}
				}
                catch(Exception e){
					testStepError = "Unable to find Contact Number "+testParameter+" to verify";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
                }
				break;
				
//Keyword for Verifying the Enterprise Email Address
				
				
				
            case "VERIFY_ENTERPRISE_EMAIL":
            	try{
					//String myxpath = "//*[contains(text(),'"+testParameter+"')]";
					String myxpath = "//div[contains(@class,'-enterpriseEmail') and contains(text(),'"+testParameter+"')]";
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						strFlag = "true";
					}else {
						testStepError = myxpath+" is not displayed";
						TestSuitRunner.logger.info(testStepError);
						strFlag = "false";
					}
				}
                catch(Exception e){
					testStepError = "Unable to find Email address "+testParameter+" to verify";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
                }
				break;
				
//Keyword for Verifying the Enterprise Map Type
				
				
				
            case "VERIFY_ENTERPRISE_MAPTYPE":
            	try{
					//String myxpath = "//*[contains(text(),'"+testParameter+"')]";
					String myxpath = "//div[contains(@class,'-mapType') and contains(text(),'"+testParameter+"')]";
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						strFlag = "true";
					}else {
						testStepError = myxpath+" is not displayed";
						TestSuitRunner.logger.info(testStepError);
						strFlag = "false";
					}
				}
                catch(Exception e){
					testStepError = "Unable to find Map Type "+testParameter+" to verify";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
                }
				break;
				
				//Keyword for Verifying the Enterprise Account Type
				
            case "VERIFY_ENTERPRISE_ACCOUNTTYPE":
            	try{
					//String myxpath = "//*[contains(text(),'"+testParameter+"')]";
					String myxpath = "//div[contains(@class,'-accountType') and contains(text(),'"+testParameter+"')]";
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						strFlag = "true";
					}else {
						testStepError = myxpath+" is not displayed";
						TestSuitRunner.logger.info(testStepError);
						strFlag = "false";
					}
				}
                catch(Exception e){
					testStepError = "Unable to find Account Type "+testParameter+" to verify";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
                }
				break;
				
				//Keyword for Verifying the Enterprise  Location Interval
				
            case "VERIFY_ENTERPRISE_LOCATIONINTERVAL":
            	try{
					//String myxpath = "//*[contains(text(),'"+testParameter+"')]";
					String myxpath = "//div[contains(@class,'-locationInterval') and contains(text(),'"+testParameter+"')]";
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						strFlag = "true";
					}else {
						testStepError = myxpath+" is not displayed";
						TestSuitRunner.logger.info(testStepError);
						strFlag = "false";
					}
				}
                catch(Exception e){
					testStepError = "Unable to find Location Interval "+testParameter+" to verify";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
                }
				break;
				
				
//Keyword for Verifying the Area Of Operation
				
            case "VERIFY_ENTERPRISE_AREAOFOPERATION":
            	try{
					//String myxpath = "//*[contains(text(),'"+testParameter+"')]";
					String myxpath = "//div[contains(@class,'-areaOfOperation') and contains(text(),'"+testParameter+"')]";
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						strFlag = "true";
					}else {
						testStepError = myxpath+" is not displayed";
						TestSuitRunner.logger.info(testStepError);
						strFlag = "false";
					}
				}
                catch(Exception e){
					testStepError = "Unable to find Area Of Operation "+testParameter+" to verify";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
                }
				break;
		//Keyword to WAIT for number of seconds specified in the test parameter
            case "WAIT_SECONDS":
            	try{
            		int time=Integer.parseInt(testParameter);
            		 //TimeUnit.SECONDS.sleep(time);
            		System.out.println("wait for "+time+" seconds started");
            		TimeUnit.SECONDS.sleep(time);
            		 //TestSuitRunner.driver.get(TestSuitRunner.i).wait(time);
            		 System.out.println("wait for "+time+" seconds ended"); 
            		 testStepError = "Waited for "+testParameter+" seconds";
            		 strFlag = "true";
            	}catch(Exception e)
            	{
            		TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
            		testStepError = "Unable to wait for "+testParameter+" seconds";
            		strFlag = "false";
            	}
            	break;
//Keyword for Verifying the Area Of Operation
								
            case "VERIFY_ENTERPRISE_NAME_MISSING":
            	try{
					//String myxpath = "//*[contains(text(),'"+testParameter+"')]";
					String myxpath = "//div[contains(@class,'-invalid-icon-default') and contains(text(),'"+testParameter+"')]";
					if (ElementLocator.elementExists("xpath",myxpath).isDisplayed()==true){
						strFlag = "true";
					}else {
						testStepError = myxpath+" is not displayed";
						TestSuitRunner.logger.info(testStepError);
						strFlag = "false";
					}
				}
                catch(Exception e){
					testStepError = "Unable to MIssing error "+testParameter+" to verify";
					TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow,e);
					strFlag = "false";
                }
				break;
			default :
				testStepError = testkeyword+" keyword not found.";
				TestSuitRunner.logger.error(testStepError+"|TC ID:- "+TestSuitRunner.currTestCase+"|TS ID:- "+TestSuitRunner.tcRow);
				break;
			}
			
			if (strFlag=="true")
			{
				flagAction=true;
			}
			else if (strFlag=="false" & TestSuitRunner.driverFlag==true)
			{
				try {
					takeScreenshot();
				} catch (Exception e) {
					testStepError = testStepError + e.getMessage();
					System.out.println(TestSuitRunner.currTestStep+ " - Failed!");
					e.printStackTrace();
				}
				flagAction=false;
			}
			else if (strFlag=="false" & TestSuitRunner.driverFlag==false){
				flagAction=false;
				System.out.println("failed - "+testStepError);
			}
			testkeyword = ""; 
			testParameter = "";
			testObject = "";
			testObjectName = "";
	
			return flagAction;
			}
	
		public void takeScreenshot() throws WebDriverException, IOException
			{
				String myTimeStamp = SaveResult.dateTimeStamp();
				
				File directory = new File (".");
				String currPath = directory.getCanonicalPath();
				screenshotFolderPath = currPath+"\\Results\\Screenshots\\";
				File theDir = new File(screenshotFolderPath);
				if (!theDir.exists()){
					System.out.println("creating directory: " + screenshotFolderPath);
				    boolean result = theDir.mkdir();  

				     if(result) {    
				       System.out.println("DIR created");  
				     }

				}

				FileOutputStream file = new FileOutputStream(screenshotFolderPath+TestSuitRunner.currTestCase+"_"+TestSuitRunner.currTestStep+"_"+myTimeStamp + ".png");
				file.write(((TakesScreenshot) TestSuitRunner.driver.get(TestSuitRunner.i)).getScreenshotAs(OutputType.BYTES));
				file.close();
			}

}
