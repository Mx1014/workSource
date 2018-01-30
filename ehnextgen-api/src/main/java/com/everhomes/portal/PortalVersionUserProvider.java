// @formatter:off
package com.everhomes.portal;

import java.util.List;

public interface PortalVersionUserProvider {

	void createPortalVersionUser(PortalVersionUser portalVersionUser);

	void deletePortalVersionUser(Long id);

	PortalVersionUser findPortalVersionUserById(Long id);

	List<PortalVersionUser> listPortalVersionUsers(Integer namespaceId, Long versionId);

	PortalVersionUser findPortalVersionUserByUserId(Long userId);
}