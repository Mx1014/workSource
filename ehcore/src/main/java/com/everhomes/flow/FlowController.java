package com.everhomes.flow;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.flow.ListFlowCaseByUserIdCommand;
import com.everhomes.rest.flow.ListFlowCaseByUserIdResponse;
import com.everhomes.rest.flow.ListFlowCaseLogsCommand;

@RestDoc(value="Flow controller", site="core")
@RestController
@RequestMapping("/flow")
public class FlowController {
	
	@Autowired
	private FlowService flowService;
	
    /**
     * <b>URL: /flow/listFlowCasesByUserId</b>
     * <p> 显示用户所有的 FlowCase </p>
     * @return FlowCase 的列表信息
     */
    @RequestMapping("listFlowCasesByUserId")
    @RestReturn(value=ListFlowCaseByUserIdResponse.class)
    public RestResponse createFlow(@Valid ListFlowCaseByUserIdCommand cmd) {
        RestResponse response = new RestResponse(flowService.listFlowCasesByUserId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /flow/listFlowCaseLogs</b>
     * <p> 显示用户所有的 FlowCase </p>
     * @return FlowCase 的列表信息
     */
    @RequestMapping("listFlowCaseLogs")
    @RestReturn(value=ListFlowCaseByUserIdResponse.class)
    public RestResponse listFlowCaseLogs(@Valid ListFlowCaseLogsCommand cmd) {
        RestResponse response = new RestResponse(flowService.listFlowCaseLogsCommand(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * TODO 1. post subject 2. event logs. {title, logo, date, message, processor, applier}
     * 
     */
}
