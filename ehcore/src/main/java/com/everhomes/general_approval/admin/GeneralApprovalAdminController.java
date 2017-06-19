package com.everhomes.general_approval.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.general_approval.GeneralApprovalService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.general_approval.ApprovalFormIdCommand;
import com.everhomes.rest.general_approval.CreateApprovalFormCommand;
import com.everhomes.rest.general_approval.CreateGeneralApprovalCommand;
import com.everhomes.rest.general_approval.GeneralApprovalDTO;
import com.everhomes.rest.general_approval.GeneralApprovalIdCommand;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdResponse;
import com.everhomes.rest.general_approval.ListApprovalFormsCommand;
import com.everhomes.rest.general_approval.ListGeneralApprovalCommand;
import com.everhomes.rest.general_approval.ListGeneralApprovalResponse;
import com.everhomes.rest.general_approval.ListGeneralFormResponse;
import com.everhomes.rest.general_approval.UpdateApprovalFormCommand;
import com.everhomes.rest.general_approval.UpdateGeneralApprovalCommand;

@RestDoc(value="General approval admin controller", site="core")
@RestController
@RequestMapping("/admin/general_approval")
public class GeneralApprovalAdminController extends ControllerBase {
	@Autowired
	private GeneralApprovalService generalApprovalService;
    /**
     * <b>URL: /admin/general_approval/createApprovalForm</b>
     * <p> 创建公司的表单  </p>
     * @return
     */
    @RequestMapping("createApprovalForm")
    @RestReturn(value=GeneralFormDTO.class)
    public RestResponse createApprovalForm(@Valid CreateApprovalFormCommand cmd) {
    	GeneralFormDTO result = generalApprovalService.createApprovalForm(cmd);
    	RestResponse response = new RestResponse(result);
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	
    	return response;
    }
    /**
     * <b>URL: /admin/general_approval/updateApprovalForm</b>
     * <p> 修改 公司的表单    </p>
     * @return
     */
    @RequestMapping("updateApprovalForm")
    @RestReturn(value=GeneralFormDTO.class)
    public RestResponse updateApprovalForm(@Valid UpdateApprovalFormCommand cmd) {
    	GeneralFormDTO result = generalApprovalService.updateApprovalForm(cmd);
    	RestResponse response = new RestResponse(result);
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	
    	return response;
    }

    /**
     * <b>URL: /admin/general_approval/getApprovalForm</b>
     * <p> 获取 公司的表单    </p>
     * @return
     */
    @RequestMapping("getApprovalForm")
    @RestReturn(value=GeneralFormDTO.class)
    public RestResponse getApprovalForm(@Valid ApprovalFormIdCommand cmd) {
    	GeneralFormDTO result = generalApprovalService.getApprovalForm(cmd);
    	RestResponse response = new RestResponse(result);
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
    	ListGeneralFormResponse result = generalApprovalService.listApprovalForms(cmd);
    	RestResponse response = new RestResponse(result);
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
    	generalApprovalService.deleteApprovalFormById(cmd);
    	RestResponse response = new RestResponse( );
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	
    	return response;
    }
    
    /**
     * <b>URL: /admin/general_approval/createGeneralApproval</b>
     * <p> 创建某个新的审批 </p>
     * @return
     */
    @RequestMapping("createGeneralApproval")
    @RestReturn(value=GeneralApprovalDTO.class)
    public RestResponse createGeneralApproval(@Valid CreateGeneralApprovalCommand cmd) {
    	GeneralApprovalDTO result = generalApprovalService.createGeneralApproval(cmd);
    	RestResponse response = new RestResponse(result);
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	
    	return response;
    }
    
    /**
     * <b>URL: /admin/general_approval/listGeneralApproval</b>
     * <p> 审批列表 </p>
     * @return
     */
    @RequestMapping("listGeneralApproval")
    @RestReturn(value=ListGeneralApprovalResponse.class)
    public RestResponse listGeneralApproval(@Valid ListGeneralApprovalCommand cmd) {
    	ListGeneralApprovalResponse result = generalApprovalService.listGeneralApproval(cmd);
    	RestResponse response = new RestResponse(result);
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	
    	return response;
    }
    
    /**
     * <b>URL: /admin/general_approval/updateGeneralApproval</b>
     * <p> 修改审批 </p>
     * @return
     */
    @RequestMapping("updateGeneralApproval")
    @RestReturn(value=GeneralApprovalDTO.class)
    public RestResponse updateGeneralApproval(@Valid UpdateGeneralApprovalCommand cmd) {
    	GeneralApprovalDTO result = generalApprovalService.updateGeneralApproval(cmd);
    	RestResponse response = new RestResponse(result);
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	
    	return response;
    }
    
    /**
     * <b>URL: /admin/general_approval/deleteGeneralApproval</b>
     * <p> 删除审批 </p>
     * @return
     */
    @RequestMapping("deleteGeneralApproval")
    @RestReturn(value=String.class)
    public RestResponse deleteGeneralApproval(@Valid GeneralApprovalIdCommand cmd) {
    	generalApprovalService.deleteGeneralApproval(cmd);
    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	
    	return response;
    }
    /**
     * <b>URL: /admin/general_approval/enableGeneralApproval</b>
     * <p> 启用审批 </p>
     * @return
     */
    @RequestMapping("enableGeneralApproval")
    @RestReturn(value=String.class)
    public RestResponse enableGeneralApproval(@Valid GeneralApprovalIdCommand cmd) {
    	generalApprovalService.enableGeneralApproval(cmd);
    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	
    	return response;
    }
    /**
     * <b>URL: /admin/general_approval/disableGeneralApproval</b>
     * <p> 不启用审批 </p>
     * @return
     */
    @RequestMapping("disableGeneralApproval")
    @RestReturn(value=String.class)
    public RestResponse disableGeneralApproval(@Valid GeneralApprovalIdCommand cmd) {
    	generalApprovalService.disableGeneralApproval(cmd);
    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	
    	return response;
    }
}
