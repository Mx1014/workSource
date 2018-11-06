package com.everhomes.family;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.family.*;
import com.everhomes.search.CommunitySearcher;
import com.everhomes.util.EtagHelper;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * <ul>家庭管理：
 * <li>根据关键字搜索家庭：调用findFamilyByKeyword()接口，关键字keyword为家庭名字；</li>
 * <li>根据家庭Id获取自己加入的家庭信息：调用getOwningFamilyById()接口；</li>
 * <li>获取自己加入的所有家庭列表：调用getUserOwningFamilies()接口；</li>
 * <li>根据地址Id查询家庭信息：调用findFamilyByAddressId()接口；</li>
 * </ul>
 */
@RestDoc(value="Family controller", site="core")
@RestController
@RequestMapping("/family")
public class FamilyController extends ControllerBase {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(FamilyController.class);
	
	@Autowired
    private FamilyProvider familyProvider;
	@Autowired
	private CommunitySearcher searcher;
	@Autowired
    private FamilyService familyService;
	
	/**
	 * <b>URL: /family/findFamilyByKeyword</b>
	 * <p>根据关键字查询家庭信息</p>
	 */
    @RequestMapping("findFamilyByKeyword")
    @RestReturn(value=FamilyDTO.class ,collection=true)
    public RestResponse findFamilyByKeyword(
        @Valid ListFamilyByKeywordCommand cmd) {
        
    	Tuple<Integer, List<FamilyDTO>> results = familyProvider.findFamilByKeyword(cmd.getKeyword());
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
    public RestResponse getOwningFamilyById(@Valid GetOwningFamilyByIdCommand cmd) {
        
        // familyId should be one of the families that user is currently in relation with
        FamilyDTO family = familyService.getOwningFamilyById(cmd);
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
        
        FamilyDTO familyDTO = this.familyService.getFamilyDetailByAddressId(cmd);
        RestResponse response = new RestResponse(familyDTO);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/getUserOwningFamilies</b>
     * <p>查询用户加入的家庭</p>
     */
    @RequestMapping("getUserOwningFamilies")
    @RestReturn(value=FamilyDTO.class ,collection=true)
    public RestResponse getUserOwningFamilies(HttpServletRequest request,HttpServletResponse response) {
        
        List<FamilyDTO> results = familyService.getUserOwningFamilies();
        RestResponse resp = new RestResponse(results);
//        if(results != null){
//            if(EtagHelper.checkHeaderEtagOnly(30,results.hashCode()+"", request, response)) {
//                resp.setResponseObject(results);
//            }
//        }
        
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }
    
    /**
     * <b>URL: /family/join</b>
     * <p>加入家庭，状态为待审核</p>
     */
    @RequestMapping("join")
    @RestReturn(value=String.class)
    public RestResponse join(@Valid JoinFamilyCommand cmd) {
        
        familyService.joinFamily(cmd);
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
    public RestResponse leave(@Valid LeaveFamilyCommand cmd) {
        
        familyService.leave(cmd, null);
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
    public RestResponse get(@Valid GetFamilyCommand cmd) {
        
        FamilyDTO family = this.familyService.getFamilyById(cmd);
        RestResponse response = new RestResponse(family);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/revokeMember</b>
     * <p>剔除家庭成员</p>
     */
    @RequestMapping("revokeMember")
    @RestReturn(value=String.class)
    public RestResponse revokeMember(@Valid RevokeMemberCommand cmd) {
    
        this.familyService.revokeMember(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /family/rejectMember</b>
     * <p>拒绝加入家庭（自己拒绝"别人邀请"或家庭成员拒绝）</p>
     */
    @RequestMapping("rejectMember")
    @RestReturn(value=String.class)
    public RestResponse rejectMember(@Valid RejectMemberCommand cmd) {
    
        this.familyService.rejectMember(cmd);
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
    public RestResponse approveMember(@Valid ApproveMemberCommand cmd) {
    
        this.familyService.approveMember(cmd);
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
        
        List<FamilyMemberDTO> results = this.familyService.listOwningFamilyMembers(cmd);
        
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
    @RestReturn(value=ListFamilyRequestsCommandResponse.class)
    public RestResponse listFamilyRequests(@Valid ListFamilyRequestsCommand cmd) {
        
        ListFamilyRequestsCommandResponse cmdResponse = this.familyService.listFamilyRequests(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
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
        
        this.familyService.setCurrentFamily(cmd);
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
        
        this.familyService.updateFamilyInfo(cmd);
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
    @RestReturn(value=ListNeighborUsersCommandResponse.class)
    public RestResponse listNeighborUsers(@Valid ListNeighborUsersCommand cmd,HttpServletRequest request, HttpServletResponse response) {
        
        ListNeighborUsersCommandResponse result = this.familyService.listNeighborUsers(cmd);
        RestResponse resp = new RestResponse();
        if(EtagHelper.checkHeaderEtagOnly(30,result.hashCode()+"", request, response)) {
            resp.setResponseObject(result);
        }
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }
    
    /**
     * <b>URL: /family/listNearbyNeighborUsers</b>
     * <p>查询附近同小区邻居</p>
     */
    @RequestMapping("listNearbyNeighborUsers")
    @RestReturn(value=NeighborUserDTO.class, collection=true)
    public RestResponse listNearbyNeighborUsers(@Valid ListNearbyNeighborUserCommand cmd) {
        
        List<NeighborUserDTO> result = this.familyService.listNearbyNeighborUsers(cmd);
        
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("listFamilyMembersByCityId")
    @RestReturn(value=FamilyMemberDTO.class, collection=true)
    public RestResponse listFamilyMembersByCityId(ListNeighborUsersCommand cmd) {
        
        List<FamilyMemberDTO> result = this.familyService.listFamilyMembersByCityId(cmd.getId(), 1, 20);
        
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("listFamilyMembersByCommunityId")
    @RestReturn(value=FamilyMemberDTO.class, collection=true)
    public RestResponse listFamilyMembersByCommunityId(ListNeighborUsersCommand cmd) {
        
        List<FamilyMemberDTO> result = this.familyService.listFamilyMembersByCommunityId(cmd.getId(), 1, 20);
        
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    @RequestMapping("listFamilyMembersByFamilyId")
    @RestReturn(value=FamilyMemberDTO.class, collection=true)
    public RestResponse listFamilyMembersByFamilyId(ListNeighborUsersCommand cmd) {
        List<FamilyMemberDTO> result = this.familyService.listFamilyMembersByFamilyId(cmd.getId(), 1, 20);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    @RequestMapping("deleteHistoryById")
    @RestReturn(value=String.class)
    public RestResponse deleteHistoryById(DeleteHistoryByIdCommand cmd) {
        this.familyService.deleteHistoryById(cmd.getHistoryId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;        
    }
    
//    /**
//     * <b>URL: /family/setPrimaryFamily</b>
//     * <p>设置常用家庭</p>
//     */
//    @RequestMapping("setPrimaryFamily")
//    @RestReturn(value=String.class)
//    public RestResponse setPrimaryFamily(@Valid SetPrimaryFamilyCommand cmd) {
//        
//        // ???
//        RestResponse response = new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }
//    
//    /**
//     * <b>URL: /family/follow</b>
//     * <p>关注家庭</p>
//     * @return
//     */
//    
//    @RequestMapping("follow")
//    @RestReturn(value=String.class)
//    public RestResponse follow(@Valid FollowFamilyCommand cmd) {
//        
//        // ???
//        RestResponse response = new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }
//    
//    
//    /**
//     * <b>URL: /family/unfollow</b>
//     * <p>取消关注家庭</p>
//     * @param familyId
//     * @return
//     */
//    @RequestMapping("unfollow")
//    @RestReturn(value=String.class)
//    public RestResponse unfollow(@Valid UnFollowFamilyCommand cmd) {
//        
//        // ???
//        RestResponse response = new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }
//    
//    /**
//     * <b>URL: /family/listFollowedFamilies</b>
//     * <p>查询所关注的家庭列表</p>
//     * @return
//     */
//    @RequestMapping("listFollowedFamilies")
//    @RestReturn(value=FamilyDTO.class, collection=true)
//    public RestResponse listFollowedFamilies() {
//        
//        // ???
//        RestResponse response = new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }
//
//    /**
//     * <b>URL: /family/setFollowedFamilyAlias</b>
//     * <p>备注关注的家庭的别名</p>
//     */
//    @RequestMapping("setFollowedFamilyAlias")
//    @RestReturn(value=String.class)
//    public RestResponse setFollowedFamilyAlias(@Valid SetFollowedFamilyAliasCommand cmd) {
//        
//        // ???
//        RestResponse response = new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }
//    
//    /**
//     * <b>URL: /family/listFollowers</b>
//     * <p>查询指定家庭Id的关注用户</p>
//     */
//    @RequestMapping("listFollowers")
//    @RestReturn(value=UserInfo.class, collection=true)
//    public RestResponse listFollowers(@Valid ListFollowersCommand cmd) {
//        
//        // ???
//        RestResponse response = new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }
    @RequireAuthentication(false)
    @RequestMapping("testLockAquiring")
    @RestReturn(value=String.class)
    public RestResponse testLockAquiring(TestLockAquiringCommand cmd) {
        this.familyService.testLockAquiring(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;        
    }

    /**
     * <b>URL: /family/listUserFamilyByCommunityId</b>
     * <p>获取用户自该园区的家庭地址</p>
     */
    @RequestMapping("listUserFamilyByCommunityId")
    @RestReturn(value=ListUserFamilyByCommunityIdResponse.class)
    public RestResponse listUserFamilyByCommunityId(ListUserFamilyByCommunityIdCommand cmd) {
        ListUserFamilyByCommunityIdResponse res = familyService.listUserFamilyByCommunityId(cmd);

        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
}
