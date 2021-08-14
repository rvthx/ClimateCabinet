package application;

import java.sql.ResultSet;
import java.sql.SQLException;


public class Sample {
	static String sampleNumber = "";
	static int slot = -1;
	static String sampleStatus = "";
	static String[] slotArr = new String [20];
	//add new sample to database
	
	public static int addSample(String sample_deviceID, String sample_Status) {
		
		//control name if already exist
		String control = "select prüfling_BauteilID from prüfling";
		try {
			 ResultSet rs = DBConnection.connection.createStatement().executeQuery(control);
			 while(rs.next()) {
				String sample_id = rs.getString(1);
				if(sample_id.equals(sample_deviceID)) {
					return -1;
				}
			}
			 
		}catch(Exception e) {
			System.out.println(e);
		}
			
		//add to database
		String query = "insert into prüfling(prüfling_BauteilID, prüfling_Status) "
				+ "VALUES('"+sample_deviceID+"', '"+sample_Status+"') ;";
		try {
			DBConnection.connection.createStatement().execute(query);
			sampleNumber = sample_deviceID;
			
		}catch(SQLException e) {
			System.out.println(e);
		}
		return 0;
	}
	
	public static int selectSample(String sample_deviceID) {
		
		//control name if already exist
		String control = "select prüfling_BauteilID from prüfling";
		try {
				ResultSet rs = DBConnection.connection.createStatement().executeQuery(control);
				while(rs.next()) {
				String device_id = rs.getString(1);
				if(device_id.equals(sample_deviceID)) {
					sampleNumber = device_id;
					slot++;
					slotArr[slot] = device_id;
					//slotMap.put(device_id,slot);
					System.out.println("i"+ slot+ "slot[i]: "+ slotArr[slot]);
					if(slotArr[19] != null) {
						return -3;
					}
					return 0;
				}
			}
		}catch(Exception e) {
			System.out.println(e);
			return -1;
		}
		return -2;
	}
	
	
	//change the status of sample
	public static int changeSampleStatus(String sample_deviceID, String sample_Status) {
		
		//if sample is active change it to inactive
		if(sample_Status == "active") {
			String query = "update prüfling set prüfling_Status = 'inactive' where prüfling_BauteilID ='"+sample_deviceID+"'";
			try {
				DBConnection.connection.createStatement().execute(query);
			}catch(SQLException e) {
				System.out.println(e);
				return -1;
			}
		}
		
		//if sample is inactive change it to active
		else {
			String query = "update prüfling set prüfling_Status = 'active' WHERE prüfling_BauteilID ='"+sample_deviceID+"'";
			try {
				DBConnection.connection.createStatement().execute(query);
			}catch(SQLException e) {
				System.out.println(e);
				return -1;
			}
		}
		
		return 0;
	}
	
	
	//update sample ID
	public static int updateSampleID(String sample_deviceID, String lastID) {
		
		String control = "select prüfling_BauteilID from prüfling";
		try {
			 ResultSet rs = DBConnection.connection.createStatement().executeQuery(control);
			 while(rs.next()) {
				String bauteil_id = rs.getString(1);
				if(bauteil_id.equals(lastID)) { //this checks if there is a sample with newly given ID
					return -1;
				}
			}
		}catch(Exception e) {
			System.out.println(e);
			return -2;
		}
		
		
		String query = "update prüfling SET prüfling_BauteilID =" + lastID +" where prüfling_BauteilID ='"+sample_deviceID+"'";
		try {
			DBConnection.connection.createStatement().execute(query);
		}catch(SQLException e) {
			System.out.println(e);
			return -2;
		}
		return 0;
	}

	//remove prufling from database
	public static int removeSample(String sample_deviceID) {
		//control name if already exist
		String control = "select prüfling_BauteilID from prüfling";
		try {
			 ResultSet rs = DBConnection.connection.createStatement().executeQuery(control);
			 while(rs.next()) {
				String sample_id = rs.getString(1);
				if(sample_id.equals(sample_deviceID)) {
					String query = "delete from prüfling WHERE prüfling_BauteilID ='"+sample_deviceID+"'";
					DBConnection.connection.createStatement().execute(query);
					return 0;
				}
			}			 
		}catch(Exception e) {
			System.out.println(e);
			return -1;
		}
		return -2;
	}
}
