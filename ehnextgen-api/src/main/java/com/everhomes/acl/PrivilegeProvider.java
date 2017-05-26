// @formatter:off
package com.everhomes.acl;

import java.util.List;

public interface PrivilegeProvider{

	List<Role> getRolesByOwnerAndKeywords(int namespaceId, long appId, String ownerType, Long ownerId, String keywords);

	List<Acl> listAcls(String ownerType, Long ownerId, String targetType, Long targetId);

	List<Acl> listAclsByScope(String ownerType, Long ownerId, String targetType, Long targetId, String scope);

	List<Acl> listAclsByModuleId(String ownerType, Long ownerId, String targetType, Long targetId, Long moduleId);

	List<Acl> listAclsByTag(String tag);

	void deleteAclsByTag(String tag);
}
