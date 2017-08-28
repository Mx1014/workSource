// @formatter:off
package com.everhomes.portal;

import java.util.List;

public interface PortalItemCategryProvider {

	void createPortalItemCategry(PortalItemCategry portalItemCategry);

	void updatePortalItemCategry(PortalItemCategry portalItemCategry);

	PortalItemCategry findPortalItemCategryById(Long id);

	List<PortalItemCategry> listPortalItemCategry();

}