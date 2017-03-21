package com.restaurantmenu.databaseconnection;

import java.sql.*;

public class MySqlDbConnection {

    private Statement stmt;
    protected Connection con;
    protected PreparedStatement prepStmt;

    protected String connectDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            String connectionStr = "jdbc:mysql://localhost:3307/hotelmenu_dblocal?useSSL=true";
            con = DriverManager.getConnection(connectionStr, "root", "");
            stmt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
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
