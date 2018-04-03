// @formatter:off
package com.everhomes.activity;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.util.RequireAuthentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/activityTest")
public class ActivityTestController extends ControllerBase {

    // test
    static String name = "myTestToken";

    /**
     * <b>URL: /activityTest/addCookie</b>
     */
    @RequestMapping("addCookie")
    @RestReturn(value=String.class)
    @RequireAuthentication(false)
    public RestResponse addCookie(HttpServletRequest request, HttpServletResponse response){
        String value = String.valueOf(System.currentTimeMillis());
        Cookie cookie = findCookieInRequest(name, request);
        if(cookie == null)
            cookie = new Cookie(name, value);
        else
            cookie.setValue(value);
        cookie.setPath("/");
        cookie.setMaxAge(10*365*24*60*60*1000);
        if(value == null || value.isEmpty())
            cookie.setMaxAge(0);
        response.addCookie(cookie);
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }

    private static Cookie findCookieInRequest(String name, HttpServletRequest request) {
        List<Cookie> matchedCookies = new ArrayList<>();

        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals(name)) {
                    matchedCookies.add(cookie);
                }
            }
        }

        if(matchedCookies.size() > 0)
            return matchedCookies.get(matchedCookies.size() - 1);
        return null;
    }

    /**
     * <b>URL: /activityTest/testCookie</b>
     */
    @RequestMapping("testCookie")
    @RequireAuthentication(false)
    @RestReturn(value=String.class)
    public RestResponse testCookie(HttpServletRequest request){
        Cookie cookie = findCookieInRequest(name, request);
        if(cookie == null){
            System.out.println("cookie is null");
        }else{
            System.out.println("cookie name: " + cookie.getName() + "cookie value: " +  cookie.getValue());
        }

        RestResponse res = new RestResponse("cookie value: " + cookie.getValue());
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }


}
