package com.everhomes.openapi;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.pmtask.PmTaskService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.pmtask.NotifyTaskResultCommand;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by ying.xiong on 2017/7/14.
 */
@RestDoc(value="pmtask open Constroller", site="core")
@RestController
@RequestMapping("/openapi")
public class PmTaskOpenController extends ControllerBase {

    @Autowired
    private PmTaskService pmTaskService;

    @RequestMapping("notifyTaskResult")
    @RestReturn(String.class)
    @RequireAuthentication(false)
    public RestResponse notifyTaskResult(@Valid NotifyTaskResultCommand cmd) {
        pmTaskService.notifyTaskResult(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
