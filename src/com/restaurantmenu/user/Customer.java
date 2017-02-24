package com.restaurantmenu.user;

import com.restaurantmenu.databaseconnection.MySqlDbConnection;
import com.restaurantmenu.responsehandle.ResponseHandle;
import com.restaurantmenu.restaurant.Restaurant;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by samintha on 2/1/2017.
 */
public class Customer extends Restaurant {

    private String tableName;
    private String token;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //initialize connection to database from super class
    public Customer(String restaurantUname) {
        this.connectDB();
        this.restaurantUname = restaurantUname;
        setRestaurantIDFromUname("SELECT restaurant_id FROM restaurant_admins WHERE restaurant_uname=? LIMIT 1",this.restaurantUname);
    }

    public String authenticateCustomerAndGetMenu() {
        JSONArray json_arr = new JSONArray();
        ResultSet rs = null;
        String SQL = "SELECT token FROM restaurant_tables WHERE restaurant_id=? AND table_name=? LIMIT 1";
        try {
            prepStmt = con.prepareStatement(SQL);
            prepStmt.setInt(1, restaurantID);
            prepStmt.setString(2, tableName);

            rs = prepStmt.executeQuery();
            if (!rs.isBeforeFirst()) {
                //if the result set is empty it will not be authenticated
                responseHandle = new ResponseHandle("authentication", "authentication failed", "empty result set");
                json_arr.put(responseHandle.getResponseJSON());
                HTTPStatusCode = 401;
            } else {
                //if the token is correct this will authenticate customer
                String temp_token = null;
                while (rs.next()) {
                    temp_token = rs.getString(1);
                }
                if (temp_token.equals(token)) {
                    ResultSet menuRs = null;
                    String menuSQL = "SELECT * FROM menuitems WHERE restaurant_id=?";
                    try {
                        prepStmt = con.prepareStatement(menuSQL);
                        prepStmt.setInt(1, restaurantID);

                        menuRs = prepStmt.executeQuery();

                        while (menuRs.next()) {
                            JSONObject json = new JSONObject();
                            json.put("itemno", menuRs.getString(2));
                            json.put("itemname", menuRs.getString(3));
                            json.put("itemdesc", menuRs.getString(4));
                            json.put("itemprice", menuRs.getDouble(5));
                            json.put("itemingredients", menuRs.getString(6));
                            json.put("itemimageurl", menuRs.getString(7));
                            json.put("no_of_diners", menuRs.getInt(8));
                            json.put("itemcategory", menuRs.getString(9));
                            json.put("item_filter", menuRs.getString(10));
                            json.put("vegetarian", menuRs.getInt(11));
                            json.put("availability", menuRs.getInt(12));
                            json_arr.put(json);
                        }
                        HTTPStatusCode = 200;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (menuRs != null) {
                                menuRs.close();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    //if the token is incorrect it will not be authenticated
                    responseHandle = new ResponseHandle("authentication", "authentication failed", "token invalid");
                    json_arr.put(responseHandle.getResponseJSON());
                    HTTPStatusCode = 401;
                }
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
