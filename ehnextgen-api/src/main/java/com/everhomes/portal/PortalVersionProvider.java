// @formatter:off
package com.everhomes.portal;

import java.util.List;

public interface PortalVersionProvider {

	void createPortalVersion(PortalVersion portalVersion);

	void updatePortalVersion(PortalVersion portalVersion);

	PortalVersion findPortalVersionById(Long id);

	PortalVersion findMaxVersion(Integer namespaceId);

	List<PortalVersion> listPortalVersion(Integer namespaceId, Byte status);


}