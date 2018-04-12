package com.everhomes.community;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Record;
import org.jooq.SelectQuery;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.everhomes.acl.ServiceModuleAppAuthorization;
import com.everhomes.acl.ServiceModuleAppAuthorizationProvider;
import com.everhomes.acl.ServiceModuleAppAuthorizationService;
import com.everhomes.enterprise.EnterpriseService;
import com.everhomes.entity.EntityType;
import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.DistributeServiceModuleAppAuthorizationCommand;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.community.CommunityFetchType;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.module.ListUserRelatedProjectByModuleCommand;
import com.everhomes.rest.module.ServiceModuleAppType;
import com.everhomes.rest.organization.ListEnterprisesCommand;
import com.everhomes.rest.organization.ListEnterprisesCommandResponse;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.rest.servicemoduleapp.InstallAppCommand;
import com.everhomes.rest.servicemoduleapp.ListServiceModuleAppsByOrgIdCommand;
import com.everhomes.rest.servicemoduleapp.ListServiceModuleAppsByOrgIdResponse;
import com.everhomes.rest.servicemoduleapp.UninstallAppCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;

public class ZuolinBaseInitialTest extends CoreServerTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZuolinBaseInitialTest.class);

    @Autowired
    OrganizationProvider organizationProvider;
    
    @Autowired
    CommunityProvider communityProvider;
    
    @Autowired
    ServiceModuleAppAuthorizationService serviceModuleAppAuthorizationService;
    
    @Autowired
    ServiceModuleAppAuthorizationProvider serviceModuleAppAuthorizationProvider;
    
    @Autowired
    UserProvider userProvider;
    
    @Autowired
    ServiceModuleAppService serviceModuleAppService;
    
    @Autowired
    OrganizationService organizationService;
    
    @Autowired
    ServiceModuleService serviceModuleService;
    
    Integer namespaceId = 2;
    int pageSize = 100;
    Long userId = 477451l;
    Long organizationId = 1041158l;

    @Before
    public void setUp() throws Exception {
        User user = userProvider.findUserById(userId);
        UserContext.setCurrentUser(user);
        UserContext.setCurrentNamespaceId(namespaceId);
        super.setUp();
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * 获取管理公司列表
     */
    @Test
    public void testListAllPMEnterpise() {
        OrganizationType oType = OrganizationType.PM;
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<Organization> organizations = organizationProvider.listEnterpriseByNamespaceIds(namespaceId, null, oType.getCode(), locator, pageSize);
        
        Assert.assertTrue(organizations.size() == 3);
        
        LOGGER.debug("testListAllPMEnterpise end");
    }
    
    @Test
    public void testListNormalEnterprise() {
        OrganizationType oType = OrganizationType.ENTERPRISE;
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<Organization> organizations = organizationProvider.listEnterpriseByNamespaceIds(namespaceId, null, oType.getCode(), locator, pageSize);
        
        Assert.assertTrue(organizations.size() == 3);
        
        LOGGER.debug("testListNormalEnterprise end");
    }
    
    @Test
    public void testListEnterpirseByCommunityId() {
        Long communityId = 240111044332060169l;
        ListEnterprisesCommand cmd = new ListEnterprisesCommand();
        cmd.setCommunityId(communityId);
        cmd.setNamespaceId(namespaceId);
        ListEnterprisesCommandResponse resp = organizationService.listEnterprises(cmd);
        Assert.assertTrue(resp.getDtos().size() == 3);   
    }
    
    @Test
    public void testListResidents() {
        CommunityType cType = CommunityType.RESIDENTIAL;
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<Community> communities = communityProvider.listCommunities(namespaceId, locator, pageSize, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_COMMUNITIES.COMMUNITY_TYPE.eq(cType.getCode()));
                return query;
            }
        });
        
        Assert.assertTrue(communities.size() == 2);
    }
    
    @Test
    public void testListComercials() {
        CommunityType cType = CommunityType.COMMERCIAL;
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<Community> communities = communityProvider.listCommunities(namespaceId, locator, pageSize, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_COMMUNITIES.COMMUNITY_TYPE.eq(cType.getCode()));
                return query;
            }
        });
        
        Assert.assertTrue(communities.size() == 3);
    }
    
    @Test
    public void testServiceModuleApp() {
        List<Long> authCommunityIds = serviceModuleAppAuthorizationService.listCommunityRelationOfOrgId(UserContext.getCurrentNamespaceId(), organizationId).stream().map(r->r.getProjectId()).collect(Collectors.toList());
        List<Long> authOrganizationIds = serviceModuleAppAuthorizationService.listCommunityAppIdOfOrgId(UserContext.getCurrentNamespaceId(), organizationId);
        
        Assert.assertTrue(authCommunityIds.size() == 1);
        Assert.assertTrue(authOrganizationIds.size() == 1);
    }
    
    @Test
    public void testServiceModuleAppCreate() {
        Long projectId = 240111044332060169l;
        
        ListingLocator locator = new ListingLocator();
        List<ServiceModuleAppAuthorization> authors = serviceModuleAppAuthorizationProvider.queryServiceModuleAppAuthorizations(locator, 10000, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(
                    ListingLocator locator, SelectQuery<? extends Record> query) {
                return query;
            }
        });
        
        for(ServiceModuleAppAuthorization obj : authors) {
            serviceModuleAppAuthorizationProvider.deleteServiceModuleAppAuthorization(obj);    
        }
        
        List<ServiceModuleApp> allApps = serviceModuleAppService.listReleaseServiceModuleApps(UserContext.getCurrentNamespaceId());
        List<Long> appOriginIds = allApps.stream().map(r->r.getOriginId()).collect(Collectors.toList());
        Assert.assertTrue(appOriginIds.size() > 0);
        
        for(Long appId : appOriginIds) {
            ServiceModuleAppAuthorization obj = new ServiceModuleAppAuthorization();
            obj.setAppId(appId);
            obj.setNamespaceId(namespaceId);
            obj.setOwnerId(organizationId);
            obj.setProjectId(projectId);
            obj.setOrganizationId(organizationId);
            serviceModuleAppAuthorizationProvider.createServiceModuleAppAuthorization(obj);    
        }
        
        List<Long> authCommunityIds = serviceModuleAppAuthorizationService.listCommunityRelationOfOrgId(UserContext.getCurrentNamespaceId(), organizationId).stream().map(r->r.getProjectId()).collect(Collectors.toList());
        Assert.assertTrue(authCommunityIds.size() > 0);
    }
    
    @Test
    public void testServiceModuleDistrube() {
//        Long projectId = 240111044332060169l;
        Long normalOrgId = 1041162l;
        Long normalDisAppId = 115084l;//资产管理
        
        ListingLocator locator = new ListingLocator();
        List<ServiceModuleAppAuthorization> authors = serviceModuleAppAuthorizationProvider.queryServiceModuleAppAuthorizations(locator, 10000, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(
                    ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.ORGANIZATION_ID.eq(organizationId).or(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.ORGANIZATION_ID.eq(normalOrgId)));
                return query;
            }
        });
        
        //remove all distrube information
        for(ServiceModuleAppAuthorization obj : authors) {
            serviceModuleAppAuthorizationProvider.deleteServiceModuleAppAuthorization(obj);    
        }
        
        //remove all install apps
        ListServiceModuleAppsByOrgIdCommand installAppsCmd = new ListServiceModuleAppsByOrgIdCommand();
        installAppsCmd.setAppType(ServiceModuleAppType.COMMUNITY.getCode());
        installAppsCmd.setInstallFlag(TrueOrFalseFlag.TRUE.getCode());
        installAppsCmd.setNamespaceId(namespaceId);
        installAppsCmd.setOrgId(organizationId);
        ListServiceModuleAppsByOrgIdResponse installAppsResp = serviceModuleAppService.listServiceModuleAppsByOrgId(installAppsCmd);
        if(installAppsResp.getServiceModuleApps().size() > 0) {
            for(ServiceModuleAppDTO appDTO: installAppsResp.getServiceModuleApps()) {
                UninstallAppCommand uninstallCmd = new UninstallAppCommand();
                uninstallCmd.setOrgAppId(appDTO.getOrgAppId());
                serviceModuleAppService.uninstallApp(uninstallCmd);       
            }
        }
        installAppsResp = serviceModuleAppService.listServiceModuleAppsByOrgId(installAppsCmd);
        Assert.assertTrue(installAppsResp.getServiceModuleApps().size() == 0);
        
        List<ServiceModuleApp> allApps = serviceModuleAppService.listReleaseServiceModuleApps(UserContext.getCurrentNamespaceId());
        List<Long> appOriginIds = allApps.stream().map(r->r.getOriginId()).collect(Collectors.toList());
        Assert.assertTrue(appOriginIds.size() > 0);
        
        List<Long> communityOriginAppIds = new ArrayList<>();
        for(ServiceModuleApp r: allApps) {
            if(r.getAppType() != null && r.getAppType().equals(ServiceModuleAppType.COMMUNITY.getCode())) {
                communityOriginAppIds.add(r.getOriginId());  
            }
        }
        
        for(Long cAppId : communityOriginAppIds) {
            InstallAppCommand installCmd = new InstallAppCommand();
            installCmd.setAppId(cAppId);
            installCmd.setOrgId(organizationId);
            serviceModuleAppService.installApp(installCmd);
        }
        installAppsResp = serviceModuleAppService.listServiceModuleAppsByOrgId(installAppsCmd);
        Assert.assertTrue(installAppsResp.getServiceModuleApps().size() == communityOriginAppIds.size());
        
        CommunityType cType = CommunityType.COMMERCIAL;
        CrossShardListingLocator listCommunityLocator = new CrossShardListingLocator();
        List<Community> communities = communityProvider.listCommunities(namespaceId, listCommunityLocator, pageSize, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_COMMUNITIES.COMMUNITY_TYPE.eq(cType.getCode()));
                return query;
            }
        });
        Assert.assertTrue(communities.size() == 3);
        List<Long> projectIds = new ArrayList<Long>();
        communities.remove(0);
        for(Community c : communities) {
            projectIds.add(c.getId());
        }
        
        for(Long appId : communityOriginAppIds) {
            DistributeServiceModuleAppAuthorizationCommand distrubeCmd = new DistributeServiceModuleAppAuthorizationCommand();
            distrubeCmd.setAppId(appId);
            distrubeCmd.setNamespaceId(namespaceId);
            distrubeCmd.setOwnerId(organizationId);
            distrubeCmd.setProjectIds(projectIds);
            distrubeCmd.setToOrgId(organizationId);
            serviceModuleAppAuthorizationService.distributeServiceModuleAppAuthorization(distrubeCmd);    
        }
        
        List<Long> authCommunityIds = serviceModuleAppAuthorizationService.listCommunityRelationOfOrgId(UserContext.getCurrentNamespaceId(), organizationId).stream().map(r->r.getProjectId()).collect(Collectors.toList());
        Assert.assertTrue(authCommunityIds.size() == projectIds.size());
        
        {
            DistributeServiceModuleAppAuthorizationCommand distrubeCmd = new DistributeServiceModuleAppAuthorizationCommand();
            distrubeCmd.setAppId(normalDisAppId);
            distrubeCmd.setNamespaceId(namespaceId);
            distrubeCmd.setOwnerId(organizationId);
            List<Long> normalProjectIds = new ArrayList<Long>();
            normalProjectIds.add(projectIds.get(1));
            distrubeCmd.setProjectIds(normalProjectIds);
            distrubeCmd.setToOrgId(normalOrgId);
            serviceModuleAppAuthorizationService.distributeServiceModuleAppAuthorization(distrubeCmd);
        }
        List<Long> normalAuthCommunityIds = serviceModuleAppAuthorizationService.listCommunityRelationOfOrgId(UserContext.getCurrentNamespaceId(), normalOrgId).stream().map(r->r.getProjectId()).collect(Collectors.toList());
        Assert.assertTrue(normalAuthCommunityIds.size() == 1);
        
        ListUserRelatedProjectByModuleCommand relatedProjectCmd = new ListUserRelatedProjectByModuleCommand();
        relatedProjectCmd.setAppId(normalDisAppId);
        relatedProjectCmd.setCommunityFetchType(CommunityFetchType.ONLY_COMMUNITY.getCode());
        relatedProjectCmd.setOrganizationId(organizationId);
        List<ProjectDTO> projects = serviceModuleService.listUserRelatedProjectByModuleId(relatedProjectCmd);
        Assert.assertTrue(projects.size() == 1);
        
        relatedProjectCmd = new ListUserRelatedProjectByModuleCommand();
        relatedProjectCmd.setAppId(normalDisAppId);
        relatedProjectCmd.setCommunityFetchType(CommunityFetchType.ONLY_COMMUNITY.getCode());
        relatedProjectCmd.setOrganizationId(normalOrgId);
        projects = serviceModuleService.listUserRelatedProjectByModuleId(relatedProjectCmd);
        Assert.assertTrue(projects.size() == 1);  
    }
    
    @Test
    public void testServiceModuleQuery() {
        Long normalOrgId = 1041162l;
        Long normalDisAppId = 115084l;//资产管理
        
        ListUserRelatedProjectByModuleCommand relatedProjectCmd = new ListUserRelatedProjectByModuleCommand();
        relatedProjectCmd.setAppId(normalDisAppId);
        relatedProjectCmd.setCommunityFetchType(CommunityFetchType.ONLY_COMMUNITY.getCode());
        relatedProjectCmd.setOrganizationId(organizationId);
        List<ProjectDTO> projects = serviceModuleService.listUserRelatedProjectByModuleId(relatedProjectCmd);
        Assert.assertTrue(projects.size() == 1);
        
        relatedProjectCmd = new ListUserRelatedProjectByModuleCommand();
        relatedProjectCmd.setAppId(normalDisAppId);
        relatedProjectCmd.setCommunityFetchType(CommunityFetchType.ONLY_COMMUNITY.getCode());
        relatedProjectCmd.setOrganizationId(normalOrgId);
        projects = serviceModuleService.listUserRelatedProjectByModuleId(relatedProjectCmd);
        Assert.assertTrue(projects.size() == 1);
    }
}
