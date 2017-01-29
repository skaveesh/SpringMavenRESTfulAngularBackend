package com.hotelmenu.user;

import  javax.ws.rs.*;
/**
 * Created by Samintha on 2017-01-29.
 */

@Path("/restaurantadmin/service")
public class RestaurantAdminServicesREST {
    @GET
    public void getRestaurantAdminCredentials(){
        RestaurantAdminServices restAdminSrv = new RestaurantAdminServices();
        restAdminSrv.connectDB();
        restAdminSrv.setRestaurantUname("");
        String returnStr = restAdminSrv.getRestaurantCredentials();
        restAdminSrv.disconnectDB();
    }
}
