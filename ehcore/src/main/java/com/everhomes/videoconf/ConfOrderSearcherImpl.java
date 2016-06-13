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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.enterprise.Enterprise;
import com.everhomes.enterprise.EnterpriseProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.videoconf.ConfOrderDTO;
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
	private EnterpriseProvider enterpriseProvider;

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
                    .field("enterpriseName.pinyin_prefix", 2.0f)
                    .field("enterpriseName.pinyin_gram", 1.0f);
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("enterpriseName");

        }

        FilterBuilder fb = null;
        if(cmd.getEnterpriseId() != null)
        	fb = FilterBuilders.termFilter("enterpriseId", cmd.getEnterpriseId());
        
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

            Enterprise enterprise = enterpriseProvider.findEnterpriseById(order.getOwnerId());
            if(null != enterprise) {
                b.field("enterpriseName", enterprise.getName());
            } else {
                b.field("enterpriseName", "");
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
		Enterprise enterprise = enterpriseProvider.findEnterpriseById(order.getOwnerId());
		if(enterprise != null)
			dto.setEnterpriseName(enterprise.getName());

		ConfEnterprises enterpriseContact = vcProvider.findByEnterpriseId(order.getOwnerId());
		if(enterpriseContact != null) {
			dto.setContactor(enterpriseContact.getContactName());
			dto.setMobile(enterpriseContact.getContact());
		}
		
//		ConfAccountCategories category = vcProvider.findAccountCategoriesById(order.getAccountCategoryId());
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
