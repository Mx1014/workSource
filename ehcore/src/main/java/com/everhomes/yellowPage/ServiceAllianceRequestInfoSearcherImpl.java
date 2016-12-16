package com.everhomes.yellowPage;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.everhomes.rest.yellowPage.SearchOneselfRequestInfoCommand;
import com.everhomes.user.CustomRequestConstants;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
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
import com.everhomes.rest.wifi.WifiOwnerType;
import com.everhomes.rest.yellowPage.RequestInfoDTO;
import com.everhomes.rest.yellowPage.SearchRequestInfoCommand;
import com.everhomes.rest.yellowPage.SearchRequestInfoResponse;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.SearchUtils;
import com.everhomes.search.ServiceAllianceRequestInfoSearcher;
import com.everhomes.settings.PaginationConfigHelper;

@Component
public class ServiceAllianceRequestInfoSearcherImpl extends AbstractElasticSearch
	implements ServiceAllianceRequestInfoSearcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAllianceRequestInfoSearcherImpl.class);
	
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
	public void syncFromDb() {
		int pageSize = 200;      
        this.deleteAll();

        syncFromServiceAllianceRequestsDb(pageSize);
        syncFromReservationRequestsDb(pageSize);
        syncFromSettleRequestInfoSearcherDb(pageSize);
        syncFromServiceAllianceApartmentRequestsDb(pageSize);
		
	}

    private void syncFromServiceAllianceApartmentRequestsDb(int pageSize) {

        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<ServiceAllianceApartmentRequests> requests = yellowPageProvider.listApartmentRequests(locator, pageSize);

            if(requests.size() > 0) {
                this.bulkUpdateServiceAllianceApartmentRequests(requests);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        LOGGER.info("sync for service alliance apartment request ok");

    }

    private void syncFromServiceAllianceRequestsDb( int pageSize) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<ServiceAllianceRequests> requests = yellowPageProvider.listServiceAllianceRequests(locator, pageSize);

            if(requests.size() > 0) {
                this.bulkUpdateServiceAllianceRequests(requests);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        LOGGER.info("sync for service alliance request ok");
    }

    private void syncFromReservationRequestsDb( int pageSize) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<ReservationRequests> requests = yellowPageProvider.listReservationRequests(locator, pageSize);

            if(requests.size() > 0) {
                this.bulkUpdateReservationRequests(requests);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        LOGGER.info("sync for reserve request ok");
    }

    private void syncFromSettleRequestInfoSearcherDb( int pageSize) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<SettleRequests> requests = yellowPageProvider.listSettleRequests(locator, pageSize);

            if(requests.size() > 0) {
                this.bulkUpdateSettleRequests(requests);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        LOGGER.info("sync for settle request ok");
    }

	@Override
	public String getIndexType() {
		return SearchUtils.SAREQUEST;
	}

	@Override
	public void bulkUpdateServiceAllianceRequests(List<ServiceAllianceRequests> requests) {
		BulkRequestBuilder brb = getClient().prepareBulk();
        for (ServiceAllianceRequests request : requests) {
            ServiceAllianceRequestInfo requestInfo = ConvertHelper.convert(request, ServiceAllianceRequestInfo.class);
            requestInfo.setTemplateType(CustomRequestConstants.SERVICE_ALLIANCE_REQUEST_CUSTOM);
            XContentBuilder source = createDoc(requestInfo);
            if(null != source) {
                LOGGER.info("service alliance request id:" + request.getId()+"-EhServiceAllianceRequests");
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(request.getId()+"-EhServiceAllianceRequests").source(source));
            }
            
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
		
	}

    @Override
    public void bulkUpdateServiceAllianceApartmentRequests(List<ServiceAllianceApartmentRequests> requests) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (ServiceAllianceApartmentRequests request : requests) {
            ServiceAllianceRequestInfo requestInfo = ConvertHelper.convert(request, ServiceAllianceRequestInfo.class);
            requestInfo.setTemplateType(CustomRequestConstants.APARTMENT_REQUEST_CUSTOM);
            XContentBuilder source = createDoc(requestInfo);
            if(null != source) {
                LOGGER.info("service alliance apartment request id:" + request.getId() + "-EhServiceAllianceApartmentRequests");
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(request.getId().toString() + "-EhServiceAllianceApartmentRequests").source(source));
            }

        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

    @Override
    public void bulkUpdateSettleRequests(List<SettleRequests> requests) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (SettleRequests request : requests) {
            ServiceAllianceRequestInfo requestInfo = ConvertHelper.convert(request, ServiceAllianceRequestInfo.class);
            requestInfo.setTemplateType(CustomRequestConstants.SETTLE_REQUEST_CUSTOM);
            XContentBuilder source = createDoc(requestInfo);
            if(null != source) {
                LOGGER.info("settle request id:" + request.getId() + "-EhSettleRequests");
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(request.getId().toString() + "-EhSettleRequests").source(source));
            }

        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }

    }

    @Override
    public void bulkUpdateReservationRequests(List<ReservationRequests> requests) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (ReservationRequests request : requests) {
            ServiceAllianceRequestInfo requestInfo = ConvertHelper.convert(request, ServiceAllianceRequestInfo.class);
            requestInfo.setTemplateType(CustomRequestConstants.RESERVE_REQUEST_CUSTOM);
            XContentBuilder source = createDoc(requestInfo);
            if(null != source) {
                LOGGER.info("reserve request id:" + request.getId() + "-" + request.getTemplateType());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(request.getId().toString() + "-" + request.getTemplateType()).source(source));
            }

        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

	@Override
	public void feedDoc(ServiceAllianceRequestInfo request) {
		XContentBuilder source = createDoc(request);
        feedDoc(request.getId().toString() + "-" + request.getTemplateType(), source);
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
        
        FilterBuilder fb = FilterBuilders.termFilter("ownerType", WifiOwnerType.fromCode(cmd.getOwnerType()).getCode());
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerId", cmd.getOwnerId()));
        if(cmd.getCategoryId() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("type", cmd.getCategoryId()));
        
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
			LOGGER.info("ServiceAllianceRequestInfoSearcherImpl query builder ："+builder);
        
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
    public SearchRequestInfoResponse searchOneselfRequestInfo(SearchOneselfRequestInfoCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());

        QueryBuilder qb = null;

        FilterBuilder fb = FilterBuilders.termFilter("type", cmd.getType());
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("creatorUid", UserContext.current().getUser().getId()));

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }

        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);

        if(LOGGER.isDebugEnabled())
            LOGGER.info("ServiceAllianceRequestInfoSearcherImpl query builder ："+builder);

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

    private XContentBuilder createDoc(ServiceAllianceRequestInfo request){
		try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("templateType", request.getTemplateType());
            b.field("type", request.getType());
            b.field("ownerType", request.getOwnerType());
            b.field("ownerId", request.getOwnerId());
            b.field("creatorName", request.getCreatorName());
            b.field("creatorMobile", request.getCreatorMobile());
            b.field("createTime", request.getCreateTime().getTime());
            b.field("creatorUid", request.getCreatorUid());
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
            LOGGER.error("Create ServiceAllianceRequestInfo " + request.getId() + "-" + request.getTemplateType() + " error");
            return null;
        }
    }
	
	private List<RequestInfoDTO> getDTOs(SearchResponse rsp) {
        List<RequestInfoDTO> dtos = new ArrayList<RequestInfoDTO>();
        SearchHit[] docs = rsp.getHits().getHits();
        for (SearchHit sd : docs) {
            try {
            	RequestInfoDTO dto = new RequestInfoDTO();
            	String[] ids = sd.getId().split("-");
            	dto.setId(Long.parseLong(ids[0]));
            	Map<String, Object> source = sd.getSource();
                dto.setTemplateType(String.valueOf(source.get("templateType")));
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
