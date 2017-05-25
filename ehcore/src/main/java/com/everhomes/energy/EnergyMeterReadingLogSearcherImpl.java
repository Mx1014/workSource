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
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
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

    // @Autowired
    // private EnergyMeterChangeLogProvider changeLogProvider;

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
            EnergyMeter meter = meterProvider.findById(readingLog.getNamespaceId(), readingLog.getMeterId());
            User operator = userProvider.findUserById(readingLog.getOperatorId());

            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            builder.field("communityId", readingLog.getCommunityId());
            builder.field("reading", readingLog.getReading());
            BigDecimal oldMeterReading = readingLog.getOldMeterReading();
            if (oldMeterReading != null) {// 换表会有旧表读数
                builder.field("oldReading", oldMeterReading);
            }
            builder.field("resetFlag", readingLog.getResetMeterFlag());
            builder.field("changeFlag", readingLog.getChangeMeterFlag());
            builder.field("operateTime", readingLog.getOperateTime().getTime());

            if(meter != null) {
                builder.field("meterId", meter.getId());
                builder.field("meterType", meter.getMeterType());
                builder.field("billCategoryId", meter.getBillCategoryId());
                builder.field("serviceCategoryId", meter.getServiceCategoryId());
                builder.field("meterName", meter.getName());
                builder.field("meterNumber", meter.getMeterNumber());
            }

            if(operator != null) {
                builder.field("operatorName", operator.getNickName());
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
        List<EnergyMeterReadingLog> logs = readingLogProvider.listMeterReadingLogs(pageAnchor, pageSize);
        while (logs != null && logs.size() > 0) {
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
//                    .field("meterNumber", 5.0f)
                    .field("meterName", 5.0f);
        }

        if (StringUtils.isNotEmpty(cmd.getOperatorName())) {
            MultiMatchQueryBuilder operatorNameQuery = QueryBuilders.multiMatchQuery(cmd.getOperatorName(), "operatorName");
            qb = QueryBuilders.boolQuery().must(qb).must(operatorNameQuery);
        }

        List<FilterBuilder> filterBuilders = new ArrayList<>();
        if (StringUtils.isNotEmpty(cmd.getMeterNumber())) {
            FilterBuilder meterNumberTerm = FilterBuilders.termFilter("meterNumber", cmd.getMeterNumber().toLowerCase());
            filterBuilders.add(meterNumberTerm);
        }
        if (cmd.getCommunityId() != null) {
            FilterBuilder communityIdTerm = FilterBuilders.termFilter("communityId", cmd.getCommunityId());
            filterBuilders.add(communityIdTerm);
        }
        if (cmd.getMeterType() != null) {
            TermFilterBuilder meterTypeTerm = FilterBuilders.termFilter("meterType", cmd.getMeterType());
            filterBuilders.add(meterTypeTerm);
        }
        if (cmd.getBillCategoryId() != null) {
            TermFilterBuilder billCategoryIdTerm = FilterBuilders.termFilter("billCategoryId", cmd.getBillCategoryId());
            filterBuilders.add(billCategoryIdTerm);
        }
        if (cmd.getServiceCategoryId() != null) {
            TermFilterBuilder serviceCategoryIdTerm = FilterBuilders.termFilter("serviceCategoryId", cmd.getServiceCategoryId());
            filterBuilders.add(serviceCategoryIdTerm);
        }
        if (cmd.getMeterId() != null) {
            TermFilterBuilder meterIdTerm = FilterBuilders.termFilter("meterId", cmd.getMeterId());
            filterBuilders.add(meterIdTerm);
        }
        RangeFilterBuilder rangeTimeTerm = new RangeFilterBuilder("operateTime");
        if (cmd.getStartTime() != null) {
            rangeTimeTerm.gte(cmd.getStartTime());
        }
        if (cmd.getEndTime() != null) {
            rangeTimeTerm.lte(cmd.getEndTime());
        }
        if (cmd.getEndTime() != null || cmd.getEndTime() != null) {
            filterBuilders.add(rangeTimeTerm);
        }

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0L;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }

        FieldSortBuilder sort = SortBuilders.fieldSort("operateTime").order(SortOrder.DESC);

        AndFilterBuilder fb = FilterBuilders.andFilter(filterBuilders.toArray(new FilterBuilder[filterBuilders.size()]));

        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH)
                .setFrom(anchor.intValue())
                .setSize(pageSize + 1)
                .addSort(sort)
                .setQuery(qb);

        LOGGER.debug("energy meter reading log query = {}", builder.toString());

        SearchResponse rsp = builder.execute().actionGet();
        List<EnergyMeterReadingLogDTO> logs = toMeterReadingLogDTOList(rsp);
        SearchEnergyMeterReadingLogsResponse response = new SearchEnergyMeterReadingLogsResponse();
        if (logs.size() > pageSize) {
            logs.remove(logs.size() - 1);
            response.setNextPageAnchor(anchor + logs.size());
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
            Object oldReading = source.get("oldReading");
            if (oldReading != null) {
                dto.setOldReading(BigDecimal.valueOf((Double) oldReading));
            }
            dto.setId(Long.valueOf(hit.getId()));
            dto.setMeterName((String) source.get("meterName"));
            Object resetFlag = source.get("resetFlag");
            dto.setResetMeterFlag(resetFlag != null ? Byte.valueOf(resetFlag.toString()) : null);
            Object changeFlag = source.get("changeFlag");
            dto.setChangeMeterFlag(changeFlag != null ? Byte.valueOf(changeFlag.toString()) : null);
            Object operateTime = source.get("operateTime");
            dto.setOperateTime(operateTime != null ? new Timestamp(Long.valueOf(operateTime.toString())) : null);
            dto.setOperatorName((String)source.get("operatorName"));
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
