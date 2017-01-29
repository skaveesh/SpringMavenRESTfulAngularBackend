package com.hotelmenu.user;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RestaurantAdminServices extends MySqlDbConnection {
    private int restaurantId;
    private String restaurantUname;
    private String restaurantName;
    private String password;
    private String email;
    private String adminFname;
    private String adminLname;
    private String contatctDisplay;
    private String emailDisplay;


    public String getRestaurantUname() {
        return restaurantUname;
    }

    public void setRestaurantUname(String restaurantUname) {
        this.restaurantUname = restaurantUname;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdminFname() {
        return adminFname;
    }

    public void setAdminFname(String adminFname) {
        this.adminFname = adminFname;
    }

    public String getAdminLname() {
        return adminLname;
    }

    public void setAdminLname(String adminLname) {
        this.adminLname = adminLname;
    }

    public String getContatctDisplay() {
        return contatctDisplay;
    }

    public void setContatctDisplay(String contatctDisplay) {
        this.contatctDisplay = contatctDisplay;
    }

    public String getEmailDisplay() {
        return emailDisplay;
    }

    public void setEmailDisplay(String emailDisplay) {
        this.emailDisplay = emailDisplay;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) { this.restaurantId = Integer.parseInt(restaurantId); }


    public String getRestaurantCredentials() {
        JSONArray json_arr = new JSONArray();
        ResultSet rs = null;
        String SQL = "SELECT email,password FROM hotelmenu_database WHERE restairant_uname=?";
        try {
            prepStmt = con.prepareStatement(SQL);
            prepStmt.setString(1,restaurantUname);
            rs = prepStmt.executeQuery();
            while (rs.next()) {
                JSONObject json = new JSONObject();
                json.put("email", rs.getString("email"));
                json.put("password", rs.getString("password"));
                json_arr.put(json);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return json_arr.toString();
    }


}
