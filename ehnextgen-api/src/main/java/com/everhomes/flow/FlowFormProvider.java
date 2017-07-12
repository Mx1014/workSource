package com.everhomes.flow;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface FlowFormProvider {

	Long createFlowForm(FlowForm obj);

	void updateFlowForm(FlowForm obj);

	void deleteFlowForm(FlowForm obj);

	FlowForm getFlowFormById(Long id);

	List<FlowForm> queryFlowForms(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

}
