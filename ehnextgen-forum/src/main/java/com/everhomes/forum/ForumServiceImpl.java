// @formatter:off

package com.everhomes.forum;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everhomes.acl.AclProvider;
import com.everhomes.acl.Privilege;
import com.everhomes.acl.ResourceUserRoleResolver;
import com.everhomes.acl.Role;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerResource;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.Family;
import com.everhomes.family.FamilyProvider;
import com.everhomes.group.Group;
import com.everhomes.group.GroupDiscriminator;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.point.PointType;
import com.everhomes.point.UserPointService;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.address.CommunityAdminStatus;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.category.CategoryConstants;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.forum.AssignTopicScopeCommand;
import com.everhomes.rest.forum.AssignedScopeDTO;
import com.everhomes.rest.forum.AttachmentDTO;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.forum.CancelLikeTopicCommand;
import com.everhomes.rest.forum.CheckUserPostCommand;
import com.everhomes.rest.forum.CheckUserPostDTO;
import com.everhomes.rest.forum.CheckUserPostStatus;
import com.everhomes.rest.forum.ForumAssignedScopeCode;
import com.everhomes.rest.forum.ForumConstants;
import com.everhomes.rest.forum.ForumLocalStringCode;
import com.everhomes.rest.forum.ForumNotificationTemplateCode;
import com.everhomes.rest.forum.ForumServiceErrorCode;
import com.everhomes.rest.forum.FreeStuffCommand;
import com.everhomes.rest.forum.FreeStuffStatus;
import com.everhomes.rest.forum.GetTopicCommand;
import com.everhomes.rest.forum.LikeTopicCommand;
import com.everhomes.rest.forum.ListActivityTopicByCategoryAndTagCommand;
import com.everhomes.rest.forum.ListPostCommandResponse;
import com.everhomes.rest.forum.ListTopicAssignedScopeCommand;
import com.everhomes.rest.forum.ListTopicCommand;
import com.everhomes.rest.forum.ListTopicCommentCommand;
import com.everhomes.rest.forum.ListUserRelatedTopicCommand;
import com.everhomes.rest.forum.LostAndFoundCommand;
import com.everhomes.rest.forum.LostAndFoundStatus;
import com.everhomes.rest.forum.NewCommentCommand;
import com.everhomes.rest.forum.NewTopicCommand;
import com.everhomes.rest.forum.PostAssignedFlag;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.forum.PostEntityTag;
import com.everhomes.rest.forum.PostPrivacy;
import com.everhomes.rest.forum.PostStatus;
import com.everhomes.rest.forum.QueryOrganizationTopicCommand;
import com.everhomes.rest.forum.QueryTopicByCategoryCommand;
import com.everhomes.rest.forum.QueryTopicByEntityAndCategoryCommand;
import com.everhomes.rest.forum.UsedAndRentalCommand;
import com.everhomes.rest.forum.UsedAndRentalStatus;
import com.everhomes.rest.forum.admin.PostAdminDTO;
import com.everhomes.rest.forum.admin.SearchTopicAdminCommand;
import com.everhomes.rest.forum.admin.SearchTopicAdminCommandResponse;
import com.everhomes.rest.group.GroupNotificationTemplateCode;
import com.everhomes.rest.group.GroupPrivacy;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.point.AddUserPointCommand;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserLikeType;
import com.everhomes.rest.visibility.VisibilityScope;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.search.PostAdminQueryFilter;
import com.everhomes.search.PostSearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhUsers;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserGroup;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserLike;
import com.everhomes.user.UserProfileContstant;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.Tuple;

@Component
public class ForumServiceImpl implements ForumService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ForumServiceImpl.class);

    @Autowired
    private AclProvider aclProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private ForumProvider forumProvider;
    
    @Autowired
    private GroupProvider groupProvider;
    
    @Autowired
    private CategoryProvider categoryProvider;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private RegionProvider regionProvider;
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Autowired
    private ContentServerService contentServerService;
    
    @Autowired
    private PostSearcher postSearcher; 
    
    @Autowired
    private LocaleStringService localeStringService;
    
    @Autowired
    private UserPointService userPointService;
    
    @Autowired
    private CoordinationProvider coordinationProvider;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private OrganizationProvider organizationProvider;
    
    @Autowired
    private UserActivityProvider userActivityProvider;
    
    @Autowired
    private FamilyProvider familyProvider;
    
    @Autowired
    private HotPostService hotPostService;
    
    @Autowired
    private MessagingService messagingService;
    
    @Autowired
    private LocaleTemplateService localeTemplateService;
    
    @Override
    public boolean isSystemForum(long forumId) {
        return forumId == ForumConstants.SYSTEM_FORUM;
    }

    @Override
    public PostDTO createTopic(NewTopicCommand cmd) {
        //checkForumPostPrivilege(cmd.getForumId());
        long startTime = System.currentTimeMillis();
        
        User user = UserContext.current().getUser();
        Long userId = user.getId();
                
        Post post = processTopicCommand(userId, cmd);

        Long embededAppId = cmd.getEmbeddedAppId();
        ForumEmbeddedHandler handler = getForumEmbeddedHandler(embededAppId);
        if(handler != null) {
            handler.preProcessEmbeddedObject(post);
            forumProvider.createPost(post);
            handler.postProcessEmbeddedObject(post);
        } else {
            forumProvider.createPost(post);
        }

        // Save the attachments after the post is saved
        processPostAttachments(userId, cmd.getAttachments(), post);
        
        // Populate the result post the same as query
        Long communityId = null;
        if(VisibleRegionType.COMMUNITY == VisibleRegionType.fromCode(cmd.getVisibleRegionType())) {
            communityId = cmd.getVisibleRegionId();
        }
        populatePost(userId, post, communityId, false);
        
        try {
            postSearcher.feedDoc(post);
            
            AddUserPointCommand pointCmd = new AddUserPointCommand(userId, PointType.CREATE_TOPIC.name(), 
                userPointService.getItemPoint(PointType.CREATE_TOPIC), userId);  
            userPointService.addPoint(pointCmd);
        } catch(Exception e) {
            LOGGER.error("Failed to add post to search engine, userId=" + userId + ", postId=" + post.getId(), e);
        }
        
        PostDTO postDto = ConvertHelper.convert(post, PostDTO.class);
        
        long endTime = System.currentTimeMillis();
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("Create a new post, userId=" + userId + ", postId=" + postDto.getId() 
                + ", elapse=" + (endTime - startTime));
        }
        
        return postDto;
    }
    
    @Override
    public PostDTO getTopic(GetTopicCommand cmd) {
        long startTime = System.currentTimeMillis();
        // 分享出去的帖来查详情时没有userId
        User user = UserContext.current().getUser();
        Long userId = -1L;
        if(user != null) {
            userId = user.getId();
        }
        
        Long forumId = cmd.getForumId();
        checkForumParameter(userId, forumId, "getTopic");
        
        Long postId = cmd.getTopicId();
        Post post = checkPostParameter(userId, forumId, postId, "getTopic");
        if(post != null) {
            try {
                this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {
                    // 重新读帖子信息，使得数字准确
                    Post tmpPost = this.forumProvider.findPostById(postId);
                    tmpPost.setViewCount(tmpPost.getViewCount() + 1);
                    this.forumProvider.updatePost(tmpPost);
                    UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(tmpPost.getCreatorUid(),IdentifierType.MOBILE.getCode());
                    String creatorUser = userIdentifier.getIdentifierToken();
                    String[] hotusers = configProvider.getValue(ConfigConstants.HOT_USERS, "").split(",");
                    boolean flag = false;
                    for(String hotuser : hotusers){
                    	if(creatorUser == hotuser || creatorUser.equals(hotuser)){
                    		flag = true;
                    	}
                    }
                    if(Byte.valueOf(PostAssignedFlag.ASSIGNED.getCode()).equals(tmpPost.getAssignedFlag()) || flag){
                    	HotPost hotpost = new HotPost();
                        hotpost.setPostId(postId);
                        hotpost.setTimeStamp(Calendar.getInstance().getTimeInMillis());
                        hotpost.setModifyType(HotPostModifyType.VIEW.getCode());
                        hotPostService.push(hotpost);
                    }
                    
                    return null;
                });
            } catch(Exception e) {
                LOGGER.error("Failed to update the view count of post, userId=" + userId + ", postId=" + postId, e);
            }
            PostDTO postDto =  getTopicById(postId, cmd.getCommunityId(), true, true);
            
            long endTime = System.currentTimeMillis();
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("Get topic details, userId=" + userId + ", postId=" + postId 
                    + ", elapse=" + (endTime - startTime) + ", cmd=" + cmd);
            }
            
            return postDto;
//            post = this.forumProvider.findPostById(postId);
//            this.forumProvider.populatePostAttachments(post);
//            populatePost(userId, post, cmd.getCommunityId(), true);
//            
//            return ConvertHelper.convert(post, PostDTO.class);
        } else {
            LOGGER.error("Forum post not found, userId=" + userId + ", forumId=" + forumId 
                + ", postId=" + postId);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
                ForumServiceErrorCode.ERROR_FORUM_TOPIC_NOT_FOUND, "Forum post not found");
        }
    }
    
    public List<PostDTO> getTopicById(List<Long> topicIds, Long communityId, boolean isDetail) {
        return getTopicById(topicIds, communityId, isDetail, false);
    }
    
    @Override
    public List<PostDTO> getTopicById(List<Long> topicIds, Long communityId, boolean isDetail, boolean getByOwnerId) {
        List<PostDTO> postDtoList = new ArrayList<PostDTO>();

        PostDTO postDto = null;
        for(Long topicId : topicIds) {
            try {
                postDto = getTopicById(topicId, communityId, isDetail, getByOwnerId);
                postDtoList.add(postDto);
            } catch(Exception e) {
                LOGGER.error(e.toString());
            }
        }
        
        return postDtoList;
    }
    
    @Override
    public PostDTO getTopicById(Long topicId, Long communityId, boolean isDetail) {
        return getTopicById(topicId, communityId, isDetail, false);
    }
    
    @Override
    public PostDTO getTopicById(Long topicId, Long communityId, boolean isDetail, boolean getByOwnerId) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        
        Post post = checkPostParameter(userId, -1L, topicId, "getTopicById");
        if(post != null) {
            if(PostStatus.ACTIVE != PostStatus.fromCode(post.getStatus())) {
                //Added by Janson
                if( (!(getByOwnerId && post.getCreatorUid().equals(userId)))
                        && (post.getCreatorUid().equals(post.getDeleterUid()) || post.getCreatorUid() != userId.longValue()) ){
            		LOGGER.error("Forum post already deleted, userId=" + userId + ", topicId=" + topicId);
//            		throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
//            				ForumServiceErrorCode.ERROR_FORUM_TOPIC_DELETED, "Forum post already deleted");
            		return null;
            	}
            }
            
            this.forumProvider.populatePostAttachments(post);
            populatePost(userId, post, communityId, isDetail, getByOwnerId);
            
            return ConvertHelper.convert(post, PostDTO.class);
        } else {
            LOGGER.error("Forum post not found, userId=" + userId + ", topicId=" + topicId);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
                ForumServiceErrorCode.ERROR_FORUM_TOPIC_NOT_FOUND, "Forum post not found");
        }
    }
    
    @Override
    public void deletePost(Long forumId, Long postId) {
        deletePost(forumId, postId, true);
    }
    
    @Override
    public void deletePost(Long forumId, Long postId, boolean deleteUserPost) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        
        checkForumParameter(userId, forumId, "getTopic");
        
        Post post = checkPostParameter(userId, forumId, postId, "deletePost");
        Long embededAppId = post.getEmbeddedAppId();
        ForumEmbeddedHandler handler = getForumEmbeddedHandler(embededAppId);
        
        List<Long> userIds = new ArrayList<Long>();
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUsers.class), null, 
                (DSLContext context, Object reducingContext)-> {
                    context.select().from(Tables.EH_USER_FAVORITES)
                        .where(Tables.EH_USER_FAVORITES.TARGET_ID.eq(postId))
                        .and(Tables.EH_USER_FAVORITES.TARGET_TYPE.eq("topic"))
                        .fetch().map( (r) ->{
                        	
                        	userIds.add(r.getValue(Tables.EH_USER_FAVORITES.OWNER_UID));

                            return null;
                        });
                    return true;
                });
        
        if(PostStatus.fromCode(post.getStatus()) == PostStatus.ACTIVE) {
            post.setStatus(PostStatus.INACTIVE.getCode());
            post.setDeleterUid(userId);
            post.setDeleteTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            
            try {
                this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {
                    this.forumProvider.updatePost(post);
                    if(deleteUserPost) {
                        if(userId.equals(post.getCreatorUid())){
                            this.userActivityProvider.deletePostedTopic(userId, postId); 
                            userActivityProvider.updateProfileIfNotExist(post.getCreatorUid(), UserProfileContstant.POSTED_TOPIC_COUNT, -1);
                            }
                        }
                    if(userIds.size() != 0){
                    	for(Long id:userIds){
                    		userActivityProvider.deleteFavorite(id, postId, "topic");
                    	}
                    }
                   return null;
                });
                
                this.postSearcher.deleteById(post.getId());
                if(handler != null) {
                    handler.postProcessEmbeddedObject(post);
                } 
            } catch(Exception e) {
                LOGGER.error("Failed to update the post status, userId=" + userId + ", postId=" + postId, e);
            }
        } else {
            //Added by Janson
            if(deleteUserPost) {
                try {
                        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {
                            if(userId.equals(post.getCreatorUid())){
                                //Try delete the exists inactive post
                                if(this.userActivityProvider.deletePostedTopic(userId, postId) > 0) {
                                    userActivityProvider.updateProfileIfNotExist(post.getCreatorUid(), UserProfileContstant.POSTED_TOPIC_COUNT, -1);    
                                    }
                                }
                            
                        return null;
                        });
                } catch(Exception e) {
                    LOGGER.error("Failed to update the post status, userId=" + userId + ", postId=" + postId, e);
                }
            }
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("The post is already deleted, userId=" + userId + ", postId=" + postId);
            }
        }
    }
    
    @Override
    public ListPostCommandResponse queryTopicsByEntityAndCategory(QueryTopicByEntityAndCategoryCommand cmd) {
        long startTime = System.currentTimeMillis();
        User user = UserContext.current().getUser();
        Long userId = -1L;
        if(user != null) {
            userId = user.getId();
        }
        
        PostEntityTag entityTag = PostEntityTag.fromCode(cmd.getEntityTag());
        ListPostCommandResponse response = null;
        if(entityTag != null) {
            switch(entityTag) {
            case USER:
                QueryTopicByCategoryCommand simpleUserCmd = new QueryTopicByCategoryCommand();
                simpleUserCmd.setForumId(cmd.getForumId());
                simpleUserCmd.setCommunityId(cmd.getCommunityId());
                simpleUserCmd.setEntityTag(cmd.getTargetTag());
                simpleUserCmd.setContentCategory(cmd.getContentCategory());
                simpleUserCmd.setActionCategory(cmd.getActionCategory());
                simpleUserCmd.setPageAnchor(cmd.getPageAnchor());
                simpleUserCmd.setPageSize(cmd.getPageSize());
                response = queryTopicsByCategory(simpleUserCmd);
                break;
            case PM:
            case GARC:
            case GANC:
            case GAPS:
            case GACW:
                QueryOrganizationTopicCommand gaUserCmd = new QueryOrganizationTopicCommand();
                gaUserCmd.setOrganizationId(cmd.getEntityId());
                gaUserCmd.setContentCategory(cmd.getContentCategory());
                gaUserCmd.setActionCategory(cmd.getActionCategory());
                gaUserCmd.setPageAnchor(cmd.getPageAnchor());
                gaUserCmd.setPageSize(cmd.getPageSize());
                response = queryOrganizationTopics(gaUserCmd);
                break;
            default:
                LOGGER.error("Enity tag is not supported, userId=" + userId + ", cmd=" + cmd);
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Unsuported entity tag");
            }
        } else {
            LOGGER.error("Enity tag is null, userId=" + userId + ", cmd=" + cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid entity tag");
        }
        
        if(LOGGER.isInfoEnabled()) {
            int size = 0;
            if(response.getPosts() == null) {
                size = response.getPosts().size();
            }
            long endTime = System.currentTimeMillis();
            LOGGER.info("Query topics by entity and category, userId=" + userId + ", size=" + size 
                + ", elapse=" + (endTime - startTime) + ", cmd=" + cmd);
        }
        
        return response;
    }
    
    @Override
    public ListPostCommandResponse queryTopicsByCategory(QueryTopicByCategoryCommand cmd) {
        long startTime = System.currentTimeMillis();
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        Long communityId = cmd.getCommunityId();
        String tag = "queryTopicsByCategory";
        Community community = checkCommunityParameter(userId, communityId, tag);
        
//        Long forumId = ForumConstants.SYSTEM_FORUM;
//        Forum forum = this.forumProvider.findForumById(forumId);
        Forum forum = checkForumParameter(userId, cmd.getForumId(), tag);
        
        PostEntityTag entityTag = PostEntityTag.fromCode(cmd.getEntityTag());
        
        // 各区域ID，说明见com.everhomes.forum.PostEntityTag
//        Map<String, Long> gaRegionIdMap = this.organizationService.getOrganizationRegionMap(communityId);
//        if(LOGGER.isInfoEnabled()) {
//            LOGGER.info("Ga regions for query topics by category, userId=" + userId 
//                + ", communityId=" + communityId + ", map=" + gaRegionIdMap);
//        }

        Condition visibilityCondition = buildPostCategoryQryCondition(user, entityTag, community, 
            cmd.getContentCategory(), cmd.getActionCategory());
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator(forum.getId());
        locator.setAnchor(cmd.getPageAnchor());
        List<Post> posts = this.forumProvider.queryPosts(locator, pageSize + 1, (loc, query) -> {
            query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ID.eq(forum.getId()));
            query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L));
            query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
            if(visibilityCondition != null) {
                query.addConditions(visibilityCondition);
            }
            
            return query;
        });
        this.forumProvider.populatePostAttachments(posts);
        
        Long nextPageAnchor = null;
        if(posts.size() > pageSize) {
            posts.remove(posts.size() - 1);
            nextPageAnchor = posts.get(posts.size() - 1).getId();
        }
        
        populatePosts(userId, posts, communityId, false);
        
        List<PostDTO> postDtoList = posts.stream().map((r) -> {
          return ConvertHelper.convert(r, PostDTO.class);  
        }).collect(Collectors.toList());
        
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("Query topics by category, userId=" + userId + ", size=" + postDtoList.size() 
                + ", elapse=" + (endTime - startTime) + ", cmd=" + cmd);
        }
        
        return new ListPostCommandResponse(nextPageAnchor, postDtoList);
    }
    
    private Condition buildPostCategoryQryCondition(Long userId, PostEntityTag entityTag, Community community, Long contentCategoryId, Long actionCategoryId) {
        Condition categoryCondition = null;
        
        Category contentCatogry = null;
        // contentCategoryId为0表示全部查，此时也不需要给category条件
        if(contentCategoryId != null && contentCategoryId.longValue() > 0) {
            contentCatogry = this.categoryProvider.findCategoryById(contentCategoryId);
        }
        if(contentCatogry != null) {
            categoryCondition = Tables.EH_FORUM_POSTS.CATEGORY_PATH.like(contentCatogry.getPath() + "%");
        }
        
        Category actionCategory = null;
        if(actionCategoryId != null && actionCategoryId.longValue() > 0) {
            actionCategory = this.categoryProvider.findCategoryById(actionCategoryId);
        }
        if(actionCategory != null) {
            Condition tempCondition = ForumPostCustomField.ACTION_CATEGORY_PATH.getField().like(actionCategory.getPath() + "%");
            if(categoryCondition != null) {
                categoryCondition = categoryCondition.and(tempCondition);
            } else {
                categoryCondition = tempCondition;
            }
        }
        
        Condition entityCondition = null;
        if(entityTag != null) {
            // 既没有查询目标，也没有类型，则只查用户相关的帖子
            switch(entityTag) {
            case USER:
                entityCondition = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(PostEntityTag.USER.getCode());
                entityCondition = entityCondition.and(Tables.EH_FORUM_POSTS.TARGET_TAG.eq(PostEntityTag.USER.getCode()));
                break;
            case PM:
            case GARC:
            case GANC:
            case GAPS:
            case GACW:
                //entityCondition = buildGaRelatedPostQryConditionByCategory(user.getId(), entityTag, community, gaRegionIdMap);
                break;
            default:
                LOGGER.error("Unsupported entity tag for query topic by category, userId=" + userId + ", entityTag=" + entityTag);
                break;
            }
            Condition c2 = Tables.EH_FORUM_POSTS.CREATOR_UID.eq(userId);
        }
        
        return null;
    }
    
    @Override
    public ListPostCommandResponse listTopics(ListTopicCommand cmd) {
        long startTime = System.currentTimeMillis();
        User user = UserContext.current().getUser();
        Long operatorId = user.getId();
        
        Long forumId = cmd.getForumId();
        Forum forum = checkForumParameter(operatorId, forumId, "listTopics");
        
        ListPostCommandResponse cmdResponse = null;
        if(isSystemForum(forumId)) {
            cmdResponse = listSystemForumTopicsByUser(forum, cmd);
        } else {
            cmdResponse = listSimpleForumTopicsByUser(forum, cmd);
        }
        
        if(LOGGER.isInfoEnabled()) {
            int size = 0;
            if(cmdResponse.getPosts() == null) {
                size = cmdResponse.getPosts().size();
            }
            long endTime = System.currentTimeMillis();
            LOGGER.info("List topics, userId=" + operatorId + ", size=" + size 
                + ", elapse=" + (endTime - startTime) + ", cmd=" + cmd);
        }
        
        return cmdResponse;
    }
    
    @Override
    public ListPostCommandResponse listUserRelatedTopics(ListUserRelatedTopicCommand cmd) {
        long startTime = System.currentTimeMillis();
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        Long communityId = cmd.getCommunityId();
        Community community = checkCommunityParameter(operatorId, communityId, "listUserRelatedTopics");
        
        // 各区域ID，说明见com.everhomes.forum.PostEntityTag
        // Map<String, Long> gaRegionIdMap = this.organizationService.getOrganizationRegionMap(communityId);
        
        // Condition condition = buildPostQryConditionByUserRelated(operator, community, gaRegionIdMap);
        Condition condition = buildPostQryConditionByUserRelated(operator, community);
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<Post> posts = this.forumProvider.queryPosts(locator, pageSize + 1, (loc, query) -> {
            query.addJoin(Tables.EH_FORUM_ASSIGNED_SCOPES, JoinType.LEFT_OUTER_JOIN,
                Tables.EH_FORUM_ASSIGNED_SCOPES.OWNER_ID.eq(Tables.EH_FORUM_POSTS.ID));
            
            query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L));
            query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
            if(condition != null) {
                query.addConditions(condition);
            }
            
            return query;
        });
        this.forumProvider.populatePostAttachments(posts);
        
        Long nextPageAnchor = null;
        if(posts.size() > pageSize) {
            posts.remove(posts.size() - 1);
            nextPageAnchor = posts.get(posts.size() - 1).getId();
        }
        
        populatePosts(operatorId, posts, communityId, false);
        
        List<PostDTO> postDtoList = posts.stream().map((r) -> {
          return ConvertHelper.convert(r, PostDTO.class);  
        }).collect(Collectors.toList());
        
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("List topics, userId=" + operatorId + ", size=" + postDtoList.size() 
                + ", elapse=" + (endTime - startTime) + ", cmd=" + cmd);
        }
        
        return new ListPostCommandResponse(nextPageAnchor, postDtoList);
    }
    
    @Override
    public ListPostCommandResponse listActivityPostByCategoryAndTag(ListActivityTopicByCategoryAndTagCommand cmd) {
    	
        long startTime = System.currentTimeMillis();
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        Long communityId = cmd.getCommunityId();
        Community community = checkCommunityParameter(operatorId, communityId, "listActivityPostByCategoryAndTag");
        
        Condition condition = buildActivityPostByCategoryAndTag(operatorId, community, cmd);
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<Post> posts = this.forumProvider.queryPosts(locator, pageSize + 1, (loc, query) -> {
            query.addJoin(Tables.EH_FORUM_ASSIGNED_SCOPES, JoinType.LEFT_OUTER_JOIN,
                Tables.EH_FORUM_ASSIGNED_SCOPES.OWNER_ID.eq(Tables.EH_FORUM_POSTS.ID));
            
            query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L));
            query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
            if(condition != null) {
                query.addConditions(condition);
            }
            
            return query;
        });
        
        this.forumProvider.populatePostAttachments(posts);
        
        populatePosts(operatorId, posts, communityId, false);
        
        Long nextPageAnchor = null;
        if(posts.size() > pageSize) {
            posts.remove(posts.size() - 1);
            nextPageAnchor = posts.get(posts.size() - 1).getId();
        }
        List<PostDTO> postDtoList = posts.stream().map((r) -> {
        	
        	PostDTO dto = ConvertHelper.convert(r, PostDTO.class);
        	if(r != null && r.getAttachments() != null && r.getAttachments().size() > 0) {
        		List<AttachmentDTO> attachments = new ArrayList<AttachmentDTO>();
        		for(Attachment attachment : r.getAttachments()) {
        			attachments.add(ConvertHelper.convert(attachment, AttachmentDTO.class));
        		}
        		dto.setAttachments(attachments);
        	}
          return dto;  
        }).collect(Collectors.toList());
        
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("List activity topics by category and tag, userId=" + operatorId + ", size=" + postDtoList.size() 
                + ", elapse=" + (endTime - startTime) + ", cmd=" + cmd);
        }
        
        return new ListPostCommandResponse(nextPageAnchor, postDtoList);
    }
    
    private Condition buildActivityPostByCategoryAndTag(Long userId, Community community, ListActivityTopicByCategoryAndTagCommand cmd) {
        Condition condition = Tables.EH_FORUM_POSTS.FORUM_ID.eq(community.getDefaultForumId());
        condition = condition.and(Tables.EH_FORUM_POSTS.EMBEDDED_APP_ID.eq(AppConstants.APPID_ACTIVITY));
        Category contentCatogry = null;
        Long contentCategoryId = cmd.getCategoryId();
        if(contentCategoryId != null && contentCategoryId.longValue() > 0) {
            contentCatogry = this.categoryProvider.findCategoryById(contentCategoryId);
        }
        if(contentCatogry != null) {
            condition = condition.and(Tables.EH_FORUM_POSTS.CATEGORY_PATH.like(contentCatogry.getPath() + "%"));
        }
        
        if(!StringUtils.isEmpty(cmd.getTag())) {
            condition = condition.and(Tables.EH_FORUM_POSTS.TAG.eq(cmd.getTag()));
        }
        
        return condition;
    }
    
    public CheckUserPostDTO checkUserPostStatus(CheckUserPostCommand cmd) {
        long startTime = System.currentTimeMillis();
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        Long communityId = cmd.getCommunityId();
        Community community = checkCommunityParameter(operatorId, communityId, "listUserRelatedTopics");
        
        // 各区域ID，说明见com.everhomes.forum.PostEntityTag
        // Map<String, Long> gaRegionIdMap = this.organizationService.getOrganizationRegionMap(communityId);
        
        // Condition condition = buildPostQryConditionByUserRelated(operator, community, gaRegionIdMap);
        Condition condition = buildPostQryConditionByUserRelated(operator, community);
        
        int pageSize = 1; // 只查最新一个
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<Post> posts = this.forumProvider.queryPosts(locator, pageSize + 1, (loc, query) -> {
            query.addJoin(Tables.EH_FORUM_ASSIGNED_SCOPES, JoinType.LEFT_OUTER_JOIN,
                Tables.EH_FORUM_ASSIGNED_SCOPES.OWNER_ID.eq(Tables.EH_FORUM_POSTS.ID));
            
            query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L));
            query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
            if(condition != null) {
                query.addConditions(condition);
            }
            
            return query;
        });
        
        CheckUserPostDTO dto = ConvertHelper.convert(cmd, CheckUserPostDTO.class);
        dto.setStatus(CheckUserPostStatus.NONE.getCode());
        if(posts.size() > 0) {
            Timestamp createTime = posts.get(0).getCreateTime();
            // 客户端指定时间戳时，有新帖超过时间戳则认识为新帖
            if(cmd.getTimestamp() != null) {
                if(createTime != null && cmd.getTimestamp() < createTime.getTime()) {
                    dto.setTimestamp(createTime.getTime());
                    dto.setStatus(CheckUserPostStatus.NEW_POST.getCode());
                }
            } else {
                // 客户端不指定时间戳时，有帖则认为有新帖
                dto.setTimestamp(createTime.getTime());
                dto.setStatus(CheckUserPostStatus.NEW_POST.getCode());
            }
        }
        long endTime = System.currentTimeMillis();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Check the user post status, operatorId=" + operatorId + ", elapse=" + (endTime - startTime) + ", result=" + dto);
        }
        
        return dto;
    }
    
    public ListPostCommandResponse queryOrganizationTopics(QueryOrganizationTopicCommand cmd) {
        long startTime = System.currentTimeMillis();
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        Long organizationId = cmd.getOrganizationId();
        Long communityId = cmd.getCommunityId();
        final Long forumId = cmd.getForumId();
        Organization organization = checkOrganizationParameter(operatorId, organizationId, "listOrganizationTopics");
        List<Long> communityIdList = new ArrayList<Long>();
        if(null == communityId){
        	communityIdList = organizationService.getOrganizationCommunityIdById(organizationId);
        }else{
        	communityIdList.add(communityId);
        }
        if(communityIdList.size() == 0) {
            LOGGER.error("Organization community is not found, operatorId=" + operatorId + ", organizationId=" + organizationId);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
                ForumServiceErrorCode.ERROR_FORUM_ORGANIZATION_COMMUNITY_NOT_FOUND, "Organization community not found");
        }
        
        Condition visibilityCondition = buildGaRelatedPostQryConditionByOrganization(operatorId, organization, communityIdList);

        Long contentCategoryId = cmd.getContentCategory();
        Long actionCategoryId = cmd.getActionCategory();
        Condition categoryCondition = buildPostCategoryCondition(contentCategoryId, actionCategoryId);
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator(ForumConstants.SYSTEM_FORUM);
        locator.setAnchor(cmd.getPageAnchor());
        List<Post> posts = this.forumProvider.queryPosts(locator, pageSize + 1, (loc, query) -> {
            query.addJoin(Tables.EH_FORUM_ASSIGNED_SCOPES, JoinType.LEFT_OUTER_JOIN, 
                Tables.EH_FORUM_ASSIGNED_SCOPES.OWNER_ID.eq(Tables.EH_FORUM_POSTS.ID));
            if(null == forumId){
            	query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ID.eq(ForumConstants.SYSTEM_FORUM));
            }else{
            	query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ID.eq(forumId));
            }
            query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L));
            query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
            if(visibilityCondition != null) {
                query.addConditions(visibilityCondition);
            }
            if(categoryCondition != null) {
                query.addConditions(categoryCondition);
            }
            
            return query;
        });
        this.forumProvider.populatePostAttachments(posts);
        
        Long nextPageAnchor = null;
        if(posts.size() > pageSize) {
            posts.remove(posts.size() - 1);
            nextPageAnchor = posts.get(posts.size() - 1).getId();
        }
        
        populatePosts(operatorId, posts, null, false);
        
        List<PostDTO> postDtoList = posts.stream().map((r) -> {
          return ConvertHelper.convert(r, PostDTO.class);  
        }).collect(Collectors.toList());
        
        if(LOGGER.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.info("Query organization topics, userId=" + operatorId + ", size=" + postDtoList.size() 
                + ", elapse=" + (endTime - startTime) + ", cmd=" + cmd);
        }        
        
        return new ListPostCommandResponse(nextPageAnchor, postDtoList);
    }
    
    @Override
    public void likeTopic(LikeTopicCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        String tag = "likeTopic";
        
        Long forumId = cmd.getForumId();
        checkForumParameter(operatorId, forumId, tag);
        
        Long topicId = cmd.getTopicId();
        checkPostParameter(operatorId, forumId, topicId, tag);
        
        try {
            this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {
                this.forumProvider.likePost(operatorId, topicId);
                Post tmpPost = this.forumProvider.findPostById(topicId);
                String creatorUid = Long.toString(tmpPost.getCreatorUid());
                String[] hotusers = configProvider.getValue(ConfigConstants.HOT_USERS, "").split(",");
                boolean flag = false;
                for(String hotuser : hotusers){
                	if(creatorUid == hotuser){
                		flag = true;
                	}
                }
                if(Byte.valueOf(PostAssignedFlag.ASSIGNED.getCode()).equals(tmpPost.getAssignedFlag()) || flag){
                	HotPost hotpost = new HotPost();
                    hotpost.setPostId(topicId);
                    hotpost.setTimeStamp(Calendar.getInstance().getTimeInMillis());
                    hotpost.setModifyType(HotPostModifyType.LIKE.getCode());
                    hotPostService.push(hotpost);
                }
               return null;
            });
        } catch(Exception e) {
            LOGGER.error("Failed to update the like count of post, userId=" + operatorId + ", topicId=" + topicId, e);
        }
    }
    
    @Override
    public void cancelLikeTopic(CancelLikeTopicCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        String tag = "cancelLikeTopic";
        
        Long forumId = cmd.getForumId();
        checkForumParameter(operatorId, forumId, tag);
        
        Long topicId = cmd.getTopicId();
        checkPostParameter(operatorId, forumId, topicId, tag);
        
        try {
            this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {
                this.forumProvider.cancelLikePost(operatorId, topicId);
               return null;
            });
        } catch(Exception e) {
            LOGGER.error("Failed to update the dislike count of post, userId=" + operatorId + ", topicId=" + topicId, e);
        }
    }
    
    @Override
    public ListPostCommandResponse listTopicComments(ListTopicCommentCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        String tag = "listTopicComments";
        
        Long forumId = cmd.getForumId();
        Forum forum = checkForumParameter(operatorId, forumId, tag);
        
        Long topicId = cmd.getTopicId();
        checkPostParameter(operatorId, forumId, topicId, tag);
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator(forumId);
        locator.setAnchor(cmd.getPageAnchor());
        List<Post> posts = this.forumProvider.queryPosts(locator, pageSize + 1, (loc, query) -> {
            query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ID.eq(forum.getId()));
            query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(topicId));
            query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
            
            return query;
        });
        this.forumProvider.populatePostAttachments(posts);
        
        Long nextPageAnchor = null;
        if(posts.size() > pageSize) {
            posts.remove(posts.size() - 1);
            nextPageAnchor = posts.get(posts.size() - 1).getId();
        }
        
        populatePosts(operatorId, posts, null, true);
        
        List<PostDTO> postDtoList = posts.stream().map((r) -> {
          return ConvertHelper.convert(r, PostDTO.class);  
        }).collect(Collectors.toList());
        
        
        return new ListPostCommandResponse(nextPageAnchor, postDtoList);
    }
    
    public void updatePostPrivacy(Long forumId, Long postId, PostPrivacy privacy) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        String tag = "updatePostPrivacy";
        
        checkForumParameter(operatorId, forumId, tag);
        
        Post post = checkPostParameter(operatorId, forumId, postId, tag);
        
        if(privacy == null) {
            LOGGER.error("Invalid privacy, operatorId=" + operatorId + ", forumId=" + forumId 
                + ", postId=" + postId + ", privacy=" + privacy);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid privacy");
        }
        
        try {
            this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {
                post.setPrivateFlag(privacy.getCode());
                this.forumProvider.updatePost(post);
               return null;
            });
        } catch(Exception e) {
            LOGGER.error("Failed to update the privacy of post, userId=" + operatorId + ", forumId=" + forumId 
                + ", postId=" + postId + ", privacy=" + privacy, e);
        }
    }
    
    @Override
    public PostDTO createComment(NewCommentCommand cmd) {
        //checkForumPostPrivilege(cmd.getForumId());
        
        User user = UserContext.current().getUser();
        Long userId = user.getId();
                
        Post post = processCommentCommand(userId, cmd);
        
        Long embededAppId = cmd.getEmbeddedAppId();
        ForumEmbeddedHandler handler = getForumEmbeddedHandler(embededAppId);
        if(handler != null) {
            handler.preProcessEmbeddedObject(post);
            forumProvider.createPost(post);
            handler.preProcessEmbeddedObject(post);
        } else {
            forumProvider.createPost(post);
        }

        // Save the attachments after the post is saved
        processPostAttachments(userId, cmd.getAttachments(), post);
        
        // Populate the result post the same as query
        populatePost(userId, post, null, true);
        
        //Send message to post creator
        
        AddUserPointCommand pointCmd = new AddUserPointCommand(userId, PointType.CREATE_COMMENT.name(), 
            userPointService.getItemPoint(PointType.CREATE_COMMENT), userId);  
        userPointService.addPoint(pointCmd);
        
        Post topic = this.forumProvider.findPostById(post.getParentPostId());
        if(topic != null && !topic.getCreatorUid().equals(userId) && !topic.getStatus().equals(PostStatus.INACTIVE.getCode())) {
            //Send message to creator
            Map<String, String> map = new HashMap<String, String>();
            String userName = (user.getNickName() == null) ? "" : user.getNickName();
            String subject = (topic.getSubject() == null) ? "" : topic.getSubject();
            map.put("userName", userName);
            map.put("postName", subject);
            sendMessageCode(topic.getCreatorUid(), user.getLocale(), map, ForumNotificationTemplateCode.FORUM_REPLAY_ONE_TO_CREATOR);
        }
        
        return ConvertHelper.convert(post, PostDTO.class);
    }
    
    private void sendMessageCode(Long uid, String locale, Map<String, String> map, int code) {
        String scope = ForumNotificationTemplateCode.SCOPE;
        
        String notifyTextForOther = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        sendMessageToUser(uid, notifyTextForOther, null);
    }
    
    private void sendMessageToUser(Long uid, String content, Map<String, String> meta) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_UID);
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), uid.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
        if(null != meta && meta.size() > 0) {
            messageDto.getMeta().putAll(meta);
            }
        
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("forum sendMessageToUser " + content + " uid= " + uid);
        }
        
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
                uid.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }
    
    @Override
    public void assignTopicScope(AssignTopicScopeCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        String tag = "assignTopicScope";
        
        Long forumId = cmd.getForumId();
        checkForumParameter(operatorId, forumId, tag);
        
        Long postId = cmd.getTopicId();
        Post post = checkPostParameter(operatorId, forumId, postId, tag);
        
        PostAssignedFlag assignedFlag = PostAssignedFlag.fromCode(cmd.getAssignedFlag());
        if(assignedFlag == null) {
            LOGGER.error("Invalid assigned flag, operatorId=" + operatorId + ", cmd=" + cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid assigned flag");
        }
        
        PostAssignedFlag postAssignedFlag = PostAssignedFlag.fromCode(post.getAssignedFlag());
        switch(assignedFlag) {
        case NONE:
            if(PostAssignedFlag.ASSIGNED == postAssignedFlag) {
                this.dbProvider.execute((status) -> {
                    List<AssignedScope> scopeList = this.forumProvider.findAssignedScopeByOwnerId(postId);
                    for(AssignedScope scope : scopeList) {
                        this.forumProvider.deleteAssignedScope(scope);
                    }
                    post.setAssignedFlag(PostAssignedFlag.NONE.getCode());
                    this.forumProvider.updatePost(post);
                    
                    return null;
                });
            } else {
                LOGGER.error("Forum post is not in assigned state, operatorId=" + operatorId + ", forumId=" + forumId 
                    + ", postId=" + postId + ", assignFlag=" + post.getAssignedFlag());
                throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
                    ForumServiceErrorCode.ERROR_FORUM_TOPIC_NOT_ASSIGNED, "Forum post is not in assigned state");
            }
            break;
        case ASSIGNED:
            ForumAssignedScopeCode scopeCode = ForumAssignedScopeCode.fromCode(cmd.getScopeCode());
            if(scopeCode == null) {
                LOGGER.error("Invalid assigned scope code, operatorId=" + operatorId + ", cmd=" + cmd);
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid assigned scope code");
            }
            
            List<Long> scopeIds = cmd.getScopeIds();
            if(scopeIds == null || scopeIds.size() == 0) {
                LOGGER.error("Assigned scope ids may not be empty, operatorId=" + operatorId + ", cmd=" + cmd);
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Assigned scope ids may not be empty");
            }
            
            if(PostAssignedFlag.NONE == postAssignedFlag) {
                this.dbProvider.execute((status) -> {
                    for(Long scopeId : scopeIds) {
                        AssignedScope scope = new AssignedScope();
                        scope.setOwnerId(postId);
                        scope.setScopeCode(scopeCode.getCode());
                        scope.setScopeId(scopeId);
                        this.forumProvider.createAssignedScope(scope);
                    }
                    post.setAssignedFlag(PostAssignedFlag.ASSIGNED.getCode());
                    this.forumProvider.updatePost(post);
                    
                    return null;
                });
            } else {
                LOGGER.error("Forum post is not in assigned state, operatorId=" + operatorId + ", forumId=" + forumId 
                    + ", postId=" + postId + ", assignFlag=" + post.getAssignedFlag());
                throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
                    ForumServiceErrorCode.ERROR_FORUM_TOPIC_ALREADY_ASSIGNED, "Forum post is already assigned");
            }
            break;
        }
    }
    
    @Override
    public List<AssignedScopeDTO> listTopicAssignedScope(ListTopicAssignedScopeCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        String tag = "listTopicAssignedScope";
        
        Long forumId = cmd.getForumId();
        checkForumParameter(operatorId, forumId, tag);
        
        Long postId = cmd.getTopicId();
        checkPostParameter(operatorId, forumId, postId, tag);
        
        List<AssignedScopeDTO> scopeDtoList = new ArrayList<AssignedScopeDTO>();
        
        AssignedScopeDTO scopeDto = null;
        ForumAssignedScopeCode scopeCode = null;
        List<AssignedScope> scopeList = this.forumProvider.findAssignedScopeByOwnerId(postId);
        for(AssignedScope scope : scopeList) {
            scopeDto = ConvertHelper.convert(scope, AssignedScopeDTO.class);
            scopeDtoList.add(scopeDto);
            
            scopeCode = ForumAssignedScopeCode.fromCode(scope.getScopeCode());
            if(scopeCode != null) {
                switch(scopeCode) {
                case ALL:
                    break;
                case COMMUNITY:
                    Community community = this.communityProvider.findCommunityById(scope.getScopeId());
                    if(community != null) {
                        scopeDto.setScopeName(community.getName());
                    } else {
                        LOGGER.error("Community not found, operatorId=" + operatorId + ", postId=" + postId + ", scope=" + scopeDto);
                    }
                    break;
                case CITY:
                    Region region = this.regionProvider.findRegionById(scope.getScopeId());
                    if(region != null) {
                        scopeDto.setScopeName(region.getName());
                    } else {
                        LOGGER.error("City not found, operatorId=" + operatorId + ", postId=" + postId + ", scope=" + scopeDto);
                    }
                    break;
                }
            }
        }
        
        return scopeDtoList;
    }
    
    @Override
    public SearchTopicAdminCommandResponse searchTopic(SearchTopicAdminCommand cmd) {
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
                PostDTO temp = getTopicById(post.getId(), cmd.getCommunityId(), false);
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

    @Override
    public void updateUsedAndRental(UsedAndRentalCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        
        Long postId = cmd.getId();
        Post post = checkPostParameter(operatorId, -1L, postId, "updateUsedAndRental");
        
        UsedAndRentalStatus status = UsedAndRentalStatus.fromCode(cmd.getStatus());
        if(status == null) {
            LOGGER.error("Invalid used and rental status, operatorId=" + operatorId + ", postId=" + postId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                "Invalid status parameter");
        }
        
        if(!operatorId.equals(post.getCreatorUid())) {
            LOGGER.error("The operator is not the creator, operatorId=" + operatorId + ", creatorId=" + post.getCreatorUid() 
                + ", postId=" + postId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                "Access denied: insufficient privilege");
        }
        
        Long embeddedAppId = post.getEmbeddedAppId();
        if(embeddedAppId == null || AppConstants.APPID_USED_AND_RENTAL != embeddedAppId.longValue()) {
            LOGGER.error("The topic is not the used and rental type, operatorId=" + operatorId + ", postId=" + postId 
                + ", expectEmbeddedAppId=" + AppConstants.APPID_USED_AND_RENTAL + ", realEmbeddedAppId=" + embeddedAppId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                "Invalid id parameter");
        }
        
        post.setEmbeddedJson(StringHelper.toJsonString(cmd));
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {
            this.forumProvider.updatePost(post);
           return null;
        });
    }

    @Override
    public void updateFreeStuff(FreeStuffCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        
        Long postId = cmd.getId();
        Post post = checkPostParameter(operatorId, -1L, postId, "updateFreeStuff");
        
        FreeStuffStatus status = FreeStuffStatus.fromCode(cmd.getStatus());
        if(status == null) {
            LOGGER.error("Invalid free stuff status, operatorId=" + operatorId + ", postId=" + postId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                "Invalid status parameter");
        }
        
        if(!operatorId.equals(post.getCreatorUid())) {
            LOGGER.error("The operator is not the creator, operatorId=" + operatorId + ", creatorId=" + post.getCreatorUid() 
                + ", postId=" + postId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                "Access denied: insufficient privilege");
        }
        
        Long embeddedAppId = post.getEmbeddedAppId();
        if(embeddedAppId == null || AppConstants.APPID_FREE_STUFF != embeddedAppId.longValue()) {
            LOGGER.error("The topic is not the free stuff type, operatorId=" + operatorId + ", postId=" + postId 
                + ", expectEmbeddedAppId=" + AppConstants.APPID_USED_AND_RENTAL + ", realEmbeddedAppId=" + embeddedAppId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                "Invalid id parameter");
        }
        
        post.setEmbeddedJson(StringHelper.toJsonString(cmd));
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {
            this.forumProvider.updatePost(post);
           return null;
        });
    }

    @Override
    public void updateLostAndFound(LostAndFoundCommand cmd) {
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        
        Long postId = cmd.getId();
        Post post = checkPostParameter(operatorId, -1L, postId, "updateFreeStuff");
        
        LostAndFoundStatus status = LostAndFoundStatus.fromCode(cmd.getStatus());
        if(status == null) {
            LOGGER.error("Invalid lost and found status, operatorId=" + operatorId + ", postId=" + postId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                "Invalid status parameter");
        }
        
        if(!operatorId.equals(post.getCreatorUid())) {
            LOGGER.error("The operator is not the creator, operatorId=" + operatorId + ", creatorId=" + post.getCreatorUid() 
                + ", postId=" + postId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                "Access denied: insufficient privilege");
        }
        
        Long embeddedAppId = post.getEmbeddedAppId();
        if(embeddedAppId == null || AppConstants.APPID_LOST_AND_FOUND != embeddedAppId.longValue()) {
            LOGGER.error("The topic is not the lost and found type, operatorId=" + operatorId + ", postId=" + postId 
                + ", expectEmbeddedAppId=" + AppConstants.APPID_USED_AND_RENTAL + ", realEmbeddedAppId=" + embeddedAppId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                "Invalid id parameter");
        }
        
        post.setEmbeddedJson(StringHelper.toJsonString(cmd));
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {
            this.forumProvider.updatePost(post);
           return null;
        });
    }
    
    @Override
    public void checkForumPostPrivilege(Long forumId) {
        if(forumId == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid forumId parameter");
        
        Forum forum = this.forumProvider.findForumById(forumId.longValue());
        if(forum == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Could not find the forum object");

        ResourceUserRoleResolver roleResolve = PlatformContext.getComponent(forum.getOwnerType());
        if(roleResolve == null) {
            assert(false);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Unable to find proper role resolver");
        }
        
        User user = UserContext.current().getUser();
        if(!this.aclProvider.checkAccess(
                EntityType.FORUM.getCode(), forum.getId(), 
                EntityType.USER.getCode(), user.getId(), 
                PrivilegeConstants.ForumNewTopic, 
                roleResolve.determineRoleInResource(user.getId(), forum.getOwnerId(), 
                        EntityType.FORUM.getCode(), forum.getId()))) {
            
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                    "Access denied: insufficient privilege");
        }
    }

    @Override
    public void checkForumModifyItemPrivilege(Long forumId, Long topicId) {
        if(forumId == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid forumId parameter");
        
        Forum forum = this.forumProvider.findForumById(forumId.longValue());
        if(forum == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Could not find the forum object");

        ResourceUserRoleResolver roleResolve = PlatformContext.getComponent(forum.getOwnerType());
        if(roleResolve == null) {
            assert(false);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Unable to find proper role resolver");
        }
        
        if(topicId == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid topicId parameter");
            
        Post post = this.forumProvider.findPostById(topicId);
        if(post == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Could not find the post object");
        
        User user = UserContext.current().getUser();
        List<Long> roles = roleResolve.determineRoleInResource(user.getId(), forum.getOwnerId(),
                EntityType.FORUM.getCode(), forum.getId());
        
        if(post.getCreatorUid() != null && post.getCreatorUid().longValue() == user.getId())
            roles.add(Role.ResourceCreator);
        if(!this.aclProvider.checkAccess(
                EntityType.POST.getCode(), topicId, 
                EntityType.USER.getCode(), user.getId(), 
                PrivilegeConstants.Write, 
                roles)) {
            
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                    "Access denied: insufficient privilege");
        }
    }

    @Override
    public void checkForumDeleteItemPrivilege(Long forumId, Long topicId) {
        if(forumId == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid forumId parameter");
        
        Forum forum = this.forumProvider.findForumById(forumId.longValue());
        if(forum == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Could not find the forum object");

        ResourceUserRoleResolver roleResolve = PlatformContext.getComponent(forum.getOwnerType());
        if(roleResolve == null) {
            assert(false);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, 
                    "Unable to find proper role resolver");
        }
        
        if(topicId == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Invalid topicId parameter");
            
        Post post = this.forumProvider.findPostById(topicId);
        if(post == null)
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
                    "Could not find the post object");
        
        User user = UserContext.current().getUser();
        List<Long> roles = roleResolve.determineRoleInResource(user.getId(), forum.getOwnerId(),
                EntityType.FORUM.getCode(), forum.getId());
        
        if(post.getCreatorUid() != null && post.getCreatorUid().longValue() == user.getId())
            roles.add(Role.ResourceCreator);
        if(!this.aclProvider.checkAccess(
                EntityType.POST.getCode(), topicId, 
                EntityType.USER.getCode(), user.getId(), 
                Privilege.Delete, 
                roles)) {
            
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                    "Access denied: insufficient privilege");
        }
    }
    
    private Forum checkForumParameter(Long operatorId, Long forumId, String tag) {
        if(forumId == null) {
            LOGGER.error("Forum id may not be null, operatorId=" + operatorId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid forum id");
        }
        
        Forum forum = this.forumProvider.findForumById(forumId);
        if(forum == null) {
            LOGGER.error("Forum not found, operatorId=" + operatorId + ", forumId=" + forumId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
                ForumServiceErrorCode.ERROR_FORUM_NOT_FOUND, "Forum not found");
        }
        
        return forum;
    }
    
    private Post checkPostParameter(Long operatorId, Long forumId, Long postId, String tag) {
        if(postId == null) {
            LOGGER.error("Forum post id may not be null, operatorId=" + operatorId 
                + ", tag=" + tag + ", forumId=" + forumId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid forum post id");
        }
        
        Post post = this.forumProvider.findPostById(postId);
        if(post == null) {
            LOGGER.error("Forum post not found, operatorId=" + operatorId + ", forumId=" + forumId 
                + ", postId=" + postId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
                ForumServiceErrorCode.ERROR_FORUM_TOPIC_NOT_FOUND, "Forum post not found");
        }
        
        return post;
    }
    
    private Community checkCommunityParameter(Long operatorId, Long communityId, String tag) {
        if(communityId == null) {
            LOGGER.error("Community id may not be null, operatorId=" + operatorId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid community id");
        }
        
        Community community = communityProvider.findCommunityById(communityId);
        if(community == null) {
            LOGGER.error("Community is not found, operatorId=" + operatorId + ", communityId=" + communityId
                + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
                ForumServiceErrorCode.ERROR_FORUM_COMMUNITY_NOT_FOUND, "Community not found");
        } 
        
        return community;
    }
    
    private Organization checkOrganizationParameter(Long operatorId, Long organizationId, String tag) {
        if(organizationId == null) {
            LOGGER.error("Organization id may not be null, operatorId=" + operatorId + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid organization id");
        }
        
        Organization organization = organizationProvider.findOrganizationById(organizationId);
        if(organization == null) {
            LOGGER.error("Organization is not found, operatorId=" + operatorId + ", organizationId=" + organizationId
                + ", tag=" + tag);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
                ForumServiceErrorCode.ERROR_FORUM_ORGANIZATION_NOT_FOUND, "Organization not found");
        } 
        
        return organization;
    }
    
//    private ListPostCommandResponse listSystemForumTopicsByUser(Forum forum, ListTopicCommand cmd) {
//        User user = UserContext.current().getUser();
//        long userId = user.getId();
//        Long communityId = cmd.getCommunityId();
//        
//        Community community = checkCommunityParameter(userId, communityId, "listSystemForumTopicsByUser");
//        
//        // 各区域ID，说明见com.everhomes.forum.PostEntityTag
//        Map<String, Long> gaRegionIdMap = this.organizationService.getOrganizationRegionMap(communityId);
//
//        // 根据查帖指定的可见性创建查询条件
//        VisibilityScope scope = VisibilityScope.fromCode(cmd.getVisibilityScope());
//        if(scope == null) {
//            scope = VisibilityScope.NEARBY_COMMUNITIES;
//        }
//        Condition visibilityCondition = buildPostVisibilityQryCondition(user, community, gaRegionIdMap, scope);
//        
//        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
//        CrossShardListingLocator locator = new CrossShardListingLocator(forum.getId());
//        locator.setAnchor(cmd.getPageAnchor());
//        List<Post> posts = this.forumProvider.queryPosts(locator, pageSize + 1, (loc, query) -> {
//            query.addJoin(Tables.EH_FORUM_ASSIGNED_SCOPES, JoinType.LEFT_OUTER_JOIN, 
//                Tables.EH_FORUM_ASSIGNED_SCOPES.OWNER_ID.eq(Tables.EH_FORUM_POSTS.ID));
//            query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ID.eq(forum.getId()));
//            query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L));
//            query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
//            if(visibilityCondition != null) {
//                query.addConditions(visibilityCondition);
//            }
//            
//            return query;
//        });
//        this.forumProvider.populatePostAttachments(posts);
//        
//        Long nextPageAnchor = null;
//        if(posts.size() > pageSize) {
//            posts.remove(posts.size() - 1);
//            nextPageAnchor = posts.get(posts.size() - 1).getId();
//        }
//        
//        populatePosts(userId, posts, communityId, false);
//        
//        List<PostDTO> postDtoList = posts.stream().map((r) -> {
//          return ConvertHelper.convert(r, PostDTO.class);  
//        }).collect(Collectors.toList());
//        
//        
//        return new ListPostCommandResponse(nextPageAnchor, postDtoList);
//    }
    
    /**
     * 查询住宅小区的社区论坛或商业园区的默认论坛里的帖。
     * 为了兼容新增的商业园区，把条件重新调整编写一遍，上面注释掉的为原住宅小区查询逻辑。by lqs 20151105
     * @param forum
     * @param cmd
     * @return
     */
    private ListPostCommandResponse listSystemForumTopicsByUser(Forum forum, ListTopicCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        Long communityId = cmd.getCommunityId();
        
        Community community = checkCommunityParameter(userId, communityId, "listSystemForumTopicsByUser");

        // 根据查帖指定的可见性创建查询条件
        VisibilityScope scope = VisibilityScope.fromCode(cmd.getVisibilityScope());
        Condition visibilityCondition = buildDefaultForumPostQryConditionForCommunity(user, community, scope);
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator(forum.getId());
        locator.setAnchor(cmd.getPageAnchor());
        List<Post> posts = this.forumProvider.queryPosts(locator, pageSize + 1, (loc, query) -> {
            query.addJoin(Tables.EH_FORUM_ASSIGNED_SCOPES, JoinType.LEFT_OUTER_JOIN, 
                Tables.EH_FORUM_ASSIGNED_SCOPES.OWNER_ID.eq(Tables.EH_FORUM_POSTS.ID));
            query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ID.eq(forum.getId()));
            query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L));
            query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
            if(visibilityCondition != null) {
                query.addConditions(visibilityCondition);
            }
            
            return query;
        });
        this.forumProvider.populatePostAttachments(posts);
        
        Long nextPageAnchor = null;
        if(posts.size() > pageSize) {
            posts.remove(posts.size() - 1);
            nextPageAnchor = posts.get(posts.size() - 1).getId();
        }
        
        populatePosts(userId, posts, communityId, false);
        
        List<PostDTO> postDtoList = posts.stream().map((r) -> {
          return ConvertHelper.convert(r, PostDTO.class);  
        }).collect(Collectors.toList());
        
        
        return new ListPostCommandResponse(nextPageAnchor, postDtoList);
    }
    
    /**
     * 对于小区/园区在默认论坛里的帖，不管creatorTag和targetTag是谁，其可见范围由visibleRegionType和VisibleRegionId决定；
     * 条件一：帖子必须是公开的。
     * 条件二：如果类型是小区/园区，由于住宅小区含周边社区概念，故对于住宅小区需要增加周边小区范围，而商业园区则只限于本园区；
     * 条件三：如果类型是片区，则需要找覆盖当前小区的所有机构（含各级上级机构），不管是发给这些机构的帖还是这些机构发的帖都满足要求。
     * 条件四：自己发的帖自己永远能看。
     * 三个条件关系：(条件一 and (条件二 or 条件三)) or 条件四
     * @param user 当前用户信息
     * @param community 小区信息
     * @param scope 范围（住宅小区才使用，里面指定是否需要扩展到周边小区）
     * @return 条件
     */
    private Condition buildDefaultForumPostQryConditionForCommunity(User user, Community community, VisibilityScope scope) {
        Long userId = -1L;
        if(user != null) {
            userId = user.getId();
        }
        
        Condition condition = Tables.EH_FORUM_POSTS.CREATOR_UID.eq(userId);
        if(community == null) {
            LOGGER.error("Community not found, userId=" + userId);
            return condition;
        }
        
        // 只有公开的帖子才能查到
        Condition c1 = Tables.EH_FORUM_POSTS.PRIVATE_FLAG.notEqual(PostPrivacy.PRIVATE.getCode());
        
        // 如果类型是小区/园区，由于住宅小区含周边社区概念，故对于住宅小区需要增加周边小区范围，而商业园区则只限于本园区；
        Condition c2 = buildDefaultForumPostQryConditionForNearbyCommunity(user, community, scope);
        
        // 如果类型是片区，则需要找覆盖当前小区的所有机构（含各级上级机构），不管是发给这些机构的帖还是这些机构发的帖都满足要求。
        Condition c3 = buildDefaultForumPostQryConditionByOrganization(user, community);
        
        if(c3 == null) {
            return (c1.and(c2)).or(condition);
        } else {
            return (c1.and(c2.or(c3))).or(condition);
        }
    }
    
    /**
     * 如果类型是小区/园区，由于住宅小区含周边社区概念，故对于住宅小区需要增加周边小区范围，而商业园区则只限于本园区；
     * 参考buildPostQryConditionForCommunity的条件二。
     * @param user 当前用户信息
     * @param community 小区信息
     * @param scope 范围（住宅小区才使用，里面指定是否需要扩展到周边小区）
     * @return 条件
     */
    private Condition buildDefaultForumPostQryConditionForNearbyCommunity(User user, Community community, VisibilityScope scope) {
        Long userId = getUserIdWithDefault(user);
        
        Long communityId = community.getId();
        CommunityType communityType = CommunityType.fromCode(community.getCommunityType());
        if(communityType == null) {
            LOGGER.error("Community type not found, userId=" + userId + ", communityId=" + community.getId() 
                + ", communityType=" + communityType + ", scope=" + scope);
            communityType = CommunityType.RESIDENTIAL;
        }
        
        // 不管是商业园区还是住宅小区都需要包含本小区ID
        List<Long> cmntyIdList = new ArrayList<Long>();
        cmntyIdList.add(communityId);
        // 对于住宅小区还存在周边社区的概述（商业园区没有此概念），此时如果不指明是“本小区”则添加上周边各小区
        if(communityType == CommunityType.RESIDENTIAL && scope != VisibilityScope.COMMUNITY && community.getStatus() == CommunityAdminStatus.ACTIVE.getCode()) {
            List<Community> nearbyCmntyList = communityProvider.findNearyByCommunityById(UserContext.getCurrentNamespaceId(UserContext.current().getNamespaceId()), communityId);
            for(Community c : nearbyCmntyList) {
                cmntyIdList.add(c.getId());
            }
        }
        Condition condition = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode());
        condition = condition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.in(cmntyIdList));
        
        return condition;
    }
    
    /**
     * 如果类型是片区，则需要找覆盖当前小区的所有机构（含各级上级机构），不管是发给这些机构的帖还是这些机构发的帖都满足要求。
     * @param user 当前用户信息
     * @param community 小区信息
     * @return 条件
     */
    private Condition buildDefaultForumPostQryConditionByOrganization(User user, Community community) {
        Long communityId = community.getId();
        // 对于发给机构的帖或者是机构发的帖，凡是覆盖该小区的所有机构都满足要求
        List<Organization> organizationList = this.organizationService.getOrganizationTreeUpToRoot(communityId);
        List<Long> organizationIdList = new ArrayList<Long>();
        for(Organization organization : organizationList) {
            organizationIdList.add(organization.getId());
        }
        Condition condition = null;
        if(organizationIdList.size() > 0) {
            condition = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.REGION.getCode());
            condition = condition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.in(organizationIdList));
        }
        
        return condition;
    }
    
    private Long getUserIdWithDefault(User user) {
        Long userId = -1L;
        if(user != null) {
            userId = user.getId();
        } else {
            if(LOGGER.isWarnEnabled()) {
                LOGGER.warn("User id is not found, -1 will be used");
            }
        }
        
        return userId;
    }
    
    private ListPostCommandResponse listSimpleForumTopicsByUser(Forum forum, ListTopicCommand cmd) {
        User user = UserContext.current().getUser();
        long userId = user.getId();
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        
        CrossShardListingLocator locator = new CrossShardListingLocator(forum.getId());
        locator.setAnchor(cmd.getPageAnchor());
        List<Post> posts = this.forumProvider.queryPosts(locator, pageSize + 1, (loc, query) -> {
            query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ID.eq(forum.getId())); 
            query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L));
            query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
            return query;
        });
        this.forumProvider.populatePostAttachments(posts);
        
        Long nextPageAnchor = null;
        if(posts.size() > pageSize) {
            posts.remove(posts.size() - 1);
            nextPageAnchor = posts.get(posts.size() - 1).getId();
        }
        
        populatePosts(userId, posts, cmd.getCommunityId(), false);
        
        List<PostDTO> postDtoList = posts.stream().map((r) -> {
          return ConvertHelper.convert(r, PostDTO.class);  
        }).collect(Collectors.toList());
        
        
        return new ListPostCommandResponse(nextPageAnchor, postDtoList);
    }
    
    private Post processTopicCommand(Long userId, NewTopicCommand cmd) {
        Post post = new Post();

        Long forumId = cmd.getForumId();
        checkForumParameter(userId, forumId, "processTopicCommand");
        
        processPostCategory(userId, cmd, post);
        processPostLocation(userId, cmd, post);
//        processPostVisibility(userId, forumId, cmd, post);
        post.setVisibleRegionType(cmd.getVisibleRegionType());
        post.setVisibleRegionId(cmd.getVisibleRegionId());
        
        post.setForumId(cmd.getForumId());
        post.setParentPostId(0L);
        
        post.setCreatorUid(userId);
        PostEntityTag creatorTag = PostEntityTag.fromCode(cmd.getCreatorTag());
        if(creatorTag == null) {
            creatorTag = PostEntityTag.USER;
        }
        post.setCreatorTag(creatorTag.getCode());

        PostEntityTag targetTag = PostEntityTag.fromCode(cmd.getTargetTag());
        if(targetTag == null) {
            targetTag = PostEntityTag.USER;
        }
        post.setTargetTag(targetTag.getCode());        
        
        post.setSubject(cmd.getSubject());
        post.setContentType(cmd.getContentType());
        post.setContent(cmd.getContent());
        Long embededAppId = cmd.getEmbeddedAppId();
        post.setEmbeddedAppId(embededAppId);
        post.setEmbeddedId(cmd.getEmbeddedId());
        post.setEmbeddedJson(cmd.getEmbeddedJson());
        post.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        
        PostPrivacy privateFlag = PostPrivacy.fromCode(cmd.getPrivateFlag());
        if(privateFlag == null) {
            // 政府机构发的维修之类的帖，默认不公开
            if(CategoryConstants.GA_PRIVACY_CATEGORIES.contains(post.getContentCategory())) {
                privateFlag =  PostPrivacy.PRIVATE;
            } else {
                privateFlag =  PostPrivacy.PUBLIC;
            }
        }
        post.setPrivateFlag(privateFlag.getCode());
        
        post.setAssignedFlag(PostAssignedFlag.NONE.getCode());
        
        return post;
    }
    
    private Post processCommentCommand(long userId, NewCommentCommand cmd) {
        Post commentPost = new Post();

        Long forumId = cmd.getForumId();
        
        Long topicId = cmd.getTopicId();
        if(topicId == null) {
            LOGGER.error("Topic id is null, userId=" + userId + ", forumId=" + forumId + ", topicId=" + topicId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid topic id");
        }
        Post topic = this.forumProvider.findPostById(topicId);
        if(topic == null) {
            LOGGER.error("Topic not found, userId=" + userId + ", forumId=" + forumId + ", topicId=" + topicId);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
                ForumServiceErrorCode.ERROR_FORUM_NOT_FOUND, "Topic not found");
        }
        
        commentPost.setForumId(cmd.getForumId());
        commentPost.setParentPostId(topic.getId());
        commentPost.setCreatorUid(userId);
        commentPost.setCreatorTag(PostEntityTag.USER.getCode());
        commentPost.setTargetTag(PostEntityTag.USER.getCode());
        commentPost.setContentType(cmd.getContentType());
        commentPost.setContent(cmd.getContent());
        commentPost.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        commentPost.setPrivateFlag(PostPrivacy.PUBLIC.getCode());
        commentPost.setAssignedFlag(PostAssignedFlag.NONE.getCode());
        commentPost.setStatus(PostStatus.ACTIVE.getCode());
        
        return commentPost;
    }
    
    private void processPostCategory(long userId, NewTopicCommand cmd, Post post) {
        Long contentCategory = cmd.getContentCategory();
        if(contentCategory == null || contentCategory.longValue() == 0 || contentCategory.longValue() == -1) {
            contentCategory = CategoryConstants.CATEGORY_ID_TOPIC_COMMON;
        }
        
        Category category = this.categoryProvider.findCategoryById(contentCategory);
        if(category == null) {
            if(LOGGER.isErrorEnabled()) {
                LOGGER.error("Content category not found, userId=" + userId + ", cmd=" + cmd);
            }
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid content category");
        }
        
        post.setCategoryId(contentCategory);
        post.setCategoryPath(category.getPath());
        
        if(cmd.getActionCategory() != null && cmd.getActionCategory().longValue() > 0) {
            category = this.categoryProvider.findCategoryById(cmd.getActionCategory());
            if(category == null) {
                if(LOGGER.isErrorEnabled()) {
                    LOGGER.error("Action category not found, userId=" + userId + ", cmd=" + cmd);
                }
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid action category");
            }
            
            post.setActionCategory(cmd.getActionCategory());
            post.setActionCategoryPath(category.getPath());
        }
    }
    
    private void processPostVisibility(long userId, Long forumId, NewTopicCommand cmd, Post post) {
        // 私有圈和兴趣圈也涉及可见区域（用发帖那一刻的小区ID）        
        VisibleRegionType regionType = VisibleRegionType.fromCode(cmd.getVisibleRegionType());
        if(regionType == null) {
            LOGGER.error("Invaild visible region type, userId=" + userId + ", forumId=" + forumId 
                + ", regionType=" + cmd.getVisibleRegionType());
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid visible region type");
        }
        
        switch(regionType) {
        case COMMUNITY:
            Long communityId = cmd.getVisibleRegionId();
            if(communityId == null) {
                LOGGER.error("Invaild visible region id, userId=" + userId + ", forumId=" + forumId 
                    + ", regionType=" + regionType + ", communityId=" + communityId);
                throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid visible region id");
            }
            Community community = this.communityProvider.findCommunityById(communityId);
            if(community == null) {
                LOGGER.error("Visible region not found, userId=" + userId + ", forumId=" + forumId 
                    + ", regionType=" + regionType + ", communityId=" + communityId);
                throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
                    ForumServiceErrorCode.ERROR_FORUM_VISIBLE_REGION_NOT_FOUND, "Visible region not found");
            }
            break;
        case REGION:
            Long regionId = cmd.getVisibleRegionId();
            if(regionId == null) {
                LOGGER.error("Invaild visible region id, userId=" + userId + ", forumId=" + forumId 
                    + ", regionType=" + regionType + ", regionId=" + regionId);
                throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid visible region id");
            }
            
            Region region = this.regionProvider.findRegionById(regionId);
            if(region == null) {
                LOGGER.error("Visible region not found, userId=" + userId + ", forumId=" + forumId 
                    + ", regionType=" + regionType + ", regionId=" + regionId);
                throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
                    ForumServiceErrorCode.ERROR_FORUM_VISIBLE_REGION_NOT_FOUND, "Visible region not found");
            }
            break;
        default:
            LOGGER.error("Invaild visible region type, userId=" + userId + ", forumId=" + forumId 
                + ", regionType=" + regionType);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE,
                ForumServiceErrorCode.ERROR_FORUM_NOT_FOUND, "Invalid visible region type");
        }
        
        post.setVisibleRegionType(regionType.getCode());
        post.setVisibleRegionId(cmd.getVisibleRegionId());
    }
    
    private void processPostAttachments(long userId, List<AttachmentDescriptor> attachmentList, Post post) {
        List<Attachment> results = null;
        
        if(attachmentList != null) {
            results = new ArrayList<Attachment>();
            
            Attachment attachment = null;
            for(AttachmentDescriptor descriptor : attachmentList) {
                attachment = new Attachment();
                attachment.setCreatorUid(userId);
                attachment.setPostId(post.getId());
                attachment.setContentType(descriptor.getContentType());
                attachment.setContentUri(descriptor.getContentUri());
                attachment.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                
                // Make sure we can save as many attachments as possible even if any of them failed
                try {
                    forumProvider.createPostAttachment(attachment);
                    results.add(attachment);
                } catch(Exception e) {
                    LOGGER.error("Failed to save the attachment, userId=" + userId 
                        + ", attachment=" + attachment, e);
                }
            }
            
            post.setAttachments(results);
        }
    }
    
    private void processPostLocation(long userId, NewTopicCommand cmd, Post post) {
        Double longitude = cmd.getLongitude();
        Double latitude = cmd.getLatitude();
        if(latitude != null && latitude != null) {
            post.setLongitude(longitude);
            post.setLatitude(latitude);
            String geohash = GeoHashUtils.encode(cmd.getLatitude(), cmd.getLongitude());
            post.setGeohash(geohash);
        }
    }
    
//    private Condition buildPostCategoryQryCondition(User user, PostEntityTag entityTag, Community community,  
//        Map<String, Long> gaRegionIdMap, Long contentCategoryId, Long actionCategoryId) {
//        Condition categoryCondition = null;
//        
//        Category contentCatogry = null;
//        // contentCategoryId为0表示全部查，此时也不需要给category条件
//        if(contentCategoryId != null && contentCategoryId.longValue() > 0) {
//            contentCatogry = this.categoryProvider.findCategoryById(contentCategoryId);
//        }
//        if(contentCatogry != null) {
//            categoryCondition = Tables.EH_FORUM_POSTS.CATEGORY_PATH.like(contentCatogry.getPath() + "%");
//        }
//        
//        Category actionCategory = null;
//        if(actionCategoryId != null && actionCategoryId.longValue() > 0) {
//            actionCategory = this.categoryProvider.findCategoryById(actionCategoryId);
//        }
//        if(actionCategory != null) {
//            Condition tempCondition = ForumPostCustomField.ACTION_CATEGORY_PATH.getField().like(actionCategory.getPath() + "%");
//            if(categoryCondition != null) {
//                categoryCondition = categoryCondition.and(tempCondition);
//            } else {
//                categoryCondition = tempCondition;
//            }
//        }
//        
//        Condition userCondition = null;
//        if(entityTag != null) {
//            switch(entityTag) {
//            case USER:
//                userCondition = buildPostQryConditionBySimpleUser(community, VisibilityScope.NEARBY_COMMUNITIES);
//                break;
//            case PM:
//            case GARC:
//            case GANC:
//            case GAPS:
//            case GACW:
//                userCondition = buildGaRelatedPostQryConditionByCategory(user.getId(), entityTag, community, gaRegionIdMap);
//                break;
//            default:
//                if(LOGGER.isWarnEnabled()) {
//                    LOGGER.warn("Entity tag is not supported, userId=" + user.getId() + ", communityId=" + community.getId() + ", entityTag=" + entityTag);
//                }
//            }
//        } else {
//            if(LOGGER.isInfoEnabled()) {
//                LOGGER.info("Entity tag is null, userId=" + user.getId() + ", communityId=" + community.getId() + ", entityTag=" + entityTag);
//            }
//        }
//        
//        if(userCondition == null) {
//            return categoryCondition;
//        } else {
//            if(categoryCondition == null) {
//                return userCondition;
//            } else {
//                return categoryCondition.and(userCondition);
//            }
//        }
//    }
    
    private Condition buildPostCategoryQryCondition(User user, PostEntityTag entityTag, Community community,  
        Long contentCategoryId, Long actionCategoryId) {
        Condition categoryCondition = null;
        
        Category contentCatogry = null;
        // contentCategoryId为0表示全部查，此时也不需要给category条件
        if(contentCategoryId != null && contentCategoryId.longValue() > 0) {
            contentCatogry = this.categoryProvider.findCategoryById(contentCategoryId);
        }
        if(contentCatogry != null) {
            categoryCondition = Tables.EH_FORUM_POSTS.CATEGORY_PATH.like(contentCatogry.getPath() + "%");
        }
        
        Category actionCategory = null;
        if(actionCategoryId != null && actionCategoryId.longValue() > 0) {
            actionCategory = this.categoryProvider.findCategoryById(actionCategoryId);
        }
        if(actionCategory != null) {
            Condition tempCondition = ForumPostCustomField.ACTION_CATEGORY_PATH.getField().like(actionCategory.getPath() + "%");
            if(categoryCondition != null) {
                categoryCondition = categoryCondition.and(tempCondition);
            } else {
                categoryCondition = tempCondition;
            }
        }
        
        Condition userCondition = null;
        // 对于entityTag为普通用户，则是查普通用户所有能看到的帖（加上类型限制）：
        // 1、若是政府相关类型的帖，住宅小区相关的帖的可见范围缩小到本小区范围
        // 2、其它帖按指定小区进行限制可见范围
        if(entityTag == null || entityTag == PostEntityTag.USER) {
            VisibilityScope scope = VisibilityScope.NEARBY_COMMUNITIES;
            if(contentCategoryId != null && CategoryConstants.GA_RELATED_CATEGORIES.contains(contentCategoryId)) {
                scope = VisibilityScope.COMMUNITY;
            }
            userCondition = buildDefaultForumPostQryConditionForCommunity(user, community, scope);
        } else {
            // 对于指定entityTag，则对creatorTag和targetTag进行限制
            // Condition privacyCondition = Tables.EH_FORUM_POSTS.PRIVATE_FLAG.notEqual(PostPrivacy.PRIVATE.getCode());
            Condition entityCondition = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(entityTag.getCode());
            entityCondition = entityCondition.or(Tables.EH_FORUM_POSTS.TARGET_TAG.eq(entityTag.getCode()));
            //Condition visibleCondition = buildDefaultForumPostQryConditionByOrganization(user, community);
            // 对于物业等政府相关的类型，则只查本小区的
            VisibilityScope scope = VisibilityScope.NEARBY_COMMUNITIES;
            if(contentCatogry != null && CategoryConstants.GA_RELATED_CATEGORIES.contains(contentCatogry.getId())) {
                scope = VisibilityScope.COMMUNITY;
            }
            Condition visibleCondition = buildDefaultForumPostQryConditionForCommunity(user, community, scope);
            if(visibleCondition != null) {
                userCondition = entityCondition.and(visibleCondition);
            } else {
                userCondition = entityCondition;
            }
        }
        
        if(userCondition == null) {
            return categoryCondition;
        } else {
            if(categoryCondition == null) {
                return userCondition;
            } else {
                return categoryCondition.and(userCondition);
            }
        }
    }
    
//    private Condition buildPostQryConditionByUserRelated(User user, Community community, Map<String, Long> gaRegionIdMap) {
//        VisibilityScope scope = VisibilityScope.NEARBY_COMMUNITIES;
//        Condition systemForumCondition = buildPostVisibilityQryCondition(user, community, gaRegionIdMap, scope);
//        systemForumCondition = Tables.EH_FORUM_POSTS.FORUM_ID.eq(ForumConstants.SYSTEM_FORUM).and(systemForumCondition);
//        
//        List<UserGroup> userGroupList = userProvider.listUserGroups(user.getId(), GroupDiscriminator.GROUP.getCode());
//        List<Long> forumIdList = new ArrayList<Long>();
//        for(UserGroup userGroup : userGroupList) {
//            Group group = groupProvider.findGroupById(userGroup.getGroupId());
//            // 不查私有圈对应的论坛帖
//            if(group != null && GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PUBLIC 
//                && group.getOwningForumId() != null) {
//                forumIdList.add(group.getOwningForumId());
//            }
//        }
//        
//        Condition otherForumCondition = null;
//        if(forumIdList.size() > 0) {
//            otherForumCondition = Tables.EH_FORUM_POSTS.FORUM_ID.in(forumIdList);
//        }
//        
//        if(systemForumCondition != null) {
//            if(otherForumCondition == null) {
//                return systemForumCondition;
//            } else {
//                return systemForumCondition.or(otherForumCondition);
//            }
//        } else {
//            return otherForumCondition;
//        }
//    }
    
    private Condition buildPostQryConditionByUserRelated(User user, Community community) {
        Long userId = -1L;
        if(user != null) {
            userId = user.getId();
        }
        
        VisibilityScope scope = VisibilityScope.NEARBY_COMMUNITIES;
        Condition systemForumCondition = buildDefaultForumPostQryConditionForCommunity(user, community, scope);
        Long forumId = community.getDefaultForumId();
        if(forumId == null || forumId <= 0) {
            LOGGER.error("No default forum is found for community, system forum will used, userId=" + userId 
                + ", communityId=" + community.getId());
            forumId = ForumConstants.SYSTEM_FORUM;
        }
        systemForumCondition = Tables.EH_FORUM_POSTS.FORUM_ID.eq(forumId).and(systemForumCondition);
        
        List<UserGroup> userGroupList = userProvider.listUserGroups(user.getId(), GroupDiscriminator.GROUP.getCode());
        List<Long> forumIdList = new ArrayList<Long>();
        for(UserGroup userGroup : userGroupList) {
            Group group = groupProvider.findGroupById(userGroup.getGroupId());
            // 不查私有圈对应的论坛帖
            if(group != null && GroupPrivacy.fromCode(group.getPrivateFlag()) == GroupPrivacy.PUBLIC 
                && group.getOwningForumId() != null) {
                forumIdList.add(group.getOwningForumId());
            }
        }
        
        Condition otherForumCondition = null;
        if(forumIdList.size() > 0) {
            otherForumCondition = Tables.EH_FORUM_POSTS.FORUM_ID.in(forumIdList);
        }
        
        if(otherForumCondition == null) {
            return systemForumCondition;
        } else {
            return systemForumCondition.or(otherForumCondition);
        }
    }
    
    private Condition buildPostVisibilityQryCondition(User user, Community community, Map<String, Long> gaRegionIdMap, VisibilityScope scope) {
        Condition simpleUserCondition = buildPostQryConditionBySimpleUser(community, scope);
        Condition gaRelatedCondition = buildGaRelatedPostQryConditionByUser(user.getId(), community, gaRegionIdMap);
        Condition assignedScopeCondition = buildPostQryConditionByAssignedScope(community);
        
        return simpleUserCondition.or(gaRelatedCondition).or(assignedScopeCondition);
    }
    
    private Condition buildPostQryConditionBySimpleUser(Community community, VisibilityScope scope) {
    	long communityId = community.getId();;
    	
        Condition c1 = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(PostEntityTag.USER.getCode());
        c1 = c1.and(Tables.EH_FORUM_POSTS.TARGET_TAG.eq(PostEntityTag.USER.getCode()));
        c1 = c1.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode()));
        switch(scope) {
        case COMMUNITY:
            c1 = c1.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(communityId));
            break;
        default: // default to query topics in nearby communities
            List<Long> nearbyCmntyIdList = new ArrayList<Long>();
            List<Community> nearbyCmntyList = new ArrayList<Community>();
            if(community.getStatus() == CommunityAdminStatus.ACTIVE.getCode())
            	nearbyCmntyList = communityProvider.findNearyByCommunityById(UserContext.getCurrentNamespaceId(UserContext.current().getNamespaceId()), communityId);
            
            for(Community c : nearbyCmntyList) {
                nearbyCmntyIdList.add(c.getId());
            }
            
            List<Long> tmpCmntyIdList = new ArrayList<Long>();
            tmpCmntyIdList.add(communityId);
            if(nearbyCmntyIdList != null && nearbyCmntyIdList.size() > 0) {
                tmpCmntyIdList.addAll(nearbyCmntyIdList);
            }
            c1 = c1.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.in(tmpCmntyIdList));
        }
        
        return c1;
    }
    
    /**
     * 普通用户查询政府相关帖子，包含两种情况：
     * 1、普通用户发给物业/业委/居委/公安/社区工作站等的、且是公开的（自己发的且是私有的则是自己看得到）
     * 2、物业/业委/居委/公安/社区工作站等自己发的、且范围是指定范围的
     * @param userId 用户ID
     * @param community 用户所在小区
     * @param gaRegionIdMap 政府机构类型和区域ID的映射表
     * @return 查询条件
     */
    private Condition buildGaRelatedPostQryConditionByUser(long userId, Community community, Map<String, Long> gaRegionIdMap) {
        long communityId = community.getId();
                
        // 普通用户发给物业/业委/居委/公安/社区工作站等的、且是公开的（自己发的且是私有的则是自己看得到）
        Condition c1 = Tables.EH_FORUM_POSTS.TARGET_TAG.eq(PostEntityTag.PM.getCode());
        c1 = c1.or(Tables.EH_FORUM_POSTS.TARGET_TAG.eq(PostEntityTag.GARC.getCode()));
        c1 = c1.or(Tables.EH_FORUM_POSTS.TARGET_TAG.eq(PostEntityTag.GANC.getCode()));
        c1 = c1.or(Tables.EH_FORUM_POSTS.TARGET_TAG.eq(PostEntityTag.GAPS.getCode()));
        c1 = c1.or(Tables.EH_FORUM_POSTS.TARGET_TAG.eq(PostEntityTag.GACW.getCode()));


        Condition c2 = Tables.EH_FORUM_POSTS.PRIVATE_FLAG.eq(PostPrivacy.PUBLIC.getCode());
        c2 = c2.or(Tables.EH_FORUM_POSTS.CREATOR_UID.eq(userId));
        
        Condition c3 = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(PostEntityTag.USER.getCode());
        c3 = c3.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode()));
        c1 = c1.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(communityId));
        c3 = c1.and(c2).and(c3);
        
        
        // 物业/业委/居委/公安/社区工作站等自己发的、且范围是指定范围的
        Condition pmCondition = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(PostEntityTag.PM.getCode());
        pmCondition = pmCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode()));
        pmCondition = pmCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(gaRegionIdMap.get(PostEntityTag.PM.getCode())));
        
        Condition garcCondition = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(PostEntityTag.GARC.getCode());
        garcCondition = garcCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode()));
        garcCondition = garcCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(gaRegionIdMap.get(PostEntityTag.GARC.getCode())));
        
        Condition gancCondition = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(PostEntityTag.GANC.getCode());
        gancCondition = gancCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.REGION.getCode()));
        gancCondition = gancCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(gaRegionIdMap.get(PostEntityTag.GANC.getCode())));
        
        Condition gapsCondition = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(PostEntityTag.GAPS.getCode());
        gapsCondition = gapsCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.REGION.getCode()));
        gapsCondition = gapsCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(gaRegionIdMap.get(PostEntityTag.GAPS.getCode())));
        
        Condition gacwCondition = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(PostEntityTag.GACW.getCode());
        gapsCondition = gapsCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.REGION.getCode()));
        gapsCondition = gapsCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(gaRegionIdMap.get(PostEntityTag.GACW.getCode())));
        
        return c3.or(pmCondition).or(pmCondition).or(garcCondition).or(gancCondition).or(gapsCondition).or(gacwCondition);
    }

    
    /**
     * 普通用户查询政府相关帖子，包含两种情况：
     * 1、普通用户发给物业/业委/居委/公安/社区工作站等的、且是公开的（自己发的且是私有的则是自己看得到）
     * 2、物业/业委/居委/公安/社区工作站等自己发的、且范围是指定范围的
     * @param userId 用户ID
     * @param community 用户所在小区
     * @param gaRegionIdMap 政府机构类型和区域ID的映射表
     * @return 查询条件
     */
    private Condition buildGaRelatedPostQryConditionByCategory(Long userId, PostEntityTag entityTag, Community community, Map<String, Long> gaRegionIdMap) {
        // 普通用户发给物业/业委/居委/公安/社区工作站等的、且是公开的（自己发的且是私有的则是自己看得到）
        // 普通用户发的帖均在自己小区内（不管是发给物业的还是给公安的）
        Condition userCondition1 = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(PostEntityTag.USER.getCode());
        userCondition1 = userCondition1.and(Tables.EH_FORUM_POSTS.TARGET_TAG.eq(entityTag.getCode()));
        Condition userCondition2 = Tables.EH_FORUM_POSTS.PRIVATE_FLAG.eq(PostPrivacy.PUBLIC.getCode());
        userCondition2 = userCondition2.or(Tables.EH_FORUM_POSTS.CREATOR_UID.eq(userId));
        Condition userCondition3 = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode());
        userCondition3 = userCondition3.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(community.getId()));
        Condition userCondition = userCondition1.and(userCondition2).and(userCondition3);
                
        // 物业/业委/居委/公安/社区工作站等自己发的、且范围是指定范围的（所发的区域ID有可能是小区ID也有可能是片区ID）
        Condition gaCondition = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(entityTag.getCode());
        Condition regionCondition = null;
        switch(entityTag) {
        case PM:
        case GARC:
            regionCondition = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode());
            break;
        case GANC:
        case GAPS:
        case GACW:
            regionCondition = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.REGION.getCode());
            break;
        default:
            if(LOGGER.isWarnEnabled()) {
                LOGGER.warn("Entity tag is not supported, userId=" + userId + ", communityId=" + community.getId() + ", entityTag=" + entityTag);
            }
        }
        if(regionCondition != null) {
            regionCondition = regionCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(gaRegionIdMap.get(entityTag.getCode())));
            gaCondition = gaCondition.and(regionCondition);
        } else {
            LOGGER.error("Region condition is null, userId=" + userId + ", communityId=" + community.getId() + ", entityTag=" + entityTag);
            return null;
        }
        
        return userCondition.or(gaCondition);
    }
    
    /**
     * 机构自己查自己可看的帖子列表
     * @param userId 用户ID
     * @param organization 机构
     * @param communityIdList 机构管理的小区列表
     * @return 条件
     */
    private Condition buildGaRelatedPostQryConditionByOrganization(Long userId, Organization organization, List<Long> communityIdList) {
        OrganizationType orgType = OrganizationType.fromCode(organization.getOrganizationType());
        if(orgType == null) {
            LOGGER.error("Unsupported organization type, userId=" + userId + ", organization=" + organization);
            return null;
        }
        
        // 普通用户发给该机构的（物业/业委/居委/公安/社区工作站等），该机构都能看得到（不管是否公开）
        Condition userCondition1 = Tables.EH_FORUM_POSTS.TARGET_TAG.eq(orgType.getCode());
        Condition userCondition3 = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode());
        userCondition3 = userCondition3.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.in(communityIdList));
        Condition userCondition = userCondition1.and(userCondition3);
                
        // 机构（物业/业委/居委/公安/社区工作站等）自己发的，该机构都能看得到
        Condition gaCondition = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(orgType.getCode());
        Condition regionCondition = null;
        switch(orgType) {
        case PM:
        case GARC:
            regionCondition = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode());
            if(communityIdList.size() > 0) {
                regionCondition = regionCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(communityIdList.get(0)));
            }
            gaCondition = gaCondition.and(regionCondition);
            break;
        case GANC:
        case GAPS:
        case GACW:
            regionCondition = Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.REGION.getCode());
            regionCondition = regionCondition.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(organization.getId()));
            gaCondition = gaCondition.and(regionCondition);
            break;
        default:
            if(LOGGER.isWarnEnabled()) {
                LOGGER.warn("Entity tag is not supported, userId=" + userId + ", organizationId=" + organization.getId() 
                    + ", communityIdList=" + communityIdList);
            }
        }
        
        return userCondition.or(gaCondition);
    }
    
    private Condition buildPostQryConditionByAssignedScope(Community community) {
        long communityId = community.getId();
        
        Condition c1 = Tables.EH_FORUM_ASSIGNED_SCOPES.SCOPE_CODE.eq(VisibilityScope.COMMUNITY.getCode());
        c1 = c1.and(Tables.EH_FORUM_ASSIGNED_SCOPES.SCOPE_ID.eq(communityId));
        
        Condition c2 = Tables.EH_FORUM_ASSIGNED_SCOPES.SCOPE_CODE.eq(VisibilityScope.CITY.getCode());
        c2 = c2.and(Tables.EH_FORUM_ASSIGNED_SCOPES.SCOPE_ID.eq(community.getCityId()));

        Condition c3 = Tables.EH_FORUM_ASSIGNED_SCOPES.SCOPE_CODE.eq(VisibilityScope.ALL.getCode());
        
        return c1.or(c2).or(c3);
    }
    
    private Condition buildPostCategoryCondition(Long contentCategoryId, Long actionCategoryId) {
        Condition categoryCondition = null;
        // contentCategoryId为0表示全部查，此时也不需要给category条件
        if(contentCategoryId != null && contentCategoryId.longValue() > 0) {
            Category contentCatogry = this.categoryProvider.findCategoryById(contentCategoryId);
            if(contentCatogry != null) {
                categoryCondition = Tables.EH_FORUM_POSTS.CATEGORY_PATH.like(contentCatogry.getPath() + "%");
            }
        }
        
        if(actionCategoryId != null && actionCategoryId.longValue() > 0) {
            Category actionCategory = this.categoryProvider.findCategoryById(actionCategoryId);
            if(actionCategory != null) {
                Condition tempCondition = ForumPostCustomField.ACTION_CATEGORY_PATH.getField().like(actionCategory.getPath() + "%");
                if(categoryCondition != null) {
                    categoryCondition = categoryCondition.and(tempCondition);
                } else {
                    categoryCondition = tempCondition;
                }
            }
        }
        
        return categoryCondition;
    }
    
    /**
     * 填充帖子中每个帖子的信息，主要是补充发帖/评论人信息和附件信息
     * @param userId 查帖人
     * @param postList 帖子/评论列表
     * @param communityId 当前用户所在小区ID
     * @param isDetail 是否查的是详情（详情的填充内容和列表填充内容略有不同，后者简略一些）
     */
    private void populatePosts(long userId, List<Post> postList, Long communityId, boolean isDetail) {
        if(postList == null || postList.size() == 0) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("The post list is empty, userId=" + userId);
            }
            return;
        } else {
            for(Post post : postList) {
                populatePost(userId, post, communityId, isDetail);
            }
        }
    }
    
    private void populatePost(long userId, Post post, Long communityId, boolean isDetail) {
        populatePost(userId, post, communityId, isDetail, false);
    }
    
    /**
     * 填充帖子信息，主要是补充发帖/评论人信息和附件信息
     * @param userId 查帖人
     * @param post 帖子/评论
     * @param communityId 用户当前所在的小区ID
     * @param isDetail 是否查的是详情（详情的填充内容和列表填充内容略有不同，后者简略一些）
     */
    private void populatePost(long userId, Post post, Long communityId, boolean isDetail, boolean getOwnTopics) {
        long startTime = System.currentTimeMillis();
        if(post == null) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("The post is null, userId=" + userId);
            }
        } else {
            try {
                Long embededAppId = post.getEmbeddedAppId();
                ForumEmbeddedHandler handler = getForumEmbeddedHandler(embededAppId);
                if(handler != null) {
                    String snapshot = null;
                    if(isDetail) {
                        snapshot = handler.renderEmbeddedObjectDetails(post);
                    } else {
                        snapshot = handler.renderEmbeddedObjectSnapshot(post);
                    }
                    post.setEmbeddedJson(snapshot);
                }
                
                post.setCommunityId(communityId);
                
                populatePostCreatorInfo(userId, post);
                
                populatePostAttachements(userId, post, post.getAttachments());
                
                populatePostStatus(userId, post, getOwnTopics);
                
                populatePostRegionInfo(userId, post);
                
                populatePostForumNameInfo(userId, post);
                
                String homeUrl = configProvider.getValue(ConfigConstants.HOME_URL, "");
                String relativeUrl = configProvider.getValue(ConfigConstants.POST_SHARE_URL, "");
                if(homeUrl.length() == 0 || relativeUrl.length() == 0) {
                    LOGGER.error("Invalid home url or post sharing url, homeUrl=" + homeUrl 
                        + ", relativeUrl=" + relativeUrl + ", postId=" + post.getId());
                } else {
                    post.setShareUrl(homeUrl + relativeUrl + "?forumId=" + post.getForumId() + "&topicId=" + post.getId());
                }
            } catch(Exception e) {
                LOGGER.error("Failed to populate the post info, userId=" + userId + ", postId=" + post.getId(), e);
            }
        }
        
        long endTime = System.currentTimeMillis();
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("Populate post, userId=" + userId + ", postId=" + post.getId() + ", elapse=" + (endTime - startTime));
        }
    }
    
//    private void populatePostCreatorInfo(long userId, Post post) {
//        Long forumId = post.getForumId();
//        if(forumId != null) {
//            String creatorNickName = post.getCreatorNickName();
//            String creatorAvatar = post.getCreatorAvatar();
//            
//            // 社区论坛的意见反馈论坛直接使用USER表中的信息作为发帖人的信息
//            if(forumId == ForumConstants.SYSTEM_FORUM || forumId == ForumConstants.FEEDBACK_FORUM) {
//                User creator = userProvider.findUserById(post.getCreatorUid());
//                if(creator != null) {
//                    // 优先使用帖子里存储的昵称和头像（2.8转过来的数据会有这些昵称和头像，因为在2.8不同家庭有不同的昵称）
//                    if(creatorNickName == null || creatorNickName.trim().length() == 0) {
//                        post.setCreatorNickName(creator.getNickName());
//                    }
//                    if(creatorAvatar == null || creatorAvatar.trim().length() == 0) {
//                        post.setCreatorAvatar(creator.getAvatar());
//                    }
//                }
//                // TODO: set the admin flag of system forum
//            } else {
//                // 普通圈使用圈成员的信息
//                Forum forum = forumProvider.findForumById(forumId);
//                if(forum != null && EntityType.GROUP.getCode().equalsIgnoreCase(forum.getOwnerType())) {
//                    GroupMember member = groupProvider.findGroupMemberByMemberInfo(forum.getOwnerId(), 
//                        EntityType.USER.getCode(), post.getCreatorUid());
//                    if(member != null) {
//                        String nickName = member.getMemberNickName();
//                        String avatar = member.getMemberAvatar();
//                        if(nickName == null || nickName.trim().length() == 0) {
//                            User creator = userProvider.findUserById(post.getCreatorUid());
//                            if(creator != null) {
//                                nickName = creator.getNickName();
//                            }
//                        }
//                        if(avatar == null || avatar.trim().length() == 0){
//                        	User creator = userProvider.findUserById(post.getCreatorUid());
//                        	if(creator != null) {
//                        		avatar = creator.getAvatar();
//                            }
//                        }
//                        // 优先使用帖子里存储的昵称和头像（2.8转过来的数据会有这些昵称和头像，因为在2.8不同家庭有不同的昵称）
//                        if(creatorNickName == null || creatorNickName.trim().length() == 0) {
//                            post.setCreatorNickName(nickName);
//                        }
//                        if(creatorAvatar == null || creatorAvatar.trim().length() == 0) {
//                            post.setCreatorAvatar(avatar);
//                        }
//                    }
//                }
//            }
//            
//            creatorAvatar = post.getCreatorAvatar();
//            if(creatorAvatar != null && creatorAvatar.length() > 0) {
//                String avatarUrl = getResourceUrlByUir(userId, creatorAvatar, 
//                    EntityType.USER.getCode(), post.getCreatorUid());
//                post.setCreatorAvatarUrl(avatarUrl);
//            }
//        }
//        
//        UserLike userLike = this.userProvider.findUserLike(userId, EntityType.POST.getCode(), post.getId());
//        if(userLike != null && UserLikeType.LIKE == UserLikeType.fromCode(userLike.getLikeType())) {
//            post.setLikeFlag(UserLikeType.LIKE.getCode());
//        } else {
//            post.setLikeFlag(UserLikeType.NONE.getCode());
//        }
//    }
    
    private void populatePostCreatorInfo(long userId, Post post) {
        // 优先使用帖子里存储的昵称和头像（2.8转过来的数据会有这些昵称和头像，因为在2.8不同家庭有不同的昵称）
        String creatorNickName = post.getCreatorNickName();
        String creatorAvatar = post.getCreatorAvatar();
        
        Long forumId = post.getForumId();
        if(forumId != null) {
            // 普通圈使用圈成员的信息
            Forum forum = forumProvider.findForumById(forumId);
            if(forum != null && EntityType.GROUP.getCode().equalsIgnoreCase(forum.getOwnerType())) {
                GroupMember member = groupProvider.findGroupMemberByMemberInfo(forum.getOwnerId(), 
                    EntityType.USER.getCode(), post.getCreatorUid());
                if(member != null) {
                    creatorNickName = member.getMemberNickName();
                    creatorAvatar = member.getMemberAvatar();
                    if(creatorNickName == null || creatorNickName.trim().length() == 0) {
                        creatorNickName = member.getMemberNickName();
                    }
                    if(creatorAvatar == null || creatorAvatar.trim().length() == 0){
                        creatorAvatar = member.getMemberAvatar();
                    }
                }
            }
        }
        
        // 无昵称时直接使用USER表中的信息作为发帖人的信息
        User creator = userProvider.findUserById(post.getCreatorUid());
        if(creator != null) {
            // 优先使用帖子里存储的昵称和头像（2.8转过来的数据会有这些昵称和头像，因为在2.8不同家庭有不同的昵称）
            if(creatorNickName == null || creatorNickName.trim().length() == 0) {
                creatorNickName = creator.getNickName();
            }
            if(creatorAvatar == null || creatorAvatar.trim().length() == 0) {
                creatorAvatar = creator.getAvatar();
            }
        }

        post.setCreatorNickName(creatorNickName);
        post.setCreatorAvatar(creatorAvatar);
        
        if(creatorAvatar != null && creatorAvatar.length() > 0) {
            String avatarUrl = getResourceUrlByUir(userId, creatorAvatar, 
                EntityType.USER.getCode(), post.getCreatorUid());
            post.setCreatorAvatarUrl(avatarUrl);
        }
        
        UserLike userLike = this.userProvider.findUserLike(userId, EntityType.POST.getCode(), post.getId());
        if(userLike != null && UserLikeType.LIKE == UserLikeType.fromCode(userLike.getLikeType())) {
            post.setLikeFlag(UserLikeType.LIKE.getCode());
        } else {
            post.setLikeFlag(UserLikeType.NONE.getCode());
        }
    }
    
    private void populatePostStatus(long userId, Post post, boolean getOwnTopics) {
        if(getOwnTopics && post.getCreatorUid().equals(userId)) {
            return;
        }
        
        if(PostStatus.INACTIVE == PostStatus.fromCode(post.getStatus())) {
            User user = this.userProvider.findUserById(userId);
            String locale = user.getLocale();
            String scope = ForumLocalStringCode.SCOPE;
            int code = 0;
            String content = null;
            if(post.getParentPostId() == null || post.getParentPostId().longValue() == 0) {
                code = ForumLocalStringCode.FORUM_TOPIC_DELETED;
                content = localeStringService.getLocalizedString(scope, String.valueOf(code), locale, "");
            } else {
                code = ForumLocalStringCode.FORUM_COMMENT_DELETED;
                content = localeStringService.getLocalizedString(scope, String.valueOf(code), locale, "");
            }
            post.setContent(content);
            post.setContentAbstract(content);
            
            post.setEmbeddedAppId(null);
            post.setEmbeddedId(null);
            post.setEmbeddedJson(null);
            post.setEmbeddedVersion(0);
            post.setAttachments(null);
        }
    }
    
    private void populatePostRegionInfo(long userId, Post post) {
        VisibleRegionType regionType = VisibleRegionType.fromCode(post.getVisibleRegionType());
        Long regionId = post.getVisibleRegionId();
        
        if(regionType != null && regionId != null) {
            String creatorNickName = post.getCreatorNickName();
            if(creatorNickName == null) {
                creatorNickName = "";
            }
            switch(regionType) {
            case COMMUNITY:
                Community community = communityProvider.findCommunityById(regionId);
                if(community != null)
                	creatorNickName = creatorNickName + "@" + community.getName();
                break;
            case REGION:
                Organization organization = organizationProvider.findOrganizationById(regionId);
                if(organization !=null)
                	creatorNickName = creatorNickName + "@" + organization.getName();
                break;
            default:
                LOGGER.error("Unsupported visible region type, userId=" + userId 
                    + ", regionType=" + regionType + ", postId=" + post.getId());
            }
            post.setCreatorNickName(creatorNickName);
        } else {
            LOGGER.error("Region type or id is null, userId=" + userId + ", postId=" + post.getId());
        }
        
    }
    
    private void populatePostForumNameInfo(long userId, Post post) {
        Long forumId = post.getForumId();
        Forum forum = forumProvider.findForumById(forumId);
        
        if(forumId == ForumConstants.SYSTEM_FORUM) {
            VisibleRegionType regionType = VisibleRegionType.fromCode(post.getVisibleRegionType());
            Long regionId = post.getVisibleRegionId();
            if(regionType != null && regionId != null) {
                String forumName = forum.getName();
                if(forumName == null) {
                    forumName = "";
                }
                switch(regionType) {
                case COMMUNITY:
                    Community community = communityProvider.findCommunityById(regionId);
                    if(community != null)
                    	forumName = community.getName() + forumName;
                    break;
                case REGION:
                    Organization organization = organizationProvider.findOrganizationById(regionId);
                    if(organization !=null)
                    	forumName = organization.getName();
                    break;
                default:
                    LOGGER.error("Unsupported visible region type, userId=" + userId 
                        + ", regionType=" + regionType + ", postId=" + post.getId());
                }
                post.setForumName(forumName);
            } else {
                LOGGER.error("Region type or id is null, userId=" + userId + ", postId=" + post.getId());
            }
        } else {
            if(forumId == ForumConstants.FEEDBACK_FORUM) {
                post.setForumName(forum.getName());
            } else {
                Group group = this.groupProvider.findGroupById(forum.getOwnerId());
                if(group != null) {
                    post.setForumName(group.getName());
                }
            }
        }
    }
    
    private void populatePostAttachements(long userId, Post post, List<Attachment> attachmentList) {
        if(attachmentList == null || attachmentList.size() == 0) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("The post attachment list is empty, userId=" + userId + ", postId=" + post.getId());
            }
        } else {
            for(Attachment attachment : attachmentList) {
                populatePostAttachement(userId, post, attachment);
            }
        }
    }
    
    private void populatePostAttachement(long userId, Post post, Attachment attachment) {
        if(attachment == null) {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("The post attachment is null, userId=" + userId + ", postId=" + post.getId());
            }
        } else {
            String contentUri = attachment.getContentUri();
            if(contentUri != null && contentUri.length() > 0) {
                try{
                    String url = contentServerService.parserUri(contentUri, EntityType.TOPIC.getCode(), post.getId());
                    attachment.setContentUrl(url);
                    
                    ContentServerResource resource = contentServerService.findResourceByUri(contentUri);
                    if(resource != null) {
                        attachment.setSize(resource.getResourceSize());
                        attachment.setMetadata(resource.getMetadata());
                    }
                }catch(Exception e){
                    LOGGER.error("Failed to parse attachment uri, userId=" + userId 
                        + ", postId=" + post.getId() + ", attachmentId=" + attachment.getId(), e);
                }
            } else {
                if(LOGGER.isWarnEnabled()) {
                    LOGGER.warn("The content uri is empty, userId=" + userId + ", attchmentId=" + attachment.getId());
                }
            }
        }
    }
    
    private String getResourceUrlByUir(long userId, String uri, String ownerType, Long ownerId) {
        String url = null;
        if(uri != null && uri.length() > 0) {
            try{
                url = contentServerService.parserUri(uri, ownerType, ownerId);
            }catch(Exception e){
                LOGGER.error("Failed to parse uri, userId=" + userId + ", uri=" + uri 
                    + ", ownerType=" + ownerType + ", ownerId=" + ownerId, e);
            }
        }
        
        return url;
    }
    
    private ForumEmbeddedHandler getForumEmbeddedHandler(Long embededAppId) {
        ForumEmbeddedHandler handler = null;
        
        if(embededAppId != null && embededAppId.longValue() > 0) {
            String handlerPrefix = ForumEmbeddedHandler.FORUM_EMBEDED_OBJ_RESOLVER_PREFIX;
            handler = PlatformContext.getComponent(handlerPrefix + embededAppId);
        }
        
        return handler;
    }

	@Override
	public SearchTopicAdminCommandResponse searchComment(SearchTopicAdminCommand cmd) {
		PostAdminQueryFilter filter = new PostAdminQueryFilter();
        String keyword = cmd.getKeyword();
        if(!StringUtils.isEmpty(keyword)) {
            filter.addQueryTerm(PostAdminQueryFilter.TERM_CONTENT);
            filter.addQueryTerm(PostAdminQueryFilter.TERM_SUBJECT);
            filter.addQueryTerm(PostAdminQueryFilter.TERM_CREATORNICKNAME);
            filter.setQueryString(keyword);
        }
        
        List<String> phones = cmd.getSenderPhones();
        if(phones != null && phones.size() > 0) {
            filter.includeFilter(PostAdminQueryFilter.TERM_IDENTIFY, phones);
        }
        
//        List<String> nickNames = cmd.getSenderNickNames();
//        if(nickNames != null && nickNames.size() > 0) {
//            filter.includeFilter(PostAdminQueryFilter.TERM_CREATORNICKNAME, nickNames);
//        }
        
        List<Long> parentPostId = new ArrayList<Long>();
        parentPostId.add(0L);
        filter.excludeFilter(PostAdminQueryFilter.TERM_PARENTPOSTID, parentPostId);

        
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
                PostDTO temp = getTopicById(post.getId(), cmd.getCommunityId(), false);
                PostAdminDTO adminPost = ConvertHelper.convert(temp, PostAdminDTO.class);
                UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(temp.getCreatorUid(),
                    IdentifierType.MOBILE.getCode());
                if(identifier != null) {
                    adminPost.setCreatorPhone(identifier.getIdentifierToken());
                }
                
                List<FamilyDTO> families = familyProvider.getUserFamiliesByUserId(temp.getCreatorUid());
                if(families != null && families.size() > 0){
                	String address = families.get(0).getAddress();
                	adminPost.setCreatorAddress(address);
                }
                
                adminPostList.add(adminPost);
            } catch(Exception e) {
                LOGGER.error(e.toString());
            }
        }
        
        response.setPosts(adminPostList);
        return response;
	}
}
