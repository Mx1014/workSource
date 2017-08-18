package com.everhomes.customer;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
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
import com.everhomes.varField.ScopeFieldItem;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

            builder.field("communityId", customer.getCommunityId());
            builder.field("namespaceId", customer.getNamespaceId());
            builder.field("name", customer.getName());
            builder.field("contactMobile", customer.getContactMobile());
            builder.field("contactName", customer.getContactName());
            builder.field("contactAddress", customer.getContactAddress());
            builder.field("categoryItemId", customer.getCategoryItemId());
            builder.field("levelItemId", customer.getLevelItemId());
            builder.field("status", customer.getStatus());

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
                    .field("contactAddress", 1.2f)
                    .field("contactMobile", 1.0f);

            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("name").addHighlightedField("contactAddress").addHighlightedField("contactMobile");
        }

        FilterBuilder fb = null;
        FilterBuilder nfb = FilterBuilders.termFilter("status", CommonStatus.INACTIVE.getCode());
        fb = FilterBuilders.notFilter(nfb);
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("namespaceId", UserContext.getCurrentNamespaceId()));
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("communityId", cmd.getCommunityId()));

        if(cmd.getCustomerCategoryId() != null)
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("categoryItemId", cmd.getCustomerCategoryId()));

        if(cmd.getLevelId() != null)
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("levelItemId", cmd.getLevelId()));

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

        if(LOGGER.isDebugEnabled())
            LOGGER.info("EnterpriseCustomerSearcherImpl query builder: {}, rsp: {}", builder, rsp);

        List<Long> ids = getIds(rsp);
        SearchEnterpriseCustomerResponse response = new SearchEnterpriseCustomerResponse();

        if(ids.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
        }

        List<EnterpriseCustomerDTO> dtos = new ArrayList<>();
        List<EnterpriseCustomer> customers = enterpriseCustomerProvider.listEnterpriseCustomersByIds(ids);
        if(customers != null && customers.size() > 0) {
            customers.forEach(customer -> {
                EnterpriseCustomerDTO dto = ConvertHelper.convert(customer, EnterpriseCustomerDTO.class);
                ScopeFieldItem CategoryItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getCategoryItemId());
                if(CategoryItem != null) {
                    dto.setCategoryItemName(CategoryItem.getItemDisplayName());
                }
                ScopeFieldItem LevelItem = fieldProvider.findScopeFieldItemByFieldItemId(customer.getNamespaceId(), customer.getLevelItemId());
                if(LevelItem != null) {
                    dto.setLevelItemName(LevelItem.getItemDisplayName());
                }
                dtos.add(dto);
            });
        }
        response.setDtos(dtos);
        return response;
    }
}
