// @formatter:off
package com.everhomes.statistics.event.admin;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.statistics.event.*;
import com.everhomes.statistics.event.StatEventJobService;
import com.everhomes.statistics.event.StatEventService;
import com.everhomes.statistics.event.StatEventTaskLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestDoc(value = "Stat event admin controller")
@RestController
@RequestMapping("/stat/event/admin")
public class StatEventAdminController extends ControllerBase {

    @Autowired
    private StatEventService statEventService;

    @Autowired
    private StatEventJobService statEventJobService;

    /**
     * <b>URL: /stat/event/admin/listEventPortalStat</b>
     * <p>获取门户配置记录</p>
     */
    @RequestMapping(value = "listEventPortalStat")
    @RestReturn(value = ListStatEventPortalStatResponse.class)
    public RestResponse listEventPortalStat(ListEventPortalStatCommand cmd) {
        ListStatEventPortalStatResponse resp = statEventService.listEventPortalStat(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/event/admin/listEventStat</b>
     * <p>获取事件统计结果</p>
     */
    @RequestMapping(value = "listEventStat")
    @RestReturn(value = StatEventStatDTO.class)
    public RestResponse listEventStat(StatListEventStatCommand cmd) {
        StatEventStatDTO dto = statEventService.listEventStat(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/event/admin/executeEventTask</b>
     * <p>执行统计任务</p>
     */
    @RequestMapping(value = "executeEventTask")
    @RestReturn(value = String.class)
    public RestResponse executeEventTask(StatExecuteEventTaskCommand cmd) {
        statEventJobService.executeTask(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/event/admin/listEventTask</b>
     * <p>统计任务列表</p>
     */
    @RequestMapping(value = "listEventTask")
    @RestReturn(value = StatEventTaskLog.class)
    public RestResponse listEventTask(StatExecuteEventTaskCommand cmd) {
        List<StatEventTaskLog> taskLog = statEventService.listEventTask(cmd);
        RestResponse response = new RestResponse(taskLog);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}