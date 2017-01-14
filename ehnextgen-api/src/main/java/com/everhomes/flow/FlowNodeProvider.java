package com.everhomes.flow;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface FlowNodeProvider {

	Long createFlowNode(FlowNode obj);

	void updateFlowNode(FlowNode obj);

	void deleteFlowNode(FlowNode obj);

	FlowNode getFlowNodeById(Long id);

	List<FlowNode> queryFlowNodes(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);

	public FlowNode findFlowNodeByName(Long flowMainId, Integer flowVersion, String nodeName);

	List<FlowNode> findFlowNodesByFlowId(Long flowMainId, Integer flowVersion);

}
