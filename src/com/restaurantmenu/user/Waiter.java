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
import java.util.*;

/**
 * Created by samintha on 2/21/2017.
 */
public class Waiter extends Restaurant {

    private String waiterUname;
    private String password;
    private String waiterName;

    public Waiter(String waiterUname) {
        this.connectDB();
        this.waiterUname = waiterUname;
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
                json_arr.put(responseHandle.getResponseJSON());
                HTTPStatusCode = 401;
            } else {
                while (rs.next()) {
                    if (BCrypt.checkpw(password, rs.getString(1))) {

                        //making a new token for authorization this waiter in next time
                        Map<String,Object> claimmap= new HashMap<String,Object>();
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
                    json_arr.put(responseHandle.getResponseJSON());
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

            responseHandle = new ResponseHandle("tables", "list of tables", new JSONObject().put("tables",tablesArr));
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
}
