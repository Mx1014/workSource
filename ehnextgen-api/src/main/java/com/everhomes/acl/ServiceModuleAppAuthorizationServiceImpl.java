package com.everhomes.acl;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.acl.DistributeServiceModuleAppAuthorizationCommand;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.acl.UpdateAppProfileCommand;
import com.everhomes.rest.oauth2.ModuleManagementType;
import com.everhomes.server.schema.Tables;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServiceModuleAppAuthorizationServiceImpl implements ServiceModuleAppAuthorizationService{

    static final Integer MAX_COUNT_IN_A_QUERY = 1000000;

    @Autowired
    private ServiceModuleAppAuthorizationProvider serviceModuleAppAuthorizationProvider;
    @Autowired
    private CommunityProvider communityProvider;
    @Autowired
    private RolePrivilegeService rolePrivilegeService;
    @Autowired
    private ServiceModuleAppProfileProvider serviceModuleAppProfileProvider;

    @Override
    public boolean checkCommunityRelationOfOrgId(Integer namespaceId, Long currentOrgId, Long checkCommunityId) {
        List<ServiceModuleAppAuthorization> authorizations = serviceModuleAppAuthorizationProvider.queryServiceModuleAppAuthorizations(new ListingLocator(), MAX_COUNT_IN_A_QUERY, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.ORGANIZATION_ID.eq(currentOrgId));
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.PROJECT_ID.eq(checkCommunityId));
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.NAMESPACE_ID.eq(namespaceId));
                return query;
            }
        });
        return authorizations.size() > 0;
    }

    @Override
    public List<ProjectDTO> listCommunityRelationOfOrgId(Integer namespaceId, Long organizationId) {
        List<ProjectDTO> dtos = new ArrayList<>();
        List<ServiceModuleAppAuthorization> authorizations = serviceModuleAppAuthorizationProvider.queryServiceModuleAppAuthorizations(new ListingLocator(), MAX_COUNT_IN_A_QUERY, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.ORGANIZATION_ID.eq(organizationId));
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.NAMESPACE_ID.eq(namespaceId));
                return query;
            }
        });
        if(authorizations.size() > 0){
            List<Long> projectIds = authorizations.stream().map(r->r.getProjectId()).collect(Collectors.toList());
            List<Community> communities = communityProvider.listCommunities(namespaceId, new ListingLocator(), MAX_COUNT_IN_A_QUERY, new ListingQueryBuilderCallback() {
                @Override
                public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                    query.addConditions(Tables.EH_COMMUNITIES.ID.in(projectIds));
                    return query;
                }
            });

            for (Community community: communities) {
                ProjectDTO dto = new ProjectDTO();
                dto.setProjectId(community.getId());
                dto.setProjectName(community.getName());
                dto.setProjectType(com.everhomes.entity.EntityType.COMMUNITY.getCode());
                dto.setCommunityType(community.getCommunityType());
                dtos.add(dto);
            }

            return rolePrivilegeService.handleTreeProject(dtos);
        }
        return dtos;
    }

    @Override
    public List<ServiceModuleAppAuthorization> listCommunityRelationOfOwnerIdAndAppId(Integer namespaceId, Long ownerId, Long appId) {
        List<ServiceModuleAppAuthorization> authorizations = serviceModuleAppAuthorizationProvider.queryServiceModuleAppAuthorizations(new ListingLocator(), MAX_COUNT_IN_A_QUERY, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.OWNER_ID.eq(ownerId));
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.NAMESPACE_ID.eq(namespaceId));
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.APP_ID.eq(appId));
                return query;
            }
        });
        return authorizations;
    }

    @Override
    public List<ServiceModuleAppAuthorization> listCommunityRelationOfOrgIdAndAppId(Integer namespaceId, Long organizationId, Long appId) {
        List<ServiceModuleAppAuthorization> authorizations = serviceModuleAppAuthorizationProvider.queryServiceModuleAppAuthorizations(new ListingLocator(), MAX_COUNT_IN_A_QUERY, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.ORGANIZATION_ID.eq(organizationId));
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.NAMESPACE_ID.eq(namespaceId));
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.APP_ID.eq(appId));
                return query;
            }
        });
        return authorizations;
    }

    @Override
    public List<Long> listCommunityAppIdOfOrgId(Integer namespaceId, Long organizationId) {
        List<ServiceModuleAppAuthorization> authorizations = serviceModuleAppAuthorizationProvider.queryServiceModuleAppAuthorizations(
                new ListingLocator(), MAX_COUNT_IN_A_QUERY, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.ORGANIZATION_ID.eq(organizationId));
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.NAMESPACE_ID.eq(namespaceId));
                return query;
            }
        });
        return authorizations.stream().map(r->r.getAppId()).collect(Collectors.toList());
    }

    @Override
    public void distributeServiceModuleAppAuthorization(DistributeServiceModuleAppAuthorizationCommand cmd) {
        List<Long> create_community_ids = new ArrayList<>();
        assert cmd.getProjectIds() != null;
        // 1.先查出对应原公司 + appId 是否有匹配的园区。如果有就更新，没有就新建
        List<ServiceModuleAppAuthorization> all_authorization = this.listCommunityRelationOfOwnerIdAndAppId(cmd.getNamespaceId(), cmd.getOwnerId(), cmd.getAppId());
        if (all_authorization != null && all_authorization.size() > 0) {
            List<ServiceModuleAppAuthorization> update_authorization = all_authorization.stream().filter(r -> cmd.getProjectIds().contains(r.getProjectId())).collect(Collectors.toList());
            update_authorization.forEach(r -> {
                r.setOrganizationId(cmd.getToOrgId());
                r.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            });
            // 如果cmd中的园区已经在这个(公司 + 应用)中分配过，则更新
            serviceModuleAppAuthorizationProvider.updateServiceModuleAppAuthorizations(update_authorization);
            List<Long> all_community_ids = all_authorization.stream().map(ServiceModuleAppAuthorization::getProjectId).collect(Collectors.toList());
            create_community_ids = cmd.getProjectIds().stream().filter(r -> !all_community_ids.contains(r)).collect(Collectors.toList());
        } else {
            create_community_ids = cmd.getProjectIds();
        }

        // 如果存在需要新增的分配关系，则进行新增
        if (create_community_ids != null && create_community_ids.size() > 0) {
            List<ServiceModuleAppAuthorization> create_authorization = new ArrayList<>();
            create_community_ids.forEach(r -> {
                ServiceModuleAppAuthorization authorization = new ServiceModuleAppAuthorization();
                authorization.setOwnerId(cmd.getOwnerId());
                authorization.setOrganizationId(cmd.getToOrgId());
                authorization.setAppId(cmd.getAppId());
                authorization.setNamespaceId(cmd.getNamespaceId());
                authorization.setControlType(ModuleManagementType.COMMUNITY_CONTROL.getCode());
                authorization.setProjectId(r);
                authorization.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                authorization.setUpdateTime(authorization.getCreateTime());
                create_authorization.add(authorization);
            });
            serviceModuleAppAuthorizationProvider.createServiceModuleAppAuthorizations(create_authorization);
        }
    }

    @Override
    public void updateAppProfile(UpdateAppProfileCommand cmd) {

        ServiceModuleAppProfile profile = ConvertHelper.convert(cmd, ServiceModuleAppProfile.class);

        if(cmd.getMobileUris() != null){
            profile.setMobileUris(StringHelper.toJsonString(cmd.getMobileUris()));
        }
        if(cmd.getPcUris() != null){
            profile.setPcUris(StringHelper.toJsonString(cmd.getPcUris()));
        }

        if(cmd.getConfigAppIds() != null){
            profile.setAppEntryInfos(StringHelper.toJsonString(cmd.getAppEntryInfos()));
        }

        ServiceModuleAppProfile oldProfile = null;
        if(cmd.getOriginId() != null){
            oldProfile = serviceModuleAppProfileProvider.findServiceModuleAppProfileByOriginId(cmd.getOriginId());
        }


        if(oldProfile == null){
            serviceModuleAppProfileProvider.createServiceModuleAppProfile(profile);
        }else {
            profile.setId(oldProfile.getId());
            serviceModuleAppProfileProvider.updateServiceModuleAppProfile(profile);
        }
    }
}
