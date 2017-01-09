package com.everhomes.flow;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface FlowTimeoutProvider {

	Long createFlowTimeout(FlowTimeout obj);

	void updateFlowTimeout(FlowTimeout obj);

	void deleteFlowTimeout(FlowTimeout obj);

	FlowTimeout getFlowTimeoutById(Long id);

	List<FlowTimeout> queryFlowTimeouts(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	List<FlowTimeout> queryValids(ListingLocator locator, int count);

	boolean deleteIfValid(Long timeoutId);

}
