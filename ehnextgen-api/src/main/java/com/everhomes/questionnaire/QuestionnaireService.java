// @formatter:off
package com.everhomes.questionnaire;

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

public interface QuestionnaireService {


	public ListQuestionnairesResponse listQuestionnaires(ListQuestionnairesCommand cmd);


	public GetQuestionnaireDetailResponse getQuestionnaireDetail(GetQuestionnaireDetailCommand cmd);


	public CreateQuestionnaireResponse createQuestionnaire(CreateQuestionnaireCommand cmd);


	public void deleteQuestionnaire(DeleteQuestionnaireCommand cmd);


	public GetQuestionnaireResultDetailResponse getQuestionnaireResultDetail(GetQuestionnaireResultDetailCommand cmd);


	public GetQuestionnaireResultSummaryResponse getQuestionnaireResultSummary(GetQuestionnaireResultSummaryCommand cmd);


	public ListOptionTargetsResponse listOptionTargets(ListOptionTargetsCommand cmd);


	public ListBlankQuestionAnswersResponse listBlankQuestionAnswers(ListBlankQuestionAnswersCommand cmd);


	public ListTargetQuestionnairesResponse listTargetQuestionnaires(ListTargetQuestionnairesCommand cmd);


	public GetTargetQuestionnaireDetailResponse getTargetQuestionnaireDetail(GetTargetQuestionnaireDetailCommand cmd);


	public CreateTargetQuestionnaireResponse createTargetQuestionnaire(CreateTargetQuestionnaireCommand cmd);

}