package com.everhomes.energy;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.energy.SearchEnergyMeterCommand;
import com.everhomes.rest.energy.SearchEnergyMeterResponse;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.EnergyMeterSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.mysql.jdbc.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2016/10/31.
 */
@Component
public class EnergyMeterSearcherImpl extends AbstractElasticSearch implements EnergyMeterSearcher {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private EnergyMeterProvider meterProvider;

    // @Autowired
    // private LocaleStringService localeStringService;

    @Autowired
    private EnergyConsumptionService energyConsumptionService;

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<EnergyMeter> meters) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (EnergyMeter meter : meters) {
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
    public void feedDoc(EnergyMeter meter) {
        XContentBuilder source = createDoc(meter);
        feedDoc(meter.getId().toString(), source);
    }

    private XContentBuilder createDoc(EnergyMeter meter) {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            builder.field("communityId", meter.getCommunityId());
            builder.field("meterType", meter.getMeterType());
            builder.field("billCategoryId", meter.getBillCategoryId());
            builder.field("serviceCategoryId", meter.getServiceCategoryId());
            builder.field("status", meter.getStatus());
            builder.field("name", meter.getName());
            builder.field("meterNumber", meter.getMeterNumber());
            builder.field("createTime", meter.getCreateTime().getTime());
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
        List<EnergyMeter> meters = meterProvider.listEnergyMeters(pageAnchor, pageSize);
        while (meters != null && meters.size() > 0) {
            bulkUpdate(meters);
            pageAnchor += (meters.size() + 1);
            meters = meterProvider.listEnergyMeters(pageAnchor, pageSize);
        }
        this.optimize(1);
        this.refresh();
        LOGGER.info("sync for energy meter ok");
    }

    @Override
    public List<Long> getMeterIds(SearchEnergyMeterCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        QueryBuilder qb;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
//                    .field("meterNumber", 5.0f)
                    .field("name", 5.0f);
        }
        /*FilterBuilder fb = new AndFilterBuilder();
        if (cmd.getCommunityId() != null) {
            fb = FilterBuilders.termFilter("communityId", cmd.getCommunityId());
        }
        if (cmd.getMeterType() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("meterType", cmd.getMeterType()));
        }
        if (cmd.getBillCategoryId() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("billCategoryId", cmd.getBillCategoryId()));
        }
        if (cmd.getServiceCategoryId() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("serviceCategoryId", cmd.getServiceCategoryId()));
        }
        if (cmd.getStatus() != null) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("status", cmd.getStatus()));
        }
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0L;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }*/
        List<FilterBuilder> filterBuilders = new ArrayList<>();
        //编号精确搜索 by xiongying20170525
        if (!StringUtils.isNullOrEmpty(cmd.getMeterNumber())) {
            TermFilterBuilder meterNumberTermFilter = FilterBuilders.termFilter("meterNumber", cmd.getMeterNumber());
            filterBuilders.add(meterNumberTermFilter);
        }
        if (cmd.getCommunityId() != null) {
            TermFilterBuilder communityIdTermFilter = FilterBuilders.termFilter("communityId", cmd.getCommunityId());
            filterBuilders.add(communityIdTermFilter);
        }
        if (cmd.getMeterType() != null) {
            TermFilterBuilder meterTypeTermFilter = FilterBuilders.termFilter("meterType", cmd.getMeterType());
            filterBuilders.add(meterTypeTermFilter);
        }
        if (cmd.getBillCategoryId() != null) {
            TermFilterBuilder billCategoryIdFilter = FilterBuilders.termFilter("billCategoryId", cmd.getBillCategoryId());
            filterBuilders.add(billCategoryIdFilter);
        }
        if (cmd.getServiceCategoryId() != null) {
            TermFilterBuilder serviceCategoryIdFilter = FilterBuilders.termFilter("serviceCategoryId", cmd.getServiceCategoryId());
            filterBuilders.add(serviceCategoryIdFilter);
        }
        if (cmd.getStatus() != null) {
            TermFilterBuilder statusFilter = FilterBuilders.termFilter("status", cmd.getStatus());
            filterBuilders.add(statusFilter);
        }
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0L;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }

        AndFilterBuilder fb = FilterBuilders.andFilter(filterBuilders.toArray(new FilterBuilder[filterBuilders.size()]));

        // FieldSortBuilder statusSort = SortBuilders.fieldSort("status").order(SortOrder.ASC);
        FieldSortBuilder createTimeSort = SortBuilders.fieldSort("createTime").order(SortOrder.DESC);

        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH)
                .setFrom(anchor.intValue())
                .setSize(pageSize + 1)
                // .addSort(statusSort) // this sort will conflict, elasticsearch bug: https://github.com/elastic/elasticsearch/issues/8688
                .addSort(createTimeSort)
                .setQuery(qb);

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query energy meters, builder={}", builder);
        }

        SearchResponse rsp = builder.execute().actionGet();
        List<Long> ids = getIds(rsp);

        return ids;
    }

    @Override
    public SearchEnergyMeterResponse queryMeters(SearchEnergyMeterCommand cmd) {
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0L;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }

        List<Long> ids = getMeterIds(cmd);
        SearchEnergyMeterResponse response = new SearchEnergyMeterResponse();
        if (ids.size() > pageSize) {
            ids.remove(ids.size() - 1);
            response.setNextPageAnchor(anchor + ids.size());
        }
        List<EnergyMeter> meters = meterProvider.listByIds((cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId()), ids);
        response.setMeters(meters.stream().map(meter -> energyConsumptionService.toEnergyMeterDTO(meter,cmd.getNamespaceId())).collect(Collectors.toList()));
        return response;
    }

    /*private EnergyMeterDTO toMeterDTO(EnergyMeter meter) {
        EnergyMeterDTO dto = ConvertHelper.convert(meter, EnergyMeterDTO.class);
        String meterStatusLocale = localeStringService.getLocalizedString(EnergyLocalStringCode.SCOPE_METER_STATUS,
                String.valueOf(meter.getStatus()), UserContext.current().getUser().getLocale(), "");
        dto.setStatus(meterStatusLocale);
        return dto;
    }*/

    @Override
    public String getIndexType() {
        return SearchUtils.ENERGY_METER;
    }
}
