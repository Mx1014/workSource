package com.everhomes.energy;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationJobPosition;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.portal.PortalService;
import com.everhomes.repeat.RepeatProvider;
import com.everhomes.repeat.RepeatSettings;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.acl.PrivilegeServiceErrorCode;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.energy.EnergyPlanDTO;
import com.everhomes.rest.energy.EnergyPlanGroupDTO;
import com.everhomes.rest.energy.SearchEnergyPlansCommand;
import com.everhomes.rest.energy.SearchEnergyPlansResponse;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.EnergyPlanSearcher;
import com.everhomes.search.SearchUtils;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ying.xiong on 2017/10/23.
 */
@Component
public class EnergyPlanSearcherImpl extends AbstractElasticSearch implements EnergyPlanSearcher {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private  EnergyPlanProvider energyPlanProvider;

    @Autowired
    private RepeatProvider repeatProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;
    @Autowired
    private PortalService portalService;

    private void checkEnergyAuth(Integer namespaceId, Long privilegeId, Long orgId, Long communityId) {
        ListServiceModuleAppsCommand cmd = new ListServiceModuleAppsCommand();
        cmd.setNamespaceId(namespaceId);
        cmd.setModuleId(ServiceModuleConstants.ENERGY_MODULE);
        cmd.setActionType(ActionType.OFFICIAL_URL.getCode());
        ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(cmd);
        Long appId = apps.getServiceModuleApps().get(0).getOriginId();
        if(!userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), EntityType.ORGANIZATIONS.getCode(), orgId,
                orgId, privilegeId, appId, null, communityId)) {
            LOGGER.error("Permission is prohibited, namespaceId={}, orgId={}, ownerType={}, ownerId={}, privilegeId={}",
                    namespaceId, orgId, EntityType.COMMUNITY.getCode(), communityId, privilegeId);
            throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_CHECK_APP_PRIVILEGE,
                    "check user privilege error");
        }
    }

    @Override
    public String getIndexType() {
        return SearchUtils.ENERGY_PLAN;
    }

    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<EnergyPlan> plans) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (EnergyPlan plan : plans) {
            XContentBuilder source = createDoc(plan);
            if(null != source) {
                LOGGER.info("id:" + plan.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType()).id(plan.getId().toString()).source(source));
            }
        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }
    }

    @Override
    public void feedDoc(EnergyPlan plan) {
        XContentBuilder source = createDoc(plan);
        feedDoc(plan.getId().toString(), source);
    }

    private XContentBuilder createDoc(EnergyPlan plan) {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            builder.field("planName", plan.getName());
            builder.field("status", plan.getStatus());
            builder.field("notifyTickMinutes", plan.getNotifyTickMinutes());
            builder.field("communityId", plan.getTargetId());
            builder.field("ownerId", plan.getOwnerId());
            builder.field("namespaceId", plan.getNamespaceId());
            RepeatSettings rs = repeatProvider.findRepeatSettingById(plan.getRepeatSettingId());
            if(rs != null) {
                builder.field("startTime", rs.getStartDate());
                builder.field("endTime", rs.getEndDate());
                builder.field("repeatType", rs.getRepeatType());
                builder.field("repeatInterval", rs.getRepeatInterval());
            }
            List<EnergyPlanGroupMap> groupMaps = energyPlanProvider.listGroupsByEnergyPlan(plan.getId());
            if(groupMaps != null && groupMaps.size() > 0) {
                for(EnergyPlanGroupMap groupMap : groupMaps) {
                    StringBuilder sb = new StringBuilder();
                    Organization group = organizationProvider.findOrganizationById(groupMap.getGroupId());
                    OrganizationJobPosition position = organizationProvider.findOrganizationJobPositionById(groupMap.getPositionId());
                    if(group != null) {
                        sb.append(group.getName());

                    }
                    if(position != null) {
                        sb.append(position.getName());
                    }

                    if(sb.length() != 0) {
                        builder.array("groupName", sb.toString());
                    }
                }
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
        List<EnergyPlan> plans = energyPlanProvider.listEnergyPlans(pageAnchor, pageSize);
        while (plans != null && plans.size() > 0) {
            bulkUpdate(plans);
            pageAnchor = plans.get(plans.size() - 1).getId() + 1;
            plans = energyPlanProvider.listEnergyPlans(pageAnchor, pageSize);
        }
        this.optimize(1);
        this.refresh();
        LOGGER.info("sync for energy plan ok");
    }

    @Override
    public SearchEnergyPlansResponse query(SearchEnergyPlansCommand cmd) {
//        userPrivilegeMgr.checkCurrentUserAuthority(EntityType.COMMUNITY.getCode(), cmd.getCommunityId(), cmd.getOrganizationId(), PrivilegeConstants.ENERGY_PLAN_LIST);
        checkEnergyAuth(cmd.getNamespaceId(), PrivilegeConstants.ENERGY_PLAN_LIST, cmd.getOrganizationId(),  cmd.getCommunityId());
        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        QueryBuilder qb = null;
        if(cmd.getKeywords() == null || cmd.getKeywords().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeywords())
                    .field("planName", 1.5f)
                    .field("groupName", 1.0f);

            builder.setHighlighterFragmentSize(60);
            builder.setHighlighterNumOfFragments(8);
            builder.addHighlightedField("planName").addHighlightedField("groupName");
        }

        FilterBuilder fb = null;
        FilterBuilder nfb = FilterBuilders.termFilter("status", CommonStatus.INACTIVE.getCode());
        fb = FilterBuilders.notFilter(nfb);
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId()));
        fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("communityId", cmd.getCommunityId()));

        if(cmd.getStartTime() != null) {
            RangeFilterBuilder rf = new RangeFilterBuilder("startTime");
            //rf.gt(cmd.getStartTime());
            rf.gte(cmd.getStartTime());//修复缺陷 #39719 【标准版全量】左邻 能耗管理 计划管理 查询栏 by ycx
            fb = FilterBuilders.andFilter(fb, rf);
        }

//        if(cmd.getEndTime() != null) {
//            RangeFilterBuilder rf = new RangeFilterBuilder("endTime");
//            //rf.lt(cmd.getEndTime());
//            rf.lte(cmd.getEndTime());//修复缺陷 #39719 【标准版全量】左邻 能耗管理 计划管理 查询栏 by ycx
//            fb = FilterBuilders.andFilter(fb, rf);
//        }

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0l;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }

        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);
//        if(cmd.getKeywords() == null || cmd.getKeywords().isEmpty()) {
//            builder.addSort("id", SortOrder.DESC);
//        }
        SearchResponse rsp = builder.execute().actionGet();

        if(LOGGER.isDebugEnabled())
            LOGGER.info("EnergyPlanSearcherImpl query builder: {}, rsp: {}", builder, rsp);

        List<EnergyPlanDTO> planDTOs = getDTOs(rsp);
        SearchEnergyPlansResponse response = new SearchEnergyPlansResponse();

        if(planDTOs.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            planDTOs.remove(planDTOs.size() - 1);
        }
        response.setPlanDTOs(planDTOs);
        return response;
    }

    private Long convertStrToTimestamp(String date) throws ParseException {
        date = date.replace("Z", " UTC");//注意是空格+UTC
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        Long d = format.parse(date).getTime();
        return d;
    }

    private List<EnergyPlanDTO> getDTOs(SearchResponse rsp) {
        List<EnergyPlanDTO> dtos = new ArrayList<>();
        SearchHit[] docs = rsp.getHits().getHits();
        for (SearchHit sd : docs) {
            try {
                EnergyPlanDTO dto = new EnergyPlanDTO();
                dto.setId(Long.parseLong(sd.getId()));
                Map<String, Object> source = sd.getSource();

                dto.setName(String.valueOf(source.get("planName")));
                dto.setNotifyTickMinutes(Integer.valueOf(String.valueOf(source.get("notifyTickMinutes"))));
                dto.setNamespaceId(Integer.valueOf(String.valueOf(source.get("namespaceId"))));
                RepeatSettingsDTO repeat = new RepeatSettingsDTO();

                repeat.setStartDate(convertStrToTimestamp(String.valueOf(source.get("startTime"))));
                repeat.setEndDate(convertStrToTimestamp(String.valueOf(source.get("endTime"))));
                repeat.setRepeatType(Byte.valueOf(String.valueOf(source.get("repeatType"))));
                repeat.setRepeatInterval(Integer.valueOf(String.valueOf(source.get("repeatInterval"))));
                dto.setRepeat(repeat);

                List<EnergyPlanGroupMap> groupMaps = energyPlanProvider.listGroupsByEnergyPlan(dto.getId());
                if(groupMaps != null && groupMaps.size() > 0) {
                    List<EnergyPlanGroupDTO> groups = new ArrayList<>();
                    groupMaps.forEach(group -> {
                        EnergyPlanGroupDTO groupDTO = ConvertHelper.convert(group, EnergyPlanGroupDTO.class);
                        StringBuilder sb = new StringBuilder();
                        Organization org = organizationProvider.findOrganizationById(group.getGroupId());
                        OrganizationJobPosition position = organizationProvider.findOrganizationJobPositionById(group.getPositionId());
                        if(org != null) {
                            sb.append(org.getName());

                        }
                        if(position != null) {
                            sb.append(position.getName());
                        }
                        groupDTO.setGroupName(sb.toString());
                        groups.add(groupDTO);
                    });
                    dto.setGroups(groups);
                }
                dtos.add(dto);
            }
            catch(Exception ex) {
                LOGGER.info("getEnergyPlanDTOs error " + ex.getMessage());
            }
        }

        return dtos;
    }
}
