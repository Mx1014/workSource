// @formatter:off
package com.everhomes.pkg;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.acl.Acl;
import com.everhomes.acl.AclAccessor;
import com.everhomes.acl.AclProvider;
import com.everhomes.acl.PrivilegeConstants;
import com.everhomes.acl.ResourceUserRoleResolver;
import com.everhomes.acl.Role;
import com.everhomes.address.Address;
import com.everhomes.address.ClaimAddressCommand;
import com.everhomes.address.ClaimedAddressInfo;
import com.everhomes.app.AppConstants;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.Family;
import com.everhomes.forum.Forum;
import com.everhomes.forum.ForumProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.namespace.Namespace;
import com.everhomes.region.RegionDescriptor;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.google.gson.Gson;

@Component
public class ClientPackageServiceImpl implements ClientPackageService {
    private Gson gson = new Gson();
    
    @Autowired
    private ClientPackageProvider clientPackageProvider;
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private CategoryProvider categoryProvider;
    
    @Autowired
    private ForumProvider forumProvider;
    
    @Autowired
    private AclProvider aclProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Override
    public long addPackage(AddClientPackageCommand cmd, MultipartFile[] files) {
    	User creator = UserContext.current().getUser();
        long creatorId = creator.getId();
        
        ClientPackage pkg = createNewClientPackage(cmd, creatorId);
        clientPackageProvider.createPackage(pkg);
        
        configurationProvider.getValue("", "/tmp");
        
    	return pkg.getId();
    }
    
    private ClientPackage createNewClientPackage(AddClientPackageCommand cmd, long creatorId) {
    	ClientPackage pkg = new ClientPackage();
    	
    	pkg.setName(cmd.getName());
    	pkg.setVersionCode(cmd.getVersionCode());
    	pkg.setPackageEdition(cmd.getPackageEdition());
    	pkg.setDevicePlatform(cmd.getDevicePlatform());
    	pkg.setDistributionChannel(cmd.getDistributionChannel());
    	pkg.setTag(cmd.getTag());
    	pkg.setJsonParams(cmd.getJsonParams());
    	pkg.setStatus(ClientPackageStatus.ACTIVE.getCode());
        pkg.setCreatorUid(creatorId);
        pkg.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        return pkg;
    }
    
//    @Override
//    public GroupDTO createGroup(CreateGroupCommand cmd) {
//        User user = UserContext.current().getUser();
//        long uid = user.getId();
//        
//        return this.dbProvider.execute((status) -> {
//            Group group = new Group();
//            group.setCreatorUid(uid);
//            group.setName(cmd.getName());
//            group.setAvatar(cmd.getAvatar());
//            group.setDescription(cmd.getDescription());
//            group.setDiscriminator(GroupDiscriminator.GROUP.getCode());
//            
//            if(cmd.getCategoryId() != null) {
//                Category category = this.categoryProvider.findCategoryById(cmd.getCategoryId());
//                if(category == null)
//                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
//                            ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid category");
//                
//                group.setCategoryId(cmd.getCategoryId());
//                group.setCategoryPath(category.getPath());
//            }
//            
//            byte privateFlag = 0;
//            if(cmd.getPrivateFlag() != null)
//                privateFlag = cmd.getPrivateFlag().byteValue();
//            group.setPrivateFlag(privateFlag);
//            
//            // join policy is not exposed current in API, derive it from its visibility flag
//            if(privateFlag != 0) {
//                group.setJoinPolicy(GroupJoinPolicy.NEED_APPROVE.getCode());
//                
//                Acl acl = new Acl();
//                acl.setOwnerType(EntityType.GROUP.getCode());
//                acl.setPrivilegeId(PrivilegeConstants.Visible);
//                acl.setGrantType((byte)1);
//                acl.setCreatorUid(User.ROOT_UID);
//                acl.setRoleId(Role.ResourceUser);
//                acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//                this.aclProvider.createAcl(acl);
//            } else {
//                Acl acl = new Acl();
//                acl.setOwnerType(EntityType.GROUP.getCode());
//                acl.setPrivilegeId(PrivilegeConstants.Visible);
//                acl.setGrantType((byte)1);
//                acl.setCreatorUid(User.ROOT_UID);
//                acl.setRoleId(Role.AuthenticatedUser);
//                acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//                this.aclProvider.createAcl(acl);
//            }
//            
//            if(cmd.getTag() != null)
//                group.setTag(cmd.getTag());
//           
//            group.setStatus(GroupAdminStatus.ACTIVE.getCode());
//            this.groupProvider.createGroup(group);
//    
//            // create the group owned forum and save it
//            Forum forum = createGroupForum(group);
//            group.setOwningForumId(forum.getId());
//            this.groupProvider.updateGroup(group);
//            
//            GroupMember member = new GroupMember();
//            member.setGroupId(group.getId());
//            member.setApproveTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//            member.setCreatorUid(uid);
//            member.setMemberType(EntityType.USER.getCode());
//            member.setMemberId(uid);
//            member.setMemberNickName(user.getAvatar());
//            member.setMemberRole(Role.ResourceCreator);
//            member.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
//            this.groupProvider.createGroupMember(member);
//            
//            if(cmd.getExplicitRegionDescriptorsJson() != null) {
//                RegionDescriptor[] regionDescriptors = gson.fromJson(cmd.getExplicitRegionDescriptorsJson(),
//                        RegionDescriptor[].class);
//                
//                if(regionDescriptors != null && regionDescriptors.length > 0) {
//                    for(RegionDescriptor regionDescriptor: regionDescriptors) {
//                        GroupVisibilityScope scope = new GroupVisibilityScope();
//                        scope.setOwnerId(group.getId());
//                        scope.setScopeCode(regionDescriptor.getRegionScope());
//                        scope.setScopeId(regionDescriptor.getRegionId());
//                        
//                        this.groupProvider.createGroupVisibleScope(scope);
//                    }
//                }
//            }
//            
//            return toGroupDTO(group);
//        });
//    }
//    
//    @Override
//    public GroupDTO updateGroup(UpdateGroupCommand cmd) {
//        if(cmd.getGroupId() == null)
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
//                    "Invalid parameter, groupId could not be empty");
//        
//        User user = UserContext.current().getUser();
//        long groupId = cmd.getGroupId().longValue();
//        checkGroupPrivilege(user.getId(), groupId, PrivilegeConstants.Write);
//        
//        Group group = this.groupProvider.findGroupById(groupId);
//        if(group == null)
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
//                    "Invalid parameter, could not find the group object");
//        
//        if(cmd.getAvatar() != null)
//            group.setAvatar(cmd.getAvatar());
//        
//        if(cmd.getDescription() != null)
//            group.setDescription(cmd.getDescription());
//        
//        if(cmd.getName() != null)
//            group.setName(cmd.getName());
//        
//        if(cmd.getTag() != null)
//            group.setTag(cmd.getTag());
//        
//        if(cmd.getVisibilityScope() != null)
//            group.setVisibilityScope(cmd.getVisibilityScope());
//        if(cmd.getVisibilityScopeId() != null)
//            group.setVisibilityScopeId(cmd.getVisibilityScopeId());
//        
//        if(cmd.getCategoryId() != null) {
//            group.setCategoryId(cmd.getCategoryId());
//            
//            Category category = this.categoryProvider.findCategoryById(cmd.getCategoryId());
//            if(category != null)
//                group.setCategoryPath(category.getPath());
//        }
//        
//        this.groupProvider.updateGroup(group);
//        return this.toGroupDTO(group);
//    }
//    
//    @Override
//    public GroupDTO getGroup(long groupId) {
//        User user = UserContext.current().getUser();
//        checkGroupPrivilege(user.getId(), groupId, PrivilegeConstants.Visible);
//        
//        Group group = this.groupProvider.findGroupById(groupId);
//        if(group == null)
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
//                    "Invalid group id parameter, could not find the group");
//        
//        return this.toGroupDTO(group);
//    }
//    
//    @Override
//    public void joinGroup(long groupId) {
//        User user = UserContext.current().getUser();
//        
//        Group group = this.groupProvider.findGroupById(groupId);
//        if(group == null)
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
//                    "Unable to find the group");
//
//        GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(groupId, EntityType.USER.getCode(), user.getId());
//        if(member == null) {
//            member = new GroupMember();
//            member.setCreatorUid(user.getId());
//            member.setGroupId(groupId);
//            member.setMemberType(EntityType.USER.getCode());
//            member.setMemberId(user.getId());
//            member.setMemberRole(Role.ResourceUser);
//            member.setMemberNickName(user.getNickName());
//            member.setMemberAvatar(user.getAvatar());
//            
//            if(GroupJoinPolicy.fromCode(group.getJoinPolicy()) == GroupJoinPolicy.FREE) {
//                member.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
//            } else {
//                member.setMemberStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
//                
//                // TODO we need to send out a notification message
//            }
//            this.groupProvider.createGroupMember(member);
//        } else {
//            if(GroupMemberStatus.fromCode(member.getMemberStatus()) == GroupMemberStatus.WAITING_FOR_ACCEPTANCE) {
//                member.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
//                member.setMemberNickName(user.getNickName());
//                member.setMemberAvatar(user.getAvatar());
//                
//                this.groupProvider.updateGroupMember(member);
//                
//                // TODO send out notification message
//            }
//        }
//    }
//    
//    @Override
//    public void inviteToJoinGroup(long groupId, long userId) {
//        User user = UserContext.current().getUser();
//        
//        Group group = this.groupProvider.findGroupById(groupId);
//        if(group == null)
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
//                    "Unable to find the group");
//        
//        GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(groupId, EntityType.USER.getCode(), userId);
//        if(member != null) {
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//                 "Target user is either already in the group or in the joining process");
//        }
//        
//        checkGroupPrivilege(user.getId(), groupId, PrivilegeConstants.GroupInviteJoin);
//        member = new GroupMember();
//        member.setCreatorUid(user.getId());
//        member.setGroupId(groupId);
//        member.setMemberType(EntityType.USER.getCode());
//        member.setMemberId(userId);
//        member.setMemberRole(Role.ResourceUser);
//        member.setMemberStatus(GroupMemberStatus.WAITING_FOR_ACCEPTANCE.getCode());
//        this.groupProvider.createGroupMember(member);
//        
//        // TODO send invitation message
//    }
//    
//    @Override
//    public void leaveGroup(long groupId) {
//        User user = UserContext.current().getUser();
//        
//        Group group = this.groupProvider.findGroupById(groupId);
//        if(group == null)
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
//                    "Unable to find the group");
//
//        GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(groupId, EntityType.USER.getCode(), user.getId());
//        if(member != null) {
//            this.groupProvider.deleteGroupMember(member);
//        }
//    }
//    
//    @Override
//    public ListMemberCommandResponse listMembersInRole(ListMemberInRoleCommand cmd) {
//        User user = UserContext.current().getUser();
//        
//        if(cmd.getGroupId() == null || cmd.getRoleId() == null)
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
//                    "groupId or roleId paramter can not be null or empty");
//        
//        Group group = this.groupProvider.findGroupById(cmd.getGroupId().longValue());
//        if(group == null)
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
//                    "Unable to find the group");
//        
//        checkGroupPrivilege(user.getId(), cmd.getGroupId(), PrivilegeConstants.GroupListMember);
//        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
//        CrossShardListingLocator locator = new CrossShardListingLocator(group.getId());
//        locator.setAnchor(cmd.getPageAnchor());
//        
//        List<GroupMember> members = this.groupProvider.queryGroupMembers(locator, pageSize + 1,
//            (loc, query) -> {
//                query.addConditions(Tables.EH_GROUP_MEMBERS.MEMBER_ROLE.eq(cmd.getRoleId())
//                        .and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.ACTIVE.getCode())));
//                
//                return query;
//            });
//        
//        Long nextPageAnchor = null;
//        if(members.size() > pageSize) {
//            members.remove(members.size() - 1);
//            nextPageAnchor = members.get(members.size() -1).getId();
//        }
//
//        List<GroupMemberDTO> memberDtos = members.stream()
//                .map((r) -> { return ConvertHelper.convert(r, GroupMemberDTO.class);})
//                .collect(Collectors.toList());
//        return new ListMemberCommandResponse(nextPageAnchor, memberDtos); 
//    }
//    
//    public ListMemberCommandResponse listMembersInStatus(ListMemberInStatusCommand cmd) {
//        User user = UserContext.current().getUser();
//        
//        if(cmd.getGroupId() == null || cmd.getStatus() == null)
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
//                    "groupId or status paramter can not be null or empty");
//        
//        Group group = this.groupProvider.findGroupById(cmd.getGroupId().longValue());
//        if(group == null)
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
//                    "Unable to find the group");
//        
//        checkGroupPrivilege(user.getId(), cmd.getGroupId(), PrivilegeConstants.GroupListMember);
//        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
//        CrossShardListingLocator locator = new CrossShardListingLocator(group.getId());
//        locator.setAnchor(cmd.getPageAnchor());
//        
//        List<GroupMember> members = this.groupProvider.queryGroupMembers(locator, pageSize + 1,
//            (loc,query) -> {
//                query.addConditions(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(cmd.getStatus()));
//                return query;
//            });
//        
//        Long nextPageAnchor = null;
//        if(members.size() > pageSize) {
//            members.remove(members.size() - 1);
//            nextPageAnchor = members.get(members.size() -1).getId();
//        }
//
//        List<GroupMemberDTO> memberDtos = members.stream()
//                .map((r) -> { return ConvertHelper.convert(r, GroupMemberDTO.class);})
//                .collect(Collectors.toList());
//        return new ListMemberCommandResponse(nextPageAnchor, memberDtos); 
//    }
//    
//    @Override
//    public void requestToBeAdmin(long groupId) {
//        User user = UserContext.current().getUser();
//        
//        Group group = this.groupProvider.findGroupById(groupId);
//        if(group == null)
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
//                    "Unable to find the group");
//
//        checkGroupPrivilege(user.getId(), groupId, PrivilegeConstants.GroupRequestAdminRole);
//        
//        GroupOpRequest request = this.groupProvider.findGroupOpRequestByRequestor(groupId, user.getId());
//        if(request == null) {
//            request = new GroupOpRequest();
//            request.setGroupId(groupId);
//            request.setReqestorUid(user.getId());
//            request.setOperationType(GroupOpType.REQUEST_ADMIN_ROLE.getCode());
//            request.setStatus(GroupOpRequestStatus.REQUESTING.getCode());
//            this.groupProvider.createGroupOpRequest(request);
//        }
//    }
//    
//    private void checkGroupPrivilege(long uid, long groupId, long privilege) {
//        ResourceUserRoleResolver resolver = PlatformContext.getComponent(EntityType.GROUP.getCode());
//        List<Long> roles = resolver.determineRoleInResource(uid, groupId, EntityType.GROUP.getCode(), groupId);
//        if(!this.aclProvider.checkAccess(EntityType.GROUP.getCode(), groupId, EntityType.USER.getCode(), uid, privilege, 
//                roles))
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
//                    "Insufficient privilege");
//    }
//    
//    private Forum createGroupForum(Group group) {
//        Forum forum = new Forum();
//        forum.setOwnerType(EntityType.GROUP.getCode());
//        forum.setOwnerId(group.getId());
//        forum.setAppId(AppConstants.APPID_FORUM);
//        forum.setNamespaceId(Namespace.DEFAULT_NAMESPACE);
//        forum.setName(EntityType.GROUP.getCode() + "-" + group.getId());
//        
//        this.forumProvider.createForum(forum);
//        return forum;
//    }
//    
//    private GroupDTO toGroupDTO(Group group) {
//        GroupDTO groupDto = new GroupDTO();
//        
//        groupDto.setId(group.getId());
//        groupDto.setOwningForumId(group.getOwningForumId());
//        groupDto.setAvatar(group.getAvatar());
//        groupDto.setCategoryId(group.getCategoryId());
//        if(group.getCategoryId() != null) {
//            Category category = this.categoryProvider.findCategoryById(group.getCategoryId());
//            if(category != null)
//                groupDto.setCategoryName(category.getName());
//        }
//        groupDto.setCreateTime(DateHelper.getDateDisplayString(TimeZone.getTimeZone("GMT"), 
//            group.getCreateTime().getTime()));
//        groupDto.setCreatorUid(group.getCreatorUid());
//        groupDto.setJoinPolicy(group.getJoinPolicy());
//        groupDto.setDescription(group.getDescription());
//        groupDto.setMemberCount(group.getMemberCount());
//        groupDto.setName(group.getName());
//        groupDto.setPrivateFlag(group.getPrivateFlag());
//        groupDto.setTag(group.getTag());
//        
//        memberInfoToGroupDTO(UserContext.current().getUser().getId(), groupDto, group);
//        return groupDto;
//    }
//    
//    private void memberInfoToGroupDTO(long uid, GroupDTO groupDto, Group group) {
//        //
//        // compute member role ourselves instead of using GroupUserRoleResolver,
//        // it is more efficient to do in this way due to the reason that
//        // we need to tell whether or not the user is a member of the group
//        //
//        GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(group.getId(), 
//            EntityType.USER.getCode(), uid);
//
//        List<Long> userInRoles = new ArrayList<>();
//        userInRoles.add(Role.AuthenticatedUser);
//        if(member != null) {
//            groupDto.setMemberOf((byte)1);
//            groupDto.setMemberNickName(member.getMemberNickName());
//            groupDto.setMemberConfigFlag(member.getMemberConfigFlag());
//            
//            if(member.getMemberRole().longValue() == Role.ResourceAdmin || 
//                    member.getMemberRole().longValue() == Role.ResourceOperator ||
//                    member.getMemberRole().longValue() == Role.ResourceCreator) {
//                if(GroupMemberStatus.fromCode(member.getMemberStatus()) == GroupMemberStatus.ACTIVE)
//                    userInRoles.add(Role.ResourceUser);
//            }
//            
//            AclAccessor groupPrivileges = this.aclProvider.getAccessor(
//                    EntityType.GROUP.getCode(), group.getId(), userInRoles);
//            groupDto.setMemberGroupPrivileges(groupPrivileges.getGrantPrivileges());
//            
//            AclAccessor forumPrivileges = this.aclProvider.getAccessor(
//                    EntityType.FORUM.getCode(), group.getOwningForumId(), userInRoles);
//            groupDto.setMemberForumPrivileges(forumPrivileges.getGrantPrivileges());
//        } else {
//            groupDto.setMemberOf((byte)0);
//        }
//    }
}
