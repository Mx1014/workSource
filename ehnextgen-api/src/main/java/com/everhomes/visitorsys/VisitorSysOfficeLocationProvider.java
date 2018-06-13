// @formatter:off
package com.everhomes.visitorsys;

import java.util.List;

public interface VisitorSysOfficeLocationProvider {

	void createVisitorSysOfficeLocation(VisitorSysOfficeLocation visitorSysOfficeLocation);

	void updateVisitorSysOfficeLocation(VisitorSysOfficeLocation visitorSysOfficeLocation);

	VisitorSysOfficeLocation findVisitorSysOfficeLocationById(Long id);

	List<VisitorSysOfficeLocation> listVisitorSysOfficeLocation();

	List<VisitorSysOfficeLocation> listVisitorSysOfficeLocation(Integer namespaceId, String ownerType, Long ownerId, Integer pageSize, Long pageAnchor);

    void deleteVisitorSysOfficeLocation(Integer namespaceId, Long id);
}