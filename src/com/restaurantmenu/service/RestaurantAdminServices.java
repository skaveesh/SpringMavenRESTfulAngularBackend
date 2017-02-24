package com.restaurantmenu.service;

import com.restaurantmenu.configuration.Constance;
import com.restaurantmenu.user.RestaurantAdmin;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.*;

import javax.annotation.security.DeclareRoles;
import  javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by Samintha on 2017-01-29.
 */

@Path("/restaurantadmin/service")
public class RestaurantAdminServices {

    @POST
    @Path("/login")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public Response loginRestaurantAdmin(@FormParam("username") String userName,
                                         @FormParam("password") String userPass){

        RestaurantAdmin restAdminSrvLogin = new RestaurantAdmin(userName);
        restAdminSrvLogin.setPassword(userPass);

        String returnStr = restAdminSrvLogin.loginRestaurantAdmin();
        restAdminSrvLogin.disconnectDB();

        return Response.status(restAdminSrvLogin.getHTTPStatusCode())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "POST")
                .header("Access-Control-Max-Age", "600000")
                .entity(returnStr).build();
    }

    @POST
    @Path("/register")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public Response registerRestaurantAdmin(@FormParam("username") String userName,
                                            @FormParam("restaurantname") String restaurantName,
                                            @FormParam("password") String password,
                                            @FormParam("email") String email,
                                            @FormParam("adminFname") String adminFname,
                                            @FormParam("adminLname") String adminLname,
                                            @FormParam("contactToDisplay") String contactToDisplay,
                                            @FormParam("emailToDisplay") String emailToDisplay){


        RestaurantAdmin restAdminSrvRegister = new RestaurantAdmin(userName);
        restAdminSrvRegister.setRestaurantName(restaurantName);
        restAdminSrvRegister.setPassword(password);
        restAdminSrvRegister.setEmail(email);
        restAdminSrvRegister.setAdminFname(adminFname);
        restAdminSrvRegister.setAdminLname(adminLname);
        restAdminSrvRegister.setContatctDisplay(contactToDisplay);
        restAdminSrvRegister.setEmailDisplay(emailToDisplay);

        String returnStr = restAdminSrvRegister.registerRestaurantAdmin();
        restAdminSrvRegister.disconnectDB();

        return Response.status(restAdminSrvRegister.getHTTPStatusCode())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET")
                .header("Access-Control-Max-Age", "600000")
                .entity(returnStr).build();
    }
}
