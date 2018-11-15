package com.everhomes.flow_statistics;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.flow.*;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.flow.FlowDTO;
import com.everhomes.rest.flow.FlowNodeType;
import com.everhomes.rest.flow_statistics.*;
import com.everhomes.server.schema.Tables;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.DateUtils;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.collections.CollectionUtils;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlowStatisticsServiceImpl implements FlowStatisticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowStatisticsServiceImpl.class);

    //默认小数位
    private static final int DEFAULT_DECIMAL = 3 ;

    @Autowired
    private FlowProvider flowProvider ;

    @Autowired
    private FlowStatisticsProvider flowStatisticsProvider ;

    @Autowired
    private FlowStatisticsHandleLogProvider flowStatisticsHandleLogProvider ;

    @Autowired
    private FlowLaneProvider flowLaneProvider ;

    @Autowired
    private ConfigurationProvider configProvider;

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
            FlowDTO flowDTO = ConvertHelper.convert(r, FlowDTO.class);
            flowDTO.fixDisplayVersion();
            dto.getFlowVersions().add(flowDTO.getLastVersion());
            return null ;
        }).collect(Collectors.toList());
        return dto ;

    }

    /**
     * 工作流版本时间跨度查询
     * 该版本的时间跨度为该版本的创建时间到下一版本的创建时间.
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
       // dto = flowStatisticsProvider.getFlowVersionCycle(cmd.getFlowMainId() ,cmd.getFlowVersion());
        Flow currentFlow =  this.findFlowVersionByVersion(cmd.getFlowMainId() , cmd.getNamespaceId() ,cmd.getFlowVersion());
        if(currentFlow == null ){
            return dto ;
        }
        Timestamp minTime = currentFlow.getCreateTime() ;
        if(minTime != null){
            dto.setStartDate(new Date(minTime.getTime()));
        }
        Flow nextFlow = this.findNextFlowVersionByVersion(cmd.getFlowMainId() , cmd.getNamespaceId() ,currentFlow.getId());
        if(nextFlow == null ){
            return dto ;
        }
        Timestamp maxTime = nextFlow.getCreateTime() ;
        if(minTime != null){
            dto.setEndDate(new Date(maxTime.getTime()));
        }
        return dto ;
    }

    /**
     * 查询某版本的工作流信息
     * @param flowMainId
     * @param namespaceId
     * @param flowVersion
     * @return
     */
    private Flow findFlowVersionByVersion(Long flowMainId , Integer namespaceId , Integer flowVersion){
        ListingLocator locator = new ListingLocator();
        List<Flow> flows = flowProvider.queryFlows(locator, 0, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(
                    ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_FLOWS.NAMESPACE_ID.eq(namespaceId));
                query.addConditions(Tables.EH_FLOWS.FLOW_MAIN_ID.eq(flowMainId));
                query.addConditions(Tables.EH_FLOWS.FLOW_VERSION.eq(flowVersion));
                return query;
            }

        });
        if (flows != null && flows.size() > 0) {
            return flows.get(0);
        }
        return null;
    }

    /**
     * 查询某版本的下一个版本的工作流信息
     * @param flowMainId
     * @param namespaceId
     * @param currentId
     * @return
     */
    private Flow findNextFlowVersionByVersion(Long flowMainId , Integer namespaceId , Long currentId){
        ListingLocator locator = new ListingLocator();
        List<Flow> flows = flowProvider.queryFlows(locator, 0, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(
                    ListingLocator locator, SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_FLOWS.NAMESPACE_ID.eq(namespaceId));
                query.addConditions(Tables.EH_FLOWS.FLOW_MAIN_ID.eq(flowMainId));
                query.addConditions(Tables.EH_FLOWS.ID.gt(currentId));
                return query;
            }

        });
        if (flows != null && flows.size() > 0) {
            return flows.get(0);
        }
        return null;
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
        response.setCurrentCycleNodesAverage(0D);
        Long flowMainId = cmd.getFlowMainId() ;
        Integer flowVersion = cmd.getFlowVersion();
        //查出该工作流的所有节点；
        List<FlowNode> nodesList = flowStatisticsProvider.getFlowNodes(flowMainId ,flowVersion);
        if(CollectionUtils.isEmpty(nodesList)){
            return response;
        }
        for(FlowNode node : nodesList){

            if(node == null){
                continue ;
            }
            if (FlowNodeType.START.getCode().equals(node.getNodeType())) {
                continue ;
            }
            if (FlowNodeType.END.getCode().equals(node.getNodeType())) {
                continue ;
            }

            StatisticsByNodesDTO dto = new StatisticsByNodesDTO();
            dto.setNodeName(node.getNodeName());
            dto.setNodeId(node.getId());
            dto.setNodeLevel(node.getNodeLevel());
            //处理次数
            Integer times = flowStatisticsHandleLogProvider.countNodesTimes(flowMainId,flowVersion,new Timestamp(cmd.getStartDate()),new Timestamp(cmd.getEndDate()),node.getId());
            if(times==null|| times == 0){
                LOGGER.debug("countNodesTimes return null or 0 .flowMainId:{},flowVersion:{},startDate:{},endDate:{},nodeId:{}",flowMainId,flowVersion,cmd.getStartDate(),cmd.getEndDate(),node.getId());
            }
            //处理时长
            Long cycles = flowStatisticsHandleLogProvider.countNodesCycle(flowMainId,flowVersion,new Timestamp(cmd.getStartDate()),new Timestamp(cmd.getEndDate()),node.getId());

            //当前周期节点平均处理时长(当前节点或子流程处理总时长/处理次数)
            Long averageCycle = 0L ;
            if(cycles!=null && times!=null&& times != 0){
                averageCycle = cycles/times ;
            }
            Double average = 0D;
            //直接返回秒
            if(averageCycle!=0){
               // average = averageCycle.doubleValue()/60/60 ;
                average = averageCycle.doubleValue() ;
                //average = handleDecimal(average);
            }
            dto.setHandleTimes(times);
            dto.setAverageHandleCycle(average);
            response.getDtos().add(dto);
        }
        //总处理次数
        Integer times = flowStatisticsHandleLogProvider.countNodesTimes(flowMainId,flowVersion,new Timestamp(cmd.getStartDate()),new Timestamp(cmd.getEndDate()),null);
        if(times==null|| times == 0){
            LOGGER.debug("countAllNodesTimes return null or 0 .flowMainId:{},flowVersion:{},startDate:{},endDate:{}",flowMainId,flowVersion,cmd.getStartDate(),cmd.getEndDate());
        }
        //总处理时长
        Long cycles = flowStatisticsHandleLogProvider.countNodesCycle(flowMainId,flowVersion,new Timestamp(cmd.getStartDate()),new Timestamp(cmd.getEndDate()),null);
        //当前周期节点平均处理时长(当前节点或子流程处理总时长/处理次数)
        Long averageCycle = 0L ;
        if(cycles!=null && times!=null&& times != 0){
             averageCycle = cycles/times ;
        }
        Double average = 0D;
        //直接返回秒
        if(averageCycle != 0){
            //average = averageCycle.doubleValue()/60/60 ;
            average = averageCycle.doubleValue() ;
            //average = handleDecimal(average);
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
        response.setCurrentCycleLanesAverage(0D);
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
            if(isStartOrEndLanes(flowMainId,flowVersion,lane.getId())){
                continue ;
            }
            StatisticsByLanesDTO dto = new StatisticsByLanesDTO();
            dto.setLaneId(lane.getId());
            dto.setLaneLevel(lane.getLaneLevel());
            dto.setLaneName(lane.getDisplayName());
            //上一周期平均处理耗时
            //获取上一周期的起止时间
            int cl = DateUtils.differentDays(new Date(cmd.getStartDate()) ,new Date(cmd.getEndDate()));
            cl= cl +1 ;
            java.util.Date lastStartDate = DateUtils.dateAfterOrBeforeDays(new Date(cmd.getStartDate()),-cl);
            java.util.Date lastEndDate = DateUtils.dateAfterOrBeforeDays(new Date(cmd.getEndDate()),-cl);

            Timestamp lastStartTime = new Timestamp(lastStartDate.getTime());
            Timestamp lastEndTime =     new Timestamp(lastEndDate.getTime());

            Integer lastTimes = this.countLanesTimes(flowMainId,flowVersion,lastStartTime,lastEndTime,lane.getId());
            if(lastTimes==null|| lastTimes==0){
                LOGGER.debug("countLastLanesTimes return null or 0 .flowMainId:{},flowVersion:{},startDate:{},endDate:{},laneId:{}",flowMainId,flowVersion,cmd.getStartDate(),cmd.getEndDate(),lane.getId());
            }
            Long lastCycles = flowStatisticsHandleLogProvider.countLanesCycle(flowMainId,flowVersion,lastStartTime,lastEndTime,lane.getId());
            Long lastAverageCycle = 0L ;
            if(lastCycles!=null && lastTimes!=null&& lastTimes!=0){
                lastAverageCycle = lastCycles/lastTimes ;
            }
            Double lastAverage = 0D;
            //直接返回秒
            if(lastAverageCycle!=0){
                //lastAverage = lastAverageCycle.doubleValue()/60/60 ;
                lastAverage = lastAverageCycle.doubleValue() ;
                //lastAverage = handleDecimal(lastAverage);
            }

            dto.setLastCycleAverage(lastAverage);
            //当前周期平均处理耗时(泳道平均处理耗时=泳道总处理耗时/经过泳道的次数)
            Integer times = this.countLanesTimes(flowMainId,flowVersion,new Timestamp(cmd.getStartDate()),new Timestamp(cmd.getEndDate()),lane.getId());
            if(times==null|| times==0){
                LOGGER.debug("countLanesTimes return null or 0 .flowMainId:{},flowVersion:{},startDate:{},endDate:{},laneId:{}",flowMainId,flowVersion,cmd.getStartDate(),cmd.getEndDate(),lane.getId());
            }
            Long cycles = flowStatisticsHandleLogProvider.countLanesCycle(flowMainId,flowVersion,new Timestamp(cmd.getStartDate()),new Timestamp(cmd.getEndDate()),lane.getId());
            Long averageCycle = 0L ;
            if(cycles!=null && times!=null&& times!=0){
                averageCycle = cycles/times ;
            }
            Double average = 0D;
            //直接返回秒
            if(averageCycle!=0){
                //average = averageCycle.doubleValue()/60/60 ;
                average = averageCycle.doubleValue() ;
                //average = handleDecimal(average);
            }
            dto.setCurrentCycleAverage(average);
            //环比效率值(上周期平均处理时间-当前周期平均处理时间)/当前周期平均处理时间 * 100%)
            Double earlyComaredVal = 0D ;
            if(lastAverage != 0){
                earlyComaredVal = 100D ;
            }
            if(average != 0){
                earlyComaredVal = (lastAverage - average) /average ;
                earlyComaredVal = handleDecimal(earlyComaredVal);
            }
            dto.setEarlyComparedVal(earlyComaredVal);
            response.getDtos().add(dto);
        }
        //当前周期泳道平均处理时长(所有泳道的处理时长总和/泳道个数)
        Integer lanesCount = flowStatisticsHandleLogProvider.countLanes(flowMainId,flowVersion,new Timestamp(cmd.getStartDate()),new Timestamp(cmd.getEndDate()));
        if(lanesCount==null|| lanesCount==0){
            LOGGER.debug("countLanes return null or 0 .flowMainId:{},flowVersion:{},startDate:{},endDate:{}",flowMainId,flowVersion,cmd.getStartDate(),cmd.getEndDate());
        }
        Long cycles = flowStatisticsHandleLogProvider.countLanesCycle(flowMainId,flowVersion,new Timestamp(cmd.getStartDate()),new Timestamp(cmd.getEndDate()),null);
        Long averageCycle = 0L ;
        if(cycles!=null && lanesCount!=null&& lanesCount != 0){
            averageCycle = cycles/lanesCount ;
        }
        Double average = 0D;
        //直接返回秒
        if(averageCycle!=0){
            //average = averageCycle.doubleValue()/60/60 ;
            average = averageCycle.doubleValue() ;
            //average = handleDecimal(average);
        }
        response.setCurrentCycleLanesAverage(average);
        return response ;
    }

    /**
     * 统计泳道次数
     * @param flowMainId
     * @param version
     * @param startTime
     * @param endTime
     * @param laneId
     * @return
     */
    private Integer countLanesTimes(Long flowMainId , Integer version ,Timestamp startTime , Timestamp endTime , Long laneId){

        //1.查询出该泳道信息
        FlowLane lane = flowLaneProvider.findById(laneId);
        if(lane == null){
            return 0 ;
        }
        //2.查询同该泳道首个节点
        FlowNode node = flowStatisticsProvider.getFlowNodeByFlowLevel(flowMainId,version,lane.getFlowNodeLevel());
        if(node == null){
            return 0 ;
        }
        //3.统计首个节点的记录数即为经过该泳道的次数（以进入的次数为准）
        Integer times = flowStatisticsHandleLogProvider.countLanesTimes(flowMainId,version,startTime,endTime,laneId,node.getId());
        return times ;
    }

    /**
     * 判断一个泳道是不是开始或结束的泳道
     * @param flowMainId
     * @param version
     * @param laneId
     * @return
     */
    private boolean isStartOrEndLanes(Long flowMainId , Integer version ,Long laneId){

        //查询出节点，再从节点类型判断，按理说开始或结束泳道只有一个节点
        List<FlowNode> nodes = flowStatisticsProvider.getFlowNodeByLaneId(flowMainId,version,laneId);
        if(CollectionUtils.isNotEmpty(nodes)){
            FlowNode node = nodes.get(0);
            if(node == null){
                return false ;
            }
            if (FlowNodeType.START.getCode().equals(node.getNodeType())) {
                return true ;
            }
            if (FlowNodeType.END.getCode().equals(node.getNodeType())) {
                return true ;
            }
        }
        return false ;
    }

    //处理小数位数
    private Double handleDecimal(Double d ){

        if(d == null){
            return d ;
        }
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        String decimalStr = configProvider.getValue(namespaceId,"flow.statistic.decimal", DEFAULT_DECIMAL+"");
        int decimal = Integer.parseInt(decimalStr);
        NumberFormat nf = NumberFormat.getNumberInstance();
        // 保留两位小数
        nf.setMaximumFractionDigits(decimal);
        // 四舍五入
        nf.setRoundingMode(RoundingMode.HALF_UP);
        String result = nf.format(d);
        return Double.valueOf(result);
    }

}
