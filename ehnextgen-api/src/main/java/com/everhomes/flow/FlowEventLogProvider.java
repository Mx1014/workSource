package com.everhomes.flow;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface FlowEventLogProvider {

	Long createFlowEventLog(FlowEventLog obj);

	void updateFlowEventLog(FlowEventLog obj);

	void deleteFlowEventLog(FlowEventLog obj);

	FlowEventLog getFlowEventLogById(Long id);

	List<FlowEventLog> queryFlowEventLogs(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

}
