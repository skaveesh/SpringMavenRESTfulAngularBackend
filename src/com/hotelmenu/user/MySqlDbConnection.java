package com.hotelmenu.user;

import java.sql.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class MySqlDbConnection {

    Statement stmt;
    Connection con;

    public void connectDB() {


        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionStr = "jdbc:mysql://hotelmenudatabase.cbnhjzempwyk.us-west-2.rds.amazonaws.com:3306/hotelmenu_database";
            con = DriverManager.getConnection(connectionStr, "samintha", "x455ldasus");
            stmt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void disconnectDB() {
        try {
            if (stmt != null)
                stmt.close();
            if (con != null)
                con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
