// @formatter:off
package com.everhomes.questionnaire;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.approval.TrueOrFalseFlag;
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
import com.everhomes.rest.questionnaire.QuestionType;
import com.everhomes.rest.questionnaire.QuestionnaireDTO;
import com.everhomes.rest.questionnaire.QuestionnaireOptionDTO;
import com.everhomes.rest.questionnaire.QuestionnaireOwnerType;
import com.everhomes.rest.questionnaire.QuestionnaireQuestionDTO;
import com.everhomes.rest.questionnaire.QuestionnaireResultTargetDTO;
import com.everhomes.rest.questionnaire.QuestionnaireServiceErrorCode;
import com.everhomes.rest.questionnaire.QuestionnaireStatus;
import com.everhomes.rest.questionnaire.QuestionnaireTargetType;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;

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
	
	@Autowired
	private ContentServerService contentServerService;
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;
	

	@Override
	public ListQuestionnairesResponse listQuestionnaires(ListQuestionnairesCommand cmd) {
		checkOwner(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		List<Questionnaire> questionnaires = questionnaireProvider.listQuestionnaireByOwner(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getPageAnchor(), pageSize+1);
		Long nextPageAnchor = null;
		if (questionnaires.size() > pageSize) {
			questionnaires.remove(questionnaires.size()-1);
			nextPageAnchor = questionnaires.get(questionnaires.size()-1).getId();
		}
		return new ListQuestionnairesResponse(nextPageAnchor, questionnaires.stream().map(q->convertToQuestionnaireDTO(q)).collect(Collectors.toList()));
	}

	@Override
	public GetQuestionnaireDetailResponse getQuestionnaireDetail(GetQuestionnaireDetailCommand cmd) {
		List<QuestionnaireOption> questionnaireOptions = questionnaireOptionProvider.listOptionsByQuestionnaireId(cmd.getQuestionnaireId());
		return new GetQuestionnaireDetailResponse(convertToQuestionnaireDTO(questionnaireOptions));
	}

	private QuestionnaireDTO convertToQuestionnaireDTO(List<QuestionnaireOption> questionnaireOptions) {
		return convertToQuestionnaireDTO(questionnaireOptions, false, null);
	}
	
	// 是否包含填空题，统计时用
	private QuestionnaireDTO convertToQuestionnaireDTO(List<QuestionnaireOption> questionnaireOptions, boolean containBlank, Integer pageSize) {
		List<QuestionnaireDTO> questionnaireDTOs = new ArrayList<>();
		// k1为问卷的id，k2为问题的id
		Map<Long, Map<Long, List<QuestionnaireOption>>> map = questionnaireOptions.parallelStream().collect(Collectors.groupingBy(QuestionnaireOption::getQuestionnaireId,Collectors.groupingBy(QuestionnaireOption::getQuestionId)));
		map.forEach((k1, v1)->{
			Questionnaire questionnaire = findQuestionnaireById(k1);
			List<QuestionnaireQuestionDTO> questionDTOs = new ArrayList<>();
			v1.forEach((k2, v2)->{
				QuestionnaireQuestion question = questionnaireQuestionProvider.findQuestionnaireQuestionById(k2);
				List<QuestionnaireOptionDTO> optionDTOs = null;
				Long nextPageAnchor = null;
				if (QuestionType.fromCode(question.getQuestionType()) == QuestionType.BLANK && containBlank) {
					// 如果是填空题，统计时补上答案，并分页显示
					Tuple<Long, List<QuestionnaireAnswer>> questionnaireAnswerTuple = findQuestionnaireAnswersByQuestionId(question.getId(), null, pageSize);
					nextPageAnchor = questionnaireAnswerTuple.first();
					optionDTOs = questionnaireAnswerTuple.second().stream().map(q->convertToOptionDTO(q)).collect(Collectors.toList());
				}else {
					optionDTOs = v2.stream().map(o->convertToOptionDTO(o)).collect(Collectors.toList());
				}
				questionDTOs.add(convertToQuestionDTO(question, optionDTOs, nextPageAnchor));
			});
			// 经过map处理后顺序会乱，所以要重新排序下
			sortQuestions(questionDTOs);
			questionnaireDTOs.add(convertToQuestionnaireDTO(questionnaire, questionDTOs));
		});
		return questionnaireDTOs.get(0);
	}
	
	private QuestionnaireOptionDTO convertToOptionDTO(QuestionnaireAnswer answer) {
		QuestionnaireOptionDTO optionDTO = new QuestionnaireOptionDTO();
		optionDTO.setOptionName(answer.getTargetName());
		optionDTO.setOptionContent(answer.getOptionContent());
		return optionDTO;
	}
	
	private Tuple<Long, List<QuestionnaireAnswer>> findQuestionnaireAnswersByOptionId(Long optionId, Long pageAnchor, Integer pageSize) {
		pageSize = pageSize==null?10:pageSize;
		List<QuestionnaireAnswer> questionnaireAnswers = questionnaireAnswerProvider.listQuestionnaireAnswerByOptionId(optionId, pageAnchor, pageSize+1);
		Long nextPageAnchor = null;
		if (questionnaireAnswers.size() > pageSize) {
			questionnaireAnswers.remove(questionnaireAnswers.size()-1);
			nextPageAnchor = questionnaireAnswers.get(questionnaireAnswers.size()-1).getId();
		}
		return new Tuple<>(nextPageAnchor, questionnaireAnswers);
	}
	
	private Tuple<Long, List<QuestionnaireAnswer>> findQuestionnaireAnswersByQuestionId(Long questionId, Long pageAnchor, Integer pageSize) {
		pageSize = pageSize==null?10:pageSize;
		List<QuestionnaireAnswer> questionnaireAnswers = questionnaireAnswerProvider.listQuestionnaireAnswerByQuestionId(questionId, pageAnchor, pageSize+1);
		Long nextPageAnchor = null;
		if (questionnaireAnswers.size() > pageSize) {
			questionnaireAnswers.remove(questionnaireAnswers.size()-1);
			nextPageAnchor = questionnaireAnswers.get(questionnaireAnswers.size()-1).getId();
		}
		return new Tuple<>(nextPageAnchor, questionnaireAnswers);
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
			
			List<QuestionnaireQuestionDTO> questionDTOs = createQuestions(questionnaire.getId(), questionnaireDTO.getQuestions());
			
			return convertToQuestionnaireDTO(questionnaire, questionDTOs);
		});
		
		return new CreateQuestionnaireResponse(result);
	}

	private List<QuestionnaireQuestionDTO> createQuestions(Long questionnaireId, List<QuestionnaireQuestionDTO> questions) {
		List<QuestionnaireQuestionDTO> resultDTOs = new ArrayList<>();
		for (QuestionnaireQuestionDTO questionDTO : questions) {
			QuestionnaireQuestion question = ConvertHelper.convert(questionDTO, QuestionnaireQuestion.class);
			question.setQuestionnaireId(questionnaireId);
			questionnaireQuestionProvider.createQuestionnaireQuestion(question);
			List<QuestionnaireOptionDTO> optionDTOs = null;
			if (QuestionType.fromCode(questionDTO.getQuestionType()) != QuestionType.BLANK) {
				optionDTOs = createOptions(questionnaireId, question.getId(), questionDTO.getOptions());
			}else {
				optionDTOs = createBlankOption(questionnaireId, question.getId());
			}
			resultDTOs.add(convertToQuestionDTO(question, optionDTOs));
		}
		
		return resultDTOs;
	}

	//针对填空题，创建一个空的选项，以便查询的时候使用
	private List<QuestionnaireOptionDTO> createBlankOption(Long questionnaireId, Long questionId) {
		QuestionnaireOption option = new QuestionnaireOption();
		option.setQuestionnaireId(questionnaireId);
		option.setQuestionId(questionId);
		questionnaireOptionProvider.createQuestionnaireOption(option);
		return Arrays.asList(convertToOptionDTO(option));
	}
	
	private QuestionnaireQuestionDTO convertToQuestionDTO(QuestionnaireQuestion question,
			List<QuestionnaireOptionDTO> optionDTOs) {
		return convertToQuestionDTO(question, optionDTOs, null);
	}

	private QuestionnaireQuestionDTO convertToQuestionDTO(QuestionnaireQuestion question,
			List<QuestionnaireOptionDTO> optionDTOs, Long nextPageAnchor) {
		QuestionnaireQuestionDTO questionDTO = ConvertHelper.convert(question, QuestionnaireQuestionDTO.class);
		questionDTO.setOptions(optionDTOs);
		questionDTO.setNextPageAnchor(nextPageAnchor);
		return questionDTO;
	}

	private List<QuestionnaireOptionDTO> createOptions(Long questionnaireId, Long questionId,
			List<QuestionnaireOptionDTO> options) {
		List<QuestionnaireOptionDTO> resultDTOs = new ArrayList<>();
		for (QuestionnaireOptionDTO optionDTO : options) {
			QuestionnaireOption option = ConvertHelper.convert(optionDTO, QuestionnaireOption.class);
			option.setQuestionnaireId(questionnaireId);
			option.setQuestionId(questionId);
			questionnaireOptionProvider.createQuestionnaireOption(option);
			resultDTOs.add(convertToOptionDTO(option));
		}
		return resultDTOs;
	}

	private QuestionnaireOptionDTO convertToOptionDTO(QuestionnaireOption option) {
		return convertToOptionDTO(option, null);
	}
	
	private QuestionnaireOptionDTO convertToOptionDTO(QuestionnaireOption option, List<QuestionnaireAnswer> questionnaireAnswers) {
		QuestionnaireOptionDTO optionDTO = ConvertHelper.convert(option, QuestionnaireOptionDTO.class);
		optionDTO.setOptionUrl(getUrl(option.getOptionUri()));
		if (questionnaireAnswers != null) {
			for (QuestionnaireAnswer questionnaireAnswer : questionnaireAnswers) {
				if (questionnaireAnswer.getOptionId().longValue() == option.getId().longValue()) {
					optionDTO.setChecked(TrueOrFalseFlag.TRUE.getCode());
					optionDTO.setOptionContent(questionnaireAnswer.getOptionContent());
					break;
				}
			}
		}
		return optionDTO;
	}

	private String getUrl(String uri) {
		if (uri != null && uri.length() > 0) {
			try {
				return contentServerService.parserUri(uri, "", UserContext.current().getUser().getId());
			} catch (Exception e) {

			}
		}
		return null;
	}

	private QuestionnaireDTO convertToQuestionnaireDTO(Questionnaire questionnaire, List<QuestionnaireQuestionDTO> questionDTOs){
		QuestionnaireDTO questionnaireDTO = ConvertHelper.convert(questionnaire, QuestionnaireDTO.class);
		questionnaireDTO.setQuestions(questionDTOs);
		questionnaireDTO.setCreateTime(questionnaire.getCreateTime()==null?null:questionnaire.getCreateTime().getTime());
		questionnaireDTO.setPublishTime(questionnaire.getPublishTime()==null?null:questionnaire.getPublishTime().getTime());
		return questionnaireDTO;
	}
	
	private QuestionnaireDTO convertToQuestionnaireDTO(Questionnaire questionnaire){
		return convertToQuestionnaireDTO(questionnaire, false, null, null);
	}
	
	private QuestionnaireDTO convertToQuestionnaireDTO(Questionnaire questionnaire, boolean containTarget, String targetType, Long targetId){
		QuestionnaireDTO questionnaireDTO = convertToQuestionnaireDTO(questionnaire, null);
		if (containTarget) {
			QuestionnaireAnswer answer = questionnaireAnswerProvider.findAnyAnswerByTarget(questionnaire.getId(), targetType, targetId);
			if (answer != null) {
				questionnaireDTO.setSubmitTime(answer.getCreateTime().getTime());
			}
		}
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

	private Questionnaire findQuestionnaireById(Integer namespaceId, Long questionnaireId) {
		Questionnaire questionnaire = findQuestionnaireById(questionnaireId);
		if (questionnaire.getNamespaceId().intValue() != namespaceId) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"invalid parameters, no questionnaire in this namespace");
		}
		return questionnaire;
	}
	
	@Override
	public void deleteQuestionnaire(DeleteQuestionnaireCommand cmd) {
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_QUESTIONNAIRE.getCode() + cmd.getQuestionnaireId()).enter(()->{
			Questionnaire questionnaire = findQuestionnaireById(cmd.getNamespaceId(), cmd.getQuestionnaireId());
			questionnaire.setStatus(QuestionnaireStatus.INACTIVE.getCode());
			questionnaireProvider.updateQuestionnaire(questionnaire);
			return null;
		});
	}

	@Override
	public GetQuestionnaireResultDetailResponse getQuestionnaireResultDetail(GetQuestionnaireResultDetailCommand cmd) {
		// 主要用于检查问卷是否存在
		findQuestionnaireById(cmd.getNamespaceId(), cmd.getQuestionnaireId());
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		int pageAnchor = cmd.getPageAnchor()==null?1:cmd.getPageAnchor().intValue();
		List<QuestionnaireAnswer> questionnaireAnswers = questionnaireAnswerProvider.listQuestionnaireTarget(cmd.getQuestionnaireId(), cmd.getKeywords(), pageAnchor, pageSize+1);
		Long nextPageAnchor = null;
		if (questionnaireAnswers.size() > pageSize) {
			questionnaireAnswers.remove(questionnaireAnswers.size()-1);
			nextPageAnchor = Long.valueOf(++pageAnchor);
		}
		return new GetQuestionnaireResultDetailResponse(nextPageAnchor, questionnaireAnswers.stream().map(q->convertToTargetDTO(q)).collect(Collectors.toList()));
	}

	private QuestionnaireResultTargetDTO convertToTargetDTO(QuestionnaireAnswer answer) {
		QuestionnaireResultTargetDTO dto = ConvertHelper.convert(answer, QuestionnaireResultTargetDTO.class);
		dto.setSubmitTime(answer.getCreateTime().getTime());
		return dto;
	}
	
	@Override
	public GetQuestionnaireResultSummaryResponse getQuestionnaireResultSummary(GetQuestionnaireResultSummaryCommand cmd) {
		List<QuestionnaireOption> questionnaireOptions = questionnaireOptionProvider.listOptionsByQuestionnaireId(cmd.getQuestionnaireId());
		return new GetQuestionnaireResultSummaryResponse(convertToQuestionnaireDTO(questionnaireOptions, true, cmd.getPageSize()));
	}

	@Override
	public ListOptionTargetsResponse listOptionTargets(ListOptionTargetsCommand cmd) {
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Tuple<Long, List<QuestionnaireAnswer>> tuple = findQuestionnaireAnswersByOptionId(cmd.getOptionId(), cmd.getPageAnchor(), pageSize);
		return new ListOptionTargetsResponse(tuple.first(), tuple.second().stream().map(q->convertToTargetDTO(q)).collect(Collectors.toList()));
	}

	@Override
	public ListBlankQuestionAnswersResponse listBlankQuestionAnswers(ListBlankQuestionAnswersCommand cmd) {
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Tuple<Long, List<QuestionnaireAnswer>> tuple = findQuestionnaireAnswersByQuestionId(cmd.getQuestionId(), cmd.getPageAnchor(), pageSize);
		return new ListBlankQuestionAnswersResponse(cmd.getQuestionId(), tuple.first(), tuple.second().stream().map(q->convertToOptionDTO(q)).collect(Collectors.toList()));
	}

	@Override
	public ListTargetQuestionnairesResponse listTargetQuestionnaires(ListTargetQuestionnairesCommand cmd) {
		checkOwner(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		List<Questionnaire> questionnaires = questionnaireProvider.listTargetQuestionnaireByOwner(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId(), cmd.getPageAnchor(), pageSize+1);
		Long nextPageAnchor = null;
		if (questionnaires.size() > pageSize) {
			questionnaires.remove(questionnaires.size()-1);
			nextPageAnchor = questionnaires.get(questionnaires.size()-1).getPublishTime().getTime();
		}
		return new ListTargetQuestionnairesResponse(nextPageAnchor, questionnaires.stream().map(q->convertToQuestionnaireDTO(q, true, cmd.getTargetType(), cmd.getTargetId())).collect(Collectors.toList()));
	}

	@Override
	public GetTargetQuestionnaireDetailResponse getTargetQuestionnaireDetail(GetTargetQuestionnaireDetailCommand cmd) {
		return new GetTargetQuestionnaireDetailResponse(getTargetQuestionnaireDetail(cmd.getQuestionnaireId(), cmd.getTargetType(), cmd.getTargetId()));
	}
	
	private QuestionnaireDTO getTargetQuestionnaireDetail(Long questionnaireId, String targetType, Long targetId){
		List<QuestionnaireOption> questionnaireOptions = questionnaireOptionProvider.listOptionsByQuestionnaireId(questionnaireId);
		return convertToQuestionnaireDTO(questionnaireOptions, targetType, targetId);
	}

	private QuestionnaireDTO convertToQuestionnaireDTO(List<QuestionnaireOption> questionnaireOptions,
			String targetType, Long targetId) {
		List<QuestionnaireDTO> questionnaireDTOs = new ArrayList<>();
		// k1为问卷的id，k2为问题的id
		Map<Long, Map<Long, List<QuestionnaireOption>>> map = questionnaireOptions.parallelStream().collect(Collectors.groupingBy(QuestionnaireOption::getQuestionnaireId,Collectors.groupingBy(QuestionnaireOption::getQuestionId)));
		map.forEach((k1, v1)->{
			Questionnaire questionnaire = findQuestionnaireById(k1);
			List<QuestionnaireQuestionDTO> questionDTOs = new ArrayList<>();
			v1.forEach((k2, v2)->{
				QuestionnaireQuestion question = questionnaireQuestionProvider.findQuestionnaireQuestionById(k2);
				List<QuestionnaireAnswer> questionnaireAnswers = questionnaireAnswerProvider.listTargetQuestionnaireAnswerByQuestionId(question.getId(), targetType, targetId);
				List<QuestionnaireOptionDTO> optionDTOs =  v2.stream().map(o->convertToOptionDTO(o, questionnaireAnswers)).collect(Collectors.toList());
				questionDTOs.add(convertToQuestionDTO(question, optionDTOs));
			});
			// 经过map处理后顺序会乱，所以要重新排序下
			sortQuestions(questionDTOs);
			questionnaireDTOs.add(convertToQuestionnaireDTO(questionnaire, questionDTOs));
		});
		return questionnaireDTOs.get(0);
	}

	private void sortQuestions(List<QuestionnaireQuestionDTO> questionDTOs) {
		questionDTOs.sort(new Comparator<QuestionnaireQuestionDTO>() {
			@Override
			public int compare(QuestionnaireQuestionDTO o1, QuestionnaireQuestionDTO o2) {
				return (int) (o1.getId() - o2.getId());
			}
		});
	}

	@Override
	public CreateTargetQuestionnaireResponse createTargetQuestionnaire(CreateTargetQuestionnaireCommand cmd) {
		final Timestamp currentTime = new Timestamp(DateHelper.currentGMTTime().getTime());
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_QUESTIONNAIRE.getCode() + cmd.getTargetType() +"_" + cmd.getTargetId() + "_" + cmd.getQuestionnaire().getId()).enter(()->{
			checkCreateTargetQuestionnaireParameters(cmd);  //防止重复提交，检查放这儿
			dbProvider.execute(s->{
				for (QuestionnaireQuestionDTO questionDTO : cmd.getQuestionnaire().getQuestions()) {
					for (QuestionnaireOptionDTO optionDTO : questionDTO.getOptions()) {
						createQuestionnaireAnswer(cmd.getQuestionnaire(), questionDTO, optionDTO, cmd.getTargetType(), cmd.getTargetId(), currentTime);
						updateOptionCheckedCount(optionDTO.getId());
					}
				}
				updateQuestionnaireCollectionCount(cmd.getQuestionnaire().getId());
				return null;
			});
			return null;
		});
		
		return new CreateTargetQuestionnaireResponse(getTargetQuestionnaireDetail(cmd.getQuestionnaire().getId(), cmd.getTargetType(), cmd.getTargetId()));
	}

	private void updateQuestionnaireCollectionCount(Long questionnaireId) {
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_QUESTIONNAIRE.getCode() + questionnaireId).enter(()->{
			Questionnaire questionnaire = questionnaireProvider.findQuestionnaireById(questionnaireId);
			questionnaire.setCollectionCount((questionnaire.getCollectionCount()==null?0:questionnaire.getCollectionCount())+1);
			questionnaireProvider.updateQuestionnaire(questionnaire);
			return null;
		});
	}

	private void updateOptionCheckedCount(Long optionId) {
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_QUESTIONNAIRE_OPTION.getCode() + optionId).enter(()->{
			QuestionnaireOption option = questionnaireOptionProvider.findQuestionnaireOptionById(optionId);
			option.setCheckedCount((option.getCheckedCount()==null?0:option.getCheckedCount())+1);
			questionnaireOptionProvider.updateQuestionnaireOption(option);
			return null;
		});
	}

	private void createQuestionnaireAnswer(QuestionnaireDTO questionnaireDTO, QuestionnaireQuestionDTO questionDTO,
			QuestionnaireOptionDTO optionDTO, String targetType, Long targetId, Timestamp currentTime) {
		QuestionnaireAnswer answer = new QuestionnaireAnswer();
		answer.setQuestionnaireId(questionnaireDTO.getId());
		answer.setQuestionId(questionDTO.getId());
		answer.setOptionId(optionDTO.getId());
		answer.setTargetType(targetType);
		answer.setTargetId(targetId);
		answer.setTargetName(getTargetName(targetType, targetId));
		answer.setOptionContent(optionDTO.getOptionContent());
		answer.setCreateTime(currentTime);
		questionnaireAnswerProvider.createQuestionnaireAnswer(answer);
	}

	private String getTargetName(String targetType, Long targetId) {
		QuestionnaireTargetType questionnaireTargetType = QuestionnaireTargetType.fromCode(targetType);
		switch (questionnaireTargetType) {
		case ORGANIZATION:
			Organization organization = organizationProvider.findOrganizationById(targetId);
			return organization.getName();
		default:
			break;
		}
		
		return null;
	}

	private void checkCreateTargetQuestionnaireParameters(CreateTargetQuestionnaireCommand cmd) {
		QuestionnaireTargetType targetType = QuestionnaireTargetType.fromCode(cmd.getTargetType());
		if (targetType == null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"targetType error, targetType=" + cmd.getTargetType());
		}
		
		QuestionnaireDTO questionnaireDTO = cmd.getQuestionnaire();
		Questionnaire questionnaire = findQuestionnaireById(questionnaireDTO.getId());
		if (QuestionnaireStatus.fromCode(questionnaire.getStatus()) != QuestionnaireStatus.ACTIVE) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"status error, status=" + questionnaire.getStatus());
		}
		
		QuestionnaireAnswer answer = questionnaireAnswerProvider.findAnyAnswerByTarget(questionnaireDTO.getId(), cmd.getTargetType(), cmd.getTargetId());
		if (answer != null) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Don't submit repeatedly!");
		}
		
		List<QuestionnaireQuestionDTO> questionDTOs = questionnaireDTO.getQuestions();
		List<QuestionnaireQuestion> questions = questionnaireQuestionProvider.listQuestionsByQuestionnaireId(questionnaireDTO.getId());
		if (questionDTOs == null || questionDTOs.size() != questions.size()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"some question may not be checked");
		}
		
		for (QuestionnaireQuestionDTO questionDTO : questionDTOs) {
			QuestionnaireQuestion question = questionnaireQuestionProvider.findQuestionnaireQuestionById(questionDTO.getId());
			if (question == null) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"error question, quesionId=%d", questionDTO.getId());
			}
			List<QuestionnaireOptionDTO> optionDTOs = questionDTO.getOptions();
			if (optionDTOs == null || optionDTOs.size() == 0) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"question %d  may not be checked", questionDTO.getId());
			}
			for (QuestionnaireOptionDTO optionDTO : optionDTOs) {
				QuestionnaireOption option = questionnaireOptionProvider.findQuestionnaireOptionById(optionDTO.getId());
				if (option == null) {
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"not this option, optionId=", optionDTO.getId());
				}
			}
		}
	}

}