package com.everhomes.flow;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.flow.FlowButtonDTO;
import com.everhomes.rest.flow.FlowCaseDetailDTO;
import com.everhomes.rest.flow.FlowCaseSearchType;
import com.everhomes.rest.flow.FlowEvaluateDTO;
import com.everhomes.rest.flow.FlowFireButtonCommand;
import com.everhomes.rest.flow.FlowGetSubjectDetailById;
import com.everhomes.rest.flow.FlowPostEvaluateCommand;
import com.everhomes.rest.flow.FlowPostSubjectCommand;
import com.everhomes.rest.flow.FlowPostSubjectDTO;
import com.everhomes.rest.flow.FlowSubjectDTO;
import com.everhomes.rest.flow.FlowUserType;
import com.everhomes.rest.flow.GetFlowCaseDetailByIdCommand;
import com.everhomes.rest.flow.ListButtonProcessorSelectionsCommand;
import com.everhomes.rest.flow.ListFlowModulesCommand;
import com.everhomes.rest.flow.ListFlowModulesResponse;
import com.everhomes.rest.flow.ListFlowUserSelectionResponse;
import com.everhomes.rest.flow.SearchFlowCaseCommand;
import com.everhomes.rest.flow.SearchFlowCaseResponse;
import com.everhomes.user.UserContext;

@RestDoc(value="Flow controller", site="core")
@RestController
@RequestMapping("/flow")
public class FlowController extends ControllerBase {
	
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
    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	
    	Byte admin = FlowCaseSearchType.ADMIN.getCode();
    	if(admin.equals(cmd.getFlowCaseSearchType())) {
    		//never admin here
    		return response;
    	}
    	
    	response.setResponseObject(flowService.searchFlowCases(cmd));
    	
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
    	Long userId = UserContext.current().getUser().getId();
        RestResponse response = new RestResponse(flowService.getFlowCaseDetail(cmd.getFlowCaseId()
        		, userId
        		, FlowUserType.fromCode(cmd.getFlowUserType())
        		, true));
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
     * <b>URL: /flow/getSubjectById</b>
     * <p> 显示某一个附言对应的详细信息 </p>
     * @return FlowCase 的列表信息
     */
    @RequestMapping("getSubjectById")
    @RestReturn(value=FlowSubjectDTO.class)
    public RestResponse postSubject(@Valid FlowGetSubjectDetailById cmd) {
        RestResponse response = new RestResponse(flowService.getSubectById(cmd.getSubjectId()));
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
     * <b>URL: /flow/listButtonSelections</b>
     * <p> 列出按钮对应的用户列表 </p>
     * @return 返回评价信息
     */
    @RequestMapping("listButtonProcessorSelections")
    @RestReturn(value=ListFlowUserSelectionResponse.class)
    public RestResponse listButtonSelections(@Valid ListButtonProcessorSelectionsCommand cmd) {
        RestResponse response = new RestResponse(flowService.listButtonProcessorSelections(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /flow/postEvaluate</b>
     * <p> 对节点进行评价 </p>
     * @return 返回评价信息
     */
    @RequestMapping("postEvaluate")
    @RestReturn(value=FlowEvaluateDTO.class)
    public RestResponse postEvaluate(@Valid FlowPostEvaluateCommand cmd) {
        RestResponse response = new RestResponse(flowService.postEvaluate(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /flow/listModules</b>
     * <p> 对节点进行评价 </p>
     * @return 返回评价信息
     */
    @RequestMapping("listModules")
    @RestReturn(value=ListFlowModulesResponse.class)
    public RestResponse listModules(@Valid ListFlowModulesCommand cmd) {
        RestResponse response = new RestResponse(flowService.listModules(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /flow/testCase</b>
     * <p> 仅仅用于测试 </p>
     * @return 
     */
    @RequestMapping("testCase")
    @RestReturn(value=String.class)
    public RestResponse testCase() {
    	flowService.testFlowCase();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
}
