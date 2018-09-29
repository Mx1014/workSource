package com.everhomes.flow_statistics;

import com.everhomes.flow.FlowNode;
import com.everhomes.rest.flow_statistics.FlowVersionCycleDTO;

import java.util.List;

public interface FlowStatisticsProvider {

    FlowVersionCycleDTO getFlowVersionCycle(Long flowMainId , Integer version);

    List<FlowNode> getFlowNodes(Long flowMainId , Integer version);
}
