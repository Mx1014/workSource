// @formatter:off
package com.everhomes.portal;

import java.util.List;

public interface PortalPublishLogProvider {

	void createPortalPublishLog(PortalPublishLog portalPublishLog);

	void updatePortalPublishLog(PortalPublishLog portalPublishLog);

	PortalPublishLog findPortalPublishLogById(Long id);

	List<PortalPublishLog> listPortalPublishLog();

}