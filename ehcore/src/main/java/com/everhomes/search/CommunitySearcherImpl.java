package com.everhomes.search;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.rest.community.CommunityDoc;
import com.everhomes.rest.enterprise.EnterpriseCommunityType;
import com.everhomes.user.UserContext;
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
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CommunitySearcherImpl extends AbstractElasticSearch implements CommunitySearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommunitySearcherImpl.class);
            
    @Autowired
    private CommunityProvider communityProvider;
    
    @Override
    public String getIndexType() {
        return SearchUtils.COMMUNITYINDEXTYPE;
    }
    
    private XContentBuilder createDoc(Community community){
        
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("name", community.getName());
            b.field("cityId", community.getCityId());
            b.field("cityName", community.getCityName());
            b.field("regionId", (community.getAreaId() == null ? community.getCityId() : community.getAreaId()));
            b.field("communityType", community.getCommunityType());
            b.field("namespaceId", community.getNamespaceId());
            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create group " + community.getId() + " error");
            return null;
        }
    }
    
    @Override
    public void syncDb() {
        Long pageSize = 200l;
        Long i = 1l;
        Long count = 0l;
        this.deleteAll();
        
        for(;;) {
            List<Community> communities = this.communityProvider.listAllCommunities(i,pageSize);
            if(null == communities || communities.size() == 0) {
                break;
                }
            
            bulkUpdate(communities);
            i++;
            count += communities.size();
            LOGGER.info("communities sync count= " + count);
        }
        
        this.optimize(1);
        this.refresh();
    }
   
    @Override
    public void bulkUpdate(List<Community> communities) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (Community community : communities) {
            XContentBuilder source = createDoc(community);
            brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                    .id(community.getId().toString()).source(source));
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }
    
    @Override
    public void feedDoc(Community community) {
        XContentBuilder source = createDoc(community);
        feedDoc(community.getId().toString(), source);
    }

    @Override
    public void deleteById(Long id) {
        this.deleteById(id.toString());
    }
    
    private CommunityDoc readDoc(Map<String, Object> source, String idAsStr) {
        try {
            CommunityDoc doc = new CommunityDoc();
            doc.setId(Long.parseLong(idAsStr));
            doc.setCityId(SearchUtils.getLongField(source.get("cityId")));
            doc.setName((String)source.get("name"));
            doc.setCityName((String)source.get("cityName"));
            doc.setRegionId(SearchUtils.getLongField(source.get("regionId")));
            doc.setNamespaceId(SearchUtils.getLongField(source.get("namespaceId")).intValue());
            doc.setCommunityType(SearchUtils.getLongField(source.get("communityType")).byteValue());
            
            return doc;
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            LOGGER.error(source.toString());
        }

        return null;
    }

    /**
     * 搜索小区
     * @param queryString
     * @param communityType
     * @param t
     * @param cityId
     * @param regionId
     * @param pageNum
     * @param pageSize
     * @return
     */
    private List<CommunityDoc> searchDocsByType(String queryString,Byte communityType, EnterpriseCommunityType t, Long cityId, Long regionId, int pageNum, int pageSize) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        
        QueryBuilder qb;
        
        //http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/query-dsl-dis-max-query.html
        //http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/query-dsl-wildcard-query.html
        //modify by yuanlei
        //既可以根据关键字来进行搜索，也可以不根据关键字来进行搜索
        if (queryString != null && queryString.length() > 0) {
            qb = QueryBuilders.multiMatchQuery(queryString)
                    .field("name", 5.0f)
                    .field("name.pinyin_prefix", 2.0f)
                    .field("name.pinyin_gram", 1.0f);
        } else {
            qb = QueryBuilders.matchAllQuery();
        }

        int namespaceId = UserContext.getCurrentNamespaceId();
        FilterBuilder fb = FilterBuilders.termFilter("namespaceId", namespaceId);

        //修改查询条件关系 add by sfyan 20170803
        List<FilterBuilder> filterBuilders = new ArrayList<>();
        if(null != communityType) {
            filterBuilders.add(FilterBuilders.termFilter("communityType", communityType));

        }
        if((null != cityId) && (cityId > 0)) {
            filterBuilders.add(FilterBuilders.termFilter("cityId", cityId));
        }

        if((null != regionId) && (regionId > 0)) {
            filterBuilders.add(FilterBuilders.termFilter("regionId", regionId));
        }
        if(filterBuilders.size() != 0){
            FilterBuilder filterBuilder = filterBuilders.get(0);
            if(filterBuilders.size() > 1){
                FilterBuilder[] fbs = new FilterBuilder[filterBuilders.size()];
                filterBuilders.toArray(fbs);
                filterBuilder = FilterBuilders.andFilter(fbs);
            }
            fb = FilterBuilders.andFilter(fb, filterBuilder);
        }

        //园区和小区都要搜索出来 by xiongying 20160518
//        if(null == fb) {
//            fb = FilterBuilders.termFilter("communityType", t.getCode());
//        } else {
//            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("communityType", EnterpriseCommunityType.Normal.getCode()));
//        }
        
        if(null != fb) {
            qb = QueryBuilders.filteredQuery(qb, fb);
        }
        
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        
        builder.setFrom(pageNum * pageSize).setSize(pageSize);
        
        builder.setQuery(qb);
        
        SearchResponse rsp = builder.execute().actionGet();
        
        List<CommunityDoc> comIds = new ArrayList<CommunityDoc>();
        SearchHit[] docs = rsp.getHits().getHits();
        for (SearchHit sd : docs) {
            CommunityDoc d = readDoc(sd.getSource(), sd.getId());
            if(null != d) {
                comIds.add(d);
            }
        }
        
        return comIds;        
    }

    @Override
    public List<CommunityDoc> searchDocs(String queryString,Byte communityType, Long cityId, Long regionId, int pageNum, int pageSize) {
        return this.searchDocsByType(queryString,communityType, EnterpriseCommunityType.Normal, cityId, regionId, pageNum, pageSize);
    }
    
    @Override
    public List<CommunityDoc> searchEnterprise(String queryString,Byte communityType, Long regionId, int pageNum, int pageSize) {
        return this.searchDocsByType(queryString,communityType, EnterpriseCommunityType.Enterprise, null, regionId, pageNum, pageSize);
    }

}
