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
import com.everhomes.util.ConvertHelper;
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
        @Valid ListFamilyByKeywordCommand cmd) {
    	Tuple<Integer, List<FamilyDTO>> results = familyProvider.findFamilByKeyword(cmd);
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
        @Valid GetOwningFamilyByIdCommand cmd) {
        
        // familyId should be one of the families that user is currently in relation with
        
        FamilyDTO family = familyProvider.getOwningFamilyById(cmd);
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
    @RestReturn(value=FamilyDTO.class)
    public RestResponse findFamilyByAddressId(
        @Valid FindFamilyByAddressIdCommand cmd) {
        
        Family family = familyProvider.findFamilyByAddressId(cmd.getAddressId());
        RestResponse response = new RestResponse(ConvertHelper.convert(family, FamilyDTO.class));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/getUserOwningFamilies</b>
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
        @Valid JoinFamilyCommand cmd) {
    	familyProvider.joinFamily(cmd);
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
        @Valid LeaveFamilyCommand cmd) {
    
        familyProvider.leave(cmd);
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
        @Valid GetFamilyCommand cmd) {
        
        FamilyDTO family = this.familyProvider.getFamilyById(cmd);
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
        @Valid EjectMemberCommand cmd) {
    
        this.familyProvider.ejectMember(cmd);
        
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
        @Valid ApproveMemberCommand cmd) {
    
        //
        this.familyProvider.approveMember(cmd);
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
    public RestResponse listOwningFamilyMembers(@Valid ListOwningFamilyMembersCommand cmd) {
            
        List<FamilyMemberDTO> results = this.familyProvider.listOwningFamilyMembers(cmd);
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
    public RestResponse listFamilyRequests(@Valid ListFamilyRequestsCommand cmd) {
        
        List<FamilyMembershipRequestDTO> results = this.familyProvider.listFamilyRequests(cmd);
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
    public RestResponse setCurrentFamily(@Valid SetCurrentFamilyCommand cmd) {
        
        this.familyProvider.setCurrentFamily(cmd);
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
    public RestResponse setPrimaryFamily(@Valid SetPrimaryFamilyCommand cmd) {
        
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
    public RestResponse listNeighborUsers(@Valid ListNeighborUsersCommand cmd) {
        
        List<NeighborUserDTO> results = this.familyProvider.listNeighborUsers(cmd);
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
    public RestResponse follow(@Valid FollowFamilyCommand cmd) {
        
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
    public RestResponse unfollow(@Valid UnFollowFamilyCommand cmd) {
        
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
    public RestResponse setFollowedFamilyAlias(@Valid SetFollowedFamilyAliasCommand cmd) {
        
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
    public RestResponse listFollowers(@Valid ListFollowersCommand cmd) {
        
        // ???
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    
}
