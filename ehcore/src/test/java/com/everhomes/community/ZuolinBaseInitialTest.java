package com.everhomes.community;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

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

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
import com.everhomes.otp.TimeBasedOneTimePasswordGenerator;
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
import com.everhomes.rest.servicemoduleapp.ListServiceModuleAppsByOrganizationIdCommand;
import com.everhomes.rest.servicemoduleapp.ListServiceModuleAppsByOrganizationIdResponse;
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
    
    /**
     * 获取普通公司列表
     */
    @Test
    public void testListNormalEnterprise() {
        OrganizationType oType = OrganizationType.ENTERPRISE;
        CrossShardListingLocator locator = new CrossShardListingLocator();
        List<Organization> organizations = organizationProvider.listEnterpriseByNamespaceIds(namespaceId, null, oType.getCode(), locator, pageSize);
        
        Assert.assertTrue(organizations.size() == 3);
        
        LOGGER.debug("testListNormalEnterprise end");
    }
    
    /**
     * 获取某一项目下的公司列表
     */
    @Test
    public void testListEnterpirseByCommunityId() {
        Long communityId = 240111044332060169l;
        ListEnterprisesCommand cmd = new ListEnterprisesCommand();
        cmd.setCommunityId(communityId);
        cmd.setNamespaceId(namespaceId);
        ListEnterprisesCommandResponse resp = organizationService.listEnterprises(cmd);
        Assert.assertTrue(resp.getDtos().size() == 3);   
    }
    
    /**
     * 获取小区列表
     */
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
    
    /**
     * 获取园区列表
     */
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
    
    /**
     * 获取所有应用列表
     */
    @Test
    public void testServiceModuleApp() {
        List<Long> authCommunityIds = serviceModuleAppAuthorizationService.listCommunityRelationOfOrgId(UserContext.getCurrentNamespaceId(), organizationId).stream().map(r->r.getProjectId()).collect(Collectors.toList());
        List<Long> authOrganizationIds = serviceModuleAppAuthorizationService.listCommunityAppIdOfOrgId(UserContext.getCurrentNamespaceId(), organizationId);
        
        Assert.assertTrue(authCommunityIds.size() == 1);
        Assert.assertTrue(authOrganizationIds.size() == 1);
    }
    
    /**
     * 给管理公司安装所有应用
     */
    @Test
    public void testServiceModuleAppCreate() {
        Long projectId = 240111044332060169l;
        
        /* 获取当前公司的所有应用授权 */
        ListingLocator locator = new ListingLocator();
        List<ServiceModuleAppAuthorization> authors = serviceModuleAppAuthorizationProvider.queryServiceModuleAppAuthorizations(locator, 10000, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(
                    ListingLocator locator, SelectQuery<? extends Record> query) {
                return query;
            }
        });
        
        /* 清空所有应用授权 */
        for(ServiceModuleAppAuthorization obj : authors) {
            serviceModuleAppAuthorizationProvider.deleteServiceModuleAppAuthorization(obj);    
        }
        
        /* 获取所有应用 */
        List<ServiceModuleApp> allApps = serviceModuleAppService.listReleaseServiceModuleApps(UserContext.getCurrentNamespaceId());
        List<Long> appOriginIds = allApps.stream().map(r->r.getOriginId()).collect(Collectors.toList());
        Assert.assertTrue(appOriginIds.size() > 0);
        
        /* 给管理公司本域空间安装所有的应用 */
        for(Long appId : appOriginIds) {
            ServiceModuleAppAuthorization obj = new ServiceModuleAppAuthorization();
            obj.setAppId(appId);
            obj.setNamespaceId(namespaceId);
            obj.setOwnerId(organizationId);
            obj.setProjectId(projectId);
            obj.setOrganizationId(organizationId);
            serviceModuleAppAuthorizationProvider.createServiceModuleAppAuthorization(obj);    
        }
        
        /*  */
        List<Long> authCommunityIds = serviceModuleAppAuthorizationService.listCommunityRelationOfOrgId(UserContext.getCurrentNamespaceId(), organizationId).stream().map(r->r.getProjectId()).collect(Collectors.toList());
        Assert.assertTrue(authCommunityIds.size() > 0);
    }
    
    /* 给管理公司安装应用，并且把管理公司某个应用分配出去 */
    @Test
    public void testServiceModuleDistrube() {
//        Long projectId = 240111044332060169l;
        Long normalOrgId = 1041162l;
        Long normalDisAppId = 115084l;//资产管理
        
        /* 获取当前公司的所有应用授权 */
        ListingLocator locator = new ListingLocator();
        List<ServiceModuleAppAuthorization> authors = serviceModuleAppAuthorizationProvider.queryServiceModuleAppAuthorizations(locator, 10000, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(
                    ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.OWNER_ID.eq(organizationId));
                return query;
            }
        });
        
        /* 清空所有应用授权 */
        //remove all distrube information
        for(ServiceModuleAppAuthorization obj : authors) {
            serviceModuleAppAuthorizationProvider.deleteServiceModuleAppAuthorization(obj);    
        }
        
        /* 删除管理公司所有已经安装的应用 */
        //remove all install apps
        ListServiceModuleAppsByOrganizationIdCommand installAppsCmd = new ListServiceModuleAppsByOrganizationIdCommand();
        installAppsCmd.setAppType(ServiceModuleAppType.COMMUNITY.getCode());
        installAppsCmd.setInstallFlag(TrueOrFalseFlag.TRUE.getCode());
        installAppsCmd.setNamespaceId(namespaceId);
        installAppsCmd.setOrganizationId(organizationId);
        ListServiceModuleAppsByOrganizationIdResponse installAppsResp = serviceModuleAppService.listServiceModuleAppsByOrganizationId(installAppsCmd);
        if(installAppsResp.getServiceModuleApps().size() > 0) {
            for(ServiceModuleAppDTO appDTO: installAppsResp.getServiceModuleApps()) {
                UninstallAppCommand uninstallCmd = new UninstallAppCommand();
                uninstallCmd.setOrgAppId(appDTO.getOrgAppId());
                serviceModuleAppService.uninstallApp(uninstallCmd);       
            }
        }
        installAppsResp = serviceModuleAppService.listServiceModuleAppsByOrganizationId(installAppsCmd);
        Assert.assertTrue(installAppsResp.getServiceModuleApps().size() == 0);
        
        /* 获取所有应用 */
        List<ServiceModuleApp> allApps = serviceModuleAppService.listReleaseServiceModuleApps(UserContext.getCurrentNamespaceId());
        List<Long> appOriginIds = allApps.stream().map(r->r.getOriginId()).collect(Collectors.toList());
        Assert.assertTrue(appOriginIds.size() > 0);
        
        /* 获取所有园区相关的应用 */
        List<Long> communityOriginAppIds = new ArrayList<>();
        for(ServiceModuleApp r: allApps) {
            if(r.getAppType() != null && r.getAppType().equals(ServiceModuleAppType.COMMUNITY.getCode())) {
                communityOriginAppIds.add(r.getOriginId());  
            }
        }
        
        /* 把所有园区的应用安装到公司 */
        for(Long cAppId : communityOriginAppIds) {
            InstallAppCommand installCmd = new InstallAppCommand();
            installCmd.setOriginId(cAppId);
            installCmd.setOrganizationId(organizationId);
            serviceModuleAppService.installApp(installCmd);
        }
        installAppsResp = serviceModuleAppService.listServiceModuleAppsByOrganizationId(installAppsCmd);
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
        /* 给管理公司相应的应用授权 */
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
        
        {/* 把管理公司的应用，分配给普通公司 */
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
    public void testAnotherPMServiceModuleDistrube() {
        Long anotherProjectId = 240111044332060180l;//之俊大厦2
        Long anotherOrgId = 1041258l;
        Long normalOrgId = 1041162l;
        Long normalDisAppId = 115084l;//资产管理
        
        ListingLocator locator = new ListingLocator();
        List<ServiceModuleAppAuthorization> authors = serviceModuleAppAuthorizationProvider.queryServiceModuleAppAuthorizations(locator, 10000, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(
                    ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_SERVICE_MODULE_APP_AUTHORIZATIONS.OWNER_ID.eq(anotherOrgId));
                return query;
            }
        });
        
        //remove all distrube information
        for(ServiceModuleAppAuthorization obj : authors) {
            serviceModuleAppAuthorizationProvider.deleteServiceModuleAppAuthorization(obj);    
        }
        
        //remove all install apps
        ListServiceModuleAppsByOrganizationIdCommand installAppsCmd = new ListServiceModuleAppsByOrganizationIdCommand();
        installAppsCmd.setAppType(ServiceModuleAppType.COMMUNITY.getCode());
        installAppsCmd.setInstallFlag(TrueOrFalseFlag.TRUE.getCode());
        installAppsCmd.setNamespaceId(namespaceId);
        installAppsCmd.setOrganizationId(anotherOrgId);
        ListServiceModuleAppsByOrganizationIdResponse installAppsResp = serviceModuleAppService.listServiceModuleAppsByOrganizationId(installAppsCmd);
        if(installAppsResp.getServiceModuleApps().size() > 0) {
            for(ServiceModuleAppDTO appDTO: installAppsResp.getServiceModuleApps()) {
                UninstallAppCommand uninstallCmd = new UninstallAppCommand();
                uninstallCmd.setOrgAppId(appDTO.getOrgAppId());
                serviceModuleAppService.uninstallApp(uninstallCmd);       
            }
        }
        installAppsResp = serviceModuleAppService.listServiceModuleAppsByOrganizationId(installAppsCmd);
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
            installCmd.setOriginId(cAppId);
            installCmd.setOrganizationId(anotherOrgId);
            serviceModuleAppService.installApp(installCmd);
        }
        installAppsResp = serviceModuleAppService.listServiceModuleAppsByOrganizationId(installAppsCmd);
        Assert.assertTrue(installAppsResp.getServiceModuleApps().size() == communityOriginAppIds.size());
        
        List<Long> projectIds = new ArrayList<Long>();
        projectIds.add(anotherProjectId);
        
        for(Long appId : communityOriginAppIds) {
            DistributeServiceModuleAppAuthorizationCommand distrubeCmd = new DistributeServiceModuleAppAuthorizationCommand();
            distrubeCmd.setAppId(appId);
            distrubeCmd.setNamespaceId(namespaceId);
            distrubeCmd.setOwnerId(anotherOrgId);
            distrubeCmd.setProjectIds(projectIds);
            distrubeCmd.setToOrgId(anotherOrgId);
            serviceModuleAppAuthorizationService.distributeServiceModuleAppAuthorization(distrubeCmd);    
        }
        
        List<Long> authCommunityIds = serviceModuleAppAuthorizationService.listCommunityRelationOfOrgId(UserContext.getCurrentNamespaceId(), anotherOrgId).stream().map(r->r.getProjectId()).collect(Collectors.toList());
        Assert.assertTrue(authCommunityIds.size() == projectIds.size());
        
        {
            DistributeServiceModuleAppAuthorizationCommand distrubeCmd = new DistributeServiceModuleAppAuthorizationCommand();
            distrubeCmd.setAppId(normalDisAppId);
            distrubeCmd.setNamespaceId(namespaceId);
            distrubeCmd.setOwnerId(anotherOrgId);
            distrubeCmd.setProjectIds(projectIds);
            distrubeCmd.setToOrgId(normalOrgId);
            serviceModuleAppAuthorizationService.distributeServiceModuleAppAuthorization(distrubeCmd);
        }
        List<Long> normalAuthCommunityIds = serviceModuleAppAuthorizationService.listCommunityRelationOfOrgId(UserContext.getCurrentNamespaceId(), normalOrgId).stream().map(r->r.getProjectId()).collect(Collectors.toList());
        Assert.assertTrue(normalAuthCommunityIds.size() == 2);
        
        ListUserRelatedProjectByModuleCommand relatedProjectCmd = new ListUserRelatedProjectByModuleCommand();
        relatedProjectCmd.setAppId(normalDisAppId);
        relatedProjectCmd.setCommunityFetchType(CommunityFetchType.ONLY_COMMUNITY.getCode());
        relatedProjectCmd.setOrganizationId(anotherOrgId);
        List<ProjectDTO> projects = serviceModuleService.listUserRelatedProjectByModuleId(relatedProjectCmd);
        Assert.assertTrue(projects.size() == 0);
        
        relatedProjectCmd = new ListUserRelatedProjectByModuleCommand();
        relatedProjectCmd.setAppId(normalDisAppId);
        relatedProjectCmd.setCommunityFetchType(CommunityFetchType.ONLY_COMMUNITY.getCode());
        relatedProjectCmd.setOrganizationId(normalOrgId);
        projects = serviceModuleService.listUserRelatedProjectByModuleId(relatedProjectCmd);
        Assert.assertTrue(projects.size() == 2);  
    }
    
    @Test
    public void testServiceModuleForum() {
        Long normalDisAppId = 114457l;//论坛
        Long normalOrgId = 1041162l;
        Long anotherOrgId = 1041258l;

        {
            Long anotherProjectId = 240111044332060169l;//之俊大厦
            List<Long> projectIds = new ArrayList<Long>();
            projectIds.add(anotherProjectId);
            
            DistributeServiceModuleAppAuthorizationCommand distrubeCmd = new DistributeServiceModuleAppAuthorizationCommand();
            distrubeCmd.setAppId(normalDisAppId);
            distrubeCmd.setNamespaceId(namespaceId);
            distrubeCmd.setOwnerId(organizationId);
            distrubeCmd.setProjectIds(projectIds);
            distrubeCmd.setToOrgId(normalOrgId);
            serviceModuleAppAuthorizationService.distributeServiceModuleAppAuthorization(distrubeCmd);
        }
        
        {
            Long anotherProjectId = 240111044332060180l;//之俊大厦2
            List<Long> projectIds = new ArrayList<Long>();
            projectIds.add(anotherProjectId);
            
            DistributeServiceModuleAppAuthorizationCommand distrubeCmd = new DistributeServiceModuleAppAuthorizationCommand();
            distrubeCmd.setAppId(normalDisAppId);
            distrubeCmd.setNamespaceId(namespaceId);
            distrubeCmd.setOwnerId(anotherOrgId);
            distrubeCmd.setProjectIds(projectIds);
            distrubeCmd.setToOrgId(normalOrgId);
            serviceModuleAppAuthorizationService.distributeServiceModuleAppAuthorization(distrubeCmd);
        }
    }
    
    @Test
    public void testServiceModuleQuery() {
        Long normalOrgId = 1041162l;
        Long normalDisAppId = 115084l;//资产管理
        Long anotherOrgId = 1041258l;
        
        List<Long> normalAuthCommunityIds = serviceModuleAppAuthorizationService.listCommunityRelationOfOrgId(UserContext.getCurrentNamespaceId(), normalOrgId).stream().map(r->r.getProjectId()).collect(Collectors.toList());
        Assert.assertTrue(normalAuthCommunityIds.size() == 2);
        
        ListUserRelatedProjectByModuleCommand relatedProjectCmd = new ListUserRelatedProjectByModuleCommand();
        relatedProjectCmd.setAppId(normalDisAppId);
        relatedProjectCmd.setCommunityFetchType(CommunityFetchType.ONLY_COMMUNITY.getCode());
        relatedProjectCmd.setOrganizationId(anotherOrgId);
        List<ProjectDTO> projects = serviceModuleService.listUserRelatedProjectByModuleId(relatedProjectCmd);
        Assert.assertTrue(projects.size() == 0);
        
        relatedProjectCmd = new ListUserRelatedProjectByModuleCommand();
        relatedProjectCmd.setAppId(normalDisAppId);
        relatedProjectCmd.setCommunityFetchType(CommunityFetchType.ONLY_COMMUNITY.getCode());
        relatedProjectCmd.setOrganizationId(normalOrgId);
        projects = serviceModuleService.listUserRelatedProjectByModuleId(relatedProjectCmd);
        Assert.assertTrue(projects.size() == 2);  
    }
    
    @Test
    public void testOPApp() {
        Long normalOrgId = 1041162l;
        Long cAppId = 114493l; // 管理员管理
        InstallAppCommand installCmd = new InstallAppCommand();
        installCmd.setOriginId(cAppId);
        installCmd.setOrganizationId(normalOrgId);
        serviceModuleAppService.installApp(installCmd);
    }
    
    @Test
    public void testTOTP() throws NoSuchAlgorithmException, InvalidKeyException {
        final TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator(30l, TimeUnit.SECONDS, 8);
        final TimeBasedOneTimePasswordGenerator totp2 = new TimeBasedOneTimePasswordGenerator(30l, TimeUnit.SECONDS, 8);

        final Key secretKey;
        final KeyGenerator keyGenerator = KeyGenerator.getInstance(totp.getAlgorithm());

        // SHA-1 and SHA-256 prefer 64-byte (512-bit) keys; SHA512 prefers 128-byte keys
        keyGenerator.init(512);

        secretKey = keyGenerator.generateKey();
//        secretKey.getEncoded();

        final Date now = new Date();
        final Date later = new Date(now.getTime() + TimeUnit.SECONDS.toMillis(28));

        System.out.format("Current password: %08d\n", totp.generateOneTimePassword(secretKey, now));
        
        SecretKeySpec key2 = new SecretKeySpec(secretKey.getEncoded(), totp2.getAlgorithm());
        System.out.format("Future password:  %08d\n", totp2.generateOneTimePassword(key2, later));

    }
}
