package com.everhomes.energy;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.energy.EnergyMeterTaskDTO;
import com.everhomes.rest.energy.SearchTasksByEnergyPlanCommand;
import com.everhomes.rest.energy.SearchTasksByEnergyPlanResponse;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.EnergyMeterTaskSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ying.xiong on 2017/10/23.
 */
public class EnergyMeterTaskSearcherImpl extends AbstractElasticSearch implements EnergyMeterTaskSearcher {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private EnergyMeterProvider energyMeterProvider;

    @Autowired
    private EnergyMeterTaskProvider energyMeterTaskProvider;

    @Override
    public String getIndexType() {
        return SearchUtils.ENERGY_TASK;
    }

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<EnergyMeterTask> tasks) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (EnergyMeterTask task : tasks) {
            XContentBuilder source = createDoc(task);
            if(null != source) {
                LOGGER.info("id:" + task.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType()).id(task.getId().toString()).source(source));
            }
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

    @Override
    public void feedDoc(EnergyMeterTask task) {
        XContentBuilder source = createDoc(task);
        feedDoc(task.getId().toString(), source);
    }

    private XContentBuilder createDoc(EnergyMeterTask task) {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            builder.field("planId", task.getPlanId());
            builder.field("communityId", task.getTargetId());
            builder.field("namespaceId", task.getNamespaceId());
            builder.field("startTime", task.getExecutiveStartTime());
            builder.field("endTime", task.getExecutiveExpireTime());
            builder.field("status", task.getStatus());
            EnergyMeter meter = energyMeterProvider.findById(task.getNamespaceId(), task.getMeterId());
            if(meter != null) {
                builder.field("meter", meter.getName());
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
        List<EnergyMeterTask> tasks = energyMeterTaskProvider.listEnergyMeterTasks(pageAnchor, pageSize);
        while (tasks != null && tasks.size() > 0) {
            bulkUpdate(tasks);
            pageAnchor += (tasks.size() + 1);
            tasks = energyMeterTaskProvider.listEnergyMeterTasks(pageAnchor, pageSize);
        }
        this.optimize(1);
        this.refresh();
        LOGGER.info("sync for energy task ok");
    }

    @Override
    public SearchTasksByEnergyPlanResponse searchTasksByEnergyPlan(SearchTasksByEnergyPlanCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        QueryBuilder qb = null;
        if(cmd.getKeywords() == null || cmd.getKeywords().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeywords())
                    .field("planName", 1.5f)
                    .field("groupName", 1.0f);

            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("planName").addHighlightedField("groupName");
        }

        FilterBuilder fb = null;
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId()));
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("communityId", cmd.getCommunityId()));

        if(cmd.getStartTime() != null) {
            RangeFilterBuilder rf = new RangeFilterBuilder("startTime");
            rf.gt(cmd.getStartTime());
            fb = FilterBuilders.andFilter(fb, rf);
        }

        if(cmd.getEndTime() != null) {
            RangeFilterBuilder rf = new RangeFilterBuilder("endTime");
            rf.lt(cmd.getEndTime());
            fb = FilterBuilders.andFilter(fb, rf);
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
        if(cmd.getKeywords() == null || cmd.getKeywords().isEmpty()) {
            builder.addSort("id", SortOrder.DESC);
        }
        SearchResponse rsp = builder.execute().actionGet();

        if(LOGGER.isDebugEnabled())
            LOGGER.info("EnergyMeterTaskSearcherImpl query builder: {}, rsp: {}", builder, rsp);

        List<Long> ids = getIds(rsp);
        SearchTasksByEnergyPlanResponse response = new SearchTasksByEnergyPlanResponse();
        List<EnergyMeterTaskDTO> taskDTOs = new ArrayList<>();
        if(ids.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
        }
        response.setTaskDTOs(taskDTOs);
        return response;
    }
}
