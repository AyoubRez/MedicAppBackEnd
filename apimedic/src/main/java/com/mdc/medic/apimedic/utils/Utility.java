package com.mdc.medic.apimedic.utils;

import javax.servlet.http.HttpServletRequest;

public class Utility {
    public static String getAppUrl(HttpServletRequest request){
        String appUrl = request.getRequestURL().toString();
        return appUrl.replace(request.getServletPath(),"");
    }
}
