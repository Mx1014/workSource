package com.everhomes.general_form;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.general_approval.GeneralApprovalService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.general_approval.*;
import com.everhomes.rest.techpark.expansion.LeaseFormRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestDoc(value="General form controller", site="core")
@RestController
@RequestMapping("/general_form")
public class GeneralFormController extends ControllerBase {

	@Autowired
	private GeneralFormService generalFormService;
//    /**
//     * <b>URL: /general_form/getTemplateByApprovalId</b>
//     * <p> 获取表单的信息 </p>
//     * @return GetTemplateByApprovalIdResponse 表单的数据信息
//     */
//    @RequestMapping("getTemplateByApprovalId")
//    @RestReturn(value=GetTemplateByApprovalIdResponse.class)
//    public RestResponse getTemplateByApprovalId(@Valid GetTemplateByApprovalIdCommand cmd) {
//    	GetTemplateByApprovalIdResponse result = generalApprovalService.getTemplateByApprovalId(cmd);
//    	RestResponse response = new RestResponse(result);
//    	response.setErrorCode(ErrorCodes.SUCCESS);
//    	response.setErrorDescription("OK");
//
//    	return response;
//    }

	/**
	 * <b>URL: /general_form/getTemplateBySourceId</b>
	 * <p> 根据业务获取表单 </p>
	 */
	@RequestMapping("getTemplateBySourceId")
	@RestReturn(value=GeneralFormDTO.class)
	public RestResponse getTemplateBySourceId(@Valid GetTemplateBySourceIdCommand cmd) {

		GeneralFormDTO dto = generalFormService.getTemplateBySourceId(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");

		return response;
	}

    /**
     * <b>URL: /general_form/postGeneralForm</b>
     * <p> 提交数据，并获取表单的信息 </p>
     */
    @RequestMapping("postGeneralForm")
    @RestReturn(value=PostGeneralFormDTO.class)
    public RestResponse postGeneralForm(PostGeneralFormCommand cmd) {
		PostGeneralFormDTO dto = generalFormService.postGeneralForm(cmd);
    	RestResponse response = new RestResponse(dto);
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	
    	return response;
    }

//	/**
//	 * <b>URL: /general_form/postApprovalForm</b>
//	 * <p> 提交数据，并获取表单的信息 </p>
//	 * @return GetTemplateByApprovalIdResponse 表单的数据信息
//	 */
//	@RequestMapping("postApprovalForm")
//	@RestReturn(value=GetTemplateByApprovalIdResponse.class)
//	public RestResponse postApprovalForm(@Valid PostApprovalFormCommand cmd) {
//		GetTemplateByApprovalIdResponse result = generalApprovalService.postApprovalForm(cmd);
//		RestResponse response = new RestResponse(result);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//
//		return response;
//	}
}