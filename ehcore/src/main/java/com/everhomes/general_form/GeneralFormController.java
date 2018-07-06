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
import java.util.List;

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
	 * <b>URL: /general_form/searchGeneralFormVals</b>
	 * <p>表单值搜索</p>
	 */
	@RequestMapping("searchGeneralFormVals")
	@RestReturn(value=SearchFormValDTO.class)
	public RestResponse searchGeneralFormVals(SearchFormValsCommand cmd) {
		SearchFormValDTO dto = generalFormService.searchGeneralFormVals(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");

		return response;
	}

	/**
	 * <b>URL: /general_form/listDefaultFields</b>
	 * <p>根據模块获取默认字段	</p>
	 */
	@RequestMapping("listDefaultFields")
	@RestReturn(value=GeneralFormFieldDTO.class)
	public RestResponse listDefaultFields(ListDefaultFieldsCommand cmd){
		List<GeneralFormFieldDTO> dtos = generalFormService.getDefaultFieldsByModuleId(cmd);
		RestResponse response = new RestResponse(dtos);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");

		return response;
	}


	/**
	 * <b>URL: /general_form/updateGeneralFormVal</b>
	 * <p> 删除表单值 </p>
	 */
	@RequestMapping("deleteGeneralFormVal")
	@RestReturn(value=String.class)
	public RestResponse deleteGeneralFormVal(PostGeneralFormValCommand cmd) {
		generalFormService.deleteGeneralFormVal(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /general_form/getGeneralFormVal</b>
	 * <p> 获取表单值 </p>
	 */
	@RequestMapping("getGeneralFormVal")
	@RestReturn(value=GeneralFormValDTO.class, collection = true)
	public RestResponse getGeneralFormVal(GetGeneralFormValCommand cmd) {
		generalFormService.getGeneralFormVal(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /general_form/saveGeneralFormVal</b>
	 * <p> 只是保存,区别于 postGeneralForm</p>
	 */
	@RequestMapping("saveGeneralFormVal")
	@RestReturn(value=String.class)
	public RestResponse saveGeneralFormVal(PostGeneralFormValCommand cmd) {

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}