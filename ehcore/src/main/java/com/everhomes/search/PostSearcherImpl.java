package com.everhomes.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.everhomes.rest.forum.*;
import com.everhomes.rest.launchpadbase.AppContext;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.CommunityService;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.family.FamilyProvider;
import com.everhomes.forum.Forum;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.ForumService;
import com.everhomes.forum.IteratePostCallback;
import com.everhomes.forum.Post;
import com.everhomes.group.Group;
import com.everhomes.group.GroupProvider;
import com.everhomes.group.GroupService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.namespace.Namespace;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.community.GetNearbyCommunitiesByIdCommand;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.group.GroupDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.search.SearchContentType;
import com.everhomes.rest.ui.forum.SearchTopicBySceneCommand;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;

@Service
public class PostSearcherImpl extends AbstractElasticSearch implements PostSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostSearcherImpl.class);
    
    @Autowired
    ConfigurationProvider configProvider;
    
    @Autowired
    ForumProvider forumProvider;
    
    @Autowired
    UserProvider userProvider;
    
    @Autowired
    UserService userService;
    
    @Autowired
    CategoryProvider categoryProvider;
    
    @Autowired
    GroupProvider groupProvider;
    
    @Autowired
    GroupService groupService;
    
    @Autowired
    private CommunityService communityService;
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
    ForumService forumService;
    
    @Autowired
    FamilyProvider familyProvider;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    OrganizationProvider organizationProvider;

	@Autowired
	private UserActivityProvider userActivityProvider;
    
    private final String contentcategory = "contentcategory";
    private final String actioncategory = "actioncategory";

    @Override
    public String getIndexType() {
        return SearchUtils.TOPICINDEXTYPE;
    }
    
    @Override
    public void deleteById(Long id) {
        this.deleteById(id.toString());
        
    }
    
    private XContentBuilder createDoc(Post post) {
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("subject", post.getSubject());
            b.field("content", post.getContent());
            b.field("tag", post.getTag());
            b.field(contentcategory, post.getCategoryPath());
            b.field(actioncategory, post.getActionCategoryPath());
            b.field("appId", post.getAppId());
            b.field("forumId", post.getForumId());
            b.field("categoryId", post.getCategoryId());
            b.field("creatorUid", post.getCreatorUid());
            b.field("visibleRegionType", post.getVisibleRegionType());
            b.field("visibleRegionId", post.getVisibleRegionId());
            b.field("visibleRegionType", post.getVisibleRegionType());
            b.field("visibleRegionId", post.getVisibleRegionId());
            b.field("parentPostId", post.getParentPostId());
            b.field("embeddedAppId", post.getEmbeddedAppId());
            b.field("cloneFlag", post.getCloneFlag());
            Forum forum = forumProvider.findForumById(post.getForumId());
            Integer namespaceId = Namespace.DEFAULT_NAMESPACE;
            if(forum != null) {
                namespaceId = forum.getNamespaceId();
            }
            b.field("namespaceId", namespaceId);
            
            User u = userProvider.findUserById(post.getCreatorUid());
            if(null != u) {
                b.field("senderName", u.getAccountName());
                b.field("senderAvatar", u.getAvatar());      
            } else {
                b.field("senderName", "");
                b.field("senderAvatar", "");    
                }
          
            b.field("forumName", 0);
            b.field("displayName", "");
            
            //Sender's info
            b.field("embeddedId", post.getEmbeddedId());
            b.field("creatorNickName", post.getCreatorNickName());
            
            UserIdentifier identify = userProvider.findClaimedIdentifierByOwnerAndType(post.getCreatorUid(), IdentifierType.MOBILE.getCode());
            if(null != identify) {
                b.field("identify", identify.getIdentifierToken());    
            } else {
                b.field("identify", "");
                }
            
            
            //http://stackoverflow.com/questions/16113439/elasticsearch-geo-distance-filter-with-multiple-locations-in-array-possible
            b.startObject("location");
            if(null == post.getLatitude() || null == post.getLongitude()) {
                b.field("lat", 0);
                b.field("lon", 0);
            } else {
                b.field("lat", post.getLatitude());
                b.field("lon", post.getLongitude());    
                }
            
            b.endObject();
            
            b.field("createTime", new Date(post.getCreateTime().getTime()));
            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create group " + post.getId() + " error");
            return null;
        }
    }

    @Override
    public void bulkUpdate(List<Post> posts) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (Post post : posts) {
        	
            XContentBuilder source = createDoc(post);
            if(null != source) {
                LOGGER.info("id:" + post.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(post.getId().toString()).source(source));    
                }
            
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

    @Override
    public void feedDoc(Post post) {
        XContentBuilder source = createDoc(post);
        
        feedDoc(post.getId().toString(), source);
    }

    @Override
    public void syncFromDb() {
        List<Post> posts = new ArrayList<Post>();
        int pageSize = 200;
        this.deleteAll();
        
        this.forumProvider.iteratePosts(pageSize, new IteratePostCallback() {

            @Override
            public void process(Post post) {
                posts.add(post);
                if(posts.size() >= pageSize) {
                    bulkUpdate(posts);
                    posts.clear();
                    }
                }
            
        }, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                //query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0l));
            	query.addConditions(Tables.EH_FORUM_POSTS.STATUS.eq(PostStatus.ACTIVE.getCode()));
                return query;
            }
            
        });
        
        if(posts.size() > 0) {
            this.bulkUpdate(posts);
            posts.clear();
            LOGGER.info("posts processStat count: " + posts.size());
        }
        
        this.optimize(1);
        this.refresh();
        
    }
   
    private FilterBuilder boolInFilter(String term, List<Long> values) {
        List<FilterBuilder> ors = new ArrayList<FilterBuilder>();
        for(Long l : values) {
            ors.add(FilterBuilders.termFilter(term, l));
        }
        if(ors.size() == 1) {
            return ors.get(0);
        }
        
        return FilterBuilders.boolFilter().should(ors.toArray(new FilterBuilder[ors.size()]));
    }
    
    private FilterBuilder getForumFilter(SearchTopicCommand cmd) {
        FilterBuilder fb = null;
        PostSearchFlag searchFlag = PostSearchFlag.fromCode(cmd.getSearchFlag());
        if(cmd.getSearchFlag() != null && searchFlag == PostSearchFlag.GLOBAL) {
            FilterBuilder comFilter = null;
            if(cmd.getCommunityId() != null) {
                GetNearbyCommunitiesByIdCommand nearCmd = new GetNearbyCommunitiesByIdCommand();
                nearCmd.setId(cmd.getCommunityId());
                Community community = communityProvider.findCommunityById(cmd.getCommunityId());
                Long forumId = ForumConstants.SYSTEM_FORUM;
                List<Long> comIds = new ArrayList<Long>();
                if(community != null) {
                    forumId = community.getDefaultForumId();
                    comIds.add(community.getId());
                }
//                List<CommunityDTO> coms = communityService.getNearbyCommunityById(nearCmd);
//                List<Long> comIds = new ArrayList<Long>();
//                for(CommunityDTO cDTO : coms) {
//                    comIds.add(cDTO.getId());
//                    }

                //加入了全部范围的帖子
                FilterBuilder allFilterType =FilterBuilders.termFilter("visibleRegionType", (long)VisibleRegionType.ALL.getCode());
                FilterBuilder allCloneFlag = FilterBuilders.termFilter("cloneFlag", (long) PostCloneFlag.NORMAL.getCode());
                FilterBuilder allForum = FilterBuilders.termFilter("forumId", forumId);
                FilterBuilder allFilter = FilterBuilders.boolFilter().must(allFilterType, allCloneFlag, allForum);

                if(comIds.size() > 0) {
                    FilterBuilder comIn = boolInFilter("visibleRegionId", comIds);
                    FilterBuilder comType = FilterBuilders.termFilter("visibleRegionType", (long)VisibleRegionType.COMMUNITY.getCode());
                    FilterBuilder comForum = FilterBuilders.termFilter("forumId", forumId);
                    comFilter = FilterBuilders.boolFilter().must(comIn, comType, comForum);

                    //加入了全部范围的帖子
                    comFilter = FilterBuilders.boolFilter().should(comFilter, allFilter);


                }
                
                //覆盖当前小区的所有机构（含各级上级机构），不管是发给这些机构的帖还是这些机构发的帖都满足要求 by xiongying 20161019
                List<Long> organizationIdList = organizationService.getOrganizationIdsTreeUpToRoot(cmd.getCommunityId());
                if(organizationIdList.size() > 0) {
                	FilterBuilder orgIn = boolInFilter("visibleRegionId", organizationIdList);
                	FilterBuilder orgType = FilterBuilders.termFilter("visibleRegionType", (long)VisibleRegionType.REGION.getCode());
                	FilterBuilder orgForum = FilterBuilders.termFilter("forumId", forumId);
                	FilterBuilder orgFilter = FilterBuilders.boolFilter().must(orgIn, orgType, orgForum);

                    //加入了全部范围的帖子
                    orgFilter = FilterBuilders.boolFilter().should(orgFilter, allFilter);

                	comFilter = FilterBuilders.boolFilter().should(comFilter, orgFilter);


                }
            }
            
            List<GroupDTO> groups = groupService.listUserRelatedGroups();
            List<Long> groupForumIds = new ArrayList<Long>();
//            Integer namespaceId = (cmd.getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : cmd.getNamespaceId();
//            if(namespaceId == Namespace.DEFAULT_NAMESPACE){
//            	 groupForumIds.add(1l);
//            }
            for(GroupDTO groupDTO : groups) {
                if(groupDTO.getOwningForumId() != null) {
                    groupForumIds.add(groupDTO.getOwningForumId());
                }
            }
            
            if(groupForumIds.size() > 0) {
                if(null == comFilter) {
                    comFilter = boolInFilter("forumId", groupForumIds);
                } else {
                    comFilter = FilterBuilders.boolFilter().should(comFilter, boolInFilter("forumId", groupForumIds)); 
                     }
                 }
            
            
//            comFilter = FilterBuilders.boolFilter().should(comFilter, FilterBuilders.termFilter("namespaceId", namespaceId));
            fb = comFilter;
        } else {
            if(cmd.getForumId() != null) {
                fb = FilterBuilders.termFilter("forumId", cmd.getForumId());    
            }
        }
        
        if(null == fb) {
            fb = FilterBuilders.termFilter("parentPostId", 0);
        } else {
            fb = FilterBuilders.boolFilter().must(fb, FilterBuilders.termFilter("parentPostId", 0));
        }
        
        return fb;
    }
    

    /**
	 * 拼接搜索串的部分移出来并增加highlight部分，以便后续处理
	 * xiongying
	 */
    private SearchResponse getQuery(SearchTopicCommand cmd) {
    	
    	SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        
        QueryBuilder qb = null;
        if(cmd.getQueryString() == null || cmd.getQueryString().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getQueryString())
                    .field("subject", 1.2f)
                    .field("content", 1.0f)
                    .field("tag", 1.0f).prefixLength(2);
            
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("subject").addHighlightedField("content").addHighlightedField("tag");
            }
        
        PrefixQueryBuilder prefix_a = null;
        if(cmd.getContentCategory() != null) {
            Category category = categoryProvider.findCategoryById(cmd.getContentCategory().longValue());
            if(category != null) {
                prefix_a = QueryBuilders.prefixQuery(contentcategory, category.getPath());    
                }
            }
        
        PrefixQueryBuilder prefix_b = null;
        if(cmd.getContentCategory() != null) {
            Category category = categoryProvider.findCategoryById(cmd.getActionCategory().longValue());
            if(category != null) {
                prefix_b = QueryBuilders.prefixQuery(actioncategory, category.getPath());    
                }
            }
        
        if(prefix_a != null && prefix_b != null) {
            qb = QueryBuilders.boolQuery().should(qb).should(prefix_b).should(prefix_b);
        } else if(prefix_a != null) {
            qb = QueryBuilders.boolQuery().should(qb).should(prefix_a);     
        } else if(prefix_b != null) {
            qb = QueryBuilders.boolQuery().should(qb).should(prefix_b);
           }
        
        FilterBuilder fb = getForumFilter(cmd);
        
        if(!StringUtils.isEmpty(cmd.getSearchContentType())) {
        	if(SearchContentType.ACTIVITY.equals(SearchContentType.fromCode(cmd.getSearchContentType()))) {
        		if(null == fb) {
        			fb = FilterBuilders.termFilter("embeddedAppId", 3);
        		} else {
                    fb = FilterBuilders.boolFilter().must(fb, FilterBuilders.termFilter("embeddedAppId", 3));
                }
                
            } 
        
        	if(SearchContentType.POLL.equals(SearchContentType.fromCode(cmd.getSearchContentType()))) {
        		if(null == fb) {
        			fb = FilterBuilders.termFilter("embeddedAppId", 14);
        		} else {
                    fb = FilterBuilders.boolFilter().must(fb, FilterBuilders.termFilter("embeddedAppId", 14));
                }
                
            } 
        
        	if(SearchContentType.TOPIC.equals(SearchContentType.fromCode(cmd.getSearchContentType()))) {
        		 int[] notEmbeddedAppIds = new int[2];
        		 notEmbeddedAppIds[0] = 3;
        		 notEmbeddedAppIds[1] = 14;
        		 
        		FilterBuilder nfb = FilterBuilders.notFilter(FilterBuilders.termsFilter("embeddedAppId", notEmbeddedAppIds));
        		if(null == fb) {
        			fb = nfb;
        		} else {
                    fb = FilterBuilders.boolFilter().must(fb, nfb);
                }
                
            } 
        }
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
        
        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);
        
        if(cmd.getQueryString() == null || cmd.getQueryString().isEmpty()) {
            builder.addSort("createTime", SortOrder.DESC);
        }
        
        if(LOGGER.isDebugEnabled())
			LOGGER.info("PostSearcherImpl query builder ："+builder);
        
        SearchResponse rsp = builder.execute().actionGet();
    	return rsp;
    }
    
    @Override
    public ListPostCommandResponse query(SearchTopicCommand cmd) {
    	SearchResponse rsp = getQuery(cmd);
        List<Long> ids = getIds(rsp);
        
        if(LOGGER.isDebugEnabled())
			LOGGER.info("PostSearcherImpl query SearchResponse ids ："+ids);
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
        
        ListPostCommandResponse listPost = new ListPostCommandResponse();
        if(ids.size() > pageSize) {
            listPost.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
         } else {
            listPost.setNextPageAnchor(null);
            }
        
        List<PostDTO> posts = this.forumService.getTopicById(ids, cmd.getCommunityId(), false);
        if(null != cmd.getQueryString() && !cmd.getQueryString().isEmpty()) {
            List<String> ss = this.analyze("ansj_query", cmd.getQueryString());
            listPost.setKeywords(String.join(" ", ss));
            }
        
        listPost.setPosts(posts);
        
        return listPost;
    }
    
    @Override
   public ListPostCommandResponse query(QueryMaker filter) {
       SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
       filter.makeQueryBuilder(builder);
       
       SearchResponse rsp = builder.execute().actionGet();
       List<Long> ids = getIds(rsp);
       
       ListPostCommandResponse listPost = new ListPostCommandResponse();
       if(ids.size() > filter.getPageSize()) {
           listPost.setNextPageAnchor(new Long(filter.getPageNumber() + 1));
           ids.remove(ids.size() - 1);
        } else {
           listPost.setNextPageAnchor(null);
           }
       
       List<PostDTO> posts = new ArrayList<PostDTO>();
       for(Long id : ids) {
           PostDTO p =  ConvertHelper.convert(this.forumProvider.findPostById(id.longValue()), PostDTO.class);
           if(p != null) {
               posts.add(p);
               }
           }
       
       listPost.setPosts(posts);
       
       return listPost;
   }
    
//    @Override
//    public ListPostCommandResponse queryByScene(SearchTopicBySceneCommand cmd) {
//        User user = UserContext.current().getUser();
//        Long userId = user.getId();
//        SceneTokenDTO sceneToken = userService.checkSceneToken(userId, cmd.getSceneToken());
//        
//        ListPostCommandResponse response = null;
//        SearchTopicCommand cmntyTopicCmd = null;
//        
//        Integer namespaceId = sceneToken.getNamespaceId();
//        Long forumId = 0L;
//        UserCurrentEntityType entityType = UserCurrentEntityType.fromCode(sceneToken.getEntityType());
//        switch(entityType) {
//        case COMMUNITY_RESIDENTIAL:
//        case COMMUNITY_COMMERCIAL:
//        case COMMUNITY:
//            Community community = communityProvider.findCommunityById(sceneToken.getEntityId());
//            if(community != null) {
//                forumId = community.getDefaultForumId();
//                
//                cmntyTopicCmd = ConvertHelper.convert(cmd, SearchTopicCommand.class);
//                cmntyTopicCmd.setNamespaceId(namespaceId);
//                cmntyTopicCmd.setCommunityId(community.getId());
//                cmntyTopicCmd.setSearchFlag(PostSearchFlag.GLOBAL.getCode());
//                response = query(cmntyTopicCmd);
//            } else {
//                if(LOGGER.isWarnEnabled()) {
//                    LOGGER.warn("Community not found, userId=" + userId + ", namespaceId=" + namespaceId + ", sceneToken=" + sceneToken);
//                }
//            }
//            break;
//        case FAMILY:
//            FamilyDTO family = familyProvider.getFamilyById(sceneToken.getEntityId());
//            if(family != null) {
//                community = communityProvider.findCommunityById(family.getCommunityId());
//                if(community != null) {
//                    forumId = community.getDefaultForumId();
//
//                    cmntyTopicCmd = ConvertHelper.convert(cmd, SearchTopicCommand.class);
//                    cmntyTopicCmd.setNamespaceId(namespaceId);
//                    cmntyTopicCmd.setCommunityId(community.getId());
//                    cmntyTopicCmd.setSearchFlag(PostSearchFlag.GLOBAL.getCode());
//                    response = query(cmntyTopicCmd);
//                } else {
//                    if(LOGGER.isWarnEnabled()) {
//                        LOGGER.warn("Community not found, sceneToken=" + sceneToken + ", communityId=" + family.getCommunityId());
//                    }
//                }
//            } else {
//                if(LOGGER.isWarnEnabled()) {
//                    LOGGER.warn("Family not found, sceneToken=" + sceneToken);
//                }
//            }
//            break;
//        case ORGANIZATION:
//            response = queryGlobalPostByOrganizationId(cmd, sceneToken, sceneToken.getEntityId());
//            break;
//        default:
//            break;
//        }
//        
//        return response;
//    }
    
    @Override
    public ListPostCommandResponse queryByScene(SearchTopicBySceneCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        SceneTokenDTO sceneToken = userService.checkSceneToken(userId, cmd.getSceneToken());
        
        ListPostCommandResponse response = null;
        
        Integer namespaceId = sceneToken.getNamespaceId();
        SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
        switch(sceneType) {
        case DEFAULT:
        case PARK_TOURIST:
            response = queryGlobalPostByCommunityId(namespaceId, cmd, sceneToken, sceneToken.getEntityId());
            break;
        case FAMILY:
            FamilyDTO family = familyProvider.getFamilyById(sceneToken.getEntityId());
            if(family != null) {
                response = queryGlobalPostByCommunityId(namespaceId, cmd, sceneToken, family.getCommunityId());
            } else {
                if(LOGGER.isWarnEnabled()) {
                    LOGGER.warn("Family not found, sceneToken=" + sceneToken);
                }
            }
            break;
        case ENTERPRISE: // 增加两场景，与园区企业保持一致 by lqs 20160517
        case ENTERPRISE_NOAUTH: // 增加两场景，与园区企业保持一致 by lqs 20160517
            Organization organization = organizationProvider.findOrganizationById(sceneToken.getEntityId());
            if(organization != null) {
                Long communityId = organizationService.getOrganizationActiveCommunityId(organization.getId());
                response = queryGlobalPostByCommunityId(namespaceId, cmd, sceneToken, communityId);
            }
            break;
        case PM_ADMIN:
            response = queryGlobalPostByOrganizationId(cmd, sceneToken, sceneToken.getEntityId());
            break;
        default:
            break;
        }
        
        return response;
    }
    
    @Override
    public SearchResponse searchByScene(SearchTopicBySceneCommand cmd) {
        User user = UserContext.current().getUser();
        Long userId = user.getId();
        //TODO 标准版要求没有场景，sceneTokenDTO固定为null，业务可能需要修改。有需要的话可以用 UserContext.current().getAppContext()的数据
        //SceneTokenDTO sceneToken = userService.checkSceneToken(userId, cmd.getSceneToken());

        AppContext appContext = UserContext.current().getAppContext();
        SearchResponse response = null;


        //Integer namespaceId = sceneToken.getNamespaceId();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
//                SceneType sceneType = SceneType.fromCode(sceneToken.getScene());
//        switch(sceneType) {
//        case DEFAULT:
//        case PARK_TOURIST:
            response = searchGlobalPostByCommunityId(namespaceId, cmd, null, appContext.getCommunityId());
//            break;
//        case FAMILY:
//            FamilyDTO family = familyProvider.getFamilyById(sceneToken.getEntityId());
//            if(family != null) {
//                response = searchGlobalPostByCommunityId(namespaceId, cmd, sceneToken, family.getCommunityId());
//            } else {
//                if(LOGGER.isWarnEnabled()) {
//                    LOGGER.warn("Family not found, sceneToken=" + sceneToken);
//                }
//            }
//            break;
//        case ENTERPRISE: // 增加两场景，与园区企业保持一致 by lqs 20160517
//        case ENTERPRISE_NOAUTH: // 增加两场景，与园区企业保持一致 by lqs 20160517
//            Organization organization = organizationProvider.findOrganizationById(sceneToken.getEntityId());
//            if(organization != null) {
//                Long communityId = organizationService.getOrganizationActiveCommunityId(organization.getId());
//                response = searchGlobalPostByCommunityId(namespaceId, cmd, sceneToken, communityId);
//            }
//            break;
//        case PM_ADMIN:
//        	SearchByMultiForumAndCmntyCommand orgTopicCmd = getGlobalPostByOrganizationIdQuery(cmd, sceneToken, sceneToken.getEntityId());
//        	response = getQueryByMultiForumAndCmnty(orgTopicCmd);
//            break;
//        default:
//            break;
//        }
//
        return response;
    }
    
    
    
    private ListPostCommandResponse queryGlobalPostByCommunityId(Integer namespaceId, SearchTopicBySceneCommand cmd, 
        SceneTokenDTO sceneToken, Long communityId) {
        Community community = communityProvider.findCommunityById(communityId);
        if(community != null) {
            SearchTopicCommand cmntyTopicCmd = ConvertHelper.convert(cmd, SearchTopicCommand.class);
            cmntyTopicCmd.setNamespaceId(namespaceId);
            cmntyTopicCmd.setCommunityId(community.getId());
            cmntyTopicCmd.setSearchFlag(PostSearchFlag.GLOBAL.getCode());
            return query(cmntyTopicCmd);
        } else {
            if(LOGGER.isWarnEnabled()) {
                LOGGER.warn("Community not found, sceneToken=" + sceneToken + ", communityId=" + communityId);
            }
        }
        
        return null;
    }
    
    private SearchResponse searchGlobalPostByCommunityId(Integer namespaceId, SearchTopicBySceneCommand cmd, 
            SceneTokenDTO sceneToken, Long communityId) {
            Community community = communityProvider.findCommunityById(communityId);
            if(community != null) {
                SearchTopicCommand cmntyTopicCmd = ConvertHelper.convert(cmd, SearchTopicCommand.class);
                cmntyTopicCmd.setNamespaceId(namespaceId);
                cmntyTopicCmd.setCommunityId(community.getId());
                cmntyTopicCmd.setSearchFlag(PostSearchFlag.GLOBAL.getCode());
                cmntyTopicCmd.setSearchContentType(cmd.getSearchContentType());
                
                SearchResponse searchResponse = getQuery(cmntyTopicCmd);
                return searchResponse;
            } else {
                if(LOGGER.isWarnEnabled()) {
                    AppContext appContext = UserContext.current().getAppContext();
                    LOGGER.warn("Community not found, appContext=" + appContext + ", communityId=" + communityId);
                }
            }
            
            return null;
        
    }
    
    private SearchByMultiForumAndCmntyCommand getGlobalPostByOrganizationIdQuery(SearchTopicBySceneCommand cmd, 
            SceneTokenDTO sceneToken, Long organizationId) {
    	List<Long> forumIdList = new ArrayList<Long>();
        List<Long> organizationList = new ArrayList<Long>();
        
        // 本公司论坛
        Organization org = this.organizationProvider.findOrganizationById(organizationId);
        if(org != null) {
            organizationList.add(org.getId());
        }
        Long forumId = getOrganizationForumId(sceneToken, org);
        if(forumId != null) {
            forumIdList.add(forumId);
        }
        
        // 所有子公司论坛
        Group group = null;
        if(org != null) {
            List<String> groupTypes = new ArrayList<String>();
            groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
            List<Organization> subOrgList = organizationProvider.listOrganizationByGroupTypes(org.getPath() + "/%", groupTypes);
            if(subOrgList != null && subOrgList.size() > 0) {
                for(Organization subOrg : subOrgList) {
                    organizationList.add(subOrg.getId());
                    if(subOrg.getGroupId() != null) {
                        group = groupProvider.findGroupById(subOrg.getGroupId());
                        if(group != null && group.getOwningForumId() != null) {
                            forumIdList.add(group.getOwningForumId());
                        }
                    }
                }
            }
        }
        
        // 所管辖的小区
        List<Long> communityIdList = new ArrayList<Long>();
        List<CommunityDTO> communities = organizationService.listAllChildrenOrganizationCoummunities(organizationId);
        for(CommunityDTO community : communities) {
            communityIdList.add(community.getId());
        }
        

        SearchByMultiForumAndCmntyCommand orgTopicCmd = ConvertHelper.convert(cmd, SearchByMultiForumAndCmntyCommand.class);
        orgTopicCmd.setCommunityIds(communityIdList);
        orgTopicCmd.setForumIds(forumIdList);
        orgTopicCmd.setRegionIds(organizationList);
        orgTopicCmd.setSearchContentType(cmd.getSearchContentType());
        
        if(LOGGER.isDebugEnabled())
        	LOGGER.info("getGlobalPostByOrganizationIdQuery: orgTopicCmd = {}", orgTopicCmd);
        
        return orgTopicCmd;
    }
    
    private ListPostCommandResponse queryGlobalPostByOrganizationId(SearchTopicBySceneCommand cmd, 
            SceneTokenDTO sceneToken, Long organizationId) {
        
    	SearchByMultiForumAndCmntyCommand orgTopicCmd = getGlobalPostByOrganizationIdQuery(cmd, sceneToken, organizationId);
        return queryByMultiForumAndCmnty(orgTopicCmd);
    }
    
    private Long getOrganizationForumId(SceneTokenDTO sceneToken, Organization organization) {
        if(organization != null && organization.getGroupId() != null) {
            Group group = groupProvider.findGroupById(organization.getGroupId());
            if(group != null && group.getOwningForumId() != null) {
                return group.getOwningForumId();
            } else {
                if(LOGGER.isWarnEnabled()) {
                    LOGGER.warn("Organization group not found, sceneToken=" + sceneToken + ", groupId=" + organization.getGroupId());
                }
            }
        } else {
            if(LOGGER.isWarnEnabled()) {
                LOGGER.warn("Organization not found, sceneToken=" + sceneToken);
            }
        }
        
        return null;
    }

    private SearchResponse getQueryByMultiForumAndCmnty(SearchByMultiForumAndCmntyCommand cmd) {
		SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        
        QueryBuilder qb = null;
        if(cmd.getQueryString() == null || cmd.getQueryString().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getQueryString())
                    .field("subject", 1.2f)
                    .field("content", 1.0f).prefixLength(2);
            
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("subject").addHighlightedField("content");
            
        }
        
        FilterBuilder fb = null;
        
        if(!StringUtils.isEmpty(cmd.getSearchContentType())) {
        	if(SearchContentType.ACTIVITY.equals(SearchContentType.fromCode(cmd.getSearchContentType()))) {

        		fb = FilterBuilders.termFilter("embeddedAppId", 3);
            } 
        
        	if(SearchContentType.POLL.equals(SearchContentType.fromCode(cmd.getSearchContentType()))) {
        		if(null == fb) {
        			fb = FilterBuilders.termFilter("embeddedAppId", 14);
        		} else {
                    fb = FilterBuilders.boolFilter().must(fb, FilterBuilders.termFilter("embeddedAppId", 14));
                }
                
            } 
        
        	if(SearchContentType.TOPIC.equals(SearchContentType.fromCode(cmd.getSearchContentType()))) {
        		 int[] notEmbeddedAppIds = new int[2];
        		 notEmbeddedAppIds[0] = 3;
        		 notEmbeddedAppIds[1] = 14;
        		 
        		FilterBuilder nfb = FilterBuilders.termsFilter("embeddedAppId", notEmbeddedAppIds);
        		if(null == fb) {
        			fb = FilterBuilders.notFilter(nfb);
        		} else {
                    fb = FilterBuilders.boolFilter().mustNot(fb, nfb);
                }
                
            } 
        }



        //加入了全部范围的帖子
        FilterBuilder allFilter =FilterBuilders.termFilter("visibleRegionType", (long)VisibleRegionType.ALL.getCode());

        // 社区论坛里符合小区的过滤条件
        FilterBuilder cmntyFilter = null;
        if(cmd.getCommunityIds() != null && cmd.getCommunityIds().size() > 0) {
            FilterBuilder comIn = boolInFilter("visibleRegionId", cmd.getCommunityIds());
            FilterBuilder comType = FilterBuilders.termFilter("visibleRegionType", (long)VisibleRegionType.COMMUNITY.getCode());
            FilterBuilder cloneFlag = FilterBuilders.termFilter("cloneFlag", (long) PostCloneFlag.NORMAL.getCode());
            cmntyFilter = FilterBuilders.boolFilter().must(comIn, comType, cloneFlag);

            cmntyFilter = FilterBuilders.boolFilter().should(cmntyFilter, allFilter);
        }
        
        // 社区论坛里符合片区的过滤条件
        FilterBuilder regionFilter = null;
        if(cmd.getRegionIds() != null && cmd.getRegionIds().size() > 0) {
            FilterBuilder comIn = boolInFilter("visibleRegionId", cmd.getRegionIds());
            FilterBuilder comType = FilterBuilders.termFilter("visibleRegionType", (long)VisibleRegionType.REGION.getCode());
            FilterBuilder cloneFlag = FilterBuilders.termFilter("cloneFlag", (long) PostCloneFlag.NORMAL.getCode());
            regionFilter = FilterBuilders.boolFilter().must(comIn, comType, cloneFlag);

            regionFilter = FilterBuilders.boolFilter().should(regionFilter, allFilter);
        }
        
        // 其它论坛的过滤条件
        FilterBuilder otherForumFilter = null;
        if(cmd.getForumIds() != null && cmd.getForumIds().size() > 0) {
            otherForumFilter = boolInFilter("forumId", cmd.getForumIds());
        }
        
        // 域空间条件
        FilterBuilder namespaceFilter = null;
        if(cmd.getNamespaceId() == null){
            cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        }
        if(cmd.getNamespaceId() != null) {
            namespaceFilter = FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId());
        }

        // 对于一家公司，其全部可见帖的范围为：1. 本公司及所有子公司的多个论坛，2. 本公司的社区圈里所有管辖的小区及公司对应的片区
        FilterBuilder resultFilter = cmntyFilter;
        if(resultFilter == null) {
            resultFilter = regionFilter;
        } else {
            if(regionFilter != null) {
                resultFilter = FilterBuilders.boolFilter().should(resultFilter, regionFilter);
            }
        }
        if(resultFilter == null) {
            resultFilter = otherForumFilter;
        } else {
            if(otherForumFilter != null) {
                resultFilter = FilterBuilders.boolFilter().should(resultFilter, otherForumFilter);
            }
        }
        if(resultFilter == null) {
            resultFilter = namespaceFilter;
        } else {
            if(namespaceFilter != null) {
                resultFilter = FilterBuilders.boolFilter().must(resultFilter, namespaceFilter);
            }
        }
        
        fb = FilterBuilders.boolFilter().must(fb, resultFilter);
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
        
        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);
        
        if(LOGGER.isDebugEnabled())
			LOGGER.info("PostSearcherImpl query builder ："+builder);
        
        SearchResponse rsp = builder.execute().actionGet();

        return rsp;
    }

	@Override
	public ListPostCommandResponse queryByMultiForumAndCmnty(SearchByMultiForumAndCmntyCommand cmd) {
		SearchResponse rsp = getQueryByMultiForumAndCmnty(cmd);
		List<Long> ids = getIds(rsp);
        
        if(LOGGER.isDebugEnabled())
			LOGGER.info("PostSearcherImpl query SearchResponse ids ："+ids);
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
        
        ListPostCommandResponse listPost = new ListPostCommandResponse();
        if(ids.size() > pageSize) {
            listPost.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
         } else {
            listPost.setNextPageAnchor(null);
            }
        
        List<PostDTO> posts = this.forumService.getTopicById(ids, cmd.getCommunityIds(), false);
        if(null != cmd.getQueryString() && !cmd.getQueryString().isEmpty()) {
            List<String> ss = this.analyze("ansj_query", cmd.getQueryString());
            listPost.setKeywords(String.join(" ", ss));
            }
        
        listPost.setPosts(posts);
        
        return listPost;
	}
}
