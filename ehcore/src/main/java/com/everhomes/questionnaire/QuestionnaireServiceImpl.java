// @formatter:off
package com.everhomes.questionnaire;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.constants.Constants;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.payment.util.DownloadUtil;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.organization.OrganizationAndDetailDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.questionnaire.*;
import com.everhomes.rest.user.NamespaceUserType;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.user.User;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.UserProvider;
import com.everhomes.util.*;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.jooq.util.derby.sys.Sys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

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

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

@Component
public class QuestionnaireServiceImpl implements QuestionnaireService, ApplicationListener<ContextRefreshedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuestionnaireServiceImpl.class);

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
	private UserPrivilegeMgr userPrivilegeMgr;
	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	private UserProvider userProvider;

	@Autowired
	private LocaleStringService stringService;

	@Autowired
	private ScheduleProvider scheduleProvider;

	@Autowired
	private QuestionnaireAsynSendMessageService questionnaireAsynSendMessageService;
	
    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
	//@PostConstruct
	public void setup(){
		//启动定时任务
		String triggerName = "questionnarieSendMessage";
		String jobName= "questionnarieSendMessage_"+System.currentTimeMillis();
		//每天一点查询即将到期的问卷，发消息给没有填的用户。
		String cronExpression = "0 0 1 * * ?";
		try {
			cronExpression = configurationProvider.getValue(ConfigConstants.QUESTIONNAIRE_SEND_MESSAGE_EXPRESS, "0 0 1 * * ?");
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			scheduleProvider.scheduleCronJob(triggerName,jobName,cronExpression,QuestionnaireSendMessageJob.class , null);
		}
	}
	
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            setup();
        }
    }
	
	@Override
	public ListQuestionnairesResponse listQuestionnaires(ListQuestionnairesCommand cmd) {
//		checkOwner(cmd.getNamespaceId(), cmd.getOwnerType(), cmd.getOwnerId());
		if(QuestionnaireStatus.fromCode(cmd.getStatus())==QuestionnaireStatus.ACTIVE){
			if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
				userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4170041710L, cmd.getAppId(), null,0L);//已发布权限
			}
		}else if(QuestionnaireStatus.fromCode(cmd.getStatus())==QuestionnaireStatus.DRAFT){
			if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
				userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4170041720L, cmd.getAppId(), null,0L);//草稿箱权限
			}
		}
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
//		checkQuestionnaireTargetType(cmd.getTargetType());

		QuestionnaireTargetType enumTargetType = QuestionnaireTargetType.fromCode(cmd.getTargetType());
		if (enumTargetType == null){
			cmd.setTargetType(null);
		}

		cmd.setNowTime(new Timestamp(System.currentTimeMillis()));
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
		return new GetQuestionnaireDetailResponse(convertToQuestionnaireDTO(cmd.getQuestionnaireId(),questionnaireOptions));
	}

	private QuestionnaireDTO convertToQuestionnaireDTO(Long questionnaireId,List<QuestionnaireOption> questionnaireOptions) {
		return convertToQuestionnaireDTO(questionnaireId,questionnaireOptions, false, null);
	}
	
	// 是否包含填空题，统计时用
	private QuestionnaireDTO convertToQuestionnaireDTO(Long questionnaireId,List<QuestionnaireOption> questionnaireOptions, boolean containBlank, Integer pageSize) {
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
		QuestionnaireDTO dto = null;
		if(questionnaireDTOs.size()>0) {
			dto = questionnaireDTOs.get(0);
		}else{
			dto = convertToQuestionnaireDTO(findQuestionnaireById(questionnaireId),(List<QuestionnaireQuestionDTO>)null);
		}
		dto.setPercentComplete(generatePercentComplete(dto.getTargetUserNum(),dto.getCollectionCount()));
		if(dto.getCollectionCount() == null){
			dto.setCollectionCount(0);
		}
		if(dto.getTargetUserNum()==null){
			dto.setTargetUserNum(0);
		}
		generateShareUrl(dto);
		generatePosterUrl(dto);
		return dto;
	}
	
	private QuestionnaireOptionDTO convertToOptionDTO(QuestionnaireAnswer answer) {
		QuestionnaireOptionDTO optionDTO = new QuestionnaireOptionDTO();
		optionDTO.setOptionName(answer.getTargetName());
		optionDTO.setOptionContent(answer.getOptionContent());
		optionDTO.setTargetPhone(answer.getTargetPhone());
		if(QuestionnaireCommonStatus.fromCode(answer.getAnonymousFlag()) == QuestionnaireCommonStatus.TRUE){
			if(QuestionnaireTargetType.fromCode(answer.getTargetType()) == QuestionnaireTargetType.ORGANIZATION) {
				optionDTO.setOptionName(stringService.getLocalizedString(QuestionnaireServiceErrorCode.SCOPE,QuestionnaireServiceErrorCode.UNKNOWN3,"zh_CN","匿名企业"));
			}else{
				optionDTO.setOptionName(stringService.getLocalizedString(QuestionnaireServiceErrorCode.SCOPE,QuestionnaireServiceErrorCode.UNKNOWN4,"zh_CN","匿名用户"));
			}
			optionDTO.setTargetPhone(null);
		}
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
		if(cmd.getCurrentPMId()!=null && cmd.getAppId()!=null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)){
			userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4170041710L, cmd.getAppId(), null,0L);//已发布权限
		}
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


			QuestionnaireDTO resultDto = convertToQuestionnaireDTO(questionnaire, questionDTOs);
			//异步计算用户范围并且发送消息
			if(QuestionnaireStatus.ACTIVE == QuestionnaireStatus.fromCode(questionnaireDTO.getStatus())){
				asynchronousSendMessage(resultDto.getId());
			}
			return resultDto;
		});

		return new CreateQuestionnaireResponse(result);
	}

	private void asynchronousSendMessage(Long questionnaireId) {
		QuestionnaireAsynchronizedServiceImpl handler = PlatformContext.getComponent("questionnaireAsynchronizedServiceImpl");
		handler.pushToQueque(questionnaireId);
	}

	private void createRanges(Long questionnaireId, List<QuestionnaireRangeDTO> ranges) {
		if(ranges!=null) {
			ranges.forEach(r -> {
				QuestionnaireRange questionnaireRange = ConvertHelper.convert(r, QuestionnaireRange.class);
				questionnaireRange.setQuestionnaireId(questionnaireId);
				questionnaireRange.setStatus(CommonStatus.ACTIVE.getCode());
				questionnaireRange.setNamespaceId(UserContext.getCurrentNamespaceId());
				QuestionnaireRangeType rangeType = QuestionnaireRangeType.fromCode(r.getRangeType());
				if (rangeType == QuestionnaireRangeType.NAMESPACE_ALL ||
						rangeType == QuestionnaireRangeType.NAMESPACE_AUTHENTICATED ||
						rangeType == QuestionnaireRangeType.NAMESPACE_UNAUTHORIZED) {
					questionnaireRange.setRange("" + UserContext.getCurrentNamespaceId());
				}
				questionnaireRangeProvider.createQuestionnaireRange(questionnaireRange);
			});
		}
	}

	private List<QuestionnaireQuestionDTO> createQuestions(Long questionnaireId, List<QuestionnaireQuestionDTO> questions) {
		List<QuestionnaireQuestionDTO> resultDTOs = new ArrayList<>();
		if(questions!=null) {
			for (QuestionnaireQuestionDTO questionDTO : questions) {
				QuestionnaireQuestion question = ConvertHelper.convert(questionDTO, QuestionnaireQuestion.class);
				question.setQuestionnaireId(questionnaireId);
				questionnaireQuestionProvider.createQuestionnaireQuestion(question);
				List<QuestionnaireOptionDTO> optionDTOs = null;
				if (QuestionType.fromCode(questionDTO.getQuestionType()) != QuestionType.BLANK) {
					optionDTOs = createOptions(questionnaireId, question.getId(), questionDTO.getOptions());
				} else {
					optionDTOs = createBlankOption(questionnaireId, question.getId());
				}
				resultDTOs.add(convertToQuestionDTO(question, optionDTOs));
			}
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
			} catch (Throwable e) {

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
		if(questionnaire.getPosterUri()!=null){
			String url = contentServerService.parserUri(questionnaire.getPosterUri());
			questionnaireDTO.setPosterUrl(url);
		}
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
		dto.setPercentComplete(generatePercentComplete(questionnaire.getTargetUserNum(),questionnaire.getCollectionCount()));

		return dto;
	}

	private String generatePercentComplete(Integer targetUserNum, Integer collectionCount) {
		if(collectionCount!=null && targetUserNum!=null
				&& targetUserNum!=0){
			Float ftargetUserNumber = (float)targetUserNum;
			Float fcollectionCount = (float)collectionCount;
			String result = String.valueOf((fcollectionCount/ftargetUserNumber)*100);
			return result.replaceFirst("([0-9]*\\.\\d[0-9])\\d*","$1");
		}
		return "0";
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

				if(targetType == QuestionnaireTargetType.ORGANIZATION && rangeType!=QuestionnaireRangeType.ENTERPRISE){
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"Invalid parameters,rangeType = "+dto.getRangeType()+", targetType = "+targetType.getCode());
				}

				switch (rangeType){
					case COMMUNITY_AUTHENTICATED:
					case COMMUNITY_UNAUTHORIZED:
					case COMMUNITY_ALL:
					case ENTERPRISE:
					case USER:
						try {
							Long.valueOf(dto.getRange());
						}catch (Throwable e){
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
		//截止日期必须在当前日期之后

		if(new Timestamp(System.currentTimeMillis()).after(new Timestamp(questionnaire.getCutOffTime()))){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameters,cutOffTime = "+questionnaire.getCutOffTime()+" before now time");
		}

		//枚举支持匿名检查
//		QuestionnaireCommonStatus supportAnonymous = QuestionnaireCommonStatus.fromCode(questionnaire.getSupportAnonymous());
		if(questionnaire.getSupportAnonymous() == null){
			questionnaire.setSupportAnonymous(QuestionnaireCommonStatus.FALSE.getCode());
//			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//					"Invalid parameters,supportAnonymous = " +questionnaire.getSupportAnonymous());
		}

		//枚举支持分享检查
		if (targetType == QuestionnaireTargetType.USER) {
//			QuestionnaireCommonStatus supportShare = QuestionnaireCommonStatus.fromCode(questionnaire.getSupportShare());
			if (questionnaire.getSupportShare() == null) {
				questionnaire.setSupportShare(QuestionnaireCommonStatus.FALSE.getCode());
//				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//						"Invalid parameters,supportShare = " + questionnaire.getSupportShare());
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
	public void exportQuestionnaireResultDetail(GetQuestionnaireResultDetailCommand cmd, HttpServletResponse httpResponse) {
		//生成excel并，输出到httpResponse
		try(
			XSSFWorkbook wb = new XSSFWorkbook();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
		){
			createXSSFWorkbook(wb,cmd);
			wb.write(out);
			DownloadUtil.download(out, httpResponse);
		} catch (java.lang.Exception e) {
			LOGGER.error("export error, e = {}", e);
		}
	}


//	private XSSFRow createRow(XSSFSheet sheet,XSSFCellStyle style,List<QuestionnaireDTO> dtos, int rowNum) {
//
//	}

	private XSSFRow createRow(XSSFSheet sheet,XSSFWorkbook wb,QuestionnaireDTO dto, int rowNum) {
		List<String> contents = new ArrayList<String >();

		contents.add(stringService.getLocalizedString(QuestionnaireServiceErrorCode.SCOPE,QuestionnaireServiceErrorCode.UNKNOWN,"zh_CN","用户名"));
		if(QuestionnaireTargetType.fromCode(dto.getTargetType()) == QuestionnaireTargetType.USER) {
			contents.add(stringService.getLocalizedString(QuestionnaireServiceErrorCode.SCOPE, QuestionnaireServiceErrorCode.UNKNOWN, "zh_CN", "手机号"));
		}
		dto.getQuestions().forEach(r->{
			QuestionType type = QuestionType.fromCode(r.getQuestionType());
			if(type!=null){
				contents.add(type.getDesc1()+r.getQuestionName());
			}
		});
		for (int i = 0; i < contents.size(); i++) {
			sheet.setColumnWidth(i,contents.get(i).length()*1000);
		}
		XSSFCellStyle style = wb.createCellStyle();
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);//水平居中
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中

		XSSFFont font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short)16);
		cellStyle.setFont(font);
		return createRow(sheet,cellStyle,contents,rowNum);
	}

	private XSSFRow createRow(XSSFSheet sheet,XSSFCellStyle style,List<String> contents, int rowNum) {
		XSSFRow row1 = sheet.createRow(rowNum);
		row1.setRowStyle(style);
		int nextColumnNum = 0;
		style.setWrapText(true);
		for (String content : contents) {
			Cell cell = row1.createCell(nextColumnNum++);
			cell.setCellValue(content);
			cell.setCellStyle(style);
		}
		return row1;
	}

	/**
	 *
	 * by dengs 20170427
	 */
	private void createXSSFWorkbook(XSSFWorkbook wb, GetQuestionnaireResultDetailCommand cmd){
		//创建style
		XSSFCellStyle style = createStyle(wb);
		//创建sheet
		XSSFSheet sheet = createSheet(wb,style);

		cmd.setPageAnchor(null);
		cmd.setPageSize(100000);

		GetTargetQuestionnaireDetailResponse targetQuestionnaireDetail = getTargetQuestionnaireDetail(ConvertHelper.convert(cmd, GetTargetQuestionnaireDetailCommand.class));
		GetQuestionnaireResultDetailResponse detail = getQuestionnaireResultDetail(cmd);

		//头
		int startrow = 1;
		createHead(wb,sheet,targetQuestionnaireDetail.getQuestionnaire(),0);
		createRow(sheet,wb,targetQuestionnaireDetail.getQuestionnaire(),startrow++);
		String stringAnswer = stringService.getLocalizedString(QuestionnaireServiceErrorCode.SCOPE,QuestionnaireServiceErrorCode.UNKNOWN,"zh_CN","答案:");

		//内容
		for (QuestionnaireResultTargetDTO resultTargetDTO : detail.getQuestionnaireResultTargets()) {
			List<String> contents = new ArrayList<String>();
			contents.add(resultTargetDTO.getTargetName());
			if(QuestionnaireTargetType.fromCode(targetQuestionnaireDetail.getQuestionnaire().getTargetType()) == QuestionnaireTargetType.USER) {
				contents.add(resultTargetDTO.getTargetPhone());
			}
			List<QuestionnaireAnswer> answers = questionnaireAnswerProvider.listQuestionnaireAnswerByQuestionnaireId(cmd.getQuestionnaireId(),resultTargetDTO.getTargetType(),resultTargetDTO.getTargetId());
			long questionId = (answers==null || answers.size()==0)?Long.MAX_VALUE:answers.get(0).getQuestionId();
			String content = "";
			for (QuestionnaireAnswer answer : answers) {
				if(questionId != answer.getQuestionId().longValue()){
					contents.add(content);
					content = "";
					questionId = answer.getQuestionId().longValue();
				}
				QuestionType type = QuestionType.fromCode(answer.getQuestionType());
				switch (type) {
					case BLANK:
						content += stringAnswer + answer.getOptionContent()+"\n";
						break;
					case RADIO:
					case IMAGE_RADIO:
					case CHECKBOX:
					case IMAGE_CHECKBOX:
						content += stringAnswer + answer.getOptionName()+"\n";
						break;
					default:
						break;
				}
			}
			contents.add(content);
			createRow(sheet,style,contents,startrow++);
		}

	}

	private void createHead(XSSFWorkbook wb, XSSFSheet sheet, QuestionnaireDTO questionnaire, int i) {
		CellRangeAddress cra = new CellRangeAddress(i,i,0,questionnaire.getQuestions().size()+1);
		sheet.addMergedRegion(cra);
		Row row = sheet.createRow(i);
		Cell cell = row.createCell(0);
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);//水平居中
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中

		XSSFFont font = wb.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short)20);
		cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);

		cell.setCellValue(questionnaire.getQuestionnaireName()+stringService.getLocalizedString(QuestionnaireServiceErrorCode.SCOPE,QuestionnaireServiceErrorCode.UNKNOWN,"zh_CN","问卷调查结果"));

	}


	/**
	 * by dengs,创建cell style,20170502
	 */
	private XSSFCellStyle createStyle(XSSFWorkbook wb){
		XSSFCellStyle style = wb.createCellStyle();// 样式对象
		XSSFFont font2 = wb.createFont();
		font2.setFontName("仿宋_GB2312");

		style.setFont(font2);//选择需要用到的字体格式

//		style.setAlignment(CellStyle.ALIGN_CENTER);//水平居中
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中
//		style.setWrapText(true);

		return style;
	}

	/**
	 * by dengs,创建sheet，20170502
	 */
	private XSSFSheet createSheet(XSSFWorkbook wb,XSSFCellStyle style) {
		XSSFSheet sheet = wb.createSheet("sheet1");
		return sheet;
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

		response.setCollectionCount(questionnaire.getCollectionCount()==null?0:questionnaire.getCollectionCount());
		response.setTargetUserNum(questionnaire.getTargetUserNum()==null?0:questionnaire.getTargetUserNum());
		response.setPercentComplete(generatePercentComplete(questionnaire.getTargetUserNum(),questionnaire.getCollectionCount()));
		return response;
	}

	private QuestionnaireResultTargetDTO convertToTargetDTO(QuestionnaireAnswer answer) {
		QuestionnaireResultTargetDTO dto = ConvertHelper.convert(answer, QuestionnaireResultTargetDTO.class);
		dto.setSubmitTime(answer.getCreateTime().getTime());
		if(QuestionnaireCommonStatus.fromCode(answer.getAnonymousFlag()) == QuestionnaireCommonStatus.TRUE){
			if(QuestionnaireTargetType.fromCode(answer.getTargetType()) == QuestionnaireTargetType.ORGANIZATION) {
				dto.setTargetName(stringService.getLocalizedString(QuestionnaireServiceErrorCode.SCOPE,QuestionnaireServiceErrorCode.UNKNOWN3,"zh_CN","匿名企业"));
			}else{
				dto.setTargetName(stringService.getLocalizedString(QuestionnaireServiceErrorCode.SCOPE,QuestionnaireServiceErrorCode.UNKNOWN4,"zh_CN","匿名用户"));
			}
			dto.setTargetPhone(null);
		}
		return dto;
	}
	
	@Override
	public GetQuestionnaireResultSummaryResponse getQuestionnaireResultSummary(GetQuestionnaireResultSummaryCommand cmd) {
		List<QuestionnaireOption> questionnaireOptions = questionnaireOptionProvider.listOptionsByQuestionnaireId(cmd.getQuestionnaireId());
		return new GetQuestionnaireResultSummaryResponse(convertToQuestionnaireDTO(cmd.getQuestionnaireId(),questionnaireOptions, true, cmd.getPageSize()));
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
		checkListTargetQuestionnairesCommand(cmd);
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		Byte answeredFlagAnchor  = null;
		Long publishTimeAnchor  = null;
		if(cmd.getPageAnchor()!=null){
			answeredFlagAnchor = Byte.valueOf(String.valueOf(cmd.getPageAnchor()).substring(0,1));
			publishTimeAnchor = Long.valueOf(String.valueOf(cmd.getPageAnchor()).substring(1));
		}
		int questionnariePageSize = configurationProvider.getIntValue("questionnariePageSize",500);
		Integer namespaceId = cmd.getNamespaceId();
		if(namespaceId == null){
			namespaceId = UserContext.getCurrentNamespaceId();
		}
		Long userId = UserContext.current().getUser().getId();
		Long organizationId = cmd.getOrganizationId();
		List<QuestionnaireDTO> questionnaires = questionnaireProvider.listTargetQuestionnaireByOwner(namespaceId,cmd.getNowTime(),
				cmd.getCollectFlag(),cmd.getTargetType(),userId,organizationId,answeredFlagAnchor,publishTimeAnchor,questionnariePageSize);
		return new ListTargetQuestionnairesResponse(generateNextPageAnchor(cmd,questionnaires,pageSize,answeredFlagAnchor,publishTimeAnchor), questionnaires.stream().map(r->generatePosterUrl(r)).collect(Collectors.toList()));
	}

	//业务层分页
	private String generateNextPageAnchor(ListTargetQuestionnairesCommand cmd,List<QuestionnaireDTO> questionnaires, Integer pageSize, Byte answeredFlagAnchor, Long publishTimeAnchor) {
		List<QuestionnaireDTO> qds = new ArrayList<QuestionnaireDTO>();
		if(questionnaires.size()<pageSize){
			return null;
		}
		QuestionnaireDTO dto = null;
		if(answeredFlagAnchor == null && publishTimeAnchor == null){
			if(pageSize>=questionnaires.size()){
				pageSize = questionnaires.size();
			}else {
				dto = questionnaires.get(pageSize);
			}
			qds.addAll(questionnaires.subList(0,pageSize));
			questionnaires.clear();
			questionnaires.addAll(qds);
			if(dto == null) {
				return null;
			}
			String nextPageAnchor = String.valueOf(dto.getAnsweredFlag()) + String.valueOf(dto.getPublishTime());
			return nextPageAnchor;
		}

		for (int i = 0; i < questionnaires.size(); i++) {
			if(questionnaires.get(i).getAnsweredFlag().byteValue() == answeredFlagAnchor
					&& questionnaires.get(i).getPublishTime().longValue() == publishTimeAnchor){
				int anchor = pageSize+i;
				if(anchor>=questionnaires.size()){
					anchor = questionnaires.size();
				}else {
					dto = questionnaires.get(anchor);
				}
				qds.addAll(questionnaires.subList(i,anchor));
				questionnaires.clear();
				questionnaires.addAll(qds);
				if(dto == null) {
					return null;
				}
				String nextPageAnchor = String.valueOf(dto.getAnsweredFlag()) + String.valueOf(dto.getPublishTime());
				return nextPageAnchor;
			}
		}
		return null;
	}

	private void checkListTargetQuestionnairesCommand(ListTargetQuestionnairesCommand cmd) {
//		checkQuestionnaireTargetType(cmd.getTargetType());
		QuestionnaireTargetType enumTargetType = QuestionnaireTargetType.fromCode(cmd.getTargetType());
		if (enumTargetType == null){
			cmd.setTargetType(null);
		}

		checkQuestionnaireCollectFlag(cmd.getCollectFlag());
		if(cmd.getPageAnchor()!=null){
			String value = String.valueOf(cmd.getPageAnchor());
			if(value.length() != 14 || !(value.startsWith("0")|| value.startsWith("2"))){
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"Invalid parameters, invaild pageAnchor = " + cmd.getPageAnchor() +", pageAnchor should length = 14, start with 0 or 2");
			}
		}
		int questionnariePageSize = configurationProvider.getIntValue("questionnariePageSize",500);
		if(cmd.getPageSize()!=null && cmd.getPageSize()>questionnariePageSize){
			cmd.setPageSize(questionnariePageSize);
		}
		cmd.setNowTime(new Timestamp(System.currentTimeMillis()));
	}

	@Override
	public GetTargetQuestionnaireDetailResponse getTargetQuestionnaireDetail(GetTargetQuestionnaireDetailCommand cmd) {
		return new GetTargetQuestionnaireDetailResponse(getTargetQuestionnaireDetail(cmd.getQuestionnaireId(), cmd.getOrganizationId()));
	}
	
	private QuestionnaireDTO getTargetQuestionnaireDetail(Long questionnaireId, Long organizationId){
		List<QuestionnaireOption> questionnaireOptions = questionnaireOptionProvider.listOptionsByQuestionnaireId(questionnaireId);
		return convertToQuestionnaireDTO(questionnaireOptions, organizationId, UserContext.current().getUser().getId());
	}

	private QuestionnaireDTO convertToQuestionnaireDTO(List<QuestionnaireOption> questionnaireOptions, Long organizationId, Long userId) {
		List<QuestionnaireDTO> questionnaireDTOs = new ArrayList<>();
		// k1为问卷的id，k2为问题的id
		Map<Long, Map<Long, List<QuestionnaireOption>>> map = questionnaireOptions.parallelStream().collect(Collectors.groupingBy(QuestionnaireOption::getQuestionnaireId,Collectors.groupingBy(QuestionnaireOption::getQuestionId)));
		map.forEach((k1, v1)->{
			Questionnaire questionnaire = findQuestionnaireById(k1);
			List<QuestionnaireQuestionDTO> questionDTOs = new ArrayList<>();
			Long sTargetId = null;
			if(QuestionnaireTargetType.ORGANIZATION == QuestionnaireTargetType.fromCode(questionnaire.getTargetType())){
				sTargetId = organizationId;
			}else {
				sTargetId = userId;
			}
			final Long targetId = sTargetId;
			Byte[] answersFlag = new Byte[1];
			answersFlag[0] = QuestionnaireCommonStatus.FALSE.getCode();
			v1.forEach((k2, v2)->{
				QuestionnaireQuestion question = questionnaireQuestionProvider.findQuestionnaireQuestionById(k2);
				List<QuestionnaireAnswer> questionnaireAnswers = questionnaireAnswerProvider.listTargetQuestionnaireAnswerByQuestionId(question.getId(), questionnaire.getTargetType(), targetId);
				if(questionnaireAnswers!=null && questionnaireAnswers.size()>0){
					answersFlag[0] = QuestionnaireCommonStatus.TRUE.getCode();
				}
				List<QuestionnaireOptionDTO> optionDTOs =  v2.stream().map(o->convertToOptionDTO(o, questionnaireAnswers)).collect(Collectors.toList());
				questionDTOs.add(convertToQuestionDTO(question, optionDTOs));
			});
			// 经过map处理后顺序会乱，所以要重新排序下
			sortQuestions(questionDTOs);
			QuestionnaireDTO e = convertToQuestionnaireDTO(questionnaire, questionDTOs);
			e.setAnsweredFlag(answersFlag[0]);
			questionnaireDTOs.add(e);
		});
		QuestionnaireDTO dto =  questionnaireDTOs.get(0);
		generateShareUrl(dto);
		generatePosterUrl(dto);
		return dto;
	}

	private QuestionnaireDTO generatePosterUrl(QuestionnaireDTO dto) {
		if(dto!=null && dto.getPosterUri()!=null){
			dto.setPosterUrl(contentServerService.parserUri(dto.getPosterUri()));
		}
		return dto;
	}

	private void generateShareUrl(QuestionnaireDTO dto) {
		if(QuestionnaireCommonStatus.FALSE == QuestionnaireCommonStatus.fromCode(dto.getAnonymousFlag())){
			return;
		}
		try {
			String homeUrl = configurationProvider.getValue(ConfigConstants.HOME_URL,"https://core.zuolin.com");
			homeUrl = homeUrl.endsWith("/")?homeUrl.substring(0,homeUrl.length()-1):homeUrl;
			String contextUrl = configurationProvider.getValue(ConfigConstants.QUESTIONNAIRE_DETAIL_URL, "/questionnaire-survey/build/index.html#/question/%s#sign_suffix");
			String srcUrl = String.format(homeUrl+contextUrl, dto.getId());
//			String shareContext = String.format("/evh/wxauth/authReq?ns=%s&src_url=%s",dto.getNamespaceId(), URLEncoder.encode(srcUrl,"utf-8"));
//			dto.setShareUrl(homeUrl+shareContext);
			String shareUrl = srcUrl.replace("index.html","index.html?ns="+dto.getNamespaceId());
			dto.setShareUrl(shareUrl);
		} catch (Exception e) {
			LOGGER.warn("generate share url = "+dto);
		}

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
		Long organizationId = QuestionnaireTargetType.ORGANIZATION == QuestionnaireTargetType.fromCode(cmd.getTargetType())?cmd.getTargetId():null;
		return new CreateTargetQuestionnaireResponse(getTargetQuestionnaireDetail(cmd.getQuestionnaire().getId(), organizationId));
	}

	private void updateQuestionnaireCollectionCount(Long questionnaireId) {
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_QUESTIONNAIRE.getCode() + questionnaireId).enter(()->{
			Questionnaire questionnaire = questionnaireProvider.findQuestionnaireById(questionnaireId);
//			Integer count = questionnaireAnswerProvider.countQuestionnaireAnswerByQuestionnaireId(questionnaireId);
//			questionnaire.setCollectionCount(count);
			questionnaire.setCollectionCount((questionnaire.getCollectionCount()==null?0:questionnaire.getCollectionCount())+1);
			questionnaireProvider.updateQuestionnaire(questionnaire);
			return null;
		});
	}

	private void updateOptionCheckedCount(Long optionId) {
		coordinationProvider.getNamedLock(CoordinationLocks.UPDATE_QUESTIONNAIRE_OPTION.getCode() + optionId).enter(()->{
			QuestionnaireOption option = questionnaireOptionProvider.findQuestionnaireOptionById(optionId);
//			Integer count = questionnaireAnswerProvider.countQuestionnaireAnswerByOptionId(optionId);
//			option.setCheckedCount(count);
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

		if(questionnaire.getCutOffTime().before(new Timestamp(System.currentTimeMillis()))){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"提交失败，问卷已结束！" + questionnaire.getStatus());
		}

		if(targetType == QuestionnaireTargetType.ORGANIZATION){
			List<QuestionnaireRange> ranges = questionnaireRangeProvider.listQuestionnaireRangeByQuestionnaireId(cmd.getQuestionnaire().getId());
			boolean isvaildTargetId = false;
			for (QuestionnaireRange range : ranges) {
				if(range.getRange().equals(String.valueOf(cmd.getTargetId()))){
					isvaildTargetId = true;
					break;
				}
			}
			if(!isvaildTargetId){
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"unknown TargetId = " + cmd.getTargetId() + ",ranges = "+StringHelper.toJsonString(ranges));
			}
		}else if(targetType == QuestionnaireTargetType.USER){
			cmd.setTargetId(UserContext.current().getUser().getId());
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
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, 201,
							"其他企业管理员已提交问卷");
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

	@Override
	public ListUsersbyIdentifiersResponse listUsersbyIdentifiers(ListUsersbyIdentifiersCommand cmd) {
		Integer namespaceId = cmd.getNamespaceId();
		if(namespaceId ==null){
			namespaceId = UserContext.getCurrentNamespaceId();
		}
		if(cmd.getIdentifiers()==null){
			return new ListUsersbyIdentifiersResponse();
		}
		List<UserIdentifier> userIdentifiers = userProvider.listClaimedIdentifiersByTokens(namespaceId, cmd.getIdentifiers());
		List<User> users = userProvider.listUserByIds(namespaceId, userIdentifiers.stream().map(r -> Long.valueOf(r.getOwnerUid())).collect(Collectors.toList()));
		Map<String,User> userMaps = generateUserMaps(userIdentifiers,users);

		return new ListUsersbyIdentifiersResponse(sortQuestionnaireUserDTOs(cmd.getIdentifiers(),userMaps));
	}

	private List<QuestionnaireUserDTO> sortQuestionnaireUserDTOs(List<String> identifiers, Map<String, User> userMaps) {
		List<QuestionnaireUserDTO> dtoRights = new ArrayList<>();
		List<QuestionnaireUserDTO> dtoUndefineds = new ArrayList<>();
		for (String sidentifier : identifiers) {
			QuestionnaireUserDTO dto = new QuestionnaireUserDTO();
			dto.setIdentifierToken(sidentifier);
			User user = userMaps.get(sidentifier);
			if(user!=null) {
				dto.setId(user.getId());
				dto.setAccountName(user.getNickName());
				dto.setVerifyStatus(QuestionnaireCommonStatus.TRUE.getCode());
				dtoRights.add(dto);
				continue;
			}
			if(isRightFormatSidentifier(sidentifier)){
				dto.setAccountName("手机号未注册");
			}else{
				dto.setAccountName("手机号有误");
			}
			dto.setVerifyStatus(QuestionnaireCommonStatus.FALSE.getCode());
			dtoUndefineds.add(dto);
		}
		dtoRights.addAll(dtoUndefineds);
		return dtoRights;
	}

	private Map<String,User> generateUserMaps(List<UserIdentifier> userIdentifiers, List<User> users) {
		Map<String,User> maps = new HashMap<>();
		for (UserIdentifier userIdentifier : userIdentifiers) {
			for (User user : users) {
				if(userIdentifier.getOwnerUid().longValue() == user.getId()){
					maps.put(userIdentifier.getIdentifierToken(),user);
				}
			}
		}
		return maps;
	}

	private static Pattern formatSidentifier = Pattern.compile("^\\d{11}$");
	private boolean isRightFormatSidentifier(String sidentifier) {
		Matcher matcher = formatSidentifier.matcher(sidentifier);
		if(matcher.find()){
			return true;
		}
		return false;
	}

	@Override
	public GetTargetQuestionnaireDetailResponse getAnsweredQuestionnaireDetail(GetTargetQuestionnaireDetailCommand cmd) {
		List<QuestionnaireOption> questionnaireOptions = questionnaireOptionProvider.listOptionsByQuestionnaireId(cmd.getQuestionnaireId());
		if(QuestionnaireTargetType.ORGANIZATION == QuestionnaireTargetType.fromCode(cmd.getTargetType())){
			return new GetTargetQuestionnaireDetailResponse(convertToQuestionnaireDTO(questionnaireOptions,cmd.getTargetId(),null));
		}
		return new GetTargetQuestionnaireDetailResponse(convertToQuestionnaireDTO(questionnaireOptions,null,cmd.getTargetId()));

	}

	@Override
	public void reScopeQuesionnaireRanges(ReScopeQuesionnaireRangesCommand cmd) {
		questionnaireAsynSendMessageService.sendAllTargetMessageAndSaveTargetScope(cmd.getQuesionnaireId());
	}

	@Override
	public void reSendQuesionnaireMessages() {
		questionnaireAsynSendMessageService.sendUnAnsweredTargetMessage();
	}

	@Override
	public List<OrganizationAndDetailDTO> listRangeOrgs(ListRangeOrgsCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		Questionnaire quest = questionnaireProvider.findQuestionnaireById(cmd.getQuestionnaireId());
		List<OrganizationAndDetailDTO> resp = new ArrayList<>();
		if(QuestionnaireTargetType.ORGANIZATION.equals(quest.getTargetType())){
			List<QuestionnaireRange> ranges = questionnaireRangeProvider.listQuestionnaireRangeByQuestionnaireId(cmd.getQuestionnaireId());
			for (QuestionnaireRange range : ranges){
				OrganizationAndDetailDTO dto = organizationProvider.getOrganizationAndDetailByorgIdAndNameId(range.getRid(),namespaceId);
				resp.add(dto);
			}
		}
		return resp;
	}
}