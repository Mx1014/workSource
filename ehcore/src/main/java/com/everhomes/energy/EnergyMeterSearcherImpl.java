package com.everhomes.energy;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.repeat.RepeatService;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.energy.EnergyLocalStringCode;
import com.everhomes.rest.energy.EnergyMeterAddressDTO;
import com.everhomes.rest.energy.EnergyMeterDTO;
import com.everhomes.rest.energy.SearchEnergyMeterCommand;
import com.everhomes.rest.energy.SearchEnergyMeterResponse;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.EnergyMeterSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.mysql.jdbc.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.AndFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermFilterBuilder;
import org.elasticsearch.search.SearchHit;
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
import java.util.Map;
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

    @Autowired
    private LocaleStringService localeStringService;

    @Autowired
    private EnergyConsumptionService energyConsumptionService;

    @Autowired
    private EnergyMeterAddressProvider energyMeterAddressProvider;

    @Autowired
    private EnergyPlanProvider energyPlanProvider;

    @Autowired
    private RepeatService repeatService;

    @Autowired
    private EnergyMeterReadingLogProvider meterReadingLogProvider;

    @Autowired
    private UserProvider userProvider;

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<EnergyMeter> meters) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (EnergyMeter meter : meters) {
            XContentBuilder source = createDoc(meter);
            if (null != source) {
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

            List<EnergyMeterAddress> existAddress = energyMeterAddressProvider.listByMeterId(meter.getId());
            List<String> addressList = new ArrayList<>();
            List<String> buildingList = new ArrayList<>();
            if (existAddress != null && existAddress.size() > 0) {
                existAddress.forEach((r) -> {
                    addressList.add(r.getAddressId().toString());
                    buildingList.add(r.getBuildingId().toString());
                });
                if (!buildingList.isEmpty()) {
                    builder.field("buildingId", String.join("|", buildingList));
                }
                if (!addressList.isEmpty()) {
                    builder.field("addressId", String.join("|", addressList));
                }
            }

            EnergyMeterReadingLog lastReading = meterReadingLogProvider.findLastReadingLogByMeterId(meter.getNamespaceId(), meter.getId());
            if (lastReading != null) {
                User operator = userProvider.findUserById(lastReading.getOperatorId());
                if (operator != null) {
                    builder.field("operatorName", operator.getNickName());
                }
            }
            List<PlanMeter> maps = energyPlanProvider.listByEnergyMeter(meter.getId());
            Boolean assignFlag = false;
            if (maps != null && maps.size() > 0) {
                List<String> planNames = new ArrayList<>();
                for (PlanMeter map : maps) {
                    if (repeatService.repeatSettingStillWork(map.getRepeatSettingId())) {
                        EnergyPlan plan = energyPlanProvider.findEnergyPlanById(map.getPlanId());
                        if (plan != null && CommonStatus.ACTIVE.equals(CommonStatus.fromCode(plan.getStatus()))) {
                            planNames.add(plan.getName());
                            assignFlag = true;
                        }
                    }
                }
                builder.array("assignPlan", planNames);
            }
            if (assignFlag) {
                builder.field("assignFlag", 1);
            } else {
                builder.field("assignFlag", 0);
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
        List<EnergyMeter> meters = meterProvider.listEnergyMeters(pageAnchor, pageSize);
        while (meters != null && meters.size() > 0) {
            bulkUpdate(meters);
            pageAnchor = meters.get(meters.size() - 1).getId() + 1;
            meters = meterProvider.listEnergyMeters(pageAnchor, pageSize);
        }
        this.optimize(1);
        this.refresh();
        LOGGER.info("sync for energy meter ok");
    }

    private SearchResponse query(SearchEnergyMeterCommand cmd) {        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
    	QueryBuilder qb = null;
    	List<FilterBuilder> filterBuilders = new ArrayList<>();
    	if (!StringUtils.isNullOrEmpty(cmd.getMeterNumber())) {
        	//支持模糊搜索 --by djm 缺陷 #34940
            String pattern = "*" + cmd.getMeterNumber() + "*";
            qb = QueryBuilders.boolQuery()
    					.must(QueryBuilders.wildcardQuery("meterNumber", pattern));
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("meterNumber");
        }
    	
    	//表计名称 支持模糊搜索  by ycx 缺陷#39664
    	if (!StringUtils.isNullOrEmpty(cmd.getKeyword())) {
    		String pattern = "*" + cmd.getKeyword() + "*";
    		if(qb != null) {
    			qb = QueryBuilders.boolQuery()
    					.must(qb)
    					.must(QueryBuilders.wildcardQuery("name", pattern));
    		}else {
    			qb = QueryBuilders.boolQuery()
    					.must(QueryBuilders.wildcardQuery("name", pattern));
    		}
    	}
        
        if (cmd.getAddressId() != null) {
            MultiMatchQueryBuilder addressId = QueryBuilders.multiMatchQuery(cmd.getAddressId(), "addressId");
            qb = QueryBuilders.boolQuery().must(qb).must(addressId);
        }
        if (cmd.getBuildingId() != null) {
            MultiMatchQueryBuilder buildingId = QueryBuilders.multiMatchQuery(cmd.getBuildingId(), "buildingId");
            qb = QueryBuilders.boolQuery().must(qb).must(buildingId);
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

//        if (cmd.getBuildingId() != null) {
//            TermFilterBuilder buildingIdFilter = FilterBuilders.termFilter("buildingId", cmd.getBuildingId());
//            filterBuilders.add(buildingIdFilter);
//        }
//
//        if (cmd.getAddressId() != null) {
//            TermFilterBuilder addressIdFilter = FilterBuilders.termFilter("addressId", cmd.getAddressId());
//            filterBuilders.add(addressIdFilter);
//        }

        if (cmd.getAssignFlag() != null) {
            TermFilterBuilder assignFlagFilter = FilterBuilders.termFilter("assignFlag", cmd.getAssignFlag());
            filterBuilders.add(assignFlagFilter);
        }

        if (cmd.getPlanName() != null) {
            FilterBuilder planFilter = FilterBuilders.termFilter("assignPlan", cmd.getPlanName());
            filterBuilders.add(planFilter);
        }

        if (org.apache.commons.lang.StringUtils.isNotEmpty(cmd.getOperatorName())) {
            FilterBuilder operatorNameFilter = FilterBuilders.termFilter("operatorName", cmd.getOperatorName());
            filterBuilders.add(operatorNameFilter);
        }

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0L;
        if (cmd.getPageAnchor() != null) {
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

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query energy meters, builder={}", builder);
        }

        SearchResponse rsp = builder.execute().actionGet();

        return rsp;
    }

    @Override
    public List<Long> getMeterIds(SearchEnergyMeterCommand cmd) {
        SearchResponse rsp = query(cmd);
        List<Long> ids = getIds(rsp);
        return ids;
    }

    @Override
    public SearchEnergyMeterResponse queryMeters(SearchEnergyMeterCommand cmd) {
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0L;
        if (cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }

        List<Long> ids = getMeterIds(cmd);
        SearchEnergyMeterResponse response = new SearchEnergyMeterResponse();
        if (ids.size() > pageSize) {
            ids.remove(ids.size() - 1);
            response.setNextPageAnchor(anchor + ids.size());
        }
        List<EnergyMeter> meters = meterProvider.listByIds((cmd.getNamespaceId() == null ? UserContext.getCurrentNamespaceId() : cmd.getNamespaceId()), ids);
        response.setMeters(meters.stream().map(meter -> energyConsumptionService.toEnergyMeterDTO(meter, cmd.getNamespaceId())).collect(Collectors.toList()));
        return response;
    }

    @Override
    public SearchEnergyMeterResponse querySimpleEnergyMeters(SearchEnergyMeterCommand cmd) {
        SearchResponse rsp = query(cmd);
        SearchEnergyMeterResponse response = new SearchEnergyMeterResponse();
        List<EnergyMeterDTO> meters = getSimpleDTOs(rsp);
        response.setMeters(meters);
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

    private List<EnergyMeterDTO> getSimpleDTOs(SearchResponse rsp) {
        List<EnergyMeterDTO> dtos = new ArrayList<>();
        SearchHit[] docs = rsp.getHits().getHits();
        for (SearchHit sd : docs) {
            try {
                EnergyMeterDTO dto = new EnergyMeterDTO();
                dto.setId(Long.parseLong(sd.getId()));
                Map<String, Object> source = sd.getSource();
                dto.setName(String.valueOf(source.get("name")));
                dto.setMeterNumber(String.valueOf(source.get("meterNumber")));

                List<EnergyMeterAddressDTO> addresses = new ArrayList<>();

                addresses = populateEnergyMeterAddresses(dto.getId());
                dto.setAddresses(addresses);

                // 表的类型
                String meterType = localeStringService.getLocalizedString(EnergyLocalStringCode.SCOPE_METER_TYPE, String.valueOf(source.get("meterType")), currLocale(), "");
                dto.setMeterType(meterType);

                // 表的状态
                String meterStatus = localeStringService.getLocalizedString(EnergyLocalStringCode.SCOPE_METER_STATUS, String.valueOf(source.get("status")), currLocale(), "");
                dto.setStatus(meterStatus);
                dtos.add(dto);

            } catch (Exception ex) {
                LOGGER.info("getTopicIds error " + ex.getMessage());
            }
        }

        return dtos;
    }
    private List<EnergyMeterAddressDTO> populateEnergyMeterAddresses(Long meterId) {
        Map<Long, EnergyMeterAddress> existAddress = energyMeterAddressProvider.findByMeterId(meterId);
        List<EnergyMeterAddressDTO> dtos = new ArrayList<>();
        if(existAddress != null && existAddress.size() > 0) {
            existAddress.forEach((id, meterAddress) -> {
                dtos.add(ConvertHelper.convert(meterAddress, EnergyMeterAddressDTO.class));
            });
        }
        return dtos;
    }

    private String currLocale() {
        return UserContext.current().getUser().getLocale();
    }
}
