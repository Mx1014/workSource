package com.everhomes.flow_statistics;

import com.everhomes.flow.*;
import com.everhomes.rest.flow.FlowNodeType;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 执行统计任务的类
 */
@Service
public class FlowStatisticsTaskServiceImpl  implements  FlowStatisticsTaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowStatisticsTaskServiceImpl.class);

    @Autowired
    private FlowStatisticsHandleLogProvider flowStatisticsHandleLogProvider ;

    @Autowired
    private FlowStatisticsProvider flowStatisticsProvider ;

    @Autowired
    private FlowService flowService ;

    @Autowired
    private  FlowLinkProvider flowLinkProvider ;

    @Autowired
    private FlowNodeProvider flowNodeProvider ;

    @Autowired
    private FlowLaneProvider flowLaneProvider ;

    /**
     * 删除全部记录，重新统计
     */
    @Override
    public void allStatistics(Integer namespaceId){
        LOGGER.info("do allStatistics ,namespaceId:{}.",namespaceId);

        //原来的记录全部删除
        flowStatisticsHandleLogProvider.deleteAll(namespaceId);
        //获取所有记录
        List<FlowEventLog> logs = flowStatisticsProvider.getAllFlowEventLogs(namespaceId);
        //循环处理记录
        for(FlowEventLog log : logs){

            //开始及结束的结节记录不保存，不在统计范围内，产品定义的
            FlowNode node = flowNodeProvider.getFlowNodeById(log.getFlowNodeId());
            if(node == null){
                continue ;
            }
            if (FlowNodeType.START.getCode().equals(node.getNodeType())) {
                continue ;
            }
            if (FlowNodeType.END.getCode().equals(node.getNodeType())) {
                continue ;
            }
            //获取该条业务的所有的flowCase
            List<FlowCase> flowCases = flowService.getAllFlowCase(log.getFlowCaseId());
            if(CollectionUtils.isEmpty(flowCases)){//它没就不用走下去了
                continue ;
            }
            List<Long> cases = new ArrayList<Long>();
            flowCases.stream().forEach(r->{
                cases.add(r.getId());
            });
            //找出当前节点的后一个节点
            List<FlowLink> linkList = flowLinkProvider.listFlowLinkByFromNodeId(log.getFlowMainId(),log.getFlowVersion(),log.getFlowNodeId());
            if(CollectionUtils.isEmpty(linkList)){//它没下一个结点那就不用走下去了
                continue ;
            }
            List<Long> nestNodeIds = new ArrayList<Long>();
            linkList.stream().forEach(r->{
                if(r.getToNodeId() != null){
                    nestNodeIds.add(r.getToNodeId());
                }
            });
            //查出当条记录的后一节点的所有记录按时间排序取第一个（当前节点时间之后的节点第一个）
            List<FlowEventLog> nextLogs = flowStatisticsProvider.getFlowEventLogs(log.getFlowMainId() ,log.getFlowVersion() ,cases , log.getCreateTime() , nestNodeIds);
            if(CollectionUtils.isEmpty(nextLogs)){//无记录则该条信息应该找不到结束点，跳过
                continue ;
            }
            Timestamp endDate = nextLogs.get(0).getCreateTime();
            FlowStatisticsHandleLog hlog = new  FlowStatisticsHandleLog();
            hlog.setEndTime(endDate);
            hlog.setFlowMainId(log.getFlowMainId());
            hlog.setFlowNodeId(log.getFlowNodeId());

            hlog.setFlowVersion(log.getFlowVersion());
            hlog.setLogId(log.getId());
            hlog.setNamespaceId(log.getNamespaceId());
            hlog.setStartTime(log.getCreateTime());
            //计算时长
            Long cycle = (endDate.getTime() - log.getCreateTime().getTime())/1000 ;
            hlog.setCycle(cycle);
            //获取节点名
            hlog.setFlowNodeName(node.getNodeName());
            //获取泳道
            hlog.setFlowLanesId(node.getFlowLaneId());
            FlowLane lane = flowLaneProvider.findById(node.getFlowLaneId());
            if(lane != null){
                hlog.setFlowLanesName(lane.getDisplayName());
            }
            hlog.setCreateTime(DateUtils.currentTimestamp());
            //保存处理记录
            flowStatisticsHandleLogProvider.create(hlog);

        }
    }


    /**
     * 在原记录的基础上追加统计
     */
    @Override
    public void appendStatistics(Integer namespaceId){

        LOGGER.info("do appendStatistics ,namespaceId:{}.",namespaceId);

        //获取所有记录
        List<FlowEventLog> logs = flowStatisticsProvider.getAllFlowEventLogs(namespaceId);
        //循环处理记录
        for(FlowEventLog log : logs){

            //开始及结束的结节记录不保存，不在统计范围内，产品定义的
            FlowNode node = flowNodeProvider.getFlowNodeById(log.getFlowNodeId());
            if(node == null){
                continue ;
            }
            if (FlowNodeType.START.getCode().equals(node.getNodeType())) {
                continue ;
            }
            if (FlowNodeType.END.getCode().equals(node.getNodeType())) {
                continue ;
            }
            //查询是否已处理，处理的跳过
            List<FlowStatisticsHandleLog> handleLogs = flowStatisticsHandleLogProvider.getStatisticsHandleLogByEventLogId(log.getId());
            if(CollectionUtils.isNotEmpty(handleLogs)){ //已处理过的不处理
                continue ;
            }
            //获取该条业务的所有的flowCase
            List<FlowCase> flowCases = flowService.getAllFlowCase(log.getFlowCaseId());
            if(CollectionUtils.isEmpty(flowCases)){//它没就不用走下去了
                continue ;
            }
            List<Long> cases = new ArrayList<Long>();
            flowCases.stream().forEach(r->{
                cases.add(r.getId());
            });
            //找出当前节点的后一个节点
            List<FlowLink> linkList = flowLinkProvider.listFlowLinkByFromNodeId(log.getFlowMainId(),log.getFlowVersion(),log.getFlowNodeId());
            if(CollectionUtils.isEmpty(linkList)){//它没下一个结点那就不用走下去了
                continue ;
            }
            List<Long> nestNodeIds = new ArrayList<Long>();
            linkList.stream().forEach(r->{
                if(r.getToNodeId() != null){
                    nestNodeIds.add(r.getToNodeId());
                }
            });
            //查出当条记录的后一节点的所有记录按时间排序取第一个（当前节点时间之后的节点第一个）
            List<FlowEventLog> nextLogs = flowStatisticsProvider.getFlowEventLogs(log.getFlowMainId() ,log.getFlowVersion() ,cases , log.getCreateTime() , nestNodeIds);
            if(CollectionUtils.isEmpty(nextLogs)){//无记录则该条信息应该找不到结束点，跳过
                continue ;
            }
            Timestamp endDate = nextLogs.get(0).getCreateTime();
            FlowStatisticsHandleLog hlog = new  FlowStatisticsHandleLog();
            hlog.setEndTime(endDate);
            hlog.setFlowMainId(log.getFlowMainId());
            hlog.setFlowNodeId(log.getFlowNodeId());

            hlog.setFlowVersion(log.getFlowVersion());
            hlog.setLogId(log.getId());
            hlog.setNamespaceId(log.getNamespaceId());
            hlog.setStartTime(log.getCreateTime());
            //计算时长
            Long cycle = (endDate.getTime() - log.getCreateTime().getTime())/1000 ;
            hlog.setCycle(cycle);
            //获取节点名
            hlog.setFlowNodeName(node.getNodeName());
            //获取泳道
            hlog.setFlowLanesId(node.getFlowLaneId());
            FlowLane lane = flowLaneProvider.findById(node.getFlowLaneId());
            if(lane != null){
                hlog.setFlowLanesName(lane.getDisplayName());
            }
            hlog.setCreateTime(DateUtils.currentTimestamp());
            //保存处理记录
            flowStatisticsHandleLogProvider.create(hlog);

        }
    }
}
