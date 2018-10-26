package com.everhomes.user.admin;

import com.everhomes.acl.*;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.domain.Domain;
import com.everhomes.entity.EntityType;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModulePrivilege;
import com.everhomes.module.ServiceModulePrivilegeType;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.portal.PortalService;
import com.everhomes.rest.acl.IdentityType;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.acl.PrivilegeServiceErrorCode;
import com.everhomes.rest.acl.ServiceModuleCategory;
import com.everhomes.rest.blacklist.BlacklistErrorCode;
import com.everhomes.rest.common.AllFlagType;
import com.everhomes.rest.common.IncludeChildFlagType;
import com.everhomes.rest.module.ControlTarget;
import com.everhomes.rest.oauth2.ControlTargetOption;
import com.everhomes.rest.oauth2.ModuleManagementType;
import com.everhomes.rest.order.OwnerType;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.portal.ListServiceModuleAppsCommand;
import com.everhomes.rest.portal.ListServiceModuleAppsResponse;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppProvider;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.entity.EntityType;
import com.everhomes.util.RuntimeErrorException;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component("SystemUser")
public class SystemUserPrivilegeMgr implements UserPrivilegeMgr {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemUserPrivilegeMgr.class);

    @Autowired
    private UserProvider userProvider;
    @Autowired
    private AclProvider aclProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @Autowired
    private ServiceModuleProvider serviceModuleProvider;

    @Autowired
    private ServiceModuleAppProvider serviceModuleAppProvider;

    @Autowired
    private AuthorizationProvider authorizationProvider;

    @Autowired
    private PortalService portalService;

    @Autowired
    private ServiceModuleAppService serviceModuleAppService;

    @Autowired
    private ServiceModuleAppAuthorizationService serviceModuleAppAuthorizationService;


    @Override
    @Deprecated
    public void checkUserPrivilege(long userId, long ownerId) {
        ResourceUserRoleResolver resolver = PlatformContext.getComponent(EntityType.USER.getCode());
        List<Long> roles = resolver.determineRoleInResource(userId, null, EntityType.USER.getCode(), null);
        if(!this.aclProvider.checkAccess("system", null, EntityType.USER.getCode(),
                userId, Privilege.All, roles))
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                    "Insufficient privilege");
    }

    /**
     * 校验角色权限
     * @param currentOrgId
     * @param privilegeId
     * @return
     */
    @Override
    public boolean checkRoleAccess(Long userId, String ownerType, Long ownerId, Long currentOrgId, Long privilegeId){

        if(null == EntityType.fromCode(ownerType) && null == ownerId && null == currentOrgId){
            return false;
        }

        List<AclRoleDescriptor> descriptors = new ArrayList<>();
        List<RoleAssignment> roleAssignments = null;

        if(null != currentOrgId){
            roleAssignments = rolePrivilegeService.getUserAllOrgRoles(currentOrgId, userId);
            for (RoleAssignment roleAssignment: roleAssignments) {
                AclRoleDescriptor descriptor = new AclRoleDescriptor(EntityType.ROLE.getCode(), roleAssignment.getRoleId());
                descriptors.add(descriptor);
            }

            if(aclProvider.checkAccessEx(EntityType.ORGANIZATIONS.getCode(), currentOrgId, privilegeId, descriptors)){
                return true;
            }
        }

        roleAssignments = aclProvider.getRoleAssignmentByResourceAndTarget(ownerType, ownerId, EntityType.USER.getCode(), userId);
        descriptors = new ArrayList<>();
        for (RoleAssignment roleAssignment: roleAssignments) {
            AclRoleDescriptor descriptor = new AclRoleDescriptor(EntityType.ROLE.getCode(), roleAssignment.getRoleId());
            descriptors.add(descriptor);
        }
        return aclProvider.checkAccessEx(ownerType, ownerId, privilegeId, descriptors);
    }
    
    private ServiceModule findPrefixServiceModule(Long subModuleId, int cnt) {
    	ServiceModule module = serviceModuleProvider.findServiceModuleById(subModuleId);
    	if(module == null) {
    		return null;
    	} else if(ServiceModuleCategory.MODULE.getCode().equals(module.getCategory())) {
    		return module;
    	} else if(cnt > 50 || module.getId().equals(subModuleId) || module.getParentId() == null || module.getParentId().equals(0l)) {
    		//loop detach
    		return null;
    	} else {
    		return findPrefixServiceModule(module.getParentId(), cnt+1);
    	}
    }
    
    private ServiceModule findPrefixServiceModule(Long subModuleId) {
    	return findPrefixServiceModule(subModuleId, 0);
    }

    //by lei.lv 没有模块管理员了，只保留检查接口，随时可能干掉
//    @Override
//    @Deprecated
//    public boolean checkModuleAdmin(Long userId, String ownerType, Long ownerId, Long privilegeId){
//
//        List<ServiceModulePrivilege> serviceModules = serviceModuleProvider.listServiceModulePrivilegesByPrivilegeId(privilegeId, ServiceModulePrivilegeType.ORDINARY);
//        if(0 < serviceModules.size()){
//            //校验是否拥有模块管理权限
//            ServiceModule module = serviceModuleProvider.findServiceModuleById(serviceModules.get(0).getModuleId());
//            Long moduleId = module.getId();
//            if(module.getLevel() > 2){
//                moduleId = module.getParentId();
//            }
//            return checkModuleAdmin(ownerType, ownerId, userId, moduleId);
//        }
//        //校验是否拥有全部模块的管理权限
//        List<AclRoleDescriptor> descriptors = new ArrayList<>();
//        descriptors.add(new AclRoleDescriptor(EntityType.USER.getCode(), userId));
//        return aclProvider.checkAccessEx(ownerType, ownerId, PrivilegeConstants.ALL_SERVICE_MODULE, descriptors);
//    }

//    @Override
//    @Deprecated
//    public boolean checkModuleAdmin(String ownerType, Long ownerId, Long userId, Long moduleId){
//        if(checkModuleAccess(ownerType, ownerId, userId, moduleId, ServiceModulePrivilegeType.SUPER)){
//            return true;
//        }
//        return checkAllModuleAdmin(ownerType, ownerId,userId);
//    }

//    @Override
//    public boolean checkModuleAllPrivileges(String ownerType, Long ownerId, Long userId, Long privilegeId){
//        List<ServiceModulePrivilege> serviceModules = serviceModuleProvider.listServiceModulePrivilegesByPrivilegeId(privilegeId, ServiceModulePrivilegeType.ORDINARY);
//        if(0 < serviceModules.size()){
//            ServiceModule module = serviceModuleProvider.findServiceModuleById(serviceModules.get(0).getModuleId());
//            return checkModuleAccess(ownerType, ownerId, userId, module.getParentId(), ServiceModulePrivilegeType.ORDINARY_ALL);
//        }
//        return false;
//    }

    @Override
    public boolean checkModuleAllPrivileges(String ownerType, Long ownerId, List<AclRoleDescriptor> descriptors, Long privilegeId){
        List<ServiceModulePrivilege> serviceModules = serviceModuleProvider.listServiceModulePrivilegesByPrivilegeId(privilegeId, ServiceModulePrivilegeType.ORDINARY);
        if(0 < serviceModules.size()){
            ServiceModule module = serviceModuleProvider.findServiceModuleById(serviceModules.get(0).getModuleId());
            return checkModuleAccess(ownerType, ownerId, descriptors, module.getParentId(), ServiceModulePrivilegeType.ORDINARY_ALL);
        }
        return false;
    }

//    @Override
//    @Deprecated
//    public boolean checkAllModuleAdmin(String ownerType, Long ownerId, Long userId){
//        List<AclRoleDescriptor> descriptors = new ArrayList<>();
//        descriptors.add(new AclRoleDescriptor(EntityType.USER.getCode(), userId));
//        if(aclProvider.checkAccessEx(ownerType, ownerId, PrivilegeConstants.ALL_SERVICE_MODULE, descriptors)){
//            return true;
//        }
//        return false;
//    }


//    @Override
//    public boolean checkModuleAccess(String ownerType, Long ownerId, Long userId, Long moduleId, ServiceModulePrivilegeType type){
//        List<AclRoleDescriptor> descriptors = new ArrayList<>();
//        descriptors.add(new AclRoleDescriptor(EntityType.USER.getCode(), userId));
//        if(checkModuleAccess(ownerType, ownerId, descriptors, moduleId, type)){
//            return true;
//        }
//        return false;
//    }

    @Override
    public boolean checkModuleAccess(String ownerType, Long ownerId, List<AclRoleDescriptor> descriptors, Long moduleId, ServiceModulePrivilegeType type){
        List<ServiceModulePrivilege> moduleAdmins = serviceModuleProvider.listServiceModulePrivileges(moduleId, type);
        for (ServiceModulePrivilege moduleAdmin:moduleAdmins) {
            if(aclProvider.checkAccessEx(ownerType, ownerId, moduleAdmin.getPrivilegeId(), descriptors)){
                return true;
            }
        }
        return false;
    }

    // 按appId校验应用管理员（应用Id）
    @Override
    public boolean checkModuleAppAdmin(String ownerType, Long ownerId, Long userId, Long privilegeId, Long appId, Long communityId, Long organizationId){
        Integer namespaceId = UserContext.current().getNamespaceId();
        // 查询privilegeId关联的模块
        Long p_moduleId = 0L;
        List<ServiceModulePrivilege> serviceModules = serviceModuleProvider.listServiceModulePrivilegesByPrivilegeId(privilegeId, ServiceModulePrivilegeType.ORDINARY);
        if(0 < serviceModules.size()){
            ServiceModule module = serviceModuleProvider.findServiceModuleById(serviceModules.get(0).getModuleId());
            p_moduleId = module.getId();
            if(!ServiceModuleCategory.MODULE.getCode().equals(module.getCategory())){
            	ServiceModule pmodule = findPrefixServiceModule(module.getParentId());
            	if(pmodule != null) {
            		p_moduleId = pmodule.getId();
            	}
            }
        }
        // 查询app关联的moduleId
        ServiceModuleApp app  = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(appId);
        if(app != null && p_moduleId != 0L && p_moduleId.longValue() == app.getModuleId().longValue()){//如果权限对应的moduleId和appId对应的模块Id相等，再校验是否对应用有权
          return checkModuleAppAdmin(namespaceId, ownerType, ownerId, userId, p_moduleId, appId, communityId, organizationId);
        }
        return false;
    }

    @Override
    public boolean checkModuleAppAdmin(Integer namespaceId, String ownerType, Long ownerId, Long userId, Long moduleId, Long appId, Long communityId, Long organizationId) {
        if(moduleId != null && appId != null){
            // 检查模块对应范围内（园区控制、OA控制、无限制控制）的全部应用权限
            ServiceModule module = this.serviceModuleProvider.findServiceModuleById(moduleId);
            Authorization authorization_target = new Authorization();
            List<Authorization> authorizations_Total = this.authorizationProvider.listAuthorizations(ownerType, ownerId, OwnerType.USER.getCode(), userId, EntityType.SERVICE_MODULE_APP.getCode(), 0L, IdentityType.MANAGE.getCode(), 0L, module.getModuleControlType(), AllFlagType.YES.getCode(), false);
            if(authorizations_Total.size() > 0){
                authorization_target = authorizations_Total.get(0);
            }else{ // 如果是单个分配的应用权限
                List<Authorization> authorizations = this.authorizationProvider.listAuthorizations(ownerType, ownerId, OwnerType.USER.getCode(), userId, EntityType.SERVICE_MODULE_APP.getCode(), moduleId, IdentityType.MANAGE.getCode(), appId, null, null, false);
                if (authorizations.size() == 0){
                    return false;
                }else {
                    authorization_target = authorizations.get(0);
                }
            }

            List<ControlTarget> controlTargets = this.authorizationProvider.listAuthorizationControlConfigs(userId, authorization_target.getControlId());
            Byte controlOption = authorization_target.getControlOption();

            if(authorization_target != null ){
                switch (ModuleManagementType.fromCode(authorization_target.getModuleControlType())){
                    case COMMUNITY_CONTROL:
                        if(controlOption == ControlTargetOption.ALL_COMMUNITY.getCode()){//配置为全园区时，返回true
                            return true;
                        }
                        if(communityId != null && communityId != 0L){
                            for (ControlTarget controlTarget : controlTargets) {
                                if(controlTarget.getId().equals(communityId)){
                                    return true;
                                }
                            }
                        }
                        break;
                    case ORG_CONTROL:
                        if(organizationId != null && organizationId != 0L){
                            List<Long> orgIds = new ArrayList<>();
                            List<String> orgPaths = new ArrayList<>();
                            for (ControlTarget controlTarget : controlTargets) {
                                if(controlTarget.getIncludeChildFlag() == IncludeChildFlagType.YES.getCode()){
                                    Organization org = this.organizationProvider.findOrganizationById(controlTarget.getId());
                                    if(org != null)
                                        orgPaths.add(org.getPath());
                                }else{
                                    orgIds.add(controlTarget.getId());
                                }
                            }

                            List orgs = this.organizationProvider.checkOrgExistInOrgOrPaths(namespaceId, organizationId, orgIds, orgPaths);
                            if(orgs != null && orgs.size() > 0){
                                return true;
                            }else{
                                return false;
                            }
                        }
                    case UNLIMIT_CONTROL:
                        return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean checkModuleAppAdmin(Integer namespaceId, Long organizationId, Long userId, Long appId) {
//        ServiceModuleApp app = this.serviceModuleProvider.findReflectionServiceModuleAppByActiveAppId(appId);
        ServiceModuleApp app  = serviceModuleAppService.findReleaseServiceModuleAppByOriginId(appId);
        if(app == null)
            return false;
        ServiceModule serviceModule = this.serviceModuleProvider.findServiceModuleById(app.getModuleId());
        if(serviceModule != null){
            List<Authorization> authorizations_Total =  this.authorizationProvider.listAuthorizations(EntityType.ORGANIZATIONS.getCode(), organizationId, OwnerType.USER.getCode(), userId, EntityType.SERVICE_MODULE_APP.getCode(), 0L, IdentityType.MANAGE.getCode(), 0L, serviceModule.getModuleControlType(), AllFlagType.YES.getCode(), false);
            if(authorizations_Total.size() > 0){
                return true;
            }
            List<Authorization> authorizations = this.authorizationProvider.listAuthorizations(EntityType.ORGANIZATIONS.getCode(), organizationId, OwnerType.USER.getCode(), userId, EntityType.SERVICE_MODULE_APP.getCode(), serviceModule.getId(), IdentityType.MANAGE.getCode(), appId, null, null, false);
            if(authorizations.size() > 0){
                return true;
            }
        }
        return false;
    }

//    @Override
//    public boolean checkModuleAppRelation(Integer namespaceId, Long communityId, Long userId, Long appId) {
//        List<Target> targets = new ArrayList<>();
//        targets.add(new Target(com.everhomes.entity.EntityType.USER.getCode(), userId));
//        //获取人员的所有相关机构
//        List<Long> orgIds = organizationService.getIncludeOrganizationIdsByUserId(userId, organizationId);
//        for (Long orgId: orgIds) {
//            targets.add(new Target(com.everhomes.entity.EntityType.ORGANIZATIONS.getCode(), orgId));
//        }
//
//        List<Authorization> listAuthorizations(EntityType.COMMUNITY, communityId, String targetType, Long targetId, null, Long authId, String identityType, Boolean targetFlag)
//        listAuthorizations(EntityType.COMMUNITY, communityId, String targetType, Long targetId, null, null, ServiceModulePrivilegeType.ORDINARY.getCode(), appId, null, null, false)
//        return false;
//    }

    @Override
    public boolean checkUserPrivilege(Long userId, Long currentOrgId, Long privilegeId, Long moduleId, Byte actionType, String customTag, Long checkOrgId, Long checkCommunityId) {
        ListServiceModuleAppsCommand cmd = new ListServiceModuleAppsCommand();
        cmd.setActionType(actionType);
        cmd.setModuleId(moduleId);
        cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
        cmd.setCustomTag(customTag);
        ListServiceModuleAppsResponse apps = portalService.listServiceModuleAppsWithConditon(cmd);
        Long appId = null;
        if(null != apps && apps.getServiceModuleApps().size() > 0){
            appId = apps.getServiceModuleApps().get(0).getOriginId();
        }
        LOGGER.debug("checkUserPrivilege get appId = {}", appId);
        if(appId ==  null){
            throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_CHECK_APP_PRIVILEGE,
                    "cannot find app for it, namespaceId = "+UserContext.getCurrentNamespaceId()+",  moduleId = "+moduleId+", customTag = "+customTag);
        }
        if(currentOrgId == null){
            OrganizationMember member = organizationProvider.findAnyOrganizationMemberByNamespaceIdAndUserId(UserContext.getCurrentNamespaceId(), userId, OrganizationType.ENTERPRISE.getCode());
            if(member != null  && !StringUtils.isEmpty(member.getGroupPath())){
                currentOrgId = Long.valueOf(member.getGroupPath().split("/")[1]);
            }
        }
        if(!checkUserPrivilege(userId, EntityType.ORGANIZATIONS.getCode(), currentOrgId, currentOrgId, privilegeId, appId, checkOrgId,  checkCommunityId)){
            throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_CHECK_APP_PRIVILEGE,
                    "check app privilege error");
        }else{
            return true;
        }
    }

    @Override
    public boolean checkUserPrivilege(Long userId, Long currentOrgId, Long privilegeId, Long appId, Long checkOrgId, Long checkCommunityId) {
        LOGGER.debug("checkUserPrivilege get appId = {}", appId);
        if(appId ==  null){
            throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_CHECK_APP_PRIVILEGE,
                    "cannot find app for it, namespaceId = "+UserContext.getCurrentNamespaceId()+",  appId " + appId);
        }
        if(currentOrgId == null){
            OrganizationMember member = organizationProvider.findAnyOrganizationMemberByNamespaceIdAndUserId(UserContext.getCurrentNamespaceId(), userId, OrganizationType.ENTERPRISE.getCode());
            if(member != null  && !StringUtils.isEmpty(member.getGroupPath())){
                currentOrgId = Long.valueOf(member.getGroupPath().split("/")[1]);
            }
        }
        if(!checkUserPrivilege(userId, EntityType.ORGANIZATIONS.getCode(), currentOrgId, currentOrgId, privilegeId, appId, checkOrgId,  checkCommunityId)){
            throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_CHECK_APP_PRIVILEGE,
                    "check app privilege error");
        }else{
            return true;
        }
    }


    @Override
    public boolean checkSuperAdmin(Long userId, Long currentOrgId){
        if(null == currentOrgId){
            return false;
        }
        List<AclRoleDescriptor> descriptors = new ArrayList<>();
        AclRoleDescriptor descriptor = new AclRoleDescriptor(EntityType.USER.getCode(), userId);
        descriptors.add(descriptor);
        return aclProvider.checkAccessEx(EntityType.ORGANIZATIONS.getCode(), currentOrgId, PrivilegeConstants.ORGANIZATION_SUPER_ADMIN, descriptors);
    }

//    @Override
//    @Deprecated
//    public boolean checkOrganizationAdmin(Long userId, Long currentOrgId){
//        List<AclRoleDescriptor> descriptors = new ArrayList<>();
//        AclRoleDescriptor descriptor = new AclRoleDescriptor(EntityType.USER.getCode(), userId);
//        descriptors.add(descriptor);
//        return aclProvider.checkAccessEx(EntityType.ORGANIZATIONS.getCode(), currentOrgId, PrivilegeConstants.ORGANIZATION_ADMIN, descriptors);
//    }


    /**
     * 校验权限
     * @param ownerType
     * @param ownerId
     * @param currentOrgId
     * @param privilegeId
     * @return
     */
    private boolean checkAccess(Long userId, String ownerType, Long ownerId, Long currentOrgId, Long privilegeId){

        // modify start --凡是与community有关的校验，需要先在orgId管理的园区内查找，如果没有就直接返回失败
        if(ownerType.equals(EntityType.COMMUNITY.getCode()) && !serviceModuleAppAuthorizationService.checkCommunityRelationOfOrgId(UserContext.getCurrentNamespaceId(), currentOrgId, ownerId)){
            return false;
        }

        List<AclRoleDescriptor> descriptors = new ArrayList<>();
        descriptors.add(new AclRoleDescriptor(EntityType.USER.getCode(), userId));
        if(null != currentOrgId){
            //获取用户所关联的机构id，包括所属机构的所有上级
            List<Long> organizationIds =  organizationService.getIncludeOrganizationIdsByUserId(userId, currentOrgId);
            LOGGER.debug("user organizationIds:{}", organizationIds);
            for (Long id: organizationIds) {
                descriptors.add(new AclRoleDescriptor(EntityType.ORGANIZATIONS.getCode(), id));
            }
//            if(aclProvider.checkAccessEx(EntityType.ORGANIZATIONS.getCode(), organizationId, privilegeId, descriptors)){
//                return true;
//            }else if(EntityType.fromCode(ownerType) == EntityType.ORGANIZATIONS || null == EntityType.fromCode(ownerType) || null == ownerId){
//                return false;
//            }
        }
        if(null != EntityType.fromCode(ownerType) && null != ownerId){
            //校验具体范围下是否拥有具体权限
            if(aclProvider.checkAccessEx(ownerType, ownerId, privilegeId, descriptors)){
                return true;
            }

            //校验具体范围下是否拥有模块的全部权限
            if(checkModuleAllPrivileges(ownerType, ownerId, descriptors, privilegeId)){
                return true;
            }

        }

        //校验全部范围下是否拥有权限
        if(aclProvider.checkAccessEx(EntityType.ALL.getCode(), 0L, privilegeId, descriptors)){
            return true;
        }

        //校验全部范围下是auth_warehouse_wentian-delta-data-release.sql否拥有模块的全部权限
        return checkModuleAllPrivileges(EntityType.ALL.getCode(), 0L, descriptors, privilegeId);
    }



    @Override
    public boolean checkUserPrivilege(Long userId, String ownerType, Long ownerId, Long currentOrgId, Long privilegeId){
        return  checkUserPrivilege(userId, ownerType, ownerId, currentOrgId, privilegeId, null, null, null);
    }

    @Override
    public boolean checkUserPrivilege(Long userId, String ownerType, Long ownerId, Long currentOrgId, Long privilegeId, Long appId, Long checkOrgId, Long checkCommunityId){
        LOGGER.debug("checkUserPrivilege start.userId={}, ownerType={}, ownerId={}, organizationId={}, privilegeId={}, appId={}, checkOrgId={}, checkCommunityId= {}" , userId, ownerType, ownerId, currentOrgId, privilegeId, appId, checkOrgId, checkCommunityId);

        Domain domain = UserContext.current().getDomain();

        if(currentOrgId == null || (null != domain && EntityType.fromCode(domain.getPortalType()) == EntityType.ZUOLIN_ADMIN)) {//如果currentOrgId为空，则按照用户-角色体系查询
            if (this.aclProvider.checkAccess("system", null, com.everhomes.server.schema.tables.pojos.EhUsers.class.getSimpleName(), UserContext.current().getUser().getId(), Privilege.Write, null)) {
                LOGGER.debug("check root privilege success.userId={}, ownerType={}, ownerId={}, organizationId={}, privilegeId={}", userId, ownerType, ownerId, currentOrgId, privilegeId);
                return true;
            }
            
            if(checkRoleAccess(userId, ownerType, ownerId, null, privilegeId)){
                LOGGER.debug("check role privilege success.userId={}, ownerType={}, ownerId={}, currentOrgId = {}, privilegeId={}" , userId, ownerType, ownerId, currentOrgId, privilegeId);
                return true;
            }
        } else if(null != currentOrgId){
            Organization organization = organizationProvider.findOrganizationById(currentOrgId);
            //子公司的时候 需要获取root 总公司的id
            if(0L != organization.getParentId()){
                currentOrgId = Long.valueOf(organization.getPath().split("/")[1]);
            }
            if(null != organization){
                if (this.aclProvider.checkAccess("system", null, com.everhomes.server.schema.tables.pojos.EhUsers.class.getSimpleName(), UserContext.current().getUser().getId(), Privilege.Write, null)) {
                        LOGGER.debug("check root privilege success.userId={}, ownerType={}, ownerId={}, organizationId={}, privilegeId={}", userId, ownerType, ownerId, currentOrgId, privilegeId);
                        return true;
                    }

                // modify start
                if(checkCommunityId != null && checkCommunityId != 0L && checkCommunityId != -1L){
                    if(!serviceModuleAppAuthorizationService.checkCommunityRelationOfOrgId(UserContext.getCurrentNamespaceId(), currentOrgId, checkCommunityId))
                        return false;
                }
                // modify end

                if(checkSuperAdmin(userId, currentOrgId)){
                    LOGGER.debug("check super admin privilege success.userId={}, ownerType={}, ownerId={}, organizationId={}, privilegeId={}" , userId, ownerType, ownerId, currentOrgId, privilegeId);
                    return true;
                }
                // 当需要校验appId，by lei.lv
                if(appId != null){
                    if(checkModuleAppAdmin(ownerType, ownerId, userId, privilegeId, appId, checkCommunityId, checkOrgId)){
                        LOGGER.debug("check moduleApp admin privilege success.userId={}, ownerType={}, ownerId={}, organizationId={}, checkCommunityId={}, checkOrgId={}, privilegeId={}, appId={}" , userId, ownerType, ownerId, currentOrgId, checkCommunityId, checkOrgId, privilegeId, appId);
                        return true;
                    }

                    // 先校验一下园区类权限细化
                    if(checkAccess(userId, EntityType.COMMUNITY.getCode(), checkCommunityId, currentOrgId, privilegeId)){
                        LOGGER.debug("check moduleApp relation privilege success.userId={}, ownerType={}, ownerId={}, organizationId={}, checkCommunityId={}, checkOrgId={}, privilegeId={}, appId={}" , userId, ownerType, ownerId, currentOrgId, checkCommunityId, checkOrgId, privilegeId, appId);
                        return true;
                    }

                    // 再兼容其他的权限赋值
                    if(checkAccess(userId, ownerType, ownerId, currentOrgId, privilegeId)){
                        LOGGER.debug("check privilege success.userId={}, ownerType={}, ownerId={}, organizationId={}, privilegeId={}" , userId, ownerType, ownerId, currentOrgId, privilegeId);
                        return true;
                    }
                }
            }else{
                LOGGER.error("Unable to find the organization.organizationId = {}",  currentOrgId);
            }

        }

        LOGGER.debug("check privilege error. userId={}, ownerType={}, ownerId={}, currentOrgId = {}, privilegeId={}" , userId, ownerType, ownerId, currentOrgId, privilegeId);
        return false;
    }

    @Override
    public void checkUserAuthority(Long userId, String ownerType, Long ownerId, Long currentOrgId, Long privilegeId){
        if(null == userId){
            userId = UserContext.current().getUser().getId();
        }

        ResourceUserRoleResolver resolver = PlatformContext.getComponent(EntityType.USER.getCode());
        List<Long> roles = resolver.determineRoleInResource(userId, null, EntityType.USER.getCode(), null);
        if(!aclProvider.checkAccess("system", null, EntityType.USER.getCode(),
                userId, Privilege.All, roles)){
            if(checkUserPrivilege(userId, ownerType, ownerId, currentOrgId, privilegeId)){
                LOGGER.debug("authority success...");
                return;
            }
            LOGGER.error("Insufficient privilege, privilegeId={}, currentOrgId = {}", privilegeId, currentOrgId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                    "Insufficient privilege");
        }
    }

//    @Override
//    public void checkCurrentUserAuthority(String ownerType, Long ownerId, Long privilegeId){
//        checkUserAuthority(null, ownerType, ownerId, null, privilegeId);
//    }

    @Override
    public void checkCurrentUserAuthority(Long currentOrgId, Long privilegeId){
        checkUserAuthority(null, null, null, currentOrgId, privilegeId);
    }

//    @Override
//    public void checkCurrentUserAuthority(String ownerType, Long ownerId, Long currentOrgId, Long privilegeId){
//        checkUserAuthority(null, ownerType, ownerId, currentOrgId, privilegeId);
//    }

    @Override
    public void checkUserBlacklistAuthority(Long userId, String ownerType, Long ownerId, Long privilegeId){

        List<AclRoleDescriptor> descriptors = new ArrayList<>();
        AclRoleDescriptor descriptor = new AclRoleDescriptor(EntityType.USER.getCode(), userId);
        descriptors.add(descriptor);

        if(aclProvider.checkAccessEx(ownerType, ownerId, privilegeId, descriptors)){
            LOGGER.error("Permission is prohibited, userId={}, privilegeId = {}", userId, privilegeId);
            throw RuntimeErrorException.errorWith(BlacklistErrorCode.SCOPE, BlacklistErrorCode.ERROR_FORBIDDEN_PERMISSIONS,
                    "Permission is prohibited");
        }
    }
    
}
