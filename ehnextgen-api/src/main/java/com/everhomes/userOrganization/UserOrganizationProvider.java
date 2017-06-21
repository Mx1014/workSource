package com.everhomes.userOrganization;

/**
 * Created by Administrator on 2017/6/19.
 */
public interface UserOrganizationProvider {

    void createUserOrganization(UserOrganization userOrganization);

    void updateUserOrganization(UserOrganization userOrganization);

    UserOrganization findUserOrganization(Integer namespaceId, Long organizationId, Long userId);

    void deleteUserOrganization(UserOrganization userOrganization);

    UserOrganization inactiveUserOrganization(UserOrganization userOrganization);
}
