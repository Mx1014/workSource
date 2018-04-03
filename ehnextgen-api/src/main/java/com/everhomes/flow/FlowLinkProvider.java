// @formatter:off
package com.everhomes.flow;

import java.util.List;

public interface FlowLinkProvider {

	void createFlowLink(FlowLink flowLink);

	void updateFlowLink(FlowLink flowLink);

	FlowLink findById(Long id);

    void deleteFlowLink(Long flowMainId, Integer flowVersion);

    List<FlowLink> listFlowLink(Long flowMainId, Integer flowVersion);

    List<FlowLink> listFlowLinkByToNodeId(Long flowMainId, Integer flowVersion, Long toNodeId);

    List<FlowLink> listFlowLinkByFromNodeId(Long flowMainId, Integer flowVersion, Long fromNodeId);
}