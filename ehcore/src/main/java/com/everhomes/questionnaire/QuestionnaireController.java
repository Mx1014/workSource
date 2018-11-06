// @formatter:off
package com.everhomes.questionnaire;

import com.everhomes.rest.organization.OrganizationAndDetailDTO;
import com.everhomes.rest.questionnaire.*;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

import javax.servlet.http.HttpServletResponse;

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
	 * <b>URL: /questionnaire/exportQuestionnaireResultDetail</b>
	 * <p>5.1.问卷调查结果详情导出-园区</p>
	 */
	@RequestMapping("exportQuestionnaireResultDetail")
	@RestReturn(String.class)
	public RestResponse exportQuestionnaireResultDetail(GetQuestionnaireResultDetailCommand cmd, HttpServletResponse response){
		questionnaireService.exportQuestionnaireResultDetail(cmd,response);
		return new RestResponse();
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
	 * <b>URL: /questionnaire/getAnsweredQuestionnaireDetail</b>
	 * <p>9.问卷调查详情-后台调用</p>
	 */
	@RequestMapping("getAnsweredQuestionnaireDetail")
	@RestReturn(GetTargetQuestionnaireDetailResponse.class)
	public RestResponse getAnsweredQuestionnaireDetail(GetTargetQuestionnaireDetailCommand cmd){
		return new RestResponse(questionnaireService.getAnsweredQuestionnaireDetail(cmd));
	}

	/**
     * <b>URL: /questionnaire/listTargetQuestionnaires</b>
	 * <p>10.问卷调查列表-企业和个人</p>
	 */
	@RequestMapping("listTargetQuestionnaires")
	@RestReturn(ListTargetQuestionnairesResponse.class)
	public RestResponse listTargetQuestionnaires(ListTargetQuestionnairesCommand cmd){
		return new RestResponse(questionnaireService.listTargetQuestionnaires(cmd));
	}

	/**
     * <b>URL: /questionnaire/getTargetQuestionnaireDetail</b>
	 * <p>11.问卷调查详情-企业和个人</p>
	 */
	@RequestMapping("getTargetQuestionnaireDetail")
	@RestReturn(GetTargetQuestionnaireDetailResponse.class)
	public RestResponse getTargetQuestionnaireDetail(GetTargetQuestionnaireDetailCommand cmd){
		return new RestResponse(questionnaireService.getTargetQuestionnaireDetail(cmd));
	}

	/**
     * <b>URL: /questionnaire/createTargetQuestionnaire</b>
	 * <p>12.提交问卷调查-企业和个人</p>
	 */
	@RequestMapping("createTargetQuestionnaire")
	@RestReturn(CreateTargetQuestionnaireResponse.class)
	public RestResponse createTargetQuestionnaire(CreateTargetQuestionnaireCommand cmd){
		return new RestResponse(questionnaireService.createTargetQuestionnaire(cmd));
	}

	/**
	 * <b>URL: /questionnaire/listUsersbyIdentifiers</b>
	 * <p>13.通过手机号查询用户信息</p>
	 */
	@RequestMapping("listUsersbyIdentifiers")
	@RestReturn(ListUsersbyIdentifiersResponse.class)
	public RestResponse listUsersbyIdentifiers(ListUsersbyIdentifiersCommand cmd){
		return new RestResponse(questionnaireService.listUsersbyIdentifiers(cmd));
	}

	/**
	 * <b>URL: /questionnaire/reScopeQuesionnaireRanges</b>
	 * <p>14.重新计算问卷的范围</p>
	 */
	@RequestMapping("reScopeQuesionnaireRanges")
	@RestReturn(String.class)
	@RequireAuthentication(false)
	public RestResponse reScopeQuesionnaireRanges(ReScopeQuesionnaireRangesCommand cmd){
		questionnaireService.reScopeQuesionnaireRanges(cmd);
		return new RestResponse();
	}

	/**
	 * <b>URL: /questionnaire/reSendQuesionnaireMessages</b>
	 * <p>15.给未回答问卷，且在一天之内到期的用户发送消息</p>
	 */
	@RequestMapping("reSendQuesionnaireMessages")
	@RestReturn(String.class)
	@RequireAuthentication(false)
	public RestResponse reSendQuesionnaireMessages(){
		questionnaireService.reSendQuesionnaireMessages();
		return new RestResponse();
	}

	/**
	 * <b>URL: /questionnaire/listRangeOrgs</b>
	 * <p>查询问卷范围企业新信息</p>
	 */
	@RequestMapping("listRangeOrg")
	@RestReturn(value = OrganizationAndDetailDTO.class,collection = true)
	public RestResponse listRangeOrgs(ListRangeOrgsCommand cmd){
		return new RestResponse(questionnaireService.listRangeOrgs(cmd));
	}

}