package com.everhomes.energy;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationJobPosition;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.energy.*;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.EnergyMeterTaskSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.mysql.jdbc.StringUtils;

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
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/10/23.
 */
@Component
public class EnergyMeterTaskSearcherImpl extends AbstractElasticSearch implements EnergyMeterTaskSearcher {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private EnergyMeterProvider energyMeterProvider;

    @Autowired
    private EnergyMeterTaskProvider energyMeterTaskProvider;

    @Autowired
    private EnergyMeterAddressProvider energyMeterAddressProvider;

    @Autowired
    private EnergyPlanProvider energyPlanProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private EnergyConsumptionService energyConsumptionService;

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
            Byte status = task.getStatus() == null ? 0 : task.getStatus();
            builder.field("status", status);
            EnergyMeter meter = energyMeterProvider.findById(task.getNamespaceId(), task.getMeterId());
            if(meter != null) {
                builder.field("meterName", meter.getName());
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
            pageAnchor = tasks.get(tasks.size() - 1).getId() + 1;
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
        if (!StringUtils.isNullOrEmpty(cmd.getKeywords())) {
        	//任务工单：表计名称 支持模糊搜索  by ycx 缺陷 #39571
            String pattern = "*" + cmd.getKeywords() + "*";
            qb = QueryBuilders.boolQuery()
    					.must(QueryBuilders.wildcardQuery("meterName", pattern));
        }
        
//        if(cmd.getKeywords() == null || cmd.getKeywords().isEmpty()) {
//            qb = QueryBuilders.matchAllQuery();
//        } else {
//            qb = QueryBuilders.multiMatchQuery(cmd.getKeywords())
//                    .field("meterName", 1.5f);
//
//            builder.setHighlighterFragmentSize(60);
//            builder.setHighlighterNumOfFragments(8);
//            builder.addHighlightedField("meterName");
//        }

        FilterBuilder fb = FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId());
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("communityId", cmd.getCommunityId()));
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("planId", cmd.getPlanId()));

        if(cmd.getStartTime() != null) {
            RangeFilterBuilder rf = new RangeFilterBuilder("startTime");
            rf.gte(cmd.getStartTime());
            fb = FilterBuilders.andFilter(fb, rf);
        }

        if(cmd.getEndTime() != null) {
            RangeFilterBuilder rf = new RangeFilterBuilder("endTime");
            rf.lte(cmd.getEndTime());
            fb = FilterBuilders.andFilter(fb, rf);
        }

        if(cmd.getStatus() != null) {
//            //已抄未完成
//            if(cmd.getStatus() == 3) {
//                FilterBuilder statusfb = FilterBuilders.termFilter("status", EnergyTaskStatus.NON_READ.getCode());
//                RangeFilterBuilder rf = new RangeFilterBuilder("endTime");
//                rf.lte(DateHelper.currentGMTTime().getTime());
//                statusfb = FilterBuilders.andFilter(statusfb, rf);
//                statusfb = FilterBuilders.orFilter(statusfb, FilterBuilders.termFilter("status", EnergyTaskStatus.NON_READ_DELAY.getCode()));
//                fb = FilterBuilders.andFilter(fb, statusfb);
//            } else {
                fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("status", cmd.getStatus()));
//            }

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
//        if(cmd.getKeywords() == null || cmd.getKeywords().isEmpty()) {
//            builder.addSort("id", SortOrder.DESC);
//        }
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

        Map<Long, EnergyMeterTask> tasks = energyMeterTaskProvider.listEnergyMeterTasks(ids);
        if(tasks != null && tasks.size() > 0) {
            tasks.forEach((id, task) -> {
                EnergyMeterTaskDTO dto = ConvertHelper.convert(task, EnergyMeterTaskDTO.class);
                EnergyMeter meter = energyMeterProvider.findById(task.getNamespaceId(), task.getMeterId());
                dto.setMeterName(meter.getName());
                dto.setMeterNumber(meter.getMeterNumber());
                dto.setMeterType(meter.getMeterType());

                dto.setMaxReading(meter.getMaxReading());
                dto.setStartReading(meter.getStartReading());
                // 日读表差
                dto.setDayPrompt(energyConsumptionService.processDayPrompt(meter,meter.getNamespaceId()));
                // 月读表差
                dto.setMonthPrompt(energyConsumptionService.processMonthPrompt(meter,meter.getNamespaceId()));

                List<EnergyMeterAddress> addressMap = energyMeterAddressProvider.listByMeterId(task.getMeterId());
                if(addressMap != null && addressMap.size() > 0) {
                    dto.setApartmentFloor(addressMap.get(0).getApartmentFloor());
                    dto.setApartmentName(addressMap.get(0).getApartmentName());
                }

                List<EnergyPlanGroupMap> groupMaps = energyPlanProvider.listGroupsByEnergyPlan(task.getPlanId());
                if(groupMaps != null && groupMaps.size() > 0) {
                    List<EnergyPlanGroupDTO> groups = new ArrayList<>();
                    groupMaps.forEach(group -> {
                        EnergyPlanGroupDTO groupDTO = ConvertHelper.convert(group, EnergyPlanGroupDTO.class);
                        StringBuilder sb = new StringBuilder();
                        Organization org = organizationProvider.findOrganizationById(group.getGroupId());
                        OrganizationJobPosition position = organizationProvider.findOrganizationJobPositionById(group.getPositionId());
                        if(org != null) {
                            sb.append(org.getName());

                        }
                        if(position != null) {
                            sb.append(position.getName());
                        }
                        groupDTO.setGroupName(sb.toString());
                        groups.add(groupDTO);
                    });
                    dto.setGroups(groups);
                }


                taskDTOs.add(dto);
            });
        }
        response.setTaskDTOs(taskDTOs);
        return response;
    }
}
