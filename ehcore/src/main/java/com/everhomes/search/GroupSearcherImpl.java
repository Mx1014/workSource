package com.everhomes.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

import com.everhomes.group.Group;
import com.everhomes.group.GroupDiscriminator;
import com.everhomes.group.GroupPrivacy;
import com.everhomes.group.GroupProvider;

@Service
public class GroupSearcherImpl extends AbstractElasticSearch implements GroupSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupSearcherImpl.class);
   
    @Autowired
    private GroupProvider groupProvider;
    
    @Override
    public String getIndexName() {
        return SearchUtils.GROUPINDEXNAME;
    }

    @Override
    public String getIndexType() {
        return SearchUtils.GROUPINDEXTYPE;
    }
    
    @Override
    public List<Long> query(GroupQueryFilter filter) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName());
        filter.initQueryBuilder(builder);
        
        SearchResponse rsp = builder.execute().actionGet();
        return getIds(rsp);
    }
    
    private XContentBuilder createDoc(Group group){
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("name", group.getName());
            b.field("description", group.getDescription());
            b.field("creatorUid", group.getCreatorUid());
            b.field("categoryId", group.getCategoryId());
            b.field("createTime", group.getCreateTime());
            b.field("category", group.getCategoryPath());
            b.field("categoryId", group.getCategoryId());
            
            String tagStr = group.getTag();
            if((null != tagStr) && (!tagStr.isEmpty())) {
                String[] tags = tagStr.split(",");
                if(tags.length > 0) {
                    b.startArray("tags");
                    for(String tag : tags) {
                        String newTag = tag.trim();
                        b.startObject().field("tag", newTag).endObject();
                    }
                    b.endArray();
                }   
            }
            
            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create group " + group.getId() + " error");
            return null;
        }
    }
    
    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
        
    }
    
    @Override
    public void bulkUpdate(List<Group> groups) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (Group group : groups) {
            XContentBuilder source = createDoc(group);
            if(null != source) {
                LOGGER.info("id:" + group.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(group.getId().toString()).source(source));    
                }
            
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }
    
    @Override
    public void feedDoc(Group group) {
        XContentBuilder source = createDoc(group);
        
        feedDoc(group.getId().toString(), source);
        
    }
    
    @Override
    public void syncFromDb() {
        List<Group> groups = new ArrayList<Group>();
        int pageSize = 200;
        AtomicInteger count = new AtomicInteger(); 
        
        this.deleteAll();
        
        this.groupProvider.iterateGroups(pageSize, GroupDiscriminator.GROUP, (group)->{
            if((group.getPrivateFlag().equals(GroupPrivacy.PUBLIC.getCode()))){
                groups.add(group); 
                }
            
            if(groups.size() >= pageSize) {
                this.bulkUpdate(groups);
                groups.clear();
                LOGGER.info("process count: " + count.get());
                }
            
         int real = count.addAndGet(1);
         if(real > 400) {
             return;
             }
        });
        
        if(groups.size() > 0) {
            this.bulkUpdate(groups);
            groups.clear();
            LOGGER.info("process count: " + count.get());
        }
        
        this.optimize(1);
        this.refresh();
    }

}
