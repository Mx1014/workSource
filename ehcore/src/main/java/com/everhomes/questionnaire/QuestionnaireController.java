// @formatter:off
package com.everhomes.questionnaire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.questionnaire.CreateQuestionnaireCommand;
import com.everhomes.rest.questionnaire.CreateQuestionnaireResponse;
import com.everhomes.rest.questionnaire.CreateTargetQuestionnaireCommand;
import com.everhomes.rest.questionnaire.CreateTargetQuestionnaireResponse;
import com.everhomes.rest.questionnaire.DeleteQuestionnaireCommand;
import com.everhomes.rest.questionnaire.GetQuestionnaireDetailCommand;
import com.everhomes.rest.questionnaire.GetQuestionnaireDetailResponse;
import com.everhomes.rest.questionnaire.GetQuestionnaireResultDetailCommand;
import com.everhomes.rest.questionnaire.GetQuestionnaireResultDetailResponse;
import com.everhomes.rest.questionnaire.GetQuestionnaireResultSummaryCommand;
import com.everhomes.rest.questionnaire.GetQuestionnaireResultSummaryResponse;
import com.everhomes.rest.questionnaire.GetTargetQuestionnaireDetailCommand;
import com.everhomes.rest.questionnaire.GetTargetQuestionnaireDetailResponse;
import com.everhomes.rest.questionnaire.ListBlankQuestionAnswersCommand;
import com.everhomes.rest.questionnaire.ListBlankQuestionAnswersResponse;
import com.everhomes.rest.questionnaire.ListOptionOrganizationsCommand;
import com.everhomes.rest.questionnaire.ListOptionOrganizationsResponse;
import com.everhomes.rest.questionnaire.ListQuestionnairesCommand;
import com.everhomes.rest.questionnaire.ListQuestionnairesResponse;
import com.everhomes.rest.questionnaire.ListTargetQuestionnairesCommand;
import com.everhomes.rest.questionnaire.ListTargetQuestionnairesResponse;

@RestController
@RequestMapping("/questionnaire")
public class QuestionnaireController extends ControllerBase {
	
	@Autowired
	private QuestionnaireService questionnaireService;
	
	/**
	 * <p>1.问卷调查列表-园区</p>
	 * <b>URL: /questionnaire/listQuestionnaires</b>
	 */
	@RequestMapping("listQuestionnaires")
	@RestReturn(ListQuestionnairesResponse.class) 
	public RestResponse listQuestionnaires(ListQuestionnairesCommand cmd){
		return new RestResponse(questionnaireService.listQuestionnaires(cmd));
	}

	/**
	 * <p>2.问卷调查详情-园区</p>
	 * <b>URL: /questionnaire/getQuestionnaireDetail</b>
	 */
	@RequestMapping("getQuestionnaireDetail")
	@RestReturn(GetQuestionnaireDetailResponse.class)
	public RestResponse getQuestionnaireDetail(GetQuestionnaireDetailCommand cmd){
		return new RestResponse(questionnaireService.getQuestionnaireDetail(cmd));
	}

	/**
	 * <p>3.创建问卷调查-园区</p>
	 * <b>URL: /questionnaire/createQuestionnaire</b>
	 */
	@RequestMapping("createQuestionnaire")
	@RestReturn(CreateQuestionnaireResponse.class)
	public RestResponse createQuestionnaire(CreateQuestionnaireCommand cmd){
		return new RestResponse(questionnaireService.createQuestionnaire(cmd));
	}

	/**
	 * <p>4.删除问卷调查-园区</p>
	 * <b>URL: /questionnaire/deleteQuestionnaire</b>
	 */
	@RequestMapping("deleteQuestionnaire")
	@RestReturn(String.class)
	public RestResponse deleteQuestionnaire(DeleteQuestionnaireCommand cmd){
		questionnaireService.deleteQuestionnaire(cmd);
		return new RestResponse();
	}

	/**
	 * <p>5.问卷调查结果详情-园区</p>
	 * <b>URL: /questionnaire/getQuestionnaireResultDetail</b>
	 */
	@RequestMapping("getQuestionnaireResultDetail")
	@RestReturn(GetQuestionnaireResultDetailResponse.class)
	public RestResponse getQuestionnaireResultDetail(GetQuestionnaireResultDetailCommand cmd){
		return new RestResponse(questionnaireService.getQuestionnaireResultDetail(cmd));
	}

	/**
	 * <p>6.问卷调查结果统计-园区</p>
	 * <b>URL: /questionnaire/getQuestionnaireResultSummary</b>
	 */
	@RequestMapping("getQuestionnaireResultSummary")
	@RestReturn(GetQuestionnaireResultSummaryResponse.class)
	public RestResponse getQuestionnaireResultSummary(GetQuestionnaireResultSummaryCommand cmd){
		return new RestResponse(questionnaireService.getQuestionnaireResultSummary(cmd));
	}

	/**
	 * <p>7.某个选项的企业列表-园区</p>
	 * <b>URL: /questionnaire/listOptionOrganizations</b>
	 */
	@RequestMapping("listOptionOrganizations")
	@RestReturn(ListOptionOrganizationsResponse.class)
	public RestResponse listOptionOrganizations(ListOptionOrganizationsCommand cmd){
		return new RestResponse(questionnaireService.listOptionOrganizations(cmd));
	}

	/**
	 * <p>8.填空题答案列表-园区</p>
	 * <b>URL: /questionnaire/listBlankQuestionAnswers</b>
	 */
	@RequestMapping("listBlankQuestionAnswers")
	@RestReturn(ListBlankQuestionAnswersResponse.class)
	public RestResponse listBlankQuestionAnswers(ListBlankQuestionAnswersCommand cmd){
		return new RestResponse(questionnaireService.listBlankQuestionAnswers(cmd));
	}

	/**
	 * <p>9.问卷调查列表-企业</p>
	 * <b>URL: /questionnaire/listTargetQuestionnaires</b>
	 */
	@RequestMapping("listTargetQuestionnaires")
	@RestReturn(ListTargetQuestionnairesResponse.class)
	public RestResponse listTargetQuestionnaires(ListTargetQuestionnairesCommand cmd){
		return new RestResponse(questionnaireService.listTargetQuestionnaires(cmd));
	}

	/**
	 * <p>10.问卷调查详情-企业</p>
	 * <b>URL: /questionnaire/getTargetQuestionnaireDetail</b>
	 */
	@RequestMapping("getTargetQuestionnaireDetail")
	@RestReturn(GetTargetQuestionnaireDetailResponse.class)
	public RestResponse getTargetQuestionnaireDetail(GetTargetQuestionnaireDetailCommand cmd){
		return new RestResponse(questionnaireService.getTargetQuestionnaireDetail(cmd));
	}

	/**
	 * <p>11.提交问卷调查-企业</p>
	 * <b>URL: /questionnaire/createTargetQuestionnaire</b>
	 */
	@RequestMapping("createTargetQuestionnaire")
	@RestReturn(CreateTargetQuestionnaireResponse.class)
	public RestResponse createTargetQuestionnaire(CreateTargetQuestionnaireCommand cmd){
		return new RestResponse(questionnaireService.createTargetQuestionnaire(cmd));
	}

}