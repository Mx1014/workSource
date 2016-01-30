package com.everhomes.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.base.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.CommunityService;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.forum.Forum;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.ForumService;
import com.everhomes.forum.IteratePostCallback;
import com.everhomes.forum.Post;
import com.everhomes.group.GroupService;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.category.CategoryAdminStatus;
import com.everhomes.rest.community.GetNearbyCommunitiesByIdCommand;
import com.everhomes.rest.forum.ForumConstants;
import com.everhomes.rest.forum.ListPostCommandResponse;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.forum.PostQueryResult;
import com.everhomes.rest.forum.PostStatus;
import com.everhomes.rest.forum.SearchTopicCommand;
import com.everhomes.rest.group.GroupDTO;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.visibility.VisibleRegionType;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

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
    CategoryProvider categoryProvider;
    
    @Autowired
    GroupService groupService;
    
    @Autowired
    private CommunityService communityService;
    
    @Autowired
    ForumService forumService;
    
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
            LOGGER.info("posts process count: " + posts.size());
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
        if(cmd.getSearchFlag() != null && cmd.getSearchFlag() == 1) {
            GetNearbyCommunitiesByIdCommand nearCmd = new GetNearbyCommunitiesByIdCommand();
            nearCmd.setId(cmd.getCommunityId());
            List<CommunityDTO> coms = communityService.getNearbyCommunityById(nearCmd);
            List<Long> comIds = new ArrayList<Long>();
            for(CommunityDTO cDTO : coms) {
                comIds.add(cDTO.getId());
                }
            
            FilterBuilder comFilter = null;
            if(comIds.size() > 0) {
                FilterBuilder comIn = boolInFilter("visibleRegionId", comIds);
                FilterBuilder comType = FilterBuilders.termFilter("visibleRegionType", (long)VisibleRegionType.COMMUNITY.getCode());
                FilterBuilder comForum = FilterBuilders.termFilter("forumId", ForumConstants.SYSTEM_FORUM);
                comFilter = FilterBuilders.andFilter(comIn, comType, comForum);
                }
            
            List<GroupDTO> groups = groupService.listUserRelatedGroups();
            List<Long> groupIds = new ArrayList<Long>();
            Integer namespaceId = (cmd.getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : cmd.getNamespaceId();
            if(namespaceId == Namespace.DEFAULT_NAMESPACE){
            	 groupIds.add(1l);
            }
            for(GroupDTO groupDTO : groups) {
                groupIds.add(groupDTO.getId());
                }
            
            if(groupIds.size() > 0) {
                if(null == comFilter) {
                    comFilter = boolInFilter("forumId", groupIds);
                } else {
                    comFilter = FilterBuilders.boolFilter().should(comFilter,boolInFilter("forumId", groupIds)); 
                     }
                 }
            
            
            comFilter = FilterBuilders.boolFilter().should(comFilter, FilterBuilders.termFilter("namespaceId", namespaceId));
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

    @Override
    public ListPostCommandResponse query(SearchTopicCommand cmd) {
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
        List<Long> ids = getIds(rsp);
        
        if(LOGGER.isDebugEnabled())
			LOGGER.info("PostSearcherImpl query SearchResponse ids ："+ids);
        
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
}
