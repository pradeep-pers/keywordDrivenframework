

/*import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;*/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class OracleJdbcConnector {
	static String DB_CONNECTION_STRING = null;
	static String DB_USER = null;
    public static boolean connector(String testParameter) throws SQLException {
        
    	try {
    	/*	String varPath = FileOperations.getTemplateProjectFolder();
    		String sqlFilePath = varPath+"//FENCE.sql";
    		FileInputStream fstream = new FileInputStream(sqlFilePath);
    		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

    		String strLine;

    		//Read File Line By Line
    		while ((strLine = br.readLine()) != null)   {
	    		  // Print the content on the console
	    			System.out.println (strLine);*/
	    			
	    		  //URL of Oracle database server
	    			for (int ivar=1; ivar<TestSuitRunner.arrTestCaseBuild.length;ivar++)
					{
						if (TestSuitRunner.arrTestCaseBuild[ivar][0].equals("DB_CONNECTION_STRING")){
							DB_CONNECTION_STRING = TestSuitRunner.arrTestCaseBuild[ivar][1];
						}
						if (TestSuitRunner.arrTestCaseBuild[ivar][0].equals("DB_USER")){
							DB_USER = TestSuitRunner.arrTestCaseBuild[ivar][1];
						}
					}
					
			        
					String url = "jdbc:oracle:thin:@"+DB_CONNECTION_STRING;
			        Properties props = new Properties();
			        props.setProperty("user",DB_USER);
			        props.setProperty("password",DB_USER);
			      
			        //creating connection to Oracle database using JDBC
			        Connection conn = DriverManager.getConnection(url,props);
			
			        //creating PreparedStatement object to execute query
			        PreparedStatement preStatement = conn.prepareStatement(testParameter);
			        preStatement.executeQuery();
    			//}

	    		//Close the input stream
	    		//br.close();
	    		System.out.println("done");
	    		return true;
	    	}
    	catch (Exception e){
    		TestSuitRunner.logger.error("mySQLConnector|connector|Unable to execute sql query.",e);
    		return false;
    	}
    }
}
