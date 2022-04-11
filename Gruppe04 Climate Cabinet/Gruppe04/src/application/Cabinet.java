package application;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Cabinet {
	
	public static Set<String> cabinet = new HashSet<String>();
	public static ArrayList<String> sampleStatus = new ArrayList<String>();
	public static ArrayList<String> samplePingStatus = new ArrayList<String>();
	
	//add new cabinet to Database
	public static int addCabinet(String cabinet_Serialnumber) {
		String control = "SELECT schrank_Seriennummer FROM schrank";
		try {
			ResultSet rs = DBConnection.connection.createStatement().executeQuery(control);
			while(rs.next()) {
				String name = rs.getString(1);
				if(name.equals(cabinet_Serialnumber)) {
					return -1;
				}
			}
		}catch(Exception e) {
			System.err.println(e);
			return -2;
		}
		
		String query = "INSERT INTO schrank(schrank_Seriennummer) VALUES ('"+cabinet_Serialnumber+"');";
		try {
			DBConnection.connection.createStatement().execute(query);
			return 0;
		}catch(Exception e) {
			System.err.println(e);
			return -1;
		}
	}

}
