package application;

import java.sql.ResultSet;

public class Order {
	static String orderName= "";
	static int order_ID;
	//add new Order to Database
	public static int addOrder(String order_Name) {
		//isim kontrolü
		String name = "";
		String control = "SELECT auftrag_Name FROM auftrag";
		try {
			ResultSet rs = DBConnection.connection.createStatement().executeQuery(control);
			while(rs.next()) {
				name = rs.getString(1);
				if(name.equals(order_Name)) {
					return -1;
				}
			}
			
		}catch(Exception e) {
			System.out.println(e);
			return -2;
		}
		String query = "INSERT INTO auftrag(auftrag_Name) VALUES ('"+order_Name+"')";
		try {
			 DBConnection.connection.createStatement().execute(query);
		}catch(Exception e) {
			System.out.println(e);
			return -2;
		}
		return 0;
	}
	
	//select an order from Database
	public static int selectOrder(String order_Name) {
		
		//control if in Database this order exists
		boolean bol = false;
		String control = "SELECT auftrag_Name FROM auftrag";
		String name = "";
		try {
			ResultSet rs = DBConnection.connection.createStatement().executeQuery(control);
			while(rs.next()) {
				name = rs.getString(1);
				if(name.equals(order_Name)) {
					bol = true;
					orderName = name;
				}
			}
			
		}catch(Exception e) {
			System.out.println(e);
			return -1;
		}
		String control2 = "SELECT auftrag_ID FROM auftrag WHERE" + " auftrag_Name" + " = "+ "'"+name+"'";
		try {
			ResultSet rs = DBConnection.connection.createStatement().executeQuery(control2);
			if(rs.next()) {
				order_ID = rs.getInt(1);
				//System.err.println(order_ID);
			}
			
		}catch(Exception e) {
			System.out.println(e);
			return -1;
		}		
		//if order in Database doesn't exists
		if(bol==false) {
			return -2;
		}
		return 0;
   }
	
	
}
