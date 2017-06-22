package com.everhomes.equipment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.equipment.EquipmentAccessoriesDTO;
import com.everhomes.rest.equipment.EquipmentTaskDTO;
import com.everhomes.rest.equipment.EquipmentTaskStatus;
import com.everhomes.rest.equipment.ListEquipmentTasksResponse;
import com.everhomes.rest.equipment.ReviewResult;
import com.everhomes.rest.equipment.SearchEquipmentAccessoriesResponse;
import com.everhomes.rest.equipment.SearchEquipmentTasksCommand;
import com.everhomes.rest.quality.OwnerType;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.EquipmentTasksSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.util.ConvertHelper;
import com.everhomes.videoconf.ConfOrders;
import com.mysql.jdbc.StringUtils;

@Component
public class EquipmentTasksSearcherImpl extends AbstractElasticSearch implements EquipmentTasksSearcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentTasksSearcherImpl.class);
			
	@Autowired
	private EquipmentProvider equipmentProvider;
	
	@Autowired
	private ConfigurationProvider configProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;
	
	@Override
	public void deleteById(Long id) {
		deleteById(id.toString());
	}

	@Override
	public void bulkUpdate(List<EquipmentInspectionTasks> tasks) {
		BulkRequestBuilder brb = getClient().prepareBulk();
        for (EquipmentInspectionTasks task : tasks) {
        	
            XContentBuilder source = createDoc(task);
            if(null != source) {
                LOGGER.info("equipment inspection task id:" + task.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(task.getId().toString()).source(source));    
                }
            
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
	}

	@Override
	public void feedDoc(EquipmentInspectionTasks task) {
		XContentBuilder source = createDoc(task);
        
        feedDoc(task.getId().toString(), source);
	}

	@Override
	public void syncFromDb() {
		int pageSize = 200;      
        this.deleteAll();
        
        CrossShardListingLocator locator = new CrossShardListingLocator();
        for(;;) {
            List<EquipmentInspectionTasks> tasks = equipmentProvider.listEquipmentInspectionTasks(locator, pageSize);
            
            if(tasks.size() > 0) {
                this.bulkUpdate(tasks);
            }
            
            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();
        
        LOGGER.info("sync for equipment inspection task ok");
	}

	@Override
	public ListEquipmentTasksResponse query(SearchEquipmentTasksCommand cmd) {
        Long privilegeId = configProvider.getLongValue(EquipmentConstant.EQUIPMENT_TASK_LIST, 0L);
        userPrivilegeMgr.checkCurrentUserAuthority(null, null, cmd.getOwnerId(), privilegeId);
		SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
		QueryBuilder qb = null;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
            		.field("taskName", 5.0f)
                    .field("taskName.pinyin_prefix", 2.0f)
                    .field("taskName.pinyin_gram", 1.0f);
            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("taskName");

        }
//
//        FilterBuilder nfb = FilterBuilders.termFilter("status", EquipmentTaskStatus.NONE.getCode());
//        FilterBuilder fb = FilterBuilders.notFilter(nfb);
//产品要求把已失效的任务也显示出来 add by xiongying20170217
        //改用namespaceId add by xiongying 20170328
        FilterBuilder fb = FilterBuilders.termFilter("namespaceId", UserContext.getCurrentNamespaceId());
//        FilterBuilder fb = FilterBuilders.termFilter("ownerId", cmd.getOwnerId());
////        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerId", cmd.getOwnerId()));
//        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("ownerType", OwnerType.fromCode(cmd.getOwnerType()).getCode()));
        
        if(cmd.getTargetId() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetId", cmd.getTargetId()));
        
        if(!StringUtils.isNullOrEmpty(cmd.getTargetType()))
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetType", OwnerType.fromCode(cmd.getTargetType()).getCode())); 
        
       // startTime  endTime  status  reviewStatus  taskType  
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
        
        
        if(cmd.getStatus() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("status", cmd.getStatus())); 
        
        if(cmd.getReviewStatus() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("reviewStatus", cmd.getReviewStatus())); 

        if(cmd.getTaskType() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("taskType", cmd.getTaskType())); 
        
        if(cmd.getInspectionCategoryId() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("inspectionCategoryId", cmd.getInspectionCategoryId()));
        
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }
        
        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);
        builder.addSort(SortBuilders.fieldSort("endTime").order(SortOrder.DESC));

        SearchResponse rsp = builder.execute().actionGet();

        List<Long> ids = getIds(rsp);
        
        ListEquipmentTasksResponse response = new ListEquipmentTasksResponse();
        if(ids.size() > pageSize) {
        	response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
         } else {
        	 response.setNextPageAnchor(null);
            }
        
        List<EquipmentTaskDTO> tasks = new ArrayList<EquipmentTaskDTO>();
        for(Long id : ids) {
        	EquipmentInspectionTasks task = equipmentProvider.findEquipmentTaskById(id);
        	EquipmentTaskDTO dto = ConvertHelper.convert(task, EquipmentTaskDTO.class);

        	EquipmentInspectionStandards standard = equipmentProvider.findStandardById(task.getStandardId());
            if(null != standard) {
            	dto.setStandardDescription(standard.getDescription());
    			dto.setStandardName(standard.getName());
            	dto.setTaskType(standard.getStandardType());
            	EquipmentInspectionTemplates template = equipmentProvider.findEquipmentInspectionTemplate(standard.getTemplateId(), standard.getOwnerId(), standard.getOwnerType());
        		if(template != null) {
        			dto.setTemplateId(template.getId());
        			dto.setTemplateName(template.getName());
        		}
            }
            
            EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(task.getEquipmentId());
            if(null != equipment) {
            	dto.setEquipmentName(equipment.getName());
            	dto.setEquipmentLocation(equipment.getLocation());
            	dto.setQrCodeFlag(equipment.getQrCodeFlag());
            }
            
            if(task.getExecutorId() != null && task.getExecutorId() != 0) {
                //总公司分公司 by xiongying20170328
                List<OrganizationMember> executors = organizationProvider.listOrganizationMembersByUId(task.getExecutorId());
//            	OrganizationMember executor = organizationProvider.findOrganizationMemberByOrgIdAndUId(task.getExecutorId(), task.getOwnerId());
            	if(executors != null && executors.size() > 0) {
            		dto.setExecutorName(executors.get(0).getContactName());
            	}
        	}
        	
        	if(task.getOperatorId() != null && task.getOperatorId() != 0) {
                List<OrganizationMember> operators = organizationProvider.listOrganizationMembersByUId(task.getOperatorId());
            	if(operators != null && operators.size() > 0) {
            		dto.setOperatorName(operators.get(0).getContactName());
            	}
        	}
        	
        	tasks.add(dto);
        }
        response.setTasks(tasks);
        
        return response;
	}

	@Override
	public String getIndexType() {
		return SearchUtils.EQUIPMENTTASKINDEXTYPE;
	}
	
	private XContentBuilder createDoc(EquipmentInspectionTasks task){
		try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("namespaceId", task.getNamespaceId());
            b.field("ownerId", task.getOwnerId());
            b.field("ownerType", task.getOwnerType());
            b.field("targetId", task.getTargetId());
            b.field("targetType", task.getTargetType());
            b.field("startTime", task.getExecutiveStartTime());
            b.field("endTime", task.getExecutiveExpireTime());
            b.field("status", task.getStatus());
            b.field("taskName", task.getTaskName());
            b.field("inspectionCategoryId", task.getInspectionCategoryId());

           // reviewStatus: 任务审核状态 0: UNREVIEWED 1: REVIEWED 
            if(ReviewResult.fromStatus(task.getReviewResult()) == ReviewResult.NONE) {
            	b.field("reviewStatus", 0);
            } else {
            	b.field("reviewStatus", 1);
            }
            
            EquipmentInspectionStandards standard = equipmentProvider.findStandardById(task.getStandardId());
            if(null != standard) {
            	b.field("taskType", standard.getStandardType());
            } else {
            	b.field("taskType", "");
            }
            
            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create equipment task " + task.getId() + " error");
            return null;
        }
    }

}
