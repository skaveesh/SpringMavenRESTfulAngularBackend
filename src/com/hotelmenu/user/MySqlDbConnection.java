package com.hotelmenu.user;

import java.sql.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class MySqlDbConnection {
	public String connectDB() {
		JSONArray json_arr = new JSONArray();

		try {
			Class.forName("com.mysql.jdbc.Driver");
			String connectionStr = "jdbc:mysql://hotelmenudatabase.cbnhjzempwyk.us-west-2.rds.amazonaws.com:3306/hotelmenu_database";
			Connection con = DriverManager.getConnection(connectionStr, "samintha", "x455ldasus");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from users");
			while (rs.next()) {
				JSONObject json = new JSONObject();
				json.put("id", rs.getInt(1));
				json.put("username", rs.getString(2));
				json.put("email", rs.getString(3));
				json_arr.put(json);
			}
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return (json_arr.toString());
	}
}
