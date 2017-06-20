package com.everhomes.userOrganization;

/**
 * Created by Administrator on 2017/6/19.
 */
public interface UserOrganizationProvider {

    public void createUserOrganization(UserOrganization userOrganization);

    public void updateUserOrganization(UserOrganization userOrganization);

    public UserOrganization findUserOrganization(Integer namespaceId, Long organizationId, Long userId);

}
