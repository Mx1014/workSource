// @formatter:off
package com.everhomes.statistics.terminal;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.statistics.terminal.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <ul>
 * <li>终端統計api</li>
 * </ul>
 */
@RestDoc(value="Stat terminal controller", site="statTerminal")
@RestController
@RequestMapping("/stat/terminal")
public class StatTerminalController extends ControllerBase {
	
    @Autowired
    private StatTerminalService statTerminalService;

    /**
     * <b>URL: /stat/terminal/executeStatTask</b>
     * <p>執行任務</p>
     */
    @RequestMapping("executeStatTask")
    @RestReturn(value=TerminalStatisticsTaskDTO.class, collection = true)
    public RestResponse executeStatTask(@Valid ExecuteTaskCommand cmd) {
        RestResponse response = new RestResponse(statTerminalService.executeStatTask(cmd.getNamespaceId(), cmd.getStartDate(), cmd.getEndDate()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/terminal/executeUserSyncTask</b>
     * <p>執行用户同步任务</p>
     */
    @RequestMapping("executeUserSyncTask")
    @RestReturn(value=Long.class, collection = true)
    public RestResponse executeUserSyncTask(@Valid ExecuteSyncUserTaskCommand cmd) {
        List<Long> userIdList = statTerminalService.executeUserSyncTask(cmd.getNamespaceId());
        RestResponse response = new RestResponse(userIdList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/terminal/deleteStatTaskLog</b>
     * <p>删除统计任务log</p>
     */
    @RequestMapping("deleteStatTaskLog")
    @RestReturn(value=String.class)
    public RestResponse deleteStatTaskLog(@Valid DeleteStatTaskLogCommand cmd) {
        statTerminalService.deleteStatTaskLog(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/terminal/getTerminalHourNUNLineChart</b>
     * <p>获取终端新增用户的时段统计线图数据</p>
     */
    @RequestMapping("getTerminalHourNUNLineChart")
    @RestReturn(value=LineChart.class)
    public RestResponse getTerminalHourNUNLineChart(@Valid TerminalStatisticsChartCommand cmd) {
        RestResponse response = new RestResponse(statTerminalService.getTerminalHourLineChart(cmd.getDates(), TerminalStatisticsType.NEW_USER));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/terminal/getTerminalHourSNLineChart</b>
     * <p>获取终端启动次数的时段统计线图数据</p>
     */
    @RequestMapping("getTerminalHourSNLineChart")
    @RestReturn(value=LineChart.class)
    public RestResponse getTerminalHourSNLineChart(@Valid TerminalStatisticsChartCommand cmd) {
        RestResponse response = new RestResponse(statTerminalService.getTerminalHourLineChart(cmd.getDates(), TerminalStatisticsType.START));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/terminal/getTerminalHourCUNLineChart</b>
     * <p>获取终端时段累计用户的时段统计线图数据</p>
     */
    @RequestMapping("getTerminalHourCUNLineChart")
    @RestReturn(value=LineChart.class)
    public RestResponse getTerminalHourCUNLineChart(@Valid TerminalStatisticsChartCommand cmd) {
        RestResponse response = new RestResponse(statTerminalService.getTerminalHourLineChart(cmd.getDates(), TerminalStatisticsType.CUMULATIVE_USER));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/terminal/getTerminalHourAUNLineChart</b>
     * <p>获取终端活跃用户的时段统计线图数据</p>
     */
    @RequestMapping("getTerminalHourAUNLineChart")
    @RestReturn(value=LineChart.class)
    public RestResponse getTerminalHourAUNLineChart(@Valid TerminalStatisticsChartCommand cmd) {
        RestResponse response = new RestResponse(statTerminalService.getTerminalHourLineChart(cmd.getDates(), TerminalStatisticsType.ACTIVE_USER));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/terminal/qryTerminalDayStatisticsByDay</b>
     * <p>获取某一天的统计数据</p>
     */
    @RequestMapping("qryTerminalDayStatisticsByDay")
    @RestReturn(value=TerminalDayStatisticsDTO.class)
    public RestResponse qryTerminalDayStatisticsByDay(@Valid ListTerminalStatisticsByDayCommand cmd) {
        RestResponse response = new RestResponse(statTerminalService.qryTerminalDayStatisticsByDay(cmd.getDate()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/terminal/getTerminalDayNUNLineChart</b>
     * <p>获取终端新增用户的日统计线图数据</p>
     */
    @RequestMapping("getTerminalDayNUNLineChart")
    @RestReturn(value=LineChart.class)
    public RestResponse getTerminalDayNUNLineChart(@Valid ListTerminalStatisticsByDateCommand cmd) {
        RestResponse response = new RestResponse(statTerminalService.getTerminalDayLineChart(cmd.getStartDate(), cmd.getEndDate(), TerminalStatisticsType.NEW_USER));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/terminal/getTerminalDaySNLineChart</b>
     * <p>获取终端启动次数的日统计线图数据</p>
     */
    @RequestMapping("getTerminalDaySNLineChart")
    @RestReturn(value=LineChart.class)
    public RestResponse getTerminalDaySNLineChart(@Valid ListTerminalStatisticsByDateCommand cmd) {
        RestResponse response = new RestResponse(statTerminalService.getTerminalDayLineChart(cmd.getStartDate(), cmd.getEndDate(), TerminalStatisticsType.START));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/terminal/getTerminalDayCUNLineChart</b>
     * <p>获取终端累计用户的日统计线图数据</p>
     */
    @RequestMapping("getTerminalDayCUNLineChart")
    @RestReturn(value=LineChart.class)
    public RestResponse getTerminalDayCUNLineChart(@Valid ListTerminalStatisticsByDateCommand cmd) {
        RestResponse response = new RestResponse(statTerminalService.getTerminalDayLineChart(cmd.getStartDate(), cmd.getEndDate(), TerminalStatisticsType.CUMULATIVE_USER));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/terminal/getTerminalDayAUNLineChart</b>
     * <p>获取终端活跃用户的日统计线图数据</p>
     */
    @RequestMapping("getTerminalDayAUNLineChart")
    @RestReturn(value=LineChart.class)
    public RestResponse getTerminalDayAUNLineChart(@Valid ListTerminalStatisticsByDateCommand cmd) {
        RestResponse response = new RestResponse(statTerminalService.getTerminalDayLineChart(cmd.getStartDate(), cmd.getEndDate(), TerminalStatisticsType.ACTIVE_USER));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/terminal/listTerminalDayStatisticsByDate</b>
     * <p>获取一段时间范围的日统计数据</p>
     */
    @RequestMapping("listTerminalDayStatisticsByDate")
    @RestReturn(value=TerminalDayStatisticsDTO.class, collection = true)
    public RestResponse listTerminalDayStatisticsByDate(@Valid ListTerminalStatisticsByDateCommand cmd) {
        RestResponse response = new RestResponse(statTerminalService.listTerminalDayStatisticsByDate(cmd.getStartDate(), cmd.getEndDate()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/terminal/getTerminalAppVersionCUNPieChart</b>
     * <p>获取终端app版本累计用户占比饼图数据</p>
     */
    @RequestMapping("getTerminalAppVersionCUNPieChart")
    @RestReturn(value=PieChart.class)
    public RestResponse getTerminalAppVersionCUNPieChart(@Valid ListTerminalStatisticsByDayCommand cmd) {
        RestResponse response = new RestResponse(statTerminalService.getTerminalAppVersionPieChart(cmd.getDate(),TerminalStatisticsType.CUMULATIVE_USER));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/terminal/getTerminalAppVersionAUNPieChart</b>
     * <p>获取终端app版本活跃用户占比饼图数据</p>
     */
    @RequestMapping("getTerminalAppVersionAUNPieChart")
    @RestReturn(value=PieChart.class)
    public RestResponse getTerminalAppVersionAUNPieChart(@Valid ListTerminalStatisticsByDayCommand cmd) {
        RestResponse response = new RestResponse(statTerminalService.getTerminalAppVersionPieChart(cmd.getDate(),TerminalStatisticsType.ACTIVE_USER));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/terminal/listTerminalAppVersionStatisticsByDay</b>
     * <p>获取终端app版本统计数据列表</p>
     */
    @RequestMapping("listTerminalAppVersionStatisticsByDay")
    @RestReturn(value=TerminalAppVersionStatisticsDTO.class)
    public RestResponse listTerminalAppVersionStatisticsByDay(@Valid ListTerminalStatisticsByDayCommand cmd) {
        RestResponse response = new RestResponse(statTerminalService.listTerminalAppVersionStatistics(cmd.getDate()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
