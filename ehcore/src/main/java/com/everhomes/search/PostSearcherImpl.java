package com.everhomes.search;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everhomes.forum.ForumProvider;
import com.everhomes.forum.Post;
import com.everhomes.forum.PostQueryResult;
import com.everhomes.forum.SearchTopicCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;

public class PostSearcherImpl extends AbstractElasticSearch implements PostSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostSearcherImpl.class);
    
    @Autowired
    ForumProvider forumPprovider;
    
    @Autowired
    UserProvider userProvider;
    
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
            b.field("category", post.getCategoryPath());
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
        //TODO
    }

    @Override
    public PostQueryResult query(QueryMaker filter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PostQueryResult query(SearchTopicCommand cmd) {
        // TODO Auto-generated method stub
        return null;
    }

}
