package com.everhomes.flow.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.flow.FlowService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.aclink.ListDoorAccessResponse;
import com.everhomes.rest.aclink.QueryDoorAccessAdminCommand;
import com.everhomes.rest.flow.CreateFlowCommand;
import com.everhomes.rest.flow.FlowDTO;
import com.everhomes.rest.flow.FlowIdCommand;
import com.everhomes.rest.flow.ListFlowBriefResponse;
import com.everhomes.rest.flow.ListFlowCommand;
import com.everhomes.rest.flow.UpdateFlowNameCommand;

@RestDoc(value="Flow Admin controller", site="core")
@RestController
@RequestMapping("/admin/flow")
public class FlowAdminController {
	
	@Autowired
	private FlowService flowService;
	
    /**
     * <b>URL: /admin/flow/createFlow</b>
     * <p> 创建一个新 Flow，一个业务模块，名字不能重复 </p>
     * @return Flow 的详细信息
     */
    @RequestMapping("createFlow")
    @RestReturn(value=FlowDTO.class)
    public RestResponse createFlow(@Valid CreateFlowCommand cmd) {
        RestResponse response = new RestResponse(flowService.createFlow(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/listFlows</b>
     * <p> 显示业务模块下的所有 Flow </p>
     * @return Flow 的列表
     */
    @RequestMapping("listFlows")
    @RestReturn(value=ListFlowBriefResponse.class)
    public RestResponse listFlows(@Valid ListFlowCommand cmd) {
        RestResponse response = new RestResponse(flowService.listBriefFlows(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/enableFlow</b>
     * <p> 启用某一个业务模块下的工作流，会自动把之前的工作流禁用 </p>
     * @return
     */
    @RequestMapping("enableFlow")
    @RestReturn(value=String.class)
    public RestResponse enableFlow(@Valid FlowIdCommand cmd) {
    		flowService.enableFlow(cmd.getFlowId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/disableFlow</b>
     * <p> 启用某一个业务模块下的工作流 </p>
     * @return
     */
    @RequestMapping("disableFlow")
    @RestReturn(value=String.class)
    public RestResponse disableFlow(@Valid FlowIdCommand cmd) {
    		flowService.disableFlow(cmd.getFlowId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/deleteFlow</b>
     * <p> 删除某一个工作流 </p>
     * @return
     */
    @RequestMapping("deleteFlow")
    @RestReturn(value=String.class)
    public RestResponse deleteFlow(@Valid FlowIdCommand cmd) {
    		flowService.deleteFlow(cmd.getFlowId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/flow/updateFlowName</b>
     * <p> 修改工作流的名字 </p>
     * @return
     */
    @RequestMapping("updateFlowName")
    @RestReturn(value=FlowDTO.class)
    public RestResponse updateFlowName(@Valid UpdateFlowNameCommand cmd) {
        RestResponse response = new RestResponse(flowService.updateFlowName(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
