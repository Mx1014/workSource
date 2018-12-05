package com.everhomes.openapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.community.CommunityService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.aclink.OpenQueryLogResponse;
import com.everhomes.rest.openapi.ListAddressesForThirdPartyCommand;
import com.everhomes.rest.openapi.ListAddressesForThirdPartyResponse;
import com.everhomes.rest.openapi.ListBuildingsForThirdPartyCommand;
import com.everhomes.rest.openapi.ListBuildingsForThirdPartyResponse;
import com.everhomes.rest.openapi.ListCommunitiesForThirdPartyCommand;
import com.everhomes.rest.openapi.ListCommunitiesForThirdPartyResponse;

@RestDoc(value="property open Controller", site="core")
@RestController
@RequestMapping("/openapi/property")
public class ZTPropertyController extends ControllerBase{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ZTPropertyController.class);
	
	@Autowired
	private CommunityService communityService;
	
	/**
     * <b>URL: /openapi/property/listCommunities</b>
     * <p>查询园区信息</p>
     * @return
     */
    @RequestMapping("listCommunities")
    @RestReturn(ListCommunitiesForThirdPartyResponse.class)
    public RestResponse listCommunities(ListCommunitiesForThirdPartyCommand cmd){
    	ListCommunitiesForThirdPartyResponse result = communityService.listCommunitiesForThirdParty(cmd);
    	RestResponse response = new RestResponse(result);
    	response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /openapi/property/listBuildings</b>
     * <p>查询楼宇信息</p>
     * @return
     */
    @RequestMapping("listBuildings")
    @RestReturn(ListBuildingsForThirdPartyResponse.class)
    public RestResponse listBuildings(ListBuildingsForThirdPartyCommand cmd){
    	ListBuildingsForThirdPartyResponse result = communityService.listBuildingsForThirdParty(cmd);
    	RestResponse response = new RestResponse(result);
    	response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /openapi/property/listAddresses</b>
     * <p>查询房源信息</p>
     * @return
     */
    @RequestMapping("listAddresses")
    @RestReturn(ListAddressesForThirdPartyResponse.class)
    public RestResponse listAddresses(ListAddressesForThirdPartyCommand cmd){
    	ListAddressesForThirdPartyResponse result = communityService.listAddressesForThirdParty(cmd);
    	RestResponse response = new RestResponse(result);
    	response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
