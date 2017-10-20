package com.everhomes.general_approval;

import javax.validation.Valid;

import com.everhomes.rest.general_approval.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

import java.util.List;
//import com.everhomes.rest.general_approval.GetActiveGeneralFormByOriginIdCommand;
//import com.everhomes.rest.general_approval.GetActiveGeneralFormByOriginIdResponse;

//import com.everhomes.rest.general_approval.PostFormCommand;
//import com.everhomes.rest.general_approval.PostFormResponse;

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
    
//    /**
//     * <b>URL: /general_approval/getActiveGeneralFormByOriginId</b>
//     * <p> 获取不带工作流的表单的信息 </p>
//     * 
//     */
//    @RequestMapping("getActiveGeneralFormByOriginId")
//    @RestReturn(value=GetActiveGeneralFormByOriginIdResponse.class)
//    public RestResponse getActiveGeneralFormByOriginId(@Valid GetActiveGeneralFormByOriginIdCommand cmd) {
//    	GetTemplateByApprovalIdResponse result = generalApprovalService.getActiveGeneralFormByOriginId(cmd);
//    	RestResponse response = new RestResponse(result);
//    	response.setErrorCode(ErrorCodes.SUCCESS);
//    	response.setErrorDescription("OK");
//    	
//    	return response;
//    }

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
     
//    /**
//     * <b>URL: /general_approval/postForm</b>
//     * <p> 提交不关联工作流的表单的数据，并获取表单的信息 </p>
//     */
//    @RequestMapping("postForm")
//    @RestReturn(value=PostFormResponse.class)
//    public RestResponse postForm(@Valid PostFormCommand cmd) {
//    	GetTemplateByApprovalIdResponse result = generalApprovalService.postForm(cmd);
//    	RestResponse response = new RestResponse(result);
//    	response.setErrorCode(ErrorCodes.SUCCESS);
//    	response.setErrorDescription("OK");
//    	
//    	return response;
//    }

	/**
	 * <b>URL: /general_approval/createApprovalTemplates</b>
	 * <p> 创建审批模板 </p>
	 */
	@RequestMapping("createApprovalTemplates")
	@RestReturn(value=String.class)
	public RestResponse createApprovalTemplates(@Valid CreateApprovalTemplatesCommand cmd) {
		generalApprovalService.createApprovalTemplates(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");

		return response;
	}

	/**
	 * <b>URL: /general_approval/listGeneralApprovalRecords</b>
	 * <p> 根据模块 id 获取对应的审批记录 </p>
	 */
	@RequestMapping("listGeneralApprovalRecords")
	@RestReturn(value=ListGeneralApprovalRecordsResponse.class, collection = true)
	public RestResponse listGeneralApprovalRecords(@Valid ListGeneralApprovalRecordsCommand cmd) {
		ListGeneralApprovalRecordsResponse results =  generalApprovalService.listGeneralApprovalRecords(cmd);
		RestResponse response = new RestResponse(results);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");

		return response;
	}
}