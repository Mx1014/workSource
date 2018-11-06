// @formatter:off
package com.everhomes.group;

import com.everhomes.acl.*;
import com.everhomes.appurl.AppUrlService;
import com.everhomes.auditlog.AuditLog;
import com.everhomes.auditlog.AuditLogProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.broadcast.Broadcast;
import com.everhomes.broadcast.BroadcastProvider;
import com.everhomes.bus.LocalEventBus;
import com.everhomes.bus.LocalEventContext;
import com.everhomes.bus.SystemEvent;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.controller.XssCleaner;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.FamilyProvider;
import com.everhomes.forum.Forum;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.ForumService;
import com.everhomes.forum.Post;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.acl.RoleConstants;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.appurl.AppUrlDTO;
import com.everhomes.rest.appurl.GetAppInfoCommand;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.common.QuestionMetaActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.forum.*;
import com.everhomes.rest.forum.admin.PostAdminDTO;
import com.everhomes.rest.forum.admin.SearchTopicAdminCommandResponse;
import com.everhomes.rest.group.*;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.region.RegionDescriptor;
import com.everhomes.rest.search.GroupQueryResult;
import com.everhomes.rest.sensitiveWord.FilterWordsCommand;
import com.everhomes.rest.ui.group.ListNearbyGroupBySceneCommand;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.rest.visibility.VisibilityScope;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.search.GroupSearcher;
import com.everhomes.search.PostAdminQueryFilter;
import com.everhomes.search.PostSearcher;
import com.everhomes.sensitiveWord.SensitiveWordService;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhForumPosts;
import com.everhomes.server.schema.tables.EhUsers;
import com.everhomes.server.schema.tables.pojos.EhGroupMembers;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.*;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.*;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.version.VersionService;
import com.google.gson.Gson;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GroupServiceImpl implements GroupService {   
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);
    
    private Gson gson = new Gson();
    
    @Autowired
    private CoordinationProvider coordinationProvider;
    
    @Autowired
    private AppUrlService appUrlService;
    
    @Autowired
    private GroupProvider groupProvider;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private UserService userService;
    
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
    
    @Autowired
    private VersionService versionService;
    
    @Autowired
    GroupSearcher groupSearcher;

    @Autowired
    private MessagingService messagingService;
    
    @Autowired
    private LocaleTemplateService localeTemplateService;
    
    @Autowired
    private ContentServerService contentServerService;
    
    @Autowired
    private AuditLogProvider auditLogProvider;
    
    @Autowired
    private FamilyProvider familyProvider;
    
    @Autowired
    private PostSearcher postSearcher;
    
    @Autowired
    private ForumService forumService;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private OrganizationProvider organizationProvider;
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
    private BroadcastProvider broadcastProvider;
    
    @Autowired
    private GroupSettingProvider groupSettingProvider;
    
    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @Autowired
    private GroupMemberLogProvider groupMemberLogProvider;

    @Autowired
    private LocaleStringService localeStringService;

    @Autowired
    private SensitiveWordService sensitiveWordService;

    //敏感词检测  --add by yanlong.liang 20180614
    private void filter(CreateGroupCommand cmd) {
        if (cmd == null) {
            return;
        }
        List<String> textList = new ArrayList<>();
        FilterWordsCommand command = new FilterWordsCommand();
        Byte moduleType = (byte)4;
        if (cmd.getClubType() != null && cmd.getClubType() ==ClubType.GUILD.getCode()) {
            moduleType = (byte)5;
        }
        command.setModuleType(moduleType);
        if (!StringUtils.isEmpty(cmd.getName())) {
            textList.add(cmd.getName());
        }
        if (!StringUtils.isEmpty(cmd.getDescription())) {
            textList.add(cmd.getDescription());
        }
        command.setTextList(textList);
        command.setCommunityId(cmd.getVisibleRegionId());
        this.sensitiveWordService.filterWords(command);
    }


    //因为提示“不允许创建俱乐部”中的俱乐部三个字是可配的，所以这里这样处理下，add by tt, 20161102
    @Override
    public RestResponse createAGroup(CreateGroupCommand cmd) {
        //xss过滤
        String content = XssCleaner.clean(cmd.getDescription());
        cmd.setDescription(content);
        //敏感词过滤
        filter(cmd);

    	Integer namespaceId =  UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
    	//创建俱乐部需要从后台获取设置的参数判断允不允许创建俱乐部， add by tt, 20161102
    	GroupSetting groupSetting = null;
    	if (cmd.getPrivateFlag() != null && GroupPrivacy.fromCode(cmd.getPrivateFlag()) == GroupPrivacy.PUBLIC) {
    		groupSetting = groupSettingProvider.findGroupSettingByNamespaceId(namespaceId, cmd.getClubType());
        	if (groupSetting != null && groupSetting.getCreateFlag() != null && TrueOrFalseFlag.fromCode(groupSetting.getCreateFlag()) == TrueOrFalseFlag.FALSE && !checkAdmin(cmd.getVisibleRegionId())) {
        		Map<String, Object> map = new HashMap<String, Object>();
                map.put("clubPlaceholderName", getClubPlaceholderName(namespaceId));
               
                String scope = GroupNotificationTemplateCode.SCOPE;
                String errorDescription = localeTemplateService.getLocaleTemplateString(scope, GroupNotificationTemplateCode.GROUP_NOT_ALLOW_TO_CREATE_GROUP, getLocale(), map, "");
                
        		return new RestResponse(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, errorDescription);
    		}
        	// 判断简介
            // 产品定义为至少一个字  add by xq.tian      2017/03/10
        	if (cmd.getDescription() == null || cmd.getDescription().length() < 1) {
        		throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, GroupServiceErrorCode.ERROR_GROUP_DESCRIPTION_LENGTH,
    					"description length cannot be less than 10!");
			}
		}

		if(cmd.getClubType() == null){
    	    cmd.setClubType(ClubType.NORMAL.getCode());
        }

    	return new RestResponse(createGroup(cmd, groupSetting));
    }
    
    private boolean checkAdmin(Long communityId) {
    	List<Long> organizationIdList = organizationService.getOrganizationIdsTreeUpToRoot(communityId);
    	for (Long organizationId : organizationIdList) {
			if (rolePrivilegeService.checkAdministrators(organizationId)) {
				return true;
			}
		}
    	return false;
    }
    
    private String getLocale() {
        User user = UserContext.current().getUser();
        if(user != null && user.getLocale() != null)
            return user.getLocale();
        return Locale.SIMPLIFIED_CHINESE.toString();
    }

    private void checkBlacklist(String ownerType, Long ownerId){
        ownerType = StringUtils.isEmpty(ownerType) ? "" : ownerType;
        ownerId = null == ownerId ? 0L : ownerId;
        Long userId = UserContext.current().getUser().getId();
        UserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserBlacklistAuthority(userId, ownerType, ownerId, PrivilegeConstants.BLACKLIST_CLUP);
    }

    private GroupDTO createGroup(CreateGroupCommand cmd, GroupSetting groupSetting) {
    	Integer namespaceId = (cmd.getNamespaceId() == null) ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId();
    	
        long startTime = System.currentTimeMillis();
        User user = UserContext.current().getUser();
        long userId = user.getId();
        
        GroupDTO groupDto = this.dbProvider.execute((status) -> {
            Group group = new Group();
            group.setCreatorUid(userId);
            group.setName(cmd.getName());
            group.setAvatar(cmd.getAvatar());
            group.setDescription(cmd.getDescription());
            group.setDescriptionType(cmd.getDescriptionType());

            group.setDiscriminator(GroupDiscriminator.GROUP.getCode());
            group.setMemberCount(1L); // 创建者也参与人数计算
            
            group.setNamespaceId(namespaceId);
            
            // 对于3.1.0以前的版本，接口并没有regionType、regionId两个字段，此时使用小区代替 modify by lqs 20151104
            VisibleRegionType regionType = VisibleRegionType.fromCode(cmd.getVisibleRegionType());
            Long regionId = cmd.getVisibleRegionId();
            if(regionType == null) {
                LOGGER.error("Unsupported region type, default to community, userId=" + userId 
                    + ", regionType=" + regionType + ", regionId=" + regionId);
                regionType = VisibleRegionType.COMMUNITY;
                regionId = user.getCommunityId();
            }
            group.setVisibleRegionType(regionType.getCode());
            group.setVisibleRegionId(regionId);
            
            if(cmd.getCategoryId() != null) {
                Category category = this.categoryProvider.findCategoryById(cmd.getCategoryId());
                if(category == null) {
                	LOGGER.error("Category not found, userId=" + userId + ", cmd=" + cmd);
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                            ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid category");
                }
                
                group.setCategoryId(cmd.getCategoryId());
                group.setCategoryPath(category.getPath());
            }
            
            byte privateFlag = 0;
            if(cmd.getPrivateFlag() != null)
                privateFlag = cmd.getPrivateFlag().byteValue();
            group.setPrivateFlag(privateFlag);

            //黑名单权限校验 by sfyan20161213
            if(GroupDiscriminator.fromCode(group.getDiscriminator()) == GroupDiscriminator.GROUP
                    && GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PUBLIC){
                checkBlacklist(null, null);
            }

            // join policy is not exposed current in API, derive it from its visibility flag
            if(privateFlag != 0) {
            	Integer joinPolicy = cmd.getJoinPolicy();
            	if (joinPolicy == null) {
            		joinPolicy = GroupJoinPolicy.NEED_APPROVE.getCode();
				}
                group.setJoinPolicy(joinPolicy);
                
                Acl acl = new Acl();
                acl.setOwnerType(EntityType.GROUP.getCode());
                acl.setPrivilegeId(PrivilegeConstants.Visible);
                acl.setGrantType((byte)1);
                acl.setCreatorUid(User.ROOT_UID);
                acl.setRoleId(Role.ResourceUser);
                acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                this.aclProvider.createAcl(acl);
            } else {
                Acl acl = new Acl();
                acl.setOwnerType(EntityType.GROUP.getCode());
                acl.setPrivilegeId(PrivilegeConstants.Visible);
                acl.setGrantType((byte)1);
                acl.setCreatorUid(User.ROOT_UID);
                acl.setRoleId(Role.AuthenticatedUser);
                acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                this.aclProvider.createAcl(acl);
                
                //创建俱乐部时，添加是否需要验证标记,创建俱乐部需要根据后台设置确定要不要审核及给创建者发消息，add by tt, 20161102
                group.setJoinPolicy(GroupJoinPolicy.fromCode(cmd.getJoinPolicy()).getCode());
                if (groupSetting == null || groupSetting.getVerifyFlag() == null || TrueOrFalseFlag.fromCode(groupSetting.getVerifyFlag()) == TrueOrFalseFlag.TRUE) {
					group.setApprovalStatus(ApprovalStatus.WAITING_FOR_APPROVING.getCode());
					group.setStatus(GroupAdminStatus.INACTIVE.getCode());
				}
            }
            
            if(cmd.getTag() != null) {
                group.setTag(cmd.getTag());
            }
            
            VisibilityScope visibilityScope = VisibilityScope.fromCode(cmd.getVisibilityScope());
            Long visibilityScopeId = cmd.getVisibilityScopeId();
            if(visibilityScope != null && visibilityScopeId != null) {
                group.setVisibilityScope(visibilityScope.getCode());
                group.setVisibilityScopeId(visibilityScopeId);
            }
            
            GroupPostFlag postFlag = GroupPostFlag.fromCode(cmd.getPostFlag());
            if(postFlag == null) {
                postFlag = GroupPostFlag.ALL;
            }
            group.setPostFlag(postFlag.getCode());
           
            if (group.getStatus() == null) {
            	group.setStatus(GroupAdminStatus.ACTIVE.getCode());
			}

			group.setTouristPostPolicy(cmd.getTouristPostPolicy());
            group.setClubType(cmd.getClubType());
            group.setPhoneNumber(cmd.getPhoneNumber());

            this.groupProvider.createGroup(group);
    
            // create the group owned forum and save it
            Forum forum = createGroupForum(group);
            group.setOwningForumId(forum.getId());
            this.groupProvider.updateGroup(group);

            GroupMember member = new GroupMember();
            member.setGroupId(group.getId());
            member.setApproveTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            member.setCreatorUid(userId);
            member.setMemberType(EntityType.USER.getCode());
            member.setMemberId(userId);
            member.setMemberNickName(user.getNickName());
            member.setMemberAvatar(user.getAvatar());
            member.setMemberRole(Role.ResourceCreator);
            member.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
            this.groupProvider.createGroupMember(member);
            
            // 为了列请求列表的时候能拿到创建者的记录，故在创建group时加上该记录
            GroupOpRequest request = new GroupOpRequest();
            request.setGroupId(group.getId());
            request.setRequestorUid(user.getId());
            request.setOperationType(GroupOpType.REQUEST_ADMIN_ROLE.getCode());
            request.setStatus(GroupOpRequestStatus.ACCEPTED.getCode());
            request.setRequestorComment("creator");
            request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.groupProvider.createGroupOpRequest(request);
            
            GroupVisibilityScope scope = null;
            if(cmd.getExplicitRegionDescriptorsJson() != null) {
                RegionDescriptor[] regionDescriptors = gson.fromJson(cmd.getExplicitRegionDescriptorsJson(),
                        RegionDescriptor[].class);
                
                if(regionDescriptors != null && regionDescriptors.length > 0) {
                    for(RegionDescriptor regionDescriptor: regionDescriptors) {
                        scope = new GroupVisibilityScope();
                        scope.setOwnerId(group.getId());
                        scope.setScopeCode(regionDescriptor.getRegionScope());
                        scope.setScopeId(regionDescriptor.getRegionId());
                        
                        this.groupProvider.createGroupVisibleScope(scope);
                    }
                }
            }
            
            createUserGroup(member, scope);
            
            //创建俱乐部需要发消息，add by tt, 20161102
            if(GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(group.getPrivateFlag())) {
            	if (groupSetting == null || groupSetting.getVerifyFlag() == null || TrueOrFalseFlag.fromCode(groupSetting.getVerifyFlag()) == TrueOrFalseFlag.TRUE) {
            		sendNotificationToCreator(group, user.getLocale(), GroupNotificationTemplateCode.GROUP_MEMBER_TO_CREATOR_WHEN_NEED_APPROVAL);
				}else {
					sendNotificationToCreator(group, user.getLocale(), GroupNotificationTemplateCode.GROUP_MEMBER_TO_CREATOR_WHEN_NO_APPROVAL);
				}
            }
            
            return toGroupDTO(user.getId(), group);
        });
        
        // add to search engine
        if(GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(groupDto.getPrivateFlag())) {
        	VisibleRegionType regionType = VisibleRegionType.fromCode(cmd.getVisibleRegionType());
        	Long regionId = cmd.getVisibleRegionId();
        	if(regionType == null) {
                LOGGER.error("Unsupported region type, default to community, userId=" + userId 
                    + ", regionType=" + regionType + ", regionId=" + regionId);
                regionType = VisibleRegionType.COMMUNITY;
                regionId = user.getCommunityId();
            }

            // 如果创建俱乐部需要审核的话，审核后再发帖子
            if (null != groupSetting && Objects.equals(groupSetting.getVerifyFlag(), TrueOrFalseFlag.FALSE.getCode())) {
                recommandGroup(groupDto, regionType, regionId);
            }

        	try {
                Group dbGroup = groupProvider.findGroupById(groupDto.getId());
                groupSearcher.feedDoc(dbGroup);
            } catch(Exception e) {
                LOGGER.error("Failed to update the group to search engine, userId=" 
                    + userId + ", groupId=" + groupDto.getId());
            }
        }
        
        long endTime = System.currentTimeMillis();
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("Create a new group, userId=" + userId + ", groupId=" + groupDto.getId() 
                + ", elapse=" + (endTime - startTime));
        }

        // 创建group事件
        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setNamespaceId(namespaceId);
            context.setUid(groupDto.getCreatorUid());
            event.setContext(context);

            event.setEntityType(EntityType.GROUP.getCode());
            event.setEntityId(groupDto.getId());
            event.setEventName(SystemEvent.GROUP_GROUP_CREATE.dft());

            event.addParam("groupDto", StringHelper.toJsonString(groupDto));
        });
        return groupDto;
    }
    
	private void sendNotificationToCreator(Group group, String locale, int code) {
		Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupName", group.getName());
       
        String scope = GroupNotificationTemplateCode.SCOPE;
        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendMessageToUser(group.getCreatorUid(), notifyTextForApplicant, null);
	}
    
    private void recommandGroup(GroupDTO groupDto, VisibleRegionType regionType, Long regionId) {
    	 
    	if(regionType == null) {
    		LOGGER.info("regionType is null ");
    		return ;
    	}

        User user = userProvider.findUserById(groupDto.getCreatorUid());
    	long userId = user.getId();

        NewTopicCommand newTopic = new NewTopicCommand();

    	Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupName", groupDto.getName());
    	String locale = user.getLocale();
        String scope = GroupNotificationTemplateCode.SCOPE;
        int code = GroupNotificationTemplateCode.GROUP_RECOMMEND;
        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
    	newTopic.setSubject(notifyTextForApplicant);
    	newTopic.setEmbeddedAppId(AppConstants.APPID_GROUP_CARD);
    	
    	GroupCardDTO groupCard = new GroupCardDTO();
    	groupCard.setId(groupDto.getId());
    	groupCard.setName(groupDto.getName());
    	groupCard.setAvatar(groupDto.getAvatar());
    	groupCard.setAvatarUrl(groupDto.getAvatarUrl());
    	groupCard.setDescription(groupDto.getDescription());
    	groupCard.setCreateTime(groupDto.getCreateTime().toString());
    	groupCard.setCreatorUid(groupDto.getCreatorUid());
    	groupCard.setPrivateFlag(groupDto.getPrivateFlag());
    	
    	newTopic.setEmbeddedJson(groupCard.toString());
    	
    	if(VisibleRegionType.COMMUNITY != regionType) {
    		newTopic.setForumId(ForumConstants.SYSTEM_FORUM);
    	}
    	else {
        	Community community = communityProvider.findCommunityById(regionId);
        	if(community == null) {
                LOGGER.error("Community is not found, userId=" + userId + ", communityId=" + user.getCommunityId());
//                throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
//                    ForumServiceErrorCode.ERROR_FORUM_COMMUNITY_NOT_FOUND, "Community not found");
        		
//        		List<OrganizationCommunity> orgCommunities = organizationProvider.listOrganizationCommunities(regionId);
//        		
//        		if(orgCommunities != null) {
//        			Community orgCommunity = communityProvider.findCommunityById(orgCommunities.get(0).getCommunityId());
//        			if(orgCommunity != null) {
//        				newTopic.setForumId(orgCommunity.getDefaultForumId());
//        			} else {
//        				LOGGER.error("orgCommunity is not found, userId=" + userId + ", communityId=" + orgCommunities.get(0).getCommunityId());
//        				return ;
//        			}
//        		}
                
                return ;
        		
        		
            } else { 
            	newTopic.setForumId(community.getDefaultForumId());
            }
    	}
    	newTopic.setVisibleRegionType(regionType.getCode());
    	newTopic.setVisibleRegionId(regionId);
    	newTopic.setCreatorTag(PostEntityTag.USER.getCode());
    	newTopic.setTargetTag(PostEntityTag.USER.getCode());
    	
    	newTopic.setPrivateFlag(PostPrivacy.PUBLIC.getCode());
    	newTopic.setContentType(PostContentType.TEXT.getCode());
    	forumService.createTopic(newTopic, user.getId());
    }
    
    @Override
    public GroupDTO updateGroup(UpdateGroupCommand cmd) {
        CreateGroupCommand createGroupCommand = ConvertHelper.convert(cmd, CreateGroupCommand.class);
        //xss过滤
        String content = XssCleaner.clean(cmd.getDescription());
        cmd.setDescription(content);

        filter(createGroupCommand);
        User operator = UserContext.current().getUser();
        long operatorUid = operator.getId();
        
        Long groupId = cmd.getGroupId();
        Group group = checkGroupParameter(groupId, operatorUid, "updateGroup");

        String oldName = group.getName();
        
        //群管理员修改群信息时显示 Insufficient privilege 所以注释掉 modified by xiongying 20160614
        //checkGroupPrivilege(operatorUid, groupId, PrivilegeConstants.Write);
        
        if(cmd.getAvatar() != null)
            group.setAvatar(cmd.getAvatar());
        
        if(cmd.getDescription() != null){
            group.setDescription(cmd.getDescription());
            group.setDescriptionType(cmd.getDescriptionType());
        }
        
        if(cmd.getName() != null)
            group.setName(cmd.getName());
        
        if(cmd.getTag() != null)
            group.setTag(cmd.getTag());
        
        if(cmd.getVisibilityScope() != null)
            group.setVisibilityScope(cmd.getVisibilityScope());
        if(cmd.getVisibilityScopeId() != null)
            group.setVisibilityScopeId(cmd.getVisibilityScopeId());
        
        if(cmd.getCategoryId() != null) {
            group.setCategoryId(cmd.getCategoryId());
            
            Category category = this.categoryProvider.findCategoryById(cmd.getCategoryId());
            if(category != null)
                group.setCategoryPath(category.getPath());
        }
        
        // 添加加入策略, add by tt, 20161102
        if (cmd.getJoinPolicy() != null) {
			group.setJoinPolicy(cmd.getJoinPolicy());
		}

		if(cmd.getTouristPostPolicy() != null){
            group.setTouristPostPolicy(cmd.getTouristPostPolicy());
        }

        group.setPhoneNumber(cmd.getPhoneNumber());
        
        group.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_GROUP.getCode()).enter(()-> {
            this.groupProvider.updateGroup(group);
            return null;
        });
        groupSearcher.feedDoc(group);

        if(cmd.getName() != null && !cmd.getName().equals(oldName)){
            sendNotificationForUpdateName(group, operator.getLocale());
        }
        
        return this.toGroupDTO(operatorUid, group);
    }
    
    @Override
    public GroupDTO getGroup(GetGroupCommand cmd) {

        //优先使用前端传来的userId。消息2.1的扫码入群是没有登录的，但是要判断该用户是否已经加入群  add by yanjun 20170725
        Long userId = cmd.getUserId();
        if(userId == null){
            User user = UserContext.current().getUser();
            userId = user != null ? user.getId() : 0;
        }

        Long groupId = cmd.getGroupId();
        // 改成通过UUID获取，不需要进行权限校验
        //checkGroupPrivilege(userId, groupId, PrivilegeConstants.Visible);
        
        Group group = this.groupProvider.findGroupById(groupId);
        
        if(group == null) {
        	LOGGER.error("Group not found, userId=" + userId + ", groupId=" + groupId);
        	throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, GroupServiceErrorCode.ERROR_GROUP_NOT_FOUND, 
                "Unable to find the group");
        } else {
            GroupDTO  dto = this.toGroupDTO(userId, group);

        	if(GroupDiscriminator.ENTERPRISE == GroupDiscriminator.fromCode(group.getDiscriminator())){
        		Organization organization = organizationProvider.findOrganizationByGroupId(groupId);
        		if(null == organization){
        			LOGGER.error("Group organization not found, operatorUid = {}, groupId = {}", userId, groupId);
    	            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, GroupServiceErrorCode.ERROR_GROUP_MEMBER_NOT_FOUND, "Unable to find the group organization");
        		}
                dto.setName(organization.getName());
                dto.setMemberOf((byte)1);

                OrganizationMember member = this.organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, organization.getId());
                if (member != null && member.getStatus() != null)
                    dto.setMemberStatus(member.getStatus());
                else
                    dto.setMemberStatus(OrganizationMemberStatus.INACTIVE.getCode());

                // 某些公司会出现group人数为0，比如管理公司  add by yanjun 20170802
                Integer count = organizationProvider.queryOrganizationPersonnelCounts(new ListingLocator(), organization.getId(), null);
                dto.setMemberCount(Long.valueOf(count));
                dto.setOrgId(organization.getId());
            }


            //群聊名称为空时填充群聊别名  edit by yanjun 20170724
            if(StringUtils.isEmpty(dto.getName())){
                String alias = getGroupAlias(dto.getId());
                dto.setAlias(alias);
                String defaultName = localeStringService.getLocalizedString(GroupLocalStringCode.SCOPE, String.valueOf(GroupLocalStringCode.GROUP_DEFAULT_NAME), UserContext.current().getUser().getLocale(), "");
                dto.setName(defaultName);
                dto.setIsNameEmptyBefore(GroupNameEmptyFlag.EMPTY.getCode());
            }



            return dto;

        }
    }

    @Override
    public Group findGroupByForumId(long forumId) {
        return groupProvider.findGroupByForumId(forumId);
    }
    
    @Override
    public ListGroupCommandResponse listGroupsByNamespaceId(ListGroupsByNamespaceIdCommand cmd){
    	
    	 User user = UserContext.current().getUser();
         int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
         CrossShardListingLocator locator = new CrossShardListingLocator();
         locator.setAnchor(cmd.getPageAnchor());

        //妹的，老客户有没有参数，满世界都要写这种判断 add by yanjun 20171107
        if(cmd.getClubType() == null){
            cmd.setClubType(ClubType.NORMAL.getCode());
        }
         
         List<Group> groups = this.groupProvider.queryGroups(locator, pageSize + 1, (loc, query)-> {
             query.addConditions(Tables.EH_GROUPS.NAMESPACE_ID.eq(cmd.getNamespaceId()));
             query.addConditions(Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode()));
             query.addConditions(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.GROUP.getCode()));
             if(null != cmd.getCategoryId())
            	 query.addConditions(Tables.EH_GROUPS.CATEGORY_ID.eq(cmd.getCategoryId()));
             if(!StringUtils.isEmpty(cmd.getKeywords())){
            	 // 俱乐部搜索时，模糊查询group的name, add by tt, 20161103
            	 if (GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(cmd.getPrivateFlag())) {
            		 query.addConditions(Tables.EH_GROUPS.NAME.like("%"+cmd.getKeywords()+"%"));
            	 }else {
            		 query.addConditions(Tables.EH_GROUPS.TAG.eq(cmd.getKeywords()));
				}
             }
             if(!StringUtils.isEmpty(cmd.getPrivateFlag()))
            	 query.addConditions(Tables.EH_GROUPS.PRIVATE_FLAG.eq(cmd.getPrivateFlag()));

             query.addConditions(Tables.EH_GROUPS.CLUB_TYPE.eq(cmd.getClubType()));
             
             return query;
         });
         
         ListGroupCommandResponse cmdResponse = new ListGroupCommandResponse();
         if(groups.size() > pageSize) {
             groups.remove(groups.size() - 1);
             cmdResponse.setNextPageAnchor(groups.get(groups.size() - 1).getId());
         }

         cmdResponse.setGroups(groups.stream().map((r)-> {
             Long userId = user != null && user.getId() != 0 ? user.getId() : -1;
             return toGroupDTO(userId, r);
         }).collect(Collectors.toList()));
         
         return cmdResponse;
    }

    @Override
    public List<GroupDTO> listOwnerGroupsByType(Byte clubType){

        Long userId = UserContext.current().getUser().getId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        int pageSize = 100000;
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<Group> groups = this.groupProvider.queryGroups(locator, pageSize + 1, (loc, query)-> {
            query.addConditions(Tables.EH_GROUPS.NAMESPACE_ID.eq(namespaceId));
            query.addConditions(Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode()));
            query.addConditions(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.GROUP.getCode()));
            query.addConditions(Tables.EH_GROUPS.PRIVATE_FLAG.eq(GroupPrivacy.PUBLIC.getCode()));
            query.addConditions(Tables.EH_GROUPS.CLUB_TYPE.eq(clubType));
            query.addConditions(Tables.EH_GROUPS.CREATOR_UID.eq(userId));
            return query;
        });

        List<GroupDTO> dtos = new ArrayList<>();
        if(groups != null){
            dtos = groups.stream().map(group -> toGroupDTO(userId, group)).collect(Collectors.toList());
        }

        return dtos;
    }
    
    @Override
    public List<GroupDTO> listUserRelatedGroups() {
    	long startTime = System.currentTimeMillis();
    	
        User user = UserContext.current().getUser();
        long userId = user.getId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        
        List<GroupDTO> groupDtoList = new ArrayList<GroupDTO>();
        
        List<UserGroup> userGroupList = userProvider.listUserGroups(userId, GroupDiscriminator.GROUP.getCode());
        int size = (userGroupList == null) ? 0 : userGroupList.size();
        Group tmpGroup = null;
        if(size > 0) {
        	for(UserGroup userGroup : userGroupList) {
        	    // 应客户端要求，过滤掉成员状态为非active的group，因为在客户端那边拿到该group后，group active成员没有本人
        	    GroupMemberStatus status = GroupMemberStatus.fromCode(userGroup.getMemberStatus());
        	    if(status != GroupMemberStatus.ACTIVE) {
        	        if(LOGGER.isDebugEnabled()) {
        	            LOGGER.debug("The group is filtered for not in active member status, userId=" + userId 
        	                + ", groupId=" + userGroup.getGroupId() + ", memberStatus=" + status);
        	        }
        	        continue;
        	    }
        		tmpGroup = groupProvider.findGroupById(userGroup.getGroupId());
        	    //加上域空间限制，否则跨域的也会查出来, add by tt, 20161101
        	    if (tmpGroup != null && tmpGroup.getNamespaceId().intValue() != namespaceId.intValue()) {
					continue;
				}
        		if(tmpGroup != null && !tmpGroup.getStatus().equals(GroupAdminStatus.INACTIVE.getCode())) {
        		    groupDtoList.add(toGroupDTO(userId, tmpGroup));
        		} else {
        		    LOGGER.error("The group is not found, userId=" + userId + ", groupId=" + userGroup.getGroupId());
        		}
            }
        }

        if(LOGGER.isInfoEnabled()) {
        	long endTime = System.currentTimeMillis();
        	LOGGER.info("List user related groups, userId=" + userId + ", size=" + size 
        			+ ", elapse=" + (endTime - startTime));
        }
        
        return groupDtoList;
    }

    @Override
    public List<GroupDTO> listUserGroups() {
        long startTime = System.currentTimeMillis();

        User user = UserContext.current().getUser();
        long userId = user.getId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        List<GroupDTO> groupDtoList = new ArrayList<GroupDTO>();
        Group tmpGroup = null;

        //添加公司group  add by yanjun 20170721
        List<OrganizationDTO> listOrg = organizationService.listUserRelateOrganizations(namespaceId, userId, OrganizationGroupType.ENTERPRISE);

        if(listOrg != null){
            for(OrganizationDTO org : listOrg){
                OrganizationMember org_member = this.organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, org.getId());
                // 过滤待审核·审核中 by lei.lv
                if(org_member.getStatus().equals(OrganizationMemberStatus.WAITING_FOR_APPROVAL.getCode()) || org_member.getStatus().equals(OrganizationMemberStatus.WAITING_FOR_ACCEPTANCE.getCode())){
                    continue;
                }
                if(org.getGroupId() != null){

                    tmpGroup = groupProvider.findGroupById(org.getGroupId());
                    //加上域空间限制，否则跨域的也会查出来, add by tt, 20161101
                    if (tmpGroup != null && tmpGroup.getNamespaceId().intValue() != namespaceId.intValue()) {
                        continue;
                    }
                    if(tmpGroup != null && !tmpGroup.getStatus().equals(GroupAdminStatus.INACTIVE.getCode())) {
                        GroupDTO dto = toGroupDTO(userId, tmpGroup);
                        dto.setMemberOf((byte)1);
                        dto.setMemberStatus(org.getMemberStatus());

                        //默认使用org那边的名称
                        if(org.getName() != null){
                            dto.setName(org.getName());
                        }

                        // 某些公司会出现group人数为0，比如管理公司  add by yanjun 20170802
                        Integer count = organizationProvider.queryOrganizationPersonnelCounts(new ListingLocator(), org.getId(), null);
                        dto.setMemberCount(Long.valueOf(count));
                        dto.setOrgId(org.getId());
                        groupDtoList.add(dto);
                    } else {
                        LOGGER.error("The group is not found, userId=" + userId + ", groupId=" + org.getGroupId());
                    }
                }

            }
        }

        //添加家庭群
        List<UserGroup> userGroupList = new ArrayList<UserGroup>();
        List<UserGroup> userGroupListFamily = userProvider.listUserGroups(userId, GroupDiscriminator.FAMILY.getCode());
        userGroupList.addAll(userGroupListFamily);
        //添加普通群
        List<UserGroup> userGroupListGroup = userProvider.listUserGroups(userId, GroupDiscriminator.GROUP.getCode());
        userGroupList.addAll(userGroupListGroup);

        int size = (userGroupList == null) ? 0 : userGroupList.size();

        if(size > 0) {
            for(UserGroup userGroup : userGroupList) {
                // 应客户端要求，过滤掉成员状态为非active的group，因为在客户端那边拿到该group后，group active成员没有本人
                GroupMemberStatus status = GroupMemberStatus.fromCode(userGroup.getMemberStatus());
                if(status != GroupMemberStatus.ACTIVE) {
                    if(LOGGER.isDebugEnabled()) {
                        LOGGER.debug("The group is filtered for not in active member status, userId=" + userId
                                + ", groupId=" + userGroup.getGroupId() + ", memberStatus=" + status);
                    }
                    continue;
                }
                tmpGroup = groupProvider.findGroupById(userGroup.getGroupId());
                //加上域空间限制，否则跨域的也会查出来, add by tt, 20161101
                if (tmpGroup != null && tmpGroup.getNamespaceId().intValue() != namespaceId.intValue()) {
                    continue;
                }

                //若是group类型，只要的私有组，即群聊。  add by yanjun 20170724
                if(tmpGroup.getDiscriminator() != null
                        && GroupDiscriminator.fromCode(tmpGroup.getDiscriminator()) == GroupDiscriminator.GROUP
                        && tmpGroup.getPrivateFlag() != null
                        && tmpGroup.getPrivateFlag() != GroupPrivacy.PRIVATE.getCode()){

                    continue;
                }
                if(tmpGroup != null && !tmpGroup.getStatus().equals(GroupAdminStatus.INACTIVE.getCode())) {
                    GroupDTO dto = toGroupDTO(userId, tmpGroup);

                    //群聊名称为空时填充群聊别名  edit by yanjun 20170724
                    if(StringUtils.isEmpty(dto.getName())){
                        String alias = getGroupAlias(dto.getId());
                        dto.setAlias(alias);
                        String defaultName = localeStringService.getLocalizedString(GroupLocalStringCode.SCOPE, String.valueOf(GroupLocalStringCode.GROUP_DEFAULT_NAME), UserContext.current().getUser().getLocale(), "");
                        dto.setName(defaultName);
                        dto.setIsNameEmptyBefore(GroupNameEmptyFlag.EMPTY.getCode());
                    }
                    groupDtoList.add(dto);
                } else {
                    LOGGER.error("The group is not found, userId=" + userId + ", groupId=" + userGroup.getGroupId());
                }

            }
        }



//
//        //排序：第一企业，第二家庭，第三群聊  add by yanjun 20170724
//        if(groupDtoList != null && groupDtoList.size() >0){
//            groupDtoList.sort(new Comparator<GroupDTO>() {
//                @Override
//                public int compare(GroupDTO o1, GroupDTO o2) {
//                    if(o2.getDiscriminator() == null ){
//                        return -1;
//                    }
//                    if(o1.getDiscriminator() == null ){
//                        return 1;
//                    }
//
//                    if(GroupDiscriminator.fromCode(o2.getDiscriminator()) == GroupDiscriminator.ENTERPRISE && GroupDiscriminator.fromCode(o1.getDiscriminator()) != GroupDiscriminator.ENTERPRISE){
//                        return 1;
//                    }else if(o2.getDiscriminator() == GroupDiscriminator.FAMILY.getCode()
//                            && (GroupDiscriminator.fromCode(o1.getDiscriminator()) != GroupDiscriminator.ENTERPRISE && GroupDiscriminator.fromCode(o1.getDiscriminator()) != GroupDiscriminator.FAMILY)){
//                        return 1;
//                    }else{
//                        return -1;
//                    }
//                }
//            });
//        }

        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("List user groups, userId=" + userId + ", size=" + size
                    + ", elapse=" + (endTime - startTime));
        }

        return groupDtoList;
    }


/*    private long getOrganizationMemberCount(Long orgId){
        ListOrganizationContactCommand command = new ListOrganizationContactCommand();
        command.setOrganizationId(orgId);
        command.setPageSize(1);
        command.setIsSignedup((byte)1);
        ListOrganizationContactCommandResponse res = organizationService.listOrganizationContacts(command);
        if(res != null && res.getTotalCount() != null){
            return res.getTotalCount().longValue();
        }
        return 0;
    }*/

    @Override
    public String getGroupAlias(Long groupId){
        ListMemberInStatusCommand cmd = new ListMemberInStatusCommand();
        cmd.setGroupId(groupId);
        cmd.setStatus(GroupMemberStatus.ACTIVE.getCode());
        cmd.setPageSize(10);
        ListMemberCommandResponse commandResponse = this.listMembersInStatus(cmd);
        if(commandResponse.getMembers() != null && commandResponse.getMembers().size() > 0){
            String  alias = "";
            int count = 0;
            for(int i = 0; i< commandResponse.getMembers().size(); i++){
                String nickName = commandResponse.getMembers().get(i).getMemberNickName();

                //居然会有名称为null的，而且还要返回5个  add by yanjun 20170816
                if(nickName == null || nickName.isEmpty()){
                    continue;
                }
                count++;
                if(count > 5){
                    break;
                }

                alias += nickName;
                alias += "、";
            }

            if(alias.length() > 0){
                return alias.substring(0, alias.length()-1);
            }

        }
        return "";
    }
    
    
    @Override
    public List<GroupDTO> listPublicGroups(ListPublicGroupCommand cmd) {
        long startTime = System.currentTimeMillis();
        
        User operator = UserContext.current().getUser();
        long operatorId = operator.getId();

        //妹的，老客户有没有参数，满世界都要写这种判断 add by yanjun 20171107
        if(cmd.getClubType() == null){
            cmd.setClubType(ClubType.NORMAL.getCode());
        }
        
        List<GroupDTO> groupDtoList = new ArrayList<GroupDTO>();
        
        List<UserGroup> userGroupList = userProvider.listUserGroups(cmd.getUserId(), GroupDiscriminator.GROUP.getCode());
        int size = (userGroupList == null) ? 0 : userGroupList.size();
        Group tmpGroup = null;
        if(size > 0) {
            for(UserGroup userGroup : userGroupList) {
                tmpGroup = groupProvider.findGroupById(userGroup.getGroupId());

                //加这一句，把后面的不等于null都删掉  add by yanjun 20171108
                if(tmpGroup == null){
                    LOGGER.error("The group is not found, userId=" + operatorId + ", groupId=" + userGroup.getGroupId());
                    continue;
                }

                // 过滤掉意见反馈圈 by lqs 20160416
                if(tmpGroup.getOwningForumId() != null && tmpGroup.getOwningForumId().longValue() == ForumConstants.FEEDBACK_FORUM) {
                    continue;
                }

                // 判断行业协会或者俱乐部  add by yanjun 20171107
                if(ClubType.fromCode(cmd.getClubType()) != ClubType.fromCode(tmpGroup.getClubType())){
                    continue;
                }
                // 且这个成员在这个已经审批通过了, add by tt, 20161112
                if(Byte.valueOf(GroupPrivacy.PUBLIC.getCode()).equals(tmpGroup.getPrivateFlag())
                		&& Byte.valueOf(GroupAdminStatus.ACTIVE.getCode()).equals(tmpGroup.getStatus())
                		&& GroupMemberStatus.fromCode(userGroup.getMemberStatus()) == GroupMemberStatus.ACTIVE) {
                	groupDtoList.add(toGroupDTO(operatorId, tmpGroup));
                } 
                // 审核中的俱乐部也要显示，add by tt, 20161103
                else if (GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(tmpGroup.getPrivateFlag())
                		&& GroupAdminStatus.INACTIVE == GroupAdminStatus.fromCode(tmpGroup.getStatus()) 
                		&& ApprovalStatus.WAITING_FOR_APPROVING == ApprovalStatus.fromCode(tmpGroup.getApprovalStatus())
                		&& operatorId == tmpGroup.getCreatorUid().longValue()) {
                	groupDtoList.add(toGroupDTO(operatorId, tmpGroup));
				}
            }
        }
        
        // 添加排序，先按审核中、创建者、管理员及成员的顺序进行排列，再按俱乐部加入时间倒序排列，add by tt, 20161103
        groupDtoList.sort((g1, g2)->{
        	if (ApprovalStatus.WAITING_FOR_APPROVING == ApprovalStatus.fromCode(g1.getApprovalStatus()) && ApprovalStatus.WAITING_FOR_APPROVING != ApprovalStatus.fromCode(g2.getApprovalStatus())) {
				return -1;
			}
        	if (ApprovalStatus.WAITING_FOR_APPROVING != ApprovalStatus.fromCode(g1.getApprovalStatus()) && ApprovalStatus.WAITING_FOR_APPROVING == ApprovalStatus.fromCode(g2.getApprovalStatus())) {
				return 1;
			}
			if (g1.getCreatorUid().longValue() == operatorId && g2.getCreatorUid().longValue() != operatorId) {
				return -1;
			}
			if (g1.getCreatorUid().longValue() != operatorId && g2.getCreatorUid().longValue() == operatorId) {
				return 1;
			}
			if (Long.valueOf(RoleConstants.ResourceAdmin).equals(g1.getMemberRole()) && !Long.valueOf(RoleConstants.ResourceAdmin).equals(g2.getMemberRole())) {
				return -1;
			}
			if (!Long.valueOf(RoleConstants.ResourceAdmin).equals(g1.getMemberRole()) && Long.valueOf(RoleConstants.ResourceAdmin).equals(g2.getMemberRole())) {
				return 1;
			}
			if (g1.getJoinTime().getTime() > g2.getJoinTime().getTime()) {
				return -1;
			}
			if (g1.getJoinTime().getTime() < g2.getJoinTime().getTime()) {
				return 1;
			}
		
        	return 0;
        });
        
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("List public groups, operatorId=" + operatorId + ", userId=" + cmd.getUserId() + ", size=" + size 
                    + ", elapse=" + (endTime - startTime));
        }
        
        return groupDtoList;
    }

    
    @Override
    public GroupMemberDTO updateGroupMember(UpdateGroupMemberCommand cmd) {
        User user = UserContext.current().getUser();
        Long operatorUid = user.getId();
        String tag = "updateGroupMember";
        
        Long groupId = cmd.getGroupId();
        Group group = checkGroupParameter(groupId, operatorUid, tag);
        
        Long memberId = cmd.getMemberId();
        GroupMember member = checkGroupMemberParameter(group, operatorUid, memberId, tag);
        
        // 本人、组创建者、管理员都可以修改成员信息
        if(!operatorUid.equals(cmd.getMemberId())) {
            checkGroupPrivilege(operatorUid, groupId, PrivilegeConstants.GroupUpdateMember);
        }
        
        if(cmd.getMemberAvatar() != null) {
            member.setMemberAvatar(cmd.getMemberAvatar());
        }
        if(cmd.getMemberNickName() != null) {
            member.setMemberNickName(cmd.getMemberNickName());
        }
        if(cmd.getPhonePrivateFlag() != null) {
            member.setPhonePrivateFlag(cmd.getPhonePrivateFlag());
        }
        if(cmd.getMuteNotificationFlag() != null) {
            member.setMuteNotificationFlag(cmd.getMuteNotificationFlag());
        }
        member.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        this.groupProvider.updateGroupMember(member);
        GroupMemberDTO groupMemberDTO = ConvertHelper.convert(member, GroupMemberDTO.class);
        populateGroupMemberDTO(operatorUid, group, groupMemberDTO);
        
        return groupMemberDTO;
    }
    
    @Override
    public GroupMemberSnapshotDTO getGroupMemberSnapshot(GetGroupMemberSnapshotCommand cmd) {
        User user = UserContext.current().getUser();
        Long operatorUid = user.getId();
        String tag = "getGroupMemberSnapshot";
        
        Long groupId = cmd.getGroupId();
        Group group = checkGroupParameter(groupId, operatorUid, tag);
        
        Long memberId = cmd.getMemberId();
        if(memberId == User.SYSTEM_UID || memberId == User.ROOT_UID) {
        	User queryUser = userProvider.findUserById(memberId);
        	
        	GroupMemberSnapshotDTO snapshot = new GroupMemberSnapshotDTO();
        	
        	snapshot.setGroupId(groupId);
        	snapshot.setGroupName(group.getName());
        	snapshot.setMemberId(memberId);
        	snapshot.setMemberNickName(queryUser.getNickName());
        	snapshot.setMemberType(EntityType.USER.getCode());
        	snapshot.setCreateTime(queryUser.getCreateTime());
        	snapshot.setMemberAvatar(queryUser.getAvatar());
        	if(queryUser.getAvatar() != null && queryUser.getAvatar().length() > 0) {
        		String url = contentServerService.parserUri(queryUser.getAvatar(), EntityType.USER.getCode(), queryUser.getId());
        		snapshot.setMemberAvatarUrl(url);
        	}
        	
        	return snapshot;
        	
        }
        else{
        	//group是公司圈的时候，从organizationMember里面查询 sfyan
        	GroupMember member = null;
        	if(GroupDiscriminator.ENTERPRISE == GroupDiscriminator.fromCode(group.getDiscriminator())){
        		Organization organization = organizationProvider.findOrganizationByGroupId(groupId);
        		if(null == organization){
        			LOGGER.error("Group organization not found, operatorUid = {}, groupId = {}, memberId = {}", operatorUid, groupId, memberId);
    	            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, GroupServiceErrorCode.ERROR_GROUP_MEMBER_NOT_FOUND, "Unable to find the group organization");
        		}
        		
        		OrganizationMember orgMember = organizationProvider.findOrganizationMemberByUIdAndOrgId(memberId, organization.getId());
        		if(null == orgMember){
        			LOGGER.error("Group organization member not found, operatorUid = {}, groupId = {}, memberId = {}, organizationId = {}", operatorUid, groupId, memberId, organization.getId());
    	            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, GroupServiceErrorCode.ERROR_GROUP_MEMBER_NOT_FOUND, "Unable to find the group organization member");
        		}
        		member = new GroupMember();
        		member.setMemberId(memberId);
        		member.setMemberNickName(orgMember.getContactName());
        		member.setGroupId(groupId);
        		member.setMemberType(orgMember.getTargetType());
        		group.setName(organization.getName());
        	}else{
        		member = groupProvider.findGroupMemberByMemberInfo(groupId, EntityType.USER.getCode(), memberId);
        	}
	        
	        
	        if(member == null) {
	            //  按产品设计，把圈子概念变成两个：群聊（对应私有圈）、俱乐部（对应兴趣圈） by lqs 20160505
	            int code = GroupServiceErrorCode.ERROR_GROUP_MEMBER_NOT_FOUND;
	            if(GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PUBLIC) {
	                code = GroupServiceErrorCode.ERROR_GROUP_CLUB_MEMBER_NOT_FOUND;
	            }
	            LOGGER.error("Group member not found, operatorUid=" + operatorUid + ", groupId=" + groupId + ", memberUuid=" + memberId);
	            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, code, "Unable to find the group member");
	        } else {
	            GroupMemberSnapshotDTO snapshot = ConvertHelper.convert(member, GroupMemberSnapshotDTO.class);
	            snapshot.setGroupName(group.getName());
	            
//	            String memberAvatar = snapshot.getMemberAvatar();
//	            //圈头像为空时用用户头像by xiongying 20160505
//	            if(memberAvatar == null || memberAvatar.trim().length() == 0) {
//	            	User queryUser = userProvider.findUserById(memberId);
//	            	snapshot.setMemberAvatar(queryUser.getAvatar());
//	            	memberAvatar = queryUser.getAvatar();
//	            }
	          //圈头像使用用户头像by xiongying 20160516
	            User queryUser = userProvider.findUserById(memberId);
	            snapshot.setMemberAvatar(queryUser.getAvatar());
	            String memberAvatar = queryUser.getAvatar();
	            if(memberAvatar != null && memberAvatar.length() > 0) {
	                try{
	                    String url = contentServerService.parserUri(memberAvatar, EntityType.USER.getCode(), member.getMemberId());
	                    snapshot.setMemberAvatarUrl(url);
	                }catch(Exception e){
	                    LOGGER.error("Failed to parse avatar uri of group member, userId=" + operatorUid 
	                        + ", groupMember=" + member, e);
	                }
	            }
	            
	            String nickName = snapshot.getMemberNickName();
	            if(nickName == null || nickName.trim().length() == 0) {
//	                User queryUser = userProvider.findUserById(memberId);
	                if(queryUser != null) {
	                    snapshot.setMemberNickName(queryUser.getNickName());
	                }
	            }
	            
	            return snapshot;
	        }
        }
    }
    
    @Override
    public void requestToJoinGroup(RequestToJoinGroupCommand cmd) {
    	createGroupMember(cmd, true);
    }
    
    private void createGroupMember(RequestToJoinGroupCommand cmd, boolean needNotify) {
    	User user = UserContext.current().getUser();
        Long userId = user.getId();
        
        Long groupId = cmd.getGroupId();
        //以前业务会传这个字段，新业务可能不传，后面加入行业协会发消息会用到这个字段，不能为null   add by yanjun 20171113
        if(cmd.getRequestText() ==null){
            cmd.setRequestText("");
        }
        Group group = checkGroupParameter(groupId, userId, "requestToJoinGroup");
        
    	GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(groupId, EntityType.USER.getCode(), user.getId());
    	// fix bug: 如果一个俱乐部原来是加入需要验证，一个用户加入了一下，后面俱乐部改成了不需要验证，这里member表会一直有一条记录
    	// 导致这个人始终无法加入该俱乐部， add by tt, 20171122
    	if (member != null && GroupDiscriminator.GROUP == GroupDiscriminator.fromCode(group.getDiscriminator()) 
        		&& GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(group.getPrivateFlag())
        		&& GroupJoinPolicy.fromCode(group.getJoinPolicy()) == GroupJoinPolicy.FREE
        		&& GroupMemberStatus.fromCode(member.getMemberStatus()) == GroupMemberStatus.WAITING_FOR_APPROVAL) {
    		deletePendingGroupMember(userId, member);
    		member = null;
    	}
        if(member == null) {
            member = new GroupMember();
            member.setCreatorUid(user.getId());
            member.setGroupId(groupId);
            member.setMemberType(EntityType.USER.getCode());
            member.setMemberId(user.getId());
            member.setMemberRole(Role.ResourceUser);
            member.setMemberNickName(user.getNickName());
            member.setMemberAvatar(user.getAvatar());
            member.setRequestorComment(cmd.getRequestText());

            GroupVisibilityScope scope = getSingleGroupScope(groupId);
            if(GroupJoinPolicy.fromCode(group.getJoinPolicy()) == GroupJoinPolicy.FREE) {
                member.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
                member.setApproveTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                member.setOperatorUid(member.getMemberId());
                
                createActiveGroupMember(member, scope);
            } else {
                member.setMemberStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
                createPendingGroupMember(member, scope);
            }

            // 在后台俱乐部成员要显示手机号 #11814 update by xq.tian  2017/06/28
            GroupPrivacy groupPrivacy = GroupPrivacy.fromCode(group.getPrivateFlag());
            if (groupPrivacy == GroupPrivacy.PUBLIC) {
                member.setPhonePrivateFlag(GroupMemberPhonePrivacy.PUBLIC.getCode());
            }

            //行业协会需要增加额外表单 add by yanjun 20171108
            if(GroupDiscriminator.GROUP == GroupDiscriminator.fromCode(group.getDiscriminator())
                    && GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(group.getPrivateFlag())
                    && ClubType.fromCode(group.getClubType()) == ClubType.GUILD){
                addGuildApply(cmd, member, userId);
            }

            // send notifications to applicant and other members
            if (needNotify) {
            	if(GroupJoinPolicy.fromCode(group.getJoinPolicy()) == GroupJoinPolicy.FREE) {
                    sendGroupNotificationForReqToJoinGroupFreely(group, member);
                } else {
                    sendGroupNotificationForReqToJoinGroupWaitingApproval(group, member);
                }
			}
        } else {
            if(GroupMemberStatus.fromCode(member.getMemberStatus()) == GroupMemberStatus.WAITING_FOR_ACCEPTANCE) {
                member.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
                member.setMemberNickName(user.getNickName());
                member.setMemberAvatar(user.getAvatar());
                updatePendingGroupMemberToActive(member);
                
                GroupMember inviter = this.groupProvider.findGroupMemberByMemberInfo(groupId, 
                    EntityType.USER.getCode(), member.getInviterUid());
                if (needNotify) {
                	sendGroupNotificationForInviteToJoinGroupFreely(group, inviter, member);
				}
            }
            
            //俱乐部可以重复申请加入，这里只改变申请理由，其它不变, add by tt, 20161104
            if (GroupDiscriminator.GROUP == GroupDiscriminator.fromCode(group.getDiscriminator()) 
            		&& GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(group.getPrivateFlag())) {
                member.setRequestorComment(cmd.getRequestText());
            	member.setMemberStatus(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());  //有可能被拒绝了重复加入
                // 在后台俱乐部成员要显示手机号 #11814 update by xq.tian  2017/06/28
                member.setPhonePrivateFlag(GroupMemberPhonePrivacy.PUBLIC.getCode());
            	groupProvider.updateGroupMember(member);

                //行业协会需要增加额外表单 add by yanjun 20171108
                if(ClubType.fromCode(group.getClubType()) == ClubType.GUILD){
                    addGuildApply(cmd, member, userId);
                }

            	if (needNotify) {
            		sendGroupNotificationForReqToJoinGroupWaitingApproval(group, member);
				}
			}
            
        }

        // 加入group事件
        GroupMember tmpMember = member;
        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setNamespaceId(group.getNamespaceId());
            context.setUid(userId);
            event.setContext(context);

            event.setEntityType(EhGroupMembers.class.getSimpleName());
            event.setEntityId(tmpMember.getId());
            event.setEventName(SystemEvent.GROUP_GROUP_JOIN.dft());

            event.addParam("group", StringHelper.toJsonString(group));
            event.addParam("member", StringHelper.toJsonString(tmpMember));
        });
    }

    //行业协会需要增加额外表单
    private void addGuildApply(RequestToJoinGroupCommand cmd, GroupMember member, Long userId){
        GuildApply guildApply = ConvertHelper.convert(cmd, GuildApply.class);
        guildApply.setNamespaceId(UserContext.getCurrentNamespaceId());
        guildApply.setApplicantUid(userId);
        guildApply.setGroupMemberId(member.getId());

        GuildApply oldGuildApply = groupProvider.findGuildApplyByGroupMemberId(member.getId());
        if(oldGuildApply != null){
            guildApply.setId(oldGuildApply.getId());
            guildApply.setUuid(oldGuildApply.getUuid());
            groupProvider.updateGuildApply(guildApply);
        }else {
            groupProvider.createGuildApply(guildApply);
        }
    }

    
    @Override
    public void requestToJoinGroupByQRCode(RequestToJoinGroupCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        
        Long groupId = cmd.getGroupId();
        Group group = checkGroupParameter(groupId, userId, "requestToJoinGroupByQRCode");
        
        GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(groupId, EntityType.USER.getCode(), user.getId());
        if(member == null) {
            member = new GroupMember();
            member.setCreatorUid(user.getId());
            member.setGroupId(groupId);
            member.setMemberType(EntityType.USER.getCode());
            member.setMemberId(user.getId());
            member.setMemberRole(Role.ResourceUser);
            member.setMemberNickName(user.getNickName());
            member.setMemberAvatar(user.getAvatar());
            member.setRequestorComment(cmd.getRequestText());
            
            Long inviterUid = cmd.getInviterId();
            User inviter = userProvider.findUserById(inviterUid);
            if(inviter != null) {
                member.setInviterUid(inviterUid);
                member.setInviteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            }

            // 扫二维码加入不需要审核
            GroupVisibilityScope scope = getSingleGroupScope(groupId);
            member.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
            createActiveGroupMember(member, scope);
            sendGroupNotificationForReqToJoinGroupFreely(group, member);
            // createPendingGroupMember(member, scope);
            // sendGroupNotificationForReqToJoinGroupWaitingApproval(group, member);
        } else {
            if(GroupMemberStatus.fromCode(member.getMemberStatus()) == GroupMemberStatus.WAITING_FOR_ACCEPTANCE) {
                member.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
                member.setMemberNickName(user.getNickName());
                member.setMemberAvatar(user.getAvatar());
                updatePendingGroupMemberToActive(member);
                
                GroupMember inviter = this.groupProvider.findGroupMemberByMemberInfo(groupId, 
                    EntityType.USER.getCode(), member.getInviterUid());
                sendGroupNotificationForInviteToJoinGroupFreely(group, inviter, member);
            }
        }
    }
    
    @Override
    public List<CommandResult> inviteToJoinGroup(InviteToJoinGroupCommand cmd) {
        long startTime = System.currentTimeMillis();
        User operator = UserContext.current().getUser();
        Long operatorUid = operator.getId();
        
        Long groupId = cmd.getGroupId();
        Group group = checkGroupParameter(groupId, operatorUid, "inviteToJoinGroup");
        
        List<CommandResult> resultList = new ArrayList<CommandResult>();
        CommandResult result = null;
        List<Long> inviteeIds = cmd.getUserIds();
        int userCount = 0;
        if(inviteeIds != null) {
            userCount = inviteeIds.size();

            //用于发信息
            String inviteeNames = "";
            ListMemberInStatusCommand listMemberInStatusCommand = new ListMemberInStatusCommand();
            listMemberInStatusCommand.setGroupId(group.getId());
            listMemberInStatusCommand.setPageSize(1000000);
            ListMemberCommandResponse response = this.listMembersInStatus(listMemberInStatusCommand);
            List<Long> includeList = new ArrayList<Long>();
            if(response != null && response.getMembers() != null && response.getMembers().size() != 0){
                for(int i = 0; i<response.getMembers().size(); i++){
                    if(response.getMembers().get(i).getMemberId() != operatorUid.longValue()){
                        includeList.add(response.getMembers().get(i).getMemberId());
                    }

                }
            }


            for(Long inviteeId : inviteeIds) {
                if(inviteeId == null) {
                    LOGGER.error("The invited user id is null, operatorId=" + operator.getId() 
                        + ", groupId=" + groupId + ", inviteeId=" + inviteeId);
                    continue;
                }
                User invitee = this.userProvider.findUserById(inviteeId);
                if(invitee == null) {
                    LOGGER.error("The invitee is not found, operatorId=" + operator.getId() 
                        + ", groupId=" + groupId + ", inviteeId=" + inviteeId);
                    continue;
                }
                result = inviteToJoinGroup(operator, group, inviteeId, cmd.getInvitationText(), true);
                resultList.add(result);

                inviteeNames = inviteeNames + invitee.getNickName() + "、";
            }

            if(inviteeNames.length() > 0){
                sendGroupNotificationForInviteToJoinGroupToinviter(group, operator.getId(), inviteeNames.substring(0, inviteeNames.length() - 1));
                sendGroupNotificationForInviteToJoinGroupToOthers(group, operator.getId(), inviteeNames.substring(0, inviteeNames.length() - 1), includeList);
            }

        }
        
        if(LOGGER.isInfoEnabled()) {
            CrossShardListingLocator locator = new CrossShardListingLocator(group.getId());
            List<GroupMember> members = this.groupProvider.queryGroupMembers(locator, 11 + 1,
                (loc,query) -> {
                    query.addConditions(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.ACTIVE.getCode()));
                    return query;
                });
            long endTime = System.currentTimeMillis();
            LOGGER.info("Invite user to group by user id, elapse=" + (endTime - startTime) + ", operatorUid=" 
                + operatorUid + ", userCount=" + userCount + ", resultCount=" + resultList.size() + ", memberCount=" + members.size() + ", cmd=" + cmd);
        }
        if(LOGGER.isDebugEnabled()) {
            for(CommandResult temp : resultList) {
                LOGGER.info("Invite user to group by user id, operatorUid=" + operatorUid + ", result=" + temp);
            }
        }
        
        return resultList;
    }
    
    @Override
    public List<CommandResult> inviteToJoinGroupByPhone(InviteToJoinGroupByPhoneCommand cmd) {
        long startTime = System.currentTimeMillis();
        User operator = UserContext.current().getUser();
        Long operatorUid = operator.getId();
        
        Long groupId = cmd.getGroupId();
        Group group = checkGroupParameter(groupId, operatorUid, "inviteToJoinGroupByPhone");
        
        List<CommandResult> resultList = new ArrayList<CommandResult>();
        CommandResult result = null;
        List<String> inviteePhones = cmd.getPhones();
        int phoneCount = 0;
        if(inviteePhones != null) {
            phoneCount = inviteePhones.size();
            for(String phone : inviteePhones) {
                UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(operator.getNamespaceId(), phone);
                if(userIdentifier == null) {
                    LOGGER.error("The user is not found by specific phone, operatorId=" + operator.getId() + ", phone=" + phone);
                    result = new CommandResult();
                    result.setErrorScope(GroupServiceErrorCode.SCOPE);
                    result.setErrorCode(GroupServiceErrorCode.ERROR_GROUP_PHONE_USER_NOT_FOUND);
                    result.setErrorDescription("The user is not found by specific phone");
                } else {
                    result = inviteToJoinGroup(operator, group, userIdentifier.getOwnerUid(), cmd.getInvitationText(), true);
                }
                result.setIdentifier(phone);
                resultList.add(result);
            }
        }
        
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("Invite user to group by user phone, elapse=" + (endTime - startTime) + ", operatorUid=" + operatorUid 
                + ", phoneCount=" + phoneCount + ", resultCount=" + resultList.size() + ", cmd=" + cmd);
        }
        if(LOGGER.isDebugEnabled()) {
            for(CommandResult temp : resultList) {
                LOGGER.info("Invite user to group by user phone, operatorUid=" + operatorUid + ", result=" + temp);
            }
        }
        
        return resultList;
    }
    
    @Override
    public List<CommandResult> inviteToJoinGroupByFamily(InviteToJoinGroupByFamilyCommand cmd) {
        long startTime = System.currentTimeMillis();
        User operator = UserContext.current().getUser();
        Long operatorUid = operator.getId();
        
        Long groupId = cmd.getGroupId();
        Group group = checkGroupParameter(groupId, operatorUid, "inviteToJoinGroupByFamily");
        
        List<CommandResult> resultList = new ArrayList<CommandResult>();
        CommandResult result = null;
        List<Long> familyIds = cmd.getFamilyIds();
        int familyCount = 0;
        if(familyIds != null) {
            familyCount = familyIds.size();
            Group family = null;
            List<GroupMember> familyMembers = null;
            for(Long familyId : familyIds) {
                family = this.groupProvider.findGroupById(familyId);
                if(family == null) {
                    LOGGER.error("Family not found, operatorUid=" + operatorUid + ", groupId=" + groupId + ", familyId=" + familyId);
                    result = new CommandResult();
                    result.setIdentifier(String.valueOf(familyId));
                    result.setErrorScope(GroupServiceErrorCode.SCOPE);
                    result.setErrorCode(GroupServiceErrorCode.ERROR_GROUP_FAMILY_NOT_FOUND);
                    result.setErrorDescription("Family not found");
                    resultList.add(result);
                    continue;
                }
                if(GroupDiscriminator.FAMILY != GroupDiscriminator.fromCode(family.getDiscriminator())) {
                    LOGGER.error("It is not family id, operatorUid=" + operatorUid + ", groupId=" + groupId 
                        + ", familyId=" + familyId + ", discriminator=" + family.getDiscriminator());
                    result = new CommandResult();
                    result.setIdentifier(String.valueOf(familyId));
                    result.setErrorScope(GroupServiceErrorCode.SCOPE);
                    result.setErrorCode(GroupServiceErrorCode.ERROR_GROUP_FAMILY_NOT_FOUND);
                    result.setErrorDescription("Family not found");
                    resultList.add(result);
                    continue;
                }
                
                familyMembers = this.groupProvider.findGroupMemberByGroupId(familyId);
                if(familyMembers == null || familyMembers.size() == 0) {
                    if(LOGGER.isInfoEnabled()) {
                        LOGGER.info("The family has no member yet, operatorUid=" + operatorUid + ", groupId=" + groupId 
                        + ", familyId=" + familyId);
                    }
                    result = new CommandResult();
                    result.setIdentifier(String.valueOf(familyId));
                    result.setErrorScope(GroupServiceErrorCode.SCOPE);
                    result.setErrorCode(GroupServiceErrorCode.ERROR_GROUP_FAMILY_MEMBER_NOT_FOUND);
                    result.setErrorDescription("Family member not found");
                    resultList.add(result);
                } else {
                    for(GroupMember familyMember : familyMembers) {
                        if(familyMember.getMemberId() != operatorUid) {
                            result = inviteToJoinGroup(operator, group, familyMember.getMemberId(), cmd.getInvitationText(), false);
                            result.setIdentifier(String.valueOf(familyId) + ":" + result.getIdentifier());
                            resultList.add(result);
                        }
                    }
                }
            }
        }
        
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("Invite user to group by family, elapse=" + (endTime - startTime) + ", operatorUid=" + operatorUid 
                + ", familyCount=" + familyCount + ", resultCount=" + resultList.size() + ", cmd=" + cmd);
        }
        if(LOGGER.isDebugEnabled()) {
            for(CommandResult temp : resultList) {
                LOGGER.info("Invite user to group by family, operatorUid=" + operatorUid + ", result=" + temp);
            }
        }
        
        return resultList;
    }
    
    @Override
    public void acceptJoinGroupInvitation(AcceptJoinGroupInvitation cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        String tag = "acceptJoinGroupInvitation";
        
        Long groupId = cmd.getGroupId();
        Group group = checkGroupParameter(groupId, userId, tag);
        GroupMember member = checkGroupMemberParameter(group, userId, userId, tag);

        GroupMemberStatus memberStatus = GroupMemberStatus.fromCode(member.getMemberStatus());
        switch(memberStatus) {
        case ACTIVE:
            LOGGER.error("Target user is already in the group, operatorUid=" + userId 
                + ", groupId=" + groupId);
            // 不在正确的状态，说明已经被处理过，为了客户端可以点击黑条消失，故不抛异常 by lqs 20160128
//            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, GroupServiceErrorCode.ERROR_USER_ALREADY_IN_GROUP,
//                "You are already in the group");
            break;
        case WAITING_FOR_ACCEPTANCE:
            member.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
            member.setMemberNickName(user.getNickName());
            member.setMemberAvatar(user.getAvatar());
            updatePendingGroupMemberToActive(member);
            
            GroupMember inviter = this.groupProvider.findGroupMemberByMemberInfo(groupId, 
                EntityType.USER.getCode(), member.getInviterUid());
            sendGroupNotificationForAcceptJoinGroupInvitation(group, inviter, member);
            break;
        default:
            LOGGER.error("Group member is not in acceptance state, userId=" + userId + ", groupId=" + groupId 
                + ", member=" + member);
            // 不在正确的状态，说明已经被处理过，为了客户端可以点击黑条消失，故不抛异常 by lqs 20160128
//            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, 
//                GroupServiceErrorCode.ERROR_GROUP_MEMBER_NOT_IN_ACCEPT_STATE, 
//                "Group member is not in acceptance state");
            break;
        }
    }
    
    @Override
    public void rejectJoinGroupInvitation(RejectJoinGroupInvitation cmd) {
    	User operator = UserContext.current().getUser();
    	Long operatorUid = operator.getId();
        String tag = "rejectJoinGroupInvitation";
        
    	Long groupId = cmd.getGroupId();
        Group group = checkGroupParameter(groupId, operatorUid, tag);
        GroupMember member = checkGroupMemberParameter(group, operatorUid, operatorUid, tag);

        GroupMemberStatus memberStatus = GroupMemberStatus.fromCode(member.getMemberStatus());
        switch(memberStatus) {
        case ACTIVE:
            LOGGER.error("Target user is already in the group, operatorUid=" + operatorUid 
                + ", groupId=" + groupId);
            // 不在正确的状态，说明已经被处理过，为了客户端可以点击黑条消失，故不抛异常 by lqs 20160128
//            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, GroupServiceErrorCode.ERROR_USER_ALREADY_IN_GROUP,
//                "You are already in the group");
            break;
        case WAITING_FOR_ACCEPTANCE:
            deletePendingGroupMember(operatorUid, member);
            member.setMemberStatus(GroupMemberStatus.REJECT.getCode());
            addGroupMemberLog(member, group, cmd.getRejectText());
            
            GroupMember inviter = this.groupProvider.findGroupMemberByMemberInfo(groupId, 
                EntityType.USER.getCode(), member.getInviterUid());
            sendGroupNotificationForRejectJoinGroupInvitation(group, inviter, member);
            break;
        default:
            LOGGER.error("Group member is not in acceptance state, operatorUid=" + operatorUid + ", groupId=" + groupId 
                + ", member=" + member);
            // 不在正确的状态，说明已经被处理过，为了客户端可以点击黑条消失，故不抛异常 by lqs 20160128
//            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, 
//                    GroupServiceErrorCode.ERROR_GROUP_MEMBER_NOT_IN_ACCEPT_STATE, 
//                    "Group member is not in acceptance state");
            break;
        }
    }
    
    private void addGroupMemberLog(GroupMember member, Group group, String rejectText) {
        GroupMemberLog memberLog = ConvertHelper.convert(member, GroupMemberLog.class);
        memberLog.setNamespaceId(group.getNamespaceId());
        memberLog.setMemberStatus(member.getMemberStatus());
        memberLog.setOperatorUid(UserContext.currentUserId());
        memberLog.setApproveTime(DateUtils.currentTimestamp());
        memberLog.setGroupMemberId(member.getId());
        memberLog.setCreatorUid(UserContext.currentUserId());
        memberLog.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        memberLog.setCommunityId(group.getFamilyCommunityId());
        memberLog.setAddressId(group.getFamilyAddressId());
        memberLog.setRejectText(rejectText);
        groupMemberLogProvider.createGroupMemberLog(memberLog);
	}

	@Override
    public void approveJoinGroupRequest(ApproveJoinGroupRequestCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorUid = operator.getId();
        String tag = "approveJoinGroupRequest";
        
        Long groupId = cmd.getGroupId();
        Group group = checkGroupParameter(groupId, operatorUid, tag);

        //行业协会做特殊处理，超级管理员也可以审核
        if(GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(group.getPrivateFlag()) && ClubType.GUILD == ClubType.fromCode(group.getClubType())){
            checkGroupPrivilegeForGuild(operator.getId(), groupId, PrivilegeConstants.GroupApproveMember, cmd.getOrganizationId());
        }else {
            checkGroupPrivilege(operator.getId(), groupId, PrivilegeConstants.GroupApproveMember);
        }

        Long userId = cmd.getUserId();
        GroupMember member = checkGroupMemberParameter(group, operatorUid, userId, tag);
        
        GroupMemberStatus memberStatus = GroupMemberStatus.fromCode(member.getMemberStatus());
        switch(memberStatus) {
        case ACTIVE:
            LOGGER.error("Target user is already in the group, operatorUid=" + operatorUid 
                + ", groupId=" + groupId + ", inviteeId=" + cmd.getUserId());
            // 不在正确的状态，说明已经被处理过，为了客户端可以点击黑条消失，故不抛异常 by lqs 20160128
//            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, GroupServiceErrorCode.ERROR_USER_ALREADY_IN_GROUP,
//                "Target user is already in the group");
            break;
        case WAITING_FOR_APPROVAL:
            member.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
            member.setApproveTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            member.setOperatorUid(operatorUid);
            
            updatePendingGroupMemberToActive(member);

            // 加入group事件
            LocalEventBus.publish(event -> {
                LocalEventContext context = new LocalEventContext();
                context.setNamespaceId(group.getNamespaceId());
                context.setUid(userId);
                event.setContext(context);

                event.setEntityType(EhGroupMembers.class.getSimpleName());
                event.setEntityId(member.getId());
                event.setEventName(SystemEvent.GROUP_GROUP_JOIN_APPROVAL.dft());

                event.addParam("group", StringHelper.toJsonString(group));
                event.addParam("member", StringHelper.toJsonString(member));
            });
            
            GroupMember approver = this.groupProvider.findGroupMemberByMemberInfo(groupId, 
                EntityType.USER.getCode(), operatorUid);
            sendGroupNotificationForApproveJoinGroupRequest(group, approver, member);
            break;
        default:
            LOGGER.error("Group member is not in waiting approval state, operatorUid=" + operatorUid + ", groupId=" + groupId 
                + ", member=" + member);
            // 不在正确的状态，说明已经被处理过，为了客户端可以点击黑条消失，故不抛异常 by lqs 20160128
//            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, 
//                    GroupServiceErrorCode.ERROR_GROUP_MEMBER_NOT_IN_APPROVAL_STATE, 
//                    "Group member is not in waiting approval state");
            break;
        }
    }

    @Override
    public void rejectJoinGroupRequest(RejectJoinGroupRequestCommand cmd) {
    	User operator = UserContext.current().getUser();
    	Long operatorUid = operator.getId();
    	String tag = "rejectJoinGroupRequest";
        
    	Long groupId = cmd.getGroupId();
    	Group group = checkGroupParameter(groupId, operatorUid, tag);

        //行业协会做特殊处理，超级管理员也可以审核
        if(GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(group.getPrivateFlag()) && ClubType.GUILD == ClubType.fromCode(group.getClubType())){
            checkGroupPrivilegeForGuild(operator.getId(), groupId, PrivilegeConstants.GroupApproveMember, cmd.getOrganizationId());
        }else {
            checkGroupPrivilege(operator.getId(), groupId, PrivilegeConstants.GroupRejectMember);
        }
        
        Long userId = cmd.getUserId();
        GroupMember member = checkGroupMemberParameter(group, operatorUid, userId, tag);
        
        GroupMemberStatus memberStatus = GroupMemberStatus.fromCode(member.getMemberStatus());
        switch(memberStatus) {
        case ACTIVE:
            LOGGER.error("Target user is already in the group, operatorUid=" + operatorUid 
                + ", groupId=" + groupId + ", inviteeId=" + cmd.getUserId());
            // 不在正确的状态，说明已经被处理过，为了客户端可以点击黑条消失，故不抛异常 by lqs 20160128
//            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, 
//                GroupServiceErrorCode.ERROR_USER_ALREADY_IN_GROUP,
//                "You is already in the group");
            break;
        case WAITING_FOR_APPROVAL:
            deletePendingGroupMember(operatorUid, member);
            member.setMemberStatus(GroupMemberStatus.REJECT.getCode());
            addGroupMemberLog(member, group, cmd.getRejectText());

            GroupMember rejecter = this.groupProvider.findGroupMemberByMemberInfo(groupId, 
                EntityType.USER.getCode(), operatorUid);
            sendGroupNotificationForRejectJoinGroupRequest(group, rejecter, member, cmd.getRejectText());
            break;
        default:
            LOGGER.error("Group member is not in waiting approval state, operatorUid=" + operatorUid + ", groupId=" + groupId 
                + ", member=" + member);
            // 不在正确的状态，说明已经被处理过，为了客户端可以点击黑条消失，故不抛异常 by lqs 20160128
//            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, 
//                    GroupServiceErrorCode.ERROR_GROUP_MEMBER_NOT_IN_APPROVAL_STATE, 
//                    "Group member is not in waiting approval state");
            break;
        }
    }
    
    @Override
    public void leaveGroup(LeaveGroupCommand cmd) {
        User user = UserContext.current().getUser();
        long userId = user.getId();
        String tag = "leaveGroup";
        
        Long groupId = cmd.getGroupId();
        Group group = checkGroupParameter(groupId, userId, tag);

        GroupMember member = checkGroupMemberParameter(group, userId, userId, tag);
        GroupMemberStatus memberStatus = GroupMemberStatus.fromCode(member.getMemberStatus());
        switch(memberStatus) {
        case ACTIVE:
            Long memberRole = member.getMemberRole();
            if(memberRole != null && memberRole.longValue() == Role.ResourceCreator) {
                LOGGER.error("Creator can't leave the group, userId=" + userId 
                    + ", groupId=" + groupId + ", memberId=" + userId + ", memberRole=" + memberRole);
                // 按产品设计，把圈子概念变成两个：群聊（对应私有圈）、俱乐部（对应兴趣圈） by lqs 20160505
                int code = GroupServiceErrorCode.ERROR_GROUP_CREATOR_LEAVE_NOT_ALLOW;
                if(GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PUBLIC) {
                    code = GroupServiceErrorCode.ERROR_GROUP_CLUB_CREATOR_LEAVE_NOT_ALLOW;
                }
                throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, 
                    code, "Creator can't leave the group");
            } else {
                deleteActiveGroupMember(userId, member, "leave group");
                sendNotificationForLeaveToCreator(group.getCreatorUid(), member, user.getLocale());
            }
            
            // 俱乐部退出不发消息，add by tt, 20161115
            if (GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PUBLIC) {
            	sendGroupNotificationForMemberLeaveGroup(group, member);
			}

            // 退出group事件
            LocalEventBus.publish(event -> {
                LocalEventContext context = new LocalEventContext();
                context.setNamespaceId(group.getNamespaceId());
                context.setUid(member.getMemberId());
                event.setContext(context);

                event.setEntityType(EhGroupMembers.class.getSimpleName());
                event.setEntityId(member.getId());
                event.setEventName(SystemEvent.GROUP_GROUP_LEAVE.dft());

                event.addParam("group", StringHelper.toJsonString(group));
                event.addParam("member", StringHelper.toJsonString(member));
            });
            break;
        default:
            LOGGER.error("Target user is not an active group member, operatorUid=" + userId + ", groupId=" + groupId 
                + ", member=" + member);
            // 不在正确的状态，说明已经被处理过，为了客户端可以点击黑条消失，故不抛异常 by lqs 20160128
//            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, 
//                    GroupServiceErrorCode.ERROR_USER_NO_IN_GROUP, 
//                    "You are not an active group member");
            break;
        }
    }
    
    @Override
    public void revokeGroupMember(RevokeGroupMemberCommand cmd) {
    	User operator = UserContext.current().getUser();
    	Long operatorUid = operator.getId();
    	String tag = "revokeGroupMember";
        
        Long groupId = cmd.getGroupId();
        Group group = checkGroupParameter(groupId, operatorUid, tag);

        //群管理员群聊删人显示 Insufficient privilege 所以注释掉 modified by xiongying 20160614
        //checkGroupPrivilege(operator.getId(), groupId, PrivilegeConstants.GroupRevokeMember);
        
        Long userId = cmd.getUserId();
        GroupMember member = checkGroupMemberParameter(group, operatorUid, userId, tag);
        GroupMemberStatus memberStatus = GroupMemberStatus.fromCode(member.getMemberStatus());
        switch(memberStatus) {
        case ACTIVE:
            Long memberRole = member.getMemberRole();
            if(memberRole != null && memberRole.longValue() == Role.ResourceCreator) {

                int code = GroupServiceErrorCode.ERROR_GROUP_CREATOR_REVOKED_NOT_ALLOW;

                //俱乐部和行业协会使用自己的文案  add by yanjun 20171115
                if (GroupDiscriminator.GROUP == GroupDiscriminator.fromCode(group.getDiscriminator()) && GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(group.getPrivateFlag())) {
                    if(ClubType.GUILD == ClubType.fromCode(group.getClubType())){
                        code = GroupServiceErrorCode.ERROR_GROUP_CREATOR_REVOKED_NOT_ALLOW_FOR_GUILD;
                    }else {
                        code = GroupServiceErrorCode.ERROR_GROUP_CREATOR_REVOKED_NOT_ALLOW_FOR_CLUB;
                    }
                }

                LOGGER.error("Creator can't be revoked from the group, userId=" + userId 
                    + ", groupId=" + groupId + ", memberId=" + userId + ", memberRole=" + memberRole);
                throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE,
                        code,
                        "Creator can't be revoked from the group");
            } else {
                deleteActiveGroupMember(userId, member, cmd.getRevokeText());
                
                GroupMember revoker = this.groupProvider.findGroupMemberByMemberInfo(groupId, 
                    EntityType.USER.getCode(), operatorUid);
                // 俱乐部移除成员时不发消息，add by tt, 20161104
                if (GroupDiscriminator.GROUP == GroupDiscriminator.fromCode(group.getDiscriminator()) && GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(group.getPrivateFlag())) {
    				return;
    			}
                sendGroupNotificationForRevokeGroupMember(group, revoker, member);
            }
            
//            sendGroupNotificationForMemberLeaveGroup(group, member);
            break;
        default:
            LOGGER.error("Target user is not an active group member, operatorUid=" + userId + ", groupId=" + groupId 
                + ", member=" + member + ", memberStatus=" + memberStatus);
            // 不在正确的状态，说明已经被处理过，为了客户端可以点击黑条消失，故不抛异常 by lqs 20160128
//            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, 
//                    GroupServiceErrorCode.ERROR_USER_NO_IN_GROUP, 
//                    "Target user is not an active group member");
            break;
        }
    }


    @Override
    public void revokeGroupMemberList(RevokeGroupMemberListCommand cmd) {
        RevokeGroupMemberCommand cmdOne = new RevokeGroupMemberCommand();
        cmdOne.setGroupId(cmd.getGroupId());
        cmdOne.setRevokeText(cmd.getRevokeText());

        String revokeNames = "";
        if(cmd.getUserIds() != null && cmd.getUserIds().size() > 0){
            for(int i = 0; i< cmd.getUserIds().size(); i++){
                cmdOne.setUserId(cmd.getUserIds().get(i));
                this.revokeGroupMember(cmdOne);

                User user = userProvider.findUserById(cmdOne.getUserId());
                revokeNames = revokeNames + user.getNickName() + "、";
            }
            sendGroupNotificationForRevokeGroupMemberToOpeartor(cmd.getGroupId(), UserContext.current().getUser().getId(), revokeNames.substring(0, revokeNames.length() - 1));
        }
    }

    @Override
    public List<GroupMemberDTO> listGroupWaitingAcceptances() {
        long startTime = System.currentTimeMillis();
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        
        List<GroupMemberDTO> memberDTOs = new ArrayList<GroupMemberDTO>();
        List<UserGroup> userGroupList = this.userProvider.listUserGroups(userId, GroupDiscriminator.GROUP.getCode());
        if(userGroupList == null || userGroupList.size() == 0) {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.info("No user group is found, userId=" + userId);
            }
        } else {
            GroupMember groupMember = null;
            GroupMemberDTO memberDTO = null;
            Long groupId = null;
            for(UserGroup userGroup : userGroupList) {
                if(GroupMemberStatus.WAITING_FOR_ACCEPTANCE == GroupMemberStatus.fromCode(userGroup.getMemberStatus())) {
                    groupId = userGroup.getGroupId();
                    groupMember = this.groupProvider.findGroupMemberByMemberInfo(groupId, EntityType.USER.getCode(), userId);
                    if(groupMember != null) {
                        memberDTO = ConvertHelper.convert(groupMember, GroupMemberDTO.class);
                        populateGroupMemberDTO(userId, null, memberDTO);
                        memberDTOs.add(memberDTO);
                    }
                }
            }
        }
        
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("List group waiting acceptances, userId=" + userId + ", size=" + memberDTOs.size() 
                + ", elapse=" + (endTime - startTime));
        }
        
        return memberDTOs;
    }
    
    @Override
    public ListGroupWaitingApprovalsCommandResponse listGroupWaitingApprovals(ListGroupWaitingApprovalsCommand cmd) {
        long startTime = System.currentTimeMillis();
        User user = UserContext.current().getUser();
        Long operatorUid = user.getId();
        
        Long groupId = cmd.getGroupId();
        checkGroupParameter(groupId, operatorUid, "listGroupWaitingApprovals");
        
        checkGroupPrivilege(user.getId(), groupId, PrivilegeConstants.GroupAdminOps);

        ListGroupWaitingApprovalsCommandResponse cmdResponse = new ListGroupWaitingApprovalsCommandResponse();
        
        ListingLocator locator = new ListingLocator(groupId);
        locator.setAnchor(cmd.getPageAnchor());
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        
        List<GroupMember> memberList = this.groupProvider.queryGroupMembers(locator, (pageSize + 1), (loc, query) -> {
            query.addConditions(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode()));
            return query;
        });

        if(memberList.size() > pageSize) {
            memberList.remove(memberList.size() - 1);
            cmdResponse.setNextPageAnchor(memberList.get(memberList.size() - 1).getId());
        }
        
        List<GroupMemberDTO> requestDTOList = memberList.stream().map((r)-> { 
            GroupMemberDTO dto = ConvertHelper.convert(r, GroupMemberDTO.class);
            populateGroupMemberDTO(operatorUid, null, dto);
            return dto;
            }).collect(Collectors.toList());
        cmdResponse.setRequests(requestDTOList); 
        
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("List group waiting approvals, operatorUid=" + operatorUid + ", size=" + requestDTOList.size() 
                + ", groupId=" + groupId + ", pageAnchor=" + cmd.getPageAnchor() + ", pageSize=" + pageSize 
                + ", nextPageAnchor=" + cmdResponse.getNextPageAnchor() + ", elapse=" + (endTime - startTime));
        }
        
        return cmdResponse;
    }
    
    
    @Override
    public ListMemberCommandResponse listMembersInRole(ListMemberInRoleCommand cmd) {
        long startTime = System.currentTimeMillis();
        User operator = UserContext.current().getUser();
        long operatorUid = operator.getId();
                
        List<Long> roleIds = cmd.getRoleIds();
        Long groupId = cmd.getGroupId();
        Group group = checkGroupParameter(groupId, operatorUid, "listMembersInRole");
                
        checkGroupPrivilege(operator.getId(), groupId, PrivilegeConstants.GroupListMember);
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator(group.getId());
        locator.setAnchor(cmd.getPageAnchor());
        
        List<GroupMember> members = this.groupProvider.queryGroupMembers(locator, pageSize + 1,
            (loc, query) -> {
                Condition c = null;
                if(roleIds != null && roleIds.size() > 0) {
                    c = Tables.EH_GROUP_MEMBERS.MEMBER_ROLE.eq(roleIds.get(0));
                    for(int i = 1; i < roleIds.size(); i++) {
                        c = c.or(Tables.EH_GROUP_MEMBERS.MEMBER_ROLE.eq(roleIds.get(i)));
                    }
                }
                if(c == null) {
                    query.addConditions(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.ACTIVE.getCode()));
                } else {
                    query.addConditions(c.and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.ACTIVE.getCode())));
                }
                
                return query;
            });
        
        Long nextPageAnchor = null;
        if(members.size() > pageSize) {
            members.remove(members.size() - 1);
            nextPageAnchor = members.get(members.size() -1).getId();
        }

        List<GroupMemberDTO> memberDtos = members.stream()
                .map((r) -> { return ConvertHelper.convert(r, GroupMemberDTO.class);})
                .collect(Collectors.toList());
        populateGroupMemberDTOs(operatorUid, group, memberDtos);
        
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("List group member in role, operatorUid=" + operatorUid + ", size=" + memberDtos.size() 
                + ", groupId=" + groupId + ", roleIds=" + cmd.getRoleIds() + ", pageAnchor=" + cmd.getPageAnchor() 
                + ", pageSize=" + pageSize + ", nextPageAnchor=" + nextPageAnchor + ", elapse=" + (endTime - startTime));
        }
        
        return new ListMemberCommandResponse(nextPageAnchor, memberDtos); 
    }
    
    public ListMemberCommandResponse listMembersInStatus(ListMemberInStatusCommand cmd) {
        long startTime = System.currentTimeMillis();
        User operator = UserContext.current().getUser();
        Long operatorUid = operator.getId();
        String tag = "listMembersInStatus, cmd=" + cmd.toString(); 
        
        Long groupId = cmd.getGroupId();
        Group group = checkGroupParameter(groupId, operatorUid, tag);
        //organization的member不在group里面 所以先去掉校验by xiongying 20160524
//        checkGroupPrivilege(operator.getId(), cmd.getGroupId(), PrivilegeConstants.GroupListMember);
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long pageAnchor = cmd.getPageAnchor() == null?0L:cmd.getPageAnchor();
        List<GroupMember> members = null;
        Long nextPageAnchor = null;
        
        // 如果是按创建者、管理员、成员顺序排序再按加入时间排序，不能按锚点分页，add by tt, 20161115

        // 俱乐部的成员需要按创建者、管理员、成员的顺序按加入时间倒序排列，add by tt, 20161103
        if (GroupDiscriminator.fromCode(group.getDiscriminator()) == GroupDiscriminator.GROUP
                && GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PUBLIC) {
//        	if (TrueOrFalseFlag.fromCode(cmd.getIncludeCreator()) == TrueOrFalseFlag.FALSE) {
//				query.addConditions(Tables.EH_GROUP_MEMBERS.MEMBER_ID.ne(group.getCreatorUid()));
//			}
//			query.addOrderBy(Tables.EH_GROUP_MEMBERS.MEMBER_ROLE.asc());

			Long from = pageAnchor * pageSize;

            //俱乐部删除的成员在groupmember中已经不存在，被放到groupmemberlog中，现在只能去log表查了
            // 此接口返回的数据是GroupMemberDTO，实际上在groupMember中已经不存在，因此此数据仅用于展现不可用于其他逻辑  add by yanjun 20171114
            if(GroupMemberStatus.REJECT == GroupMemberStatus.fromCode(cmd.getStatus())){
                List<GroupMemberLog> list = groupMemberLogProvider.listGroupMemberLogByGroupId(cmd.getGroupId(), cmd.getKeyword(), from, pageSize+1);
                if(list.size() > pageSize) {
                    list.remove(list.size() - 1);
                    nextPageAnchor = pageAnchor + 1;
                }

                List<GroupMemberDTO> dtos = getGroupMemberDtoFromLog(list);

                ListMemberCommandResponse response = new ListMemberCommandResponse();
                response.setMembers(dtos);
                response.setNextPageAnchor(nextPageAnchor);
                return response;
            }


            boolean includeCreator = TrueOrFalseFlag.fromCode(cmd.getIncludeCreator()) == TrueOrFalseFlag.TRUE;
            members = groupProvider.listPublicGroupMembersByStatus(
                    cmd.getGroupId(), cmd.getKeyword(), cmd.getStatus(),
                    from, pageSize+1, includeCreator, group.getCreatorUid());
			if(members.size() > pageSize) {
	            members.remove(members.size() - 1);
	            nextPageAnchor = pageAnchor + 1;
	        }
		} else {
			CrossShardListingLocator locator = new CrossShardListingLocator(group.getId());
	        locator.setAnchor(cmd.getPageAnchor());
	        
	        members = this.groupProvider.queryGroupMembers(locator, pageSize + 1, (loc,query) -> {
                if(cmd.getStatus() != null) {
                    query.addConditions(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(cmd.getStatus()));
                }
                return query;
            });
	        
	        if(members.size() > pageSize) {
	            members.remove(members.size() - 1);
	            nextPageAnchor = members.get(members.size() -1).getId();
	        }
		}

        List<GroupMemberDTO> memberDtos = members.stream().map(
                (r) -> ConvertHelper.convert(r, GroupMemberDTO.class)).collect(Collectors.toList());

        populateGroupMemberDTOs(operatorUid, group, memberDtos);
        
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("List group member in status, operatorUid=" + operatorUid + ", size=" + memberDtos.size() + ", memberSize=" + members.size()
                + ", groupId=" + groupId + ", status=" + cmd.getStatus() + ", pageAnchor=" + cmd.getPageAnchor() 
                + ", pageSize=" + pageSize + ", nextPageAnchor=" + nextPageAnchor + ", elapse=" + (endTime - startTime));
        }
        return new ListMemberCommandResponse(nextPageAnchor, memberDtos);
    }

    private List<GroupMemberDTO> getGroupMemberDtoFromLog(List<GroupMemberLog> listLog){

        if(listLog == null || listLog.size() ==0){
            return null;
        }

        List<GroupMemberDTO> dtos = new ArrayList<>();
        for(GroupMemberLog log: listLog){
            GroupMemberDTO dto = ConvertHelper.convert(log, GroupMemberDTO.class);
            dto.setRejectTime(log.getApproveTime());
            dto.setId(log.getGroupMemberId());

            User user = userProvider.findUserById(log.getMemberId());
            if(user != null){
                dto.setGender(user.getGender());
                dto.setMemberNickName(user.getNickName());
            }

            UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(log.getMemberId(), IdentifierType.MOBILE.getCode());
            if(userIdentifier != null){
                dto.setCellPhone(userIdentifier.getIdentifierToken());
            }

            Group group = groupProvider.findGroupById(dto.getGroupId());
            //行业协会加入申请时的信息，包括企业等  add by yanjun 20171107
            if(ClubType.fromCode(group.getClubType()) == ClubType.GUILD){
                GuildApply guildApply = groupProvider.findGuildApplyByGroupMemberId(log.getGroupMemberId());
                GuildApplyDTO guildApplyDTO = ConvertHelper.convert(guildApply, GuildApplyDTO.class);
                populateGuildApplyDTO(guildApplyDTO);
                dto.setGuildApplyDTO(guildApplyDTO);
            }

            dtos.add(dto);

        }
        return dtos;


    }

    
    @Override
    public void requestToBeAdmin(RequestAdminRoleCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        String tag = "requestToBeAdmin";
        
        Long groupId = cmd.getGroupId();
        Group group = checkGroupParameter(groupId, userId, tag);
        GroupMember member = checkGroupMemberParameter(group, userId, userId, tag);
        
        checkGroupPrivilege(user.getId(), groupId, PrivilegeConstants.GroupRequestAdminRole);
        
        GroupOpRequest request = this.groupProvider.findGroupOpRequestByRequestor(groupId, user.getId());
        if(request == null) {
            request = new GroupOpRequest();
            request.setGroupId(groupId);
            request.setRequestorUid(user.getId());
            request.setOperationType(GroupOpType.REQUEST_ADMIN_ROLE.getCode());
            request.setStatus(GroupOpRequestStatus.REQUESTING.getCode());
            request.setRequestorComment(cmd.getRequestText());
            request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.groupProvider.createGroupOpRequest(request);
            
            sendGroupNotificationForReqToBeGroupAdminWaitingApproval(group, member);
        } else {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("The group admin op request is already existed, request=" + request);
            }
        }
    }
    
    @Override
    public void inviteToBeAdmin(InviteToBeAdminCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        String tag = "inviteToBeAdmin";
        
        Long groupId = cmd.getGroupId();
        Long targetUid = cmd.getUserId();
        Group group = checkGroupParameter(groupId, operatorId, tag);
        GroupMember member = checkGroupMemberParameter(group, operatorId, targetUid, tag);

        checkGroupPrivilege(operator.getId(), groupId, PrivilegeConstants.GroupInviteAdminRole);
        
        GroupOpRequest request = this.groupProvider.findGroupOpRequestByRequestor(groupId, targetUid);
        if(request == null) {
            request = new GroupOpRequest();
            request.setGroupId(groupId);
            request.setRequestorUid(operator.getId());
            request.setOperationType(GroupOpType.INVITE_ADMIN_ROLE.getCode());
            request.setTargetUid(targetUid);
            request.setStatus(GroupOpRequestStatus.ACCEPTED.getCode());
            request.setRequestorComment(cmd.getInvitationText());
            request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.groupProvider.createGroupOpRequest(request);
            
            Long memberRole = member.getMemberRole();
            if(memberRole == null || (memberRole != Role.ResourceAdmin && memberRole != Role.ResourceCreator)) {
                member.setMemberRole(Role.ResourceAdmin);
                this.groupProvider.updateGroupMember(member);
            }
            // 俱乐部设为管理员时不用发消息，add by tt, 20161104
            if (GroupDiscriminator.GROUP == GroupDiscriminator.fromCode(group.getDiscriminator()) && GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(group.getPrivateFlag())) {
				return;
			}
            GroupMember inviter = this.groupProvider.findGroupMemberByMemberInfo(groupId, EntityType.USER.getCode(), operatorId);
            sendGroupNotificationForInviteToBeGroupAdminFreely(group, inviter, member);
        } else {
            if(GroupOpRequestStatus.ACCEPTED != GroupOpRequestStatus.fromCode(request.getStatus())) {
                request.setRequestorUid(operator.getId());
                request.setOperationType(GroupOpType.INVITE_ADMIN_ROLE.getCode());
                request.setStatus(GroupOpRequestStatus.ACCEPTED.getCode());
                request.setRequestorComment(cmd.getInvitationText());
                request.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                
                this.groupProvider.createGroupOpRequest(request);
                
                Long memberRole = member.getMemberRole();
                if(memberRole == null || (memberRole != Role.ResourceAdmin && memberRole != Role.ResourceCreator)) {
                    member.setMemberRole(Role.ResourceAdmin);
                    this.groupProvider.updateGroupMember(member);
                }
                GroupMember inviter = this.groupProvider.findGroupMemberByMemberInfo(groupId, EntityType.USER.getCode(), operatorId);
                sendGroupNotificationForInviteToBeGroupAdminFreely(group, inviter, member);
            }
        }
    } 
    
    @Override
    public void resignAdminRole(ResignAdminRoleCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        String tag = "resignAdminRole";
        
        Long groupId = cmd.getGroupId();
        Group group = checkGroupParameter(groupId, userId, tag);
        GroupMember member = checkGroupMemberParameter(group, userId, userId, tag);
        
        if(member.getMemberRole() == null) {
            LOGGER.error("Invalid user role status in the group, userId=" + userId 
                + ", groupId=" + groupId + ", memberId=" + userId + ", memberRole=" + member.getMemberRole());
            // 按产品设计，把圈子概念变成两个：群聊（对应私有圈）、俱乐部（对应兴趣圈） by lqs 20160505
            int code = GroupServiceErrorCode.ERROR_GROUP_INVALID_ROLE_STATUS;
            if(GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PUBLIC) {
                code = GroupServiceErrorCode.ERROR_GROUP_CLUB_INVALID_ROLE_STATUS;
            }
            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, code, 
                    "Invalid user role status in the group");
        }
        
        if(member.getMemberRole().longValue() == Role.ResourceCreator) {
            LOGGER.error("Creator can't resign admin role in the group, userId=" + userId 
                + ", groupId=" + groupId + ", memberId=" + userId + ", memberRole=" + member.getMemberRole());
            // 按产品设计，把圈子概念变成两个：群聊（对应私有圈）、俱乐部（对应兴趣圈） by lqs 20160505
            int code = GroupServiceErrorCode.ERROR_GROUP_CREATOR_ADMIN_RESIGN_NOT_ALLOW;
            if(GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PUBLIC) {
                code = GroupServiceErrorCode.ERROR_GROUP_CLUB_CREATOR_ADMIN_RESIGN_NOT_ALLOW;
            }
            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, code, 
                    "Creator can't resign admin role in the group");
        }
        
        if(member.getMemberRole().longValue() == Role.ResourceAdmin) {
            member.setMemberRole(Role.ResourceUser);
            this.dbProvider.execute((status) -> {
                this.groupProvider.updateGroupMember(member);
                deleteUserGroupOpRequest(groupId, user.getId(), userId, "resign admin role");
                return null;
            });
            
            sendGroupNotificationForResignGroupAdmin(group, member);
        } else {
            LOGGER.error("Member is not in admin role in the group, userId=" + userId 
                + ", groupId=" + groupId + ", memberId=" + userId + ", memberRole=" + member.getMemberRole());
            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, GroupServiceErrorCode.ERROR_GROUP_NOT_IN_ADMIN_ROLE, 
                    "Invalid user role status in the group");
        }
    }
    
    @Override
    public void revokeAdminRole(RevokeAdminRoleCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        String tag = "revokeAdminRole";
        
        Long groupId = cmd.getGroupId();
        Long targetUid = cmd.getUserId();
        Group group = checkGroupParameter(groupId, operatorId, tag);
        GroupMember member = checkGroupMemberParameter(group, operatorId, targetUid, tag);
        
        checkGroupPrivilege(operator.getId(), groupId, PrivilegeConstants.GroupAdminOps);
        
        if(member.getMemberRole() == null) {
            LOGGER.error("Invalid user role status in the group, operatorId=" + operatorId 
                + ", groupId=" + groupId + ", memberId=" + targetUid + ", memberRole=" + member.getMemberRole());
            // 按产品设计，把圈子概念变成两个：群聊（对应私有圈）、俱乐部（对应兴趣圈） by lqs 20160505
            int code = GroupServiceErrorCode.ERROR_GROUP_INVALID_ROLE_STATUS;
            if(GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PUBLIC) {
                code = GroupServiceErrorCode.ERROR_GROUP_CLUB_INVALID_ROLE_STATUS;
            }
            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, code, 
                    "Invalid user role status in the group");
        }
        
        if(member.getMemberRole().longValue() == Role.ResourceCreator) {
            LOGGER.error("Creator can't be revoked admin role in the group, operatorId=" + operatorId 
                + ", groupId=" + groupId + ", memberId=" + targetUid + ", memberRole=" + member.getMemberRole());
            // 按产品设计，把圈子概念变成两个：群聊（对应私有圈）、俱乐部（对应兴趣圈） by lqs 20160505
            int code = GroupServiceErrorCode.ERROR_GROUP_CREATOR_ADMIN_REVOKED_NOT_ALLOW;
            if(GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PUBLIC) {
                code = GroupServiceErrorCode.ERROR_GROUP_CLUB_CREATOR_ADMIN_REVOKED_NOT_ALLOW;
            }
            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, code, 
                    "Creator can't be revoke admin role in the group");
        }
        
        if(member.getMemberRole().longValue() == Role.ResourceAdmin) {
            member.setMemberRole(Role.ResourceUser);
            this.dbProvider.execute((status) -> {
                this.groupProvider.updateGroupMember(member);
                deleteUserGroupOpRequest(groupId, targetUid, operator.getId(), cmd.getRevokeText());
                return null;
            });
            // 俱乐部取消管理员时不用发消息，add by tt, 20161104
            if (GroupDiscriminator.GROUP == GroupDiscriminator.fromCode(group.getDiscriminator()) && GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(group.getPrivateFlag())) {
				return;
			}
            GroupMember revoker = this.groupProvider.findGroupMemberByMemberInfo(groupId, EntityType.USER.getCode(), operatorId);
            sendGroupNotificationForRevokeGroupAdmin(group, revoker, member);
        } else {
            LOGGER.error("Member is not in admin role in the group, operatorId=" + operatorId 
                + ", groupId=" + groupId + ", memberId=" + targetUid + ", memberRole=" + member.getMemberRole());
            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, GroupServiceErrorCode.ERROR_GROUP_NOT_IN_ADMIN_ROLE, 
                    "Invalid user role status in the group");
        }
    }
    
    @Override
    public void approveAdminRole(ApproveAdminRoleCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        String tag = "approveAdminRole";
        
        Long groupId = cmd.getGroupId();
        Long targetUid = cmd.getUserId();
        Group group = checkGroupParameter(groupId, operatorId, tag);
        GroupMember member = checkGroupMemberParameter(group, operatorId, targetUid, tag);
        
        checkGroupPrivilege(operator.getId(), groupId, PrivilegeConstants.GroupAdminOps);
        
        GroupOpRequest request = this.groupProvider.findGroupOpRequestByRequestor(groupId, targetUid);
        if(request == null) {
            LOGGER.error("Group operate request record not found, operatorId=" + operatorId 
                + ", groupId=" + groupId + ", targetUid=" + targetUid);
            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, GroupServiceErrorCode.ERROR_GROUP_OP_REQUEST_NOT_FOUND, 
                    "Unable to find the corresponding request record");
        }
        
        if(GroupOpRequestStatus.REQUESTING != GroupOpRequestStatus.fromCode(request.getStatus())) {
            LOGGER.error("Group operate request record not in requesting state, operatorId=" + operatorId 
                + ", groupId=" + groupId + ", targetUid=" + targetUid);
            // 不在正确的状态，说明已经被处理过，为了客户端可以点击黑条消失，故不抛异常 by lqs 20160128
//            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, 
//                GroupServiceErrorCode.ERROR_GROUP_OP_REQUEST_NOT_IN_REQUESTING_STATE, "The corresponding request record is not in requesting state");
            return;
        }
        
        if(member.getMemberRole() == null) {
            LOGGER.error("Invalid user role status in the group, operatorId=" + operatorId 
                + ", groupId=" + groupId + ", memberId=" + targetUid + ", memberRole=" + member.getMemberRole());
            // 按产品设计，把圈子概念变成两个：群聊（对应私有圈）、俱乐部（对应兴趣圈） by lqs 20160505
            int code = GroupServiceErrorCode.ERROR_GROUP_INVALID_ROLE_STATUS;
            if(GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PUBLIC) {
                code = GroupServiceErrorCode.ERROR_GROUP_CLUB_INVALID_ROLE_STATUS;
            }
            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, code, 
                    "Invalid user role status in the group");
        }

        request.setOperatorUid(operator.getId());
        request.setStatus(GroupOpRequestStatus.ACCEPTED.getCode());
        request.setProcessTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        this.dbProvider.execute((status) -> {
            if(member.getMemberRole().longValue() != Role.ResourceAdmin 
                && member.getMemberRole().longValue() != Role.ResourceCreator) {
                member.setMemberRole(Role.ResourceAdmin);
                this.groupProvider.updateGroupMember(member);
            }
            
            this.groupProvider.updateGroupOpRequest(request);
            return null;
        });

        GroupMember approver = this.groupProvider.findGroupMemberByMemberInfo(groupId, EntityType.USER.getCode(), operatorId);
        sendGroupNotificationForApproveGroupAdminRequest(group, approver, member);
    }
 
    @Override
    public void rejectAdminRole(RejectAdminRoleCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        String tag = "approveAdminRole";
        
        Long groupId = cmd.getGroupId();
        Long targetUid = cmd.getUserId();
        Group group = checkGroupParameter(groupId, operatorId, tag);
        GroupMember member = checkGroupMemberParameter(group, operatorId, targetUid, tag);
        
        checkGroupPrivilege(operator.getId(), groupId, PrivilegeConstants.GroupAdminOps);
        
        GroupOpRequest request = this.groupProvider.findGroupOpRequestByRequestor(groupId, targetUid);
        if(request == null) {
            LOGGER.error("Group operate request record not found, operatorId=" + operatorId 
                + ", groupId=" + groupId + ", targetUid=" + targetUid);
            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, GroupServiceErrorCode.ERROR_GROUP_OP_REQUEST_NOT_FOUND, 
                    "Unable to find the corresponding request record");
        }
        
        if(GroupOpRequestStatus.REQUESTING != GroupOpRequestStatus.fromCode(request.getStatus())) {
            LOGGER.error("Group operate request record not in requesting state, operatorId=" + operatorId 
                + ", groupId=" + groupId + ", targetUid=" + targetUid);
            // 不在正确的状态，说明已经被处理过，为了客户端可以点击黑条消失，故不抛异常 by lqs 20160128
//            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, 
//                GroupServiceErrorCode.ERROR_GROUP_OP_REQUEST_NOT_IN_REQUESTING_STATE, "The corresponding request record is not in requesting state");
            return;
        }
        
        if(member.getMemberRole() == null) {
            LOGGER.error("Invalid user role status in the group, operatorId=" + operatorId 
                + ", groupId=" + groupId + ", memberId=" + targetUid + ", memberRole=" + member.getMemberRole());
            // 按产品设计，把圈子概念变成两个：群聊（对应私有圈）、俱乐部（对应兴趣圈） by lqs 20160505
            int code = GroupServiceErrorCode.ERROR_GROUP_INVALID_ROLE_STATUS;
            if(GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PUBLIC) {
                code = GroupServiceErrorCode.ERROR_GROUP_CLUB_INVALID_ROLE_STATUS;
            }
            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, code, 
                    "Invalid user role status in the group");
        }
        
        request.setOperatorUid(operator.getId());
        request.setProcessMessage(cmd.getRejectText());
        request.setStatus(GroupOpRequestStatus.REJECTED.getCode());
        request.setProcessTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        this.groupProvider.updateGroupOpRequest(request);
        
        //deleteUserGroupOpRequest(groupId, memberId, operatorUid, reason);
        GroupMember rejecter = this.groupProvider.findGroupMemberByMemberInfo(groupId, EntityType.USER.getCode(), operatorId);
        sendGroupNotificationForRejectGroupAdminRequest(group, rejecter, member);
    }
    
    @Override
    public Byte getGroupAdimRoleStatus(GetAdminRoleStatusCommand cmd) {
    	User operator = UserContext.current().getUser();
    	Long operatorUid = operator.getId(); 

        Long groupId = cmd.getGroupId();
    	checkGroupParameter(groupId, operatorUid, "getGroupAdimRoleStatus");
    	
    	GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(groupId, 
    			EntityType.USER.getCode(), operatorUid);
            
        Byte result = new Byte((byte)0);
        if(member != null) {
        	GroupOpRequest opRequest = groupProvider.findGroupOpRequestByRequestor(groupId, operatorUid);
        	if(opRequest != null) {
        	    result = opRequest.getStatus();
        	}
        }
        
        return result;
    }
    
    @Override
    public ListAdminOpRequestCommandResponse listGroupOpRequests(ListAdminOpRequestCommand cmd) {
        long startTime = System.currentTimeMillis();
        User operator = UserContext.current().getUser();
        Long operatorUid = operator.getId(); 
        
        Long groupId = cmd.getGroupId();
        Group group = checkGroupParameter(groupId, operatorUid, "");
        
        checkGroupPrivilege(operator.getId(), cmd.getGroupId().longValue(), PrivilegeConstants.GroupAdminOps);

        ListAdminOpRequestCommandResponse cmdResponse = new ListAdminOpRequestCommandResponse();
        
        ListingLocator locator = new ListingLocator(cmd.getGroupId().longValue());
        locator.setAnchor(cmd.getPageAnchor());
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        List<GroupOpRequest> requests = this.groupProvider.queryGroupOpRequests(locator, pageSize + 1, 
            (loc, query) -> {
                query.addConditions(Tables.EH_GROUP_OP_REQUESTS.GROUP_ID.eq(cmd.getGroupId().longValue()));
                
                if(cmd.getAdminStatus() != null)
                    query.addConditions(Tables.EH_GROUP_OP_REQUESTS.STATUS.eq(cmd.getAdminStatus().byteValue()));
                
                return query;
            });

        if(requests.size() > pageSize) {
            requests.remove(requests.size() - 1);
            cmdResponse.setNextPageAnchor(requests.get(requests.size() - 1).getId());
        }
        List<GroupOpRequestDTO> requestDTOList = requests.stream().map((r)-> { 
            GroupOpRequestDTO dto = ConvertHelper.convert(r, GroupOpRequestDTO.class);
            populateGroupOpRequestDTO(operator.getId(), group, dto);
            return dto;
            }).collect(Collectors.toList());
        cmdResponse.setRequests(requestDTOList); 
        
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("List group operate requests, operatorUid=" + operatorUid + ", size=" + requestDTOList.size() 
                + ", groupId=" + groupId + ", pageAnchor=" + cmd.getPageAnchor() + ", pageSize=" + pageSize 
                + ", nextPageAnchor=" + cmdResponse.getNextPageAnchor() + ", elapse=" + (endTime - startTime));
        }
        
        return cmdResponse;
    }
    
    @Override
    public ListGroupCommandResponse listGroupByTag(ListGroupByTagCommand cmd) {
        User user = UserContext.current().getUser();
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<Group> groups = this.groupProvider.queryGroups(locator, pageSize + 1, (loc, query)-> {
            query.addConditions(Tables.EH_GROUPS.TAG.eq(cmd.getTag()));
            query.addConditions(Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode()));

            buildGroupSearchVisibilityConditioons(query, cmd.getSearchVisibilityScope(),
                    cmd.getSearchVisibilityScopeId());
            return query;
        });
        
        ListGroupCommandResponse cmdResponse = new ListGroupCommandResponse();
        if(groups.size() > pageSize) {
            groups.remove(groups.size() - 1);
            cmdResponse.setNextPageAnchor(groups.get(groups.size() - 1).getId());
        }
        cmdResponse.setGroups(groups.stream().map((r)-> { 
            return toGroupDTO(user.getId(), r); 
        }).collect(Collectors.toList())); 
        
        return cmdResponse;
    }
    
    // 
    // Currently only support VisibilityScope.ALL, VisibilityScope.COMMUNITY_ONLY, VisibilityScope.NEARBY_COMMUNITIES, 
    // VisibilityScope.CITY_ONLY
    //
    private void buildGroupSearchVisibilityConditioons(SelectQuery<? extends Record> query, 
        Byte visibilityScopeType, Long visibilityScopeId) {
        
        VisibilityScope scope = VisibilityScope.fromCode(visibilityScopeType);
        if(scope == null)
            return;
        
        switch(scope) {
        case ALL:
            return;
            
        case COMMUNITY:
            query.addConditions(Tables.EH_GROUPS.VISIBILITY_SCOPE.eq(VisibilityScope.COMMUNITY.getCode()).and(
                    Tables.EH_GROUPS.VISIBILITY_SCOPE_ID.eq(visibilityScopeId.longValue())));
            return;

        case NEARBY_COMMUNITIES :
            // TODO
            return;
        
        case CITY:
            if(visibilityScopeId == null)
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                        "Invalid visibility scope id");
            
            query.addConditions(Tables.EH_GROUPS.VISIBILITY_SCOPE.eq(VisibilityScope.CITY.getCode()).and(
                Tables.EH_GROUPS.VISIBILITY_SCOPE_ID.eq(visibilityScopeId.longValue())));
            return;
            
        default :
            break;
        }
    }
    
    public ListNearbyGroupCommandResponse listNearbyGroups(ListNearbyGroupCommand cmd) {
        long startTime = System.currentTimeMillis();
        User operator = UserContext.current().getUser();
        Long operatorUid = operator.getId();
        
        Integer namespaceId = null == cmd.getNamespaceId() ? 0 : cmd.getNamespaceId();
        
        Integer pageOffset = cmd.getPageOffset();
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        
        int offset = 0;
        if(pageOffset == null || (pageOffset != null && pageOffset.intValue() <= 0)) {
            pageOffset = 1;
        }
        offset = (pageOffset.intValue() - 1) * pageSize; 
        
        Long categoryId = cmd.getCategoryId();
        List<Group> groups = this.groupProvider.queryPulbicGroups(1000, (loc, query) -> {
        	query.addConditions(Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode()));
        	query.addConditions(Tables.EH_GROUPS.NAMESPACE_ID.eq(namespaceId));
            Category category = null;
            if(categoryId != null) {
                category = this.categoryProvider.findCategoryById(categoryId);
                query.addConditions(Tables.EH_GROUPS.CATEGORY_PATH.like(category.getPath() + "%"));
            }
            return null;
        });
        
        ListNearbyGroupCommandResponse cmdResponse = new ListNearbyGroupCommandResponse();
        List<GroupDTO> groupDTOs = new ArrayList<GroupDTO>();
        Group group = null;
        for(int i = offset; i < groups.size(); i++) {
            group = groups.get(i);
            // 过滤掉意见反馈圈 by lqs 20160416
            if(group.getOwningForumId() != null && group.getOwningForumId().longValue() == ForumConstants.FEEDBACK_FORUM) {
                continue;
            }
            
            groupDTOs.add(toGroupDTO(operator.getId(), group));
            if(groupDTOs.size() >= pageSize) {
                break;
            }
        }
        if(groups.size() > (offset + pageSize)) {
            cmdResponse.setNextPageOffset(pageOffset.intValue() + 1);
        } 
        cmdResponse.setGroups(groupDTOs);
        
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("List group waiting approvals, operatorUid=" + operatorUid + ", size=" + groupDTOs.size() 
                + ", categoryId=" + cmd.getCategoryId() + ", pageOffset=" + pageOffset + ", pageSize=" + pageSize 
                + ", nextPageOffset=" + cmdResponse.getNextPageOffset() + ", elapse=" + (endTime - startTime));
        }
        
        return cmdResponse;
    }
    
    public ListGroupCommandResponse searchGroup(SearchGroupCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        
        GroupQueryResult result =  groupSearcher.query(cmd);
        ListGroupCommandResponse resp = new ListGroupCommandResponse();
        resp.setNextPageAnchor(result.getPageAnchor());
        
        List<GroupDTO> groups = new ArrayList<GroupDTO>();
        for(Long id : result.getIds()) {
            Group group = groupProvider.findGroupById(id);
            if(group == null) {
                continue;
            }
            
            GroupDTO groupDTO = toGroupDTO(operatorId, group);
            // GroupDTO groupDTO = ConvertHelper.convert(group, GroupDTO.class);
            if(groupDTO != null) {
                groups.add(groupDTO);    
            }
            
        }
        
        resp.setGroups(groups);
        
        return resp;
    }
    
    private Group checkGroupParameter(Long groupId, Long operatorUid, String tag) {
        if(groupId == null) {
            LOGGER.error("Group id is null, operatorUid=" + operatorUid + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "group id can not be null");
        }
        
        Group group = this.groupProvider.findGroupById(groupId);
        if(group == null || group.getStatus().byteValue() == GroupAdminStatus.INACTIVE.getCode()) {
        	int code = GroupServiceErrorCode.ERROR_GROUP_NOT_FOUND;
        	if (group != null && GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PUBLIC) {
				code = GroupServiceErrorCode.ERROR_GROUP_CLUB_NOT_FOUND;
			}
            LOGGER.error("Group not found, operatorUid=" + operatorUid + ", groupId=" + groupId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, code, 
                    "Unable to find the group");
        }
        
        return group;
    }
    
    private GroupMember checkGroupMemberParameter(Group group, Long operatorUid, Long targetUid, String tag) {
        Long groupId = group.getId();
        if(targetUid == null) {
            LOGGER.error("Target user id is null, operatorUid=" + operatorUid + ", groupId=" + groupId 
                + ", targetUid=" + targetUid + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Target user id can not be null");
        }
        
        GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(groupId, EntityType.USER.getCode(), targetUid);
        if(member == null) {
            LOGGER.error("Group member not found, operatorUid=" + operatorUid + ", groupId=" + groupId 
                + ", memberId=" + targetUid + ", tag=" + tag);
            // 按产品设计，把圈子概念变成两个：群聊（对应私有圈）、俱乐部（对应兴趣圈） by lqs 20160505
            int code = GroupServiceErrorCode.ERROR_GROUP_MEMBER_NOT_FOUND;
            if(GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PUBLIC) {
                code = GroupServiceErrorCode.ERROR_GROUP_CLUB_MEMBER_NOT_FOUND;
            }
            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, code,
                    "Unable to find the group member record");
        }
        
        return member;
    }
    
    private void deleteUserGroupOpRequest(long groupId, long memberId, long operatorUid, String reason) {
        GroupOpRequest request = this.groupProvider.findGroupOpRequestByRequestor(groupId, memberId);
        if(request != null) {
            AuditLog auditLog = toEhAuditLog(AppConstants.APPID_GROUP, operatorUid, reason, groupId, request);
            this.groupProvider.deleteGroupOpRequest(request);
            this.auditLogProvider.createAuditLog(auditLog);
        } else {
            if(LOGGER.isWarnEnabled()) {
                LOGGER.warn("The group operator request is not found, groupId=" + groupId  
                    + ", memberId=" + memberId + ", operatorUid=" + operatorUid + ", memberId=" + memberId);
            }
        }
    }
    
    private AuditLog toEhAuditLog(long appId, long operatorUid, String reason, long groupId, GroupOpRequest request) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAppId(appId);
        auditLog.setOperatorUid(operatorUid);
        auditLog.setRequestorUid(request.getRequestorUid());
        auditLog.setRequestorComment(request.getRequestorComment());
        auditLog.setReason(reason);
        auditLog.setResourceType(EntityType.GROUP.getCode());
        auditLog.setResourceId(groupId);
        auditLog.setCreateTime(request.getCreateTime());
        auditLog.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        return auditLog;
    }
    
    private Condition buildUserRelatedGroupActionConditions(long userId, Byte memberStatus, boolean isAdmin) {
        Condition condition = null;
        // When memberStatus isn't specified, we may need to query two kinds of actions:
        // 1. the waiting-acceptance actions for the specific user;
        // 2. the waiting-approval actions if the specific user is admin of group;
        if(memberStatus == null) {
            condition = Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.WAITING_FOR_ACCEPTANCE.getCode());
            condition = condition.and(Tables.EH_GROUP_MEMBERS.MEMBER_ID.eq(userId));
            if(isAdmin) {
                condition = condition.or(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode()));
            }
        } else {
            if(memberStatus.byteValue() == GroupMemberStatus.WAITING_FOR_ACCEPTANCE.getCode()) {
                condition = Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.WAITING_FOR_ACCEPTANCE.getCode());
                condition = condition.and(Tables.EH_GROUP_MEMBERS.MEMBER_ID.eq(userId));
            } else {
                if(isAdmin) {
                    condition = Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
                }
            }
        }
        
        return condition;
    }
    
    private void checkGroupPrivilege(long uid, long groupId, long privilege) {
        ResourceUserRoleResolver resolver = PlatformContext.getComponent(EntityType.GROUP.getCode());
        List<Long> roles = resolver.determineRoleInResource(uid, groupId, EntityType.GROUP.getCode(), groupId);
        if(!this.aclProvider.checkAccess(EntityType.GROUP.getCode(), groupId, EntityType.USER.getCode(), uid, privilege, 
                roles))
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                    "Insufficient privilege");
    }

    //给行业协会开一个小门，超级管理员也可以审核
    private void checkGroupPrivilegeForGuild(long uid, long groupId, long privilege, Long organizationId) {
        ResourceUserRoleResolver resolver = PlatformContext.getComponent(EntityType.GROUP.getCode());
        List<Long> roles = resolver.determineRoleInResource(uid, groupId, EntityType.GROUP.getCode(), groupId);
        if(this.aclProvider.checkAccess(EntityType.GROUP.getCode(), groupId, EntityType.USER.getCode(), uid, privilege,
                roles)){
           return;
        }

        UserPrivilegeMgr sysResolver = PlatformContext.getComponent("SystemUser");
        boolean result = sysResolver.checkSuperAdmin(uid, organizationId);

        if(result){
            return;
        }

        //最后检查root权限，没有权限报错
        SystemUserPrivilegeMgr systemResolver = PlatformContext.getComponent("SystemUser");
        systemResolver.checkUserPrivilege(uid, 0);

//        if (!result) {
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
//                    "Insufficient privilege");
//        }

    }
    
    private void populateGroupDTOs(long userId, List<GroupDTO> groups) {
        if(groups != null) {
            for(GroupDTO group : groups) {
                populateGroupDTO(userId, group);
            }
        }
    }
    
    private void populateGroupDTO(Long userId, GroupDTO group) {
        if(group == null) {
            return;
        }
        
        String avatar = group.getAvatar();
        if(avatar != null && avatar.length() > 0) {
            try{
                String url = contentServerService.parserUri(avatar, EntityType.GROUP.getCode(), group.getId());
                group.setAvatarUrl(url);
            }catch(Exception e){
                LOGGER.error("Failed to parse avatar uri of group member, userId=" + userId 
                    + ", group=" + group, e);
            }
        }
    }
    
    private void populateGroupMemberDTOs(long userId, Group group, List<GroupMemberDTO> groupMembers) {
        if(groupMembers != null) {
            for(GroupMemberDTO groupMember : groupMembers) {
                populateGroupMemberDTO(userId, group, groupMember);
            }
        }
    }
    
    private void populateGroupMemberDTO(long userId, Group group, GroupMemberDTO groupMember) {
        if(groupMember == null) {
            return;
        }
        
        if(group == null) {
            group = this.groupProvider.findGroupById(groupMember.getGroupId());
        }
        
        if(group != null) {
            groupMember.setGroupName(group.getName());
        }
        
        // 按产品设计，每个人在整个系统中只有一个头像，故即使是圈成员也要从用户中拿头像 by xiongying 20160505
        User user = userProvider.findUserById(groupMember.getMemberId());
        if(user != null) {
            String memberAvatar = user.getAvatar();
            if(memberAvatar != null && memberAvatar.length() > 0) {
                try {
                    String url = contentServerService.parserUri(memberAvatar, EntityType.USER.getCode(), groupMember.getMemberId());
                    groupMember.setMemberAvatarUrl(url);
                } catch(Exception e) {
                    LOGGER.error("Failed to parse avatar uri of group member, userId=" + userId
                            + ", groupMember=" + groupMember, e);
                }
            }

            groupMember.setGender(user.getGender());
            groupMember.setUserNickName(user.getNickName());
        } else {
            LOGGER.error("The user related to the member not existed, userId=" + userId
                    + ", memberId=" + groupMember.getMemberId() + ", groupMember=" + groupMember);
        }

        String memberNickName = groupMember.getMemberNickName();
        if(memberNickName == null && user != null) {
            groupMember.setMemberNickName(user.getNickName());
        }

        GroupMemberPhonePrivacy phonePrivateFlag = GroupMemberPhonePrivacy.fromCode(groupMember.getPhonePrivateFlag());
        GroupPrivacy groupPrivacy = GroupPrivacy.fromCode(group.getPrivateFlag());
        if(phonePrivateFlag == GroupMemberPhonePrivacy.PUBLIC
                // 在后台俱乐部成员要显示手机号 #11814 update by xq.tian  2017/06/28
                || groupPrivacy == GroupPrivacy.PUBLIC) {
            UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(
                    groupMember.getMemberId(), IdentifierType.MOBILE.getCode());
            if(userIdentifier != null) {
                groupMember.setCellPhone(userIdentifier.getIdentifierToken());
            }
        }
        
        Long inviterUid = groupMember.getInviterUid();
        if(inviterUid != null) {
            GroupMember inviterGroupMember = this.groupProvider.findGroupMemberByMemberInfo(groupMember.getGroupId(), 
                EntityType.USER.getCode(), inviterUid);
            if(inviterGroupMember != null) {
                groupMember.setInviterNickName(inviterGroupMember.getMemberNickName());
                String inviterMemberAvatar = inviterGroupMember.getMemberAvatar();
                if(inviterMemberAvatar != null && inviterMemberAvatar.length() > 0) {
                    groupMember.setInviterAvatar(inviterMemberAvatar);
                    try {
                        String url = contentServerService.parserUri(inviterMemberAvatar, EntityType.USER.getCode(), groupMember.getMemberId());
                        groupMember.setInviterAvatarUrl(url);
                    } catch(Exception e) {
                        LOGGER.error("Failed to parse avatar uri of group member, userId=" + userId 
                            + ", groupMember=" + groupMember, e);
                    }
                }
            }
        }

        //行业协会加入申请时的信息，包括企业等  add by yanjun 20171107
        if(ClubType.fromCode(group.getClubType()) == ClubType.GUILD){
            GuildApply guildApply = groupProvider.findGuildApplyByGroupMemberId(groupMember.getId());
            GuildApplyDTO guildApplyDTO = ConvertHelper.convert(guildApply, GuildApplyDTO.class);
            populateGuildApplyDTO(guildApplyDTO);
            groupMember.setGuildApplyDTO(guildApplyDTO);
        }
    }
    
    private void populateGroupOpRequestDTO(long userId, Group group, GroupOpRequestDTO request) {
        if(request == null) {
            return;
        }
        
        if(group != null) {
            request.setGroupName(group.getName());
        }
        
        if(request.getRequestorUid() == null) {
            LOGGER.error("The requestor id should not be null, userId=" + userId + ", request=" + request);
            return;
        }
        
        GroupMember groupMember = this.groupProvider.findGroupMemberByMemberInfo(group.getId(), 
            EntityType.USER.getCode(), request.getRequestorUid());
        if(groupMember != null) {
            request.setRequestorName(groupMember.getMemberNickName());
            
            String memberAvatar = groupMember.getMemberAvatar();
            if(memberAvatar != null && memberAvatar.length() > 0) {
                try{
                    String url = contentServerService.parserUri(memberAvatar, EntityType.USER.getCode(), groupMember.getMemberId());
                    request.setRequestorAvatarUrl(url);
                }catch(Exception e){
                    LOGGER.error("Failed to parse avatar uri of group member, userId=" + userId 
                        + ", request=" + request, e);
                }
            }
        }
    }
    
    private Forum createGroupForum(Group group) {
        Forum forum = new Forum();
        forum.setOwnerType(EntityType.GROUP.getCode());
        forum.setOwnerId(group.getId());
        forum.setAppId(AppConstants.APPID_FORUM);
        forum.setNamespaceId(group.getNamespaceId());
        forum.setName(EntityType.GROUP.getCode() + "-" + group.getId());
        forum.setModifySeq(0L);
        Timestamp currTime = new Timestamp(DateHelper.currentGMTTime().getTime());
        forum.setUpdateTime(currTime);
        forum.setCreateTime(currTime);
        
        this.forumProvider.createForum(forum);
        return forum;
    }
    
    private GroupDTO toGroupDTO(Long operatorId, Group group) {
        // GroupDTO groupDto = new GroupDTO();
        GroupDTO groupDto = ConvertHelper.convert(group, GroupDTO.class);
//        if(LOGGER.isDebugEnabled()) {
//            LOGGER.debug("Convert to group dto, operatorId={}, discriminator={}, groupDto={}", operatorId, group.getDiscriminator(), groupDto);
//        }
        
        //groupDto.setId(group.getId());
        //groupDto.setOwningForumId(group.getOwningForumId());
        //groupDto.setAvatar(group.getAvatar());
        //groupDto.setCategoryId(group.getCategoryId());
        if(group.getCategoryId() != null) {
            Category category = this.categoryProvider.findCategoryById(group.getCategoryId());
            if(category != null)
                groupDto.setCategoryName(category.getName());
        }
        
        //添加操作者,add by tt, 20161103
        if (group.getOperatorUid() != null) {
			groupDto.setOperatorName(getUserName(group.getOperatorUid()));
		}
        
        groupDto.setShareUrl(getShareUrl(group));

        groupDto.setScanJoinUrl(getScanJoinUrl(group));
        groupDto.setScanDownloadUrl(getScanDownloadUrl(group));
        //groupDto.setBehaviorTime(DateHelper.getDateDisplayString(TimeZone.getTimeZone("GMT"),
        //    group.getBehaviorTime().getTime()));
        //groupDto.setCreatorUid(group.getCreatorUid());
        //groupDto.setJoinPolicy(group.getJoinPolicy());
        //groupDto.setDescription(group.getDescription());
        //groupDto.setMemberCount(group.getMemberCount());
        //groupDto.setName(group.getName());
        //groupDto.setPrivateFlag(group.getPrivateFlag());
        //groupDto.setTag(group.getTag());

        memberInfoToGroupDTO(operatorId, groupDto, group);

        populateGroupDTO(operatorId, groupDto);

        groupDto.setDescriptionUrl(getDescriptionUrl(group.getId()));
        
        return groupDto;
    }


    private String getDescriptionUrl(Long id){
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        String homeUrl = configProvider.getValue(namespaceId, ConfigConstants.HOME_URL, "");
        String descriptionUrl = configProvider.getValue(namespaceId, ConfigConstants.CLUB_DESCRIPTION_URL, "");
        return homeUrl + descriptionUrl + "?groupId=" + id;
    }
    
    private String getShareUrl(Group group) {
    	String homeUrl = configProvider.getValue(group.getNamespaceId(), ConfigConstants.HOME_URL, "");
		String shareUrl = configProvider.getValue(group.getNamespaceId(), ConfigConstants.CLUB_SHARE_URL, "");
		if (homeUrl.length() == 0 || shareUrl.length() == 0) {
			LOGGER.error("Invalid home url or share url, homeUrl=" + homeUrl + ", shareUrl=" + shareUrl);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
	                ErrorCodes.ERROR_GENERAL_EXCEPTION, "Invalid home url or share url");
		} else {
			return homeUrl + shareUrl + "?namespaceId=" + group.getNamespaceId()+"&groupId="+group.getId()+"&realm=";
		}
	}

	private String getScanJoinUrl(Group group){
        String homeUrl = configProvider.getValue(group.getNamespaceId(), ConfigConstants.HOME_URL, "");
        String scanJoinUrl = configProvider.getValue(group.getNamespaceId(), "group.scanJoin.url", "/mobile/static/message/src/addGroup.html");
        if (homeUrl.length() == 0 || scanJoinUrl.length() == 0) {
            LOGGER.error("Invalid home url or scanJoinUrl, homeUrl=" + homeUrl + ", scanJoinUrl=" + scanJoinUrl);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_GENERAL_EXCEPTION, "Invalid home url or scanJoinUrl");
        }
        return homeUrl + scanJoinUrl;

    }

    private String getScanDownloadUrl(Group group){
        String homeUrl = configProvider.getValue(group.getNamespaceId(), ConfigConstants.HOME_URL, "");
        String scanDownloadUrl = configProvider.getValue(group.getNamespaceId(), "group.scanDownload.url", "");
        if (homeUrl.length() == 0 || scanDownloadUrl.length() == 0) {
            LOGGER.error("Invalid home url or scanDownloadUrl, homeUrl=" + homeUrl + ", scanDownloadUrl=" + scanDownloadUrl);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_GENERAL_EXCEPTION, "Invalid home url or scanDownloadUrl");
        }
        return homeUrl + scanDownloadUrl;
    }

	private void memberInfoToGroupDTO(Long uid, GroupDTO groupDto, Group group) {
        //
        // compute member role ourselves instead of using GroupUserRoleResolver,
        // it is more efficient to do in this way due to the reason that
        // we need to tell whether or not the user is a member of the group
        //
        GroupMember member = groupProvider.findGroupMemberByMemberInfo(group.getId(), EntityType.USER.getCode(), uid);
        
        // List<Long> userInRoles = new ArrayList<>();
        // userInRoles.add(Role.AuthenticatedUser);
        
       //Gives user rights to the club property,modify by sfyan 20160505
        List<Long> memberForumPrivileges = new ArrayList<>();
        if(member != null) {
            groupDto.setMemberOf((byte)1);
            groupDto.setMemberNickName(member.getMemberNickName());
            groupDto.setPhonePrivateFlag(member.getPhonePrivateFlag());
            groupDto.setMuteNotificationFlag(member.getMuteNotificationFlag());
            groupDto.setMemberStatus(member.getMemberStatus());
            groupDto.setMemberRole(member.getMemberRole());
            groupDto.setJoinTime(member.getCreateTime());
            
            if(GroupPostFlag.fromCode(group.getPostFlag()) == GroupPostFlag.ALL||
                    member.getMemberRole() == Role.ResourceAdmin ||
                    member.getMemberRole() == Role.ResourceOperator ||
                    member.getMemberRole() == Role.ResourceCreator ) {
                if(GroupMemberStatus.fromCode(member.getMemberStatus()) == GroupMemberStatus.ACTIVE){
                	memberForumPrivileges.add(PrivilegeConstants.ForumNewTopic);
                	memberForumPrivileges.add(PrivilegeConstants.ForumDeleteTopic);
                	memberForumPrivileges.add(PrivilegeConstants.ForumDeleteTopic);
                	memberForumPrivileges.add(PrivilegeConstants.ForumNewReply);
                	// userInRoles.add(Role.ResourceUser);
                }
            }
            groupDto.setMemberForumPrivileges(memberForumPrivileges);

            // 此代码有问题，会把很多不相干的数据查出来，导致数据非常多，
            // 我也不是很清楚业务逻辑，没法改，而且客户端那边说也没用上，所以就注释掉了
            // 如果还要用的话，这里需要改一下          add by xq.tian   2017/03/13
            // AclAccessor groupPrivileges = this.aclProvider.getAccessor(
            //         EntityType.GROUP.getCode(), group.getId(), userInRoles);
            // groupDto.setMemberGroupPrivileges(groupPrivileges.getGrantPrivileges());
            
//            AclAccessor forumPrivileges = this.aclProvider.getAccessor(
//                    EntityType.FORUM.getCode(), group.getOwningForumId(), userInRoles);
        } else {
            groupDto.setMemberOf((byte)0);
            groupDto.setMemberNickName("");

            Organization organization = organizationProvider.findOrganizationByGroupId(group.getId());
            if(organization != null){
                OrganizationMember orgmember = organizationProvider.findOrganizationMemberByUIdAndOrgId(uid, organization.getId());
                if(orgmember != null && orgmember.getContactName() != null){
                    groupDto.setMemberNickName(orgmember.getContactName());
                }
            }
        }
    }
    
    private void createUserGroup(GroupMember member, GroupVisibilityScope scope) {
        UserGroup userGroup = new UserGroup();
        userGroup.setOwnerUid(member.getMemberId());
//        userGroup.setGroupDiscriminator(GroupDiscriminator.GROUP.getCode());
        userGroup.setGroupId(member.getGroupId());
        Group group = groupProvider.findGroupById(member.getGroupId());
        userGroup.setGroupDiscriminator(group.getDiscriminator());
        if(scope != null) {
	        userGroup.setRegionScope(scope.getScopeCode());
	        userGroup.setRegionScopeId(scope.getScopeId());
        }
        userGroup.setMemberRole(member.getMemberRole());
        userGroup.setMemberStatus(member.getMemberStatus());
        
        this.userProvider.createUserGroup(userGroup);
    }
    
    private void updateUserGroupMemberStatus(GroupMember member) {
    	long ownerId = member.getMemberId(); 
    	long groupId = member.getGroupId();
    	UserGroup userGroup = userProvider.findUserGroupByOwnerAndGroup(ownerId, groupId);
    	userGroup.setMemberStatus(member.getMemberStatus());
    	userProvider.updateUserGroup(userGroup);
    }
    
    private GroupVisibilityScope getSingleGroupScope(long groupId) {
        List<GroupVisibilityScope> scopeList = groupProvider.listGroupVisibilityScopes(groupId);
        GroupVisibilityScope scope = (scopeList == null || scopeList.size() == 0) ? null : scopeList.get(0);
        
        return scope;
    }
    
    private CommandResult inviteToJoinGroup(User operator, Group group, Long inviteeId, String invitationText, boolean isFreeJoin) {
        CommandResult result = new CommandResult();
        result.setIdentifier(String.valueOf(inviteeId));
        result.setErrorScope(ErrorCodes.SCOPE_GENERAL);
        result.setErrorCode(ErrorCodes.SUCCESS);
        result.setErrorDescription("OK");
        
        try {
            Long groupId = group.getId();

            GroupMember member = this.groupProvider.findGroupMemberByMemberInfo(groupId, EntityType.USER.getCode(), inviteeId);
            if(member != null) {
                if(GroupMemberStatus.ACTIVE.getCode() == member.getMemberStatus().byteValue()) {
                    if(LOGGER.isInfoEnabled()) {
                        LOGGER.info("Target user is already in the group, operatorId=" + operator.getId() 
                            + ", groupId=" + groupId + ", inviteeId=" + inviteeId);
                    }
                    result.setErrorScope(GroupServiceErrorCode.SCOPE);
                    if(GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PRIVATE) {
                        result.setErrorCode(GroupServiceErrorCode.ERROR_USER_ALREADY_IN_GROUP);
                    } else {
                        result.setErrorCode(GroupServiceErrorCode.ERROR_USER_ALREADY_IN_GROUP_CLUB);
                    }
                    result.setErrorDescription("Target user is already in the group");
                } else {
//                    if(LOGGER.isInfoEnabled()) {
//                        LOGGER.info("Target user is in the joining group process, operatorId=" + operator.getId() 
//                            + ", groupId=" + groupId + ", inviteeId=" + inviteeId);
//                    }
//                    result.setErrorScope(GroupServiceErrorCode.SCOPE);
//                    result.setErrorCode(GroupServiceErrorCode.ERROR_USER_IN_JOINING_GROUP_PROCESS);
//                    result.setErrorDescription("Target user is in the joining group process");
                	
                	//A创建了群，邀请了B，A退出了群并把权限转移给了B，B再邀请A则邀请不了了， update by tt,20160809
                	member.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
                	member.setMemberRole(RoleConstants.ResourceUser);
                	groupProvider.updateGroupMember(member);
                	result.setErrorCode(ErrorCodes.SUCCESS);
                	result.setErrorDescription("OK");
                }
            } else {
            	//群管理员群聊拉人显示 Insufficient privilege 所以注释掉 modified by xiongying 20160613
                //checkGroupPrivilege(operator.getId(), groupId, PrivilegeConstants.GroupInviteJoin);
                
                // 通过userId邀请的用户直接加入（一般来自于客户端可见的用户列表）
                member = new GroupMember();
                member.setCreatorUid(operator.getId());
                member.setGroupId(groupId);
                member.setMemberType(EntityType.USER.getCode());
                member.setMemberId(inviteeId);
                member.setMemberRole(Role.ResourceUser);
                member.setApproveTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                member.setOperatorUid(member.getMemberId());
                member.setRequestorComment(invitationText);
                member.setInviterUid(operator.getId());
                member.setInviteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                
                GroupVisibilityScope scope = getSingleGroupScope(groupId);
                User invitee = this.userProvider.findUserById(inviteeId);
                if(invitee != null) {
                    member.setMemberAvatar(invitee.getAvatar());
                    member.setMemberNickName(invitee.getNickName());
                }
                if(isFreeJoin) {
                    member.setMemberStatus(GroupMemberStatus.ACTIVE.getCode());
                    createActiveGroupMember(member, scope);
                } else {
                    member.setMemberStatus(GroupMemberStatus.WAITING_FOR_ACCEPTANCE.getCode());
                    createPendingGroupMember(member, scope);
                }
                
                // send notifications
                GroupMember inviter = groupProvider.findGroupMemberByMemberInfo(groupId, EntityType.USER.getCode(), operator.getId());
                if(isFreeJoin) {
                    sendGroupNotificationForInviteToJoinGroupFreely(group, inviter, member);
                } else {
                    sendGroupNotificationForInviteToJoinGroupWaitingAcceptance(group, inviter, member);
                }
            }
        } catch(Exception e) {
            LOGGER.error("Failed to invited the group member, operatorId=" + operator.getId() 
                + ", groupId=" + group.getId() + ", inviteeId=" + inviteeId + ", isFreeJoin=" + isFreeJoin, e);
            result.setErrorCode(ErrorCodes.ERROR_GENERAL_EXCEPTION);
            result.setErrorDescription(e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 创建状态为Active的group成员时，需要改变group里的memberCount属性（成员数加1）；
     * 为了保证成员数的正确性，需要添加锁；（如果添加的是待审核的成员则不需要加锁）
     * @param member 成员
     * @param scope 成员范围
     */
    private void createActiveGroupMember(GroupMember member, GroupVisibilityScope scope) {
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_GROUP.getCode()).enter(()-> {
            this.dbProvider.execute((status) -> {
                this.groupProvider.createGroupMember(member);
                createUserGroup(member, scope);
                
                Group group = this.groupProvider.findGroupById(member.getGroupId());
                group.setMemberCount(group.getMemberCount() + 1);
                this.groupProvider.updateGroup(group);
                return null;
            });
            return null;
        });
    }    
    
    /**
     * 创建待审核的group成员时，由于不需要改变成员数量、故不需要加锁；但需要使用事务；
     * @param member 成员
     * @param scope 成员范围
     */
    private void createPendingGroupMember(GroupMember member, GroupVisibilityScope scope) {
        this.dbProvider.execute((status) -> {
            this.groupProvider.createGroupMember(member);
            createUserGroup(member, scope);
            return null;
        });
    }
    
    /**
     * 当同意成员加入group或者接受别人邀请加入group时，成员状态则待审核变为active，
     * 此时group里的成员数需要增加，为了保证成员数的正确性，需要添加锁；
     * @param member 成员
     */
    private void updatePendingGroupMemberToActive(GroupMember member) {
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_GROUP.getCode()).enter(()-> {
            this.dbProvider.execute((status) -> {
                this.groupProvider.updateGroupMember(member);
                updateUserGroupMemberStatus(member);
                
                Group group = this.groupProvider.findGroupById(member.getGroupId());
                group.setMemberCount(group.getMemberCount() + 1);
                this.groupProvider.updateGroup(group);
                return null;
            });
            return null;
        });
    } 
    
    /**
     * 删除group的active成员时，除了删除成员信息之外，还需要减去成员数；
     * 为了保证成员数的正确性，需要添加锁；
     * @param operatorUid 操作者
     * @param member 成员
     * @param reason 删除原因
     */
    private void deleteActiveGroupMember(Long operatorUid, GroupMember member, String reason) {
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_GROUP.getCode()).enter(()-> {
            this.dbProvider.execute((status) -> {
                //deleteGroupMember已经减了成员数，不需要再次减
                this.groupProvider.deleteGroupMember(member);
                this.userProvider.deleteUserGroup(operatorUid, member.getGroupId());
                deleteUserGroupOpRequest(member.getGroupId(), member.getMemberId(), operatorUid, reason);
                return null;
            });
            return null;
        });
    } 
    
    /**
     * 删除group的待审核成员时，只需要删除成员信息，不需要减去成员数、故不需要加锁；
     * @param operatorUid 操作者
     * @param member 成员
     */
    private void deletePendingGroupMember(Long operatorUid, GroupMember member) {
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_GROUP.getCode()).enter(()-> {
            this.dbProvider.execute((status) -> {
                this.groupProvider.deleteGroupMember(member);
                this.userProvider.deleteUserGroup(member.getMemberId(), member.getGroupId());
                return null;
            });
            return null;
        });
    } 
    
    private void sendGroupNotificationToIncludeUser(Long groupId, Long userId, String message) {
        List<Long> includeList = new ArrayList<Long>();
        includeList.add(userId);
        sendGroupNotification(groupId, includeList, null, message, null, null);
    }
    
    private void sendGroupNotificationToIncludeUser(Long groupId, Long userId, String message, 
            MetaObjectType metaObjectType, QuestionMetaObject metaObject) {
        List<Long> includeList = new ArrayList<Long>();
        includeList.add(userId);
        sendGroupNotification(groupId, includeList, null, message, metaObjectType, metaObject);
    }

    private void sendGroupNotificationToExcludeUsers(Long groupId, Long operatorId, Long targetId, String message) {
        List<Long> excludeList = new ArrayList<Long>();
        if(operatorId != null) {
            excludeList.add(operatorId);
        }
        if(targetId != null) {
            excludeList.add(targetId);
        }
        sendGroupNotification(groupId, null, excludeList, message, null, null);
    }
    
    private void sendGroupNotificationInGroupSession(Long groupId,  List<Long> includeList, List<Long> excludeList, 
            String message, MetaObjectType metaObjectType, QuestionMetaObject metaObject) {
        if(message != null && message.length() != 0) {
            String channelType = MessageChannelType.GROUP.getCode();
            String channelToken = String.valueOf(groupId);
            MessageDTO messageDto = new MessageDTO();
            messageDto.setAppId(AppConstants.APPID_MESSAGING);
            messageDto.setSenderUid(User.SYSTEM_UID);
            messageDto.setChannels(new MessageChannel(channelType, channelToken));
            messageDto.setBodyType(MessageBodyType.NOTIFY.getCode());
            messageDto.setBody(message);
            messageDto.setMetaAppId(AppConstants.APPID_GROUP);
            if(includeList != null && includeList.size() > 0) {
                messageDto.getMeta().put(MessageMetaConstant.INCLUDE, StringHelper.toJsonString(includeList));
            }
            if(excludeList != null && excludeList.size() > 0) {
                messageDto.getMeta().put(MessageMetaConstant.EXCLUDE, StringHelper.toJsonString(excludeList));
            }
            if(metaObjectType != null) {
                messageDto.getMeta().put(MessageMetaConstant.META_OBJECT_TYPE, metaObjectType.getCode());
            }
            if(metaObject != null) {
                messageDto.getMeta().put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));
            }

            messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, channelType, 
                channelToken, messageDto, MessagingConstants.MSG_FLAG_STORED.getCode());
        }
    }
    
    private void sendGroupNotification(Long groupId, List<Long> includeList, List<Long> excludeList,
            String message, MetaObjectType metaObjectType, QuestionMetaObject metaObject) {
        if(message == null || message.isEmpty()) {
            return;
        }
        
        boolean groupSession = true;
        Group group = this.groupProvider.findGroupById(groupId);
        if(null == group) {
            return;
        }
        
        if(group.getPrivateFlag().equals(GroupPrivacy.PUBLIC.getCode())) {
            groupSession = false;
        }
        
        if(groupSession) {
            sendGroupNotificationInGroupSession(groupId, includeList, excludeList, message, metaObjectType, metaObject);
        } else {
            //User session
            Map<String, String> meta = null;
            if(metaObjectType != null && metaObject != null) {
                meta = new HashMap<String, String>();
                meta.put(MessageMetaConstant.META_OBJECT_TYPE, metaObjectType.getCode());
                meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));
            }
            
            if(includeList != null && includeList.size() > 0) {
                for(Long userId : includeList) {
                    sendMessageToUser(userId, message, meta);
                }
                return;
            }
            
            Map<Long, Long> exclude = new HashMap<Long, Long>();
            if(excludeList != null) {
                for(Long userId : excludeList) {
                    exclude.put(userId, 1l);
                }
            }
            
            ListingLocator locator = new ListingLocator(groupId);
            List<GroupMember> members = this.groupProvider.listGroupMembers(locator, 1000);
            for(GroupMember gm : members) {
                if(exclude.get(gm.getMemberId()) == null) {
                    sendMessageToUser(gm.getMemberId(), message, meta);
                }
            }
        }
    }

    private void sendRouterGroupNotificationUseSystemUser(List<Long> includeList, List<Long> excludeList, String message, String routerUri) {
        if(message == null || message.isEmpty()) {
            return;
        }

        if(includeList != null && includeList.size() > 0) {
            if (excludeList != null && excludeList.size() > 0) {
                includeList = includeList.stream().filter(r -> !excludeList.contains(r)).collect(Collectors.toList());
            }

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("sendRouterGroupNotificationUseSystemUser includeList {}", includeList);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("sendRouterGroupNotificationUseSystemUser excludeList {}", excludeList);

            MessageDTO messageDto = new MessageDTO();
            messageDto.setAppId(AppConstants.APPID_MESSAGING);
            messageDto.setSenderUid(User.SYSTEM_UID);
            messageDto.setBodyType(MessageBodyType.TEXT.getCode());
            messageDto.setBody(message);
            messageDto.setMetaAppId(AppConstants.APPID_GROUP);

            RouterMetaObject mo = new RouterMetaObject();
            mo.setUrl(routerUri);
            Map<String, String> meta = new HashMap<>();
            meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
            meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(mo));
            messageDto.setMeta(meta);

            includeList.stream().distinct().forEach(targetId -> {
                messageDto.setChannels(Collections.singletonList(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(targetId))));
                messagingService.routeMessage(User.SYSTEM_USER_LOGIN,
                        AppConstants.APPID_MESSAGING, ChannelType.USER.getCode(), String.valueOf(targetId),
                        messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
            });
        }
    }

    private void sendGroupNotificationUseSystemUser(List<Long> includeList, List<Long> excludeList, String message) {
        if(message == null || message.isEmpty()) {
            return;
        }

        if(includeList != null && includeList.size() > 0) {
            if (excludeList != null && excludeList.size() > 0) {
                includeList = includeList.stream().filter(r -> !excludeList.contains(r)).collect(Collectors.toList());
            }

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("sendGroupNotificationUseSystemUser includeList {}", includeList);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("sendGroupNotificationUseSystemUser excludeList {}", excludeList);

            MessageDTO messageDto = new MessageDTO();
            messageDto.setAppId(AppConstants.APPID_MESSAGING);
            messageDto.setSenderUid(User.SYSTEM_UID);
            messageDto.setBodyType(MessageBodyType.TEXT.getCode());
            messageDto.setBody(message);
            messageDto.setMetaAppId(AppConstants.APPID_GROUP);

            includeList.stream().distinct().forEach(targetId -> {
                messageDto.setChannels(Collections.singletonList(new MessageChannel(ChannelType.USER.getCode(), String.valueOf(targetId))));
                messagingService.routeMessage(User.SYSTEM_USER_LOGIN,
                        AppConstants.APPID_MESSAGING, ChannelType.USER.getCode(), String.valueOf(targetId),
                        messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
            });
        }
    }

    /**
     * 私有圈
     * @param group
     * @param member
     */
    private void sendGroupNotificationPrivateForReqToJoinGroupFreely(Group group, GroupMember member) {
     // send notification to the applicant
        try {

            User user = userProvider.findUserById(member.getMemberId());


            String notifyTextForApplicant = localeStringService.getLocalizedString(GroupLocalStringCode.SCOPE, String.valueOf(GroupLocalStringCode.GROUP_SCAN_TO_JOIN), user.getLocale(), "");
            //sendMessageToUser(newCreator.getMemberId(), notifyTextForApplicant, null);

            sendGroupNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant);



//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("groupName", group.getName());
//            User user = userProvider.findUserById(member.getMemberId());
//            String userName = "";
//            if(member.getMemberNickName() != null && !member.getMemberNickName().trim().isEmpty()) {
//                userName = member.getMemberNickName();
//            } else {
//                userName = user.getNickName();
//                if (null != userName) {
//                    userName = "";
//                    }
//                }
//            map.put("userName", userName);
//            String locale = user.getLocale();
//
//            // send notification to who is requesting to join the group
//            String scope = GroupNotificationTemplateCode.SCOPE;
//            int code = GroupNotificationTemplateCode.GROUP_FREE_JOIN_REQ_FOR_APPLICANT;
//            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//            sendGroupNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant);
//
//            // send notification to all members in the group
//            code = GroupNotificationTemplateCode.GROUP_FREE_JOIN_REQ_FOR_OTHER;
//            String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//
//            //Modify by Janson
//            List<Long> includeList = getGroupAdminIncludeList(group.getId(), member.getMemberId(), null);
//            //sendGroupNotificationToExcludeUsers(group.getId(), member.getMemberId(), null, notifyTextForOther);
//            if(includeList.size() > 0) {
//
//                QuestionMetaObject metaObject = createGroupQuestionMetaObject(group, member, null);
//                metaObject.setRequestInfo(notifyTextForOther);
//
//                QuestionMetaActionData actionData = new QuestionMetaActionData();
//                actionData.setMetaObject(metaObject);
//
//                String routerUri = RouterBuilder.build(Router.GROUP_MEMBER_APPLY, actionData);
//
//                sendRouterGroupNotificationUseSystemUser(includeList, null, notifyTextForOther, routerUri);
//            }
        } catch(Exception e) {
            LOGGER.error("Failed to send notification, groupId=" + group.getId() + ", memberId=" + member.getMemberId(), e);
        }
    }
    
    // 兴趣圈     小左对话框       有人订阅兴趣圈（圈不需要审批），通知申请者订阅成功       您已订阅兴趣圈“瑞地自由度” 
    private void sendGroupNotificationPublicForReqToJoinGroupFreely(Group group, GroupMember member) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("groupName", group.getName());
            User user = userProvider.findUserById(member.getMemberId());
            String userName = "";
            if(member.getMemberNickName() != null && !member.getMemberNickName().trim().isEmpty()) {
                userName = member.getMemberNickName();
            } else {
                userName = user.getNickName();
                if (null != userName) {
                    userName = "";
                    }
                }
            map.put("userName", userName);
            String locale = user.getLocale();

            // send notification to who is requesting to join the group
            String scope = GroupNotificationTemplateCode.SCOPE;

            //俱乐部和行业协会使用自己的文案  add by yanjun 20171115
            if (GroupDiscriminator.GROUP == GroupDiscriminator.fromCode(group.getDiscriminator()) && GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(group.getPrivateFlag())) {

                int code;
                map.put("groupName", group.getName());
                if (ClubType.GUILD == ClubType.fromCode(group.getClubType())) {
                    code = GroupNotificationTemplateCode.GROUP_MEMBER_JOIN_FREE_FOR_GUILD;
                } else {
                    code = GroupNotificationTemplateCode.GROUP_MEMBER_JOIN_FREE_FOR_CLUB;
                }

                String notifyTextForAdmin = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
                List<Long> includeList = getGroupAdminIncludeList(group.getId(), member.getMemberId(), null);
                if(includeList.size() > 0) {
                    sendGroupNotification(group.getId(), includeList, null, notifyTextForAdmin, null, null);
                }

                return;

            }


            int code = GroupNotificationTemplateCode.GROUP_MEMBER_PUBLIC_APPLICANT;
            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            sendGroupNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant);
            
            // send notification to all members in the group
            code = GroupNotificationTemplateCode.GROUP_MEMBER_PUBLIC_MEMBER_CHANGE;
            String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            
            //Modify by Janson
            List<Long> includeList = getGroupAdminIncludeList(group.getId(), member.getMemberId(), null);
            //sendGroupNotificationToExcludeUsers(group.getId(), member.getMemberId(), null, notifyTextForOther);
            if(includeList.size() > 0) {
                QuestionMetaObject metaObject = createGroupQuestionMetaObject(group, member, null);
                metaObject.setRequestInfo(notifyTextForOther);

                QuestionMetaActionData actionData = new QuestionMetaActionData();
                actionData.setMetaObject(metaObject);

                String routerUri = RouterBuilder.build(Router.GROUP_MEMBER_APPLY, actionData);

                sendRouterGroupNotificationUseSystemUser(includeList, null, notifyTextForOther, routerUri);
            }
            
       } catch(Exception e) {
            LOGGER.error("Failed to send notification, groupId=" + group.getId() + ", memberId=" + member.getMemberId(), e);
        }
    }
    
    /**
     * 共有圈与私有圈消息不一样
     * @param group
     * @param member
     */
    private void sendGroupNotificationForReqToJoinGroupFreely(Group group, GroupMember member) {
        if(group.getPrivateFlag().equals(GroupPrivacy.PRIVATE.getCode())) {
            this.sendGroupNotificationPrivateForReqToJoinGroupFreely(group, member);
        } else {

            //产品要求需要发消息   add by yanjun 20171115
//        	//加入俱乐部时，如果不需要审核，不发消息，add by tt, 20161104
//        	if (GroupDiscriminator.GROUP == GroupDiscriminator.fromCode(group.getDiscriminator()) && GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(group.getPrivateFlag())) {
//        	    return;
//			}
            this.sendGroupNotificationPublicForReqToJoinGroupFreely(group, member);
        }
    }

    private void sendGroupNotificationForReqToJoinGroupWaitingApproval(Group group, GroupMember member) {
        // send notification to the applicant
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("groupName", group.getName());
            map.put("userName", member.getMemberNickName());
            User user = userProvider.findUserById(member.getMemberId());
            String locale = user.getLocale();

            // send notification to who is requesting to join the group
            // 加入需要审核的俱乐部时，不给申请者发消息，add by tt, 20161104
            String scope = GroupNotificationTemplateCode.SCOPE;
            int code = 0;
            if (GroupDiscriminator.GROUP != GroupDiscriminator.fromCode(group.getDiscriminator()) || GroupPrivacy.PUBLIC != GroupPrivacy.fromCode(group.getPrivateFlag())) {
            	code = GroupNotificationTemplateCode.GROUP_AUTH_JOIN_REQ_FOR_APPLICANT;
            	String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            	sendGroupNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant);
			}

            // send notification to all administrators in the group

            code = GroupNotificationTemplateCode.GROUP_AUTH_JOIN_REQ_FOR_OPERATOR;
            // 如果是俱乐部，则按以下模板发送消息，add by tt, 20161104
            if (GroupDiscriminator.GROUP == GroupDiscriminator.fromCode(group.getDiscriminator()) && GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(group.getPrivateFlag())) {
                if(ClubType.GUILD == ClubType.fromCode(group.getClubType())){
                    code = GroupNotificationTemplateCode.GROUP_MEMBER_TO_ADMIN_WHEN_REQUEST_TO_JOIN_FOR_GUILD;
                    GuildApply guildApply = groupProvider.findGuildApplyByGroupMemberId(member.getId());
                    map.put("userName", guildApply.getName());
                    map.put("organizationName", guildApply.getOrganizationName());
                }else {
                    map.put("reason", member.getRequestorComment());
                    code = GroupNotificationTemplateCode.GROUP_MEMBER_TO_ADMIN_WHEN_REQUEST_TO_JOIN;
                }


			}
            String notifyTextForAdmin = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            List<Long> includeList = getGroupAdminIncludeList(group.getId(), member.getMemberId(), null);
            if(includeList.size() > 0) {

                QuestionMetaObject metaObject = createGroupQuestionMetaObject(group, member, null);
                metaObject.setRequestInfo(notifyTextForAdmin);

                //在信息中增加审批信息 add by yanjun 20171108
                GuildApply guildApply = groupProvider.findGuildApplyByGroupMemberId(member.getId());
                GuildApplyDTO guildApplyDTO = ConvertHelper.convert(guildApply, GuildApplyDTO.class);
                populateGuildApplyDTO(guildApplyDTO);
                metaObject.setJsonInfo(StringHelper.toJsonString(guildApplyDTO));

                if(GroupDiscriminator.GROUP == GroupDiscriminator.fromCode(group.getDiscriminator())
                        && GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(group.getPrivateFlag())
                        && ClubType.GUILD == ClubType.fromCode(group.getClubType())){
                    metaObject.setDetailType(DetailType.GUILD.getCode());
                }

                // 下面的应该写错了，这里不影响以前逻辑的情况下，把俱乐部的metaObjectType换成GROUP_REQUEST_TO_JOIN，add by tt, 20161104
                if (GroupDiscriminator.GROUP == GroupDiscriminator.fromCode(group.getDiscriminator()) && GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(group.getPrivateFlag())) {
                    QuestionMetaActionData actionData = new QuestionMetaActionData();
                    actionData.setMetaObject(metaObject);

                    String routerUri = RouterBuilder.build(Router.GROUP_MEMBER_APPLY, actionData);
                	sendRouterGroupNotificationUseSystemUser(includeList, null, notifyTextForAdmin, routerUri);
                }else {
                    QuestionMetaActionData actionData = new QuestionMetaActionData();
                    actionData.setMetaObject(metaObject);

                    String routerUri = RouterBuilder.build(Router.GROUP_INVITE_APPLY, actionData);
                    sendRouterGroupNotificationUseSystemUser(includeList, null, notifyTextForAdmin, routerUri);
				}
            }
        } catch(Exception e) {
            LOGGER.error("Failed to send notification, groupId=" + group.getId() + ", memberId=" + member.getMemberId(), e);
        }
    }

    private void sendGroupNotificationForInviteToJoinGroupFreely(Group group, GroupMember inviter, GroupMember invitee) {
        if(inviter == null || invitee == null) {
            LOGGER.error("The inviter or invitee should not be null, inviter=" + inviter + ", invitee=" + invitee);
            return;
        }
        
        try {
            // send notification to the applicant
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("groupName", group.getName());
            map.put("inviterName", inviter.getMemberNickName());
            map.put("userName", invitee.getMemberNickName());
            User user = userProvider.findUserById(invitee.getMemberId());
            String locale = user.getLocale();
            
            // send notification to who is invited to join the group
            String scope = GroupNotificationTemplateCode.SCOPE;

            int code = GroupNotificationTemplateCode.GROUP_BE_INVITE_TO_JOIN;
            String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            //sendGroupNotificationToExcludeUsers(group.getId(), inviter.getMemberId(), invitee.getMemberId(), notifyTextForOther);

            sendGroupNotificationToIncludeUser(group.getId(), invitee.getMemberId(), notifyTextForOther);

            //发一条消息通知客户端
            sendGroupNotificationToIncludeUser(group.getId(), invitee.getMemberId(), notifyTextForOther, MetaObjectType.GROUP_INVITE_TO_JOIN_FREE, null);
            //sendGroupNotification(invitee.getMemberId(), null, null, notifyTextForOther, null, null);
        } catch(Exception e) {
            LOGGER.error("Failed to send notification, groupId=" + group.getId() + ", inviterId=" 
                + inviter.getMemberId() + ", inviteeId=" + invitee.getMemberId(), e);
        }
    }

    private void sendGroupNotificationForInviteToJoinGroupToinviter(Group group, Long inviterId, String inviteeNames) {
        if(inviterId == null || inviteeNames == null) {
            LOGGER.error("The inviter or inviteeNames should not be null, inviter=" + inviterId + ", inviteeNames=" + inviteeNames);
            return;
        }

        try {
            // send notification to the applicant
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userNameList", inviteeNames);
            User user = userProvider.findUserById(inviterId);
            String locale = user.getLocale();

            String scope = GroupNotificationTemplateCode.SCOPE;
            int code = GroupNotificationTemplateCode.GROUP_INVITE_USERS_TO_JOIN;
            String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");


            sendGroupNotificationToIncludeUser(group.getId(), inviterId, notifyTextForOther);
        } catch(Exception e) {
            LOGGER.error("Failed to send notification, groupId=" + group.getId() + ", inviterId="
                    + inviterId + ", inviteeId=" + inviterId, e);
        }
    }

    private void sendGroupNotificationForInviteToJoinGroupToOthers(Group group, Long inviterId, String inviteeNames, List<Long> includeList) {
        if(inviterId == null || inviteeNames == null) {
            LOGGER.error("The inviter or inviteeNames should not be null, inviter=" + inviterId + ", inviteeNames=" + inviteeNames);
            return;
        }
        if(includeList.size() == 0){
            return;
        }

        try {

            User user = userProvider.findUserById(inviterId);
            String locale = user.getLocale();
            // send notification to the applicant
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("inviterName", user.getNickName());
            map.put("userNameList", inviteeNames);

            String scope = GroupNotificationTemplateCode.SCOPE;
            int code = GroupNotificationTemplateCode.GROUP_OTHER_INVITE_USERS_TO_JOIN;
            String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

            //sendMessageToUser(newCreator.getMemberId(), notifyTextForApplicant, null);
            sendGroupNotification(group.getId(), includeList, null, notifyTextForOther, null, null);
        } catch(Exception e) {
            LOGGER.error("Failed to send notification, groupId=" + group.getId() + ", inviterId="
                    + inviterId + ", inviteeId=" + inviterId, e);
        }
    }
    
    private void sendGroupNotificationForInviteToJoinGroupWaitingAcceptance(Group group, GroupMember inviter, GroupMember invitee) {
        // send notification to the applicant
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupName", group.getName());
        map.put("operatorName", inviter.getMemberNickName());
        map.put("userName", invitee.getMemberNickName());
        User user = userProvider.findUserById(invitee.getMemberId());
        String locale = user.getLocale();
        
        // send notification to who is invited to join the group
        // 邀请时通知只发能被邀请人，避免在按家庭邀请时暴露家庭成员
        String scope = GroupNotificationTemplateCode.SCOPE;
        int code = GroupNotificationTemplateCode.GROUP_AUTH_JOIN_INVITATION_REQ_FOR_OPERATOR;
        String notifyTextForOperator = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

        QuestionMetaObject metaObject = createGroupQuestionMetaObject(group, inviter, invitee);
        metaObject.setRequestInfo(notifyTextForOperator);
        List<Long> includeList = new ArrayList<>();
        includeList.add(invitee.getMemberId());
        QuestionMetaActionData actionData = new QuestionMetaActionData();
        actionData.setMetaObject(metaObject);

        String routerUri = RouterBuilder.build(Router.GROUP_INVITE_APPLY, actionData);
        sendRouterGroupNotificationUseSystemUser(includeList, null, notifyTextForOperator, routerUri);

        // send notification to inviter
        //code = GroupNotificationTemplateCode.GROUP_AUTH_JOIN_INVITATION_REQ_FOR_APPLICANT;
        //String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        //sendGroupNotificationToIncludeUser(group.getId(), invitee.getMemberId(), notifyTextForApplicant);
        
        // send notification to all administrators in the group
        //code = GroupNotificationTemplateCode.GROUP_AUTH_JOIN_INVITATION_REQ_FOR_OTHER;
        //String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        //List<Long> includeList = getGroupAdminIncludeList(group.getId(), inviter.getMemberId(), invitee.getMemberId());
        //sendGroupNotification(group.getId(), includeList, null, notifyTextForOther, null, null);
    }
    
    private void sendGroupNotificationForAcceptJoinGroupInvitation(Group group, GroupMember inviter, GroupMember invitee) {
        if(inviter == null || invitee == null) {
            LOGGER.error("The inviter or invitee should not be null, inviter=" + inviter + ", invitee=" + invitee);
            return;
        }
        
        // send notification to the applicant
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupName", group.getName());
        map.put("operatorName", inviter.getMemberNickName());
        map.put("userName", invitee.getMemberNickName());
        User user = userProvider.findUserById(invitee.getMemberId());
        String locale = user.getLocale();
        
        // send notification to who is invited to join the group
        String scope = GroupNotificationTemplateCode.SCOPE;
        int code = GroupNotificationTemplateCode.GROUP_JOIN_INVITATION_ACCEPT_FOR_APPLICANT;
        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendGroupNotificationUseSystemUser(Collections.singletonList(inviter.getMemberId()), null, notifyTextForApplicant);

        // send notification to inviter
        code = GroupNotificationTemplateCode.GROUP_JOIN_INVITATION_ACCEPT_FOR_OPERATOR;
        String notifyTextForOperator = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendGroupNotificationUseSystemUser(Collections.singletonList(invitee.getMemberId()), null, notifyTextForOperator);

        // send notification to all members in the group
        code = GroupNotificationTemplateCode.GROUP_JOIN_INVITATION_ACCEPT_FOR_OTHER;
        String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

        sendGroupNotificationToExcludeUsers(group.getId(), inviter.getMemberId(), invitee.getMemberId(), notifyTextForOther);
    }
    
    private void sendGroupNotificationForRejectJoinGroupInvitation(Group group, GroupMember inviter, GroupMember invitee) {
        if(inviter == null || invitee == null) {
            LOGGER.error("The inviter or invitee should not be null, inviter=" + inviter + ", invitee=" + invitee);
            return;
        }
        
        // send notification to the applicant
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupName", group.getName());
        map.put("operatorName", inviter.getMemberNickName());
        map.put("userName", invitee.getMemberNickName());
        User user = userProvider.findUserById(invitee.getMemberId());
        String locale = user.getLocale();
        
        // send notification to who is invited to join the group
        String scope = GroupNotificationTemplateCode.SCOPE;
        int code = GroupNotificationTemplateCode.GROUP_JOIN_INVITATION_REJECT_FOR_APPLICANT;
        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendGroupNotificationToIncludeUser(group.getId(), invitee.getMemberId(), notifyTextForApplicant);

        // send notification to inviter
        code = GroupNotificationTemplateCode.GROUP_JOIN_INVITATION_REJECT_FOR_OPERATOR;
        String notifyTextForOperator = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendGroupNotificationToIncludeUser(group.getId(), inviter.getMemberId(), notifyTextForOperator);
        
        // send notification to all members in the group
        code = GroupNotificationTemplateCode.GROUP_JOIN_INVITATION_REJECT_FOR_OTHER;
        String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendGroupNotificationToExcludeUsers(group.getId(), inviter.getMemberId(), invitee.getMemberId(), notifyTextForOther);
    }
    
    private void sendGroupNotificationForApproveJoinGroupRequest(Group group, GroupMember opeartor, GroupMember requestor) {
        if(opeartor == null || requestor == null) {
            LOGGER.error("The opeartor or requestor should not be null, opeartor=" + opeartor + ", requestor=" + requestor);
            return;
        }
        //加入俱乐部通过时，只给申请者发消息通过了，add by tt, 20161104
    	if (GroupDiscriminator.GROUP == GroupDiscriminator.fromCode(group.getDiscriminator()) && GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(group.getPrivateFlag())) {
    		Map<String, Object> map = new HashMap<String, Object>();
            map.put("groupName", group.getName());
            String locale = getLocale();
            
            String scope = GroupNotificationTemplateCode.SCOPE;
            int code = GroupNotificationTemplateCode.GROUP_MEMBER_APPROVE_REQUEST_TO_JOIN;
            if(ClubType.GUILD == ClubType.fromCode(group.getClubType())){
                code = GroupNotificationTemplateCode.GROUP_MEMBER_APPROVE_REQUEST_TO_JOIN_FOR_GUILD;
                GuildApply guildApply = groupProvider.findGuildApplyByGroupMemberId(requestor.getId());
                map.put("organizationName", guildApply.getOrganizationName());
            }
            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            sendGroupNotificationToIncludeUser(group.getId(), requestor.getMemberId(), notifyTextForApplicant);
    		
			return;
		}
        // send notification to the applicant
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupName", group.getName());
        map.put("operatorName", opeartor.getMemberNickName());
        map.put("userName", requestor.getMemberNickName());
        User user = userProvider.findUserById(requestor.getMemberId());
        String locale = user.getLocale();
        
        // send notification to who is invited to join the group
        String scope = GroupNotificationTemplateCode.SCOPE;
        int code = GroupNotificationTemplateCode.GROUP_AUTH_JOIN_APPROVE_FOR_APPLICANT;
        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendGroupNotificationToIncludeUser(group.getId(), requestor.getMemberId(), notifyTextForApplicant);

        // send notification to inviter
        code = GroupNotificationTemplateCode.GROUP_AUTH_JOIN_APPROVE_FOR_OPERATOR;
        String notifyTextForOperator = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendGroupNotificationToIncludeUser(group.getId(), opeartor.getMemberId(), notifyTextForOperator);
        
        // send notification to all members in the group
        code = GroupNotificationTemplateCode.GROUP_AUTH_JOIN_APPROVE_FOR_OTHER;
        String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendGroupNotificationToExcludeUsers(group.getId(), opeartor.getMemberId(), requestor.getMemberId(), notifyTextForOther);
    }
    
    private void sendGroupNotificationForRejectJoinGroupRequest(Group group, GroupMember opeartor, GroupMember requestor, String rejectText) {
        if(opeartor == null || requestor == null) {
            LOGGER.error("The opeartor or requestor should not be null, opeartor=" + opeartor + ", requestor=" + requestor);
            return;
        }
        //加入俱乐部被拒绝时，只给申请者发消息拒绝了，add by tt, 20161104
    	if (GroupDiscriminator.GROUP == GroupDiscriminator.fromCode(group.getDiscriminator()) && GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(group.getPrivateFlag())) {
    		Map<String, Object> map = new HashMap<String, Object>();
            map.put("groupName", group.getName());
            String locale = getLocale();
            
            String scope = GroupNotificationTemplateCode.SCOPE;
            int code = GroupNotificationTemplateCode.GROUP_MEMBER_REJECT_REQUEST_TO_JOIN;

            if(ClubType.GUILD == ClubType.fromCode(group.getClubType())){
                code = GroupNotificationTemplateCode.GROUP_MEMBER_REJECT_REQUEST_TO_JOIN_FOR_GUILD;
                GuildApply guildApply = groupProvider.findGuildApplyByGroupMemberId(requestor.getId());
                map.put("organizationName", guildApply.getOrganizationName());
                map.put("rejectText", rejectText);
            }

            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            sendGroupNotificationToIncludeUser(group.getId(), requestor.getMemberId(), notifyTextForApplicant);
    		
			return;
		}
    	
        // send notification to the applicant
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupName", group.getName());
        map.put("operatorName", opeartor.getMemberNickName());
        map.put("userName", requestor.getMemberNickName());
        User user = userProvider.findUserById(requestor.getMemberId());
        String locale = user.getLocale();
        
        // send notification to who is invited to join the group
        String scope = GroupNotificationTemplateCode.SCOPE;
        int code = GroupNotificationTemplateCode.GROUP_AUTH_JOIN_REJECT_FOR_APPLICANT;
        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendGroupNotificationToIncludeUser(group.getId(), requestor.getMemberId(), notifyTextForApplicant);

        // send notification to inviter
        code = GroupNotificationTemplateCode.GROUP_AUTH_JOIN_REJECT_FOR_OPERATOR;
        String notifyTextForOperator = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendGroupNotificationToIncludeUser(group.getId(), opeartor.getMemberId(), notifyTextForOperator);
        
        // send notification to all members in the group
        code = GroupNotificationTemplateCode.GROUP_AUTH_JOIN_REJECT_FOR_OTHER;
        String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendGroupNotificationToExcludeUsers(group.getId(), opeartor.getMemberId(), requestor.getMemberId(), notifyTextForOther);
    }
    
    private void sendGroupNotificationPrivateForMemberLeaveGroup(Group group, GroupMember member) {
        if(member == null) {
            LOGGER.error("The member should not be null, member=" + member);
            return;
        }
        
        // send notification to the applicant
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupName", group.getName());
        map.put("userName", member.getMemberNickName());
        User user = userProvider.findUserById(member.getMemberId());
        String locale = user.getLocale();
        
        // send notification to who is leaving the group
        String scope = GroupNotificationTemplateCode.SCOPE;
        //int code = GroupNotificationTemplateCode.GROUP_MEMBER_LEAVE_FOR_OPERATOR;
        //String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        //sendGroupNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant);
        
        // send notification to all members in the group
        int code = GroupNotificationTemplateCode.GROUP_MEMBER_LEAVE_FOR_OTHER;
        final String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        GroupPrivacy groupPrivacy = GroupPrivacy.fromCode(group.getPrivateFlag());
        //Modify by Janson
        if(groupPrivacy == GroupPrivacy.PRIVATE) {
            sendGroupNotificationToExcludeUsers(group.getId(), member.getMemberId(), null, notifyTextForOther);    
        } else {
            List<Long> includeList = getGroupAdminIncludeList(group.getId(), member.getMemberId(), null);
            if(includeList.size() > 0) {
                sendGroupNotification(group.getId(), includeList, null, notifyTextForOther, 
                    null, null);
            }
        }
    }
    
    //公众圈推出信息
    private void sendGroupNotificationPublicForMemberLeaveGroup(Group group, GroupMember member) {
        if(member == null) {
            LOGGER.error("The member should not be null, member=" + member);
            return;
        }
        
        // send notification to the applicant
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupName", group.getName());
        map.put("userName", member.getMemberNickName());
        User user = userProvider.findUserById(member.getMemberId());
        String locale = user.getLocale();
        
        String scope = GroupNotificationTemplateCode.SCOPE;
        
        // send notification to all members in the group
        int code = GroupNotificationTemplateCode.GROUP_MEMBER_DELETE_MEMBER;

        //俱乐部和行业协会使用自己的文案
        if (GroupDiscriminator.GROUP == GroupDiscriminator.fromCode(group.getDiscriminator()) && GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(group.getPrivateFlag())) {

            if(ClubType.GUILD == ClubType.fromCode(group.getClubType())){
                code = GroupNotificationTemplateCode.GROUP_MEMBER_DELETE_MEMBER_FOR_GUILD;
            }else {
                code = GroupNotificationTemplateCode.GROUP_MEMBER_DELETE_MEMBER_FOR_CLUB;
            }

        }

        String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendMessageToUser(member.getMemberId(), notifyTextForOther, null);

        //俱乐部和行业协会不在发送人数有变化的信息 add by yanjun 20171115
        if (GroupDiscriminator.GROUP == GroupDiscriminator.fromCode(group.getDiscriminator()) && GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(group.getPrivateFlag())) {
            return;
        }

        code = GroupNotificationTemplateCode.GROUP_MEMBER_PUBLIC_MEMBER_CHANGE;
        notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        
        List<Long> includeList = getGroupAdminIncludeList(group.getId(), member.getMemberId(), null);
        if(includeList.size() > 0) {
            sendGroupNotification(group.getId(), includeList, null, notifyTextForOther, null, null);
        }
    }
    
    private void sendGroupNotificationForMemberLeaveGroup(Group group, GroupMember member) {
        if(group.getPrivateFlag().equals(GroupPrivacy.PRIVATE.getCode())) {
            sendGroupNotificationPrivateForMemberLeaveGroup(group, member);
        } else {
            sendGroupNotificationPublicForMemberLeaveGroup(group, member);
        }
    }

    private void sendGroupNotificationForRevokeGroupMember(Group group, GroupMember opeartor, GroupMember member) {
        if(opeartor == null || member == null) {
            LOGGER.error("The opeartor or member should not be null, opeartor=" + opeartor + ", requestor=" + member);
            return;
        }

        // send notification to the applicant
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userName", opeartor.getMemberNickName());
        User user = userProvider.findUserById(member.getMemberId());
        String locale = user.getLocale();

        String scope = GroupNotificationTemplateCode.SCOPE;
        int code = GroupNotificationTemplateCode.GROUP_BE_REMOVE;
        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

//        Date now = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
//        String hhmm = dateFormat.format( now );
//
//        sendGroupNotificationToIncludeUser(group.getId(), member.getMemberId(), hhmm);

        //sendMessageToUser(newCreator.getMemberId(), notifyTextForApplicant, null);
        sendGroupNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant);

        //给客户端发一条通知消息
        sendGroupNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant, MetaObjectType.GROUP_MEMBER_DELETE, null);

//        // send notification to who is invited to join the group
//        String scope = GroupNotificationTemplateCode.SCOPE;
//        int code = GroupNotificationTemplateCode.GROUP_MEMBER_INVOKE_FOR_APPLICANT;
//        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//        sendGroupNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant);
//
//        // send notification to inviter
//        code = GroupNotificationTemplateCode.GROUP_MEMBER_INVOKE_FOR_OPERATOR;
//        String notifyTextForOperator = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//        sendGroupNotificationToIncludeUser(group.getId(), opeartor.getMemberId(), notifyTextForOperator);
//
//        // send notification to all members in the group
//        code = GroupNotificationTemplateCode.GROUP_MEMBER_INVOKE_FOR_OTHER;
//        String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
//        sendGroupNotificationToExcludeUsers(group.getId(), opeartor.getMemberId(), member.getMemberId(), notifyTextForOther);
    }

    private void sendGroupNotificationForRevokeGroupMemberToOpeartor(Long groupId, Long opeartorId, String revokeNames) {
        if(groupId == null || opeartorId == null) {
            LOGGER.error("The groupId or opeartorId should not be null, groupId=" + groupId + ", opeartorId=" + opeartorId);
            return;
        }

        // send notification to the applicant
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userNameList", revokeNames);
        User user = userProvider.findUserById(opeartorId);
        String locale = user.getLocale();

        String scope = GroupNotificationTemplateCode.SCOPE;
        int code = GroupNotificationTemplateCode.GROUP_REMOVE_MEMBERS;
        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

        //sendMessageToUser(newCreator.getMemberId(), notifyTextForApplicant, null);
        sendGroupNotificationToIncludeUser(groupId, opeartorId, notifyTextForApplicant);

    }
    
    private void sendGroupNotificationForReqToBeGroupAdminWaitingApproval(Group group, GroupMember member) {
        // send notification to the applicant
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("groupName", group.getName());
            map.put("userName", member.getMemberNickName());
            User user = userProvider.findUserById(member.getMemberId());
            String locale = user.getLocale();
            
            // send notification to who is requesting to join the group
            String scope = GroupAdminNotificationTemplateCode.SCOPE;
            int code = GroupAdminNotificationTemplateCode.GROUP_ADMINROLE_REQ_FOR_APPLICANT;
            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            sendGroupNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant);
            
            // send notification to all administrators in the group
            code = GroupAdminNotificationTemplateCode.GROUP_ADMINROLE_REQ_FOR_OPERATOR;
            final String notifyTextForAdmin = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            List<Long> includeList = getGroupAdminIncludeList(group.getId(), member.getMemberId(), null);
            if(includeList.size() > 0) {
                QuestionMetaObject metaObject = createGroupQuestionMetaObject(group, member, null);
                metaObject.setRequestInfo(notifyTextForAdmin);

                QuestionMetaActionData actionData = new QuestionMetaActionData();
                actionData.setMetaObject(metaObject);

                String routerUri = RouterBuilder.build(Router.GROUP_MANAGER_APPLY, actionData);
                sendRouterGroupNotificationUseSystemUser(includeList, null, notifyTextForAdmin, routerUri);
            }
        } catch(Exception e) {
            LOGGER.error("Failed to send notification, groupId=" + group.getId() + ", memberId=" + member.getMemberId(), e);
        }
    }
    
    private void sendGroupNotificationForInviteToBeGroupAdminFreely(Group group, GroupMember inviter, GroupMember invitee) {
        if(inviter == null || invitee == null) {
            LOGGER.error("The inviter or invitee should not be null, inviter=" + inviter + ", invitee=" + invitee);
            return;
        }
        
        try {
            // send notification to the applicant
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("groupName", group.getName());
            map.put("operatorName", inviter.getMemberNickName());
            map.put("userName", invitee.getMemberNickName());
            User user = userProvider.findUserById(invitee.getMemberId());
            String locale = user.getLocale();
            
            // send notification to who is invited to join the group
            String scope = GroupAdminNotificationTemplateCode.SCOPE;
            int code = GroupAdminNotificationTemplateCode.GROUP_ADMINROLE_FREE_INVITATION_FOR_APPLICANT;
            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            sendGroupNotificationToIncludeUser(group.getId(), invitee.getMemberId(), notifyTextForApplicant);

            // send notification to inviter
            code = GroupAdminNotificationTemplateCode.GROUP_ADMINROLE_FREE_INVITATION_FOR_OPERATOR;
            String notifyTextForOperator = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            sendGroupNotificationToIncludeUser(group.getId(), inviter.getMemberId(), notifyTextForOperator);

            // send notification to inviter
            code = GroupAdminNotificationTemplateCode.GROUP_ADMINROLE_FREE_INVITATION_FOR_OTHER;
            String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            List<Long> includeList = getGroupAdminIncludeList(group.getId(), inviter.getMemberId(), invitee.getMemberId());
            if(includeList.size() > 0) {
                sendGroupNotification(group.getId(), includeList, null, notifyTextForOther, null, null);
            }
        } catch(Exception e) {
            LOGGER.error("Failed to send notification, groupId=" + group.getId() + ", inviterId=" 
                + inviter.getMemberId() + ", inviteeId=" + invitee.getMemberId(), e);
        }
    }  
    
    private void sendGroupNotificationForInviteToBeGroupAdminWaitingAcceptance(Group group, GroupMember inviter, GroupMember invitee) {
        if(inviter == null || invitee == null) {
            LOGGER.error("The inviter or invitee should not be null, inviter=" + inviter + ", invitee=" + invitee);
            return;
        }
        
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("groupName", group.getName());
            map.put("operatorName", inviter.getMemberNickName());
            map.put("userName", invitee.getMemberNickName());
            User user = userProvider.findUserById(invitee.getMemberId());
            String locale = user.getLocale();
            
            String scope = GroupAdminNotificationTemplateCode.SCOPE;
            int code = GroupAdminNotificationTemplateCode.GROUP_ADMINROLE_INVITATION_REQ_FOR_OPERATOR;
            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            sendGroupNotificationToIncludeUser(group.getId(), invitee.getMemberId(), notifyTextForApplicant);

            code = GroupAdminNotificationTemplateCode.GROUP_ADMINROLE_INVITATION_REQ_FOR_APPLICANT;
            String notifyTextForOperator = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            List<Long> includeList = getGroupAdminIncludeList(group.getId(), inviter.getMemberId(), invitee.getMemberId());
            if(includeList.size() > 0) {
                sendGroupNotification(group.getId(), includeList, null, notifyTextForOperator, null, null);
            }
        } catch(Exception e) {
            LOGGER.error("Failed to send notification, groupId=" + group.getId() + ", inviterId=" 
                + inviter.getMemberId() + ", inviteeId=" + invitee.getMemberId(), e);
        }
    }
    
    private void sendGroupNotificationForResignGroupAdmin(Group group, GroupMember member) {
        if(member == null) {
            LOGGER.error("The member should not be null, member=" + member);
            return;
        }
        
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("groupName", group.getName());
            map.put("userName", member.getMemberNickName());
            User user = userProvider.findUserById(member.getMemberId());
            String locale = user.getLocale();
            
            String scope = GroupAdminNotificationTemplateCode.SCOPE;
            int code = GroupAdminNotificationTemplateCode.GROUP_ADMINROLE_LEAVE_FOR_OPERATOR;
            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            sendGroupNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant);

            code = GroupAdminNotificationTemplateCode.GROUP_ADMINROLE_LEAVE_FOR_OTHER;
            String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            List<Long> includeList = getGroupAdminIncludeList(group.getId(), member.getMemberId(), null);
            if(includeList.size() > 0) {
                sendGroupNotification(group.getId(), includeList, null, notifyTextForOther, null, null);
            }
        } catch(Exception e) {
            LOGGER.error("Failed to send notification, groupId=" + group.getId() + ", inviterId=" + member.getMemberId(), e);
        }
    }  
    
    private void sendGroupNotificationForRevokeGroupAdmin(Group group, GroupMember operator, GroupMember member) {
        if(operator == null || member == null) {
            LOGGER.error("The operator or member should not be null, operator=" + operator + ", member=" + member);
            return;
        }
        
        try {
            // send notification to the applicant
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("groupName", group.getName());
            map.put("operatorName", operator.getMemberNickName());
            map.put("userName", member.getMemberNickName());
            User user = userProvider.findUserById(member.getMemberId());
            String locale = user.getLocale();
            
            String scope = GroupAdminNotificationTemplateCode.SCOPE;
            int code = GroupAdminNotificationTemplateCode.GROUP_ADMINROLE_INVOKE_FOR_APPLICANT;
            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            sendGroupNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant);
            
            scope = GroupAdminNotificationTemplateCode.SCOPE;
            code = GroupAdminNotificationTemplateCode.GROUP_ADMINROLE_INVOKE_FOR_OPERATOR;
            String notifyTextForOperator = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            sendGroupNotificationToIncludeUser(group.getId(), operator.getMemberId(), notifyTextForOperator);

            code = GroupAdminNotificationTemplateCode.GROUP_ADMINROLE_INVOKE_FOR_OTHER;
            String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            List<Long> includeList = getGroupAdminIncludeList(group.getId(), operator.getMemberId(), member.getMemberId());
            if(includeList.size() > 0) {
                sendGroupNotification(group.getId(), includeList, null, notifyTextForOther, null, null);
            }
        } catch(Exception e) {
            LOGGER.error("Failed to send notification, groupId=" + group.getId() + ", inviterId=" + member.getMemberId(), e);
        }
    }  
    
    private void sendGroupNotificationForApproveGroupAdminRequest(Group group, GroupMember operator, GroupMember member) {
        if(operator == null || member == null) {
            LOGGER.error("The operator or member should not be null, operator=" + operator + ", member=" + member);
            return;
        }
        
        try {
            // send notification to the applicant
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("groupName", group.getName());
            map.put("operatorName", operator.getMemberNickName());
            map.put("userName", member.getMemberNickName());
            User user = userProvider.findUserById(member.getMemberId());
            String locale = user.getLocale();
            
            String scope = GroupAdminNotificationTemplateCode.SCOPE;
            int code = GroupAdminNotificationTemplateCode.GROUP_ADMINROLE_APPROVE_FOR_APPLICANT;
            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            sendGroupNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant);
            
            scope = GroupAdminNotificationTemplateCode.SCOPE;
            code = GroupAdminNotificationTemplateCode.GROUP_ADMINROLE_APPROVE_FOR_OPERATOR;
            String notifyTextForOperator = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            sendGroupNotificationToIncludeUser(group.getId(), operator.getMemberId(), notifyTextForOperator);

            code = GroupAdminNotificationTemplateCode.GROUP_ADMINROLE_APPROVE_FOR_OTHER;
            String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            List<Long> includeList = getGroupAdminIncludeList(group.getId(), operator.getMemberId(), member.getMemberId());
            if(includeList.size() > 0) {
                sendGroupNotification(group.getId(), includeList, null, notifyTextForOther, null, null);
            }
        } catch(Exception e) {
            LOGGER.error("Failed to send notification, groupId=" + group.getId() + ", inviterId=" + member.getMemberId(), e);
        }
    }   
    
    private void sendGroupNotificationForRejectGroupAdminRequest(Group group, GroupMember operator, GroupMember member) {
        if(operator == null || member == null) {
            LOGGER.error("The operator or member should not be null, operator=" + operator + ", member=" + member);
            return;
        }
        
        try {
            // send notification to the applicant
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("groupName", group.getName());
            map.put("operatorName", operator.getMemberNickName());
            map.put("userName", member.getMemberNickName());
            User user = userProvider.findUserById(member.getMemberId());
            String locale = user.getLocale();
            
            String scope = GroupAdminNotificationTemplateCode.SCOPE;
            int code = GroupAdminNotificationTemplateCode.GROUP_ADMINROLE_REJECT_FOR_APPLICANT;
            String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            sendGroupNotificationToIncludeUser(group.getId(), member.getMemberId(), notifyTextForApplicant);
            
            scope = GroupAdminNotificationTemplateCode.SCOPE;
            code = GroupAdminNotificationTemplateCode.GROUP_ADMINROLE_REJECT_FOR_OPERATOR;
            String notifyTextForOperator = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            sendGroupNotificationToIncludeUser(group.getId(), operator.getMemberId(), notifyTextForOperator);

            code = GroupAdminNotificationTemplateCode.GROUP_ADMINROLE_REJECT_FOR_OTHER;
            String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            List<Long> includeList = getGroupAdminIncludeList(group.getId(), operator.getMemberId(), member.getMemberId());
            if(includeList.size() > 0) {
                sendGroupNotification(group.getId(), includeList, null, notifyTextForOther, null, null);
            }
        } catch(Exception e) {
            LOGGER.error("Failed to send notification, groupId=" + group.getId() + ", inviterId=" + member.getMemberId(), e);
        }
    }  
    
    private QuestionMetaObject createGroupQuestionMetaObject(Group group, GroupMember requestor, GroupMember target) {
        QuestionMetaObject metaObject = new QuestionMetaObject();
        
        if(group != null) {
            metaObject.setResourceType(EntityType.GROUP.getCode());
            metaObject.setResourceId(group.getId());
        }
        
        if(requestor != null) {
            metaObject.setRequestorUid(requestor.getMemberId());
            metaObject.setRequestTime(requestor.getCreateTime());
            metaObject.setRequestorNickName(requestor.getMemberNickName());
            String avatar = requestor.getMemberAvatar();
            metaObject.setRequestorAvatar(avatar);
            if(avatar != null && avatar.length() > 0) {
                try{
                    String url = contentServerService.parserUri(avatar, EntityType.GROUP.getCode(), group.getId());
                    metaObject.setRequestorAvatarUrl(url);
                }catch(Exception e){
                    LOGGER.error("Failed to parse avatar uri of group member, groupId=" + group.getId() 
                        + ", memberId=" + requestor.getMemberId(), e);
                }
            }
            metaObject.setRequestId(requestor.getId());

            //增加电话信息 add by yanjun 20171114
            UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(requestor.getMemberId(), IdentifierType.MOBILE.getCode());
            if(userIdentifier != null){
                metaObject.setRequestorPhone(userIdentifier.getIdentifierToken());
            }
        }
        
        if(target != null) {
            metaObject.setTargetType(EntityType.USER.getCode());
            metaObject.setTargetId(target.getMemberId());
            metaObject.setRequestId(target.getId());
        }
        
        return metaObject;
    }
    
    private List<Long> getGroupAdminIncludeList(Long groupId, Long operatorId, Long targetId) {
        CrossShardListingLocator locator = new CrossShardListingLocator(groupId);
        List<GroupMember> adminMembers = this.groupProvider.queryGroupMembers(locator, Integer.MAX_VALUE,(loc, query) -> {
            Condition c = Tables.EH_GROUP_MEMBERS.MEMBER_ROLE.eq(Role.ResourceCreator);
            c = c.or(Tables.EH_GROUP_MEMBERS.MEMBER_ROLE.eq(Role.ResourceAdmin));
            query.addConditions(c);
            return query;
        });
        List<Long> includeList = new ArrayList<Long>();
        for(GroupMember adminMember : adminMembers) {
            if((operatorId == null || !operatorId.equals(adminMember.getMemberId())) 
                && (targetId == null || !targetId.equals(adminMember.getMemberId()))) {
                includeList.add(adminMember.getMemberId());
            }
        }
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Get group admin include list, groupId=" + groupId + ", operatorId=" + operatorId 
                + ", targetId=" + targetId + ", includeList=" + includeList);
        }
        
        return includeList;
    }

	@Override
	public ListGroupCommandResponse listGroups(ListGroupCommand cmd) {
		long startTime = System.currentTimeMillis();
        User operator = UserContext.current().getUser();
        Long operatorUid = operator.getId();
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        
        
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor()==null?0L:cmd.getPageAnchor());
        Long categoryId = cmd.getCategoryId();
        String keyWord = cmd.getKeyWord();
        List<Group> groups = this.groupProvider.queryGroups(locator, pageSize+1, (loc, query) -> {
        	query.addConditions(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.GROUP.getCode()));
        	query.addConditions(Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode()));
        	
        	Category category = null;
            if(categoryId != null) {
                category = this.categoryProvider.findCategoryById(categoryId);
                query.addConditions(Tables.EH_GROUPS.CATEGORY_PATH.like(category.getPath() + "%"));
            }
            
            if(keyWord != null){
            	Condition c1 = Tables.EH_GROUPS.NAME.like("%" + keyWord + "%");
            	Condition c2 = Tables.EH_GROUPS.DESCRIPTION.like("%" + keyWord + "%");
            	Condition c3 = c1.or(c2);
            	query.addConditions(c3);
            }
            return query;
        });
        
        ListGroupCommandResponse cmdResponse = new ListGroupCommandResponse();
        if(groups.size() > pageSize) {
            groups.remove(groups.size() - 1);
            cmdResponse.setNextPageAnchor(groups.get(groups.size() - 1).getId());
        }
        cmdResponse.setGroups(groups.stream().map((r)-> { 
        	GroupDTO groupDTO = toGroupDTO(operatorUid, r);
        	Long creatorId = r.getCreatorUid();
        	UserInfo user = userService.getUserSnapshotInfo(creatorId);
        	if(user != null)
        		groupDTO.setCreatorName(user.getNickName());
        	
        	List<FamilyDTO> userFamily = familyProvider.getUserFamiliesByUserId(creatorId);
        	if(userFamily != null){
        		String familyName = userFamily.get(0).getAddress();
        		groupDTO.setCreatorFamilyName(familyName);
        	}
        		
            return groupDTO; 
        }).collect(Collectors.toList())); 
        
        
        
        return cmdResponse;
	}
	
    private void sendMessageToUser(Long uid, String content, Map<String, String> meta) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_UID);
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), uid.toString()));
        // messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.NOTIFY.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_GROUP);
        if(null != meta && meta.size() > 0) {
            messageDto.getMeta().putAll(meta);
            }
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
                uid.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }

    /**
     * 开启或者关闭工作台发消息
     * @param uid
     * @param content
     * @param openOrCloseType
     */
    @Override
    public void workBenchSendMessageToUser(Long uid , String content , String openOrCloseType){
        //首先需要进行非空校验
        if(uid != null && !StringUtils.isEmpty(content) && !StringUtils.isEmpty(openOrCloseType)){
            //说明传过来的参数都不为空，那么我们才能进行发送消息
            //接下来我们需要判断我们现在做的操作是开启工作台还是关闭工作台，这个需要根据我们传进来的openOrCloseType字段，来进行判断
            //根据uid来查询eh_organization_members表，然后拿到所有的公司的id
//            List<Long> orgIdList = organizationProvider.findOrganizationIdListByTargetId(uid);
//            boolean openWorkBench = false;
            if(openOrCloseType.equals(MetaObjectType.WORK_BENCH_FLAG_OPEN.getCode())){
                //说明我们需要做的是开启工作台
                //接下来我们需要做的是查询该员工所在的公司是否已经开启了工作台，如果都没有开启工作台，那么我们就给该客户发消息说将要开启工作台，如果
                //只要存在一个公司已经开启了工作台，那么我们就不需要给该用户发消息了
                //接下来进行非空校验
//                if(!CollectionUtils.isEmpty(orgIdList)){
                    //说明查询到的公司的id的集合不为空，那么我们采用forEach循环进行遍历
                    /*for(Long lon : orgIdList){
                        //接下来根据每一个公司id来查询eh_organizations 表中的工作台（work_platform_flag）是否是1,1-表示的是已经开启了工作台，0-表示的是没有开启工作台
                        Organization organization = organizationProvider.findOrganizationById(lon);
                        if(organization.getWorkPlatformFlag() == TrueOrFalseFlag.TRUE.getCode()){
                            //说明该公司已经开启了工作台，那么我们就不需要给该员工发送将要开启工作台的消息了
                            //直接跳出
                            openWorkBench = true;
                            break;
                        }
                        //说明该公司之前是没有开启工作台，那么我们就直接continue操作，进行第二次循环，目的是循环完所有的公司，如果都是没有开启工作台，那么我们就给该
                        //用户进行发送消息，说将要开启工作台
                        continue;
                    }*/
//                    if(!openWorkBench){
                        //说明该员工所在的公司之前都没有开启工作台，那么我们就该该员工发送消息，说将要开启工作台
                    //在这里是这样设置的，不管是开启工作台还是关闭工作台都需要分别发送两条消息，一条是带有meta_object_type的，一条是不带有meta_object_type的
                    //首先我们来发送一条不带有meta_object_type的消息
/*
                MessageDTO messageDto = new MessageDTO();
                messageDto.setAppId(AppConstants.APPID_MESSAGING);
                messageDto.setSenderUid(User.SYSTEM_UID);
                messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), uid.toString()));
                messageDto.setBodyType(MessageBodyType.TEXT.getCode());
                messageDto.setBody(content);
                messageDto.setMetaAppId(AppConstants.APPID_GROUP);
//                messageDto.getMeta().put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.WORK_BENCH_FLAG_OPEN.getCode());
                messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                        uid.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());*/
                //再发送一条带有meta_object_type的消息
                MessageDTO messageDto1 = new MessageDTO();
                messageDto1.setAppId(AppConstants.APPID_MESSAGING);
                messageDto1.setSenderUid(User.SYSTEM_UID);
                messageDto1.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), uid.toString()));
                messageDto1.setBodyType(MessageBodyType.TEXT.getCode());
                messageDto1.setBody(content);
                messageDto1.setMetaAppId(AppConstants.APPID_GROUP);
                messageDto1.getMeta().put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.WORK_BENCH_FLAG_OPEN.getCode());
                messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                        uid.toString(), messageDto1, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
//                    }

            }else if(openOrCloseType.equals(MetaObjectType.WORK_BENCH_FLAG_CLOSE.getCode())){
                //说明我们需要做的是关闭工作台
                //接下来对orgIdList集合进行非空校验
//                if(!CollectionUtils.isEmpty(orgIdList)){
                //说明查询到的公司的id的集合不为空，那么我们采用forEach循环进行遍历
                /*for(Long lon : orgIdList){
                    //接下来根据每一个公司id来查询eh_organizations 表中的工作台（work_platform_flag）是否是1,1-表示的是已经开启了工作台，0-表示的是没有开启工作台
                    Organization organization = organizationProvider.findOrganizationById(lon);
                    if(organization.getWorkPlatformFlag() == TrueOrFalseFlag.TRUE.getCode()){
                        //说明该公司已经开启了工作台，那么我们就不需要给该员工发送将要开启工作台的消息了
                        //直接跳出
                        openWorkBench = true;
                        break;
                    }
                    //说明该公司之前是没有开启工作台，那么我们就直接continue操作，进行第二次循环，目的是循环完所有的公司，如果都是没有开启工作台，那么我们就给该
                    //用户进行发送消息，说将要开启工作台
                    continue;
                }*/
//                    if(openWorkBench == true){
                //说明该员工所在的公司之前都已经开启工作台，那么我们就该该员工发送消息，说将要关闭工作台

/*                MessageDTO messageDto = new MessageDTO();
                messageDto.setAppId(AppConstants.APPID_MESSAGING);
                messageDto.setSenderUid(User.SYSTEM_UID);
                messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), uid.toString()));
                messageDto.setBodyType(MessageBodyType.TEXT.getCode());
                messageDto.setBody(content);
                messageDto.setMetaAppId(AppConstants.APPID_GROUP);
//                messageDto.getMeta().put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.WORK_BENCH_FLAG_CLOSE.getCode());
                messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                        uid.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());*/

                MessageDTO messageDto1 = new MessageDTO();
                messageDto1.setAppId(AppConstants.APPID_MESSAGING);
                messageDto1.setSenderUid(User.SYSTEM_UID);
                messageDto1.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), uid.toString()));
                messageDto1.setBodyType(MessageBodyType.TEXT.getCode());
                messageDto1.setBody(content);
                messageDto1.setMetaAppId(AppConstants.APPID_GROUP);
                messageDto1.getMeta().put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.WORK_BENCH_FLAG_CLOSE.getCode());
                messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                        uid.toString(), messageDto1, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
//                    }

//                }

            }
        }
    }


    private void sendSystemMessageToUser(Long uid, String content, Map<String, String> meta) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_UID);
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), uid.toString()));
        // messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_GROUP);
        if(null != meta && meta.size() > 0) {
            messageDto.getMeta().putAll(meta);
        }
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                uid.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }
    
    private void sendNotificationToAdmin(List<Long> adminList, String operator, Group group, String locale) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupName", group.getName());
        map.put("userName", operator);
        
        String scope = GroupNotificationTemplateCode.SCOPE;
        int code = GroupNotificationTemplateCode.GROUP_MEMBER_DELETED_ADMIN;
        if(GroupDiscriminator.fromCode(group.getDiscriminator()) == GroupDiscriminator.GROUP && GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PUBLIC){
        	//如果解散俱乐部，消息改为你加入的“${groupName}”已解散， add by tt, 20161102
        	code = GroupNotificationTemplateCode.GROUP_MEMBER_TO_ALL_WHEN_DELETE;
        }
        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
       
        for(Long userId: adminList) {
            sendMessageToUser(userId, notifyTextForApplicant, null);
        }
    }
    
    private void sendNotificationToCreator(Long creator, String userName, Group group, String locale) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupName", group.getName());
        map.put("userName", userName);
       
        String scope = GroupNotificationTemplateCode.SCOPE;
        int code = GroupNotificationTemplateCode.GROUP_MEMBER_DELETED_OPERATOR;
        if(GroupDiscriminator.fromCode(group.getDiscriminator()) == GroupDiscriminator.GROUP && GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PUBLIC){
        	//如果解散俱乐部，消息改为你加入的“${groupName}”已解散， add by tt, 20161102
        	code = GroupNotificationTemplateCode.GROUP_MEMBER_TO_ALL_WHEN_DELETE;
        }
        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendMessageToUser(creator, notifyTextForApplicant, null);
    }
    
    private void sendNotifactionToMembers(List<Long> members, String nickName, Group group, String locale) {
        Map<String, Object> map = new HashMap<String, Object>();
        if(group.getName() == null ){
            map.put("groupName", nickName);
        }else {
            map.put("groupName", group.getName());
        }

//        map.put("userName", userName);
       
        String scope = GroupNotificationTemplateCode.SCOPE;
        int code = GroupNotificationTemplateCode.GROUP_DELETE;


        //俱乐部和行业协会使用自己的文案
        if (GroupDiscriminator.GROUP == GroupDiscriminator.fromCode(group.getDiscriminator()) && GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(group.getPrivateFlag())) {

            if(ClubType.GUILD == ClubType.fromCode(group.getClubType())){
                code = GroupNotificationTemplateCode.GROUP_DELETE_FOR_GUILD;
            }else {
                code = GroupNotificationTemplateCode.GROUP_DELETE_FOR_CLUB;
            }

        }


//        int code = GroupNotificationTemplateCode.GROUP_MEMBER_DELETE_MEMBER;
//        //如果是解散群聊，提示普通人${userName}已删除群聊“${groupName}”，update by tt, 20160811
//        if(GroupDiscriminator.fromCode(group.getDiscriminator()) == GroupDiscriminator.GROUP && GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PRIVATE){
//        	code = GroupNotificationTemplateCode.GROUP_MEMBER_DELETED_ADMIN;
//        }else if (GroupDiscriminator.fromCode(group.getDiscriminator()) == GroupDiscriminator.GROUP && GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PUBLIC) {
//        	//如果解散俱乐部，消息改为你加入的“${groupName}”已解散， add by tt, 20161102
//        	code = GroupNotificationTemplateCode.GROUP_MEMBER_TO_ALL_WHEN_DELETE;
//		}
        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        
            //如果圈太大，不发消息
        //make better hear
        if(members.size() > 100) {
            return;
            }
        
        for(Long userId: members) {
            sendSystemMessageToUser(userId, notifyTextForApplicant, null);

            //发一条消息通知客户端
            /*Map<String, String> meta = meta = new HashMap<String, String>();
            meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.GROUP_TALK_DISSOLVED.getCode());
            meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(null));
            sendSystemMessageToUser(userId, notifyTextForApplicant, meta);*/

            //发一条消息通知客户端
            sendGroupNotificationToIncludeUser(group.getId(), userId, notifyTextForApplicant, MetaObjectType.GROUP_TALK_DISSOLVED, null);
        }
    }
    
    @Override
    public void deleteGroupByCreator(long groupId) {
        User user = UserContext.current().getUser();
        //先把别名查出来，因为删除之后查不了  add by yanjun
        String alias = getGroupAlias(groupId);

        Group group = checkGroupParameter(groupId, user.getId(), "deleteGroup");
        if(!user.getId().equals(group.getCreatorUid()) && !isAdmin(user.getId(), groupId)) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                    "Forbidden");
        }
        group.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        if(group.getStatus().equals(GroupAdminStatus.INACTIVE.getCode())) {
            throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, GroupServiceErrorCode.ERROR_GROUP_NOT_FOUND, 
                    "Unable to find the group");
            }
        
        List<Post> groupPost = new ArrayList<Post>();
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhForumPosts.class), null, 
                ( context,  reducingContext) -> {
                    List<Post> post = context.select().from(Tables.EH_FORUM_POSTS)
                            .where(Tables.EH_FORUM_POSTS.FORUM_ID.eq(group.getIntegralTag4()))
                            .fetch().map((r) -> {
                                           return ConvertHelper.convert(r, Post.class);
                            });
                    groupPost.addAll(post);
                        return true;
                });
            
        for(Post post : groupPost){
            forumService.deletePost(group.getIntegralTag4(), post.getId(), false);
        }
        
        group.setStatus(GroupAdminStatus.INACTIVE.getCode());
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_GROUP.getCode()).enter(()-> {
            this.groupProvider.updateGroup(group);
            return null;
        });
        
        this.groupSearcher.deleteById(groupId);
        
        //TODO 如果圈很大怎么办？
        List<Long> members = new ArrayList<Long>();
        List<Long> admins = getGroupAdminIncludeList(group.getId(), user.getId(), null);

        List<GroupMember> groupMember = groupProvider.findGroupMemberByGroupId(groupId);
        for(int i = 0; i < groupMember.size(); i++ ){
            GroupMember gm =  groupMember.get(i);
            gm.setMemberStatus(GroupMemberStatus.INACTIVE.getCode());
            members.add(gm.getMemberId());

//            if(gm.getMemberId().equals(user.getId())) {
//                if(gm.getMemberNickName() != null && !gm.getMemberNickName().isEmpty()) {
//                    nickName = gm.getMemberNickName();
//                    }
//            } else {
//                if(RoleConstants.ResourceCreator != gm.getMemberRole().longValue() && RoleConstants.ResourceAdmin != gm.getMemberRole().longValue()) {
//                    members.add(gm.getMemberId());
//                }
//            }
            groupProvider.updateGroupMember(gm);
        }

  
        //Send message to all other admins
        String locale = user.getLocale();
//        sendNotificationToAdmin(admins, nickName, group, locale);
//
//        //Send message to creator
//        sendNotificationToCreator(user.getId(), nickName, group, locale);
        
        sendNotifactionToMembers(members, alias, group, locale);

        // 删除group事件
        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setNamespaceId(group.getNamespaceId());
            context.setUid(group.getCreatorUid());
            event.setContext(context);

            event.setEntityType(EntityType.GROUP.getCode());
            event.setEntityId(groupId);
            event.setEventName(SystemEvent.GROUP_GROUP_DELETE.dft());

            event.addParam("group", StringHelper.toJsonString(group));
            GroupMember member = groupProvider.findGroupMemberByMemberInfo(groupId, EhUsers.class.getSimpleName(), user.getId());
            if (member != null) {
                event.addParam("member", StringHelper.toJsonString(member));
            }
        });

        // 退出group事件
        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setNamespaceId(group.getNamespaceId());
            context.setUid(user.getId());
            event.setContext(context);

            GroupMember member = groupProvider.findGroupMemberByMemberInfo(groupId, EhUsers.class.getSimpleName(), user.getId());
            if (member != null) {
                event.setEntityType(EhGroupMembers.class.getSimpleName());
                event.setEntityId(member.getId());
                event.setEventName(SystemEvent.GROUP_GROUP_LEAVE.dft());

                event.addParam("group", StringHelper.toJsonString(group));
                event.addParam("member", StringHelper.toJsonString(member));
            }
        });
    }

    /**
     * 如果是转移权限的，则不是group表中的创建者
     */
    private boolean isAdmin(Long userId, long groupId) {
    	ListingLocator locator = new ListingLocator(groupId);
		List<GroupMember> list = groupProvider.queryGroupMembers(locator, 10, (loc, query)->{
									 Condition c1 = Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.ACTIVE.getCode());
									 Condition c2 = Tables.EH_GROUP_MEMBERS.MEMBER_TYPE.eq(EntityType.USER.getCode());
									 Condition c3 = Tables.EH_GROUP_MEMBERS.MEMBER_ROLE.eq(RoleConstants.ResourceCreator);
						             query.addConditions(c1, c2, c3);
						             return query;
								 });
		if (list != null && list.size() > 0) {
			for (GroupMember groupMember : list) {
				if(groupMember.getMemberId().longValue() == userId.longValue()){
					return true;
				}
			}
		}
    	
		return false;
	}

	//管理员删除与创建者删除不一样。
	@Override
	public void deleteGroup(long groupId) {
		User operator = UserContext.current().getUser();
        long operatorUid = operator.getId();
		
		Group group = checkGroupParameter(groupId, operatorUid, "deleteGroup");
		
		group.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		group.setStatus(GroupAdminStatus.INACTIVE.getCode());
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_GROUP.getCode()).enter(()-> {
            this.groupProvider.updateGroup(group);
            return null;
        });
       
       //sendNotificationToOperator(operator.getId(), operator.getNamespaceId())
       
//        groupMember.stream().map(gm->{
//        	gm.setMemberStatus(GroupMemberStatus.INACTIVE.getCode());
//        	groupProvider.updateGroupMember(gm);
//			return null;
//        });
        
        List<Post> groupPost = new ArrayList<Post>();
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhForumPosts.class), null, 
                ( context,  reducingContext) -> {
                	List<Post> post = context.select().from(Tables.EH_FORUM_POSTS)
                			.where(Tables.EH_FORUM_POSTS.FORUM_ID.eq(group.getIntegralTag4()))
                			.fetch().map((r) -> {
                		                   return ConvertHelper.convert(r, Post.class);
                			});
                	groupPost.addAll(post);
                		return true;	
                });
        	
        for(Post post : groupPost){
        	forumService.deletePost(group.getIntegralTag4(), post.getId());
        }
       
       
//        groupPost.stream().map(r->{
//        	r.setStatus(PostStatus.INACTIVE.getCode());
//        	forumProvider.updatePost(r);
//        	return null;
//        });

        if(LOGGER.isInfoEnabled()) {
        	LOGGER.info("delete a group, userId=" + operatorUid + ", groupId=" + group.getId());
        }

        // 删除group
        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setNamespaceId(group.getNamespaceId());
            context.setUid(operatorUid);
            event.setContext(context);

            event.setEntityType(EntityType.GROUP.getCode());
            event.setEntityId(groupId);
            event.setEventName(SystemEvent.GROUP_GROUP_DELETE.dft());

            event.addParam("group", StringHelper.toJsonString(group));
        });
    }

	@Override
	public SearchTopicAdminCommandResponse searchGroupTopics(
			SearchGroupTopicAdminCommand cmd) {
		PostAdminQueryFilter filter = new PostAdminQueryFilter();
        String keyword = cmd.getKeyword();
        if(!StringUtils.isEmpty(keyword)) {
            filter.addQueryTerm(PostAdminQueryFilter.TERM_CONTENT);
            filter.addQueryTerm(PostAdminQueryFilter.TERM_SUBJECT);
            filter.setQueryString(keyword);
        }
        
        List<String> phones = cmd.getSenderPhones();
        if(phones != null && phones.size() > 0) {
            filter.includeFilter(PostAdminQueryFilter.TERM_IDENTIFY, phones);
        }
        
        List<String> nickNames = cmd.getSenderNickNames();
        if(nickNames != null && nickNames.size() > 0) {
            filter.includeFilter(PostAdminQueryFilter.TERM_SENDERNAME, nickNames);
        }
        
        List<Long> forumId = new ArrayList<Long>();
        forumId.add(ForumConstants.SYSTEM_FORUM);
        forumId.add(ForumConstants.FEEDBACK_FORUM);
        filter.excludeFilter(PostAdminQueryFilter.TERM_FORUMID, forumId);
        
        List<Long> includeForumId = new ArrayList<Long>();
        if(!StringUtils.isEmpty(cmd.getForumId()))
        	includeForumId.add(cmd.getForumId());
        if(includeForumId != null && includeForumId.size() > 0){
        	filter.includeFilter(PostAdminQueryFilter.TERM_FORUMID, includeForumId);
        }
        
        List<Long> parentPostId = new ArrayList<Long>();
        parentPostId.add(0L);
        filter.includeFilter(PostAdminQueryFilter.TERM_PARENTPOSTID, parentPostId);
        
        Long startTime = cmd.getStartTime();
        if(startTime != null) {
            filter.dateFrom(new Date(startTime));
        }
        
        Long endTime = cmd.getEndTime();
        if(endTime != null) {
            filter.dateTo(new Date(endTime));
        }
        
        int pageNum = 0;
        if(cmd.getPageAnchor() != null) {
            pageNum = cmd.getPageAnchor().intValue();
        } else {
            pageNum = 0;
        }
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        filter.setPageInfo(pageNum, pageSize);
        ListPostCommandResponse queryResponse = postSearcher.query(filter);
        
        SearchTopicAdminCommandResponse response = new SearchTopicAdminCommandResponse();
        response.setKeywords(queryResponse.getKeywords());
        response.setNextPageAnchor(queryResponse.getNextPageAnchor());
        
        List<PostAdminDTO> adminPostList = new ArrayList<PostAdminDTO>();
        List<PostDTO> postList = queryResponse.getPosts();
        for(PostDTO post : postList) {
            try {
                PostDTO temp = forumService.getTopicById(post.getId(), cmd.getCommunityId(), false);
                PostAdminDTO adminPost = ConvertHelper.convert(temp, PostAdminDTO.class);
                UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(temp.getCreatorUid(),
                    IdentifierType.MOBILE.getCode());
                if(identifier != null) {
                    adminPost.setCreatorPhone(identifier.getIdentifierToken());
                }
                
                adminPostList.add(adminPost);
            } catch(Exception e) {
                LOGGER.error(e.toString());
            }
        }
        
        response.setPosts(adminPostList);
        return response;
	}
	
	//Add by Janson, Adapter for group members
	@Override
	public List<GroupMember> listMessageGroupMembers(ListingLocator locator, int pageSize) {
	    final long TIMEOUT = 1000*60*10;//10 minute
	    GroupMemberCaches cache = null;
	    
	    Integer namespaceId = UserContext.current().getNamespaceId();
	    Group group = this.groupProvider.findGroupById(locator.getEntityId());
	    if(null == group) {
	        //Check if group is null, then return empty
	        return new ArrayList<GroupMember>();
	    //modify by sfyan, 20160429
	    } else if (GroupDiscriminator.fromCode(group.getDiscriminator()) == GroupDiscriminator.ENTERPRISE) {
	        cache = this.organizationProvider.listGroupMessageMembers(namespaceId, group.getId(), pageSize);
	        long now = System.currentTimeMillis() - TIMEOUT;
	        if(cache.getSize() > 0 && now > cache.getTick()) {
	            //timeout
	            this.organizationProvider.evictGroupMessageMembers(namespaceId, group.getId(), pageSize);
	            cache = this.organizationProvider.listGroupMessageMembers(namespaceId, group.getId(), pageSize);
	        }
	        return cache.getMembers();
	    } else {
	        cache = this.groupProvider.listGroupMessageMembers(namespaceId, locator, pageSize);
	        long now = System.currentTimeMillis() - TIMEOUT;
	        if(cache.getSize() > 0 && now > cache.getTick()) {
                //timeout
                this.groupProvider.evictGroupMessageMembers(namespaceId, locator, pageSize);
                cache = this.groupProvider.listGroupMessageMembers(namespaceId, locator, pageSize);
            }
            return cache.getMembers();
	    }
	    
	}

//    @Override
//	public ListNearbyGroupCommandResponse listNearbyGroupsByScene(ListNearbyGroupBySceneCommand cmd) {
//        User user = UserContext.current().getUser();
//        Long userId = user.getId();
//        Integer namespaceId = UserContext.getCurrentNamespaceId();
//        SceneTokenDTO sceneTokenDto = userService.checkSceneToken(userId, cmd.getSceneToken());
//        
//        ListNearbyGroupCommandResponse resp = null;
//        Community community = null;
//        ListNearbyGroupCommand execCmd = null;
//        UserCurrentEntityType entityType = UserCurrentEntityType.fromCode(sceneTokenDto.getEntityType());
//        switch(entityType) {
//        case COMMUNITY_RESIDENTIAL:
//        case COMMUNITY_COMMERCIAL:
//        case COMMUNITY:
//            community = communityProvider.findCommunityById(sceneTokenDto.getEntityId());
//            execCmd = ConvertHelper.convert(cmd, ListNearbyGroupCommand.class);
//            execCmd.setCommunityId(community.getId());
//            execCmd.setNamespaceId(namespaceId);
//            resp = listNearbyGroups(execCmd);
//            break;
//        case FAMILY:
//            FamilyDTO family = familyProvider.getFamilyById(sceneTokenDto.getEntityId());
//            if(family != null) {
//                community = communityProvider.findCommunityById(family.getCommunityId());
//                execCmd = ConvertHelper.convert(cmd, ListNearbyGroupCommand.class);
//                execCmd.setCommunityId(community.getId());
//                execCmd.setNamespaceId(namespaceId);
//                resp = listNearbyGroups(execCmd);
//            } else {
//                if(LOGGER.isWarnEnabled()) {
//                    LOGGER.warn("Family not found, sceneToken=" + sceneTokenDto);
//                }
//            }
//            break;
//        case ORGANIZATION:
//            // 由于目前listNearbyGroups接口并没有实现跟经纬度有关的逻辑，故小区并没有用，而在管理员场景也没有小区，
//            // 故只按订阅人数查询所有兴趣圈  by lqs 20160419
//            execCmd = ConvertHelper.convert(cmd, ListNearbyGroupCommand.class);
//            execCmd.setNamespaceId(namespaceId);
//            resp = listNearbyGroups(execCmd);
//            break;
//        default:
//            LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneTokenDto);
//            break;
//        }
//        
//        return resp;
//	}
	
    @Override
    public ListNearbyGroupCommandResponse listNearbyGroupsByScene(ListNearbyGroupBySceneCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        SceneTokenDTO sceneTokenDto = userService.checkSceneToken(userId, cmd.getSceneToken());
        
        ListNearbyGroupCommandResponse resp = null;
        Community community = null;
        ListNearbyGroupCommand execCmd = null;
        SceneType sceneType = SceneType.fromCode(sceneTokenDto.getScene());
        switch(sceneType) {
        case DEFAULT:
        case PARK_TOURIST:
            community = communityProvider.findCommunityById(sceneTokenDto.getEntityId());
            execCmd = ConvertHelper.convert(cmd, ListNearbyGroupCommand.class);
            execCmd.setCommunityId(community.getId());
            execCmd.setNamespaceId(namespaceId);
            resp = listNearbyGroups(execCmd);
            break;
        case FAMILY:
            FamilyDTO family = familyProvider.getFamilyById(sceneTokenDto.getEntityId());
            if(family != null) {
                community = communityProvider.findCommunityById(family.getCommunityId());
                execCmd = ConvertHelper.convert(cmd, ListNearbyGroupCommand.class);
                execCmd.setCommunityId(community.getId());
                execCmd.setNamespaceId(namespaceId);
                resp = listNearbyGroups(execCmd);
            } else {
                if(LOGGER.isWarnEnabled()) {
                    LOGGER.warn("Family not found, sceneToken=" + sceneTokenDto);
                }
            }
            break;
        case PM_ADMIN:// 无小区ID
        case ENTERPRISE: // 增加两场景，与园区企业保持一致 by lqs 20160517
        case ENTERPRISE_NOAUTH: // 增加两场景，与园区企业保持一致 by lqs 20160517
            // 由于目前listNearbyGroups接口并没有实现跟经纬度有关的逻辑，故小区并没有用，而在管理员场景也没有小区，
            // 故只按订阅人数查询所有兴趣圈  by lqs 20160419
            execCmd = ConvertHelper.convert(cmd, ListNearbyGroupCommand.class);
            execCmd.setNamespaceId(namespaceId);
            resp = listNearbyGroups(execCmd);
            break;
        default:
            LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneTokenDto);
            break;
        }
        
        return resp;
    }

	@Override
	public void quitAndTransferPrivilege(QuitAndTransferPrivilegeCommand cmd) {
		User user = UserContext.current().getUser();
		Long userId = user.getId();
		Long groupId = cmd.getGroupId();
		Group group = groupProvider.findGroupById(groupId);
		if (group == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                    ErrorCodes.ERROR_INVALID_PARAMETER, "not exist group: userId="+userId+", groupId="+groupId);
		}
		GroupMember gm = checkTransferPrivilegeParameters(userId, groupId);
		
		//检查如果当前群只有创建者一个人了，则退出并转移权限时直接解散该群
		if (checkIfOnlyOneGroupMember(userId, groupId)) {
			deleteGroupByCreator(groupId);
		}else{
			coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_GROUP.getCode()+groupId).enter(()-> {
				dbProvider.execute(t->{
					//从本群中退出
					quitFromGroup(userId, gm);
					//转移权限
					GroupMember newCreator = transferPrivilege(userId, group);
					//发消息
					if (GroupDiscriminator.fromCode(group.getDiscriminator()) == GroupDiscriminator.GROUP && GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PRIVATE) {
						//退出群聊时发送消息
						//sendNotificationToOldCreator(gm, user);
						sendNotificationToNewCreator(newCreator, user.getLocale());
					}else {
						//退出俱乐部时发送消息，add by tt, 20161102
						sendNotificationToNewCreatorWhenTransferCreator(newCreator, user.getLocale());
						sendNotificationToOthersWhenTransferCreator(groupId, newCreator, user.getLocale());
					}
					
					return null;
				});
				return null;
			});
		}

        // 退出group事件
        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setNamespaceId(group.getNamespaceId());
            context.setUid(gm.getMemberId());
            event.setContext(context);

            event.setEntityType(EhGroupMembers.class.getSimpleName());
            event.setEntityId(gm.getId());
            event.setEventName(SystemEvent.GROUP_GROUP_LEAVE.dft());

            event.addParam("group", StringHelper.toJsonString(group));
            event.addParam("member", StringHelper.toJsonString(gm));
        });
	}

	private void sendNotificationToOldCreator(GroupMember gm, User user) {
		Group group = groupProvider.findGroupById(gm.getGroupId());
		String nickname = gm.getMemberNickName();
		if (StringUtils.isEmpty(nickname)) {
			nickname = user.getNickName();
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupName", group.getName());
       
        String scope = GroupNotificationTemplateCode.SCOPE;
        int code = GroupNotificationTemplateCode.GROUP_MEMBER_LEAVE_FOR_OPERATOR;
        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, user.getLocale(), map, "");
        sendMessageToUser(user.getId(), notifyTextForApplicant, null);
	}

	private void sendNotificationToNewCreator(GroupMember newCreator, String locale) {
		Group group = groupProvider.findGroupById(newCreator.getGroupId());
		String nickname = newCreator.getMemberNickName();
		if (StringUtils.isEmpty(nickname)) {
			User user = userProvider.findUserById(newCreator.getMemberId());
			nickname = user.getNickName();
		}
		
//		Map<String, Object> map = new HashMap<String, Object>();
//        map.put("groupName", group.getName());
       
        String scope = GroupAdminNotificationTemplateCode.SCOPE;
        int code = GroupAdminNotificationTemplateCode.GROUP_ADMINROLE_APPROVE_FOR_APPLICANT;
        String notifyTextForApplicant = localeStringService.getLocalizedString(GroupLocalStringCode.SCOPE, String.valueOf(GroupLocalStringCode.GROUP_BE_MANAGER), locale, "");
        //sendMessageToUser(newCreator.getMemberId(), notifyTextForApplicant, null);

        sendGroupNotificationToIncludeUser(group.getId(), newCreator.getMemberId(), notifyTextForApplicant);
	}

    private void sendNotificationForLeaveToCreator(Long creatorId, GroupMember leaveMember, String locale) {
        Group group = groupProvider.findGroupById(leaveMember.getGroupId());
        String nickname = leaveMember.getMemberNickName();
        if (StringUtils.isEmpty(nickname)) {
            User user = userProvider.findUserById(leaveMember.getMemberId());
            nickname = user.getNickName();
        }

		Map<String, Object> map = new HashMap<String, Object>();
        map.put("userName", nickname);

        String scope = GroupNotificationTemplateCode.SCOPE;
        int code = GroupNotificationTemplateCode.GROUP_MEMBER_LEAVE;

        //俱乐部和行业协会使用自己的文案
        if (GroupDiscriminator.GROUP == GroupDiscriminator.fromCode(group.getDiscriminator()) && GroupPrivacy.PUBLIC == GroupPrivacy.fromCode(group.getPrivateFlag())) {

            map.put("groupName", group.getName());
            if (ClubType.GUILD == ClubType.fromCode(group.getClubType())) {
                code = GroupNotificationTemplateCode.GROUP_MEMBER_LEAVE_FOR_GUILD;
            } else {
                code = GroupNotificationTemplateCode.GROUP_MEMBER_LEAVE_FOR_CLUB;
            }

            String notifyTextForAdmin = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
            List<Long> includeList = getGroupAdminIncludeList(group.getId(), leaveMember.getMemberId(), null);
            if(includeList.size() > 0) {
                sendGroupNotification(group.getId(), includeList, null, notifyTextForAdmin, null, null);
            }

            return;

        }

        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

        //发送会话内提示时间
//        Date now = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
//        String hhmm = dateFormat.format( now );
//        sendGroupNotificationToIncludeUser(group.getId(), creatorId, hhmm);

        //sendMessageToUser(newCreator.getMemberId(), notifyTextForApplicant, null);
        sendGroupNotificationToIncludeUser(group.getId(), creatorId, notifyTextForApplicant);
    }

    private void sendNotificationForUpdateName(Group group, String locale) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupName", group.getName());

        String scope = GroupNotificationTemplateCode.SCOPE;
        int code = GroupNotificationTemplateCode.GROUP_RENAME;
        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");

        //sendMessageToUser(newCreator.getMemberId(), notifyTextForApplicant, null);
        sendGroupNotification(group.getId(), null, null, notifyTextForApplicant, null, null);
    }

	private boolean checkIfOnlyOneGroupMember(Long userId, Long groupId) {
		ListingLocator locator = new ListingLocator(groupId);
		List<GroupMember> list = groupProvider.queryGroupMembers(locator, 2, (loc, query)->{
									 Condition c1 = Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.ACTIVE.getCode());
									 Condition c2 = Tables.EH_GROUP_MEMBERS.MEMBER_TYPE.eq(EntityType.USER.getCode());
						             query.addConditions(c1, c2);
						             return query;
								 });
		if (list != null && list.size() == 1) {
			return true;
		}
		
		return false;
	}

	private GroupMember transferPrivilege(Long userId, Group group) {
		GroupMember gm = groupProvider.findGroupMemberTopOne(group.getId());
		gm.setMemberRole(RoleConstants.ResourceCreator);
		gm.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		gm.setOperatorUid(userId);
		groupProvider.updateGroupMember(gm);
		
		// 重新查，否则成员数量不对，20161119
		group = this.groupProvider.findGroupById(gm.getGroupId());
		group.setCreatorUid(gm.getMemberId());
		groupProvider.updateGroup(group);
		
		return gm;
	}

	private void quitFromGroup(Long userId, GroupMember gm) {
		this.groupProvider.deleteGroupMember(gm);
        this.userProvider.deleteUserGroup(userId, gm.getGroupId());
        deleteUserGroupOpRequest(gm.getGroupId(), gm.getMemberId(), userId, "leave group");
        Group group = this.groupProvider.findGroupById(gm.getGroupId());
        long memberCount = group.getMemberCount() - 1;
        memberCount = (memberCount < 0) ? 0 : memberCount;
        group.setMemberCount(memberCount);
        this.groupProvider.updateGroup(group);
	}

	private GroupMember checkTransferPrivilegeParameters(Long userId, Long groupId) {
		//1.groupId不能为空
		if (groupId == null) {
			LOGGER.error("Invalid parameters, userId = "+userId+", groupId"+groupId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameters, userId = "+userId+", groupId"+groupId);
		}
		//2.userId是这个group的成员
		GroupMember gm = groupProvider.findGroupMemberByMemberInfo(groupId, EntityType.USER.getCode(), userId);
		if (gm == null) {
			LOGGER.error("group member not found, userId = "+userId+", groupId"+groupId);
			throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, 
					GroupServiceErrorCode.ERROR_GROUP_MEMBER_NOT_FOUND, "group member not found, userId = "+userId+", groupId"+groupId);
		}
		
		//3.userId是这个group的创建者
		if (gm.getMemberRole().longValue() != RoleConstants.ResourceCreator) {
			LOGGER.error("group member is not the creator, userId = "+userId+", groupId"+groupId);
			throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, 
					GroupServiceErrorCode.ERROR_GROUP_MEMBER_IS_NOT_CREATOR, "group member is not the creator, userId = "+userId+", groupId"+groupId);
		}
		
		return gm;
	}
    
	
	
	
	@Override
	public ListUserGroupPostResponse listUserGroupPost(ListUserGroupPostCommand cmd) {
		User user = UserContext.current().getUser();
		if (user == null || user.getId() == null || user.getId().longValue() == 0L) {
			return new ListUserGroupPostResponse();
		}

        //妹的，老客户有没有参数，满世界都要写这种判断 add by yanjun 20171107
        if(cmd.getClubType() == null){
            cmd.setClubType(ClubType.NORMAL.getCode());
        }
		
		List<GroupDTO> groupList = listUserRelatedGroups();

        groupList = groupList.stream().filter( r  -> ClubType.fromCode(r.getClubType()) == ClubType.fromCode(cmd.getClubType())).collect(Collectors.toList());

		List<Long> forumIdList = groupList.stream().map(g->g.getOwningForumId()).collect(Collectors.toList());
		
		ListUserGroupPostResponse response = forumService.listUserGroupPost(VisibilityScope.COMMUNITY, 0L, forumIdList, user.getId(), cmd.getPageAnchor(), cmd.getPageSize());
		
		return response;
	}

	@Override
	public void transferCreatorPrivilege(TransferCreatorPrivilegeCommand cmd) {
		User user = UserContext.current().getUser();
		Long userId = user.getId();
		Long groupId = cmd.getGroupId();
		Group group = checkGroupExists(userId, groupId);
		GroupMember oldCreator = checkTransferPrivilegeParameters(userId, groupId);
		GroupMember newCreator = checkGroupMemberExists(cmd.getUserId(), groupId);
		
		dbProvider.execute(t->{
			//转移权限
			transferPrivilegeToUser(oldCreator, newCreator, group);
			//发消息
			sendNotificationToNewCreatorWhenTransferCreator(newCreator, user.getLocale());
			sendNotificationToOthersWhenTransferCreator(groupId, newCreator, user.getLocale());
			
			return null;
		});
	}

	private GroupMember checkGroupMemberExists(Long userId, Long groupId) {
		GroupMember newCreator = groupProvider.findGroupMemberByMemberInfo(groupId, EhUsers.class.getSimpleName(), userId);
		if (newCreator == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
	                ErrorCodes.ERROR_INVALID_PARAMETER, "not exist group member, userId = "+userId+", groupId"+groupId);
		}
		return newCreator;
	}

	//发给除了新创建者以外的所有成员
	private void sendNotificationToOthersWhenTransferCreator(Long groupId, GroupMember newCreator, String locale) {
		Group group = groupProvider.findGroupById(groupId);
		String nickname = newCreator.getMemberNickName();
		if (StringUtils.isEmpty(nickname)) {
			User user = userProvider.findUserById(newCreator.getMemberId());
			nickname = user.getNickName();
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupName", group.getName());
        map.put("newCreator", nickname);
       
        String scope = GroupNotificationTemplateCode.SCOPE;
        int code = GroupNotificationTemplateCode.GROUP_MEMBER_TRANSFER_CREATOR_TO_OTHERS;  //${newCreator}已成为“${groupName}”的创建者
        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        
        List<GroupMember> groupMemberList = listMessageGroupMembers(new ListingLocator(groupId), 10000);
        groupMemberList.forEach(gm->{
			if(gm.getMemberId().longValue() != newCreator.getMemberId().longValue()){
				sendMessageToUser(gm.getMemberId(), notifyTextForApplicant, null);
			}
        });
	}

	//发给新创建者
	private void sendNotificationToNewCreatorWhenTransferCreator(GroupMember newCreator, String locale) {
		Group group = groupProvider.findGroupById(newCreator.getGroupId());
		
		Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupName", group.getName());
       
        String scope = GroupNotificationTemplateCode.SCOPE;
        int code = GroupNotificationTemplateCode.GROUP_MEMBER_TRANSFER_CREATOR_TO_NEW_CREATOR;  //你已成为“${groupName}”的创建者
        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendMessageToUser(newCreator.getMemberId(), notifyTextForApplicant, null);
	}

	private void transferPrivilegeToUser(GroupMember fromUser, GroupMember toUser, Group group) {
		//把创建者自己变成普通用户
		fromUser.setMemberRole(RoleConstants.ResourceUser);
		fromUser.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		fromUser.setOperatorUid(fromUser.getMemberId());
		groupProvider.updateGroupMember(fromUser);
		
		//修改group的创建者为新创建者
		group.setCreatorUid(toUser.getMemberId());
		group.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		groupProvider.updateGroup(group);
		
		//修改原普通成员为创建者
		toUser.setMemberRole(RoleConstants.ResourceCreator);
		toUser.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		toUser.setOperatorUid(fromUser.getMemberId());
		groupProvider.updateGroupMember(toUser);
	}

    //敏感词检测  --add by yanlong.liang 20180614
    private void filter(CreateBroadcastCommand cmd, Long ownerId) {
        List<String> textList = new ArrayList<>();
        FilterWordsCommand command = new FilterWordsCommand();
        Group group = groupProvider.findGroupById(ownerId);
        Byte moduleType = (byte)4;
        if (group != null) {
            if (group.getClubType() ==ClubType.GUILD.getCode()) {
                moduleType = (byte)5;
            }
        }
        command.setModuleType(moduleType);
        if (!StringUtils.isEmpty(cmd.getTitle())) {
            textList.add(cmd.getTitle());
        }
        if (!StringUtils.isEmpty(cmd.getContent())) {
            textList.add(cmd.getContent());
        }
        command.setTextList(textList);
        command.setCommunityId(cmd.getCommunityId());
        this.sensitiveWordService.filterWords(command);
    }


	@Override
	public CreateBroadcastResponse createBroadcast(CreateBroadcastCommand cmd) {
        filter(cmd,cmd.getOwnerId());
		User user = UserContext.current().getUser();
		Long userId = user.getId();
		checkCreateBroadcastParameters(userId, cmd);
		checkCreateBroadcastPrivilege(userId, cmd.getOwnerType(), cmd.getOwnerId());
		
		Broadcast broadcast = new Broadcast();
		broadcast.setNamespaceId(UserContext.getCurrentNamespaceId());
		broadcast.setOwnerType(cmd.getOwnerType());
		broadcast.setOwnerId(cmd.getOwnerId());
		broadcast.setTitle(cmd.getTitle());
		broadcast.setContentType(cmd.getContentType());
		broadcast.setContent(cmd.getContent());
		broadcast.setContentAbstract(cmd.getContentAbstract());
		broadcast.setCreatorUid(userId);
		broadcast.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		broadcast.setUpdateTime(broadcast.getCreateTime());
		broadcast.setOperatorUid(userId);
		broadcastProvider.createBroadcast(broadcast);
		
		return new CreateBroadcastResponse(toBroadcastDTO(broadcast));
	}

	private void checkCreateBroadcastPrivilege(Long userId, String ownerType, Long ownerId) {
		BroadcastOwnerType broadcastOwnerType = BroadcastOwnerType.fromCode(ownerType);
		switch (broadcastOwnerType) {
		case GROUP:
			Group group = groupProvider.findGroupById(ownerId);
			if (group == null) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"not exist group!");
			}
			Integer remainCount = getRemainBroadcastCount(group.getNamespaceId(), group.getId(), group.getClubType());
			if (remainCount <= 0) {
				throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, GroupServiceErrorCode.ERROR_GROUP_BEYOND_BROADCAST_COUNT,
						"beyond avalable count!");
			}
			
			break;

		default:
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"owner type error!");
		}
	}

	private void checkCreateBroadcastParameters(Long userId, CreateBroadcastCommand cmd) {
		if (cmd.getOwnerId() == null || BroadcastOwnerType.fromCode(cmd.getOwnerType()) == null || StringUtils.isEmpty(cmd.getTitle()) 
				|| StringUtils.isEmpty(cmd.getContent()) || StringUtils.isEmpty(cmd.getContentType())) {
			LOGGER.error("Invalid parameters, operatorId=" + userId + ", cmd=" + cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters");
		}
		if (cmd.getTitle().length() > 30) {
			throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, GroupServiceErrorCode.ERROR_BROADCAST_TITLE_LENGTH,
					"title length cannot be greater than 30!");
		}
		if (cmd.getContent().length() > 200) {
			throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, GroupServiceErrorCode.ERROR_BROADCAST_CONTENT_LENGTH,
					"content length cannot be greater than 200");
		}
	}

	@Override
	public GetBroadcastByTokenResponse getBroadcastByToken(GetBroadcastByTokenCommand cmd) {
		if (StringUtils.isEmpty(cmd.getBroadcastToken())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters");
		}
		Long id = WebTokenGenerator.getInstance().fromWebToken(cmd.getBroadcastToken(), Long.class);
		
		Broadcast broadcast = broadcastProvider.findBroadcastById(id);
		
		return new GetBroadcastByTokenResponse(toBroadcastDTO(broadcast));
	}

	private String getUserName(Long userId) {
		if (userId != null) {
			User user = userProvider.findUserById(userId);
			if (user != null) {
				return user.getNickName();
			}
		}
		return "";
	}
	
	@Override
	public ListBroadcastsResponse listBroadcasts(ListBroadcastsCommand cmd) {
		if (BroadcastOwnerType.fromCode(cmd.getOwnerType()) == null || cmd.getOwnerId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters");
		}
		
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		
		List<Broadcast> broadcastList = broadcastProvider.listBroadcastByOwner(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getPageAnchor(), pageSize+1);
		Integer count = broadcastProvider.countBroadcastByOwner(cmd.getOwnerType(), cmd.getOwnerId());
		Long nextPageAnchor = null;
		if (broadcastList != null && broadcastList.size() > pageSize) {
			broadcastList.remove(broadcastList.size()-1);
			nextPageAnchor = broadcastList.get(broadcastList.size()-1).getId();
		}
		
		List<BroadcastDTO> resultList = broadcastList.stream().map(b->{
			return toBroadcastDTO(b);
		}).collect(Collectors.toList());
		
		return new ListBroadcastsResponse(resultList, nextPageAnchor, count);
	}
	
	private BroadcastDTO toBroadcastDTO(Broadcast broadcast){
		BroadcastDTO result = ConvertHelper.convert(broadcast, BroadcastDTO.class);
		result.setBroadcastToken(WebTokenGenerator.getInstance().toWebToken(broadcast.getId()));
		result.setCreatorName(getUserName(broadcast.getCreatorUid()));
		return result;
	}

	@Override
	public GroupParametersResponse setGroupParameters(SetGroupParametersCommand cmd) {
		if (cmd.getNamespaceId() == null || TrueOrFalseFlag.fromCode(cmd.getCreateFlag()) == null || TrueOrFalseFlag.fromCode(cmd.getVerifyFlag()) == null
				|| TrueOrFalseFlag.fromCode(cmd.getMemberPostFlag()) == null || TrueOrFalseFlag.fromCode(cmd.getMemberCommentFlag()) == null
				|| TrueOrFalseFlag.fromCode(cmd.getAdminBroadcastFlag()) == null || cmd.getBroadcastCount() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters");
		}
		
		GroupSetting groupSetting = ConvertHelper.convert(cmd, GroupSetting.class);
		groupSetting.setNamespaceId(cmd.getNamespaceId());
		groupSetting.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		groupSetting.setCreatorUid(UserContext.current().getUser().getId());
		groupSetting.setUpdateTime(groupSetting.getCreateTime());
		groupSetting.setOperatorUid(groupSetting.getCreatorUid());

		//妹的，老客户有没有参数，满世界都要写这种判断 add by yanjun 20171107
		if(cmd.getClubType() == null){
		    cmd.setClubType(ClubType.NORMAL.getCode());
        }
		GroupSetting old = null;
		if ((old = getGroupSetting(cmd.getNamespaceId(), cmd.getClubType())) == null) {
			groupSettingProvider.createGroupSetting(groupSetting);
		}else {
			groupSetting.setCreateTime(old.getCreateTime());
			groupSetting.setCreatorUid(old.getCreatorUid());
			groupSetting.setId(old.getId());
			groupSettingProvider.updateGroupSetting(groupSetting);
		}


		//原来的MemberCommentFlag字段在客户端是废弃的。现在将是否允许评论放到一个统一的表里，在查询帖子时从帖子层面控制是否允许评论。add by yanjun 20171206
        Byte forumModuleType = ForumModuleType.CLUB.getCode();
        if(ClubType.fromCode(cmd.getClubType()) == ClubType.GUILD){
            forumModuleType = ForumModuleType.GUILD.getCode();
        }
        forumService.saveInteractSetting(cmd.getNamespaceId(), forumModuleType, 0L, cmd.getMemberCommentFlag());
		
		return ConvertHelper.convert(groupSetting, GroupParametersResponse.class);
	}



	@Override
	public GroupParametersResponse getGroupParameters(GetGroupParametersCommand cmd) {
		if (cmd.getNamespaceId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters");
		}

        //妹的，老客户有没有参数，满世界都要写这种判断 add by yanjun 20171107
        if(cmd.getClubType() == null){
            cmd.setClubType(ClubType.NORMAL.getCode());
        }
		return getGroupParameters(cmd.getNamespaceId(), cmd.getClubType());
	}

	private GroupSetting getGroupSetting(Integer namespaceId, Byte clubType){
		return groupSettingProvider.findGroupSettingByNamespaceId(namespaceId, clubType);
	}
	
	private GroupParametersResponse getGroupParameters(Integer namespaceId, Byte clubType) {
		GroupSetting groupSetting = getGroupSetting(namespaceId, clubType);
		if (groupSetting == null) {
			return new GroupParametersResponse(namespaceId, TrueOrFalseFlag.TRUE.getCode(), TrueOrFalseFlag.TRUE.getCode(), TrueOrFalseFlag.TRUE.getCode(), TrueOrFalseFlag.TRUE.getCode(), TrueOrFalseFlag.TRUE.getCode(), 1, clubType);
		}
		return ConvertHelper.convert(groupSetting, GroupParametersResponse.class);
	}
	
	@Override
	public CreateGroupCategoryResponse createGroupCategory(CreateGroupCategoryCommand cmd) {
		User user = UserContext.current().getUser();
		Long userId = user.getId();
		checkGroupCategoryParameters(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getCategoryName());
		checkDuplicationGroupCategoryName(cmd.getNamespaceId(), cmd.getCategoryName(), null);
		
		Category parentCategory = categoryProvider.findCategoryById(2L);
		String prefix = "";
		if (parentCategory != null) {
			prefix = parentCategory.getName()+"/";
		}
		Category category = new Category();
		category.setParentId(2L);
		category.setLinkId(0L);
		category.setName(cmd.getCategoryName());
		category.setPath(prefix+cmd.getCategoryName());
		category.setDefaultOrder(0);
		category.setStatus(CategoryAdminStatus.ACTIVE.getCode());
		category.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		category.setNamespaceId(cmd.getNamespaceId());
		categoryProvider.createCategory(category);
		
		return new CreateGroupCategoryResponse(toCategoryDTO(category));
	}

	private CategoryDTO toCategoryDTO(Category category) {
		CategoryDTO result = new CategoryDTO();
		result.setCategoryId(category.getId());
		result.setCategoryName(category.getName());
		result.setNamespaceId(category.getNamespaceId());
		return result;
	}

	private Category checkDuplicationGroupCategoryName(Integer namespaceId, String categoryName, Long id) {
		Category category = categoryProvider.findCategoryByNamespaceAndName(2L, namespaceId,null,null, categoryName);
		if (category != null && (id == null || id.longValue() != category.getId().longValue())) {
			throw RuntimeErrorException.errorWith(GroupServiceErrorCode.SCOPE, GroupServiceErrorCode.ERROR_GROUP_CATEGORY_NAME_EXIST,
					"exist name, name="+categoryName);
		}
		return category;
	}

	private void checkGroupCategoryParameters(Long userId, Integer namespaceId, String ownerType, Long ownerId,
			String categoryName) {
		if (namespaceId == null || GroupCategoryOwnerType.fromCode(ownerType) == null || ownerId == null
				|| StringUtils.isEmpty(categoryName)) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters");
		}
	}

	@Override
	public UpdateGroupCategoryResponse updateGroupCategory(UpdateGroupCategoryCommand cmd) {
		User user = UserContext.current().getUser();
		Long userId = user.getId();
		checkGroupCategoryParameters(userId, cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getCategoryName());
		checkDuplicationGroupCategoryName(cmd.getNamespaceId(), cmd.getCategoryName(), cmd.getCategoryId());
		Category category = categoryProvider.findCategoryById(cmd.getCategoryId());
		if (category == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"not exist category");
		}
		
		Category parentCategory = categoryProvider.findCategoryById(category.getParentId());
		String prefix = "";
		if (parentCategory != null) {
			prefix = parentCategory.getName()+"/";
		}
		category.setName(cmd.getCategoryName());
		category.setPath(prefix+cmd.getCategoryName());
		categoryProvider.updateCategory(category);
		
		return new UpdateGroupCategoryResponse(toCategoryDTO(category));
	}

	@Override
	public void deleteGroupCategory(DeleteGroupCategoryCommand cmd) {
		if (cmd.getNamespaceId() == null || GroupCategoryOwnerType.fromCode(cmd.getOwnerType()) == null || cmd.getOwnerId() == null
				|| cmd.getCategoryId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters");
		}
		
		Category category = categoryProvider.findCategoryById(cmd.getCategoryId());
		if (category == null || category.getStatus().byteValue() != CategoryAdminStatus.ACTIVE.getCode()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"not exist category");
		}
		
		categoryProvider.deleteCategory(category);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListGroupCategoriesResponse listGroupCategories(ListGroupCategoriesCommand cmd) {
		if (cmd.getNamespaceId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters");
		}
		
		List<Category> categoryList = categoryProvider.listChildCategories(cmd.getNamespaceId(), 2L, CategoryAdminStatus.ACTIVE
				, new Tuple<String, SortOrder>("id", SortOrder.ASC));
		
		List<CategoryDTO> resultList = categoryList.stream().map(c->toCategoryDTO(c)).collect(Collectors.toList());
		 
		return new ListGroupCategoriesResponse(resultList);
	}

	@Override
	public ListGroupsByApprovalStatusResponse listGroupsByApprovalStatus(ListGroupsByApprovalStatusCommand cmd) {
		if (cmd.getNamespaceId() == null || ApprovalStatus.fromCode(cmd.getApprovalStatus()) == null || PrivateFlag.fromCode(cmd.getPrivateFlag()) == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters");
		}

        //妹的，老客户有没有参数，满世界都要写这种判断 add by yanjun 20171107
        if(cmd.getClubType() == null){
            cmd.setClubType(ClubType.NORMAL.getCode());
        }
		
		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        
        List<Group> groups = this.groupProvider.queryGroups(locator, pageSize + 1, (loc, query)-> {
            query.addConditions(Tables.EH_GROUPS.NAMESPACE_ID.eq(cmd.getNamespaceId()));
            query.addConditions(Tables.EH_GROUPS.DISCRIMINATOR.eq(GroupDiscriminator.GROUP.getCode()));
           	query.addConditions(Tables.EH_GROUPS.PRIVATE_FLAG.eq(cmd.getPrivateFlag()));
           	
           	if (ApprovalStatus.fromCode(cmd.getApprovalStatus()) == ApprovalStatus.WAITING_FOR_APPROVING) {
           		query.addConditions(Tables.EH_GROUPS.APPROVAL_STATUS.eq(ApprovalStatus.WAITING_FOR_APPROVING.getCode()));
           		query.addConditions(Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.INACTIVE.getCode()));
			}else if (ApprovalStatus.fromCode(cmd.getApprovalStatus()) == ApprovalStatus.AGREEMENT) {
           		query.addConditions(Tables.EH_GROUPS.APPROVAL_STATUS.eq(ApprovalStatus.AGREEMENT.getCode()));
           		query.addConditions(Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode()));
			}else {
				query.addConditions(Tables.EH_GROUPS.APPROVAL_STATUS.eq(ApprovalStatus.REJECTION.getCode()));
           		query.addConditions(Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.INACTIVE.getCode()));
			}

			query.addConditions(Tables.EH_GROUPS.CLUB_TYPE.eq(cmd.getClubType()));

            return query;
        });
        
        ListGroupsByApprovalStatusResponse response = new ListGroupsByApprovalStatusResponse();
        if(groups.size() > pageSize) {
            groups.remove(groups.size() - 1);
            response.setNextPageAnchor(groups.get(groups.size() - 1).getId());
        }
        response.setGroups(groups.stream().map((r)-> { 
            return toGroupDTO(UserContext.current().getUser().getId(), r); 
        }).collect(Collectors.toList()));
		
		return response;
	}

	@Override
	public void approvalGroupRequest(ApprovalGroupRequestCommand cmd) {
		User user = UserContext.current().getUser();
		Long userId = user.getId();
		Group group = checkGroupExists(userId, cmd.getGroupId());
		if (group.getStatus().byteValue() != GroupAdminStatus.INACTIVE.getCode() || group.getApprovalStatus() == null || group.getApprovalStatus().byteValue() != ApprovalStatus.WAITING_FOR_APPROVING.getCode()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
	                ErrorCodes.ERROR_INVALID_PARAMETER, "group status error, userId = "+userId+", groupId"+cmd.getGroupId());
		}
		
		dbProvider.execute((s)->{
			group.setStatus(GroupAdminStatus.ACTIVE.getCode());
			group.setApprovalStatus(ApprovalStatus.AGREEMENT.getCode());
			group.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			group.setOperatorUid(userId);
			groupProvider.updateGroup(group);
			
			sendNotificationToCreatorWhenApproval(group, user.getLocale(), GroupNotificationTemplateCode.GROUP_MEMBER_TO_CREATOR_WHEN_APPROVAL);  //你申请创建的“${groupName}”已通过
			return null;
		});

		// 发送推荐帖
        //不需要发送推荐帖
//         recommandGroup(toGroupDTO(group.getCreatorUid() ,group), VisibleRegionType.fromCode(group.getVisibleRegionType()), group.getVisibleRegionId());

        // 审核group成功事件
        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setNamespaceId(group.getNamespaceId());
            context.setUid(userId);
            event.setContext(context);

            event.setEntityType(EntityType.GROUP.getCode());
            event.setEntityId(group.getId());
            event.setEventName(SystemEvent.GROUP_GROUP_APPROVAL.dft());

            event.addParam("group", StringHelper.toJsonString(group));
        });
	}
	
	private void sendNotificationToCreatorWhenApproval(Group group, String locale, int code) {
		Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupName", group.getName());
       
        String scope = GroupNotificationTemplateCode.SCOPE;
        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendMessageToUser(group.getCreatorUid(), notifyTextForApplicant, null);
	}

	private Group checkGroupExists(Long userId, Long groupId) {
		//1.groupId不能为空
		if (groupId == null) {
			LOGGER.error("Invalid parameters, userId = "+userId+", groupId"+groupId);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
	                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameters, userId = "+userId+", groupId"+groupId);
		}
		
		Group group = groupProvider.findGroupById(groupId);
		if (group == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
	                ErrorCodes.ERROR_INVALID_PARAMETER, "not exist group, userId = "+userId+", groupId"+groupId);
		}
		
		return group;
	}

	@Override
	public void rejectGroupRequest(RejectGroupRequestCommand cmd) {
		User user = UserContext.current().getUser();
		Long userId = user.getId();
		Group group = checkGroupExists(userId, cmd.getGroupId());
		if (group.getStatus().byteValue() != GroupAdminStatus.INACTIVE.getCode() || group.getApprovalStatus() == null || group.getApprovalStatus().byteValue() != ApprovalStatus.WAITING_FOR_APPROVING.getCode()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
	                ErrorCodes.ERROR_INVALID_PARAMETER, "group status error, userId = "+userId+", groupId"+cmd.getGroupId());
		}
		
		dbProvider.execute((s)->{
			group.setStatus(GroupAdminStatus.INACTIVE.getCode());
			group.setApprovalStatus(ApprovalStatus.REJECTION.getCode());
			group.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			group.setOperatorUid(userId);
			groupProvider.updateGroup(group);
			
			sendNotificationToCreatorWhenApproval(group, user.getLocale(), GroupNotificationTemplateCode.GROUP_MEMBER_TO_CREATOR_WHEN_REJECTED);
			return null;
		});

	}

	@Override
	public GetClubPlaceholderNameResponse getClubPlaceholderName(GetClubPlaceholderNameCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId();
		if (namespaceId == null) {
			namespaceId = 0;
		}
		return new GetClubPlaceholderNameResponse(getClubPlaceholderName(namespaceId));
	}
	
	private String getClubPlaceholderName(Integer namespaceId){
		String clubPlaceholderName = configProvider.getValue(namespaceId, ConfigConstants.CLUB_PLACEHOLDER_NAME, "");
		if (StringUtils.isEmpty(clubPlaceholderName)) {
			clubPlaceholderName = "俱乐部";
		}
		return clubPlaceholderName;
	}

	@Override
	public GetRemainBroadcastCountResponse getRemainBroadcastCount(GetRemainBroadcastCountCommand cmd) {
		Long userId = UserContext.current().getUser().getId();
		if (cmd.getNamespaceId() == null || cmd.getGroupId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
	                ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters, groupId"+cmd.getGroupId());
		}
		checkGroupExists(userId, cmd.getGroupId());

		Group group = groupProvider.findGroupById(cmd.getGroupId());
		
		return new GetRemainBroadcastCountResponse(getRemainBroadcastCount(cmd.getNamespaceId(), cmd.getGroupId(), ClubType.fromCode(group.getClubType()).getCode()));
	}

	private Integer getRemainBroadcastCount(Integer namespaceId, Long groupId, Byte clubType) {
		Integer availableCount = getGroupParameters(namespaceId, clubType).getBroadcastCount();
		
		Integer usedCount = broadcastProvider.selectBroadcastCountToday(namespaceId, BroadcastOwnerType.GROUP.getCode(), groupId);
		
		return availableCount >= usedCount ? availableCount - usedCount : 0;
	}

	@Override
	public GetShareInfoResponse getShareInfo(GetShareInfoCommand cmd) {
		if (cmd.getNamespaceId() == null || cmd.getGroupId() == null || StringUtils.isEmpty(cmd.getRealm())) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
	                ErrorCodes.ERROR_INVALID_PARAMETER, "invalid parameters, cmd"+cmd);
		}
		GetShareInfoResponse response = new GetShareInfoResponse();
//		VersionInfoDTO versionInfoDTO = versionService.getVersionInfo(cmd.getRealm());
//		response.setAppName(versionInfoDTO.getAppName());
//		response.setAppIconUrl(versionInfoDTO.getIconUrl());
//		response.setDownloadUrl(versionInfoDTO.getDownloadUrl());
		// 熊颖之前有个接口可以获取app名称和图标, update by tt, 20161115
		AppUrlDTO appUrlDTO = appUrlService.getAppInfo(new GetAppInfoCommand(cmd.getNamespaceId(),OSType.Android.getCode()));
		response.setAppName(appUrlDTO.getName());
		response.setAppIconUrl(appUrlDTO.getLogoUrl());
		response.setDownloadUrl(appUrlDTO.getDownloadUrl());
		response.setAppDescription(appUrlDTO.getDescription());
		
		Group group = groupProvider.findGroupById(cmd.getGroupId());
		response.setGroupName(group.getName());
		response.setGroupDescription(group.getDescription());
		response.setGroupAvatarUrl(getUrl(group.getAvatar()));
		
        ListingLocator locator = new ListingLocator(group.getId());
        List<GroupMember> groupMembers = groupProvider.listGroupMembers(locator, 10000);
        List<String> groupMemberAvatarList = new ArrayList<>();
        if (groupMembers != null) {
			for (GroupMember gm : groupMembers) {
				User user = userProvider.findUserById(gm.getMemberId());
				String memberAvatar = getUrl(user.getAvatar());
				groupMemberAvatarList.add(memberAvatar);
			}
		}
		response.setGroupMemberAvatarList(groupMemberAvatarList);
		
		response.setClubPlaceholderName(getClubPlaceholderName(cmd.getNamespaceId()));
		
		return response;
	}
	
	private String getUrl(String uri){
		if (StringUtils.isEmpty(uri)) {
			return "";
		}
		try{
            String url = contentServerService.parserUri(uri, EntityType.GROUP.getCode(), null);
            return url;
        }catch(Exception e){
        }
		return "";
	}

	@Override
	public void cancelGroupRequest(CancelGroupRequestCommand cmd) {
		User user = UserContext.current().getUser();
		Long userId = user.getId();
		Group group = checkGroupExists(userId, cmd.getGroupId());
		if (group.getStatus().byteValue() != GroupAdminStatus.INACTIVE.getCode() || group.getApprovalStatus() == null || group.getApprovalStatus().byteValue() != ApprovalStatus.WAITING_FOR_APPROVING.getCode()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
	                ErrorCodes.ERROR_INVALID_PARAMETER, "group status error, userId = "+userId+", groupId"+cmd.getGroupId());
		}
		if (userId == null || userId.longValue() != group.getCreatorUid().longValue()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
	                ErrorCodes.ERROR_INVALID_PARAMETER, "error user: userId="+userId+", groupId"+cmd.getGroupId());
		}
		
		dbProvider.execute((s)->{
			group.setStatus(GroupAdminStatus.INACTIVE.getCode());
			group.setApprovalStatus(null);
			group.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			group.setDeleteTime(group.getUpdateTime());
			group.setOperatorUid(userId);
			groupProvider.updateGroup(group);
			return null;
		});
	}

	@Override
	public GroupDTO createBusinessGroup(String groupName) {
		CreateGroupCommand cmd = new CreateGroupCommand();
		cmd.setName(groupName);
		cmd.setPrivateFlag(GroupPrivacy.PRIVATE.getCode());
		cmd.setJoinPolicy(GroupJoinPolicy.FREE.getCode());
		
		return createGroup(cmd, null);
	}

	@Override
	public void joinBusinessGroup(Long groupId) {
		RequestToJoinGroupCommand cmd = new RequestToJoinGroupCommand();
		cmd.setGroupId(groupId);
		createGroupMember(cmd, false);
	}

    @Override
    public void deleteBroadcastByToken(DeleteBroadcastByTokenCommand cmd) {
        if (StringUtils.isEmpty(cmd.getBroadcastToken())) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameters");
        }
        Long id = WebTokenGenerator.getInstance().fromWebToken(cmd.getBroadcastToken(), Long.class);

        Broadcast broadcast = broadcastProvider.findBroadcastById(id);
        if (broadcast != null) {
            broadcastProvider.deleteBroadcast(broadcast);
        }
    }

    @Override
    public GuildApplyDTO findGuildApply(FindGuildApplyCommand cmd) {
        GuildApply guildApply = groupProvider.findGuildApplyById(cmd.getId());
        GuildApplyDTO dto = ConvertHelper.convert(guildApply, GuildApplyDTO.class);
        populateGuildApplyDTO(dto);
        return dto;
    }

    @Override
    public GuildApplyDTO findGuildApplyByGroupMemberId(FindGuildApplyByGroupMemberIdCommand cmd) {
        GuildApply guildApply = groupProvider.findGuildApplyByGroupMemberId(cmd.getGroupMemberId());
        return ConvertHelper.convert(guildApply, GuildApplyDTO.class);
    }

    @Override
    public IndustryTypeDTO findIndustryType(FindIndustryTypeCommand cmd) {
        IndustryType industryType = groupProvider.findIndustryTypeById(cmd.getId());
        return ConvertHelper.convert(industryType, IndustryTypeDTO.class);
    }

    @Override
    public ListIndustryTypesResponse listIndustryTypes(ListIndustryTypesCommand cmd) {
        ListIndustryTypesResponse response = new ListIndustryTypesResponse();
        List<IndustryType> list = groupProvider.listIndustryTypes(cmd.getNamespaceId());
        if(list != null){
            List<IndustryTypeDTO> dtos =  list.stream().map(r-> ConvertHelper.convert(r, IndustryTypeDTO.class))
                    .collect(Collectors.toList());
            response.setDtos(dtos);
        }
        return response;
    }

    @Override
    public ListGuildAppliesResponse listGuildApplies(ListGuildAppliesCommand cmd) {
        ListGuildAppliesResponse response = new ListGuildAppliesResponse();

        List<GuildApply> list = groupProvider.listGuildApplies(cmd.getNamespaceId(), cmd.getGroupId(), cmd.getApplicantUid());
        if(list != null){
            List<GuildApplyDTO> dtos =  list.stream().map(r-> {
                GuildApplyDTO dto = ConvertHelper.convert(r, GuildApplyDTO.class);
                populateGuildApplyDTO(dto);
                return dto;
            }).collect(Collectors.toList());
            response.setDtos(dtos);
        }
        return response;
    }

    private void populateGuildApplyDTO(GuildApplyDTO dto) {
	    if(dto == null){
	        return;
        }
        if(dto.getAvatar() != null){

            Long userId = UserContext.currentUserId();
	        if(userId == null){
	            userId = dto.getApplicantUid();
            }
            String url = contentServerService.parserUri(dto.getAvatar(), EntityType.USER.getCode(), userId);
            dto.setAvatarUrl(url);
        }

        Group group = groupProvider.findGroupById(dto.getGroupId());

        if(group != null){
            dto.setGroupName(group.getName());
        }

    }







}
