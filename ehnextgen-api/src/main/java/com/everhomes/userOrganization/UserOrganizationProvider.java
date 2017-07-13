package com.everhomes.userOrganization;

/**
 * Created by Administrator on 2017/6/19.
 */
public interface UserOrganizationProvider {

    void createUserOrganizations(UserOrganizations userOrganizations);

    void updateUserOrganizations(UserOrganizations userOrganizations);

    UserOrganizations findUserOrganizations(Integer namespaceId, Long organizationId, Long userId);

    void deleteUserOrganizations(UserOrganizations userOrganizations);

    UserOrganizations inactiveUserOrganizations(UserOrganizations userOrganizations);

    UserOrganizations rejectUserOrganizations(UserOrganizations userOrganizations);
}
