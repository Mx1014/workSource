package com.everhomes.community;



import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.community.BuildingDTO;
import com.everhomes.rest.community.GetBuildingCommand;
import com.everhomes.rest.community.GetCommunitiesByIdsCommand;
import com.everhomes.rest.community.GetCommunitiesByNameAndCityIdCommand;
import com.everhomes.rest.community.GetCommunityByIdCommand;
import com.everhomes.rest.community.ListBuildingCommand;
import com.everhomes.rest.community.ListBuildingCommandResponse;
import com.everhomes.rest.community.ListCommunitiesByKeywordCommandResponse;
import com.everhomes.rest.community.UpdateCommunityRequestStatusCommand;
import com.everhomes.rest.community.admin.CommunityUserDto;
import com.everhomes.rest.community.admin.CommunityUserResponse;
import com.everhomes.rest.community.admin.CountCommunityUserResponse;
import com.everhomes.rest.community.admin.CountCommunityUsersCommand;
import com.everhomes.rest.community.admin.ListCommunityUsersCommand;
import com.everhomes.rest.community.admin.ListComunitiesByKeywordAdminCommand;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.EtagHelper;

/**
 * 园区：含住宅小区（即平时所说的小区）和商用园区（如科技园）
 */
@RestDoc(value="Community controller", site="core")
@RestController
@RequestMapping("/community")
public class CommunityController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityController.class);
    
    @Autowired
    private CommunityService communityService;
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    /**
     * <b>URL: /community/get</b>
     * <p>根据园区ID获取园区信息</p>
     */
    @RequestMapping("get")
    @RestReturn(value=CommunityDTO.class)
    public RestResponse getCommunityById(GetCommunityByIdCommand cmd, HttpServletRequest request, HttpServletResponse paramResponse) {
        CommunityDTO community = this.communityService.getCommunityById(cmd);

        RestResponse response =  new RestResponse();
        
        Timestamp etagTime = community.getUpdateTime();
        if(etagTime != null) {
            etagTime = community.getUpdateTime();
            if(etagTime == null) {
                etagTime = community.getCreateTime();
            }
        }
        
        if(etagTime != null) {
            int interval = configurationProvider.getIntValue(AppConstants.DEFAULT_ETAG_TIMEOUT_KEY, 
                AppConstants.DEFAULT_ETAG_TIMEOUT_SECONDS);
            if(EtagHelper.checkHeaderCache(interval, etagTime.getTime(), request, paramResponse)) {
                response.setResponseObject(community);
            }
        } else {
            if(LOGGER.isWarnEnabled()) {
                LOGGER.warn("No update time or create time in the community, cmd=" + cmd);
            }
            response.setResponseObject(community);
        }
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /community/getCommunitiesByNameAndCityId</b>
     * <p>根据小区名称和城市id搜索小区</p>
     */
    @RequestMapping("getCommunitiesByNameAndCityId")
    @RestReturn(value=CommunityDTO.class, collection=true)
    public RestResponse getCommunitiesByNameAndCityId(@Valid GetCommunitiesByNameAndCityIdCommand cmd) {
        List<CommunityDTO> results = this.communityService.getCommunitiesByNameAndCityId(cmd);

        RestResponse response =  new RestResponse(results);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /community/getCommunitiesByIds</b>
     * <p>根据小区id列表查询小区列表</p>
     */
    @RequestMapping("getCommunitiesByIds")
    @RestReturn(value=CommunityDTO.class, collection=true)
    public RestResponse getCommunitiesByIds(@Valid GetCommunitiesByIdsCommand cmd) {
        List<CommunityDTO> results = this.communityService.getCommunitiesByIds(cmd);
        RestResponse response =  new RestResponse(results);

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /community/updateCommunityRequestStatus</b>
     * <p>根据小区id更新小区收集状态</p>
     */
    @RequestMapping("updateCommunityRequestStatus")
    @RestReturn(value=String.class)
    public RestResponse updateCommunityRequestStatus(@Valid UpdateCommunityRequestStatusCommand cmd) {
        this.communityService.updateCommunityRequestStatus(cmd);
        RestResponse response =  new RestResponse();

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
	 * <b>URL: /community/listBuildings</b>
	 * <p>根据园区号查询楼栋列表</p>
	 */
	@RequestMapping("listBuildings")
    @RestReturn(value=ListBuildingCommandResponse.class)
	public RestResponse listBuildings(ListBuildingCommand cmd) {
		
		ListBuildingCommandResponse buildings = communityService.listBuildings(cmd);
		RestResponse response =  new RestResponse(buildings);

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}
	
	/**
	 * <b>URL: /community/getBuilding</b>
	 * <p>查询指定园区内指定楼栋详情</p>
	 */
	@RequestMapping("getBuilding")
    @RestReturn(value=BuildingDTO.class)
	public RestResponse getBuilding(GetBuildingCommand cmd) {
		BuildingDTO dto = communityService.getBuilding(cmd);
		RestResponse response =  new RestResponse(dto);

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}
	
	/**
	 * <b>URL: /community/listCommunityUsers</b>
	 * <p>查询园区用户列表</p>
	 */
	@RequestMapping("listCommunityUsers")
    @RestReturn(value=CommunityUserResponse.class)
	public RestResponse listCommunityUsers(ListCommunityUsersCommand cmd) {
		CommunityUserResponse res = communityService.listUserCommunities(cmd);
		RestResponse response =  new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}
	

	
	/**
	 * <b>URL: /community/countCommunityUsers</b>
	 * <p>统计园区用户列表</p>
	 */
	@RequestMapping("countCommunityUsers")
    @RestReturn(value=CountCommunityUserResponse.class)
	public RestResponse countCommunityUsers(CountCommunityUsersCommand cmd) {
		
		CountCommunityUserResponse resp = communityService.countCommunityUsers(cmd);
		RestResponse response =  new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}
	
	 /**
     * <b>URL: /community/listCommunitiesByNamespaceId</b>
     * <p>根据域查询小区</p>
     */
    @RequestMapping("listCommunitiesByNamespaceId")
    @RestReturn(value=ListCommunitiesByKeywordCommandResponse.class)
    public RestResponse listCommunitiesByNamespaceId(ListComunitiesByKeywordAdminCommand cmd) {
    	
    	ListCommunitiesByKeywordCommandResponse cmdResponse = this.communityService.listCommunitiesByKeyword(cmd);
    	RestResponse response =  new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }

}
