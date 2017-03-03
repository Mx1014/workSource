package com.everhomes.user.admin;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.acl.*;
import com.everhomes.module.ServiceModulePrivilege;
import com.everhomes.module.ServiceModulePrivilegeType;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.blacklist.BlacklistErrorCode;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
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
     * @param organizationId
     * @param privilegeId
     * @return
     */
    @Override
    public boolean checkRoleAccess(Long userId, String ownerType, Long ownerId, Long organizationId, Long privilegeId){
        List<RoleAssignment> roleAssignments = rolePrivilegeService.getUserAllOrgRoles(organizationId, userId);
        List<AclRoleDescriptor> descriptors = new ArrayList<>();
        for (RoleAssignment roleAssignment: roleAssignments) {
            AclRoleDescriptor descriptor = new AclRoleDescriptor(EntityType.ROLE.getCode(), roleAssignment.getRoleId());
            descriptors.add(descriptor);
        }
        if(aclProvider.checkAccessEx(EntityType.ORGANIZATIONS.getCode(), organizationId, privilegeId, descriptors)){
            return true;
        }else if(EntityType.fromCode(ownerType) == EntityType.ORGANIZATIONS || null == EntityType.fromCode(ownerType) || null == ownerId ){
            return false;
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
    public boolean checkModuleAdmin(Long userId, String ownerType, Long ownerId, Long organizationId, Long privilegeId){
        List<ServiceModulePrivilege> serviceModules = serviceModuleProvider.listServiceModulePrivilegesByPrivilegeId(privilegeId, ServiceModulePrivilegeType.ORDINARY);
        List<Long> moduleIds = new ArrayList<>();
        for (ServiceModulePrivilege serviceModule: serviceModules) {
            if(!moduleIds.contains(serviceModule.getModuleId()))
                moduleIds.add(serviceModule.getModuleId());
        }
        if(0 < moduleIds.size()){
            List<ServiceModulePrivilege> moduleAdmins = serviceModuleProvider.listServiceModulePrivileges(moduleIds, ServiceModulePrivilegeType.SUPER);
            for (ServiceModulePrivilege moduleAdmin:moduleAdmins) {
                if(checkAccess(userId, ownerType, ownerId, organizationId, moduleAdmin.getPrivilegeId())){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean checkSuperAdmin(Long userId, Long organizationId){
        List<AclRoleDescriptor> descriptors = new ArrayList<>();
        AclRoleDescriptor descriptor = new AclRoleDescriptor(EntityType.USER.getCode(), userId);
        descriptors.add(descriptor);
        return aclProvider.checkAccessEx(EntityType.ORGANIZATIONS.getCode(), organizationId, PrivilegeConstants.ORGANIZATION_SUPER_ADMIN, descriptors);
    }

    @Override
    public boolean checkOrganizationAdmin(Long userId, Long organizationId){
        List<AclRoleDescriptor> descriptors = new ArrayList<>();
        AclRoleDescriptor descriptor = new AclRoleDescriptor(EntityType.USER.getCode(), userId);
        descriptors.add(descriptor);
        return aclProvider.checkAccessEx(EntityType.ORGANIZATIONS.getCode(), organizationId, PrivilegeConstants.ORGANIZATION_ADMIN, descriptors);
    }


    /**
     * 校验权限
     * @param ownerType
     * @param ownerId
     * @param organizationId
     * @param privilegeId
     * @return
     */
    private boolean checkAccess(Long userId, String ownerType, Long ownerId, Long organizationId, Long privilegeId){
        List<AclRoleDescriptor> descriptors = new ArrayList<>();
        descriptors.add(new AclRoleDescriptor(EntityType.USER.getCode(), userId));
        if(null != organizationId){
            List<String> groupTypes = new ArrayList<>();
            groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
            groupTypes.add(OrganizationGroupType.GROUP.getCode());
            groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
            List<OrganizationDTO> orgDTOs = organizationService.getOrganizationMemberGroups(groupTypes, userId, organizationId);
            LOGGER.debug("user organizations:{}", orgDTOs);

            for (OrganizationDTO orgDTO: orgDTOs) {
                descriptors.add(new AclRoleDescriptor(EntityType.ORGANIZATIONS.getCode(), orgDTO.getId()));
            }

            if(aclProvider.checkAccessEx(EntityType.ORGANIZATIONS.getCode(), organizationId, privilegeId, descriptors)){
                return true;
            }else if(EntityType.fromCode(ownerType) == EntityType.ORGANIZATIONS || null == EntityType.fromCode(ownerType) || null == ownerId){
                return false;
            }
        }
        return aclProvider.checkAccessEx(ownerType, ownerId, privilegeId, descriptors);
    }

    @Override
    public boolean checkUserPrivilege(Long userId, String ownerType, Long ownerId, Long organizationId, Long privilegeId){
        if(checkSuperAdmin(userId, organizationId)){
            LOGGER.debug("check super admin privilege success.userId={}, ownerType={}, ownerId={}, organizationId={}, privilegeId={}" , userId, ownerType, ownerId, organizationId, privilegeId);
            return true;
        }

        if(checkModuleAdmin(userId, ownerType, ownerId, organizationId, privilegeId)){
            LOGGER.debug("check module admin privilege success.userId={}, ownerType={}, ownerId={}, organizationId={}, privilegeId={}" , userId, ownerType, ownerId, organizationId, privilegeId);
            return true;
        }

        if(checkAccess(userId, ownerType, ownerId, organizationId, privilegeId)){
            LOGGER.debug("check privilege success.userId={}, ownerType={}, ownerId={}, organizationId={}, privilegeId={}" , userId, ownerType, ownerId, organizationId, privilegeId);
            return true;
        }

        if(checkRoleAccess(userId, ownerType, ownerId, organizationId, privilegeId)){
            LOGGER.debug("check role privilege success.userId={}, ownerType={}, ownerId={}, organizationId={}, privilegeId={}" , userId, ownerType, ownerId, organizationId, privilegeId);
            return true;
        }

        LOGGER.debug("check privilege error. userId={}, ownerType={}, ownerId={}, organizationId={}, privilegeId={}" , userId, ownerType, ownerId, organizationId, privilegeId);
        return false;
    }

    @Override
    public void checkUserAuthority(Long userId, String ownerType, Long ownerId, Long organizationId, Long privilegeId){
        if(checkUserPrivilege(userId, ownerType, ownerId, organizationId, privilegeId)){
            LOGGER.debug("authority success...");
            return;
        }
        LOGGER.error("Insufficient privilege, privilegeId={}, organizationId = {}", privilegeId, organizationId);
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
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
