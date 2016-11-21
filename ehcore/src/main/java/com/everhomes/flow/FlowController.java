package com.everhomes.flow;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.flow.FlowButtonDTO;
import com.everhomes.rest.flow.FlowCaseDetailDTO;
import com.everhomes.rest.flow.FlowFireButtonCommand;
import com.everhomes.rest.flow.FlowPostSubjectCommand;
import com.everhomes.rest.flow.FlowPostSubjectDTO;
import com.everhomes.rest.flow.GetFlowCaseDetailByIdCommand;
import com.everhomes.rest.flow.SearchFlowCaseCommand;
import com.everhomes.rest.flow.SearchFlowCaseResponse;
import com.everhomes.rest.flow.ListFlowCaseLogsCommand;

@RestDoc(value="Flow controller", site="core")
@RestController
@RequestMapping("/flow")
public class FlowController {
	
	@Autowired
	private FlowService flowService;
	
    /**
     * <b>URL: /flow/searchFlowCases</b>
     * <p> 搜索某一个用户的 FlowCase </p>
     * @return FlowCase 的列表信息
     */
    @RequestMapping("searchFlowCases")
    @RestReturn(value=SearchFlowCaseResponse.class)
    public RestResponse searchFlowCases(@Valid SearchFlowCaseCommand cmd) {
        RestResponse response = new RestResponse(flowService.searchFlowCases(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /flow/getFlowCaseDetailById</b>
     * <p> 显示用户所有的 FlowCase </p>
     * @return FlowCase 的列表信息
     */
    @RequestMapping("getFlowCaseDetailById")
    @RestReturn(value=FlowCaseDetailDTO.class)
    public RestResponse getFlowCaseDetailById(@Valid GetFlowCaseDetailByIdCommand cmd) {
        RestResponse response = new RestResponse(flowService.getFlowCaseDetail(cmd.getFlowCaseId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /flow/postSubject</b>
     * <p> 显示用户所有的 FlowCase </p>
     * @return FlowCase 的列表信息
     */
    @RequestMapping("postSubject")
    @RestReturn(value=FlowPostSubjectDTO.class)
    public RestResponse postSubject(@Valid FlowPostSubjectCommand cmd) {
        RestResponse response = new RestResponse(flowService.postSubject(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /flow/fireButton</b>
     * <p> 显示用户所有的 FlowCase </p>
     * @return FlowCase 的列表信息
     */
    @RequestMapping("fireButton")
    @RestReturn(value=FlowButtonDTO.class)
    public RestResponse fireButton(@Valid FlowFireButtonCommand cmd) {
        RestResponse response = new RestResponse(flowService.fireButton(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * TODO 1. post subject 2. event logs. {title, logo, date, message, processor, applier}
     * 
     */
}
