package com.everhomes.flow_statistics;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.flow.*;
import com.everhomes.rest.flow_statistics.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateHelper;
import com.everhomes.util.DateUtils;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlowStatisticsServiceImpl implements FlowStatisticsService {

    @Autowired
    private FlowProvider flowProvider ;

    @Autowired
    private FlowStatisticsProvider flowStatisticsProvider ;

    @Autowired
    private FlowStatisticsHandleLogProvider flowStatisticsHandleLogProvider ;

    @Autowired
    private FlowLaneProvider flowLaneProvider ;

    /**
     * 工作流版本查询
     * @param cmd
     * @return
     */
    @Override
    public FindFlowVersionDTO findFlowVersion(FindFlowVersionCommand cmd ){
        FindFlowVersionDTO dto = new FindFlowVersionDTO();
        if(dto.getFlowVersions() == null){
            dto.setFlowVersions(new ArrayList<Integer>());
        }
        if(cmd.getFlowMainId() == null ){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "flowMainId is null  .");
        }
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        cmd.setNamespaceId(namespaceId);
        List<Flow> list =  flowProvider.findFlowVersion(cmd.getFlowMainId() , cmd.getNamespaceId());
        if(list == null || list.size() < 1){
            return dto ;
        }
        list.stream().map(r->{
            dto.getFlowVersions().add(r.getFlowVersion());
            return null ;
        }).collect(Collectors.toList());
        return dto ;

    }

    /**
     * 工作流版本时间跨度查询
     * @param cmd
     * @return
     */
    @Override
    public FlowVersionCycleDTO getFlowVersionCycle(FlowVersionCycleCommand cmd ){

        FlowVersionCycleDTO dto = new FlowVersionCycleDTO();

        if(cmd.getFlowMainId() == null ){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "flowMainId is null  .");
        }
        if(cmd.getFlowVersion() == null ){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "flowVersion is null  .");
        }
        dto = flowStatisticsProvider.getFlowVersionCycle(cmd.getFlowMainId() ,cmd.getFlowVersion());
        return dto ;
    }

    /**
     * 按泳道统计处理效率
     * @param cmd
     * @return
     */
    @Override
    public StatisticsByLanesResponse statisticsByLanes(StatisticsByLanesCommand cmd){

        StatisticsByLanesResponse result = handleLanes(cmd);

        return result ;
    }

    /**
     * 按节点统计处理效率
     * @param cmd
     * @return
     */
    @Override
    public StatisticsByNodesResponse statisticsByNodes( StatisticsByNodesCommand cmd ){

        StatisticsByNodesResponse result = handleNodes(cmd);

        return result ;
    }

    /**
     * 处理节点返回结果
     * @param cmd
     * @return
     */
    private StatisticsByNodesResponse handleNodes(StatisticsByNodesCommand cmd){
        StatisticsByNodesResponse response = new StatisticsByNodesResponse();
        List<StatisticsByNodesDTO> list  = new ArrayList<StatisticsByNodesDTO>();
        response.setDtos(list);
        Long flowMainId = cmd.getFlowMainId() ;
        Integer flowVersion = cmd.getFlowVersion();
        //查出该工作流的所有节点；
        List<FlowNode> nodesList = flowStatisticsProvider.getFlowNodes(flowMainId ,flowVersion);
        if(CollectionUtils.isEmpty(nodesList)){
            return response;
        }
        for(FlowNode node : nodesList){
            StatisticsByNodesDTO dto = new StatisticsByNodesDTO();
            dto.setNodeName(node.getNodeName());
            dto.setNodeId(node.getId());
            dto.setNodeLevel(node.getNodeLevel());
            //处理次数
            Integer times = flowStatisticsHandleLogProvider.countNodesTimes(flowMainId,flowVersion,cmd.getStartDate(),cmd.getEndDate(),node.getId());
            //处理时长
            Long cycles = flowStatisticsHandleLogProvider.countNodesCycle(flowMainId,flowVersion,cmd.getStartDate(),cmd.getEndDate(),node.getId());

            //当前周期节点平均处理时长(当前节点或子流程处理总时长/处理次数)
            Long averageCycle = 0L ;
            if(cycles!=null && times!=null&& !times.equals(0)){
                averageCycle = cycles/times ;
            }
            Double average = 0D;
            //由秒转化为小时
            if(!averageCycle.equals(0)){
                average = averageCycle.doubleValue()/60/60 ;
            }
            dto.setHandleTimes(times);
            dto.setAverageHandleCycle(average);
        }
        //总处理次数
        Integer times = flowStatisticsHandleLogProvider.countNodesTimes(flowMainId,flowVersion,cmd.getStartDate(),cmd.getEndDate(),null);
        //总处理时长
        Long cycles = flowStatisticsHandleLogProvider.countNodesCycle(flowMainId,flowVersion,cmd.getStartDate(),cmd.getEndDate(),null);
        //当前周期节点平均处理时长(当前节点或子流程处理总时长/处理次数)
        Long averageCycle = 0L ;
        if(cycles!=null && times!=null&& !times.equals(0)){
             averageCycle = cycles/times ;
        }
        Double average = 0D;
        //由秒转化为小时
        if(!averageCycle.equals(0)){
            average = averageCycle.doubleValue()/60/60 ;
        }
        response.setCurrentCycleNodesAverage(average);
        return response;
    }

    /**
     * 处理节点返回结果
     * @param cmd
     * @return
     */
    private StatisticsByLanesResponse handleLanes(StatisticsByLanesCommand cmd){

        StatisticsByLanesResponse response = new StatisticsByLanesResponse();
        List<StatisticsByLanesDTO> list = new ArrayList<StatisticsByLanesDTO>();
        response.setDtos(list);
        if(cmd.getStartDate() == null ||cmd.getEndDate()==null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "the startDate and endDate can not be null .");
        }
        Long flowMainId = cmd.getFlowMainId() ;
        Integer flowVersion = cmd.getFlowVersion();
        //查出该工作流所有的泳道
        List<FlowLane> lanesList = flowLaneProvider.listFlowLane(flowMainId ,flowVersion);
        if(CollectionUtils.isEmpty(lanesList)){
            return response;
        }
        for(FlowLane lane :lanesList){
            StatisticsByLanesDTO dto = new StatisticsByLanesDTO();
            dto.setLaneId(lane.getId());
            dto.setLaneLevel(lane.getLaneLevel());
            dto.setLaneName(lane.getDisplayName());
            //上一周期平均处理耗时
            //获取上一周期的起止时间
            int cl = DateUtils.differentDays(new Date(cmd.getStartDate().getTime()) ,new Date(cmd.getEndDate().getTime()));
            cl= cl +1 ;
            java.util.Date lastStartDate = DateUtils.dateAfterOrBeforeDays(new Date(cmd.getStartDate().getTime()),-cl);
            java.util.Date lastEndDate = DateUtils.dateAfterOrBeforeDays(new Date(cmd.getEndDate().getTime()),-cl);

            Timestamp lastStartTime = new java.sql.Timestamp(lastStartDate.getTime());
            Timestamp lastEndTime =     new java.sql.Timestamp(lastEndDate.getTime());

            Integer lastTimes = flowStatisticsHandleLogProvider.countLanesTimes(flowMainId,flowVersion,lastStartTime,lastEndTime,lane.getId());
            Long lastCycles = flowStatisticsHandleLogProvider.countLanesCycle(flowMainId,flowVersion,lastStartTime,lastEndTime,lane.getId());
            Long lastAverageCycle = 0L ;
            if(lastCycles!=null && lastTimes!=null&& !lastTimes.equals(0)){
                lastAverageCycle = lastCycles/lastTimes ;
            }
            Double lastAverage = 0D;
            //由秒转化为小时
            if(!lastAverageCycle.equals(0)){
                lastAverage = lastAverageCycle.doubleValue()/60/60 ;
            }

            dto.setLastCycleAverage(lastAverage);
            //当前周期平均处理耗时(泳道平均处理耗时=泳道总处理耗时/经过泳道的次数)
            Integer times = flowStatisticsHandleLogProvider.countLanesTimes(flowMainId,flowVersion,cmd.getStartDate(),cmd.getEndDate(),lane.getId());
            Long cycles = flowStatisticsHandleLogProvider.countLanesCycle(flowMainId,flowVersion,cmd.getStartDate(),cmd.getEndDate(),lane.getId());
            Long averageCycle = 0L ;
            if(cycles!=null && times!=null&& !times.equals(0)){
                averageCycle = cycles/times ;
            }
            Double average = 0D;
            //由秒转化为小时
            if(!averageCycle.equals(0)){
                average = averageCycle.doubleValue()/60/60 ;
            }
            dto.setCurrentCycleAverage(average);
            //环比效率值(上周期平均处理时间-当前周期平均处理时间)/当前周期平均处理时间 * 100%)
            Double earlyComaredVal = (lastAverage - average) /average ;
            dto.setEarlyComparedVal(earlyComaredVal);
        }
        //当前周期泳道平均处理时长
        Integer times = flowStatisticsHandleLogProvider.countLanesTimes(flowMainId,flowVersion,cmd.getStartDate(),cmd.getEndDate(),null);
        Long cycles = flowStatisticsHandleLogProvider.countLanesCycle(flowMainId,flowVersion,cmd.getStartDate(),cmd.getEndDate(),null);
        Long averageCycle = 0L ;
        if(cycles!=null && times!=null&& !times.equals(0)){
            averageCycle = cycles/times ;
        }
        Double average = 0D;
        //由秒转化为小时
        if(!averageCycle.equals(0)){
            average = averageCycle.doubleValue()/60/60 ;
        }
        response.setCurrentCycleLanesAverage(average);
        return response ;
    }

    public void method(Long flowMainId , Integer flowVersion)
    {
        //节点记录处理逻辑
        //通过flowMainId，flowVersion查出工作流的所有节点
        List<FlowNode> nodesList = flowStatisticsProvider.getFlowNodes(flowMainId ,flowVersion);
        //切分出某一任务的所有记录并按节点与时间排好序

        //计算各节点的任务处理时间

        //通过flowMainId，flowVersion　查询logs记录，并且将其按时间顺序排序
    }


}
