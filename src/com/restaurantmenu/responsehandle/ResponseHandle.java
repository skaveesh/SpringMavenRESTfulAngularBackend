package com.restaurantmenu.responsehandle;

import org.json.JSONObject;

/**
 * Created by samintha on 2/15/2017.
 */
public class ResponseHandle {
    private String message;
    private Object content;
    private String reason;

    public ResponseHandle(String message, String reason, Object content) {
        this.message = message;
        this.content = content;
        this.reason = reason;
    }

    public ResponseHandle(){
        //server error message
        this.message = "server error";
        this.content = "server side error";
        this.reason = "";
    }

    public JSONObject getResponseJSON(){
        JSONObject errorObject = new JSONObject();

        errorObject.put("message", this.message);
        errorObject.put("content", this.content);
        errorObject.put("reason", this.reason);

        return errorObject;
    }
}
