package com.restaurantmenu.restaurant;

import com.restaurantmenu.databaseconnection.MySqlDbConnection;
import com.restaurantmenu.responsehandle.ResponseHandle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by samintha on 2/24/2017.
 */
public class Restaurant extends MySqlDbConnection {

    protected String restaurantUname;
    protected String restaurantName;
    protected int restaurantID;
    protected int HTTPStatusCode;
    protected ResponseHandle responseHandle;

    public String getRestaurantUname() {
        return restaurantUname;
    }

    public void setRestaurantUname(String restaurantUname) {
        this.restaurantUname = restaurantUname;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }

    public int getHTTPStatusCode() {
        return HTTPStatusCode;
    }

    protected void setRestaurantIDFromUname(String SQLQuery, String Uname) {
        int restaurtID = -1;
        ResultSet rs = null;
        try {
            prepStmt = con.prepareStatement(SQLQuery);
            prepStmt.setString(1, Uname);

            rs = prepStmt.executeQuery();
            while (rs.next()) {
                restaurtID = rs.getInt(1);
            }
            this.restaurantID = restaurtID;
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
    }

    public String getRestaurantDetails() {
        JSONArray json_arr = new JSONArray();
        ResultSet rs = null;
        String SQL = "SELECT restaurant_name,contact_to_display,email_to_display FROM restaurant_admins WHERE restaurant_id=? LIMIT 1";
        try {
            prepStmt = con.prepareStatement(SQL);
            prepStmt.setInt(1, restaurantID);

            rs = prepStmt.executeQuery();
            while (rs.next()) {
                JSONObject json = new JSONObject();
                json.put("restaurant_name", rs.getString(1));
                json.put("contact", rs.getString(2));
                json.put("email", rs.getString(3));
                json_arr.put(json);
            }
            HTTPStatusCode = 200;
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
