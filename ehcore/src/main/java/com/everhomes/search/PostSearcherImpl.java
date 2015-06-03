package com.everhomes.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
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
import com.everhomes.category.CategoryAdminStatus;
import com.everhomes.category.CategoryProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.IteratePostCallback;
import com.everhomes.forum.ListPostCommandResponse;
import com.everhomes.forum.Post;
import com.everhomes.forum.PostDTO;
import com.everhomes.forum.PostQueryResult;
import com.everhomes.forum.SearchTopicCommand;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
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
    CategoryProvider categoryProvider;
    
    private final String contentcategory = "contentcategory";
    private final String actioncategory = "actioncategory";
    
    @Override
    public String getIndexName() {
        return SearchUtils.TOPICINDEXNAME;
    }

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
            User u = userProvider.findUserById(post.getCreatorUid());
            
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
            b.field("visibleRegionId", post.getVisibleRegionId().intValue());
            b.field("senderName", u.getAccountName());
            b.field("senderAvatar", u.getAvatar());
            b.field("forumName", 0);
            b.field("displayName", "");
            
            //http://stackoverflow.com/questions/16113439/elasticsearch-geo-distance-filter-with-multiple-locations-in-array-possible
            b.startObject("location");
            b.field("lat", post.getLatitude());
            b.field("lon", post.getLongitude());
            b.endObject();
            
            b.field("sendTime", post.getCreateTime());
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
        AtomicInteger count = new AtomicInteger();
        this.deleteAll();
        
        this.forumProvider.iteratePosts(pageSize, new IteratePostCallback() {

            @Override
            public void process(Post post) {
                if(posts.size() >= pageSize) {
                    bulkUpdate(posts);
                    posts.clear();
                    }
               
                int real = count.addAndGet(1);
                if(real > 400) {
                    return;
                    }
            }
            
        }, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_FORUM_POSTS.PARENT_POST_ID.eq(0l));
                return query;
            }
            
        });
        
        if(posts.size() > 0) {
            this.bulkUpdate(posts);
            posts.clear();
            LOGGER.info("posts process count: " + count.get());
        }
        
    }

    @Override
    public ListPostCommandResponse query(SearchTopicCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName());
        
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
        
        FilterBuilder fb = null;
        if(cmd.getForumId() != null) {
            fb = FilterBuilders.termFilter("forumId", cmd.getForumId());    
            }           
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize().intValue());
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
        
        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);
        
        SearchResponse rsp = builder.execute().actionGet();
        List<Long> ids = getIds(rsp);
        
        ListPostCommandResponse listPost = new ListPostCommandResponse();
        if(ids.size() > pageSize) {
            listPost.setNextPageAnchor(anchor + 1);
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
        
        return listPost;
    }
}
