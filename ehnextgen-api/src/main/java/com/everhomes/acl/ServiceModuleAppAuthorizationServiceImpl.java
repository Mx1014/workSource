package com.everhomes.acl;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.portal.PortalService;
import com.everhomes.portal.PortalVersion;
import com.everhomes.rest.acl.*;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.oauth2.ModuleManagementType;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.servicemoduleapp.ServiceModuleAppAuthorizationDTO;
import com.everhomes.server.schema.Tables;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppProvider;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;
import org.jooq.Condition;
import org.jooq.JoinType;
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

    @Autowired
    private ServiceModuleAppService serviceModuleAppService;

    @Autowired
    private ServiceModuleAppProvider serviceModuleAppProvider;

    @Autowired
    private OrganizationProvider organizationProvider;


    @Autowired
    private PortalService portalService;

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
    public List<ServiceModuleAppAuthorization> listCommunityRelations(Integer namespaceId, Long organizationId, Long communityId) {
        List<ServiceModuleAppAuthorization> authorizations = serviceModuleAppAuthorizationProvider.queryServiceModuleAppAuthorizations(new ListingLocator(), MAX_COUNT_IN_A_QUERY, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.NAMESPACE_ID.eq(namespaceId));
                if (organizationId != null){
                    query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.ORGANIZATION_ID.eq(organizationId));
                }
                if(communityId != null){
                    query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.PROJECT_ID.eq(communityId));
                }

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

//        if(cmd.getAppEntryInfos() != null){
//            profile.setAppEntryInfos(StringHelper.toJsonString(cmd.getAppEntryInfos()));
//        }

        if(cmd.getDependentAppIds() != null){
            profile.setDependentAppIds(StringHelper.toJsonString(cmd.getDependentAppIds()));
        }

        if(cmd.getIconUri() != null){
            profile.setIconUri(cmd.getIconUri());
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

        ServiceModuleApp app = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(cmd.getOriginId());
        app.setDefaultAppFlag(cmd.getDefaultAppFlag());
        serviceModuleAppProvider.updateServiceModuleApp(app);

    }

    @Override
    public void deleteServiceModuleAppAuthorizationByOrganizationId(Long organizationId) {

        //TODO

    }

    @Override
    public ListAppAuthorizationsByOwnerIdResponse listAppAuthorizationsByOwnerId(ListAppAuthorizationsByOwnerIdCommand cmd) {
        ListAppAuthorizationsByOwnerIdResponse response = new ListAppAuthorizationsByOwnerIdResponse();

        final int pageSize = cmd.getPageSize() == null ? 20 : cmd.getPageSize();

        PortalVersion releaseVersion = portalService.findReleaseVersion(cmd.getNamespaceId());

        List<ServiceModuleAppAuthorization> authorizations = serviceModuleAppAuthorizationProvider.queryServiceModuleAppAuthorizations(new ListingLocator(), MAX_COUNT_IN_A_QUERY, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.OWNER_ID.eq(cmd.getOwnerId()));
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.NAMESPACE_ID.eq(cmd.getNamespaceId()));
                if(cmd.getOrganizationId() != null){
                    query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.ORGANIZATION_ID.eq(cmd.getOrganizationId()));
                }

                //过滤掉系统应用
                query.addJoin(Tables.EH_SERVICE_MODULE_APPS, JoinType.JOIN, Tables.EH_SERVICE_MODULE_APPS.ORIGIN_ID.eq(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.APP_ID));
                Condition systemAppCondition = Tables.EH_SERVICE_MODULE_APPS.SYSTEM_APP_FLAG.isNull().or(Tables.EH_SERVICE_MODULE_APPS.SYSTEM_APP_FLAG.eq((byte)0));
                query.addConditions(systemAppCondition);
                query.addConditions(Tables.EH_SERVICE_MODULE_APPS.VERSION_ID.eq(releaseVersion.getId()));

                //不包含授权给自己的记录
                if(cmd.getIncludeAuthToOwnerFlag() != null && cmd.getIncludeAuthToOwnerFlag().byteValue() == 0){
                    query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.ORGANIZATION_ID.ne(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.OWNER_ID));
                }

                if(cmd.getPageAnchor() != null){
                    query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.ID.gt(cmd.getPageAnchor()));
                }
                query.addLimit(pageSize);

                query.addOrderBy(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.ID.asc());

                return query;
            }
        });

        if(authorizations != null && authorizations.size() > pageSize){
            authorizations.remove(pageSize);
            response.setNextPageAnchor(authorizations.get(pageSize - 1).getId());

        }
        List<ServiceModuleAppAuthorizationDTO> dtos = new ArrayList<ServiceModuleAppAuthorizationDTO>();
        if(authorizations != null){
            for (ServiceModuleAppAuthorization authorization: authorizations){
                ServiceModuleAppAuthorizationDTO dto = toDto(authorization);
                dtos.add(dto);
            }
        }

        response.setDtos(dtos);
        return response;
    }

    @Override
    public ListAppAuthorizationsByOwnerIdResponse listAppAuthorizationsByOrganizatioinId(ListAppAuthorizationsByOrganizatioinIdCommand cmd) {

        ListAppAuthorizationsByOwnerIdResponse response = new ListAppAuthorizationsByOwnerIdResponse();

        final int pageSize = cmd.getPageSize() == null ? 20 : cmd.getPageSize();

        PortalVersion releaseVersion = portalService.findReleaseVersion(cmd.getNamespaceId());

        List<ServiceModuleAppAuthorization> authorizations = serviceModuleAppAuthorizationProvider.queryServiceModuleAppAuthorizations(new ListingLocator(), MAX_COUNT_IN_A_QUERY, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.OWNER_ID.ne(cmd.getOrganizationId()));
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.NAMESPACE_ID.eq(cmd.getNamespaceId()));
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.ORGANIZATION_ID.eq(cmd.getOrganizationId()));


                //过滤掉系统应用
                query.addJoin(Tables.EH_SERVICE_MODULE_APPS, JoinType.JOIN, Tables.EH_SERVICE_MODULE_APPS.ORIGIN_ID.eq(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.APP_ID));
                Condition systemAppCondition = Tables.EH_SERVICE_MODULE_APPS.SYSTEM_APP_FLAG.isNull().or(Tables.EH_SERVICE_MODULE_APPS.SYSTEM_APP_FLAG.eq((byte)0));
                query.addConditions(systemAppCondition);
                query.addConditions(Tables.EH_SERVICE_MODULE_APPS.VERSION_ID.eq(releaseVersion.getId()));

                if(cmd.getPageAnchor() != null){
                    query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.ID.gt(cmd.getPageAnchor()));
                }
                query.addLimit(pageSize);


                return query;
            }
        });

        if(authorizations != null && authorizations.size() > pageSize){
            authorizations.remove(pageSize);
            response.setNextPageAnchor(authorizations.get(pageSize - 1).getId());

        }

        List<ServiceModuleAppAuthorizationDTO> dtos = new ArrayList<ServiceModuleAppAuthorizationDTO>();
        if(authorizations != null){
            for (ServiceModuleAppAuthorization authorization: authorizations){
                ServiceModuleAppAuthorizationDTO dto = toDto(authorization);
                dtos.add(dto);
            }
        }


        response.setDtos(dtos);
        return response;
    }

    private ServiceModuleAppAuthorizationDTO toDto(ServiceModuleAppAuthorization appAuthorization){
        ServiceModuleAppAuthorizationDTO dto = ConvertHelper.convert(appAuthorization, ServiceModuleAppAuthorizationDTO.class);
        Community community = communityProvider.findCommunityById(appAuthorization.getProjectId());
        if(community != null){
            dto.setProjectName(community.getName());
        }
        Organization ownerOrganization = organizationProvider.findOrganizationById(appAuthorization.getOwnerId());
        if (ownerOrganization != null){
            dto.setOwnerName(ownerOrganization.getName());
        }
        Organization toOrganization = organizationProvider.findOrganizationById(appAuthorization.getOrganizationId());
        if(toOrganization != null){
            dto.setOrganizationName(toOrganization.getName());
        }
        ServiceModuleApp serviceModuleApp = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(appAuthorization.getAppId());
        if(serviceModuleApp != null){
            dto.setAppName(serviceModuleApp.getName());
        }

        return dto;

    }


    @Override
    public void addAllCommunityAppAuthorizations(Integer namespaceId, Long ownerId, Long appId) {
        ListingLocator locator = new CrossShardListingLocator();
        List<Community> communities = communityProvider.listCommunities(namespaceId, null, ownerId, null, null, null, locator, 10000);
        List<Long> communityIds = new ArrayList<>();
        if(communities != null){
            communityIds =  communities.stream().map(r -> r.getId()).collect(Collectors.toList());
        }

        DistributeServiceModuleAppAuthorizationCommand cmd = new DistributeServiceModuleAppAuthorizationCommand();
        cmd.setNamespaceId(namespaceId);
        cmd.setOwnerId(ownerId);
        cmd.setAppId(appId);
        cmd.setToOrgId(ownerId);
        cmd.setProjectIds(communityIds);
        distributeServiceModuleAppAuthorization(cmd);
    }

    /**
     * 更新应用的授权
     * @param namespaceId
     * @param organizationId
     * @param communityId
     */
    @Override
    public void updateAllAuthToNewOrganization(Integer namespaceId, Long organizationId, Long communityId) {


        //先清除所有原有的授权
        List<ServiceModuleAppAuthorization> serviceModuleAppAuthorizations = listCommunityRelations(namespaceId, null, communityId);
        if(serviceModuleAppAuthorizations != null){
            for(ServiceModuleAppAuthorization authorization: serviceModuleAppAuthorizations){
                serviceModuleAppAuthorizationProvider.deleteServiceModuleAppAuthorization(authorization);
            }
        }


        PortalVersion releaseVersion = portalService.findReleaseVersion(namespaceId);

        List<ServiceModuleApp> apps = serviceModuleAppProvider.listServiceModuleAppsByOrganizationId(releaseVersion.getId(), null, null, organizationId, TrueOrFalseFlag.TRUE.getCode(), TrueOrFalseFlag.TRUE.getCode(), null, 1000);

        if (apps != null && apps.size() > 0) {
            List<ServiceModuleAppAuthorization> createAuthorizations = new ArrayList<>();
            apps.forEach(r -> {
                ServiceModuleAppAuthorization authorization = new ServiceModuleAppAuthorization();
                authorization.setOwnerId(organizationId);
                authorization.setOrganizationId(organizationId);
                authorization.setAppId(r.getOriginId());
                authorization.setNamespaceId(namespaceId);
                authorization.setControlType(ModuleManagementType.COMMUNITY_CONTROL.getCode());
                authorization.setProjectId(communityId);
                authorization.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                authorization.setUpdateTime(authorization.getCreateTime());
                createAuthorizations.add(authorization);
            });
            serviceModuleAppAuthorizationProvider.createServiceModuleAppAuthorizations(createAuthorizations);
        }
    }

    @Override
    public void removeAllCommunityAppAuthorizations(Integer namespaceId, Long ownerId, Long appId) {
        List<ServiceModuleAppAuthorization> authorizations = serviceModuleAppAuthorizationProvider.queryServiceModuleAppAuthorizations(new ListingLocator(), MAX_COUNT_IN_A_QUERY, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.NAMESPACE_ID.eq(namespaceId));
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.OWNER_ID.eq(ownerId));
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.APP_ID.eq(appId));
                return query;
            }
        });

        if(authorizations != null){
            for (ServiceModuleAppAuthorization authorization: authorizations){
                serviceModuleAppAuthorizationProvider.deleteServiceModuleAppAuthorization(authorization);
            }
        }

    }

    @Override
    public ServiceModuleAppDTO getAppProfile(GetAppProfileCommand cmd) {
        ServiceModuleApp app = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(cmd.getOriginId());
        ServiceModuleAppProfile profile = serviceModuleAppProfileProvider.findServiceModuleAppProfileByOriginId(cmd.getOriginId());

        if(app != null && profile != null){
            app.setProfileId(profile.getId());
            app.setAppNo(profile.getAppNo());
            app.setDisplayVersion(profile.getDisplayVersion());
            app.setDescription(profile.getDescription());
            app.setMobileFlag(profile.getMobileFlag());
            app.setMobileUris(profile.getMobileUris());
            app.setPcFlag(profile.getPcFlag());
            app.setPcUris(profile.getPcUris());
            app.setAppEntryInfos(profile.getAppEntryInfos());
            app.setIndependentConfigFlag(profile.getIndependentConfigFlag());
            app.setDependentAppIds(profile.getDependentAppIds());
            app.setSupportThirdFlag(profile.getSupportThirdFlag());
            app.setIconUri(profile.getIconUri());
        }

        return portalService.processServiceModuleAppDTO(app);
    }


    @Override
    public ServiceModuleAppAuthorization findServiceModuleAppAuthorization(Long projectId, Long appId) {
        List<ServiceModuleAppAuthorization> authorizations = serviceModuleAppAuthorizationProvider.queryServiceModuleAppAuthorizations(new ListingLocator(), MAX_COUNT_IN_A_QUERY, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.PROJECT_ID.eq(projectId));
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.APP_ID.eq(appId));
                return query;
            }
        });

        if(authorizations != null && authorizations.size() > 0){
            return authorizations.get(0);
        }
        return null;
    }
}
