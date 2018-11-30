package com.everhomes.visitorsys;

import com.everhomes.rest.visitorsys.BaseVisitorDTO;
import com.everhomes.rest.visitorsys.ListFreqVisitorsCommand;
import com.everhomes.rest.visitorsys.ListFreqVisitorsResponse;
import com.everhomes.rest.visitorsys.VisitorsysConstant;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.FreqVisitorSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.util.RuntimeErrorException;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FreqVisitorSearcherImpl extends AbstractElasticSearch implements FreqVisitorSearcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(FreqVisitorSearcherImpl.class);

    private static final String[] syncFields = {"namespaceId", "ownerType", "ownerId", "id",
            "visitorName", "followUpNumbers", "visitorPhone", "visitReasonId", "visitReason",
            "inviterId", "inviterName", "plannedVisitTime", "visitTime", "createTime", "visitStatus", "bookingStatus",
            "visitorType", "enterpriseId", "enterpriseName", "officeLocationId", "officeLocationName","idNumber"};

    @Autowired
    VisitorSysVisitorProvider visitorSysVisitorProvider;

    @Override
    public String getIndexType() {
        return SearchUtils.FREQVISITOR;
    }

    @Override
    public ListFreqVisitorsResponse searchVisitors(ListFreqVisitorsCommand cmd) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());

        QueryBuilder qb = null;

        if (StringUtils.isEmpty(cmd.getVisitorPhone())) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            // es中超过10个字无法搜索出来结果，这里把关键词截断处理
            if (cmd.getVisitorPhone().length() > 10) {
                cmd.setVisitorPhone(cmd.getVisitorPhone().substring(0, 10));
            }
            qb = QueryBuilders.boolQuery().must(QueryBuilders.queryString("*" + cmd.getVisitorPhone() + "*").field("visitorPhone"));
        }

        List<FilterBuilder> fbList = new ArrayList();
        fbList.add(FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId()));
        fbList.add(FilterBuilders.termFilter("ownerType", cmd.getOwnerType()));
        fbList.add(FilterBuilders.termFilter("ownerId", cmd.getOwnerId()));


        qb = QueryBuilders.filteredQuery(qb, FilterBuilders.andFilter(fbList.toArray(new FilterBuilder[fbList.size()])));
        builder.setQuery(qb);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("VisitorsysSearcherImpl query builder ：" + builder);

        SearchResponse searchResponse = builder.execute().actionGet();
        if (LOGGER.isTraceEnabled())
            LOGGER.trace("VisitorsysSearcherImpl query searchResponse ：" + searchResponse);

        ListFreqVisitorsResponse response = new ListFreqVisitorsResponse();
        List<BaseVisitorDTO> dtos = generateVisitorDtos(searchResponse);
        response.setVisitorDtoList(dtos);

        return response;
    }

    @Override
    @Scheduled(cron="0 0 1 * * ?")
    public void syncVisitorsFromDb(Integer namespaceId) {
        this.deleteAll();
        List<VisitorSysVisitor> list = visitorSysVisitorProvider.listFreqVisitor();

        for (VisitorSysVisitor visitorSysVisitor : list) {
            feedDoc(visitorSysVisitor);
        }
    }

    private void feedDoc(VisitorSysVisitor object) {
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
