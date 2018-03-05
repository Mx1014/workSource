// @formatter:off
package com.everhomes.portal;

import java.util.List;

public interface PortalVersionProvider {

	void createPortalVersion(PortalVersion portalVersion);

	void updatePortalVersion(PortalVersion portalVersion);

	PortalVersion findPortalVersionById(Long id);

    void deleteById(Long id);

    PortalVersion findMaxBigVersion(Integer namespaceId);

	PortalVersion findReleaseVersion(Integer namespaceId);

	PortalVersion findPreviewVersion(Integer namespaceId);

	List<PortalVersion> listPortalVersion(Integer namespaceId, Byte status);


}