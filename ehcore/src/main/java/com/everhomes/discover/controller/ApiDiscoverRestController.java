package com.everhomes.discover.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.RequireAuthentication;
import com.everhomes.controller.ControllerBase;
import com.everhomes.rest.RestResponse;

@RestController
public class ApiDiscoverRestController extends ControllerBase {

    @RequestMapping("/discover")
    @RequireAuthentication(false)
    public RestResponse apiDiscover() {
        return new RestResponse(ControllerBase.getRestMethodList());
    }
}
