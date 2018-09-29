package com.everhomes.flow_statistics;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.flow.Flow;
import com.everhomes.flow.FlowNode;
import com.everhomes.flow.FlowProvider;
import com.everhomes.rest.flow_statistics.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlowStatisticsServiceImpl implements FlowStatisticsService {

    @Autowired
    private FlowProvider flowProvider ;

    @Autowired
    private FlowStatisticsProvider flowStatisticsProvider ;

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
        StatisticsByLanesResponse result =new  StatisticsByLanesResponse();


        return result ;
    }

    /**
     * 按节点统计处理效率
     * @param cmd
     * @return
     */
    @Override
    public StatisticsByNodesResponse statisticsByNodes( StatisticsByNodesCommand cmd ){
        StatisticsByNodesResponse result =new  StatisticsByNodesResponse();


        return result ;
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
