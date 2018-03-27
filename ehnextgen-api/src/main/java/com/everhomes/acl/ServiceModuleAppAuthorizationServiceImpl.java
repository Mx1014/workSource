package com.everhomes.acl;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.acl.DistributeServiceModuleAppAuthorizationCommand;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.server.schema.Tables;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceModuleAppAuthorizationServiceImpl implements ServiceModuleAppAuthorizationService{

    static final Integer MAX_COUNT_IN_A_QUERY = 1000000;

    @Autowired
    private ServiceModuleAppAuthorizationProvider serviceModuleAppAuthorizationProvider;
    @Autowired
    private CommunityProvider communityProvider;
    @Autowired
    private RolePrivilegeService rolePrivilegeService;

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
    public List<ServiceModuleAppAuthorization> listCommunityRelationOfOrgIdAndAppId(Integer namespaceId, Long organizationId, Long appId, Long projectId) {
        List<ServiceModuleAppAuthorization> authorizations = serviceModuleAppAuthorizationProvider.queryServiceModuleAppAuthorizations(new ListingLocator(), MAX_COUNT_IN_A_QUERY, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.ORGANIZATION_ID.eq(organizationId));
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.NAMESPACE_ID.eq(namespaceId));
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.APP_ID.eq(appId));
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.PROJECT_ID.eq(projectId));
                return query;
            }
        });
        return authorizations;
    }

    @Override
    public List<Long> listCommunityAppIdOfOrgId(Integer namespaceId, Long organizationId) {
        List<ServiceModuleAppAuthorization> authorizations = serviceModuleAppAuthorizationProvider.queryServiceModuleAppAuthorizations(new ListingLocator(), MAX_COUNT_IN_A_QUERY, new ListingQueryBuilderCallback() {
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
        // 1.先查出对应原公司 + appId 是否有匹配的园区。如果有就更新，没有就新建
        this.listCommunityRelationOfOrgIdAndAppId(cmd.getNamespaceId(), cmd.getFromOrgId(), cmd.getAppId(), cmd.get)
    }

}
