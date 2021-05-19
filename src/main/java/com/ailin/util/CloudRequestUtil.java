package com.ailin.util;


import com.ailin.mode.CloudRequest;

import javax.servlet.http.HttpServletRequest;

public class CloudRequestUtil {

    public static CloudRequest getCloudRequest(HttpServletRequest req){
        CloudRequest ret = new CloudRequest();
        ret.setToken(req.getParameter("token"));
        ret.setIP(req.getParameter("IP"));
        ret.setOsType(req.getParameter("os"));
        ret.setUserAgent(req.getParameter("userAgent"));
        ret.setUrl(req.getParameter("url"));
        ret.setFunctionCode(req.getParameter("functionCode"));
        ret.setPackageName(req.getParameter("packageName"));
        return ret;
    }

}
