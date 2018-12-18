package com.everhomes.openapi;

import com.everhomes.aclink.dingxin.DingxinService;
import com.everhomes.rest.aclink.*;
import com.everhomes.util.RequireAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.aclink.AclinkLogService;
import com.everhomes.aclink.DoorAccessService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

@RestDoc(value = "Aclink open Constroller", site = "core")
@RestController
@RequestMapping("/openapi/aclink")
public class AclinkOpenController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(AclinkOpenController.class); 
    @Autowired
    private DoorAccessService doorAccessService;
    @Autowired
    private AclinkLogService aclinkLogService;
    @Autowired
    private DingxinService dingxinService;

    /**
     * 
     * <b>URL: /openapi/aclink/batchCreateVisitors</b>
     * <p>批量授权访客 </p>
     * @return
     */
    @RequestMapping("batchCreateVisitors")
    @RestReturn(BatchCreateVisitorsResponse.class)
    public RestResponse batchCreateVisitors(BatchCreateVisitorsCommand cmd){
    	RestResponse response = new RestResponse(doorAccessService.batchCreateVisitors(cmd));
    	response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * 
     * <b>URL: /openapi/aclink/invalidVistorAuths</b>
     * <p>批量取消授权</p>
     * @return
     */
    @RequestMapping("invalidVistorAuths")
    @RestReturn(String.class)
    public RestResponse invalidVistorAuths(InvalidVistorAuthsCommand cmd){
    	doorAccessService.invalidVistorAuths(cmd);
    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * 
     * <b>URL: /openapi/aclink/queryLogs</b>
     * <p>查询开门记录 </p>
     * @return
     */
    @RequestMapping("queryLogs")
    @RestReturn(OpenQueryLogResponse.class)
    public RestResponse queryLogs(OpenQueryLogCommand cmd){
    	RestResponse response = new RestResponse(aclinkLogService.openQueryLogs(cmd));
    	response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     *
     * <b>URL: /openapi/aclink/verifyDoorAuth</b>
     * <p> (鼎芯)判断是否有开门权限</p>
     * @return
     */
    @RequestMapping("verifyDoorAuth")
    @RestReturn(value=String.class)
    @RequireAuthentication(value = false)
    public RestResponse verifyOpenAuth(VerifyDoorAuthCommand cmd) {
        RestResponse response = new RestResponse();
        response.setResponseObject(dingxinService.verifyDoorAuth(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
