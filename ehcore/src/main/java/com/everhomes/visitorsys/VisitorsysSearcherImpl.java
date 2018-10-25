// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.visitorsys.*;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.SearchUtils;
import com.everhomes.search.VisitorsysSearcher;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/5/4 17:24
 */
@Component
public class VisitorsysSearcherImpl extends AbstractElasticSearch implements VisitorsysSearcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(VisitorsysSearcherImpl.class);

    private static final String[] syncFields = {"namespaceId", "ownerType", "ownerId", "id",
            "visitorName", "followUpNumbers", "visitorPhone", "visitReasonId", "visitReason",
            "inviterId", "inviterName", "plannedVisitTime", "visitTime", "createTime", "visitStatus", "bookingStatus",
            "visitorType", "enterpriseId", "enterpriseName", "officeLocationId", "officeLocationName",
            "statsDate", "statsHour", "statsWeek","idNumber"};
    private static final DateTimeFormatter dateSF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    public VisitorSysVisitorProvider visitorSysVisitorProvider;
    @Autowired
    public OrganizationService organizationService;
    @Override
    public String getIndexType() {
        return SearchUtils.VISITORSYS;
    }

    @Override
    public void deleteById(Long visitorId) {
        this.deleteById(String.valueOf(visitorId));
    }

    private void feedDoc(VisitorSysVisitor object) {
        generateStatsInfo(object);
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            String idstring = null;
            for (String syncField : syncFields) {
                String methodName = "get" + syncField.substring(0, 1).toUpperCase() + syncField.substring(1, syncField.length());
                Method method = object.getClass().getMethod(methodName);
                Object result = method.invoke(object);
                if (result instanceof Timestamp) {
                    b.field(syncField, ((Timestamp) result).getTime());
                } else {
                    b.field(syncField, result);
                }
                if (syncField.equals("id")) {
                    idstring = result.toString();
                }
            }
            LOGGER.info(b.toString());
            feedDoc(String.valueOf(idstring), b);
        } catch (Exception ex) {
            LOGGER.error("Create VisitorSysCount {} error", object.toString(), ex);
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_SYNC_ES_FAIL,
                    "es Create VisitorSysCount " + object + " error");
        }
    }

    @Override
    public ListBookedVisitorsResponse searchVisitors(ListBookedVisitorParams params) {
        SearchResponse searchResponse = searchResponse(params, SearchType.QUERY_THEN_FETCH);
        ListBookedVisitorsResponse response = new ListBookedVisitorsResponse();
        List<BaseVisitorDTO> dtos = generateVisitorDtos(searchResponse);

        if (dtos.size() > params.getPageSize()) {
            response.setNextPageAnchor(params.getPageAnchor() + 1);
            dtos.remove(dtos.size() - 1);
        }

        response.setVisitorDtoList(dtos);

        return response;
    }

    /**
     * 数量和数据搜索
     *
     * @param params
     * @param searchType
     * @return
     */
    private SearchResponse searchResponse(ListBookedVisitorParams params, SearchType searchType) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());

        QueryBuilder qb = null;
        if (StringUtils.isEmpty(params.getKeyWords())) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            // es中超过10个字无法搜索出来结果，这里把关键词截断处理
            if (params.getKeyWords().length() > 10) {
                params.setKeyWords(params.getKeyWords().substring(0, 10));
            }
            qb = QueryBuilders.boolQuery().must(QueryBuilders.queryString("*" + params.getKeyWords() + "*").field("visitorName").field("visitorPhone").field("inviterName"));
        }
        List<FilterBuilder> fbList = new ArrayList();
        fbList.add(FilterBuilders.termFilter("namespaceId", params.getNamespaceId()));
        fbList.add(FilterBuilders.termFilter("ownerType", params.getOwnerType()));
        fbList.add(FilterBuilders.termFilter("ownerId", params.getOwnerId()));
        if (params.getVisitorType() != null) {
            fbList.add(FilterBuilders.termFilter("visitorType", params.getVisitorType()));
        }
        if (params.getVisitorPhone() != null) {
            fbList.add(FilterBuilders.termFilter("visitorPhone", params.getVisitorPhone()));
        }
        if (params.getOfficeLocationId() != null) {
             fbList.add(FilterBuilders.termFilter("officeLocationId", params.getOfficeLocationId()));
        }
        if (params.getVisitReasonId() != null) {
             fbList.add(FilterBuilders.termFilter("visitReasonId", params.getVisitReasonId()));
        }

        if (params.getEnterpriseId() != null) {
             fbList.add(FilterBuilders.termFilter("enterpriseId", params.getEnterpriseId()));
        }

        if(!StringUtils.isEmpty(params.getIdNumber())){
            fbList.add(FilterBuilders.termFilter("idNumber",params.getIdNumber()));
        }

        VisitorsysSearchFlagType visitorsysSearchFlagType = VisitorsysSearchFlagType.fromCode(params.getSearchFlag());
        FieldSortBuilder sort = SortBuilders.fieldSort("id").order(SortOrder.DESC);
        if (visitorsysSearchFlagType == VisitorsysSearchFlagType.BOOKING_MANAGEMENT) {
            fbList.add(FilterBuilders.termFilter("visitorType", VisitorsysVisitorType.BE_INVITED.getCode()));
            RangeFilterBuilder rf = new RangeFilterBuilder("plannedVisitTime");
            if (params.getStartPlannedVisitTime() != null) {
                rf.gt(params.getStartPlannedVisitTime());
            }
            if (params.getEndPlannedVisitTime() != null) {
                rf.lt(params.getEndPlannedVisitTime());
            }
            fbList.add(rf);
            if (params.getBookingStatus() != null) {
                 fbList.add(FilterBuilders.termFilter("bookingStatus", params.getBookingStatus()));
            } else {
                 fbList.add(FilterBuilders.notFilter(FilterBuilders.termFilter("bookingStatus", VisitorsysStatus.DELETED.getCode())));
                 fbList.add(FilterBuilders.notFilter(FilterBuilders.termFilter("bookingStatus", VisitorsysStatus.HIDDEN.getCode())));
            }
            sort = SortBuilders.fieldSort("plannedVisitTime").order(SortOrder.ASC);
        } else if (visitorsysSearchFlagType == VisitorsysSearchFlagType.VISITOR_MANAGEMENT) {
            RangeFilterBuilder rf = new RangeFilterBuilder("visitTime");
            if (params.getStartVisitTime() != null) {
                rf.gt(params.getStartVisitTime());
            }
            if (params.getEndVisitTime() != null) {
                rf.lt(params.getEndVisitTime());
            }
            fbList.add(rf);
            if (params.getVisitStatus() != null) {
                fbList.add(FilterBuilders.termFilter("visitStatus", params.getVisitStatus()));
            } else {
                fbList.add(FilterBuilders.notFilter(FilterBuilders.termFilter("visitStatus", VisitorsysStatus.DELETED.getCode())));
                fbList.add(FilterBuilders.notFilter(FilterBuilders.termFilter("visitStatus", VisitorsysStatus.NOT_VISIT.getCode())));
                fbList.add(FilterBuilders.notFilter(FilterBuilders.termFilter("visitStatus", VisitorsysStatus.HIDDEN.getCode())));
            }
            sort = SortBuilders.fieldSort("visitTime").order(SortOrder.DESC);
        } else if (visitorsysSearchFlagType == VisitorsysSearchFlagType.CLIENT_BOOKING) {
            fbList.add(FilterBuilders.termFilter("visitorType", VisitorsysVisitorType.BE_INVITED.getCode()));

            VisitorsysStatus bookingStatus = VisitorsysStatus.fromBookingCode(params.getBookingStatus());
            if (bookingStatus != null) {
                if (bookingStatus == VisitorsysStatus.NOT_VISIT) {
                    sort = SortBuilders.fieldSort("plannedVisitTime").order(SortOrder.ASC);
                }
                if (bookingStatus == VisitorsysStatus.HAS_VISITED) {
                    sort = SortBuilders.fieldSort("visitTime").order(SortOrder.DESC);
                }
                fbList.add(FilterBuilders.termFilter("bookingStatus", params.getBookingStatus()));
            } else {
                fbList.add(FilterBuilders.notFilter(FilterBuilders.termFilter("bookingStatus", VisitorsysStatus.DELETED.getCode())));
                fbList.add(FilterBuilders.notFilter(FilterBuilders.termFilter("bookingStatus", VisitorsysStatus.HIDDEN.getCode())));
                sort = SortBuilders.fieldSort("createTime").order(SortOrder.DESC);
            }
        }

        qb = QueryBuilders.filteredQuery(qb, FilterBuilders.andFilter(fbList.toArray(new FilterBuilder[fbList.size()])));
        builder.setSearchType(searchType);
        if (searchType == SearchType.QUERY_THEN_FETCH) {
            builder.setFrom(params.getPageAnchor().intValue() * params.getPageSize()).setSize(params.getPageSize() + 1);
        }
        builder.setQuery(qb);
        builder.addSort(sort);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("VisitorsysSearcherImpl query builder ：" + builder);

        SearchResponse searchResponse = builder.execute().actionGet();
        if (LOGGER.isTraceEnabled())
            LOGGER.trace("VisitorsysSearcherImpl query searchResponse ：" + searchResponse);
        return searchResponse;
    }


    private SearchResponse searchByAgg(ListBookedVisitorParams params, SearchType searchType) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());

        QueryBuilder qb = QueryBuilders.matchAllQuery();
        List<FilterBuilder> fbList = new ArrayList();
        fbList.add(FilterBuilders.termFilter("namespaceId", params.getNamespaceId()));
        fbList.add( FilterBuilders.termFilter("ownerType", params.getOwnerType()));
        fbList.add( FilterBuilders.termFilter("ownerId", params.getOwnerId()));
        fbList.add( FilterBuilders.notFilter(FilterBuilders.termFilter("visitStatus", VisitorsysStatus.DELETED.getCode())));
        fbList.add( FilterBuilders.notFilter(FilterBuilders.termFilter("visitStatus", VisitorsysStatus.HIDDEN.getCode())));


        TermsBuilder field = null;

        if (params.getStartVisitorCountTime() != null && params.getEndVisitorCountTime() != null) {
            fbList.add(
                    FilterBuilders
                            .rangeFilter("visitTime")
                            .from(params.getStartVisitTime())
                            .to(params.getEndVisitorCountTime()));
            field = AggregationBuilders.terms("statsDate").field("statsDate");
        }
        if (params.getStartDailyAvgVisitorTime() != null && params.getEndDailyAvgVisitorTime() != null) {
            fbList.add(
                    FilterBuilders
                            .rangeFilter("visitTime")
                            .from(params.getStartDailyAvgVisitorTime())
                            .to(params.getEndDailyAvgVisitorTime()));
            field = AggregationBuilders.terms("statsWeek").field("statsWeek");
        }
        if (params.getStartTimeShareAvgVisitorTime() != null && params.getEndTimeShareAvgVisitorTime() != null) {
            fbList.add(
                    FilterBuilders
                            .rangeFilter("visitTime")
                            .from(params.getStartTimeShareAvgVisitorTime())
                            .to(params.getEndTimeShareAvgVisitorTime()));
            field = AggregationBuilders.terms("statsHour").field("statsHour");
        }
        if (params.getStartRankingTime() != null && params.getEndRankingTime() != null) {
            fbList.add(
                    FilterBuilders
                            .rangeFilter("visitTime")
                            .from(params.getStartRankingTime())
                            .to(params.getEndRankingTime()));
            field = AggregationBuilders.terms("enterpriseName").field("enterpriseName").order(Terms.Order.count(false));
        }
        qb = QueryBuilders.filteredQuery(qb, FilterBuilders.andFilter(fbList.toArray(new FilterBuilder[fbList.size()])));
        builder.addAggregation(field);
        builder.setSearchType(searchType);
        if (searchType == SearchType.QUERY_THEN_FETCH) {
            builder.setFrom(0).setSize(Integer.MAX_VALUE);
        }
        builder.setQuery(qb);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("VisitorsysSearcherImpl query builder ：" + builder);

        SearchResponse searchResponse = builder.execute().actionGet();
        if (LOGGER.isTraceEnabled())
            LOGGER.trace("VisitorsysSearcherImpl query searchResponse ：" + searchResponse);
        return searchResponse;
    }


    @Override
    public GetStatisticsResponse getStatistics(GetStatisticsCommand cmd) {
        GetStatisticsResponse response = new GetStatisticsResponse();
        VisitorsysFlagType visitorsysFlagType = checkVisitorsysFlag(cmd.getStatsAllTimeCountFlag());
        if (visitorsysFlagType == VisitorsysFlagType.YES) {
            response.setVisitorCount(searchVisitorCount(cmd));
            response.setInvitedVisitorCount(searchInvitedVisitorCount(cmd));
        }
        if (cmd.getStartVisitorCountTime() != null && cmd.getEndVisitorCountTime() != null) {
            SearchResponse searchResponse = searchTimeVisitorCount(cmd);
            response.setTimeVisitorCount(searchResponse.getHits().getTotalHits());
            Map<String, Aggregation> asMap = searchResponse.getAggregations().getAsMap();
            response.setVisitorCountStatsList(generateTermsTolist(asMap.get("statsDate")));
        }
        if (cmd.getStartDailyAvgVisitorTime() != null && cmd.getEndDailyAvgVisitorTime() != null) {
            SearchResponse searchResponse = searchDailyAvgVisitor(cmd);
            Map<String, Aggregation> asMap = searchResponse.getAggregations().getAsMap();
            response.setDailyAvgStatsList(generateTermsTolist(asMap.get("statsWeek")));

        }
        if (cmd.getStartTimeShareAvgVisitorTime() != null && cmd.getEndTimeShareAvgVisitorTime() != null) {
            SearchResponse searchResponse = searchShareAvgVisitor(cmd);
            Map<String, Aggregation> asMap = searchResponse.getAggregations().getAsMap();
            response.setTimeShareAvgStatsList(generateTermsTolist(asMap.get("statsHour")));
        }
        if (cmd.getStartRankingTime() != null && cmd.getEndRankingTime() != null) {
            SearchResponse searchResponse = searchRankingCount(cmd);
            Map<String, Aggregation> asMap = searchResponse.getAggregations().getAsMap();
            response.setEnterpriseRankingList(generateTermsToRanklist(response,cmd.getRankingPageSize(),cmd.getRankingPageAnchor(),asMap.get("enterpriseName")));
        }
        return response;
    }

    private List<VisitorMostVisitedDTO> generateTermsToRanklist(GetStatisticsResponse response, Integer rankingPageSize,
                                                                Long rankingPageAnchor, Aggregation terms) {
        if(terms==null){
            return null;
        }
        Integer pageSize = rankingPageSize==null?20:rankingPageSize;
        Integer pageAnchor = rankingPageAnchor == null ? 0 : Integer.valueOf(String.valueOf(rankingPageAnchor));
        if (terms instanceof StringTerms) {
            StringTerms stringTerms = (StringTerms) terms;
            if (stringTerms.getBuckets() == null) {
                return null;
            }
            List<Terms.Bucket> buckets = stringTerms.getBuckets();
            int start = pageAnchor*pageSize;
            int end = pageAnchor*pageSize+pageSize;
            if(buckets.size()<=start) {
                return null;
            }else if(buckets.size()<end && buckets.size()>start){
                buckets = buckets.subList(start,buckets.size());
            }else {
                buckets = buckets.subList(start, end);
                response.setNextRankingPageAnchor(Long.valueOf(pageAnchor+1));
            }
            List<VisitorMostVisitedDTO> list = new ArrayList<>();
            for (int i = 0; i < buckets.size(); i++) {
                Terms.Bucket r = buckets.get(i);
                VisitorMostVisitedDTO dto = new VisitorMostVisitedDTO();
                dto.setRanking(start+i);
                dto.setEnterpriseName(r.getKey());
                dto.setVisitorCount(r.getDocCount());
                list.add(dto);
            }
            return list;
        }
        return null;

    }

    /**
     * 按时间区间统计最常被访问的企业
     * 1.可按“过去7天”、“过去30天”及自定义区间筛选。显示不同企业在一定时间内的访客量。
     * @param cmd
     * @return
     */
    private SearchResponse searchRankingCount(GetStatisticsCommand cmd) {
        ListBookedVisitorParams params = generateBaseParams(cmd);
        params.setStartRankingTime(cmd.getStartRankingTime());
        params.setEndRankingTime(cmd.getEndRankingTime());
        SearchResponse searchResponse = searchByAgg(params, SearchType.QUERY_THEN_FETCH);
        return searchResponse;
    }

    /**
     * 按时间区间统计分时平均访客
     * 1.可按“过去7天”、“过去30天”及自定义区间筛选。显示不同小时的平均数据量。
     * @param cmd
     * @return
     */
    private SearchResponse searchShareAvgVisitor(GetStatisticsCommand cmd) {
        ListBookedVisitorParams params = generateBaseParams(cmd);
        params.setStartTimeShareAvgVisitorTime(cmd.getStartTimeShareAvgVisitorTime());
        params.setEndTimeShareAvgVisitorTime(cmd.getEndTimeShareAvgVisitorTime());
        SearchResponse searchResponse = searchByAgg(params, SearchType.QUERY_THEN_FETCH);
        return searchResponse;
    }

    /**
     * 按时间区间统计日平均访客
     * 1.可按“过去7天”、“过去30天”及自定义区间筛选。显示每个day的平均数据量。（day = 周一、周二、周三...）
     * @param cmd
     * @return
     */
    private SearchResponse searchDailyAvgVisitor(GetStatisticsCommand cmd) {
        ListBookedVisitorParams params = generateBaseParams(cmd);
        params.setStartDailyAvgVisitorTime(cmd.getStartDailyAvgVisitorTime());
        params.setEndDailyAvgVisitorTime(cmd.getEndDailyAvgVisitorTime());
        SearchResponse searchResponse = searchByAgg(params, SearchType.QUERY_THEN_FETCH);
        return searchResponse;
    }

    private List<BaseVisitorStatsDTO> generateTermsTolist(Aggregation terms) {
        if (terms == null) {
            return null;
        }
        if (terms instanceof StringTerms) {
            StringTerms stringTerms = (StringTerms) terms;
            if (stringTerms.getBuckets() == null) {
                return null;
            }
            return stringTerms.getBuckets().stream().map(r -> {
                BaseVisitorStatsDTO dto = new BaseVisitorStatsDTO();
                dto.setTimePoint(r.getKey());
                dto.setTimePointCount(r.getDocCount());
                return dto;
            }).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 按时间区间统计访客量
     * 可按“过去7天”、“过去30天”及自定义区间筛选。显示每个date的数据量。
     * @param cmd
     * @return
     */
    private SearchResponse searchTimeVisitorCount(GetStatisticsCommand cmd) {
        ListBookedVisitorParams params = generateBaseParams(cmd);
        params.setStartVisitorCountTime(cmd.getStartVisitorCountTime());
        params.setEndVisitorCountTime(cmd.getEndVisitorCountTime());
        SearchResponse searchResponse = searchByAgg(params, SearchType.QUERY_THEN_FETCH);
        return searchResponse;
    }

    /**
     * cmd中的基本信息转到params中
     * @param cmd
     * @return
     */
    private ListBookedVisitorParams generateBaseParams(GetStatisticsCommand cmd) {
        ListBookedVisitorParams params = new ListBookedVisitorParams();
        params.setOwnerId(cmd.getOwnerId());
        params.setOwnerType(cmd.getOwnerType());
        params.setNamespaceId(cmd.getNamespaceId());
        return params;
    }

    /**
     * 查询预约访客数量
     * @param cmd
     * @return
     */
    private Long searchInvitedVisitorCount(GetStatisticsCommand cmd) {
        ListBookedVisitorParams params = ConvertHelper.convert(cmd,ListBookedVisitorParams.class);
        params.setSearchFlag(VisitorsysSearchFlagType.BOOKING_MANAGEMENT.getCode());
        params.setVisitorType(VisitorsysVisitorType.BE_INVITED.getCode());
        return searchResponse(params,SearchType.COUNT).getHits().getTotalHits();
    }

    /**
     * 查询所有访客数量
     * @param cmd
     * @return
     */
    private Long searchVisitorCount(GetStatisticsCommand cmd) {
        ListBookedVisitorParams params = ConvertHelper.convert(cmd,ListBookedVisitorParams.class);
        params.setSearchFlag(VisitorsysSearchFlagType.VISITOR_MANAGEMENT.getCode());
        return searchResponse(params,SearchType.COUNT).getHits().getTotalHits();
    }

    /**
     * 检查flag参数
     * @param flag
     * @return
     */
    private VisitorsysFlagType checkVisitorsysFlag(Byte flag) {
        VisitorsysFlagType flagType = VisitorsysFlagType.fromCode(flag);
        if(flagType ==null){
            throw RuntimeErrorException.errorWith(VisitorsysConstant.SCOPE, VisitorsysConstant.ERROR_INVALD_PARAMS, "unknow visitor flag = "+flag);
        }
        return flagType;
    }

    @Override
    public void syncVisitorsFromDb(Integer namespaceId) {
        int pageSize = 200;
        this.deleteAll();

        ListBookedVisitorParams params = new ListBookedVisitorParams();
        params.setSearchFlag(VisitorsysSearchFlagType.SYNCHRONIZATION.getCode());
        params.setNamespaceId(namespaceId);
        params.setPageSize(pageSize);
        while(true){
            List<VisitorSysVisitor> list = visitorSysVisitorProvider.listVisitorSysVisitor(params);
            for (VisitorSysVisitor visitorSysVisitor : list) {
                feedDoc(visitorSysVisitor);
            }
            if(list.size()<pageSize){
                break;
            }
            params.setPageAnchor(list.get(list.size()-1).getId());
        }
    }

    private void generateStatsInfo(VisitorSysVisitor visitorSysVisitor) {
        if(visitorSysVisitor!=null && visitorSysVisitor.getVisitTime()!=null){
            visitorSysVisitor.setStatsDate(visitorSysVisitor.getVisitTime().toLocalDateTime().format(dateSF));
            Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(visitorSysVisitor.getVisitTime().getTime());
            visitorSysVisitor.setStatsHour(instance.get(Calendar.HOUR_OF_DAY));
            visitorSysVisitor.setStatsWeek(instance.get(Calendar.DAY_OF_WEEK));
           // VisitorsysOwnerType visitorsysOwnerType = VisitorsysOwnerType.fromCode(visitorSysVisitor.getOwnerType());
           // if(visitorsysOwnerType==VisitorsysOwnerType.ENTERPRISE) {
           //     Long communityId = organizationService.getOrganizationActiveCommunityId(visitorSysVisitor.getOwnerId());
            //    visitorSysVisitor.setCommunityId(communityId);
           // }
        }
    }

    @Override
    public void syncVisitor(Integer namespaceId,Long visitorId) {
        VisitorSysVisitor visitor = visitorSysVisitorProvider.findVisitorSysVisitorById(namespaceId, visitorId);
        feedDoc(visitor);
    }

    @Override
    public void syncVisitor(VisitorSysVisitor visitor) {
        feedDoc(visitor);
    }
    private List<BaseVisitorDTO> generateVisitorDtos(SearchResponse rsp) {
        List<BaseVisitorDTO> dtos = new ArrayList<>();
        SearchHit[] docs = rsp.getHits().getHits();
        for (SearchHit sd : docs) {
            try {
                BaseVisitorDTO dto = new BaseVisitorDTO();
                String id = sd.getId();
                dto.setId(Long.parseLong(id));
                Map<String, Object> source = sd.getSource();
                dto.setNamespaceId(Integer.valueOf(source.get("namespaceId").toString()));
                dto.setOwnerType(String.valueOf(source.get("ownerType")));
                dto.setOwnerId(Long.valueOf(source.get("ownerId").toString()));
                Object visitorName = source.get("visitorName");
                if(visitorName!=null) {
                    dto.setVisitorName(String.valueOf(visitorName));
                }
                Object followUpNumbers = source.get("followUpNumbers");
                if(followUpNumbers!=null){
                    dto.setFollowUpNumbers(Long.valueOf(String.valueOf(followUpNumbers)));
                }

                Object visitorPhone = source.get("visitorPhone");
                if(visitorPhone!=null){
                    dto.setVisitorPhone(String.valueOf(visitorPhone));
                }
                Object visitReasonId = source.get("visitReasonId");
                if(visitReasonId!=null){
                    dto.setVisitReasonId(Long.valueOf(visitReasonId.toString()));
                }
                Object visitReason = source.get("visitReason");
                if(visitReason!=null){
                    dto.setVisitReason(String.valueOf(visitReason));
                }
                Object inviterId = source.get("inviterId");
                if(inviterId!=null){
                    dto.setInviterId(Long.valueOf(inviterId.toString()));
                }
                Object inviterName = source.get("inviterName");
                if(inviterName!=null){
                    dto.setInviterName(String.valueOf(inviterName));
                }
                Object plannedVisitTime = source.get("plannedVisitTime");
                if(plannedVisitTime!=null){
                    dto.setPlannedVisitTime(new Timestamp(Long.valueOf(plannedVisitTime.toString())));
                }
                Object visitTime = source.get("visitTime");
                if(visitTime!=null){
                    dto.setVisitTime(new Timestamp(Long.valueOf(visitTime.toString())));
                }
                Object createTime = source.get("createTime");
                if(createTime!=null){
                    dto.setCreateTime(new Timestamp(Long.valueOf(createTime.toString())));
                }
                Object visitStatus = source.get("visitStatus");
                if(visitStatus!=null){
                    dto.setVisitStatus(Byte.valueOf(visitStatus.toString()));
                }
                Object bookingStatus = source.get("bookingStatus");
                if(bookingStatus!=null){
                    dto.setBookingStatus(Byte.valueOf(bookingStatus.toString()));
                }
                Object visitorType = source.get("visitorType");
                if(visitorType!=null){
                    dto.setVisitorType(Byte.valueOf(visitorType.toString()));
                }
                Object enterpriseId = source.get("enterpriseId");
                if(enterpriseId!=null){
                    dto.setEnterpriseId(Long.valueOf(enterpriseId.toString()));
                }
                Object enterpriseName = source.get("enterpriseName");
                if(enterpriseName!=null){
                    dto.setEnterpriseName(String.valueOf(enterpriseName));
                }
                Object officeLocationId = source.get("officeLocationId");
                if(officeLocationId!=null){
                    dto.setOfficeLocationId(Long.valueOf(officeLocationId.toString()));
                }
                Object officeLocationName = source.get("officeLocationName");
                if(officeLocationName!=null){
                    dto.setOfficeLocationName(String.valueOf(officeLocationName));}
                Object idNumber = source.get("idNumber");
                if(null != idNumber)
                    dto.setIdNumber(String.valueOf(idNumber));

                dtos.add(dto);
            }
            catch(Exception ex) {
                LOGGER.info("generateVisitorDtos error " + ex.getMessage());
            }
        }

        return dtos;
    }

}
