// @formatter:off
package com.everhomes.portal;

import java.util.List;

public interface PortalItemProvider {

	void createPortalItem(PortalItem portalItem);

	void updatePortalItem(PortalItem portalItem);

	PortalItem findPortalItemById(Long id);

	List<PortalItem> listPortalItem();

}