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
	 * <p>根据关键字查询家庭信息</p>
	 */
    @RequestMapping("findFamilyByKeyword")
    @RestReturn(value=FamilyDTO.class)
    public RestResponse findFamilyByKeyword(
        @RequestParam(value = "keyword", required = true) String keyword) {
    	Tuple<Integer, List<FamilyDTO>> results = familyProvider.findFamilByKeyword(keyword);
    	RestResponse response = new RestResponse(results.second());
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/getOwningFamilyById</b>
     * <p>根据家庭Id查询用户所在家庭</p>
     */
    @RequestMapping("getOwningFamilyById")
    @RestReturn(value=FamilyDTO.class)
    public RestResponse getOwningFamilyById(
        @RequestParam(value = "familyId", required = true) Long familyId) {
        
        // familyId should be one of the families that user is currently in relation with
        
        FamilyDTO family = familyProvider.getOwningFamilyById(familyId);
        RestResponse response = new RestResponse(family);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/findFamilyByAddressId</b>
     * <p>根据地址Id查询家庭信息</p>
     */
    @RequestMapping("findFamilyByAddressId")
    @RestReturn(value=Family.class)
    public RestResponse findFamilyByAddressId(
        @RequestParam(value = "addressId", required = true) Long addressId) {
        
        Family family = familyProvider.findFamilyByAddressId(addressId);
        RestResponse response = new RestResponse(family);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/getOwningFamilyById</b>
     * <p>查询用户加入的家庭</p>
     */
    @RequestMapping("getUserOwningFamilies")
    @RestReturn(value=FamilyDTO.class)
    public RestResponse getUserOwningFamilies() {
        
        List<FamilyDTO> results = familyProvider.getUserOwningFamilies();
        RestResponse response = new RestResponse(results);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/join</b>
     * <p>加入家庭，状态为待审核</p>
     */
    @RequestMapping("join")
    @RestReturn(value=String.class)
    public RestResponse join(
        @RequestParam(value = "familyId", required = true) Long familyId) {
    	familyProvider.joinFamily(familyId);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/leave</b>
     * <p>退出家庭</p>
     */
    @RequestMapping("leave")
    @RestReturn(value=String.class)
    public RestResponse leave(
        @RequestParam(value = "familyId", required = true) Long familyId) {
    
        familyProvider.leave(familyId);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/get</b>
     * <p>根据家庭Id查询家庭信息</p>
     */
    @RequestMapping("get")
    @RestReturn(value=FamilyDTO.class)
    public RestResponse get(
        @RequestParam(value = "familyId", required = true) Long familyId) {
        
        FamilyDTO family = this.familyProvider.getFamilyById(familyId);
        RestResponse response = new RestResponse(family);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/ejectMember</b>
     * <p>剔除家庭成员</p>
     */
    @RequestMapping("ejectMember")
    @RestReturn(value=String.class)
    public RestResponse ejectMember(
        @RequestParam(value = "familyId", required = true) Long familyId,
        @RequestParam(value = "memberUid", required = true) Long memberUid,
        @RequestParam(value = "reason", required = false) String reason) {
    
        this.familyProvider.ejectMember(familyId,memberUid,reason);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/approveMember</b>
     * <p>批准家庭成员加入</p>
     */
    @RequestMapping("approveMember")
    @RestReturn(value=String.class)
    public RestResponse approveMember(
        @RequestParam(value = "familyId", required = true) Long familyId,
        @RequestParam(value = "memberUid", required = true) Long memberUid) {
    
        //
        this.familyProvider.approveMember(familyId, memberUid);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/listOwningFamilyMembers</b>
     * <p>查询用户所在家庭的成员列表</p>
     */
    @RequestMapping("listOwningFamilyMembers")
    @RestReturn(value=FamilyMemberDTO.class, collection=true)
    public RestResponse listOwningFamilyMembers(@RequestParam(value = "familyId", required = true) Long familyId) {
            
        List<FamilyMemberDTO> results = this.familyProvider.listOwningFamilyMembers(familyId);
        RestResponse response = new RestResponse(results);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/listFamilyRequests</b>
     * <p>查询加入家庭的待处理的申请列表</p>
     */
    @RequestMapping("listFamilyRequests")
    @RestReturn(value=FamilyMembershipRequestDTO.class, collection=true)
    public RestResponse listFamilyRequests(@RequestParam(value = "familyId", required = false) Long familyId,
            @RequestParam(value = "pageOffset", required = false) Long pageOffset) {
        
        List<FamilyMembershipRequestDTO> results = this.familyProvider.listFamilyRequests(familyId,pageOffset);
        RestResponse response = new RestResponse(results);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/setCurrentFamily</b>
     * <p>设置当前家庭</p>
     */
    @RequestMapping("setCurrentFamily")
    @RestReturn(value=String.class)
    public RestResponse setCurrentFamily(@RequestParam(value = "familyId", required = true) Long familyId) {
        
        this.familyProvider.setCurrentFamily(familyId);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/setPrimaryFamily</b>
     * <p>设置常用家庭</p>
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
     * <p>更新家庭信息</p>
     */
    @RequestMapping("updateFamilyInfo")
    @RestReturn(value=String.class)
    public RestResponse updateFamilyInfo(@Valid UpdateFamilyInfoCommand cmd) {
        
        this.familyProvider.updateFamilyInfo(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/listNeighborUsers</b>
     * <p>查询小区邻居列表</p>
     */
    @RequestMapping("listNeighborUsers")
    @RestReturn(value=NeighborUserDTO.class, collection=true)
    public RestResponse listNeighborUsers(@RequestParam(value = "familyId", required = false) Long familyId,
            @RequestParam(value = "pageOffset", required = false) Long pageOffset) {
        
        List<NeighborUserDTO> results = this.familyProvider.listNeighborUsers(familyId,pageOffset);
        RestResponse response = new RestResponse(results);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/listNearbyNeighborUsers</b>
     * <p>查询附近同小区邻居</p>
     */
    @RequestMapping("listNearbyNeighborUsers")
    @RestReturn(value=NeighborUserDTO.class, collection=true)
    public RestResponse listNearbyNeighborUsers(@Valid ListNearbyNeighborUserCommand cmd) {
        
        List<NeighborUserDTO> result = this.familyProvider.listNearbyNeighborUsers(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/follow</b>
     * <p>关注家庭</p>
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
     * <p>取消关注家庭</p>
     * @param familyId
     * @return
     */
    @RequestMapping("unfollow")
    @RestReturn(value=String.class)
    public RestResponse unfollow(
        @RequestParam(value = "familyId", required = true) Long familyId) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/listFollowedFamilies</b>
     * <p>查询所关注的家庭列表</p>
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
     * <p>备注关注的家庭的别名</p>
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
     * <p>查询指定家庭Id的关注用户</p>
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
    
    
}
