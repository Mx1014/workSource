// @formatter:off
package com.everhomes.userOrganization;

import com.everhomes.rest.userOrganization.CreateUserOrganizationCommand;

public interface UserOrganizationService {

    public UserOrganization createUserOrganization(CreateUserOrganizationCommand cmd);

}