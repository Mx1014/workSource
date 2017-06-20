// @formatter:off
package com.everhomes.userOrganization;

import com.everhomes.rest.userOrganization.CreateUserOrganizationCommand;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class userOrganizationServiceImpl implements UserOrganizationService {
    @Autowired
    private UserOrganizationProvider userOrganizationProvider;

    @Override
    public UserOrganization createUserOrganization(CreateUserOrganizationCommand cmd) {
        UserOrganization userOrganization = ConvertHelper.convert(cmd, UserOrganization.class);
        this.userOrganizationProvider.createUserOrganization(userOrganization);
        return userOrganization;
    }

}