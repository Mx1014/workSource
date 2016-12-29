package com.everhomes.general_approval;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdCommand;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdResponse;

@RestDoc(value="General approval controller", site="core")
@RestController
@RequestMapping("/general_approval")
public class GeneralApprovalController extends ControllerBase {
    /**
     * <b>URL: /general_approval/getTemplateByApprovalId</b>
     * <p> 获取表单的信息 </p>
     * @return GetTemplateByApprovalIdResponse 表单的数据信息
     */
    @RequestMapping("getTemplateByApprovalId")
    @RestReturn(value=GetTemplateByApprovalIdResponse.class)
    public RestResponse getTemplateByApprovalId(@Valid GetTemplateByApprovalIdCommand cmd) {
    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	
    	return response;
    }
    
    @RequestMapping("postApprovalForm")
    @RestReturn(value=GetTemplateByApprovalIdResponse.class)
    public RestResponse postApprovalForm(@Valid GetTemplateByApprovalIdCommand cmd) {
    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	
    	return response;
    }
}