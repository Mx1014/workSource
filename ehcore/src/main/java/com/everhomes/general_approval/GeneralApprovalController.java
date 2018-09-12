package com.everhomes.general_approval;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.general_approval.CreateOrUpdateGeneralFormValuesWithFlowCommand;
import com.everhomes.rest.general_approval.GetGeneralFormsAndValuesByFlowNodeCommand;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdCommand;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdResponse;
import com.everhomes.rest.general_approval.GetUserRealNameCommand;
import com.everhomes.rest.general_approval.ListActiveGeneralApprovalCommand;
import com.everhomes.rest.general_approval.ListGeneralApprovalCommand;
import com.everhomes.rest.general_approval.ListGeneralApprovalResponse;
import com.everhomes.rest.general_approval.ListGeneralFormResponse;
import com.everhomes.rest.general_approval.PostApprovalFormCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestDoc(value="General approval controller", site="core")
@RestController
@RequestMapping("/general_approval")
public class GeneralApprovalController extends ControllerBase {
	public static final Long MODULE_ID = 52000L;
	@Autowired
	private GeneralApprovalService generalApprovalService;
    /**
     * <b>URL: /general_approval/getTemplateByApprovalId</b>
     * <p> 获取表单的信息 </p>
     * @return GetTemplateByApprovalIdResponse 表单的数据信息
     */
    @RequestMapping("getTemplateByApprovalId")
    @RestReturn(value=GetTemplateByApprovalIdResponse.class)
    public RestResponse getTemplateByApprovalId(@Valid GetTemplateByApprovalIdCommand cmd) {
    	GetTemplateByApprovalIdResponse result = generalApprovalService.getTemplateByApprovalId(cmd);
    	RestResponse response = new RestResponse(result);
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	
    	return response;
    }

    /**
     * <b>URL: /general_approval/listActiveGeneralApproval</b>
     * <p> 审批列表 </p>
     * @return
     */
    @RequestMapping("listActiveGeneralApproval")
    @RestReturn(value=ListGeneralApprovalResponse.class)
    public RestResponse listActiveGeneralApproval(@Valid ListActiveGeneralApprovalCommand  cmd) {
    	ListGeneralApprovalResponse result = generalApprovalService.listActiveGeneralApproval(cmd);
    	RestResponse response = new RestResponse(result);
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	
    	return response;
    }
    
    /**
     * <b>URL: /general_approval/postApprovalForm</b>
     * <p> 提交数据，并获取表单的信息 </p>
     * @return GetTemplateByApprovalIdResponse 表单的数据信息
     */
    @RequestMapping("postApprovalForm")
    @RestReturn(value=GetTemplateByApprovalIdResponse.class)
    public RestResponse postApprovalForm(@Valid PostApprovalFormCommand cmd) {
    	GetTemplateByApprovalIdResponse result = generalApprovalService.postApprovalForm(cmd);
    	RestResponse response = new RestResponse(result);
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	
    	return response;
    }

	/**
	 * <b>URL: /general_approval/listAvailableGeneralApprovals</b>
	 * <p> 根据可见范围获取审批列表 </p>
	 */
	@RequestMapping("listAvailableGeneralApprovals")
	@RestReturn(value=ListGeneralApprovalResponse.class)
	public RestResponse listAvailableGeneralApprovals(ListGeneralApprovalCommand cmd) {
		ListGeneralApprovalResponse res = generalApprovalService.listAvailableGeneralApprovals(cmd);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");

		return response;
	}

	/**
	 * <b>URL: /general_approval/getUserRealName</b>
	 * <p> 获取用户在公司中的信息 </p>
	 */
	@RequestMapping("getUserRealName")
	@RestReturn(value=String.class)
	public RestResponse getUserContactName(GetUserRealNameCommand cmd) {
		String res = generalApprovalService.getUserRealName(cmd);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");

		return response;
	}

	/**
	 * <b>URL: /general_approval/getGeneralFormsAndValuesByFlowNode</b>
	 * <p> 根据工作流节点获取表单列表和值 </p>
	 */
	@RequestMapping("getGeneralFormsAndValuesByFlowNode")
	@RestReturn(value = ListGeneralFormResponse.class)
	public RestResponse getGeneralFormsAndValuesByFlowNode(@Valid GetGeneralFormsAndValuesByFlowNodeCommand cmd) {
		ListGeneralFormResponse listGeneralFormResponse = generalApprovalService.getGeneralFormsAndValuesByFlowNode(cmd);
		RestResponse response = new RestResponse(listGeneralFormResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /general_approval/createOrUpdateGeneralFormValuesWithFlow</b>
	 * <p> 编辑工作流表单值 </p>
	 */
	@RequestMapping("createOrUpdateGeneralFormValuesWithFlow")
	@RestReturn(value = String.class)
	public RestResponse createOrUpdateGeneralFormValuesWithFlow(CreateOrUpdateGeneralFormValuesWithFlowCommand cmd) {
		generalApprovalService.createOrUpdateGeneralFormValuesWithFlow(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}