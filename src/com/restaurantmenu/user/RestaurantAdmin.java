package com.restaurantmenu.user;

import com.restaurantmenu.configuration.Constance;
import com.restaurantmenu.responsehandle.ResponseHandle;
import com.restaurantmenu.restaurant.Restaurant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.compression.CompressionCodecs;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


public class RestaurantAdmin extends Restaurant {

    private int restaurantId;
    private String password;
    private String email;
    private String adminFname;
    private String adminLname;
    private String contatctDisplay;
    private String emailDisplay;

    //initialize connection to database from super class
    public RestaurantAdmin(String restaurantUname) {
        this.connectDB();
        this.restaurantUname = restaurantUname;
        setRestaurantIDFromUname("SELECT restaurant_id FROM restaurant_admins WHERE restaurant_uname=? LIMIT 1",this.restaurantUname);
    }

    public String getRestaurantUname() {
        return restaurantUname;
    }

    public void setRestaurantUname() {

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

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = Integer.parseInt(restaurantId);
    }


    public String loginRestaurantAdmin() {
        JSONArray json_arr = new JSONArray();
        ResultSet rs = null;
        String SQL = "SELECT password FROM restaurant_admins WHERE restaurant_uname=?";
        try {
            prepStmt = con.prepareStatement(SQL);
            prepStmt.setString(1, restaurantUname);

            rs = prepStmt.executeQuery();
            if (!rs.isBeforeFirst()) {
                //if the restaurant_uname is incorrect it will not be authenticated
                responseHandle = new ResponseHandle("authentication", "authentication failed", "username or password incorrect");
                json_arr.put(responseHandle.getResponseJSON());
                HTTPStatusCode = 401;
            } else {
                while (rs.next()) {
                    if (BCrypt.checkpw(password, rs.getString(1))) {

                        String tokenJJWT = Jwts.builder()
                                .compressWith(CompressionCodecs.DEFLATE)
                                .setIssuer("Login for Restaurant Admin")
                                .claim("username",restaurantUname)
                                .setIssuedAt(new Date())
                                .setExpiration(new Date(new Date().getTime()+7*24*60*60*1000)) //set expiration to week
                                .signWith(SignatureAlgorithm.HS512, Constance.AUTHORIZATION_PASSWORD)
                                .compact();

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

    public String registerRestaurantAdmin() {
        JSONArray json_arr = new JSONArray();
        if (isAlreadyExists("SELECT * FROM restaurant_admins WHERE restaurant_uname=? LIMIT 1",restaurantUname)) {
            responseHandle = new ResponseHandle("registration", "registration failed", "email already exists");
            json_arr.put(responseHandle.getResponseJSON());
            HTTPStatusCode = 403;
        } else if (isAlreadyExists("SELECT * FROM restaurant_admins WHERE email=? LIMIT 1", email)) {
            responseHandle = new ResponseHandle("registration", "registration failed", "username already exists");
            json_arr.put(responseHandle.getResponseJSON());
            HTTPStatusCode = 403;
        } else {
            String SQL = "INSERT INTO `restaurant_admins`(`restaurant_uname`, `restaurant_name`, `password`, `email`, `admin_fname`, `admin_lname`, `contact_to_display`, `email_to_display`) VALUES (?,?,?,?,?,?,?,?)";
            try {
                prepStmt = con.prepareStatement(SQL);
                prepStmt.setString(1, restaurantUname);
                prepStmt.setString(2, restaurantName);
                prepStmt.setString(3, BCrypt.hashpw(password, BCrypt.gensalt())); //encrypting password
                prepStmt.setString(4, email);
                prepStmt.setString(5, adminFname);
                prepStmt.setString(6, adminLname);
                prepStmt.setString(7, contatctDisplay);
                prepStmt.setString(8, emailDisplay);

                prepStmt.executeUpdate();
                responseHandle = new ResponseHandle("registration", "registration success", "");
                json_arr.put(responseHandle.getResponseJSON());
                HTTPStatusCode = 201;
            } catch (SQLException e) {
                e.printStackTrace();
                responseHandle = new ResponseHandle("registration", "registration failed", "");
                json_arr.put(responseHandle.getResponseJSON());
                HTTPStatusCode = 403;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return json_arr.toString();
    }

    private boolean isAlreadyExists(String SQLQuery, String valueToCheck) {
        boolean alreadyExists = false;

        ResultSet rs = null;
        try {
            prepStmt = con.prepareStatement(SQLQuery);
            prepStmt.setString(1, valueToCheck);

            rs = prepStmt.executeQuery();
            alreadyExists = ((!rs.isBeforeFirst()) ? false : true);
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
        return alreadyExists;
    }
}
