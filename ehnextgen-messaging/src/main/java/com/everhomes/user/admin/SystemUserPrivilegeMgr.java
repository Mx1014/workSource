package com.everhomes.user.admin;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.acl.*;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationServiceErrorCode;
import com.everhomes.rest.user.IdentifierType;
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



    @Override
    public void checkUserPrivilege(long userId, long ownerId) {
        ResourceUserRoleResolver resolver = PlatformContext.getComponent(EntityType.USER.getCode());
        List<Long> roles = resolver.determineRoleInResource(userId, null, EntityType.USER.getCode(), null);
        if(!this.aclProvider.checkAccess("system", null, EntityType.USER.getCode(),
                userId, Privilege.All, roles))
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
                    "Insufficient privilege");
    }

    private boolean checkOrganizationRoleAccess(Long organizationId, Long privilegeId){
        User user = UserContext.current().getUser();
        List<RoleAssignment> roleAssignments = rolePrivilegeService.getUserAllOrgRoles(organizationId, user.getId());

        List<AclRoleDescriptor> descriptors = new ArrayList<>();
        for (RoleAssignment roleAssignment: roleAssignments) {
            AclRoleDescriptor descriptor = new AclRoleDescriptor(EntityType.ROLE.getCode(), roleAssignment.getRoleId());
            descriptors.add(descriptor);
        }
        boolean flag = aclProvider.checkAccessEx(EntityType.ORGANIZATIONS.getCode(), organizationId, privilegeId, descriptors);
        return flag;
    }



    private boolean checkAccess(String ownerType, Long ownerId, Long organizationId, Long privilegeId){
        User user = UserContext.current().getUser();

        Organization organization = organizationProvider.findOrganizationById(organizationId);

        if(null == organization){
            LOGGER.debug("user organization is null..");
            return false;
        }

        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());

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

        AclRoleDescriptor descriptor = new AclRoleDescriptor(EntityType.USER.getCode(), user.getId());
        descriptors.add(descriptor);

        boolean flag = aclProvider.checkAccessEx(EntityType.ORGANIZATIONS.getCode(), organizationId, privilegeId, descriptors);

        if(!flag){
            return aclProvider.checkAccessEx(ownerType, ownerId, privilegeId, descriptors);
        }
        return flag;
    }

    @Override
    public boolean checkUserAuthority(String ownerType, Long ownerId, Long organizationId, Long privilegeId){
        if(!checkOrganizationRoleAccess(organizationId, privilegeId)){
            LOGGER.error("non-privileged, privilegeId={}, organizationId = {}", privilegeId, organizationId);
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_NO_PRIVILEGED,
                    "non-privileged.");
        }

        if(!checkAccess(ownerType, ownerId, organizationId, privilegeId)){
            LOGGER.error("non-privileged, ownerType = {}, ownerId = {}, privilegeId={}, organizationId = {}", ownerType, ownerId, privilegeId, organizationId);
            throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_NO_PRIVILEGED,
                    "non-privileged.");
        }
        return true;
    }
    
}
