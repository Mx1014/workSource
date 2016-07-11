package com.everhomes.search;

import java.util.List;

import com.everhomes.organization.OrganizationMember;
import com.everhomes.rest.videoconf.ListUsersWithoutVideoConfPrivilegeCommand;
import com.everhomes.rest.videoconf.ListUsersWithoutVideoConfPrivilegeResponse;

public interface UserWithoutConfAccountSearcher {

	void deleteById(Long id);
    void bulkUpdate(List<OrganizationMember> members);
    void feedDoc(OrganizationMember member);
    void syncFromDb();
    ListUsersWithoutVideoConfPrivilegeResponse query(ListUsersWithoutVideoConfPrivilegeCommand cmd);
}
