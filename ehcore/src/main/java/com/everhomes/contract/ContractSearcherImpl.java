package com.everhomes.contract;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.customer.IndividualCustomerProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractBuildingMapping;
import com.everhomes.openapi.ContractBuildingMappingProvider;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.organization.OrganizationOwner;
import com.everhomes.organization.pm.OrganizationOwnerType;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.rest.contract.*;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.ContractSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.varField.FieldProvider;
import com.everhomes.varField.ScopeField;
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
import java.util.stream.Collectors;

/**
 * Created by ying.xiong on 2017/8/17.
 */
@Component
public class ContractSearcherImpl extends AbstractElasticSearch implements ContractSearcher {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private ContractBuildingMappingProvider contractBuildingMappingProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private IndividualCustomerProvider individualCustomerProvider;

    @Autowired
    private EnterpriseCustomerProvider enterpriseCustomerProvider;

    @Autowired
    private FieldProvider fieldProvider;

    @Autowired
    private PropertyMgrProvider propertyMgrProvider;

    @Override
    public String getIndexType() {
        return SearchUtils.CONTRACT;
    }

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<Contract> contracts) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (Contract contract : contracts) {
            XContentBuilder source = createDoc(contract);
            if(null != source) {
                LOGGER.info("id:" + contract.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType()).id(contract.getId().toString()).source(source));
            }
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

    @Override
    public void feedDoc(Contract contract) {
        XContentBuilder source = createDoc(contract);
        feedDoc(contract.getId().toString(), source);
    }

    private XContentBuilder createDoc(Contract contract) {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();

            builder.field("communityId", contract.getCommunityId());
            builder.field("namespaceId", contract.getNamespaceId());
            builder.field("name", contract.getName());
            builder.field("contractNumber", contract.getContractNumber());
            builder.field("customerName", contract.getCustomerName());
            builder.field("status", contract.getStatus());
            builder.field("contractType", contract.getContractType());
            builder.field("categoryItemId", contract.getCategoryItemId());
            builder.field("contractStartDate", contract.getContractStartDate());
            builder.field("contractEndDate", contract.getContractEndDate());
            if(contract.getRent() != null) {
                builder.field("amount", contract.getRent());
            } else {
                builder.field("amount", "");
            }

            if(CustomerType.INDIVIDUAL.equals(CustomerType.fromStatus(contract.getCustomerType()))) {
                OrganizationOwner owner = individualCustomerProvider.findOrganizationOwnerById(contract.getCustomerId());
                builder.field("ownerTypeId", owner.getOrgOwnerTypeId());
            } else if(CustomerType.ENTERPRISE.equals(CustomerType.fromStatus(contract.getCustomerType()))) {
                EnterpriseCustomer customer = enterpriseCustomerProvider.findById(contract.getCustomerId());
                builder.field("customerCategoryItemId", customer.getCategoryItemId());
            }


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
            List<Contract> contracts = contractProvider.listContracts(locator, pageSize);

            if(contracts.size() > 0) {
                this.bulkUpdate(contracts);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();
        LOGGER.info("sync for contracts ok");
    }

    @Override
    public ListContractsResponse queryContracts(SearchContractCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        QueryBuilder qb = null;
        if(cmd.getKeywords() == null || cmd.getKeywords().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeywords())
                    .field("name", 1.2f)
                    .field("customerName", 1.2f)
                    .field("contractNumber", 1.2f);

            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("name").addHighlightedField("customerName").addHighlightedField("contractNumber");
        }

        FilterBuilder fb = null;
        FilterBuilder nfb = FilterBuilders.termFilter("status", ContractStatus.INACTIVE.getCode());
        fb = FilterBuilders.notFilter(nfb);
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId()));
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("communityId", cmd.getCommunityId()));

        if(cmd.getStatus() != null)
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("status", cmd.getStatus()));

        if(cmd.getCategoryItemId() != null)
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("categoryItemId", cmd.getCategoryItemId()));

        if(cmd.getContractType() != null)
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("contractType", cmd.getContractType()));

        if(cmd.getCustomerCategoryId() != null) {
            FilterBuilder customerfb = FilterBuilders.termFilter("customerCategoryItemId", cmd.getCustomerCategoryId());
            ScopeField scopeField = fieldProvider.findScopeField(cmd.getNamespaceId(), cmd.getCustomerCategoryId());
            if(scopeField != null) {
                OrganizationOwnerType ownerType = propertyMgrProvider.findOrganizationOwnerTypeByDisplayName(scopeField.getFieldDisplayName());
                if(ownerType != null) {
                    customerfb = FilterBuilders.orFilter(customerfb, FilterBuilders.termFilter("ownerTypeId", ownerType.getId()));
                }
            }
            fb = FilterBuilders.andFilter(fb, customerfb);
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

        if(LOGGER.isDebugEnabled())
            LOGGER.info("ContractSearcherImpl query builder: {}, rsp: {}", builder, rsp);

        List<Long> ids = getIds(rsp);
        ListContractsResponse response = new ListContractsResponse();

        if(ids.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
        }

        List<ContractDTO> dtos = new ArrayList<ContractDTO>();
        List<Contract> contracts = contractProvider.listContractsByIds(ids);
        if(contracts != null && contracts.size() > 0) {
            contracts.forEach(contract -> {
                ContractDTO dto = ConvertHelper.convert(contract, ContractDTO.class);
                processContractApartments(dto);
                dtos.add(dto);
            });
        }
        response.setContracts(dtos);
        return response;
    }

    private void processContractApartments(ContractDTO dto) {
        List<ContractBuildingMapping> contractApartments = contractBuildingMappingProvider.listByContract(dto.getId());
        if(contractApartments != null && contractApartments.size() > 0) {
            List<BuildingApartmentDTO> apartmentDtos = contractApartments.stream().map(apartment -> {
                BuildingApartmentDTO apartmentDto = ConvertHelper.convert(apartment, BuildingApartmentDTO.class);
                return apartmentDto;
            }).collect(Collectors.toList());
            dto.setBuildings(apartmentDtos);
        }
    }
}
