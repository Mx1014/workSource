package com.everhomes.pmtask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.everhomes.community.CommunityProvider;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.module.ListUserRelatedProjectByModuleCommand;
import com.everhomes.rest.pmtask.*;
import org.apache.commons.lang.CharSet;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.SearchUtils;
import com.everhomes.util.RuntimeErrorException;

@Component
public class PmTaskSearchImpl extends AbstractElasticSearch implements PmTaskSearch{
    private static final Logger LOGGER = LoggerFactory.getLogger(PmTaskSearchImpl.class);

    @Autowired
	private PmTaskProvider pmTaskProvider;
    @Autowired
    private ServiceModuleService serviceModuleService;

	@Override
	public String getIndexType() {
		return SearchUtils.PMTASK;
	}
	
	private XContentBuilder createDoc(PmTask task){
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            
            b.field("address", task.getAddress());
            b.field("addressId", task.getAddressId());
            b.field("namespaceId", task.getNamespaceId());
            b.field("ownerId", task.getOwnerId());
            b.field("ownerType", task.getOwnerType());
            b.field("content", task.getContent());
            b.field("creatorUid", task.getCreatorUid());
        	b.field("taskCategoryId", task.getTaskCategoryId());
            b.field("createTime", task.getCreateTime().getTime());
            b.field("status", task.getStatus());
            b.field("flowCaseId", task.getFlowCaseId());
            b.field("requestorName", task.getRequestorName());
            b.field("requestorPhone", task.getRequestorPhone());
            String buildingName = URLEncoder.encode(task.getBuildingName() == null ? "" : task.getBuildingName(), StandardCharsets.UTF_8.name());
            b.field("buildingName", buildingName);
            b.field("organizationUid",task.getOrganizationUid()==null?0:task.getOrganizationUid());
            b.field("star",task.getStar());
            b.field("amount",task.getAmount());
//          多应用标识
            b.field("appId",task.getAppId());

            PmTaskCategory appType = pmTaskProvider.findCategoryById(task.getTaskCategoryId());
            //多入口查全部数据
            if (null != appType && Arrays.asList(PmTaskAppType.TYPES).contains(appType.getParentId())) {
                b.field("appType", appType.getParentId());
            }

            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create pmtask error, taskId={}", task.getId());
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_SYNC_ES_FAIL,
    				"Create pmtask error.");
        }
    }
    
    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
        
    }
    
    @Override
    public void bulkUpdate(List<PmTask> tasks) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (PmTask task : tasks) {
            XContentBuilder source = createDoc(task);
            if(null != source) {
                LOGGER.info("Sync pmtask(update), id=" + task.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(task.getId().toString()).source(source));    
            }
            
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }
    
    @Override
    public void feedDoc(PmTask task) {
        XContentBuilder source = createDoc(task);
        
        feedDoc(task.getId().toString(), source);
    }
    
    @Override
    public void syncPmTask() {
        int pageSize = 200;
        Long nextPageAnchor = null;
        
        this.deleteAll();
        for(;;){
            List<PmTask> tasks = pmTaskProvider.listPmTask(null, null, null, nextPageAnchor, pageSize);
        	//tasks = tasks.stream().filter(r-> r.getFlowCaseId()!=null && r.getFlowCaseId()>0).collect(Collectors.toList());
        	if(tasks.size() > 0){

        		if(tasks.size() == pageSize){
            		nextPageAnchor = tasks.get(tasks.size()-1).getCreateTime().getTime();
            	}
        	}
 	        
 	       this.bulkUpdate(tasks);
 	       
 	       if(tasks.size() < pageSize) {
	        	break;
	        }
 	       
           tasks.clear();
           LOGGER.info("Sync pmtask(syncupdate), processStat count: " + tasks.size());
           
        }
        //TODO merge ?
        //http://www.elastic.co/guide/en/elasticsearch/guide/current/merge-process.html
        this.optimize(1);
        this.refresh();
    }

    @Override
    public List<PmTaskDTO> searchDocsByType(Byte status, String queryString,Long ownerId, String ownerType, Long categoryId, Long startDate,
    		Long endDate, Long addressId, String buildingName,Byte creatorType, Long pageAnchor, Integer pageSize) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        
        
        FilterBuilder fb;
//      int namespaceId = UserContext.getCurrentNamespaceId();

        fb = FilterBuilders.termFilter("ownerId", ownerId);
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerType", ownerType));
        
        BoolQueryBuilder qb;
        //http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/query-dsl-dis-max-query.html
        //http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/query-dsl-wildcard-query.html
        qb = QueryBuilders.boolQuery();
        
        if(StringUtils.isNotBlank(queryString)){
        	MultiMatchQueryBuilder mb = QueryBuilders.multiMatchQuery(queryString,"requestorName","requestorPhone","content");
            qb = qb.must(mb);	
        }
        
        RangeQueryBuilder rb = null;
        if(null !=pageAnchor){
        	rb = QueryBuilders.rangeQuery("createTime").lte(pageAnchor);
            qb = qb.must(rb);	
        }
        if(null != startDate){
            rb = QueryBuilders.rangeQuery("createTime").gt(startDate);
        }
        if(null != endDate){
        	if(null == rb)
        		rb = QueryBuilders.rangeQuery("createTime");
            rb.lt(endDate);
            qb = qb.must(rb);
        }
        if (null!=creatorType){
            if (creatorType.equals(PmtaskCreatorType.SELF.getCode()))
                rb = QueryBuilders.rangeQuery("organizationUid").lte(0l);
            if (creatorType.equals(PmtaskCreatorType.OTHERS.getCode()))
                rb = QueryBuilders.rangeQuery("organizationUid").gt(0l);
            qb = qb.must(rb);
        }

        if(StringUtils.isNotBlank(buildingName)){
        	QueryStringQueryBuilder sb = QueryBuilders.queryString(buildingName).field("buildingName");
            qb = qb.must(sb);	
        }
        
        if(null != categoryId){
            if (Arrays.asList(PmTaskAppType.TYPES).contains(categoryId)) {
                QueryStringQueryBuilder sb = QueryBuilders.queryString(categoryId.toString()).field("appType");
                qb = qb.must(sb);
            }else {
                QueryStringQueryBuilder sb = QueryBuilders.queryString(categoryId.toString()).field("taskCategoryId");
                qb = qb.must(sb);
            }
        }
        
        if(null != status){
        	QueryStringQueryBuilder sb = QueryBuilders.queryString(status.toString()).field("status");
            qb = qb.must(sb);	
        }
        
        if(null != addressId){
        	QueryStringQueryBuilder sb = QueryBuilders.queryString(addressId.toString()).field("addressId");
            qb = qb.must(sb);	
        }
        
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        if(null != pageSize)
        	builder.setSize(pageSize);
        builder.setQuery(qb).setPostFilter(fb);
        // builder.addSort("createTime", SortOrder.ASC);
        builder.addSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC));
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Search pm tasks, builder={}", builder);
        }
        
        SearchResponse rsp = builder.execute().actionGet();
        
        List<PmTaskDTO> dtos = new ArrayList<>();
        SearchHit[] docs = rsp.getHits().getHits();
        for (SearchHit sd : docs) {
        	PmTaskDTO d = readDoc(sd.getSource(), sd.getId());
            if(null != d) {
            	dtos.add(d);
            }
        }
        
        return dtos;        
    }

    @Override
    public List<PmTaskDTO> searchAllDocsByType(SearchTasksCommand cmd, Integer pageSize) {
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());

        FilterBuilder fb;
//      int namespaceId = UserContext.getCurrentNamespaceId();
        List<Long> ownerIds = new ArrayList<>();
        if(-1L == cmd.getOwnerId()){
            ListUserRelatedProjectByModuleCommand cmd1 = new ListUserRelatedProjectByModuleCommand();
            cmd1.setModuleId(20100L);
            cmd1.setAppId(cmd.getAppId());
            cmd1.setOrganizationId(cmd.getCurrentPMId());
            List<ProjectDTO> dtos = serviceModuleService.listUserRelatedProjectByModuleId(cmd1);
            ownerIds.addAll(dtos.stream().map(elem ->{return elem.getProjectId();}).collect(Collectors.toList()));
            fb = FilterBuilders.inFilter("ownerId",ownerIds.toArray());
        } else{
            fb = FilterBuilders.termFilter("ownerId", cmd.getOwnerId());
        }

        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerType", cmd.getOwnerType()));

        BoolQueryBuilder qb;
        //http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/query-dsl-dis-max-query.html
        //http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/query-dsl-wildcard-query.html
        qb = QueryBuilders.boolQuery();

        if(StringUtils.isNotBlank(cmd.getKeyword())){
            MultiMatchQueryBuilder mb = QueryBuilders.multiMatchQuery(cmd.getKeyword(),"requestorName","requestorPhone","content");
            qb = qb.must(mb);
        }

        RangeQueryBuilder rb = null;
        if(null != cmd.getPageAnchor()){
            rb = QueryBuilders.rangeQuery("createTime").lte(cmd.getPageAnchor());
            qb = qb.must(rb);
        }
        if(null != cmd.getStartDate()){
            rb = QueryBuilders.rangeQuery("createTime").gt(cmd.getStartDate());
        }
        if(null != cmd.getEndDate()){
            if(null == rb)
                rb = QueryBuilders.rangeQuery("createTime");
            rb.lt(cmd.getEndDate());
            qb = qb.must(rb);
        }
        if (null!= cmd.getCreatorType()){
            if (cmd.getCreatorType().equals(PmtaskCreatorType.SELF.getCode()))
                rb = QueryBuilders.rangeQuery("organizationUid").lte(0l);
            if (cmd.getCreatorType().equals(PmtaskCreatorType.OTHERS.getCode()))
                rb = QueryBuilders.rangeQuery("organizationUid").gt(0l);
            qb = qb.must(rb);
        }

        if(StringUtils.isNotBlank(cmd.getBuildingName())){
            String buildingName = "";
            try {
                buildingName = URLEncoder.encode(cmd.getBuildingName(),StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            QueryStringQueryBuilder sb = QueryBuilders.queryString(buildingName).field("buildingName");
            qb = qb.must(sb);
        }

        if(null != cmd.getTaskCategoryId()){
            if (Arrays.asList(PmTaskAppType.TYPES).contains(cmd.getTaskCategoryId())) {
                QueryStringQueryBuilder sb = QueryBuilders.queryString(cmd.getTaskCategoryId().toString()).field("appType");
                qb = qb.must(sb);
            }else {
                QueryStringQueryBuilder sb = QueryBuilders.queryString(cmd.getTaskCategoryId().toString()).field("taskCategoryId");
                qb = qb.must(sb);
            }
        }

        if(null != cmd.getStatus()){
            QueryStringQueryBuilder sb = QueryBuilders.queryString(cmd.getStatus().toString()).field("status");
            qb = qb.must(sb);
        }

        if(null != cmd.getAddressId()){
            QueryStringQueryBuilder sb = QueryBuilders.queryString(cmd.getAddressId().toString()).field("addressId");
            qb = qb.must(sb);
        }

        if(null != cmd.getAppId()){
            QueryStringQueryBuilder sb = QueryBuilders.queryString(cmd.getAppId().toString()).field("appId");
            qb = qb.must(sb);
        }

        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        if(null != pageSize)
            builder.setSize(pageSize);
        builder.setQuery(qb).setPostFilter(fb);
        // builder.addSort("createTime", SortOrder.ASC);
        builder.addSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC));
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Search pm tasks, builder={}", builder);
        }

        SearchResponse rsp = builder.execute().actionGet();

        List<PmTaskDTO> dtos = new ArrayList<>();
        SearchHit[] docs = rsp.getHits().getHits();
        for (SearchHit sd : docs) {
            PmTaskDTO d = readDoc(sd.getSource(), sd.getId());
            if(null != d) {
                dtos.add(d);
            }
        }

        return dtos;
    }
    
    private PmTaskDTO readDoc(Map<String, Object> source, String idAsStr) {
        try {
        	PmTaskDTO doc = new PmTaskDTO();
            doc.setId(Long.parseLong(idAsStr));
            doc.setAddress((String)source.get("address"));
            doc.setAddressId(SearchUtils.getLongField(source.get("addressId")));
            doc.setOwnerId(SearchUtils.getLongField(source.get("ownerId")));
            doc.setOwnerType((String)source.get("ownerType"));
            doc.setContent((String)source.get("content"));
            doc.setCreatorUid(SearchUtils.getLongField(source.get("creatorUid")));
            doc.setTaskCategoryId(SearchUtils.getLongField(source.get("taskCategoryId")));
            doc.setCreateTime(new Timestamp((Long)source.get("createTime")));
            doc.setStatus(((Integer)source.get("status")).byteValue());
            doc.setRequestorName((String)source.get("requestorName"));
            doc.setRequestorPhone((String)source.get("requestorPhone"));
            doc.setFlowCaseId(SearchUtils.getLongField(source.get("flowCaseId")));
            String buildingName = (String)source.get("buildingName");
            doc.setBuildingName(URLDecoder.decode(buildingName,StandardCharsets.UTF_8.name()));
            doc.setOrganizationUid(SearchUtils.getLongField(source.get("organizationUid")));
            doc.setStar( null != source.get("star") ? (String)source.get("star"):"");
            doc.setAmount(SearchUtils.getLongField(source.get("amount")));
//          多应用标识
            doc.setAppId(SearchUtils.getLongField(source.get("appId")));
            
            return doc;
        }catch (Exception ex) {
            LOGGER.error("Pmtask readDoc failed, source={}, id={}", source, idAsStr);
            throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_QUERY_ES_FAIL,
    				"readDoc Exception.");
        }

    }
    
}
