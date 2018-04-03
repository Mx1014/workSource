// @formatter:off
package com.everhomes.userOrganization;

import com.everhomes.rest.userOrganization.CreateUserOrganizationCommand;

public interface UserOrganizationService {

    public UserOrganizations createUserOrganizations(CreateUserOrganizationCommand cmd);

}