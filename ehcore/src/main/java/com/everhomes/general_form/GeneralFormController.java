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

	@Autowired
	private GeneralFormSearcher generalFormSearcher;

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

    /**postGeneralFormVal
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
		ListGeneralFormValResponse dto = generalFormSearcher.queryGeneralForm(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");

		return response;
	}



	/**
     * <b>URL: /general_form/listDefaultFields</b>
     * <p>根據模块获取默认字段    </p>
     */
    @RequestMapping("listDefaultFields")
    @RestReturn(value=GeneralFormFieldDTO.class)
    public RestResponse listDefaultFields(ListDefaultFieldsCommand cmd){
        List<GeneralFormFieldDTO> dtos = generalFormService.getDefaultFieldsByModuleId(cmd);
        RestResponse response = new RestResponse(dtos);
        response.setErrorDescription("OK");
        return response;
    }


	/**
	 * <b>URL: /general_form/syncFromDb</b>
	 * <p>刷新es</p>
	 */
	@RequestMapping("syncFromDb")
	@RestReturn(value=String.class)
	public RestResponse syncFromDb(){
		generalFormSearcher.syncFromDb();
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /general_form/deleteGeneralFormVal</b>
	 * <p> 删除表单值 </p>
	 */
	@RequestMapping("deleteGeneralFormVal")
	@RestReturn(value=Long.class)
	public RestResponse deleteGeneralFormVal(PostGeneralFormValCommand cmd) {
		Long id = generalFormService.deleteGeneralFormValWithPrivi(cmd);
		RestResponse response = new RestResponse(id);
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
		List<GeneralFormValDTO> dto = generalFormService.getGeneralFormValWithPrivi(cmd);
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /general_form/saveGeneralFormVal</b>
	 * <p> 只是保存,区别于 postGeneralForm</p>
	 */
	@RequestMapping("saveGeneralFormVal")
	@RestReturn(value=Long.class)
	public RestResponse saveGeneralFormVal(PostGeneralFormValCommand cmd) {
		Long sourceId = generalFormService.saveGeneralFormVal(cmd);
		RestResponse response = new RestResponse(sourceId);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


	/**
	 * <b>URL: /general_form/saveGeneralFormFilter</b>
	 * <p> 用于保存筛选字段，并删除之前的筛选字段记录</p>
	 * @param cmd
	 * @return
	 */
	@RequestMapping("saveGeneralFormFilter")
	@RestReturn(value=String.class, collection = true)
	public RestResponse saveGeneralFormFilter(PostGeneralFormFilterCommand cmd) {
		List<String> fieldNames = generalFormService.saveGeneralFormFilter(cmd);
		RestResponse response = new RestResponse(fieldNames);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /general_form/listGeneralFormFilter</b>
	 * <p> 用于获取筛选字段</p>
	 * @param cmd
	 * @return
	 */
	@RequestMapping("listGeneralFormFilter")
	@RestReturn(value=String.class, collection = true)
	public RestResponse listGeneralFormFilter(GetGeneralFormFilterCommand cmd) {
		List<String> fieldNames = generalFormService.listGeneralFormFilter(cmd);
		RestResponse response = new RestResponse(fieldNames);
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
    
    
    /**
	 * <b>URL: /general_form/syncFromDbV2</b>
	 * <p>同步数据库数据到es(同步接口版本V2)</p>
	 */
	@RequestMapping("syncFromDbV2")
	@RestReturn(value=String.class)
	public RestResponse syncFromDbV2(){
		generalFormSearcher.syncFromDbV2();
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

}