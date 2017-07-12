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
import com.everhomes.rest.questionnaire.ListOptionTargetsCommand;
import com.everhomes.rest.questionnaire.ListOptionTargetsResponse;
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
     * <b>URL: /questionnaire/listQuestionnaires</b>
	 * <p>1.问卷调查列表-园区</p>
	 */
	@RequestMapping("listQuestionnaires")
	@RestReturn(ListQuestionnairesResponse.class) 
	public RestResponse listQuestionnaires(ListQuestionnairesCommand cmd){
		return new RestResponse(questionnaireService.listQuestionnaires(cmd));
	}

	/**
     * <b>URL: /questionnaire/getQuestionnaireDetail</b>
	 * <p>2.问卷调查详情-园区</p>
	 */
	@RequestMapping("getQuestionnaireDetail")
	@RestReturn(GetQuestionnaireDetailResponse.class)
	public RestResponse getQuestionnaireDetail(GetQuestionnaireDetailCommand cmd){
		return new RestResponse(questionnaireService.getQuestionnaireDetail(cmd));
	}

	/**
     * <b>URL: /questionnaire/createQuestionnaire</b>
	 * <p>3.创建问卷调查-园区</p>
	 */
	@RequestMapping("createQuestionnaire")
	@RestReturn(CreateQuestionnaireResponse.class)
	public RestResponse createQuestionnaire(CreateQuestionnaireCommand cmd){
		return new RestResponse(questionnaireService.createQuestionnaire(cmd));
	}

	/**
     * <b>URL: /questionnaire/deleteQuestionnaire</b>
	 * <p>4.删除问卷调查-园区</p>
	 */
	@RequestMapping("deleteQuestionnaire")
	@RestReturn(String.class)
	public RestResponse deleteQuestionnaire(DeleteQuestionnaireCommand cmd){
		questionnaireService.deleteQuestionnaire(cmd);
		return new RestResponse();
	}

	/**
     * <b>URL: /questionnaire/getQuestionnaireResultDetail</b>
	 * <p>5.问卷调查结果详情-园区</p>
	 */
	@RequestMapping("getQuestionnaireResultDetail")
	@RestReturn(GetQuestionnaireResultDetailResponse.class)
	public RestResponse getQuestionnaireResultDetail(GetQuestionnaireResultDetailCommand cmd){
		return new RestResponse(questionnaireService.getQuestionnaireResultDetail(cmd));
	}

	/**
     * <b>URL: /questionnaire/getQuestionnaireResultSummary</b>
	 * <p>6.问卷调查结果统计-园区</p>
	 */
	@RequestMapping("getQuestionnaireResultSummary")
	@RestReturn(GetQuestionnaireResultSummaryResponse.class)
	public RestResponse getQuestionnaireResultSummary(GetQuestionnaireResultSummaryCommand cmd){
		return new RestResponse(questionnaireService.getQuestionnaireResultSummary(cmd));
	}

	/**
     * <b>URL: /questionnaire/listOptionTargets</b>
	 * <p>7.某个选项的企业列表-园区</p>
	 */
	@RequestMapping("listOptionTargets")
	@RestReturn(ListOptionTargetsResponse.class)
	public RestResponse listOptionTargets(ListOptionTargetsCommand cmd){
		return new RestResponse(questionnaireService.listOptionTargets(cmd));
	}

	/**
     * <b>URL: /questionnaire/listBlankQuestionAnswers</b>
	 * <p>8.填空题答案列表-园区</p>
	 */
	@RequestMapping("listBlankQuestionAnswers")
	@RestReturn(ListBlankQuestionAnswersResponse.class)
	public RestResponse listBlankQuestionAnswers(ListBlankQuestionAnswersCommand cmd){
		return new RestResponse(questionnaireService.listBlankQuestionAnswers(cmd));
	}

	/**
     * <b>URL: /questionnaire/listTargetQuestionnaires</b>
	 * <p>9.问卷调查列表-企业</p>
	 */
	@RequestMapping("listTargetQuestionnaires")
	@RestReturn(ListTargetQuestionnairesResponse.class)
	public RestResponse listTargetQuestionnaires(ListTargetQuestionnairesCommand cmd){
		return new RestResponse(questionnaireService.listTargetQuestionnaires(cmd));
	}

	/**
     * <b>URL: /questionnaire/getTargetQuestionnaireDetail</b>
	 * <p>10.问卷调查详情-企业</p>
	 */
	@RequestMapping("getTargetQuestionnaireDetail")
	@RestReturn(GetTargetQuestionnaireDetailResponse.class)
	public RestResponse getTargetQuestionnaireDetail(GetTargetQuestionnaireDetailCommand cmd){
		return new RestResponse(questionnaireService.getTargetQuestionnaireDetail(cmd));
	}

	/**
     * <b>URL: /questionnaire/createTargetQuestionnaire</b>
	 * <p>11.提交问卷调查-企业</p>
	 */
	@RequestMapping("createTargetQuestionnaire")
	@RestReturn(CreateTargetQuestionnaireResponse.class)
	public RestResponse createTargetQuestionnaire(CreateTargetQuestionnaireCommand cmd){
		return new RestResponse(questionnaireService.createTargetQuestionnaire(cmd));
	}

}