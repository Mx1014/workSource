package com.everhomes.flow;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface FlowStatProvider {

	Long createFlowStat(FlowStat obj);

	void updateFlowStat(FlowStat obj);

	void deleteFlowStat(FlowStat obj);

	FlowStat getFlowStatById(Long id);

	List<FlowStat> queryFlowStats(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

}
