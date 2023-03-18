import java.sql.*;
public class dbOperations {
	static{ // would have to be surrounded by try catch
	    try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   // this will load the class Driver
	}
	public static String getObjectFromDb(String query)
	{
		/*String url = "jdbc:mysql://localhost:3306/automation";
        String userid = "root";
        String password = "root@123";*/
        String url = TestSuitRunner.arrTestCaseBuild[37][1];
        String userid = TestSuitRunner.arrTestCaseBuild[38][1];
        String password = TestSuitRunner.arrTestCaseBuild[39][1];
        try {
            Connection connection = DriverManager.getConnection(url,
                    userid, password);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs!=null)
            {
            rs.next();
            System.out.println("Object Fetched successfully");
            //System.out.println(rs.getObject(1));
            //System.out.println(rs.toString());
            //System.out.println(rs.getMetaData());
            return(rs.getObject(1).toString());
            }
            else
            {
            	System.out.println("Object not Found");
            	return("Object not found");
            }
               // st.executeUpdate(query);
                //"INSERT INTO obj_repo (obj_name, obj_value) VALUES('" + key + "','" + value + "')";
                
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return("Object not found");
        }
    
	}

}
