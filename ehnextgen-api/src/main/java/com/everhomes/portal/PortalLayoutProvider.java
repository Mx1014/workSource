// @formatter:off
package com.everhomes.portal;

import java.util.List;

public interface PortalLayoutProvider {

	void createPortalLayout(PortalLayout portalLayout);

    void createPortalLayouts(List<PortalLayout> portalLayouts);

    void updatePortalLayout(PortalLayout portalLayout);

	PortalLayout findPortalLayoutById(Long id);

	List<PortalLayout> listPortalLayout(Integer namespaceId, String name, Long versionId);

	PortalLayout getPortalLayout(Integer namespaceId, String name, Long versionId);

    List<PortalLayout> listPortalLayoutByVersion(Integer namespaceId, Long versionId);

    PortalLayout findIndexPortalLayout(Long versionId, Byte type);

    void deleteByVersionId(Long versionId);
}