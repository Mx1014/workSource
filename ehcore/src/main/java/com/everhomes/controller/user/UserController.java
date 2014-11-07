package com.everhomes.controller.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.rest.RestResponse;

@RestController
@RequestMapping("/user")
public class UserController {
    @RequestMapping("logon")
    public RestResponse logon(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = request.getSession().getId();
        
        String obj = "OK, session id: " + sessionId;
        return new RestResponse(obj);
    }
    
    @RequestMapping("logoff")
    public String logoff(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();;
        return "OK, invalidated";
    }
}
