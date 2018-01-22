package com.everhomes.equipment;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.equipment.EquipmentInspectionPlanDTO;
import com.everhomes.rest.equipment.EquipmentStandardRelationDTO;
import com.everhomes.rest.equipment.EquipmentTaskDTO;
import com.everhomes.rest.equipment.ListEquipmentTasksResponse;
import com.everhomes.rest.equipment.SearchEquipmentTasksCommand;
import com.everhomes.rest.quality.OwnerType;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.EquipmentTasksSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionEquipmentPlanMap;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.mysql.jdbc.StringUtils;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class EquipmentTasksSearcherImpl extends AbstractElasticSearch implements EquipmentTasksSearcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentTasksSearcherImpl.class);

	@Autowired
	private EquipmentProvider equipmentProvider;

    @Autowired
    private EquipmentService equipmentService;


    @Autowired
	private ConfigurationProvider configProvider;

	@Autowired
	private OrganizationProvider organizationProvider;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
    private PortalService portalService;

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

        checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_TASK_LIST,cmd.getTargetId());

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
//产品要求把已失效的任务也显示出来 add by xiongying20170217 改用namespaceId add by xiongying 20170328
        FilterBuilder fb = FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId());
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
        if(cmd.getTaskType() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("taskType", cmd.getTaskType()));

        if(cmd.getInspectionCategoryId() != null)
        	fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("inspectionCategoryId", cmd.getInspectionCategoryId()));

        //3.0.3需要改成多状态匹配
        if (cmd.getStatus() != null && cmd.getStatus().size() > 0) {
                fb = FilterBuilders.andFilter(fb, FilterBuilders.termsFilter("status", cmd.getStatus()));
        }
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0L;
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

        List<EquipmentTaskDTO> tasks = new ArrayList<>();
        for (Long id : ids) {
            EquipmentInspectionTasks task = equipmentProvider.findEquipmentTaskById(id);
            EquipmentTaskDTO dto = ConvertHelper.convert(task, EquipmentTaskDTO.class);

            if (task.getPlanId() == null || task.getPlanId() == 0L) {
                EquipmentInspectionPlanDTO plansDTO = processEquipmentInspectionObjectsByPlanId(
                        ConvertHelper.convert(equipmentProvider.getEquipmmentInspectionPlanById(id),EquipmentInspectionPlanDTO.class));
                if (null != plansDTO) {
                    dto.setPlanDescription(plansDTO.getRemarks());
                    dto.setTaskType(plansDTO.getPlanType());
                    dto.setEquipments(plansDTO.getEquipmentStandardRelations());
                }
            } else {
                //兼容之前的task
                EquipmentStandardRelationDTO equipmentStandardRelation = new EquipmentStandardRelationDTO();
                EquipmentInspectionStandards standard = equipmentProvider.findStandardById(task.getStandardId());
                if (null != standard) {
                    dto.setPlanDescription(standard.getDescription());
                    dto.setTaskType(standard.getStandardType());
                    equipmentStandardRelation.setStandardId(standard.getId());
                    equipmentStandardRelation.setStandardName(standard.getName());
                }

                EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(task.getEquipmentId());
                if (null != equipment) {
                    equipmentStandardRelation.setEquipmentId(equipment.getId());
                    equipmentStandardRelation.setEquipmentName(equipment.getName());
                    equipmentStandardRelation.setLocation(equipment.getLocation());
                }
                List<EquipmentStandardRelationDTO> equipments = new ArrayList<>();
                equipments.add(equipmentStandardRelation);
                dto.setEquipments(equipments);
            }
        }
        response.setTasks(tasks);

        return response;
	}

    private void checkUserPrivilege(Long orgId, Long privilegeId, Long communityId) {
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), orgId, privilegeId, EquipmentConstant.EQUIPMENT_MODULE, null, null, null,communityId);
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

//            // reviewStatus: 任务审核状态 0: UNREVIEWED 1: REVIEWED
//            if (ReviewResult.fromStatus(task.getReviewResult()) == ReviewResult.NONE) {
//                b.field("reviewStatus", 0);
//            } else if (ReviewResult.fromStatus(task.getReviewResult()) == ReviewResult.QUALIFIED) {
//                b.field("reviewStatus", 1);
//            } else if (ReviewResult.fromStatus(task.getReviewResult()) == ReviewResult.REVIEW_DELAY) {
//                b.field("reviewStatus", 4);
//            }

            EquipmentInspectionPlans plan = equipmentProvider.getEquipmmentInspectionPlanById(task.getPlanId());
            if(null != plan) {
            	b.field("taskType", plan.getPlanType());
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

    private EquipmentInspectionPlanDTO processEquipmentInspectionObjectsByPlanId(EquipmentInspectionPlanDTO planDTO) {

        List<EquipmentInspectionEquipmentPlanMap> planMaps = equipmentProvider.getEquipmentInspectionPlanMap(planDTO.getId());
        List<EquipmentStandardRelationDTO> relationDTOS = new ArrayList<>();
        if (planMaps != null && planMaps.size() > 0) {
            for (EhEquipmentInspectionEquipmentPlanMap map : planMaps) {
                EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(map.getEquimentId());
                EquipmentInspectionStandards standard = equipmentProvider.findStandardById(map.getStandardId());

                EquipmentStandardRelationDTO relations = new EquipmentStandardRelationDTO();
                if (equipment != null) {
                    relations.setEquipmentName(equipment.getName());
                    relations.setEquipmentId(equipment.getId());
                    relations.setLocation(equipment.getLocation());
                }
                if (standard != null) {
                    relations.setStandardName(standard.getName());
                    relations.setStandardId(standard.getId());
                    relations.setRepeatType(standard.getRepeatType());
                }
                relations.setOrder(map.getDefaultOrder());
                relations.setPlanId(planDTO.getId());

                relationDTOS.add(relations);
            }
            relationDTOS.sort(Comparator.comparingLong(EquipmentStandardRelationDTO::getOrder));
        }
        planDTO.setEquipmentStandardRelations(relationDTOS);
        return planDTO;
    }
}
