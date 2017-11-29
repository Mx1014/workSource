package com.everhomes.equipment;


import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.equipment.searchEquipmentInspectionPlansCommand;
import com.everhomes.rest.equipment.searchEquipmentInspectionPlansResponse;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.EquipmentPlanSearcher;
import com.everhomes.search.SearchUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

public class EquipmentPlanSearcherImpl extends AbstractElasticSearch implements EquipmentPlanSearcher {

    private static  final Logger LOGGER = LoggerFactory.getLogger(EquipmentPlanSearcherImpl.class);

    @Autowired
    private  EquipmentProvider equipmentProvider;


    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<EquipmentInspectionPlans> plans) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (EquipmentInspectionPlans plan : plans) {

            XContentBuilder source = createDoc(plan);
            if(null != source) {
                LOGGER.info("equipment Plan id:" + plan.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(plan.getId().toString()).source(source));
            }

        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }

    }


    @Override
    public void feedDoc(EquipmentInspectionPlans plan) {
        XContentBuilder source = createDoc(plan);

        feedDoc(plan.getId().toString(), source);

    }

    @Override
    public void syncFromDb() {
        int pageSize = 200;
        this.deleteAll();

        ListingLocator locator = new ListingLocator();
        for(;;) {
            List<EquipmentInspectionPlans> plans = equipmentProvider.ListEquipmentInspectionPlans(locator, pageSize);

            if(plans.size() > 0) {
                this.bulkUpdate(plans);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();

        LOGGER.info("sync for equipment plans ok");

    }

    @Override
    public searchEquipmentInspectionPlansResponse query(searchEquipmentInspectionPlansCommand cmd) {
        return null;
    }

    @Override
    public String getIndexType() {
        return SearchUtils.EQUIPMENTPLANINDEXTYPE;
    }

    private XContentBuilder createDoc(EquipmentInspectionPlans plan) {
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("namespaceId", plan.getNamespaceId());
            b.field("ownerId", plan.getOwnerId());
            b.field("ownerType", plan.getOwnerType());
            b.field("targetId", plan.getTargetId());
            b.field("targetType", plan.getTargetType());
            b.field("name", plan.getName());
            b.field("planNumber", plan.getPlanNumber());
            b.field("planType", plan.getPlanType());
            //关联计划的周期类型
            b.field("repeatType", plan.getRepeatSettings().getRepeatType());

            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create accessory " + plan.getId() + " error");
            return null;
        }
    }
}
