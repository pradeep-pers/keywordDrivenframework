

import java.sql.*;

public class mySQLConnector {
	
	public static boolean connector(String testParameter) throws SQLException
	{
		//Connection con = null;
		  //String url = "jdbc:mysql://10.44.232.86:3306/";
		String url = "jdbc:mysql://10.222.115.115:3306/automation";
		//  String db = "risk_interface";
		  String driver = "com.mysql.jdbc.Driver";
		  String user = "automationDB";
		  String pass = "automationDB@123";
		  try{
		  //Class.forName(driver).newInstance();
		  Class.forName(driver);
		  Connection con = DriverManager.getConnection(url, user, pass);//`enter code here`
		  Statement st = con.createStatement();
		  //ResultSet res = 
		  st.execute(testParameter);
				  //st.executeQuery("delete from perm_trace_results where result_msisdn=3033813000;");
				  System.out.println("SQL code executed successfully.");
				  return true;
		  //System.out.println("Emp_code: " + "\t" + "Emp_name: ");
		  /*while (res.next()) {
		  int i = res.getInt("result_msisdn");
		  String s = res.getString("phoneid");
		  System.out.println(i + "\t\t" + s);
		  }*/
		  }
		  catch (SQLException e){
			  TestSuitRunner.logger.error("mySQLConnector|connector|Unable to execute mysql query.",e);
		  return false;
		  }  
		  catch (Exception e){
		  e.printStackTrace();
		  return false;
		  }
/*		finally {
		con.close();
		}*/
	}

}
