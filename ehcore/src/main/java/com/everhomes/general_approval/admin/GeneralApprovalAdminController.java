package com.everhomes.general_approval.admin;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.general_approval.ApprovalFormIdCommand;
import com.everhomes.rest.general_approval.CreateApprovalFormCommand;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.ListApprovalFormsCommand;
import com.everhomes.rest.general_approval.ListGeneralFormResponse;

@RestDoc(value="General approval admin controller", site="core")
@RestController
@RequestMapping("/admin/general_approval")
public class GeneralApprovalAdminController extends ControllerBase {
    /**
     * <b>URL: /admin/general_approval/createApprovalForm</b>
     * <p> 创建公司的表单，如果存在 formOriginId 则为修改 </p>
     * @return
     */
    @RequestMapping("createApprovalForm")
    @RestReturn(value=GeneralFormDTO.class)
    public RestResponse getTemplateByApprovalId(@Valid CreateApprovalFormCommand cmd) {
    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	
    	return response;
    }
    
    /**
     * <b>URL: /admin/general_approval/listApprovalForms</b>
     * <p> 获取表单列表 </p>
     * @return
     */
    @RequestMapping("listApprovalForms")
    @RestReturn(value=ListGeneralFormResponse.class)
    public RestResponse listApprovalForms(@Valid ListApprovalFormsCommand cmd) {
    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	
    	return response;
    }

    /**
     * <b>URL: /admin/general_approval/deleteApprovalFormById</b>
     * <p> 删除某个表单 </p>
     * @return
     */
    @RequestMapping("deleteApprovalFormById")
    @RestReturn(value=String.class)
    public RestResponse deleteApprovalFormById(@Valid ApprovalFormIdCommand cmd) {
    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	
    	return response;
    }
    
}
