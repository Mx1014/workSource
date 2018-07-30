package com.everhomes.equipment;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.equipment.EquipmentInspectionPlanDTO;
import com.everhomes.rest.equipment.EquipmentStandardRelationDTO;
import com.everhomes.rest.equipment.EquipmentTaskDTO;
import com.everhomes.rest.equipment.EquipmentTaskStatus;
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


        FilterBuilder fb = FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId());
        FilterBuilder nfb = FilterBuilders.termFilter("status", EquipmentTaskStatus.NONE.getCode());
        fb = FilterBuilders.andFilter(fb,FilterBuilders.notFilter(nfb));
        if (cmd.getTargetId() != null && cmd.getTargetId() != 0L) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetId", cmd.getTargetId()));

            if (!StringUtils.isNullOrEmpty(cmd.getTargetType()))
                fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetType", OwnerType.fromCode(cmd.getTargetType()).getCode()));
        }else if(cmd.getTargetIds()!=null && cmd.getTargetIds().size()>0){
            // only all scope field term with ownerId followed by this rule
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termsFilter("targetId", cmd.getTargetIds()));
        }

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
        if (cmd.getStatus() != null && cmd.getStatus().size() > 0 && cmd.getStatus().get(0) != null) {
                fb = FilterBuilders.andFilter(fb, FilterBuilders.termsFilter("status", cmd.getStatus()));
        }
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0L;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }

        qb = QueryBuilders.filteredQuery(qb, fb);
        LOGGER.debug("FilterBuilder:",fb.toString());
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);
        //unMappedType
//        builder.addSort(SortBuilders.fieldSort("status").order(SortOrder.ASC));
        builder.addSort(SortBuilders.fieldSort("startTime").order(SortOrder.DESC));

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
        EquipmentInspectionPlans plan = new EquipmentInspectionPlans();
        for (Long id : ids) {
            EquipmentInspectionTasks task = equipmentProvider.findEquipmentTaskById(id);
            EquipmentTaskDTO dto = ConvertHelper.convert(task, EquipmentTaskDTO.class);
            if (task != null) {
                if (task.getPlanId() != null && task.getPlanId() != 0L) {
                    plan = equipmentProvider.getEquipmmentInspectionPlanById(task.getPlanId());
                    if (null != plan) {
                        EquipmentInspectionPlanDTO plansDTO = processEquipmentInspectionObjectsByPlanId(
                                ConvertHelper.convert(plan, EquipmentInspectionPlanDTO.class));
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
                if(task.getExecutorId() != null && task.getExecutorId() != 0) {
                    List<OrganizationMember> executors = organizationProvider.listOrganizationMembersByUId(task.getExecutorId());
                    if(executors != null && executors.size() > 0) {
                        dto.setExecutorName(executors.get(0).getContactName());
                    }
                }
                tasks.add(dto);
            }
        }
//        if (tasks != null && tasks.size() > 0) {
//            tasks = tasks.stream().sorted(Comparator.comparing(EquipmentTaskDTO::getStatus)).collect(Collectors.toList());
//        }
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

            EquipmentInspectionPlans plan = equipmentProvider.getEquipmmentInspectionPlanById(task.getPlanId());
            if (null != plan) {
                b.field("taskType", plan.getPlanType());
            } else {
                //兼容旧数据
                EquipmentInspectionStandards standard = equipmentProvider.findStandardById(task.getStandardId());
                if (standard != null) {
                    b.field("taskType", standard.getStandardType());
                }else {
                    b.field("taskType", "");
                }
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
                EquipmentInspectionEquipments equipment = equipmentProvider.findEquipmentById(map.getEquipmentId());
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
