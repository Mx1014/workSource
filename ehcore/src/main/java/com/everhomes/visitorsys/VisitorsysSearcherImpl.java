// @formatter:off
package com.everhomes.visitorsys;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.visitorsys.*;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.SearchUtils;
import com.everhomes.search.VisitorsysSearcher;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/5/4 17:24
 */
@Component
public class VisitorsysSearcherImpl extends AbstractElasticSearch implements VisitorsysSearcher{

    private static final Logger LOGGER = LoggerFactory.getLogger(VisitorsysSearcherImpl.class);

    private static final String[] syncFields = {"namespaceId","ownerType","ownerId","id",
            "visitorName","followUpNumbers","visitorPhone","visitReasonId","visitReason",
            "inviterId","inviterName","plannedVisitTime","visitTime","createTime","visitStatus","bookingStatus",
            "visitorType","enterpriseId","enterpriseName","officeLocationId","officeLocationName"};
    @Autowired
    public VisitorSysVisitorProvider visitorSysVisitorProvider;

    @Override
    public String getIndexType() {
        return SearchUtils.VISITORSYS;
    }

    @Override
    public void deleteById(Long visitorId) {
        this.deleteById(String.valueOf(visitorId));
    }

//    private void feedDoc(BaseVisitorDTO baseVisitorDTO) {
//        try {
//            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
//            b.field("namespaceId",baseVisitorDTO.getNamespaceId());
//            b.field("ownerType",baseVisitorDTO.getOwnerType());
//            b.field("ownerId",baseVisitorDTO.getOwnerId());
//            b.field("id",baseVisitorDTO.getId());
//            b.field("visitorName",baseVisitorDTO.getVisitorName());
//            b.field("followUpNumbers",baseVisitorDTO.getFollowUpNumbers());
//            b.field("visitorPhone",baseVisitorDTO.getVisitorPhone());
//            b.field("visitReasonId",baseVisitorDTO.getVisitReasonId());
//            b.field("visitReason",baseVisitorDTO.getVisitReason());
//            b.field("inviterId",baseVisitorDTO.getInviterId());
//            b.field("inviterName",baseVisitorDTO.getInviterName());
//            b.field("plannedVisitTime",baseVisitorDTO.getPlannedVisitTime());
//            b.field("visitTime",baseVisitorDTO.getVisitTime());
//            b.field("visitStatus",baseVisitorDTO.getVisitStatus());
//            b.field("visitorType",baseVisitorDTO.getVisitorType());
//            b.field("enterpriseId",baseVisitorDTO.getEnterpriseId());
//            b.field("enterpriseName",baseVisitorDTO.getEnterpriseName());
//            b.field("officeLocationId",baseVisitorDTO.getOfficeLocationId());
//            b.field("officeLocationName",baseVisitorDTO.getOfficeLocationName());
//            LOGGER.info(b.toString());
//            feedDoc(String.valueOf(baseVisitorDTO.getId()), b);
//        } catch (IOException ex) {
//            LOGGER.error("Create VisitorSysCount {} error",baseVisitorDTO.getId(),ex);
//            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
//                    "es Create VisitorSysCount {} error",baseVisitorDTO.getId());
//        }
//    }

    @Override
    public void feedDoc(Object object) {
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            String idstring = null;
            for (String syncField : syncFields) {
                String methodName = "get"+ syncField.substring(0,1).toUpperCase()+syncField.substring(1,syncField.length());
                Method method = object.getClass().getMethod(methodName);
                Object result = method.invoke(object);
                if(result instanceof Timestamp){
                    b.field(syncField,((Timestamp)result).getTime());
                }else {
                    b.field(syncField, result);
                }
                if(syncField.equals("id")){
                    idstring=result.toString();
                }
            }
            LOGGER.info(b.toString());
            feedDoc(String.valueOf(idstring), b);
        } catch (Exception ex) {
            LOGGER.error("Create VisitorSysCount {} error",object.toString(),ex);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "es Create VisitorSysCount "+object+" error");
        }
    }

    @Override
    public ListBookedVisitorsResponse searchVisitors(ListBookedVisitorParams params) {
        SearchResponse searchResponse = searchResponse(params,SearchType.QUERY_THEN_FETCH);
        ListBookedVisitorsResponse response = new ListBookedVisitorsResponse();
        List<BaseVisitorDTO> dtos = generateVisitorDtos(searchResponse);

        if(dtos.size() > params.getPageSize()){
            response.setNextPageAnchor(params.getPageAnchor()+1);
            dtos.remove(dtos.size() - 1);
        }

        response.setVisitorDtoList(dtos);

        return response;
    }

    private SearchResponse searchResponse(ListBookedVisitorParams params, SearchType searchType) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());

        QueryBuilder qb = null;
        if(StringUtils.isEmpty(params.getKeyWords())) {
            qb = QueryBuilders.matchAllQuery();
        }else {
            // es中超过10个字无法搜索出来结果，这里把关键词截断处理
            if (params.getKeyWords().length() > 10) {
                params.setKeyWords(params.getKeyWords().substring(0,10));
            }
            qb = QueryBuilders.boolQuery().must(QueryBuilders.queryString("*" + params.getKeyWords() + "*").field("visitorName").field("visitorPhone").field("inviterName"));
        }


        FilterBuilder fb = FilterBuilders.termFilter("namespaceId",params.getNamespaceId());
        fb =FilterBuilders.andFilter(fb,FilterBuilders.termFilter("ownerType",params.getOwnerType()));
        fb =FilterBuilders.andFilter(fb,FilterBuilders.termFilter("ownerId",params.getOwnerId()));
        if(params.getVisitorType()!=null){
            fb =FilterBuilders.andFilter(fb,FilterBuilders.termFilter("visitorType",params.getVisitorType()));
        }
        if(params.getOfficeLocationId()!=null){
            fb =FilterBuilders.andFilter(fb,FilterBuilders.termFilter("officeLocationId",params.getOfficeLocationId()));
        }
        if(params.getVisitReasonId()!=null){
            fb =FilterBuilders.andFilter(fb,FilterBuilders.termFilter("visitReasonId",params.getVisitReasonId()));
        }

        if(params.getEnterpriseId()!=null){
            fb =FilterBuilders.andFilter(fb,FilterBuilders.termFilter("enterpriseId",params.getEnterpriseId()));
        }

        VisitorsysSearchFlagType visitorsysSearchFlagType = VisitorsysSearchFlagType.fromCode(params.getSearchFlag());
        FieldSortBuilder sort = SortBuilders.fieldSort("id").order(SortOrder.DESC);;
        if(visitorsysSearchFlagType == VisitorsysSearchFlagType.BOOKING_MANAGEMENT) {
            RangeFilterBuilder rf = new RangeFilterBuilder("plannedVisitTime");
            if (params.getStartPlannedVisitTime() != null) {
                rf.gt(params.getStartPlannedVisitTime());
            }
            if (params.getEndPlannedVisitTime() != null) {
                rf.lt(params.getEndPlannedVisitTime());
            }
            if(params.getBookingStatus()!=null){
                fb = FilterBuilders.andFilter(fb,FilterBuilders.termFilter("bookingStatus", params.getBookingStatus()));
            }else{
                fb = FilterBuilders.andFilter(fb,FilterBuilders.notFilter(FilterBuilders.termFilter("bookingStatus", VisitorsysVisitStatus.DELETED.getCode())));
            }
            sort = SortBuilders.fieldSort("plannedVisitTime").order(SortOrder.DESC);
        }else if (visitorsysSearchFlagType == VisitorsysSearchFlagType.VISITOR_MANAGEMENT){
            RangeFilterBuilder rf = new RangeFilterBuilder("visitTime");
            if (params.getStartVisitTime() != null) {
                rf.gt(params.getStartVisitTime());
            }
            if (params.getEndVisitTime() != null) {
                rf.lt(params.getEndVisitTime());
            }
            if(params.getVisitStatus()!=null){
                fb = FilterBuilders.andFilter(fb,FilterBuilders.termFilter("visitStatus", params.getVisitStatus()));
            }else {
                fb = FilterBuilders.andFilter(fb,FilterBuilders.notFilter(FilterBuilders.termFilter("visitStatus", VisitorsysVisitStatus.DELETED.getCode())));
            }
            sort = SortBuilders.fieldSort("visitTime").order(SortOrder.DESC);
        }


        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(searchType);
        if(searchType == SearchType.QUERY_THEN_FETCH) {
            builder.setFrom(params.getPageAnchor().intValue() * params.getPageSize()).setSize(params.getPageSize() + 1);
        }
        builder.setQuery(qb);
        builder.addSort(sort);

        if(LOGGER.isDebugEnabled())
            LOGGER.info("VisitorsysSearcherImpl query builder ："+builder);

        SearchResponse searchResponse = builder.execute().actionGet();
        if(LOGGER.isDebugEnabled())
            LOGGER.info("VisitorsysSearcherImpl query searchResponse ："+searchResponse);
        return searchResponse;
    }


    private SearchResponse searchByAgg(ListBookedVisitorParams params, SearchType searchType) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());

        QueryBuilder qb =  QueryBuilders.matchAllQuery();
        FilterBuilder fb = FilterBuilders.termFilter("namespaceId",params.getNamespaceId());
        fb =FilterBuilders.andFilter(fb,FilterBuilders.termFilter("ownerType",params.getOwnerType()));
        fb =FilterBuilders.andFilter(fb,FilterBuilders.termFilter("ownerId",params.getOwnerId()));

        AggregationBuilders.terms("createTime");
        if(params.getStartVisitorCountTime()!=null && params.getEndVisitorCountTime()!=null){
            fb =FilterBuilders.andFilter(fb,
                    FilterBuilders
                            .rangeFilter("createTime")
                            .from(params.getStartVisitTime())
                            .to(params.getEndVisitorCountTime()));
        }
        if(params.getStartDailyAvgVisitorTime()!=null && params.getEndDailyAvgVisitorTime()!=null){
            fb =FilterBuilders.andFilter(fb,
                    FilterBuilders
                            .rangeFilter("createTime")
                            .from(params.getStartDailyAvgVisitorTime())
                            .to(params.getEndDailyAvgVisitorTime()));
        }
        if(params.getStartTimeShareAvgVisitorTime()!=null && params.getEndTimeShareAvgVisitorTime()!=null){
            fb =FilterBuilders.andFilter(fb,
                    FilterBuilders
                            .rangeFilter("createTime")
                            .from(params.getStartTimeShareAvgVisitorTime())
                            .to(params.getEndTimeShareAvgVisitorTime()));
        }
        if(params.getStartRankingTime()!=null && params.getEndRankingTime()!=null){
            fb =FilterBuilders.andFilter(fb,
                    FilterBuilders
                            .rangeFilter("createTime")
                            .from(params.getStartRankingTime())
                            .to(params.getEndRankingTime()));
        }
        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(searchType);
        if(searchType == SearchType.QUERY_THEN_FETCH) {
            builder.setFrom(0).setSize(Integer.MAX_VALUE);
        }
        builder.setQuery(qb);

        if(LOGGER.isDebugEnabled())
            LOGGER.info("VisitorsysSearcherImpl query builder ："+builder);

        SearchResponse searchResponse = builder.execute().actionGet();
        if(LOGGER.isDebugEnabled())
            LOGGER.info("VisitorsysSearcherImpl query searchResponse ："+searchResponse);
        return searchResponse;
    }


    @Override
    public GetStatisticsResponse countVisitors(GetStatisticsCommand cmd) {
        GetStatisticsResponse response = new GetStatisticsResponse();
        VisitorsysFlagType visitorsysFlagType = checkVisitorsysFlag(cmd.getStatsAllTimeCountFlag());
        if(visitorsysFlagType == VisitorsysFlagType.YES){
            response.setVisitorCount(searchVisitorCount(cmd));
            response.setInvitedVisitorCount(searchInvitedVisitorCount(cmd));
        }
        if(cmd.getStartVisitorCountTime() != null && cmd.getEndVisitorCountTime()!=null){
            SearchResponse searchResponse = searchTimeVisitorCount(cmd);
            response.setTimeVisitorCount(searchResponse.getHits().getTotalHits());
//            response.setVisitorCountStatsList(searchResponse);
        }
        return response;
    }

    private SearchResponse searchTimeVisitorCount(GetStatisticsCommand cmd) {
        ListBookedVisitorParams params = new ListBookedVisitorParams();
        params.setOwnerId(cmd.getOwnerId());
        params.setOwnerType(cmd.getOwnerType());
        params.setNamespaceId(cmd.getNamespaceId());
        params.setSearchFlag(VisitorsysSearchFlagType.VISITOR_MANAGEMENT.getCode());
        params.setStartVisitorCountTime(cmd.getStartVisitorCountTime());
        params.setEndVisitorCountTime(cmd.getEndVisitorCountTime());
        SearchResponse searchResponse = searchByAgg(params, SearchType.QUERY_THEN_FETCH);
        return searchResponse;
    }

    private Long searchInvitedVisitorCount(GetStatisticsCommand cmd) {
        ListBookedVisitorParams params = ConvertHelper.convert(cmd,ListBookedVisitorParams.class);
        params.setSearchFlag(VisitorsysSearchFlagType.BOOKING_MANAGEMENT.getCode());
        params.setVisitorType(VisitorsysVisitorType.BE_INVITED.getCode());
        return searchResponse(params,SearchType.COUNT).getHits().getTotalHits();
    }

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
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL
                    , ErrorCodes.ERROR_INVALID_PARAMETER, "unknow visitor flag = "+flag);
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
                feedDoc(ConvertHelper.convert(visitorSysVisitor, BaseVisitorDTO.class));
            }
            if(list.size()<pageSize){
                break;
            }
            params.setPageAnchor(list.get(list.size()-1).getId());
        }
    }

    @Override
    public void syncVisitor(Integer namespaceId,Long visitorId) {
        VisitorSysVisitor visitor = visitorSysVisitorProvider.findVisitorSysVisitorById(namespaceId, visitorId);
        feedDoc(visitor);
    }

    @Override
    public void syncVisitor(Object visitor) {
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

                dtos.add(dto);
            }
            catch(Exception ex) {
                LOGGER.info("generateVisitorDtos error " + ex.getMessage());
            }
        }

        return dtos;
    }

}
