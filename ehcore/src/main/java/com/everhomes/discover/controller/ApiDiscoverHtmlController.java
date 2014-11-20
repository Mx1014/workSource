package com.everhomes.discover.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.everhomes.controller.ControllerBase;
import com.everhomes.controller.RequireAuthentication;

@Controller
public class ApiDiscoverHtmlController extends ControllerBase {
    @RequestMapping("/api/**")
    @RequireAuthentication(false)
    public String apiDiscover(HttpServletRequest request, Model model) {
        String uri = request.getRequestURI().toString();
        if(uri.startsWith("/api"))
            uri = uri.substring(4);

        if(uri.isEmpty() || uri.equals("**")) {
            model.addAttribute("restMethods", ControllerBase.getRestMethodList());
            return "api-list"; 
        } else {
            model.addAttribute("restMethod", ControllerBase.getRestMethod(uri));
            return "api-input";
        }
    }
}
