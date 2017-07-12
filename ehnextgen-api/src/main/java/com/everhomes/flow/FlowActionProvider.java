package com.everhomes.flow;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface FlowActionProvider {

	Long createFlowAction(FlowAction obj);

	void updateFlowAction(FlowAction obj);

	void deleteFlowAction(FlowAction obj);

	FlowAction getFlowActionById(Long id);

	List<FlowAction> queryFlowActions(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	FlowAction findFlowActionByBelong(Long belong, String entityType,
			String actionType, String actionStepType, String flowStepType);

	List<FlowAction> findFlowActionsByBelong(Long belong, String entityType,
			String actionType, String actionStepType, String flowStepType);

}
