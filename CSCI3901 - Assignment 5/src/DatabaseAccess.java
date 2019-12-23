import java.sql.*;

//DatabaseAccess class contains the methods for creating a connection 
//as well for terminating the existing connection 
public class DatabaseAccess {
	
	//method for creating a connection 
	Connection createConnection(){
		Connection con = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection
					("jdbc:mysql://db.cs.dal.ca:3306/csci3901","amoli","B00835717");			
			}
		catch(Exception e){e.printStackTrace();}
		return con;
	}
	
	//method for terminating an existing connection 
	void closeConnection(Connection con){
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
