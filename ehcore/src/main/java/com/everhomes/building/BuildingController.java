package com.everhomes.building;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

@RestController
@RequestMapping("/building")
public class BuildingController extends ControllerBase {
	
	@Autowired
	private BuildingService buildingService;
	
	/**
	 * <b>URL: /building/listBuildings</b>
	 * <p>根据园区号查询楼栋列表</p>
	 */
	@RequestMapping("listBuildings")
    @RestReturn(value=ListBuildingCommandResponse.class)
	public RestResponse listBuildings(ListBuildingCommand cmd) {
		
		ListBuildingCommandResponse buildings = buildingService.listBuildings(cmd);
		RestResponse response =  new RestResponse(buildings);

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}
	
	/**
	 * <b>URL: /building/getBuilding</b>
	 * <p>查询指定园区内指定楼栋详情</p>
	 */
	@RequestMapping("getBuilding")
    @RestReturn(value=BuildingDTO.class, collection=true)
	public RestResponse getBuilding(GetBuildingCommand cmd) {
		BuildingDTO dto = buildingService.getBuilding(cmd);
		RestResponse response =  new RestResponse(dto);

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}

	
}
