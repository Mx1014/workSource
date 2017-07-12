// @formatter:off
package com.everhomes.discover;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.util.RequireAuthentication;
import com.everhomes.controller.ControllerBase;
import com.everhomes.rest.RestResponse;

/**
 * API discover for JSON responses
 * 
 * @author Kelven Yang
 *
 */
@RestDoc(value="API discover REST controller", site="messaging")
@RestController
public class ApiDiscoverRestController extends ControllerBase {

    @Value("${javadoc.root}")
    private String javadocRoot;

    @RequestMapping("/discover")
    @RequireAuthentication(false)
    public RestResponse apiDiscover(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("Content-Type", "application/javascript; charset=utf-8");
        return new RestResponse(ControllerBase.getRestMethodList(javadocRoot, "core"));
    }
}
