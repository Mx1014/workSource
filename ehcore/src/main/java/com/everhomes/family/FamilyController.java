package com.everhomes.family;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.family.FamilyDTO;
import com.everhomes.family.FamilyMemberDTO;
import com.everhomes.family.FamilyMembershipRequestDTO;
import com.everhomes.family.ListNearbyNeighborUserCommand;
import com.everhomes.family.NeighborUserDTO;
import com.everhomes.family.UpdateFamilyInfoCommand;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.UserInfo;
import com.everhomes.util.Tuple;

@RestController
@RequestMapping("/family")
public class FamilyController extends ControllerBase {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(FamilyController.class);
	
	@Autowired
    private FamilyProvider familyProvider;
	
	/**
	 * <b>URL: /family/findFamilyByKeyword</b>
	 * 根据关键字查询家庭信息
	 */
    @RequestMapping("findFamilyByKeyword")
    @RestReturn(value=FamilyDTO.class)
    public RestResponse findFamilyByKeyword(
        @RequestParam(value = "keyword", required = true) String keyword) {
    	Tuple<Integer, List<FamilyDTO>> result = familyProvider.findFamilByKeyword(keyword);
    	RestResponse response = new RestResponse(result.second());
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/getOwningFamilyById</b>
     * 根据家庭Id查询用户所在家庭
     */
    @RequestMapping("getOwningFamilyById")
    @RestReturn(value=FamilyDTO.class)
    public RestResponse getOwningFamilyById(
        @RequestParam(value = "familyId", required = true) Long familyId) {
        
        // familyId should be one of the families that user is currently in relation with
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/join</b>
     * 加入家庭
     */
    @RequestMapping("join")
    @RestReturn(value=String.class)
    public RestResponse join(
        @RequestParam(value = "familyId", required = true) Long familyId) {
    
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/leave</b>
     * 退出家庭
     */
    @RequestMapping("leave")
    @RestReturn(value=String.class)
    public RestResponse leave(
        @RequestParam(value = "familyId", required = true) Long familyId) {
    
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/follow</b>
     * 关注家庭
     * @return
     */
    @RequestMapping("follow")
    @RestReturn(value=String.class)
    public RestResponse follow(
        @RequestParam(value = "familyId", required = true) Long familyId,
        @RequestParam(value = "aliasName", required = false) String aliasName) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/unfollow</b>
     * 取消关注家庭
     * @param familyId
     * @return
     */
    @RequestMapping("unfollow")
    @RestReturn(value=String.class)
    public RestResponse follow(
        @RequestParam(value = "familyId", required = true) Long familyId) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/listFollowedFamilies</b>
     * 查询所关注的家庭列表
     * @return
     */
    @RequestMapping("listFollowedFamilies")
    @RestReturn(value=FamilyDTO.class, collection=true)
    public RestResponse listFollowedFamilies() {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /family/setFollowedFamilyAlias</b>
     * 备注关注的家庭的别名
     */
    @RequestMapping("setFollowedFamilyAlias")
    @RestReturn(value=String.class)
    public RestResponse setFollowedFamilyAlias(@RequestParam(value = "familyId", required = true) Long familyId,
            @RequestParam(value = "aliasName", required = true) String aliasName) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/listFollowers</b>
     * 查询指定家庭Id的关注用户
     */
    @RequestMapping("listFollowers")
    @RestReturn(value=UserInfo.class, collection=true)
    public RestResponse listFollowers(@RequestParam(value = "familyId", required = true) Long familyId,
            @RequestParam(value = "pageOffset", required = false) Long pageOffset) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/get</b>
     * 根据家庭Id查询家庭信息
     */
    @RequestMapping("get")
    @RestReturn(value=FamilyDTO.class)
    public RestResponse get(
        @RequestParam(value = "familyId", required = true) Long familyId) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/ejectMember</b>
     * 剔除家庭成员
     */
    @RequestMapping("ejectMember")
    @RestReturn(value=String.class)
    public RestResponse ejectMember(
        @RequestParam(value = "familyId", required = true) Long familyId,
        @RequestParam(value = "memberUid", required = true) Long memberUid,
        @RequestParam(value = "reason", required = false) String reason) {
    
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/approveMember</b>
     * 批准家庭成员加入
     */
    @RequestMapping("approveMember")
    @RestReturn(value=String.class)
    public RestResponse approveMember(
        @RequestParam(value = "familyId", required = true) Long familyId,
        @RequestParam(value = "memberUid", required = true) Long memberUid) {
    
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/listOwningFamilyMembers</b>
     * 查询用户所在家庭的成员列表
     */
    @RequestMapping("listOwningFamilyMembers")
    @RestReturn(value=FamilyMemberDTO.class, collection=true)
    public RestResponse listOwningFamilyMembers(@RequestParam(value = "familyId", required = true) Long familyId) {
            
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/listFamilyRequests</b>
     * 查询加入家庭的待处理的申请列表
     */
    @RequestMapping("listFamilyRequests")
    @RestReturn(value=FamilyMembershipRequestDTO.class, collection=true)
    public RestResponse listFamilyRequests(@RequestParam(value = "familyId", required = false) Long familyId,
            @RequestParam(value = "pageOffset", required = false) Long pageOffset) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/setCurrentFamily</b>
     * 设置当前家庭
     */
    @RequestMapping("setCurrentFamily")
    @RestReturn(value=String.class)
    public RestResponse setCurrentFamily(@RequestParam(value = "familyId", required = true) Long familyId) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/setPrimaryFamily</b>
     * 设置常用家庭
     */
    @RequestMapping("setPrimaryFamily")
    @RestReturn(value=String.class)
    public RestResponse setPrimaryFamily(@RequestParam(value = "familyId", required = true) Long familyId) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/updateFamilyInfo</b>
     * 更新家庭信息
     */
    @RequestMapping("updateFamilyInfo")
    @RestReturn(value=String.class)
    public RestResponse updateFamilyInfo(@Valid UpdateFamilyInfoCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/listNeighborUsers</b>
     * 查询小区邻居列表
     */
    @RequestMapping("listNeighborUsers")
    @RestReturn(value=NeighborUserDTO.class, collection=true)
    public RestResponse listNeighborUsers(@RequestParam(value = "familyId", required = false) Long familyId,
            @RequestParam(value = "pageOffset", required = false) Long pageOffset) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/listNearbyNeighborUsers</b>
     * 查询附近同小区邻居
     */
    @RequestMapping("listNearbyNeighborUsers")
    @RestReturn(value=NeighborUserDTO.class, collection=true)
    public RestResponse listNearbyNeighborUsers(@Valid ListNearbyNeighborUserCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
