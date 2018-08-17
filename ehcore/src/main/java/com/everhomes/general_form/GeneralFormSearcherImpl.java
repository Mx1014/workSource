package com.everhomes.general_form;

import com.everhomes.address.AddressProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.general_approval.*;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Service
public class GeneralFormSearcherImpl extends AbstractElasticSearch implements GeneralFormSearcher{

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    GeneralFormProvider generalFormProvider;
    @Autowired
    AddressProvider addressProvider;
    @Autowired
    EnterpriseCustomerProvider enterpriseCustomerProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }



    @Override
    public void bulkUpdate(List<GeneralFormVal> generalFormVals) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        XContentBuilder source = createDoc(generalFormVals);
        if(null != source) {
            LOGGER.info("id:" + generalFormVals.iterator().next().getSourceId());
            brb.add(Requests.indexRequest(getIndexName()).type(getIndexType()).id(generalFormVals.iterator().next().getSourceId().toString()).source(source));
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }



    @Override
    public void feedDoc(List<GeneralFormVal> generalFormVals) {
        XContentBuilder source = createDoc(generalFormVals);
        feedDoc(generalFormVals.iterator().next().getSourceId().toString(), source);
    }

    @Override
    public void syncFromDb() {
        int pageSize = 200;
        this.deleteAll();

        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<GeneralFormVal> generalFormVal = generalFormProvider.listGeneralForm();
        List<GeneralFormValRequest> generalFormValRequests = generalFormProvider.listGeneralFormValRequest();
        List<List<GeneralFormVal>> vals = new ArrayList<>();
        for(GeneralFormValRequest request: generalFormValRequests){
            Long sourceId = request.getId();
            List<GeneralFormVal> createBulUpdate = new ArrayList<>();
            for(GeneralFormVal temp :generalFormVal){
                if(temp.getSourceId() == sourceId){
                    createBulUpdate.add(temp);
                }
            }
            vals.add(createBulUpdate);
        }

        for(List<GeneralFormVal> temp : vals){
            if(temp.size() > 0) {
                this.bulkUpdate(temp);
            }
        }
        /*for(int i = 0 ; i < vals.size() ; i++ ) {


            if(generalFormVal.size() > 0) {
                this.bulkUpdate(generalFormVal);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }*/

        this.optimize(1);
        this.refresh();
        LOGGER.info("sync for contracts ok");
    }

    @Override
    public ListGeneralFormValResponse queryGeneralForm(SearchFormValsCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());

        QueryBuilder qb = null;

        FilterBuilder fb = null;
        FilterBuilder nfb = null;
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        fb = FilterBuilders.termFilter("namespaceId", namespaceId);
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("formVersion", cmd.getFormVersion()));
        if(cmd.getFormOriginId() == null || cmd.getFormOriginId() == 0){
            LOGGER.error("don't config approval or don't have approval is used");

            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,"没有找到正在启用的审批或表单");

        }
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("namespaceId", namespaceId));
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerId", cmd.getOwnerId()));
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerType", cmd.getOwnerType()));

        // if(cmd.getDisplayFields().size() > 0){
        //     List<String> displayFieldNames = cmd.getDisplayFields().stream().map(SearchGeneralFormItem::getFieldName).collect(Collectors.toList());
        //     List<String> displayFieldValues = cmd.getDisplayFields().stream().map(SearchGeneralFormItem::getFieldValue).collect(Collectors.toList());
        //     fb = FilterBuilders.andFilter(fb, FilterBuilders.termsFilter("fieldName", displayFieldNames));
        //     qb = QueryBuilders.multiMatchQuery(displayFieldValues).field("fieldValues",1.2f);
        // }
        // if(cmd.getFilteredFields().size() > 0){
        //     List<String> filteredFieldNames = cmd.getFilteredFields().stream().map(SearchGeneralFormItem::getFieldName).collect(Collectors.toList());
        //     nfb = FilterBuilders.notFilter(FilterBuilders.termsFilter("fieldName", filteredFieldNames));
        //     fb = FilterBuilders.notFilter(nfb);
        // }

        if(cmd.getConditionFields() != null && cmd.getConditionFields().size() > 0){
            for(SearchGeneralFormItem item : cmd.getConditionFields()){
                if(StringUtils.isNotBlank(item.getFieldValue())) {
                    GeneralFormFieldType type = GeneralFormFieldType.fromCode(item.getFieldType().getCode());
                    switch (type) {
                        case NUMBER_TEXT:
                            fb = FilterBuilders.andFilter(fb, FilterBuilders.termsFilter(item.getFieldName(), item.getFieldValue()));
                            break;
                        case INTEGER_TEXT:
                            fb = FilterBuilders.andFilter(fb, FilterBuilders.termsFilter(item.getFieldName(), item.getFieldValue()));
                            break;
                        case DROP_BOX:
                            fb = FilterBuilders.andFilter(fb, FilterBuilders.termsFilter(item.getFieldName(), item.getFieldValue()));
                            break;
                        case SINGLE_LINE_TEXT:
                            fb = FilterBuilders.andFilter(fb, FilterBuilders.termsFilter(item.getFieldName(), item.getFieldValue()));
                            break;
                        case MULTI_LINE_TEXT:
                            fb = FilterBuilders.andFilter(fb, FilterBuilders.termsFilter(item.getFieldName(), item.getFieldValue()));
                            break;
                        default:
                            fb = FilterBuilders.andFilter(fb, FilterBuilders.termsFilter(item.getFieldName(), item.getFieldValue()));
                            break;
                    }
                }
            }
        }
        qb = QueryBuilders.filteredQuery(qb, fb);

        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }


        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);
        builder.addSort("id", SortOrder.DESC);

        SearchResponse rsp = builder.execute().actionGet();

        if(LOGGER.isDebugEnabled())
            LOGGER.info("GeneralFormValSearcherImpl query builder: {}, rsp: {}", builder, rsp);

        List<Long> ids = getIds(rsp);
        ListGeneralFormValResponse response = new ListGeneralFormValResponse();

        if(ids.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
        }

        

        List<Map<String, Object>> fieldVals = new ArrayList<>();
        for (SearchHit hit : rsp.getHits().getHits()) {
            Object sourceIdObj = hit.getSource().get("sourceId");
            Object ownerIdObj = hit.getSource().get("ownerId");
            Object moduleIdObj = hit.getSource().get("moduleId");
            if (sourceIdObj != null && ownerIdObj != null) {
                // GeneralFormValRequest request = generalFormProvider.getGeneralFormValRequest(UserContext.getCurrentNamespaceId(), Long.valueOf(sourceIdObj.toString()), Long.valueOf(ownerIdObj.toString()));
                Long sourceId = Long.valueOf(sourceIdObj.toString());
                Long moduleId = Long.valueOf(moduleIdObj.toString());
                Long ownerId = Long.valueOf(ownerIdObj.toString());
                List<GeneralFormVal> vals = generalFormProvider.getGeneralFormVal(namespaceId, sourceId, moduleId, ownerId);
                Map<String, Object> returnMap = new HashMap<>();
                for (GeneralFormVal val : vals) {
                    
                    GeneralFormValDTO dto = ConvertHelper.convert(val, GeneralFormValDTO.class);     
                    String fieldValue = dto.getFieldValue();
                    ObjectMapper mapper = new ObjectMapper(); 
                    JavaType jvt = mapper.getTypeFactory().constructParametricType(HashMap.class,String.class,String.class);
                    Map<String,String> urMap;
                    try {
                        urMap = mapper.readValue(fieldValue, jvt);
                        for (Entry<String, String> entry : urMap.entrySet()) {
                            fieldValue  = entry.getValue();

                            if(entry.getKey().equals("addressId")){
                                fieldValue = addressProvider.getAddressNameById(Long.valueOf(entry.getValue()));
                                break;
                            }
                            if(entry.getKey().equals("customerName")){
                                EnterpriseCustomer customer = enterpriseCustomerProvider.findById(Long.valueOf(entry.getValue()));
                                fieldValue = customer.getName();
                                break;
                            }
                            if(entry.getKey().equals("text")) {
                                if (StringUtils.isNotBlank(fieldValue)) {
                                    break;
                                }
                            }

                        }
                        returnMap.put(dto.getFieldName(), fieldValue);
                    } catch (IOException e) {
                    }

                }
                GeneralFormValRequest request = generalFormProvider.getGeneralFormValRequest(sourceId);
                if(request != null)
                    returnMap.put("approvalStatus",request.getApprovalStatus());
                returnMap.put("sourceId", sourceId);

                fieldVals.add(returnMap);
            }
        }
        // for(GeneralFormVal val : vals) {
        //     dtos.add(ConvertHelper.convert(val, GeneralFormValDTO.class));
        // }

        // response.setFieldVals(dtos);

        response.setValList(fieldVals);

        return response;
    }

    @Override
    public String getIndexType() {
        return SearchUtils.GENERAL_FORM_VALS;
    }


    private XContentBuilder createDoc(List<GeneralFormVal> generalFormVal) {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            if (generalFormVal.size() > 0) {
                GeneralFormVal first = generalFormVal.iterator().next();
                builder.field("id", first.getSourceId());
                builder.field("organizationId", first.getOrganizationId());
                builder.field("namespaceId", first.getNamespaceId());
                builder.field("ownerId", first.getOwnerId());
                builder.field("ownerType", first.getOwnerType());
                builder.field("moduleId", first.getModuleId());
                builder.field("moduleType", first.getModuleType());
                builder.field("sourceId", first.getSourceId());
                builder.field("sourceType", first.getSourceType());
                builder.field("formOriginId", first.getFormOriginId());
                builder.field("formVersion", first.getFormVersion());
                builder.field("approvalStatus", 0);
            }

            for (GeneralFormVal val : generalFormVal) {
                builder.field(val.getFieldName(), val.getFieldValue());
            }

            builder.endObject();
            return builder;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
