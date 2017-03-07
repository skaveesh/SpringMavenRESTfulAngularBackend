package com.restaurantmenu.user;

import com.restaurantmenu.configuration.Constance;
import com.restaurantmenu.responsehandle.ResponseHandle;
import com.restaurantmenu.restaurant.Restaurant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.compression.CompressionCodecs;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by samintha on 2/21/2017.
 */
public class Waiter extends Restaurant {

    private int waiterId = -1;
    private String waiterUname;
    private String password;
    private String waiterName;
    private String tableName = null;

    public Waiter(String waiterUname) {
        this.connectDB();
        this.waiterUname = waiterUname;
        setWaiterIdFromUname(); //setting waiter ID from uname
        setRestaurantIDFromUname("SELECT restaurant_id FROM waiters WHERE waiter_uname=? LIMIT 1", this.waiterUname);
    }

    public String getWaiterUname() {
        return waiterUname;
    }

    public void setWaiterUname() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWaiterName() {
        return waiterName;
    }

    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String loginRestaurantWaiter() {
        JSONArray json_arr = new JSONArray();
        ResultSet rs = null;
        String SQL = "SELECT password FROM waiters WHERE waiter_uname=? LIMIT 1";
        try {
            prepStmt = con.prepareStatement(SQL);
            prepStmt.setString(1, waiterUname);

            rs = prepStmt.executeQuery();
            if (!rs.isBeforeFirst()) {
                //if the restaurant_uname is incorrect it will not be authenticated
                responseHandle = new ResponseHandle("authentication", "authentication failed", "username or password incorrect");
                HTTPStatusCode = 401;
            } else {
                while (rs.next()) {
                    if (BCrypt.checkpw(password, rs.getString(1))) {

                        //making a new token for authorization this waiter in next time
                        Map<String, Object> claimmap = new HashMap<String, Object>();
                        claimmap.put("username", (Object) waiterUname);
                        String tokenJJWT = Jwts.builder()
                                .setClaims(claimmap)
                                .setIssuer("Login for Restaurant Waiter")
                                .setIssuedAt(new Date())
                                .setExpiration(new Date(new Date().getTime() + 7 * 24 * 60 * 60 * 1000)) //set expiration to week
                                .signWith(SignatureAlgorithm.HS512, Constance.AUTHORIZATION_PASSWORD)
                                .compact();

                        //making json object and putting it into response
                        JSONObject contentJson = new JSONObject();
                        contentJson.put("auth_token", tokenJJWT);

                        responseHandle = new ResponseHandle("authentication", "authentication success", contentJson);
                        HTTPStatusCode = 200;
                    } else {
                        responseHandle = new ResponseHandle("authentication", "authentication failed", "username or password incorrect");
                        HTTPStatusCode = 401;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            responseHandle = new ResponseHandle();
            HTTPStatusCode = 500;
        } catch (Exception e) {
            e.printStackTrace();
            responseHandle = new ResponseHandle();
            HTTPStatusCode = 500;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            json_arr.put(responseHandle.getResponseJSON());
        }
        return json_arr.toString();
    }

    public void setWaiterIdFromUname() {
        ResultSet rs = null;
        String SQL = "SELECT waiter_id FROM waiters WHERE waiter_uname=? LIMIT 1";

        try {
            prepStmt = con.prepareStatement(SQL);
            prepStmt.setString(1, waiterUname);

            rs = prepStmt.executeQuery();
            while (rs.next()) {
                this.waiterId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTables() {
        JSONArray json_arr = new JSONArray();
        ResultSet rs = null;
        String SQL = "SELECT table_name FROM restaurant_tables WHERE restaurant_id=?";
        try {
            prepStmt = con.prepareStatement(SQL);
            prepStmt.setInt(1, restaurantID);

            rs = prepStmt.executeQuery();

            JSONArray tablesArr = new JSONArray();
            while (rs.next()) {
                tablesArr.put(rs.getString(1));
            }

            responseHandle = new ResponseHandle("tables", "list of tables", new JSONObject().put("tables", tablesArr));
            HTTPStatusCode = 200;
            json_arr.put(responseHandle.getResponseJSON());

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

    public String setNewToken() {
        JSONArray jsonArray = new JSONArray();
        String SQL = "UPDATE restaurant_tables SET waiter_id =?,token=? WHERE restaurant_id=? AND table_name=?";
        try {
            prepStmt = con.prepareStatement(SQL);
            prepStmt.setInt(1, waiterId);
            prepStmt.setString(2, token);
            prepStmt.setInt(3, restaurantID);
            prepStmt.setString(4, tableName);

            int i = prepStmt.executeUpdate();

            if (i == 1) {
                JSONObject content = new JSONObject();
                content.put("restaurant_name", restaurantUname);
                content.put("table_name", tableName);
                content.put("token", token);
                responseHandle = new ResponseHandle("token", "success setting token", content);
                HTTPStatusCode = 200;
            } else {
                responseHandle = new ResponseHandle("token", "failed setting token", "");
                HTTPStatusCode = 401;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            responseHandle = new ResponseHandle("token", "failed setting token", "");
            HTTPStatusCode = 500;
        } finally {
            jsonArray.put(responseHandle.getResponseJSON());
        }
        return jsonArray.toString();
    }

    public String getOrdersOfTheTable() {
        JSONArray jsonArray = new JSONArray();
        ResultSet rs = null;
        String SQL = "SELECT ordered.item_no,ordered.quantity,menu.item_name,menu.item_price,ordered.timestamp_of_order " +
                "FROM orders " +
                "AS ordered, " +
                "(SELECT restaurant_id, item_no, item_name, item_price " +
                "FROM menuitems) " +
                "AS menu " +
                "WHERE menu.restaurant_id = ordered.restaurant_id " +
                "AND menu.item_no = ordered.item_no " +
                "AND ordered.restaurant_id = ? " +
                "AND ordered.table_name = ?";

        try {
            prepStmt = con.prepareStatement(SQL);
            prepStmt.setInt(1,restaurantID);
            prepStmt.setString(2,tableName);

            rs = prepStmt.executeQuery();

            JSONArray tablesArr = new JSONArray();
            DecimalFormat df = new DecimalFormat("#.00");
            while (rs.next()) {
                JSONObject singleOrderItem = new JSONObject();
                singleOrderItem.put("item_no",rs.getString(1));
                singleOrderItem.put("quantity",rs.getInt(2));
                singleOrderItem.put("name",rs.getString(3));
                singleOrderItem.put("price",df.format(rs.getFloat(4)));
                singleOrderItem.put("time",rs.getString(5));
                tablesArr.put(singleOrderItem);
            }

            responseHandle = new ResponseHandle("orders", "orders for table-"+tableName, new JSONObject().put("ordered_items", tablesArr));
            HTTPStatusCode = 200;


        } catch (SQLException e) {
            e.printStackTrace();
            responseHandle = new ResponseHandle("token", "failed setting token", "");
            HTTPStatusCode = 500;
        } finally {
            jsonArray.put(responseHandle.getResponseJSON());
        }
        return jsonArray.toString();
    }
}
