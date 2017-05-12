// @formatter:off
package com.everhomes.acl;

import java.util.List;

public interface PrivilegeProvider{

	List<Role> getRolesByOwnerAndKeywords(int namespaceId, long appId, String ownerType, Long ownerId, String keywords);

}
