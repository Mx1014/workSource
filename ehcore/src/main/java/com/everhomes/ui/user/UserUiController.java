// @formatter:off
package com.everhomes.ui.user;

import com.everhomes.activity.ActivityService;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.family.FamilyService;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.activity.ListActivitiesReponse;
import com.everhomes.rest.family.ListNeighborUsersCommand;
import com.everhomes.rest.family.ListNeighborUsersCommandResponse;
import com.everhomes.rest.family.ParamType;
import com.everhomes.rest.organization.ListOrganizationContactCommand;
import com.everhomes.rest.organization.ListOrganizationContactCommandResponse;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.ui.organization.SetCurrentCommunityForSceneCommand;
import com.everhomes.rest.ui.user.*;
import com.everhomes.rest.user.UserCurrentEntityType;
import com.everhomes.user.UserService;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.WebTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <ul>
 * <li>在客户端组件化的过程中，有一些与界面有关的逻辑会放到服务器端</li>
 * <li>专门提供客户端逻辑的API都放到该Controller中，这类API属于比较高层的API，专门服务于界面</li>
 * </ul>
 */
@RestDoc(value="UserUi controller", site="userui")
@RestController
@RequestMapping("/ui/user")
public class UserUiController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserUiController.class);
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private FamilyService familyService;

    @Autowired
    private UserService userService;
    
    //@Autowired
    //private OpPromotionService opPromotionService;
    
    @Autowired
    private ActivityService activityService;
    
    /**
     * <b>URL: /ui/user/listUserRelatedScenes</b>
     * <p>列出用户当前域空间下的相关场景。</p>
     * <p>必须在请求的Header中提供域空间。</p>
     */
    @RequestMapping("listUserRelatedScenes")
    @RestReturn(value=SceneDTO.class, collection=true)
    public RestResponse listUserRelatedScenes() {
        List<SceneDTO> sceneDtoList = userService.listUserRelatedScenes();
        
        RestResponse response = new RestResponse(sceneDtoList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    } 

    /**
     * <p>根据指定的场景查询通讯录列表。</p>
     * <p>对于左邻版，普通用户查询的是邻居好友列表；对于园区版，查询的是公司通讯录列表；而对于物业管理员场景，查询的是同事列表。</p> 
     * <p>url:/ui/user/ </p>
     */
    @RequestMapping(value = "listContactsByScene")
    @RestReturn(value = ListContactBySceneRespose.class)
    public RestResponse listContactsByScene(@Valid ListContactsBySceneCommand cmd) throws Exception {
    	WebTokenGenerator webToken = WebTokenGenerator.getInstance();
 	    SceneTokenDTO sceneToken = webToken.fromWebToken(cmd.getSceneToken(), SceneTokenDTO.class);

 	    List<SceneContactDTO> dtos = null;
		if(UserCurrentEntityType.ORGANIZATION == UserCurrentEntityType.fromCode(sceneToken.getEntityType())){
			ListOrganizationContactCommand command = new ListOrganizationContactCommand();
			//兼容老版本的app by sfyan 20161020
			if(null == cmd.getOrganizationId()){
				cmd.setOrganizationId(sceneToken.getEntityId());
			}
			command.setOrganizationId(cmd.getOrganizationId());
			command.setPageSize(100000);
			command.setIsSignedup(cmd.getIsSignedup());
			ListOrganizationContactCommandResponse res = organizationService.listOrganizationContacts(command);
			List<OrganizationContactDTO> members = res.getMembers();
			if(null != members){
				dtos = members.stream().map(r->{
					SceneContactDTO dto = new SceneContactDTO();
					dto.setContactId(r.getId());
					dto.setContactName(r.getContactName());
					dto.setContactPhone(r.getContactToken());
					dto.setContactAvatar(r.getAvatar());
					dto.setUserId(r.getTargetId());
					dto.setInitial(r.getInitial());
					dto.setFullInitial(r.getFullInitial());
					dto.setFullPinyin(r.getFullPinyin());
					//增加岗位显示与 detailId added by R 20120713
					dto.setJobPosition(r.getJobPosition());
					dto.setDetailId(r.getDetailId());
					return dto;
				}).collect(Collectors.toList());
			}
			 
		}
		
		ListNeighborUsersCommandResponse resp = null;
		// 仅有小区信息看不到邻居 listNeighborUsers方法里示支持小区 by lqs 20160416
 	    if(UserCurrentEntityType.COMMUNITY == UserCurrentEntityType.fromCode(sceneToken.getEntityType())){
// 	    	ListNeighborUsersCommand command = new ListNeighborUsersCommand();
// 	    	command.setIsPinyin(1);
// 	    	command.setType(ParamType.COMMUNITY.getCode());
// 	    	command.setId(sceneToken.getEntityId());
// 	    	resp= familyService.listNeighborUsers(command);
		}else if(UserCurrentEntityType.FAMILY == UserCurrentEntityType.fromCode(sceneToken.getEntityType())){
 	    	ListNeighborUsersCommand command = new ListNeighborUsersCommand();
 	    	command.setIsPinyin(1);
 	    	command.setType(ParamType.FAMILY.getCode());
 	    	command.setId(sceneToken.getEntityId());
 	    	resp = familyService.listNeighborUsers(command);
		}
 	    
 	    if(null != resp && null != resp.getNeighborUserList() &&  0 != resp.getNeighborUserList().size()){
 	    	dtos = resp.getNeighborUserList().stream().map(r->{
				SceneContactDTO dto = new SceneContactDTO();
				dto.setContactId(r.getUserId());
				dto.setContactName(r.getUserName());
				dto.setStatusLine(r.getUserStatusLine());
				dto.setOccupation(r.getOccupation());
				dto.setContactAvatar(r.getUserAvatarUrl());
				dto.setUserId(r.getUserId());
				dto.setInitial(r.getInitial());
				dto.setFullInitial(r.getFullInitial());
				dto.setFullPinyin(r.getFullPinyin());
				dto.setNeighborhoodRelation(r.getNeighborhoodRelation());
				return dto;
			}).collect(Collectors.toList());
 	    }
 	    
		ListContactBySceneRespose res = new ListContactBySceneRespose();
		res.setContacts(dtos);
		RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /ui/user/getUserRelatedAddresses</b>
     * <p>根据用户域空间场景获取用户相关的地址列表</p>
     */
    @RequestMapping("getUserRelatedAddresses")
    @RestReturn(value=GetUserRelatedAddressResponse.class)
    public RestResponse getUserRelatedAddresses(GetUserRelatedAddressCommand cmd) {
        //List<UserServiceAddressDTO> result = this.userActivityService.getUserRelateServiceAddress();
        GetUserRelatedAddressResponse cmdResponse = userService.getUserRelatedAddresses(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /ui/user/getUserOpPromotionsByScene</b>
     * <p>获取用户相关的运营活动，打开APP时调用一次该接口，里面包含用户当前可享受的运营活动。
     *    对于一次性的活动，如果还在有效期内，用户不管是否已经查看，此接口都有数据返回，客户端需要记录是否看过来决定是否还显示该活动。</p>
     */
//    @RequestMapping(value = "getUserOpPromotionsByScene")
//    @RestReturn(ListUserOpPromotionsRespose.class)
//    public RestResponse getUserOpPromotionsByScene(GetUserOpPromotionCommand cmd) throws Exception {
//        ListUserOpPromotionsRespose cmdResponse = opPromotionService.getUserOpPromotionsByScene(cmd);
//        
//        RestResponse response = new RestResponse(cmdResponse);
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }
//    
    /**
     * <b>URL: /ui/user/setUserCurrentScene</b>
     * <p>设置当前的scene信息，服务器端不使用此信息，会在userInfo里还给客户端做选中上一次的场景使用</p>
     * <p>废弃，使用listScenesByCummunityId代替</p>
     */
//    @RequestMapping(value = "setUserCurrentScene")
//    @RestReturn(ListUserOpPromotionsRespose.class)
//    public RestResponse setUserCurrentScene(SetUserCurrentSceneCommand cmd) throws Exception {
//        ListUserOpPromotionsRespose cmdResponse = null;
//        
//        RestResponse response = new RestResponse(cmdResponse);
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }
    
    /**
     * <b>URL: /ui/user/setCurrentCommunityForScene</b>
     * <p>设置当前小区以获得场景</p>
     */
    @RequestMapping("setCurrentCommunityForScene")
    @RestReturn(value=SceneDTO.class, collection=true)
    public RestResponse setCurrentCommunityForScene(SetCurrentCommunityForSceneCommand cmd) {
        List<SceneDTO> sceneDtoList = userService.setCurrentCommunityForScene(cmd);
        
        RestResponse response = new RestResponse(sceneDtoList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /ui/user/listNearbyActivitiesByScene</b>
     * <p>根据场景、类型查询周边/同城活动</p>
     */
    @RequestMapping("listNearbyActivitiesByScene")
    @RestReturn(value=ListActivitiesReponse.class)
	@RequireAuthentication(false)
    public RestResponse listNearbyActivitiesByScene(ListNearbyActivitiesBySceneCommand cmd){
        ListActivitiesReponse rsp = activityService.listNearbyActivitiesByScene(cmd);
        RestResponse response = new RestResponse(rsp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
       return response;
   }
    
    /**
	 * <b>URL: /ui/user/searchContentsByScene</b>
	 * <p>根据场景、查询类型查询相应内容</p>
	 */
	@RequestMapping("searchContentsByScene")
	@RestReturn(value=SearchContentsBySceneReponse.class)
	public RestResponse searchContentsByScene(SearchContentsBySceneCommand cmd) {
		SearchContentsBySceneReponse rsp = userService.searchContentsByScene(cmd);
	    RestResponse response = new RestResponse(rsp);
	    response.setErrorCode(ErrorCodes.SUCCESS);
	    response.setErrorDescription("OK");

	    return response;
	} 
	
	    /**
	 * <b>URL: /ui/user/listSearchTypesByScene</b>
	 * <p>根据场景、域空间查查询内容类型</p>
	 */
	@RequestMapping("listSearchTypesByScene")
	@RestReturn(value=ListSearchTypesBySceneReponse.class)
	public RestResponse listSearchTypesByScene(ListSearchTypesBySceneCommand cmd) {
		ListSearchTypesBySceneReponse rsp = userService.listSearchTypesByScene(cmd);
	    RestResponse response = new RestResponse(rsp);
	    response.setErrorCode(ErrorCodes.SUCCESS);
	    response.setErrorDescription("OK");
	       
	    return response;
	}

	/**
	 * <b>URL: /ui/user/listTouristRelatedScenes</b>
	 * <p>列出游客当前域空间下的相关场景。</p>
	 * <p>必须在请求的Header中提供域空间。</p>
	 */
	@RequestMapping("listTouristRelatedScenes")
	@RestReturn(value=SceneDTO.class, collection=true)
	@RequireAuthentication(false)
	public RestResponse listTouristRelatedScenes() {
		List<SceneDTO> sceneDtoList = userService.listTouristRelatedScenes();
		RestResponse response = new RestResponse(sceneDtoList);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
 
/*
    @RequestMapping("getCurrentContactRealInfo")
    @RestReturn(value=SceneContactV2DTO.class)
    public RestResponse getCurrentContactRealInfo(GetCurrentContactRealInfoCommand cmd) {
        SceneContactV2DTO result = userService.getCurrentContactRealInfo(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }*/

    /**
     * <b>URL: /ui/user/getRelevantContactInfo</b>
     * <p>获取相关用户的详细信息。</p>
     * <p>获取用户公司、姓名、部门等信息</p>
     */
    @RequestMapping("getRelevantContactInfo")
    @RestReturn(value=SceneContactV2DTO.class)
    public RestResponse getRelevantContactInfo(GetRelevantContactInfoCommand cmd) {
        SceneContactV2DTO result = userService.getRelevantContactInfo(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    } 
	
	/**
	 * <b>URL: /ui/user/listAuthForms</b>
	 * <p>获取家庭认证和公司认证的sourceType,sourceId</p>
	 */
	@RequestMapping("listAuthForms")
	@RestReturn(value=ListAuthFormsResponse.class)
	public RestResponse listAuthForm() {
		ListAuthFormsResponse listAuthFormResponse = userService.listAuthForms();
		RestResponse response = new RestResponse(listAuthFormResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /ui/user/getFamilyButtonStatus</b>
	 * <p>获取文案,和家庭下button的是否显示</p>
	 */
	@RequestMapping("getFamilyButtonStatus")
	@RestReturn(value=GetFamilyButtonStatusResponse.class)
	public RestResponse getFamilyButtonStatus() {
		GetFamilyButtonStatusResponse getFamilyButtonStatusResponse = userService.getFamilyButtonStatus();
		RestResponse response = new RestResponse(getFamilyButtonStatusResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

}
