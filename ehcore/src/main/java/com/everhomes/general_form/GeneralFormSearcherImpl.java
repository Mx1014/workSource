package com.everhomes.general_form;

import com.everhomes.community.Building;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractBuildingMapping;
import com.everhomes.rest.general_approval.GeneralFormValDTO;
import com.everhomes.rest.general_approval.ListGeneralFormValResponse;
import com.everhomes.rest.general_approval.SearchFormValsCommand;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.SearchUtils;
import com.everhomes.server.schema.tables.pojos.EhGeneralFormVals;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeneralFormSearcherImpl extends AbstractElasticSearch implements GeneralFormSearcher{

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    GeneralFormProvider generalFormProvider;

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
    }

    @Override
    public ListGeneralFormValResponse queryContracts(SearchFormValsCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        QueryBuilder qb = null;
        return null;
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
