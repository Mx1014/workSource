package com.everhomes.flow_statistics;

import com.everhomes.flow.FlowCase;
import com.everhomes.flow.FlowEventLog;
import com.everhomes.flow.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 执行统计任务的类
 */
@Service
public class FlowStatisticsTaskHandler {

    @Autowired
    private FlowStatisticsHandleLogProvider flowStatisticsHandleLogProvider ;

    @Autowired
    private FlowStatisticsProvider flowStatisticsProvider ;

    @Autowired
    private FlowService flowService ;

    /**
     * 删除全部记录，重新统计
     */
    public void allStatistics(){

        //原来的记录全部删除
        flowStatisticsHandleLogProvider.deleteAll();
        //获取所有记录
        List<FlowEventLog> logs = flowStatisticsProvider.getAllFlowEventLogs();
        //循环处理记录
        for(FlowEventLog log : logs){

            //获取该条业务的所有的flowCase
            List<FlowCase> flowCases = flowService.getAllFlowCase(log.getFlowCaseId());
            //找出当前节点的后一个节点

            //查出当条记录所在的任务的记录

            //查出当条记录的后一节点
            //查出当条记录的后一节点的所有记录按时间排序取第一个（当前节点时间之后的节点第一个）

        }
    }



    /**
     * 在原记录的基础上追加统计
     */
    public void appendStatistics(){

    }
}
