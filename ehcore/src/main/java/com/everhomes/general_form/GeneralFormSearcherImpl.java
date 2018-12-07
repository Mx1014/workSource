package com.everhomes.general_form;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.everhomes.address.AddressProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.flow.FlowService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.flow.FlowCaseDetailDTOV2;
import com.everhomes.rest.flow.FlowNodeDetailDTO;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.general_approval.GeneralFormFieldType;
import com.everhomes.rest.general_approval.GeneralFormValDTO;
import com.everhomes.rest.general_approval.ListGeneralFormValResponse;
import com.everhomes.rest.general_approval.SearchFormValsCommand;
import com.everhomes.rest.general_approval.SearchGeneralFormItem;
import com.everhomes.rest.investmentAd.GeneralFormValTemplate;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    FlowService flowService;
    @Autowired
    UserProvider userProvider;

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
            LOGGER.info("Bulk update general form to es, id={}, size={}" 
            		, generalFormVals.iterator().next().getSourceId(), generalFormVals.size());
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
    	long startTime = System.currentTimeMillis();
        int pageSize = 200;
        this.deleteAll();

        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<GeneralFormVal> generalFormVal = generalFormProvider.listGeneralForm();
        List<GeneralFormValRequest> generalFormValRequests = generalFormProvider.listGeneralFormValRequest();
        List<List<GeneralFormVal>> vals = new ArrayList<>();
        if(LOGGER.isDebugEnabled()) {
        	int valueSize = (generalFormVal == null) ? 0 : generalFormVal.size();
        	int requestSize = (generalFormValRequests == null) ? 0 : generalFormValRequests.size();
        	LOGGER.debug("Start to sync general form data, valueSize={}, requestSize={}", valueSize, requestSize);
        }
        
        int round = 0;
        long roundStartTime = System.currentTimeMillis();
        long roundEndTime = roundStartTime;
        for(GeneralFormValRequest request: generalFormValRequests){
            Long sourceId = request.getId();
            List<GeneralFormVal> createBulUpdate = new ArrayList<>();
            for(GeneralFormVal temp :generalFormVal){
                if(temp.getSourceId().equals(sourceId)){
                    createBulUpdate.add(temp);
                }
            }
            vals.add(createBulUpdate);
        	round++;
        	if(LOGGER.isDebugEnabled() && (round % 10000) == 0) {
        		roundEndTime = System.currentTimeMillis();
        		LOGGER.debug("Start to sync general form data, round={}, elapse={}", round, (roundEndTime - roundStartTime));
        		roundStartTime = roundEndTime;
        	}
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
        long endTime = System.currentTimeMillis();
        LOGGER.info("End to sync general form data, elapse={}", (endTime - startTime));
    }

    @Override
    public ListGeneralFormValResponse queryGeneralForm(SearchFormValsCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());

        QueryBuilder qb = QueryBuilders.matchAllQuery();
        FilterBuilder fb = null;
        FilterBuilder nfb = null;
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        fb = FilterBuilders.termFilter("namespaceId", namespaceId);


        if(cmd.getFormOriginId() == null || cmd.getFormOriginId() == 0){
            LOGGER.error("don't config approval or don't have approval is used");

            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,"没有找到正在启用的审批或表单");

        }
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerId", cmd.getOwnerId()));

        if(cmd.getModuleId() != null){
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("moduleId", cmd.getModuleId()));
        }

        if(cmd.getApprovalId() != null && cmd.getApprovalId() != 0){
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("approvalId", cmd.getApprovalId()));
        }

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
        builder.addSort(SortBuilders.fieldSort("id").order(SortOrder.DESC));

        SearchResponse rsp = null;
        try {
            rsp = builder.execute().actionGet();
        }catch (Exception e){
            LOGGER.error("execute the es has failed");
            return null;
        }

        if(LOGGER.isDebugEnabled())
            LOGGER.info("GeneralFormValSearcherImpl query builder: {}, rsp: {}", builder, rsp);

        List<Long> ids = getIds(rsp);
        ListGeneralFormValResponse response = new ListGeneralFormValResponse();

        SearchHit[] tempHits = rsp.getHits().getHits();
        SearchHit[] hits;

        if(ids.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            hits = new SearchHit[tempHits.length-1];
            System.arraycopy(tempHits, 0, hits, 0, tempHits.length - 1);
        }else{
            hits = tempHits;
        }

        List<Map<String, Object>> fieldVals = new ArrayList<>();
        for (SearchHit hit : hits) {
            Object sourceIdObj = hit.getSource().get("sourceId");
            Object ownerIdObj = hit.getSource().get("ownerId");
            Object moduleIdObj = hit.getSource().get("moduleId");
            Object formOriginIdObj = hit.getSource().get("formOriginId");
            Object formVersionObj = hit.getSource().get("formVersion");
            if (sourceIdObj != null && ownerIdObj != null) {
                // GeneralFormValRequest request = generalFormProvider.getGeneralFormValRequest(UserContext.getCurrentNamespaceId(), Long.valueOf(sourceIdObj.toString()), Long.valueOf(ownerIdObj.toString()));
                Long sourceId = Long.valueOf(sourceIdObj.toString());
                Long moduleId = Long.valueOf(moduleIdObj.toString());
                Long ownerId = Long.valueOf(ownerIdObj.toString());
                Long formOriginId = Long.valueOf(formOriginIdObj.toString());
                Long formVersion = Long.valueOf(formVersionObj.toString());
                List<GeneralFormVal> vals = generalFormProvider.getGeneralFormVal(namespaceId, sourceId, moduleId, ownerId);
                Map<String, Object> returnMap = new HashMap<>();
                for (GeneralFormVal val : vals) {
                    GeneralFormValDTO dto = ConvertHelper.convert(val, GeneralFormValDTO.class);     
                    String fieldValue = dto.getFieldValue();
                    String fieldName = dto.getFieldName();
                   //设置显示的值
                    if ("APARTMENT".equals(fieldName)) {
        				GeneralFormValTemplate parseObject = JSON.parseObject(val.getFieldValue(), GeneralFormValTemplate.class);
        				String jsonStr = parseObject.getText();
        				returnMap.put(fieldName, jsonStr);
                    }else {
                    	ObjectMapper mapper = new ObjectMapper(); 
                        JavaType jvt = mapper.getTypeFactory().constructParametricType(HashMap.class,String.class,String.class);
                        Map<String,String> urMap;
                        try {
                            urMap = mapper.readValue(fieldValue, jvt);
                            for (Entry<String, String> entry : urMap.entrySet()) {
                                fieldValue  = entry.getValue();

                                if(entry.getKey().equals("addressId")){
                                    if(StringUtils.isNotBlank(entry.getValue())) {
                                        fieldValue = addressProvider.getAddressNameById(Long.valueOf(entry.getValue()));
                                    }
                                    break;
                                }
                                if(entry.getKey().equals("customerName")){
                                    if(StringUtils.isNotBlank(entry.getValue())) {
                                        EnterpriseCustomer customer = enterpriseCustomerProvider.findById(Long.valueOf(entry.getValue()));
                                        fieldValue = customer.getName();
                                    }
                                    break;
                                }
                                if(entry.getKey().equals("text")) {
                                    if (StringUtils.isNotBlank(fieldValue)) {
                                        break;
                                    }
                                }
                            }
                            returnMap.put(fieldName, fieldValue);
                        } catch (IOException e) {
                            returnMap.put(fieldName, dto.getFieldValue());
                        }
					}
                }
                GeneralFormValRequest request = generalFormProvider.getGeneralFormValRequest(sourceId);
                if(request != null){
                	returnMap.put("approvalStatus",request.getApprovalStatus());
                	//--------房源招商申请业务--------
                	if (request.getInvestmentAdId() != null) {
                		returnMap.put("investmentAdId",request.getInvestmentAdId());
					}
                	returnMap.put("transformStatus",request.getTransformStatus());
                	//--------房源招商申请业务--------
                    returnMap.put("createdTime", request.getCreatedTime());
                }
                returnMap.put("sourceId", sourceId);
                returnMap.put("formVersion", formVersion);
                returnMap.put("formOriginId", formOriginId);
                if(StringUtils.isNotBlank(cmd.getReferType())){
                    FlowCaseDetailDTOV2 flowCase = flowService.getFlowCaseDetailByRefer(moduleId, FlowUserType.NO_USER, UserContext.currentUserId(), cmd.getReferType(), sourceId, false);
                    if(flowCase != null){
                        FlowNodeDetailDTO flowNode = flowService.getFlowNodeDetail(flowCase.getCurrentNodeId());
                        returnMap.put("flowCaseCreateTime", flowCase.getCreateTime().getTime());
                        returnMap.put("nodeName", flowNode.getNodeName());
                        returnMap.put("applyUser", userProvider.findUserById(flowCase.getApplyUserId()).getNickName());
                    }
                }

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
                GeneralFormValRequest request = generalFormProvider.getGeneralFormValRequest(first.getSourceId());
                if(request != null) {
                    if (request.getApprovalStatus() == null) {
                        builder.field("审批状态", 0);
                    } else {
                        builder.field("审批状态", request.getApprovalStatus());
                    }
                }

            }

            for (GeneralFormVal val : generalFormVal) {
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
                            //fieldValue = addressProvider.getAddressNameById(Long.valueOf(entry.getValue()));
                            if (StringUtils.isNotBlank(fieldValue)) {
                                break;
                            }
                        }
                        if(entry.getKey().equals("customerName")){
                            if (StringUtils.isNotBlank(fieldValue)) {
                                break;
                            }
                        }
                        if(entry.getKey().equals("text")) {
                            if (StringUtils.isNotBlank(fieldValue)) {
                                break;
                            }
                        }

                    }
                    builder.field(val.getFieldName(), fieldValue);
                } catch (IOException e) {
                	
                }

            }

            builder.endObject();
            return builder;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



	@Override
	public void syncFromDbV2() {
		long startTime = System.currentTimeMillis();
		LOGGER.info("start to sync general form data V2");
        
		this.deleteAll();
        LOGGER.info("sync general form data V2,delete existed data ok!");
        
        List<GeneralFormValRequest> generalFormValRequests = generalFormProvider.listGeneralFormValRequest();
        if (generalFormValRequests != null && generalFormValRequests.size() > 0) {
        	for (GeneralFormValRequest request : generalFormValRequests) {
        		LOGGER.info("sync general form data V2,save request, requestId : {}",request.getId());
        		List<GeneralFormVal> generalFormVals = generalFormProvider.getGeneralFormValBySourceId(request.getId());
            	if (generalFormVals != null && generalFormVals.size() > 0) {
            		this.bulkUpdate(generalFormVals);
    			}
    		}
		}
        this.optimize(1);
        this.refresh();
        long endTime = System.currentTimeMillis();
        LOGGER.info("end to sync general form data V2, elapse={}", (endTime - startTime));
	}

}
