package com.hotelmenu.user;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCrypt;

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

    private int HTTPStatusCode;


    //initialize connection to database from super class
    public RestaurantAdminServices() {
        this.connectDB();
    }

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

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = Integer.parseInt(restaurantId);
    }

    public int getHTTPStatusCode() {
        return HTTPStatusCode;
    }

    public String getRestaurantCredentials() {
        JSONArray json_arr = new JSONArray();
        ResultSet rs = null;
        String SQL = "SELECT password FROM restaurant_admins WHERE restaurant_uname=?";
        try {
            prepStmt = con.prepareStatement(SQL);
            prepStmt.setString(1, restaurantUname);

            rs = prepStmt.executeQuery();
            if (!rs.isBeforeFirst()) {
                //if the restaurant_uname is incorrect it will not be authenticated
                JSONObject json = new JSONObject();
                json.put("authenticated", false);
                json_arr.put(json);
                HTTPStatusCode = 401;
            } else {
                while (rs.next()) {
                    JSONObject json = new JSONObject();
                    if (BCrypt.checkpw(password, rs.getString(1))) {
                        json.put("authenticated", true);
                        HTTPStatusCode = 200;
                    } else {
                        json.put("authenticated", false);
                        HTTPStatusCode = 401;
                    }
                    json_arr.put(json);
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
        JSONObject json = new JSONObject();
        if (isEmailAlreadyExists()) {
            json.put("registered-successfully", false);
            json.put("email-already-exists", true);
            json_arr.put(json);
            HTTPStatusCode = 403;
        } else if (isUsernameAlreadyExists()) {
            json.put("registered-successfully", false);
            json.put("username-already-exists", true);
            json_arr.put(json);
            HTTPStatusCode = 403;
        } else {
            String SQL = "INSERT INTO `restaurant_admins`(`restaurant_uname`, `restaurant_name`, `password`, `email`, `admin_fname`, `admin_lname`, `contact_to_display`, `email_to_display`) VALUES (?,?,?,?,?,?,?,?)";
            try {
                prepStmt = con.prepareStatement(SQL);
                prepStmt.setString(1, restaurantUname);
                prepStmt.setString(2, restaurantName);
                prepStmt.setString(3, BCrypt.hashpw(password,BCrypt.gensalt())); //encrypting password
                prepStmt.setString(4, email);
                prepStmt.setString(5, adminFname);
                prepStmt.setString(6, adminLname);
                prepStmt.setString(7, contatctDisplay);
                prepStmt.setString(8, emailDisplay);

                prepStmt.executeUpdate();
                json.put("registered-successfully", true);
                json_arr.put(json);
                HTTPStatusCode = 201;
            } catch (SQLException e) {
                e.printStackTrace();
                json.put("registered-successfully", false);
                json_arr.put(json);
                HTTPStatusCode = 403;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return json_arr.toString();
    }

    public boolean isUsernameAlreadyExists() {
        boolean alreadyExists = false;

        ResultSet rs = null;
        String SQL = "SELECT * FROM restaurant_admins WHERE restaurant_uname=? LIMIT 1";
        try {
            prepStmt = con.prepareStatement(SQL);
            prepStmt.setString(1, restaurantUname);

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

    public boolean isEmailAlreadyExists() {
        boolean alreadyExists = false;

        ResultSet rs = null;
        String SQL = "SELECT * FROM restaurant_admins WHERE email=? LIMIT 1";
        try {
            prepStmt = con.prepareStatement(SQL);
            prepStmt.setString(1, email);

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
