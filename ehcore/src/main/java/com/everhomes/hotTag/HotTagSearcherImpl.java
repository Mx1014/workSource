package com.everhomes.hotTag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.action.WriteConsistencyLevel;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
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
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.organization.Organization;
import com.everhomes.rest.hotTag.SearchTagCommand;
import com.everhomes.rest.hotTag.SearchTagResponse;
import com.everhomes.rest.hotTag.TagDTO;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.SearchUtils;
import com.everhomes.search.HotTagSearcher;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.videoconf.ConfEnterpriseSearcherImpl;
import com.everhomes.videoconf.ConfEnterprises;

@Component
public class HotTagSearcherImpl extends AbstractElasticSearch implements HotTagSearcher{

	private static final Logger LOGGER = LoggerFactory.getLogger(HotTagSearcherImpl.class);
	
    @Autowired
    private ConfigurationProvider  configProvider;
	

	@Override
	public void feedDoc(HotTags tag) {
		XContentBuilder source = createDoc(tag);
        
		feedDoc(tag.getName()+"-"+tag.getServiceType(), source);
		
	}

	@Override
	public SearchTagResponse query(SearchTagCommand cmd) {

	    //热门标签查询实限制10个字符  add by yanjun 20170629
        if(cmd.getKeyword() != null && cmd.getKeyword().trim().length() > 10){
            cmd.setKeyword(cmd.getKeyword().trim().substring(0, 10));
        }

		SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        
        QueryBuilder qb;
        
        FilterBuilder fb = null;
        if(StringUtils.isEmpty(cmd.getKeyword())) {
        	qb = QueryBuilders.matchAllQuery();
        } else {
        	qb = QueryBuilders.multiMatchQuery(cmd.getKeyword(), "name");
        }
        
        if(!StringUtils.isEmpty(cmd.getServiceType())) {
        	fb = FilterBuilders.termFilter("serviceType", cmd.getServiceType());
        }
		
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
        
        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);
        
        SearchResponse rsp = builder.execute().actionGet();
        
        List<String> ids = getTags(rsp);
        
        SearchTagResponse response = new SearchTagResponse();
        if(ids.size() > pageSize) {
        	response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
        } else {
       	 response.setNextPageAnchor(null);
        }
        
        List<TagDTO> tags = ids.stream().map(r -> {
        	TagDTO tag = new TagDTO();
        	String[] t = r.split("-");
        	if(t.length > 0) {
        		tag.setName(t[0]);
        	}
        	return tag;
        }).collect(Collectors.toList());

        // 默认第一个值要返回搜索的关键字。如果有关键字一样的标签，根据elasticsearch匹配度，它会排第一。因此仅需和第一个返回值比较。 add by yanjun 20170613
        if(tags.size() == 0 || tags.get(0).getName()==null || !tags.get(0).getName().equals(cmd.getKeyword())){
            TagDTO tag = new TagDTO();
            tag.setName(cmd.getKeyword());
            tags.add(0, tag);
        }

        response.setTags(tags);
		return response;
	}
	
	private List<String> getTags(SearchResponse rsp) {
        List<String> results = new ArrayList<String>();
        SearchHit[] docs = rsp.getHits().getHits();
        for (SearchHit sd : docs) {
            try {
                results.add(sd.getId());
            }
            catch(Exception ex) {
                LOGGER.info("getTagIds error " + ex.getMessage());
            }
        }
        
        return results;
    }

	@Override
	public String getIndexType() {
		
		return SearchUtils.HOTTAGINDEXTYPE;
	}
	
	private XContentBuilder createDoc(HotTags tag){
		try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("name", tag.getName());
            b.field("serviceType", tag.getServiceType());
            b.field("hotFlag", tag.getHotFlag());
            
            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create tag " + tag.getName() + " error");
            return null;
        }
    }
}
