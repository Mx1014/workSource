// @formatter:off
package com.everhomes.discover.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.everhomes.controller.ControllerBase;
import com.everhomes.controller.RequireAuthentication;

/**
 * 
 * API listing and API test controller
 * 
 * @author Kelven Yang
 *
 */
@Controller
public class ApiDiscoverHtmlController extends ControllerBase {
    @RequestMapping("/api/**")
    @RequireAuthentication(false)
    public String apiDiscover(HttpServletRequest request, HttpServletResponse response, Model model) {
        response.addHeader("Content-Type", "text/html; charset=utf-8");
        String uri = request.getRequestURI().toString();
        if(uri.startsWith("/api"))
            uri = uri.substring(4);

        if(uri.isEmpty() || uri.equals("**") || uri.equals("/") || uri.equals("/*")) {
            model.addAttribute("restMethods", ControllerBase.getRestMethodList());
            return "api-list"; 
        } else {
            model.addAttribute("restMethod", ControllerBase.getRestMethod(uri));
            return "api-input";
        }
    }
}
