package com.everhomes.flow;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface FlowButtonProvider {

	Long createFlowButton(FlowButton obj);

	void updateFlowButton(FlowButton obj);

	void deleteFlowButton(FlowButton obj);

	FlowButton getFlowButtonById(Long id);

	List<FlowButton> queryFlowButtons(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

}
