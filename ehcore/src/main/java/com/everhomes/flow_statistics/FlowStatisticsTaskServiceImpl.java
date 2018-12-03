package com.everhomes.flow_statistics;

import com.everhomes.flow.*;
import com.everhomes.rest.flow.FlowNodeType;
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
 * @author liangming.huang
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
        //获取所有正常结束状态的记录
        List<FlowEventLog> logs = flowStatisticsProvider.queryAllNormalFlowEventLogs(namespaceId);
        List<String> lanesList = new ArrayList<>();
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
            //它没就不用走下去了
            if(CollectionUtils.isEmpty(flowCases)){
                continue ;
            }
            //该条业务的flowCaseId
            Long rootFlowCaseId = flowCases.get(0).getRootFlowCaseId();

            List<Long> cases = new ArrayList<>();
            flowCases.stream().forEach( r -> cases.add(r.getId()));
            //找出当前节点的后一个节点
            List<FlowLink> linkList = flowLinkProvider.listFlowLinkByFromNodeId(log.getFlowMainId(),log.getFlowVersion(),log.getFlowNodeId());
            //它没下一个结点那就不用走下去了
            if(CollectionUtils.isEmpty(linkList)){
                continue ;
            }
            List<Long> nestNodeIds = new ArrayList<>();
            linkList.stream().forEach(r->{
                if(r.getToNodeId() != null){
                    nestNodeIds.add(r.getToNodeId());
                }
            });
            //查出当条记录的后一节点的所有记录按时间排序取第一个（当前节点时间之后的节点第一个）
            List<FlowEventLog> nextLogs = flowStatisticsProvider.getFlowEventLogs(log.getFlowMainId() ,log.getFlowVersion() ,cases , log.getCreateTime() , nestNodeIds);
            //无记录则该条信息应该找不到结束点，跳过
            if(CollectionUtils.isEmpty(nextLogs)){
                continue ;
            }
            Timestamp endDate = nextLogs.get(0).getCreateTime();
            FlowStatisticsHandleLog hlog = createFlowStatisticsHandleLog(endDate, log, node, rootFlowCaseId);
            Long flowCaseId = log.getFlowCaseId();
            Long flowLaneId = node.getFlowLaneId();
            //如果lanesList中存在泳道信息，说明已经记录过这个泳道的周期时长
            if(lanesList.contains(flowCaseId+"-"+flowLaneId)){
                hlog.setFlowLanesCycle(0L);
            } else{
                //lanesList中添加泳道信息，标记这个泳道已处理
                lanesList.add(flowCaseId+"-"+flowLaneId);
                Long flowLaneCycle = getFlowLaneCycle(flowCaseId, flowLaneId, log);
                hlog.setFlowLanesCycle(flowLaneCycle);
            }
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
        //获取统计表里的记录最大创建时间，作为追加统计的增量时间
        Timestamp maxTime = flowStatisticsHandleLogProvider.getMaxStatisticsTime();
        //第一次统计后，后面每次统计的增量时间往前推3小时，避免遗漏数据
        if(maxTime != null){
            maxTime = new Timestamp(maxTime.getTime() - 3*60*60*1000);
        }
        List<FlowEventLog> logs = flowStatisticsProvider.getRecentFlowEventLog(namespaceId, maxTime);
        List<String> lanesList = new ArrayList<>();
        for(FlowEventLog log : logs){
            //开始及结束的结节记录不保存，不在统计范围内，产品定义的
            FlowNode node = flowNodeProvider.getFlowNodeById(log.getFlowNodeId());
            Boolean flag = (node == null)
                    || (FlowNodeType.START.getCode().equals(node.getNodeType()))
                    || (FlowNodeType.END.getCode().equals(node.getNodeType()));
            if(flag){
                continue ;
            }
            //查询是否已处理
            List<FlowStatisticsHandleLog> handleLogs = flowStatisticsHandleLogProvider.getStatisticsHandleLogByEventLogId(log.getId());
            //已处理的,跳过
            if(CollectionUtils.isNotEmpty(handleLogs)){
                continue ;
            }
            //获取该条业务的所有的flowCase
            List<FlowCase> flowCases = flowService.getAllFlowCase(log.getFlowCaseId());
            //flowCase为空，跳过
            if(CollectionUtils.isEmpty(flowCases)){
                continue ;
            }
            //该条业务的flowCaseId
            Long rootFlowCaseId = flowCases.get(0).getRootFlowCaseId();

            List<Long> cases = new ArrayList<>();
            flowCases.stream().forEach( r -> cases.add(r.getId()));
            //找出当前节点的后一个节点
            List<FlowLink> linkList = flowLinkProvider.listFlowLinkByFromNodeId(log.getFlowMainId(),log.getFlowVersion(),log.getFlowNodeId());
            //它没下一个结点那就不用走下去了
            if(CollectionUtils.isEmpty(linkList)){
                continue ;
            }
            List<Long> nestNodeIds = new ArrayList<>();
            linkList.stream().forEach(r->{
                if(r.getToNodeId() != null){
                    nestNodeIds.add(r.getToNodeId());
                }
            });
            //查出当条记录的后一节点的所有记录按时间排序取第一个（当前节点时间之后的节点第一个）
            List<FlowEventLog> nextLogs = flowStatisticsProvider.getFlowEventLogs(log.getFlowMainId() ,log.getFlowVersion() ,cases , log.getCreateTime() , nestNodeIds);
            //无记录则该条信息应该找不到结束点，跳过
            if(CollectionUtils.isEmpty(nextLogs)){
                continue ;
            }
            Timestamp endDate = nextLogs.get(0).getCreateTime();
            FlowStatisticsHandleLog hlog = createFlowStatisticsHandleLog(endDate, log, node, rootFlowCaseId);
            Long flowCaseId = log.getFlowCaseId();
            Long flowLaneId = node.getFlowLaneId();
            //如果lanesList中存在泳道信息，说明已经记录过这个泳道的周期时长
            if(lanesList.contains(flowCaseId+"-"+flowLaneId)){
                hlog.setFlowLanesCycle(0L);
            } else{
                //lanesList中添加泳道信息，标记这个泳道已处理
                lanesList.add(flowCaseId+"-"+flowLaneId);
                Long flowLaneCycle = getFlowLaneCycle(flowCaseId, flowLaneId, log);
                hlog.setFlowLanesCycle(flowLaneCycle);
            }
            //保存处理记录
            flowStatisticsHandleLogProvider.create(hlog);
        }
    }


    /**
     * 创建工作流统计记录
     */
    private FlowStatisticsHandleLog createFlowStatisticsHandleLog(Timestamp endDate, FlowEventLog log, FlowNode node, Long rootFlowCaseId){
        FlowStatisticsHandleLog hlog = new  FlowStatisticsHandleLog();
        hlog.setEndTime(endDate);
        hlog.setFlowMainId(log.getFlowMainId());
        hlog.setFlowNodeId(log.getFlowNodeId());
        hlog.setFlowVersion(log.getFlowVersion());
        hlog.setLogId(log.getId());
        hlog.setNamespaceId(log.getNamespaceId());
        hlog.setStartTime(log.getCreateTime());
        hlog.setFlowCaseId(rootFlowCaseId);
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
      return hlog;
    }


    /**
     * 获取当前泳道的周期时长
     */
    private Long getFlowLaneCycle(Long flowCaseId, Long flowLaneId, FlowEventLog log){

        //当前泳道的开始处理时间
        Timestamp startTime = flowStatisticsProvider.getFlowLaneStartTime(flowCaseId, flowLaneId);

        //下面开始获取下一个泳道的开始处理时间，作为当前泳道的结束时间
        //1.获取工作流图表
        FlowGraph flowGraph = flowService.getFlowGraph(log.getFlowMainId(), log.getFlowVersion());
        //2.根据工作流图表，获取当前泳道level
        Integer flowLaneLevel = flowGraph.getGraphLane(flowLaneId).getFlowLane().getLaneLevel();
        //3.下一个泳道的level比当前泳道level大1，根据level获取泳道ID
        Long nextFlowLaneId = flowGraph.getGraphLane(flowLaneLevel + 1).getFlowLane().getId();
        //4.获取下一个泳道的开始处理时间
        Timestamp endTime = flowStatisticsProvider.getFlowLaneStartTime(flowCaseId, nextFlowLaneId);

        Long flowLaneCycle = 0L;
        if(startTime != null && endTime != null){
            flowLaneCycle = (endTime.getTime() - startTime.getTime()) / 1000;
        }
        return flowLaneCycle;
    }
}
