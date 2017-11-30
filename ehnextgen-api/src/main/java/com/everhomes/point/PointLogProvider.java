// @formatter:off
package com.everhomes.point;

import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.point.ListPointLogsCommand;

import java.util.List;

public interface PointLogProvider {

	void createPointLog(PointLog pointLog);

	void updatePointLog(PointLog pointLog);

	PointLog findById(Long id);

    List<PointLog> listPointLogs(ListPointLogsCommand cmd, ListingLocator locator);
}