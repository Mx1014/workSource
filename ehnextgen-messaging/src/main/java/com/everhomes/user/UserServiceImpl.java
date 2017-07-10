// @formatter:off
package com.everhomes.user;

import com.everhomes.acl.AclProvider;
import com.everhomes.acl.PortalRoleResolver;
import com.everhomes.acl.Role;
import com.everhomes.address.AddressService;
import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.border.Border;
import com.everhomes.border.BorderConnection;
import com.everhomes.border.BorderConnectionProvider;
import com.everhomes.border.BorderProvider;
import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusMessageDispatcher;
import com.everhomes.bus.LocalBusMessageHandler;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.business.BusinessService;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.controller.WebRequestInterceptor;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.device.Device;
import com.everhomes.device.DeviceProvider;
import com.everhomes.enterprise.Enterprise;
import com.everhomes.enterprise.EnterpriseContactService;
import com.everhomes.enterprise.EnterpriseProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.FamilyProvider;
import com.everhomes.family.FamilyService;
import com.everhomes.forum.ForumService;
import com.everhomes.group.Group;
import com.everhomes.group.GroupProvider;
import com.everhomes.launchpad.LaunchPadService;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.mail.MailHandler;
import com.everhomes.messaging.MessagingKickoffService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.messaging.PusherService;
import com.everhomes.messaging.UserMessageRoutingHandler;
import com.everhomes.msgbox.Message;
import com.everhomes.msgbox.MessageBoxProvider;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceDetail;
import com.everhomes.namespace.NamespaceResource;
import com.everhomes.namespace.NamespaceResourceProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.news.NewsService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.point.UserPointService;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.address.ClaimAddressCommand;
import com.everhomes.rest.address.ClaimedAddressInfo;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.business.ShopDTO;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.energy.util.ParamErrorCodes;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.family.FamilyMemberFullDTO;
import com.everhomes.rest.family.ListAllFamilyMembersCommandResponse;
import com.everhomes.rest.family.admin.ListAllFamilyMembersAdminCommand;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.rest.launchpad.LaunchPadItemDTO;
import com.everhomes.rest.link.RichLinkDTO;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.namespace.NamespaceCommunityType;
import com.everhomes.rest.namespace.NamespaceResourceType;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.point.AddUserPointCommand;
import com.everhomes.rest.point.GetUserTreasureCommand;
import com.everhomes.rest.point.GetUserTreasureResponse;
import com.everhomes.rest.point.PointType;
import com.everhomes.rest.search.SearchContentType;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.ui.organization.SetCurrentCommunityForSceneCommand;
import com.everhomes.rest.ui.user.*;
import com.everhomes.rest.user.*;
import com.everhomes.rest.user.admin.*;
import com.everhomes.server.schema.tables.pojos.EhUserIdentifiers;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsProvider;
import com.everhomes.util.*;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.geo.GeoHashUtils;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Size;
import javax.validation.metadata.ConstraintDescriptor;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.everhomes.server.schema.Tables.EH_USER_IDENTIFIERS;
import static com.everhomes.util.RuntimeErrorException.errorWith;

/**
 * 
 * Implement business logic for user management
 * 
 * @author Kelven Yang
 *
 */
@Component
public class UserServiceImpl implements UserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	private static final String SIGN_APP_KEY = "sign.appKey";
	private static final String EXPIRE_TIME="invitation.expiretime";
	private static final String YZX_VCODE_TEMPLATE_ID = "yzx.vcode.templateid";
	private static final String MW_VCODE_TEMPLATE_CONTENT = "mw.vcode.template.content";
	private static final String VCODE_SEND_TYPE = "sms.handler.type";

	private static final String X_EVERHOMES_DEVICE = "x-everhomes-device";

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
	private DbProvider dbProvider;

	@Autowired
	private BigCollectionProvider bigCollectionProvider;

	@Autowired
	private UserProvider userProvider;

	@Autowired
	private SmsProvider smsProvider;

	@Autowired
	private CategoryProvider categoryProvider;

	@Autowired
	private CommunityProvider communityProvider;

	@Autowired
	private CoordinationProvider coordinationProvider;

	@Autowired
	private AclProvider aclProvider;

	@Autowired
	private RegionProvider regionProvider;

	@Autowired
	private ContentServerService contentServerService;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private FamilyService familyService;

	@Autowired
	private FamilyProvider familyProvider;

	@Autowired
	private AddressService addressService;

	@Autowired
	private MessageBoxProvider messageBoxProvider;

	@Autowired
	private UserPointService userPointService;

	@Autowired
	private EnterpriseContactService enterpriseContactService;

	@Autowired
	private AppProvider appProvider;

	@Autowired
	private UserActivityProvider userActivityProvider;

	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired 
	private EnterpriseProvider enterpriseProvider;

	@Autowired
	private NamespaceResourceProvider namespaceResourceProvider;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private MessagingService messagingService;

	@Autowired
	private LocaleTemplateService localeTemplateService;

	@Autowired
	private DeviceProvider deviceProvider;

	@Autowired
	private BorderConnectionProvider borderConnectionProvider;

	@Autowired
	private PusherService pusherService;

	@Autowired
	private LocalBus localBus;

	@Autowired
	private BorderProvider borderProvider;

	@Autowired
	private LocaleStringService localeStringService;

	@Autowired
	private PropertyMgrService propertyMgrService;

	@Autowired
	private MessagingKickoffService kickoffService;

	@Autowired
	private UserImpersonationProvider userImpersonationProvider;

	@Autowired
	private NewsService newsService;

	@Autowired
	private ForumService forumService;

	@Autowired
	private ConfigurationProvider configProvider;
	
    @Autowired
    private LaunchPadService launchPadService;
    
    @Autowired
    private BusinessService businessService;

    @Autowired
    private GroupProvider groupProvider;

    @Autowired
    private UserIdentifierLogProvider userIdentifierLogProvider;

    @Autowired
    private UserAppealLogProvider userAppealLogProvider;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

	private static final String DEVICE_KEY = "device_login";

	@PostConstruct
	public void setup() {
		localBus.subscribe("border.close", LocalBusMessageDispatcher.getDispatcher(this));
	}

	@Override
	public SignupToken signup(SignupCommand cmd, HttpServletRequest request) {
		final IdentifierType identifierType = IdentifierType.fromString(cmd.getType());
		if(identifierType == null) {
			LOGGER.error("Invalid or unsupported identifier type, cmd=" + cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid or unsupported identifier type");
		}

		final String identifierToken = cmd.getToken();
		// 客户端有的时候会传一个非手机号的token过来，此时打印日志以便定位，不改变原来的流程
		if(identifierToken == null || identifierToken.length() > 20) {
			LOGGER.warn("Identifier token is invalid, cmd=" + cmd);
		}

		boolean overrideExisting = false;
		if(cmd.getIfExistsThenOverride() != null && cmd.getIfExistsThenOverride().intValue() != 0) {
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("The ifExistsThenOverride flag is true, cmd=" + cmd);
			}
			overrideExisting = true;
		}

		// UserIdentifier existingClaimedIdentifier = this.userProvider.findClaimedIdentifierByToken(identifierToken);
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		UserIdentifier existingClaimedIdentifier = this.userProvider.findClaimedIdentifierByToken(namespaceId, identifierToken);
		if(existingClaimedIdentifier != null && !overrideExisting) {
			LOGGER.error("User identifier token has already been claimed, cmd=" + cmd + ", identifierId=" + existingClaimedIdentifier.getId() 
					+ ", ownerUid=" + existingClaimedIdentifier.getOwnerUid() + ", identifierType=" + existingClaimedIdentifier.getIdentifierType() 
					+ ", identifierToken=" + existingClaimedIdentifier.getIdentifierToken());
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_IDENTIFIER_ALREADY_CLAIMED, 
					"User identifier token has already been claimed");
		}

		// 在创建用户信息前就检查此次短信发送的合法性
        this.verifySmsTimes("signup", identifierToken, request.getHeader(X_EVERHOMES_DEVICE));

        String ip = request.getHeader("x-forwarded-for");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("Proxy-Client-IP"); 
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("WL-Proxy-Client-IP"); 
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("HTTP_CLIENT_IP"); 
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getRemoteAddr(); 
		} 

		String regIp = ip;

		UserIdentifier identifier = this.dbProvider.execute((TransactionStatus status) -> {
			User user = new User();
			user.setStatus(UserStatus.INACTIVE.getCode());
			user.setRegIp(regIp);
			user.setRegChannelId(cmd.getChannel_id());
			user.setNamespaceId(cmd.getNamespaceId());
			userProvider.createUser(user);

			UserIdentifier newIdentifier = new UserIdentifier();
			newIdentifier.setOwnerUid(user.getId());
			newIdentifier.setIdentifierType(identifierType.getCode());
			newIdentifier.setIdentifierToken(identifierToken);
			newIdentifier.setNamespaceId(namespaceId);

			String verificationCode = RandomGenerator.getRandomDigitalString(6);
			newIdentifier.setClaimStatus(IdentifierClaimStatus.VERIFYING.getCode());
			newIdentifier.setVerificationCode(verificationCode);
			newIdentifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			newIdentifier.setRegionCode(cmd.getRegionCode());
			userProvider.createIdentifier(newIdentifier);

			//            String templateId = configurationProvider.getValue(YZX_VCODE_TEMPLATE_ID, "");
            //            smsProvider.sendSms(identifierToken, verificationCode,templateId);
            return newIdentifier;
        });

        LOGGER.info("Send verfication code: " + identifier.getVerificationCode() + " for new user: " + identifierToken);
        sendVerificationCodeSms(identifier.getNamespaceId(), this.getYzxRegionPhoneNumber(identifierToken, cmd.getRegionCode()), identifier.getVerificationCode());

        SignupToken signupToken = new SignupToken(identifier.getOwnerUid(), identifierType, identifierToken);
		if(StringUtils.isEmpty(signupToken.getIdentifierToken())) {
			LOGGER.error("Signup token should not be empty, signupToken" + signupToken + ", cmd=" + cmd);
		} else {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("User signup succeed, signupToken" + signupToken + ", cmd=" + cmd);
			}
		}

		return signupToken;
	}

    /**
     * 校验短信发送频率
     */
    private void verifySmsTimes(String smsAction, String identifierToken, String deviceId) {
    	//added by janson 消息序列化不正确的根本原因在于这里 03-31
        RedisTemplate template = bigCollectionProvider.getMapAccessor("sendSmsTimes", "").getTemplate(new StringRedisSerializer());
        // 设置value的序列化，要不然下面的increment方法会报错 
//        template.setValueSerializer(new StringRedisSerializer()); 坚决不用这种写法，会导致消息模块报错！因为这个是设置全局的 template
        ValueOperations op = template.opsForValue();

        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Integer smsMinDuration = Integer.parseInt(configProvider.getValue(namespaceId,"sms.verify.minDuration.seconds", "60"));
        Integer smsTimesDeviceForAnHour = Integer.parseInt(configProvider.getValue(namespaceId, "sms.verify.device.timesForAnHour", "10"));
        Integer smsTimesDeviceForADay = Integer.parseInt(configProvider.getValue(namespaceId, "sms.verify.device.timesForADay", "20"));
        Integer smsTimesPhoneForAnHour = Integer.parseInt(configProvider.getValue(namespaceId, "sms.verify.phone.timesForAnHour", "3"));
        Integer smsTimesPhoneForADay = Integer.parseInt(configProvider.getValue(namespaceId, "sms.verify.phone.timesForADay", "5"));

        // 老版本的客户端没有deviceId
        boolean hasDeviceId = StringUtils.isNotBlank(deviceId);

        // 每个手机号每天发送次数≤5
        String phoneDayKey = getCacheKey("sendSmsTimes", smsAction, SmsVerify.Type.PHONE.name(), SmsVerify.Duration.DAY.name(), identifierToken);
        Object times = op.get(phoneDayKey);

        if(times == null) {
            // 设置今天晚上23:59:59过期
            LocalDate tomorrowStart = LocalDate.now().plusDays(1);
            long seconds = (java.sql.Date.valueOf(tomorrowStart).getTime() - System.currentTimeMillis()) / 1000;
            op.set(phoneDayKey, String.valueOf(0), seconds, TimeUnit.SECONDS);
        } else {
            Integer t = Integer.valueOf((String) times);
            if (t >= smsTimesPhoneForADay) {
                LOGGER.error("Verification code request is too frequent, please try again after 24 hours. phone={}, times={}", identifierToken, t);
                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_SMS_TOO_FREQUENT_DAY,
                        "Verification code request is too frequent, please try again after 24 hours");
            }
        }

        // 每个手机设备每天发送次数≤20
        String deviceDayKey = getCacheKey("sendSmsTimes", smsAction, SmsVerify.Type.DEVICE.name(), SmsVerify.Duration.DAY.name(), deviceId);
        if (hasDeviceId) {
            times = op.get(deviceDayKey);
            if(times == null) {
                // 设置今天晚上23:59:59过期
                LocalDate tomorrowStart = LocalDate.now().plusDays(1);
                long seconds = (java.sql.Date.valueOf(tomorrowStart).getTime() - System.currentTimeMillis()) / 1000;
                op.set(deviceDayKey, String.valueOf(0), seconds, TimeUnit.SECONDS);
            } else {
                Integer t = Integer.valueOf((String) times);
                if (t >= smsTimesDeviceForADay) {
                    LOGGER.error("Verification code request is too frequent, please try again after 24 hours. deviceId={}, times={}", deviceId, t);
                    throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_SMS_TOO_FREQUENT_DAY,
                            "Verification code request is too frequent, please try again after 24 hours");
                }
            }
        }

        // 每个手机号每小时发送次数≤3
        String phoneHourKey = getCacheKey("sendSmsTimes", smsAction, SmsVerify.Type.PHONE.name(), SmsVerify.Duration.HOUR.name(), identifierToken);
        times = op.get(phoneHourKey);

        if(times == null) {
            op.set(phoneHourKey, String.valueOf(0), 1, TimeUnit.HOURS);
        } else {
            Integer t = Integer.valueOf((String) times);
            if (t >= smsTimesPhoneForAnHour) {
                LOGGER.error("Verification code request is too frequent, please 1 hour to try again. phone={}, times={}", identifierToken, t);
                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_SMS_TOO_FREQUENT_HOUR,
                        "Verification code request is too frequent, please 1 hour to try again");
            }
        }

        // 每个手机设备每小时发送次数≤10
        String deviceHourKey = getCacheKey("sendSmsTimes", smsAction, SmsVerify.Type.DEVICE.name(), SmsVerify.Duration.HOUR.name(), deviceId);
        if (hasDeviceId) {
            times = op.get(deviceHourKey);

            if(times == null) {
                op.set(deviceHourKey, String.valueOf(0), 1, TimeUnit.HOURS);
            } else {
                Integer t = Integer.valueOf((String) times);
                if (t >= smsTimesDeviceForAnHour) {
                    LOGGER.error("Verification code request is too frequent, please 1 hour to try again. deviceId={}, times={}", deviceId, t);
                    throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_SMS_TOO_FREQUENT_HOUR,
                            "Verification code request is too frequent, please 1 hour to try again");
                }
            }
        }

        // 发送验证码时间不得小于60s
        String minDurationKey = getCacheKey("sendSmsTimes", smsAction, SmsVerify.Type.PHONE.name(), SmsVerify.Duration.SECOND.name(), identifierToken);
        if (smsMinDuration > 0) {
            times = op.get(minDurationKey);

            if(times == null) {
                op.set(minDurationKey, String.valueOf(0), smsMinDuration, TimeUnit.SECONDS);
            } else {
                LOGGER.error("The time for sending the verification code shall not be less than {}s, phone={}, deviceId={}.", smsMinDuration, identifierToken, deviceId);
                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_SMS_MIN_DURATION,
                        "The time for sending the verification code shall not be less than %s s", smsMinDuration);
            }
        }

        LOGGER.info("sms verify success smsAction={}, identifierToken={}, deviceId={}", smsAction, identifierToken, deviceId);
        // 发送次数增加 1
        op.increment(phoneHourKey, 1L);
        op.increment(phoneDayKey, 1L);
        if (hasDeviceId) {
            op.increment(deviceHourKey, 1L);
            op.increment(deviceDayKey, 1L);
        }
    }

    private String getCacheKey(String... keys) {
        return StringUtils.join(Arrays.asList(keys), ":");
    }

    private void sendVerificationCodeSms(Integer namespaceId, String phoneNumber, String verificationCode){
		List<Tuple<String, Object>> variables = smsProvider.toTupleList(SmsTemplateCode.KEY_VCODE, verificationCode);
		String templateScope = SmsTemplateCode.SCOPE;
		int templateId = SmsTemplateCode.VERIFICATION_CODE;
		String templateLocale = UserContext.current().getUser().getLocale();
		smsProvider.sendSms(namespaceId, phoneNumber, templateScope, templateId, templateLocale, variables);

		//		String smsType = configurationProvider.getValue(namespaceId, VCODE_SEND_TYPE, "");
		//		if(smsType.equalsIgnoreCase("YZX")){
		//			String templateId = configurationProvider.getValue(namespaceId, YZX_VCODE_TEMPLATE_ID, "");
		//			smsProvider.sendSms(number, verificationCode,templateId);
		//		}else if(smsType.equalsIgnoreCase("MW")){
		//			String templateContent = configurationProvider.getValue(namespaceId, MW_VCODE_TEMPLATE_CONTENT, "");
		//			String txt = convert(templateContent,new HashMap<String, String>() {
		//				private static final long serialVersionUID = 1L;
		//				{
		//					put("vcode", verificationCode);
		//				}
		//			}, "");
		//			smsProvider.sendSms(number, txt,null);
		//		}
	}
	private String convert(String template, Map<String, String> variables, String defaultVal) {
		String pattern = "\\$\\{(.*?)\\}";
		Pattern match = Pattern.compile(pattern);
		Matcher m = match.matcher(template);
		while (m.find()) {
			String name = m.group(1);
			template = template.replace(String.format("${%s}", name), getValue(variables, name, ""));
		}
		return template;
	}

	private String getValue(Map<String, String> map, String key, String defaultVal) {
		if (!map.containsKey(key)) {
			return defaultVal;
		}
		return map.get(key);
	}
	@Override
	public UserIdentifier findIdentifierByToken(Integer namespaceId, SignupToken signupToken) {
		assert(signupToken != null);

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(User.class, signupToken.getUserId()));
		Record record = context.select().from(EH_USER_IDENTIFIERS)
				.where(EH_USER_IDENTIFIERS.OWNER_UID.eq(signupToken.getUserId()))
				.and(EH_USER_IDENTIFIERS.IDENTIFIER_TYPE.eq(signupToken.getIdentifierType().getCode()))
				.and(EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(signupToken.getIdentifierToken()))
				.and(EH_USER_IDENTIFIERS.NAMESPACE_ID.eq(namespaceId))
				.fetchOne();
		if(LOGGER.isDebugEnabled()) {
			String sql = context.select().from(EH_USER_IDENTIFIERS)
					.where(EH_USER_IDENTIFIERS.OWNER_UID.eq(signupToken.getUserId()))
					.and(EH_USER_IDENTIFIERS.IDENTIFIER_TYPE.eq(signupToken.getIdentifierType().getCode()))
					.and(EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(signupToken.getIdentifierToken())).getSQL();
			LOGGER.debug("Find identifier by token, recordIsNull=" + (record == null) + ", signupToken=" + signupToken + ", sql=" + sql);
		}

		return ConvertHelper.convert(record, UserIdentifier.class);
	}

	@Override
	public void resendVerficationCode(Integer namespaceId, SignupToken signupToken, Integer regionCode, HttpServletRequest request) {
		UserIdentifier identifier = this.findIdentifierByToken(namespaceId, signupToken);
		if(identifier == null) {
			LOGGER.error("User identifier not found in db, signupToken=" + signupToken);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_SIGNUP_TOKEN, "Invalid signup token");
		}

		if(identifier.getClaimStatus() == IdentifierClaimStatus.CLAIMING.getCode() ||
				identifier.getClaimStatus() == IdentifierClaimStatus.VERIFYING.getCode()) {

            this.verifySmsTimes("signup", identifier.getIdentifierToken(), request.getHeader(X_EVERHOMES_DEVICE));

			Timestamp ts = identifier.getNotifyTime();
			if(ts == null || isVerificationExpired(ts)) {
				String verificationCode = RandomGenerator.getRandomDigitalString(6);
				identifier.setVerificationCode(verificationCode);

				LOGGER.debug("Send notification code " + verificationCode + " to " + identifier.getIdentifierToken());
				//                String templateId = configurationProvider.getValue(YZX_VCODE_TEMPLATE_ID, "");
				//                smmProvider.sendSms( identifier.getIdentifierToken(), verificationCode, templateId);
				//增加区号发送短信 by sfyan 20161012
				sendVerificationCodeSms(namespaceId, this.getYzxRegionPhoneNumber(identifier.getIdentifierToken(), regionCode), verificationCode);
			} else {

				// TODO
				LOGGER.debug("Send notification code " + identifier.getVerificationCode() + " to " + identifier.getIdentifierToken());
				//                String templateId = configurationProvider.getValue(YZX_VCODE_TEMPLATE_ID, "");
				//                smmProvider.sendSms( identifier.getIdentifierToken(), identifier.getVerificationCode(),templateId);
				sendVerificationCodeSms(namespaceId, this.getYzxRegionPhoneNumber(identifier.getIdentifierToken(), regionCode), identifier.getVerificationCode());
			}

			identifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			identifier.setRegionCode(regionCode);
			this.userProvider.updateIdentifier(identifier);
		} else {
			LOGGER.error("Token status is not claiming or verifying, signupToken=" + signupToken + ", identifierId=" + identifier.getId() 
					+ ", ownerUid=" + identifier.getOwnerUid() + ", identifierType=" + identifier.getIdentifierType() 
					+ ", identifierToken=" + identifier.getIdentifierToken() + ", identifierStatus=" + identifier.getClaimStatus());
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALD_TOKEN_STATUS, "Invalid token status");
		}
	}

	@Override
	public UserLogin verifyAndLogon(VerifyAndLogonCommand cmd) {
		SignupToken signupToken = WebTokenGenerator.getInstance().fromWebToken(cmd.getSignupToken(), SignupToken.class);
		if(signupToken == null) {
			cmd.setInitialPassword("");
			LOGGER.error("Signup token is empty, cmd=" + cmd);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, 
					UserServiceErrorCode.ERROR_INVALID_SIGNUP_TOKEN, "Invalid signup token");
		}
		if(StringUtils.isEmpty(cmd.getInitialPassword())){
			LOGGER.error("password cannot be empty, cmd=" + cmd);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PASSWORD, "password cannot be empty");
		}

		String verificationCode = cmd.getVerificationCode();
		String deviceIdentifier = cmd.getDeviceIdentifier();
		int namespaceId = cmd.getNamespaceId() == null ? Namespace.DEFAULT_NAMESPACE : cmd.getNamespaceId();

		UserIdentifier identifier = this.findIdentifierByToken(namespaceId, signupToken);
		if(identifier == null) {
			LOGGER.error("User identifier not found in db, signupToken=" + signupToken + ", cmd=" + cmd);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_SIGNUP_TOKEN, "Invalid signup token");
		}

		// make it idempotent in case client disconnects before it has received the successful return
		if((identifier.getClaimStatus() == IdentifierClaimStatus.VERIFYING.getCode() ||
				identifier.getClaimStatus() == IdentifierClaimStatus.CLAIMED.getCode())
				&& identifier.getVerificationCode() != null 
				&& identifier.getVerificationCode().equals(verificationCode)) {

			UserLogin rLogin = this.dbProvider.execute((TransactionStatus status)-> {
				if(identifier.getClaimStatus() == IdentifierClaimStatus.VERIFYING.getCode()) {
					UserIdentifier existingClaimedIdentifier = this.userProvider.findClaimedIdentifierByToken(namespaceId, identifier.getIdentifierToken());
					if(existingClaimedIdentifier != null) {
						existingClaimedIdentifier.setClaimStatus(IdentifierClaimStatus.TAKEN_OVER.getCode());
						this.userProvider.updateIdentifier(existingClaimedIdentifier);
					}

					identifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
					this.userProvider.updateIdentifier(identifier);
				}

				User user = this.userProvider.findUserById(identifier.getOwnerUid());
				user.setStatus(UserStatus.ACTIVE.getCode());
				user.setNickName(cmd.getNickName());
				user.setGender(UserGender.UNDISCLOSURED.getCode());

				String salt=EncryptionUtils.createRandomSalt();
				user.setSalt(salt);
				try {
					user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s",cmd.getInitialPassword(),salt)));
				} catch (Exception e) {
					LOGGER.error("encode password failed", e);
					throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PASSWORD, "Unable to create password hash");

				}
				//update user invitation code
				if(StringUtils.isNotEmpty(cmd.getInvitationCode())){
					createInvitationRecord(cmd.getInvitationCode(),identifier,user);
				}
				this.userProvider.updateUser(user);

				UserLogin login = createLogin(namespaceId, user, deviceIdentifier, cmd.getPusherIdentify());
				login.setStatus(UserLoginStatus.LOGGED_IN);

				return login;
			});

			// 刷新企业通讯录
			organizationService.processUserForMember(identifier);

			//刷新地址信息
			propertyMgrService.processUserForOwner(identifier);

			return rLogin;
		}



		LOGGER.error("Invalid verification code or identifier status, signupToken=" + signupToken + ", cmd=" + cmd 
				+ ", identifierId=" + identifier.getId()  + ", ownerUid=" + identifier.getOwnerUid() 
				+ ", identifierType=" + identifier.getIdentifierType() + ", identifierToken=" + identifier.getIdentifierToken() 
				+ ", identifierStatus=" + identifier.getClaimStatus() + ", verificationCode=" + identifier.getVerificationCode());
		throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_VERIFICATION_CODE, "Invalid verification code or state");
	}

	@Override
	public UserLogin verifyAndLogonByIdentifier(VerifyAndLogonByIdentifierCommand cmd) {
		List<UserIdentifier> identifiers = this.userProvider.findClaimingIdentifierByToken(cmd.getUserIdentifier());
		if(identifiers.size() == 0) {
			cmd.setInitialPassword("");
			LOGGER.error("Unable to locate the account by specified identifier, cmd=" + cmd);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_SIGNUP_TOKEN, 
					"Unable to locate the account by specified identifier");
		}
		if(StringUtils.isEmpty(cmd.getInitialPassword())) {
			LOGGER.error("password cannot be empty, cmd=" + cmd);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PASSWORD, "password cannot be empty");
		}

		int namespaceId = cmd.getNamespaceId() == null ? Namespace.DEFAULT_NAMESPACE : cmd.getNamespaceId();
		for(UserIdentifier identifier: identifiers) {
			// make it idempotent in case client disconnects before it has received the successful return
			// therefore, we also check status of CLAIMED in addition to VERIFYING
			if((identifier.getClaimStatus() == IdentifierClaimStatus.VERIFYING.getCode() ||
					identifier.getClaimStatus() == IdentifierClaimStatus.CLAIMED.getCode())
					&& identifier.getVerificationCode() != null 
					&& identifier.getVerificationCode().equals(cmd.getVerificationCode())) {

				return this.dbProvider.execute((TransactionStatus status)-> {
					if(identifier.getClaimStatus() == IdentifierClaimStatus.VERIFYING.getCode()) {

						UserIdentifier existingClaimedIdentifier = this.userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getUserIdentifier());
						if(existingClaimedIdentifier != null) {
							existingClaimedIdentifier.setClaimStatus(IdentifierClaimStatus.TAKEN_OVER.getCode());
							this.userProvider.updateIdentifier(existingClaimedIdentifier);
						}

						identifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
						this.userProvider.updateIdentifier(identifier);
					}

					User user = this.userProvider.findUserById(identifier.getOwnerUid());
					user.setStatus(UserStatus.ACTIVE.getCode());
					user.setNickName(cmd.getNickName());
					String salt=EncryptionUtils.createRandomSalt();
					user.setSalt(salt);
					try {
						user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s",cmd.getInitialPassword(),salt)));
					} catch (Exception e) {
						cmd.setInitialPassword("");
						LOGGER.error("Unable to create password hash, cmd=" + cmd, e);
						throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "Unable to create password hash");
					}
					//verify invitation code
					if(StringUtils.isNotEmpty(cmd.getInvitationCode())){
						createInvitationRecord(cmd.getInvitationCode(), identifier,user);
					}
					this.userProvider.updateUser(user);

					UserLogin login = createLogin(cmd.getNamespaceId() == null ? Namespace.DEFAULT_NAMESPACE : cmd.getNamespaceId(), user
							, cmd.getDeviceIdentifier(), cmd.getPusherIdentify());
					login.setStatus(UserLoginStatus.LOGGED_IN);
					return login;
				});
			}
		}

		StringBuilder strBuilder = new StringBuilder();
		boolean isFirst = true;
		for(UserIdentifier identifier: identifiers) {
			if(!isFirst) {
				strBuilder.append(", ");
			}
			strBuilder.append("{identifierId=" + identifier.getId()  + ", ownerUid=" + identifier.getOwnerUid() 
					+ ", identifierType=" + identifier.getIdentifierType() + ", identifierToken=" + identifier.getIdentifierToken() 
					+ ", identifierStatus=" + identifier.getClaimStatus() + ", verificationCode=" + identifier.getVerificationCode() + "}");
			isFirst = false;
		}
		LOGGER.error("Invalid verification code or claim status, cmd=" + cmd + ", identifiersInDb=[" + strBuilder.toString() + "]");
		throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_VERIFICATION_CODE, "Invalid verification code or state");
	}

	private void createInvitationRecord(String invitationCode, UserIdentifier identifier, User user) {
		UserInvitationsDTO invitation = userProvider.findUserInvitationByCode(invitationCode);
		if(invitation==null){
			LOGGER.error("invalid invitation code.{}",invitationCode);
			return;
		}
		if(invitation.getExpiration().getTime()<DateHelper.currentGMTTime().getTime()){
			LOGGER.error("invalid invitation code,out of time.{}",invitationCode);
			return;
		}
		if(invitation.getMaxInviteCount()!=0&&invitation.getCurrentInviteCount()>invitation.getMaxInviteCount()){
			LOGGER.error("invalid invitation code,out of max invitie count.{}",invitationCode);
			return;
		}
		UserInvitationRoster roster=new UserInvitationRoster();
		//generate id ?how
		roster.setContact(identifier.getIdentifierToken());
		roster.setInviteId(invitation.getId());
		roster.setName(user.getAccountName());//how to set name
		userProvider.createUserInvitationRoster(roster,identifier.getOwnerUid());
		//update invitation
		Integer invitationCount = invitation.getCurrentInviteCount();
		invitation.setCurrentInviteCount(invitationCount==null?0:invitationCount.intValue()+1);
		userProvider.updateInvitation(ConvertHelper.convert(invitation,UserInvitation.class));
		user.setInvitorUid(invitation.getOwnerUid());
		user.setInviteType(invitation.getInviteType());
		//update user info?
		AddUserPointCommand pointCmd=new AddUserPointCommand(user.getId(), PointType.INVITED_USER.name(), userPointService.getItemPoint(PointType.INVITED_USER), invitation.getOwnerUid());  
		userPointService.addPoint(pointCmd);
	}

	@Override
	public User logonDryrun(String userIdentifierToken, String password) {
		User user;
		user = this.userProvider.findUserByAccountName(userIdentifierToken);
		if(user == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("findUserByAccountName user is null");
            }
			UserIdentifier identifier = this.userProvider.findClaimedIdentifierByToken(Namespace.DEFAULT_NAMESPACE, userIdentifierToken);
			if(identifier != null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("findClaimedIdentifierByToken identifier is null");
                }
				user = this.userProvider.findUserById(identifier.getOwnerUid());
			}
		}

        if (user != null) {
            if (!EncryptionUtils.validateHashPassword(password, user.getSalt(), user.getPasswordHash())) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("logonDryrun validateHashPassword failure");
                }
                return null;
            }
            if (UserStatus.fromCode(user.getStatus()) != UserStatus.ACTIVE) {
                return null;
            }
        }
		return user;
	}

	@Override
	public UserLogin logon(int namespaceId, String userIdentifierToken, String password, String deviceIdentifier, String pusherIdentify) {
		User user = null;
		user = this.userProvider.findUserByAccountName(userIdentifierToken);
		if(user == null) {
			UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByToken(namespaceId, userIdentifierToken);
			if(userIdentifier == null) {
				LOGGER.warn("Unable to find identifier record,  namespaceId={}, userIdentifierToken={}, deviceIdentifier={}, pusherIdentify={}", 
				        namespaceId, userIdentifierToken, deviceIdentifier, pusherIdentify);
				throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_UNABLE_TO_LOCATE_USER, "Unable to locate user");
			} else {
				user = this.userProvider.findUserById(userIdentifier.getOwnerUid());
				if(user == null) {
					LOGGER.error("Unable to find owner user of identifier record,  namespaceId={}, userIdentifierToken={}, deviceIdentifier={}, pusherIdentify={}", 
                        namespaceId, userIdentifierToken, deviceIdentifier, pusherIdentify);
					throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_USER_NOT_EXIST, "User does not exist");
				}
			}
		}

		if(UserStatus.fromCode(user.getStatus()) != UserStatus.ACTIVE)
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_ACCOUNT_NOT_ACTIVATED, "User acount has not been activated yet");

		if(!EncryptionUtils.validateHashPassword(password, user.getSalt(), user.getPasswordHash())) {
			LOGGER.error("Password does not match for " + userIdentifierToken);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PASSWORD, "Invalid password");
		}

		if(deviceIdentifier != null && deviceIdentifier.isEmpty())
			deviceIdentifier = null;

		UserLogin login = createLogin(namespaceId, user, deviceIdentifier, pusherIdentify);
		login.setStatus(UserLoginStatus.LOGGED_IN);
		//add user point
		AddUserPointCommand cmd = new AddUserPointCommand();
		cmd.setOperatorUid(user.getId());
		cmd.setUid(user.getId());
		cmd.setPointType(PointType.APP_OPENED.name());
		cmd.setPoint(userPointService.getItemPoint(PointType.APP_OPENED));
		userPointService.addPoint(cmd);

		//added by Janson, mark as disconnected
		unregisterLoginConnection(login);

		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("User logon succeed, userIdentifierToken=" + userIdentifierToken + ", userLogin=" + login);
		}
		return login;
	}

	@Override
	public UserLogin logonByToken(LoginToken loginToken) {
		assert(loginToken != null);
		String userKey = NameMapper.getCacheKey("user", loginToken.getUserId(), null);
		Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, String.valueOf(loginToken.getLoginId()));
		UserLogin login = accessor.getMapValueObject(String.valueOf(loginToken.getLoginId()));
		if(login != null && login.getLoginInstanceNumber() == loginToken.getLoginInstanceNumber()) {
			login.setStatus(UserLoginStatus.LOGGED_IN);
			login.setLastAccessTick(DateHelper.currentGMTTime().getTime());
			accessor.putMapValueObject(String.valueOf(loginToken.getLoginId()), login);

			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("User login succeed, loginToken=" + loginToken + ", userLogin=" + login);
			}

			return login;
		}

//		if(kickoffService.isKickoff(UserContext.current().getNamespaceId(), loginToken)) {
//			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, 
//					UserServiceErrorCode.ERROR_KICKOFF_BY_OTHER, "Kickoff by others"); 		    
//		}

		LOGGER.error("Invalid token or token has expired, userKey=" + userKey + ", loginToken=" + loginToken + ", userLogin=" + login);
		throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_LOGIN_TOKEN, 
				"Invalid token or token has expired");
	}

	/**
	 * Get all logins by deviceId
	 * Added by Janson
	 * @param deviceIdentifier
	 * @return
	 * 
	 */
	private List<UserLogin> listLoginByDevice(String deviceIdentifier) {
		String deviceKey = NameMapper.getCacheKey(DEVICE_KEY, deviceIdentifier, null);
		List<UserLogin> logins = new ArrayList<>();

		// get "index" accessor
		String hkeyIndex = "0";
		Accessor accessor = this.bigCollectionProvider.getMapAccessor(deviceKey, hkeyIndex);
		// 取出的值由于历史BUG导致有可能是String、有可能是Integer，如果是Integer则会报错，需要统一转换一下 by lqs 20160114
		//String maxId = accessor.getMapValueObject(hkeyIndex);
		Object maxId = accessor.getMapValueObject(hkeyIndex);
		if(maxId != null) {
			for(int i = 1; i <= Integer.parseInt(maxId.toString()); i++) {
				String hkeyLogin = String.valueOf(i);
				Accessor accessorLogin = this.bigCollectionProvider.getMapAccessor(deviceKey, hkeyLogin);
				UserLogin login = accessorLogin.getMapValueObject(hkeyLogin);
				if(login != null && login.getStatus() == UserLoginStatus.LOGGED_IN) {
					logins.add(login);
				}
			}
		}
		return logins;
	}

	private void kickoffLoginByDevice(UserLogin newLogin) {
		if(newLogin.getDeviceIdentifier() == null || newLogin.getDeviceIdentifier().isEmpty()) {
			return;
		}

		List<UserLogin> logins = listLoginByDevice(newLogin.getDeviceIdentifier());
		for(UserLogin login : logins) {
			//For some reason ???
			if(login != newLogin) {
				//kickoff the login
				login.setStatus(UserLoginStatus.LOGGED_OFF);
				saveLogin(login);
			}
		}

		//Save newLogin to deviceMap
		String deviceKey = NameMapper.getCacheKey(DEVICE_KEY, newLogin.getDeviceIdentifier(), null);
		String hkeyIndex = "0";
		Accessor accessor = this.bigCollectionProvider.getMapAccessor(deviceKey, hkeyIndex);
		Object maxId = accessor.getMapValueObject(hkeyIndex);
		if(maxId == null) {
			maxId = "1";
		} else {
			maxId = Integer.toString(Integer.parseInt(maxId.toString()) + 1);
		}
		Accessor accessorLogin = this.bigCollectionProvider.getMapAccessor(deviceKey, maxId);
		accessorLogin.putMapValueObject(maxId.toString(), newLogin);
		accessor.putMapValueObject(hkeyIndex, Integer.valueOf(maxId.toString()));
	}

	private boolean foundUserLogin(UserLogin login, User user, String deviceIdentifier, LogonRef ref) {
		if(login.getDeviceIdentifier() == null 
				|| login.getDeviceIdentifier().equals(DeviceIdentifierType.INNER_LOGIN.name())
				|| (login.getImpersonationId() != null && login.getImpersonationId() > 0)) {
			//not user login
			return false;
		}

		if(ref.getFoundLogin() != null) {
			//found user login again
			return true;
		}

		if(!deviceIdentifier.equals(login.getDeviceIdentifier())) {
			if(login.getStatus() == UserLoginStatus.LOGGED_IN) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("User is kickoff(normal) for logined in another place, userId=" + user.getId()
							+ ", newNamespaceId=" + ref.getNamespaceId() + ", newDeviceIdentifier="
							+ deviceIdentifier + ", oldUserLogin=" + login);
				}
				//kickoff this login
				ref.setOldDeviceId(login.getDeviceIdentifier());
				ref.setOldLoginToken(new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), login.getImpersonationId()));
				login.setStatus(UserLoginStatus.LOGGED_OFF); //顺序非常重要，不能放上面一行
//				login.setLoginInstanceNumber(new Random().nextInt());
			}
			login.setDeviceIdentifier(deviceIdentifier);
		}

		//must found twice
		ref.setFoundLogin(login);
		return false;
	}

	private boolean foundImpersonationLogin(UserLogin login, User user, String deviceIdentifier, LogonRef ref) {
		if(login.getDeviceIdentifier() == null 
				|| login.getDeviceIdentifier().equals(DeviceIdentifierType.INNER_LOGIN.name())
				|| login.getImpersonationId() == null
				|| login.getImpersonationId().equals(0)) {
			//not impersonation login
			return false;
		}

		if(!deviceIdentifier.equals(login.getDeviceIdentifier())) {
			if(login.getStatus() == UserLoginStatus.LOGGED_IN) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("User is kickoff(impersonation) for logined in another place, userId=" + user.getId()
							+ ", newNamespaceId=" + ref.getNamespaceId() + ", newDeviceIdentifier="
							+ deviceIdentifier + ", oldUserLogin=" + login);
				}
				//kickoff this login
				ref.setOldDeviceId(login.getDeviceIdentifier());
				ref.setOldLoginToken(new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), ref.getImpId()));
				login.setStatus(UserLoginStatus.LOGGED_OFF);//顺序非常重要，不能放上面一行
//				login.setLoginInstanceNumber(new Random().nextInt());
			}
			login.setDeviceIdentifier(deviceIdentifier);
		}

		ref.setFoundLogin(login);
		return true;
	}

	private boolean foundLoginByLoginType(User user, String deviceIdentifier, LoginType loginType, LogonRef ref) {
		Integer namespaceId = ref.getNamespaceId();
		String userKey = NameMapper.getCacheKey("user", user.getId(), null);
		String hkeyIndex = "0";
		Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyIndex);
		Object o = accessor.getMapValueObject(hkeyIndex);
		Integer maxLoginId = null == o ? null : Integer.valueOf(o + "");
		UserLogin foundLogin = null;
		int nextLoginId = 1;
		if(maxLoginId != null) {
			for(int i = 1; i <= maxLoginId.intValue(); i++) {
				String hkeyLogin = String.valueOf(i);
				Accessor accessorLogin = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
				UserLogin login = accessorLogin.getMapValueObject(hkeyLogin);
				if(login == null) {
					continue;
				}

				//found web login
				if(loginType == LoginType.WEB) {
					if(login.getDeviceIdentifier() == null) {
						ref.setFoundLogin(login);
					}
				}
				//found inner login
				if(loginType == LoginType.INNER) {
					if(deviceIdentifier.equals(login.getDeviceIdentifier())) {
						ref.setFoundLogin(login);
						break;
					}
				}

				//found user login
				if(loginType == LoginType.USER) {
					if(foundUserLogin(login, user, deviceIdentifier, ref)) {
						//found twice, clear all for this user
						if(LOGGER.isInfoEnabled()) {
							LOGGER.info("User is kickoff state3 remove old all login tokens, userId=" + user.getId() 
									+ ", newNamespaceId=" + namespaceId + ", newDeviceIdentifier=" + deviceIdentifier
									+ ", oldUserLogin=" + login);
						}

						//found twice, delete all logins
						accessor.getTemplate().delete(accessor.getBucketName());
						accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyIndex);
						ref.setOldDeviceId("");
						ref.setNextLoginId(1);
						ref.setFoundLogin(null);
						break;
					}
				}

				//found impersonation
				if(loginType == LoginType.IMPERSONATION) {
					if(foundImpersonationLogin(login, user, deviceIdentifier, ref)) {
						break;
					}
				}

				//check
				if(foundLogin == null && login.getLoginId() >= nextLoginId){
					nextLoginId = login.getLoginId() + 1;
				}

			}
		}

		if(ref.getFoundLogin() != null) {
			ref.setNextLoginId(ref.getFoundLogin().getLoginId());
			return true;
		} else {
			ref.setNextLoginId(nextLoginId);
		}
		return false;
	}

	private UserLogin createLogin(int namespaceId, User inUser, String deviceIdentifier, String pusherIdentify) {
		Boolean isNew = false;
		User user = null;
		Long impId = null;
		UserImpersonation userImp = userImpersonationProvider.getUserImpersonationByOwnerId(inUser.getId());
		if(userImp == null) {
			user = inUser;
		} else {
			user = userProvider.findUserById(userImp.getTargetId());
			if(user == null) {
				LOGGER.warn("get impersonation userId error. userId=" + userImp.getTargetId() + ", impId=" + userImp.getId());
				user = inUser;
			}
			impId = inUser.getId();
		}

		String userKey = NameMapper.getCacheKey("user", user.getId(), null);

		// get "index" accessor
		String hkeyIndex = "0";
		Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyIndex);
		Object o = accessor.getMapValueObject(hkeyIndex);
		LogonRef ref = new LogonRef();
		ref.setNamespaceId(namespaceId);
		ref.setOldDeviceId("");
		ref.setImpId(impId);
		if(deviceIdentifier == null || deviceIdentifier.isEmpty()) {
			foundLoginByLoginType(user, null, LoginType.WEB, ref);
		} else if(deviceIdentifier.equals(DeviceIdentifierType.INNER_LOGIN.name())) {
			foundLoginByLoginType(user, deviceIdentifier, LoginType.INNER, ref);
		} else if(impId != null) {
			foundLoginByLoginType(user, deviceIdentifier, LoginType.IMPERSONATION, ref);
		} else {
			foundLoginByLoginType(user, deviceIdentifier, LoginType.USER, ref);
		}

		UserLogin foundLogin = ref.getFoundLogin();
		if(foundLogin == null) {
			foundLogin = new UserLogin(namespaceId, user.getId(), ref.getNextLoginId(), deviceIdentifier, pusherIdentify);
			accessor.putMapValueObject(hkeyIndex, ref.getNextLoginId());

			isNew = true;
		}

		foundLogin.setImpersonationId(impId);
		foundLogin.setStatus(UserLoginStatus.LOGGED_IN);
		foundLogin.setLastAccessTick(DateHelper.currentGMTTime().getTime());
		foundLogin.setPusherIdentify(pusherIdentify);
		String hkeyLogin = String.valueOf(ref.getNextLoginId());
		Accessor accessorLogin = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
		accessorLogin.putMapValueObject(hkeyLogin, foundLogin);

		if(isNew && deviceIdentifier != null && (!deviceIdentifier.equals(DeviceIdentifierType.INNER_LOGIN.name()))) {
			//Kickoff other login in this devices which is not inner login
			kickoffLoginByDevice(foundLogin);
		}

		//TODO better here, get token from foundLogin
		LoginToken token = new LoginToken(foundLogin.getUserId(), foundLogin.getLoginId(), foundLogin.getLoginInstanceNumber(), foundLogin.getImpersonationId());
		
		if(!ref.getOldDeviceId().isEmpty() && !ref.getOldLoginToken().toString().equals(token.toString())) {
			kickoffService.kickoff(namespaceId, ref.getOldLoginToken());
			kickoffService.remoteKickoffTag(namespaceId, token);
			
			//	        String locale = Locale.SIMPLIFIED_CHINESE.toString();
			//	        if(null != user && user.getLocale() != null && !user.getLocale().isEmpty()) {
			//	            locale = user.getLocale(); 
			//	        }
			//	        
			//	       //TODO INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'messaging', '5', 'zh_CN', '其它登录设备已经被踢出');
			//		    String msg = this.localeStringService.getLocalizedString(
			//	                MessagingLocalStringCode.SCOPE,
			//	                String.valueOf(MessagingLocalStringCode.KICK_OFF_ALERT),
			//	                locale,
			//	                "kickoff other devices");
			//		    sendMessageToUser(user.getId(), msg, MessagingConstants.MSG_FLAG_STORED_PUSH);
		}

		return foundLogin;
	}

	private void unregisterLoginConnection(int borderId, long userId, int loginId) {
		LOGGER.debug("unregisterLoginConnection due to border down, borderId: {}, userId: {}, loginId: {}", borderId, userId, loginId);

		String userKey = NameMapper.getCacheKey("user", userId, null);
		String hkeyLogin = String.valueOf(loginId);
		Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
		UserLogin login = accessor.getMapValueObject(hkeyLogin);
		if(login != null) {
			login.setLoginBorderId(null);
			login.setLastAccessTick(DateHelper.currentGMTTime().getTime());

			LOGGER.debug("Unregister login connection for login: {}", login.toString());
			accessor.putMapValueObject(hkeyLogin, login);
		} 
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@LocalBusMessageHandler("border.close")
	private LocalBusSubscriber.Action onBorderClose(Object sender, String subject, Object args, String subscriptionPath) {
		LOGGER.debug("Process border down event, borderId: {}");

		Border border = (Border)args;

		String key = String.valueOf(border.getId());
		Accessor accessor = this.bigCollectionProvider.getMapAccessor("border", key);

		Map entries = accessor.getTemplate().opsForHash().entries(key);
		for(Object hKey: entries.keySet()) {
			if(hKey != null) {
				String[] tokens = hKey.toString().split("\\.");

				long userId = Long.parseLong(tokens[0]);
				int loginId = Integer.parseInt(tokens[1]);

				unregisterLoginConnection(border.getId(), userId, loginId);
			}
		}
		accessor.getTemplate().delete(key);
		return LocalBusSubscriber.Action.none;
	}

	private void registerBorderTracker(int borderId, long userId, int loginId) {
		LOGGER.debug("Register border tracker, borderId: {}, userId: {}, loginId {}", borderId, userId, loginId);

		String key = String.valueOf(borderId);
		String hKey = String.valueOf(userId) +  String.valueOf(loginId);
		Accessor accessor = this.bigCollectionProvider.getMapAccessor("border", key);

		accessor.putMapValueObject(hKey, "");
	}

	@SuppressWarnings("unchecked")
	private void unregisterBorderTracker(int borderId, long userId, int loginId) {
		LOGGER.debug("Unregister border tracker, borderId: {}, userId: {}, loginId {}", borderId, userId, loginId);

		String key = String.valueOf(borderId);
		String hKey = String.valueOf(userId) +  String.valueOf(loginId);
		Accessor accessor = this.bigCollectionProvider.getMapAccessor("border", key);

		accessor.getTemplate().opsForHash().delete(key, hKey);
	}

	public UserLogin registerLoginConnection(LoginToken loginToken, int borderId, String borderSessionId) {
		String userKey = NameMapper.getCacheKey("user", loginToken.getUserId(), null);

		String hkeyLogin = String.valueOf(loginToken.getLoginId());
		Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
		UserLogin login = accessor.getMapValueObject(hkeyLogin);
		if(login != null && login.getStatus() == UserLoginStatus.LOGGED_IN) {
			//Save loginBorderId here
			login.setLoginBorderId(borderId);
			login.setBorderSessionId(borderSessionId);
			login.setLastAccessTick(DateHelper.currentGMTTime().getTime());
			accessor.putMapValueObject(hkeyLogin, login);

			registerBorderTracker(borderId, loginToken.getUserId(), loginToken.getLoginId());
			return login;
		} else {
			LOGGER.warn("Unable to find UserLogin in big map, borderId=" + borderId 
					+ ", loginToken=" + StringHelper.toJsonString(loginToken) + ", borderSessionId=" + borderSessionId);
			return null;
		}
	}

	public UserLogin unregisterLoginConnection(LoginToken loginToken, int borderId, String borderSessionId) {
		String userKey = NameMapper.getCacheKey("user", loginToken.getUserId(), null);
		String hkeyLogin = String.valueOf(loginToken.getLoginId());
		Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
		UserLogin login = accessor.getMapValueObject(hkeyLogin);
		if(login != null) {
			boolean canRemove = false;
			if( login.getLoginBorderId() == null || login.getBorderSessionId() == null ) {
				canRemove = true;
			}

			//如果 login.getLoginBorderId() 与 login.getBorderSessionId() 都不为空，则判断登录的信息有任何不相等，则此 borderId 的会话已经无效
			if(!canRemove && !(login.getLoginBorderId().equals(borderId) && login.getBorderSessionId().equals(borderSessionId))) {
				canRemove = false;
			} else {
				canRemove = true;
			}

			if(canRemove) {
				login.setLoginBorderId(null);
				login.setBorderSessionId(null);
				login.setLastAccessTick(DateHelper.currentGMTTime().getTime());
				accessor.putMapValueObject(hkeyLogin, login);

				unregisterBorderTracker(borderId, loginToken.getUserId(), loginToken.getLoginId());		        
			} else {
				//TODO 需要广播丢失的 session 信息到所有的 border service 么。
				LOGGER.info("The session is expired, borderId=" + borderId + " borderSessionId=" + borderSessionId
						+ ", oldBorderId=" + login.getLoginBorderId() + ", oldSessionId=" + login.getBorderSessionId());
			}

		} else {
			LOGGER.warn("Unable to find UserLogin in big map, borderId=" + borderId 
					+ ", loginToken=" + StringHelper.toJsonString(loginToken));
		}

		return login;
	}    

	public UserLogin unregisterLoginConnection(UserLogin userLogin) {
		String userKey = NameMapper.getCacheKey("user", userLogin.getUserId(), null);
		String hkeyLogin = String.valueOf(userLogin.getLoginId());
		Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
		UserLogin login = accessor.getMapValueObject(hkeyLogin);
		if(login != null) {
			login.setLoginBorderId(null);
			login.setLastAccessTick(DateHelper.currentGMTTime().getTime());
			accessor.putMapValueObject(hkeyLogin, login);

			if(userLogin.getLoginBorderId() != null) {
				unregisterBorderTracker(userLogin.getLoginBorderId(), userLogin.getUserId(), userLogin.getLoginId());    
			}

		}

		return login;
	}    

	public void saveLogin(UserLogin login) {
		String userKey = NameMapper.getCacheKey("user", login.getUserId(), null);
		String hkeyLogin = String.valueOf(login.getLoginId());
		Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
		accessor.putMapValueObject(hkeyLogin, login);
	}

	@Override
	public List<UserLogin> listUserLogins(long uid) {
		List<UserLogin> logins = new ArrayList<>();
		String userKey = NameMapper.getCacheKey("user", uid, null);

		// get "index" accessor
		String hkeyIndex = "0";
		Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyIndex);
		Object maxLoginId = accessor.getMapValueObject(hkeyIndex);
		if(maxLoginId != null) {
			for(int i = 1; i <= Integer.parseInt(maxLoginId.toString()); i++) {
				String hkeyLogin = String.valueOf(i);
				Accessor accessorLogin = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
				UserLogin login = accessorLogin.getMapValueObject(hkeyLogin);
				if(login != null) {
					logins.add(login);
				}
			}
		}
		return logins;
	}

	@Override
	public UserLogin findLoginByToken(LoginToken loginToken) {
		String userKey = NameMapper.getCacheKey("user", loginToken.getUserId(), null);
		String hkeyLogin = String.valueOf(loginToken.getLoginId());
		Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
		return accessor.getMapValueObject(hkeyLogin);
	}

	@Override
	public void logoff(UserLogin login) {
		String userKey = NameMapper.getCacheKey("user", login.getUserId(), null);
		String hkeyLogin = String.valueOf(login.getLoginId());
		Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
		login.setStatus(UserLoginStatus.LOGGED_OFF);
		accessor.putMapValueObject(hkeyLogin, login);

		//TODO delete deviceId?

		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("User is logoff, userLogin=" + login);
		}
	}

	@Override
	public boolean isValidLoginToken(LoginToken loginToken) {
		assert(loginToken != null);
		
		//added by janson, isKickoff ? 2017-03-29
//		if(kickoffService.isKickoff(UserContext.getCurrentNamespaceId(), loginToken)) {
////			kickoffService.remoteKickoffTag(UserContext.getCurrentNamespaceId(), loginToken);
//	      throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_KICKOFF_BY_OTHER, "Kickoff by others");
//		}
		
		String userKey = NameMapper.getCacheKey("user", loginToken.getUserId(), null);
		Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, String.valueOf(loginToken.getLoginId()));
		UserLogin login = accessor.getMapValueObject(String.valueOf(loginToken.getLoginId()));
		if(login != null && login.getLoginInstanceNumber() == loginToken.getLoginInstanceNumber()) {
			return true;
		} else {
			LOGGER.error("Invalid token, userKey=" + userKey + ", loginToken=" + loginToken + ", login=" + login);
			return false;
		}
	}

	private static boolean isVerificationExpired(Timestamp ts) {
		// TODO hard-code expiration time to 10 minutes
		return DateHelper.currentGMTTime().getTime() - ts.getTime() > 10*60000;
	}

	private Long getDateDifference(Timestamp compareValue) {
		Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String strCompare = format.format(compareValue);
		String strNow = format.format(now);
		long day = 0L;

		try {
			Date dateNow = format .parse(strNow);
			Date dateCompare = format .parse(strCompare);
			day = (dateNow.getTime()-dateCompare.getTime())/(24*60*60*1000);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			LOGGER.error("compareValue : {}", compareValue, e );
		}


		return day;
	}

	@Override
	public UserInfo getUserInfo() {
		User user = this.userProvider.findUserById(UserContext.current().getUser().getId());
		assert(user != null);

		UserInfo info = ConvertHelper.convert(user, UserInfo.class);
		// 把用户头像的处理独立到一个方法中 by lqs 20151211
		populateUserAvatar(info, user.getAvatar());
		//		info.setAvatar(user.getAvatar());
		//		try{
		//			String url = contentServerService.parserUri(user.getAvatar(), EntityType.USER.getCode(), user.getId());
		//			info.setAvatarUrl(url);
		//		}catch(Exception e){
		//			LOGGER.error("Failed to parse avatar uri, userId=" + user.getId() + ", avatar=" + user.getAvatar());
		//		}

		if(user.getCreateTime() != null) {
			Long days = getDateDifference(user.getCreateTime());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("days", days);
			String scope = UserNotificationTemplateCode.SCOPE;
			int code = UserNotificationTemplateCode.USER_REGISTER_DAYS;

			String notifyText = localeTemplateService.getLocaleTemplateString(user.getNamespaceId(),scope, code, user.getLocale(), map, "");
			info.setRegisterDaysDesc(notifyText);

		}
		if(user.getCommunityId()!=null){
			Community community = communityProvider.findCommunityById(user.getCommunityId());
			if(community != null){
				info.setRegionId(community.getCityId());
				Region region = regionProvider.findRegionById(community.getCityId());
				if(region != null){
					info.setRegionName(region.getName());
					info.setRegionPath(region.getPath());
				}
				info.setCommunityName(community.getName());
				info.setCommunityType(community.getCommunityType());
			}
		}
		if(user.getBirthday() != null) {
			info.setBirthday(new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthday()));
		}

		if(user.getHomeTown() != null) {
			Category category = this.categoryProvider.findCategoryById(user.getHomeTown());
			if(category != null)
				info.setHometownName(category.getName());
		}
		List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(user.getId());

        Stream<UserIdentifier> identifierStream = identifiers.stream().filter((r) -> {
            return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.MOBILE;
        });

        List<String> phones = identifierStream.map(EhUserIdentifiers::getIdentifierToken).collect(Collectors.toList());
		info.setPhones(phones);

        List<Integer> regionCodes = identifierStream.map(EhUserIdentifiers::getRegionCode).collect(Collectors.toList());
        info.setRegionCodes(regionCodes);

		List<String> emails = identifiers.stream().filter((r)-> { return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.EMAIL; })
				.map(EhUserIdentifiers::getIdentifierToken)
				.collect(Collectors.toList());
		info.setEmails(emails);

		// 用户当前选择的实体（可能有小区、家庭、机构）
		List<UserCurrentEntity> entityList = listUserCurrentEntity(user.getId());
		if(entityList.size() > 0) {
			info.setEntityList(entityList);
		}

		GetUserTreasureCommand cmd = new GetUserTreasureCommand();
		cmd.setUid(user.getId());
		GetUserTreasureResponse treasure = userPointService.getUserTreasure(cmd);
		if(treasure != null){
			BeanUtils.copyProperties(treasure, info);
		}
		return info;
	}

	@Override
	public void setUserInfo(SetUserInfoCommand cmd) {
		User user = this.userProvider.findUserById(UserContext.current().getUser().getId());

		user.setAvatar(cmd.getAvatarUri());
		String birthdayString = cmd.getBirthday();
		if(StringUtils.isNotEmpty(birthdayString)) {
			try {
				SimpleDateFormat fromat = new SimpleDateFormat("yyyy-MM-dd");
				user.setBirthday(new java.sql.Date(fromat.parse(birthdayString).getTime()));
			} catch (Exception e) {
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
						"Invalid birthday paramter");
			}
		}

		user.setCompany(cmd.getCompany());
		user.setSchool(cmd.getSchool());
		user.setHomeTown(cmd.getHomeTown());
		user.setStatusLine(cmd.getStatusLine());
		user.setNickName(cmd.getNickName());
		user.setGender(cmd.getGender());
		user.setOccupation(cmd.getOccupation());

		this.userProvider.updateUser(user);
	}

	@Override
	public void setUserAccountInfo(SetUserAccountInfoCommand cmd) {
		if(cmd.getAccountName() == null || cmd.getAccountName().isEmpty() ||
				cmd.getPassword() == null || cmd.getPassword().isEmpty())
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"accountName and password can not be empty");

		this.coordinationProvider.getNamedLock(CoordinationLocks.SETUP_ACCOUNT_NAME.getCode()).enter(()->{
			User user = this.userProvider.findUserById(UserContext.current().getUser().getId());
			User userOther = this.userProvider.findUserByAccountName(cmd.getAccountName());
			if(userOther != null && userOther.getId() != user.getId())
				throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_ACCOUNT_NAME_ALREADY_EXISTS,
						"accountName is already used by others");

			user.setAccountName(cmd.getAccountName());
			String salt=EncryptionUtils.createRandomSalt();
			user.setSalt(salt);
			user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s",cmd.getPassword(),salt)));
			this.userProvider.updateUser(user);

			return null;
		});
	}

	@Override
	public CommunityDTO setUserCurrentCommunity(long communityId) {
		User user = this.userProvider.findUserById(UserContext.current().getUser().getId());
		user.setCommunityId(communityId);
		this.userProvider.updateUser(user);

		updateUserCurrentCommunityToProfile(user.getId(), communityId, user.getNamespaceId());

		Community community = this.communityProvider.findCommunityById(communityId);
		if(community != null) {
			return ConvertHelper.convert(community, CommunityDTO.class);
		} else {
			return null;
		}
	}

	/**
	 * 当用户从不同版的APP登录进来时，若之前没有选中的园区，则默认设置一个
	 * @return 选中的园区ID
	 */
	@Override
	public Long setDefaultCommunity(Long userId, Integer namespaceId) {
		//不能从UserContext获取，应该由外面传入 by sfyan 20160601
		//      User user = UserContext.current().getUser();
		Long communityId = 0L;

		try {
			List<UserCurrentEntity> entityList = listUserCurrentEntity(userId);
			if(!containPartnerCommunity(namespaceId, entityList)) {
				List<NamespaceResource> resources = namespaceResourceProvider.listResourceByNamespace(namespaceId, NamespaceResourceType.COMMUNITY);
				if(resources != null && resources.size() == 1) {
					communityId = resources.get(0).getResourceId();
					updateUserCurrentCommunityToProfile(userId, communityId, namespaceId);
					if(LOGGER.isInfoEnabled()) {
						LOGGER.info("Set default community, userId=" + userId + ", communityId=" + communityId 
								+ ", namespaceId=" + namespaceId);
					}
				} else {
					if(LOGGER.isInfoEnabled()) {
						LOGGER.info("Community not found, ignore to set default community, userId=" + userId  
								+ ", namespaceId=" + namespaceId);
					}
				}
			}
		} catch(Exception e) {
			LOGGER.error("Failed to set default community, userId=" + userId + ", namespaceId=" + namespaceId, e);
		}

		return communityId;
	}

	private boolean containPartnerCommunity(Integer namespaceId, List<UserCurrentEntity> entityList) {
		if(entityList == null || entityList.size() == 0) {
			return false;
		}

		boolean isFound = false;
		for(UserCurrentEntity entity : entityList) {
			UserCurrentEntityType type = UserCurrentEntityType.fromCode(entity.getEntityType());
			if(namespaceId.equals(entity.getNamespaceId()) 
					&& (type == UserCurrentEntityType.COMMUNITY_COMMERCIAL || type == UserCurrentEntityType.COMMUNITY 
					|| type == UserCurrentEntityType.COMMUNITY_RESIDENTIAL)) {
				isFound = true;
				break;
			}
		}

		return isFound;
	}

	@Override
	public void updateUserCurrentCommunityToProfile(Long userId, Long communityId, Integer namespaceId) {
		Community community = this.communityProvider.findCommunityById(communityId);
		if(community != null) {
			String key = UserCurrentEntityType.COMMUNITY_RESIDENTIAL.getUserProfileKey();
			if(CommunityType.fromCode(community.getCommunityType()) == CommunityType.COMMERCIAL) {
				key = UserCurrentEntityType.COMMUNITY_COMMERCIAL.getUserProfileKey();
			}
			long timestemp = DateHelper.currentGMTTime().getTime();
			userActivityProvider.updateUserCurrentEntityProfile(userId, key, communityId, timestemp, namespaceId);
		}
	}

	@Override
	public List<UserIdentifierDTO> listUserIdentifiers() {
		long uid = UserContext.current().getUser().getId();
		List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(uid);

		return identifiers.stream().map((r) -> { return ConvertHelper.convert(r, UserIdentifierDTO.class); })
				.collect(Collectors.toList());
	}

	@Override
	public void deleteUserIdentifier(long identifierId) {
		User user = this.userProvider.findUserById(UserContext.current().getUser().getId());
		long uid = user.getId();

		UserIdentifier identifier = this.userProvider.findIdentifierById(identifierId);
		if(identifier == null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Could not find the identifier");

		if(identifier.getOwnerUid() != uid)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, 
					"Access denied");

		if(user.getPasswordHash() == null || user.getPasswordHash().isEmpty())
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
					UserServiceErrorCode.ERROR_ACCOUNT_PASSWORD_NOT_SET,
					"Account password has not been properly setup yet");

		this.userProvider.deleteIdentifier(identifier);
	}

	@Override
	public void resendVerficationCode(ResendVerificationCodeByIdentifierCommand cmd, HttpServletRequest request) {
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getIdentifier());
        if(userIdentifier==null){
            LOGGER.error("cannot find user identifierToken.identifierToken={}",cmd.getIdentifier());
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
                    UserServiceErrorCode.ERROR_USER_NOT_EXIST, "can not find user identifierToken or status is error");
        }

        this.verifySmsTimes("fogotPasswd", userIdentifier.getIdentifierToken(), request.getHeader(X_EVERHOMES_DEVICE));

        String verificationCode = RandomGenerator.getRandomDigitalString(6);
        userIdentifier.setVerificationCode(verificationCode);
        userIdentifier.setRegionCode(cmd.getRegionCode());
        userIdentifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        sendVerificationCodeSms(userIdentifier.getNamespaceId(), this.getYzxRegionPhoneNumber(userIdentifier.getIdentifierToken(), userIdentifier.getRegionCode()), verificationCode);

        this.userProvider.updateIdentifier(userIdentifier);

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Send notification code " + verificationCode + " to " + userIdentifier.getIdentifierToken());
        }
	}

	@Override
	public UserInvitationsDTO createInvatation(CreateInvitationCommand cmd) {
		// validate
		assert cmd.getInviteType()!=null;
		assert StringUtils.isNotEmpty(cmd.getTargetEntityType());
		User user = UserContext.current().getUser();
		List<UserIdentifier> indentifiers = userProvider.listUserIdentifiersOfUser(user.getId());
		if(CollectionUtils.isEmpty(indentifiers)){
			LOGGER.error("cannot find user");
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_USER_NOT_EXIST, "cannot find user");
		}
		ClaimedAddressInfo address=null;
		try{
			ClaimAddressCommand claimCmd=new ClaimAddressCommand();
			claimCmd.setApartmentName(cmd.getAptNumber());
			claimCmd.setBuildingName(cmd.getBuildingNum());
			claimCmd.setCommunityId(cmd.getCommunityId());
			address = addressService.claimAddress(claimCmd);
		}catch(Exception e){
			//TODO
			//skip all exception
			LOGGER.warn("cmd : {}", cmd, e);
		}

		//get enum type
		InvitationType inviteTye = InvitationType.fromCode(cmd.getInviteType());
		EntityType entityType=EntityType.fromCode(cmd.getTargetEntityType());

		UserInvitation invitations = new UserInvitation();
		invitations.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		// current count
		invitations.setCurrentInviteCount(0);

		invitations.setOwnerUid(user.getId());
		if(address!=null){
			invitations.setMaxInviteCount(1);
		}else{
			invitations.setMaxInviteCount(0);
		}
		invitations.setCurrentInviteCount(0);
		invitations.setInviteType(inviteTye.getCode());
		invitations.setTargetEntityId(cmd.getTargetEntityId());
		invitations.setTargetEntityType(entityType.getCode());
		invitations.setStatus(InvitationStatus.active.getCode());
		int maxTryCount = 2; 
		int tryCountPerTime = 3; 
		for(int index=0;index<maxTryCount;index++){
			for(int rindex=0;rindex<tryCountPerTime;rindex++){
				try{
					tryGenerateInvitation(indentifiers.get(0).getIdentifierToken(),invitations);
					//send notify
					sendNotify(user.getId(),"");
					return ConvertHelper.convert(invitations, UserInvitationsDTO.class);
				}catch(Exception e){
					LOGGER.error("create invitation code failed,retry",e);
				}
				try{
					//sleep for while
					Thread.sleep(200);
				}catch(Exception e){

				}
			}
		}
		LOGGER.error("cannot create invitation code");
		throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVITATION_CODE, "invitation code create failed");
	}

	private void sendNotify(Long uid,String message){
		//TODO
	}

	private void tryGenerateInvitation(String indentifier,UserInvitation invitations){
		long expirationTime=configurationProvider.getLongValue(EXPIRE_TIME, 4320);
		String inviteCode=EncryptionUtils.genInviteCodeByIdentifier(indentifier);
		invitations.setExpiration(new Timestamp(DateHelper.currentGMTTime().getTime() + 60 * 1000 * expirationTime));
		invitations.setInviteCode(inviteCode);
		this.userProvider.createInvitation(invitations);
	}

	@Override
	public void assumePortalRole(AssumePortalRoleCommand cmd) {
		if(cmd.getRoleId() == null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameter, roleId could not be empty");

		Role role = this.aclProvider.getRoleById(cmd.getRoleId().longValue());
		if(role == null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Invalid parameter, roleId should be a valid one");

		PortalRoleResolver resolver = PlatformContext.getComponent(PortalRoleResolver.PORTAL_ROLE_RESOLVER_PREFIX + role.getAppId());
		if(resolver == null)
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
					ErrorCodes.ERROR_INVALID_PARAMETER,
					"Unable to find portal role resolver");

		resolver.assumePortalRole(cmd.getRoleId());

		// if there is no exception, save it into the login
		UserLogin login = UserContext.current().getLogin();
		login.setPortalRole(cmd.getRoleId());
		this.saveLogin(login);
	}

	@Override
	public long getNextStoreSequence(UserLogin login, int namespaceId, long appId) {
		String msgBoxKey = UserMessageRoutingHandler.getMessageBoxKey(login, namespaceId, appId);
		return this.messageBoxProvider.allocBoxSequence(msgBoxKey);
	}

	@Override
	public User findUserByIndentifier(Integer namespaceId, String indentifierToken) {
		UserIdentifier userIndentifier = userProvider.findClaimedIdentifierByToken(namespaceId, indentifierToken);
		if(userIndentifier==null){
			return null;
		}
		return userProvider.findUserById(userIndentifier.getOwnerUid());
	}

	@Override
	public UserInfo getUserInfo(Long uid) {
		if(uid==null){
			LOGGER.error("invalid uid,cannot null");
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS, "uid cannot be null");
		}
		User user=UserContext.current().getUser();
		if(user.getId().longValue()==uid.longValue()){
			return getUserInfo();
		}
		User queryUser=userProvider.findUserById(uid);
		if(queryUser==null){
			LOGGER.error("cannot find user any information.uid={}",uid);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_USER_NOT_EXIST, "cannot find user information");
		}
		List<FamilyDTO> currentUserFamilies=familyProvider.getUserFamiliesByUserId(user.getId());
		List<FamilyDTO> queryUserFamilies=familyProvider.getUserFamiliesByUserId(queryUser.getId());
		if(CollectionUtils.isEmpty(currentUserFamilies)){
			LOGGER.error("cannot find any family information");
			return null;
		}
		LOGGER.info("Find current user family {},query user family {}",currentUserFamilies,queryUserFamilies);
		//if have same family ,the result >0
		List<Long> queryUserFamilyIds = queryUserFamilies.stream().map(r -> {
			Long id = r.getId();
			return id;
		}).collect(Collectors.toList());
		List<FamilyDTO> currUserFamilies = new ArrayList<FamilyDTO>();
		for(FamilyDTO family:currentUserFamilies){

			if(queryUserFamilyIds.contains(family.getId()))
				currUserFamilies.add(family);
		}
		//        currentUserFamilies.retainAll(queryUserFamilies);
		if(CollectionUtils.isEmpty(currUserFamilies)){
			LOGGER.error("cannot find user information ,because the current user and to lookup user in diff family.current_uid={},uid={}",user.getId(),uid);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PERMISSION, "permission denied");
		}
		UserInfo info=ConvertHelper.convert(queryUser, UserInfo.class);
		// 把用户头像的处理独立到一个方法中 by lqs 20151211
		populateUserAvatar(info, queryUser.getAvatar());
		//		info.setAvatar(queryUser.getAvatar());
		//		try{
		//			String url=contentServerService.parserUri(queryUser.getAvatar(),EntityType.USER.getCode(),queryUser.getId());
		//			info.setAvatarUrl(url);
		//		}catch(Exception e){
		//			LOGGER.error("Failed to parse user avatar uri, userId=" + uid + ", avatar=" + info.getAvatar());
		//		}
		if(queryUser.getCommunityId()!=null){
			Community community = communityProvider.findCommunityById(queryUser.getCommunityId());
			if(community!=null){
				info.setRegionId(community.getCityId());
				Region region = regionProvider.findRegionById(community.getCityId());
				if(region!=null){
					info.setRegionName(region.getName());
					info.setRegionPath(region.getPath());
				}
				info.setCommunityName(community.getName());
			}
		}

		if(queryUser.getBirthday() != null) {
			info.setBirthday(new SimpleDateFormat("yyyy-MM-dd").format(queryUser.getBirthday()));
		}
		if(queryUser.getHomeTown() != null) {
			Category category = this.categoryProvider.findCategoryById(queryUser.getHomeTown());
			if(category != null)
				info.setHometownName(category.getName());
		}
		List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(queryUser.getId());
		List<String> phones = identifiers.stream().filter((r)-> { return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.MOBILE; })
				.map((r) -> { return r.getIdentifierToken(); })
				.collect(Collectors.toList());
		info.setPhones(phones);
		List<String> emails = identifiers.stream().filter((r)-> { return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.EMAIL; })
				.map((r) -> { return r.getIdentifierToken(); })
				.collect(Collectors.toList());
		info.setEmails(emails);

		// 用户当前选择的实体（可能有小区、家庭、机构）
		List<UserCurrentEntity> entityList = listUserCurrentEntity(user.getId());
		if(entityList.size() > 0) {
			info.setEntityList(entityList);
		}

		return info;
	}

	private UserInfo getUserBasicInfoByQueryUser(User queryUser, boolean hideMobile) {
		UserInfo info=ConvertHelper.convert(queryUser, UserInfo.class);
		List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(queryUser.getId());
		List<String> phones = identifiers.stream().filter((r)-> { return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.MOBILE; })
				.map((r) -> {
					if(hideMobile) {
						String token=r.getIdentifierToken();
						String prefix=token.substring(0, 3);
						String end=token.substring(token.length()-4, token.length());
						//replace phone number with ****
						return String.format("%s%s%s", prefix,"****",end);      
					} else {
						return r.getIdentifierToken();
					}

				})
				.collect(Collectors.toList());
		info.setPhones(phones);

		List<String> emails = identifiers.stream().filter((r)-> { return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.EMAIL; })
				.map((r) -> { return r.getIdentifierToken(); })
				.collect(Collectors.toList());
		info.setEmails(emails);
		if(queryUser.getBirthday() != null)
			info.setBirthday(new SimpleDateFormat("yyyy-MM-dd").format(queryUser.getBirthday()));
		if(queryUser.getCommunityId()!=null){
			Community community = communityProvider.findCommunityById(queryUser.getCommunityId());
			if(community!=null){
				info.setRegionId(community.getCityId());
				Region region = regionProvider.findRegionById(community.getCityId());
				if(region!=null){
					info.setRegionName(region.getName());
					info.setRegionPath(region.getPath());
				}
				info.setCommunityName(community.getName());
			}
		}
		// 把用户头像的处理独立到一个方法中 by lqs 20151211
		populateUserAvatar(info, queryUser.getAvatar());
		//		info.setAvatar(queryUser.getAvatar());
		//		try{
		//			String url=contentServerService.parserUri(queryUser.getAvatar(),EntityType.USER.getCode(),queryUser.getId());
		//			info.setAvatarUrl(url);
		//		}catch(Exception e){
		//			LOGGER.info("getUserBasicInfo error: " + e.getMessage());
		//		}
		return info;
	}

	private void populateUserAvatar(UserInfo user, String avatarUri) {
		if(avatarUri == null || avatarUri.trim().length() == 0) {
			avatarUri = getUserAvatarUriByGender(user.getId(), user.getNamespaceId(), user.getGender());
		}
		user.setAvatarUri(avatarUri);
		try{
			String url=contentServerService.parserUri(avatarUri, EntityType.USER.getCode(), user.getId());

			// 用户的头像设置为固定的地址    add by xq.tian  2017/04/19
            // String encode = encodeUrl("avatar/" + user.getId());
            // url = url.replaceAll("image/.+?\\?", String.format("%s%s%s", "image/", encode, "?"));

            user.setAvatarUrl(url);
		}catch(Exception e){
			LOGGER.error("Failed to parse avatar uri, userId=" + user.getId() + ", avatar=" + avatarUri, e);
		}
	}

    public static String encodeUrl(String path) {
        byte[] code = Base64.getEncoder().encode(path.getBytes(Charset.forName("utf-8")));
        return new String(code, Charset.forName("utf-8")).replace("/", "_").replace("+", "-").replace("=", "");
    }

	@Override
	public String getUserAvatarUriByGender(Long userId, Integer namespaceId, Byte gener) {
		UserGender userGender = UserGender.fromCode(gener);
		if(userGender == null) {
			userGender = UserGender.UNDISCLOSURED;
		}

		String avatarUri = null;
		switch(userGender) {
		case MALE:
			avatarUri = configurationProvider.getValue(namespaceId, "user.avatar.male.url", "");
			break;
		case FEMALE:
			avatarUri = configurationProvider.getValue(namespaceId, "user.avatar.female.url", "");
			break;
		default:
			avatarUri = configurationProvider.getValue(namespaceId, "user.avatar.undisclosured.url", "");
			break;
		}

		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Gen the default avatar for user by gender, userId=" + userId 
					+ ", namespaceId=" + namespaceId + ", gener=" + gener + ", avatarUri=" + avatarUri);
		}

		return avatarUri;
	}

	private UserInfo getUserBasicInfo(Long uid, boolean hideMobile) {
		assert(uid != null);
		User user=UserContext.current().getUser();
		if(user != null && user.getId() != null && user.getId().longValue() == uid.longValue()){
			return getUserInfo();
		}

		User queryUser = userProvider.findUserById(uid);
		if(queryUser == null){
			return null;
		}

		return getUserBasicInfoByQueryUser(queryUser, hideMobile);
	}

	@Override
	public UserInfo getUserBasicByUuid(String uuid) {
		User queryUser = userProvider.findUserByUuid(uuid);
		if(queryUser == null){
			return null;
		}
		return getUserBasicInfoByQueryUser(queryUser, false);
	}

	@Override
	public UserInfo getUserSnapshotInfo(Long uid) {
		return getUserBasicInfo(uid, true);
	}

	@Override
	public UserInfo getUserSnapshotInfoWithPhone(Long uid) {
		return getUserBasicInfo(uid, false);
	}

	@Override
	public List<ListUsersWithAddrResponse> listUsersWithAddr(ListUsersWithAddrCommand cmd) {


		ListAllFamilyMembersAdminCommand familycmd = new ListAllFamilyMembersAdminCommand();
		familycmd.setPageOffset(cmd.getPageOffset());
		familycmd.setPageSize(cmd.getPageSize());
		ListAllFamilyMembersCommandResponse response = familyService.listAllFamilyMembers(familycmd);
		List<ListUsersWithAddrResponse> results = new ArrayList<ListUsersWithAddrResponse>();

		if(response != null){

			response.getRequests().forEach(r ->{
				ListUsersWithAddrResponse usersWithAddr = new ListUsersWithAddrResponse();
				usersWithAddr.setFamilyName(r.getFamilyName());
				usersWithAddr.setCommunityName(r.getCommunityName());
				usersWithAddr.setApartmentName(r.getApartmentName());
				usersWithAddr.setAreaName(r.getAreaName());
				usersWithAddr.setBuildingName(r.getBuildingName());
				usersWithAddr.setCityName(r.getCityName());
				usersWithAddr.setCellPhone(r.getCellPhone());
				usersWithAddr.setAddressStatus(r.getAddressStatus());
				usersWithAddr.setApartmentStatus(r.getMembershipStatus());
				usersWithAddr.setNickName(r.getMemberNickName());
				usersWithAddr.setCellPhoneNumberLocation(r.getCellPhone());
				usersWithAddr.setCreateTime(r.getCreateTime());
				usersWithAddr.setId(r.getId());

				results.add(usersWithAddr);
			});


		}

		return results;
	}
	@Override
	public UsersWithAddrResponse searchUsersWithAddr(
			SearchUsersWithAddrCommand cmd) {

		ListAllFamilyMembersAdminCommand familycmd = new ListAllFamilyMembersAdminCommand();
		familycmd.setPageOffset(cmd.getPageOffset());
		familycmd.setPageSize(cmd.getPageSize());
		ListAllFamilyMembersCommandResponse response = familyService.listAllFamilyMembers(familycmd);
		List<FamilyMemberFullDTO> familyResults = response.getRequests();

		String nickName = cmd.getNickName();
		String cellPhone = cmd.getCellPhone();
		List<ListUsersWithAddrResponse> results = new ArrayList<ListUsersWithAddrResponse>();

		if(response != null){
			for(FamilyMemberFullDTO member : familyResults){
				if((nickName == null || "".equals(nickName) || nickName == member.getMemberNickName() || nickName.equals(member.getMemberNickName())) &&
						(cellPhone == null || "".equals(cellPhone) ||cellPhone == member.getCellPhone() || cellPhone.equals(member.getCellPhone()))){
					ListUsersWithAddrResponse usersWithAddr = new ListUsersWithAddrResponse();
					usersWithAddr.setFamilyName(member.getFamilyName());
					usersWithAddr.setCommunityName(member.getCommunityName());
					usersWithAddr.setApartmentName(member.getApartmentName());
					usersWithAddr.setAreaName(member.getAreaName());
					usersWithAddr.setBuildingName(member.getBuildingName());
					usersWithAddr.setCityName(member.getCityName());
					usersWithAddr.setCellPhone(member.getCellPhone());
					usersWithAddr.setAddressStatus(member.getAddressStatus());
					usersWithAddr.setApartmentStatus(member.getMembershipStatus());
					usersWithAddr.setNickName(member.getMemberNickName());
					usersWithAddr.setCellPhoneNumberLocation(member.getCellPhone());
					usersWithAddr.setCreateTime(member.getCreateTime());
					usersWithAddr.setId(member.getId());

					results.add(usersWithAddr);
				}
			}

		}

		UsersWithAddrResponse users = new UsersWithAddrResponse();
		users.setUsers(results);
		users.setNextPageOffset(response.getNextPageOffset());
		return users;
	}
	@Override
	public ListInvitatedUserResponse listInvitatedUser(
			ListInvitatedUserCommand cmd) {
		CrossShardListingLocator locator=new CrossShardListingLocator();
		locator.setAnchor(cmd.getAnchor()==null?0L:cmd.getAnchor());
		if(cmd.getPageSize()==null){
			int value=configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
			cmd.setPageSize(value);
		}
		List<InvitatedUsers> result = new ArrayList<InvitatedUsers>();
		List<InvitationRoster> invitationRoster = userProvider.listInvitationRostor(locator, cmd.getPageSize(), null);

		invitationRoster.forEach(r ->{
			InvitatedUsers invitatedUsers = new InvitatedUsers();
			invitatedUsers.setUserNickName(r.getUserNickName());
			invitatedUsers.setInviteType(r.getInviteType());
			invitatedUsers.setRegisterTime(r.getRegTime());

			List<UserIdentifier> userIdentifiers = this.userProvider.listUserIdentifiersOfUser(r.getUid());
			if(userIdentifiers != null && userIdentifiers.size() != 0)
				invitatedUsers.setUserCellPhone(userIdentifiers.get(0).getIdentifierToken());

			List<UserIdentifier> inviterIdentifiers = this.userProvider.listUserIdentifiersOfUser(r.getInviterId());
			if(inviterIdentifiers != null && inviterIdentifiers.size() != 0)
				invitatedUsers.setInviterCellPhone(inviterIdentifiers.get(0).getIdentifierToken());
			User inviter = this.userProvider.findUserById(r.getInviterId());
			if(inviter != null)
				invitatedUsers.setInviter(inviter.getNickName());

			result.add(invitatedUsers);

		});
		ListInvitatedUserResponse response = new ListInvitatedUserResponse();
		response.setInvitatedUsers(result);
		if(result.size()<cmd.getPageSize()){
			response.setNextPageAnchor(null);
		}else{
			response.setNextPageAnchor(locator.getAnchor());
		}
		return response;
	}
	@Override
	public ListInvitatedUserResponse searchInvitatedUser(
			SearchInvitatedUserCommand cmd) {

		CrossShardListingLocator locator=new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor()==null?0L:cmd.getPageAnchor());
		if(cmd.getPageSize()==null){
			int value=configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
			cmd.setPageSize(value);
		}
		List<InvitatedUsers> result = new ArrayList<InvitatedUsers>();
		List<InvitationRoster> invitationRoster = userProvider.listInvitationRostor(locator, cmd.getPageSize(), null);
		String userPhone = cmd.getUserPhone();
		String inviterPhone = cmd.getInviterPhone();

		invitationRoster.forEach(r ->{
			InvitatedUsers invitatedUsers = new InvitatedUsers();

			List<UserIdentifier> userIdentifiers = this.userProvider.listUserIdentifiersOfUser(r.getUid());
			if(userIdentifiers != null && userIdentifiers.size() != 0)
				invitatedUsers.setUserCellPhone(userIdentifiers.get(0).getIdentifierToken());

			List<UserIdentifier> inviterIdentifiers = this.userProvider.listUserIdentifiersOfUser(r.getInviterId());
			if(inviterIdentifiers != null && inviterIdentifiers.size() != 0)
				invitatedUsers.setInviterCellPhone(inviterIdentifiers.get(0).getIdentifierToken());
			User inviter = this.userProvider.findUserById(r.getInviterId());
			if(inviter != null)
				invitatedUsers.setInviter(inviter.getNickName());

			if((userPhone == null || "".equals(userPhone) || userPhone == invitatedUsers.getUserCellPhone() || userPhone.equals(invitatedUsers.getUserCellPhone()))
					&& (inviterPhone == null || "".equals(inviterPhone) || inviterPhone == invitatedUsers.getInviterCellPhone() ||inviterPhone.equals(invitatedUsers.getInviterCellPhone()))){
				invitatedUsers.setUserNickName(r.getUserNickName());
				invitatedUsers.setInviteType(r.getInviteType());
				invitatedUsers.setRegisterTime(r.getRegTime());
				invitatedUsers.setUserId(r.getUid());
				invitatedUsers.setInviterId(r.getInviterId());

				result.add(invitatedUsers);
			}

		});
		ListInvitatedUserResponse response = new ListInvitatedUserResponse();
		response.setInvitatedUsers(result);
		if(result.size()<cmd.getPageSize()){
			response.setNextPageAnchor(null);
		}else{
			response.setNextPageAnchor(locator.getAnchor());
		}
		return response;
	}
	@Override
	public GetSignatureCommandResponse getSignature() {
		User user = UserContext.current().getUser();
		return this.produceSignature(user);
	}

	private GetSignatureCommandResponse produceSignature(User user) {
		//2016-07-29:modify by liujinwne,parameter name don't be signed.

		Long userId = user.getId();
		//String name = user.getNickName();
		String appKey = configurationProvider.getValue(SIGN_APP_KEY, "44952417-b120-4f41-885f-0c1110c6aece");
		Long timeStamp = System.currentTimeMillis();
		Integer randomNum = (int) (Math.random()*1000);
		App app = appProvider.findAppByKey(appKey);
		Map<String,String> map = new HashMap<String, String>();
		map.put("id", userId+"");
		//map.put("name", name);
		map.put("appKey", appKey+"");
		map.put("timeStamp", timeStamp+"");
		map.put("randomNum", randomNum+"");
		long s = System.currentTimeMillis();
		String signature = SignatureHelper.computeSignature(map, app.getSecretKey());
		long e = System.currentTimeMillis();
		LOGGER.debug("getBizSignature-elapse2="+(e-s));
		GetSignatureCommandResponse result = new GetSignatureCommandResponse();
		result.setId(userId);
		//result.setName(name);
		result.setAppKey(appKey);
		result.setTimeStamp(timeStamp);
		result.setRandomNum(randomNum);
		result.setSignature(signature);
		return result;
	}

	@Override
	public List<UserCurrentEntity> listUserCurrentEntity(Long userId) {
		List<UserCurrentEntity> entityList = new ArrayList<UserCurrentEntity>();

		for(UserCurrentEntityType type : UserCurrentEntityType.values()) {
			UserProfile profile = userActivityProvider.findUserProfileBySpecialKey(userId, type.getUserProfileKey());
			if(profile != null) {
				UserCurrentEntity entity = new UserCurrentEntity();
				entity.setEntityType(type.getCode());
				entity.setEntityId(profile.getItemValue());
				entity.setTimestamp(profile.getIntegralTag1());
				Long namespaceId = profile.getIntegralTag2();
				if(namespaceId != null) {
					entity.setNamespaceId((int)namespaceId.longValue());
				} else {
					entity.setNamespaceId(Namespace.DEFAULT_NAMESPACE);
				}

				entityList.add(entity);

				String entityId = entity.getEntityId();
				if(entityId != null && entityId.length() > 0) {
					try {
						Long id = Long.parseLong(entityId);
						switch(type) {
						case COMMUNITY_COMMERCIAL:
						case COMMUNITY_RESIDENTIAL:
							Community community = communityProvider.findCommunityById(id);
							if(community != null) {
								entity.setEntityName(community.getName());
							} else {
								LOGGER.error("Community not found, userId=" + userId + ", communityId=" + id + ", type=" + type);
							}
							break;
						case FAMILY:
							FamilyDTO family = familyProvider.getFamilyById(id);
							if(family != null) {
								entity.setEntityName(family.getName());
							} else {
								LOGGER.error("Family not found, userId=" + userId + ", familyId=" + id + ", type=" + type);
							}
							break;
						case ORGANIZATION:
							Organization organization = organizationProvider.findOrganizationById(id);
							if(organization != null) {
								entity.setEntityName(organization.getName());
								if(organization.getDirectlyEnterpriseId() != null && organization.getDirectlyEnterpriseId() != 0) {
									entity.setDirectlyEnterpriseId(organization.getDirectlyEnterpriseId());
								} else {
									entity.setDirectlyEnterpriseId(organization.getId());
								}

							} else {
								LOGGER.error("Organization not found, userId=" + userId + ", organizationId=" + id + ", type=" + type);
							}
							break;
						case ENTERPRISE:
							Enterprise enterprise = enterpriseProvider.findEnterpriseById(id);
							if(enterprise != null) {
								entity.setEntityName(enterprise.getName());
							} else {
								LOGGER.error("Enterprise not found, userId=" + userId + ", enterpriseId=" + id + ", type=" + type);
							}
							break;
						}
					} catch (Exception e) {
						LOGGER.error("Invalid entity id, userId=" + userId + ", entity=" + entity, e);
					}
				}
			}
		}

		return entityList;
	}
	@Override
	public UserLogin synThridUser(SynThridUserCommand cmd) {
		User user = this.checkThirdUserIsExist(cmd.getNamespaceId(), cmd.getNamespaceUserToken(), false);
		if(user == null){
			user = new User();
			user.setNickName(cmd.getUserName()==null?cmd.getNamespaceUserToken():cmd.getUserName());
			user.setStatus(UserStatus.ACTIVE.getCode());
			user.setPoints(0);
			user.setLevel((byte)1);
			user.setGender((byte)1);
			user.setNamespaceId(cmd.getNamespaceId());
			user.setNamespaceUserToken(cmd.getNamespaceUserToken());;
			userProvider.createUser(user);
		}

		//UserLogin login = createLogin(Namespace.DEFAULT_NAMESPACE, user, cmd.getSiteUri());
		UserLogin login = createLogin(cmd.getNamespaceId(), user, cmd.getDeviceIdentifier()==null?"":cmd.getDeviceIdentifier(), cmd.getPusherIdentify());
		login.setStatus(UserLoginStatus.LOGGED_IN);

		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("synThridUser-UserLogin="+StringHelper.toJsonString(login));
		}

		return login;
	}

	private User checkThirdUserIsExist(Integer namespaceId, String namespaceUserToken, boolean isThrowExcep) {
		User user = this.userProvider.findUserByNamespace(namespaceId, namespaceUserToken);
		if(user != null){
			LOGGER.error("user is exist.could not add.id="+user.getId()+", namespaceId=" + namespaceId
					+ ", namespaceUserToken=" + namespaceUserToken);
			if(isThrowExcep){
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
						"user is exist,could not add.");
			}
		}
		return user;
	}

	private void checkIsNull(SynThridUserCommand cmd) {
		if(cmd.getRandomNum() == null || cmd.getRandomNum().equals("")){
			LOGGER.error("randomNum not be null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"randomNum not be null");
		}
		if(cmd.getTimestamp() == null || cmd.getTimestamp().equals("")){
			LOGGER.error("timestamp not be null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"timestamp not be null");
		}
		if(cmd.getNamespaceUserToken() == null || cmd.getNamespaceUserToken().equals("")){
			LOGGER.error("siteUserToken not be null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"siteUserToken not be null");
		}
		if(cmd.getNamespaceId() == null){
			LOGGER.error("Namespace is null, namespaceId=" + cmd.getNamespaceId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Namespace is null");
		}
	}

	@Override
	public GetSignatureCommandResponse getThirdSignature(GetBizSignatureCommand cmd) {
		if(cmd.getNamespaceId() == null){
			LOGGER.error("Namespace is null, namespaceId=" + cmd.getNamespaceId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Namespace is null");
		}
		if(cmd.getNamespaceUserToken() == null || cmd.getNamespaceUserToken().equals("")){
			LOGGER.error("Namespace user token is null, token=" + cmd.getNamespaceUserToken());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"siteUserToken not be null");
		}

		User user = this.checkThirdUserIsExist(cmd.getNamespaceId(),cmd.getNamespaceUserToken(),true);
		return this.produceSignature(user);
	}

	@Override
	public UserInfo getUserInfoById(GetUserInfoByIdCommand cmd) {
		this.checkIsNull(cmd);
		this.checkSign(cmd);
		User queryUser = userProvider.findUserById(cmd.getId());
		if(queryUser == null){
			return null;
		}
		return getUserBasicInfoByQueryUser(queryUser, false);
	}

	private void checkSign(GetUserInfoByIdCommand cmd) {
		//2016-07-29:modify by liujinwne,parameter name don't be signed.

		String appKey = cmd.getZlAppKey();
		App app = appProvider.findAppByKey(appKey);
		if(app==null){
			LOGGER.error("app not found.appKey="+appKey);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"app not found.");
		}
		String signature = cmd.getZlSignature();
		Map<String,String> map = new HashMap<String, String>();
		map.put("appKey", appKey);
		map.put("id", cmd.getId()+"");
		//map.put("name", cmd.getName());
		map.put("randomNum", cmd.getRandomNum()+"");
		map.put("timeStamp", cmd.getTimeStamp()+"");
		String nsignature = SignatureHelper.computeSignature(map, app.getSecretKey());
		if(!nsignature.equals(signature)){
			LOGGER.error("check signature fail.nsign="+nsignature+",sign="+signature);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"check signature fail.");
		}
	}
	private void checkIsNull(GetUserInfoByIdCommand cmd) {
		//2016-07-29:modify by liujinwne,parameter name don't be signed.
		if(StringUtils.isEmpty(cmd.getZlSignature())||StringUtils.isEmpty(cmd.getZlAppKey())){
			LOGGER.error("zlSignature or zlAppKey is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"zlSignature or zlAppKey is null.");
		}
		if(cmd.getId()==null){
			LOGGER.error("id is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"id is null.");
		}
		if(cmd.getRandomNum()==null||cmd.getTimeStamp()==null){
			LOGGER.error("randomNum or timeStamp is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"randomNum or timeStamp is null.");
		}
	}
	/**
	 * 短信验证码测试接口，只有超级管理员可以调该接口来测试
	 */
	@Override
	public void sendUserTestSms(SendUserTestSmsCommand cmd) {
	    Integer namespaceId = cmd.getNamespaceId(); 
	    String phoneNumber = cmd.getPhoneNumber();
	    if(phoneNumber == null || phoneNumber.trim().length() == 0) {
	        LOGGER.error("Phone number should not be empty, namespaceId=" + namespaceId + ", phoneNumber=" + phoneNumber);
	        return;
	    }
	    namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
	    String value = configurationProvider.getValue(namespaceId, "sms.vcodetest.flag", "false");
	    if("true".equalsIgnoreCase(value)) {
	        String verificationCode = RandomGenerator.getRandomDigitalString(6);
	        sendVerificationCodeSms(namespaceId,this.getYzxRegionPhoneNumber(phoneNumber, cmd.getRegionCode()), verificationCode);
	    }
	}

	/**
	 * 发邮件测试接口，只有超级管理员可以调该接口来测试
	 */
	@Override
	public void sendUserTestMail(SendUserTestMailCommand cmd) {
		User user = UserContext.current().getUser();
		String from = cmd.getFrom();
		String to = cmd.getTo();
		String subject = cmd.getSubject();
		String body = cmd.getBody();
		String attachmet1 = cmd.getAttachment1();
		String attachmet2 = cmd.getAttachment2();
		List<String> attachmentList = new ArrayList<String>();
		if(attachmet1 != null && attachmet1.trim().length() > 0) {
			attachmentList.add(attachmet1);
		}
		if(attachmet2 != null && attachmet2.trim().length() > 0) {
			attachmentList.add(attachmet2);
		}

		String handlerName = MailHandler.MAIL_RESOLVER_PREFIX + MailHandler.HANDLER_JSMTP;
		MailHandler handler = PlatformContext.getComponent(handlerName);

		handler.sendMail(user.getNamespaceId(), from, to, subject, body, attachmentList);
	}

	@Override
	public RichLinkDTO sendUserRichLinkMessage(SendUserTestRichLinkMessageCommand cmd) {
		RichLinkDTO linkDto = ConvertHelper.convert(cmd, RichLinkDTO.class);
		if(linkDto.getCoverUrl() == null && cmd.getCoverUri() != null) {
			String url = contentServerService.parserUri(cmd.getCoverUri(), EntityType.USER.getCode(), User.SYSTEM_UID);
			if(url != null) {
				linkDto.setCoverUrl(url);
			}
		}

		String targetPhone = cmd.getTargetPhone();
		if(targetPhone == null || targetPhone.trim().length() == 0) {
			LOGGER.error("User not found for the phone, cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "User not found for the phone");
		}

		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getTargetNamespaceId());
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, targetPhone);
		if(userIdentifier == null) {
			LOGGER.error("User not found for the phone(identifier), cmd={}", cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "User not found for the phone");
		}

		String targetUserId = String.valueOf(userIdentifier.getOwnerUid());
		MessageDTO messageDto = new MessageDTO();
		messageDto.setAppId(AppConstants.APPID_MESSAGING);
		messageDto.setSenderUid(User.SYSTEM_UID);
		messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), targetUserId));
		messageDto.setBodyType(MessageBodyType.RICH_LINK.getCode());
		messageDto.setBody(StringHelper.toJsonString(linkDto));
		messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);
		Map<String, String> meta = messageDto.getMeta();
		if(meta == null) {
			meta = new HashMap<String, String>();
		}
		meta.put(MessageMetaConstant.POPUP_FLAG, String.valueOf(MessagePopupFlag.POPUP.getCode()));
		messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
				targetUserId, messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());

		return linkDto;
	}

	@Override
	public UserLogin innerLogin(Integer namespaceId, Long userId,String deviceIdentifier, String pusherIdentify){
		User user = userProvider.findUserById(userId);
		if(user == null){
			LOGGER.error("user not found.userId="+userId);
			return null;
		}
		UserLogin login = createLogin(namespaceId, user, deviceIdentifier, pusherIdentify);
		login.setStatus(UserLoginStatus.LOGGED_IN);

		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("innerLogin-UserLogin="+StringHelper.toJsonString(login));
		}
		return login;
	}
	@Override
	public List<UserInfo> listUserByKeyword(String keyword) {
		List<UserInfo> userInfos = new ArrayList<UserInfo>();
		List<User> users = userProvider.listUserByNickNameOrIdentifier(keyword);
		if(users!=null&&!users.isEmpty()){
			for(User user : users){
				userInfos.add(getUserBasicInfoByQueryUser(user,false));
			}
		}
		return userInfos;
	}
	@Override
	public List<User> listUserByIdentifier(String identifier) {
		List<User> users = new ArrayList<User>();
		List<UserIdentifier> userIdentifiers = userProvider.listUserIdentifierByIdentifier(identifier);
		if(userIdentifiers!=null&&!userIdentifiers.isEmpty()){
			for(UserIdentifier r : userIdentifiers){
				User user = userProvider.findUserById(r.getOwnerUid());
				if(user!=null)
					users.add(user);
			}
		}
		return users;
	}
	@Override
	public List<UserInfo> listUserInfoByIdentifier(String identifier) {
		List<UserInfo> userInfos = new ArrayList<UserInfo>();
		List<User> users = listUserByIdentifier(identifier);
		if(users!=null&&!users.isEmpty()){
			for(User r : users){
				userInfos.add(getUserBasicInfoByQueryUser(r,false));
			}
		}
		return userInfos;
	}

	@Override
	public List<SceneDTO> listUserRelatedScenes() {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		Long userId = UserContext.current().getUser().getId();

		List<SceneDTO> sceneList = new ArrayList<SceneDTO>();

		// 处于家庭对应的场景
		// 列出用户有效家庭 mod by xiongying 20160523
		List<FamilyDTO> familyList = this.familyProvider.getUserFamiliesByUserId(userId);
		toFamilySceneDTO(namespaceId, userId, sceneList, familyList);

		// 处于某个公司对应的场景
		OrganizationGroupType groupType = OrganizationGroupType.ENTERPRISE;
		List<OrganizationDTO> organizationList = organizationService.listUserRelateOrganizations(namespaceId, userId, groupType);
		//toOrganizationSceneDTO(sceneList, enterpriseList);
		for(OrganizationDTO orgDto : organizationList) {
			String orgType = orgDto.getOrganizationType();
			// 在园区通用版和左邻小区版合并后，改为按域空间判断，0域空间不只列物业公司场景 by lqs 20160517
			//if(isCmntyScene) { // 小区版只列物业公司的场景，园区版则列所有公司的场景  by lqs 20160510
			//	        if(OrganizationType.isGovAgencyOrganization(orgType)) {
			//	            SceneDTO sceneDto = toOrganizationSceneDTO(namespaceId, userId, orgDto, SceneType.PM_ADMIN);
			//	            if(sceneDto != null) {
			//	                sceneList.add(sceneDto);
			//	            }
			//	        } else {
			//	            if(LOGGER.isDebugEnabled()) {
			//	                LOGGER.debug("Ignore the organization for it is not govagency type, userId=" + userId 
			//	                    + ", organizationId=" + orgDto.getId() + ", orgType=" + orgType);
			//	            }
			//	        } else {
			//                SceneType sceneType = SceneType.PARK_PM_ADMIN;
			//                if(!OrganizationType.isGovAgencyOrganization(orgType)) {
			//                    if(OrganizationMemberStatus.fromCode(orgDto.getMemberStatus()) == OrganizationMemberStatus.ACTIVE) {
			//                        sceneType = SceneType.PARK_ENTERPRISE;
			//                    } else {
			//                        sceneType = SceneType.PARK_ENTERPRISE_NOAUTH;
			//                    }
			//                }
			//                SceneDTO sceneDto = toOrganizationSceneDTO(namespaceId, userId, orgDto, sceneType);
			//                if(sceneDto != null) {
			//                    sceneList.add(sceneDto);
			//                }
			//	        }
			SceneType sceneType = SceneType.PM_ADMIN;
			if(!OrganizationType.isGovAgencyOrganization(orgType)) {
				if(OrganizationMemberStatus.fromCode(orgDto.getMemberStatus()) == OrganizationMemberStatus.ACTIVE) {
					sceneType = SceneType.ENTERPRISE;
				} else {
					sceneType = SceneType.ENTERPRISE_NOAUTH;
				}
			} 
			SceneDTO sceneDto = toOrganizationSceneDTO(namespaceId, userId, orgDto, sceneType);
			if(sceneDto != null) {
				sceneList.add(sceneDto);
			}
		}

		// 当用户既没有选择家庭、也没有在某个公司内时，他有可能选过某个小区/园区，此时也把对应域空间下所选的小区作为场景 by lqs 2010416
		if(sceneList.size() == 0) {
			SceneDTO communityScene = getCurrentCommunityScene(namespaceId, userId);
			if(communityScene != null) {
				sceneList.add(communityScene);
			}
		}

		return sceneList;
	}

	private SceneDTO getCurrentCommunityScene(Integer namespaceId, Long userId) {
		SceneDTO communityScene = null;

		UserCurrentEntityType[] entityTypes = new UserCurrentEntityType[]{
				UserCurrentEntityType.COMMUNITY_RESIDENTIAL, 
				UserCurrentEntityType.COMMUNITY_COMMERCIAL, 
				UserCurrentEntityType.COMMUNITY};
		for(UserCurrentEntityType entityType : entityTypes) {
			UserProfile profile = userActivityProvider.findUserProfileBySpecialKey(userId, entityType.getUserProfileKey());
			if(profile != null && profile.getIntegralTag2() != null && profile.getIntegralTag2().intValue() == namespaceId.intValue()) {
				Long communityId = null;
				try {
					communityId = Long.parseLong(profile.getItemValue());
				} catch (Exception e) {
					LOGGER.error("Failed to parse community id in user profile, profile={}", profile, e);
				}

				if(communityId != null) {
					Community community = communityProvider.findCommunityById(communityId);
					if(community != null) {
						CommunityDTO communityDTO = ConvertHelper.convert(community, CommunityDTO.class);

						SceneType sceneType = SceneType.DEFAULT;
						CommunityType communityType = CommunityType.fromCode(community.getCommunityType());
						if(communityType == CommunityType.COMMERCIAL) {
							sceneType = SceneType.PARK_TOURIST;
						}

						communityScene = toCommunitySceneDTO(namespaceId, userId, communityDTO, sceneType);
					}
				}
				break;
			}
		}

		return communityScene;
	}

	@Override
	public void toFamilySceneDTO(Integer namespaceId, Long userId, List<SceneDTO> sceneList, List<FamilyDTO> familyDtoList) {
		SceneDTO sceneDto = null;
		if(familyDtoList != null && familyDtoList.size() > 0) {
			for(FamilyDTO familyDto : familyDtoList) {
				sceneDto = toFamilySceneDTO(namespaceId, userId, familyDto);
				if(sceneDto != null) {
					sceneList.add(sceneDto);
				}
			}
		} else {
			if(LOGGER.isWarnEnabled()) {
				LOGGER.warn("No family is found for the scene, namespaceId=" + namespaceId + ", userId=" + userId);
			}
		}
	}

	@Override
	public SceneDTO toFamilySceneDTO(Integer namespaceId, Long userId, FamilyDTO familyDto) {
		SceneDTO sceneDto = new SceneDTO();

		// 增加场景类型到sceneDTO中，使得客户端不需要使用EntityType来作场景 by lqs 20160510
		sceneDto.setSceneType(SceneType.FAMILY.getCode());

		sceneDto.setEntityType(UserCurrentEntityType.FAMILY.getCode());
		StringBuffer fullName = new StringBuffer();
		StringBuffer aliasName = new StringBuffer();

		if(!StringUtils.isEmpty(familyDto.getCityName())){
			fullName.append(familyDto.getCityName());
		}
		if(!StringUtils.isEmpty(familyDto.getAreaName())){
			fullName.append(familyDto.getAreaName());
		}
		if(!StringUtils.isEmpty(familyDto.getCommunityName())){
			fullName.append(familyDto.getCommunityName());
			aliasName.append(familyDto.getCommunityName());
		}
		if(!StringUtils.isEmpty(familyDto.getName())){
			fullName.append(familyDto.getName());
			aliasName.append(familyDto.getName());
		}
		sceneDto.setName(fullName.toString());
		sceneDto.setAliasName(aliasName.toString());
		sceneDto.setAvatar(familyDto.getAvatarUri());
		sceneDto.setAvatarUrl(familyDto.getAvatarUrl());

		String entityContent = StringHelper.toJsonString(familyDto);
		sceneDto.setEntityContent(entityContent);

		SceneTokenDTO sceneTokenDto = toSceneTokenDTO(namespaceId, userId, familyDto, SceneType.FAMILY);
		String sceneToken = WebTokenGenerator.getInstance().toWebToken(sceneTokenDto);
		sceneDto.setSceneToken(sceneToken);

		return sceneDto;
	}

	@Override
	public SceneTokenDTO toSceneTokenDTO(Integer namespaceId, Long userId, FamilyDTO familyDto, SceneType sceneType) {
		SceneTokenDTO sceneTokenDto = new SceneTokenDTO();
		sceneTokenDto.setEntityType(UserCurrentEntityType.FAMILY.getCode());
		sceneTokenDto.setScene(sceneType.getCode());
		sceneTokenDto.setEntityId(familyDto.getId());
		sceneTokenDto.setNamespaceId(namespaceId);
		sceneTokenDto.setUserId(userId);

		return sceneTokenDto;
	}

	@Override
	public void toOrganizationSceneDTO(Integer namespaceId, Long userId, List<SceneDTO> sceneList, 
			List<OrganizationDTO> organizationDtoList, SceneType sceneType) {
		SceneDTO sceneDto = null;
		if(organizationDtoList != null && organizationDtoList.size() > 0) {
			for(OrganizationDTO orgDto : organizationDtoList) {
				sceneDto = toOrganizationSceneDTO(namespaceId, userId, orgDto, sceneType);
				if(sceneDto != null) {
					sceneList.add(sceneDto);
				}
			}
		} else {
			if(LOGGER.isWarnEnabled()) {
				LOGGER.warn("No family is found for the scene, namespaceId=" + namespaceId + ", userId=" + userId);
			}
		}
	}

    public static void main(String[] args) {
        System.out.println(GeoHashUtils.encode(22.322272, 114.043532));
    }

	@Override
	public SceneDTO toOrganizationSceneDTO(Integer namespaceId, Long userId, OrganizationDTO organizationDto, SceneType sceneType) {
		SceneDTO sceneDto = new SceneDTO();

		// 增加场景类型到sceneDTO中，使得客户端不需要使用EntityType来作场景 by lqs 20160510
		sceneDto.setSceneType(sceneType.getCode());

		sceneDto.setEntityType(UserCurrentEntityType.ORGANIZATION.getCode());
		sceneDto.setName(organizationDto.getName().trim());
		// 在园区先暂时优先显示园区名称，后面再考虑怎样显示公司名称 by lqs 20160514
		String aliasName = organizationDto.getDisplayName();
		//if(sceneType.getCode().contains("park") && organizationDto.getCommunityName() != null) {
		//    aliasName = organizationDto.getCommunityName();
		//}
		// 在园区通用版与左邻小区版合并后，只要不是物业公司，则优先显示小区/园区名称 by lqs 20160517
		// 不管什么公司都要显示本公司的简称 by sfyan 20170606
//		String orgType = organizationDto.getOrganizationType();
//		if(!OrganizationType.isGovAgencyOrganization(orgType)) {
//			aliasName = organizationDto.getCommunityName();
//		}
        if (aliasName == null || aliasName.trim().isEmpty()) {
            aliasName = organizationDto.getName().trim();
        }
        sceneDto.setAliasName(aliasName);
		sceneDto.setAvatar(organizationDto.getAvatarUri());
		sceneDto.setAvatarUrl(organizationDto.getAvatarUrl());

		String entityContent = StringHelper.toJsonString(organizationDto);
		sceneDto.setEntityContent(entityContent);

		SceneTokenDTO sceneTokenDto = toSceneTokenDTO(namespaceId, userId, organizationDto, sceneType);
		String sceneToken = WebTokenGenerator.getInstance().toWebToken(sceneTokenDto);
		sceneDto.setSceneToken(sceneToken);

		return sceneDto;
	}

	//	private SceneType getOrganizationSceneType(Integer namespaceId, Long userId, OrganizationDTO organizationDto) {
	//	    boolean isCmntyScene = isCommunityScene(userId, namespaceId);
	//	    if(isCmntyScene) {
	//	        OrganizationType orgType = OrganizationType.fromCode(organizationDto.getOrganizationType());
	//	        if(orgType == OrganizationType.PM) {
	//	            
	//	        }
	//	    }
	//	}

	@Override
	public SceneTokenDTO toSceneTokenDTO(Integer namespaceId, Long userId, OrganizationDTO organizationDto, SceneType sceneType) {
		SceneTokenDTO sceneTokenDto = new SceneTokenDTO();
		sceneTokenDto.setEntityType(UserCurrentEntityType.ORGANIZATION.getCode());
		sceneTokenDto.setScene(sceneType.getCode());
		sceneTokenDto.setEntityId(organizationDto.getId());
		sceneTokenDto.setNamespaceId(namespaceId);
		sceneTokenDto.setUserId(userId);

		return sceneTokenDto;
	}

	public GetUserRelatedAddressResponse getUserRelatedAddresses(GetUserRelatedAddressCommand cmd) {
		User user = UserContext.current().getUser();
		Long userId = user.getId();
		Integer namespaceId = UserContext.getCurrentNamespaceId();

		checkSceneToken(userId, cmd.getSceneToken());

		GetUserRelatedAddressResponse response = new GetUserRelatedAddressResponse();
		List<FamilyDTO> familyList = familyService.getUserOwningFamilies();
		response.setFamilyList(familyList);

		OrganizationGroupType groupType = OrganizationGroupType.ENTERPRISE;
		List<OrganizationDTO> organizationList = organizationService.listUserRelateOrganizations(namespaceId, userId, groupType);

		// 把园区场景也支持之后，普通公司的地址也需要显示出来，故不过进行物业公司的过滤 by lqs 20160513
		//	    List<OrganizationDTO> organizations = new ArrayList<OrganizationDTO>();
		//	    for(OrganizationDTO orgDto : organizationList) {
		//	        String orgType = orgDto.getOrganizationType();
		//	        if(OrganizationType.isGovAgencyOrganization(orgType)) {
		//	        	organizations.add(orgDto);
		//	        } else {
		//	            if(LOGGER.isDebugEnabled()) {
		//	                LOGGER.debug("Ignore the organization for it is not govagency type, userId=" + userId 
		//	                    + ", organizationId=" + orgDto.getId() + ", orgType=" + orgType);
		//	            }
		//	        }
		//        }

		response.setOrganizationList(organizationList);

		return response;
	}

	@Override
	public SceneTokenDTO checkSceneToken(Long userId, String sceneToken) {
		SceneTokenDTO sceneTokenDto = null;

		try {
			sceneTokenDto = WebTokenGenerator.getInstance().fromWebToken(sceneToken, SceneTokenDTO.class);

			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("Parse scene token, userId={}, sceneToken={}", userId, sceneTokenDto);
			}
		} catch(Exception e) {
			LOGGER.error("Invalid scene token, userId=" + userId + ", sceneToken=" + sceneToken, e);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_SCENE_TOKEN, 
					"Invalid scene token");
		}

		if(sceneTokenDto == null) {
			LOGGER.error("Scene token is null, userId=" + userId + ", sceneToken=" + sceneToken);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_SCENE_TOKEN, 
					"Invalid scene token");
		}

		SceneType sceneType = SceneType.fromCode(sceneTokenDto.getScene());
		if(sceneType == null) {
			LOGGER.error("Scene type is null, userId=" + userId + ", sceneToken=" + sceneToken + ", sceneTokenDto=" + sceneTokenDto);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_SCENE_TOKEN, 
					"Invalid scene token");
		}

		UserCurrentEntityType userEntityType = UserCurrentEntityType.fromCode(sceneTokenDto.getEntityType());
		if(userEntityType == null) {
			LOGGER.error("User entity type is null, userId=" + userId + ", sceneToken=" + sceneToken + ", sceneTokenDto=" + sceneTokenDto);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_SCENE_TOKEN, 
					"Invalid scene token");
		}

		return sceneTokenDto;
	}

	@Override
	public List<SceneDTO> setCurrentCommunityForScene(SetCurrentCommunityForSceneCommand cmd) {
		User user = UserContext.current().getUser();
		Long userId = user.getId();
		Integer namespaceId = UserContext.getCurrentNamespaceId();

		Long communityId = cmd.getCommunityId();
		if(communityId == null) {
			LOGGER.error("Community id may not be null, userId={}, namespaceId={}, cmd={}", userId, namespaceId, cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Community id may not be null");
		}

		Community community = communityProvider.findCommunityById(communityId);
		if(community == null) {
			LOGGER.error("Community not found, userId={}, namespaceId={}, cmd={}", userId, namespaceId, cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"Community not found");
		}

		// 为了避免用户每次都需要选择一个小区，需要调用原来的设置小区流程 by lqs 20160416
		setUserCurrentCommunity(cmd.getCommunityId());

		// 把下面代码移到listUserRelatedScenes()里做为通用流程，即列场景时按统一条件列小区场景 by lqs 20160416 
		//        CommunityDTO communityDTO = ConvertHelper.convert(community, CommunityDTO.class);
		//        List<SceneDTO> sceneList = new ArrayList<SceneDTO>();
		//        SceneDTO communityScene = toCommunitySceneDTO(namespaceId, userId, communityDTO);
		//	    sceneList.add(communityScene);

		return listUserRelatedScenes();
	}

	@Override
	public SceneTokenDTO toSceneTokenDTO(Integer namespaceId, Long userId, CommunityDTO community, SceneType sceneType) {
		SceneTokenDTO sceneTokenDto = new SceneTokenDTO();
		sceneTokenDto.setEntityType(UserCurrentEntityType.COMMUNITY.getCode());
		sceneTokenDto.setScene(sceneType.getCode());
		sceneTokenDto.setEntityId(community.getId());
		sceneTokenDto.setNamespaceId(namespaceId);
		sceneTokenDto.setUserId(userId);

		return sceneTokenDto;
	}

	@Override
	public SceneDTO toCommunitySceneDTO(Integer namespaceId, Long userId, CommunityDTO community, SceneType sceneType) {
		StringBuffer fullName = new StringBuffer();
		StringBuffer aliasName = new StringBuffer();

		if(!StringUtils.isEmpty(community.getCityName())){
			fullName.append(community.getCityName());
		}
		if(!StringUtils.isEmpty(community.getAreaName())){
			fullName.append(community.getAreaName());
		}
		if(!StringUtils.isEmpty(community.getName())){
			fullName.append(community.getName());
			aliasName.append(community.getName());
		}

		SceneDTO sceneDto = new SceneDTO();
		sceneDto.setSceneType(sceneType.getCode());
		sceneDto.setEntityType(UserCurrentEntityType.COMMUNITY.getCode());
		sceneDto.setName(fullName.toString());
		sceneDto.setAliasName(aliasName.toString());

		String entityContent = StringHelper.toJsonString(community);
		sceneDto.setEntityContent(entityContent);

		SceneTokenDTO sceneTokenDto = toSceneTokenDTO(namespaceId, userId, community, sceneType);
		String sceneToken = WebTokenGenerator.getInstance().toWebToken(sceneTokenDto);
		sceneDto.setSceneToken(sceneToken);

		return sceneDto;
	}

    /**
     * 判断是否是小区版场景（相对于园区版）
     * @param userId 用户ID
     * @param namespaceId 用户所在的域空间ID
     * @return 如果是小区
     */
    private boolean isCommunityScene(Long userId, Integer namespaceId) {
        if(namespaceId == null) {
            return false;
        } else {
            NamespaceDetail namespaceDetail = namespaceResourceProvider.findNamespaceDetailByNamespaceId(namespaceId);
            if(namespaceDetail != null) {
                NamespaceCommunityType communityType = NamespaceCommunityType.fromCode(namespaceDetail.getResourceType());
                if(communityType != null) {
                    switch(communityType) {
                    case COMMUNITY_RESIDENTIAL:
                    case COMMUNITY_MIX:
                        return true;
                    case COMMUNITY_COMMERCIAL:
                        return false;
                    }
                }
            }
            
            // 在没有配置时，默认使用域空间ID来判断
            return (Namespace.DEFAULT_NAMESPACE == namespaceId.intValue());
        }
    }
    
    /**
     * 用户实时状态信息
     */
    @Override
    public UserLoginResponse listLoginsByPhone(ListLoginByPhoneCommand cmd) {
        User user = null;
        try {
            if(cmd.getPhone() != null && cmd.getPhone().length() < 9) {
                Long id = Long.valueOf(cmd.getPhone());
                user = this.userProvider.findUserById(id);
            }    
        } catch(Exception ex) {
         LOGGER.info("try userId not found", ex);
        }
        
        if(user == null) {
            user = this.findUserByIndentifier(cmd.getNamespaceId(), cmd.getPhone());    
        }
        
        if(user == null) {
            //throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_CLASS_NOT_FOUND, "User not found!");
            return new UserLoginResponse();
        }
        
        Map<String, Long> deviceMap = new HashMap<String, Long>();
        List<UserLogin> logins = this.listUserLogins(user.getId());
        List<UserLoginDTO> dtos = logins.stream().map((r) -> { return r.toDto(); }).collect(Collectors.toList());
        
        for(UserLoginDTO dto : dtos) {
            if(dto.getDeviceIdentifier() != null && !dto.getDeviceIdentifier().isEmpty()) {
                Device device = deviceProvider.findDeviceByDeviceId(dto.getDeviceIdentifier());
                
                if(device != null) {
                    deviceMap.put(device.getDeviceId(), 0l);
                    dto.setDeviceType(device.getPlatform());
                }
            }
            
            dto.setLastPushPing(0l);
            if(dto.getDeviceType() == null) {
                dto.setDeviceType("other");
            }
            
            if(dto.getLoginBorderId() != null) {
                dto.setIsOnline((byte)1);
                BorderConnection conn = borderConnectionProvider.getBorderConnection(dto.getLoginBorderId());
                if(conn != null) {
                    dto.setBorderStatus(conn.getConnectionState());
                    }
            } else {
            dto.setIsOnline((byte)0);    
            }
        }
        
        deviceMap = pusherService.requestDevices(deviceMap);
        for(UserLoginDTO dto : dtos) {
            if(dto.getDeviceIdentifier() != null && !dto.getDeviceIdentifier().isEmpty()) {
                Long last = deviceMap.get(dto.getDeviceIdentifier());
                if(last != null) {
                    dto.setLastPushPing(last);
                }
            }
        }
        
        UserLoginResponse resp = new UserLoginResponse();
        resp.setLogins(dtos);
        
        return resp;
    }
    
    private void sendMessageToUser(Long userId, String body, MessagingConstants flag) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_UID);
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()), 
                new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(body);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);

        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
                userId.toString(), messageDto, flag.getCode());        
    }
    
    @Override
    public String sendMessageTest(SendMessageTestCommand cmd) {
        String body = "test message " + Double.valueOf(Math.random());
        sendMessageToUser(cmd.getUserId(), body, MessagingConstants.MSG_FLAG_STORED);
        return body;
    }
    
    @Override
    public String pushMessageTest(SendMessageTestCommand cmd) {
        Message msg = new Message();
        msg.setAppId(AppConstants.APPID_MESSAGING);
        msg.setSenderUid(User.SYSTEM_UID);
        msg.setChannelToken(cmd.getUserId().toString());
        msg.setChannelType(MessageChannelType.USER.toString());
        msg.setContent("test push " + Double.valueOf(Math.random()));
        msg.setMetaAppId(AppConstants.APPID_MESSAGING);
        msg.setCreateTime(System.currentTimeMillis());
        Integer namespaceId = cmd.getNamespaceId();
        if(namespaceId == null) {
            namespaceId = 0;
        }
        msg.setNamespaceId(namespaceId);
        
        Map<String, String> meta = new HashMap<String, String>();
        meta.put("bodyType", "TEXT");
        msg.setMeta(meta);
        
        List<UserLogin> logins = this.listUserLogins(cmd.getUserId());
        for(UserLogin login : logins) {
            if(cmd.getLoginId().equals(login.getLoginId())) {
                this.pusherService.pushMessage(User.SYSTEM_USER_LOGIN, login, 0, msg);
            }
        }
        
        return msg.getContent();
    }
    
    @Override
    public BorderListResponse listBorders() {
        BorderListResponse resp = new BorderListResponse();
        List<String> strs = new ArrayList<String>();
        List<Border> borders = this.borderProvider.listAllBorders();
        for(Border border : borders) {
            strs.add(String.format("%s:%d", border.getPublicAddress(), border.getPublicPort()));
        }
        
        resp.setBorders(strs);
        
        return resp;
    }
    
    @Override
    public UserImpersonationDTO createUserImpersonation(CreateUserImpersonationCommand cmd) {
        User owner = this.findUserByIndentifier(cmd.getNamespaceId(), cmd.getOwnerPhone());
        User target = this.findUserByIndentifier(cmd.getNamespaceId(), cmd.getTargetPhone());
        if(owner == null || target == null) {
            return null;
        }
        UserImpersonation obj = new UserImpersonation();
        obj.setDescription(cmd.getDescription());
        obj.setNamespaceId(cmd.getNamespaceId());
        obj.setOwnerId(owner.getId());
        obj.setTargetId(target.getId());
        obj.setOwnerType(EntityType.USER.getCode());
        obj.setTargetType(EntityType.USER.getCode());
        obj.setStatus(UserStatus.ACTIVE.getCode());
        this.userImpersonationProvider.createUserImpersonation(obj);
        
        return ConvertHelper.convert(obj, UserImpersonationDTO.class);
    }
    
    @Override
    public void deleteUserImpersonation(DeleteUserImpersonationCommand cmd) {
        UserImpersonation obj = new UserImpersonation();
        obj.setId(cmd.getUserImpersonationId());
        this.userImpersonationProvider.deleteUserImpersonation(obj);
    }
    
    @Override
    public SearchUserImpersonationResponse listUserImpersons(SearchUserImpersonationCommand cmd) {
        SearchUserImpersonationResponse resp = new SearchUserImpersonationResponse();
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getAnchor());
        int count = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        List<UserImperInfo> impers = this.userImpersonationProvider.searchUserByPhone(cmd.getNamespaceId(), cmd.getPhone(), cmd.getImperOnly(), locator, count);
        for(UserImperInfo info : impers) {
            if(info.getOwnerId() != null && info.getTargetId() != null) {
                UserInfo u1 = this.getUserBasicInfo(info.getOwnerId(), false);
                UserInfo u2 = this.getUserBasicInfo(info.getTargetId(), false);
                if(u1 != null && u2 != null) {
                    if(u1.getId().equals(info.getId())) {
                        //ownerId match
                        info.setPhone(u1.getPhones().get(0));
                        info.setTargetPhone(u2.getPhones().get(0));
                    } else {
                        //target match
                        info.setPhone(u2.getPhones().get(0));
                        info.setTargetPhone(u1.getPhones().get(0));
                    }
                }
            }
        }
        resp.setImpersonations(impers);
        resp.setNextPageAnchor(locator.getAnchor());
        return resp;
    }

	@Override
	public SearchContentsBySceneReponse searchContentsByScene(
			SearchContentsBySceneCommand cmd) {
		long startTime = System.currentTimeMillis();
		User user = UserContext.current().getUser();
		Long userId = user.getId();
		Integer namespaceId = UserContext.getCurrentNamespaceId(); 
		//	    SceneTokenDTO sceneToken = checkSceneToken(userId, cmd.getSceneToken());

		if(StringUtils.isEmpty(cmd.getContentType())) {
			cmd.setContentType(SearchContentType.ALL.getCode());
		}
		SearchContentType contentType = SearchContentType.fromCode(cmd.getContentType());

		SearchContentsBySceneReponse response = new SearchContentsBySceneReponse();
		switch(contentType) {
		case ACTIVITY:
		case POLL:
		case TOPIC:
			response = forumService.searchContents(cmd, contentType);
			break;

		case NEWS:
			response = newsService.searchNewsByScene(cmd);
			break;
		case LAUNCHPADITEM:
			response = launchPadService.searchLaunchPadItemByScene(cmd);
			break;
		case SHOP:
			response = businessService.searchShops(cmd);
			break;
		case ALL:
			int pageSize = (int)configProvider.getIntValue("search.content.size", 3);
			cmd.setPageSize(pageSize);

			List<ContentBriefDTO> dtos = new ArrayList<ContentBriefDTO>();
			List<LaunchPadItemDTO> itemDtos = new ArrayList<LaunchPadItemDTO>();
			List<ShopDTO> shopDtos = new ArrayList<ShopDTO>();
			response.setDtos(dtos);
			response.setLaunchPadItemDtos(itemDtos);
			response.setShopDTOs(shopDtos);

			SearchTypes searchType = userActivityProvider.findByContentAndNamespaceId(namespaceId, SearchContentType.ACTIVITY.getCode());
			if(searchType == null){
				searchType = userActivityProvider.findByContentAndNamespaceId(0, SearchContentType.ACTIVITY.getCode());
			}
			if(searchType != null) {
				if(forumService.searchContents(cmd, SearchContentType.ACTIVITY) != null 
						&& forumService.searchContents(cmd, SearchContentType.ACTIVITY).getDtos() != null) {
					response.getDtos().addAll(forumService.searchContents(cmd, SearchContentType.ACTIVITY).getDtos());	
				}
			}

			searchType = userActivityProvider.findByContentAndNamespaceId(namespaceId, SearchContentType.POLL.getCode());
			if(searchType == null){
				searchType = userActivityProvider.findByContentAndNamespaceId(0, SearchContentType.POLL.getCode());
			}
			if(searchType != null) {
				if(forumService.searchContents(cmd, SearchContentType.POLL) != null 
						&& forumService.searchContents(cmd, SearchContentType.POLL).getDtos() != null) {
					response.getDtos().addAll(forumService.searchContents(cmd, SearchContentType.POLL).getDtos());	
				}
			}
			
			searchType = userActivityProvider.findByContentAndNamespaceId(namespaceId, SearchContentType.TOPIC.getCode());
			if(searchType == null){
				searchType = userActivityProvider.findByContentAndNamespaceId(0, SearchContentType.TOPIC.getCode());
			}
			if(searchType != null) {
				if(forumService.searchContents(cmd, SearchContentType.TOPIC) != null 
						&& forumService.searchContents(cmd, SearchContentType.TOPIC).getDtos() != null) {
					response.getDtos().addAll(forumService.searchContents(cmd, SearchContentType.TOPIC).getDtos());	
				}
			}

			searchType = userActivityProvider.findByContentAndNamespaceId(namespaceId, SearchContentType.NEWS.getCode());
			if(searchType == null){
				searchType = userActivityProvider.findByContentAndNamespaceId(0, SearchContentType.NEWS.getCode());
			}
			if(searchType != null) {
				if(newsService.searchNewsByScene(cmd) != null 
						&& newsService.searchNewsByScene(cmd).getDtos() != null) {
					response.getDtos().addAll(newsService.searchNewsByScene(cmd).getDtos());
				}
			}
			
			//查询应用 add by yanjun 20170419
			searchType = userActivityProvider.findByContentAndNamespaceId(namespaceId, SearchContentType.LAUNCHPADITEM.getCode());
			if(searchType == null){
				searchType = userActivityProvider.findByContentAndNamespaceId(0, SearchContentType.LAUNCHPADITEM.getCode());
			}
			if(searchType != null) {
				 SearchContentsBySceneReponse tempResp = launchPadService.searchLaunchPadItemByScene(cmd);
				if( tempResp != null 
						&& tempResp.getLaunchPadItemDtos() != null) {
					response.getLaunchPadItemDtos().addAll(tempResp.getLaunchPadItemDtos());
				}
			}
			
			//查询电商店铺 add by yanjun 20170419
			searchType = userActivityProvider.findByContentAndNamespaceId(namespaceId, SearchContentType.SHOP.getCode());
			if(searchType == null){
				searchType = userActivityProvider.findByContentAndNamespaceId(0, SearchContentType.SHOP.getCode());
			}
			if(searchType != null) {
				SearchContentsBySceneReponse tempResp = businessService.searchShops(cmd);
				if(tempResp != null 
						&& tempResp.getShopDTOs() != null) {
					response.getShopDTOs().addAll(tempResp.getShopDTOs());
				}
			}
			

			break;

		default:
			LOGGER.error("Unsupported content type for search, contentType=" + cmd.getContentType());
			break;
		}

		if(LOGGER.isDebugEnabled()) {
			long endTime = System.currentTimeMillis();
			LOGGER.debug("search contents by scene, userId={}, namespaceId={}, elapse={}, cmd={}", 
					userId, namespaceId, (endTime - startTime), cmd);
		}
		return response;
	}

	@Override
	public ListSearchTypesBySceneReponse listSearchTypesByScene(
			ListSearchTypesBySceneCommand cmd) {
		User user = UserContext.current().getUser();
		Long userId = user.getId();
		Integer namespaceId = UserContext.getCurrentNamespaceId();

		SceneTokenDTO sceneToken = checkSceneToken(userId, cmd.getSceneToken());

		//先按域空间查，ownerid和ownertype暂时不用
		ListSearchTypesBySceneReponse response = new ListSearchTypesBySceneReponse();
		response.setSearchTypes(new ArrayList<SearchTypeDTO>());
		List<SearchTypes> searchTypes = userActivityProvider.listByNamespaceId(namespaceId);

		//域空间下没配的话则返回左邻域下的作为默认 add by xiongying20170306
		if(searchTypes == null || searchTypes.size() == 0) {
			searchTypes = userActivityProvider.listByNamespaceId(0);
		}

		if(searchTypes != null && searchTypes.size() > 0) {
			response.getSearchTypes().addAll(searchTypes.stream().map(r -> {
				SearchTypeDTO dto = ConvertHelper.convert(r, SearchTypeDTO.class);
				return dto;
			}).collect(Collectors.toList()));
		}
		return response;
	}

	// 移自WebRequestInterceptor并改为public方法，使得其它地方也可以调用 by lqs 20160922
	public boolean isValid(LoginToken token) {
		if(token == null) {
			User user = UserContext.current().getUser();
			Long userId = -1L;
			if(user != null) {
				userId = user.getId();
			}
			//          It's ok when using signature
			//          LOGGER.error("Invalid token, token={}, userId={}", token, userId);
			return false;
		}

		return this.isValidLoginToken(token);
	}

	// 移自WebRequestInterceptor并改为public方法，使得其它地方也可以调用 by lqs 20160922
	public LoginToken getLoginToken(HttpServletRequest request) {
		Map<String, String[]> paramMap = request.getParameterMap();
		String loginTokenString = null;
		for(Map.Entry<String, String[]> entry : paramMap.entrySet()) {
			String value = StringUtils.join(entry.getValue(), ",");
			if(LOGGER.isTraceEnabled())
				LOGGER.trace("HttpRequest param " + entry.getKey() + ": " + value);
			if(entry.getKey().equals("token"))
				loginTokenString = value;
		}

		if(loginTokenString == null) {
			if(request.getCookies() != null) {
				List<Cookie> matchedCookies = new ArrayList<>();

				for(Cookie cookie : request.getCookies()) {
					if(LOGGER.isTraceEnabled())
						LOGGER.trace("HttpRequest cookie " + cookie.getName() + ": " + cookie.getValue() + ", path: " + cookie.getPath());

					if(cookie.getName().equals("token")) {
						matchedCookies.add(cookie);
					}
				}

				if(matchedCookies.size() > 0)
					loginTokenString = matchedCookies.get(matchedCookies.size() - 1).getValue();
			}
		}

		if(loginTokenString != null)
			try{
				return WebTokenGenerator.getInstance().fromWebToken(loginTokenString, LoginToken.class);
			} catch (Exception e) {
				LOGGER.error("Invalid login token.tokenString={}",loginTokenString, e);
				return null;
			}

		return null;
	}

	public UserLogin logonBythirdPartUser(Integer namespaceId, String userType, String userToken, HttpServletRequest request, HttpServletResponse response) {
		List<User> userList = this.userProvider.findThirdparkUserByTokenAndType(namespaceId, userType, userToken);
		if(userList == null || userList.size() == 0) {
			LOGGER.error("Unable to find the thridpark user, namespaceId={}, userType={}, userToken={}", namespaceId, userType, userToken);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_USER_NOT_EXIST, "User does not exist");
		}

		User user = userList.get(0);
		if(UserStatus.fromCode(user.getStatus()) != UserStatus.ACTIVE) {
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_ACCOUNT_NOT_ACTIVATED, 
					"User account has not been activated yet");
		}

		UserLogin login = createLogin(namespaceId, user, null, null);
		login.setStatus(UserLoginStatus.LOGGED_IN);

		//added by Janson, mark as disconnected
		unregisterLoginConnection(login);

		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("User logon succeed, namespaceId={}, userType={}, userToken={}, userLogin={}", namespaceId, userType, userToken, login);
		}

		LoginToken token = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), login.getImpersonationId());
		String tokenString = WebTokenGenerator.getInstance().toWebToken(token);

		LOGGER.debug(String.format("Return login info. token: %s, login info: ", tokenString, StringHelper.toJsonString(login)));
		WebRequestInterceptor.setCookieInResponse("token", tokenString, request, response);

		return login;
	}

	@Override
	public boolean signupByThirdparkUser(User user, HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("Proxy-Client-IP"); 
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("WL-Proxy-Client-IP"); 
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("HTTP_CLIENT_IP"); 
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getRemoteAddr(); 
		} 

		String regIp = ip;
		user.setRegIp(regIp);
		user.setStatus(UserStatus.ACTIVE.getCode());

		Integer namespaceId = user.getNamespaceId();
		String namespaceUserType = user.getNamespaceUserType();
		String namespaceUserToken = user.getNamespaceUserToken();
		List<User> userList = userProvider.findThirdparkUserByTokenAndType(namespaceId, namespaceUserType, namespaceUserToken);
		if(userList == null || userList.size() == 0) {
			userProvider.createUser(user);
			return true;
		} else {
			LOGGER.warn("User already existed, namespaceId={}, userType={}, userToken={}", namespaceId, namespaceUserType, namespaceUserToken);
			return false;
		}
	}

	@Override
	public Boolean validateUserPass(ValidatePassCommand cmd) {
		if(cmd.getUserId() == null) {
			LOGGER.error("userId is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"userId is null");
		}
		if(StringUtils.isEmpty(cmd.getPassword())) {
			LOGGER.error("password is null");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"password is null");
		}
		User user = userProvider.findUserById(cmd.getUserId());
		if(user == null) {
			LOGGER.error("user not found.userId=" + cmd.getUserId());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, UserServiceErrorCode.ERROR_USER_NOT_EXIST,
					"user not found");
		}
		if(!EncryptionUtils.validateHashPassword(cmd.getPassword(), user.getSalt(), user.getPasswordHash())) {
			return false;
		}
		return true;
	}

	@Override
	public List<SceneDTO> listTouristRelatedScenes() {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		Long userId = UserContext.current().getUser().getId();
		List<NamespaceResource> resources = namespaceResourceProvider.listResourceByNamespace(namespaceId, NamespaceResourceType.COMMUNITY);
		List<SceneDTO> sceneList = new ArrayList<SceneDTO>();
		for (NamespaceResource resource : resources) {
			Community community = communityProvider.findCommunityById(resource.getResourceId());
			if(null != community){
				CommunityDTO communityDTO = ConvertHelper.convert(community, CommunityDTO.class);
				SceneType sceneType = SceneType.DEFAULT;
				if(CommunityType.fromCode(community.getCommunityType()) == CommunityType.COMMERCIAL){
					sceneType = SceneType.PARK_TOURIST;
				}
				SceneDTO sceneDTO = this.toCommunitySceneDTO(namespaceId, userId, communityDTO, sceneType);
				sceneList.add(sceneDTO);
			}
		}
		return sceneList;
	}

	/**
	 * 判断是否登录
	 * @return
	 */
	public boolean isLogon(){
		//added by janson 2017-03-29
//		UserLogin userLogin = UserContext.current().getLogin();
//		LoginToken token = new LoginToken(userLogin.getUserId(), userLogin.getLoginId(), userLogin.getLoginInstanceNumber(), userLogin.getImpersonationId());
//		if(kickoffService.isKickoff(UserContext.getCurrentNamespaceId(), token)) {
//			kickoffService.remoteKickoffTag(UserContext.getCurrentNamespaceId(), token);
//         throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
//                UserServiceErrorCode.ERROR_KICKOFF_BY_OTHER, "Kickoff by others");
//		}
		
		User user = UserContext.current().getUser();
		if(null == user){
			return false;
		}
		
		LOGGER.debug("Check for login. userId = {}", user.getId());
		if(user.getId() > 0){
			return true;
		}
		return false;
	}


	public void checkUserScene(SceneType sceneType){
		// 判断是否是登录 by sfyan 20161009
		if(!this.isLogon()){
			// 没登录 检查场景是否是游客
			if(sceneType == SceneType.FAMILY || sceneType == SceneType.PM_ADMIN  || sceneType == SceneType.ENTERPRISE || sceneType == SceneType.ENTERPRISE_NOAUTH ){
				LOGGER.error("Not logged in.Cannot access this scene. sceneType = {}", sceneType.getCode());
				throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_UNAUTHENTITICATION,
						"Not logged in.Cannot access this scene");
			}
		}
	}

	@Override
	public UserLogin reSynThridUser(InitBizInfoCommand cmd) {
		validateInitBizInfoCommand(cmd);

		User user = createUserIFNoExist(cmd);
		if(StringUtils.isNotBlank(cmd.getMark())) {
			createUserIdentifierIfNoExist(user, cmd.getMark());
		}

		UserLogin login = createLogin(cmd.getNamespaceId(), user, cmd.getDeviceIdentifier()==null?"":cmd.getDeviceIdentifier(), null);
		login.setStatus(UserLoginStatus.LOGGED_IN);

		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("reSynThridUser-UserLogin="+StringHelper.toJsonString(login));
		}
		return login;
	}

	private UserIdentifier createUserIdentifierIfNoExist(User user, String identifierToken) {
		UserIdentifier identifier = this.userProvider.findClaimedIdentifierByToken(
				user.getNamespaceId(),
				identifierToken);

		if(identifier != null
				&& identifier.getOwnerUid().longValue() != user.getId().longValue()) {
			LOGGER.error("user identifier ownerId not equal to userId.namespaceId=" + user.getNamespaceId()
					+ ",identifier=" + identifierToken
					+ ",ownerId=" + identifier.getOwnerUid()
					+ ",userId=" + user.getId());
		}
		
		if(identifier == null) {
			identifier = new UserIdentifier();
			identifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
			identifier.setIdentifierToken(identifierToken);
			identifier.setIdentifierType(IdentifierType.MOBILE.getCode());
			identifier.setNamespaceId(user.getNamespaceId());
			identifier.setOwnerUid(user.getId());
			identifier.setRegionCode(86);
			userProvider.createIdentifier(identifier);
		} else {
			identifier.setIdentifierToken(identifierToken);
			userProvider.updateIdentifier(identifier);
		}
		return identifier;
	}

	private void validateInitBizInfoCommand(InitBizInfoCommand cmd) {
		if(StringUtils.isEmpty(cmd.getLabel())){
			LOGGER.error("label is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"label is null.");
		}
		if(cmd.getNamespaceId() == null) {
			LOGGER.error("namespaceId is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"namespaceId is null.");
		}
	}

	private User createUserIFNoExist(InitBizInfoCommand cmd) {
		User user = checkThirdUserIsExist(cmd.getNamespaceId(), cmd.getLabel(), false);
		if(user == null) {
			user = new User();
			user.setNickName(cmd.getDetail()==null?"":cmd.getDetail());
			user.setStatus(UserStatus.ACTIVE.getCode());
			user.setPoints(0);
			user.setLevel((byte)1);
			user.setGender((byte)1);
			user.setNamespaceId(cmd.getNamespaceId());
			user.setNamespaceUserToken(cmd.getLabel());;
			user.setAvatar(cmd.getDescription());
			userProvider.createUser(user);
		} else {
			user.setNickName(cmd.getDetail()==null?"":cmd.getDetail());
			user.setNamespaceId(cmd.getNamespaceId());
			user.setAvatar(cmd.getDescription());
			userProvider.updateUser(user);
		}
		return user;
	}

	@Override
	public InitBizInfoDTO findInitBizInfo() {
		User user = UserContext.current().getUser();
		
		InitBizInfoDTO response = new InitBizInfoDTO();
		response.setLabel(user.getId().toString());
		response.setDetail(user.getNickName());
		response.setNamespaceId(user.getNamespaceId());
		response.setSubNonce((int)(Math.random()*1000));
		response.setSubTimestamp(System.currentTimeMillis());
		
		//identifier token
		List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(user.getId());
		if(identifiers != null && !identifiers.isEmpty()) {
			response.setMark(identifiers.get(0).getIdentifierToken());
		}
		
		//user avatarUrl
		UserInfo info = ConvertHelper.convert(user, UserInfo.class);
		populateUserAvatar(info, user.getAvatar());
		response.setDescription(info.getAvatarUrl());
		
		//signature
		String appKey = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), ConfigConstants.SYNCH_USER_APP_KEY, "");
		String secretKey = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), ConfigConstants.SYNCH_USER_SECRET_KEY, "");
		response.setSubKey(appKey);
		String sign = getInitBizInfoDTOSign(response, secretKey);
		response.setSubSign(sign);
		
		return response;
	}

    @Override
    public UserNotificationSettingDTO updateUserNotificationSetting(UpdateUserNotificationSettingCommand cmd) {
        UserMuteNotificationFlag flag = UserMuteNotificationFlag.fromCode(cmd.getMuteFlag());
        if (flag != null) {
            User user = UserContext.current().getUser();
            String lockFlag = CoordinationLocks.USER_NOTIFICATION_SETTING.getCode() + user.getId() + cmd.getTargetType() + cmd.getTargetId();
            Tuple<UserNotificationSetting, Boolean> tuple = coordinationProvider.getNamedLock(lockFlag).enter(() -> {
                UserNotificationSetting setting = userProvider.findUserNotificationSetting(EntityType.USER.getCode(), user.getId(), cmd.getTargetType(), cmd.getTargetId());
                if (setting != null) {
                    setting.setMuteFlag(cmd.getMuteFlag());
                    setting.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    setting.setUpdateUid(user.getId());
                    userProvider.updateUserNotificationSetting(setting);
                } else {
                    setting = new UserNotificationSetting();
                    setting.setOwnerType(EntityType.USER.getCode());
                    setting.setOwnerId(user.getId());
                    setting.setTargetType(cmd.getTargetType());
                    setting.setTargetId(cmd.getTargetId());
                    setting.setMuteFlag(cmd.getMuteFlag());
                    setting.setNamespaceId(UserContext.getCurrentNamespaceId());
                    setting.setAppId(AppConstants.APPID_DEFAULT);
                    setting.setCreateTime(Timestamp.from(Instant.now()));
                    setting.setCreatorUid(user.getId());
                    userProvider.createUserNotificationSetting(setting);
                }
                return setting;
            });
            return tuple.second() ? toUserNotificationSettingDTO(tuple.first()) : new UserNotificationSettingDTO();
        }
        return new UserNotificationSettingDTO();
    }

    private UserNotificationSettingDTO toUserNotificationSettingDTO(UserNotificationSetting setting) {
        if (setting != null) {
            return ConvertHelper.convert(setting, UserNotificationSettingDTO.class);
        }
        return new UserNotificationSettingDTO();
    }

    @Override
    public UserNotificationSettingDTO getUserNotificationSetting(GetUserNotificationSettingCommand cmd) {
        User user = UserContext.current().getUser();
        UserNotificationSetting setting = userProvider.findUserNotificationSetting(EntityType.USER.getCode(), user.getId(), cmd.getTargetType(), cmd.getTargetId());
        return toUserNotificationSettingDTO(setting);
    }

    @Override
    public MessageSessionInfoDTO getMessageSessionInfo(GetMessageSessionInfoCommand cmd) {
        MessageSessionInfoDTO dto = new MessageSessionInfoDTO();
        com.everhomes.rest.common.EntityType targetType = com.everhomes.rest.common.EntityType.fromCode(cmd.getTargetType());
        User user = UserContext.current().getUser();

        UserNotificationSetting setting = userProvider.findUserNotificationSetting(
                EntityType.USER.getCode(), user.getId(), cmd.getTargetType(), cmd.getTargetId());
        if (setting != null) {
            dto.setMuteFlag(setting.getMuteFlag());
        } else {
            dto.setMuteFlag(UserMuteNotificationFlag.NONE.getCode());
        }

        switch (targetType) {
            case USER:
                UserInfo userInfo = this.getUserSnapshotInfo(cmd.getTargetId());
                if (userInfo != null) {
                    dto.setName(userInfo.getNickName());
                    dto.setAvatar(userInfo.getAvatarUrl());
                    if (userInfo.getId() < User.MAX_SYSTEM_USER_ID) {
                        dto.setMessageType(UserMessageType.NOTICE.getCode());
                    } else {
                        dto.setMessageType(UserMessageType.MESSAGE.getCode());
                    }
                } else {
                    LOGGER.warn("userInfo are not found, cmd={}", cmd.toString());
                }
                break;
            case GROUP:
                Group group = groupProvider.findGroupById(cmd.getTargetId());
                if (group != null) {
                    String name = group.getName();
                    // 如果是公司的话，就显示公司的名称，@see com.everhomes.group.GroupServiceImpl#getGroupMemberSnapshot
                    if (GroupDiscriminator.ENTERPRISE == GroupDiscriminator.fromCode(group.getDiscriminator())) {
                        Organization organization = organizationProvider.findOrganizationByGroupId(group.getId());
                        name = organization.getName();
                    }
                    dto.setName(name);
                    dto.setMessageType(UserMessageType.MESSAGE.getCode());
                    String avatar = parseUri(group.getAvatar(), com.everhomes.rest.common.EntityType.GROUP.getCode(), group.getId());
                    dto.setAvatar(avatar);
                } else {
                    LOGGER.warn("group are not found, cmd={}", cmd.toString());
                }
                break;
            default:
                LOGGER.warn("targetType are not found, cmd={}", cmd.toString());
        }
        return dto;
    }

    @Override
    public void sendVerificationCodeByResetIdentifier(SendVerificationCodeByResetIdentifierCommand cmd, HttpServletRequest request) {
        User currUser = UserContext.current().getUser();
        if (currUser == null) return;
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(currUser.getId(), IdentifierType.MOBILE.getCode());

        final Long    halfAnHour = 30 * 60 * 1000L;
        final Long    currUserId = currUser.getId();
        final Integer regionCode = cmd.getRegionCode();
        final String  newIdentifier = cmd.getIdentifier();
        final String  oldIdentifier = userIdentifier.getIdentifierToken();
        final Integer namespaceId = UserContext.getCurrentNamespaceId();
        final String  verificationCode = RandomGenerator.getRandomDigitalString(6);

        // 给原来手机号发送短信验证码
        if (newIdentifier == null) {
            // this.verifySmsTimes("resetIdentifier", oldIdentifier, request.getHeader(X_EVERHOMES_DEVICE));

            UserIdentifierLog log = userIdentifierLogProvider.findByUserIdAndIdentifier(currUserId, oldIdentifier);
            // 如果半个小时没有完成整个过程，需要从头开始执行整个流程
            if (log != null && log.notExpire(halfAnHour) && log.getClaimStatus() == IdentifierClaimStatus.CLAIMING.getCode()) {
                log.setVerificationCode(verificationCode);
                log.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                userIdentifierLogProvider.updateUserIdentifierLog(log);
            } else {
                log = new UserIdentifierLog();
                log.setClaimStatus(IdentifierClaimStatus.CLAIMING.getCode());
                log.setVerificationCode(verificationCode);
                log.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                log.setRegionCode(regionCode);
                log.setNamespaceId(namespaceId);
                log.setIdentifierToken(oldIdentifier);
                log.setOwnerUid(currUserId);
                userIdentifierLogProvider.createUserIdentifierLog(log);
            }
            this.sendVerificationCodeSms(namespaceId, oldIdentifier, verificationCode);
        }
        // 给新手机号发送短信验证码
        else {
            // this.verifySmsTimes("resetIdentifier", newIdentifier, request.getHeader(X_EVERHOMES_DEVICE));

            UserIdentifierLog log = userIdentifierLogProvider.findByUserIdAndIdentifier(currUserId, oldIdentifier);
            // 如果半个小时没有完成整个过程，需要从头开始执行整个流程
            if (log != null && log.notExpire(halfAnHour)
                    && (log.getClaimStatus() == IdentifierClaimStatus.VERIFYING.getCode()
                        || log.getClaimStatus() == IdentifierClaimStatus.CLAIMED.getCode())) {
                log.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
                log.setVerificationCode(verificationCode);
                log.setIdentifierToken(newIdentifier);
                log.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                userIdentifierLogProvider.updateUserIdentifierLog(log);
            } else {
                LOGGER.error("it is not atomic to reset newIdentifier, userId = {}, newIdentifier={}",
                        currUser.getId(), newIdentifier);
                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_PLEASE_TRY_AGAIN_TO_FIRST_STEP,
                        "please try again to the first step");
            }

            this.sendVerificationCodeSms(namespaceId, newIdentifier, verificationCode);
        }
    }

    @Override
    public void verifyResetIdentifierCode(VerifyResetIdentifierCodeCommand cmd) {
        User currUser = UserContext.current().getUser();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        if (currUser == null) return;
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(currUser.getId(), IdentifierType.MOBILE.getCode());
        UserIdentifierLog log = userIdentifierLogProvider.findByUserId(currUser.getId());

        // 如果半个小时没有完成整个过程，需要从头开始执行整个流程
        final Long halfAnHour = 30 * 60 * 1000L;
        if (log != null && log.notExpire(halfAnHour)) {
            IdentifierClaimStatus claimStatus = IdentifierClaimStatus.fromCode(log.getClaimStatus());
            switch (claimStatus) {
                case CLAIMING:
                    if (log.checkVerificationCode(cmd.getVerificationCode())) {
                        log.setClaimStatus(IdentifierClaimStatus.VERIFYING.getCode());
                        userIdentifierLogProvider.updateUserIdentifierLog(log);
                    } else {
                        LOGGER.error("verification code incorrect or expired {}", cmd.getVerificationCode());
                        throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_VERIFICATION_CODE_INCORRECT_OR_EXPIRED,
                                "verification code incorrect or expired %s", cmd.getVerificationCode());
                    }
                    break;
                case CLAIMED:
                    // 检查新手机号是否已经是注册用户
                    UserIdentifier newIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, log.getIdentifierToken());
                    if (newIdentifier != null) {
                        LOGGER.error("the new identifier are already exist {}", log.getIdentifierToken());
                        throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_NEW_IDENTIFIER_USER_EXIST,
                                "the new identifier are already exist {}", log.getIdentifierToken());
                    }
                    if (log.checkVerificationCode(cmd.getVerificationCode())) {
                        log.setClaimStatus(IdentifierClaimStatus.TAKEN_OVER.getCode());
                        // 加个事务
                        dbProvider.execute(r -> {
                            userIdentifierLogProvider.updateUserIdentifierLog(log);
                            UserResetIdentifierVo vo = new UserResetIdentifierVo(
                                    log.getOwnerUid(), userIdentifier.getIdentifierToken(),
                                    userIdentifier.getRegionCode(), log.getIdentifierToken(), log.getRegionCode());
                            resetUserIdentifier(currUser, vo);
                            return true;
                        });
                    } else {
                        LOGGER.error("verification code incorrect or expired {}", cmd.getVerificationCode());
                        throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_VERIFICATION_CODE_INCORRECT_OR_EXPIRED,
                                "verification code incorrect or expired {}", cmd.getVerificationCode());
                    }
                    break;
                default:
                    LOGGER.error("it is not atomic to reset newIdentifier, userId = {}, newIdentifier={}",
                            currUser.getId(), log.getIdentifierToken());
                    throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_PLEASE_TRY_AGAIN_TO_FIRST_STEP,
                            "please try again to the first step");
            }
        } else {
            LOGGER.error("it is not atomic to reset newIdentifier, userId = {}, log = {}",
                    currUser.getId(), log);
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_PLEASE_TRY_AGAIN_TO_FIRST_STEP,
                    "please try again to the first step");
        }
    }

    private void resetUserIdentifier(User user, UserResetIdentifierVo vo) {
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
        userIdentifier.setIdentifierToken(vo.getNewIdentifier());
        userIdentifier.setRegionCode(vo.getNewRegionCode());
        userProvider.updateIdentifier(userIdentifier);
        // 发布修改手机号事件
        applicationEventPublisher.publishEvent(new ResetUserIdentifierEvent(vo));
    }

    @Override
    public UserAppealLogDTO createResetIdentifierAppeal(CreateResetIdentifierAppealCommand cmd) {
        validate(cmd);
        UserAppealLog log = new UserAppealLog();
        log.setNamespaceId(UserContext.getCurrentNamespaceId());
        log.setName(cmd.getName());
        log.setEmail(cmd.getEmail());
        log.setNewIdentifier(cmd.getNewIdentifier());
        log.setNewRegionCode(cmd.getNewRegionCode());
        log.setOldIdentifier(cmd.getOldIdentifier());
        log.setOldRegionCode(cmd.getOldRegionCode());
        log.setOwnerUid(UserContext.current().getUser().getId());
        log.setRemarks(cmd.getRemarks());
        log.setStatus(UserAppealLogStatus.WAITING_FOR_APPROVAL.getCode());

        userAppealLogProvider.createUserAppealLog(log);
        return toUserAppealLogDTO(log);
    }

    @Override
    public ListUserAppealLogsResponse listUserAppealLogs(ListUserAppealLogsCommand cmd) {
	    validate(cmd);
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getAnchor());

        List<UserAppealLog> logList = userAppealLogProvider.listUserAppealLog(locator, cmd.getStatus(), pageSize);

        List<UserAppealLogDTO> dtoList = logList.stream().map(this::toUserAppealLogDTO).collect(Collectors.toList());

        ListUserAppealLogsResponse response = new ListUserAppealLogsResponse();
        response.setAppealLogs(dtoList);
        response.setNextPageAnchor(locator.getAnchor());
        return response;
    }

    @Override
    public UserAppealLogDTO updateUserAppealLog(UpdateUserAppealLogCommand cmd) {
        validate(cmd);
        UserAppealLogStatus status = UserAppealLogStatus.fromCode(cmd.getStatus());
        if (status != null) {
            Tuple<UserAppealLog, Boolean> tuple = coordinationProvider.getNamedLock(
                    CoordinationLocks.USER_APPEAL_LOG.getCode() + cmd.getId()).enter(() -> {
                UserAppealLog log = userAppealLogProvider.findUserAppealLogById(cmd.getId());
                if (log != null && !Objects.equals(log.getStatus(), cmd.getStatus())) {
                    log.setStatus(status.getCode());
                    userAppealLogProvider.updateUserAppealLog(log);

                    // 如果是通过，则重置用户手机号
                    if (status == UserAppealLogStatus.ACTIVE) {
                        Integer namespaceId = UserContext.getCurrentNamespaceId();
                        // 检查申诉新手机号是否已经是注册用户
                        UserIdentifier newIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, log.getNewIdentifier());
                        if (newIdentifier != null) {
                            LOGGER.error("the new identifier are already exist {}", log.getNewIdentifier());
                            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_NEW_IDENTIFIER_USER_EXIST,
                                    "the new identifier are already exist {}", log.getNewIdentifier());
                        }
                        User user = userProvider.findUserById(log.getOwnerUid());
                        UserResetIdentifierVo vo = new UserResetIdentifierVo(
                                log.getOwnerUid(), log.getOldIdentifier(),
                                log.getOldRegionCode(), log.getNewIdentifier(), log.getNewRegionCode());
                        resetUserIdentifier(user, vo);
                        // 把当前用户退出登录
                        List<UserLogin> userLogins = listUserLogins(user.getId());
                        userLogins.forEach(this::logoff);
                    }
                }
                return log;
            });
            if (tuple.second() && tuple.first() != null) {
                // 发消息或者发短信
                sendMessageOrSmsByResetIdentifier(status, tuple.first());
                return toUserAppealLogDTO(tuple.first());
            }
        }
        LOGGER.error("update user appeal log failed, cmd = {}", cmd);
        throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_UPDATE_USER_APPEAL_LOG,
                "update user appeal log failed");
    }

    private void sendMessageOrSmsByResetIdentifier(UserAppealLogStatus status, UserAppealLog log) {
        User user = UserContext.current().getUser();
        String locale = Locale.SIMPLIFIED_CHINESE.toString();
        if (user != null) {
            locale = user.getLocale();
        }
        switch (status) {
            case ACTIVE:
                List<Tuple<String, Object>> variables = smsProvider.toTupleList("newIdentifier", log.getNewIdentifier());
                String templateScope = SmsTemplateCode.SCOPE;
                int templateId = SmsTemplateCode.RESET_IDENTIFIER_APPEAL_SUCCESS_CODE;
                smsProvider.sendSms(log.getNamespaceId(), log.getNewIdentifier(), templateScope, templateId, locale, variables);
                break;
            case INACTIVE:
                String messageBody = localeStringService.getLocalizedString(UserLocalStringCode.SCOPE, UserLocalStringCode.REJECT_APPEAL_IDENTIFIER_CODE, locale, "");
                sendMessageToUser(log.getOwnerUid(), messageBody, MessagingConstants.MSG_FLAG_STORED_PUSH);
                break;
        }
    }

    private UserAppealLogDTO toUserAppealLogDTO(UserAppealLog log) {
        return ConvertHelper.convert(log, UserAppealLogDTO.class);
    }

    private String parseUri(String uri, String ownerType, Long ownerId) {
        String avatar = null;
        try {
            avatar = contentServerService.parserUri(uri, ownerType, ownerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return avatar;
    }

    private String getInitBizInfoDTOSign(InitBizInfoDTO response, String secretKey) {
		Map<String,String> params = new HashMap<String, String>();
		params.put("label", response.getLabel());
		params.put("namespaceId", response.getNamespaceId()+"");
		params.put("subNonce", response.getSubNonce()+"");
		params.put("subTimestamp", response.getSubTimestamp()+"");
		params.put("subKey", response.getSubKey());
		if(StringUtils.isNotBlank(response.getDetail())) {
			params.put("detail", response.getDetail());
		}
		if(StringUtils.isNotBlank(response.getMark())) {
			params.put("mark", response.getMark());
		}
		if(StringUtils.isNotBlank(response.getDescription())) {
			params.put("description", response.getDescription());
		}
		String sign = SignatureHelper.computeSignature(params, secretKey);
		try {
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			sign = URLEncoder.encode(sign);
		}
		return sign;
	}

	private String getYzxRegionPhoneNumber(String identifierToken, Integer regionCode){
		//国内电话不要拼区号，发送短信走国内通道，便宜
		if(null == regionCode || 86 == regionCode ){
			return identifierToken;
		}
		return "00" + regionCode + identifierToken;
	}

	@Override
	public ListRegisterUsersResponse searchUserByNamespace(SearchUserByNamespaceCommand cmd) {
	    ListRegisterUsersResponse resp = new ListRegisterUsersResponse();
	    if(cmd.getNamespaceId() == null) {
	        cmd.setNamespaceId(0);
	    }
	    
	    int count = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
	    CrossShardListingLocator locator = new CrossShardListingLocator();
	    locator.setAnchor(cmd.getAnchor());
	    List<User> users = this.userProvider.listUserByNamespace(cmd.getKeyword(), cmd.getNamespaceId(), locator, count);
	    resp.setNextPageAnchor(locator.getAnchor());
	    resp.setValues(new ArrayList<UserInfo>());
	    for(User u : users) {
	        UserInfo ui = getUserBasicInfoByQueryUser(u, false);
	        if(ui != null) {
	            resp.getValues().add(ui);
	        }
	    }
	    
	    return resp;
	}

    // 参数校验方法
    // 可以校验带bean validation 注解的对象
    // 校验失败, 抛出异常, 异常信息附带参数值信息
    private void validate(Object o) {
        Set<ConstraintViolation<Object>> result = validator.validate(o);

        for (ConstraintViolation<Object> v : result) {
            ConstraintDescriptor<?> constraintDescriptor = v.getConstraintDescriptor();
            String constraintAnnotationClassName = constraintDescriptor.getAnnotation().annotationType().getName();
            switch (constraintAnnotationClassName) {
                // 参数长度检查
                case "javax.validation.constraints.Size":
                    Size size = (Size) constraintDescriptor.getAnnotation();
                    int max = size.max();
                    if (max > 0) {
                        LOGGER.error("Parameter over length: [ {} ]", v.getPropertyPath());
                        throw errorWith(ParamErrorCodes.SCOPE, ParamErrorCodes.ERROR_OVER_LENGTH,
                                "Parameter over length: [ %s ]", v.getPropertyPath());
                    }
                    break;
                // 其他参数检查
                default:
                    LOGGER.error("Invalid parameter {} [ {} ]", v.getPropertyPath(), v.getInvalidValue());
                    throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                            "Invalid parameter %s [ %s ]", v.getPropertyPath(), v.getInvalidValue());
            }
        }
    }
}
