// @formatter:off
package com.everhomes.portal;

import java.util.List;

public interface PortalItemGroupProvider {

	void createPortalItemGroup(PortalItemGroup portalItemGroup);

	void updatePortalItemGroup(PortalItemGroup portalItemGroup);

    void deleteByVersionId(Long versionId);

    PortalItemGroup findPortalItemGroupById(Long id);

    Integer findMaxDefaultOrder(Long layoutId);

    List<PortalItemGroup> listPortalItemGroup(Long layoutId);

	void createPortalItemGroups(List<PortalItemGroup> portalItemGroups);

    List<PortalItemGroup> listPortalItemGroupByVersion(Integer namespaceId, Long versionId);

    List<PortalItemGroup> listPortalItemGroupByWidgetAndStyle(Integer namespaceId, Long versionId, String widget, String style);

    List<PortalItemGroup> listBannerItemGroupByAppId(Long appId);
}