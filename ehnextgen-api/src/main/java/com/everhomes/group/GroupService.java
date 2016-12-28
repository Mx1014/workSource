// @formatter:off
package com.everhomes.group;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.forum.admin.SearchTopicAdminCommand;
import com.everhomes.rest.forum.admin.SearchTopicAdminCommandResponse;
import com.everhomes.rest.group.AcceptJoinGroupInvitation;
import com.everhomes.rest.group.ApprovalGroupRequestCommand;
import com.everhomes.rest.group.ApproveAdminRoleCommand;
import com.everhomes.rest.group.ApproveJoinGroupRequestCommand;
import com.everhomes.rest.group.CancelGroupRequestCommand;
import com.everhomes.rest.group.CommandResult;
import com.everhomes.rest.group.CreateBroadcastCommand;
import com.everhomes.rest.group.CreateBroadcastResponse;
import com.everhomes.rest.group.CreateGroupCategoryCommand;
import com.everhomes.rest.group.CreateGroupCategoryResponse;
import com.everhomes.rest.group.CreateGroupCommand;
import com.everhomes.rest.group.DeleteGroupCategoryCommand;
import com.everhomes.rest.group.GetAdminRoleStatusCommand;
import com.everhomes.rest.group.GetBroadcastByTokenCommand;
import com.everhomes.rest.group.GetBroadcastByTokenResponse;
import com.everhomes.rest.group.GetClubPlaceholderNameCommand;
import com.everhomes.rest.group.GetClubPlaceholderNameResponse;
import com.everhomes.rest.group.GetGroupCommand;
import com.everhomes.rest.group.GetGroupMemberSnapshotCommand;
import com.everhomes.rest.group.GetGroupParametersCommand;
import com.everhomes.rest.group.GetRemainBroadcastCountCommand;
import com.everhomes.rest.group.GetRemainBroadcastCountResponse;
import com.everhomes.rest.group.GetShareInfoCommand;
import com.everhomes.rest.group.GetShareInfoResponse;
import com.everhomes.rest.group.GroupDTO;
import com.everhomes.rest.group.GroupMemberDTO;
import com.everhomes.rest.group.GroupMemberSnapshotDTO;
import com.everhomes.rest.group.InviteToBeAdminCommand;
import com.everhomes.rest.group.InviteToJoinGroupByFamilyCommand;
import com.everhomes.rest.group.InviteToJoinGroupByPhoneCommand;
import com.everhomes.rest.group.InviteToJoinGroupCommand;
import com.everhomes.rest.group.LeaveGroupCommand;
import com.everhomes.rest.group.ListAdminOpRequestCommand;
import com.everhomes.rest.group.ListAdminOpRequestCommandResponse;
import com.everhomes.rest.group.ListBroadcastsCommand;
import com.everhomes.rest.group.ListBroadcastsResponse;
import com.everhomes.rest.group.ListGroupByTagCommand;
import com.everhomes.rest.group.ListGroupCategoriesCommand;
import com.everhomes.rest.group.ListGroupCategoriesResponse;
import com.everhomes.rest.group.ListGroupCommand;
import com.everhomes.rest.group.ListGroupCommandResponse;
import com.everhomes.rest.group.ListGroupWaitingApprovalsCommand;
import com.everhomes.rest.group.ListGroupWaitingApprovalsCommandResponse;
import com.everhomes.rest.group.ListGroupsByApprovalStatusCommand;
import com.everhomes.rest.group.ListGroupsByApprovalStatusResponse;
import com.everhomes.rest.group.ListGroupsByNamespaceIdCommand;
import com.everhomes.rest.group.ListMemberCommandResponse;
import com.everhomes.rest.group.ListMemberInRoleCommand;
import com.everhomes.rest.group.ListMemberInStatusCommand;
import com.everhomes.rest.group.ListNearbyGroupCommand;
import com.everhomes.rest.group.ListNearbyGroupCommandResponse;
import com.everhomes.rest.group.ListPublicGroupCommand;
import com.everhomes.rest.group.ListUserGroupPostCommand;
import com.everhomes.rest.group.ListUserGroupPostResponse;
import com.everhomes.rest.group.QuitAndTransferPrivilegeCommand;
import com.everhomes.rest.group.RejectAdminRoleCommand;
import com.everhomes.rest.group.RejectGroupRequestCommand;
import com.everhomes.rest.group.RejectJoinGroupInvitation;
import com.everhomes.rest.group.RejectJoinGroupRequestCommand;
import com.everhomes.rest.group.RequestAdminRoleCommand;
import com.everhomes.rest.group.RequestToJoinGroupCommand;
import com.everhomes.rest.group.ResignAdminRoleCommand;
import com.everhomes.rest.group.RevokeAdminRoleCommand;
import com.everhomes.rest.group.RevokeGroupMemberCommand;
import com.everhomes.rest.group.SearchGroupCommand;
import com.everhomes.rest.group.SearchGroupTopicAdminCommand;
import com.everhomes.rest.group.SetGroupParametersCommand;
import com.everhomes.rest.group.GroupParametersResponse;
import com.everhomes.rest.group.TransferCreatorPrivilegeCommand;
import com.everhomes.rest.group.UpdateGroupCategoryCommand;
import com.everhomes.rest.group.UpdateGroupCategoryResponse;
import com.everhomes.rest.group.UpdateGroupCommand;
import com.everhomes.rest.group.UpdateGroupMemberCommand;
import com.everhomes.rest.openapi.CreateBusinessGroupResponse;
import com.everhomes.rest.ui.group.ListNearbyGroupBySceneCommand;

public interface GroupService {
//    GroupDTO createGroup(CreateGroupCommand cmd);
    GroupDTO updateGroup(UpdateGroupCommand cmd);
    GroupDTO getGroup(GetGroupCommand cmd);
    List<GroupDTO> listUserRelatedGroups();
    List<GroupDTO> listPublicGroups(ListPublicGroupCommand cmd);
    
    /** 更新组成员信息 */
    GroupMemberDTO updateGroupMember(UpdateGroupMemberCommand cmd);
    
    /** 获取成员简要信息 */
    public GroupMemberSnapshotDTO getGroupMemberSnapshot(GetGroupMemberSnapshotCommand cmd);
    
    /** 自己主动申请加入group */
    void requestToJoinGroup(RequestToJoinGroupCommand cmd);
    
    /** 扫二维码申请加入 */
    void requestToJoinGroupByQRCode(RequestToJoinGroupCommand cmd);
    
    /** 邀请别人加入group */
    List<CommandResult> inviteToJoinGroup(InviteToJoinGroupCommand cmd);
    
    /** 通过电话号码邀请别人加入group */
    List<CommandResult> inviteToJoinGroupByPhone(InviteToJoinGroupByPhoneCommand cmd);
    
    /** 通过家庭把整个家庭成员邀请加入group */
    List<CommandResult> inviteToJoinGroupByFamily(InviteToJoinGroupByFamilyCommand cmd);
    
    /** 接受别人邀请自己加入group的请求 */
    void acceptJoinGroupInvitation(AcceptJoinGroupInvitation cmd);
    
    /** 拒绝别人邀请自己加入group的请求 */
    void rejectJoinGroupInvitation(RejectJoinGroupInvitation cmd);

    /** 批准指定用户加入group的申请 */
    void approveJoinGroupRequest(ApproveJoinGroupRequestCommand cmd);

    /** 拒绝指定用户加入group的申请 */
    void rejectJoinGroupRequest(RejectJoinGroupRequestCommand cmd);
    
    /** 自己主动退出group */
    void leaveGroup(LeaveGroupCommand cmd);
    
    /** 把指定group成员从group中踢出 */
    void revokeGroupMember(RevokeGroupMemberCommand cmd);
    
    /** 列出用户相关的被邀请加入group、等待接受/拒绝的处理列表 */
    List<GroupMemberDTO> listGroupWaitingAcceptances();
    
    /** 列出用户可审批的、加入group的请求列表 */
    ListGroupWaitingApprovalsCommandResponse listGroupWaitingApprovals(ListGroupWaitingApprovalsCommand cmd);
    
    /** 列出指定组、指定角色的管理员列表（角色可以多种） */
    ListMemberCommandResponse listMembersInRole(ListMemberInRoleCommand cmd);
    
    ListMemberCommandResponse listMembersInStatus(ListMemberInStatusCommand cmd);
    
    /** 主动申请成为group管理员 */
    void requestToBeAdmin(RequestAdminRoleCommand cmd);
    
    /** 邀请别人成为group管理员 */
    void inviteToBeAdmin(InviteToBeAdminCommand cmd);
    
    /** 批准 成为管理员的申请 */
    void approveAdminRole(ApproveAdminRoleCommand cmd);
    
    /** 拒绝 成为管理员的申请 */
    void rejectAdminRole(RejectAdminRoleCommand cmd);
    
    /** 主动辞去管理员角色 */
    void resignAdminRole(ResignAdminRoleCommand cmd);
    
    /** 取消某人的管理员角色 */
    void revokeAdminRole(RevokeAdminRoleCommand cmd);
    
    /** 获取管理员状态：是否是管理员 */
    Byte getGroupAdimRoleStatus(GetAdminRoleStatusCommand cmd);
    
    ListAdminOpRequestCommandResponse listGroupOpRequests(ListAdminOpRequestCommand cmd);
    
    ListGroupCommandResponse listGroupByTag(ListGroupByTagCommand cmd);
    ListNearbyGroupCommandResponse listNearbyGroups(ListNearbyGroupCommand cmd);
    ListGroupCommandResponse searchGroup(SearchGroupCommand cmd);
    ListGroupCommandResponse listGroups(ListGroupCommand cmd );
    
    /**
     * 管理员搜索圈帖子信息
     * @param cmd 命令
     * @return 帖子列表
     */
    SearchTopicAdminCommandResponse searchGroupTopics(SearchGroupTopicAdminCommand cmd);
    
    void deleteGroup(long groupId);
    
    //Just for route message
    List<GroupMember> listMessageGroupMembers(ListingLocator locator, int pageSize);
    void deleteGroupByCreator(long groupId);
    ListGroupCommandResponse listGroupsByNamespaceId(ListGroupsByNamespaceIdCommand cmd);
    ListNearbyGroupCommandResponse listNearbyGroupsByScene(ListNearbyGroupBySceneCommand cmd);
	void quitAndTransferPrivilege(QuitAndTransferPrivilegeCommand cmd);
	

	public ListUserGroupPostResponse listUserGroupPost(ListUserGroupPostCommand cmd);


	public void transferCreatorPrivilege(TransferCreatorPrivilegeCommand cmd);


	public CreateBroadcastResponse createBroadcast(CreateBroadcastCommand cmd);


	public GetBroadcastByTokenResponse getBroadcastByToken(GetBroadcastByTokenCommand cmd);


	public ListBroadcastsResponse listBroadcasts(ListBroadcastsCommand cmd);


	public GroupParametersResponse setGroupParameters(SetGroupParametersCommand cmd);


	public GroupParametersResponse getGroupParameters(GetGroupParametersCommand cmd);


	public ListGroupsByApprovalStatusResponse listGroupsByApprovalStatus(ListGroupsByApprovalStatusCommand cmd);


	public void approvalGroupRequest(ApprovalGroupRequestCommand cmd);


	public void rejectGroupRequest(RejectGroupRequestCommand cmd);


	public CreateGroupCategoryResponse createGroupCategory(CreateGroupCategoryCommand cmd);


	public UpdateGroupCategoryResponse updateGroupCategory(UpdateGroupCategoryCommand cmd);


	public void deleteGroupCategory(DeleteGroupCategoryCommand cmd);


	public ListGroupCategoriesResponse listGroupCategories(ListGroupCategoriesCommand cmd);
	GetClubPlaceholderNameResponse getClubPlaceholderName(GetClubPlaceholderNameCommand cmd);
	RestResponse createAGroup(CreateGroupCommand cmd);
	GetRemainBroadcastCountResponse getRemainBroadcastCount(GetRemainBroadcastCountCommand cmd);
	GetShareInfoResponse getShareInfo(GetShareInfoCommand cmd);
	void cancelGroupRequest(CancelGroupRequestCommand cmd);
	GroupDTO createBusinessGroup(String groupName);
	void joinBusinessGroup(Long groupId);

}
