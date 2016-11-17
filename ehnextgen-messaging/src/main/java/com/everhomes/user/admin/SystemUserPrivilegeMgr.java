package com.everhomes.user.admin;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.acl.*;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationServiceErrorCode;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.serviceModule.ServiceModulePrivilege;
import com.everhomes.serviceModule.ServiceModulePrivilegeType;
import com.everhomes.serviceModule.ServiceModuleProvider;
import com.everhomes.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.entity.EntityType;
import com.everhomes.server.schema.tables.pojos.EhUsers;
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
    private boolean checkOrganizationRoleAccess(Long userId, Long organizationId, Long privilegeId){
        List<RoleAssignment> roleAssignments = rolePrivilegeService.getUserAllOrgRoles(organizationId, userId);

        List<AclRoleDescriptor> descriptors = new ArrayList<>();
        for (RoleAssignment roleAssignment: roleAssignments) {
            AclRoleDescriptor descriptor = new AclRoleDescriptor(EntityType.ROLE.getCode(), roleAssignment.getRoleId());
            descriptors.add(descriptor);
        }
        return aclProvider.checkAccessEx(EntityType.ORGANIZATIONS.getCode(), organizationId, privilegeId, descriptors);
    }

    /**
     * 校验模块管理员权限
     * @param ownerType
     * @param ownerId
     * @param organizationId
     * @param privilegeId
     * @return
     */
    private boolean checkModuleAdmin(Long userId, String ownerType, Long ownerId, Long organizationId, Long privilegeId){
        List<ServiceModulePrivilege> serviceModules = serviceModuleProvider.listServiceModulePrivilegesByPrivilegeId(privilegeId, ServiceModulePrivilegeType.ORDINARY);
        List<ServiceModulePrivilege> moduleAdmins = new ArrayList<>();
        for (ServiceModulePrivilege serviceModule:serviceModules) {
            moduleAdmins.addAll(serviceModuleProvider.listServiceModulePrivilegesByPrivilegeId(serviceModule.getPrivilegeId(), ServiceModulePrivilegeType.ORDINARY));
        }

        for (ServiceModulePrivilege moduleAdmin:moduleAdmins) {
            if(checkAccess(userId, ownerType, ownerId, organizationId, moduleAdmin.getPrivilegeId())){
                return true;
            }
        }
        return false;
    }

    /**
     * 校验超级管理员
     * @param organizationId
     * @return
     */
    private boolean checkSuperAdmin(Long userId, Long organizationId){
        List<AclRoleDescriptor> descriptors = new ArrayList<>();
        AclRoleDescriptor descriptor = new AclRoleDescriptor(EntityType.USER.getCode(), userId);
        descriptors.add(descriptor);
        return aclProvider.checkAccessEx(EntityType.ORGANIZATIONS.getCode(), organizationId, PrivilegeConstants.ORGANIZATION_SUPER_ADMIN, descriptors);
    }

    /**
     * 校验超级管理员
     * @param organizationId
     * @return
     */
    private boolean checkOrganizationAdmin(Long userId, Long organizationId){
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

        Organization organization = organizationProvider.findOrganizationById(organizationId);

        if(null == organization){
            LOGGER.debug("user organization is null..");
            return false;
        }

        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(userId, IdentifierType.MOBILE.getCode());

        if(null == userIdentifier){
            LOGGER.debug("user identifierToken is null..");
            return false;
        }
        List<OrganizationDTO> orgDTOs = organizationService.getOrganizationMemberGroups(OrganizationGroupType.DEPARTMENT, userIdentifier.getIdentifierToken(), organization.getPath());

        List<AclRoleDescriptor> descriptors = new ArrayList<>();
        for (OrganizationDTO orgDTO: orgDTOs) {
            AclRoleDescriptor descriptor = new AclRoleDescriptor(EntityType.ORGANIZATIONS.getCode(), orgDTO.getId());
            descriptors.add(descriptor);
        }

        AclRoleDescriptor descriptor = new AclRoleDescriptor(EntityType.USER.getCode(), userId);
        descriptors.add(descriptor);


        if(aclProvider.checkAccessEx(EntityType.ORGANIZATIONS.getCode(), organizationId, privilegeId, descriptors)){
            return true;
        }

        return aclProvider.checkAccessEx(ownerType, ownerId, privilegeId, descriptors);
    }

    @Override
    public void checkUserAuthority(Long userId, String ownerType, Long ownerId, Long organizationId, Long privilegeId){

        if(checkSuperAdmin(userId, organizationId)){
            LOGGER.debug("check super admin privilege success...");
            return;
        }

        if(checkModuleAdmin(userId, ownerType, ownerId, organizationId, privilegeId)){
            LOGGER.debug("check module admin privilege success...");
            return;
        }

        if(checkAccess(userId, ownerType, ownerId, organizationId, privilegeId)){
            LOGGER.debug("check privilege success...");
            return;
        }

        if(checkOrganizationRoleAccess(userId, organizationId, privilegeId)){
            LOGGER.debug("check role privilege success...");
            return;
        }

        LOGGER.error("Insufficient privilege, privilegeId={}, organizationId = {}", privilegeId, organizationId);
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                "Insufficient privilege");
    }
    
}
