// @formatter:off
package com.everhomes.user;

import com.alibaba.fastjson.JSON;
import com.everhomes.PictureValidate.PictureValidateService;
import com.everhomes.PictureValidate.PictureValidateServiceErrorCode;
import com.everhomes.acl.AclProvider;
import com.everhomes.acl.PortalRoleResolver;
import com.everhomes.acl.Role;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.address.AddressService;
import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.archives.ArchivesService;
import com.everhomes.asset.AssetPaymentStrings;
import com.everhomes.authorization.*;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.border.Border;
import com.everhomes.border.BorderConnection;
import com.everhomes.border.BorderConnectionProvider;
import com.everhomes.border.BorderProvider;
import com.everhomes.bus.*;
import com.everhomes.business.BusinessService;
import com.everhomes.category.Category;
import com.everhomes.category.CategoryProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.contract.ContractService;
import com.everhomes.controller.WebRequestInterceptor;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.device.Device;
import com.everhomes.device.DeviceProvider;
import com.everhomes.enterprise.Enterprise;
import com.everhomes.enterprise.EnterpriseProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.family.FamilyProvider;
import com.everhomes.family.FamilyService;
import com.everhomes.forum.ForumService;
import com.everhomes.group.Group;
import com.everhomes.group.GroupProvider;
import com.everhomes.group.GroupService;
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
import com.everhomes.namespace.*;
import com.everhomes.naming.NameMapper;
import com.everhomes.news.NewsService;
import com.everhomes.openapi.FunctionCardHandler;
import com.everhomes.organization.*;
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.point.UserLevel;
import com.everhomes.point.UserPointService;
import com.everhomes.qrcode.QRCodeService;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.ListServiceModuleAdministratorsCommand;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.acl.PrivilegeServiceErrorCode;
import com.everhomes.rest.address.*;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.asset.PushUsersCommand;
import com.everhomes.rest.asset.PushUsersResponse;
import com.everhomes.rest.asset.TargetDTO;
import com.everhomes.rest.business.ShopDTO;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.contentserver.ContentCacheConfigDTO;
import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.energy.util.ParamErrorCodes;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.family.FamilyMemberFullDTO;
import com.everhomes.rest.family.ListAllFamilyMembersCommandResponse;
import com.everhomes.rest.family.admin.ListAllFamilyMembersAdminCommand;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.group.GroupDiscriminator;
import com.everhomes.rest.group.GroupLocalStringCode;
import com.everhomes.rest.group.GroupNameEmptyFlag;
import com.everhomes.rest.launchpad.LaunchPadItemDTO;
import com.everhomes.rest.link.RichLinkDTO;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.namespace.*;
import com.everhomes.rest.openapi.FunctionCardDto;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.point.AddUserPointCommand;
import com.everhomes.rest.point.GetUserTreasureCommand;
import com.everhomes.rest.point.GetUserTreasureResponse;
import com.everhomes.rest.point.PointType;
import com.everhomes.rest.qrcode.NewQRCodeCommand;
import com.everhomes.rest.qrcode.QRCodeDTO;
import com.everhomes.rest.search.SearchContentType;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.ui.organization.SetCurrentCommunityForSceneCommand;
import com.everhomes.rest.ui.user.BindPhoneCommand;
import com.everhomes.rest.ui.user.BindPhoneType;
import com.everhomes.rest.ui.user.*;
import com.everhomes.rest.ui.user.VerificationCodeForBindPhoneCommand;
import com.everhomes.rest.ui.user.VerificationCodeForBindPhoneResponse;
import com.everhomes.rest.user.*;
import com.everhomes.rest.user.admin.*;
import com.everhomes.rest.user.admin.UserAppealLogStatus;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhAddresses;
import com.everhomes.server.schema.tables.EhGroupMemberLogs;
import com.everhomes.server.schema.tables.pojos.EhUserIdentifiers;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.SmsBlackList;
import com.everhomes.sms.SmsBlackListCreateType;
import com.everhomes.sms.SmsBlackListProvider;
import com.everhomes.sms.SmsBlackListStatus;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.sdk.SdkUserService;
import com.everhomes.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Size;
import javax.validation.metadata.ConstraintDescriptor;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

import static com.everhomes.rest.ui.user.SceneType.*;
import static com.everhomes.server.schema.Tables.EH_USER_IDENTIFIERS;
import static com.everhomes.util.RuntimeErrorException.errorWith;


/**
 * Implement business logic for user management
 *
 * @author Kelven Yang
 */
@Component
public class UserServiceImpl implements UserService, ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String SIGN_APP_KEY = "sign.appKey";
    private static final String EXPIRE_TIME = "invitation.expiretime";
    private static final String YZX_VCODE_TEMPLATE_ID = "yzx.vcode.templateid";
    private static final String MW_VCODE_TEMPLATE_CONTENT = "mw.vcode.template.content";
    private static final String VCODE_SEND_TYPE = "sms.handler.type";

    private static final String X_EVERHOMES_DEVICE = "x-everhomes-device";
    private static final Byte SCENE_EXAMPLE = 5;

    private static final String SCAN_FOR_LOGON_SERVER = "http://web.zuolin.com";

    //推荐场景转换启用参数
    private final static Integer SCENE_SWITCH_ENABLE = 0;
    private final static Integer SCENE_SWITCH_DISABLE = 1;
    private final static Integer SCENE_SWITCH_DEFAULT_FLAG_ENABLE = 1;
    private final static Integer SCENE_SWITCH_DEFAULT_FLAG_DISABLE = 0;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    private static String ANBANG_CLIENTID;
    private static String ANBANG_CLIENTSECRET;
    private static String ANBANG_OAUTH_URL;
    private static String ANBANG_USERS_URL;
    private static String ANBANG_CURRENT_USER_URL;
    private static Integer ANBANG_NAMESPACE_ID;

    @Autowired
    private SdkUserService sdkUserService;

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
    private AuthorizationThirdPartyFormProvider authorizationThirdPartyFormProvider;

    @Autowired
    private AuthorizationThirdPartyButtonProvider authorizationThirdPartyButtonProvider;

    @Autowired
    private UserIdentifierLogProvider userIdentifierLogProvider;

    @Autowired
    private UserAppealLogProvider userAppealLogProvider;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private SmsBlackListProvider smsBlackListProvider;

    @Autowired
    private NamespaceResourceService namespaceResourceService;

    @Autowired
    List<FunctionCardHandler> functionCardHandlers;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
    private GroupService groupService;

    @Autowired
    private PictureValidateService pictureValidateService;

    @Autowired
    private ArchivesService archivesService;

    @Autowired
    private NamespaceProvider namespaceProvider;

    private static final String DEVICE_KEY = "device_login";

    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
    //@PostConstruct
    public void setup() {
        localBus.subscribe("border.close", LocalBusMessageDispatcher.getDispatcher(this));
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            setup();
        }
    }

    /**
     * 从数据库Load安邦的配置信息
     */
    private void loadAnbangConfigFromDb() {
        ANBANG_NAMESPACE_ID = configurationProvider.getIntValue("anbang.namespace.id", 999949);
        ANBANG_OAUTH_URL = configurationProvider.getValue(ANBANG_NAMESPACE_ID, "anbang.oauth.url", "http://139.196.255.176:8000") + "/api/auth/oauth/token";
        ANBANG_CLIENTID = configurationProvider.getValue(ANBANG_NAMESPACE_ID, "anbang.clientid", "zuolin");
        ANBANG_CLIENTSECRET = configurationProvider.getValue(ANBANG_NAMESPACE_ID, "anbang.clientsecret", "enVvbGluMjAxODAxMDI=");
        ANBANG_USERS_URL = configurationProvider.getValue(ANBANG_NAMESPACE_ID, "anbang.oauth.url", "http://139.196.255.176:8000") + "/api/permission/user/synchronization";
        ANBANG_CURRENT_USER_URL = configurationProvider.getValue(ANBANG_NAMESPACE_ID, "anbang.oauth.url", "http://139.196.255.176:8000") + "/api/auth/current-user";
    }

    @Override
    public SignupToken signup(SignupCommand cmd, HttpServletRequest request) {
        final IdentifierType identifierType = IdentifierType.fromString(cmd.getType());
        if (identifierType == null) {
            LOGGER.error("Invalid or unsupported identifier type, cmd=" + cmd);
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid or unsupported identifier type");
        }

        final String identifierToken = cmd.getToken();
        // 客户端有的时候会传一个非手机号的token过来，此时打印日志以便定位，不改变原来的流程
        if (identifierToken == null || identifierToken.length() > 20) {
            LOGGER.warn("Identifier token is invalid, cmd=" + cmd);
        }

        boolean overrideExisting = false;
        if (cmd.getIfExistsThenOverride() != null && cmd.getIfExistsThenOverride().intValue() != 0) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("The ifExistsThenOverride flag is true, cmd=" + cmd);
            }
            overrideExisting = true;
        }

        // UserIdentifier existingClaimedIdentifier = this.userProvider.findClaimedIdentifierByToken(identifierToken);
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        UserIdentifier existingClaimedIdentifier = this.userProvider.findClaimedIdentifierByToken(namespaceId, identifierToken);
        if (existingClaimedIdentifier != null && !overrideExisting) {
            LOGGER.error("User identifier token has already been claimed, cmd=" + cmd + ", identifierId=" + existingClaimedIdentifier.getId()
                    + ", ownerUid=" + existingClaimedIdentifier.getOwnerUid() + ", identifierType=" + existingClaimedIdentifier.getIdentifierType()
                    + ", identifierToken=" + existingClaimedIdentifier.getIdentifierToken());
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_IDENTIFIER_ALREADY_CLAIMED,
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
        sendVerificationCodeSms(identifier.getNamespaceId(), this.getRegionPhoneNumber(identifierToken, cmd.getRegionCode()), identifier.getVerificationCode());

        SignupToken signupToken = new SignupToken(identifier.getOwnerUid(), identifierType, identifierToken);
        if (StringUtils.isEmpty(signupToken.getIdentifierToken())) {
            LOGGER.error("Signup token should not be empty, signupToken" + signupToken + ", cmd=" + cmd);
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("User signup succeed, signupToken" + signupToken + ", cmd=" + cmd);
            }
        }

        return signupToken;
    }

    /**
     * 校验短信发送频率
     */
    private void verifySmsTimes(String smsAction, String identifierToken, String deviceId) {
        checkSmsBlackList(smsAction, identifierToken);

        // added by janson 消息序列化不正确的根本原因在于这里 03-31
        RedisTemplate template = bigCollectionProvider.getMapAccessor("sendSmsTimes", "").getTemplate(new StringRedisSerializer());
        // 设置value的序列化，要不然下面的increment方法会报错
        // template.setValueSerializer(new StringRedisSerializer()); 坚决不用这种写法，会导致消息模块报错！因为这个是设置全局的 template
        ValueOperations op = template.opsForValue();

        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Integer smsMinDuration = Integer.parseInt(configProvider.getValue(namespaceId, "sms.verify.minDuration.seconds", "60"));
        Integer smsTimesDeviceForAnHour = Integer.parseInt(configProvider.getValue(namespaceId, "sms.verify.device.timesForAnHour", "10"));
        Integer smsTimesDeviceForADay = Integer.parseInt(configProvider.getValue(namespaceId, "sms.verify.device.timesForADay", "20"));
        Integer smsTimesPhoneForAnHour = Integer.parseInt(configProvider.getValue(namespaceId, "sms.verify.phone.timesForAnHour", "3"));
        Integer smsTimesPhoneForADay = Integer.parseInt(configProvider.getValue(namespaceId, "sms.verify.phone.timesForADay", "5"));

        // 老版本的客户端没有deviceId
        // boolean hasDeviceId = StringUtils.isNotBlank(deviceId);
        boolean hasDeviceId = false;// 客户端传来的deviceId有问题，先不校验这个

        // 每个手机号每天发送次数≤5
        String phoneDayKey = getCacheKey("sendSmsTimes", smsAction, SmsVerify.Type.PHONE.name(), SmsVerify.Duration.DAY.name(), identifierToken);
        Object times = op.get(phoneDayKey);

        if (times == null) {
            // 设置今天晚上23:59:59过期
            LocalDate tomorrowStart = LocalDate.now().plusDays(1);
            long seconds = (java.sql.Date.valueOf(tomorrowStart).getTime() - System.currentTimeMillis()) / 1000;
            op.set(phoneDayKey, String.valueOf(0), seconds, TimeUnit.SECONDS);
        } else {
            Integer t = Integer.valueOf((String) times);
            if (t >= smsTimesPhoneForADay) {
                createSmsBlackList(smsAction, identifierToken);
                LOGGER.error("Verification code request is too frequent with phone, please try again after 24 hours. phone={}, deviceId={}, times={}", identifierToken, deviceId, t);
                throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_SMS_TOO_FREQUENT_DAY,
                        "Verification code request is too frequent, please try again after 24 hours");
            }
        }

        // 每个手机设备每天发送次数≤20
        String deviceDayKey = getCacheKey("sendSmsTimes", smsAction, SmsVerify.Type.DEVICE.name(), SmsVerify.Duration.DAY.name(), deviceId);
        if (hasDeviceId) {
            times = op.get(deviceDayKey);
            if (times == null) {
                // 设置今天晚上23:59:59过期
                LocalDate tomorrowStart = LocalDate.now().plusDays(1);
                long seconds = (java.sql.Date.valueOf(tomorrowStart).getTime() - System.currentTimeMillis()) / 1000;
                op.set(deviceDayKey, String.valueOf(0), seconds, TimeUnit.SECONDS);
            } else {
                Integer t = Integer.valueOf((String) times);
                if (t >= smsTimesDeviceForADay) {
                    LOGGER.error("Verification code request is too frequent with device, please try again after 24 hours. phone={}, deviceId={}, times={}", identifierToken, deviceId, t);
                    throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_SMS_TOO_FREQUENT_DAY,
                            "Verification code request is too frequent, please try again after 24 hours");
                }
            }
        }

        // 每个手机号每小时发送次数≤3
        String phoneHourKey = getCacheKey("sendSmsTimes", smsAction, SmsVerify.Type.PHONE.name(), SmsVerify.Duration.HOUR.name(), identifierToken);
        times = op.get(phoneHourKey);

        if (times == null) {
            op.set(phoneHourKey, String.valueOf(0), 1, TimeUnit.HOURS);
        } else {
            Integer t = Integer.valueOf((String) times);
            if (t >= smsTimesPhoneForAnHour) {
                LOGGER.error("Verification code request is too frequent with phone, please 1 hour to try again. phone={}, deviceId={}, times={}", identifierToken, deviceId, t);
                throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_SMS_TOO_FREQUENT_HOUR,
                        "Verification code request is too frequent, please 1 hour to try again");
            }
        }

        // 每个手机设备每小时发送次数≤10
        String deviceHourKey = getCacheKey("sendSmsTimes", smsAction, SmsVerify.Type.DEVICE.name(), SmsVerify.Duration.HOUR.name(), deviceId);
        if (hasDeviceId) {
            times = op.get(deviceHourKey);

            if (times == null) {
                op.set(deviceHourKey, String.valueOf(0), 1, TimeUnit.HOURS);
            } else {
                Integer t = Integer.valueOf((String) times);
                if (t >= smsTimesDeviceForAnHour) {
                    LOGGER.error("Verification code request is too frequent with device, please 1 hour to try again. phone={}, deviceId={}, times={}", identifierToken, deviceId, t);
                    throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_SMS_TOO_FREQUENT_HOUR,
                            "Verification code request is too frequent, please 1 hour to try again");
                }
            }
        }

        // 发送验证码时间不得小于60s
        String minDurationKey = getCacheKey("sendSmsTimes", smsAction, SmsVerify.Type.PHONE.name(), SmsVerify.Duration.SECOND.name(), identifierToken);
        if (smsMinDuration > 0) {
            times = op.get(minDurationKey);

            if (times == null) {
                op.set(minDurationKey, String.valueOf(0), smsMinDuration, TimeUnit.SECONDS);
            } else {
                LOGGER.error("The time for sending the verification code shall not be less than {}s, phone={}, deviceId={}.", smsMinDuration, identifierToken, deviceId);
                throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_SMS_MIN_DURATION,
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

    private void createSmsBlackList(String smsAction, String identifierToken) {
        SmsBlackList blackList = new SmsBlackList();
        blackList.setReason(smsAction);
        blackList.setContactToken(identifierToken);
        blackList.setStatus(SmsBlackListStatus.BLOCK.getCode());
        blackList.setCreateType(SmsBlackListCreateType.SYSTEM.getCode());
        blackList.setNamespaceId(UserContext.getCurrentNamespaceId());
        smsBlackListProvider.createSmsBlackList(blackList);
    }

    @Override
    public void checkSmsBlackList(String smsAction, String identifierToken) {
        SmsBlackList blackList = smsBlackListProvider.findByContactToken(identifierToken);
        if (blackList != null && Objects.equals(blackList.getStatus(), SmsBlackListStatus.BLOCK.getCode())) {
            LOGGER.info("sms black list user try to send sms, smsAction = {}, contactToken = {}", smsAction, identifierToken);
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_SMS_BLACK_LIST,
                    "Hi guys, you are black list user.");
        }
    }

    private String getCacheKey(String... keys) {
        return StringUtils.join(Arrays.asList(keys), ":");
    }

    @Override
    public void sendVerificationCodeSms(Integer namespaceId, String phoneNumber, String verificationCode) {
        List<Tuple<String, Object>> variables = smsProvider.toTupleList(SmsTemplateCode.KEY_VCODE, verificationCode);
        String templateScope = SmsTemplateCode.SCOPE;
        int templateId = SmsTemplateCode.VERIFICATION_CODE;
        String templateLocale = UserContext.current().getUser().getLocale();
        smsProvider.sendSms(namespaceId, phoneNumber, templateScope, templateId, templateLocale, variables);

        //		String smsType = configurationProvider.getValue(namespaceId, SMS_HANDLER_TYPE, "");
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
    
    /**
     * 积分发送短信用
     * @param namespaceId
     * @param phoneNumber
     * @param verificationCode
     */
    
    private void sendVerificationCodeSms4Point(Integer namespaceId, String phoneNumber, String verificationCode) {
        List<Tuple<String, Object>> variables = smsProvider.toTupleList(SmsTemplateCode.KEY_VCODE, verificationCode);
        String templateScope = SmsTemplateCode.SCOPE;
       // int templateId = SmsTemplateCode.POINT_VERIFICATION_CODE;
         int templateId = SmsTemplateCode.VERIFICATION_CODE;      
        String templateLocale = UserContext.current().getUser().getLocale();
        smsProvider.sendSms(namespaceId, phoneNumber, templateScope, templateId, templateLocale, variables);
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
        assert (signupToken != null);

        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(User.class, signupToken.getUserId()));
        Record record = context.select().from(EH_USER_IDENTIFIERS)
                .where(EH_USER_IDENTIFIERS.OWNER_UID.eq(signupToken.getUserId()))
                .and(EH_USER_IDENTIFIERS.IDENTIFIER_TYPE.eq(signupToken.getIdentifierType().getCode()))
                .and(EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.eq(signupToken.getIdentifierToken()))
                .and(EH_USER_IDENTIFIERS.NAMESPACE_ID.eq(namespaceId))
                .fetchOne();
        if (LOGGER.isDebugEnabled()) {
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
        if (identifier == null) {
            LOGGER.error("User identifier not found in db, signupToken=" + signupToken);
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_SIGNUP_TOKEN, "Invalid signup token");
        }

        if (identifier.getClaimStatus() == IdentifierClaimStatus.CLAIMING.getCode() ||
                identifier.getClaimStatus() == IdentifierClaimStatus.VERIFYING.getCode()) {

            this.verifySmsTimes("signup", identifier.getIdentifierToken(), request.getHeader(X_EVERHOMES_DEVICE));

            Timestamp ts = identifier.getNotifyTime();
            if (ts == null || isVerificationExpired(ts)) {
                String verificationCode = RandomGenerator.getRandomDigitalString(6);
                identifier.setVerificationCode(verificationCode);

                LOGGER.debug("Send notification code " + verificationCode + " to " + identifier.getIdentifierToken());
                //                String templateId = configurationProvider.getValue(YZX_VCODE_TEMPLATE_ID, "");
                //                smmProvider.sendSms( identifier.getIdentifierToken(), verificationCode, templateId);
                //增加区号发送短信 by sfyan 20161012
                sendVerificationCodeSms(namespaceId, this.getRegionPhoneNumber(identifier.getIdentifierToken(), regionCode), verificationCode);
            } else {

                // TODO
                LOGGER.debug("Send notification code " + identifier.getVerificationCode() + " to " + identifier.getIdentifierToken());
                //                String templateId = configurationProvider.getValue(YZX_VCODE_TEMPLATE_ID, "");
                //                smmProvider.sendSms( identifier.getIdentifierToken(), identifier.getVerificationCode(),templateId);
                sendVerificationCodeSms(namespaceId, this.getRegionPhoneNumber(identifier.getIdentifierToken(), regionCode), identifier.getVerificationCode());
            }

            identifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            identifier.setRegionCode(regionCode);
            this.userProvider.updateIdentifier(identifier);
        } else {
            LOGGER.error("Token status is not claiming or verifying, signupToken=" + signupToken + ", identifierId=" + identifier.getId()
                    + ", ownerUid=" + identifier.getOwnerUid() + ", identifierType=" + identifier.getIdentifierType()
                    + ", identifierToken=" + identifier.getIdentifierToken() + ", identifierStatus=" + identifier.getClaimStatus());
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALD_TOKEN_STATUS, "Invalid token status");
        }
    }
    
    /**
     * 积分发送短信用
     * @param namespaceId
     * @param regionCode
     * @param request
     */
    @Override
    public void sendVerficationCode4Point(Integer namespaceId, UserDTO user, Integer regionCode, HttpServletRequest request) {
        LOGGER.info("sendVerficationCode4Point  -->  user:[{}]",user);
    		//检查用户是否存在
		String phone = user.getIdentifierToken() ;
		if(namespaceId ==null || StringUtils.isBlank(phone) ){
			LOGGER.error("namespaceId or phone is null");
			return ;
		}
		UserIdentifier identifier = userProvider.findClaimedIdentifierByToken(namespaceId, phone);
        	if (identifier == null) {
        		LOGGER.error("userIdentifier  is null.namespace:[{}] ;phone:[{}]",namespaceId ,phone);
    			return ;
        	}

        	//因为调试需要,先把这段限制去注掉
            //this.verifySmsTimes("signup", identifier.getIdentifierToken(), request.getHeader(X_EVERHOMES_DEVICE));

            Timestamp ts = identifier.getNotifyTime();
            //验证码过期了重新生成,没有过期则用原来的
            if (ts == null || isVerificationExpired(ts)) {
                String verificationCode = RandomGenerator.getRandomDigitalString(6);
                identifier.setVerificationCode(verificationCode);

                LOGGER.debug("Send notification code " + verificationCode + " to " + identifier.getIdentifierToken());
                sendVerificationCodeSms4Point(namespaceId, this.getRegionPhoneNumber(identifier.getIdentifierToken(), regionCode), verificationCode);
            } else {
                // TODO
                LOGGER.debug("Send notification code " + identifier.getVerificationCode() + " to " + identifier.getIdentifierToken());     
                sendVerificationCodeSms4Point(namespaceId, this.getRegionPhoneNumber(identifier.getIdentifierToken(), regionCode), identifier.getVerificationCode());
            }

            identifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            identifier.setRegionCode(regionCode);
            this.userProvider.updateIdentifier(identifier);
    }
    

    @Override
    public UserLogin verifyAndLogon(VerifyAndLogonCommand cmd) {
        SignupToken signupToken = WebTokenGenerator.getInstance().fromWebToken(cmd.getSignupToken(), SignupToken.class);
        if (signupToken == null) {
            cmd.setInitialPassword("");
            LOGGER.error("Signup token is empty, cmd=" + cmd);
            throw errorWith(UserServiceErrorCode.SCOPE,
                    UserServiceErrorCode.ERROR_INVALID_SIGNUP_TOKEN, "Invalid signup token");
        }
        if (StringUtils.isEmpty(cmd.getInitialPassword())) {
            LOGGER.error("password cannot be empty, cmd=" + cmd);
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PASSWORD, "password cannot be empty");
        }

        String verificationCode = cmd.getVerificationCode();
        String deviceIdentifier = cmd.getDeviceIdentifier();
        int namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());

        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, signupToken.getIdentifierToken());

        if (null != userIdentifier) {
            LOGGER.error("The identify token has been registered, signupToken = {}, cmd = {}", signupToken, cmd);
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_IDENTIFY_TOKEN_REGISTERED, "The identify token has been registered");
        }

        UserIdentifier identifier = this.findIdentifierByToken(namespaceId, signupToken);
        if (identifier == null) {
            LOGGER.error("User identifier not found in db, signupToken=" + signupToken + ", cmd=" + cmd);
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_SIGNUP_TOKEN, "Invalid signup token");
        }

        // make it idempotent in case client disconnects before it has received the successful return
        if ((identifier.getClaimStatus() == IdentifierClaimStatus.VERIFYING.getCode() ||
                identifier.getClaimStatus() == IdentifierClaimStatus.CLAIMED.getCode())
                && identifier.getVerificationCode() != null
                && identifier.getVerificationCode().equals(verificationCode)) {

            UserLogin rLogin = this.dbProvider.execute((TransactionStatus status) -> {
                if (identifier.getClaimStatus() == IdentifierClaimStatus.VERIFYING.getCode()) {
                    //覆盖账号流程没有闭环，暂时注释掉
//					UserIdentifier existingClaimedIdentifier = this.userProvider.findClaimedIdentifierByToken(namespaceId, identifier.getIdentifierToken());
//					if(existingClaimedIdentifier != null) {
//						existingClaimedIdentifier.setClaimStatus(IdentifierClaimStatus.TAKEN_OVER.getCode());
//						this.userProvider.updateIdentifier(existingClaimedIdentifier);
//					}

                    identifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
                    this.userProvider.updateIdentifier(identifier);
                }

                User user = this.userProvider.findUserById(identifier.getOwnerUid());
                user.setStatus(UserStatus.ACTIVE.getCode());
                user.setNickName(cmd.getNickName());
                user.setGender(UserGender.UNDISCLOSURED.getCode());

                String salt = EncryptionUtils.createRandomSalt();
                user.setSalt(salt);
                try {
                    user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", cmd.getInitialPassword(), salt)));
                } catch (Exception e) {
                    LOGGER.error("encode password failed", e);
                    throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PASSWORD, "Unable to create password hash");

                }
                //update user invitation code
                if (StringUtils.isNotEmpty(cmd.getInvitationCode())) {
                    createInvitationRecord(cmd.getInvitationCode(), identifier, user);
                }
                this.userProvider.updateUser(user);

                UserLogin login = createLogin(namespaceId, user, deviceIdentifier, cmd.getPusherIdentify());
                login.setStatus(UserLoginStatus.LOGGED_IN);
                UserIdentifier uIdentifier = userProvider.findClaimedIdentifierByTokenAndNotUserId(namespaceId, identifier.getIdentifierToken(), identifier.getOwnerUid());
                if (null != uIdentifier) {
                    LOGGER.error("The identify token has been registered, signupToken = {}, cmd = {}", signupToken, cmd);
                    throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_IDENTIFY_TOKEN_REGISTERED, "The identify token has been registered");
                }
                return login;
            });

            // 刷新企业通讯录
            organizationService.processUserForMember(identifier);

            //刷新地址信息
            propertyMgrService.processUserForOwner(identifier);

            // 注册成功事件
            LocalEventBus.publish(event -> {
                LocalEventContext context = new LocalEventContext();
                context.setUid(rLogin.getUserId());
                context.setNamespaceId(namespaceId);
                event.setContext(context);

                event.setEntityType(EntityType.USER.getCode());
                event.setEntityId(rLogin.getUserId());
                event.setEventName(SystemEvent.ACCOUNT_REGISTER_SUCCESS.dft());
            });
            return rLogin;
        }


        LOGGER.error("Invalid verification code or identifier status, signupToken=" + signupToken + ", cmd=" + cmd
                + ", identifierId=" + identifier.getId() + ", ownerUid=" + identifier.getOwnerUid()
                + ", identifierType=" + identifier.getIdentifierType() + ", identifierToken=" + identifier.getIdentifierToken()
                + ", identifierStatus=" + identifier.getClaimStatus() + ", verificationCode=" + identifier.getVerificationCode());
        throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_VERIFICATION_CODE, "Invalid verification code or state");
    }

    @Override
    public UserLogin verifyAndLogonByIdentifier(VerifyAndLogonByIdentifierCommand cmd) {
        List<UserIdentifier> identifiers = this.userProvider.findClaimingIdentifierByToken(cmd.getUserIdentifier());
        if (identifiers.size() == 0) {
            cmd.setInitialPassword("");
            LOGGER.error("Unable to locate the account by specified identifier, cmd=" + cmd);
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_SIGNUP_TOKEN,
                    "Unable to locate the account by specified identifier");
        }
        if (StringUtils.isEmpty(cmd.getInitialPassword())) {
            LOGGER.error("password cannot be empty, cmd=" + cmd);
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PASSWORD, "password cannot be empty");
        }

        int namespaceId = cmd.getNamespaceId() == null ? Namespace.DEFAULT_NAMESPACE : cmd.getNamespaceId();
        for (UserIdentifier identifier : identifiers) {
            // make it idempotent in case client disconnects before it has received the successful return
            // therefore, we also check status of CLAIMED in addition to VERIFYING
            if ((identifier.getClaimStatus() == IdentifierClaimStatus.VERIFYING.getCode() ||
                    identifier.getClaimStatus() == IdentifierClaimStatus.CLAIMED.getCode())
                    && identifier.getVerificationCode() != null
                    && identifier.getVerificationCode().equals(cmd.getVerificationCode())) {

                return this.dbProvider.execute((TransactionStatus status) -> {
                    if (identifier.getClaimStatus() == IdentifierClaimStatus.VERIFYING.getCode()) {

                        UserIdentifier existingClaimedIdentifier = this.userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getUserIdentifier());
                        if (existingClaimedIdentifier != null) {
                            existingClaimedIdentifier.setClaimStatus(IdentifierClaimStatus.TAKEN_OVER.getCode());
                            this.userProvider.updateIdentifier(existingClaimedIdentifier);
                        }

                        identifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
                        this.userProvider.updateIdentifier(identifier);
                    }

                    User user = this.userProvider.findUserById(identifier.getOwnerUid());
                    user.setStatus(UserStatus.ACTIVE.getCode());
                    user.setNickName(cmd.getNickName());
                    String salt = EncryptionUtils.createRandomSalt();
                    user.setSalt(salt);
                    try {
                        user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", cmd.getInitialPassword(), salt)));
                    } catch (Exception e) {
                        cmd.setInitialPassword("");
                        LOGGER.error("Unable to create password hash, cmd=" + cmd, e);
                        throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION, "Unable to create password hash");
                    }
                    //verify invitation code
                    if (StringUtils.isNotEmpty(cmd.getInvitationCode())) {
                        createInvitationRecord(cmd.getInvitationCode(), identifier, user);
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
        for (UserIdentifier identifier : identifiers) {
            if (!isFirst) {
                strBuilder.append(", ");
            }
            strBuilder.append("{identifierId=" + identifier.getId() + ", ownerUid=" + identifier.getOwnerUid()
                    + ", identifierType=" + identifier.getIdentifierType() + ", identifierToken=" + identifier.getIdentifierToken()
                    + ", identifierStatus=" + identifier.getClaimStatus() + ", verificationCode=" + identifier.getVerificationCode() + "}");
            isFirst = false;
        }
        LOGGER.error("Invalid verification code or claim status, cmd=" + cmd + ", identifiersInDb=[" + strBuilder.toString() + "]");
        throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_VERIFICATION_CODE, "Invalid verification code or state");
    }

    private void createInvitationRecord(String invitationCode, UserIdentifier identifier, User user) {
        UserInvitationsDTO invitation = userProvider.findUserInvitationByCode(invitationCode);
        if (invitation == null) {
            LOGGER.error("invalid invitation code.{}", invitationCode);
            return;
        }
        if (invitation.getExpiration().getTime() < DateHelper.currentGMTTime().getTime()) {
            LOGGER.error("invalid invitation code,out of time.{}", invitationCode);
            return;
        }
        if (invitation.getMaxInviteCount() != 0 && invitation.getCurrentInviteCount() > invitation.getMaxInviteCount()) {
            LOGGER.error("invalid invitation code,out of max invitie count.{}", invitationCode);
            return;
        }
        UserInvitationRoster roster = new UserInvitationRoster();
        //generate id ?how
        roster.setContact(identifier.getIdentifierToken());
        roster.setInviteId(invitation.getId());
        roster.setName(user.getAccountName());//how to set name
        userProvider.createUserInvitationRoster(roster, identifier.getOwnerUid());
        //update invitation
        Integer invitationCount = invitation.getCurrentInviteCount();
        invitation.setCurrentInviteCount(invitationCount == null ? 0 : invitationCount.intValue() + 1);
        userProvider.updateInvitation(ConvertHelper.convert(invitation, UserInvitation.class));
        user.setInvitorUid(invitation.getOwnerUid());
        user.setInviteType(invitation.getInviteType());
        //update user info?
        AddUserPointCommand pointCmd = new AddUserPointCommand(user.getId(), PointType.INVITED_USER.name(), userPointService.getItemPoint(PointType.INVITED_USER), invitation.getOwnerUid());
        userPointService.addPoint(pointCmd);
    }

    @Override
    public UserLogin logonDryrun(Integer namespaceId, String userIdentifierToken, String password) {
        User user = this.userProvider.findUserByAccountName(userIdentifierToken);
        if (user == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("findUserByAccountName user is null");
            }
            UserIdentifier identifier = this.userProvider.findClaimedIdentifierByToken(namespaceId, userIdentifierToken);
            if (identifier != null) {
                user = this.userProvider.findUserById(identifier.getOwnerUid());
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("findClaimedIdentifierByToken identifier is null");
                }
            }
        }

        if (user != null // 存在用户
                && UserStatus.fromCode(user.getStatus()) == UserStatus.ACTIVE // 正常状态
                && EncryptionUtils.validateHashPassword(password, user.getSalt(), user.getPasswordHash()) /*密码正确*/) {
            return createLogin(user.getNamespaceId(), user, null, null);
        }
        return null;
    }

    @Override
    public UserLogin logon(int namespaceId, Integer regionCode, String userIdentifierToken, String password, String deviceIdentifier, String pusherIdentify) {
        User user = this.userProvider.findUserByAccountName(userIdentifierToken);
        if (user == null) {
            UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByToken(namespaceId, userIdentifierToken);
            // 把regionCode的检查加上，之前是没有检查的    add by xq.tian 2017/07/12
            if (userIdentifier != null && Objects.equals((userIdentifier.getRegionCode() != null ? userIdentifier.getRegionCode() : 86), regionCode)) {
                user = this.userProvider.findUserById(userIdentifier.getOwnerUid());
                if (user == null) {
                    LOGGER.error("Unable to find owner user of identifier record,  namespaceId={}, userIdentifierToken={}, deviceIdentifier={}, pusherIdentify={}",
                            namespaceId, userIdentifierToken, deviceIdentifier, pusherIdentify);
                    throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_USER_NAME_OR_PASSORD, "User does not exist");
                }
            } else {
                LOGGER.warn("Unable to find identifier record,  namespaceId={}, userIdentifierToken={}, deviceIdentifier={}, pusherIdentify={}",
                        namespaceId, userIdentifierToken, deviceIdentifier, pusherIdentify);
                throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_USER_NAME_OR_PASSORD, "Unable to locate user");
            }
        }

        if (UserStatus.fromCode(user.getStatus()) != UserStatus.ACTIVE)
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_ACCOUNT_NOT_ACTIVATED, "User acount has not been activated yet");

        if (!EncryptionUtils.validateHashPassword(password, user.getSalt(), user.getPasswordHash())) {
            LOGGER.error("Password does not match for " + userIdentifierToken);
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_USER_NAME_OR_PASSORD, "Invalid password");
        }

        if (deviceIdentifier != null && deviceIdentifier.isEmpty())
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

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("User logon succeed, userIdentifierToken=" + userIdentifierToken + ", userLogin=" + login);
        }
        return login;
    }

    @Override
    public UserLogin logonByToken(LoginToken loginToken) {
        assert (loginToken != null);
        String userKey = NameMapper.getCacheKey("user", loginToken.getUserId(), null);
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, String.valueOf(loginToken.getLoginId()));
        UserLogin login = accessor.getMapValueObject(String.valueOf(loginToken.getLoginId()));
        if (login != null && login.getLoginInstanceNumber() == loginToken.getLoginInstanceNumber()) {
            login.setStatus(UserLoginStatus.LOGGED_IN);
            login.setLastAccessTick(DateHelper.currentGMTTime().getTime());
            accessor.putMapValueObject(String.valueOf(loginToken.getLoginId()), login);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("User login succeed, loginToken=" + loginToken + ", userLogin=" + login);
            }

            return login;
        }

//		if(kickoffService.isKickoff(UserContext.current().getNamespaceId(), loginToken)) {
//			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
//					UserServiceErrorCode.ERROR_KICKOFF_BY_OTHER, "Kickoff by others");
//		}

        LOGGER.error("Invalid token or token has expired, userKey=" + userKey + ", loginToken=" + loginToken + ", userLogin=" + login);
        throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_LOGIN_TOKEN,
                "Invalid token or token has expired");
    }

    /**
     * Get all logins by deviceId
     * Added by Janson
     *
     * @param deviceIdentifier
     * @return
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
        if (maxId != null) {
            for (int i = 1; i <= Integer.parseInt(maxId.toString()); i++) {
                String hkeyLogin = String.valueOf(i);
                Accessor accessorLogin = this.bigCollectionProvider.getMapAccessor(deviceKey, hkeyLogin);
                UserLogin login = accessorLogin.getMapValueObject(hkeyLogin);
                if (login != null && login.getStatus() == UserLoginStatus.LOGGED_IN) {
                    logins.add(login);
                }
            }
        }
        return logins;
    }

    private void kickoffLoginByDevice(UserLogin newLogin) {
        if (newLogin.getDeviceIdentifier() == null || newLogin.getDeviceIdentifier().isEmpty()) {
            return;
        }

        List<UserLogin> logins = listLoginByDevice(newLogin.getDeviceIdentifier());
        for (UserLogin login : logins) {
            //For some reason ???
            if (login != newLogin) {
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
        if (maxId == null) {
            maxId = "1";
        } else {
            maxId = Integer.toString(Integer.parseInt(maxId.toString()) + 1);
        }
        Accessor accessorLogin = this.bigCollectionProvider.getMapAccessor(deviceKey, maxId);
        accessorLogin.putMapValueObject(maxId.toString(), newLogin);
        accessor.putMapValueObject(hkeyIndex, Integer.valueOf(maxId.toString()));
    }

    private boolean foundUserLogin(UserLogin login, User user, String deviceIdentifier, LogonRef ref) {
        if (login.getDeviceIdentifier() == null
                || login.getDeviceIdentifier().equals(DeviceIdentifierType.INNER_LOGIN.name())
                || (login.getImpersonationId() != null && login.getImpersonationId() > 0)) {
            //not user login
            return false;
        }

        if (ref.getFoundLogin() != null) {
            //found user login again
            return true;
        }

        if (!deviceIdentifier.equals(login.getDeviceIdentifier())) {
            if (login.getStatus() == UserLoginStatus.LOGGED_IN) {
                if (LOGGER.isInfoEnabled()) {
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
        if (login.getDeviceIdentifier() == null
                || login.getDeviceIdentifier().equals(DeviceIdentifierType.INNER_LOGIN.name())
                || login.getImpersonationId() == null
                || login.getImpersonationId().equals(0)) {
            //not impersonation login
            return false;
        }

        if (!deviceIdentifier.equals(login.getDeviceIdentifier())) {
            if (login.getStatus() == UserLoginStatus.LOGGED_IN) {
                if (LOGGER.isInfoEnabled()) {
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
        if (maxLoginId != null) {
            for (int i = 1; i <= maxLoginId; i++) {
                String hkeyLogin = String.valueOf(i);
                Accessor accessorLogin = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
                UserLogin login = accessorLogin.getMapValueObject(hkeyLogin);
                if (login == null) {
                    continue;
                }

                //found web login
                if (loginType == LoginType.WEB) {
                    if (login.getDeviceIdentifier() == null) {
                        ref.setFoundLogin(login);
                    }
                }
                //found inner login
                if (loginType == LoginType.INNER) {
                    if (deviceIdentifier.equals(login.getDeviceIdentifier())) {
                        ref.setFoundLogin(login);
                        break;
                    }
                }

                //found user login
                if (loginType == LoginType.USER) {
                    if (foundUserLogin(login, user, deviceIdentifier, ref)) {
                        //found twice, clear all for this user
                        if (LOGGER.isInfoEnabled()) {
                            LOGGER.info("User is kickoff state3 remove old all login tokens, userId=" + user.getId()
                                    + ", newNamespaceId=" + namespaceId + ", newDeviceIdentifier=" + deviceIdentifier
                                    + ", oldUserLogin=" + login);
                        }

                        //found twice, delete all logins
                        accessor.getTemplate().delete(accessor.getBucketName());
                        // accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyIndex);
                        ref.setOldDeviceId("");
                        ref.setNextLoginId(1);
                        ref.setFoundLogin(null);
                        break;
                    }
                }

                //found impersonation
                if (loginType == LoginType.IMPERSONATION) {
                    if (foundImpersonationLogin(login, user, deviceIdentifier, ref)) {
                        break;
                    }
                }

                //check
                if (login.getLoginId() >= nextLoginId) {
                    nextLoginId = login.getLoginId() + 1;
                }

            }
        }

        if (ref.getFoundLogin() != null) {
            ref.setNextLoginId(ref.getFoundLogin().getLoginId());
            return true;
        } else {
            ref.setNextLoginId(nextLoginId);
        }
        return false;
    }

    private UserLogin createLogin(int namespaceId, User inUser, String deviceIdentifier, String pusherIdentify) {
        return createLogin(namespaceId, inUser, deviceIdentifier, pusherIdentify, null);
    }

    private UserLogin createLogin(int namespaceId, User inUser, String deviceIdentifier, String pusherIdentify, LoginToken loginToken) {
        Boolean isNew = false;
        User user = null;
        Long impId = null;
        UserImpersonation userImp = userImpersonationProvider.getUserImpersonationByOwnerId(inUser.getId());
        if (userImp == null) {
            user = inUser;
        } else {
            user = userProvider.findUserById(userImp.getTargetId());
            if (user == null) {
                LOGGER.warn("get impersonation userId error. userId=" + userImp.getTargetId() + ", impId=" + userImp.getId());
                user = inUser;
            }
            impId = inUser.getId();
        }

        String userKey = NameMapper.getCacheKey("user", user.getId(), null);

        // get "index" accessor
        String hkeyIndex = "0";
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyIndex);
        // Object o = accessor.getMapValueObject(hkeyIndex);
        LogonRef ref = new LogonRef();
        ref.setNamespaceId(namespaceId);
        ref.setOldDeviceId("");
        ref.setImpId(impId);
        if (deviceIdentifier == null || deviceIdentifier.isEmpty()) {
            foundLoginByLoginType(user, null, LoginType.WEB, ref);
        } else if (deviceIdentifier.equals(DeviceIdentifierType.INNER_LOGIN.name())) {
            foundLoginByLoginType(user, deviceIdentifier, LoginType.INNER, ref);
        } else if (impId != null) {
            foundLoginByLoginType(user, deviceIdentifier, LoginType.IMPERSONATION, ref);
        } else {
            foundLoginByLoginType(user, deviceIdentifier, LoginType.USER, ref);
        }

        String appVersion = UserContext.current().getVersion();
        UserLogin foundLogin = ref.getFoundLogin();
        if (foundLogin == null) {
            foundLogin = new UserLogin(namespaceId, user.getId(), ref.getNextLoginId(), deviceIdentifier, pusherIdentify, appVersion);
            // 统一用户那边登录的 loginInstanceNumber,这里需要和那边一样才行
            if (loginToken != null) {
                foundLogin.setLoginInstanceNumber(loginToken.getLoginInstanceNumber());
            }
            accessor.putMapValueObject(hkeyIndex, ref.getNextLoginId());

            isNew = true;
        }

        foundLogin.setImpersonationId(impId);
        foundLogin.setStatus(UserLoginStatus.LOGGED_IN);
        foundLogin.setLastAccessTick(DateHelper.currentGMTTime().getTime());
        foundLogin.setPusherIdentify(pusherIdentify);
        foundLogin.setAppVersion(appVersion);
        String hkeyLogin = String.valueOf(ref.getNextLoginId());
        Accessor accessorLogin = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
        LOGGER.debug("createLogin|hId = " + hkeyLogin);
        accessorLogin.putMapValueObject(hkeyLogin, foundLogin);

        if (isNew && deviceIdentifier != null && (!deviceIdentifier.equals(DeviceIdentifierType.INNER_LOGIN.name()))) {
            //Kickoff other login in this devices which is not inner login
            kickoffLoginByDevice(foundLogin);
        }

        //TODO better here, get token from foundLogin
        LoginToken token = new LoginToken(foundLogin.getUserId(), foundLogin.getLoginId(), foundLogin.getLoginInstanceNumber(), foundLogin.getImpersonationId());

        if (!ref.getOldDeviceId().isEmpty() && !ref.getOldLoginToken().toString().equals(token.toString())) {
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
        if (login != null) {
            login.setLoginBorderId(null);
            login.setLastAccessTick(DateHelper.currentGMTTime().getTime());

            LOGGER.debug("Unregister login connection for login: {}", login.toString());
            LOGGER.debug("unregisterLoginConnection|hId = " + hkeyLogin);
            accessor.putMapValueObject(hkeyLogin, login);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @LocalBusMessageHandler("border.close")
    private LocalBusSubscriber.Action onBorderClose(Object sender, String subject, Object args, String subscriptionPath) {
        LOGGER.debug("Process border down event, borderId: {}");

        Border border = (Border) args;

        String key = String.valueOf(border.getId());
        Accessor accessor = this.bigCollectionProvider.getMapAccessor("border", key);

        Map entries = accessor.getTemplate().opsForHash().entries(key);
        for (Object hKey : entries.keySet()) {
            if (hKey != null) {
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
        String hKey = String.valueOf(userId) + String.valueOf(loginId);
        Accessor accessor = this.bigCollectionProvider.getMapAccessor("border", key);

        accessor.putMapValueObject(hKey, "");
    }

    @SuppressWarnings("unchecked")
    private void unregisterBorderTracker(int borderId, long userId, int loginId) {
        LOGGER.debug("Unregister border tracker, borderId: {}, userId: {}, loginId {}", borderId, userId, loginId);

        String key = String.valueOf(borderId);
        String hKey = String.valueOf(userId) + String.valueOf(loginId);
        Accessor accessor = this.bigCollectionProvider.getMapAccessor("border", key);

        accessor.getTemplate().opsForHash().delete(key, hKey);
    }

    public UserLogin registerLoginConnection(LoginToken loginToken, int borderId, String borderSessionId) {
        String userKey = NameMapper.getCacheKey("user", loginToken.getUserId(), null);

        String hkeyLogin = String.valueOf(loginToken.getLoginId());
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
        UserLogin login = accessor.getMapValueObject(hkeyLogin);
        if (login != null && login.getStatus() == UserLoginStatus.LOGGED_IN) {
            //Save loginBorderId here
            login.setLoginBorderId(borderId);
            login.setBorderSessionId(borderSessionId);
            login.setLastAccessTick(DateHelper.currentGMTTime().getTime());
            accessor.putMapValueObject(hkeyLogin, login);

            // 发布用户切换App到前台事件   add by xq.tian 2017/07/13
            applicationEventPublisher.publishEvent(new BorderRegisterEvent(login));

            // 打开App事件
            LocalEventBus.publish(event -> {
                LocalEventContext context = new LocalEventContext();
                context.setNamespaceId(login.getNamespaceId());
                context.setUid(login.getUserId());
                event.setContext(context);

                event.setEntityType(EntityType.USER.getCode());
                event.setEntityId(login.getUserId());
                event.setEventName(SystemEvent.ACCOUNT_OPEN_APP.dft());
            });

            registerBorderTracker(borderId, loginToken.getUserId(), loginToken.getLoginId());
            return login;
        } else {
            LOGGER.warn("Unable to find UserLogin in big map, borderId=" + borderId
                    + ", loginToken=" + StringHelper.toJsonString(loginToken) + ", borderSessionId=" + borderSessionId);
            return null;
        }
    }

    /**
     * 微信端活跃记录
     */
    @Override
    public void registerWXLoginConnection(HttpServletRequest request) {
    	/*if(request == null){
    		LOGGER.info("request is null ");
    	}else{
    		LOGGER.info(request.toString());
    	}*/	 
    	 Cookie loginTokenCookie = findCookieInRequest("token",request);
    	 if(loginTokenCookie == null){
    		 LOGGER.info("loginTokenCookie is null ");
    		 return ;
    	 }
         String token = loginTokenCookie.getValue();
         LoginToken loginToken = WebTokenGenerator.getInstance().fromWebToken(token, LoginToken.class);
         if(token == null)
             return ;
         
        String userKey = NameMapper.getCacheKey("user", loginToken.getUserId(), null);

        String hkeyLogin = String.valueOf(loginToken.getLoginId());
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
        UserLogin login = accessor.getMapValueObject(hkeyLogin);
        if (login != null && login.getStatus() == UserLoginStatus.LOGGED_IN) {
            //Save loginBorderId here
            login.setLoginBorderId(null);
            login.setBorderSessionId(null);
            login.setLastAccessTick(DateHelper.currentGMTTime().getTime());
            accessor.putMapValueObject(hkeyLogin, login);

            // 发布用户切换出场景到前台事件   add by liangming.huang 2018/08/16
            applicationEventPublisher.publishEvent(new BorderRegisterEvent(login));

        }
    }
    
    private static Cookie findCookieInRequest(String name, HttpServletRequest request) {
        List<Cookie> matchedCookies = new ArrayList<>();
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals(name)) {
                    matchedCookies.add(cookie);
                }
            }
        }
        if(matchedCookies.size() > 0)
            return matchedCookies.get(matchedCookies.size() - 1);
        return null;
    }
    public UserLogin unregisterLoginConnection(LoginToken loginToken, int borderId, String borderSessionId) {
        String userKey = NameMapper.getCacheKey("user", loginToken.getUserId(), null);
        String hkeyLogin = String.valueOf(loginToken.getLoginId());
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
        UserLogin login = accessor.getMapValueObject(hkeyLogin);
        if (login != null) {
            boolean canRemove = false;
            if (login.getLoginBorderId() == null || login.getBorderSessionId() == null) {
                canRemove = true;
            }

            //如果 login.getLoginBorderId() 与 login.getBorderSessionId() 都不为空，则判断登录的信息有任何不相等，则此 borderId 的会话已经无效
            if (!canRemove && !(login.getLoginBorderId().equals(borderId) && login.getBorderSessionId().equals(borderSessionId))) {
                canRemove = false;
            } else {
                canRemove = true;
            }

            if (canRemove) {
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
        if (login != null) {
            login.setLoginBorderId(null);
            login.setLastAccessTick(DateHelper.currentGMTTime().getTime());
            LOGGER.debug("unregisterLoginConnection|hId = " + hkeyLogin);
            accessor.putMapValueObject(hkeyLogin, login);

            if (userLogin.getLoginBorderId() != null) {
                unregisterBorderTracker(userLogin.getLoginBorderId(), userLogin.getUserId(), userLogin.getLoginId());
            }

        }

        return login;
    }

    public void saveLogin(UserLogin login) {
        String userKey = NameMapper.getCacheKey("user", login.getUserId(), null);
        String hkeyLogin = String.valueOf(login.getLoginId());
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
        LOGGER.debug("saveLogin|hId = " + hkeyLogin);
        accessor.putMapValueObject(hkeyLogin, login);
    }

    @Override
    public List<UserLogin> listUserLogins(long uid) {
        if (uid == 0) {
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_UNABLE_TO_LOCATE_USER, "uid=0 not found");
        }

        List<UserLogin> logins = new ArrayList<>();
        String userKey = NameMapper.getCacheKey("user", uid, null);

        // get "index" accessor
        String hkeyIndex = "0";
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, hkeyIndex);
        Object maxLoginId = accessor.getMapValueObject(hkeyIndex);
        if (maxLoginId != null) {
            // 日志太多，先注释掉 by lqs 20171102
            // LOGGER.debug("maxLoginId: "+maxLoginId);
            // LOGGER.debug("maxLoginId.toString: "+maxLoginId.toString());
            for (int i = 1; i <= Integer.parseInt(maxLoginId.toString()); i++) {
                String hkeyLogin = String.valueOf(i);
                Accessor accessorLogin = this.bigCollectionProvider.getMapAccessor(userKey, hkeyLogin);
                UserLogin login = accessorLogin.getMapValueObject(hkeyLogin);
                if (login != null) {
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

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("User is logoff, userLogin=" + login);
        }
    }

    @Override
    public boolean isValidLoginToken(LoginToken loginToken) {
        assert (loginToken != null);

        //added by janson, isKickoff ? 2017-03-29
//		if(kickoffService.isKickoff(UserContext.getCurrentNamespaceId(), loginToken)) {
////			kickoffService.remoteKickoffTag(UserContext.getCurrentNamespaceId(), loginToken);
//	      throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_KICKOFF_BY_OTHER, "Kickoff by others");
//		}

        String userKey = NameMapper.getCacheKey("user", loginToken.getUserId(), null);
        Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, String.valueOf(loginToken.getLoginId()));
        UserLogin login = accessor.getMapValueObject(String.valueOf(loginToken.getLoginId()));
        if (login != null && login.getLoginInstanceNumber() == loginToken.getLoginInstanceNumber()) {
            return true;
        } else {
            // 去统一用户那边检查登录状态
            String tokenString = WebTokenGenerator.getInstance().toWebToken(loginToken);
            com.everhomes.rest.user.user.UserInfo userInfo = null;
            try {
                userInfo = sdkUserService.validateToken(tokenString);
            } catch (Exception e) {
                // e.printStackTrace();
            }
            if (userInfo != null && userInfo.getId().equals(loginToken.getUserId())) {
                User user = userProvider.findUserById(userInfo.getId());
                if (user != null) {
                    LOGGER.info("User service check success, loginToken={}", loginToken);
                    createLogin(userInfo.getNamespaceId(), user, null, null, loginToken);
                    return true;
                }
            } else {
                LOGGER.info("User service check failure, loginToken={}", loginToken);
            }

            LOGGER.error("Invalid token, userKey=" + userKey + ", loginToken=" + loginToken + ", login=" + login);
            return false;
        }
    }

    private static boolean isVerificationExpired(Timestamp ts) {
        // TODO hard-code expiration time to 10 minutes
        return DateHelper.currentGMTTime().getTime() - ts.getTime() > 10 * 60000;
    }

    private Long getDateDifference(Timestamp compareValue) {
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String strCompare = format.format(compareValue);
        String strNow = format.format(now);
        long day = 0L;

        try {
            Date dateNow = format.parse(strNow);
            Date dateCompare = format.parse(strCompare);
            day = (dateNow.getTime() - dateCompare.getTime()) / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            LOGGER.error("compareValue : {}", compareValue, e);
        }


        return day;
    }

    @Override
    public UserInfo getUserInfo() {
        User user = this.userProvider.findUserById(UserContext.current().getUser().getId());
        assert (user != null);

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

        if (user.getCreateTime() != null) {
            Long days = getDateDifference(user.getCreateTime());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("days", days);
            String scope = UserNotificationTemplateCode.SCOPE;
            int code = UserNotificationTemplateCode.USER_REGISTER_DAYS;

            String notifyText = localeTemplateService.getLocaleTemplateString(user.getNamespaceId(), scope, code, user.getLocale(), map, "");
            info.setRegisterDaysDesc(notifyText);

        }
        if (user.getCommunityId() != null) {
            Community community = communityProvider.findCommunityById(user.getCommunityId());
            if (community != null) {
                info.setRegionId(community.getCityId());
                Region region = regionProvider.findRegionById(community.getCityId());
                if (region != null) {
                    info.setRegionName(region.getName());
                    info.setRegionPath(region.getPath());
                }
                info.setCommunityName(community.getName());
                info.setCommunityType(community.getCommunityType());
            }
        }
        if (user.getBirthday() != null) {
            info.setBirthday(new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthday()));
        }

        if (user.getHomeTown() != null) {
            Category category = this.categoryProvider.findCategoryById(user.getHomeTown());
            if (category != null)
                info.setHometownName(category.getName());
        }
        List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(user.getId());

        List<String> phones = identifiers.stream()
                .filter((r) -> {
                    return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.MOBILE;
                })
                .map(EhUserIdentifiers::getIdentifierToken)
                .collect(Collectors.toList());
        info.setPhones(phones);

        List<Integer> regionCodes = identifiers.stream()
                .filter((r) -> {
                    return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.MOBILE;
                })
                .map(EhUserIdentifiers::getRegionCode)
                .collect(Collectors.toList());
        info.setRegionCodes(regionCodes);

        List<String> emails = identifiers.stream().filter((r) -> {
            return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.EMAIL;
        })
                .map(EhUserIdentifiers::getIdentifierToken)
                .collect(Collectors.toList());
        info.setEmails(emails);

		// 用户当前选择的实体（可能有小区、家庭、机构）
		List<UserCurrentEntity> entityList = listUserCurrentEntity(user.getId());
		if(entityList.size() > 0) {
			//在返回前排个序
			sortCurrentEntity(entityList);info.setEntityList(entityList);
		}

        GetUserTreasureCommand cmd = new GetUserTreasureCommand();
        cmd.setUid(user.getId());
        GetUserTreasureResponse treasure = userPointService.getUserTreasure(cmd);
        if (treasure != null) {
            BeanUtils.copyProperties(treasure, info);
        }
        return info;
    }

    @Override
    public void setUserInfo(SetUserInfoCommand cmd) {
        User user = this.userProvider.findUserById(UserContext.current().getUser().getId());

        // 修改用户信息之前的user
        User oldUser = ConvertHelper.convert(user, User.class);

        user.setAvatar(cmd.getAvatarUri());
        String birthdayString = cmd.getBirthday();
        if (StringUtils.isNotEmpty(birthdayString)) {
            try {
                SimpleDateFormat fromat = new SimpleDateFormat("yyyy-MM-dd");
                user.setBirthday(new java.sql.Date(fromat.parse(birthdayString).getTime()));
            } catch (Exception e) {
                throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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

        UserIdentifier emailIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.EMAIL.getCode());
        if (emailIdentifier != null) {
            emailIdentifier.setIdentifierToken(cmd.getEmail());
            this.userProvider.updateIdentifier(emailIdentifier);
        }else {
            if (!StringUtils.isBlank(cmd.getEmail())) {
                UserIdentifier userIdentifier = new UserIdentifier();
                userIdentifier.setOwnerUid(user.getId());
                userIdentifier.setIdentifierToken(cmd.getEmail());
                userIdentifier.setIdentifierType(IdentifierType.EMAIL.getCode());
                userIdentifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
                userIdentifier.setNamespaceId(UserContext.getCurrentNamespaceId());
                userIdentifier.setCreateTime(new Timestamp(new Date().getTime()));
                userIdentifier.setNotifyTime(new Timestamp(new Date().getTime()));
                this.userProvider.createIdentifier(userIdentifier);
            }
        }

        // 完善个人信息事件
        LocalEventBus.publish(event -> {
            LocalEventContext context = new LocalEventContext();
            context.setUid(user.getId());
            context.setNamespaceId(user.getNamespaceId());
            event.setContext(context);

            event.setEntityType(EntityType.USER.getCode());
            event.setEntityId(user.getId());
            event.setEventName(SystemEvent.ACCOUNT_COMPLETE_INFO.dft());

            event.addParam("oldUser", StringHelper.toJsonString(oldUser));
            event.addParam("newUser", StringHelper.toJsonString(user));
        });
    }

    @Override
    public void setUserAccountInfo(SetUserAccountInfoCommand cmd) {
        if (cmd.getAccountName() == null || cmd.getAccountName().isEmpty() ||
                cmd.getPassword() == null || cmd.getPassword().isEmpty())
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "accountName and password can not be empty");

        this.coordinationProvider.getNamedLock(CoordinationLocks.SETUP_ACCOUNT_NAME.getCode()).enter(() -> {
            User user = this.userProvider.findUserById(UserContext.current().getUser().getId());
            User userOther = this.userProvider.findUserByAccountName(cmd.getAccountName());
            if (userOther != null && userOther.getId() != user.getId())
                throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_ACCOUNT_NAME_ALREADY_EXISTS,
                        "accountName is already used by others");

            user.setAccountName(cmd.getAccountName());
            String salt = EncryptionUtils.createRandomSalt();
            user.setSalt(salt);
            user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", cmd.getPassword(), salt)));
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
        if (community != null) {
            return ConvertHelper.convert(community, CommunityDTO.class);
        } else {
            return null;
        }
    }

    /**
     * 当用户从不同版的APP登录进来时，若之前没有选中的园区，则默认设置一个
     *
     * @return 选中的园区ID
     */
    @Override
    public Long setDefaultCommunity(Long userId, Integer namespaceId) {
        //不能从UserContext获取，应该由外面传入 by sfyan 20160601
        //      User user = UserContext.current().getUser();
        Long communityId = 0L;

        try {
            List<UserCurrentEntity> entityList = listUserCurrentEntity(userId);
            if (!containPartnerCommunity(namespaceId, entityList)) {
                List<NamespaceResource> resources = namespaceResourceProvider.listResourceByNamespace(namespaceId, NamespaceResourceType.COMMUNITY);
                if (resources != null && resources.size() == 1) {
                    communityId = resources.get(0).getResourceId();
                    updateUserCurrentCommunityToProfile(userId, communityId, namespaceId);
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("Set default community, userId=" + userId + ", communityId=" + communityId
                                + ", namespaceId=" + namespaceId);
                    }
                } else {
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("Community not found, ignore to set default community, userId=" + userId
                                + ", namespaceId=" + namespaceId);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to set default community, userId=" + userId + ", namespaceId=" + namespaceId, e);
        }

        return communityId;
    }

    /**
     * 当用户从不同版的APP登录进来时，若之前没有选中的园区，则默认设置一个
     * 与setDefaultCommunity不同之处在于resources.size() > 1
     *
     * @return 选中的园区ID
     */
    @Override
    public Long setDefaultCommunityForWx(Long userId, Integer namespaceId) {
        Long communityId = 0L;
        try {
            List<UserCurrentEntity> entityList = listUserCurrentEntity(userId);
            if (!containPartnerCommunity(namespaceId, entityList)) {
                List<NamespaceResource> resources = namespaceResourceProvider.listResourceByNamespace(namespaceId, NamespaceResourceType.COMMUNITY);
                if (resources != null && resources.size() > 1) {
                    communityId = resources.get(0).getResourceId();
                    updateUserCurrentCommunityToProfile(userId, communityId, namespaceId);
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("Set default community, userId=" + userId + ", communityId=" + communityId
                                + ", namespaceId=" + namespaceId);
                    }
                } else {
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("Community not found, ignore to set default community, userId=" + userId
                                + ", namespaceId=" + namespaceId);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to set default community, userId=" + userId + ", namespaceId=" + namespaceId, e);
        }

        return communityId;
    }

    private boolean containPartnerCommunity(Integer namespaceId, List<UserCurrentEntity> entityList) {
        if (entityList == null || entityList.size() == 0) {
            return false;
        }

        boolean isFound = false;
        for (UserCurrentEntity entity : entityList) {
            UserCurrentEntityType type = UserCurrentEntityType.fromCode(entity.getEntityType());
            if (namespaceId.equals(entity.getNamespaceId())
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
        if (community != null) {
            String key = UserCurrentEntityType.COMMUNITY_RESIDENTIAL.getUserProfileKey();
            if (CommunityType.fromCode(community.getCommunityType()) == CommunityType.COMMERCIAL) {
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

        return identifiers.stream().map((r) -> {
            return ConvertHelper.convert(r, UserIdentifierDTO.class);
        })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserIdentifier(long identifierId) {
        User user = this.userProvider.findUserById(UserContext.current().getUser().getId());
        long uid = user.getId();

        UserIdentifier identifier = this.userProvider.findIdentifierById(identifierId);
        if (identifier == null)
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Could not find the identifier");

        if (identifier.getOwnerUid() != uid)
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED,
                    "Access denied");

        if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty())
            throw errorWith(UserServiceErrorCode.SCOPE,
                    UserServiceErrorCode.ERROR_ACCOUNT_PASSWORD_NOT_SET,
                    "Account password has not been properly setup yet");

        this.userProvider.deleteIdentifier(identifier);
    }

    @Override
    public void resendVerficationCode(ResendVerificationCodeByIdentifierCommand cmd, HttpServletRequest request) {
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getIdentifier());
        if (userIdentifier == null) {
            LOGGER.error("cannot find user identifierToken.identifierToken={}", cmd.getIdentifier());
            throw errorWith(UserServiceErrorCode.SCOPE,
                    UserServiceErrorCode.ERROR_USER_NOT_EXIST, "can not find user identifierToken or status is error");
        }

        this.verifySmsTimes("fogotPasswd", userIdentifier.getIdentifierToken(), request.getHeader(X_EVERHOMES_DEVICE));

        String verificationCode = RandomGenerator.getRandomDigitalString(6);
        userIdentifier.setVerificationCode(verificationCode);
        userIdentifier.setRegionCode(cmd.getRegionCode());
        userIdentifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        sendVerificationCodeSms(userIdentifier.getNamespaceId(), this.getRegionPhoneNumber(userIdentifier.getIdentifierToken(), userIdentifier.getRegionCode()), verificationCode);

        this.userProvider.updateIdentifier(userIdentifier);

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Send notification code " + verificationCode + " to " + userIdentifier.getIdentifierToken());
        }
	}

	@Override
	public void resendVerficationCodeByApp(ResendVerificationCodeByIdentifierCommand cmd, HttpServletRequest request) {
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getIdentifier());
		if(userIdentifier==null){
			LOGGER.error("cannot find user identifierToken.identifierToken={}",cmd.getIdentifier());
			throw errorWith(UserServiceErrorCode.SCOPE,
					UserServiceErrorCode.ERROR_USER_NOT_EXIST, "can not find user identifierToken or status is error");
		}

		//判断手机号的用户与当前用户是否一致
		if (userIdentifier.getOwnerUid() == null || !userIdentifier.getOwnerUid().equals(UserContext.currentUserId())) {
			LOGGER.error("phone not match user error");
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_PHONE_NOT_MATCH_USER,
					"phone not match user error");
		}

		if (cmd.getRegionCode() != null && userIdentifier.getRegionCode() != null && !cmd.getRegionCode().equals(userIdentifier.getRegionCode())) {
			LOGGER.error("phone not match user error");
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_PHONE_NOT_MATCH_USER,
					"phone not match user error");
		}

		this.verifySmsTimes("fogotPasswd", userIdentifier.getIdentifierToken(), request.getHeader(X_EVERHOMES_DEVICE));

		String verificationCode = RandomGenerator.getRandomDigitalString(6);
		userIdentifier.setVerificationCode(verificationCode);
		userIdentifier.setRegionCode(cmd.getRegionCode());
		userIdentifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

		sendVerificationCodeSms(userIdentifier.getNamespaceId(), this.getRegionPhoneNumber(userIdentifier.getIdentifierToken(), userIdentifier.getRegionCode()), verificationCode);

		this.userProvider.updateIdentifier(userIdentifier);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Send notification code " + verificationCode + " to " + userIdentifier.getIdentifierToken());
		}
    }



	@Override
	public void sendCodeWithPictureValidate(SendCodeWithPictureValidateCommand cmd, HttpServletRequest request) {

        //校验图片验证码
        Boolean validateFlag = pictureValidateService.validateCode(request, cmd.getPictureCode());
        if (!validateFlag) {
            LOGGER.error("invalid picture code, validate fail");
            throw errorWith(PictureValidateServiceErrorCode.SCOPE,
                    PictureValidateServiceErrorCode.ERROR_INVALID_CODE, "invalid picture code");
        }

        //发送手机验证码
        ResendVerificationCodeByIdentifierCommand sendcmd = new ResendVerificationCodeByIdentifierCommand();
        sendcmd.setNamespaceId(cmd.getNamespaceId());
        sendcmd.setIdentifier(cmd.getIdentifier());
        sendcmd.setRegionCode(cmd.getRegionCode());
        resendVerficationCode(sendcmd, request);
    }

    @Override
	public void sendCodeWithPictureValidateByApp(SendCodeWithPictureValidateCommand cmd, HttpServletRequest request) {
		//校验图片验证码
		Boolean validateFlag = pictureValidateService.validateCodeByApp(cmd.getPictureCode());
		if(!validateFlag){
			LOGGER.error("invalid picture code, validate fail");
			throw errorWith(PictureValidateServiceErrorCode.SCOPE,
					PictureValidateServiceErrorCode.ERROR_INVALID_CODE, "invalid picture code");
		}

		//发送手机验证码
		ResendVerificationCodeByIdentifierCommand sendcmd = new ResendVerificationCodeByIdentifierCommand();
		sendcmd.setNamespaceId(cmd.getNamespaceId());
		sendcmd.setIdentifier(cmd.getIdentifier());
		sendcmd.setRegionCode(cmd.getRegionCode());
		resendVerficationCodeByApp(sendcmd, request);
	}

	@Override
    public UserInvitationsDTO createInvatation(CreateInvitationCommand cmd) {
        // validate
        assert cmd.getInviteType() != null;
        assert StringUtils.isNotEmpty(cmd.getTargetEntityType());
        User user = UserContext.current().getUser();
        List<UserIdentifier> indentifiers = userProvider.listUserIdentifiersOfUser(user.getId());
        if (CollectionUtils.isEmpty(indentifiers)) {
            LOGGER.error("cannot find user");
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_USER_NOT_EXIST, "cannot find user");
        }
        ClaimedAddressInfo address = null;
        try {
            ClaimAddressCommand claimCmd = new ClaimAddressCommand();
            claimCmd.setApartmentName(cmd.getAptNumber());
            claimCmd.setBuildingName(cmd.getBuildingNum());
            claimCmd.setCommunityId(cmd.getCommunityId());
            address = addressService.claimAddress(claimCmd);
        } catch (Exception e) {
            //TODO
            //skip all exception
            LOGGER.warn("cmd : {}", cmd, e);
        }

        //get enum type
        InvitationType inviteTye = InvitationType.fromCode(cmd.getInviteType());
        EntityType entityType = EntityType.fromCode(cmd.getTargetEntityType());

        UserInvitation invitations = new UserInvitation();
        invitations.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        // current count
        invitations.setCurrentInviteCount(0);

        invitations.setOwnerUid(user.getId());
        if (address != null) {
            invitations.setMaxInviteCount(1);
        } else {
            invitations.setMaxInviteCount(0);
        }
        invitations.setCurrentInviteCount(0);
        invitations.setInviteType(inviteTye.getCode());
        invitations.setTargetEntityId(cmd.getTargetEntityId());
        invitations.setTargetEntityType(entityType.getCode());
        invitations.setStatus(InvitationStatus.active.getCode());
        int maxTryCount = 2;
        int tryCountPerTime = 3;
        for (int index = 0; index < maxTryCount; index++) {
            for (int rindex = 0; rindex < tryCountPerTime; rindex++) {
                try {
                    tryGenerateInvitation(indentifiers.get(0).getIdentifierToken(), invitations);
                    //send notify
                    sendNotify(user.getId(), "");
                    return ConvertHelper.convert(invitations, UserInvitationsDTO.class);
                } catch (Exception e) {
                    LOGGER.error("create invitation code failed,retry", e);
                }
                try {
                    //sleep for while
                    Thread.sleep(200);
                } catch (Exception e) {

                }
            }
        }
        LOGGER.error("cannot create invitation code");
        throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVITATION_CODE, "invitation code create failed");
    }

    private void sendNotify(Long uid, String message) {
        //TODO
    }

    private void tryGenerateInvitation(String indentifier, UserInvitation invitations) {
        long expirationTime = configurationProvider.getLongValue(EXPIRE_TIME, 4320);
        String inviteCode = EncryptionUtils.genInviteCodeByIdentifier(indentifier);
        invitations.setExpiration(new Timestamp(DateHelper.currentGMTTime().getTime() + 60 * 1000 * expirationTime));
        invitations.setInviteCode(inviteCode);
        this.userProvider.createInvitation(invitations);
    }

    @Override
    public void assumePortalRole(AssumePortalRoleCommand cmd) {
        if (cmd.getRoleId() == null)
            throw errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter, roleId could not be empty");

        Role role = this.aclProvider.getRoleById(cmd.getRoleId().longValue());
        if (role == null)
            throw errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter, roleId should be a valid one");

        PortalRoleResolver resolver = PlatformContext.getComponent(PortalRoleResolver.PORTAL_ROLE_RESOLVER_PREFIX + role.getAppId());
        if (resolver == null)
            throw errorWith(ErrorCodes.SCOPE_GENERAL,
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
        if (userIndentifier == null) {
            return null;
        }
        return userProvider.findUserById(userIndentifier.getOwnerUid());
    }

    @Override
    public UserInfo getUserInfo(Long uid) {
        if (uid == null) {
            LOGGER.error("invalid uid,cannot null");
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS, "uid cannot be null");
        }
        User user = UserContext.current().getUser();
        if (user.getId().longValue() == uid.longValue()) {
            return getUserInfo();
        }
        User queryUser = userProvider.findUserById(uid);
        if (queryUser == null) {
            LOGGER.error("cannot find user any information.uid={}", uid);
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_USER_NOT_EXIST, "cannot find user information");
        }
        List<FamilyDTO> currentUserFamilies = familyProvider.getUserFamiliesByUserId(user.getId());
        List<FamilyDTO> queryUserFamilies = familyProvider.getUserFamiliesByUserId(queryUser.getId());
        if (CollectionUtils.isEmpty(currentUserFamilies)) {
            LOGGER.error("cannot find any family information");
            return null;
        }
        LOGGER.info("Find current user family {},query user family {}", currentUserFamilies, queryUserFamilies);
        //if have same family ,the result >0
        List<Long> queryUserFamilyIds = queryUserFamilies.stream().map(r -> {
            Long id = r.getId();
            return id;
        }).collect(Collectors.toList());
        List<FamilyDTO> currUserFamilies = new ArrayList<FamilyDTO>();
        for (FamilyDTO family : currentUserFamilies) {

            if (queryUserFamilyIds.contains(family.getId()))
                currUserFamilies.add(family);
        }
        //        currentUserFamilies.retainAll(queryUserFamilies);
        if (CollectionUtils.isEmpty(currUserFamilies)) {
            LOGGER.error("cannot find user information ,because the current user and to lookup user in diff family.current_uid={},uid={}", user.getId(), uid);
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PERMISSION, "permission denied");
        }
        UserInfo info = ConvertHelper.convert(queryUser, UserInfo.class);
        // 把用户头像的处理独立到一个方法中 by lqs 20151211
        populateUserAvatar(info, queryUser.getAvatar());
        //		info.setAvatar(queryUser.getAvatar());
        //		try{
        //			String url=contentServerService.parserUri(queryUser.getAvatar(),EntityType.USER.getCode(),queryUser.getId());
        //			info.setAvatarUrl(url);
        //		}catch(Exception e){
        //			LOGGER.error("Failed to parse user avatar uri, userId=" + uid + ", avatar=" + info.getAvatar());
        //		}
        if (queryUser.getCommunityId() != null) {
            Community community = communityProvider.findCommunityById(queryUser.getCommunityId());
            if (community != null) {
                info.setRegionId(community.getCityId());
                Region region = regionProvider.findRegionById(community.getCityId());
                if (region != null) {
                    info.setRegionName(region.getName());
                    info.setRegionPath(region.getPath());
                }
                info.setCommunityName(community.getName());
            }
        }

        if (queryUser.getBirthday() != null) {
            info.setBirthday(new SimpleDateFormat("yyyy-MM-dd").format(queryUser.getBirthday()));
        }
        if (queryUser.getHomeTown() != null) {
            Category category = this.categoryProvider.findCategoryById(queryUser.getHomeTown());
            if (category != null)
                info.setHometownName(category.getName());
        }
        List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(queryUser.getId());
        List<String> phones = identifiers.stream().filter((r) -> {
            return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.MOBILE;
        })
                .map((r) -> {
                    return r.getIdentifierToken();
                })
                .collect(Collectors.toList());
        info.setPhones(phones);
        List<String> emails = identifiers.stream().filter((r) -> {
            return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.EMAIL;
        })
                .map((r) -> {
                    return r.getIdentifierToken();
                })
                .collect(Collectors.toList());
        info.setEmails(emails);

        // 用户当前选择的实体（可能有小区、家庭、机构）
        List<UserCurrentEntity> entityList = listUserCurrentEntity(user.getId());
        if (entityList.size() > 0) {
            info.setEntityList(entityList);
        }

        return info;
    }

    private UserInfo getUserBasicInfoByQueryUser(User queryUser, boolean hideMobile) {
        UserInfo info = ConvertHelper.convert(queryUser, UserInfo.class);
        List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(queryUser.getId());
        List<String> phones = identifiers.stream().filter((r) -> {
            return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.MOBILE;
        })
                .map((r) -> {
                    if (hideMobile) {
                        String token = r.getIdentifierToken();
                        String prefix = token.substring(0, 3);
                        String end = token.substring(token.length() - 4, token.length());
                        //replace phone number with ****
                        return String.format("%s%s%s", prefix, "****", end);
                    } else {
                        return r.getIdentifierToken();
                    }

                })
                .collect(Collectors.toList());
        info.setPhones(phones);

        List<String> emails = identifiers.stream().filter((r) -> {
            return IdentifierType.fromCode(r.getIdentifierType()) == IdentifierType.EMAIL;
        })
                .map((r) -> {
                    return r.getIdentifierToken();
                })
                .collect(Collectors.toList());
        info.setEmails(emails);
        if (queryUser.getBirthday() != null)
            info.setBirthday(new SimpleDateFormat("yyyy-MM-dd").format(queryUser.getBirthday()));
        if (queryUser.getCommunityId() != null) {
            Community community = communityProvider.findCommunityById(queryUser.getCommunityId());
            if (community != null) {
                info.setRegionId(community.getCityId());
                Region region = regionProvider.findRegionById(community.getCityId());
                if (region != null) {
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
        if (avatarUri == null || avatarUri.trim().length() == 0) {
            avatarUri = getUserAvatarUriByGender(user.getId(), user.getNamespaceId(), user.getGender());
        }
        user.setAvatarUri(avatarUri);
        try {
            String url = contentServerService.parserUri(avatarUri, EntityType.USER.getCode(), user.getId());

            // 用户的头像设置为固定的地址    add by xq.tian  2017/04/19
            // String encode = encodeUrl("avatar/" + user.getId());
            // url = url.replaceAll("image/.+?\\?", String.format("%s%s%s", "image/", encode, "?"));

            user.setAvatarUrl(url);
        } catch (Exception e) {
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
        if (userGender == null) {
            userGender = UserGender.UNDISCLOSURED;
        }

        String avatarUri = null;
        switch (userGender) {
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

//		if(LOGGER.isDebugEnabled()) {
//			LOGGER.debug("Gen the default avatar for user by gender, userId=" + userId
//					+ ", namespaceId=" + namespaceId + ", gener=" + gener + ", avatarUri=" + avatarUri);
//		}

        return avatarUri;
    }

    private UserInfo getUserBasicInfo(Long uid, boolean hideMobile) {
        assert (uid != null);
        User user = UserContext.current().getUser();
        if (user != null && user.getId() != null && user.getId().longValue() == uid.longValue()) {
            return getUserInfo();
        }

        User queryUser = userProvider.findUserById(uid);
        if (queryUser == null) {
            return null;
        }

        return getUserBasicInfoByQueryUser(queryUser, hideMobile);
    }

    @Override
    public UserInfo getUserBasicByUuid(String uuid) {
        User queryUser = userProvider.findUserByUuid(uuid);
        if (queryUser == null) {
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

        if (response != null) {

            response.getRequests().forEach(r -> {
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

        if (response != null) {
            for (FamilyMemberFullDTO member : familyResults) {
                if ((nickName == null || "".equals(nickName) || nickName == member.getMemberNickName() || nickName.equals(member.getMemberNickName())) &&
                        (cellPhone == null || "".equals(cellPhone) || cellPhone == member.getCellPhone() || cellPhone.equals(member.getCellPhone()))) {
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
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getAnchor() == null ? 0L : cmd.getAnchor());
        if (cmd.getPageSize() == null) {
            int value = configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
            cmd.setPageSize(value);
        }
        List<InvitatedUsers> result = new ArrayList<InvitatedUsers>();
        List<InvitationRoster> invitationRoster = userProvider.listInvitationRostor(locator, cmd.getPageSize(), null);

        invitationRoster.forEach(r -> {
            InvitatedUsers invitatedUsers = new InvitatedUsers();
            invitatedUsers.setUserNickName(r.getUserNickName());
            invitatedUsers.setInviteType(r.getInviteType());
            invitatedUsers.setRegisterTime(r.getRegTime());

            List<UserIdentifier> userIdentifiers = this.userProvider.listUserIdentifiersOfUser(r.getUid());
            if (userIdentifiers != null && userIdentifiers.size() != 0)
                invitatedUsers.setUserCellPhone(userIdentifiers.get(0).getIdentifierToken());

            List<UserIdentifier> inviterIdentifiers = this.userProvider.listUserIdentifiersOfUser(r.getInviterId());
            if (inviterIdentifiers != null && inviterIdentifiers.size() != 0)
                invitatedUsers.setInviterCellPhone(inviterIdentifiers.get(0).getIdentifierToken());
            User inviter = this.userProvider.findUserById(r.getInviterId());
            if (inviter != null)
                invitatedUsers.setInviter(inviter.getNickName());

            result.add(invitatedUsers);

        });
        ListInvitatedUserResponse response = new ListInvitatedUserResponse();
        response.setInvitatedUsers(result);
        if (result.size() < cmd.getPageSize()) {
            response.setNextPageAnchor(null);
        } else {
            response.setNextPageAnchor(locator.getAnchor());
        }
        return response;
    }

    @Override
    public ListInvitatedUserResponse searchInvitatedUser(
            SearchInvitatedUserCommand cmd) {

        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor() == null ? 0L : cmd.getPageAnchor());
        if (cmd.getPageSize() == null) {
            int value = configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
            cmd.setPageSize(value);
        }
        List<InvitatedUsers> result = new ArrayList<InvitatedUsers>();
        List<InvitationRoster> invitationRoster = userProvider.listInvitationRostor(locator, cmd.getPageSize(), null);
        String userPhone = cmd.getUserPhone();
        String inviterPhone = cmd.getInviterPhone();

        invitationRoster.forEach(r -> {
            InvitatedUsers invitatedUsers = new InvitatedUsers();

            List<UserIdentifier> userIdentifiers = this.userProvider.listUserIdentifiersOfUser(r.getUid());
            if (userIdentifiers != null && userIdentifiers.size() != 0)
                invitatedUsers.setUserCellPhone(userIdentifiers.get(0).getIdentifierToken());

            List<UserIdentifier> inviterIdentifiers = this.userProvider.listUserIdentifiersOfUser(r.getInviterId());
            if (inviterIdentifiers != null && inviterIdentifiers.size() != 0)
                invitatedUsers.setInviterCellPhone(inviterIdentifiers.get(0).getIdentifierToken());
            User inviter = this.userProvider.findUserById(r.getInviterId());
            if (inviter != null)
                invitatedUsers.setInviter(inviter.getNickName());

            if ((userPhone == null || "".equals(userPhone) || userPhone == invitatedUsers.getUserCellPhone() || userPhone.equals(invitatedUsers.getUserCellPhone()))
                    && (inviterPhone == null || "".equals(inviterPhone) || inviterPhone == invitatedUsers.getInviterCellPhone() || inviterPhone.equals(invitatedUsers.getInviterCellPhone()))) {
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
        if (result.size() < cmd.getPageSize()) {
            response.setNextPageAnchor(null);
        } else {
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
        Integer randomNum = (int) (Math.random() * 1000);
        App app = appProvider.findAppByKey(appKey);
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", userId + "");
        //map.put("name", name);
        map.put("appKey", appKey + "");
        map.put("timeStamp", timeStamp + "");
        map.put("randomNum", randomNum + "");
        long s = System.currentTimeMillis();
        String signature = SignatureHelper.computeSignature(map, app.getSecretKey());
        long e = System.currentTimeMillis();
        LOGGER.debug("getBizSignature-elapse2=" + (e - s));
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

        for (UserCurrentEntityType type : UserCurrentEntityType.values()) {
            UserProfile profile = userActivityProvider.findUserProfileBySpecialKey(userId, type.getUserProfileKey());
            if (profile != null) {
                UserCurrentEntity entity = new UserCurrentEntity();
                entity.setEntityType(type.getCode());
                entity.setEntityId(profile.getItemValue());
                entity.setTimestamp(profile.getIntegralTag1());
                Long namespaceId = profile.getIntegralTag2();
                if (namespaceId != null) {
                    entity.setNamespaceId((int) namespaceId.longValue());
                } else {
                    entity.setNamespaceId(Namespace.DEFAULT_NAMESPACE);
                }

                entityList.add(entity);

                String entityId = entity.getEntityId();
                if (entityId != null && entityId.length() > 0) {
                    try {
                        Long id = Long.parseLong(entityId);
                        switch (type) {
                            case COMMUNITY_COMMERCIAL:
                            case COMMUNITY_RESIDENTIAL:
                                Community community = communityProvider.findCommunityById(id);
                                if (community != null) {
                                    entity.setEntityName(community.getName());
                                } else {
                                    LOGGER.error("Community not found, userId=" + userId + ", communityId=" + id + ", type=" + type);
                                }
                                break;
                            case FAMILY:
                                FamilyDTO family = familyProvider.getFamilyById(id);
                                if (family != null) {
                                    entity.setEntityName(family.getName());
                                } else {
                                    LOGGER.error("Family not found, userId=" + userId + ", familyId=" + id + ", type=" + type);
                                }
                                break;
                            case ORGANIZATION:
                                Organization organization = organizationProvider.findOrganizationById(id);
                                if (organization != null) {
                                    entity.setEntityName(organization.getName());
                                    if (organization.getDirectlyEnterpriseId() != null && organization.getDirectlyEnterpriseId() != 0) {
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
                                if (enterprise != null) {
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
        if (user == null) {
            user = new User();
            user.setNickName(cmd.getUserName() == null ? cmd.getNamespaceUserToken() : cmd.getUserName());
            user.setStatus(UserStatus.ACTIVE.getCode());
            user.setPoints(0);
            user.setLevel((byte) 1);
            user.setGender((byte) 1);
            user.setNamespaceId(cmd.getNamespaceId());
            user.setNamespaceUserToken(cmd.getNamespaceUserToken());
            ;
            userProvider.createUser(user);
        }

        //UserLogin login = createLogin(Namespace.DEFAULT_NAMESPACE, user, cmd.getSiteUri());
        UserLogin login = createLogin(cmd.getNamespaceId(), user, cmd.getDeviceIdentifier() == null ? "" : cmd.getDeviceIdentifier(), cmd.getPusherIdentify());
        login.setStatus(UserLoginStatus.LOGGED_IN);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("synThridUser-UserLogin=" + StringHelper.toJsonString(login));
        }

        return login;
    }

    private User checkThirdUserIsExist(Integer namespaceId, String namespaceUserToken, boolean isThrowExcep) {
        User user = this.userProvider.findUserByNamespace(namespaceId, namespaceUserToken);
        if (user != null) {
            LOGGER.error("user is exist.could not add.id=" + user.getId() + ", namespaceId=" + namespaceId
                    + ", namespaceUserToken=" + namespaceUserToken);
            if (isThrowExcep) {
                throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                        "user is exist,could not add.");
            }
        }
        return user;
    }

    private void checkIsNull(SynThridUserCommand cmd) {
        if (cmd.getRandomNum() == null || cmd.getRandomNum().equals("")) {
            LOGGER.error("randomNum not be null");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "randomNum not be null");
        }
        if (cmd.getTimestamp() == null || cmd.getTimestamp().equals("")) {
            LOGGER.error("timestamp not be null");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "timestamp not be null");
        }
        if (cmd.getNamespaceUserToken() == null || cmd.getNamespaceUserToken().equals("")) {
            LOGGER.error("siteUserToken not be null");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "siteUserToken not be null");
        }
        if (cmd.getNamespaceId() == null) {
            LOGGER.error("Namespace is null, namespaceId=" + cmd.getNamespaceId());
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Namespace is null");
        }
    }

    @Override
    public GetSignatureCommandResponse getThirdSignature(GetBizSignatureCommand cmd) {
        if (cmd.getNamespaceId() == null) {
            LOGGER.error("Namespace is null, namespaceId=" + cmd.getNamespaceId());
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Namespace is null");
        }
        if (cmd.getNamespaceUserToken() == null || cmd.getNamespaceUserToken().equals("")) {
            LOGGER.error("Namespace user token is null, token=" + cmd.getNamespaceUserToken());
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "siteUserToken not be null");
        }

        User user = this.checkThirdUserIsExist(cmd.getNamespaceId(), cmd.getNamespaceUserToken(), true);
        return this.produceSignature(user);
    }

    @Override
    public UserInfo getUserInfoById(GetUserInfoByIdCommand cmd) {
        this.checkIsNull(cmd);
        this.checkSign(cmd);
        User queryUser = userProvider.findUserById(cmd.getId());
        if (queryUser == null) {
            return null;
        }
        return getUserBasicInfoByQueryUser(queryUser, false);
    }

    private void checkSign(GetUserInfoByIdCommand cmd) {
        //2016-07-29:modify by liujinwne,parameter name don't be signed.

        String appKey = cmd.getZlAppKey();
        App app = appProvider.findAppByKey(appKey);
        if (app == null) {
            LOGGER.error("app not found.appKey=" + appKey);
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "app not found.");
        }
        String signature = cmd.getZlSignature();
        Map<String, String> map = new HashMap<String, String>();
        map.put("appKey", appKey);
        map.put("id", cmd.getId() + "");
        //map.put("name", cmd.getName());
        map.put("randomNum", cmd.getRandomNum() + "");
        map.put("timeStamp", cmd.getTimeStamp() + "");
        String nsignature = SignatureHelper.computeSignature(map, app.getSecretKey());
        if (!nsignature.equals(signature)) {
            LOGGER.error("check signature fail.nsign=" + nsignature + ",sign=" + signature);
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "check signature fail.");
        }
    }

    private void checkIsNull(GetUserInfoByIdCommand cmd) {
        //2016-07-29:modify by liujinwne,parameter name don't be signed.
        if (StringUtils.isEmpty(cmd.getZlSignature()) || StringUtils.isEmpty(cmd.getZlAppKey())) {
            LOGGER.error("zlSignature or zlAppKey is null.");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "zlSignature or zlAppKey is null.");
        }
        if (cmd.getId() == null) {
            LOGGER.error("id is null.");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "id is null.");
        }
        if (cmd.getRandomNum() == null || cmd.getTimeStamp() == null) {
            LOGGER.error("randomNum or timeStamp is null.");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
        if (phoneNumber == null || phoneNumber.trim().length() == 0) {
            LOGGER.error("Phone number should not be empty, namespaceId=" + namespaceId + ", phoneNumber=" + phoneNumber);
            return;
        }
        namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        String value = configurationProvider.getValue(namespaceId, "sms.vcodetest.flag", "false");
        if ("true".equalsIgnoreCase(value)) {
            String verificationCode = RandomGenerator.getRandomDigitalString(6);
            sendVerificationCodeSms(namespaceId, this.getRegionPhoneNumber(phoneNumber, cmd.getRegionCode()), verificationCode);
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
        if (attachmet1 != null && attachmet1.trim().length() > 0) {
            attachmentList.add(attachmet1);
        }
        if (attachmet2 != null && attachmet2.trim().length() > 0) {
            attachmentList.add(attachmet2);
        }

        String handlerName = MailHandler.MAIL_RESOLVER_PREFIX + MailHandler.HANDLER_JSMTP;
        MailHandler handler = PlatformContext.getComponent(handlerName);

        handler.sendMail(user.getNamespaceId(), from, to, subject, body, attachmentList);
    }

    @Override
    public RichLinkDTO sendUserRichLinkMessage(SendUserTestRichLinkMessageCommand cmd) {
        RichLinkDTO linkDto = ConvertHelper.convert(cmd, RichLinkDTO.class);
        if (linkDto.getCoverUrl() == null && cmd.getCoverUri() != null) {
            String url = contentServerService.parserUri(cmd.getCoverUri(), EntityType.USER.getCode(), User.SYSTEM_UID);
            if (url != null) {
                linkDto.setCoverUrl(url);
            }
        }

        String targetPhone = cmd.getTargetPhone();
        if (targetPhone == null || targetPhone.trim().length() == 0) {
            LOGGER.error("User not found for the phone, cmd={}", cmd);
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "User not found for the phone");
        }

        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getTargetNamespaceId());
        UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, targetPhone);
        if (userIdentifier == null) {
            LOGGER.error("User not found for the phone(identifier), cmd={}", cmd);
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "User not found for the phone");
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
        if (meta == null) {
            meta = new HashMap<String, String>();
        }
        meta.put(MessageMetaConstant.POPUP_FLAG, String.valueOf(MessagePopupFlag.POPUP.getCode()));
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                targetUserId, messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());

        return linkDto;
    }

    @Override
    public UserLogin innerLogin(Integer namespaceId, Long userId, String deviceIdentifier, String pusherIdentify) {
        User user = userProvider.findUserById(userId);
        if (user == null) {
            LOGGER.error("user not found.userId=" + userId);
            return null;
        }
        UserLogin login = createLogin(namespaceId, user, deviceIdentifier, pusherIdentify);
        login.setStatus(UserLoginStatus.LOGGED_IN);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("innerLogin-UserLogin=" + StringHelper.toJsonString(login));
        }
        return login;
    }

    @Override
    public List<UserInfo> listUserByKeyword(String keyword) {
        List<UserInfo> userInfos = new ArrayList<UserInfo>();
        List<User> users = userProvider.listUserByNickNameOrIdentifier(keyword);
        if (users != null && !users.isEmpty()) {
            for (User user : users) {
                userInfos.add(getUserBasicInfoByQueryUser(user, false));
            }
        }
        return userInfos;
    }

    @Override
    public List<User> listUserByIdentifier(String identifier) {
        List<User> users = new ArrayList<User>();
        List<UserIdentifier> userIdentifiers = userProvider.listUserIdentifierByIdentifier(identifier);
        if (userIdentifiers != null && !userIdentifiers.isEmpty()) {
            for (UserIdentifier r : userIdentifiers) {
                User user = userProvider.findUserById(r.getOwnerUid());
                if (user != null)
                    users.add(user);
            }
        }
        return users;
    }

    @Override
    public List<UserInfo> listUserInfoByIdentifier(String identifier) {
        List<UserInfo> userInfos = new ArrayList<UserInfo>();
        List<User> users = listUserByIdentifier(identifier);
        if (users != null && !users.isEmpty()) {
            for (User r : users) {
                userInfos.add(getUserBasicInfoByQueryUser(r, false));
            }
        }
        return userInfos;
    }

    @Override
    public List<SceneDTO> listUserRelatedScenes() {
        return listUserRelatedScenes(null);
    }

    @Override
    public List<SceneDTO> listUserRelatedScenes(ListUserRelatedScenesCommand cmd) {
        Integer defaultFlag = SCENE_SWITCH_DEFAULT_FLAG_DISABLE;
        if (cmd != null) {
            defaultFlag = cmd.getDefaultFlag();
        }
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Long userId = UserContext.current().getUser().getId();

        List<SceneDTO> sceneList = new ArrayList<SceneDTO>();
        List<SceneDTO> residential_sceneList = new ArrayList<SceneDTO>();
        List<SceneDTO> commercial_sceneList = new ArrayList<SceneDTO>();


        // 处于小区场景
        // 列出用户有效家庭 mod by xiongying 20160523
        addFamilySceneToList(userId, namespaceId, residential_sceneList);
        sceneList.addAll(residential_sceneList);
        // 处于某个公司对应的场景
        addOrganizationSceneToList(userId, namespaceId, commercial_sceneList);
        sceneList.addAll(commercial_sceneList);


        /** 从配置项中查询是否开启 **/
        Integer switchFlag = this.configurationProvider.getIntValue(namespaceId, "scenes.switchKey", SCENE_SWITCH_DISABLE);
        LOGGER.debug("switchFlag is" + switchFlag);
        if (defaultFlag == SCENE_SWITCH_DEFAULT_FLAG_ENABLE && switchFlag == SCENE_SWITCH_ENABLE) {
            /** 查询默认场景 **/
            Community default_community_one = new Community();
            if (commercial_sceneList.size() == 0) {
                //如果园区场景为0，通过小区查询默认园区
                default_community_one = findDefaultCommunity(namespaceId, userId, residential_sceneList, CommunityType.COMMERCIAL.getCode());
                LOGGER.debug("If the park scene is 0, query the default park");
            }
//			else if (commercial_sceneList.size() == 1 && commercial_sceneList.get(0).getSceneType() == SceneType.PM_ADMIN.getCode()){
//				//如果园区场景有且只有一个，通过小区查询默认园区
//				default_community_one = findDefaultCommunity(namespaceId,userId,residential_sceneList,CommunityType.COMMERCIAL.getCode());
//				LOGGER.debug("如果园区场景有且只有一个，通过小区查询默认园区");
//			}

            if (default_community_one != null && default_community_one.getCommunityType() != null) {
                sceneList.add(convertCommunityToScene(namespaceId, userId, default_community_one));
            } else {
                LOGGER.debug("The default park scene was not found");
            }


            Community default_community_two = new Community();
            if (residential_sceneList.size() == 0) {
                //如果小区场景为0，通过园区查询默认小区
                default_community_two = findDefaultCommunity(namespaceId, userId, commercial_sceneList, CommunityType.RESIDENTIAL.getCode());
                LOGGER.debug("If the cell scene is 0, check the default cell through the park");
            }

            if (default_community_two != null && default_community_two.getCommunityType() != null) {
                sceneList.add(convertCommunityToScene(namespaceId, userId, default_community_two));
            } else {
                LOGGER.debug("The default scene was not found");
            }

            // set方法进入
            // 当用户既没有选择家庭、也没有在某个公司内时，他有可能选过某个小区/园区，此时也把对应域空间下所选的小区作为场景 by lqs 2010416
            if (sceneList.size() == 0) {
                SceneDTO communityScene = getCurrentCommunityScene(namespaceId, userId);
                if (communityScene != null) {
                    sceneList.add(communityScene);
                }
            }

		}else{
			// 当用户既没有选择家庭、也没有在某个公司内时，他有可能选过某个小区/园区，此时也把对应域空间下所选的小区作为场景 by lqs 2010416
			if(sceneList.size() == 0) {
				SceneDTO communityScene = getCurrentCommunityScene(namespaceId, userId);
				if(communityScene != null) {
					sceneList.add(communityScene);
				}
			}
		}
		//在返回前排个序,上一次改错位置了，这次注释掉
		//sortSceneDTO(sceneList);
		return sceneList;
	}

    private SceneDTO getCurrentCommunityScene(Integer namespaceId, Long userId) {
        SceneDTO communityScene = null;

        UserCurrentEntityType[] entityTypes = new UserCurrentEntityType[]{
                UserCurrentEntityType.COMMUNITY_RESIDENTIAL,
                UserCurrentEntityType.COMMUNITY_COMMERCIAL,
                UserCurrentEntityType.COMMUNITY};
        for (UserCurrentEntityType entityType : entityTypes) {
            UserProfile profile = userActivityProvider.findUserProfileBySpecialKey(userId, entityType.getUserProfileKey());
            if (profile != null && profile.getIntegralTag2() != null && profile.getIntegralTag2().intValue() == namespaceId.intValue()) {
                Long communityId = null;
                try {
                    communityId = Long.parseLong(profile.getItemValue());
                } catch (Exception e) {
                    LOGGER.error("Failed to parse community id in user profile, profile={}", profile, e);
                }

                if (communityId != null) {
                    Community community = communityProvider.findCommunityById(communityId);
                    if (community != null) {
                        CommunityDTO communityDTO = ConvertHelper.convert(community, CommunityDTO.class);

                        SceneType sceneType = DEFAULT;
                        CommunityType communityType = CommunityType.fromCode(community.getCommunityType());
                        if (communityType == CommunityType.COMMERCIAL) {
                            sceneType = PARK_TOURIST;
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
        if (familyDtoList != null && familyDtoList.size() > 0) {
            for (FamilyDTO familyDto : familyDtoList) {
                sceneDto = toFamilySceneDTO(namespaceId, userId, familyDto);
                if (sceneDto != null) {
                    sceneList.add(sceneDto);
                }
            }
        } else {
            if (LOGGER.isWarnEnabled()) {
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


        String fullName = familyDto.getName();
        String aliasName = familyDto.getName();
        StringBuffer titlieName = new StringBuffer();
        String communityName = "";
        if (familyDto.getCommunityAliasName() != null && !familyDto.getCommunityAliasName().equals("")) {
            communityName = familyDto.getCommunityAliasName();
        } else {
            communityName = familyDto.getCommunityName();
        }

        // 处理名称
        GetNamespaceDetailCommand cmd = new GetNamespaceDetailCommand();
        cmd.setNamespaceId(namespaceId);
        NamespaceDetailDTO namespaceDetail = this.namespaceResourceService.getNamespaceDetail(cmd);
        NamespaceNameType namespaceNameType = NamespaceNameType.fromCode(namespaceDetail.getNameType());
        switch (namespaceNameType) {
            case ONLY_COMPANY_NAME:
                titlieName.append(familyDto.getName());
                break;
            case ONLY_COMMUNITY_NAME:
                titlieName.append(communityName);
                break;
            case COMMUNITY_COMPANY_NAME:
                titlieName.append(communityName).append(familyDto.getName());
                break;
        }

//		if(!StringUtils.isEmpty(familyDto.getCityName())){
//			fullName.append(familyDto.getCityName());
//		}
//		if(!StringUtils.isEmpty(familyDto.getAreaName())){
//			fullName.append(familyDto.getAreaName());
//		}
//		if(!StringUtils.isEmpty(familyDto.getCommunityName())){
//			fullName.append(familyDto.getCommunityName());
//			aliasName.append(familyDto.getCommunityName());
//		}
//		if(!StringUtils.isEmpty(familyDto.getName())){
//			fullName.append(familyDto.getName());
//			aliasName.append(familyDto.getName());
//		}


        sceneDto.setTitleName(titlieName.toString());
        sceneDto.setName(fullName);
        sceneDto.setAliasName(aliasName);
        sceneDto.setAvatar(familyDto.getAvatarUri());
        sceneDto.setAvatarUrl(familyDto.getAvatarUrl());
        sceneDto.setCommunityName(communityName);

        String entityContent = StringHelper.toJsonString(familyDto);
        sceneDto.setEntityContent(entityContent);

        SceneTokenDTO sceneTokenDto = toSceneTokenDTO(namespaceId, userId, familyDto, SceneType.FAMILY);
        String sceneToken = WebTokenGenerator.getInstance().toWebToken(sceneTokenDto);
        sceneDto.setSceneToken(sceneToken);

        sceneDto.setCommunityType(CommunityType.RESIDENTIAL.getCode());
        sceneDto.setStatus(familyDto.getMembershipStatus());
        sceneDto.setCommunityId(familyDto.getCommunityId());
        if (familyDto.getCommunityId() != null) {
            Community community = this.communityProvider.findCommunityById(familyDto.getCommunityId());
            sceneDto.setCommunityName(communityName);
        }

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
        if (organizationDtoList != null && organizationDtoList.size() > 0) {
            for (OrganizationDTO orgDto : organizationDtoList) {
                sceneDto = toOrganizationSceneDTO(namespaceId, userId, orgDto, sceneType);
                if (sceneDto != null) {
                    sceneList.add(sceneDto);
                }
            }
        } else {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("No family is found for the scene, namespaceId=" + namespaceId + ", userId=" + userId);
            }
        }
    }

    /*public static void main(String[] args) {
        System.out.println(GeoHashUtils.encode(121.643166, 31.223298));
    }*/

    @Override
    public SceneDTO toOrganizationSceneDTO(Integer namespaceId, Long userId, OrganizationDTO organizationDto, SceneType sceneType) {
        SceneDTO sceneDto = new SceneDTO();

        // 增加场景类型到sceneDTO中，使得客户端不需要使用EntityType来作场景 by lqs 20160510
        sceneDto.setSceneType(sceneType.getCode());

        sceneDto.setEntityType(UserCurrentEntityType.ORGANIZATION.getCode());

        String fullName = organizationDto.getName();
        String aliasName = organizationDto.getName();
        StringBuffer titlieName = new StringBuffer();
        String communityName = "";
        if (organizationDto.getCommunityAliasName() != null && !organizationDto.getCommunityAliasName().equals("")) {
            communityName = organizationDto.getCommunityAliasName();
        } else {
            communityName = organizationDto.getCommunityName();
        }

        //加上province
        Region city = regionProvider.findRegionById(organizationDto.getCityId());
        if (city != null) {
            Region province = regionProvider.findRegionById(city.getParentId());
            if (province != null) {
                organizationDto.setProvinceId(province.getId());
                organizationDto.setProvinceName(province.getName());
            }
        }

        String organizaitonName = "";
        if (organizationDto.getDisplayName() != null && !organizationDto.getDisplayName().equals("")) {
            organizaitonName = organizationDto.getDisplayName();
        } else {
            organizaitonName = organizationDto.getName();
        }
        // 处理名称
        GetNamespaceDetailCommand cmd = new GetNamespaceDetailCommand();
        cmd.setNamespaceId(namespaceId);
        NamespaceDetailDTO namespaceDetail = this.namespaceResourceService.getNamespaceDetail(cmd);
        NamespaceNameType namespaceNameType = NamespaceNameType.fromCode(namespaceDetail.getNameType());
        switch (namespaceNameType) {
            case ONLY_COMPANY_NAME:
                titlieName.append(organizaitonName);
                break;
            case ONLY_COMMUNITY_NAME:
                titlieName.append(communityName);
                break;
            case COMMUNITY_COMPANY_NAME:
                titlieName.append(communityName).append(organizaitonName);
                break;
        }
//		sceneDto.setName(organizationDto.getName().trim());
        // 在园区先暂时优先显示园区名称，后面再考虑怎样显示公司名称 by lqs 20160514
//		String aliasName = organizationDto.getDisplayName();
        //if(sceneType.getCode().contains("park") && organizationDto.getCommunityName() != null) {
        //    aliasName = organizationDto.getCommunityName();
        //}
        // 在园区通用版与左邻小区版合并后，只要不是物业公司，则优先显示小区/园区名称 by lqs 20160517
        // 不管什么公司都要显示本公司的简称 by sfyan 20170606
//		String orgType = organizationDto.getOrganizationType();
//		if(!OrganizationType.isGovAgencyOrganization(orgType)) {
//			aliasName = organizationDto.getCommunityName();
//		}
//        if (aliasName == null || aliasName.trim().isEmpty()) {
//            aliasName = organizationDto.getName().trim();
//        }
        sceneDto.setTitleName(titlieName.toString());
        sceneDto.setName(fullName);
        sceneDto.setAliasName(organizaitonName);
        sceneDto.setAvatar(organizationDto.getAvatarUri());
        sceneDto.setAvatarUrl(organizationDto.getAvatarUrl());
        sceneDto.setCommunityName(communityName);

        String entityContent = StringHelper.toJsonString(organizationDto);
        sceneDto.setEntityContent(entityContent);

        SceneTokenDTO sceneTokenDto = toSceneTokenDTO(namespaceId, userId, organizationDto, sceneType);
        String sceneToken = WebTokenGenerator.getInstance().toWebToken(sceneTokenDto);
        sceneDto.setSceneToken(sceneToken);

        sceneDto.setCommunityType(CommunityType.COMMERCIAL.getCode());

        List<OrganizationMember> members = this.organizationProvider.findOrganizationMembersByOrgIdAndUId(userId, organizationDto.getId());
        if (members != null && members.size() > 0) {
            sceneDto.setStatus(members.get(0).getStatus());
        } else {
            LOGGER.debug("This OrganizationMember is trouble");
        }
        OrganizationCommunityRequest organizationCommunityRequest = this.organizationProvider.getOrganizationCommunityRequestByOrganizationId(organizationDto.getId());
        if (organizationCommunityRequest != null) {
            sceneDto.setCommunityId(organizationCommunityRequest.getCommunityId());
            Community community = this.communityProvider.findCommunityById(organizationCommunityRequest.getCommunityId());
            sceneDto.setCommunityName(communityName);
        }

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

        if (!StringUtils.isEmpty(cmd.getSceneToken())) {
            checkSceneToken(userId, cmd.getSceneToken());
        }

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

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Parse scene token, userId={}, sceneToken={}", userId, sceneTokenDto);
            }
        } catch (Exception e) {
            LOGGER.error("Invalid scene token, userId=" + userId + ", sceneToken=" + sceneToken, e);
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_SCENE_TOKEN,
                    "Invalid scene token");
        }

        if (sceneTokenDto == null) {
            LOGGER.error("Scene token is null, userId=" + userId + ", sceneToken=" + sceneToken);
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_SCENE_TOKEN,
                    "Invalid scene token");
        }

        SceneType sceneType = SceneType.fromCode(sceneTokenDto.getScene());
        if (sceneType == null) {
            LOGGER.error("Scene type is null, userId=" + userId + ", sceneToken=" + sceneToken + ", sceneTokenDto=" + sceneTokenDto);
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_SCENE_TOKEN,
                    "Invalid scene token");
        }

        UserCurrentEntityType userEntityType = UserCurrentEntityType.fromCode(sceneTokenDto.getEntityType());
        if (userEntityType == null) {
            LOGGER.error("User entity type is null, userId=" + userId + ", sceneToken=" + sceneToken + ", sceneTokenDto=" + sceneTokenDto);
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_SCENE_TOKEN,
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
        if (communityId == null) {
            LOGGER.error("Community id may not be null, userId={}, namespaceId={}, cmd={}", userId, namespaceId, cmd);
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Community id may not be null");
        }

        Community community = communityProvider.findCommunityById(communityId);
        if (community == null) {
            LOGGER.error("Community not found, userId={}, namespaceId={}, cmd={}", userId, namespaceId, cmd);
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
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
        String fullName = community.getName();
        String aliasName = community.getAliasName();
        String communityName = "";
        if (community.getAliasName() != null && !community.getAliasName().equals("")) {
            communityName = community.getAliasName();
        } else {
            communityName = community.getName();
        }

        // 处理名称
        GetNamespaceDetailCommand cmd = new GetNamespaceDetailCommand();
        cmd.setNamespaceId(namespaceId);
        NamespaceDetailDTO namespaceDetail = this.namespaceResourceService.getNamespaceDetail(cmd);
        NamespaceNameType namespaceNameType = NamespaceNameType.fromCode(namespaceDetail.getNameType());
//
//		if(!StringUtils.isEmpty(community.getCityName())){
//			fullName.append(community.getCityName());
//		}
//		if(!StringUtils.isEmpty(community.getAreaName())){
//			fullName.append(community.getAreaName());
//		}
//		if(!StringUtils.isEmpty(community.getName())){
//			fullName.append(community.getName());
//			aliasName.append(community.getName());
//		}

        SceneDTO sceneDto = new SceneDTO();
        sceneDto.setTitleName(communityName);
        sceneDto.setSceneType(sceneType.getCode());
        sceneDto.setEntityType(UserCurrentEntityType.COMMUNITY.getCode());
        sceneDto.setName(fullName);
        sceneDto.setAliasName(communityName);

        String entityContent = StringHelper.toJsonString(community);
        sceneDto.setEntityContent(entityContent);

        SceneTokenDTO sceneTokenDto = toSceneTokenDTO(namespaceId, userId, community, sceneType);
        String sceneToken = WebTokenGenerator.getInstance().toWebToken(sceneTokenDto);
        sceneDto.setSceneToken(sceneToken);
        sceneDto.setCommunityType(community.getCommunityType());
        sceneDto.setCommunityId(community.getId());
        sceneDto.setCommunityName(community.getName());

        return sceneDto;
    }

    /**
     * 判断是否是小区版场景（相对于园区版）
     *
     * @param userId      用户ID
     * @param namespaceId 用户所在的域空间ID
     * @return 如果是小区
     */
    private boolean isCommunityScene(Long userId, Integer namespaceId) {
        if (namespaceId == null) {
            return false;
        } else {
            NamespaceDetail namespaceDetail = namespaceResourceProvider.findNamespaceDetailByNamespaceId(namespaceId);
            if (namespaceDetail != null) {
                NamespaceCommunityType communityType = NamespaceCommunityType.fromCode(namespaceDetail.getResourceType());
                if (communityType != null) {
                    switch (communityType) {
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
            if (cmd.getPhone() != null && cmd.getPhone().length() < 9) {
                Long id = Long.valueOf(cmd.getPhone());
                user = this.userProvider.findUserById(id);
            }
        } catch (Exception ex) {
            LOGGER.info("try userId not found", ex);
        }

        if (user == null) {
            user = this.findUserByIndentifier(cmd.getNamespaceId(), cmd.getPhone());
        }

        if (user == null) {
            //throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_CLASS_NOT_FOUND, "User not found!");
            return new UserLoginResponse();
        }

        Map<String, Long> deviceMap = new HashMap<String, Long>();
        List<UserLogin> logins = this.listUserLogins(user.getId());
        List<UserLoginDTO> dtos = logins.stream().map((r) -> {
            return r.toDto();
        }).collect(Collectors.toList());

        for (UserLoginDTO dto : dtos) {
            if (dto.getDeviceIdentifier() != null && !dto.getDeviceIdentifier().isEmpty()) {
                Device device = deviceProvider.findDeviceByDeviceId(dto.getDeviceIdentifier());

                if (device != null) {
                    deviceMap.put(device.getDeviceId(), 0l);
                    dto.setDeviceType(device.getPlatform());
                }
            }

            dto.setLastPushPing(0l);
            if (dto.getDeviceType() == null) {
                dto.setDeviceType("other");
            }

            if (dto.getLoginBorderId() != null) {
                dto.setIsOnline((byte) 1);
                BorderConnection conn = borderConnectionProvider.getBorderConnection(dto.getLoginBorderId());
                if (conn != null) {
                    dto.setBorderStatus(conn.getConnectionState());
                }
            } else {
                dto.setIsOnline((byte) 0);
            }
        }

        deviceMap = pusherService.requestDevices(deviceMap);
        for (UserLoginDTO dto : dtos) {
            if (dto.getDeviceIdentifier() != null && !dto.getDeviceIdentifier().isEmpty()) {
                Long last = deviceMap.get(dto.getDeviceIdentifier());
                if (last != null) {
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
        if (namespaceId == null) {
            namespaceId = 0;
        }
        msg.setNamespaceId(namespaceId);

        Map<String, String> meta = new HashMap<String, String>();
        meta.put("bodyType", "TEXT");
        msg.setMeta(meta);

        List<UserLogin> logins = this.listUserLogins(cmd.getUserId());
        for (UserLogin login : logins) {
            if (cmd.getLoginId().equals(login.getLoginId())) {
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
        for (Border border : borders) {
            strs.add(String.format("%s:%d", border.getPublicAddress(), border.getPublicPort()));
        }

        resp.setBorders(strs);

        return resp;
    }

    @Override
    public UserImpersonationDTO createUserImpersonation(CreateUserImpersonationCommand cmd) {
        User owner = this.findUserByIndentifier(cmd.getNamespaceId(), cmd.getOwnerPhone());
        User target = this.findUserByIndentifier(cmd.getNamespaceId(), cmd.getTargetPhone());
        if (owner == null || target == null) {
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
        for (UserImperInfo info : impers) {
            if (info.getOwnerId() != null && info.getTargetId() != null) {
                UserInfo u1 = this.getUserBasicInfo(info.getOwnerId(), false);
                UserInfo u2 = this.getUserBasicInfo(info.getTargetId(), false);
                if (u1 != null && u2 != null) {
                    if (u1.getId().equals(info.getId())) {
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

        if (StringUtils.isEmpty(cmd.getContentType())) {
            cmd.setContentType(SearchContentType.ALL.getCode());
        }
        SearchContentType contentType = SearchContentType.fromCode(cmd.getContentType());

        SearchContentsBySceneReponse response = new SearchContentsBySceneReponse();
        switch (contentType) {
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
                int pageSize = (int) configProvider.getIntValue("search.content.size", 3);
                cmd.setPageSize(pageSize);

                List<ContentBriefDTO> dtos = new ArrayList<ContentBriefDTO>();
                List<LaunchPadItemDTO> itemDtos = new ArrayList<LaunchPadItemDTO>();
                List<ShopDTO> shopDtos = new ArrayList<ShopDTO>();
                response.setDtos(dtos);
                response.setLaunchPadItemDtos(itemDtos);
                response.setShopDTOs(shopDtos);

                //活动
                SearchTypes searchType = getSearchTypes(namespaceId, SearchContentType.ACTIVITY.getCode());
                if (searchType != null) {
                    SearchContentsBySceneReponse res = forumService.searchContents(cmd, SearchContentType.ACTIVITY);
                    if (res != null && res.getDtos() != null) {
                        response.getDtos().addAll(res.getDtos());
                    }
                }

                //投票
                searchType = getSearchTypes(namespaceId, SearchContentType.POLL.getCode());
                if (searchType != null) {
                    SearchContentsBySceneReponse res = forumService.searchContents(cmd, SearchContentType.POLL);
                    if (res != null && res != null) {
                        response.getDtos().addAll(res.getDtos());
                    }
                }

                //话题
                searchType = getSearchTypes(namespaceId, SearchContentType.TOPIC.getCode());
                if (searchType != null) {
                    SearchContentsBySceneReponse res = forumService.searchContents(cmd, SearchContentType.TOPIC);
                    if (res != null
                            && res.getDtos() != null) {
                        response.getDtos().addAll(res.getDtos());
                    }
                }

                //新闻
                searchType = getSearchTypes(namespaceId, SearchContentType.NEWS.getCode());
                if (searchType != null) {
                    SearchContentsBySceneReponse res = newsService.searchNewsByScene(cmd);
                    if (res != null && res.getDtos() != null) {
                        response.getDtos().addAll(res.getDtos());
                    }
                }

                //查询应用 add by yanjun 20170419
                searchType = getSearchTypes(namespaceId, SearchContentType.LAUNCHPADITEM.getCode());
                if (searchType != null) {
                    SearchContentsBySceneReponse tempResp = launchPadService.searchLaunchPadItemByScene(cmd);
                    if (tempResp != null && tempResp.getLaunchPadItemDtos() != null) {
                        response.getLaunchPadItemDtos().addAll(tempResp.getLaunchPadItemDtos());
                    }
                }

                //查询电商店铺 add by yanjun 20170419
                searchType = getSearchTypes(namespaceId, SearchContentType.SHOP.getCode());
                if (searchType != null) {
                    SearchContentsBySceneReponse tempResp = businessService.searchShops(cmd);
                    if (tempResp != null
                            && tempResp.getShopDTOs() != null) {
                        response.getShopDTOs().addAll(tempResp.getShopDTOs());
                    }
                }

                break;

            default:
                LOGGER.error("Unsupported content type for search, contentType=" + cmd.getContentType());
                break;
        }

        if (LOGGER.isDebugEnabled()) {
            long endTime = System.currentTimeMillis();
            LOGGER.debug("search contents by scene, userId={}, namespaceId={}, elapse={}, cmd={}",
                    userId, namespaceId, (endTime - startTime), cmd);
        }
        return response;
    }

    @Override
    public SearchTypes getSearchTypes(Integer namespaceId, String searchContentType) {
        SearchTypes searchType = userActivityProvider.findByContentAndNamespaceId(namespaceId, searchContentType);
        //找不到就找0域空间的
        if (searchType == null) {
            searchType = userActivityProvider.findByContentAndNamespaceId(0, searchContentType);
        }

        return searchType;
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
        if (searchTypes == null || searchTypes.size() == 0) {
            searchTypes = userActivityProvider.listByNamespaceId(0);
        }

        if (searchTypes != null && searchTypes.size() > 0) {
            response.getSearchTypes().addAll(searchTypes.stream().map(r -> {
                SearchTypeDTO dto = ConvertHelper.convert(r, SearchTypeDTO.class);
                return dto;
            }).collect(Collectors.toList()));
        }
        return response;
    }

    // 移自WebRequestInterceptor并改为public方法，使得其它地方也可以调用 by lqs 20160922
    public boolean isValid(LoginToken token) {
        if (token == null) {
            User user = UserContext.current().getUser();
            Long userId = -1L;
            if (user != null) {
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
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            String value = StringUtils.join(entry.getValue(), ",");
            if (LOGGER.isTraceEnabled())
                LOGGER.trace("HttpRequest param " + entry.getKey() + ": " + value);
            if (entry.getKey().equals("token"))
                loginTokenString = value;
        }

        if (loginTokenString == null) {
            if (request.getCookies() != null) {
                List<Cookie> matchedCookies = new ArrayList<>();

                for (Cookie cookie : request.getCookies()) {
                    if (LOGGER.isTraceEnabled())
                        LOGGER.trace("HttpRequest cookie " + cookie.getName() + ": " + cookie.getValue() + ", path: " + cookie.getPath());

                    if (cookie.getName().equals("token")) {
                        matchedCookies.add(cookie);
                    }
                }

                if (matchedCookies.size() > 0)
                    loginTokenString = matchedCookies.get(matchedCookies.size() - 1).getValue();
            }
        }

        if (loginTokenString != null)
            try {
                return WebTokenGenerator.getInstance().fromWebToken(loginTokenString, LoginToken.class);
            } catch (Exception e) {
                LOGGER.error("Invalid login token.tokenString={}", loginTokenString, e);
                return null;
            }

        return null;
    }

    public UserLogin logonBythirdPartUser(Integer namespaceId, String userType, String userToken, HttpServletRequest request, HttpServletResponse response) {
        List<User> userList = this.userProvider.findThirdparkUserByTokenAndType(namespaceId, userType, userToken);
        if (userList == null || userList.size() == 0) {
            LOGGER.error("Unable to find the thridpark user, namespaceId={}, userType={}, userToken={}", namespaceId, userType, userToken);
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_USER_NOT_EXIST, "User does not exist");
        }

        User user = userList.get(0);
        if (UserStatus.fromCode(user.getStatus()) != UserStatus.ACTIVE) {
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_ACCOUNT_NOT_ACTIVATED,
                    "User account has not been activated yet");
        }

        UserLogin login = createLogin(namespaceId, user, null, null);
        login.setStatus(UserLoginStatus.LOGGED_IN);

        //added by Janson, mark as disconnected
        unregisterLoginConnection(login);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("User logon succeed, namespaceId={}, userType={}, userToken={}, userLogin={}", namespaceId, userType, userToken, login);
        }

        LoginToken token = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), login.getImpersonationId());
        String tokenString = WebTokenGenerator.getInstance().toWebToken(token);

        LOGGER.debug(String.format("Return login info. token: %s, login info: ", tokenString, StringHelper.toJsonString(login)));

        //微信公众号的accessToken过期时间是7200秒，需要设置cookie小于7200。
        //防止用户在coreserver处于登录状态而accessToken已过期，重新登录之后会刷新accessToken   add by yanjun 20170906
        WebRequestInterceptor.setCookieInResponse("token", tokenString, request, response, 7000);

        WebRequestInterceptor.setCookieInResponse("namespace_id", String.valueOf(namespaceId), request, response);
        return login;
    }

    @Override
    public UserLogin logonBythirdPartAppUser(Integer namespaceId, String userType, String userToken, HttpServletRequest request, HttpServletResponse response) {
        List<User> userList = this.userProvider.findThirdparkUserByTokenAndType(namespaceId, userType, userToken);
        if(userList == null || userList.size() == 0) {
            LOGGER.error("Unable to find the thridpark user, namespaceId={}, userType={}, userToken={}", namespaceId, userType, userToken);
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_USER_NOT_EXIST, "User does not exist");
        }

        User user = userList.get(0);
        if(UserStatus.fromCode(user.getStatus()) != UserStatus.ACTIVE) {
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_ACCOUNT_NOT_ACTIVATED,
                    "User account has not been activated yet");
        }

        UserLogin login = createLogin(namespaceId, user, request.getParameter("deviceIdentifier"), request.getParameter("pusherIdentify"));
        login.setStatus(UserLoginStatus.LOGGED_IN);

        //added by Janson, mark as disconnected
        unregisterLoginConnection(login);

        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("User logon succeed, namespaceId={}, userType={}, userToken={}, userLogin={}", namespaceId, userType, userToken, login);
        }

        LoginToken token = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), login.getImpersonationId());
        String tokenString = WebTokenGenerator.getInstance().toWebToken(token);

        LOGGER.debug(String.format("Return login info. token: %s, login info: ", tokenString, StringHelper.toJsonString(login)));

        //微信公众号的accessToken过期时间是7200秒，需要设置cookie小于7200。
        //防止用户在coreserver处于登录状态而accessToken已过期，重新登录之后会刷新accessToken   add by yanjun 20170906
        WebRequestInterceptor.setCookieInResponse("token", tokenString, request, response, 7000);

        WebRequestInterceptor.setCookieInResponse("namespace_id", String.valueOf(namespaceId), request, response);
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
        if (userList == null || userList.size() == 0) {

            //将微信头像下载下来
            CsFileLocationDTO fileLocationDTO = contentServerService.uploadFileByUrl("avatar.jpg", user.getAvatar());
            if (fileLocationDTO != null && fileLocationDTO.getUri() != null) {
                user.setAvatar(fileLocationDTO.getUri());
            }

            userProvider.createUser(user);

            //设定默认园区  add by  yanjun 20170915
            setDefaultCommunity(user.getId(), namespaceId);

            return true;
        } else {
            LOGGER.warn("User already existed, namespaceId={}, userType={}, userToken={}", namespaceId, namespaceUserType, namespaceUserToken);
            return false;
        }
    }

    @Override
    public User signupByThirdparkUserByApp(User user, HttpServletRequest request) {
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

            //将微信头像下载下来
            CsFileLocationDTO fileLocationDTO = contentServerService.uploadFileByUrl("avatar.jpg", user.getAvatar());
            if(fileLocationDTO != null && fileLocationDTO.getUri() != null){
                user.setAvatar(fileLocationDTO.getUri());
            }

            userProvider.createUser(user);

            //设定默认园区  add by  yanjun 20170915
            setDefaultCommunity(user.getId(), namespaceId);

            return user;
        } else {
            LOGGER.warn("User already existed, namespaceId={}, userType={}, userToken={}", namespaceId, namespaceUserType, namespaceUserToken);
            return userList.get(0);
        }
    }

    @Override
    public Boolean validateUserPass(ValidatePassCommand cmd) {
        if (cmd.getUserId() == null) {
            LOGGER.error("userId is null");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "userId is null");
        }
        if (StringUtils.isEmpty(cmd.getPassword())) {
            LOGGER.error("password is null");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "password is null");
        }
        User user = userProvider.findUserById(cmd.getUserId());
        if (user == null) {
            LOGGER.error("user not found.userId=" + cmd.getUserId());
            throw errorWith(ErrorCodes.SCOPE_GENERAL, UserServiceErrorCode.ERROR_USER_NOT_EXIST,
                    "user not found");
        }
        if (!EncryptionUtils.validateHashPassword(cmd.getPassword(), user.getSalt(), user.getPasswordHash())) {
            return false;
        }
        return true;
    }

    @Override
    public List<SceneDTO> listTouristRelatedScenes() {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Long userId = UserContext.current().getUser().getId();
        // 修改成按照defalutOrder排序 by lei.lv 20170915
        List<NamespaceResource> resources = namespaceResourceProvider.listResourceByNamespaceOrderByDefaultOrder(namespaceId, NamespaceResourceType.COMMUNITY);
        List<SceneDTO> sceneList = new ArrayList<SceneDTO>();
        for (NamespaceResource resource : resources) {
            Community community = communityProvider.findCommunityById(resource.getResourceId());
            if (null != community) {
                CommunityDTO communityDTO = ConvertHelper.convert(community, CommunityDTO.class);
                SceneType sceneType = DEFAULT;
                if (CommunityType.fromCode(community.getCommunityType()) == CommunityType.COMMERCIAL) {
                    sceneType = PARK_TOURIST;
                }
                SceneDTO sceneDTO = this.toCommunitySceneDTO(namespaceId, userId, communityDTO, sceneType);
                sceneList.add(sceneDTO);
            }
        }
        sceneList.stream().filter(r -> {
            return r.getSceneToken() != null;
        }).collect(Collectors.toList());
        return sceneList;
    }

    @Override
    public List<SceneDTO> listAllCommunityScenes() {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Long userId = UserContext.current().getUser().getId();
        // 修改成按照defalutOrder排序 by lei.lv 20170915
        List<NamespaceResource> resources = namespaceResourceProvider.listResourceByNamespace(namespaceId, NamespaceResourceType.COMMUNITY);
        List<SceneDTO> sceneList = new ArrayList<SceneDTO>();
        for (NamespaceResource resource : resources) {
            Community community = communityProvider.findCommunityById(resource.getResourceId());
            if (null != community) {
                CommunityDTO communityDTO = ConvertHelper.convert(community, CommunityDTO.class);
                SceneType sceneType = DEFAULT;
                if (CommunityType.fromCode(community.getCommunityType()) == CommunityType.COMMERCIAL) {
                    sceneType = PARK_TOURIST;
                }
                SceneDTO sceneDTO = this.toCommunitySceneDTO(namespaceId, userId, communityDTO, sceneType);
                sceneList.add(sceneDTO);
            }
        }
        sceneList.stream().filter(r -> {
            return r.getSceneToken() != null;
        }).collect(Collectors.toList());
        //设置排序字段
        sceneList = handleSortName(sceneList);
        return sceneList;
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    public boolean isLogon() {
        //added by janson 2017-03-29
//		UserLogin userLogin = UserContext.current().getLogin();
//		LoginToken token = new LoginToken(userLogin.getUserId(), userLogin.getLoginId(), userLogin.getLoginInstanceNumber(), userLogin.getImpersonationId());
//		if(kickoffService.isKickoff(UserContext.getCurrentNamespaceId(), token)) {
//			kickoffService.remoteKickoffTag(UserContext.getCurrentNamespaceId(), token);
//         throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
//                UserServiceErrorCode.ERROR_KICKOFF_BY_OTHER, "Kickoff by others");
//		}

        User user = UserContext.current().getUser();
        if (null == user) {
            return false;
        }

        LOGGER.debug("Check for login. userId = {}", user.getId());
        if (user.getId() > 0) {
            return true;
        }
        return false;
    }


    public void checkUserScene(SceneType sceneType) {
        // 判断是否是登录 by sfyan 20161009
        if (!this.isLogon()) {
            // 没登录 检查场景是否是游客
            if (sceneType == SceneType.FAMILY || sceneType == SceneType.PM_ADMIN || sceneType == SceneType.ENTERPRISE || sceneType == SceneType.ENTERPRISE_NOAUTH) {
                LOGGER.error("Not logged in.Cannot access this scene. sceneType = {}", sceneType.getCode());
                throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_UNAUTHENTITICATION,
                        "Not logged in.Cannot access this scene");
            }
        }
    }

    @Override
    public UserLogin reSynThridUser(InitBizInfoCommand cmd) {
        validateInitBizInfoCommand(cmd);

        User user = createUserIFNoExist(cmd);
        if (StringUtils.isNotBlank(cmd.getMark())) {
            createUserIdentifierIfNoExist(user, cmd.getMark());
        }

        UserLogin login = createLogin(cmd.getNamespaceId(), user, cmd.getDeviceIdentifier() == null ? "" : cmd.getDeviceIdentifier(), null);
        login.setStatus(UserLoginStatus.LOGGED_IN);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("reSynThridUser-UserLogin=" + StringHelper.toJsonString(login));
        }
        return login;
    }

    private UserIdentifier createUserIdentifierIfNoExist(User user, String identifierToken) {
        UserIdentifier identifier = this.userProvider.findClaimedIdentifierByToken(
                user.getNamespaceId(),
                identifierToken);

        if (identifier != null
                && identifier.getOwnerUid().longValue() != user.getId().longValue()) {
            LOGGER.error("user identifier ownerId not equal to userId.namespaceId=" + user.getNamespaceId()
                    + ",identifier=" + identifierToken
                    + ",ownerId=" + identifier.getOwnerUid()
                    + ",userId=" + user.getId());
        }

        if (identifier == null) {
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
        if (StringUtils.isEmpty(cmd.getLabel())) {
            LOGGER.error("label is null.");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "label is null.");
        }
        if (cmd.getNamespaceId() == null) {
            LOGGER.error("namespaceId is null.");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "namespaceId is null.");
        }
    }

    private User createUserIFNoExist(InitBizInfoCommand cmd) {
        User user = checkThirdUserIsExist(cmd.getNamespaceId(), cmd.getLabel(), false);
        if (user == null) {
            user = new User();
            user.setNickName(cmd.getDetail() == null ? "" : cmd.getDetail());
            user.setStatus(UserStatus.ACTIVE.getCode());
            user.setPoints(0);
            user.setLevel((byte) 1);
            user.setGender((byte) 1);
            user.setNamespaceId(cmd.getNamespaceId());
            user.setNamespaceUserToken(cmd.getLabel());
            ;
            user.setAvatar(cmd.getDescription());
            userProvider.createUser(user);
        } else {
            user.setNickName(cmd.getDetail() == null ? "" : cmd.getDetail());
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
        response.setSubNonce((int) (Math.random() * 1000));
        response.setSubTimestamp(System.currentTimeMillis());

        //identifier token
        List<UserIdentifier> identifiers = this.userProvider.listUserIdentifiersOfUser(user.getId());
        if (identifiers != null && !identifiers.isEmpty()) {
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

                    //群聊名称为空时填充群聊别名  edit by yanjun 20170724
                    if (name == null || "".equals(name)) {
                        String alias = groupService.getGroupAlias(group.getId());
                        dto.setAlias(alias);
                        String defaultName = localeStringService.getLocalizedString(GroupLocalStringCode.SCOPE, String.valueOf(GroupLocalStringCode.GROUP_DEFAULT_NAME), UserContext.current().getUser().getLocale(), "");
                        dto.setName(defaultName);
                        dto.setIsNameEmptyBefore(GroupNameEmptyFlag.EMPTY.getCode());
                    }

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

        final Long halfAnHour = 30 * 60 * 1000L;
        final Long currUserId = currUser.getId();
        final Integer newRegionCode = cmd.getRegionCode();
        final String newIdentifier = cmd.getIdentifier();
        final String oldIdentifier = userIdentifier.getIdentifierToken();
        final Integer oldRegionCode = userIdentifier.getRegionCode();
        final Integer namespaceId = UserContext.getCurrentNamespaceId();
        final String verificationCode = RandomGenerator.getRandomDigitalString(6);

        // 给原来手机号发送短信验证码
        if (newIdentifier == null) {
            // this.verifySmsTimes("resetIdentifier", oldIdentifier, request.getHeader(X_EVERHOMES_DEVICE));

            UserIdentifierLog log = userIdentifierLogProvider.findByUserIdAndIdentifier(currUserId, oldRegionCode, oldIdentifier);
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
                log.setRegionCode(oldRegionCode);
                log.setNamespaceId(namespaceId);
                log.setIdentifierToken(oldIdentifier);
                log.setOwnerUid(currUserId);
                userIdentifierLogProvider.createUserIdentifierLog(log);
            }
            this.sendVerificationCodeSms(namespaceId, getRegionPhoneNumber(oldIdentifier, oldRegionCode), verificationCode);
        }
        // 给新手机号发送短信验证码
        else {
            // this.verifySmsTimes("resetIdentifier", newIdentifier, request.getHeader(X_EVERHOMES_DEVICE));

            UserIdentifierLog log = userIdentifierLogProvider.findByUserId(currUserId);
            // 如果半个小时没有完成整个过程，需要从头开始执行整个流程
            if (log != null && log.notExpire(halfAnHour)
                    && (log.getClaimStatus() == IdentifierClaimStatus.VERIFYING.getCode()
                    || log.getClaimStatus() == IdentifierClaimStatus.CLAIMED.getCode())) {
                log.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
                log.setVerificationCode(verificationCode);
                log.setIdentifierToken(newIdentifier);
                log.setRegionCode(newRegionCode);
                log.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                userIdentifierLogProvider.updateUserIdentifierLog(log);
            } else {
                LOGGER.error("it is not atomic to reset newIdentifier, userId = {}, newIdentifier={}",
                        currUser.getId(), newIdentifier);
                throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_PLEASE_TRY_AGAIN_TO_FIRST_STEP,
                        "please try again to the first step");
            }

            this.sendVerificationCodeSms(namespaceId, getRegionPhoneNumber(newIdentifier, newRegionCode), verificationCode);
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
                        throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_VERIFICATION_CODE_INCORRECT_OR_EXPIRED,
                                "verification code incorrect or expired %s", cmd.getVerificationCode());
                    }
                    break;
                case CLAIMED:
                    // 检查新手机号是否已经是注册用户
                    UserIdentifier newIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, log.getIdentifierToken());
                    if (newIdentifier != null) {
                        LOGGER.error("the new identifier are already exist {}", log.getIdentifierToken());
                        throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_NEW_IDENTIFIER_USER_EXIST,
                                "the new identifier are already exist %s", log.getIdentifierToken());
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
                        throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_VERIFICATION_CODE_INCORRECT_OR_EXPIRED,
                                "verification code incorrect or expired %s", cmd.getVerificationCode());
                    }
                    break;
                default:
                    LOGGER.error("it is not atomic to reset newIdentifier, userId = {}, newIdentifier={}",
                            currUser.getId(), log.getIdentifierToken());
                    throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_PLEASE_TRY_AGAIN_TO_FIRST_STEP,
                            "please try again to the first step");
            }
        } else {
            LOGGER.error("it is not atomic to reset newIdentifier, userId = {}, log = {}",
                    currUser.getId(), log);
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_PLEASE_TRY_AGAIN_TO_FIRST_STEP,
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

        String home = configProvider.getValue(ConfigConstants.HOME_URL, "");

        // ------------------------------------------------------------------------
        String wjl = "jinlan.wang@zuolin.com";
        String xqt = "xq.tian@zuolin.com";
        String handlerName = MailHandler.MAIL_RESOLVER_PREFIX + MailHandler.HANDLER_JSMTP;
        MailHandler handler = PlatformContext.getComponent(handlerName);
        // String account = configurationProvider.getValue(UserContext.getCurrentNamespaceId(), "mail.smtp.account", "zuolin@zuolin.com");

        String body = String.format("User \"%s(%s)\" has send a appeal, server is \"%s\", please check out. \n[%s]",
                cmd.getName(), cmd.getOldIdentifier(), home, log.toString());
        handler.sendMail(UserContext.getCurrentNamespaceId(), null, wjl, "User Appeal", body);
        handler.sendMail(UserContext.getCurrentNamespaceId(), null, xqt, "User Appeal", body);
        // ------------------------------------------------------------------------

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
                    // 如果是通过，则重置用户手机号
                    if (status == UserAppealLogStatus.ACTIVE) {
                        Integer namespaceId = UserContext.getCurrentNamespaceId();
                        // 检查申诉新手机号是否已经是注册用户
                        UserIdentifier newIdentifier = userProvider.findClaimedIdentifierByToken(log.getNamespaceId(), log.getNewIdentifier());
                        if (newIdentifier != null) {
                            LOGGER.error("the new identifier are already exist {}", log.getNewIdentifier());
                            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_NEW_IDENTIFIER_USER_EXIST,
                                    "the new identifier are already exist %s", log.getNewIdentifier());
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
                    log.setStatus(status.getCode());
                    userAppealLogProvider.updateUserAppealLog(log);
                } else {
                    return null;
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
        throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_UPDATE_USER_APPEAL_LOG,
                "update user appeal log failed");
    }

    @Override
    public UserIdentifierLogDTO listResetIdentifierCode(ListResetIdentifierCodeCommand cmd) {
        UserIdentifierLog log = userIdentifierLogProvider.findByIdentifier(cmd.getIdentifier());
        if (log != null) {
            return toUserIdentifierLogDTO(log);
        }
        return new UserIdentifierLogDTO();
    }

    private UserIdentifierLogDTO toUserIdentifierLogDTO(UserIdentifierLog log) {
        return ConvertHelper.convert(log, UserIdentifierLogDTO.class);
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
                smsProvider.sendSms(log.getNamespaceId(), getRegionPhoneNumber(log.getNewIdentifier(), log.getNewRegionCode()), templateScope, templateId, locale, variables);
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
        Map<String, String> params = new HashMap<String, String>();
        params.put("label", response.getLabel());
        params.put("namespaceId", response.getNamespaceId() + "");
        params.put("subNonce", response.getSubNonce() + "");
        params.put("subTimestamp", response.getSubTimestamp() + "");
        params.put("subKey", response.getSubKey());
        if (StringUtils.isNotBlank(response.getDetail())) {
            params.put("detail", response.getDetail());
        }
        if (StringUtils.isNotBlank(response.getMark())) {
            params.put("mark", response.getMark());
        }
        if (StringUtils.isNotBlank(response.getDescription())) {
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

    private String getRegionPhoneNumber(String identifierToken, Integer regionCode) {
        //国内电话不要拼区号，发送短信走国内通道，便宜
        if (null == regionCode || 86 == regionCode) {
            return identifierToken;
        }
        return "00" + regionCode + identifierToken;
    }

    @Override
    public ListRegisterUsersResponse searchUserByNamespace(SearchUserByNamespaceCommand cmd) {
        ListRegisterUsersResponse resp = new ListRegisterUsersResponse();
        if (cmd.getNamespaceId() == null) {
            cmd.setNamespaceId(0);
        }

        int count = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getAnchor());
        List<User> users = this.userProvider.listUserByNamespace(cmd.getKeyword(), cmd.getNamespaceId(), locator, count);
        resp.setNextPageAnchor(locator.getAnchor());
        resp.setValues(new ArrayList<UserInfo>());
        for (User u : users) {
            UserInfo ui = getUserBasicInfoByQueryUser(u, false);
            if (ui != null) {
                resp.getValues().add(ui);
            }
        }

        return resp;
    }

    @Override
    public ListAuthFormsResponse listAuthForms() {
        int namespaceId = UserContext.getCurrentNamespaceId();
        List<AuthorizationThirdPartyForm> list = authorizationThirdPartyFormProvider.listFormSourceByOwner(EntityType.NAMESPACE.getCode(), Long.valueOf(namespaceId));
        ListAuthFormsResponse response = new ListAuthFormsResponse();
        response.setSourceDto(list.stream().map(r -> ConvertHelper.convert(r, FormSourceDTO.class)).collect(Collectors.toList()));
        return response;
    }

    @Override
    public SearchUsersResponse searchUsers(SearchUsersCommand cmd) {
        SearchUsersResponse resp = new SearchUsersResponse();
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());

        int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<User> users = this.userProvider.listUserByNamespace(cmd.getKeywords(), namespaceId, locator, pageSize);
        resp.setNextPageAnchor(locator.getAnchor());
        resp.setDtos(new ArrayList<>());
        for (User u : users) {
            UserDTO dto = ConvertHelper.convert(u, UserDTO.class);
            List<OrganizationMember> members = organizationProvider.listOrganizationMembersByUId(u.getId());
            dto.setName(dto.getNickName());
            if (members.size() > 0)
                dto.setName(members.get(0).getContactName());
            resp.getDtos().add(dto);
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


    @Override
    public SceneContactV2DTO getRelevantContactInfo(GetRelevantContactInfoCommand cmd) {
		/*
		1.传参无detailId：则获取当前用户信息
		2.传参有detailId：则获取指定用户的详细信息
		 */
        if (org.springframework.util.StringUtils.isEmpty(cmd.getDetailId())) {
            //	情况1
            return getCurrentUserRealInfo(cmd.getOrganizationId());
        } else {
            //  情况2
            return getTheUserRealInfo(cmd.getDetailId());
        }
    }

    /* 当前用户 */
    private SceneContactV2DTO getCurrentUserRealInfo(Long organizationId) {
        Long userId = UserContext.currentUserId();
        SceneContactV2DTO dto = new SceneContactV2DTO();
        OrganizationMember member = organizationProvider.findOrganizationMemberByUIdAndOrgId(userId, organizationId);
        if (member == null)
            return dto;
        OrganizationMemberDetails memberArchive = this.organizationProvider.findOrganizationMemberDetailsByDetailId(member.getDetailId());
        if (memberArchive == null)
            return dto;
        dto.setUserId(userId);
        dto.setContactName(memberArchive.getContactName());
        dto.setContactToken(memberArchive.getContactToken());
        return dto;
    }

    /* 指定用户 */
    private SceneContactV2DTO getTheUserRealInfo(Long detailId) {
        SceneContactV2DTO dto = new SceneContactV2DTO();
        OrganizationMemberDetails memberArchive = this.organizationProvider.findOrganizationMemberDetailsByDetailId(detailId);
        if (memberArchive == null)
            return dto;

        //  1.设置基本信息
        dto.setOrganizationId(memberArchive.getOrganizationId());
        dto.setUserId(memberArchive.getTargetId());
        dto.setTargetType(memberArchive.getTargetType());
        dto.setDetailId(memberArchive.getId());
        dto.setContactName(memberArchive.getContactName());
        dto.setContactToken(memberArchive.getContactToken());
        if (!StringUtils.isEmpty(memberArchive.getEnName()))
            dto.setContactEnglishName(memberArchive.getEnName());
        dto.setGender(memberArchive.getGender());
        if (!StringUtils.isEmpty(memberArchive.getEmail()))
            dto.setEmail(memberArchive.getEmail());
        if (!StringUtils.isEmpty(memberArchive.getWorkEmail()))
            dto.setWorkEmail(memberArchive.getWorkEmail());
        dto.setRegionCode(memberArchive.getRegionCode());
        if (!StringUtils.isEmpty(memberArchive.getContactShortToken()))
            dto.setContactShortToken(memberArchive.getContactShortToken());

        //  2.设置公司名称
        Organization topEnterprise = this.organizationProvider.findOrganizationById(dto.getOrganizationId());
        OrganizationDetail topEnterpriseDetail = this.organizationProvider.findOrganizationDetailByOrganizationId(dto.getOrganizationId());
        if (topEnterpriseDetail != null)
            dto.setEnterpriseName(topEnterpriseDetail.getDisplayName());
        else
            dto.setEnterpriseName(topEnterprise.getName());

        //	3.设置部门
        /*
        List<OrganizationDTO> departments = getContactDepartments(topEnterprise.getPath(), groupTypes, dto.getContactToken());
        //	对于 belongTo 的数据再做处理, add by lei.lv 2017/11/30
        if (departments == null || departments.size() < 1) {
            groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
            departments = getContactDepartments(topEnterprise.getPath(), groupTypes, dto.getContactToken());
        }
        dto.setDepartments(departments);
        */
        dto.setDepartments(getContactDepartment(dto.getDetailId()));

        //  4.设置岗位名称
        dto.setJobPosition(getContactJobPosition(dto.getDetailId()));

        //  5.设置头像(由于迁移数据detail表中可能没有头像信息，故由原始的组织架构接口获得)
        if (OrganizationMemberTargetType.USER.getCode().equals(dto.getTargetType())) {
            User user = userProvider.findUserById(dto.getUserId());
            if (null != user) {
                dto.setContactAvatar(contentServerService.parserUri(user.getAvatar(), EntityType.USER.getCode(), user.getId()));
            }
        }

        //	6.隐私保护电话号码处理
        OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndToken(dto.getContactToken(), dto.getOrganizationId());
        dto.setContactToken(null);
        if (member == null)
            return dto;
        dto.setVisibleFlag(member.getVisibleFlag());
        if (rolePrivilegeService.checkIsSystemOrAppAdmin(dto.getOrganizationId(), UserContext.currentUserId()) || VisibleFlag.fromCode(dto.getVisibleFlag()) == VisibleFlag.SHOW)
            dto.setContactToken(memberArchive.getContactToken());

        //  7.管理员校验
        if (rolePrivilegeService.checkIsSystemOrAppAdmin(dto.getOrganizationId(), dto.getUserId()))
            dto.setAdminFlag(TrueOrFalseFlag.TRUE.getCode());
        else
            dto.setAdminFlag(TrueOrFalseFlag.FALSE.getCode());
        return dto;
    }

    /* 查询部门的方法 */
    private List<OrganizationDTO> getContactDepartment(Long detailId) {
        OrganizationMember member = organizationProvider.findMemberDepartmentByDetailId(detailId);
        if(member == null)
            return null;
        Organization dep = organizationProvider.findOrganizationById(member.getOrganizationId());
        if(dep == null)
            return null;

        Organization parentDep = organizationProvider.findOrganizationById(dep.getParentId());
        OrganizationDTO dto = new OrganizationDTO();
        if(OrganizationGroupType.fromCode(member.getGroupType()) == OrganizationGroupType.DIRECT_UNDER_ENTERPRISE){
            dto.setId(parentDep.getId());
            dto.setParentId(parentDep.getId());
            dto.setName(parentDep.getName());
            dto.setParentName(parentDep.getName());
            dto.setPath(parentDep.getPath());
        }else{
            dto.setId(dep.getId());
            dto.setParentId(dep.getParentId());
            dto.setPath(dep.getPath());
            dto.setName(dep.getName());
            dto.setParentName(parentDep.getName());
        }
        return Collections.singletonList(dto);
    }

    private List<OrganizationDTO> getContactJobPosition(Long detailId) {
        List<OrganizationMember> members = organizationProvider.findMemberJobPositionByDetailId(detailId);
        if (members == null || members.size() == 0)
            return null;
        return members.stream().map(r -> {
            OrganizationDTO dto = new OrganizationDTO();
            Organization org = organizationProvider.findOrganizationById(r.getOrganizationId());
            if (org != null) {
                dto.setId(org.getId());
                dto.setName(org.getName());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    private List<OrganizationDTO> getContactDepartments(String path, List<String> groupTypes, String contactToken) {
        List<OrganizationMember> members = organizationProvider.listOrganizationMemberByPath(path, groupTypes, contactToken);
        List<OrganizationDTO> departments = new ArrayList<>();
        if (members != null && members.size() > 0) {
            for (OrganizationMember member : members) {
                Organization group = organizationProvider.findOrganizationById(member.getOrganizationId());
                if (null != group && OrganizationStatus.fromCode(group.getStatus()) == OrganizationStatus.ACTIVE) {
                    departments.add(ConvertHelper.convert(group, OrganizationDTO.class));
                }
            }
            //  设置父部门名称
            if (departments != null && departments.size() > 0) {
                for (OrganizationDTO department : departments) {
                    if (department.getParentId().equals(0L)) {
                        department.setParentName(department.getName());
                        continue;
                    }
                    department.setParentName(this.organizationProvider.findOrganizationById(department.getParentId()).getName());
                }
            }
        }
        return departments;
    }

    @Override
    public SceneContactV2DTO getContactInfoByUserId(GetContactInfoByUserIdCommand cmd) {
        // 1.通过 userId 与 organizationId 去找到 detailId
        // 2.根据 detailId 调用之前的获取信息接口
        List<OrganizationMember> members = this.organizationProvider.findOrganizationMembersByOrgIdAndUId(cmd.getUserId(), cmd.getOrganizationId());
        GetRelevantContactInfoCommand command = new GetRelevantContactInfoCommand();
        command.setDetailId(members.get(0).getDetailId());
        command.setOrganizationId(cmd.getOrganizationId());
        SceneContactV2DTO dto = this.getRelevantContactInfo(command);
        if (dto != null)
            return dto;
        else
            return null;
    }

    @Override
    public GetFamilyButtonStatusResponse getFamilyButtonStatus() {
        int namespaceId = UserContext.getCurrentNamespaceId();
        AuthorizationThirdPartyButton buttonstatus = authorizationThirdPartyButtonProvider.getButtonStatusByOwner(EntityType.NAMESPACE.getCode(), Long.valueOf(namespaceId));
        GetFamilyButtonStatusResponse response = ConvertHelper.convert(buttonstatus, GetFamilyButtonStatusResponse.class);
        if (response == null) {
            String document = localeStringService.getLocalizedString(AuthorizationErrorCode.SCOPE,
                    AuthorizationErrorCode.FAMILY_DETAIL, UserContext.current().getUser().getLocale(), AuthorizationErrorCode.FAMILY_DETAIL_S);
            String[] documents = document.split("\\|");
            response = new GetFamilyButtonStatusResponse(documents[0], FamilyButtonStatusType.SHOW.getCode(), FamilyButtonStatusType.SHOW.getCode(), FamilyButtonStatusType.SHOW.getCode(), FamilyButtonStatusType.SHOW.getCode(), documents[1], documents[2]);
        }
        return response;
    }

    @Override
    public List<String[]> listBuildingAndApartmentById(Long uid) {
        List<String[]> list = new ArrayList<>();
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhGroupMemberLogs t1 = Tables.EH_GROUP_MEMBER_LOGS.as("t1");
        EhAddresses t2 = Tables.EH_ADDRESSES.as("t2");
//        EhCommunities t3 = Tables.EH_COMMUNITIES.as("t3");
//        context.select(t2.BUILDING_NAME,t2.APARTMENT_NAME,t3.NAME)
        context.select(t2.BUILDING_NAME, t2.APARTMENT_NAME)
//				.from(t1,t2,t3)
                .from(t1, t2)
                .where(t1.MEMBER_ID.eq(uid))
                .and(t1.ADDRESS_ID.eq(t2.ID))
//                .and(t2.COMMUNITY_ID.eq(t3.ID))
                .fetch(r -> {
//                    String[] v = new String[3];
                    String[] v = new String[2];
//                    v[0] = r.getValue(t3.NAME);
                    v[0] = r.getValue(t2.BUILDING_NAME);
                    v[1] = r.getValue(t2.APARTMENT_NAME);
                    list.add(v);
                    return null;
                });
        return list;
    }

    @Override
    public TargetDTO findTargetByNameAndAddress(String contractNum, String targetName, Long communityId, String tel, String ownerType, String targetType, Integer namespaceId) {
        TargetDTO dto = new TargetDTO();
        if (contractNum != null) {
            if (namespaceId == null) {
                namespaceId = UserContext.getCurrentNamespaceId();
            }
            String handler = configurationProvider.getValue(namespaceId, "contractService", "");
            ContractService contractService = PlatformContext.getComponent(ContractService.CONTRACT_PREFIX + handler);
            List<Object> typeIdNameAndTel = contractService.findCustomerByContractNum(contractNum, communityId, ownerType);
            if (typeIdNameAndTel != null && typeIdNameAndTel.size() > 0) {
                dto.setTargetType((String) typeIdNameAndTel.get(0));
                dto.setTargetId((Long) typeIdNameAndTel.get(1));
                dto.setTargetName((String) typeIdNameAndTel.get(2));
                dto.setUserIdentifier((String) typeIdNameAndTel.get(3));
                dto.setContractId((Long) typeIdNameAndTel.get(4));
            }
        }
        if (dto.getTargetId() == null) {
            if (targetType != null && targetType.equals(AssetPaymentStrings.EH_USER)) {
                dto = userProvider.findUserByToken(tel, namespaceId);
                return dto;
            }
            if (targetType != null && targetType.equals(AssetPaymentStrings.EH_ORGANIZATION)) {
                Organization org = organizationProvider.findOrganizationByName(targetName, namespaceId);
                if (org != null) {
                    dto.setTargetName(org.getName());
                    dto.setTargetType(AssetPaymentStrings.EH_ORGANIZATION);
                    dto.setTargetId(org.getId());
                    //查企业的管理员
                    ListServiceModuleAdministratorsCommand cmd1 = new ListServiceModuleAdministratorsCommand();
                    cmd1.setOrganizationId(org.getId());
                    cmd1.setActivationFlag((byte) 1);
                    cmd1.setOwnerType("EhOrganizations");
                    cmd1.setOwnerId(null);
                    List<OrganizationContactDTO> organizationContactDTOS = rolePrivilegeService
                            .listOrganizationAdministrators(cmd1);
                    if (organizationContactDTOS != null && organizationContactDTOS.size() > 0) {
                        Long contactId = organizationContactDTOS.get(0).getTargetId();
                        String enterpriseAdminTel = userProvider.findMobileByUid(contactId);
                        dto.setUserIdentifier(enterpriseAdminTel);
                    }
                }
                return dto;
            }
        } else {
            return dto;
        }
        //确定客户的优先度， 查到合同算查到人，楼栋门牌只是为了填写账单的地址用
//            List<AddressIdAndName> addressByPossibleName = addressService.findAddressByPossibleName(UserContext.getCurrentNamespaceId(), communityId, buildingName, apartmentName);
//            List<Long> ids = new ArrayList<>();
//            for (int i = 0; i < addressByPossibleName.size(); i++){
//                ids.add(addressByPossibleName.get(i).getAddressId());
//            }
        //想在eh_user中找
//            List<TargetDTO> users = userProvider.findUesrIdByNameAndAddressId(targetName,ids,tel);
//            //再在eh_organization中找
//            List<TargetDTO> organizations = organizationProvider.findOrganizationIdByNameAndAddressId(targetName,ids);


        return null;
    }


    @Override
    public Long getCommunityIdBySceneToken(SceneTokenDTO sceneTokenDTO) {

        SceneType sceneType = SceneType.fromCode(sceneTokenDTO.getScene());

        Long communityId = null;

        switch (sceneType) {
            case DEFAULT:
            case PARK_TOURIST:
                communityId = sceneTokenDTO.getEntityId();

                break;
            case FAMILY:
                FamilyDTO family = familyProvider.getFamilyById(sceneTokenDTO.getEntityId());
                Community community = null;
                if (family != null) {
                    community = communityProvider.findCommunityById(family.getCommunityId());
                } else {
                    if (LOGGER.isWarnEnabled()) {
                        LOGGER.warn("Family not found, sceneToken=" + sceneTokenDTO);
                    }
                }
                if (community != null) {
                    communityId = community.getId();
                }

                break;
            case PM_ADMIN:// 无小区ID
            case ENTERPRISE: // 增加两场景，与园区企业保持一致
            case ENTERPRISE_NOAUTH: // 增加两场景，与园区企业保持一致
                OrganizationCommunityRequest organizationCommunityRequest = organizationProvider.
                        getOrganizationCommunityRequestByOrganizationId(sceneTokenDTO.getEntityId());
                if (null != organizationCommunityRequest) {
                    communityId = organizationCommunityRequest.getCommunityId();
                }
                break;
            default:
                LOGGER.error("Unsupported scene for simple user, sceneToken=" + sceneTokenDTO);
                break;
        }

        return communityId;
    }

    private Long getOrgIdBySceneToken(SceneTokenDTO sceneTokenDTO) {
        SceneType sceneType = SceneType.fromCode(sceneTokenDTO.getScene());

        switch (sceneType) {
            case PM_ADMIN:// 无小区ID
            case ENTERPRISE: // 增加两场景，与园区企业保持一致
            case ENTERPRISE_NOAUTH: // 增加两场景，与园区企业保持一致
                return sceneTokenDTO.getEntityId();
            default:
                break;
        }

        return null;
    }


    public List<SceneDTO> listUserRelatedScenesByCurrentType(ListUserRelatedScenesByCurrentTypeCommand cmd) {
        if (cmd.getSceneType() == null) {
            LOGGER.error("Invalid listUserRelatedScenesByCurrentType, cmd=" + cmd);
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid listUserRelatedScenesByCurrentType type");
        }
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Long userId = UserContext.current().getUser().getId();

        List<SceneDTO> sceneList = new ArrayList<SceneDTO>();

        if (DEFAULT == SceneType.fromCode(cmd.getSceneType()) || FAMILY == SceneType.fromCode(cmd.getSceneType())) {
            // 当前是家庭场景，列出有效园区场景
            addOrganizationSceneToList(userId, namespaceId, sceneList);
            //addTouristSceneToList(userId, namespaceId, sceneList);
        } else if (PARK_TOURIST == SceneType.fromCode(cmd.getSceneType()) || ENTERPRISE == SceneType.fromCode(cmd.getSceneType()) || ENTERPRISE_NOAUTH == SceneType.fromCode(cmd.getSceneType()) || PM_ADMIN == SceneType.fromCode(cmd.getSceneType())) {
            // 当前是园区场景，列出有效家庭场景
            addFamilySceneToList(userId, namespaceId, sceneList);
        }
        /** 从配置项中查询是否开启 **/
        Integer switchFlag = this.configurationProvider.getIntValue(namespaceId, "scenes.switchKey", SCENE_SWITCH_ENABLE);
        if (switchFlag == SCENE_SWITCH_ENABLE) {
            /** 查询默认场景 **/
            // 如果没有有效场景
            Community default_community = new Community();
            if (sceneList.size() == 0) {
                switch (SceneType.fromCode(cmd.getSceneType())) {
                    case DEFAULT:
                    case FAMILY:
                        default_community = findDefaultCommunity(namespaceId, userId, sceneList, CommunityType.COMMERCIAL.getCode());
                        break;
                    case PM_ADMIN:
                    case PARK_TOURIST:
                    case ENTERPRISE:
                    case ENTERPRISE_NOAUTH:
                        default_community = findDefaultCommunity(namespaceId, userId, sceneList, CommunityType.RESIDENTIAL.getCode());
                        break;
                }

                //把community转换成场景
                SceneType sceneType = DEFAULT;
                CommunityType communityType = CommunityType.fromCode(default_community.getCommunityType());
                if (communityType == CommunityType.COMMERCIAL) {
                    sceneType = PARK_TOURIST;
                }

                CommunityDTO default_communityDTO = ConvertHelper.convert(default_community, CommunityDTO.class);
                SceneDTO default_communityScene = toCommunitySceneDTO(namespaceId, userId, default_communityDTO, sceneType);
                default_communityScene.setStatus(SCENE_EXAMPLE);
                sceneList.add(default_communityScene);
            }
        }
        if (sceneList == null && sceneList.size() == 0) {
            LOGGER.debug("There is no enable switch Scene");
            return null;
        }
        Collections.reverse(sceneList);
        return sceneList;
    }

    @Override
    public SceneDTO getProfileScene() {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Long userId = UserContext.current().getUser().getId();
        SceneDTO communityScene = getCurrentCommunityScene(namespaceId, userId);
        if (communityScene != null) {
            return communityScene;
        }
        return null;
    }

    @Override
    public List<SceneDTO> listUserRelateScenesByCommunityId(ListUserRelateScenesByCommunityId cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        Long userId = UserContext.current().getUser().getId();

        List<SceneDTO> sceneList = new ArrayList<SceneDTO>();
        // 列出用户有效家庭 mod by xiongying 20160523
        addFamilySceneToList(userId, namespaceId, sceneList);
        // 处于某个公司对应的场景
        addOrganizationSceneToList(userId, namespaceId, sceneList);

        //当关联场景不为空，且与参数中的园区id相匹配时，返回关联的场景
        if (sceneList.size() > 0) {
            List<SceneDTO> flist = sceneList.stream().filter(r -> {
                return r.getCommunityId().equals(cmd.getCommunityId());
            }).collect(Collectors.toList());
            if (flist.size() > 0) {
                return flist;
            }
        }
        //当关联场景为空，且没有与参数中的园区id相匹配时，返回参数用的社区场景
        sceneList.clear();
        Community community = this.communityProvider.findCommunityById(cmd.getCommunityId());
        sceneList.add(convertCommunityToScene(namespaceId, userId, community));
        //设置排序字段
        sceneList = handleSortName(sceneList);
        return sceneList;
    }

    @Override
    public List<SceneDTO> listAllCommunityScenesIfGeoExist(ListAllCommunityScenesIfGeoExistCommand cmd) {
        ListNearbyMixCommunitiesCommandV2Response resp = new ListNearbyMixCommunitiesCommandV2Response();

        int namespaceId = (UserContext.current().getNamespaceId() == null) ? Namespace.DEFAULT_NAMESPACE : UserContext.current().getNamespaceId();

        Long userId = UserContext.current().getUser().getId();

        if (cmd.getLatitude() != null && cmd.getLongitude() != null) {

            ListingLocator locator = new CrossShardListingLocator();
            int pageSize = PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize());
            ListNearbyMixCommunitiesCommand listNearbyMixCommunitiesCommand = new ListNearbyMixCommunitiesCommand();
            listNearbyMixCommunitiesCommand.setLatigtue(cmd.getLatitude());
            listNearbyMixCommunitiesCommand.setLongitude(cmd.getLongitude());
            listNearbyMixCommunitiesCommand.setPageSize(pageSize);
            listNearbyMixCommunitiesCommand.setPageAnchor(0L);

            List<Community> communities = this.addressService.listMixCommunitiesByDistanceWithNamespaceId(listNearbyMixCommunitiesCommand, locator, pageSize);
            //如果查询不出结果
            if (communities == null)
                return this.listTouristRelatedScenes();

            List<SceneDTO> sceneList = new ArrayList<SceneDTO>();

            communities.stream().map(r -> {
                CommunityDTO dto = ConvertHelper.convert(r, CommunityDTO.class);
                SceneType sceneType = DEFAULT;
                if (CommunityType.fromCode(dto.getCommunityType()) == CommunityType.COMMERCIAL) {
                    sceneType = PARK_TOURIST;
                }
                SceneDTO sceneDTO = this.toCommunitySceneDTO(namespaceId, userId, dto, sceneType);
                sceneList.add(sceneDTO);
                return null;
            }).collect(Collectors.toList());
            //返回前设置排序字段
            handleSortName(sceneList);
            return sceneList;
        } else {
        	//返回前设置排序字段
            return handleSortName(this.listTouristRelatedScenes());
        }
    }

    private List<SceneDTO> addFamilySceneToList(Long userId, Integer namespaceId, List<SceneDTO> sceneList) {
        List<FamilyDTO> familyList = this.familyProvider.getUserFamiliesByUserId(userId);
        toFamilySceneDTO(namespaceId, userId, sceneList, familyList);
        return sceneList;
    }

    private List<SceneDTO> addOrganizationSceneToList(Long userId, Integer namespaceId, List<SceneDTO> sceneList) {
        // 处于某个公司对应的场景
        OrganizationGroupType groupType = OrganizationGroupType.ENTERPRISE;

        //:todo 校验
        if (userId < User.MAX_SYSTEM_USER_ID) {
            LOGGER.error("userId is not legal! userId: {}", userId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "userId is not legal! userId: {}", userId);
        }

        List<OrganizationDTO> organizationList = organizationService.listUserRelateOrganizations(namespaceId, userId, groupType);
        for (OrganizationDTO orgDto : organizationList) {
            String orgType = orgDto.getOrganizationType();
            SceneType sceneType = SceneType.PM_ADMIN;
            if (!OrganizationType.isGovAgencyOrganization(orgType)) {
                if (OrganizationMemberStatus.fromCode(orgDto.getMemberStatus()) == OrganizationMemberStatus.ACTIVE) {
                    sceneType = SceneType.ENTERPRISE;
                } else {
                    sceneType = SceneType.ENTERPRISE_NOAUTH;
                }
            }
            SceneDTO sceneDto = toOrganizationSceneDTO(namespaceId, userId, orgDto, sceneType);
            if (sceneDto != null) {
                sceneList.add(sceneDto);
            }
        }
        return sceneList;
    }

    //添加非pm_admin場景的园区场景
    private List<SceneDTO> addTouristSceneToList(Long userId, Integer namespaceId, List<SceneDTO> sceneList) {
        // 处于某个公司对应的场景
        OrganizationGroupType groupType = OrganizationGroupType.ENTERPRISE;
        List<OrganizationDTO> organizationList = organizationService.listUserRelateOrganizations(namespaceId, userId, groupType);
        for (OrganizationDTO orgDto : organizationList) {
            String orgType = orgDto.getOrganizationType();
            if (!OrganizationType.isGovAgencyOrganization(orgType)) {
                SceneType sceneType;
                if (OrganizationMemberStatus.fromCode(orgDto.getMemberStatus()) == OrganizationMemberStatus.ACTIVE) {
                    sceneType = SceneType.ENTERPRISE;
                } else {
                    sceneType = SceneType.ENTERPRISE_NOAUTH;
                }
                SceneDTO sceneDto = toOrganizationSceneDTO(namespaceId, userId, orgDto, sceneType);
                if (sceneDto != null) {
                    sceneList.add(sceneDto);
                }
            }
        }
        return sceneList;
    }


    // 查询默认场景
    private Community findDefaultCommunity(Integer namespaceId, Long userId, List<SceneDTO> sceneList, Byte type) {

        SceneType shadowSceneType = DEFAULT;
        if (type == CommunityType.COMMERCIAL.getCode()) {
            shadowSceneType = PARK_TOURIST;
        } else {
            shadowSceneType = DEFAULT;
        }

        //先从默认关联表中查询
        Long defalut_communityId = null;
        Community defalut_community = null;
        for (SceneDTO scene : sceneList) {
            SceneTokenDTO sceneToken = this.checkSceneToken(userId, scene.getSceneToken());
            defalut_communityId = this.communityProvider.findDefaultCommunityByCommunityId(namespaceId, sceneToken.getEntityId());
            if (defalut_communityId != null) {
                break;
            }
        }

        if (defalut_communityId == null) {//找默认的社区
            SceneDTO communityScene = getCurrentCommunityScene(namespaceId, userId);
            if (communityScene != null && communityScene.getSceneType() == shadowSceneType.getCode()) {
//				defalut_community = ConvertHelper.convert(communityScene, Community.class);
                defalut_community = this.communityProvider.findCommunityById(communityScene.getCommunityId());
                LOGGER.debug("findDefaultCommunity, findUserProfileCommunity:" + StringHelper.toJsonString(defalut_community));
            } else {
                //再从namespace中取默认
                defalut_community = this.communityProvider.findFirstCommunityByNameSpaceIdAndType(namespaceId, type);
            }
        } else {
            defalut_community = this.communityProvider.findCommunityById(defalut_communityId);
        }

        return defalut_community;
    }

    @Override
    //把默认community转换成DTO
    public SceneDTO convertCommunityToScene(Integer namespaceId, Long userId, Community default_community) {
        //把community转换成场景
        SceneType sceneType = DEFAULT;
        CommunityType communityType = CommunityType.fromCode(default_community.getCommunityType());
        if (communityType == CommunityType.COMMERCIAL) {
            sceneType = PARK_TOURIST;
        }

        CommunityDTO default_communityDTO = ConvertHelper.convert(default_community, CommunityDTO.class);
        SceneDTO default_communityScene = toCommunitySceneDTO(namespaceId, userId, default_communityDTO, sceneType);
        default_communityScene.setStatus(SCENE_EXAMPLE);
        return default_communityScene;
    }

    @Override
    public UserIdentifier getUserIdentifier(Long userId) {
        if (userId == null) {
            return null;
        }
        return userProvider.findClaimedIdentifierByOwnerAndType(userId, IdentifierType.MOBILE.getCode());
    }

    @Override
    public VerificationCodeForBindPhoneResponse verificationCodeForBindPhone(VerificationCodeForBindPhoneCommand cmd) {

        verifySmsTimes("verificationCodeForWechat", cmd.getPhone(), "");
        VerificationCodeForBindPhoneResponse response = new VerificationCodeForBindPhoneResponse();
        User user = UserContext.current().getUser();
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        String verificationCode = RandomGenerator.getRandomDigitalString(6);
        List<Tuple<String, Object>> variables = smsProvider.toTupleList(SmsTemplateCode.KEY_VCODE, verificationCode);


        //如果这个微信已经绑定过手机，并且和新的手机号一致，直接报错提醒
        UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
        if (userIdentifier != null && userIdentifier.getIdentifierToken() != null && userIdentifier.getIdentifierToken().equals(cmd.getPhone())) {
            LOGGER.error("allready bindPhone, phone={}", userIdentifier.getIdentifierToken());
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_IDENTIFIER_ALREADY_CLAIMED, "allready bindPhone, phone=" + userIdentifier.getIdentifierToken());
        }


        //查看该手机是否已经注册
        userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getPhone());
        if (userIdentifier != null) {

            //手机注册过的或者发过验证码的，更新验证码
            response.setBindPhoneType(BindPhoneType.WECHATTOPHONE.getCode());
            userIdentifier.setVerificationCode(verificationCode);
            userIdentifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            userProvider.updateIdentifier(userIdentifier);
        } else {

            //查看该用户是否已绑定手机
            userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
            if (userIdentifier != null) {
                response.setBindPhoneType(BindPhoneType.ALREADYBIND.getCode());
                response.setOldPhone(userIdentifier.getIdentifierToken());

                UserIdentifierLog log = new UserIdentifierLog();
                log.setNamespaceId(namespaceId);
                log.setOwnerUid(user.getId());
                log.setIdentifierToken(cmd.getPhone());
                log.setVerificationCode(verificationCode);
                log.setRegionCode(cmd.getRegionCode());
                log.setClaimStatus(IdentifierClaimStatus.VERIFYING.getCode());
                userIdentifierLogProvider.createUserIdentifierLog(log);

            } else {
                response.setBindPhoneType(BindPhoneType.PHONETOWECHAT.getCode());
                //该用户是否已经发过短信
                userIdentifier = this.userProvider.findIdentifierByOwnerAndTypeAndClaimStatus(user.getId(), IdentifierType.MOBILE.getCode(), IdentifierClaimStatus.VERIFYING.getCode());
                if (userIdentifier == null) {
                    //用户没发送过验证码的，新建一条验证状态的userIdentifier
                    userIdentifier = new UserIdentifier();
                    userIdentifier.setOwnerUid(user.getId());
                    userIdentifier.setIdentifierType(IdentifierType.MOBILE.getCode());
                    userIdentifier.setIdentifierToken(cmd.getPhone());
                    userIdentifier.setNamespaceId(namespaceId);

                    userIdentifier.setClaimStatus(IdentifierClaimStatus.VERIFYING.getCode());
                    userIdentifier.setVerificationCode(verificationCode);
                    userIdentifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    userIdentifier.setRegionCode(cmd.getRegionCode());
                    userProvider.createIdentifier(userIdentifier);
                } else {
                    //该用户发送过验证，更新验证码和时间
                    userIdentifier.setVerificationCode(verificationCode);
                    userIdentifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    userProvider.updateIdentifier(userIdentifier);
                }
            }
        }
        smsProvider.sendSms(namespaceId, cmd.getPhone(), SmsTemplateCode.SCOPE, SmsTemplateCode.VERIFICATION_CODE, user.getLocale(), variables);

        return response;
    }

    @Override
	public void verificationCodeForBindPhoneByApp(VerificationCodeForBindPhoneCommand cmd) {

		verifySmsTimes("verificationCodeForWechat", cmd.getPhone(), "");
		User user = this.userProvider.findUserById(cmd.getUserId());
        if(user == null){
            LOGGER.error("user is null, userId={}", cmd.getUserId());
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_USER_NOT_EXIST, "user not exists, userId=" + cmd.getUserId());
        }
		Integer namespaceId = UserContext.getCurrentNamespaceId();

		String verificationCode = RandomGenerator.getRandomDigitalString(6);
		List<Tuple<String, Object>> variables = smsProvider.toTupleList(SmsTemplateCode.KEY_VCODE, verificationCode);


		//查看该手机是否已经注册
		UserIdentifier userIdentifier = userProvider.findClaimingIdentifierByToken(namespaceId, cmd.getPhone());
		if(userIdentifier != null){

			//手机注册过的或者发过验证码的，更新验证码
			userIdentifier.setVerificationCode(verificationCode);
			userIdentifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			userProvider.updateIdentifier(userIdentifier);
		}else {
			//用户没发送过验证码的，新建一条验证状态的userIdentifier
			userIdentifier = new UserIdentifier();
			userIdentifier.setOwnerUid(user.getId());
			userIdentifier.setIdentifierType(IdentifierType.MOBILE.getCode());
			userIdentifier.setIdentifierToken(cmd.getPhone());
			userIdentifier.setNamespaceId(namespaceId);

			userIdentifier.setClaimStatus(IdentifierClaimStatus.VERIFYING.getCode());
			userIdentifier.setVerificationCode(verificationCode);
			userIdentifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			userIdentifier.setRegionCode(cmd.getRegionCode());
			userProvider.createIdentifier(userIdentifier);
		}
		smsProvider.sendSms(namespaceId, cmd.getPhone(), SmsTemplateCode.SCOPE, SmsTemplateCode.VERIFICATION_CODE, user.getLocale(), variables);

	}

	@Override
    public UserLogin bindPhone(BindPhoneCommand cmd) {
        User user = UserContext.current().getUser();
        Integer namespaceId = UserContext.getCurrentNamespaceId();


        if (cmd.getPhone() == null) {
            LOGGER.error("phoneNumber param error");
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PHONE, "phone param error");
        }


        //查看该手机是否已经注册
        if (cmd.getBindPhoneType().byteValue() == BindPhoneType.WECHATTOPHONE.getCode()) {

            //使用传来的手机做查询校验，如果手机是否被篡改，查询和校验都不会通过的
            UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getPhone());

            //如果手机已经注册过，则校验验证码，更新微信信息(昵称、头像、性别)到该手机用户

            verificationCode(userIdentifier, cmd.getVerificationCode());

            User existUser = userProvider.findUserById(userIdentifier.getOwnerUid());
            existUser.setNickName(user.getNickName());
            existUser.setAvatar(user.getAvatar());
            existUser.setGender(user.getGender());
            existUser.setNamespaceUserToken(user.getNamespaceUserToken());
            existUser.setNamespaceUserType(user.getNamespaceUserType());

			userProvider.updateUser(existUser);
			//注销当前用户，
			//防止自己将自己绑定，被设置成无效
			if(user.getId() != existUser.getId()){
				user.setStatus(UserStatus.INACTIVE.getCode());
				user.setNamespaceUserToken("");
				user.setNamespaceUserType(null);
				LOGGER.info("user={}",user);userProvider.updateUser(user);
			}
			//add by huangliangming 20180731 使得微信注册时能够激活管理员
			// 刷新企业通讯录
            organizationService.processUserForMember(userIdentifier);
            //刷新地址信息
            propertyMgrService.processUserForOwner(userIdentifier);
            
            UserLogin oldLogin = UserContext.current().getLogin();
            if (oldLogin != null) {
                this.logoff(oldLogin);
            }

            UserLogin login = createLogin(namespaceId, existUser, null, null);
            login.setStatus(UserLoginStatus.LOGGED_IN);

            return login;
        } else if (cmd.getBindPhoneType().byteValue() == BindPhoneType.ALREADYBIND.getCode()) {
            UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());

//			verificationCode(userIdentifier, cmd.getVerificationCode());

            if (userIdentifier == null || userIdentifier.getIdentifierToken() != cmd.getOldPhone()) {
                LOGGER.error("old phone param error");
                throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PHONE, "old phone param error");
            }

            UserIdentifierLog log = userIdentifierLogProvider.findByUserIdAndIdentifier(user.getId(), cmd.getRegionCode(), cmd.getPhone());
            verificationCode(log, cmd.getVerificationCode());

            userIdentifier.setIdentifierToken(cmd.getPhone());
            userIdentifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            userProvider.updateIdentifier(userIdentifier);

          //add by huangliangming 20180731 使得微信注册时能够激活管理员
			// 刷新企业通讯录
            organizationService.processUserForMember(userIdentifier);
            //刷新地址信息
            propertyMgrService.processUserForOwner(userIdentifier);
            return null;

        } else {
            //校验新验证码，更新状态正式启用绑定该手机号码
            UserIdentifier userIdentifier = this.userProvider.findIdentifierByOwnerAndTypeAndClaimStatus(user.getId(), IdentifierType.MOBILE.getCode(), IdentifierClaimStatus.VERIFYING.getCode());

            verificationCode(userIdentifier, cmd.getVerificationCode());

            //发验证码的手机和绑定的的手机是否相等，检查手机是否被篡改
            if (!cmd.getPhone().equals(userIdentifier.getIdentifierToken())) {
                LOGGER.error("phoneNumber param error");
                throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PHONE, "phone param error");
            }

            userIdentifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
            userIdentifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

          //add by huangliangming 20180731 使得微信注册时能够激活管理员
			// 刷新企业通讯录
            organizationService.processUserForMember(userIdentifier);
            //刷新地址信息
            propertyMgrService.processUserForOwner(userIdentifier);
            
            user = userProvider.findUserById(user.getId());
            String salt = EncryptionUtils.createRandomSalt();
            user.setSalt(salt);
            try {
                String randomCode = RandomGenerator.getRandomDigitalString(6);
                if (cmd.getPassword() != null && cmd.getPassword().length() > 0) {
                    randomCode = cmd.getPassword();
                }
                user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", randomCode, salt)));
            } catch (Exception e) {
                LOGGER.error("encode password failed", e);
                throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PASSWORD, "Unable to create password hash");

            }

            userProvider.updateUser(user);
            userProvider.updateIdentifier(userIdentifier);
            return null;
        }

    }

	@Override
    public UserLogin bindPhoneByApp(BindPhoneCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId();

        if( cmd.getPhone() == null){
            LOGGER.error("phoneNumber param error");
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PHONE, "phone param error");
        }
        if( cmd.getUserId() == null){
            LOGGER.error("UserId is null");
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS, "UserId is null");
        }
        User user = this.userProvider.findUserById(cmd.getUserId());
        UserIdentifier userIdentifier = userProvider.findClaimingIdentifierByToken(namespaceId,cmd.getPhone());

        UserLogin login = new UserLogin();
        verificationCode(userIdentifier, cmd.getVerificationCode());
        //查看该手机是否已经注册
        if(userIdentifier.getClaimStatus() == IdentifierClaimStatus.CLAIMED.getCode()){

            //如果手机已经注册过，则校验验证码，更新微信信息到该手机用户


            User existUser = userProvider.findUserById(userIdentifier.getOwnerUid());
            if (StringUtils.isBlank(existUser.getAvatar())) {
                existUser.setAvatar(user.getAvatar());
            }
            existUser.setGender(user.getGender());
            existUser.setNamespaceUserToken(user.getNamespaceUserToken());
            existUser.setNamespaceUserType(user.getNamespaceUserType());

            userProvider.updateUser(existUser);
            userProvider.deleteUser(user);
            login = createLogin(namespaceId, existUser, cmd.getDeviceIdentifier(), cmd.getPusherIdentify());
            login.setStatus(UserLoginStatus.LOGGED_IN);

        }else{

            //发验证码的手机和绑定的的手机是否相等，检查手机是否被篡改
            if(!cmd.getPhone().equals(userIdentifier.getIdentifierToken())){
                LOGGER.error("phoneNumber param error");
                throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PHONE, "phone param error");
            }
            userIdentifier.setOwnerUid(user.getId());
            userIdentifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
            userIdentifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            userIdentifier.setRegionCode(cmd.getRegionCode());
            String salt=EncryptionUtils.createRandomSalt();
            user.setSalt(salt);
            try {
                String randomCode = RandomGenerator.getRandomDigitalString(6);
                if(cmd.getPassword() != null && cmd.getPassword().length() > 0){
                    randomCode = cmd.getPassword();
                }
                user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s",randomCode,salt)));
            } catch (Exception e) {
                LOGGER.error("encode password failed", e);
                throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PASSWORD, "Unable to create password hash");

            }

            userProvider.updateUser(user);
            userProvider.updateIdentifier(userIdentifier);
            login = createLogin(namespaceId, user, cmd.getDeviceIdentifier(), cmd.getPusherIdentify());
            login.setStatus(UserLoginStatus.LOGGED_IN);
            
          //add by huangliangming 20180731 使得微信注册时能够激活管理员
			// 刷新企业通讯录
            organizationService.processUserForMember(userIdentifier);
            //刷新地址信息
            propertyMgrService.processUserForOwner(userIdentifier);
        }
        return login;
    }private void verificationCode(UserIdentifier userIdentifier, String code){
		if(userIdentifier == null || code == null || userIdentifier.getVerificationCode() == null || !userIdentifier.getVerificationCode().equals(code)){
			throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_VERIFICATION_CODE, "Invalid verification code or state");
		}
	}

    private void verificationCode(UserIdentifierLog userIdentifierlog, String code) {
        if (userIdentifierlog == null || code == null || userIdentifierlog.getVerificationCode() == null || !userIdentifierlog.getVerificationCode().equals(code)) {
            throw errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_VERIFICATION_CODE, "Invalid verification code or state");
        }
    }

    @Override
    public void checkVerifyCodeAndResetPassword(CheckVerifyCodeAndResetPasswordCommand cmd) {
        assert StringUtils.isNotEmpty(cmd.getVerifyCode());
        assert StringUtils.isNotEmpty(cmd.getIdentifierToken());
        assert StringUtils.isNotEmpty(cmd.getNewPassword());
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        UserIdentifier identifier = userProvider.findIdentifierByVerifyCode(cmd.getVerifyCode(),
                cmd.getIdentifierToken());
        if (null == identifier) {
            LOGGER.error("invalid operation,can not find verify information");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_VERIFICATION_CODE,
                    "invalid params");
        }

        // check the expire time
        if (DateHelper.currentGMTTime().getTime() - identifier.getNotifyTime().getTime() > 10 * 60000) {
            LOGGER.error("the verifycode is invalid with timeout");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
                    UserServiceErrorCode.ERROR_VERIFICATION_CODE_EXPIRED, "Invalid token status");
        }

        if (namespaceId == null || identifier.getNamespaceId() == null || namespaceId.intValue() != identifier.getNamespaceId().intValue()) {
            LOGGER.error("the namespaceId is invalid");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
                    UserServiceErrorCode.ERROR_INVALID_PARAMS, "Invalid namespaceId");
        }

		//判断手机号的用户与当前用户是否一致
		if (identifier.getOwnerUid() == null || !identifier.getOwnerUid().equals(UserContext.currentUserId())) {
            LOGGER.error("phone not match user error");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_PHONE_NOT_MATCH_USER,
                    "phone not match user error");
        }

		if (cmd.getRegionCode() != null && identifier.getRegionCode() != null && !cmd.getRegionCode().equals(identifier.getRegionCode())) {
            LOGGER.error("phone not match user error");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_PHONE_NOT_MATCH_USER,
                    "phone not match user error");
        }// find user by uid
		User user = userProvider.findUserById(identifier.getOwnerUid());
		user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", cmd.getNewPassword(), user.getSalt())));
		userProvider.updateUser(user);

    }

    @Override
	public void checkVerifyCodeAndResetPasswordWithoutIdentifyToken(CheckVerifyCodeAndResetPasswordWithoutIdentifyTokenCommand cmd) {
		User user = UserContext.current().getUser();
		if (user == null) {
            LOGGER.error("user not exists");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_USER_NOT_EXIST,
                    "user not exists");
		}
		UserIdentifier userIdentifier = this.userProvider.findUserIdentifiersOfUser(user.getId(),user.getNamespaceId());
		if (userIdentifier == null) {
            LOGGER.error("userIdentifier not exists");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
                    "userIdentifier not exists");
        }
        if (StringUtils.isBlank(userIdentifier.getVerificationCode()) || !userIdentifier.getVerificationCode().equals(cmd.getVerifyCode())) {
            LOGGER.error("invalid operation,can not find verify information");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_VERIFICATION_CODE,
                    "invalid params");
        }
        // check the expire time
        if (DateHelper.currentGMTTime().getTime() - userIdentifier.getNotifyTime().getTime() > 10 * 60000) {
            LOGGER.error("the verifycode is invalid with timeout");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
                    UserServiceErrorCode.ERROR_INVALD_TOKEN_STATUS, "Invalid token status");
        }
		user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", cmd.getNewPassword(), user.getSalt())));
		userProvider.updateUser(user);
	}

	@Override
    public UserTemporaryTokenDTO checkUserTemporaryToken(CheckUserTemporaryTokenCommand cmd) {

        if (StringUtils.isEmpty(cmd.getUserToken())) {
            LOGGER.error("userToken is empty");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
                    UserServiceErrorCode.ERROR_INVALID_USERTOKEN, "invalid usertoken");
        }

        try {
            UserTemporaryTokenDTO token = WebTokenGenerator.getInstance().fromWebToken(cmd.getUserToken(), UserTemporaryTokenDTO.class);


            if (token == null || token.getStartTime() == null || token.getInterval() == null) {
                LOGGER.error("userToken is invalid");
                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
                        UserServiceErrorCode.ERROR_INVALID_USERTOKEN, "invalid usertoken");
            }

            if (System.currentTimeMillis() > token.getStartTime() + token.getInterval()) {
                LOGGER.error("time expired");
                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
                        UserServiceErrorCode.ERROR_INVALID_USERTOKEN, "invalid usertoken");
            }

            return token;

        } catch (Exception ex) {
            LOGGER.error("userToken is invalid");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE,
                    UserServiceErrorCode.ERROR_INVALID_USERTOKEN, "invalid usertoken");
        }

    }

    @Override
    public CheckContactAdminResponse checkContactAdmin(CheckContactAdminCommand cmd) {
        CheckContactAdminResponse response = new CheckContactAdminResponse();
		/* 更换为如下所示的新的权限校验方式
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");

		if(resolver.checkSuperAdmin(UserContext.current().getUser().getId(), cmd.getOrganizationId())
				|| resolver.checkOrganizationAdmin(UserContext.current().getUser().getId(), cmd.getOrganizationId()))
			response.setIsAdmin(ContactAdminFlag.YES.getCode());
		else
			response.setIsAdmin(ContactAdminFlag.NO.getCode()); */
        Long enterpriseId = organizationService.getTopOrganizationId(cmd.getOrganizationId());
        boolean isAdmin = checkUserPrivilege(UserContext.currentUserId(), enterpriseId);
        if (isAdmin)
            response.setIsAdmin(TrueOrFalseFlag.TRUE.getCode());
        else
            response.setIsAdmin(TrueOrFalseFlag.FALSE.getCode());
        return response;
    }

    private boolean checkUserPrivilege(Long userId, Long enterpriseId) {
        if (userId == 0)
            return false;
        boolean isAdmin;
        try {
            isAdmin = userPrivilegeMgr.checkUserPrivilege(userId, enterpriseId, PrivilegeConstants.CREATE_OR_MODIFY_PERSON, FlowConstants.ORGANIZATION_MODULE, null, null, enterpriseId, null);
        } catch (Exception e) {
            isAdmin = false;
        }
        return isAdmin;
    }

    /**
     * 用于测试服务器状态，不要用于业务使用 by lqs 20171019
     */
    @Override
    public String checkServerStatus() {
        Map<String, String> result = new HashMap<String, String>();
        int flag = 0x00;

        // 检查是否可以申请内存创建对象
        Object obj = new Object();
        result.put("objectCreated", "OK");
        flag = flag | 0x01;

        // 检查redis storage 连接是否正常（可读可写）
        try {
            String key = "check.server.status";
            String heartbeat = "server.heartbeat.check";
            RedisTemplate template = bigCollectionProvider.getMapAccessor(key, "").getTemplate(new StringRedisSerializer());
            ValueOperations op = template.opsForValue();
            Object times = op.get(heartbeat);
            op.set(heartbeat, String.valueOf(System.currentTimeMillis()));

            result.put("redisStorageStatus", "OK");
            flag = flag | 0x02;
        } catch (Exception e) {
            LOGGER.error("Redis storage invalid state", e);
            result.put("redisStorageStatus", e.getMessage());
        }

        // 检查redis cache是否可以正常evict缓存和缓存内容
        try {
            userProvider.updateCacheStatus();
            userProvider.checkCacheStatus();
            result.put("redisCacheStatus", "OK");
            flag = flag | 0x04;
        } catch (Exception e) {
            LOGGER.error("Redis cache invalid state", e);
            result.put("redisCacheStatus", e.getMessage());
        }

        // 检查数据库查询是否正常
        try {
            namespaceResourceProvider.checkDbStatus();
            result.put("dbStatus", "OK");
            flag = flag | 0x08;
        } catch (Exception e) {
            LOGGER.error("Db invalid state", e);
            result.put("dbStatus", e.getMessage());
        }

        if (flag == 0x0f) {
            result.put("coreStatus", "OK");
        } else {
            result.put("coreStatus", String.valueOf(flag));
        }

        return StringHelper.toJsonString(result);
    }

    @Override
    public SystemInfoResponse updateUserBySystemInfo(
            SystemInfoCommand cmd, HttpServletRequest request, HttpServletResponse response) {

        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        User user = UserContext.current().getUser();
        UserLogin login = UserContext.current().getLogin();

        SystemInfoResponse resp = new SystemInfoResponse();
        String urlInBrowser = configProvider.getValue(
                namespaceId,
                ConfigConstants.APP_SYSTEM_UPLOAD_URL_IN_BROWSER,
                "https://upload.zuolin.com");

        resp.setUploadUrlInBrowser(urlInBrowser);

        if (user != null && user.getId() >= User.MAX_SYSTEM_USER_ID && login != null) {
            String userKey = NameMapper.getCacheKey("user", user.getId(), null);
            Accessor accessor = this.bigCollectionProvider.getMapAccessor(userKey, String.valueOf(login.getLoginId()));
            UserLogin newLogin = accessor.getMapValueObject(String.valueOf(login.getLoginId()));
            if (newLogin != null
                    && newLogin.getLoginId() == login.getLoginId()
                    && newLogin.getLoginInstanceNumber() == newLogin.getLoginInstanceNumber()
                    ) {
                //update device info
                login.setPusherIdentify(cmd.getPusherIdentify());
                login.setDeviceIdentifier(cmd.getDeviceIdentifier());

                if (!(newLogin.getPusherIdentify() != null
                        && newLogin.getPusherIdentify().equals(cmd.getPusherIdentify())
                        && newLogin.getDeviceIdentifier() != null
                        && newLogin.getDeviceIdentifier().equals(cmd.getPusherIdentify()))) {
                    //not equal, set the newLogin
                    newLogin.setPusherIdentify(cmd.getPusherIdentify());
                    newLogin.setDeviceIdentifier(cmd.getDeviceIdentifier());
                    accessor.putMapValueObject(String.valueOf(newLogin.getLoginId()), newLogin);
                }

            }

            List<Border> borders = this.borderProvider.listAllBorders();
            List<String> borderStrs = borders.stream().map((Border border) -> {
                return String.format("%s:%d", border.getPublicAddress(), border.getPublicPort());
            }).collect(Collectors.toList());

            resp.setAccessPoints(borderStrs);
            resp.setContentServer(contentServerService.getContentServer());
        }

        Long l = configurationProvider.getLongValue(namespaceId, ConfigConstants.PAY_PLATFORM, 0L);
        resp.setPaymentPlatform(l);

        Integer mypublishFlag = configurationProvider.getIntValue(namespaceId, ConfigConstants.MY_PUBLISH_FLAG, 1);
        resp.setMyPublishFlag(mypublishFlag.byteValue());
        //查询不显示的更多地址信息的标志
        Integer addressDialogStyle = configurationProvider.getIntValue(namespaceId, "zhifuhui.display.flag", 1);
        resp.setAddressDialogStyle(addressDialogStyle);

        resp.setScanForLogonServer(this.configurationProvider.getValue(namespaceId, "scanForLogonServer", SCAN_FOR_LOGON_SERVER));

        // 客户端资源缓存配置
        String clientCacheConfig = configurationProvider.getValue(
                namespaceId, ConfigConstants.CONTENT_CLIENT_CACHE_CONFIG,
                // Default config
                "{\"ignoreParameters\":[\"token\",\"auth_key\"]}");
        resp.setContentCacheConfig(
                (ContentCacheConfigDTO) StringHelper.fromJsonString(clientCacheConfig, ContentCacheConfigDTO.class));

        return resp;
    }

    @Override
    public void syncUsersFromAnBangWuYe(SyncUsersFromAnBangWuYeCommand cmd) {
        this.loadAnbangConfigFromDb();
        //调用授权接口
        String timestamp = null;
        if (cmd.getIsAll() == 1) {//全量
            timestamp = null;
        } else if (cmd.getIsAll() == 0) {//从上次同步时间的增量
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            timestamp = formatter.format(DateHelper.currentGMTTime());
        }
        List<String> timestampList = new ArrayList<>();
        timestampList.add(timestamp);

        String secret = String.format("%s:%s", ANBANG_CLIENTID, ANBANG_CLIENTSECRET);
        String baseStr = new String(Base64.getEncoder().encode(secret.getBytes()));

        MultiValueMap<String, Object> headerParam = new LinkedMultiValueMap<String, Object>();
        headerParam.add("Authorization", "Basic " + baseStr);


        MultiValueMap<String, Object> bodyParam = new LinkedMultiValueMap<String, Object>();
        bodyParam.add("client_id", ANBANG_CLIENTID);
        bodyParam.add("client_secret", ANBANG_CLIENTSECRET);
        bodyParam.add("grant_type", "client_credentials");

        dbProvider.execute(r -> {
            try {
                restCall(HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED, ANBANG_OAUTH_URL, headerParam, bodyParam, new ListenableFutureCallback<ResponseEntity<String>>() {

                    @Override
                    public void onSuccess(ResponseEntity<String> result) {
                        Map resultMap = JSON.parseObject(result.getBody().toString());
                        String acess_token = String.valueOf(resultMap.get("access_token"));


                        Map headerParam = new HashMap();
                        headerParam.put("Authorization", "bearer " + acess_token);

                        StringBuffer getUrl = new StringBuffer(ANBANG_USERS_URL);
                        if (timestampList.get(0) != null) {
                            getUrl.append("?").append("startDate=").append(timestampList.get(0));
                        }

                        try {
                            ListenableFuture<ResponseEntity<String>> users_result = restCall(HttpMethod.GET, MediaType.APPLICATION_JSON, getUrl.toString(), headerParam, null, new ListenableFutureCallback<ResponseEntity<String>>() {

                                @Override
                                public void onSuccess(ResponseEntity<String> result) {
                                    List<Map> userList = (List) JSON.parseObject(result.getBody().toString()).get("data");
                                    if (userList != null && userList.size() > 0) {
//										if (timestampList.get(0) == null) {//todo 参数为null,为全量同步,同步所有的用户
//											// 删除全部的用户
//											userProvider.deleteUserAndUserIdentifiers(0, null, NamespaceUserType.ANBANG.getCode());
//										}else{  //todo 参数传当前时间,为增量同步，只同步上次同步拘束时间~当前时间的数据
//											//如果有之前同步过的用户，删掉重建
//											List<String> namespaceUserTokens = new ArrayList<>();
//											for (Map userInfo : userList) {
//												namespaceUserTokens.add(userInfo.get("id").toString());
//											}
//											userProvider.deleteUserAndUserIdentifiers(0, namespaceUserTokens, NamespaceUserType.ANBANG.getCode());
//										}
                                        LOGGER.debug("AnBang user size" + userList.size());

                                        List<User> users = userList.stream().map(r2 -> {
                                            User user = new User();
                                            user.setNamespaceId(ANBANG_NAMESPACE_ID);
                                            user.setNickName(r2.get("nickname") != null ? r2.get("nickname").toString() : "");
                                            user.setIdentifierToken(r2.get("login") != null ? r2.get("login").toString() : "");
                                            user.setAvatar(r2.get("avatar") != null ? r2.get("avatar").toString() : "");
                                            user.setCreateTime(r2.get("createdDate") != null ? Timestamp.valueOf(r2.get("createdDate").toString()) : null);
                                            user.setPasswordHash(r2.get("password") != null ? r2.get("password").toString() : "");
                                            user.setStatus(UserStatus.ACTIVE.getCode());
                                            user.setLocale(Locale.CHINA.toString());
                                            user.setNamespaceUserToken(r2.get("id") != null ? r2.get("id").toString() : "");
                                            user.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                                            user.setNamespaceUserType(NamespaceUserType.ANBANG.getCode());
                                            user.setThirdData(r2.toString());
                                            return user;
                                        }).collect(Collectors.toList());

                                        //create
                                        createUsersAndUserIdentifiers(users);
                                        LOGGER.debug("AnBang createUsersAndUserIdentifiers completed");
                                    }
                                }

                                @Override
                                public void onFailure(Throwable ex) {
                                    LOGGER.error(ex.getMessage());
                                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                                            "Unable to sync2");
                                }
                            });
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable ex) {
                        LOGGER.error(ex.getMessage());
                        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                                "Unable to sync1");
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public String makeAnbangRedirectUrl(Long userId, String location, Map<String, String[]> paramMap) {
        this.loadAnbangConfigFromDb();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(location);

        if (paramMap.get("sceneToken") != null) {
            String sceneToken = StringUtils.join(paramMap.get("sceneToken"));
            SceneTokenDTO sceneDTO = checkSceneToken(userId, sceneToken);
            Long communityId = getCommunityIdBySceneToken(sceneDTO);
            Long orgId = getOrgIdBySceneToken(sceneDTO);
            if (communityId != null) {
                builder.queryParam("communityId", communityId);
            }
            if (orgId != null) {
                builder.queryParam("organizationId", orgId);
            }
        }

        builder.queryParam("ns", ANBANG_NAMESPACE_ID);
        builder.queryParam("namespaceId", ANBANG_NAMESPACE_ID);
        builder.queryParam("userId", userId);

        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            if (!entry.getKey().equals("token") && !entry.getKey().equals("redirect") && !entry.getKey().equals("abtoken")) {
                builder.queryParam(entry.getKey(), entry.getValue());
            }
        }

        return builder.build().toUriString();
    }

    @Override
    public void pushUserDemo() {
        MultiValueMap<String, Object> headerParam = new LinkedMultiValueMap<String, Object>();

        Map bodyMap = new HashMap();
        bodyMap.put("appKey", "578580df-7015-4a42-b61f-b5c0ec0bc38a");
//		bodyMap.put("timestamp", String.valueOf(DateHelper.currentGMTTime().getTime()));
        bodyMap.put("timestamp", "1515490912375");
        bodyMap.put("nonce", "10036426855237293345");
        bodyMap.put("secretKey", "S2rPpM5fGsgAx6CeAMTb5R2MOIsHmiScPmqCNR+NsD2TjeUlmuuls6xt1WYO/YqsGnLUMt1RKRnB5xzoVjwOng==");
        bodyMap.put("nickName", "慧盟管家");
        bodyMap.put("identifierToken", "18844157372");
        bodyMap.put("avatar", null);
        bodyMap.put("namespaceUserToken", "2018103116310798698");

        MultiValueMap bodyParam = new LinkedMultiValueMap<String, Object>();
        bodyParam.add("appKey", "578580df-7015-4a42-b61f-b5c0ec0bc38a");
//		bodyParam.add("timestamp", String.valueOf(DateHelper.currentGMTTime().getTime()));
        bodyParam.add("timestamp", "1515490912375");
        bodyParam.add("nonce", "10036426855237293345");
        bodyParam.add("secretKey", "S2rPpM5fGsgAx6CeAMTb5R2MOIsHmiScPmqCNR+NsD2TjeUlmuuls6xt1WYO/YqsGnLUMt1RKRnB5xzoVjwOng==");
        bodyParam.add("nickName", "慧盟管家");
        bodyParam.add("identifierToken", "18844157372");
        bodyParam.add("avatar", null);
        bodyParam.add("namespaceUserToken", "2018103116310798698");

        bodyParam.add("signature", SignatureHelper.computeSignature(bodyMap, "S2rPpM5fGsgAx6CeAMTb5R2MOIsHmiScPmqCNR+NsD2TjeUlmuuls6xt1WYO/YqsGnLUMt1RKRnB5xzoVjwOng=="));


        try {
            ListenableFuture<ResponseEntity<String>> auth_result = restCall(HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED, "http://printtest.zuolin.com/evh/openapi/pushUsers", headerParam, bodyParam, new ListenableFutureCallback<ResponseEntity<String>>() {

                @Override
                public void onSuccess(ResponseEntity<String> result) {
                    LOGGER.debug(result.toString());
                }

                @Override
                public void onFailure(Throwable ex) {
                    LOGGER.error(ex.getMessage());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public PushUsersResponse createUsersForAnBang(PushUsersCommand cmd) {
        this.loadAnbangConfigFromDb();
        dbProvider.execute(r -> {
            List<String> tokens = Collections.singletonList(cmd.getNamespaceUserToken());
//			this.userProvider.deleteUserAndUserIdentifiers(0,tokens,NamespaceUserType.ANBANG.getCode());
            User user = new User();
            user.setStatus(UserStatus.ACTIVE.getCode());
            user.setNamespaceId(ANBANG_NAMESPACE_ID);
            user.setNickName(cmd.getNickName());
            user.setGender(UserGender.UNDISCLOSURED.getCode());
            user.setNamespaceUserType(NamespaceUserType.ANBANG.getCode());
            user.setNamespaceUserToken(cmd.getNamespaceUserToken());
            user.setLevel(UserLevel.L1.getCode());
            user.setAvatar(cmd.getAvatar() != null ? cmd.getAvatar() : "");
            String salt = EncryptionUtils.createRandomSalt();
            user.setSalt(salt);
            try {
                user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", salt)));
            } catch (Exception e) {
                LOGGER.error("encode password failed");
                throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PASSWORD, "Unable to create password hash");
            }

            List<User> users = new ArrayList<>();
            users.add(user);
            createUsersAndUserIdentifiers(users);
            return null;
        });
        return null;
    }

    @Override
    public User getUserFromAnBangToken(String token) {
        this.loadAnbangConfigFromDb();
        StringBuffer getUrl = new StringBuffer(ANBANG_CURRENT_USER_URL);
        Map headerParam = new HashMap();
        headerParam.put("Authorization", "bearer " + token);

        try {
            ResponseEntity<String> result = restCallSync(HttpMethod.GET, MediaType.APPLICATION_JSON, getUrl.toString(), headerParam, null);
            Map currentUser = (Map) JSON.parseObject(result.getBody().toString()).get("data");
            String userIdentifierToken = currentUser.get("login").toString();
            int regionCode = 86;
            int namespaceId = ANBANG_NAMESPACE_ID;
            User user = userProvider.findUserByAccountName(userIdentifierToken);
            if (user == null) {
                UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, userIdentifierToken);
                // 把 regionCode 的检查加上，之前是没有检查的    add by xq.tian 2017/07/12
                if (userIdentifier != null && Objects.equals((userIdentifier.getRegionCode() != null ? userIdentifier.getRegionCode() : 86), regionCode)) {
                    user = userProvider.findUserById(userIdentifier.getOwnerUid());
                }

            }

            if (UserStatus.fromCode(user.getStatus()) != UserStatus.ACTIVE) {
                return null;
            }

            return user;

        } catch (Exception ex) {
            LOGGER.error("anbang error", ex);
            return null;
        }

    }

    @Override
    public UserLogin verifyUserByTokenFromAnBang(String token) {
        this.loadAnbangConfigFromDb();
        List<UserLogin> logins = new ArrayList<>();
        User user = getUserFromAnBangToken(token);
        if (user == null) {
            return null;
        }

        dbProvider.execute(r -> {
            UserLogin login = createLogin(ANBANG_NAMESPACE_ID, user, null, null);
            login.setStatus(UserLoginStatus.LOGGED_IN);
            logins.add(login);
            return null;
        });
        return logins.size() > 0 ? logins.get(0) : null;

    }

    @Override
    public List<SceneDTO> listAnbangRelatedScenes(ListAnBangRelatedScenesCommand cmd) {
        this.loadAnbangConfigFromDb();
        User user = getUserFromAnBangToken(cmd.getAbtoken());
        if (user != null) {
            ListUserRelatedScenesCommand cmd2 = new ListUserRelatedScenesCommand();
            cmd2.setDefaultFlag(0);
            UserContext.current().setUser(user);
            UserContext.current().setNamespaceId(ANBANG_NAMESPACE_ID);
            List<SceneDTO> sceneDtoList = listUserRelatedScenes(cmd2);
            return sceneDtoList;
        }

        return null;
    }

    @Override
    public void logonBuAnBangToken() {

    }

    public ListenableFuture<ResponseEntity<String>> restCall(HttpMethod method, MediaType mediaType, String url, Map headerParam, Map bodyParam, ListenableFutureCallback<ResponseEntity<String>> responseCallback) throws UnsupportedEncodingException {
        AsyncRestTemplate template = new AsyncRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headerParam.forEach((k, v) -> {
            headers.add(k.toString(), v.toString().replace("[", "").replace("]", ""));
        });

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<Map<String, String>>(bodyParam, headers);
        ListenableFuture<ResponseEntity<String>> future = template.exchange(url, method, requestEntity, String.class);

        if (responseCallback != null) {
            future.addCallback(responseCallback);
        }

        return future;
    }

    public ResponseEntity<String> restCallSync(HttpMethod method, MediaType mediaType, String url, Map headerParam, Map bodyParam) throws UnsupportedEncodingException {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headerParam.forEach((k, v) -> {
            headers.add(k.toString(), v.toString().replace("[", "").replace("]", ""));
        });

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<Map<String, String>>(bodyParam, headers);
        ResponseEntity<String> future = template.exchange(url, method, requestEntity, String.class);

        return future;
    }

    private void createUsersAndUserIdentifiers(List<User> users) {
        for (User user : users) {
            UserIdentifier old_userIdentifier = this.userProvider.findClaimedIdentifierByToken(user.getNamespaceId(), user.getIdentifierToken());

            if (old_userIdentifier != null) {
                User old_user = this.userProvider.findUserById(old_userIdentifier.getOwnerUid());
                if (old_user != null) {
                    old_user.setUpdateTime(user.getUpdateTime());
                    old_user.setNamespaceUserToken(user.getNamespaceUserToken());
                    old_user.setNamespaceUserType(user.getNamespaceUserType());
                    this.userProvider.updateUser(old_user);

                    old_userIdentifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
                    old_userIdentifier.setRegionCode(86);
                    old_userIdentifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    this.userProvider.updateIdentifierByUid(old_userIdentifier);
                } else {
                    old_userIdentifier.setClaimStatus(IdentifierClaimStatus.FREE_STANDING.getCode());
                    this.userProvider.deleteIdentifier(old_userIdentifier);
                    old_userIdentifier = null;
                }
            }

            if (old_userIdentifier == null) {
                this.userProvider.createUser(user);
                UserIdentifier userIdentifier = new UserIdentifier();
                userIdentifier.setOwnerUid(user.getId());
                userIdentifier.setIdentifierType(IdentifierType.MOBILE.getCode());
                userIdentifier.setIdentifierToken(user.getIdentifierToken());
                userIdentifier.setNamespaceId(user.getNamespaceId());
                userIdentifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
                userIdentifier.setRegionCode(86);
                userIdentifier.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                userIdentifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                this.userProvider.createIdentifier(userIdentifier);
            }
        }
    }

    // 生成随机key
    @Override
    public QRCodeDTO querySubjectIdForScan() {
        String uuid = UUID.randomUUID().toString();
        String subjectId = "waitScanForLogon" + uuid.replace("-", "");
        NewQRCodeCommand cmd = new NewQRCodeCommand();
        cmd.setActionData(subjectId);
        return qrCodeService.createQRCode(cmd);
    }

    // 登录等待
    @Override
    public DeferredResult<RestResponse> waitScanForLogon(String subjectId, HttpServletRequest contextRequest, HttpServletResponse contextResponse) {


        DeferredResult<RestResponse> finalResult = this.messagingService.blockingEvent(subjectId, "ORORDINARY", 30 * 1000, new DeferredResult.DeferredResultHandler() {

            @Override
            public void handleResult(Object result) {
                RestResponse restResponse = (RestResponse) result;
                BlockingEventResponse response = (BlockingEventResponse) restResponse.getResponseObject();
                if (response.getStatus() != BlockingEventStatus.CONTINUTE) {
                    LOGGER.error("waitScanForLogon failure");
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "waitScanForLogon failure");
                }
                if (!response.getSubject().equals("blockingEventKey." + subjectId)) {
                    LOGGER.error("waitScanForLogon failure");
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "waitScanForLogon failure");
                }
                if (StringUtils.isEmpty(response.getMessage())) {
                    LOGGER.error("waitScanForLogon failure");
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "waitScanForLogon failure");
                }

                String token = null;
                try {
                    token = URLDecoder.decode(response.getMessage(), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
//					String[] tokenParam = token.substring(1,token.length()-2).split("&");
                if (token.indexOf("\"") == 0)
                    token = token.substring(1, token.length());
                if (token.indexOf("\"") == token.length() - 1)
                    token = token.substring(0, token.length() - 1);
                String[] tokenParam = token.trim().split("&");
                String salt = tokenParam[0];
                if (salt.equals(SALT)) {
                    Long userId = Long.valueOf(tokenParam[1]);
                    Integer namespaceId = Integer.valueOf(tokenParam[2]);
                    String userToken = tokenParam[3];
                    LOGGER.debug("userLoginToken = {}", userToken);

                    LoginToken logintoken = WebTokenGenerator.getInstance().fromWebToken(userToken, LoginToken.class);
                    Map valueMap = new HashMap();
                    valueMap.put("userLogin", GsonUtil.toJson(logintoken));
                    valueMap.put("args", tokenParam[4]);
                    valueMap.put("contentServer", contentServerService.getContentServer());
                    response.setMessage(GsonUtil.toJson(valueMap));
                    restResponse.setResponseObject(response);
                    restResponse.setErrorCode(ErrorCodes.SUCCESS);
                    restResponse.setErrorDescription("OK");

//					//todo 验证
                    UserLogin origin_login = logonByToken(logintoken);
                    User user = userProvider.findUserById(origin_login.getUserId());
                    UserLogin login = createLogin(namespaceId, user, null, null);
                    LoginToken newToken = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), login.getImpersonationId());
                    String tokenString = WebTokenGenerator.getInstance().toWebToken(newToken);
                    WebRequestInterceptor.setCookieInResponse("token", tokenString, contextRequest, contextResponse);
                } else {
                    restResponse.setErrorCode(ErrorCodes.ERROR_ACCESS_DENIED);
                    LOGGER.error("waitScanForLogon failure");
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "waitScanForLogon failure");
                }
            }

        });

        return finalResult;
    }

    private static final String SALT = "salt";

    // 获取当前准备登录用户的混淆key
    @Override
    public String getSercetKeyForScan(String args) {
        String userToken = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
        String plain = SALT + "&" + UserContext.currentUserId() + "&" + UserContext.getCurrentNamespaceId() + "&" + userToken + "&" + args;
        String token = null;
        try {
            token = URLEncoder.encode(plain, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return token;
    }

    // 扫码登录
    @Override
    public void logonByScan(String subjectId, String message) {
        this.messagingService.signalBlockingEvent(subjectId, message, 3 * 1000);
    }


    // 获取统一的功能卡片
    @Override
    public List<FunctionCardDto> listUserRelatedCards() {
        List<FunctionCardDto> cardDtos = new ArrayList<>();
        functionCardHandlers.forEach(r -> {
            cardDtos.add(r.listCards(1000000, 1L));
        });
        return cardDtos;
    }

    @Override
    public SearchUserByIdentifierResponse searchUserByIdentifier(SearchUserByIdentifierCommand cmd) {

        if (cmd.getIdentifierToken().length() < 7) {
            return null;
        }

        int pageSize = 10;

        List<User> users = userProvider.searchUserByIdentifier(cmd.getIdentifierToken(), cmd.getNamespaceId(), pageSize);

        List<UserDTO> dtos = new ArrayList<>();
        if (users != null) {
            dtos = users.stream().map(r -> ConvertHelper.convert(r, UserDTO.class)).collect(Collectors.toList());
        }
        SearchUserByIdentifierResponse response = new SearchUserByIdentifierResponse();
        response.setDtos(dtos);
        return response;
    }

    @Override
    public Byte isUserAuth() {
        User user = UserContext.current().getUser();
        int amount = this.organizationProvider.getUserOrgAmount(user.getId());
        return amount > 0 ? (byte) 1 : (byte) 0;
    }

    /**
     * 返回的认证场景排序
     * 排序规则为：communityId 顺序排列
     *
     * @param sceneDTOList
     * @return
     */
    private List<UserCurrentEntity> sortCurrentEntity(List<UserCurrentEntity> sceneDTOList) {
        //传入数组为空或数据小于2（即没数据或数据只有一条时），是没必要排序的，直接返回。
        if (sceneDTOList == null || sceneDTOList.size() < 2) return sceneDTOList;
        //排序
        sceneDTOList.sort(new Comparator<UserCurrentEntity>() {
            @Override
            public int compare(UserCurrentEntity o1, UserCurrentEntity o2) {
                if (o1.getEntityId() == null || o2.getEntityId() == null) {
                    return 0;
                }
                return o1.getEntityId().compareTo(o2.getEntityId());
            }
        });
        return sceneDTOList;
    }

    
    /**
     * 根据手机号查询某域空间中的用户
     * @param cmd
     * @return
     */
    @Override
    public UserDTO getUserFromPhone(FindUserByPhoneCommand cmd) {
        LOGGER.info("getUserFromPhone  -->  cmd:[{}]",cmd);
		Integer namespaceId = cmd.getNamespaceId();
		String phone = cmd.getPhone() ;
		if(namespaceId ==null || StringUtils.isBlank(phone) ){
			return null ;
		}
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, phone);
		if (userIdentifier != null) {
            User user = userProvider.findUserById(userIdentifier.getOwnerUid());
            if (user != null) {
                user.setIdentifierToken(userIdentifier.getIdentifierToken());
                return ConvertHelper.convert(user, UserDTO.class);
            }
        }
		return null;
	}
    
    
    /**
     * 校验验证码是否正确
     * @param cmd
     * @return
     */
    @Override
    public PointCheckVCDTO pointCheckVerificationCode(PointCheckVerificationCodeCommand cmd) {

        PointCheckVCDTO returnDTO = new PointCheckVCDTO();
        String verificationCode = cmd.getVerificationCode();
        String phone = cmd.getPhone();
        int namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());


        UserIdentifier identifier = userProvider.findClaimedIdentifierByToken(namespaceId, phone);
        if (identifier == null) {
            LOGGER.error("User identifier not found in db, namespaceId=" + namespaceId + ", phone="+phone+",cmd=" + cmd);
            returnDTO.setResultCode(false);
            returnDTO.setResult("User identifier not found in db");
            return  returnDTO;
        }

        //检查传过来的验证码跟生成保存在数据库中的相等即可
        if (identifier.getVerificationCode() != null
                && identifier.getVerificationCode().equals(verificationCode)) {
        	returnDTO.setResultCode(true);
        	return  returnDTO;
        	
        }
        
        return null;
    }

    /**
     * 有简称取简称,无简称取全名
     * @param sceneList
     * @return
     */
	private List<SceneDTO> handleSortName(List<SceneDTO> sceneList ){
	    
		  if(sceneList != null && sceneList.size()>0)
		  {
			  for(SceneDTO dto : sceneList){
				  if(StringUtils.isBlank(dto.getAliasName())){
					  dto.setAliasName(dto.getName()); 
				  }
			  }
		  }
		  return sceneList ;
	  }

      @Override
	  public UserDTO getTopAdministrator( GetTopAdministratorCommand cmd){

            if(cmd.getOrgId() == null ){
                LOGGER.error("orgId is null in the  cmd = {}",  cmd);
                throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,"orgId is null.");
            }
          Organization org = checkOrganization(cmd.getOrgId());
        //  Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
          if(org != null) {
              if (org.getAdminTargetId() != null) {
                  UserIdentifier userIdentifier = userProvider
                          .findClaimedIdentifierByOwnerAndType(org.getAdminTargetId(),IdentifierType.MOBILE.getCode());
                  if (userIdentifier != null) {
                      User user = userProvider.findUserById(userIdentifier.getOwnerUid());
                      if (user != null) {
                          user.setIdentifierToken(userIdentifier.getIdentifierToken());
                          return ConvertHelper.convert(user, UserDTO.class);
                      }
                  }
                  return null;
              }
          }
          return null;
      }


        private Organization checkOrganization(Long organizationId) {
            Organization org = organizationProvider.findOrganizationById(organizationId);
            if(org == null){
                LOGGER.error("Unable to find the organization.organizationId = {}",  organizationId);
                throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,"Unable to find the organization.");
            }
            return org;
        }

    /*******************统一用户同步数据**********************/
    @KafkaListener(id="syncCreateUser", topicPattern = "user-create-event")
    public void syncCreateUser(ConsumerRecord<?, String> record) {
        User user =  (User) StringHelper.fromJsonString(record.value(), User.class);
        Namespace namespace = this.namespaceProvider.findNamespaceById(2);
        if (namespace != null) {
            if (namespace.getId().equals(user.getNamespaceId())) {
                //在接收到kafka的消息之前，core server可能已经向统一用户拉取数据了，
                //所以这里加个判断，是新增还是更新.
                User existsUser = this.userProvider.findUserById(user.getId());
                if (existsUser != null) {
                    this.userProvider.updateUserFromUnite(user);
                }else {
                    this.userProvider.createUserFromUnite(user);
                }
            }
        }else {
            if (!Integer.valueOf(2).equals(user.getNamespaceId())) {
                User existsUser = this.userProvider.findUserById(user.getId());
                if (existsUser != null) {
                    this.userProvider.updateUserFromUnite(user);
                }else {
                    this.userProvider.createUserFromUnite(user);
                }
            }
        }
    }

    @KafkaListener(id="syncUpdateUser", topicPattern = "user-update-event")
    public void syncUpdateUser(ConsumerRecord<?, String> record) {
        User user =  (User) StringHelper.fromJsonString(record.value(), User.class);
        Namespace namespace = this.namespaceProvider.findNamespaceById(2);
        if (namespace != null) {
            if (namespace.getId().equals(user.getNamespaceId())) {
                this.userProvider.updateUserFromUnite(user);
            }
        }else {
            if (!Integer.valueOf(2).equals(user.getNamespaceId())) {
                this.userProvider.updateUserFromUnite(user);
            }
        }
    }

    @KafkaListener(id="syncDeleteUser", topicPattern = "user-delete-event")
    public void syncDeleteUser(ConsumerRecord<?, String> record) {
        User user =  (User) StringHelper.fromJsonString(record.value(), User.class);
        Namespace namespace = this.namespaceProvider.findNamespaceById(2);
        if (namespace != null) {
            if (namespace.getId().equals(user.getNamespaceId())) {
                this.userProvider.deleteUser(user);
            }
        }else {
            if (!Integer.valueOf(2).equals(user.getNamespaceId())) {
                this.userProvider.deleteUser(user);
            }
        }

    }

    @KafkaListener(id="syncCreateUserIdentifier", topicPattern = "userIdentifier-create-event")
    public void syncCreateUserIdentifier(ConsumerRecord<?, String> record) {
        UserIdentifier userIdentifier =  (UserIdentifier) StringHelper.fromJsonString(record.value(), UserIdentifier.class);
        Namespace namespace = this.namespaceProvider.findNamespaceById(2);
        if (namespace != null) {
            if (namespace.getId().equals(userIdentifier.getNamespaceId())) {
                UserIdentifier existsIdentifier = this.userProvider.findClaimingIdentifierByToken(userIdentifier.getNamespaceId(), userIdentifier.getIdentifierToken());
                if (existsIdentifier != null) {
                    this.userProvider.updateIdentifierFromUnite(userIdentifier);
                }else {
                    this.userProvider.createIdentifierFromUnite(userIdentifier);
                }
            }
        }else {
            if (!Integer.valueOf(2).equals(userIdentifier.getNamespaceId())) {
                UserIdentifier existsIdentifier = this.userProvider.findClaimingIdentifierByToken(userIdentifier.getNamespaceId(), userIdentifier.getIdentifierToken());
                if (existsIdentifier != null) {
                    this.userProvider.updateIdentifierFromUnite(userIdentifier);
                }else {
                    this.userProvider.createIdentifierFromUnite(userIdentifier);
                }
            }
        }

    }

    @KafkaListener(id="syncUpdateUserIdentifier", topicPattern = "userIdentifier-update-event")
    public void syncUpdateUserIdentifier(ConsumerRecord<?, String> record) {
        UserIdentifier userIdentifier =  (UserIdentifier) StringHelper.fromJsonString(record.value(), UserIdentifier.class);
        Namespace namespace = this.namespaceProvider.findNamespaceById(2);
        if (namespace != null) {
            if (namespace.getId().equals(userIdentifier.getNamespaceId())) {
                this.userProvider.updateIdentifierFromUnite(userIdentifier);
            }
        }else {
            if (!Integer.valueOf(2).equals(userIdentifier.getNamespaceId())) {
                this.userProvider.updateIdentifierFromUnite(userIdentifier);
            }
        }
    }

    @KafkaListener(id="userKickoffMessage", topicPattern = "user.kickoff")
    public void userKickoffMessage(ConsumerRecord<?, String> record) {
        UserLogin newLogin = (UserLogin) StringHelper.fromJsonString(record.value(), UserLogin.class);
        kickoffLoginByDevice(newLogin);
    }
    /*********************同步数据 END************************/

}
