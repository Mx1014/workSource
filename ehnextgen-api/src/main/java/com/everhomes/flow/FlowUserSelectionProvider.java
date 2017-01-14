package com.everhomes.flow;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface FlowUserSelectionProvider {

	Long createFlowUserSelection(FlowUserSelection obj);

	void updateFlowUserSelection(FlowUserSelection obj);

	void deleteFlowUserSelection(FlowUserSelection obj);

	FlowUserSelection getFlowUserSelectionById(Long id);

	List<FlowUserSelection> queryFlowUserSelections(ListingLocator locator,
			int count, ListingQueryBuilderCallback queryBuilderCallback);

	List<FlowUserSelection> findSelectionByBelong(Long belongId,
			String belongEntity, String flowUserBelongType);

	List<FlowUserSelection> deleteSelectionByBelong(Long belongId, String belongEntity,
			String flowUserBelongType);

	List<FlowUserSelection> findSelectionByBelong(Long belongId,
			String belongEntity, String flowUserBelongType, Integer ver);

	FlowUserSelection findFlowNodeSelectionUser(Long flowNodeId, Integer ver,
			Long userId);

	void createFlowUserSelections(List<FlowUserSelection> objs);

	void deleteFlowUserSelections(List<FlowUserSelection> objs);

}
