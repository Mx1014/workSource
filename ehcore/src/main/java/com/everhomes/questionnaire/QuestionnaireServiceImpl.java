// @formatter:off
package com.everhomes.questionnaire;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.CommunityService;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.common.ActivationFlag;
import com.everhomes.rest.community.admin.CommunityUserAddressResponse;
import com.everhomes.rest.community.admin.ListCommunityUsersCommand;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.questionnaire.*;
import com.everhomes.rest.user.NamespaceUserType;
import com.everhomes.user.User;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.util.TimSorter;
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
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;

@Component
public class QuestionnaireServiceImpl implements QuestionnaireService {
	
	@Autowired
	private QuestionnaireProvider questionnaireProvider;

	@Autowired
	private QuestionnaireRangeProvider questionnaireRangeProvider;

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

	@Autowired
	private UserProvider userProvider;


	@Override
	public ListQuestionnairesResponse listQuestionnaires(ListQuestionnairesCommand cmd) {
//		checkOwner(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		checkListQuestionnairesCommand(cmd);

		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Integer namespaceId = cmd.getNamespaceId();
		if(namespaceId == null){
			namespaceId = UserContext.getCurrentNamespaceId();
		}
		List<Questionnaire> questionnaires = questionnaireProvider.listQuestionnaireByOwner(cmd, namespaceId, pageSize+1);
		Long nextPageAnchor = null;
		if (questionnaires.size() > pageSize) {
			questionnaires.remove(questionnaires.size()-1);
			nextPageAnchor = questionnaires.get(questionnaires.size()-1).getId();
		}
		return new ListQuestionnairesResponse(nextPageAnchor, questionnaires.stream().map(q->convertToQuestionnaireDTO(q,cmd.getNowTime())).collect(Collectors.toList()));
	}

	private void checkListQuestionnairesCommand(ListQuestionnairesCommand cmd) {
		checkQuestionnaireStatus(cmd.getStatus());
		checkQuestionnaireCollectFlag(cmd.getCollectFlag());
		checkQuestionnaireTargetType(cmd.getTargetType());
	}

	private void checkQuestionnaireTargetType(String targetType) {
		if(targetType == null){
			return ;
		}
		QuestionnaireTargetType enumTargetType = QuestionnaireTargetType.fromCode(targetType);
		if (enumTargetType == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters, Unknown targetType = " + targetType );
		}
	}

	private void checkQuestionnaireCollectFlag(Byte collectFlag) {
		if(collectFlag == null){
			return ;
		}
		QuestionnaireCollectFlagType enumCollectFlag = QuestionnaireCollectFlagType.fromCode(collectFlag);
		if (enumCollectFlag == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters, Unknown collectFlag = " + enumCollectFlag );
		}
	}

	private void checkQuestionnaireStatus(Byte status) {
		if(status == null){
			return ;
		}
		QuestionnaireStatus enumStatus = QuestionnaireStatus.fromCode(status);
		if (enumStatus == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters, Unknown status = " + status );
		}
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
		QuestionnaireDTO dto = questionnaireDTOs.get(0);
		dto.setPercentComplete(generatePercentComplete(dto.getTargetUserNum(),dto.getCollectionCount()));
		return dto;
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
				questionnaireRangeProvider.deleteRangesByQuestionnaireId(questionnaireDTO.getId());
			} else {
				questionnaire = ConvertHelper.convert(questionnaireDTO, Questionnaire.class);
				questionnaire.setCutOffTime(questionnaireDTO.getCutOffTime()==null?null:new Timestamp(questionnaireDTO.getCutOffTime()));
				questionnaire.setPublishTime(questionnaireDTO.getPublishTime()==null?null:new Timestamp(questionnaireDTO.getPublishTime()));
				questionnaireProvider.createQuestionnaire(questionnaire);
			}
			
			List<QuestionnaireQuestionDTO> questionDTOs = createQuestions(questionnaire.getId(), questionnaireDTO.getQuestions());

			createRanges(questionnaire.getId(), questionnaireDTO.getRanges());

			return convertToQuestionnaireDTO(questionnaire, questionDTOs);
		});

		//异步计算用户范围并且发送消息
		if(QuestionnaireStatus.ACTIVE == QuestionnaireStatus.fromCode(questionnaireDTO.getStatus())){
			asynchronousSendMessage(result.getId());
		}
		return new CreateQuestionnaireResponse(result);
	}

	private void asynchronousSendMessage(Long questionnaireId) {
		QuestionnaireAsynchronizedServiceImpl handler = PlatformContext.getComponent("questionnaireAsynchronizedServiceImpl");
		handler.pushToQueque(questionnaireId);
	}

	private void createRanges(Long questionnaireId, List<QuestionnaireRangeDTO> ranges) {
		ranges.forEach(r->{
			QuestionnaireRange questionnaireRange = ConvertHelper.convert(r,QuestionnaireRange.class);
			questionnaireRange.setQuestionnaireId(questionnaireId);
			questionnaireRange.setStatus(CommonStatus.ACTIVE.getCode());
			questionnaireRangeProvider.createQuestionnaireRange(questionnaireRange);
		});
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
		questionnaireDTO.setCutOffTime(questionnaire.getCutOffTime()==null?null:questionnaire.getCutOffTime().getTime());
		questionnaireDTO.setPublishTime(questionnaire.getPublishTime()==null?null:questionnaire.getPublishTime().getTime());
		List<QuestionnaireRange> listRanges = questionnaireRangeProvider.listQuestionnaireRangeByQuestionnaireId(questionnaire.getId());
		questionnaireDTO.setRanges(listRanges.stream().map(r->ConvertHelper.convert(r,QuestionnaireRangeDTO.class)).collect(Collectors.toList()));
		return questionnaireDTO;
	}
	
	private QuestionnaireDTO convertToQuestionnaireDTO(Questionnaire questionnaire, Timestamp nowTime){
		QuestionnaireDTO dto = convertToQuestionnaireDTO(questionnaire, false, null, null);
		//v1.1版本补充一些属性
		if(questionnaire.getCutOffTime()!=null){
			dto.setCutOffTime(questionnaire.getCutOffTime().getTime());
			if(questionnaire.getCutOffTime().before(nowTime)){
				dto.setCollectFlag(QuestionnaireCollectFlagType.FINISHED.getCode());
			}else if(questionnaire.getCutOffTime().after(nowTime)){
				dto.setCollectFlag(QuestionnaireCollectFlagType.COLLECTING.getCode());
			}

		}
		if(questionnaire.getPosterUri()!=null){
			String url = contentServerService.parserUri(questionnaire.getPosterUri());
			dto.setPosterUrl(url);
		}
		if(questionnaire.getCollectionCount()!=null && questionnaire.getTargetUserNum()!=null
				&& questionnaire.getTargetUserNum()!=0){
			dto.setPercentComplete(generatePercentComplete(questionnaire.getTargetUserNum(),questionnaire.getCollectionCount()));
		}

		return dto;
	}

	private String generatePercentComplete(Integer targetUserNum, Integer collectionCount) {
		Float ftargetUserNumber = (float)targetUserNum;
		Float fcollectionCount = (float)collectionCount;
		String result = String.valueOf((fcollectionCount/ftargetUserNumber)*100);
		return result.replaceFirst("([0-9]*\\.\\d[0-9])\\d*","$1");
	}


	private QuestionnaireDTO convertToQuestionnaireDTO(Questionnaire questionnaire, boolean containTarget, String targetType, Long targetId){
		QuestionnaireDTO questionnaireDTO = convertToQuestionnaireDTO(questionnaire, (List)null);
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
		if (questionnaire.getNamespaceId().intValue() != questionnaireDTO.getNamespaceId().intValue()) {
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
		//新增部分属性
		questionnaire.setPosterUri(questionnaireDTO.getPosterUri());
		questionnaire.setTargetType(questionnaireDTO.getTargetType());
		questionnaire.setCutOffTime(questionnaireDTO.getCutOffTime()==null?null:new Timestamp(questionnaireDTO.getCutOffTime()));
		questionnaire.setPublishTime(questionnaireDTO.getPublishTime()==null?null:new Timestamp(questionnaireDTO.getPublishTime()));
		questionnaire.setSupportAnonymous(questionnaireDTO.getSupportAnonymous());
		questionnaire.setSupportShare(questionnaireDTO.getSupportShare());

		questionnaireProvider.updateQuestionnaire(questionnaire);
	}

//	private void checkOwner(Integer namespaceId, String ownerType, Long ownerId){
//		QuestionnaireOwnerType questionnaireOwnerType;
//		if (namespaceId == null || (questionnaireOwnerType=QuestionnaireOwnerType.fromCode(ownerType)) == null
//				|| ownerId == null) {
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid parameters, namespaceId=" + namespaceId + ", ownerType=" + ownerType + ", ownerId=" + ownerId);
//		}
//		switch (questionnaireOwnerType) {
//		case COMMUNITY:
//			Community community = communityProvider.findCommunityById(ownerId);
//			if (community == null) {
//				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//						"there is no such community, ownerId="+ownerId);
//			}
//			break;
//
//		default:
//			break;
//		}
//	}
	
	private void checkQuestionnaireParameters(QuestionnaireDTO questionnaire) {
//		checkOwner(questionnaire.getNamespaceId(), questionnaire.getOwnerType(), questionnaire.getOwnerId());
		if(questionnaire == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters,questionnaire is null");
		}
		//问卷名称非空检查
		if (StringUtils.isBlank(questionnaire.getQuestionnaireName())) {
			throw RuntimeErrorException.errorWith(QuestionnaireServiceErrorCode.SCOPE, QuestionnaireServiceErrorCode.QUESTIONNAIRE_NAME_EMPTY,
					"Invalid parameters, questionnaire name cannot be null");
		}
		if (questionnaire.getQuestionnaireName().length() > 50) {
			throw RuntimeErrorException.errorWith(QuestionnaireServiceErrorCode.SCOPE, QuestionnaireServiceErrorCode.QUESTIONNAIRE_NAME_LENGTH_BEYOND_50,
					"Invalid parameters, questionnaire name length cannot be beyond 50");
		}

		QuestionnaireStatus status;
		if ((status=QuestionnaireStatus.fromCode(questionnaire.getStatus())) == null || status == QuestionnaireStatus.INACTIVE) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters,status="+questionnaire.getStatus());
		}

		//如保存为草稿，则只检查标题和状态的正确性
		if(status == QuestionnaireStatus.DRAFT){
			return ;
		}

		if(StringUtils.isBlank(questionnaire.getDescription())){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters,description is null");
		}

		//问卷题目数量非0检查
		if (questionnaire.getQuestions() == null || questionnaire.getQuestions().size() == 0) {
			throw RuntimeErrorException.errorWith(QuestionnaireServiceErrorCode.SCOPE, QuestionnaireServiceErrorCode.NO_QUESTIONS,
					"Invalid parameters, there are no questions");
		}

		if(QuestionnaireTargetType.ORGANIZATION == QuestionnaireTargetType.fromCode(questionnaire.getTargetType())){
			questionnaire.setSupportShare(null);
		}

		//问题里面的枚举，非空等检查
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

		//检查枚举，收集的目标
		QuestionnaireTargetType targetType = QuestionnaireTargetType.fromCode(questionnaire.getTargetType());
		if(targetType == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters,TargetType="+questionnaire.getTargetType());
		}

		//收集的范围非空检查，范围类型枚举检查
		if(questionnaire.getRanges() == null || questionnaire.getRanges().size() == 0 ){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters,ranges is null");
		}else{
			for (QuestionnaireRangeDTO dto : questionnaire.getRanges()) {
				QuestionnaireRangeType rangeType = QuestionnaireRangeType.fromCode(dto.getRangeType());
				if(rangeType==null){
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"Invalid parameters,rangeType = "+dto.getRangeType());
				}

				switch (rangeType){
					case COMMUNITY_AUTHENTICATED:
					case COMMUNITY_UNAUTHORIZED:
					case COMMUNITY_ALL:
					case ENTERPRISE:
					case USER:
						try {
							Long.valueOf(dto.getRange());
						}catch (Exception e){
							throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
									"Invalid parameters,rangeType = "+dto.getRangeType()+", range = "+dto.getRange());
						}
				}
			}
		}

		//截止日期非空检查
		if(questionnaire.getCutOffTime() == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters,cutOffTime is null");
		}

		//枚举支持匿名检查
		QuestionnaireCommonStatus supportAnonymous = QuestionnaireCommonStatus.fromCode(questionnaire.getSupportAnonymous());
		if(supportAnonymous == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters,supportAnonymous = " +questionnaire.getSupportAnonymous());
		}

		//枚举支持分享检查
		if (targetType == QuestionnaireTargetType.USER) {
			QuestionnaireCommonStatus supportShare = QuestionnaireCommonStatus.fromCode(questionnaire.getSupportShare());
			if (supportShare == null) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"Invalid parameters,supportShare = " + questionnaire.getSupportShare());
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
		Questionnaire questionnaire = findQuestionnaireById(cmd.getNamespaceId(), cmd.getQuestionnaireId());
		
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		int pageAnchor = cmd.getPageAnchor()==null?1:cmd.getPageAnchor().intValue();
		List<QuestionnaireAnswer> questionnaireAnswers = questionnaireAnswerProvider.listQuestionnaireTarget(cmd.getQuestionnaireId(), cmd.getKeywords(),cmd.getTargetFrom(), pageAnchor, pageSize+1);
		Long nextPageAnchor = null;
		if (questionnaireAnswers.size() > pageSize) {
			questionnaireAnswers.remove(questionnaireAnswers.size()-1);
			nextPageAnchor = Long.valueOf(++pageAnchor);
		}
		GetQuestionnaireResultDetailResponse response = new GetQuestionnaireResultDetailResponse(nextPageAnchor, questionnaireAnswers.stream().map(q->convertToTargetDTO(q)).collect(Collectors.toList()));

		response.setCollectionCount(questionnaire.getCollectionCount());
		response.setTargetUserNum(questionnaire.getTargetUserNum());
		response.setPercentComplete(generatePercentComplete(questionnaire.getTargetUserNum(),questionnaire.getCollectionCount()));
		return response;
	}

	private QuestionnaireResultTargetDTO convertToTargetDTO(QuestionnaireAnswer answer) {
		QuestionnaireResultTargetDTO dto = ConvertHelper.convert(answer, QuestionnaireResultTargetDTO.class);
		dto.setSubmitTime(answer.getCreateTime().getTime());
		if(QuestionnaireCommonStatus.fromCode(answer.getAnonymousFlag()) == QuestionnaireCommonStatus.TRUE){
			if(QuestionnaireTargetType.fromCode(answer.getTargetType()) == QuestionnaireTargetType.ORGANIZATION) {
				dto.setTargetName("匿名企业");
			}else{
				dto.setTargetName("匿名用户");
			}
		}
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
//		checkOwner(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		
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
			Integer count = questionnaireAnswerProvider.countQuestionnaireAnswerByQuestionnaireId(questionnaireId);
			questionnaire.setCollectionCount(count);
			questionnaire.setCollectionCount((questionnaire.getCollectionCount()==null?0:questionnaire.getCollectionCount())+1);
			questionnaireProvider.updateQuestionnaire(questionnaire);
			return null;
		});
	}

	private void updateOptionCheckedCount(Long optionId) {
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_QUESTIONNAIRE_OPTION.getCode() + optionId).enter(()->{
			QuestionnaireOption option = questionnaireOptionProvider.findQuestionnaireOptionById(optionId);
			Integer count = questionnaireAnswerProvider.countQuestionnaireAnswerByOptionId(optionId);
			option.setCheckedCount(count);
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
		answer.setOptionContent(optionDTO.getOptionContent());
		answer.setCreateTime(currentTime);
		answer.setAnonymousFlag(questionnaireDTO.getAnonymousFlag()==null?QuestionnaireCommonStatus.FALSE.getCode():questionnaireDTO.getAnonymousFlag());
		generateTarget(answer,targetType,targetId);
		questionnaireAnswerProvider.createQuestionnaireAnswer(answer);
	}

	private void generateTarget(QuestionnaireAnswer answer, String targetType, Long targetId) {
		QuestionnaireTargetType questionnaireTargetType = QuestionnaireTargetType.fromCode(targetType);
		switch (questionnaireTargetType) {
			case ORGANIZATION:
				Organization organization = organizationProvider.findOrganizationById(targetId);
				if(organization == null){
					answer.setTargetName("");
				}else {
					answer.setTargetName(organization.getName());
				}
				break;
			case USER:
				User user = userProvider.findUserById(targetId);
				if(user == null){
					answer.setTargetName("");
					answer.setTargetFrom(QuestionnaireUserType.APP.getCode());
				}else {
					answer.setTargetName(user.getNickName());
					NamespaceUserType userType = NamespaceUserType.fromCode(user.getNamespaceUserType());
					answer.setTargetFrom(userType == NamespaceUserType.WX?QuestionnaireUserType.WX.getCode():QuestionnaireUserType.APP.getCode());
				}
				UserIdentifier userIdentifier = userProvider.findUserIdentifiersOfUser(targetId,UserContext.getCurrentNamespaceId());
				if(userIdentifier != null) {
					answer.setTargetPhone(userIdentifier.getIdentifierToken());
				}else{
					answer.setTargetPhone("");
				}
				break;
			default:
				break;
		}
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
					"提交失败，问卷未发布！" + questionnaire.getStatus());
		}

		if(!targetType.getCode().equals(questionnaire.getTargetType())){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"questionnaire type = " + questionnaire.getTargetType() + " summit type = "+cmd.getTargetType());
		}

		//支持匿名
		if(QuestionnaireCommonStatus.FALSE == QuestionnaireCommonStatus.fromCode(questionnaire.getSupportAnonymous())
				&& QuestionnaireCommonStatus.TRUE == QuestionnaireCommonStatus.fromCode(questionnaireDTO.getSupportAnonymous())){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"提交失败，问卷不支持匿名回答!");
		}

		QuestionnaireAnswer answer = questionnaireAnswerProvider.findAnyAnswerByTarget(questionnaireDTO.getId(), cmd.getTargetType(), cmd.getTargetId());
		if (answer != null) {
			if(QuestionnaireTargetType.ORGANIZATION == QuestionnaireTargetType.fromCode(cmd.getTargetType())) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"提交失败，其他管理员已填写问卷！");
			}else{
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"提交失败，已填写问卷！");
			}
		}
		
		List<QuestionnaireQuestionDTO> questionDTOs = questionnaireDTO.getQuestions();
		List<QuestionnaireQuestion> questions = questionnaireQuestionProvider.listQuestionsByQuestionnaireId(questionnaireDTO.getId());
		if (questionDTOs == null || questionDTOs.size() != questions.size()) {
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"提交失败，请完成答卷！");
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