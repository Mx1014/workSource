package com.everhomes.discover;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.RequireAuthentication;
import com.everhomes.controller.RestControllerBase;
import com.everhomes.rest.RestResponse;

@RestController
public class DiscoverController extends RestControllerBase {

    @RequestMapping("/discover")
    @RequireAuthentication(false)
    public RestResponse apiDiscover() {
        return new RestResponse(RestControllerBase.getRestMethodList());
    }
}
