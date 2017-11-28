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

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.group.Group;
import com.everhomes.group.GroupAdminStatus;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.group.GroupProvider;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.group.GroupPrivacy;
import com.everhomes.rest.group.SearchGroupCommand;
import com.everhomes.rest.search.GroupQueryResult;
import com.everhomes.settings.PaginationConfigHelper;

@Service
public class GroupSearcherImpl extends AbstractElasticSearch implements GroupSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupSearcherImpl.class);
   
    @Autowired
    private GroupProvider groupProvider;
    
    @Autowired
    private ConfigurationProvider  configProvider;

    @Override
    public String getIndexType() {
        return SearchUtils.GROUPINDEXTYPE;
    }
    
    @Override
    public GroupQueryResult query(QueryMaker filter) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        filter.makeQueryBuilder(builder);
        
        SearchResponse rsp = builder.execute().actionGet();
        List<Long> ids = getIds(rsp);
        
        GroupQueryResult result = new GroupQueryResult();
        if(ids.size() > filter.getPageSize()) {
            result.setPageAnchor(new Long(filter.getPageNumber() + 1));
            ids.remove(ids.size() - 1);
         } else {
            result.setPageAnchor(null);    
            }
        
        result.setIds(ids);
        
        return result;
    }
    
    private XContentBuilder createDoc(Group group){
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("name", group.getName());
            b.field("namespaceId", group.getNamespaceId());
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
                LOGGER.info("Sync group(update), id=" + group.getId());
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
        
        this.deleteAll();
        
        this.groupProvider.iterateGroups(pageSize, GroupDiscriminator.GROUP, (group)->{
            if((group.getPrivateFlag().equals(GroupPrivacy.PUBLIC.getCode())) 
            		&& GroupAdminStatus.fromCode(group.getStatus()) == GroupAdminStatus.ACTIVE){
                groups.add(group); 
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Sync group(add), id=" + group.getId());
                }
            } else {
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Sync group(ignore), id=" + group.getId());
                }
            }
            
            if(groups.size() >= pageSize) {
                this.bulkUpdate(groups);
                groups.clear();
                LOGGER.info("Sync group(iterate), processStat count: " + groups.size());
            }
        });
        
        if(groups.size() > 0) {
            this.bulkUpdate(groups);
            groups.clear();
            LOGGER.info("Sync group(syncupdate), processStat count: " + groups.size());
        }
        
        //TODO merge ?
        //http://www.elastic.co/guide/en/elasticsearch/guide/current/merge-process.html
        this.optimize(1);
        this.refresh();
    }

    @SuppressWarnings("unchecked")
    @Override
    public GroupQueryResult query(SearchGroupCommand cmd) {
        GroupQueryFilter filter = new GroupQueryFilter();
        int pageNum = 0;
        if(cmd.getPageAnchor() != null) {
            pageNum = cmd.getPageAnchor().intValue();
        }

        Integer namespaceId = (cmd.getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : cmd.getNamespaceId();
        @SuppressWarnings("rawtypes")
        List namespaceList = new ArrayList();
        namespaceList.add(namespaceId);
        
        filter.setPageInfo(pageNum, PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize()));
        filter.addQueryTerm(GroupQueryFilter.TERM_NAME).addQueryTerm(GroupQueryFilter.TERM_TAG);
        filter.includeFilter(GroupQueryFilter.TERM_NAMESPACE_ID, namespaceList);
        filter.addQueryTerm(GroupQueryFilter.TERM_NAME).addQueryTerm(GroupQueryFilter.TERM_NAMESPACE_ID);
        filter.setQueryString(cmd.getQueryString());
        return this.query(filter);
    }

}
