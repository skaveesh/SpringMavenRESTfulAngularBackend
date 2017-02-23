package com.restaurantmenu.service;

import com.restaurantmenu.user.Customer;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by samintha on 2/1/2017.
 */

@Path("/customers/service")
public class CustomerServices {

    @GET
    @Path("/menu/{restaurantuname}/{tablename}/{token}")
    @Produces("application/json")
    public Response getMenuItems(@PathParam("restaurantuname") String restaurantUName,
                                 @PathParam("tablename") String tableName,
                                 @PathParam("token") String token) {

        Customer customerSrvAuthToken = new Customer(restaurantUName);
        customerSrvAuthToken.setTableName(tableName);
        customerSrvAuthToken.setToken(token);

        String returnStr = customerSrvAuthToken.authenticateCustomerAndGetMenu();
        customerSrvAuthToken.disconnectDB();

        return Response.status(customerSrvAuthToken.getHTTPStatusCode())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET")
                .header("Access-Control-Max-Age", "600000")
                .entity(returnStr).build();
    }

    @GET
    @Path("/restaurantdetails/{restaurantuname}")
    @Produces("application/json")
    public Response getRestaurantDetails(@PathParam("restaurantuname") String restaurantUName) {

        Customer customerGetRestaurantDetails = new Customer(restaurantUName);

        String returnStr = customerGetRestaurantDetails.getRestaurantDetails();
        customerGetRestaurantDetails.disconnectDB();

        return Response.status(customerGetRestaurantDetails.getHTTPStatusCode())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET")
                .header("Access-Control-Max-Age", "600000")
                .entity(returnStr).build();
    }
}
