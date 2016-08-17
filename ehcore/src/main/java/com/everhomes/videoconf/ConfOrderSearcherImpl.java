package com.everhomes.videoconf;

import java.io.IOException;
import java.util.ArrayList;
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
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.enterprise.Enterprise;
import com.everhomes.enterprise.EnterpriseProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.videoconf.ConfCapacity;
import com.everhomes.rest.videoconf.ConfOrderDTO;
import com.everhomes.rest.videoconf.ConfType;
import com.everhomes.rest.videoconf.ListVideoConfAccountOrderCommand;
import com.everhomes.rest.videoconf.ListVideoConfAccountOrderResponse;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.ConfOrderSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;

@Component
public class ConfOrderSearcherImpl extends AbstractElasticSearch implements
		ConfOrderSearcher {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfOrderSearcherImpl.class);
	
	@Autowired
    private ConfigurationProvider configProvider;
	
	@Autowired
	private VideoConfProvider vcProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;

	@Override
	public void deleteById(Long id) {
		deleteById(id.toString());
	}

	@Override
	public void bulkUpdate(List<ConfOrders> orders) {
		BulkRequestBuilder brb = getClient().prepareBulk();
        for (ConfOrders order : orders) {
        	
            XContentBuilder source = createDoc(order);
            if(null != source) {
                LOGGER.info("conf order id:" + order.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(order.getId().toString()).source(source));    
                }
            
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }


	}

	@Override
	public void feedDoc(ConfOrders order) {

		XContentBuilder source = createDoc(order);
        
        feedDoc(order.getId().toString(), source);

	}

	@Override
	public void syncFromDb() {
		int pageSize = 200;      
        this.deleteAll();
        
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<ConfOrders> orders = vcProvider.findOrdersByEnterpriseId(null, locator, pageSize);
            
            if(orders.size() > 0) {
                this.bulkUpdate(orders);
            }
            
            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();
        
        LOGGER.info("sync for conference order ok");


	}

	@Override
	public ListVideoConfAccountOrderResponse query(
			ListVideoConfAccountOrderCommand cmd) {
		SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
		QueryBuilder qb = null;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
            		.field("enterpriseName", 5.0f)
                    .field("contactor", 2.0f)
                    .field("mobile", 1.0f);
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("enterpriseName").addHighlightedField("contactor").addHighlightedField("mobile");

        }

        FilterBuilder fb = null;
        if(cmd.getEnterpriseId() != null)
        	fb = FilterBuilders.termFilter("enterpriseId", cmd.getEnterpriseId());
        
        if(cmd.getConfCapacity() != null) {
        	if(null != fb)
        		fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("confCapacity", cmd.getConfCapacity())); 
        	else {
        		fb = FilterBuilders.termFilter("confCapacity", cmd.getConfCapacity());
        	}
        }
        
        if(cmd.getConfType() != null) {
        	if(null != fb)
        		fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("confType", cmd.getConfType())); 
        	else {
        		fb = FilterBuilders.termFilter("confType", cmd.getConfType());
        	}
        }
        
        if(cmd.getStartTime() != null) {
        	RangeFilterBuilder rf = new RangeFilterBuilder("createTime");
        	rf.gt(cmd.getStartTime());
        	fb = FilterBuilders.andFilter(fb, rf); 
        }
        
        if(cmd.getEndTime() != null) {
        	RangeFilterBuilder rf = new RangeFilterBuilder("createTime");
        	rf.lt(cmd.getEndTime());
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
        
        SearchResponse rsp = builder.execute().actionGet();

        List<Long> ids = getIds(rsp);
        
        ListVideoConfAccountOrderResponse response = new ListVideoConfAccountOrderResponse();
        if(ids.size() > pageSize) {
        	response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
         } else {
        	 response.setNextPageAnchor(null);
            }
        
        List<ConfOrderDTO> confOrders = new ArrayList<ConfOrderDTO>();
        for(Long id : ids) {
        	ConfOrders order = vcProvider.findOredrById(id);
        	ConfOrderDTO dto = toConfOrderDTO(order);

        	confOrders.add(dto);
        }
        response.setConfOrders(confOrders);
        
        return response;
	}

	@Override
	public String getIndexType() {
		return SearchUtils.CONFORDERINDEXTYPE;
	}
	
	private XContentBuilder createDoc(ConfOrders order){
		try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("enterpriseId", order.getOwnerId());
            b.field("createTime", order.getCreateTime());
            
            ConfAccountCategories category = vcProvider.findAccountCategoriesById(order.getAccountCategoryId());
            if(null != category) {
            	//0: 25方仅视频, 1: 25方支持电话, 2: 100方仅视频, 3: 100方支持电话, 4: 6方仅视频, 5: 50方仅视频, 6: 50方支持电话
            	if(0 == category.getConfType()) {
            		b.field("confCapacity", ConfCapacity.CONF_CAPACITY_25.getStatus());
                    b.field("confType", ConfType.CONF_TYPE_VIDEO_ONLY.getStatus());
            	}
            	
            	if(1 == category.getConfType()) {
            		b.field("confCapacity", ConfCapacity.CONF_CAPACITY_25.getStatus());
                    b.field("confType", ConfType.CONF_TYPE_PHONE_SUPPORT.getStatus());
            	}
            	
            	if(2 == category.getConfType()) {
            		b.field("confCapacity", ConfCapacity.CONF_CAPACITY_100.getStatus());
                    b.field("confType", ConfType.CONF_TYPE_VIDEO_ONLY.getStatus());
            	}
            	
            	if(3 == category.getConfType()) {
            		b.field("confCapacity", ConfCapacity.CONF_CAPACITY_100.getStatus());
                    b.field("confType", ConfType.CONF_TYPE_PHONE_SUPPORT.getStatus());
            	}
            	
            	if(4 == category.getConfType()) {
            		b.field("confCapacity", ConfCapacity.CONF_CAPACITY_6.getStatus());
                    b.field("confType", ConfType.CONF_TYPE_VIDEO_ONLY.getStatus());
            	}
            	
            	if(5 == category.getConfType()) {
            		b.field("confCapacity", ConfCapacity.CONF_CAPACITY_50.getStatus());
                    b.field("confType", ConfType.CONF_TYPE_VIDEO_ONLY.getStatus());
            	}
            	
            	if(6 == category.getConfType()) {
            		b.field("confCapacity", ConfCapacity.CONF_CAPACITY_50.getStatus());
                    b.field("confType", ConfType.CONF_TYPE_PHONE_SUPPORT.getStatus());
            	}
            	
            } else {
            	b.field("confCapacity", "");
                b.field("confType", "");
            }

            Organization org = organizationProvider.findOrganizationById(order.getOwnerId());
            if(null != org) {
                b.field("enterpriseName", org.getName());
            } else {
                b.field("enterpriseName", "");
            }
            
            ConfEnterprises enterpriseContact = vcProvider.findByEnterpriseId(order.getOwnerId());
    		if(null != enterpriseContact) {
    			b.field("contactor", enterpriseContact.getContactName());
    			b.field("mobile", enterpriseContact.getContact());
    		} else {
                b.field("contactor", "");
                b.field("mobile", "");
            }
    		
    		
            
            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create order " + order.getId() + " error");
            return null;
        }
    }
	
	private ConfOrderDTO toConfOrderDTO(ConfOrders order) {
		
		ConfOrderDTO dto = new ConfOrderDTO();
		dto.setId(order.getId());
		dto.setEnterpriseId(order.getOwnerId());
		Organization org = organizationProvider.findOrganizationById(order.getOwnerId());
		if(org != null)
			dto.setEnterpriseName(org.getName());

		dto.setContactor(order.getBuyerName());
		dto.setMobile(order.getBuyerContact());
		
		if(null == dto.getContactor()) {
			ConfEnterprises enterpriseContact = vcProvider.findByEnterpriseId(order.getOwnerId());
			if(enterpriseContact != null) {
				dto.setContactor(enterpriseContact.getContactName());
				dto.setMobile(enterpriseContact.getContact());
			}
		}
		
		ConfAccountCategories category = vcProvider.findAccountCategoriesById(order.getAccountCategoryId());
		if(null != category) {
        	//0: 25方仅视频, 1: 25方支持电话, 2: 100方仅视频, 3: 100方支持电话, 4: 6方仅视频, 5: 50方仅视频, 6: 50方支持电话
        	if(0 == category.getConfType()) {
        		dto.setConfCapacity(ConfCapacity.CONF_CAPACITY_25.getCode());
        		dto.setConfType(ConfType.CONF_TYPE_VIDEO_ONLY.getCode());
        	}
        	
        	if(1 == category.getConfType()) {
        		dto.setConfCapacity(ConfCapacity.CONF_CAPACITY_25.getCode());
        		dto.setConfType(ConfType.CONF_TYPE_PHONE_SUPPORT.getCode());
        	}
        	
        	if(2 == category.getConfType()) {
        		dto.setConfCapacity(ConfCapacity.CONF_CAPACITY_100.getCode());
        		dto.setConfType(ConfType.CONF_TYPE_VIDEO_ONLY.getCode());
        	}
        	
        	if(3 == category.getConfType()) {
        		dto.setConfCapacity(ConfCapacity.CONF_CAPACITY_100.getCode());
        		dto.setConfType(ConfType.CONF_TYPE_PHONE_SUPPORT.getCode());
        	}
        	
        	if(4 == category.getConfType()) {
        		dto.setConfCapacity(ConfCapacity.CONF_CAPACITY_6.getCode());
        		dto.setConfType(ConfType.CONF_TYPE_VIDEO_ONLY.getCode());
        	}
        	
        	if(5 == category.getConfType()) {
        		dto.setConfCapacity(ConfCapacity.CONF_CAPACITY_50.getCode());
        		dto.setConfType(ConfType.CONF_TYPE_VIDEO_ONLY.getCode());
        	}
        	
        	if(6 == category.getConfType()) {
        		dto.setConfCapacity(ConfCapacity.CONF_CAPACITY_50.getCode());
        		dto.setConfType(ConfType.CONF_TYPE_PHONE_SUPPORT.getCode());
        	}
        	
        }
//		if(category != null)
//			dto.setAccountChannelType(category.getChannelType());
		dto.setCreateTime(order.getCreateTime());
		dto.setQuantity(order.getQuantity());
		dto.setPeriod(order.getPeriod());
		dto.setAmount(order.getAmount());
		int assignedAccount = vcProvider.countOrderAccounts(order.getId(), (byte) 1);
		dto.setAssignedQuantity(assignedAccount);
		dto.setAccountCategoryId(order.getAccountCategoryId());
		dto.setInvoiceFlag(order.getInvoiceReqFlag());
		dto.setMakeOutFlag(order.getInvoiceIssueFlag());
		dto.setBuyChannel(order.getOnlineFlag());
		return dto;
	}

}
