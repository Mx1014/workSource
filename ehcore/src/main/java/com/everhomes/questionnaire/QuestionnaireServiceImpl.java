// @formatter:off
package com.everhomes.questionnaire;

import org.springframework.stereotype.Component;

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

@Component
public class QuestionnaireServiceImpl implements QuestionnaireService {
	
	

	@Override
	public ListQuestionnairesResponse listQuestionnaires(ListQuestionnairesCommand cmd) {
	
		return new ListQuestionnairesResponse();
	}

	@Override
	public GetQuestionnaireDetailResponse getQuestionnaireDetail(GetQuestionnaireDetailCommand cmd) {
	
		return new GetQuestionnaireDetailResponse();
	}

	@Override
	public CreateQuestionnaireResponse createQuestionnaire(CreateQuestionnaireCommand cmd) {
	
		return new CreateQuestionnaireResponse();
	}

	@Override
	public void deleteQuestionnaire(DeleteQuestionnaireCommand cmd) {
	

	}

	@Override
	public GetQuestionnaireResultDetailResponse getQuestionnaireResultDetail(GetQuestionnaireResultDetailCommand cmd) {
	
		return new GetQuestionnaireResultDetailResponse();
	}

	@Override
	public GetQuestionnaireResultSummaryResponse getQuestionnaireResultSummary(GetQuestionnaireResultSummaryCommand cmd) {
	
		return new GetQuestionnaireResultSummaryResponse();
	}

	@Override
	public ListOptionOrganizationsResponse listOptionOrganizations(ListOptionOrganizationsCommand cmd) {
	
		return new ListOptionOrganizationsResponse();
	}

	@Override
	public ListBlankQuestionAnswersResponse listBlankQuestionAnswers(ListBlankQuestionAnswersCommand cmd) {
	
		return new ListBlankQuestionAnswersResponse();
	}

	@Override
	public ListTargetQuestionnairesResponse listTargetQuestionnaires(ListTargetQuestionnairesCommand cmd) {
	
		return new ListTargetQuestionnairesResponse();
	}

	@Override
	public GetTargetQuestionnaireDetailResponse getTargetQuestionnaireDetail(GetTargetQuestionnaireDetailCommand cmd) {
	
		return new GetTargetQuestionnaireDetailResponse();
	}

	@Override
	public CreateTargetQuestionnaireResponse createTargetQuestionnaire(CreateTargetQuestionnaireCommand cmd) {
	
		return new CreateTargetQuestionnaireResponse();
	}

}