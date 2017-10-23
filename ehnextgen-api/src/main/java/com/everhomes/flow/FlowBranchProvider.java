// @formatter:off
package com.everhomes.flow;

import java.util.List;

public interface FlowBranchProvider {

	void createFlowBranch(FlowBranch flowBranch);

	void updateFlowBranch(FlowBranch flowBranch);

	FlowBranch findById(Long id);

    void deleteFlowBranch(Long flowMainId, Integer flowVersion);

    FlowBranch findBranch(Long originalNodeId);

    List<FlowBranch> findByFlowId(Long flowId, Integer flowVersion);
}