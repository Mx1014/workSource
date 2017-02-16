// @formatter:off
package com.everhomes.questionnaire;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
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
import com.everhomes.rest.questionnaire.QuestionType;
import com.everhomes.rest.questionnaire.QuestionnaireDTO;
import com.everhomes.rest.questionnaire.QuestionnaireOptionDTO;
import com.everhomes.rest.questionnaire.QuestionnaireOwnerType;
import com.everhomes.rest.questionnaire.QuestionnaireQuestionDTO;
import com.everhomes.rest.questionnaire.QuestionnaireServiceErrorCode;
import com.everhomes.rest.questionnaire.QuestionnaireStatus;
import com.everhomes.util.ConvertHelper;
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
	
	@Autowired
	private CommunityProvider communityProvider;
	
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private CoordinationProvider coordinationProvider;
	

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
		QuestionnaireDTO questionnaireDTO = cmd.getQuestionnaire();
		checkQuestionnaireParameters(questionnaireDTO);
		
		
		QuestionnaireDTO result = (QuestionnaireDTO)dbProvider.execute(s->{
			//如果是重新编辑问卷，则把之前的题目和选项删除
			Questionnaire questionnaire;
			if (questionnaireDTO.getId() != null) {
				questionnaire = (Questionnaire)coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_QUESTIONNAIRE.getCode() + questionnaireDTO.getId()).enter(()->{
					Questionnaire q = findQuestionnaireForUpdate(questionnaireDTO);
					updateQuestionnaire(q, questionnaireDTO);
					return q;
				}).first();
				
				questionnaireQuestionProvider.deleteQuestionsByQuestionnaireId(questionnaireDTO.getId());
				questionnaireOptionProvider.deleteOptionsByQuestionnaireId(questionnaireDTO.getId());
			} else {
				questionnaire = ConvertHelper.convert(questionnaireDTO, Questionnaire.class);
				questionnaireProvider.createQuestionnaire(questionnaire);
			}
			
			List<QuestionnaireQuestionDTO> questionDTOs = createQuestions(questionnaireDTO.getQuestions());
			
			return convertToQuestionnaireDTO(questionnaire, questionDTOs);
		});
		
		return new CreateQuestionnaireResponse(result);
	}

	private List<QuestionnaireQuestionDTO> createQuestions(List<QuestionnaireQuestionDTO> questions) {
		return null;
	}

	private QuestionnaireDTO convertToQuestionnaireDTO(Questionnaire questionnaire, List<QuestionnaireQuestionDTO> questionDTOs){
		QuestionnaireDTO questionnaireDTO = ConvertHelper.convert(questionnaire, QuestionnaireDTO.class);
		questionnaireDTO.setQuestions(questionDTOs);
		return questionnaireDTO;
	}
	
	// 只有草稿状态的问卷才可以更新
	private Questionnaire findQuestionnaireForUpdate(QuestionnaireDTO questionnaireDTO) {
		Questionnaire questionnaire = findQuestionnaire(questionnaireDTO);
		if (QuestionnaireStatus.fromCode(questionnaire.getStatus()) != QuestionnaireStatus.DRAFT) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"status error, status=" + questionnaire.getStatus());
		}
		return questionnaire;
	}
	
	private Questionnaire findQuestionnaire(QuestionnaireDTO questionnaireDTO) {
		Questionnaire questionnaire = findQuestionnaireById(questionnaireDTO.getId());
		if (questionnaire.getNamespaceId().intValue() != questionnaireDTO.getNamespaceId().intValue() ||
				!questionnaire.getOwnerType().equals(questionnaireDTO.getOwnerType()) ||
				questionnaire.getOwnerId().longValue() != questionnaireDTO.getOwnerId().longValue()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"there is no such questionnaire in this context, questionnaireId=" + questionnaireDTO.getId());
		}
		return questionnaire;
	}

	private Questionnaire findQuestionnaireById(Long id) {
		Questionnaire questionnaire = questionnaireProvider.findQuestionnaireById(id);
		if (questionnaire == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"there is no such questionnaire, questionnaireId=" + id);
		}
		return questionnaire;
	}

	private void updateQuestionnaire(Questionnaire questionnaire, QuestionnaireDTO questionnaireDTO) {
		questionnaire.setQuestionnaireName(questionnaireDTO.getQuestionnaireName());
		questionnaire.setDescription(questionnaireDTO.getDescription());
		questionnaire.setStatus(questionnaireDTO.getStatus());
		questionnaire.setPublishTime(questionnaireDTO.getPublishTime());
		questionnaireProvider.updateQuestionnaire(questionnaire);
	}

	private void checkOwner(Integer namespaceId, String ownerType, Long ownerId){
		QuestionnaireOwnerType questionnaireOwnerType;
		if (namespaceId == null || (questionnaireOwnerType=QuestionnaireOwnerType.fromCode(ownerType)) == null
				|| ownerId == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters, namespaceId=" + namespaceId + ", ownerType=" + ownerType + ", ownerId=" + ownerId);
		}
		switch (questionnaireOwnerType) {
		case COMMUNITY:
			Community community = communityProvider.findCommunityById(ownerId);
			if (community == null) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"there is no such community, ownerId="+ownerId);
			}
			break;

		default:
			break;
		}
	}
	
	private void checkQuestionnaireParameters(QuestionnaireDTO questionnaire) {
		checkOwner(questionnaire.getNamespaceId(), questionnaire.getOwnerType(), questionnaire.getOwnerId());
		if (StringUtils.isBlank(questionnaire.getQuestionnaireName())) {
			throw RuntimeErrorException.errorWith(QuestionnaireServiceErrorCode.SCOPE, QuestionnaireServiceErrorCode.QUESTIONNAIRE_NAME_EMPTY,
					"Invalid parameters, questionnaire name cannot be null");
		}
		if (questionnaire.getQuestionnaireName().length() > 50) {
			throw RuntimeErrorException.errorWith(QuestionnaireServiceErrorCode.SCOPE, QuestionnaireServiceErrorCode.QUESTIONNAIRE_NAME_LENGTH_BEYOND_50,
					"Invalid parameters, questionnaire name length cannot be beyond 50");
		}
		if (questionnaire.getQuestions() == null || questionnaire.getQuestions().size() == 0) {
			throw RuntimeErrorException.errorWith(QuestionnaireServiceErrorCode.SCOPE, QuestionnaireServiceErrorCode.NO_QUESTIONS,
					"Invalid parameters, there are no questions");
		}
		QuestionnaireStatus status;
		if ((status=QuestionnaireStatus.fromCode(questionnaire.getStatus())) == null || status == QuestionnaireStatus.INACTIVE) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters,status="+questionnaire.getStatus());
		}
		
		for (QuestionnaireQuestionDTO question: questionnaire.getQuestions()) {
			if (StringUtils.isBlank(question.getQuestionName())) {
				throw RuntimeErrorException.errorWith(QuestionnaireServiceErrorCode.SCOPE, QuestionnaireServiceErrorCode.QUESTION_NAME_EMPTY,
						"Invalid parameters, question name cannot be null");
			}
			QuestionType questionType;
			if ((questionType=QuestionType.fromCode(question.getQuestionType()))==null) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"Invalid parameters, question type error, question type="+question.getQuestionType());
			}
			if (questionType != QuestionType.BLANK) {
				if ((question.getOptions() == null || question.getOptions().size() == 0)) {
					throw RuntimeErrorException.errorWith(QuestionnaireServiceErrorCode.SCOPE, QuestionnaireServiceErrorCode.NO_OPTIONS,
							"Invalid parameters, there are no options");
				}
				for (QuestionnaireOptionDTO option : question.getOptions()) {
					if (StringUtils.isBlank(option.getOptionName())) {
						throw RuntimeErrorException.errorWith(QuestionnaireServiceErrorCode.SCOPE, QuestionnaireServiceErrorCode.OPTION_NAME_EMPTY,
								"Invalid parameters, option name cannot be null");
					}
				}
			}
		}
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