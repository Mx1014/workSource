// @formatter:off
package com.everhomes.entity;

import com.everhomes.schema.tables.pojos.EhAclRoles;
import com.everhomes.schema.tables.pojos.EhNamespaces;
import com.everhomes.server.schema.tables.pojos.EhActivities;
import com.everhomes.server.schema.tables.pojos.EhActivityRoster;
import com.everhomes.server.schema.tables.pojos.EhAddresses;
import com.everhomes.server.schema.tables.pojos.EhAppUrls;
import com.everhomes.server.schema.tables.pojos.EhAuthorizationRelations;
import com.everhomes.server.schema.tables.pojos.EhBuildings;
import com.everhomes.server.schema.tables.pojos.EhCategories;
import com.everhomes.server.schema.tables.pojos.EhCommunities;
import com.everhomes.server.schema.tables.pojos.EhCommunityApprove;
import com.everhomes.server.schema.tables.pojos.EhContracts;
import com.everhomes.server.schema.tables.pojos.EhCustomerTrackings;
import com.everhomes.server.schema.tables.pojos.EhDomains;
import com.everhomes.server.schema.tables.pojos.EhEnergyMeterTasks;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseCustomers;
import com.everhomes.server.schema.tables.pojos.EhEnterpriseOpRequests;
import com.everhomes.server.schema.tables.pojos.EhEquipmentInspectionTasks;
import com.everhomes.server.schema.tables.pojos.EhForumPosts;
import com.everhomes.server.schema.tables.pojos.EhForums;
import com.everhomes.server.schema.tables.pojos.EhGroups;
import com.everhomes.server.schema.tables.pojos.EhInvestmentAdvertisements;
import com.everhomes.server.schema.tables.pojos.EhLeaseBuildings;
import com.everhomes.server.schema.tables.pojos.EhLeaseProjects;
import com.everhomes.server.schema.tables.pojos.EhLeasePromotions;
import com.everhomes.server.schema.tables.pojos.EhNews;
import com.everhomes.server.schema.tables.pojos.EhOrganizations;
import com.everhomes.server.schema.tables.pojos.EhParkingCarVerifications;
import com.everhomes.server.schema.tables.pojos.EhParkingCardRequests;
import com.everhomes.server.schema.tables.pojos.EhParkingClearanceLogs;
import com.everhomes.server.schema.tables.pojos.EhParkingLots;
import com.everhomes.server.schema.tables.pojos.EhPaymentApplications;
import com.everhomes.server.schema.tables.pojos.EhPmTasks;
import com.everhomes.server.schema.tables.pojos.EhPortalItemCategories;
import com.everhomes.server.schema.tables.pojos.EhPortalItemGroups;
import com.everhomes.server.schema.tables.pojos.EhPortalItems;
import com.everhomes.server.schema.tables.pojos.EhPortalLayouts;
import com.everhomes.server.schema.tables.pojos.EhQualityInspectionTasks;
import com.everhomes.server.schema.tables.pojos.EhRegions;
import com.everhomes.server.schema.tables.pojos.EhRelocationRequestItems;
import com.everhomes.server.schema.tables.pojos.EhRelocationRequests;
import com.everhomes.server.schema.tables.pojos.EhResourceCategories;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceCategories;
import com.everhomes.server.schema.tables.pojos.EhServiceAlliances;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleApps;
import com.everhomes.server.schema.tables.pojos.EhServiceModules;
import com.everhomes.server.schema.tables.pojos.EhTalentRequests;
import com.everhomes.server.schema.tables.pojos.EhUserImpersonations;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.server.schema.tables.pojos.EhWarehouseRequests;
import com.everhomes.server.schema.tables.pojos.EhGeneralFormVals;

/**
 * <p>实体类型:</p>
 * <ul>
 * <li>USER：用户</li>
 * <li>GROUP: 组</li>
 * <li>FORUM: 论坛</li>
 * <li>ADDRESS: 地址</li>
 * <li>ADDRESS: 地址</li>
 * <li>CATEGORY: 类型</li>
 * <li>COMMUNITY: 小区</li>
 * <li>REGION: 区域</li>
 * <li>POST: 帖子/评论</li>
 * <li>FAMILY: 家庭</li>
 * <li>FLEAMARKET: 跳蚤市场</li>
 * <li>ORGANIZATIONS: 机构</li>
 * <li>BUILDING: 楼栋</li>
 * <li>APPURLS: app 信息</li>
 * <li>NEWS: 新闻</li>
 * <li>EhZuolinAdmins: 左邻管理</li>
 * </ul>
 */
public enum EntityType {
    USER(EhUsers.class.getSimpleName()),
    GROUP(EhGroups.class.getSimpleName()),
    FORUM(EhForums.class.getSimpleName()),
    ADDRESS(EhAddresses.class.getSimpleName()),
    CATEGORY(EhCategories.class.getSimpleName()),
    COMMUNITY(EhCommunities.class.getSimpleName()),
    REGION(EhRegions.class.getSimpleName()),
    POST(EhForumPosts.class.getSimpleName()),
    ACTIVITY(EhActivities.class.getSimpleName()),
    FAMILY("EhFamilies"),
    FLEAMARKET("EhFleaMarkets"),
    TOPIC("EhTopics"),
    ORGANIZATIONS(EhOrganizations.class.getSimpleName()),
    BUILDING(EhBuildings.class.getSimpleName()),
    NAMESPACE(EhNamespaces.class.getSimpleName()),
    APPURLS(EhAppUrls.class.getSimpleName()),
    NEWS(EhNews.class.getSimpleName()),
    IMPERSONATION(EhUserImpersonations.class.getSimpleName()),
    SACATEGORY(EhServiceAllianceCategories.class.getSimpleName()),
    ROLE(EhAclRoles.class.getSimpleName()),
    RESOURCE_CATEGORY(EhResourceCategories.class.getSimpleName()),
    PARKING_CARD_REQUEST(EhParkingCardRequests.class.getSimpleName()),
    PARKING_LOT(EhParkingLots.class.getSimpleName()),
    PARKING_CLEARANCE_LOG(EhParkingClearanceLogs.class.getSimpleName()),
    ENTERPRISE_OP_REQUEST(EhEnterpriseOpRequests.class.getSimpleName()),
    PM_TASK(EhPmTasks.class.getSimpleName()),
    SERVICE_MODULE(EhServiceModules.class.getSimpleName()),
    AUTHORIZATION_RELATION(EhAuthorizationRelations.class.getSimpleName()),
    ZUOLIN_ADMIN("EhZuolinAdmins"),
    ALL("EhAll"),
    LEASE_PROMOTION(EhLeasePromotions.class.getSimpleName()),
    WAREHOUSE_REQUEST(EhWarehouseRequests.class.getSimpleName()),
    CONTRACT(EhContracts.class.getSimpleName()),
    ENTERPRISE_CUSTOMER(EhEnterpriseCustomers.class.getSimpleName()),
    PORTAL_ITEM(EhPortalItems.class.getSimpleName()),
    PORTAL_ITEM_CATEGORY(EhPortalItemCategories.class.getSimpleName()),
    PORTAL_LAYOUT(EhPortalLayouts.class.getSimpleName()),
    PORTAL_ITEM_GROUP(EhPortalItemGroups.class.getSimpleName()),
    SERVICE_MODULE_APP(EhServiceModuleApps.class.getSimpleName()),
    SERVICE_ALLIANCE(EhServiceAlliances.class.getSimpleName()),
    BIZ("EhBizs"),
    ORGANIZATION_FILE("EhOrganizationFiles"),
    COMMUNITY_APPROVE(EhCommunityApprove.class.getSimpleName()),
	TALENT_REQUEST(EhTalentRequests.class.getSimpleName()),

	EQUIPMENT_TASK(EhEquipmentInspectionTasks.class.getSimpleName()),
	QUALITY_TASK(EhQualityInspectionTasks.class.getSimpleName()),
    CHILD_PROJECT("child_project"),
    LEASE_BUILDING(EhLeaseBuildings.class.getSimpleName()),

    LEASE_PROJECT(EhLeaseProjects.class.getSimpleName()),
	CUSTOMER_TRACKING(EhCustomerTrackings.class.getSimpleName()),

    PARKING_CAR_VERIFICATION(EhParkingCarVerifications.class.getSimpleName()),

    ENERGY_TASK(EhEnergyMeterTasks.class.getSimpleName()),
    RELOCATION_REQUEST(EhRelocationRequests.class.getSimpleName()),
    RELOCATION_REQUEST_ITEM(EhRelocationRequestItems.class.getSimpleName()),
    DOMAIN(EhDomains.class.getSimpleName()),
    PAYMENT_APPLICATION(EhPaymentApplications.class.getSimpleName()),
    GENERAL_FORM_VAL(EhGeneralFormVals.class.getSimpleName()),
    ACTIVITY_ROSTER(EhActivityRoster.class.getSimpleName()),
	INVESTMENT_ADVERTISEMENT(EhInvestmentAdvertisements.class.getSimpleName());



    private String code;
    
    private EntityType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static EntityType fromCode(String code) {
        if(code == null)
            return null;
        
        if(code.equalsIgnoreCase(USER.getCode()))
            return USER;
        else if(code.equalsIgnoreCase(GROUP.getCode()))
            return GROUP;
        else if(code.equalsIgnoreCase(FORUM.getCode()))
            return FORUM;
        else if(code.equalsIgnoreCase(ADDRESS.getCode()))
            return ADDRESS;
        else if(code.equalsIgnoreCase(CATEGORY.getCode()))
            return CATEGORY;
        else if(code.equalsIgnoreCase(COMMUNITY.getCode()))
            return COMMUNITY;
        else if(code.equalsIgnoreCase(REGION.getCode()))
            return REGION;
        else if(code.equalsIgnoreCase(FAMILY.getCode()))
            return FAMILY;
        else if(code.equalsIgnoreCase(POST.getCode()))
            return POST;
        else if(code.equalsIgnoreCase(ORGANIZATIONS.getCode()))
            return ORGANIZATIONS;
        else if(code.equalsIgnoreCase(ROLE.getCode()))
            return ROLE;
        else if(code.equalsIgnoreCase("EhFamilies"))
            return FAMILY;
        else if(code.equalsIgnoreCase("EhFleaMarkets"))
            return FLEAMARKET;
        else if(code.equalsIgnoreCase("EhTopics"))
            return TOPIC;
        else if(code.equalsIgnoreCase("EhBuildings"))
        	return BUILDING;
        else if(code.equalsIgnoreCase(IMPERSONATION.getCode())) {
            return IMPERSONATION;
        }
        for (EntityType entityType : EntityType.values()) {
            if (entityType.getCode().equals(code)) {
                return entityType;
            }
        }
        return null;
    }
}
