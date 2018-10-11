package com.everhomes.flow_statistics;

import com.everhomes.flow.FlowEventLog;
import com.everhomes.flow.FlowNode;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.flow_statistics.FlowVersionCycleDTO;

import java.util.List;

public interface FlowStatisticsProvider {

    FlowVersionCycleDTO getFlowVersionCycle(Long flowMainId , Integer version);

    List<FlowNode> getFlowNodes(Long flowMainId , Integer version);

    List<FlowEventLog> getAllFlowEventLogs();

    List<FlowEventLog> queryFlowEventLog(ListingLocator locator, int count,
                                         ListingQueryBuilderCallback callback ,
                                         ListingQueryBuilderCallback orderCallback );
}
