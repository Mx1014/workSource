package com.everhomes.building.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.rest.RestResponse;

@RestController
@RequestMapping("/admin/building")
public class BuildingAdminController extends ControllerBase {

	public RestResponse addBuilding() {
		RestResponse response =  new RestResponse();

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}
	
	public RestResponse deleteBuilding() {
		RestResponse response =  new RestResponse();

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}
	
	public RestResponse updateBuilding() {
		RestResponse response =  new RestResponse();

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}
	
}
