// @formatter:off
package com.everhomes.discover.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.RequireAuthentication;
import com.everhomes.controller.ControllerBase;
import com.everhomes.rest.RestResponse;

/**
 * API discover for JSON responses
 * 
 * @author Kelven Yang
 *
 */
@RestController
public class ApiDiscoverRestController extends ControllerBase {

    @RequestMapping("/discover")
    @RequireAuthentication(false)
    public RestResponse apiDiscover(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("Content-Type", "application/javascript; charset=utf-8");
        return new RestResponse(ControllerBase.getRestMethodList());
    }
}
