package com.everhomes.flow;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface FlowEvaluateProvider {

	Long createFlowEvaluate(FlowEvaluate obj);

	void updateFlowEvaluate(FlowEvaluate obj);

	void deleteFlowEvaluate(FlowEvaluate obj);

	FlowEvaluate getFlowEvaluateById(Long id);

	List<FlowEvaluate> queryFlowEvaluates(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	List<FlowEvaluate> findEvaluates(Long flowCaseId, Long flowMainId,
			Integer flowVersion);

	void createFlowEvaluate(List<FlowEvaluate> objs);

}
