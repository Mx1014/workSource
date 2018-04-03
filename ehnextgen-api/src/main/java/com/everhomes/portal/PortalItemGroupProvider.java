// @formatter:off
package com.everhomes.portal;

import java.util.List;

public interface PortalItemGroupProvider {

	void createPortalItemGroup(PortalItemGroup portalItemGroup);

	void updatePortalItemGroup(PortalItemGroup portalItemGroup);

	PortalItemGroup findPortalItemGroupById(Long id);

	List<PortalItemGroup> listPortalItemGroup(Long layoutId);

	void createPortalItemGroups(List<PortalItemGroup> portalItemGroups);

}