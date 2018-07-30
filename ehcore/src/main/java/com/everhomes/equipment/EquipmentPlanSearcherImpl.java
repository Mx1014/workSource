package com.everhomes.equipment;


import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.repeat.RepeatProvider;
import com.everhomes.repeat.RepeatService;
import com.everhomes.repeat.RepeatSettings;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.equipment.EquipmentInspectionPlanDTO;
import com.everhomes.rest.equipment.SearchEquipmentInspectionPlansCommand;
import com.everhomes.rest.equipment.searchEquipmentInspectionPlansResponse;
import com.everhomes.rest.quality.OwnerType;
import com.everhomes.rest.repeat.RepeatSettingsDTO;
import com.everhomes.search.AbstractElasticSearch;
import com.everhomes.search.EquipmentPlanSearcher;
import com.everhomes.search.SearchUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class EquipmentPlanSearcherImpl extends AbstractElasticSearch implements EquipmentPlanSearcher {

    private static  final Logger LOGGER = LoggerFactory.getLogger(EquipmentPlanSearcherImpl.class);

    @Autowired
    private  EquipmentProvider equipmentProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private RepeatProvider repeatProvider;

    @Autowired
    private RepeatService repeatService;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;


    @Override
    public void deleteById(Long id) {
        deleteById(id.toString());
    }

    @Override
    public void bulkUpdate(List<EquipmentInspectionPlans> plans) {
        BulkRequestBuilder brb = getClient().prepareBulk();
        for (EquipmentInspectionPlans plan : plans) {

            XContentBuilder source = createDoc(plan);
            if(null != source) {
                LOGGER.info("equipment Plan id:" + plan.getId());
                brb.add(Requests.indexRequest(getIndexName()).type(getIndexType())
                        .id(plan.getId().toString()).source(source));
            }

        }
        if (brb.numberOfActions() > 0) {
            brb.execute().actionGet();
        }

    }


    @Override
    public void feedDoc(EquipmentInspectionPlans plan) {
        XContentBuilder source = createDoc(plan);

        feedDoc(plan.getId().toString(), source);

    }

    @Override
    public void syncFromDb() {
        int pageSize = 200;
        this.deleteAll();

        ListingLocator locator = new ListingLocator();
        for(;;) {
            List<EquipmentInspectionPlans> plans = equipmentProvider.ListEquipmentInspectionPlans(locator, pageSize);

            if(plans.size() > 0) {
                this.bulkUpdate(plans);
            }

            if(locator.getAnchor() == null) {
                break;
            }
        }

        this.optimize(1);
        this.refresh();

        LOGGER.info("sync for equipment plans ok");

    }

    @Override
    public searchEquipmentInspectionPlansResponse query(SearchEquipmentInspectionPlansCommand cmd) {

        //check auth
        checkUserPrivilege(cmd.getOwnerId(), PrivilegeConstants.EQUIPMENT_PLAN_LIST,cmd.getTargetId());

        SearchRequestBuilder builder = getClient().prepareSearch(getIndexName()).setTypes(getIndexType());
        QueryBuilder qb = null;
        if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.multiMatchQuery(cmd.getKeyword())
                    .field("name", 5.0f)
                    .field("planNumber", 5.0f);

            builder.addHighlightedField("name")
                    .addHighlightedField("planNumber");

        }
        FilterBuilder fb = FilterBuilders.termFilter("namespaceId", cmd.getNamespaceId());
        if (cmd.getTargetId() != null && cmd.getTargetId() != 0L) {
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetId", cmd.getTargetId()));
            if (!StringUtils.isNullOrEmpty(cmd.getTargetType()))
                fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("targetType", OwnerType.fromCode(cmd.getTargetType()).getCode()));
        } else if (cmd.getTargetIds() != null && cmd.getTargetIds().size() > 0) {
            // this scope should never user organization id
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termsFilter("targetId", cmd.getTargetIds()));
        }


        if(cmd.getInspectionCategoryId()!=null)
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("inspectionCategoryId", cmd.getInspectionCategoryId()));

        if (cmd.getStatus()!=null){
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("status", cmd.getStatus()));
        }
        if (cmd.getPlanType()!=null){
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("planType", cmd.getPlanType()));
        }
        //增加频次  repeatType
        if(cmd.getRepeatType()!=null)
            fb = FilterBuilders.andFilter(fb, FilterBuilders.termFilter("repeatType", cmd.getRepeatType()));

        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = 0L;
        if(cmd.getPageAnchor() != null) {
            anchor = cmd.getPageAnchor();
        }

        qb = QueryBuilders.filteredQuery(qb, fb);
        builder.setSearchType(SearchType.QUERY_THEN_FETCH);
        builder.setFrom(anchor.intValue() * pageSize).setSize(pageSize + 1);
        builder.setQuery(qb);

        SearchResponse rsp = builder.execute().actionGet();

        List<Long> ids = getIds(rsp);

        searchEquipmentInspectionPlansResponse response = new searchEquipmentInspectionPlansResponse();
        if(ids.size() > pageSize) {
            response.setNextPageAnchor(anchor + 1);
            ids.remove(ids.size() - 1);
        } else {
            response.setNextPageAnchor(null);
        }

        List<EquipmentInspectionPlanDTO> plans = new ArrayList<EquipmentInspectionPlanDTO>();
        for(Long id : ids) {
            //RP上只展示计划基本信息和执行周期
            EquipmentInspectionPlans plan = equipmentProvider.getEquipmmentInspectionPlanById(id);
            /*if(plan.getPlanMainId()!=null){
                removeMainPlan(plans,plan.getPlanMainId());
            }*/
            //填充执行开始时间  执行频率  执行时长
            RepeatSettings repeatSettings = repeatProvider.findRepeatSettingById(plan.getRepeatSettingId());
            if(repeatSettings!=null){
                RepeatSettingsDTO repeatSettingDTO =ConvertHelper.
                        convert(repeatProvider.findRepeatSettingById(plan.getRepeatSettingId()), RepeatSettingsDTO.class);

                plan.setExecuteStartTime(repeatService.getExecuteStartTime(repeatSettingDTO));
                plan.setExecutionFrequency(repeatService.getExecutionFrequency(repeatSettingDTO));
                plan.setLimitTime(repeatService.getlimitTime(repeatSettingDTO));
            }
            plans.add(ConvertHelper.convert(plan, EquipmentInspectionPlanDTO.class));
//            //取出所有有修改过的原始计划id
//            List<Long> oldVersionPlanIds = new ArrayList<>();
//            if (plan.getPlanMainId() != null && plan.getPlanMainId() != 0L) {
//                oldVersionPlanIds.add(plan.getPlanMainId());
//            }
//            //resultSet of plan
//            plans.add(ConvertHelper.convert(plan, EquipmentInspectionPlanDTO.class));
//            // 去掉原始计划显示  显示修改之后的
//            for (Long versionId : oldVersionPlanIds) {
//                plans.removeIf(p -> p.getId().equals(versionId));
//            }

        }
        response.setEquipmentInspectionPlans(plans);

        return response;
    }

    @Override
    public String getIndexType() {
        return SearchUtils.EQUIPMENTPLANINDEXTYPE;
    }

    private XContentBuilder createDoc(EquipmentInspectionPlans plan) {
        try {
            XContentBuilder b = XContentFactory.jsonBuilder().startObject();
            b.field("namespaceId", plan.getNamespaceId());
            b.field("ownerId", plan.getOwnerId());
            b.field("ownerType", plan.getOwnerType());
            b.field("targetId", plan.getTargetId());
            b.field("targetType", plan.getTargetType());
            b.field("name", plan.getName());
            b.field("planNumber", plan.getPlanNumber());
            b.field("planType", plan.getPlanType());
            b.field("status", plan.getStatus());
            b.field("inspectionCategoryId", plan.getInspectionCategoryId());

            //关联计划的周期类型
            RepeatSettings repeat = repeatProvider.findRepeatSettingById(plan.getRepeatSettingId());
            if (repeat != null)
                b.field("repeatType", repeat.getRepeatType());

            b.endObject();
            return b;
        } catch (IOException ex) {
            LOGGER.error("Create EquipmentInspectionPlan " + plan.getId() + " error");
            return null;
        }
    }

    private void checkUserPrivilege(Long orgId, Long privilegeId, Long communityId) {
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), orgId, privilegeId, EquipmentConstant.EQUIPMENT_MODULE, null, null, null,communityId);
    }
}
