package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	private static final String URL = "jdbc:mysql://160.153.129.32:3306/Klimapruefschrank?autoRecorrect=True&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"; //3306
	private static final String USERNAME = "gruppe04";
	private static final String PASSWORD = "Inf202Gruppe04";

	public static Connection connection = null;

	public static int establishConnection() {
		try {
	        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	        System.out.println("Connection with Database established.");
       } catch (SQLException e) {
    	   e.printStackTrace();
    	   return -1;
    	   } 
	   return 0;
	   }
	public static void closeConnection() {
		try {
			if (connection != null) {
				connection.close();
			}
		}catch (SQLException ex) {
			ex.printStackTrace();
			}
	}
	    
}
