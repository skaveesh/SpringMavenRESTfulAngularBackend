package com.hotelmenu.user;

import org.springframework.security.crypto.bcrypt.BCrypt;

import  javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by Samintha on 2017-01-29.
 */

@Path("/restaurantadmin/service")
public class RestaurantAdminServicesREST {

    @PUT
    @Path("/put")
    public void putdata(){
        System.out.println("this is inside put method");
    }

    @GET
    public void getdet() {
        System.out.println("this is inside getdet method ");
        //return Response.status(200).entity("getUserById is called, id : ").build();
    }

    @POST
    @Path("/login")
    public Response loginRestaurantAdmin(@FormParam("username") String userName,
                                         @FormParam("password") String userPass){

        RestaurantAdminServices restAdminSrvLogin = new RestaurantAdminServices();
        restAdminSrvLogin.setRestaurantUname(userName);
        restAdminSrvLogin.setPassword(userPass);

        String returnStr = restAdminSrvLogin.getRestaurantCredentials();
        restAdminSrvLogin.disconnectDB();

        return Response.status(restAdminSrvLogin.getHTTPStatusCode()).entity(returnStr).build();
    }

    @POST
    @Path("/register")
    public Response registerRestaurantAdmin(@FormParam("username") String userName,
                                            @FormParam("restaurantname") String restaurantName,
                                            @FormParam("password") String password,
                                            @FormParam("email") String email,
                                            @FormParam("adminFname") String adminFname,
                                            @FormParam("adminLname") String adminLname,
                                            @FormParam("contactToDisplay") String contactToDisplay,
                                            @FormParam("emailToDisplay") String emailToDisplay){

        RestaurantAdminServices restAdminSrvRegister = new RestaurantAdminServices();
        restAdminSrvRegister.setRestaurantUname(userName);
        restAdminSrvRegister.setRestaurantName(restaurantName);
        restAdminSrvRegister.setPassword(password);
        restAdminSrvRegister.setEmail(email);
        restAdminSrvRegister.setAdminFname(adminFname);
        restAdminSrvRegister.setAdminLname(adminLname);
        restAdminSrvRegister.setContatctDisplay(contactToDisplay);
        restAdminSrvRegister.setEmailDisplay(emailToDisplay);

        String returnStr = restAdminSrvRegister.registerRestaurantAdmin();
        restAdminSrvRegister.disconnectDB();

        return Response.status(restAdminSrvRegister.getHTTPStatusCode()).entity(returnStr).build();
    }
}
