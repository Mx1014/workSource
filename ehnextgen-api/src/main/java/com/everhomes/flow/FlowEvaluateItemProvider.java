package com.everhomes.flow;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface FlowEvaluateItemProvider {

	Long createFlowEvaluateItem(FlowEvaluateItem obj);

	void updateFlowEvaluateItem(FlowEvaluateItem obj);

	void deleteFlowEvaluateItem(FlowEvaluateItem obj);

	FlowEvaluateItem getFlowEvaluateItemById(Long id);

	List<FlowEvaluateItem> queryFlowEvaluateItems(ListingLocator locator,
			int count, ListingQueryBuilderCallback queryBuilderCallback);

	List<FlowEvaluateItem> findFlowEvaluateItemsByFlowId(Long flowId,
			Integer flowVer);

	void deleteFlowEvaluateItem(List<FlowEvaluateItem> objs);

	void createFlowEvaluateItem(List<FlowEvaluateItem> objs);

}
