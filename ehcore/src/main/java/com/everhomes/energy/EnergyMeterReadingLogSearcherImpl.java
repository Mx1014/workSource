package com.everhomes.energy;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.energy.EnergyMeterReadingLogDTO;
import com.everhomes.rest.energy.SearchEnergyMeterReadingLogsCommand;
import com.everhomes.rest.energy.SearchEnergyMeterReadingLogsResponse;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.EnergyMeterReadingLogSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xq.tian on 2016/10/31.
 */
@Component
public class EnergyMeterReadingLogSearcherImpl extends AbstractElasticSearch implements EnergyMeterReadingLogSearcher {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private EnergyMeterProvider meterProvider;

    @Autowired
    private EnergyMeterReadingLogProvider readingLogProvider;

    @Autowired
    private UserProvider userProvider;

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<EnergyMeterReadingLog> meters) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (EnergyMeterReadingLog meter : meters) {
            XContentBuilder source = createDoc(meter);
            if(null != source) {
                LOGGER.info("id:" + meter.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType()).id(meter.getId().toString()).source(source));
            }
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

    @Override
    public void feedDoc(EnergyMeterReadingLog meter) {
        XContentBuilder source = createDoc(meter);
        feedDoc(meter.getId().toString(), source);
    }

    private XContentBuilder createDoc(EnergyMeterReadingLog readingLog) {
        try {
            EnergyMeter meter = meterProvider.findById(UserContext.getCurrentNamespaceId(), readingLog.getMeterId());
            User operator = userProvider.findUserById(readingLog.getOperatorId());

            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            builder.field("communityId", readingLog.getCommunityId());
            builder.field("meterType", meter.getMeterType());
            builder.field("billCategoryId", meter.getBillCategoryId());
            builder.field("serviceCategoryId", meter.getServiceCategoryId());
            builder.field("reading", readingLog.getReading());
            builder.field("resetFlag", readingLog.getResetMeterFlag());
            builder.field("changeFlag", readingLog.getChangeMeterFlag());
            builder.field("meterName", meter.getName());
            builder.field("meterNumber", meter.getMeterNumber());
            builder.field("operatorName", operator.getNickName());
            builder.field("operateTime", readingLog.getOperateTime());
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
        List<EnergyMeterReadingLog> logs = readingLogProvider.listMeterReadingLogs(pageAnchor, pageSize);
        while (logs != null && logs.size() >= 0) {
            bulkUpdate(logs);
            pageAnchor += (logs.size() + 1);
            logs = readingLogProvider.listMeterReadingLogs(pageAnchor, pageSize);
        }
        this.optimize(1);
        this.refresh();
        LOGGER.info("sync for energy meter reading log ok");
    }

    @Override
    public SearchEnergyMeterReadingLogsResponse queryMeterReadingLogs(SearchEnergyMeterReadingLogsCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        QueryBuilder qb;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
                    .field("meterNumber", 5.0f)
                    .field("operatorName", 5.0f)
                    .field("meterName", 2.0f);
        }

        FilterBuilder fb = FilterBuilders.termFilter("communityId", cmd.getCommunityId());
        if (cmd.getMeterType() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("meterType", cmd.getMeterType()));
        }
        if (cmd.getBillCategoryId() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("billCategoryId", cmd.getBillCategoryId()));
        }
        if (cmd.getServiceCategoryId() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("serviceCategoryId", cmd.getServiceCategoryId()));
        }
        if (cmd.getStartTime() != null) {
            RangeFilterBuilder startTime = new RangeFilterBuilder("operateTime").gte(cmd.getStartTime());
            fb = FilterBuilders.andFilter(fb, startTime);
        }
        if (cmd.getEndTime() != null) {
            RangeFilterBuilder endTime = new RangeFilterBuilder("operateTime").gte(cmd.getEndTime());
            fb = FilterBuilders.andFilter(fb, endTime);
        }

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0L;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }

        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH)
                .setFrom(anchor.intValue() * pageSize)
                .setSize(pageSize + 1)
                .setQuery(qb);

        SearchResponse rsp = builder.execute().actionGet();
        List<EnergyMeterReadingLogDTO> logs = toMeterReadingLogDTOList(rsp);
        SearchEnergyMeterReadingLogsResponse response = new SearchEnergyMeterReadingLogsResponse();
        if (logs.size() > pageSize) {
            logs.remove(logs.size() - 1);
            response.setNextPageAnchor(logs.size());
        }
        response.setLogs(logs);
        return response;
    }

    private List<EnergyMeterReadingLogDTO> toMeterReadingLogDTOList(SearchResponse response) {
        List<EnergyMeterReadingLogDTO> dtoList = new ArrayList<>();
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            EnergyMeterReadingLogDTO dto = new EnergyMeterReadingLogDTO();
            Map<String, Object> source = hit.getSource();
            dto.setReading(BigDecimal.valueOf((Double) source.get("reading")));
            dto.setId(Long.valueOf(hit.getId()));
            dto.setMeterName((String) source.get("meterName"));
            dto.setResetMeterFlag(Byte.valueOf((String) source.get("resetFlag")));
            dto.setOperateTime(new Timestamp(Long.valueOf((String) source.get("operateTime"))));
            dto.setOperatorName((String) source.get("operatorName"));
            dto.setMeterNumber((String)source.get("meterNumber"));

            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public String getIndexType() {
        return SearchUtils.ENERGY_METER_READING_LOG;
    }
}
