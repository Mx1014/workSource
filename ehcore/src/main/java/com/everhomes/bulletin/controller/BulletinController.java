package com.everhomes.bulletin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

@RestController
@RequestMapping("/bulletin")
public class BulletinController extends ControllerBase {
    
    @RequestMapping("requestAdminRole")
    @RestReturn(value=String.class)
    public RestResponse requestAdminRole(@RequestParam(value = "communityId", required = true) Long communityId) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("resignAdminRole")
    @RestReturn(value=String.class)
    public RestResponse reignAdminRole(@RequestParam(value = "communityId", required = true) Long communityId) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("getAdminRoleStatus")
    @RestReturn(value=Byte.class)
    public RestResponse getAdminRoleStatus(@RequestParam(value = "communityId", required = true) Long communityId) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
