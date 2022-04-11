package application;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
	
	static boolean isloggedIn = false;
	static boolean isAdmin = false;
	
	static String userName;
	static String userRole;
	static String cabinetNumber="";
	static String userStatus;
	
	public static int  user_ID;
	public static int  cabinet_ID;
	
	public static int addUser(String user_Name, String user_Password, String user_Role, String user_Status) {
		
		//control if there is another user with the same name
		String control = "SELECT benutzer_Name FROM benutzer";
		try {
			 ResultSet rs = DBConnection.connection.createStatement().executeQuery(control);
			while(rs.next()) {
				String name = rs.getString(1);
				if(name.equals(user_Name)) {
					return -1;
				}
			}
		}catch(Exception e) {
			System.err.println(e);
			return -2;
		}
		
		//code the password
		int password = user_Password.hashCode();
		String passwordstr = Integer.toString(password);
		
		
		//add new user to the Database
		String query = "INSERT INTO benutzer(benutzer_Name, benutzer_Passwort, benutzer_Rolle, benutzer_Status) "
				+ "VALUES('"+user_Name+"', '"+passwordstr+"', '"+user_Role+"', '"+user_Status+"') ;";
		try {
			DBConnection.connection.createStatement().execute(query);
		}catch(SQLException e) {
			System.out.println(e);
			return -2;
		}
		return 0;
	}
	
	public static int loginUser(String user_Name, String user_Password) {
		//control if  are there any user in database with the same name
		String control = "SELECT benutzer_Name FROM benutzer";
		boolean bol = false;
		boolean bol2 = false;
		
		try {
			
			ResultSet rs = DBConnection.connection.createStatement().executeQuery(control);
			while(rs.next()) {
				String name = rs.getString(1);
				if(name.equals(user_Name)) {
					bol = true;
				}
			}
			
		}catch(Exception e) {
			System.err.println(e);
			return -2;
		}
		
		//if user name is in database 
		if(bol == false) {
			return -1;
		}
		
		
		//control if password is true
		String query = "SELECT benutzer_Passwort FROM benutzer WHERE benutzer_Name = '"+ user_Name +"'";
		int password = user_Password.hashCode();
		String passwordstr = Integer.toString(password);
		try {
			
			ResultSet rs = DBConnection.connection.createStatement().executeQuery(query);
			while(rs.next()) {
				if(passwordstr.equals(rs.getString(1))) {
					bol2=true;
				}
			}
			
		}catch(Exception e) {
			System.out.println(e);
			return -2;
		}
		
		
		//password is true
		if(bol2==true) {
			
			//if there is already a user, another user can not login
			if(isloggedIn == true) {
				return -3;
			}
			
			isloggedIn = true;
			//control if user is admin or tester
			String query2 = "SELECT benutzer_Rolle FROM benutzer WHERE benutzer_Name = '"+ user_Name +"'";
			try {
				
				ResultSet rs = DBConnection.connection.createStatement().executeQuery(query2);
				while(rs.next()) {
					if(rs.getString(1).equals("ADMIN")) {
						isAdmin = true;
						userRole = "ADMIN";
					}else {
						userRole = "TESTER"; 
					}
					userName = user_Name;
				}
				return 0;
				
			}catch(Exception e) {
				System.out.println(e);
				return -2;
			}
			
			
		}else {
			return -4;
		}
	}
	
	//select cabinet from Database
	public static int selectCabinet(String cabinet_Serialnumber){
		
		if(cabinet_Serialnumber == "") {
			return 1;
		}
		//control if there is a cabinet with that number
		boolean bol = false;
		String control = "SELECT schrank_Seriennummer FROM schrank";
		String control1 = "SELECT benutzer_ID FROM benutzer WHERE benutzer_Name = '"+ User.userName+"'";
		String control2 = "select schrank_ID from schrank where schrank_Seriennummer = '"+cabinet_Serialnumber+"'";
		
		try {
			 ResultSet rs = DBConnection.connection.createStatement().executeQuery(control);
			 while(rs.next()) {
				String serialnumber = rs.getString(1);
				if(serialnumber.equals(cabinet_Serialnumber)) {
					bol = true;
					User.cabinetNumber = cabinet_Serialnumber;
				}
			}
			
		}catch(Exception e) {
			System.out.println(e);
		
		}
		
		if(bol==false) {
			return -1;
		}
		else {
			try {
				ResultSet rs1 = DBConnection.connection.createStatement().executeQuery(control1);
				ResultSet rs2 = DBConnection.connection.createStatement().executeQuery(control2);
				while(rs1.next()) {
					user_ID = rs1.getInt(1);
					System.out.println(user_ID);
				}
				while(rs2.next()) {
					cabinet_ID = rs2.getInt(1);
					System.out.println(cabinet_ID);
				}
				
				System.out.println(user_ID);
				String query = "INSERT INTO nutzungLog(benutzer_ID, schrank_ID) "
						+ "VALUES("+user_ID+", "+cabinet_ID+");";
				try {
					DBConnection.connection.createStatement().execute(query);
				}catch(SQLException e) {
					
					System.out.println(e);
					return -1;
				}
				
			}catch(Exception e) {
				System.out.println(e);
			}	
		}
		return 0;
	}
	
	
	//logout user
	public static int logoutUser() {
		isloggedIn = false;
		isAdmin = false;
		userName="";
		userRole ="";
		
		return 0;
	}
	
}
