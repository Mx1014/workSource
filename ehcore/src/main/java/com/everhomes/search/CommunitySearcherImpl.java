package com.everhomes.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import com.everhomes.address.ListAllCommunitesCommand;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityDoc;
import com.everhomes.community.CommunityProvider;

@Service
public class CommunitySearcherImpl extends AbstractElasticSearch implements CommunitySearcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommunitySearcherImpl.class);
            
    @Autowired
    private CommunityProvider communityProvider;
    
    @Override
    public String getIndexName() {
        return SearchUtils.COMMUNITYINDEXNAME;
    }

    @Override
    public String getIndexType() {
        return SearchUtils.COMMUNITYINDEXTYPE;
    }
    
    private XContentBuilder createDoc(Community community){
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("name", community.getName());
            b.field("cityId", community.getCityId());
            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create group " + community.getId() + " error");
            return null;
        }
    }
    
    @Override
    public void syncDb() {
        ListAllCommunitesCommand cmd = new ListAllCommunitesCommand();
        Long pageSize = 200l;
        Long i = 1l;
        Long count = 0l;
        
        for(;;) {
            cmd.setPageOffset(i);
            cmd.setPageSize(pageSize);
            List<Community> communities = this.communityProvider.listAllCommunities(cmd);
            bulkUpdate(communities);
            count += communities.size();
            LOGGER.info("communities sync count= " + count);
            if(null == communities || communities.size() == 0) {
                break;
            }
            
            //Just test 400 items
            if(count > 400) {
                break;
            }
            
        }
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
    public List<String> search(String keyword) {
        return search(keyword, 0l, 80);
    }

    @Override
    public List<String> search(String keyword, Long cityId) {
        return search(keyword, cityId, 80);
    }

    @Override
    public List<String> search(String keyword, Long cityId, int pageSize) {
        List<CommunityDoc> comIds = searchDocs(keyword, cityId, 0, pageSize);
        List<String> results = new ArrayList<String>();
        
        for(CommunityDoc c : comIds) {
            try {
                results.add(c.getName());
            }
            catch(Exception e) {
                
            }
        }
        
        return results;
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
    
            
            return doc;
        }
        catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            LOGGER.error(source.toString());
        }

        return null;
    }

    @Override
    public List<CommunityDoc> searchDocs(String queryString, Long cityId, int pageNum, int pageSize) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName());
        
        QueryBuilder qb;
        
        //http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/query-dsl-dis-max-query.html
        //http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/query-dsl-wildcard-query.html
        qb = QueryBuilders.multiMatchQuery(queryString)
                .field("name", 5.0f)
                .field("name.pinyin_prefix", 2.0f)
                .field("name.pinyin_gram", 1.0f).analyzer("simple");
        
        FilterBuilder fb = null;
        if((null != cityId) && (cityId > 0)) {
            fb = FilterBuilders.termFilter("cityId", cityId);
        }
        
        if(null != fb) {
            qb = QueryBuilders.filteredQuery(qb, fb);
        }
        
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        
        builder.setFrom(pageNum).setSize(pageSize);
        
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

}
