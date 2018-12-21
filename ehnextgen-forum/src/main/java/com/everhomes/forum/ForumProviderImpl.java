package com.everhomes.forum;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.everhomes.server.schema.tables.daos.*;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.EhForumServiceTypesRecord;
import com.everhomes.server.schema.tables.records.EhInteractSettingsRecord;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.Constants;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.Organization;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.forum.ForumLocalStringCode;
import com.everhomes.rest.forum.PostStatus;
import com.everhomes.rest.organization.OfficialFlag;
import com.everhomes.rest.ui.forum.MediaDisplayFlag;
import com.everhomes.rest.user.UserFavoriteTargetType;
import com.everhomes.rest.user.UserLikeType;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.records.EhForumAttachmentsRecord;
import com.everhomes.server.schema.tables.records.EhForumPostsRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.sharding.ShardingProvider;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserLike;
import com.everhomes.user.UserProfileContstant;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RecordHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class ForumProviderImpl implements ForumProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ForumProviderImpl.class);
    
    @Autowired 
    private SequenceProvider sequenceProvider;
    
    @Autowired
    private CoordinationProvider coordinator;
    
    @Autowired
    private ShardingProvider shardingProvider;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private BigCollectionProvider bigCollectionProvider;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private UserActivityProvider userActivityProvider;
    
    @Autowired
    private CoordinationProvider coordinationProvider;
    
    @Autowired
    private LocaleStringService localeStringService;
    
    @Autowired
    private HotPostService hotPostService;
    
    @Override
    public void createForum(Forum forum) {
        // 平台1.0.0版本更新主表ID获取方式 by lqs 20180516
        long id = this.dbProvider.allocPojoRecordId(EhForums.class);
        //long id = this.shardingProvider.allocShardableContentId(EhForums.class).second();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhForums.class, id));
        
        EhForumsDao dao = new EhForumsDao(context.configuration());
        
        forum.setId(id);
        forum.setUuid(UUID.randomUUID().toString());
        forum.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.insert(forum);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhForums.class, null);
    }

    @Cacheable(value="ForumById", key="#id", unless="#result == null")
    @Override
    public Forum findForumById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhForums.class, id));
        
        EhForumsDao dao = new EhForumsDao(context.configuration());
        EhForums record = dao.findById(id);
        return ConvertHelper.convert(record, Forum.class);
    }

    @Cacheable(value="ForumByName", key="#name", unless="#result == null")
    @Override
    public Forum findForumByName(final String name) {
        final List<EhForums> records = new ArrayList<EhForums>();
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhForums.class), records, (DSLContext context, List<EhForums> reducingContext) -> {
            EhForumsDao dao = new EhForumsDao(context.configuration());
            List<EhForums> l = dao.fetchByName(name);
            if(l.size() > 0) {
                records.addAll(l);
                return false;
            }
            return true;
        });
        
        if(records.size() > 0)
            return ConvertHelper.convert(records.get(0), Forum.class);
        return null;
    }

    @Cacheable(value="ForumByOwner", key="{#ownerType, #ownerId}", unless="#result.size() == 0")
    @Override
    public List<Forum> findForumByOwner(final String ownerType, final long ownerId) {
        List<Forum> groupList = new ArrayList<Forum>();
		dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class),
			groupList, (DSLContext context, Object reducingContext) -> {
				List<Forum> list = context.select().from(Tables.EH_FORUMS)
					.where(Tables.EH_FORUMS.OWNER_TYPE.eq(ownerType))
					.and(Tables.EH_FORUMS.OWNER_ID.eq(ownerId))
					.fetch().map((r) -> {
						return ConvertHelper.convert(r, Forum.class);
					});

				if (list != null && !list.isEmpty()) {
					groupList.addAll(list);
				}

				return true;
			});
        
        return groupList;
    }

    @Caching(evict={@CacheEvict(value="ForumById", key="#forum.id"), 
            @CacheEvict(value="ForumByName", key="#forum.name"),
            @CacheEvict(value="ForumByOwner", key="{#forum.ownerType, #forum.ownerId}")})
    @Override
    public void updateForum(Forum forum) {
        assert(forum.getId() != null);
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhForums.class, forum.getId()));
        EhForumsDao dao = new EhForumsDao(context.configuration());
        dao.update(forum);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhForums.class, forum.getId());
    }
    
    @Caching(evict={@CacheEvict(value="ForumById", key="#forum.id"), 
            @CacheEvict(value="ForumByName", key="#forum.name"),
            @CacheEvict(value="ForumByOwner", key="{#forum.ownerType, #forum.ownerId}")})
    @Override
    public void deleteForum(Forum forum) {
        assert(forum.getId() != null);
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhForums.class, forum.getId()));
        EhForumsDao dao = new EhForumsDao(context.configuration());
        dao.delete(forum);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhForums.class, forum.getId());
    }

    @Override
    public void deleteForum(long id) {
        ForumProvider self = PlatformContext.getComponent(ForumProvider.class);
        Forum forum = self.findForumById(id);
        if(forum != null)
            self.deleteForum(forum);
    }

    @Override
    public void createPost(Post post) {
        long startTime = System.currentTimeMillis();
        assert(post.getForumId() != null);

        // 平台1.0.0版本更新主表ID获取方式 by lqs 20180516
        long id = this.dbProvider.allocPojoRecordId(EhForumPosts.class);
        //long id = this.shardingProvider.allocShardableContentId(EhForumPosts.class).second();
        long seq = sequenceProvider.getNextSequence(Constants.FORUM_MODIFY_SEQUENCE_DOMAIN_NAME);
        
        post.setId(id);
        post.setUuid(UUID.randomUUID().toString());
        post.setModifySeq(seq);

        //编辑后发布需要重新设置创建时间.
        Timestamp ts = new Timestamp(DateHelper.currentGMTTime().getTime());
        post.setCreateTime(ts);
        if(post.getUpdateTime() == null) {
            post.setUpdateTime(post.getCreateTime());
        }
        // 添加帖子要默认为非官方，否则由于查询里有排除官方的帖子的条件，而该字段为NULL导致查不出所有新帖子 by lqs 20160723
        if(post.getOfficialFlag() == null) {
            post.setOfficialFlag(OfficialFlag.NO.getCode());
        }
        
        if (post.getMediaDisplayFlag() == null) {
			post.setMediaDisplayFlag(MediaDisplayFlag.YES.getCode());
		}
        
        this.coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_POST.getCode()).enter(()-> {
            this.dbProvider.execute((status) -> {
                Post parentPost = null;
                if(post.getParentPostId() != null && post.getParentPostId() != 0) {
                    parentPost = findPostById(post.getParentPostId());
                    if(parentPost == null) {
                        throw new InvalidParameterException("Missing parent post info in post parameter");
                    }
//                  post.setFloorNumber(parentPost.getChildCount() + 1);
                    //评论的楼层数为帖子的next floor number mod by xiongying 20160428
                    long floorNumber = parentPost.getIntegralTag2() == null ? 0 : parentPost.getIntegralTag2();
                    post.setFloorNumber(floorNumber);
                    
                } else {
                	if(post.getEmbeddedAppId() != null && post.getEmbeddedAppId().equals(AppConstants.APPID_ACTIVITY)) {
                		userActivityProvider.addPostedTopic(post.getCreatorUid(), UserFavoriteTargetType.ACTIVITY.getCode(), id);
                    } else {
                    	userActivityProvider.addPostedTopic(post.getCreatorUid(), UserFavoriteTargetType.TOPIC.getCode(), id);
                    }
                    
                    userActivityProvider.updateProfileIfNotExist(post.getCreatorUid(), UserProfileContstant.POSTED_TOPIC_COUNT, 1);
                }
                
                DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhForumPosts.class, id));
                EhForumPostsDao dao = new EhForumPostsDao(context.configuration());
                
                dao.insert(post);
                DaoHelper.publishDaoAction(DaoAction.CREATE, EhForumPosts.class, null);
            
                if(parentPost != null) {
                	// 增加评论时帖子的next floor number加1 mod by xiongying 20160428
                	long floorNumber = parentPost.getIntegralTag2() == null ? 0 : parentPost.getIntegralTag2();
                	parentPost.setIntegralTag2(floorNumber+1);
                    parentPost.setChildCount(parentPost.getChildCount() + 1);
                    ForumProvider self = PlatformContext.getComponent(ForumProvider.class);
                    self.updatePost(parentPost);
                }
                
                return null;
            });
            
            return null;
        });
        
        long endTime = System.currentTimeMillis();
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("Create a new post, id=" + id + ", elapse=" + (endTime - startTime));
        }
    }

    @Caching(evict={@CacheEvict(value="ForumPostById", key="#post.id"),
        @CacheEvict(value="ForumPostByUuid", key="#post.uuid")})
    @Override
    public void updatePost(Post post) {
        assert(post.getId() != 0);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhForumPosts.class, post.getId()));
        
        Post parentPost = null;
        if(post.getParentPostId() != null && post.getParentPostId() != 0) {
            parentPost = findPostById(post.getParentPostId());
            if(parentPost == null) {
                throw new InvalidParameterException("Missing parent post info in post parameter");
            }
//            post.setFloorNumber(parentPost.getChildCount() - 1);
            // 删除评论时评论置为inactive，评论内容不变 mod by xiongying 20160428
//            String template = localeStringService.getLocalizedString(
//            		ForumLocalStringCode.SCOPE,
//                    String.valueOf(ForumLocalStringCode.FORUM_COMMENT_DELETED),
//                    UserContext.current().getUser().getLocale(),
//                    "");
//           
//            post.setContent(template);
//            post.setStatus(PostStatus.ACTIVE.getCode());
//        } else {
//            userActivityProvider.addPostedTopic(post.getCreatorUid(), id);
        }
        
        EhForumPostsDao dao = new EhForumPostsDao(context.configuration());
        post.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(post);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhForumPosts.class, post.getId());
        
        
    }

    @Caching(evict={@CacheEvict(value="ForumPostById", key="#postId"),
        @CacheEvict(value="ForumPostByUuid", key="#post.uuid")})
    @Override
    public void deletePost(long postId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhForumPosts.class, postId));
        
        EhForumPostsDao dao = new EhForumPostsDao(context.configuration());
        dao.deleteById(postId);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhForumPosts.class, postId);
    }
    
    @Cacheable(value="ForumPostById", key="#postId", unless="#result == null")
    @Override
    public Post findPostById(long postId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhForumPosts.class, postId));
        EhForumPostsDao dao = new EhForumPostsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(postId), Post.class);
    }
    
    @Cacheable(value = "ForumPostByUuid", key="#uuid", unless="#result == null")
    @Override
    public Post findPostByUuid(String uuid) {
        Post[] result = new Post[1];
        
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhGroups.class), null, 
            (DSLContext context, Object reducingContext) -> {
                result[0] = context.select().from(Tables.EH_FORUM_POSTS)
                    .where(Tables.EH_FORUM_POSTS.UUID.eq(uuid))
                    .fetchAny().map((r) -> {
                        return ConvertHelper.convert(r, Post.class);
                    });

                if (result[0] != null) {
                    return false;
                } else {
                    return true;
                }
            });
        
        return result[0];
    }

    @Caching(evict={@CacheEvict(value="ForumAttachmentList", key="#attachment.postId")})
    @Override
    public void createPostAttachment(Attachment attachment) {
        assert(attachment.getPostId() != null);
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhForumPosts.class, attachment.getPostId()));
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhForumAttachments.class));
        attachment.setId(id);
        
        EhForumAttachmentsDao dao = new EhForumAttachmentsDao(context.configuration());
        dao.insert(attachment);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhForumAttachments.class, null);
    }

    @Cacheable(value="ForumAttachmentById", key="#id", unless="#result == null")
    @Override
    public Attachment findAttachmentById(final long id) {
        final Attachment[] result = new Attachment[1];
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhForumPosts.class), result, (DSLContext context, Attachment[] reducingContext) -> {
                
            EhForumAttachmentsDao dao = new EhForumAttachmentsDao(context.configuration());
            EhForumAttachments pojo = dao.findById(id);
            if(pojo != null) {
                result[0] = ConvertHelper.convert(pojo, Attachment.class);
                return false;
            }
            
            return true;
        });
        
        return result[0];
    }

    @Caching(evict={@CacheEvict(value="ForumAttachmentById", key="#attachment.id"), 
            @CacheEvict(value="ForumAttachmentList", key="#attachment.postId")})
    @Override
    public void updateAttachment(final Attachment attachment) {
        this.dbProvider.mapReduce(AccessSpec.readWriteWith(EhForumPosts.class), null, (DSLContext context, Object reducingContext) -> {
                
            EhForumAttachmentsDao dao = new EhForumAttachmentsDao(context.configuration());
            EhForumAttachments pojo = dao.findById(attachment.getId());
            if(pojo != null) {
                dao.update(attachment);
                DaoHelper.publishDaoAction(DaoAction.MODIFY, EhForumAttachments.class, attachment.getId());
                return false;
            }
            return true;
        });
    }
    
    @Caching(evict={@CacheEvict(value="ForumAttachmentById", key="#attachment.id"), 
            @CacheEvict(value="ForumAttachmentList", key="attachment.postId")})
    @Override
    public void deleteAttachment(final Attachment attachment) {
        this.dbProvider.mapReduce(AccessSpec.readWriteWith(EhForumPosts.class), null, (DSLContext context, Object reducingContext) -> {
                
            EhForumAttachmentsDao dao = new EhForumAttachmentsDao(context.configuration());
            EhForumAttachments pojo = dao.findById(attachment.getId());
            if(pojo != null) {
                dao.deleteById(attachment.getId());;
                DaoHelper.publishDaoAction(DaoAction.MODIFY, EhForumAttachments.class, attachment.getId());
                return false;
            }
            return true;
        });
    }

    @Override
    public void deleteAttachment(final long id) {
        ForumProvider self = PlatformContext.getComponent(ForumProvider.class);
        
        Attachment attachment = self.findAttachmentById(id);
        if(attachment != null)
            self.deleteAttachment(attachment);
    }
    
    @Caching(evict={@CacheEvict(value="ForumAttachmentList", key="#postId")})
    @Override
    public List<Attachment> listPostAttachments(long postId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhForumPosts.class, postId));
        
        return context.selectFrom(Tables.EH_FORUM_ATTACHMENTS)
            .where(Tables.EH_FORUM_ATTACHMENTS.POST_ID.eq(postId))
            .fetch()
            .map((r)-> { return ConvertHelper.convert(r, Attachment.class); } );
    }

    @Caching(evict={@CacheEvict(value="ForumPostById", key="#postId")})
    @Override
    public void likePost(long uid, long postId) {
        Post post = this.findPostById(postId);
        if(post != null) {
            UserLike userLike = this.userProvider.findUserLike(uid, EntityType.POST.getCode(), postId);
            if(userLike != null) {
                if(UserLikeType.fromCode(userLike.getLikeType()) == UserLikeType.NONE) {
                    userLike.setLikeType(UserLikeType.LIKE.getCode());
                    this.userProvider.updateUserLike(userLike);
                    
                    post.setLikeCount(post.getLikeCount().longValue() + 1);
                    ForumProvider self = PlatformContext.getComponent(ForumProvider.class);
                    self.updatePost(post);
                }
            } else {
                userLike = new UserLike();
                userLike.setOwnerUid(uid);
                userLike.setTargetType(EntityType.POST.getCode());
                userLike.setTargetId(postId);
                userLike.setLikeType(UserLikeType.LIKE.getCode());
                this.userProvider.createUserLike(userLike);

                // count does not to be exact accurate, no transaction is used here
                post.setLikeCount(post.getLikeCount().longValue() + 1);
                ForumProvider self = PlatformContext.getComponent(ForumProvider.class);
                self.updatePost(post);
            }
        } else {
            if(LOGGER.isWarnEnabled()) {
                LOGGER.warn("Post is not found, userId=" + uid + ", postId=" + postId);
            }
        }
    }

    @Caching(evict={@CacheEvict(value="ForumPostById", key="#postId")})
    @Override
    public void cancelLikePost(long uid, long postId) {
        Post post = this.findPostById(postId);
        if(post != null) {
            UserLike userLike = this.userProvider.findUserLike(uid, EntityType.POST.getCode(), postId);
            if(userLike != null) {
                if(UserLikeType.fromCode(userLike.getLikeType()) == UserLikeType.LIKE) {
                    userLike.setLikeType(UserLikeType.NONE.getCode());
                    this.userProvider.updateUserLike(userLike);
                    
                    long likeCount = post.getLikeCount().longValue() - 1;
                    likeCount = (likeCount < 0) ? 0 : likeCount;
                    post.setLikeCount(likeCount);
                    ForumProvider self = PlatformContext.getComponent(ForumProvider.class);
                    self.updatePost(post);
                }
            } else {
                userLike = new UserLike();
                userLike.setOwnerUid(uid);
                userLike.setTargetType(EntityType.POST.getCode());
                userLike.setTargetId(postId);
                userLike.setLikeType(UserLikeType.NONE.getCode());
                this.userProvider.createUserLike(userLike);
            }
        } else {
            if(LOGGER.isWarnEnabled()) {
                LOGGER.warn("Post is not found, userId=" + uid + ", postId=" + postId);
            }
        }
    }
    
    @Caching(evict={@CacheEvict(value="ForumPostById", key="#postId")})
    @Override
    public void dislikePost(long postId, long uid) {
        Post post = this.findPostById(postId);
        if(post != null) {
            UserLike userLike = this.userProvider.findUserLike(uid, EntityType.POST.getCode(), postId);
            if(userLike != null) {
                if(UserLikeType.fromCode(userLike.getLikeType()) == UserLikeType.LIKE) {
                    userLike.setLikeType(UserLikeType.DISLIKE.getCode());
                    this.userProvider.updateUserLike(userLike);
                    
                    //post.setDislikeCount(post.getDislikeCount().longValue() + 1);
                    long likeCount = post.getLikeCount().longValue() - 1;
                    likeCount = (likeCount < 0) ? 0 : likeCount;
                    post.setLikeCount(likeCount);
                    ForumProvider self = PlatformContext.getComponent(ForumProvider.class);
                    self.updatePost(post);
                }
            } else {
                userLike = new UserLike();
                userLike.setOwnerUid(uid);
                userLike.setTargetType(EntityType.POST.getCode());
                userLike.setTargetId(postId);
                userLike.setLikeType(UserLikeType.DISLIKE.getCode());
                this.userProvider.createUserLike(userLike);

                // count does not to be exact accurate, no transaction is used here
                //post.setDislikeCount(post.getDislikeCount().longValue() + 1);
                ForumProvider self = PlatformContext.getComponent(ForumProvider.class);
                self.updatePost(post);
            }
        }
    }
    
    @Caching(evict={@CacheEvict(value="ForumPostById", key="#postId")})
    @Override
    public void cancelLikeDislikePost(long postId, long uid) {
        Post post = this.findPostById(postId);
        if(post != null) {
            UserLike userLike = this.userProvider.findUserLike(uid, EntityType.POST.getCode(), postId);
            if(userLike != null) {
                if(UserLikeType.fromCode(userLike.getLikeType()) == UserLikeType.LIKE) {
                    post.setLikeCount(post.getLikeCount().longValue() - 1);
                } else {
                    //post.setDislikeCount(post.getDislikeCount().longValue() - 1);
                }

                ForumProvider self = PlatformContext.getComponent(ForumProvider.class);
                self.updatePost(post);
                this.userProvider.deleteUserLike(userLike);
            }        
        }
    }

    @Override
    public List<Post> queryPosts(CrossShardListingLocator forums[], int maxRecentReplyingPosts, final int count, 
            final ListingQueryBuilderCallback queryBuilderCallback,
            Comparator<Post> mergeCamparable) {
        long startTime = System.currentTimeMillis();
        assert(queryBuilderCallback != null);
        assert(mergeCamparable != null);
        
        final List<Post> results = new ArrayList<Post>();
        for(final CrossShardListingLocator locator : forums) {
            final List<Post> perForumResults = new ArrayList<Post>();
            
            final Integer[] limit = new Integer[1];
            limit[0] = count;
            if(locator.getShardIterator() == null) {
                AccessSpec accessSpec = AccessSpec.readOnlyWith(EhForumPosts.class);
                ShardIterator shardIterator = new ShardIterator(accessSpec);
                
                locator.setShardIterator(shardIterator);
            }
           
            this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, reducingContext) -> {
                SelectQuery<EhForumPostsRecord> query = context.selectQuery(Tables.EH_FORUM_POSTS);
                query.addConditions(Tables.EH_FORUM_POSTS.FORUM_ID.eq(locator.getEntityId()));
                //query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.isNull());
                if(queryBuilderCallback != null) {
                    queryBuilderCallback.buildCondition(locator, query);
                }

                //置顶的优先排序  add by yanjun 20171023
                query.addOrderBy(Tables.EH_FORUM_POSTS.STICK_FLAG.desc());
                query.addOrderBy(Tables.EH_FORUM_POSTS.STICK_TIME.desc());

                query.addOrderBy(Tables.EH_FORUM_POSTS.CREATE_TIME.desc());

                Integer offset = 0;
                if(locator.getAnchor() != null) {
                    // MD，加上置顶功能后无法使用锚点排序，一页页来 add by yanjun 20171031
                    offset = (locator.getAnchor().intValue() - 1 ) * (count-1);
                }
                query.addLimit(offset, limit[0]);
                
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Query posts by forum, sql=" + query.getSQL());
                    LOGGER.debug("Query posts by forum, bindValues=" + query.getBindValues());
                }
                
//                List<Post> l = query.fetch().map((EhForumPostsRecord record) -> {
//                    return ConvertHelper.convert(record, Post.class);
//                });
                
                List<EhForumPostsRecord> records = query.fetch().map(new EhForumPostsRecordMapper());
                List<Post> l = records.stream().map((r) -> {
                    return ConvertHelper.convert(r, Post.class);
                }).collect(Collectors.toList());

                if(l.size() > 0) {
                    perForumResults.addAll(l);
                    results.addAll(l);
                    limit[0] = count - perForumResults.size();
                    locator.setAnchor(perForumResults.get(perForumResults.size() -1).getModifySeq());
                    
                    if(perForumResults.size() >= count)
                        return AfterAction.done;
                }
                
                return AfterAction.next;
            });
        }
        
        Collections.sort(results, mergeCamparable);
        
        List<Post> mergedResults = new ArrayList<Post>();
        if(results.size() > 0) {
            mergedResults = results.subList(0, Math.min(results.size(), count));
            populatePostAttachments(mergedResults);
            // No need to query replies when querying posts
            //populateRecentReplyPosts(mergedResults, maxRecentReplyingPosts);
        }
        
        long endTime = System.currentTimeMillis();
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("Query posts by forum, resultSize=" + mergedResults.size() 
                + ", maxCount=" + count + ", elapse=" + (endTime - startTime));
        }
        
        return mergedResults;
    }
    
    @Override
    public List<Post> queryPosts(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback) {
        long startTime = System.currentTimeMillis();
        assert(locator.getEntityId() != 0);
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhForums.class, locator.getEntityId()));
        
        SelectQuery<EhForumPostsRecord> query = context.selectQuery(Tables.EH_FORUM_POSTS);
        query.addSelect(Tables.EH_FORUM_POSTS.fields());
        query.setDistinct(true);
    
        if(queryBuilderCallback != null) {
            queryBuilderCallback.buildCondition(locator, query);
        }

        Integer offset = 0;
        if(locator.getAnchor() != null) {
        	//后台发布活动：开放时间选择可早于当前时间（包括开始、结束及报名截止时间，同时刷新活动发布时间为活动开始时间前24小时） (活动2.6.0的)
        	//此时ID和CREATE_TIME的顺序不一致，此处改用创建时间排序 ，  add by yanjun 20170522
        	//query.addConditions(Tables.EH_FORUM_POSTS.ID.lt(locator.getAnchor()));
            //query.addConditions(Tables.EH_FORUM_POSTS.CREATE_TIME.lt(new Timestamp(locator.getAnchor())));

            // MD，加上置顶功能后无法使用锚点排序，一页页来 add by yanjun 20171031
            offset = (locator.getAnchor().intValue() - 1 ) * (count-1);
        }

        //置顶的优先排序  add by yanjun 20171023
        query.addOrderBy(Tables.EH_FORUM_POSTS.STICK_FLAG.desc());
        query.addOrderBy(Tables.EH_FORUM_POSTS.STICK_TIME.desc());

        query.addOrderBy(Tables.EH_FORUM_POSTS.CREATE_TIME.desc());
        query.addLimit(offset, count);
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query posts by count, sql=" + query.getSQL());
            LOGGER.debug("Query posts by count, bindValues=" + query.getBindValues());
        }
        
        List<EhForumPostsRecord> records = query.fetch().map(new EhForumPostsRecordMapper());
        List<Post> posts = records.stream().map((r) -> {
            return ConvertHelper.convert(r, Post.class);
        }).collect(Collectors.toList());

//        locator.setAnchor(null);
//        if(posts.size() == count) {
//        	//后台发布活动：开放时间选择可早于当前时间（包括开始、结束及报名截止时间，同时刷新活动发布时间为活动开始时间前24小时） (活动2.6.0的)
//        	//此时ID和CREATE_TIME的顺序不一致，此处改用创建时间排序 ，  add by yanjun 20170522
//
//            // MD，加上置顶功能后无法使用锚点排序，一页页来     此处删掉 由service自己处理  add by yanjun 20171031
//            Long nextpageAnchor = locator.getAnchor() == null ? 2 : locator.getAnchor() + 1;
//            locator.setAnchor(nextpageAnchor);
//        }
        
        long endTime = System.currentTimeMillis();
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("Query posts by count, resultSize=" + posts.size() 
                + ", maxCount=" + count + ", elapse=" + (endTime - startTime));
        }
        
        return posts;
    }
    
    @Override
    public List<Post> queryReplyingPosts(final CrossShardListingLocator locator, final int count, 
            final ListingQueryBuilderCallback queryBuilderCallback) {
        assert(queryBuilderCallback != null);
        
        final List<Post> results = new ArrayList<Post>();
        final Integer[] limit = new Integer[1];
        limit[0] = count;
        
        if(locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhForumPosts.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            
            locator.setShardIterator(shardIterator);
        }
        
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, reducingContext) -> {
            SelectQuery<EhForumPostsRecord> query = context.selectQuery(Tables.EH_FORUM_POSTS);
            query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(locator.getEntityId()));
            queryBuilderCallback.buildCondition(locator, query);
            query.addLimit(limit[0]);
            List<Post> l = query.fetch().map((EhForumPostsRecord record) -> {
                return ConvertHelper.convert(record, Post.class);
            });
            
            if(l.size() > 0) {
                results.addAll(l);
                limit[0] = count - results.size();
                locator.setAnchor(results.get(results.size() -1).getModifySeq());
                
                if(results.size() >= count)
                    return AfterAction.done;
            }
            return AfterAction.next;
        });
        
        if(results.size() > 0) {
            populatePostAttachments(results);
        }
        
        return results;
    }
    
    @Override
    public void iteratePosts(int count, IteratePostCallback callback, ListingQueryBuilderCallback queryBuilderCallback) {
        assert(count > 0);
        
        List<Post> postList = null;
        int maxIndex = 0; // Max index of group list in loop
        CrossShardListingLocator locator = new CrossShardListingLocator();
        Long pageAnchor = null;
        do {
            locator.setAnchor(pageAnchor);
            
            postList = queryPosts(locator, (count + 1), queryBuilderCallback);
            
            if(postList == null || postList.size() == 0) {
                break;
            } else {
                // if there are still more records in db
                if(postList.size() > count) {
                    maxIndex = postList.size() - 2;
                    pageAnchor = pageAnchor == null ? 2 : pageAnchor + 1;
                } else {
                    // no more record in db
                    maxIndex = postList.size() - 1;
                    pageAnchor = null;
                }

                for(int i = 0; i <= maxIndex; i++) {
                    callback.process(postList.get(i));
                }
            }
        } while (pageAnchor != null);
    }
    
    private void populateRecentReplyPosts(List<Post> posts, int maxRecentReplyingPosts) {
        assert(posts != null);
        
        for(Post post : posts) {
            CrossShardListingLocator locator = new CrossShardListingLocator(post.getId());
            List<Post> replyingPosts = this.queryReplyingPosts(locator, maxRecentReplyingPosts, (loc, query)-> {
                query.addOrderBy(Tables.EH_FORUM_POSTS.MODIFY_SEQ.desc());
                return query;
            });
            post.setCommentPosts(replyingPosts);
        }
    }
    
    public void populatePostAttachments(final Post post) {
        if(post == null) {
            return;
        } else {
            List<Post> posts = new ArrayList<Post>();
            posts.add(post);
            
            populatePostAttachments(posts);
        }
    }
    
    public void populatePostAttachments(final List<Post> posts) {
        if(posts == null || posts.size() == 0) {
            return;
        }
            
        final List<Long> postIds = new ArrayList<Long>();
        final Map<Long, Post> mapPosts = new HashMap<Long, Post>();
        
        for(Post post: posts) {
            postIds.add(post.getId());
            mapPosts.put(post.getId(), post);
        }
        
        // 平台1.0.0版本更新，已不支持getContentShards()接口，经与kelven讨论目前没有用到多shard，
        // 故先暂时去掉，若后面需要支持多shard再思考解决办法 by lqs 20180516
        //List<Integer> shards = this.shardingProvider.getContentShards(EhForumPosts.class, postIds);
        //this.dbProvider.mapReduce(shards, AccessSpec.readOnlyWith(EhForumPosts.class), null, (DSLContext context, Object reducingContext) -> {
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhForumPosts.class), null, (DSLContext context, Object reducingContext) -> {
            SelectQuery<EhForumAttachmentsRecord> query = context.selectQuery(Tables.EH_FORUM_ATTACHMENTS);
            query.addConditions(Tables.EH_FORUM_ATTACHMENTS.POST_ID.in(postIds));
            query.fetch().map((EhForumAttachmentsRecord record) -> {
                Post post = mapPosts.get(record.getPostId());
                assert(post != null);
                post.getAttachments().add(ConvertHelper.convert(record, Attachment.class));
            
                return null;
            });
            return true;
        });
    }

    @Caching(evict={@CacheEvict(value="AssignedScopeByOwnerId", key="#scope.ownerId")})
    @Override
    public void createAssignedScope(AssignedScope scope) {
        assert(scope.getOwnerId() != null);
        
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhForumPosts.class, scope.getOwnerId()));
        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhForumAssignedScopes.class));
        scope.setId(id);
        
        EhForumAssignedScopesDao dao = new EhForumAssignedScopesDao(context.configuration());
        dao.insert(scope);
        
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhForumAssignedScopes.class, null);
    }
    
    @Cacheable(value="AssignedScopeById", key="#id", unless="#result == null")
    @Override
    public AssignedScope findAssignedScopeById(Long id) {
        final AssignedScope[] result = new AssignedScope[1];
        
        this.dbProvider.mapReduce(AccessSpec.readOnlyWith(EhForumPosts.class), null, 
            (DSLContext context, Object reducingContext) -> {
                EhForumAssignedScopesDao dao = new EhForumAssignedScopesDao(context.configuration());
                result[0] = ConvertHelper.convert(dao.findById(id), AssignedScope.class);
                if(result[0] == null) {
                    return true;
                } else {
                    return false;
                }
            });
        
        return result[0];
    }
    
    @Cacheable(value="AssignedScopeByOwnerId", key="#ownerId", unless="#result.size() == 0")
    @Override
    public List<AssignedScope> findAssignedScopeByOwnerId(Long ownerId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhForumPosts.class, ownerId));
        List<AssignedScope> scopeList = context.select().from(Tables.EH_FORUM_ASSIGNED_SCOPES)
            .where(Tables.EH_FORUM_ASSIGNED_SCOPES.OWNER_ID.eq(ownerId))
            .fetch().map((r) -> {
                return ConvertHelper.convert(r, AssignedScope.class);
            });
        
        return scopeList;
    }
    
    @Caching(evict={@CacheEvict(value="AssignedScopeById", key="#scope.id"),
        @CacheEvict(value="AssignedScopeByOwnerId", key="#scope.ownerId")})
    @Override
    public void deleteAssignedScope(AssignedScope scope) {
        assert(scope.getId() != null);
        assert(scope.getOwnerId() != null);
        
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhForumPosts.class, scope.getOwnerId()));
        EhForumAssignedScopesDao dao = new EhForumAssignedScopesDao(context.configuration());
        dao.delete(scope);
        
        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhForumAssignedScopes.class, scope.getId());
    }
    
    @Override
    public void deleteAssignedScopeById(Long id) {
        ForumProvider forumProvider = PlatformContext.getComponent(ForumProvider.class);
        
        AssignedScope scope = forumProvider.findAssignedScopeById(id);
        if(scope != null) {
            forumProvider.deleteAssignedScope(scope);
        }
    }

	@Override
	@Scheduled(cron="0 0/2 *  * * ? ")
	public void modifyHotPost() {
		
		while(true){
			HotPost hotpost = hotPostService.pull();
			if(hotpost != null){
				long posttime = hotpost.getTimeStamp();
				long current = Calendar.getInstance().getTimeInMillis();
				long diff = (current-posttime)/60000;
				
				modifyHot(hotpost);
				
				if(diff<1){
					break;
				}
			}
			else{
				break;
			}
		}
	}
	
	private void modifyHot(HotPost hotpost){
		Post post = this.findPostById(hotpost.getPostId());
		if(post != null){
			if(Byte.valueOf(HotPostModifyType.VIEW.getCode()).equals(hotpost.getModifyType())){
				post.setViewCount(post.getViewCount().longValue() + 20);
	            ForumProvider self = PlatformContext.getComponent(ForumProvider.class);
	            self.updatePost(post);
			}
			
			if(Byte.valueOf(HotPostModifyType.LIKE.getCode()).equals(hotpost.getModifyType())){
				post.setLikeCount(post.getLikeCount().longValue() + 20);
	            ForumProvider self = PlatformContext.getComponent(ForumProvider.class);
	            self.updatePost(post);
			}
		}
	}

	/**
	 * 金地抓取数据使用
	 */
	@Override
	public List<Post> listForumPostByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor,
			int pageSize) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> result = context.select(Tables.EH_FORUM_POSTS.fields()).from(Tables.EH_FORUM_POSTS)
			.join(Tables.EH_USERS).on(Tables.EH_FORUM_POSTS.CREATOR_UID.eq(Tables.EH_USERS.ID)).and(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L))
			.and(Tables.EH_FORUM_POSTS.EMBEDDED_APP_ID.eq(AppConstants.APPID_DEFAULT))
			.and(Tables.EH_FORUM_POSTS.UPDATE_TIME.eq(new Timestamp(timestamp)))
			.and(Tables.EH_FORUM_POSTS.ID.gt(pageAnchor))
			.orderBy(Tables.EH_FORUM_POSTS.ID.asc())
			.limit(pageSize)
			.fetch();
		
		if (result != null && result.isNotEmpty()) {
			return result.map(r->RecordHelper.convert(r, Post.class));
		}
		return new ArrayList<Post>();
	}

	/**
	 * 金地抓取数据使用
	 */
	@Override
	public List<Post> listForumPostByUpdateTime(Integer namespaceId, Long timestamp, int pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Result<Record> result = context.select(Tables.EH_FORUM_POSTS.fields()).from(Tables.EH_FORUM_POSTS)
			.join(Tables.EH_USERS).on(Tables.EH_FORUM_POSTS.CREATOR_UID.eq(Tables.EH_USERS.ID)).and(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId))
			.and(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0L))
			.and(Tables.EH_FORUM_POSTS.EMBEDDED_APP_ID.eq(AppConstants.APPID_DEFAULT))
			.and(Tables.EH_FORUM_POSTS.UPDATE_TIME.gt(new Timestamp(timestamp)))
			.orderBy(Tables.EH_FORUM_POSTS.UPDATE_TIME.asc(), Tables.EH_FORUM_POSTS.ID.asc())
			.limit(pageSize)
			.fetch();
			
		if (result != null && result.isNotEmpty()) {
			return result.map(r->RecordHelper.convert(r, Post.class));
		}
		return new ArrayList<Post>();
	}

	@Override
	public List<Post> listForumCommentByUpdateTimeAndAnchor(Integer namespaceId, Long timestamp, Long pageAnchor,
			int pageSize) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		com.everhomes.server.schema.tables.EhForumPosts t1 = Tables.EH_FORUM_POSTS.as("t1");
		com.everhomes.server.schema.tables.EhUsers t2 = Tables.EH_USERS.as("t2");
		com.everhomes.server.schema.tables.EhForumPosts t3 = Tables.EH_FORUM_POSTS.as("t3");
		Result<Record> result = context.select(t1.fields()).from(t1)
			.join(t2).on(t1.CREATOR_UID.eq(t2.ID)).and(t2.NAMESPACE_ID.eq(namespaceId))
			.join(t3).on(t1.PARENT_POST_ID.eq(t3.ID)).and(t3.EMBEDDED_APP_ID.eq(AppConstants.APPID_DEFAULT))
			.and(t1.PARENT_POST_ID.ne(0L))
			.and(t1.UPDATE_TIME.eq(new Timestamp(timestamp)))
			.and(t1.ID.gt(pageAnchor))
			.orderBy(t1.ID.asc())
			.limit(pageSize)
			.fetch();
		
		if (result != null && result.isNotEmpty()) {
			return result.map(r->RecordHelper.convert(r, Post.class));
		}
		return new ArrayList<Post>();
	}

	@Override
	public List<Post> listForumCommentByUpdateTime(Integer namespaceId, Long timestamp, int pageSize) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		com.everhomes.server.schema.tables.EhForumPosts t1 = Tables.EH_FORUM_POSTS.as("t1");
		com.everhomes.server.schema.tables.EhUsers t2 = Tables.EH_USERS.as("t2");
		com.everhomes.server.schema.tables.EhForumPosts t3 = Tables.EH_FORUM_POSTS.as("t3");
		Result<Record> result = context.select(t1.fields()).from(t1)
			.join(t2).on(t1.CREATOR_UID.eq(t2.ID)).and(t2.NAMESPACE_ID.eq(namespaceId))
			.join(t3).on(t1.PARENT_POST_ID.eq(t3.ID)).and(t3.EMBEDDED_APP_ID.eq(AppConstants.APPID_DEFAULT))
			.and(t1.PARENT_POST_ID.ne(0L))
			.and(t1.UPDATE_TIME.gt(new Timestamp(timestamp)))
			.orderBy(t1.UPDATE_TIME.asc(), t1.ID.asc())
			.limit(pageSize)
			.fetch();
			
		if (result != null && result.isNotEmpty()) {
			return result.map(r->RecordHelper.convert(r, Post.class));
		}
		return new ArrayList<Post>();
	}

	@Override
    public Forum findForumByNamespaceId(Integer namespaceId){

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Result<Record> result = context.select(Tables.EH_FORUMS.fields()).from(Tables.EH_FORUMS)
                .join(Tables.EH_COMMUNITIES)
                .on(Tables.EH_FORUMS.ID.eq(Tables.EH_COMMUNITIES.DEFAULT_FORUM_ID))
                .where(Tables.EH_FORUMS.NAMESPACE_ID.eq(namespaceId))
                .limit(1)
                .fetch();

        if (result != null && result.isNotEmpty()) {
            return RecordHelper.convert(result.get(0), Forum.class);
        }
	    return null;
    }


    @Override
    public List<Post> listPostsByRealPostId(Long realPostId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Result<Record> result = context.select().from(Tables.EH_FORUM_POSTS)
                .where(Tables.EH_FORUM_POSTS.REAL_POST_ID.eq(realPostId))
                .fetch();

        if (result != null && result.isNotEmpty()) {
            return result.map(r->RecordHelper.convert(r, Post.class));
        }
        return new ArrayList<Post>();
    }

    @Override
    public List<ForumCategory> listForumCategoryByForumId(Long forumId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Result<Record> result = context.select().from(Tables.EH_FORUM_CATEGORIES)
                .where(Tables.EH_FORUM_CATEGORIES.FORUM_ID.eq(forumId))
                .fetch();

        if (result != null && result.isNotEmpty()) {
            return result.map(r->RecordHelper.convert(r, ForumCategory.class));
        }
        return new ArrayList<ForumCategory>();
    }

    @Override
    public ForumCategory findForumCategoryById(Long Id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.select().from(Tables.EH_FORUM_CATEGORIES)
                .where(Tables.EH_FORUM_CATEGORIES.ID.eq(Id))
                .fetchOneInto(ForumCategory.class);
    }

    @Override
    public void createForumCategory(ForumCategory category){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        if(category.getId() == null){
            long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhForumCategories.class));
            category.setId(id);
        }

        if(category.getUuid() == null){
            category.setUuid(UUID.randomUUID().toString());
        }

        EhForumCategoriesDao dao = new EhForumCategoriesDao(context.configuration());
        dao.insert(category);
        DaoHelper.publishDaoAction(DaoAction.CREATE, EhForumCategories.class, null);
    }

    @Override
    public InteractSetting findInteractSetting(Integer namespaceId, Byte moduleType, Long categoryId) {

        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhInteractSettingsRecord> query = context.selectQuery(Tables.EH_INTERACT_SETTINGS);
        query.addConditions(Tables.EH_INTERACT_SETTINGS.NAMESPACE_ID.eq(namespaceId));
        query.addConditions(Tables.EH_INTERACT_SETTINGS.MODULE_TYPE.eq(moduleType));
        if(categoryId != null){
            query.addConditions(Tables.EH_INTERACT_SETTINGS.CATEGORY_ID.eq(categoryId));
        }
        return query.fetchAnyInto(InteractSetting.class);
    }

    @Override
    public void createInteractSetting(InteractSetting setting) {

        long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhInteractSettings.class));
        setting.setId(id);
        setting.setUuid(UUID.randomUUID().toString());
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhInteractSettingsDao dao = new EhInteractSettingsDao(context.configuration());
        dao.insert(setting);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhInteractSettings.class, null);
    }

    @Override
    public void updateInteractSetting(InteractSetting setting) {

        assert(setting.getId() == null);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhInteractSettingsDao dao = new EhInteractSettingsDao(context.configuration());
        dao.update(setting);

        DaoHelper.publishDaoAction(DaoAction.MODIFY,EhInteractSettings.class, setting.getId());
    }

    @Override
    public void createForumServiceTypes(List<ForumServiceType> list) {
        for(ForumServiceType s: list){
            if(s.getId() == null){
                long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhForumServiceTypes.class));
                s.setId(id);
            }
        }
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhForumServiceTypesDao dao = new EhForumServiceTypesDao(context.configuration());
        dao.insert(list.toArray(new ForumServiceType[list.size()]));
    }

    @Override
    public List<ForumServiceType> listForumServiceTypes(Integer namespaceId, Byte moduleType, Long categoryId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhForumServiceTypesRecord> query = context.selectQuery(Tables.EH_FORUM_SERVICE_TYPES);
        query.addConditions(Tables.EH_FORUM_SERVICE_TYPES.NAMESPACE_ID.eq(namespaceId));
        if(moduleType != null){
            query.addConditions(Tables.EH_FORUM_SERVICE_TYPES.MODULE_TYPE.eq(moduleType));
        }

        if(categoryId != null){
            query.addConditions(Tables.EH_FORUM_SERVICE_TYPES.CATEGORY_ID.eq(categoryId));
        }

        query.addOrderBy(Tables.EH_FORUM_SERVICE_TYPES.SORT_NUM.asc());

        List<ForumServiceType> res = query.fetch().map(record -> ConvertHelper.convert(record, ForumServiceType.class));
        return res;
    }

    @Override
    public void deleteForumServiceTypes(List<Long> ids) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhForumServiceTypesDao dao = new EhForumServiceTypesDao(context.configuration());
        dao.deleteById(ids);

    }

    @Caching(evict={@CacheEvict(value="ForumPostById", key="#post.id"),
            @CacheEvict(value="ForumPostByUuid", key="#post.uuid")})
    @Override
    public void updatePostAfterPublish(Post post) {
        assert(post.getId() != 0);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhForumPosts.class, post.getId()));
        EhForumPostsDao dao = new EhForumPostsDao(context.configuration());
        post.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        dao.update(post);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhForumPosts.class, post.getId());
    }

    @Caching(evict={@CacheEvict(value="ForumAttachmentList", key="#postId")})
    @Override
    public void deleteAttachments(Long postId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        context.delete(Tables.EH_FORUM_ATTACHMENTS).where(Tables.EH_FORUM_ATTACHMENTS.POST_ID.eq(postId)).execute();
    }

}
