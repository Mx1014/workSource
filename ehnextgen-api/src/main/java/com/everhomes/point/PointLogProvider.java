// @formatter:off
package com.everhomes.point;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.point.ListPointLogsCommand;

import java.util.List;

public interface PointLogProvider {

	void createPointLog(PointLog pointLog);

	void updatePointLog(PointLog pointLog);

    List<PointLog> query(ListingLocator locator, int count, ListingQueryBuilderCallback callback);

    List<PointLog> listPointLogs(ListPointLogsCommand cmd, ListingLocator locator);

    PointLog findById(Long id);

    PointLog findByUidAndEntity(Integer namespaceId, Long uid, String eventName, String entityType, Long entityId);

    Integer countPointLog(Integer namespaceId, Long systemId, Long uid, String eventName, Long createTime);

    PointLog findByRuleIdAndEntity(Integer namespaceId, Long uid, Long ruleId, String entityType, Long entityId);
}