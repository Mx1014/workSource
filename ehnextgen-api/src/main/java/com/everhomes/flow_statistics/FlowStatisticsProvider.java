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

    /**
     * 获取上一次效率统计之后，新增的FlowEventLog
     */
    List<FlowEventLog> getRecentFlowEventLog(Integer namespaceId, Timestamp maxTime);

    /**
     * 获取泳道开始时间，即该泳道所有节点的最小开始时间
     */
    Timestamp getFlowLaneStartTime(Long flowCaseId, Long flowLaneId);

    /**
     * 查询所有正常结束状态的FlowEventLog
     */
    List<FlowEventLog> queryAllNormalFlowEventLogs(Integer namespaceId);
}
