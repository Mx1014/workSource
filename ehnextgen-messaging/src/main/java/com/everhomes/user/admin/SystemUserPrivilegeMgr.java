package com.everhomes.user.admin;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.acl.*;
import com.everhomes.domain.Domain;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModulePrivilege;
import com.everhomes.module.ServiceModulePrivilegeType;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.blacklist.BlacklistErrorCode;
import com.everhomes.rest.common.PortalType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.user.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.entity.EntityType;
import com.everhomes.util.RuntimeErrorException;

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

    @Override
    public boolean checkModuleAdmin(Long userId, String ownerType, Long ownerId, Long privilegeId){

        List<ServiceModulePrivilege> serviceModules = serviceModuleProvider.listServiceModulePrivilegesByPrivilegeId(privilegeId, ServiceModulePrivilegeType.ORDINARY);
        if(0 < serviceModules.size()){
            //校验是否拥有模块管理权限
            ServiceModule module = serviceModuleProvider.findServiceModuleById(serviceModules.get(0).getModuleId());
            return checkModuleAdmin(ownerType, ownerId, userId, module.getParentId());
        }
        //校验是否拥有全部模块的管理权限
        List<AclRoleDescriptor> descriptors = new ArrayList<>();
        descriptors.add(new AclRoleDescriptor(EntityType.USER.getCode(), userId));
        return aclProvider.checkAccessEx(ownerType, ownerId, PrivilegeConstants.ALL_SERVICE_MODULE, descriptors);
    }

    @Override
    public boolean checkModuleAdmin(String ownerType, Long ownerId, Long userId, Long moduleId){
        if(checkModuleAccess(ownerType, ownerId, userId, moduleId, ServiceModulePrivilegeType.SUPER)){
            return true;
        }
        return checkAllModuleAdmin(ownerType, ownerId,userId);
    }

    @Override
    public boolean checkModuleAllPrivileges(String ownerType, Long ownerId, Long userId, Long privilegeId){
        List<ServiceModulePrivilege> serviceModules = serviceModuleProvider.listServiceModulePrivilegesByPrivilegeId(privilegeId, ServiceModulePrivilegeType.ORDINARY);
        if(0 < serviceModules.size()){
            ServiceModule module = serviceModuleProvider.findServiceModuleById(serviceModules.get(0).getModuleId());
            return checkModuleAccess(ownerType, ownerId, userId, module.getParentId(), ServiceModulePrivilegeType.ORDINARY_ALL);
        }
        return false;
    }

    @Override
    public boolean checkModuleAllPrivileges(String ownerType, Long ownerId, List<AclRoleDescriptor> descriptors, Long privilegeId){
        List<ServiceModulePrivilege> serviceModules = serviceModuleProvider.listServiceModulePrivilegesByPrivilegeId(privilegeId, ServiceModulePrivilegeType.ORDINARY);
        if(0 < serviceModules.size()){
            ServiceModule module = serviceModuleProvider.findServiceModuleById(serviceModules.get(0).getModuleId());
            return checkModuleAccess(ownerType, ownerId, descriptors, module.getParentId(), ServiceModulePrivilegeType.ORDINARY_ALL);
        }
        return false;
    }

    @Override
    public boolean checkAllModuleAdmin(String ownerType, Long ownerId, Long userId){
        List<AclRoleDescriptor> descriptors = new ArrayList<>();
        descriptors.add(new AclRoleDescriptor(EntityType.USER.getCode(), userId));
        if(aclProvider.checkAccessEx(ownerType, ownerId, PrivilegeConstants.ALL_SERVICE_MODULE, descriptors)){
            return true;
        }
        return false;
    }


    @Override
    public boolean checkModuleAccess(String ownerType, Long ownerId, Long userId, Long moduleId, ServiceModulePrivilegeType type){
        List<AclRoleDescriptor> descriptors = new ArrayList<>();
        descriptors.add(new AclRoleDescriptor(EntityType.USER.getCode(), userId));
        if(checkModuleAccess(ownerType, ownerId, descriptors, moduleId, type)){
            return true;
        }
        return false;
    }

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

    @Override
    public boolean checkOrganizationAdmin(Long userId, Long currentOrgId){
        List<AclRoleDescriptor> descriptors = new ArrayList<>();
        AclRoleDescriptor descriptor = new AclRoleDescriptor(EntityType.USER.getCode(), userId);
        descriptors.add(descriptor);
        return aclProvider.checkAccessEx(EntityType.ORGANIZATIONS.getCode(), currentOrgId, PrivilegeConstants.ORGANIZATION_ADMIN, descriptors);
    }


    /**
     * 校验权限
     * @param ownerType
     * @param ownerId
     * @param currentOrgId
     * @param privilegeId
     * @return
     */
    private boolean checkAccess(Long userId, String ownerType, Long ownerId, Long currentOrgId, Long privilegeId){
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

        //校验全部范围下是否拥有模块的全部权限
        return checkModuleAllPrivileges(EntityType.ALL.getCode(), 0L, descriptors, privilegeId);
    }



    @Override
    public boolean checkUserPrivilege(Long userId, String ownerType, Long ownerId, Long currentOrgId, Long privilegeId){

        Domain domain = UserContext.current().getDomain();

        if(null != currentOrgId){
            if(null != currentOrgId){
                Organization organization = organizationProvider.findOrganizationById(currentOrgId);
                //子公司的时候 需要获取root 总公司的id
                if(0L != organization.getParentId()){
                    currentOrgId = Long.valueOf(organization.getPath().split("/")[1]);
                }
                if(null != organization){
                    if(OrganizationType.PM == OrganizationType.fromCode(organization.getOrganizationType())){
                        if(checkSuperAdmin(userId, currentOrgId)){
                            LOGGER.debug("check super admin privilege success.userId={}, ownerType={}, ownerId={}, organizationId={}, privilegeId={}" , userId, ownerType, ownerId, currentOrgId, privilegeId);
                            return true;
                        }
                        if(checkModuleAdmin(userId, EntityType.ORGANIZATIONS.getCode(), currentOrgId, privilegeId)){
                            LOGGER.debug("check module admin privilege success.userId={}, ownerType={}, ownerId={}, organizationId={}, privilegeId={}" , userId, ownerType, ownerId, currentOrgId, privilegeId);
                            return true;
                        }
                    }else{
                        if(checkOrganizationAdmin(userId, currentOrgId)){
                            LOGGER.debug("check organization admin privilege success.userId={}, ownerType={}, ownerId={}, organizationId={}, privilegeId={}" , userId, ownerType, ownerId, currentOrgId, privilegeId);
                            return true;
                        }
                    }
                }else{
                    LOGGER.error("Unable to find the organization.organizationId = {}",  currentOrgId);
                }

            }
        }else if(null != domain && PortalType.fromCode(domain.getPortalType()) == PortalType.ZUOLIN){
            if(checkRoleAccess(userId, ownerType, ownerId, null, privilegeId)){
                LOGGER.debug("check role privilege success.userId={}, ownerType={}, ownerId={}, currentOrgId = {}, privilegeId={}" , userId, ownerType, ownerId, currentOrgId, privilegeId);
                return true;
            }
        }

        if(checkAccess(userId, ownerType, ownerId, currentOrgId, privilegeId)){
            LOGGER.debug("check privilege success.userId={}, ownerType={}, ownerId={}, organizationId={}, privilegeId={}" , userId, ownerType, ownerId, currentOrgId, privilegeId);
            return true;
        }
        LOGGER.debug("check privilege error. userId={}, ownerType={}, ownerId={}, currentOrgId = {}, privilegeId={}" , userId, ownerType, ownerId, currentOrgId, privilegeId);
        return false;
    }

    @Override
    public void checkUserAuthority(Long userId, String ownerType, Long ownerId, Long currentOrgId, Long privilegeId){


        if(null == userId){
            userId = UserContext.current().getUser().getId();
        }

        if(checkUserPrivilege(userId, ownerType, ownerId, currentOrgId, privilegeId)){
            LOGGER.debug("authority success...");
            return;
        }
        LOGGER.error("Insufficient privilege, privilegeId={}, currentOrgId = {}", privilegeId, currentOrgId);
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }

    @Override
    public void checkCurrentUserAuthority(String ownerType, Long ownerId, Long privilegeId){
        checkUserAuthority(null, ownerType, ownerId, null, privilegeId);
    }

    @Override
    public void checkCurrentUserAuthority(Long currentOrgId, Long privilegeId){
        checkUserAuthority(null, null, null, currentOrgId, privilegeId);
    }

    @Override
    public void checkCurrentUserAuthority(String ownerType, Long ownerId, Long currentOrgId, Long privilegeId){
        checkUserAuthority(null, ownerType, ownerId, currentOrgId, privilegeId);
    }

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
