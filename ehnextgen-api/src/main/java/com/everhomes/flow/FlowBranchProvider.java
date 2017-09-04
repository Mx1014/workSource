// @formatter:off
package com.everhomes.flow;

public interface FlowBranchProvider {

	void createFlowBranch(FlowBranch flowBranch);

	void updateFlowBranch(FlowBranch flowBranch);

	FlowBranch findById(Long id);

    void deleteFlowBranch(Long flowMainId, Integer flowVersion);

    FlowBranch findBranch(Long originalNodeId);
}