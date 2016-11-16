package com.everhomes.flow;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface FlowCaseProvider {

	Long createFlowCase(FlowCase obj);

	void updateFlowCase(FlowCase obj);

	void deleteFlowCase(FlowCase obj);

	FlowCase getFlowCaseById(Long id);

	List<FlowCase> queryFlowCases(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

}
