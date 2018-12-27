// @formatter:off
package com.everhomes.portal;

import java.util.List;

public interface PortalNavigationBarProvider {

	void createPortalNavigationBar(PortalNavigationBar portalNavigationBar);

	void updatePortalNavigationBar(PortalNavigationBar portalNavigationBar);

	void deletePortalNavigationBar(Long id);

	PortalNavigationBar findPortalNavigationBarById(Long id);

	List<PortalNavigationBar> listPortalNavigationBar(Long versionId);

	List<PortalNavigationBar> listPortalNavigationBarByOrder(Long versionId, Integer order);

	Integer maxOrder(Integer namespaceId, Long versionId);
}