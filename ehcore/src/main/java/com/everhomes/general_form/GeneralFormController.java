package com.everhomes.general_form;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.general_approval.*;
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
	 * <p> 提交表单值 </p>
	 */
	@RequestMapping("postGeneralForm")
	@RestReturn(value=PostGeneralFormDTO.class)
	public RestResponse postGeneralForm(PostGeneralFormValCommand cmd) {
		PostGeneralFormDTO dto = generalFormService.postGeneralForm(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");

		return response;
	}

	/**
	 * <b>URL: /general_form/updateGeneralFormVal</b>
	 * <p> 编辑表单值 </p>
	 */
	@RequestMapping("updateGeneralFormVal")
	@RestReturn(value=PostGeneralFormDTO.class)
	public RestResponse updateGeneralFormVal(PostGeneralFormValCommand cmd) {
		PostGeneralFormDTO dto = generalFormService.updateGeneralFormVal(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");

		return response;
	}

    /**
     * <b>URL: /general_form/getGeneralFormReminder</b>
     * <p> 表单提醒项</p>
     */
    @RequestMapping("getGeneralFormReminder")
    @RestReturn(value=GeneralFormReminderDTO.class)
    public RestResponse getGeneralFormReminder(GeneralFormReminderCommand cmd) {
        GeneralFormReminderDTO dto = generalFormService.getGeneralFormReminder(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

	/**
	 * <b>URL: /general_form/createGeneralFromPrintTemplate</b>
	 * <p> 新增表单打印模板 </p>
	 */
	@RequestMapping("createGeneralFromPrintTemplate")
	@RestReturn(value=GeneralFormPrintTemplateDTO.class)
	public RestResponse createGeneralFromPrintTemplate(AddGeneralFormPrintTemplateCommand cmd) {
		GeneralFormPrintTemplateDTO dto = generalFormService.createGeneralFormPrintTemplate(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");

		return response;
	}

    /**
     * <b>URL: /general_form/updateGeneralFromPrintTemplate</b>
     * <p> 更新表单打印模板 </p>
     */
    @RequestMapping("updateGeneralFromPrintTemplate")
    @RestReturn(value=GeneralFormPrintTemplateDTO.class)
    public RestResponse updateGeneralFromPrintTemplate(UpdateGeneralFormPrintTemplateCommand cmd) {
        GeneralFormPrintTemplateDTO dto = generalFormService.updateGeneralFormPrintTemplate(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

    /**
     * <b>URL: /general_form/getGeneralFromPrintTemplate</b>
     * <p> 获取表单打印模板 </p>
     */
    @RequestMapping("getGeneralFromPrintTemplate")
    @RestReturn(value=GeneralFormPrintTemplateDTO.class)
    public RestResponse getGeneralFromPrintTemplate(GetGeneralFormPrintTemplateCommand cmd) {
        GeneralFormPrintTemplateDTO dto = generalFormService.getGeneralFormPrintTemplate(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");

        return response;
    }

}