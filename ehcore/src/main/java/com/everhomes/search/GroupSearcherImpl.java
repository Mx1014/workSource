package com.everhomes.search;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.everhomes.group.GroupDTO;

@Service
public class GroupSearcherImpl extends AbstractElasticSearch implements GroupSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupSearcherImpl.class);
    
    @Override
    public String getIndexName() {
        return SearchUtils.GROUPINDEXNAME;
    }

    @Override
    public String getIndexType() {
        return SearchUtils.GROUPINDEXTYPE;
    }
    
    private XContentBuilder createDoc(GroupDTO group){
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("name", group.getName());
            b.field("description", group.getDescription());
            b.field("creatorUid", group.getCreatorUid());
            b.field("categoryId", group.getCategoryId());
            b.field("createTime", group.getCreateTime());
            
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
    public void bulkUpdate(List<GroupDTO> groups) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (GroupDTO group : groups) {
            XContentBuilder source = createDoc(group);
            brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                    .id(group.getId().toString()).source(source));
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }
    
    @Override
    public void feedDoc(GroupDTO group) {
        XContentBuilder source = createDoc(group);
        
        feedDoc(group.getId().toString(), source);
        
    }

}
