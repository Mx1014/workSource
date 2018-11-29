package com.everhomes.flow_statistics;

import com.everhomes.rest.flow_statistics.*;

public interface FlowStatisticsService {

    FindFlowVersionDTO findFlowVersion(FindFlowVersionCommand cmd );


    FlowVersionCycleDTO getFlowVersionCycle(FlowVersionCycleCommand cmd );

    StatisticsByLanesResponse statisticsByLanes(StatisticsByLanesCommand cmd);

    StatisticsByNodesResponse statisticsByNodes( StatisticsByNodesCommand cmd );
}
