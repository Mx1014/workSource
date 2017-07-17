// @formatter:off
package com.everhomes.statistics.event.admin;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.statistics.event.ListEventPortalStatCommand;
import com.everhomes.rest.statistics.event.StatEventPortalStatDTO;
import com.everhomes.rest.statistics.event.StatEventStatDTO;
import com.everhomes.rest.statistics.event.StatListEventStatCommand;
import com.everhomes.statistics.event.StatEventService;
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

    /**
     * <b>URL: /stat/event/admin/listEventPortalStat</b>
     * <p>获取门户配置记录</p>
     */
    @RequestMapping(value = "listEventPortalStat")
    @RestReturn(value = StatEventPortalStatDTO.class, collection = true)
    public RestResponse listEventPortalStat(ListEventPortalStatCommand cmd) {
        List<StatEventPortalStatDTO> list = statEventService.listEventPortalStat(cmd);
        RestResponse response = new RestResponse(list);
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
}