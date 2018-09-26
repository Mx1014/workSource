// @formatter:off
package com.everhomes.discover;

import com.everhomes.controller.ControllerBase;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * API listing and API test controller
 * 
 * @author Kelven Yang
 *
 */
@Controller
@RestDoc(value="API discover HTML controller", site="messaging")
public class ApiDiscoverHtmlController extends ControllerBase {
    
    @Value("${javadoc.root}")
    private String javadocRoot;
    
    @SuppressDiscover
    @RequestMapping("/api/**")
    @RequireAuthentication(false)
    public String apiDiscover(HttpServletRequest request, HttpServletResponse response, Model model) {
        
        response.addHeader("Content-Type", "text/html; charset=utf-8");
        String uri = request.getRequestURI().toString();
        
        int pos = uri.indexOf("/api");
        uri = uri.substring(pos + 4);

        if(uri.isEmpty() || uri.equals("**") || uri.equals("/") || uri.equals("/*")) {
            if(request.getParameter("index") != null) 
                model.addAttribute("index", request.getParameter("index"));
            else
                model.addAttribute("index", "0");
                
            model.addAttribute("restMethods", ControllerBase.getRestMethodList(javadocRoot, "core"));
            model.addAttribute("javadocRoot", javadocRoot);
            
            String contextPath = request.getContextPath();
            model.addAttribute("contextPath", contextPath);            
            
            return "api-index"; 
        } else {
            model.addAttribute("restMethod", ControllerBase.getRestMethod(uri));
            model.addAttribute("javadocRoot", javadocRoot);
            
            String contextPath = request.getContextPath();
            model.addAttribute("contextPath", contextPath);            
            return "api-input";
        }

      /* String uri = request.getRequestURI().toString();
       int pos = uri.indexOf("/api");
       if(uri.substring(pos + 4).length() > 0) {
           return "redirect:/api";
       }
        
       return "api-react";*/
    }
    
    @SuppressDiscover
    @RequestMapping("/api-menu")
    @RequireAuthentication(false)
    public String apiIndex(HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("restMethods", ControllerBase.getRestMethodList(javadocRoot, "core"));
        return "api-menu";
    }
    
    @SuppressDiscover
    @RequestMapping("/api-panel")
    @RequireAuthentication(false)
    public String apiPanel(HttpServletRequest request, HttpServletResponse response, Model model) {
        int methodIndex = 0;
        String index = request.getParameter("index");
        if(index != null)
            methodIndex = Integer.parseInt(index);
        
        model.addAttribute("restMethod", ControllerBase.getRestMethodList(javadocRoot, "core").get(methodIndex));
        model.addAttribute("javadocRoot", javadocRoot);
        
        String contextPath = request.getContextPath();
        model.addAttribute("contextPath", contextPath);
        
        return "api-input";
    }
}
