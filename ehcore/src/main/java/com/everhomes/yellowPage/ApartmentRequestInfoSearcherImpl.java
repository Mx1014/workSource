package com.everhomes.yellowPage;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.yellowPage.RequestInfoDTO;
import com.everhomes.rest.yellowPage.SearchRequestInfoCommand;
import com.everhomes.rest.yellowPage.SearchRequestInfoResponse;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.ApartmentRequestInfoSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;

@Component
public class ApartmentRequestInfoSearcherImpl  extends AbstractElasticSearch implements ApartmentRequestInfoSearcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentRequestInfoSearcherImpl.class);
	
	@Autowired
	private YellowPageProvider yellowPageProvider;
	@Autowired
	private OrganizationProvider organizationProvider;
	
	@Autowired
	private ConfigurationProvider configProvider;
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public void deleteById(Long id) {
		deleteById(id.toString());
	}

	@Override
	public void bulkUpdate(List<ServiceAllianceApartmentRequests> requests) {
		BulkRequestBuilder brb = getClient().prepareBulk();
        for (ServiceAllianceApartmentRequests request : requests) {
	            XContentBuilder source = createDoc(request);
	            if(null != source) {
	                LOGGER.info("service alliance apartment request id:" + request.getId());
	                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
	                        .id(request.getId().toString()).source(source)); 
	                }
            
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
	}

	@Override
	public void feedDoc(ServiceAllianceApartmentRequests request) {
		XContentBuilder source = createDoc(request);
        feedDoc(request.getId().toString(), source);
	}

	@Override
	public void syncFromDb() {
		int pageSize = 200;      
        this.deleteAll();
        
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<ServiceAllianceApartmentRequests> requests = yellowPageProvider.listApartmentRequests(locator, pageSize);
            
            if(requests.size() > 0) {
                this.bulkUpdate(requests);
            }
            
            if(locator.getAnchor() == null) {
                break;
            }
        }
        
        LOGGER.info("sync for service alliance apartment request ok");

	}

	@Override
	public SearchRequestInfoResponse searchRequestInfo(
			SearchRequestInfoCommand cmd) {

		SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        
        QueryBuilder qb = null;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
                    .field("creatorName", 1.2f)
                    .field("creatorOrganization", 1.0f);
            
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("creatorName").addHighlightedField("creatorOrganization");
            
        }
        
        FilterBuilder fb = FilterBuilders.termFilter("type", cmd.getCategoryId());
        
        RangeFilterBuilder rf = new RangeFilterBuilder("createDate");
        if(cmd.getStartDay() != null) {
        	rf.gte(cmd.getStartDay());
        	fb = FilterBuilders.andFilter(fb, rf); 
        }
        
        if(cmd.getEndDay() != null) {
        	rf.lte(cmd.getEndDay());
        	fb = FilterBuilders.andFilter(fb, rf); 
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
        
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            builder.addSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC).ignoreUnmapped(true));
        }
        
        if(LOGGER.isDebugEnabled())
			LOGGER.info("ApartmentRequestInfoSearcherImpl query builder ："+builder);
        
        SearchResponse rsp = builder.execute().actionGet();
        SearchRequestInfoResponse response = new SearchRequestInfoResponse();
        List<RequestInfoDTO> dtos = getDTOs(rsp);
        
        if(dtos.size() > pageSize){
        	response.setNextPageAnchor(anchor+1);
        	dtos.remove(dtos.size() - 1);
        }
        
        response.setDtos(dtos);
        
		return response;
	}

	@Override
	public String getIndexType() {
		return SearchUtils.APARTMENTREQUEST;
	}
	
	private XContentBuilder createDoc(ServiceAllianceApartmentRequests request){
		try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("type", request.getType());
            b.field("ownerType", request.getOwnerType());
            b.field("ownerId", request.getOwnerId());
            b.field("creatorName", request.getCreatorName());
            b.field("creatorMobile", request.getCreatorMobile());
            b.field("createTime", request.getCreateTime().getTime());
            String d = format.format(request.getCreateTime().getTime());  
            try {
				Date date=format.parse(d);
				b.field("createDate", date.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
            
			Organization org = organizationProvider.findOrganizationById(request.getCreatorOrganizationId());
          
			if(org != null) {
			    b.field("creatorOrganization", org.getName());
            } else {
                b.field("creatorOrganization", "");
            }
            
			ServiceAlliances sa = yellowPageProvider.findServiceAllianceById(request.getServiceAllianceId(), request.getOwnerType(), request.getOwnerId());
            if(sa != null) {
            	b.field("serviceOrganization", sa.getName());
            } else {
                b.field("serviceOrganization", "");
            }
			
            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create ServiceAllianceApartmentRequests " + request.getId() + " error");
            return null;
        }
    }
	
	private List<RequestInfoDTO> getDTOs(SearchResponse rsp) {
        List<RequestInfoDTO> dtos = new ArrayList<RequestInfoDTO>();
        SearchHit[] docs = rsp.getHits().getHits();
        for (SearchHit sd : docs) {
            try {
            	RequestInfoDTO dto = new RequestInfoDTO();
            	dto.setId(Long.parseLong(sd.getId()));
            	Map<String, Object> source = sd.getSource();
            	
            	dto.setCreatorName(String.valueOf(source.get("creatorName")));
            	dto.setCreatorMobile(String.valueOf(source.get("creatorMobile")));
            	dto.setCreatorOrganization(String.valueOf(source.get("creatorOrganization")));
            	dto.setServiceOrganization(String.valueOf(source.get("serviceOrganization")));
            	Long time = SearchUtils.getLongField(source.get("createDate"));  
                String day = format.format(time);
            	dto.setCreateTime(day);
            	
            	dtos.add(dto);
            }
            catch(Exception ex) {
                LOGGER.info("getRequestInfoDTOs error " + ex.getMessage());
            }
        }
        
        return dtos;
    }

}
