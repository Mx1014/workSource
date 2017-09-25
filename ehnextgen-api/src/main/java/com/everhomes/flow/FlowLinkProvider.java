// @formatter:off
package com.everhomes.flow;

import java.util.List;

public interface FlowLinkProvider {

	void createFlowLink(FlowLink flowLink);

	void updateFlowLink(FlowLink flowLink);

	FlowLink findById(Long id);

    void deleteFlowLink(Long flowMainId, Integer flowVersion);

    List<FlowLink> listFlowLink(Long flowMainId, Integer flowVersion);

    List<FlowLink> listFlowLinkByToNodeId(Long toNodeId, Integer flowVersion);

    List<FlowLink> listFlowLinkByFromNodeId(Long fromNodeId, Integer flowVersion);
}