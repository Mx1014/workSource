// @formatter:off
package com.everhomes.visitorsys;

import java.util.List;

public interface VisitorSysOfficeLocationProvider {

	void createVisitorSysOfficeLocation(VisitorSysOfficeLocation visitorSysOfficeLocation);

	void updateVisitorSysOfficeLocation(VisitorSysOfficeLocation visitorSysOfficeLocation);

	VisitorSysOfficeLocation findVisitorSysOfficeLocationById(Long id);

	List<VisitorSysOfficeLocation> listVisitorSysOfficeLocation();

}