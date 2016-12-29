package com.everhomes.general_approval.admin;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.general_approval.CreateApprovalFormCommand;
import com.everhomes.rest.general_approval.GeneralFormDTO;

@RestDoc(value="General approval admin controller", site="core")
@RestController
@RequestMapping("/admin/general_approval")
public class GeneralApprovalAdminController extends ControllerBase {
    /**
     * <b>URL: /admin/general_approval/createApprovalForm</b>
     * <p> 创建公司的表单 </p>
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
}
