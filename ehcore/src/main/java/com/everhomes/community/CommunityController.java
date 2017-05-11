package com.everhomes.community;



import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.community.*;
import com.everhomes.rest.community.admin.*;
import com.everhomes.util.RequireAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.app.AppConstants;
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
    @RequireAuthentication(false)
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
     * <b>URL: /community/exportCommunityUsers</b>
     * <p>导出园区用户列表</p>
     */
    @RequestMapping("exportCommunityUsers")
    public void exportCommunityUsers(ListCommunityUsersCommand cmd, HttpServletResponse response) {
        communityService.exportCommunityUsers(cmd, response);


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

    /**
     * <b>URL: /community/createResourceCategory</b>
     * <p>创建资源分类</p>
     */
    @RequestMapping("createResourceCategory")
    @RestReturn(value=String.class)
    public RestResponse createResourceCategory(CreateResourceCategoryCommand cmd) {
        RestResponse response =  new RestResponse();
        communityService.createResourceCategory(cmd);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /community/updateResourceCategory</b>
     * <p>修改资源分类</p>
     */
    @RequestMapping("updateResourceCategory")
    @RestReturn(value=String.class)
    public RestResponse updateResourceCategory(UpdateResourceCategoryCommand cmd) {
        RestResponse response =  new RestResponse();
        communityService.updateResourceCategory(cmd);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /community/deleteResourceCategory</b>
     * <p>删除资源分类</p>
     */
    @RequestMapping("deleteResourceCategory")
    @RestReturn(value=String.class)
    public RestResponse deleteResourceCategory(DeleteResourceCategoryCommand cmd) {
        RestResponse response =  new RestResponse();
        communityService.deleteResourceCategory(cmd);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /community/createResourceCategoryAssignment</b>
     * <p>给资源分配类型</p>
     */
    @RequestMapping("createResourceCategoryAssignment")
    @RestReturn(value=String.class)
    public RestResponse createResourceCategoryAssignment(CreateResourceCategoryAssignmentCommand cmd) {
        RestResponse response =  new RestResponse();
        communityService.createResourceCategoryAssignment(cmd);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /community/deleteResourceCategoryAssignment</b>
     * <p>删除资源分配类型</p>
     */
    @RequestMapping("deleteResourceCategoryAssignment")
    @RestReturn(value=String.class)
    public RestResponse deleteResourceCategoryAssignment(CreateResourceCategoryAssignmentCommand cmd) {
        RestResponse response =  new RestResponse();
        communityService.deleteResourceCategoryAssignment(cmd);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /community/listResourceCategories</b>
     * <p>分类列表</p>
     */
    @RequestMapping("listResourceCategories")
    @RestReturn(value=ResourceCategoryDTO.class, collection = true)
    public RestResponse listResourceCategories(ListResourceCategoryCommand cmd) {
    	List<ResourceCategoryDTO> list = communityService.listResourceCategories(cmd);
        RestResponse response = new RestResponse(list);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /community/listTreeResourceCategories</b>
     * <p>分类列表</p>
     */
    @RequestMapping("listTreeResourceCategories")
    @RestReturn(value=ResourceCategoryDTO.class, collection = true)
    public RestResponse listTreeResourceCategories(ListResourceCategoryCommand cmd) {
    	List<ResourceCategoryDTO> list = communityService.listTreeResourceCategories(cmd);
        RestResponse response = new RestResponse(list);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /community/listTreeResourceCategoryAssignments</b>
     * <p>分配了资源的分类树状列表</p>
     */
    @RequestMapping("listTreeResourceCategoryAssignments")
    @RestReturn(value=ResourceCategoryDTO.class, collection = true)
    public RestResponse listTreeResourceCategoryAssignments(ListResourceCategoryCommand cmd) {
    	List<ResourceCategoryDTO> list = communityService.listTreeResourceCategoryAssignments(cmd);

        RestResponse response =  new RestResponse(list);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /community/listCommunitiesByCategory</b>
     * <p>根据域,城市，区县，分类，关键字查询小区</p>
     */
    @RequestMapping("listCommunitiesByCategory")
    @RestReturn(value=ListCommunitiesByKeywordCommandResponse.class)
    public RestResponse listCommunitiesByCategory(ListCommunitiesByCategoryCommand cmd) {
    	ListCommunitiesByKeywordCommandResponse resp = communityService.listCommunitiesByCategory(cmd);
        RestResponse response =  new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;

    }


    /**
     * <b>URL: /community/updateCommunity</b>
     * <p>更新小区信息</p>
     */
    @RequestMapping("updateCommunity")
    @RestReturn(value=String.class)
    public RestResponse updateCommunity(@Valid UpdateCommunityAdminCommand cmd) {
        this.communityService.updateCommunity(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /community/listChildProjects</b>
     * <p>查询小区的子项目</p>
     */
    @RequestMapping("listChildProjects")
    @RestReturn(value=ProjectDTO.class, collection = true)
    public RestResponse listChildProjects(ListChildProjectCommand cmd) {
        RestResponse response =  new RestResponse(communityService.listChildProjects(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /community/createChildProject</b>
     * <p>创建小区的子项目</p>
     */
    @RequestMapping("createChildProject")
    @RestReturn(value=String.class)
    public RestResponse createChildProject(CreateChildProjectCommand cmd) {
        communityService.createChildProject(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /community/updateChildProject</b>
     * <p>修改小区的子项目</p>
     */
    @RequestMapping("updateChildProject")
    @RestReturn(value=String.class)
    public RestResponse updateChildProject(UpdateChildProjectCommand cmd) {
        communityService.updateChildProject(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /community/deleteChildProject</b>
     * <p>删除小区的子项目</p>
     */
    @RequestMapping("deleteChildProject")
    @RestReturn(value=String.class)
    public RestResponse deleteChildProject(DeleteChildProjectCommand cmd) {
        communityService.deleteChildProject(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /community/getTreeProjectCategories</b>
     * <p>项目分类树状列表</p>
     */
    @RequestMapping("getTreeProjectCategories")
    @RestReturn(value=String.class)
    public RestResponse getTreeProjectCategories(GetTreeProjectCategoriesCommand cmd) {
        RestResponse response =  new RestResponse(communityService.getTreeProjectCategories(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
