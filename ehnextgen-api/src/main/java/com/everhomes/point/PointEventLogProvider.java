// @formatter:off
package com.everhomes.point;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.util.List;

public interface PointEventLogProvider {

	void createPointEventLog(PointEventLog pointEventLog);

	void updatePointEventLog(PointEventLog pointEventLog);

    List<PointEventLog> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback);

    Long getNextEventLogId();

    void createPointEventLogsWithId(List<PointEventLog> removeLogs);

    PointEventLog findById(Long id);

    List<PointEventLog> listEventLog(Long categoryId, Byte status, int pageSize, ListingLocator locator);

    void updatePointEventLogStatus(List<Long> idList, Byte status);
}