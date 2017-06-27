// @formatter:off
package com.everhomes.userOrganization;

import com.everhomes.rest.userOrganization.CreateUserOrganizationCommand;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserOrganizationServiceImpl implements UserOrganizationService {
    @Autowired
    private UserOrganizationProvider userOrganizationProvider;

    @Override
    public UserOrganizations createUserOrganizations(CreateUserOrganizationCommand cmd) {
        UserOrganizations userOrganizations = ConvertHelper.convert(cmd, UserOrganizations.class);
        this.userOrganizationProvider.createUserOrganizations(userOrganizations);
        return userOrganizations;
    }

}