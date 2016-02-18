package com.everhomes.organization;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.organization.SearchOrganizationCommand;
import com.everhomes.rest.search.GroupQueryResult;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.GroupQueryFilter;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;

@Service
public class OrganizationSearcherImpl extends AbstractElasticSearch implements OrganizationSearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationSearcherImpl.class);
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private ConfigurationProvider  configProvider;

    @Override
    public String getIndexType() {
        return SearchUtils.ENTERPRISEINDEXTYPE;
    }
    
    private XContentBuilder createDoc(Organization organization){
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("namespaceId", organization.getNamespaceId());
            b.field("communityId", organization.getCommunityId());
            b.field("name", organization.getName());
            b.field("description", organization.getDescription());
            b.field("createTime", organization.getCreateTime());
            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create group " + organization.getId() + " error");
            return null;
        }
    }
    
    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
        
    }
    
    @Override
    public void bulkUpdate(List<Organization> organizations) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (Organization organization : organizations) {
        	if(null == organization){
        		continue;
        	}
            XContentBuilder source = createDoc(organization);
            if(null != source) {
                LOGGER.info("id:" + organization.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(organization.getId().toString()).source(source));    
                }
            
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }
    
    @Override
    public void feedDoc(Organization organization) {
        XContentBuilder source = createDoc(organization);
        
        feedDoc(organization.getId().toString(), source);
        
    }
    
    @Override
    public void syncFromDb() {
        this.deleteAll();
        
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            
        	List<Organization> organizations = organizationService.getSyncDatas();
        	
            if(organizations.size() > 0) {
                this.bulkUpdate(organizations);
            }
            
            if(locator.getAnchor() == null) {
                break;
            }
        }
        
        this.optimize(1);
        this.refresh();
        
        LOGGER.info("sync for company ok");
    }

    @Override
    public GroupQueryResult query(SearchOrganizationCommand cmd) {
        GroupQueryFilter filter = new GroupQueryFilter();
        int pageNum = 0;
        if(cmd.getPageAnchor() != null) {
            pageNum = cmd.getPageAnchor().intValue();
        }
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

       SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        
        QueryBuilder qb;
        
        if(StringUtils.isEmpty(cmd.getKeyword())) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
                    .field("name");    
        }
        
//        FilterBuilder fb = null;
//
//        if(null == fb) {
//            fb = FilterBuilders.termFilter("communityType", t.getCode());
//        } else {
//            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("communityType", EnterpriseCommunityType.Normal.getCode()));
//        }
        
//        if(null != fb) {
//            qb = QueryBuilders.filteredQuery(qb, fb);
//        }
        
//        Integer namespaceId = (cmd.getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : cmd.getNamespaceId();
//        FilterBuilder fb = FilterBuilders.termFilter("namespaceId", namespaceId);
//        qb = QueryBuilders.filteredQuery(qb, fb);
        
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        
        builder.setFrom(pageNum * pageSize).setSize(pageSize+1);
        
        builder.setQuery(qb);
        
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

	
}
