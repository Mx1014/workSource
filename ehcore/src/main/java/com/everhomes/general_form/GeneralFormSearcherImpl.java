package com.everhomes.general_form;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.general_approval.GeneralFormValDTO;
import com.everhomes.rest.general_approval.ListGeneralFormValResponse;
import com.everhomes.rest.general_approval.SearchFormValsCommand;
import com.everhomes.rest.general_approval.SearchGeneralFormItem;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
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
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeneralFormSearcherImpl extends AbstractElasticSearch implements GeneralFormSearcher{

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    GeneralFormProvider generalFormProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<GeneralFormVal> generalFormVals) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (GeneralFormVal generalFormVal : generalFormVals) {
            XContentBuilder source = createDoc(generalFormVal);
            if(null != source) {
                LOGGER.info("id:" + generalFormVal.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType()).id(generalFormVal.getId().toString()).source(source));
            }
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }



    @Override
    public void feedDoc(GeneralFormVal generalFormVal) {
        XContentBuilder source = createDoc(generalFormVal);
        feedDoc(generalFormVal.getId().toString(), source);
    }

    @Override
    public void syncFromDb() {
        int pageSize = 200;
        this.deleteAll();

        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<GeneralFormVal> generalFormVal = generalFormProvider.listGeneralForm(locator, pageSize);

            if(generalFormVal.size() > 0) {
                this.bulkUpdate(generalFormVal);
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
    public ListGeneralFormValResponse queryGeneralForm(SearchFormValsCommand cmd) {

        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());



        QueryBuilder qb = null;


        FilterBuilder fb = null;
        FilterBuilder nfb = null;
        fb = FilterBuilders.termFilter("formOriginId", cmd.getFormOriginId());
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("formVersion", cmd.getFormVersion()));

        if(cmd.getDisplayFields().size() > 0){
            List<String> displayFieldNames = cmd.getDisplayFields().stream().map(SearchGeneralFormItem::getFieldName).collect(Collectors.toList());
            List<String> displayFieldValues = cmd.getDisplayFields().stream().map(SearchGeneralFormItem::getFieldValue).collect(Collectors.toList());
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termsFilter("fieldName", displayFieldNames));
            qb = QueryBuilders.multiMatchQuery(displayFieldValues).field("fieldValues",1.2f);
        }
        if(cmd.getFilteredFields().size() > 0){
            List<String> filteredFieldNames = cmd.getFilteredFields().stream().map(SearchGeneralFormItem::getFieldName).collect(Collectors.toList());
            nfb = FilterBuilders.notFilter(FilterBuilders.termsFilter("fieldName", filteredFieldNames));
            fb = FilterBuilders.notFilter(nfb);
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

        List<GeneralFormValDTO> dtos = new ArrayList<>();
        List<GeneralFormVal> vals = generalFormProvider.listGeneralFormItemByIds(ids);
        for(GeneralFormVal val : vals){
            dtos.add(ConvertHelper.convert(val, GeneralFormValDTO.class));
        }

        response.setFieldVals(dtos);


        return response;
    }

    @Override
    public String getIndexType() {
        return SearchUtils.GENERAL_FORM_VALS;
    }


    private XContentBuilder createDoc(GeneralFormVal generalFormVal) {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();

            builder.field("id", generalFormVal.getId());
            builder.field("organizationId", generalFormVal.getOrganizationId());
            builder.field("namespaceId", generalFormVal.getNamespaceId());
            builder.field("ownerId", generalFormVal.getOwnerId());
            builder.field("ownerType", generalFormVal.getOwnerType());
            builder.field("moduleId", generalFormVal.getModuleId());
            builder.field("moduleType", generalFormVal.getModuleType());
            builder.field("sourceId", generalFormVal.getSourceId());
            builder.field("sourceType", generalFormVal.getSourceType());
            builder.field("formOriginId", generalFormVal.getFormOriginId());
            builder.field("formVersion", generalFormVal.getFormVersion());
            builder.field("fieldName", generalFormVal.getFieldName());
            builder.field("fieldType", generalFormVal.getFieldType());
            builder.field("fieldValue", generalFormVal.getFieldValue());
            builder.field("valExtra", generalFormVal.getCreateTime());

            builder.endObject();
            return builder;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
