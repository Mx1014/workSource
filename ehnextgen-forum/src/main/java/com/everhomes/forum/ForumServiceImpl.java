// @formatter:off

package com.everhomes.forum;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
import com.everhomes.acl.PrivilegeConstants;
import com.everhomes.acl.ResourceUserRoleResolver;
import com.everhomes.acl.Role;
import com.everhomes.app.AppConstants;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryConstants;
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
import com.everhomes.forum.admin.PostAdminDTO;
import com.everhomes.forum.admin.SearchTopicAdminCommand;
import com.everhomes.forum.admin.SearchTopicAdminCommandResponse;
import com.everhomes.group.Group;
import com.everhomes.group.GroupDiscriminator;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupPrivacy;
import com.everhomes.group.GroupProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.OrganizationType;
import com.everhomes.point.AddUserPointCommand;
import com.everhomes.point.PointType;
import com.everhomes.point.UserPointService;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.search.PostAdminQueryFilter;
import com.everhomes.search.PostSearcher;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhUsers;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.IdentifierType;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserGroup;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserLike;
import com.everhomes.user.UserLikeType;
import com.everhomes.user.UserProfileContstant;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.visibility.VisibilityScope;
import com.everhomes.visibility.VisibleRegionType;

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
                    return null;
                });
            } catch(Exception e) {
                LOGGER.error("Failed to update the view count of post, userId=" + userId + ", postId=" + postId, e);
            }
            PostDTO postDto =  getTopicById(postId, cmd.getCommunityId(), true);
            
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
    
    @Override
    public List<PostDTO> getTopicById(List<Long> topicIds, Long communityId, boolean isDetail) {
        List<PostDTO> postDtoList = new ArrayList<PostDTO>();

        PostDTO postDto = null;
        for(Long topicId : topicIds) {
            try {
                postDto = getTopicById(topicId, communityId, isDetail);
                postDtoList.add(postDto);
            } catch(Exception e) {
                LOGGER.error(e.toString());
            }
        }
        
        return postDtoList;
    }
    
    @Override
    public PostDTO getTopicById(Long topicId, Long communityId, boolean isDetail) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        
        Post post = checkPostParameter(userId, -1L, topicId, "getTopicById");
        if(post != null) {
            if(PostStatus.ACTIVE != PostStatus.fromCode(post.getStatus())) {
            	if(!(post.getCreatorUid() != post.getDeleterUid() && post.getCreatorUid() == userId)){
            		LOGGER.error("Forum post already deleted, userId=" + userId + ", topicId=" + topicId);
            		throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
            				ForumServiceErrorCode.ERROR_FORUM_TOPIC_DELETED, "Forum post already deleted");
            	}
            }
            
            this.forumProvider.populatePostAttachments(post);
            populatePost(userId, post, communityId, isDetail);
            
            return ConvertHelper.convert(post, PostDTO.class);
        } else {
            LOGGER.error("Forum post not found, userId=" + userId + ", topicId=" + topicId);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
                ForumServiceErrorCode.ERROR_FORUM_TOPIC_NOT_FOUND, "Forum post not found");
        }
    }
    
    @Override
    public void deletePost(Long forumId, Long postId) {
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
                    if(userId.equals(post.getCreatorUid())){
                    	userActivityProvider.updateProfileIfNotExist(post.getCreatorUid(), UserProfileContstant.POSTED_TOPIC_COUNT, -1);
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
        long userId = user.getId();
        Long communityId = cmd.getCommunityId();
        Community community = checkCommunityParameter(userId, communityId, "queryTopicsByCategory");
        
        Long forumId = ForumConstants.SYSTEM_FORUM;
        Forum forum = this.forumProvider.findForumById(forumId);
        
        PostEntityTag entityTag = PostEntityTag.fromCode(cmd.getEntityTag());
        
        // 各区域ID，说明见com.everhomes.forum.PostEntityTag
        Map<String, Long> gaRegionIdMap = this.organizationService.getOrganizationRegionMap(communityId);
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("Ga regions for query topics by category, userId=" + userId 
                + ", communityId=" + communityId + ", map=" + gaRegionIdMap);
        }

        Condition visibilityCondition = buildPostCategoryQryCondition(user, entityTag, community, gaRegionIdMap, 
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
        Map<String, Long> gaRegionIdMap = this.organizationService.getOrganizationRegionMap(communityId);
        
        Condition condition = buildPostQryConditionByUserRelated(operator, community, gaRegionIdMap);
        
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
    
    public ListPostCommandResponse queryOrganizationTopics(QueryOrganizationTopicCommand cmd) {
        long startTime = System.currentTimeMillis();
        User operator = UserContext.current().getUser();
        Long operatorId = operator.getId();
        Long organizationId = cmd.getOrganizationId();
        Organization organization = checkOrganizationParameter(operatorId, organizationId, "listOrganizationTopics");
        
        List<Long> communityIdList = organizationService.getOrganizationCommunityIdById(organizationId);
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
            query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ID.eq(ForumConstants.SYSTEM_FORUM));
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
        
        AddUserPointCommand pointCmd = new AddUserPointCommand(userId, PointType.CREATE_COMMENT.name(), 
            userPointService.getItemPoint(PointType.CREATE_COMMENT), userId);  
        userPointService.addPoint(pointCmd);
        
        return ConvertHelper.convert(post, PostDTO.class);
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
    
    private ListPostCommandResponse listSystemForumTopicsByUser(Forum forum, ListTopicCommand cmd) {
        User user = UserContext.current().getUser();
        long userId = user.getId();
        Long communityId = cmd.getCommunityId();
        
        Community community = checkCommunityParameter(userId, communityId, "listSystemForumTopicsByUser");
        
        // 各区域ID，说明见com.everhomes.forum.PostEntityTag
        Map<String, Long> gaRegionIdMap = this.organizationService.getOrganizationRegionMap(communityId);

        // 根据查帖指定的可见性创建查询条件
        VisibilityScope scope = VisibilityScope.fromCode(cmd.getVisibilityScope());
        if(scope == null) {
            scope = VisibilityScope.NEARBY_COMMUNITIES;
        }
        Condition visibilityCondition = buildPostVisibilityQryCondition(user, community, gaRegionIdMap, scope);
        
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
            privateFlag =  PostPrivacy.PUBLIC;
        }
        post.setPrivateFlag(privateFlag.getCode());
        
        post.setAssignedFlag(PostAssignedFlag.NONE.getCode());
        
        return post;
    }
    
    private Post processCommentCommand(long userId, NewCommentCommand cmd) {
        Post commentPost = new Post();

        Long forumId = cmd.getForumId();
        if(forumId == null) {
            LOGGER.error("Forum id is null, userId=" + userId + ", forumId=" + forumId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid forum id");
        }
        Forum forum = this.forumProvider.findForumById(forumId);
        if(forum == null) {
            LOGGER.error("Forum not found, userId=" + userId + ", forumId=" + forumId);
            throw RuntimeErrorException.errorWith(ForumServiceErrorCode.SCOPE, 
                ForumServiceErrorCode.ERROR_FORUM_NOT_FOUND, "forum not found");
        }
        
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
        
        if(!forumId.equals(topic.getForumId())) {
            LOGGER.error("Topic is not in the forum, userId=" + userId + ", forumId=" + forumId + ", topicId=" + topicId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid forum id");
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
            
            post.setCategoryId(cmd.getContentCategory());
            post.setCategoryPath(category.getPath());
        }
        
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
    
    private Condition buildPostCategoryQryCondition(User user, PostEntityTag entityTag, Community community,  
        Map<String, Long> gaRegionIdMap, Long contentCategoryId, Long actionCategoryId) {
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
        if(entityTag != null) {
            switch(entityTag) {
            case USER:
                userCondition = buildPostQryConditionBySimpleUser(community, VisibilityScope.NEARBY_COMMUNITIES);
                break;
            case PM:
            case GARC:
            case GANC:
            case GAPS:
            case GACW:
                userCondition = buildGaRelatedPostQryConditionByCategory(user.getId(), entityTag, community, gaRegionIdMap);
                break;
            default:
                if(LOGGER.isWarnEnabled()) {
                    LOGGER.warn("Entity tag is not supported, userId=" + user.getId() + ", communityId=" + community.getId() + ", entityTag=" + entityTag);
                }
            }
        } else {
            if(LOGGER.isInfoEnabled()) {
                LOGGER.info("Entity tag is null, userId=" + user.getId() + ", communityId=" + community.getId() + ", entityTag=" + entityTag);
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
    
    private Condition buildPostQryConditionByUserRelated(User user, Community community, Map<String, Long> gaRegionIdMap) {
        VisibilityScope scope = VisibilityScope.NEARBY_COMMUNITIES;
        Condition systemForumCondition = buildPostVisibilityQryCondition(user, community, gaRegionIdMap, scope);
        systemForumCondition = Tables.EH_FORUM_POSTS.FORUM_ID.eq(ForumConstants.SYSTEM_FORUM).and(systemForumCondition);
        
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
        
        if(systemForumCondition != null) {
            if(otherForumCondition == null) {
                return systemForumCondition;
            } else {
                return systemForumCondition.or(otherForumCondition);
            }
        } else {
            return otherForumCondition;
        }
    }
    
    private Condition buildPostVisibilityQryCondition(User user, Community community, Map<String, Long> gaRegionIdMap, VisibilityScope scope) {
        Condition simpleUserCondition = buildPostQryConditionBySimpleUser(community, scope);
        Condition gaRelatedCondition = buildGaRelatedPostQryConditionByUser(user.getId(), community, gaRegionIdMap);
        Condition assignedScopeCondition = buildPostQryConditionByAssignedScope(community);
        
        return simpleUserCondition.or(gaRelatedCondition).or(assignedScopeCondition);
    }
    
    private Condition buildPostQryConditionBySimpleUser(Community community, VisibilityScope scope) {
        long communityId = community.getId();
        
        Condition c1 = Tables.EH_FORUM_POSTS.CREATOR_TAG.eq(PostEntityTag.USER.getCode());
        c1 = c1.and(Tables.EH_FORUM_POSTS.TARGET_TAG.eq(PostEntityTag.USER.getCode()));
        c1 = c1.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_TYPE.eq(VisibleRegionType.COMMUNITY.getCode()));
        switch(scope) {
        case COMMUNITY:
            c1 = c1.and(Tables.EH_FORUM_POSTS.VISIBLE_REGION_ID.eq(communityId));
            break;
        default: // default to query topics in nearby communities
            List<Long> nearbyCmntyIdList = new ArrayList<Long>();
            List<Community> nearbyCmntyList = communityProvider.findNearyByCommunityById(communityId);
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
    
    /**
     * 填充帖子信息，主要是补充发帖/评论人信息和附件信息
     * @param userId 查帖人
     * @param post 帖子/评论
     * @param communityId 用户当前所在的小区ID
     * @param isDetail 是否查的是详情（详情的填充内容和列表填充内容略有不同，后者简略一些）
     */
    private void populatePost(long userId, Post post, Long communityId, boolean isDetail) {
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
                
                populatePostStatus(userId, post);
                
                populatePostRegionInfo(userId, post);
                
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
    
    private void populatePostCreatorInfo(long userId, Post post) {
        Long forumId = post.getForumId();
        if(forumId != null) {
            String creatorNickName = post.getCreatorNickName();
            String creatorAvatar = post.getCreatorAvatar();
            
            // 社区论坛的意见反馈论坛直接使用USER表中的信息作为发帖人的信息
            if(forumId == ForumConstants.SYSTEM_FORUM || forumId == ForumConstants.FEEDBACK_FORUM) {
                User creator = userProvider.findUserById(post.getCreatorUid());
                if(creator != null) {
                    // 优先使用帖子里存储的昵称和头像（2.8转过来的数据会有这些昵称和头像，因为在2.8不同家庭有不同的昵称）
                    if(creatorNickName == null || creatorNickName.length() == 0) {
                        post.setCreatorNickName(creator.getNickName());
                    }
                    if(creatorAvatar == null || creatorAvatar.length() == 0) {
                        post.setCreatorAvatar(creator.getAvatar());
                    }
                }
                // TODO: set the admin flag of system forum
            } else {
                // 普通圈使用圈成员的信息
                Forum forum = forumProvider.findForumById(forumId);
                if(forum != null && EntityType.GROUP.getCode().equalsIgnoreCase(forum.getOwnerType())) {
                    GroupMember member = groupProvider.findGroupMemberByMemberInfo(forum.getOwnerId(), 
                        EntityType.USER.getCode(), post.getCreatorUid());
                    if(member != null) {
                        String nickName = member.getMemberNickName();
                        String avatar = member.getMemberAvatar();
                        if(nickName == null || nickName.trim().length() == 0) {
                            User creator = userProvider.findUserById(post.getCreatorUid());
                            if(creator != null) {
                                nickName = creator.getNickName();
                            }
                        }
                        if(avatar == null || avatar.trim().length() == 0){
                        	User creator = userProvider.findUserById(post.getCreatorUid());
                        	if(creator != null) {
                        		avatar = creator.getAvatar();
                            }
                        }
                        // 优先使用帖子里存储的昵称和头像（2.8转过来的数据会有这些昵称和头像，因为在2.8不同家庭有不同的昵称）
                        if(creatorNickName == null || creatorNickName.length() == 0) {
                            post.setCreatorNickName(nickName);
                        }
                        if(creatorAvatar == null || creatorAvatar.length() == 0) {
                            post.setCreatorAvatar(member.getMemberAvatar());
                        }
                    }
                    
                    Group group = this.groupProvider.findGroupById(forum.getOwnerId());
                    if(group != null) {
                        post.setForumName(group.getName());
                    }
                }
            }
            
            creatorAvatar = post.getCreatorAvatar();
            if(creatorAvatar != null && creatorAvatar.length() > 0) {
                String avatarUrl = getResourceUrlByUir(userId, creatorAvatar, 
                    EntityType.USER.getCode(), post.getCreatorUid());
                post.setCreatorAvatarUrl(avatarUrl);
            }
        }
        
        UserLike userLike = this.userProvider.findUserLike(userId, EntityType.POST.getCode(), post.getId());
        if(userLike != null && UserLikeType.LIKE == UserLikeType.fromCode(userLike.getLikeType())) {
            post.setLikeFlag(UserLikeType.LIKE.getCode());
        } else {
            post.setLikeFlag(UserLikeType.NONE.getCode());
        }
    }
    
    private void populatePostStatus(long userId, Post post) {
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
        
        if(regionId == 0L){
        	post.setCreatorNickName(post.getCreatorNickName());
        }
        else if(regionType != null && regionId != null) {
            String creatorNickName = post.getCreatorNickName();
            if(creatorNickName == null) {
                creatorNickName = "";
            }
            switch(regionType) {
            case COMMUNITY:
                Community community = communityProvider.findCommunityById(regionId);
                creatorNickName = creatorNickName + "@" + community.getName();
                break;
            case REGION:
                Organization organization = organizationProvider.findOrganizationById(regionId);
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
}
