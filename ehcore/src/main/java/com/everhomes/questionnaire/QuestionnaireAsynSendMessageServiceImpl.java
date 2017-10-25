// @formatter:off
package com.everhomes.questionnaire;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.community.CommunityService;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.ActivationFlag;
import com.everhomes.rest.common.OfficialActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.community.admin.CommunityUserAddressResponse;
import com.everhomes.rest.community.admin.CommunityUserDto;
import com.everhomes.rest.community.admin.CommunityUserResponse;
import com.everhomes.rest.community.admin.ListCommunityUsersCommand;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.questionnaire.QuestionnaireRangeType;
import com.everhomes.rest.questionnaire.QuestionnaireServiceErrorCode;
import com.everhomes.rest.questionnaire.QuestionnaireTargetType;
import com.everhomes.rest.user.*;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserActivity;
import com.everhomes.user.UserContext;
import com.everhomes.userOrganization.UserOrganizations;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Component
public class QuestionnaireAsynSendMessageServiceImpl implements QuestionnaireAsynSendMessageService{
	private static final Logger LOGGER= LoggerFactory.getLogger(QuestionnaireAsynSendMessageServiceImpl.class);

	@Autowired
	protected ContentServerService contentServerService;

	@Autowired
	private QuestionnaireProvider questionnaireProvider;

	@Autowired
	private MessagingService messagingService;

	@Autowired
	private LocaleStringService stringService;

	@Autowired
	private NamespaceProvider namespaceProvider;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private CommunityService communityService;

	@Autowired
	private RolePrivilegeService rolePrivilegeService;

	@Autowired
	private QuestionnaireRangeProvider questionnaireRangeProvider;

	@Autowired
	private QuestionnaireAnswerProvider questionnaireAnswerProvider;
	@Autowired
	private OrganizationProvider organizationProvider;

	@Override
	public void sendAllTargetMessageAndSaveTargetScope(Long questionnaireId) {
		Questionnaire questionnaire = questionnaireProvider.findQuestionnaireById(questionnaireId);
		if(questionnaire == null){
			LOGGER.info("unknow questionnaireId = " +questionnaireId);
			return ;
		}
		//设置一个上下文，调用其他接口要使用
		UserContext.setCurrentNamespaceId(questionnaire.getNamespaceId());
		UserContext.current().setUser(new User());
		UserContext.current().getUser().setId(questionnaire.getCreatorUid());
		//计算推送消息的用户范围
		Set<String> userLevelRanges = calculateQuesionnaireRange(questionnaire);
		//保存文件的范围和用户数量
		questionnaire.setTargetUserNum(userLevelRanges.size());
		questionnaire.setUserScope(StringHelper.toJsonString(userLevelRanges));
		//发送消息
		questionnaire.setScopeSentMessageUsers(StringHelper.toJsonString(sendMessage(questionnaire)));
		questionnaireProvider.updateQuestionnaire(questionnaire);
	}

	@Override
	public void sendUnAnsweredTargetMessage() {
		LOGGER.info("start sendUnAnsweredTargetMessage at ", System.currentTimeMillis());
		int intervalTime = configurationProvider.getIntValue(ConfigConstants.QUESTIONNAIRE_REMIND_TIME_INTERVAL,24);
		Timestamp remindTime = new Timestamp(System.currentTimeMillis()+intervalTime*3600*1000);
		List<Questionnaire> questionnaires = questionnaireProvider.listApproachCutoffTimeQuestionnaire(remindTime);
		Set<String> userLevelRanges = null;
		for (Questionnaire questionnaire : questionnaires) {
			String originalUseScope = questionnaire.getUserScope();
			List<QuestionnaireAnswer> answers = questionnaireAnswerProvider.listQuestionnaireAnswerByQuestionnaireId(questionnaire.getId(), questionnaire.getTargetType());
			if(QuestionnaireTargetType.fromCode(questionnaire.getTargetType()) == QuestionnaireTargetType.ORGANIZATION){
				List<QuestionnaireRange> originalRanges = questionnaireRangeProvider.listQuestionnaireRangeByQuestionnaireId(questionnaire.getId());
				userLevelRanges = calculateUnAnsweredQuesionnaireRange(originalRanges,answers);
			}else{
				userLevelRanges = calculateUnAnsweredQuesionnaireRange(questionnaire,answers);
			}
			questionnaire.setUserScope(StringHelper.toJsonString(userLevelRanges));
			questionnaire.setScopeResentMessageUsers(StringHelper.toJsonString(sendMessage(questionnaire)));
			questionnaire.setUserScope(originalUseScope);
			questionnaireProvider.updateQuestionnaire(questionnaire);
		}

	}

	private Set<String> calculateUnAnsweredQuesionnaireRange(List<QuestionnaireRange> originalRanges, List<QuestionnaireAnswer> answers) {
		Set<String> userLevelRanges = new HashSet<String>();
		for (QuestionnaireRange originalRange : originalRanges) {
			boolean answered = false;
			for (QuestionnaireAnswer answer : answers) {
				if(QuestionnaireTargetType.fromCode(answer.getTargetType()) == QuestionnaireTargetType.ORGANIZATION
						&& originalRange.getRange().equals(String.valueOf(answer.getTargetId()))){
					answered = true;
					break;
				}
			}
			if (!answered){
				userLevelRanges.addAll(getOrganizationAdministrators(originalRange.getRange()));
			}
		}
		return userLevelRanges;
	}
	private Set<String> calculateUnAnsweredQuesionnaireRange(Questionnaire questionnaire, List<QuestionnaireAnswer> answers) {
		List<String> ranges = JSONObject.parseObject(questionnaire.getUserScope(),new TypeReference<List<String>>(){});
		Set<String> userLevelRanges = new HashSet<String>();
		ranges.forEach(range->{
			boolean answered = false;
			for (QuestionnaireAnswer answer : answers) {
				if(answer.getTargetId().longValue() == Long.valueOf(range)){
					answered = true;
					break;
				}
			}
			if(!answered){
				userLevelRanges.add(range);
			}
		});
		return userLevelRanges;
	}


	private List<String> sendMessage(Questionnaire questionnaire){
		List<String> sendedranges = new ArrayList<>();
		try {
			LOGGER.info("send message to:" + questionnaire.getUserScope());
			List<String> ranges = JSONObject.parseObject(questionnaire.getUserScope(), new TypeReference<List<String>>() {
			});

			//部分不用循环创建的对象
			Namespace namespace = namespaceProvider.findNamespaceById(questionnaire.getNamespaceId());
			String string1 = stringService.getLocalizedString(QuestionnaireServiceErrorCode.SCOPE, QuestionnaireServiceErrorCode.UNKNOWN1, "zh_CN", "邀请您参与《");
			String string2 = stringService.getLocalizedString(QuestionnaireServiceErrorCode.SCOPE, QuestionnaireServiceErrorCode.UNKNOWN2, "zh_CN", "》问卷调查。");
			String url = String.format(configurationProvider.getValue(ConfigConstants.QUESTIONNAIRE_DETAIL_URL, "https://www.zuolin.com/mobile/static/coming_soon/index.html"), questionnaire.getId());

			MessageChannel channel2 = new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId()));

			// 组装路由
			OfficialActionData actionData = new OfficialActionData();
			actionData.setUrl(url);

			String uri = RouterBuilder.build(Router.BROWSER_I, actionData);
			RouterMetaObject metaObject = new RouterMetaObject();
			metaObject.setUrl(uri);

			Map<String, String> meta = new HashMap<String, String>();
			meta.put(MessageMetaConstant.MESSAGE_SUBJECT, stringService.getLocalizedString(QuestionnaireServiceErrorCode.SCOPE, QuestionnaireServiceErrorCode.UNKNOWN, "zh_CN", "问卷调查邀请函"));
			meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.MESSAGE_ROUTER.getCode());
			meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));

			String body = new StringBuffer().append(namespace.getName()).append(string1).append(questionnaire.getQuestionnaireName()).append(string2).append(questionnaire.getDescription()).toString();
			MessageDTO messageDto = new MessageDTO();
			ranges.forEach(range -> {
				messageDto.setAppId(AppConstants.APPID_MESSAGING);
				messageDto.setSenderUid(User.SYSTEM_UID);
				messageDto.setChannels(
						new MessageChannel(MessageChannelType.USER.getCode(), range),
						channel2
				);

				messageDto.setBodyType(MessageBodyType.TEXT.getCode());
				messageDto.setBody(body);
				messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
				messageDto.setMeta(meta);

				messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
						range, messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
				sendedranges.add(range);
			});
		}finally {
			return sendedranges;
		}
	}

	private Set<String> calculateQuesionnaireRange(Questionnaire questionnaireDTO) {
		List<QuestionnaireRange> originalRanges = questionnaireRangeProvider.listQuestionnaireRangeByQuestionnaireId(questionnaireDTO.getId());
		Set<String> userLevelRanges = new HashSet<String>();
		QuestionnaireTargetType targetType = QuestionnaireTargetType.fromCode(questionnaireDTO.getTargetType());
		for (QuestionnaireRange originalRange : originalRanges) {
			QuestionnaireRangeType enumRangeType = QuestionnaireRangeType.fromCode(originalRange.getRangeType());
			if(targetType == QuestionnaireTargetType.USER){
				switch (enumRangeType){
					case USER:
						userLevelRanges.add(originalRange.getRange());
						break;
					case BUILDING:
						ListEnterprisesCommand cmd = new ListEnterprisesCommand();
						cmd.setCommunityId(originalRange.getCommunityId());
						cmd.setNamespaceId(originalRange.getNamespaceId());
						cmd.setBuildingName(originalRange.getRange());
						cmd.setPageSize(1000000);
						ListEnterprisesCommandResponse rs = organizationService.listNewEnterprises(cmd);
						rs.getDtos().stream().forEach(r->{
							userLevelRanges.addAll(getEnterpriseUsers(String.valueOf(r.getOrganizationId())));
						});
						break;
					case ENTERPRISE:
						userLevelRanges.addAll(getEnterpriseUsers(originalRange.getRange()));
						break;
					case COMMUNITY_ALL:
						userLevelRanges.addAll(getCommunityUsers(originalRange.getRange(),0));
						break;
					case COMMUNITY_UNAUTHORIZED:
						userLevelRanges.addAll(getCommunityUsers(originalRange.getRange(),1));
						break;
					case COMMUNITY_AUTHENTICATED:
						userLevelRanges.addAll(getCommunityUsers(originalRange.getRange(),2));
						break;
				}
			}else if (targetType == QuestionnaireTargetType.ORGANIZATION){
				if (QuestionnaireRangeType.ENTERPRISE == enumRangeType){
					userLevelRanges.addAll(getOrganizationAdministrators(originalRange.getRange()));
				}else{
					throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
							"status error, targetType = organization, unknown rangeType = "+originalRange.getRangeType());
				}
			}
		}
		return userLevelRanges;
	}

	private List<String> getOrganizationAdministrators(String range) {
		ListServiceModuleAdministratorsCommand cmd = new ListServiceModuleAdministratorsCommand();
		cmd.setOrganizationId(Long.valueOf(range));
		cmd.setActivationFlag(ActivationFlag.YES.getCode());
		List<OrganizationContactDTO> dtos = rolePrivilegeService.listOrganizationAdministrators(cmd);
		return dtos.stream().map(r->String.valueOf(r.getTargetId())).collect(Collectors.toList());
	}

	private  List<String> getCommunityUsers(String range, Integer isAuthor) {
		ListCommunityUsersCommand cmd = new ListCommunityUsersCommand();
		cmd.setCommunityId(Long.valueOf(range));
		cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
		cmd.setPageSize(10000000);
		cmd.setIsAuth(isAuthor);
		//这里只需要查询userID，其他的附加查询不需要，所以自己搞了个查询。
		List<UserOrganizations> userOrganizations = listUserCommunities(cmd);
		return userOrganizations.stream().map(r->String.valueOf(r.getUserId())).collect(Collectors.toList());
	}

	private List<String> getEnterpriseUsers(String range) {
		ListOrganizationContactCommand cmd = new ListOrganizationContactCommand();
		cmd.setOrganizationId( Long.valueOf(range));
		cmd.setPageSize(10000000);//这里需要查询全部的人员
		ListOrganizationMemberCommandResponse response = organizationService.listOrganizationPersonnels(cmd, false);
		if(response == null || response.getMembers() == null){
			return new ArrayList<>();
		}
		return response.getMembers().stream().map(r->String.valueOf(r.getTargetId())).collect(Collectors.toList());
	}

	private List<UserOrganizations> listUserCommunities(
			ListCommunityUsersCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		return organizationProvider.listUserOrganizations(locator, pageSize, new ListingQueryBuilderCallback() {
			@Override
			public SelectQuery<? extends Record> buildCondition(ListingLocator locator, SelectQuery<? extends Record> query) {
				query.addConditions(Tables.EH_USERS.NAMESPACE_ID.eq(namespaceId));
				query.addConditions(Tables.EH_USERS.STATUS.eq(UserStatus.ACTIVE.getCode()));
				if(null != cmd.getOrganizationId()){
					query.addConditions(Tables.EH_USER_ORGANIZATIONS.ORGANIZATION_ID.eq(cmd.getOrganizationId()));
				}


				if(UserSourceType.WEIXIN == UserSourceType.fromCode(cmd.getUserSourceType())){
					query.addConditions(Tables.EH_USERS.NAMESPACE_USER_TYPE.eq(NamespaceUserType.WX.getCode()));
				}else if(UserSourceType.APP == UserSourceType.fromCode(cmd.getUserSourceType())){
					query.addConditions(Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.isNotNull());
				}

				if(null != cmd.getCommunityId()){
					query.addConditions(Tables.EH_ORGANIZATION_COMMUNITY_REQUESTS.COMMUNITY_ID.eq(cmd.getCommunityId()));
				}

				if(!StringUtils.isEmpty(cmd.getKeywords())){
					Condition cond = Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(cmd.getKeywords());
					cond = cond.or(Tables.EH_USERS.NICK_NAME.like("%" + cmd.getKeywords() + "%"));
					query.addConditions(cond);
				}

				if(null != UserGender.fromCode(cmd.getGender())){
					query.addConditions(Tables.EH_USERS.GENDER.eq(cmd.getGender()));
				}

				if(null != cmd.getExecutiveFlag()){
					query.addConditions(Tables.EH_USERS.EXECUTIVE_TAG.eq(cmd.getExecutiveFlag()));
				}

				if(AuthFlag.AUTHENTICATED == AuthFlag.fromCode(cmd.getIsAuth())){
					query.addConditions(Tables.EH_USER_ORGANIZATIONS.STATUS.eq(UserOrganizationStatus.ACTIVE.getCode()));
				}else if(AuthFlag.PENDING_AUTHENTICATION == AuthFlag.fromCode(cmd.getIsAuth())){
					query.addConditions(Tables.EH_USER_ORGANIZATIONS.STATUS.eq(UserOrganizationStatus.WAITING_FOR_APPROVAL.getCode()));
				}else if(AuthFlag.UNAUTHORIZED == AuthFlag.fromCode(cmd.getIsAuth())){
					query.addConditions(Tables.EH_USER_ORGANIZATIONS.STATUS.isNull());
				}

				query.addGroupBy(Tables.EH_USERS.ID);

				Condition cond = Tables.EH_USERS.ID.isNotNull();
				if(AuthFlag.UNAUTHORIZED == AuthFlag.fromCode(cmd.getIsAuth())){
					cond = cond.and(" `eh_users`.`id` not in (select user_id from eh_user_organizations where status = " + UserOrganizationStatus.ACTIVE.getCode() + " or status = " + UserOrganizationStatus.WAITING_FOR_APPROVAL.getCode() + ")");
				}else if(AuthFlag.PENDING_AUTHENTICATION == AuthFlag.fromCode(cmd.getIsAuth())){
					cond = cond.and(" `eh_users`.`id` not in (select user_id from eh_user_organizations where status = " + UserOrganizationStatus.ACTIVE.getCode() + ")");
				}
				query.addHaving(cond);

				return query;
			}
		});
	}
}