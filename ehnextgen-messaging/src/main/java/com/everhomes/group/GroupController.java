// @formatter:off
package com.everhomes.group;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.controller.XssExclude;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.group.*;
import com.everhomes.util.RequireAuthentication;
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
 * <ul>圈管理：
 * <li>创建私有圈和兴趣圈：调用create()接口，通过privateFlag来区分；创建圈的人拥有管理员权限；</li>
 * <li>订阅兴趣圈：调用requestToJoin()接口，不需要批准；由于兴趣圈加入不需要批准，故目前approveJoinRequest()、rejectJoinRequest()不使用；</li>
 * <li>私有圈邀请人加入：调用inviteToJoin()接口；邀请人需要被邀请人审核，审核时分别调用acceptInvitation()、rejectInvitation()来同意的拒绝；</li>
 * <li>自己主动退出圈：调用leave()接口；</li>
 * <li>把别人踢出圈：调用revokeMember()，此接口需要管理员权限才能调用；</li>
 * <li>列出个人有关的圈：调用listUserRelatedGroups()；这些圈包含还在等待审核、等待同意加入、已经加入的圈；</li>
 * <li>搜索兴趣圈：调用search()；</li>
 * </ul>
 * <ul>圈管理员管理：
 * <li>自己主动申请成为管理员：调用requestToBeAdmin()接口，需要管理员调用approveAdminRole()、rejectAdminRole()来进行批准和拒绝；</li>
 * <li>把别人指为管理员：调用inviteToBeAdmin()接口；不需要额外同意和拒绝；</li>
 * <li>自己退出管理员角色：调用resignAdminRole()接口；</li>
 * <li>取消管理员角色：调用revokeAdminRole()接口，被取消管理员角色的人变成普通成员；</li>
 * </ul>
 */
@RestDoc(value="Group controller", site="messaging")
@RestController
@RequestMapping("/group")
public class GroupController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private GroupProvider groupProvider;
    
    @Autowired
    private GroupService groupService;
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    /**
     * <b>URL: /group/create</b>
     * <p>创建一个新group，并根据group创建对应的论坛（创建者自动成员group的成员，并拥有管理group的权限）</p>
     * <p>创建group时必须指定一个category，否则会报general:506错误。</p>
     */
    @XssExclude
    @RequestMapping("create")
    @RestReturn(value=GroupDTO.class)
    public RestResponse create(@Valid CreateGroupCommand cmd) {
        return groupService.createAGroup(cmd);
    }
    
//    @RequestMapping("test")
//    @RestReturn(value=GroupDTO.class)
//    public RestResponse test(@Valid CreateGroupCommand cmd) {
//        
//        // ???
//        RestResponse response = new RestResponse();
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }
    
    /**
     * <b>URL: /group/update</b>
     * <p>更新group信息</p>
     */
    @XssExclude
    @RequestMapping("update")
    @RestReturn(value=GroupDTO.class)
    public RestResponse update(@Valid UpdateGroupCommand cmd) {
        GroupDTO groupDto = this.groupService.updateGroup(cmd);
        
        RestResponse response = new RestResponse(groupDto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/get</b>
     * <p>获取指定ID对应的group信息</p>
     */
    @RequestMapping("get")
    @RequireAuthentication(false)
    @RestReturn(value=GroupDTO.class)
    public RestResponse get(@Valid GetGroupCommand cmd) {
        GroupDTO groupDto = this.groupService.getGroup(cmd);
        RestResponse response = new RestResponse();
        // 由于涉及到订阅人数和是否已经订阅该圈的变化，不宜做Etag
        response.setResponseObject(groupDto);
        
//        Timestamp etagTime = groupDto.getUpdateTime();
//        if(etagTime == null) {
//            etagTime = groupDto.getBehaviorTime();
//        } 
//        if(etagTime != null) {
//            int interval = configurationProvider.getIntValue(AppConstants.DEFAULT_ETAG_TIMEOUT_KEY, 
//                AppConstants.DEFAULT_ETAG_TIMEOUT_SECONDS);
//            if(EtagHelper.checkHeaderCache(interval, etagTime.getTime(), request, paramResponse)) {
//                response.setResponseObject(groupDto);
//            }
//        } else {
//            if(LOGGER.isWarnEnabled()) {
//                LOGGER.warn("No update time or create time in the group, cmd=" + cmd);
//            }
//            response.setResponseObject(groupDto);
//        }
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/listUserRelatedGroups</b>
     * <p>获取自己所有拥有的group，含成员的不同状态，参考{@link com.everhomes.rest.group.GroupMemberStatus}</p>
     */
    @RequestMapping("listUserRelatedGroups")
    @RestReturn(value=GroupDTO.class, collection=true)
    public RestResponse listUserRelatedGroups() {
    	List<GroupDTO> groups = this.groupService.listUserRelatedGroups();

    	RestResponse response = new RestResponse(groups);
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	return response;
    }

    /**
     * <b>URL: /group/listUserGroups</b>
     * <p>获取自己所有拥有的group，含成员的不同状态，参考{@link com.everhomes.rest.group.GroupMemberStatus}</p>
     */
    @RequestMapping("listUserGroups")
    @RestReturn(value=GroupDTO.class, collection=true)
    public RestResponse listUserGroups() {
        List<GroupDTO> groups = this.groupService.listUserGroups();

        RestResponse response = new RestResponse(groups);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    
    /**
     * <b>URL: /group/listGroupsByNamespaceId</b>
     * <p>获取全部的group</p>
     */
    @RequestMapping("listGroupsByNamespaceId")
    @RequireAuthentication(false)
    @RestReturn(value=ListGroupCommandResponse.class)
    public RestResponse listGroupsByNamespaceId(ListGroupsByNamespaceIdCommand cmd){
    	RestResponse response = new RestResponse(groupService.listGroupsByNamespaceId(cmd));
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	return response;
    }
    
    /**
     * <b>URL: /group/listPublicGroups</b>
     * <p>列出指定用户关注的公开的group</p>
     */
    @RequestMapping("listPublicGroups")
    @RestReturn(value=GroupDTO.class, collection=true)
    public RestResponse listPublicGroups(ListPublicGroupCommand cmd) {
        List<GroupDTO> groups = this.groupService.listPublicGroups(cmd);
        
        RestResponse response = new RestResponse(groups);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/updateGroupMember</b>
     * <p>更新group成员信息（如昵称、头像）</p>
     */
    @RequestMapping("updateGroupMember")
    @RestReturn(value=GroupMemberDTO.class)
    public RestResponse updateGroupMember(UpdateGroupMemberCommand cmd) {
        GroupMemberDTO groupMember = this.groupService.updateGroupMember(cmd);
        
        RestResponse response = new RestResponse(groupMember);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/getGroupMemberSnapshot</b>
     * <p>获取group成员快照信息（如昵称、头像），不需要权限控制</p>
     */
    @RequestMapping("getGroupMemberSnapshot")
    @RestReturn(value=GroupMemberSnapshotDTO.class)
    public RestResponse getGroupMemberSnapshot(GetGroupMemberSnapshotCommand cmd, 
            HttpServletRequest request, HttpServletResponse paramResponse) {
        GroupMemberSnapshotDTO memberSnapshot = this.groupService.getGroupMemberSnapshot(cmd);
        
        RestResponse response = new RestResponse();
        // 去掉etag add by xiongying 20160531
//        Timestamp etagTime = memberSnapshot.getUpdateTime();
//        if(etagTime != null) {
//            etagTime = memberSnapshot.getUpdateTime();
//        }
//        if(etagTime != null) {
//            int interval = configurationProvider.getIntValue(AppConstants.DEFAULT_ETAG_TIMEOUT_KEY, 
//                AppConstants.DEFAULT_ETAG_TIMEOUT_SECONDS);
//            if(EtagHelper.checkHeaderCache(interval, etagTime.getTime(), request, paramResponse)) {
//                response.setResponseObject(memberSnapshot);
//            }
//        } else {
//            if(LOGGER.isWarnEnabled()) {
//                LOGGER.warn("No update time or create time in the group member, cmd=" + cmd);
//            }
//            response.setResponseObject(memberSnapshot);
//        }
        
        response.setResponseObject(memberSnapshot);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/requestToJoin</b>
     * <p>主动申请加入指定ID对应的group</p>
     */
    @RequestMapping("requestToJoin")
    @RestReturn(value=String.class)
    public RestResponse requestToJoin(@Valid RequestToJoinGroupCommand cmd) {
        this.groupService.requestToJoinGroup(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/requestToJoinGroupByQRCode</b>
     * <p>扫二维码申请加入指定ID对应的group</p>
     */
    @RequestMapping("requestToJoinGroupByQRCode")
    @RestReturn(value=String.class)
    public RestResponse requestToJoinGroupByQRCode(RequestToJoinGroupCommand cmd) {
        this.groupService.requestToJoinGroupByQRCode(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/inviteToJoin</b>
     * <p>通过用户ID邀请指定用户加入指定group</p>
     */
    @RequestMapping("inviteToJoin")
    @RestReturn(value=CommandResult.class, collection=true)
    public RestResponse inviteToJoin(@Valid InviteToJoinGroupCommand cmd) {
        List<CommandResult> cmdResponse = this.groupService.inviteToJoinGroup(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/inviteToJoinByPhone</b>
     * <p>通过电话号码邀请指定用户加入指定group</p>
     */
    @RequestMapping("inviteToJoinByPhone")
    @RestReturn(value=CommandResult.class, collection=true)
    public RestResponse inviteToJoinByPhone(@Valid InviteToJoinGroupByPhoneCommand cmd) {
        List<CommandResult> cmdResponse = this.groupService.inviteToJoinGroupByPhone(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/inviteToJoinByFamily</b>
     * <p>通过门牌号邀请指定家庭里的所有成员加入指定group</p>
     */
    @RequestMapping("inviteToJoinByFamily")
    @RestReturn(value=CommandResult.class, collection=true)
    public RestResponse inviteToJoinByFamily(InviteToJoinGroupByFamilyCommand cmd) {
        List<CommandResult> cmdResponse = this.groupService.inviteToJoinGroupByFamily(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/acceptJoinInvitation</b>
     * <p>接受别人的邀请加入指定ID对应的group</p>
     */
    @RequestMapping("acceptJoinInvitation")
    @RestReturn(value=String.class)
    public RestResponse acceptInvitation(@Valid AcceptJoinGroupInvitation cmd) {
    
        this.groupService.acceptJoinGroupInvitation(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/rejectJoinInvitation</b>
     * <p>拒绝别人的邀请加入指定ID对应group的请求</p>
     */
    @RequestMapping("rejectJoinInvitation")
    @RestReturn(value=String.class)
    public RestResponse rejectInvitation(@Valid RejectJoinGroupInvitation cmd) {
    
        this.groupService.rejectJoinGroupInvitation(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/approveJoinRequest</b>
     * <p>批准 别人申请加入group的请求</p>
     */
    @RequestMapping("approveJoinRequest")
    @RestReturn(value=String.class)
    public RestResponse approveJoinRequest(@Valid ApproveJoinGroupRequestCommand cmd) {
    
        this.groupService.approveJoinGroupRequest(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/rejectJoinRequest</b>
     * <p>拒绝 别人申请加入group的请求</p>
     */
    @RequestMapping("rejectJoinRequest")
    @RestReturn(value=String.class)
    public RestResponse rejectJoinRequest(@Valid RejectJoinGroupRequestCommand cmd) {
    
        this.groupService.rejectJoinGroupRequest(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/leave</b>
     * <p>退出指定group</p>
     */
    @RequestMapping("leave")
    @RestReturn(value=String.class)
    public RestResponse leave(@Valid LeaveGroupCommand cmd) {
    
        this.groupService.leaveGroup(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/revokeMember</b>
     * <p>把指定成员从group里踢出</p>
     */
    @RequestMapping("revokeMember")
    @RestReturn(value=String.class)
    public RestResponse revokeMember(@Valid RevokeGroupMemberCommand cmd) {
    
        this.groupService.revokeGroupMember(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /group/revokeMemberList</b>
     * <p>把指定成员从group里踢出</p>
     */
    @RequestMapping("revokeMemberList")
    @RestReturn(value=String.class)
    public RestResponse revokeMemberList(@Valid RevokeGroupMemberListCommand cmd) {

        this.groupService.revokeGroupMemberList(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    
    /**
     * <b>URL: /group/listGroupWaitingAcceptances</b>
     * <p>列出用户相关的被邀请加入group、等待接受/拒绝的处理列表，当用户被邀请进圈时，由于还不是圈的成员，
     *    无法通过圈来获取信息，故需要列出这些待处理列表，以便用户处理同意或拒绝加入圈。
     * </p>
     */
    @RequestMapping("listGroupWaitingAcceptances")
    @RestReturn(value=GroupMemberDTO.class, collection=true)
    public RestResponse listGroupWaitingAcceptances() {
    	List<GroupMemberDTO> memberDTOs = this.groupService.listGroupWaitingAcceptances();
    	
    	RestResponse response = new RestResponse(memberDTOs);
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	return response;
    }
    
    /**
     * <b>URL: /group/listGroupWaitingApprovals</b>
     * <p>列出用户可审批的、加入group的请求列表，当用户已经加入圈并拥有审核其它成员加入权限时，
     *    可列出这些申请加入的请求列表，以便通过或拒绝其它成员的加入。
     * </p>
     */
    @RequestMapping("listGroupWaitingApprovals")
    @RestReturn(value=ListGroupWaitingApprovalsCommandResponse.class)
    public RestResponse listGroupWaitingApprovals(ListGroupWaitingApprovalsCommand cmd) {
        ListGroupWaitingApprovalsCommandResponse cmdResponse = this.groupService.listGroupWaitingApprovals(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/listMembersInRole</b>
     * <p>按指定成员角色，分页获取group正常成员列表</p>
     */
    @RequestMapping("listMembersInRole")
    @RestReturn(value=ListMemberCommandResponse.class)
    public RestResponse listMembersInRole(ListMemberInRoleCommand cmd) {
        ListMemberCommandResponse commandResponse = this.groupService.listMembersInRole(cmd);
        
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/listMembersInStatus</b>
     * <p>按指定成员状态，分页获取group成员列表</p>
     */
    @RequestMapping("listMembersInStatus")
    @RestReturn(value=ListMemberCommandResponse.class)
    public RestResponse listMembersInStatus(ListMemberInStatusCommand cmd) {
        ListMemberCommandResponse commandResponse = this.groupService.listMembersInStatus(cmd);
        
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/inviteToBeAdmin</b>
     * <p>通过指定手机号，邀请手机号对应的用户成为group的管理员。</p>
     * @return 邀请结果
     */
    @RequestMapping("inviteToBeAdmin")
    @RestReturn(value=String.class)
    public RestResponse inviteToBeAdmin(@Valid InviteToBeAdminCommand cmd) {
    
        this.groupService.inviteToBeAdmin(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/requestToBeAdmin</b>
     * <p>申请成为指定group的管理员</p>
     * @param groupId group id
     * @return 申请结果
     */
    @RequestMapping("requestToBeAdmin")
    @RestReturn(value=String.class)
    public RestResponse requestToBeAdmin(@Valid RequestAdminRoleCommand cmd) {
    
        this.groupService.requestToBeAdmin(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/revokeAdminRole</b>
     * <p>去掉指定用户在指定group的管理员角色，被取消管理员角色的人变成普通成员；</p>
     * @param groupId group id
     * @param userId 用户ID
     * @return 去掉操作的结果
     */
    @RequestMapping("revokeAdminRole")
    @RestReturn(value=String.class)
    public RestResponse revokeAdminRole(RevokeAdminRoleCommand cmd) {
    
        this.groupService.revokeAdminRole(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/resignAdminRole</b>
     * <p>自已主动去掉指定group的管理员角色</p>
     * @param groupId group id
     * @return 去掉的结果
     */
    @RequestMapping("resignAdminRole")
    @RestReturn(value=String.class)
    public RestResponse resignAdminRole(ResignAdminRoleCommand cmd) {
    
        this.groupService.resignAdminRole(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/approveAdminRole</b>
     * <p>批准指定用户成为指定group的管理员</p>
     * @param groupId group id
     * @param userId 用户ID
     * @return 批准的结果
     */
    @RequestMapping("approveAdminRole")
    @RestReturn(value=String.class)
    public RestResponse approveAdminRole(ApproveAdminRoleCommand cmd) {
    
        this.groupService.approveAdminRole(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/rejectAdminRole</b>
     * <p>拒绝指定用户成为指定group的管理员</p>
     * @param groupId group id
     * @param userId 用户ID
     * @return 拒绝的结果
     */
    @RequestMapping("rejectAdminRole")
    @RestReturn(value=String.class)
    public RestResponse rejectAdminRole(RejectAdminRoleCommand cmd) {
    
        this.groupService.rejectAdminRole(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/getAdminRoleStatus</b>
     * <p>获取自己在group的管理员状态</p>
     * @param groupId group id
     * @return 管理员状态，参考{@link com.everhomes.rest.group.GroupOpRequestStatus}
     */
    @RequestMapping("getAdminRoleStatus")
    @RestReturn(value=Byte.class)
    public RestResponse getAdminRoleStatus(GetAdminRoleStatusCommand cmd) {
        Byte result = groupService.getGroupAdimRoleStatus(cmd);
        
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/listAdminOpRequests</b>
     * <p>获取自身有关的成为指定group管理员的申请信息（含自己主动申请和被邀请）</p>
     */
    @RequestMapping("listAdminOpRequests")
    @RestReturn(value=ListAdminOpRequestCommandResponse.class)
    public RestResponse listAdminOpRequests(@Valid ListAdminOpRequestCommand cmd) {
    
        ListAdminOpRequestCommandResponse cmdResponse = this.groupService.listGroupOpRequests(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/listNearbyGroups</b>
     * <p>根据请求人所在的经纬度位置，获取周边的group信息</p>
     */
    @RequestMapping("listNearbyGroups")
    @RestReturn(value=ListNearbyGroupCommandResponse.class)
    public RestResponse listNearbyGroups(@Valid ListNearbyGroupCommand cmd) {
    
        ListNearbyGroupCommandResponse cmdResponse = this.groupService.listNearbyGroups(cmd);
        RestResponse response = new RestResponse(cmdResponse);
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/listGroupsByTag</b>
     * <p>根据指定的group可见性范围及标签获取group信息</p>
     */
    @RequestMapping("listGroupsByTag")
    @RestReturn(value=ListGroupCommandResponse.class)
    public RestResponse listGroupsByTag(@Valid ListGroupByTagCommand cmd) {
        ListGroupCommandResponse cmdResponse = this.groupService.listGroupByTag(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /group/search</b>
     * <p>根据请求人所在位置及指定的条件搜索符合条件的group信息</p>
     */
    @RequestMapping("search")
    @RestReturn(value=ListGroupCommandResponse.class)
    public RestResponse search(@Valid SearchGroupCommand cmd) {
    
        ListGroupCommandResponse cmdResponse = this.groupService.searchGroup(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    //Add by Janson
    @RequestMapping("deleteById")
    @RestReturn(value=String.class)
    public RestResponse deleteGroupById(@Valid DeleteGroupByIdCommand cmd) {
    
        this.groupService.deleteGroupByCreator(cmd.getGroupId().longValue());
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }    
    
    /**
     * <b>URL: /group/quitAndTransferPrivilege</b>
     * <p>退出并转移权限</p>
     */
    @RequestMapping("quitAndTransferPrivilege")
    @RestReturn(value=String.class)
    public RestResponse quitAndTransferPrivilege(QuitAndTransferPrivilegeCommand cmd){
    	groupService.quitAndTransferPrivilege(cmd);
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    
	/**
	 * <p>1.查询我加入的俱乐部发的贴子</p>
	 * <b>URL: /group/listUserGroupPost</b>
	 */
	@RequestMapping("listUserGroupPost")
	@RestReturn(ListUserGroupPostResponse.class)
	public RestResponse listUserGroupPost(ListUserGroupPostCommand cmd){
		return new RestResponse(groupService.listUserGroupPost(cmd));
	}

	/**
	 * <p>2.转移创建者权限</p>
	 * <b>URL: /group/transferCreatorPrivilege</b>
	 */
	@RequestMapping("transferCreatorPrivilege")
	@RestReturn(String.class)
	public RestResponse transferCreatorPrivilege(TransferCreatorPrivilegeCommand cmd){
		groupService.transferCreatorPrivilege(cmd);
		return new RestResponse();
	}

	/**
	 * <p>3.创建广播消息</p>
	 * <b>URL: /group/createBroadcast</b>
	 */
	@RequestMapping("createBroadcast")
	@RestReturn(CreateBroadcastResponse.class)
	public RestResponse createBroadcast(CreateBroadcastCommand cmd){
		return new RestResponse(groupService.createBroadcast(cmd));
	}

    /**
     * <p>删除广播</p>
     * <b>URL: /group/deleteBroadcastByToken</b>
     */
    @RequestMapping("deleteBroadcastByToken")
    @RestReturn(String.class)
    public RestResponse deleteBroadcastByToken(DeleteBroadcastByTokenCommand cmd){
        groupService.deleteBroadcastByToken(cmd);
        return new RestResponse();
    }

	/**
	 * <p>4.获取广播详情</p>
	 * <b>URL: /group/getBroadcastByToken</b>
	 */
	@RequireAuthentication(false)
	@RequestMapping("getBroadcastByToken")
	@RestReturn(GetBroadcastByTokenResponse.class)
	public RestResponse getBroadcastByToken(GetBroadcastByTokenCommand cmd){
		return new RestResponse(groupService.getBroadcastByToken(cmd));
	}

	/**
	 * <p>5.列出广播消息</p>
	 * <b>URL: /group/listBroadcasts</b>
	 */
	@RequestMapping("listBroadcasts")
    @RequireAuthentication(false)
	@RestReturn(ListBroadcastsResponse.class)
	public RestResponse listBroadcasts(ListBroadcastsCommand cmd){
		return new RestResponse(groupService.listBroadcasts(cmd));
	}

	/**
	 * <p>6.设置俱乐部参数</p>
	 * <b>URL: /group/setGroupParameters</b>
	 */
	@RequestMapping("setGroupParameters")
	@RestReturn(GroupParametersResponse.class)
	public RestResponse setGroupParameters(SetGroupParametersCommand cmd){
		return new RestResponse(groupService.setGroupParameters(cmd));
	}

	/**
	 * <p>7.获取俱乐部参数</p>
	 * <b>URL: /group/getGroupParameters</b>
	 */
	@RequestMapping("getGroupParameters")
	@RestReturn(GroupParametersResponse.class)
	public RestResponse getGroupParameters(GetGroupParametersCommand cmd){
		return new RestResponse(groupService.getGroupParameters(cmd));
	}

	/**
	 * <p>8.按审核状态查询圈</p>
	 * <b>URL: /group/listGroupsByApprovalStatus</b>
	 */
	@RequestMapping("listGroupsByApprovalStatus")
	@RestReturn(ListGroupsByApprovalStatusResponse.class)
	public RestResponse listGroupsByApprovalStatus(ListGroupsByApprovalStatusCommand cmd){
		return new RestResponse(groupService.listGroupsByApprovalStatus(cmd));
	}

	/**
	 * <p>9.审核通过俱乐部申请</p>
	 * <b>URL: /group/approvalGroupRequest</b>
	 */
	@RequestMapping("approvalGroupRequest")
	@RestReturn(String.class)
	public RestResponse approvalGroupRequest(ApprovalGroupRequestCommand cmd){
		groupService.approvalGroupRequest(cmd);
		return new RestResponse();
	}

	/**
	 * <p>10.拒绝俱乐部申请</p>
	 * <b>URL: /group/rejectGroupRequest</b>
	 */
	@RequestMapping("rejectGroupRequest")
	@RestReturn(String.class)
	public RestResponse rejectGroupRequest(RejectGroupRequestCommand cmd){
		groupService.rejectGroupRequest(cmd);
		return new RestResponse();
	}

	/**
	 * <p>11.创建圈分类</p>
	 * <b>URL: /group/createGroupCategory</b>
	 */
	@RequestMapping("createGroupCategory")
	@RestReturn(CreateGroupCategoryResponse.class)
	public RestResponse createGroupCategory(CreateGroupCategoryCommand cmd){
		return new RestResponse(groupService.createGroupCategory(cmd));
	}

	/**
	 * <p>12.更新圈分类</p>
	 * <b>URL: /group/updateGroupCategory</b>
	 */
	@RequestMapping("updateGroupCategory")
	@RestReturn(UpdateGroupCategoryResponse.class)
	public RestResponse updateGroupCategory(UpdateGroupCategoryCommand cmd){
		return new RestResponse(groupService.updateGroupCategory(cmd));
	}

	/**
	 * <p>13.删除圈分类</p>
	 * <b>URL: /group/deleteGroupCategory</b>
	 */
	@RequestMapping("deleteGroupCategory")
	@RestReturn(String.class)
	public RestResponse deleteGroupCategory(DeleteGroupCategoryCommand cmd){
		groupService.deleteGroupCategory(cmd);
		return new RestResponse();
	}

	/**
	 * <p>14.列出圈分类</p>
	 * <b>URL: /group/listGroupCategories</b>
	 */
	@RequestMapping("listGroupCategories")
	@RestReturn(ListGroupCategoriesResponse.class)
	public RestResponse listGroupCategories(ListGroupCategoriesCommand cmd){
		return new RestResponse(groupService.listGroupCategories(cmd));
	}

	/**
	 * <p>15.获取$俱乐部$占位符的名称</p>
	 * <b>URL: /group/getClubPlaceholderName</b>
	 */
	@RequestMapping("getClubPlaceholderName")
	@RestReturn(GetClubPlaceholderNameResponse.class)
	public RestResponse getClubPlaceholderName(GetClubPlaceholderNameCommand cmd){
		return new RestResponse(groupService.getClubPlaceholderName(cmd));
	}
	
	/**
	 * <p>16.剩余可发广播数</p>
	 * <b>URL: /group/getRemainBroadcastCount</b>
	 */
	@RequestMapping("getRemainBroadcastCount")
	@RestReturn(GetRemainBroadcastCountResponse.class)
	public RestResponse getRemainBroadcastCount(GetRemainBroadcastCountCommand cmd){
		return new RestResponse(groupService.getRemainBroadcastCount(cmd));
	}
	
	/**
	 * <p>17.获取分享信息</p>
	 * <b>URL: /group/getShareInfo</b>
	 */
	@RequireAuthentication(false)
	@RequestMapping("getShareInfo")
	@RestReturn(GetShareInfoResponse.class)
	public RestResponse getShareInfo(GetShareInfoCommand cmd){
		return new RestResponse(groupService.getShareInfo(cmd));
	}

	/**
	 * <p>18.取消俱乐部申请</p>
	 * <b>URL: /group/cancelGroupRequest</b>
	 */
	@RequestMapping("cancelGroupRequest")
	@RestReturn(String.class)
	public RestResponse cancelGroupRequest(CancelGroupRequestCommand cmd){
		groupService.cancelGroupRequest(cmd);
		return new RestResponse();
	}

    /**
     * <p>获取行业协会申请信息</p>
     * <b>URL: /group/findGuildApply</b>
     */
    @RequestMapping("findGuildApply")
    @RestReturn(GuildApplyDTO.class)
    public RestResponse findGuildApply(FindGuildApplyCommand cmd){

        GuildApplyDTO cmdResponse = groupService.findGuildApply(cmd);

        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>获取行业协会申请信息</p>
     * <b>URL: /group/findGuildApplyByGroupMemberId</b>
     */
    @RequestMapping("findGuildApplyByGroupMemberId")
    @RestReturn(GuildApplyDTO.class)
    public RestResponse findGuildApplyByGroupMemberId(FindGuildApplyByGroupMemberIdCommand cmd){

        GuildApplyDTO cmdResponse = groupService.findGuildApplyByGroupMemberId(cmd);

        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>获取行业协会申请信息</p>
     * <b>URL: /group/listGuildApplies</b>
     */
    @RequestMapping("listGuildApplies")
    @RestReturn(ListGuildAppliesResponse.class)
    public RestResponse listGuildApplies(ListGuildAppliesCommand cmd){

        ListGuildAppliesResponse cmdResponse = groupService.listGuildApplies(cmd);

        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>获取所述行业类型</p>
     * <b>URL: /group/findIndustryType</b>
     */
    @RequestMapping("findIndustryType")
    @RestReturn(IndustryTypeDTO.class)
    public RestResponse findIndustryType(FindIndustryTypeCommand cmd){

        IndustryTypeDTO cmdResponse = groupService.findIndustryType(cmd);

        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <p>获取所述行业类型</p>
     * <b>URL: /group/listIndustryTypes</b>
     */
    @RequestMapping("listIndustryTypes")
    @RestReturn(ListIndustryTypesResponse.class)
    public RestResponse listIndustryTypes(ListIndustryTypesCommand cmd){

        ListIndustryTypesResponse cmdResponse = groupService.listIndustryTypes(cmd);

        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
