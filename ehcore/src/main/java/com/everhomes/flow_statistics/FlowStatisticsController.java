package com.everhomes.flow_statistics;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.flow_statistics.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工作流效率统计  Controller
 * @author huanglm
 *
 */
@RestDoc(value="FlowStatistics  controller", site="core")
@RestController
@RequestMapping("/flowstatistics")
public class FlowStatisticsController extends ControllerBase {

    @Autowired
    private  FlowStatisticsService flowStatisticsService ;

    /**
     * <b>URL: /flowstatistics/findFlowVersion</b>
     * <p>1). 工作流版本查询API</p>
     */
    @RequestMapping("findFlowVersion")
    @RestReturn(value=FindFlowVersionDTO.class)
    public RestResponse findFlowVersion( FindFlowVersionCommand cmd ) {

        FindFlowVersionDTO resultDTO = flowStatisticsService.findFlowVersion(cmd) ;
        RestResponse response = new RestResponse(resultDTO);
        setResponseSuccess(response);
        return response;
    }

    /**
     * <b>URL: /flowstatistics/getFlowVersionCycle</b>
     * <p>2). 工作流版本时间跨度查询API</p>
     */
    @RequestMapping("getFlowVersionCycle")
    @RestReturn(value=FlowVersionCycleDTO.class)
    public RestResponse getFlowVersionCycle( FlowVersionCycleCommand cmd ) {

        FlowVersionCycleDTO resultDTO = flowStatisticsService.getFlowVersionCycle(cmd) ;
        RestResponse response = new RestResponse(resultDTO);
        setResponseSuccess(response);
        return response;
    }

    /**
     * <b>URL: /flowstatistics/statisticsByLanes</b>
     * <p>3). 按泳道统计处理效率API</p>
     */
    @RequestMapping("statisticsByLanes")
    @RestReturn(value=StatisticsByLanesResponse.class)
    public RestResponse statisticsByLanes( StatisticsByLanesCommand cmd ) {

        StatisticsByLanesResponse resultDTO = flowStatisticsService.statisticsByLanes(cmd) ;
        RestResponse response = new RestResponse(resultDTO);
        setResponseSuccess(response);
        return response;
    }

    /**
     * <b>URL: /flowstatistics/statisticsByLanes</b>
     * <p>4). 按节点统计处理效率API</p>
     */
    @RequestMapping("statisticsByNodes")
    @RestReturn(value=StatisticsByNodesResponse.class)
    public RestResponse statisticsByNodes( StatisticsByNodesCommand cmd ) {

        StatisticsByNodesResponse resultDTO = flowStatisticsService.statisticsByNodes(cmd) ;
        RestResponse response = new RestResponse(resultDTO);
        setResponseSuccess(response);
        return response;
    }



    /**
     * <p>设置response 成功信息</p>
     * @param response
     */
    private void setResponseSuccess(RestResponse response){
        if(response == null ) return ;

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
    }

}
