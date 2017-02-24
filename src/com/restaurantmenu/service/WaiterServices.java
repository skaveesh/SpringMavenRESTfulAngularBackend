package com.restaurantmenu.service;

import com.restaurantmenu.configuration.Constance;
import com.restaurantmenu.responsehandle.ResponseHandle;
import com.restaurantmenu.user.RestaurantAdmin;
import com.restaurantmenu.user.Waiter;
import io.jsonwebtoken.*;
import org.json.JSONArray;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Date;

/**
 * Created by samintha on 2/21/2017.
 */

@Path("/restaurantwaiter/service")
public class WaiterServices {

    @POST
    @Path("/login")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public Response loginRestaurantAdmin(@FormParam("username") String userName,
                                         @FormParam("password") String userPass) {

        Waiter restWaiterLogin = new Waiter(userName);
        restWaiterLogin.setPassword(userPass);

        String returnStr = restWaiterLogin.loginRestaurantWaiter();
        restWaiterLogin.disconnectDB();

        return Response.status(restWaiterLogin.getHTTPStatusCode())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "POST")
                .header("Access-Control-Max-Age", "600000")
                .entity(returnStr).build();
    }

    @POST
    @Path("/gettables")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public Response getTablesOfRestaurant(@FormParam("auth_token") String authToken, @FormParam("username") String userName) {

        String returnStr;
        int returnStatusCode;
        ResponseHandle responseHandle;
        try {
            Jws<Claims> claims = Jwts.parser()
                    .requireIssuer("Login for Restaurant Waiter")
                    .setSigningKey(Constance.AUTHORIZATION_PASSWORD)
                    .parseClaimsJws(authToken);

            if (userName.equals(claims.getBody().get("username").toString()) && new Date().before(claims.getBody().getExpiration())) {
                Waiter waiter = new Waiter(userName);
                returnStr = waiter.getTables();
                returnStatusCode = waiter.getHTTPStatusCode();
                waiter.disconnectDB();
            } else {
                throw new Exception("error token");
            }
        } catch (Exception e) {
            responseHandle = new ResponseHandle("authentication", "authorization failed", "login again");
            JSONArray json_arr = new JSONArray();
            json_arr.put(responseHandle.getResponseJSON());
            returnStr = json_arr.toString();
            returnStatusCode = 401;
        }


        return Response.status(returnStatusCode)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "POST")
                .header("Access-Control-Max-Age", "600000")
                .entity(returnStr).build();
    }
}
