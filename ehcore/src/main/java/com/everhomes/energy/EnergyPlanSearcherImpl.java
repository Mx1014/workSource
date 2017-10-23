package com.everhomes.energy;

import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationJobPosition;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.repeat.RepeatProvider;
import com.everhomes.repeat.RepeatSettings;
import com.everhomes.rest.energy.SearchEnergyPlansCommand;
import com.everhomes.rest.energy.SearchEnergyPlansResponse;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.EnergyPlanSearcher;
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

/**
 * Created by ying.xiong on 2017/10/23.
 */
public class EnergyPlanSearcherImpl extends AbstractElasticSearch implements EnergyPlanSearcher {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private  EnergyPlanProvider energyPlanProvider;

    @Autowired
    private RepeatProvider repeatProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Override
    public String getIndexType() {
        return SearchUtils.ENERGY_PLAN;
    }

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<EnergyPlan> plans) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (EnergyPlan plan : plans) {
            XContentBuilder source = createDoc(plan);
            if(null != source) {
                LOGGER.info("id:" + plan.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType()).id(plan.getId().toString()).source(source));
            }
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

    @Override
    public void feedDoc(EnergyPlan plan) {
        XContentBuilder source = createDoc(plan);
        feedDoc(plan.getId().toString(), source);
    }

    private XContentBuilder createDoc(EnergyPlan plan) {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            builder.field("planName", plan.getName());
            builder.field("communityId", plan.getTargetId());
            builder.field("ownerId", plan.getOwnerId());
            builder.field("namespaceId", plan.getNamespaceId());
            RepeatSettings rs = repeatProvider.findRepeatSettingById(plan.getRepeatSettingId());
            if(rs != null) {
                builder.field("startTime", rs.getStartDate());
                builder.field("endTime", rs.getEndDate());
            }
            List<EnergyPlanGroupMap> groupMaps = energyPlanProvider.listGroupsByEnergyPlan(plan.getId());
            if(groupMaps != null && groupMaps.size() > 0) {
                for(EnergyPlanGroupMap groupMap : groupMaps) {
                    StringBuilder sb = new StringBuilder();
                    Organization group = organizationProvider.findOrganizationById(groupMap.getGroupId());
                    OrganizationJobPosition position = organizationProvider.findOrganizationJobPositionById(groupMap.getPositionId());
                    if(group != null) {
                        sb.append(group.getName());

                    }
                    if(position != null) {
                        sb.append(position.getName());
                    }

                    if(sb.length() != 0) {
                        builder.array("groupName", sb.toString());
                    }
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
    public void syncFromDb() {
        this.deleteAll();
        int pageSize = 200;
        long pageAnchor = 0;
        List<EnergyPlan> plans = energyPlanProvider.listEnergyPlans(pageAnchor, pageSize);
        while (plans != null && plans.size() > 0) {
            bulkUpdate(plans);
            pageAnchor += (plans.size() + 1);
            plans = energyPlanProvider.listEnergyPlans(pageAnchor, pageSize);
        }
        this.optimize(1);
        this.refresh();
        LOGGER.info("sync for energy plan ok");
    }

    @Override
    public SearchEnergyPlansResponse query(SearchEnergyPlansCommand cmd) {
        return null;
    }
}
