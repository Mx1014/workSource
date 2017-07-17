// @formatter:off
package com.everhomes.statistics.event;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.statistics.event.StatPostDeviceCommand;
import com.everhomes.rest.statistics.event.StatPostDeviceResponse;
import com.everhomes.rest.statistics.event.StatPostLogFileCommand;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <ul>
 *     <li>用户行为统计</li>
 * </ul>
 */
@RestDoc(value = "Stat event controller")
@RestController
@RequestMapping("/stat/event")
public class StatEventController extends ControllerBase {

    @Autowired
    private StatEventService statEventService;

    /**
     * <b>URL: /stat/event/postLog</b>
     * <p>上传事件log</p>
     */
    @RequireAuthentication(false)
    @RequestMapping(value = "postLog", method = RequestMethod.POST)
    @RestReturn(value = String.class)
    public RestResponse postLog(HttpServletRequest request) {
        statEventService.postLog(request);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/event/postDevice</b>
     * <p>上传设备信息, 返回sessionId和上传策略</p>
     */
    @RequireAuthentication(false)
    @RequestMapping(value = "postDevice", method = RequestMethod.POST)
    @RestReturn(value = StatPostDeviceResponse.class)
    public RestResponse postDevice(StatPostDeviceCommand cmd, HttpServletRequest request) {
        StatPostDeviceResponse resp = statEventService.postDevice(cmd, request.getRemoteAddr(), request.getLocalAddr());
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /stat/event/postLogFile</b>
     * <p>上传App log文件</p>
     */
    @RequireAuthentication(false)
    @RequestMapping(value = "postLogFile", method = RequestMethod.POST)
    @RestReturn(value = String.class)
    public RestResponse postLogFile(StatPostLogFileCommand cmd) {
        statEventService.postLogFile(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
