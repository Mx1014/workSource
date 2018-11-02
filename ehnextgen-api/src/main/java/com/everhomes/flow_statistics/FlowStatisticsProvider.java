package com.everhomes.flow_statistics;

import com.everhomes.flow.FlowEventLog;
import com.everhomes.flow.FlowNode;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.flow_statistics.FlowVersionCycleDTO;

import java.sql.Timestamp;
import java.util.List;

public interface FlowStatisticsProvider {

    FlowVersionCycleDTO getFlowVersionCycle(Long flowMainId , Integer version);

    List<FlowNode> getFlowNodes(Long flowMainId , Integer version);

    List<FlowEventLog> getAllFlowEventLogs(Integer namespaceId);

    List<FlowEventLog> queryFlowEventLog(ListingLocator locator, int count,
                                         ListingQueryBuilderCallback callback ,
                                         ListingQueryBuilderCallback orderCallback );

    List<FlowEventLog> getFlowEventLogs(Long flowMainId , Integer version , List<Long>flowCases , Timestamp startDate , List<Long> flowNodeIds);

    FlowNode getFlowNodeByFlowLevel(Long flowMainId , Integer version , Integer flowNodeLevel);

    List<FlowNode> getFlowNodeByLaneId(Long flowMainId , Integer version ,Long laneId);
}
