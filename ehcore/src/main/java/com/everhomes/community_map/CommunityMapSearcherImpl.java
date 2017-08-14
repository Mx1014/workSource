package com.everhomes.community_map;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationAddress;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.search.GroupQueryResult;
import com.everhomes.rest.search.OrganizationQueryResult;
import com.everhomes.rest.videoconf.ConfServiceErrorCode;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.GroupQueryFilter;
import com.everhomes.search.OrganizationSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CommunityMapSearcherImpl extends AbstractElasticSearch{
    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityMapSearcherImpl.class);
    
    @Autowired
    private ConfigurationProvider  configProvider;

    @Override
    public String getIndexType() {
        return SearchUtils.ENTERPRISEINDEXTYPE;
    }

    public GroupQueryResult query(SearchOrganizationCommand cmd) {
        GroupQueryFilter filter = new GroupQueryFilter();

        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());

        int pageNum = 0;
        if(cmd.getPageAnchor() != null) {
            pageNum = cmd.getPageAnchor().intValue();
        }
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

       SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());

        QueryBuilder boolQuery = null;

        if (cmd.getKeyword().length() > 10) {
            cmd.setKeyword(cmd.getKeyword().substring(cmd.getKeyword().length() - 10));
        }

        QueryBuilder qb = QueryBuilders.queryString("*"+cmd.getKeyword()+"*").field("name");
        boolQuery = QueryBuilders.boolQuery().must(qb);

        List<FilterBuilder> fbList = new ArrayList<>();
        FilterBuilder fb = FilterBuilders.termFilter("namespaceId", namespaceId);
        fbList.add(fb);
        
        // 每个企业（含物业管理公司）都有可能在某个园区内，当客户端提供园区作为过滤条件时，则在园区范围内挑选园区 by lqs 20160512
        if(cmd.getCommunityId() != null) {
            FilterBuilder cmntyFilter = FilterBuilders.termFilter("communityId", cmd.getCommunityId());
//            fb = FilterBuilders.andFilter(fb, cmntyFilter);
            fbList.add(cmntyFilter);
        }
        
        // 用于一些场景下只能搜索出普通公司 by sfyan 20160523
        //empty判断 by xiongying 20160613
        if(!StringUtils.isEmpty(cmd.getOrganizationType())) {
        	//转小写查 by xiongying 20160524
            FilterBuilder orgTypeFilter = FilterBuilders.termFilter("organizationType", cmd.getOrganizationType().toLowerCase());
//            fb = FilterBuilders.andFilter(fb, orgTypeFilter);
            fbList.add(orgTypeFilter);
        }
        
        if (cmd.getSetAdminFlag() != null) {
        	FilterBuilder adminFlagFilter = FilterBuilders.termFilter("setAdminFlag", cmd.getSetAdminFlag());
//            fb = FilterBuilders.andFilter(fb, adminFlagFilter);
        	fbList.add(adminFlagFilter);
		}
        FilterBuilder addressFilter = FilterBuilders.existsFilter("addresses");
        fbList.add(addressFilter);

        fb = FilterBuilders.andFilter(fbList.toArray(new FilterBuilder[fbList.size()]));

        boolQuery = QueryBuilders.filteredQuery(boolQuery, fb);
       
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        
        builder.setFrom(pageNum * pageSize).setSize(pageSize + 1);
        
        builder.setQuery(boolQuery);
        
        
        builder.addSort("id", SortOrder.DESC);
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Query organization, cmd={}, builder={}", cmd, builder);
        }
        
        SearchResponse rsp = builder.execute().actionGet();
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("result from elasticsearch {}", rsp);
        }
        
        List<Long> ids = getIds(rsp);
        GroupQueryResult result = new GroupQueryResult();

        if(ids.size() > pageSize){
            result.setPageAnchor(Long.valueOf(pageNum + 1));
            ids.remove(ids.size() - 1);
        }
        result.setIds(ids);
        
        return result;
    }

}
