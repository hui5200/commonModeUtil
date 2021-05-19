package com.ailin.contoller;

import com.ailin.mode.CloudRequest;
import com.ailin.util.CloudRequestUtil;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    public HttpServletRequest getRequest(){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        assert ra != null;
        return ((ServletRequestAttributes) ra).getRequest();
    }

    public CloudRequest getCloudRequest(){

        return CloudRequestUtil.getCloudRequest(getRequest());
    }
}
