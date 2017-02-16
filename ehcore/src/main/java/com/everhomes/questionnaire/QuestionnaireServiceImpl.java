// @formatter:off
package com.everhomes.questionnaire;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
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
import com.everhomes.rest.questionnaire.QuestionnaireDTO;
import com.everhomes.rest.questionnaire.QuestionnaireQuestionDTO;
import com.everhomes.rest.questionnaire.QuestionnaireServiceErrorCode;
import com.everhomes.util.RuntimeErrorException;

@Component
public class QuestionnaireServiceImpl implements QuestionnaireService {
	
	@Autowired
	private QuestionnaireProvider questionnaireProvider;
	
	@Autowired
	private QuestionnaireQuestionProvider questionnaireQuestionProvider;
	
	@Autowired
	private QuestionnaireOptionProvider questionnaireOptionProvider;
	
	@Autowired
	private QuestionnaireAnswerProvider questionnaireAnswerProvider;
	

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
		QuestionnaireDTO questionnaire = cmd.getQuestionnaire();
		checkQuestionnaireParameters(questionnaire);
		
		
		
		
		return new CreateQuestionnaireResponse();
	}

	private void checkQuestionnaireParameters(QuestionnaireDTO questionnaire) {
		if (questionnaire.getNamespaceId() == null || StringUtils.isBlank(questionnaire.getOwnerType())
				|| questionnaire.getOwnerId() == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters, namespaceId=" + questionnaire.getNamespaceId() + ", ownerType=" + questionnaire.getOwnerType() + ", ownerId=" + questionnaire.getOwnerId());
		}
		if (StringUtils.isBlank(questionnaire.getQuestionnaireName())) {
			throw RuntimeErrorException.errorWith(QuestionnaireServiceErrorCode.SCOPE, QuestionnaireServiceErrorCode.QUESTIONNAIRE_NAME_EMPTY,
					"Invalid parameters");
		}
		if (questionnaire.getQuestionnaireName().length() > 50) {
			throw RuntimeErrorException.errorWith(QuestionnaireServiceErrorCode.SCOPE, QuestionnaireServiceErrorCode.QUESTIONNAIRE_NAME_LENGTH_BEYOND_50,
					"Invalid parameters");
		}
		if (questionnaire.getQuestions() == null || questionnaire.getQuestions().size() == 0) {
			throw RuntimeErrorException.errorWith(QuestionnaireServiceErrorCode.SCOPE, QuestionnaireServiceErrorCode.NO_QUESTIONS,
					"Invalid parameters");
		}
		if () {
			
		}
		
		for (QuestionnaireQuestionDTO question: questionnaire.getQuestions()) {
			if (StringUtils.isBlank(question.getQuestionName())) {
				throw RuntimeErrorException.errorWith(QuestionnaireServiceErrorCode.SCOPE, QuestionnaireServiceErrorCode.NO_QUESTIONS,
						"Invalid parameters");
			}
			
		}
		
		
//		private Long id;
//		private Integer namespaceId;
//		private String ownerType;
//		private Long ownerId;
//		private String questionnaireName;
//		private String description;
//		private Integer collectionCount;
//		private Byte status;
//		private Timestamp publishTime;
//		private Timestamp createTime;
//		private Timestamp submitTime;
		
		
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