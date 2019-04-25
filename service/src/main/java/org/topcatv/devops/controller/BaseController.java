package org.topcatv.devops.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

/**
 * @author liuyi
 */
public class BaseController {

    static final String MEDIA_TYPE = MediaType.APPLICATION_JSON_UTF8_VALUE;
    protected Logger logger;

    public BaseController() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    protected String objectResult(Object object) {
        JSONObject root = new JSONObject();
        root.put("success", true);
        root.put("data", object);
        root.put("status", 200);
        return root.toString();
    }

    protected String errorMessage(String message) {
        return errorMessage(message, -1);
    }

    protected String errorMessage(String message, int status) {
        JSONObject root = new JSONObject();
        root.put("success", false);
        root.put("message", message);
        root.put("status", status);
        return root.toString();
    }

}
