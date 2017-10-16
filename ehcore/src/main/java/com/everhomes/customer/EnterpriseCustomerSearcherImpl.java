package com.everhomes.customer;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.OrganizationMemberDetails;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import com.everhomes.rest.customer.SearchEnterpriseCustomerCommand;
import com.everhomes.rest.customer.SearchEnterpriseCustomerResponse;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.EnterpriseCustomerSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.varField.FieldProvider;
import com.everhomes.varField.FieldService;
import com.everhomes.varField.ScopeFieldItem;

import org.apache.commons.lang.StringUtils;
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
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/8/17.
 */
@Component
public class EnterpriseCustomerSearcherImpl extends AbstractElasticSearch implements EnterpriseCustomerSearcher {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private EnterpriseCustomerProvider enterpriseCustomerProvider;

    @Autowired
    private FieldProvider fieldProvider;
    
	@Autowired
	private OrganizationProvider organizationProvider;

    @Autowired
    private FieldService fieldService;

    @Override
    public String getIndexType() {
        return SearchUtils.ENTERPRISE_CUSTOMER;
    }

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<EnterpriseCustomer> customers) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (EnterpriseCustomer customer : customers) {
            XContentBuilder source = createDoc(customer);
            if(null != source) {
                LOGGER.info("id:" + customer.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType()).id(customer.getId().toString()).source(source));
            }
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

    @Override
    public void feedDoc(EnterpriseCustomer customer) {
        XContentBuilder source = createDoc(customer);
        feedDoc(customer.getId().toString(), source);
    }

    private XContentBuilder createDoc(EnterpriseCustomer customer) {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();

            builder.field("id", customer.getId());
            builder.field("communityId", customer.getCommunityId());
            builder.field("namespaceId", customer.getNamespaceId());
            builder.field("name", customer.getName());
            builder.field("contactMobile", customer.getContactMobile());
            builder.field("contactName", customer.getContactName());
            builder.field("contactAddress", customer.getContactAddress());
            builder.field("categoryItemId", customer.getCategoryItemId());
            builder.field("levelItemId", customer.getLevelItemId());
            builder.field("status", customer.getStatus());
            builder.field("trackingUid",customer.getTrackingUid());
            builder.field("trackingName",customer.getTrackingName() == null ? "" : customer.getTrackingName());
            builder.field("lastTrackingTime" , customer.getLastTrackingTime());
            builder.field("propertyType" , customer.getPropertyType());
            builder.field("propertyUnitPrice" , customer.getPropertyUnitPrice());
            builder.field("propertyArea" , customer.getPropertyArea());
           
            builder.endObject();
            return builder;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void syncFromDb() {
        int pageSize = 200;
        this.deleteAll();

        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomers(locator, pageSize);

            if(customers.size() > 0) {
                this.bulkUpdate(customers);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();
        LOGGER.info("sync for customers ok");
    }

    @Override
    public SearchEnterpriseCustomerResponse queryEnterpriseCustomers(SearchEnterpriseCustomerCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        QueryBuilder qb = null;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
                    .field("name", 1.5f)
                    .field("contactName", 1.2f)
                    .field("contactAddress", 1.2f)
                    .field("contactMobile", 1.0f)
                    .field("trackingName" , 1.0f);

            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("name").addHighlightedField("contactName").addHighlightedField("contactAddress").addHighlightedField("contactMobile")
            		.addHighlightedField("trackingName");
        }

        FilterBuilder fb = null;
        FilterBuilder nfb = FilterBuilders.termFilter("status", CommonStatus.INACTIVE.getCode());
        fb = FilterBuilders.notFilter(nfb);
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("namespaceId", UserContext.getCurrentNamespaceId()));
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("communityId", cmd.getCommunityId()));

        if(cmd.getCustomerCategoryId() != null)
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("categoryItemId", cmd.getCustomerCategoryId()));

        if(cmd.getLevelId() != null)
            fb = FilterBuilders.andFilter(fb, FilterBuilders.inFilter("levelItemId", cmd.getLevelId().split(",")));
        
        //查询全部客户、我的客户、公共客户
        if(null != cmd.getType()){
        	if(2 == cmd.getType()){
        		fb = FilterBuilders.andFilter(fb ,FilterBuilders.termFilter("trackingUid", UserContext.currentUserId()));
        	}else if(3 == cmd.getType()){
        		fb = FilterBuilders.andFilter(fb ,FilterBuilders.termFilter("trackingUid", -1l));
        	}
        }
        //跟进时间、资产类型、资产面积、资产单价增加筛选
        if(null != cmd.getLastTrackingTime() && cmd.getLastTrackingTime() > 0){
        	RangeFilterBuilder rf = new RangeFilterBuilder("lastTrackingTime");
        	Long startTime = getTomorrowLastTimestamp(cmd.getLastTrackingTime());
        	rf.gte(startTime);
        	fb = FilterBuilders.andFilter(fb, rf); 
        }
        
        if(null != cmd.getPropertyType()){
        	fb = FilterBuilders.andFilter(fb ,FilterBuilders.inFilter("propertyType", cmd.getPropertyType().split(",")));
        }
        
        if(null != cmd.getPropertyArea()){
        	RangeFilterBuilder rf = new RangeFilterBuilder("propertyArea");
        	if(cmd.getPropertyArea().indexOf(",") > -1 && cmd.getPropertyArea().split(",").length == 2){
        		if(null != cmd.getPropertyArea().split(",")[0] && !"@".equals(cmd.getPropertyArea().split(",")[0])){
        			rf.gte(Double.parseDouble(cmd.getPropertyArea().split(",")[0]));
        		}
        		if(null != cmd.getPropertyArea().split(",")[1] && !"@".equals(cmd.getPropertyArea().split(",")[1])){
        			rf.lte(Double.parseDouble(cmd.getPropertyArea().split(",")[1]));
        		}
        		fb = FilterBuilders.andFilter(fb, rf); 
        	}
        	
        }
        if(null != cmd.getPropertyUnitPrice()){
        	RangeFilterBuilder rf = new RangeFilterBuilder("propertyUnitPrice");
        	if(cmd.getPropertyUnitPrice().indexOf(",") > -1 && cmd.getPropertyUnitPrice().split(",").length == 2){
        		if(null != cmd.getPropertyUnitPrice().split(",")[0] && !"@".equals(cmd.getPropertyUnitPrice().split(",")[0])){
        			rf.gte(Double.parseDouble(cmd.getPropertyUnitPrice().split(",")[0]));
        		}
        		if(null != cmd.getPropertyUnitPrice().split(",")[1] && !"@".equals(cmd.getPropertyUnitPrice().split(",")[1])){
        			rf.lte(Double.parseDouble(cmd.getPropertyUnitPrice().split(",")[1]));
        		}
        		fb = FilterBuilders.andFilter(fb, rf); 
        	}
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
        if(cmd.getSortField() != null && cmd.getSortType() != null) {
            if(cmd.getSortType() == 0) {
                builder.addSort(cmd.getSortField(), SortOrder.ASC);
            } else if(cmd.getSortType() == 1) {
                builder.addSort(cmd.getSortField(), SortOrder.DESC);
            }
        }
        SearchResponse rsp = builder.execute().actionGet();

        if(LOGGER.isDebugEnabled())
            LOGGER.info("EnterpriseCustomerSearcherImpl query builder: {}, rsp: {}", builder, rsp);

        List<Long> ids = getIds(rsp);
        SearchEnterpriseCustomerResponse response = new SearchEnterpriseCustomerResponse();

        if(ids.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
        }

        List<EnterpriseCustomerDTO> dtos = new ArrayList<>();
        Map<Long, EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomersByIds(ids);
        if(customers != null && customers.size() > 0) {
            //一把取出来的列表顺序和搜索引擎中得到的ids的顺序不一定一样 以搜索引擎的为准 by xiongying 20170907
            ids.forEach(id -> {
                EnterpriseCustomer customer = customers.get(id);
                EnterpriseCustomerDTO dto = ConvertHelper.convert(customer, EnterpriseCustomerDTO.class);
//                ScopeFieldItem categoryItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getCategoryItemId());
                ScopeFieldItem categoryItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getCommunityId(), customer.getCategoryItemId());
                if(categoryItem != null) {
                    dto.setCategoryItemName(categoryItem.getItemDisplayName());
                }
//                ScopeFieldItem levelItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getLevelItemId());
                ScopeFieldItem levelItem = fieldService.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getCommunityId(), customer.getLevelItemId());
                if(levelItem != null) {
                    dto.setLevelItemName(levelItem.getItemDisplayName());
                }
                if(null != dto.getCorpIndustryItemId()){
                	ScopeFieldItem corpIndustryItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getCommunityId(),dto.getCorpIndustryItemId());
                	if(null != corpIndustryItem){
                		dto.setCorpIndustryItemName(corpIndustryItem.getItemDisplayName());
                	}
                }
                if(null != dto.getContactGenderItemId()){
                	ScopeFieldItem contactGenderItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getCommunityId(),dto.getContactGenderItemId());
                	if(null != contactGenderItem){
                		dto.setContactGenderItemName(contactGenderItem.getItemDisplayName());
                	}
                }
                if(dto.getTrackingUid() != null && dto.getTrackingUid() != -1) {
                	dto.setTrackingName(dto.getTrackingName());
                }
                if(null != dto.getPropertyType()){
                	ScopeFieldItem propertyTypeItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getCommunityId(),dto.getPropertyType());
                	if(null != propertyTypeItem){
                		dto.setPropertyTypeName(propertyTypeItem.getItemDisplayName());
                	}
                }
                dtos.add(dto);
            });
        }
        response.setDtos(dtos);
        return response;
    }
    
    private Long getTomorrowLastTimestamp(Integer lastTrackingTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -(lastTrackingTime-1));
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}
}
