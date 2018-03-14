// @formatter:off
package com.everhomes.portal;

import java.util.List;

public interface PortalVersionUserProvider {

	void createPortalVersionUser(PortalVersionUser portalVersionUser);

    void deleteByVersionId(Long versionId);

    void deletePortalVersionUsers(Integer namespaceId);

    PortalVersionUser findPortalVersionUserById(Long id);

	List<PortalVersionUser> listPortalVersionUsers(Integer namespaceId, Long versionId);

	PortalVersionUser findPortalVersionUserByUserId(Long userId);
}