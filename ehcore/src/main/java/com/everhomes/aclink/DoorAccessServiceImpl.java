// @formatter:off
package com.everhomes.aclink;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipayLogger;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.atomikos.util.FastDateFormat;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.aclink.huarun.*;
import com.everhomes.aclink.lingling.*;
import com.everhomes.aclink.uclbrt.UclbrtHttpClient;
import com.everhomes.address.Address;
import com.everhomes.address.AddressProvider;
import com.everhomes.app.App;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.blacklist.BlacklistService;
import com.everhomes.border.Border;
import com.everhomes.border.BorderConnectionProvider;
import com.everhomes.border.BorderProvider;
import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.SystemEvent;
import com.everhomes.business.BusinessService;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.CommunityService;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.group.Group;
import com.everhomes.group.GroupAdminStatus;
import com.everhomes.group.GroupMember;
import com.everhomes.group.GroupProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplate;
import com.everhomes.locale.LocaleTemplateProvider;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.organization.*;
import com.everhomes.payment.util.DownloadUtil;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.aclink.*;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.community.BuildingDTO;
import com.everhomes.rest.community.CommunityType;
import com.everhomes.rest.community.GetBuildingCommand;
import com.everhomes.rest.community.GetCommunitiesByIdsCommand;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.messaging.*;
import com.everhomes.rest.openapi.FamilyAddressDTO;
import com.everhomes.rest.openapi.OrganizationAddressDTO;
import com.everhomes.rest.openapi.UserAddressDTO;
import com.everhomes.rest.organization.ListUserRelatedOrganizationsCommand;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberStatus;
import com.everhomes.rest.organization.OrganizationSimpleDTO;
import com.everhomes.rest.rpc.server.AclinkRemotePdu;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.GetUserDefaultAddressCommand;
import com.everhomes.rest.user.IdentifierClaimStatus;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.sequence.LocalSequenceGenerator;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhUserIdentifiers;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.*;
import com.everhomes.util.*;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.util.excel.ExcelUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Security;
import java.sql.Timestamp;
import java.text.Format;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class DoorAccessServiceImpl implements DoorAccessService, LocalBusSubscriber, ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DoorAccessServiceImpl.class);

    @Autowired
    UclbrtHttpClient uclbrtHttpClient;
    @Autowired
    BigCollectionProvider bigCollectionProvider;
    
    @Autowired
    DoorAccessProvider doorAccessProvider;
    
    @Autowired
    DoorAuthProvider doorAuthProvider;
    
    @Autowired
    DoorCommandProvider doorCommandProvider;
    
    @Autowired
    AclinkMsgGenerator msgGenerator;
    
    @Autowired
    AclinkMessageSequence messageSequence;
    
    @Autowired
    private DbProvider dbProvider;
    
    @Autowired
    private AclinkProvider aclinkProvider;
    
    @Autowired
    private AclinkMessageSequence aclinkMessageSequence;
    
    @Autowired
    private AesUserKeyProvider aesUserKeyProvider;
    
    @Autowired
    private AesServerKeyService aesServerKeyService;
    
    @Autowired
    private OwnerDoorProvider ownerDoorProvider;
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ConfigurationProvider  configProvider;
    
    @Autowired
    private MessagingService messagingService;
    
    @Autowired
    private AclinkFirmwareProvider aclinkFirmwareProvider;
    
    @Autowired
    private AclinkLinglingService aclinkLinglingService;
    
    @Autowired
    private SmsProvider smsProvider;
    
    @Autowired
    private OrganizationProvider organizationProvider;
    
    @Autowired
    private RolePrivilegeService rolePrivilegeService;
    
    @Autowired
    private LocaleTemplateService localeTemplateService;
    
    @Autowired
    private BorderProvider borderProvider;
    
    @Autowired
    private BorderConnectionProvider borderConnectionProvider;
    
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
    private AddressProvider addressProvider;
    
    @Autowired
    private AclinkLogProvider aclinkLogProvider;
    
    @Autowired
    private DoorUserPermissionProvider doorUserPermissionProvider;
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
    private AclinkHuarunService aclinkHuarunService;
    
    @Autowired
    private UserActivityProvider userActivityProvider;
    
    @Autowired
    private LocalBus localBus;
    
    @Autowired
    private LocaleTemplateProvider localeTemplateProvider;
    
    @Autowired
    private DoorAuthLevelProvider doorAuthLevelProvider;
    
    @Autowired
    private NamespaceProvider namespaceProvider;
    
    @Autowired
    private BlacklistService blacklistService;
    
    @Autowired
    private AclinkServerService aclinkServerService;

    @Autowired
    private FaceRecognitionPhotoProvider faceRecognitionPhotoProvider;

    @Autowired
    private FaceRecognitionPhotoService faceRecognitionPhotoService;

    @Autowired
    private AclinkServerProvider aclinkServerProvider;

    @Autowired
    private CommunityService communityService;
    
    @Autowired
    private GroupProvider groupProvider;

    @Autowired
    private ContentServerService contentServerService;
    
    @Autowired
    private LocaleStringService localeStringService;
    
    @Autowired
    private AclinkIpadService aclinkIpadService;
    
    @Autowired
    private AclinkCameraService aclinkCameraService;
    
    @Autowired
    private BusinessService businessService;
    
    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
    private ConfigurationProvider configurationProvider;

    AlipayClient alipayClient;
    
    final Pattern npattern = Pattern.compile("\\d+");
    
    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    
    final static String LAST_TICK = "dooraccess:%d:lasttick";

    final static String DOOR_AUTH_ALL_USER = "doorauth:%d:alluser";

    final static long TASK_TICK_TIMEOUT = 5*60*1000;
    public final static String Manufacturer = "zuolin001";
    private final static long MAX_KEY_ID = 1024;
    private static final long KEY_TICK_15_MINUTE = 15*60*1000l;
//    private static final long KEY_TICK_30_MINUTE = 30*60*1000l;
    private static final long KEY_TICK_ONE_HOUR = 3600*1000l;
    private static final long KEY_TICK_ONE_DAY = KEY_TICK_ONE_HOUR*24l;
    private static final long KEY_TICK_7_DAY = KEY_TICK_ONE_DAY*7;
    private static Format huarunDateFormatter = FastDateFormat.getInstance("yyyy-MM-dd");

//  大沙河项目 旺龙云对接
    private final static String WANGLONG_APP_ID = "647984150393028609";
    private final static String WANGLONG_APP_SECRET = "3A4E0D7F0E5A2C1E";

    private final static String URL_GETAPPINFOS = "http://sdk.api.jia-r.com/projectManage/getAppInfos";
    private final static String URL_GETKEYU = "http://sdk.api.jia-r.com/projectManage/getKeyU";

    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
    //@PostConstruct
    public void setup() {
        Security.addProvider(new BouncyCastleProvider());
        String subcribeKey = DaoHelper.getDaoActionPublishSubject(DaoAction.MODIFY, EhUserIdentifiers.class, null);
        localBus.subscribe(subcribeKey, this);
        String subcribeKey2 = SystemEvent.ACCOUNT_AUTH_SUCCESS.getCode();
        localBus.subscribe(subcribeKey2, new LocalBusSubscriber() {

			@Override
			public Action onLocalBusMessage(Object arg0, String arg1, Object arg2, String arg3) {
				// Must be
				try {
					ExecutorUtil.submit(new Runnable() {
						
						@Override
						public void run() {
							LOGGER.info("start run.....");
							LocalEvent localEvent = (LocalEvent) arg2;
							Long uId = localEvent.getContext().getUid();
							Long orgId = Long.valueOf((String) localEvent.getParams().get("orgId"));
							Integer namespaceId = localEvent.getContext().getNamespaceId();
							if (null == uId) {
								LOGGER.error("None of UserIdentifier");
							} else {
								if (LOGGER.isDebugEnabled()) {
									LOGGER.debug("newUserAutoAuth id= " + uId);
								}

								try {
									joinCompanyAutoAuth(namespaceId, orgId, uId);
								} catch (Exception exx) {
									LOGGER.error("execute promotion error promotionId=" + uId, exx);
								}

							}
						}
					});
				} catch (Exception e) {
					LOGGER.error("onLocalBusMessage error ", e);
				} finally {

				}

				return Action.none;
			}
		});
    }
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            setup();
        }
    }
    
    private AlipayClient getAlipayClient() {
        if(alipayClient == null) {
            synchronized(this) {
                if(alipayClient == null) {
                    //String APP_ID = "2017072507886723";
                    //String APP_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCPhrLtvxkqtQ+30XXwp/7U3xhJJRJJjwGbFRQC2W0UDOVoKqi6bPaL34AP9L2NPhGgq5K5YIMAzt1BBc4OAvml0v8/RLKqUmlTf3iYDnVdua98gLy4tztHco7A8tjy1hqkdQ8kAbFnxsW6pHNL1XByA8itmICHzhMBdNk2/Z87r+n2otr11ebKOYTSfNO9SSsBvZQz0wxDpgoLgbosNWaLxaOPbFtDHAKsNOVttuQfWUMSuGvX9xxu0wi7WxFXvKb8z31szmd3WcXbtCTpPPCx5euI0IuOKGKF4P6NX0iy0TokIrzmt4jvn5xG/Bq5/VvJJS686Kc/bxqXdjtFH0dRAgMBAAECggEAKePupWV5OvXNuKDyA2OFBSx4MiEXzVBn75OfW5WKOKfq7RRGWuMisoBxKDcOfAL5siNhl6mLktjNywSet4g2xSdoSFcMrpmPFEfIMtlFeC2SAoywiFkyfA/7imVW3MmQzR89ZAz6coeZfngxDpklUKG6GLDCEuEauvoXy+0KZKjpkCNywKbwvWnUZaPiLIE9Px8kqy9ZyPMUu79r8DZf080Yg6nQRzxsSjFuik9c5eCD2UuFLV4mIW0e50mwnFtZnOxK++SCydL9KLd5HeA1IN82AObvevG+5LEwCWmDFpIncVBZfVulH1+yxZCePQNN56m871Ba/4uQISbnhZHlYQKBgQDm/EKjog64fPt3xb5abBPJ0w0BoHhKoLof1Qa3AumvDcAyZwtmeWlKWr4WrZ9WeZyz10So7gIoDpmzGIhdIjV/xkb34Sd9m+M41xDTZIPNpUe6h9XoZGAE7o+nY5scWq91nPFW4grPqcRdn1tyP2dOljFEIhD6dX6iJAihiI/aYwKBgQCfEcG3btf40QJIX2WcG/MwI+yWSrVgahw70H76Yvl85ZjsmOBQiACeUbVuxGcdlX8vtAT3XYvETtXdIBW635szvIDrywsKNtJtz8qWgX5Q95MSRhQ4tljGpVezhzdRfu1wHKm0oxEe1xVaot8HKKiJCYWuhokpvRxECA06692LuwKBgE3U5JOEsNcjbgyeuhR35HcWQYSx0La8z9qYCmoydhGBXajeJe5CrOLcDr9Pg6g81DuZJs6RXHKo8MtzUceoFkTWx+UQniDqHTdy6H2CmhL6RWAqEz76S4x94jPyETsNp5/G4V94TVJKDxvI7aRijunhG/qsS/JJEwGJiMr9XBOnAoGADFDBoMQSMI9uD9Bi+40mbOm7HX+3Pzm36eGgkx4qlsLn7hl/9HwzIA7Pbz4BhcbXTAgyAjzZ318DK9WaGRfK2lyT1q2nsyi/bgUSeEiaUQZ5+oY2dpWXlfmjKqEjZUngdDej4/pkDvE0FApcHh/FvKZiFTsRT4v2rkW5UICGbJUCgYAaBHuMw1X4A1Zi0EtVzO7f58lZ8lfwapNbVqdwZsTCzjVMEPK6HefHDmxN1ylRNOJh8wVcpOTYuGNtySHBN5ZDC3KaUnbmwyDfJZoe+esGxbkDhvzEJ4fB/FiE+P6k9sado0Dh2ry2rsKPpf88drscUZhmvPUTaFYN8msjlJF69A==";
                    //String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAphmpO+yCACAtcE724TRl9n1WGKSeXN3iaXGR5s1L72MSS0hZdS6yQaM3ZCYCEUZT0d6awCnovXrNzudzOAZ7pFW+n1WU7tgIg/1n8lv99rgFCzjm8R2qOxeI6j7k3UsS8cUHLsRnj37dr+lQyTHMLGlOS0VBe4EqwfRgq7UQS++zINcwQI05orUuv38mKwR6Eth6BOL4E+1YiAmIS35YX2KawLmeYfnOVWZ9q2l6KKQ9faIVDTdw0C8uX6yYn9ltdiB9sZJJ2Ir/Uxf8q4mk74yRjNc32k+iOg7tBwpqJdvd0ktbdKjKxoNvXKwbZiNQaKw7NuH9ql9d9Kr2gXVYFQIDAQAB";
                    
                    String URL = "https://openapi.alipay.com/gateway.do";
                    String APP_ID = AclinkConstant.ALI_APP_ID;

                    //TODO better for constant
                    String APP_PRIVATE_KEY = AclinkConstant.ALI_APP_PRIVATE_KEY;
                    String FORMAT = "json";
                    String CHARSET = "UTF-8";
                    String ALIPAY_PUBLIC_KEY = AclinkConstant.ALI_APP_PUBLIC_KEY;
                    String SIGN_TYPE = "RSA2";
                    AlipayLogger.setNeedEnableLogger(true);
                    alipayClient = new DefaultAlipayClient(URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);    
                }               
            }
        }
        
        return alipayClient;
    }
    
    private void sendMessageToUser(Long uid, String content, Map<String, String> meta) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_UID);
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), uid.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_ACLINK);
        if(null != meta && meta.size() > 0) {
            messageDto.getMeta().putAll(meta);
            }
        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(), 
                uid.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }
    
    @Override
    public void sendMessageToUser(Long uid, Long doorId, Byte doorType) {
        User user = userProvider.findUserById(uid);
        DoorAccess doorAcc = doorAccessProvider.getDoorAccessById(doorId);
        
        if(user == null || doorAcc == null) {
            return;
        }
        
        sendMessageToUser(user, doorAcc, doorType);
    }
    
    
    private void sendMessageToUser(User user, DoorAccess doorAcc, Byte doorType) {
        String locale = user.getLocale();
        Map<String, Object> map = new HashMap<String, Object>();
        String userName = user.getNickName();
        if(userName == null || userName.isEmpty()) {
            userName = user.getAccountName();
        }
        map.put("userName", userName);
        
        //displayName不为空就拿displayName by liuyilin 20180625
        map.put("doorName", doorAcc.getDisplayNameNotEmpty());
        
        String scope = AclinkNotificationTemplateCode.SCOPE;
        int code = AclinkNotificationTemplateCode.ACLINK_NEW_AUTH;
        String notifyTextForApplicant = localeTemplateService.getLocaleTemplateString(scope, code, locale, map, "");
        
        Map<String, String> meta = new HashMap<String, String>();
        meta.put(MessageMetaConstant.META_OBJECT_TYPE, MetaObjectType.ACLINK_AUTH_CHANGED.getCode());
        meta.put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(new AclinkMessageMeta(user.getId(), doorAcc.getId(), doorType)));
        sendMessageToUser(user.getId(), notifyTextForApplicant, meta);
    }
    
    //列出某园区下的所有锁门禁
    @Override
    public List<DoorAccessDTO> listDoorAccessByOwnerId(CrossShardListingLocator locator, Long ownerId, DoorAccessOwnerType ownerType, int count) {
        List<DoorAccess> dacs = doorAccessProvider.listDoorAccessByOwnerId(locator, ownerId, ownerType, count);
        List<DoorAccessDTO> dtos = new ArrayList<DoorAccessDTO>();
        for(DoorAccess da : dacs) {
            DoorAccessDTO dto = ConvertHelper.convert(da, DoorAccessDTO.class);
            User user = userProvider.findUserById(da.getCreatorUserId());
            String nickName = (user.getNickName() == null ? user.getNickName(): user.getAccountName());
            dto.setCreatorName(nickName);
            if(da.getDisplayName() == null) {
                dto.setDisplayName(da.getName());
            }
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public ListDoorAccessResponse searchDoorAccessByAdmin(QueryDoorAccessAdminCommand cmd) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
//        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        ListDoorAccessResponse resp = new ListDoorAccessResponse();
        
        List<DoorAccessDTO> dtos = doorAccessProvider.searchDoorAccessDTO(locator, cmd);
        if(dtos == null || dtos.size() == 0){
            resp.setDoors(dtos);
        	return resp;
        }
        for(DoorAccessDTO dto : dtos) {
            Long rv = getDoorAccessLastTick(ConvertHelper.convert(dto, DoorAccess.class));
            
            if((rv.longValue()+TASK_TICK_TIMEOUT) > System.currentTimeMillis()) {
            	dto.setLinkStatus(DoorAccessLinkStatus.SUCCESS.getCode());
            } else {
            	dto.setLinkStatus(DoorAccessLinkStatus.FAILED.getCode());
                }
            
            //User user = userProvider.findUserById(da.getCreatorUserId());
            UserInfo user = userService.getUserSnapshotInfoWithPhone(dto.getCreatorUserId());
            if(user != null) {
                String nickName = (user.getNickName() == null ? user.getNickName(): user.getAccountName());
                dto.setCreatorName(nickName);
                String phone = null;
                if(user.getPhones() != null && user.getPhones().size() > 0) {
                    phone = user.getPhones().get(0);
                    }

                dto.setCreatorPhone(phone);
                }
            
            Aclink aclink = aclinkProvider.getAclinkByDoorId(dto.getId());
            if(aclink != null){
            	dto.setVersion(aclink.getFirwareVer());
            }
            
            if(dto.getGroupId() > 0) {
                DoorAccess group = doorAccessProvider.getDoorAccessById(dto.getGroupId());
                if(group != null) {
                    dto.setGroupId(group.getId());
                    dto.setGroupName(group.getDisplayNameNotEmpty());
                }
            }
            
            if(dto.getDisplayName() == null) {
                dto.setDisplayName(dto.getName());
            }
            
            if(dto.getEnableAmount() == null){
            	dto.setEnableAmount((byte) 0);
            }

            if(dto.getLocalServerId() != null && dto.getLocalServerId() != 0L){
            	AclinkServerDTO serverDto = aclinkServerService.findLocalServerById(dto.getLocalServerId());
            	if(serverDto != null ){
            		dto.setServer(serverDto);
            	}
            }

//            dtos.add(dto);
        }
        resp.setDoors(dtos);
        resp.setNextPageAnchor(locator.getAnchor());
        
        return resp;
    }
    
    @Override
    public ListDoorAccessResponse listDoorAccessByOwnerId(ListDoorAccessByOwnerIdCommand cmd) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        ListDoorAccessResponse resp = new ListDoorAccessResponse();
        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        DoorAccessOwnerType typ = DoorAccessOwnerType.fromCode(cmd.getOwnerType()); 
        resp.setDoors(this.listDoorAccessByOwnerId(locator, cmd.getOwnerId(), typ, count));
        resp.setNextPageAnchor(locator.getAnchor());
        return resp;
    }
    
    @Override
    public AclinkUserResponse listAclinkUsers(ListAclinkUserCommand cmd) {
        //添加权限 add by liqingyan
//        if (cmd.getOwnerType() != null && cmd.getOwnerType() == 1 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
//            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4112041121L, cmd.getAppId(), null, cmd.getCurrentProjectId());
//        }
        if (cmd.getOwnerType() != null && cmd.getOwnerType() == 0 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4111041111L, cmd.getAppId(), null, cmd.getCurrentProjectId());
        }
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());

        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        List<User> users = new ArrayList<User>();

        DoorAccess door = doorAccessProvider.getDoorAccessById(cmd.getDoorId());
        Community community = null;
        if(door == null){
    		throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "door is not found");
    	}
    	if(DoorAccessOwnerType.COMMUNITY.getCode() == door.getOwnerType()){//communityId暂时查doorAccess.ownerId,以后还是应该由前端传
    		//小区,运营后台,0
    		community = communityProvider.findCommunityById(door.getOwnerId());
    	}

    	List<GroupMember> groupMembers = new ArrayList<GroupMember>();
        if(!StringUtils.isEmpty(cmd.getKeyword())){
            users = userProvider.listUserByNamespace(cmd.getKeyword(), namespaceId, locator, pageSize);
        }else if(null != cmd.getOrganizationId()){
            users = doorAuthProvider.listDoorAuthByOrganizationId(cmd.getOrganizationId(), cmd.getIsOpenAuth(), cmd.getDoorId(), locator, pageSize);
        } else if(null != cmd.getCommunityId() && !StringUtils.isEmpty(cmd.getBuildingName())) {
            CommunityType communityType = CommunityType.RESIDENTIAL;
            if(cmd.getCommunityType() != null) {
                communityType = CommunityType.fromCode(cmd.getCommunityType());
            }
         users = listDoorAuthByBuildingName(communityType, cmd.getIsOpenAuth(), cmd.getDoorId(), cmd.getCommunityId(), cmd.getBuildingName(), locator, pageSize, namespaceId);    
        } else {
        	if(community != null ){
        		users = doorAuthProvider.listCommunityAclinkUsers(cmd.getIsAuth(), cmd.getIsOpenAuth(), cmd.getDoorId(), community.getCommunityType(), door.getOwnerId(), locator, pageSize, namespaceId);
        		groupMembers = listGroupMemberByCommunityId(community.getId());
        	}else{
        		//community为空,ownerType可能是1或者2,按照以前的方法处理
        		users = doorAuthProvider.listDoorAuthByIsAuth(cmd.getIsAuth(), cmd.getIsOpenAuth(), cmd.getDoorId(), locator, pageSize, namespaceId);
        	}
        }

        List<AclinkUserDTO> userDTOs = new ArrayList<>();

        for (User user: users) {
            List<OrganizationSimpleDTO> organizationDTOs = organizationService.listUserRelateOrgs(null, user);
            AclinkUserDTO dto = ConvertHelper.convert(user, AclinkUserDTO.class);
            dto.setRegisterTime(user.getCreateTime().getTime());
            if(null != organizationDTOs && organizationDTOs.size() > 0){
                OrganizationSimpleDTO organization =  organizationDTOs.get(0);
                dto.setUserName(organization.getContactName());
                dto.setCompany(organization.getName());
                dto.setCompanyId(organization.getId());
                UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
                if(null != identifier){
                    dto.setPhone(identifier.getIdentifierToken());
                }
                dto.setIsAuth((byte)1);
            }else{
                UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
                dto.setIsAuth((byte)0);
                if(null != identifier){
                    dto.setPhone(identifier.getIdentifierToken());
                }
            }
            if(community != null){
            	if(user.getStatus() != null){
                	dto.setIsAuth(user.getStatus());
                }else{
                	if(community.getCommunityType() == 0){
                		dto.setIsAuth(checkResidentialUserIsAuth(user.getId(), groupMembers));
                	}else if(community.getCommunityType() == 1){
                		dto.setIsAuth(checkCommercialUserIsAuth(user.getId(), community.getId()));
                	}else{
                		dto.setIsAuth((byte) 0);
                	}
                }
            }

            DoorAuth doorAuth = doorAuthProvider.queryValidDoorAuthForever(cmd.getDoorId(), dto.getId());
            if(doorAuth != null) {
                dto.setRightOpen(doorAuth.getRightOpen());
                dto.setRightRemote(doorAuth.getRightRemote());
                dto.setRightVisitor(doorAuth.getRightVisitor());
                dto.setAuthId(doorAuth.getId());
                dto.setAuthTime(doorAuth.getCreateTime().getTime());
            } else {
                dto.setRightOpen((byte)0);
                dto.setRightRemote((byte)0);
                dto.setRightVisitor((byte)0);
            }
            if(dto.getIsAuth() != 1){
            	dto.setAuthTime(null);
            }

            //人脸识别照片 目前只传第一张
            List<FaceRecognitionPhoto> photos = faceRecognitionPhotoProvider.listFacialRecognitionPhotoByUser(new CrossShardListingLocator(), user.getId(), 0);
            if(photos != null && photos.size()>0){
            	FaceRecognitionPhoto photo = photos.get(0);
            	FaceRecognitionPhotoDTO photoDTO = ConvertHelper.convert(photos.get(0), FaceRecognitionPhotoDTO.class);
            	if(photo.getSyncTime() != null && photo.getSyncTime().getTime() > photo.getOperateTime().getTime()){
            		photoDTO.setSyncStatus((byte) 1);
            	}
            	dto.setPhoto(photoDTO);
            }
            userDTOs.add(dto);
        }

        AclinkUserResponse resp = new AclinkUserResponse();
        resp.setUsers(userDTOs);
        resp.setNextPageAnchor(locator.getAnchor());
        return resp;
    }
    
    private List<User> listDoorAuthByBuildingName(CommunityType communityType, Byte isOpenAuth, Long doorId, Long communityId, String buildingName, CrossShardListingLocator locator, int pageSize, Integer namespaceId) {
        List<Long> userIds = null;
        if(communityType == CommunityType.COMMERCIAL) {
            userIds = doorAuthProvider.listDoorAuthByBuildingName2(isOpenAuth, doorId, communityId, buildingName, locator, pageSize, namespaceId);    
        } else {
            userIds = doorAuthProvider.listDoorAuthByBuildingName(isOpenAuth, doorId, communityId, buildingName, locator, pageSize, namespaceId);
        }
        
        return userIds.stream().map(u -> {
            User user = userProvider.findUserById(u);
            return user;
        }).collect(Collectors.toList());
    }

    @Override
    public void exportAclinkUsersXls(ListAclinkUserCommand cmd, HttpServletResponse response){
        ByteArrayOutputStream out = null;
        XSSFWorkbook wb = this.createXSSFWorkbook(cmd);
        try {
            out = new ByteArrayOutputStream();
            wb.write(out);
            DownloadUtil.download(out, response);
        } catch (Exception e) {
            LOGGER.error("export error, e = {}", e);
        } finally{
            try {
                wb.close();
                out.close();
            } catch (IOException e) {
                LOGGER.error("close error", e);
            }
        }
    }
//add by liqingyan 日志导出
    @Override
    public void exportAclinkLogsXls(AclinkQueryLogCommand cmd, HttpServletResponse httpResponse){
        ByteArrayOutputStream output = null;
        XSSFWorkbook wb = this.createLogsXSSFWorkbook(cmd);
        try {
            output = new ByteArrayOutputStream();
            wb.write(output);
            DownloadUtil.download(output, httpResponse);
        } catch (Exception e) {
            LOGGER.error("export error, e = {}", e);
        } finally{
            try {
                wb.close();
                output.close();
            } catch (IOException e) {
                LOGGER.error("close error", e);
            }
        }
    }



    /**
     * 根据小区id查找groupMemberId
     */
    private List<GroupMember> listGroupMemberByCommunityId(Long communityId){
    	List<Group> groups = groupProvider.listGroupByCommunityId(communityId, (loc, query) -> {
            Condition c = Tables.EH_GROUPS.STATUS.eq(GroupAdminStatus.ACTIVE.getCode());
            query.addConditions(c);
            return query;
        });
		List<Long> groupIds = new ArrayList<Long>();
		for (Group group : groups) {
			groupIds.add(group.getId());
		}
		return groupProvider.listGroupMemberByGroupIds(groupIds,new CrossShardListingLocator(),null,(loc, query) -> {
			Condition c = Tables.EH_GROUP_MEMBERS.MEMBER_TYPE.eq(EntityType.USER.getCode());
			c = c.and(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.eq(GroupMemberStatus.ACTIVE.getCode()));
            query.addConditions(c);
			query.addOrderBy(Tables.EH_GROUP_MEMBERS.MEMBER_ID.desc());
			query.addOrderBy(Tables.EH_GROUP_MEMBERS.MEMBER_STATUS.desc());
			query.addGroupBy(Tables.EH_GROUP_MEMBERS.MEMBER_ID);
            return query;
        });
    }

    /**
	 * 查看住宅小区用户是否已认证,需要与用户管理一致
	 * @return 0 未认证 1 已认证
	 */
    private Byte checkResidentialUserIsAuth(Long userId, List<GroupMember> groupMembers){
    	if(groupMembers != null && groupMembers.size() > 0){
    		for(GroupMember member : groupMembers){
    			if(member.getMemberId().equals(userId)){
    				return 1;
    			}
    		}
    	}
    	return 0;
    }

    /**
	 * 查看商业小区用户是否已认证,需要与用户管理一致
	 * @return 0 未认证 1 已认证
	 */
	private Byte checkCommercialUserIsAuth(Long userId, Long communityId) {
		List<OrganizationMember> members = organizationProvider.listOrganizationMembers(userId);
		if (null != members) {
			for (OrganizationMember member : members) {
				if (OrganizationMemberStatus.ACTIVE == OrganizationMemberStatus.fromCode(member.getStatus())
						&& OrganizationGroupType.ENTERPRISE == OrganizationGroupType.fromCode(member.getGroupType())) {
					return 1;
				}
			}
		}
		return 0;
	}

    /**
     * 创建excel
     * @param cmd
     * @return
     */
    private XSSFWorkbook createXSSFWorkbook(ListAclinkUserCommand cmd){
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());

        XSSFWorkbook wb = new XSSFWorkbook();
        String sheetName = "门禁授权用户列表";
        XSSFSheet sheet = wb.createSheet(sheetName);
        XSSFCellStyle style = wb.createCellStyle();// 样式对象
        Font font = wb.createFont();
        font.setFontHeightInPoints((short)20);
        font.setFontName("Courier New");

        style.setFont(font);

        XSSFCellStyle titleStyle = wb.createCellStyle();// 样式对象
        titleStyle.setFont(font);
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        int rowNum = 0;

        XSSFRow row1 = sheet.createRow(rowNum ++);
        row1.setRowStyle(style);
        int cellN = 0;
        row1.createCell(cellN ++).setCellValue("用户昵称");
        row1.createCell(cellN ++).setCellValue("手机号");
        row1.createCell(cellN ++).setCellValue("注册时间");
        row1.createCell(cellN ++).setCellValue("认证状态");
        row1.createCell(cellN ++).setCellValue("开门权限");
        if(namespaceId != 999990){
            row1.createCell(cellN ++).setCellValue("访客授权权限");
            row1.createCell(cellN ++).setCellValue("远程开门权限");
        }
        row1.createCell(cellN ++).setCellValue("真实姓名");
        row1.createCell(cellN ++).setCellValue("公司");
        row1.createCell(cellN ++).setCellValue("认证时间");

        cmd.setPageSize(1000);
        while (true){
            AclinkUserResponse userRes = this.listAclinkUsers(cmd);
            for (AclinkUserDTO user: userRes.getUsers()) {
                cellN = 0;
                XSSFRow row = sheet.createRow(rowNum ++);
                row.setRowStyle(style);
                row.createCell(cellN ++).setCellValue(user.getNickName());
                row.createCell(cellN ++).setCellValue(user.getPhone());
                row.createCell(cellN ++).setCellValue(DateUtil.dateToStr(new Date(user.getRegisterTime()), DateUtil.DATE_TIME_LINE));
                row.createCell(cellN ++).setCellValue(user.getIsAuth() > 0 ? "已认证" : "未认证");
                row.createCell(cellN ++).setCellValue(user.getRightOpen() > 0 ? "已授权" : "未授权");
                if(namespaceId != 999990){
                    row.createCell(cellN ++).setCellValue(user.getRightVisitor() > 0 ? "已授权" : "未授权");
                    row.createCell(cellN ++).setCellValue(user.getRightRemote() > 0 ? "已授权" : "未授权");
                }
                row.createCell(cellN ++).setCellValue(user.getUserName());
                row.createCell(cellN ++).setCellValue(user.getCompany());
                row.createCell(cellN ++).setCellValue(null != user.getAuthTime() ? DateUtil.dateToStr(new Date(user.getRegisterTime()), DateUtil.DATE_TIME_LINE) : "");
            }

            if(null == userRes.getNextPageAnchor()){
                break;
            }
            cmd.setPageAnchor(userRes.getNextPageAnchor());
        }

        return wb;
    }
//add by liqingyan 待翻译
    /**
     * 创建导出日志excel
     * @param cmd
     * @return
     */
    private XSSFWorkbook createLogsXSSFWorkbook(AclinkQueryLogCommand cmd){
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());

        XSSFWorkbook wb = new XSSFWorkbook();
        String sheetName = "门禁日志列表";
        XSSFSheet sheet = wb.createSheet(sheetName);
        XSSFCellStyle style = wb.createCellStyle();// 样式对象
        Font font = wb.createFont();
        font.setFontHeightInPoints((short)20);
        font.setFontName("Courier New");

        style.setFont(font);

        XSSFCellStyle titleStyle = wb.createCellStyle();// 样式对象
        titleStyle.setFont(font);
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);

        int rowNum = 0;

        XSSFRow row1 = sheet.createRow(rowNum ++);
        row1.setRowStyle(style);
        int cellN = 0;
        row1.createCell(cellN ++).setCellValue("姓名");
        row1.createCell(cellN ++).setCellValue("手机号码");
        row1.createCell(cellN ++).setCellValue("门禁名称");
        row1.createCell(cellN ++).setCellValue("授权类型");
        row1.createCell(cellN ++).setCellValue("开门方式");
        row1.createCell(cellN ++).setCellValue("开门时间");

        cmd.setPageSize(1000);
        while (true){
            AclinkQueryLogResponse userRes = this.queryLogs(cmd);
            for (AclinkLogDTO user: userRes.getDtos()) {
                cellN = 0;
                XSSFRow row = sheet.createRow(rowNum ++);
                row.setRowStyle(style);
                row.createCell(cellN ++).setCellValue(user.getUserName());
                row.createCell(cellN ++).setCellValue(user.getUserIdentifier());
                row.createCell(cellN ++).setCellValue(user.getDoorName());
                row.createCell(cellN ++).setCellValue(user.getAuthType() > 0 ? "常规授权" : "临时授权");
                row.createCell(cellN ++).setCellValue(user.getEventType().equals(0L) ? "蓝牙开门": equals(1L)? "二维码开门": equals(2L)? "远程开门":equals(3L)? "人脸开门":"");
                row.createCell(cellN ++).setCellValue(DateUtil.dateToStr(new Date(user.getLogTime()), DateUtil.DATE_TIME_LINE));
            }

            if(null == userRes.getNextPageAnchor()){
                break;
            }
            cmd.setPageAnchor(userRes.getNextPageAnchor());
        }

        return wb;
    }

    @Override
    public DoorAuthStatisticsDTO qryDoorAuthStatistics(QryDoorAuthStatisticsCommand cmd){
        DoorAuthStatisticsDTO dto = new DoorAuthStatisticsDTO();
        Integer namespaceId = UserContext.getCurrentNamespaceId();
        DoorAccess door = doorAccessProvider.getDoorAccessById(cmd.getDoorId());
        if(door == null){
        	throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE,AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "门禁不存在或已删除");
        }
        if(door.getOwnerType() == 0){
        	//小区,认证状态需要与用户认证一致;公司未认证且不在园区中的用户,不作统计
        	Community community = communityProvider.findCommunityById(door.getOwnerId());
        	Long utn = doorAuthProvider.countCommunityDoorAuthUser(null, null, cmd.getDoorId(), community.getId(), community.getCommunityType(), namespaceId, cmd.getRightType());
            Long autn = doorAuthProvider.countCommunityDoorAuthUser(null, (byte)1, cmd.getDoorId(), community.getId(), community.getCommunityType(), namespaceId, cmd.getRightType());
            Long uautn = doorAuthProvider.countCommunityDoorAuthUser(null, (byte)0, cmd.getDoorId(), community.getId(), community.getCommunityType(), namespaceId, cmd.getRightType());
            Long acutn = doorAuthProvider.countCommunityDoorAuthUser((byte)1, (byte)1, cmd.getDoorId(), community.getId(), community.getCommunityType(), namespaceId, cmd.getRightType());
            Long aucutn = doorAuthProvider.countCommunityDoorAuthUser((byte)0, (byte)1, cmd.getDoorId(), community.getId(), community.getCommunityType(), namespaceId, cmd.getRightType());
            Long uacutn = doorAuthProvider.countCommunityDoorAuthUser((byte)1, (byte)0, cmd.getDoorId(), community.getId(), community.getCommunityType(), namespaceId, cmd.getRightType());
            Long uaucutn = doorAuthProvider.countCommunityDoorAuthUser((byte)0, (byte)0, cmd.getDoorId(), community.getId(), community.getCommunityType(), namespaceId, cmd.getRightType());
            dto.setUtn(utn);
            dto.setAutn(autn);
            dto.setUautn(uautn);
            dto.setAcutn(acutn);
            dto.setAucutn(aucutn);
            dto.setUacutn(uacutn);
            dto.setUaucutn(uaucutn);
        }else{
        	//企业或家庭,按以前方式统计
        	Long utn = doorAuthProvider.countDoorAuthUser(null, null, cmd.getDoorId(), namespaceId, cmd.getRightType());
            Long autn = doorAuthProvider.countDoorAuthUser(null, (byte)1, cmd.getDoorId(), namespaceId, cmd.getRightType());
            Long uautn = doorAuthProvider.countDoorAuthUser(null, (byte)0, cmd.getDoorId(), namespaceId, cmd.getRightType());
            Long acutn = doorAuthProvider.countDoorAuthUser((byte)1, (byte)1, cmd.getDoorId(), namespaceId, cmd.getRightType());
            Long aucutn = doorAuthProvider.countDoorAuthUser((byte)0, (byte)1, cmd.getDoorId(), namespaceId, cmd.getRightType());
            Long uacutn = doorAuthProvider.countDoorAuthUser((byte)1, (byte)0, cmd.getDoorId(), namespaceId, cmd.getRightType());
            Long uaucutn = doorAuthProvider.countDoorAuthUser((byte)0, (byte)0, cmd.getDoorId(), namespaceId, cmd.getRightType());
            dto.setUtn(utn);
            dto.setAutn(autn);
            dto.setUautn(uautn);
            dto.setAcutn(acutn);
            dto.setAucutn(aucutn);
            dto.setUacutn(uacutn);
            dto.setUaucutn(uaucutn);
        }
        return dto;
    }

    @Override
    public ListDoorAuthLogResponse listDoorAuthLogs(ListDoorAuthLogCommand cmd){
        ListDoorAuthLogResponse res = new ListDoorAuthLogResponse();
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<DoorAuthLogDTO> dtos = new ArrayList<>();
        List<DoorAuthLog> logs = doorAuthProvider.listDoorAuthLogsByUserId(locator, pageSize, cmd.getUserId(), cmd.getDoorId());
        for (DoorAuthLog log: logs) {
            DoorAuthLogDTO dto = ConvertHelper.convert(log, DoorAuthLogDTO.class);
            dto.setCreateTime(log.getCreateTime().getTime());
            User user = userProvider.findUserById(log.getCreateUid());
            if(null != user){
                dto.setCreateUname(user.getNickName());
                UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
                if(null != identifier){
                    dto.setCreateUtoken(identifier.getIdentifierToken());
                }
            }
            dtos.add(dto);
        }
        res.setDtos(dtos);
        res.setNextPageAnchor(locator.getAnchor());
        return res;
    }

    /**
     * <ul> 授权的静态数据，不同授权对应的真实钥匙需要客户端来取的时候产生。
     * <li></li>
     * </ul>
     */
    @Override
    public DoorAuthDTO createDoorAuth(CreateDoorAuthCommand cmd) {
    	User operator = UserContext.current().getUser();
        if(cmd.getApproveUserId() == null) {
            cmd.setApproveUserId(operator.getId());//操作人
        }
        
        if(cmd.getRightOpen() == null) {
            cmd.setRightOpen((byte)0);
        }
        if(cmd.getRightRemote() == null) {
            cmd.setRightRemote((byte)0);
        }
        if(cmd.getRightVisitor() == null) {
            cmd.setRightVisitor((byte)0);
        }
        
        DoorAccess doorAcc = doorAccessProvider.getDoorAccessById(cmd.getDoorId());
        UserInfo custom = userService.getUserSnapshotInfoWithPhone(cmd.getUserId());//被授权人
        
        if(custom == null) {
        	throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "the input user is not found");
        }
        
        if(cmd.getOperatorOrgId() == null) {
            List<OrganizationDTO> dtos = organizationService.listUserRelateOrganizations(UserContext.getCurrentNamespaceId(), cmd.getApproveUserId(), OrganizationGroupType.ENTERPRISE);
            if(dtos != null && dtos.size() > 0) {
                OrganizationDTO orgDTO = dtos.get(0);
                cmd.setOperatorOrgId(orgDTO.getId());
            }
        }
        
        if(doorAcc.getDoorType().equals(DoorAccessType.ACLINK_HUARUN_GROUP.getCode())) {
            OrganizationDTO orgDTO = null;
            List<OrganizationDTO> dtos = organizationService.listUserRelateOrganizations(UserContext.getCurrentNamespaceId(), cmd.getUserId(), OrganizationGroupType.ENTERPRISE);
            if(dtos != null && dtos.size() > 0) {
                orgDTO = dtos.get(0);
            }
            
            if(orgDTO == null) {
                throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "the input user haven't companies");   
                }
        	
        	OrganizationMember om = organizationProvider.findOrganizationMemberByUIdAndOrgId(custom.getId(), orgDTO.getId());
			if(om != null && om.getContactName() != null && !om.getContactName().isEmpty()) {
				custom.setNickName(om.getContactName());
			}
			
        	AclinkHuarunSyncUser syncUser = new AclinkHuarunSyncUser();
        	syncUser.setPhone(custom.getPhones().get(0));
        	syncUser.setUsername(custom.getNickName());
        	syncUser.setOrganization(om.getOrganizationName());
        	AclinkHuarunSyncUserResp resp = aclinkHuarunService.syncUser(syncUser);
        	if(resp.getStatus() < 0) {
        		throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "huarun response error " + resp.getDescription());
        	}
        }

        if(doorAcc.getDoorType().equals(DoorAccessType.ACLINK_WANGLONG_GROUP.getCode())){
            JSONObject data = JSON.parseObject(doorAcc.getFloorId());
            String projectId = data.getString("building");
//            JSONArray floors = data.getJSONArray("floors");
//            for(int i = 0;i < floors.size();i++){
//                JSONObject floor = floors.getJSONObject(i);
//                String floorId = floor.getString("id");
//            }
            cmd.setKeyU(this.getKeyU(projectId,cmd.getUserId().toString()));
        }


        DoorAuth doorAuth = ConvertHelper.convert(cmd, DoorAuth.class);
        DoorAuthDTO rlt = null;
        
        if(doorAuth.getAuthType().equals(DoorAuthType.FOREVER.getCode())) {
            DoorAuth doorAuthResult = this.dbProvider.execute(new TransactionCallback<DoorAuth>() {
                @Override
                public DoorAuth doInTransaction(TransactionStatus arg0) {
                    DoorAuth doorAuth = doorAuthProvider.queryValidDoorAuthForever(doorAcc.getId(), custom.getId());
                    if(doorAuth != null) {
                        doorAuth.setRightOpen(cmd.getRightOpen());
                        doorAuth.setRightRemote(cmd.getRightRemote());
                        doorAuth.setRightVisitor(cmd.getRightVisitor());
                        if(null != cmd.getKeyU()){
                            doorAuth.setKeyU(cmd.getKeyU());
                        }
                        doorAuthProvider.updateDoorAuth(doorAuth);
                        //by liuyilin 20180502 同步信息到内网服务器
                        if(doorAcc.getLocalServerId() != null){
                        	SyncLocalPhotoByUserIdCommand syncCmd = new SyncLocalPhotoByUserIdCommand();
                    		syncCmd.setOwnerId(doorAcc.getOwnerId());
                    		syncCmd.setOwnerType(doorAcc.getOwnerType());
                    		syncCmd.setUserId(doorAuth.getUserId());
                    		faceRecognitionPhotoService.syncLocalPhotoByUserId(syncCmd);
                        }
                		//---
                        return doorAuth;
                        }
                    
                    doorAuth = new DoorAuth();
               
                    doorAuth.setRightOpen(cmd.getRightOpen());
                    doorAuth.setRightRemote(cmd.getRightRemote());
                    doorAuth.setRightVisitor(cmd.getRightVisitor());
                    
                    doorAuth.setDoorId(doorAcc.getId());
                    doorAuth.setAuthType(DoorAuthType.FOREVER.getCode());
                    doorAuth.setOwnerId(doorAcc.getOwnerId());
                    doorAuth.setOwnerType(doorAcc.getOwnerType());
                    doorAuth.setApproveUserId(cmd.getApproveUserId());
                    doorAuth.setUserId(custom.getId());
                    doorAuth.setStatus(DoorAuthStatus.VALID.getCode());
                    doorAuth.setOrganization(cmd.getOrganization());
                    doorAuth.setDescription(cmd.getDescription());
                    doorAuth.setKeyU(cmd.getKeyU());

                    //Set the auth driver type
                    if(doorAcc.getDoorType().equals(DoorAccessType.ACLINK_LINGLING.getCode())
                            || doorAcc.getDoorType().equals(DoorAccessType.ACLINK_LINGLING_GROUP.getCode())) {
                        doorAuth.setDriver(DoorAccessDriverType.LINGLING.getCode());
                    } else if(doorAcc.getDoorType().equals(DoorAccessType.ACLINK_UCLBRT_DOOR.getCode())) {
                		doorAuth.setDriver(DoorAccessDriverType.UCLBRT.getCode());
	                } else if(doorAcc.getDoorType().equals(DoorAccessType.ACLINK_HUARUN_GROUP.getCode())) {
	            		doorAuth.setDriver(DoorAccessDriverType.HUARUN_ANGUAN.getCode());
	                }else if(doorAcc.getDoorType().equals(DoorAccessType.ACLINK_WANGLONG_GROUP.getCode())){
                        doorAuth.setDriver(DoorAccessDriverType.WANG_LONG.getCode());
                    }else if(doorAcc.getDoorType().equals(DoorAccessType.ACLINK_BUS.getCode())){
                    	doorAuth.setDriver(DoorAccessDriverType.BUS.getCode());
                    }else {
                        doorAuth.setDriver(DoorAccessDriverType.ZUOLIN.getCode());
                    }
                    
                    if(custom.getPhones() != null && custom.getPhones().size() > 0) {
                        doorAuth.setPhone(custom.getPhones().get(0));    
                        }
                    
                    doorAuthProvider.createDoorAuth(doorAuth);
                    //by liuyilin 20180502 同步信息到内网服务器
                    if(doorAcc.getLocalServerId() != null){
                    	SyncLocalPhotoByUserIdCommand syncCmd = new SyncLocalPhotoByUserIdCommand();
                		syncCmd.setOwnerId(doorAcc.getOwnerId());
                		syncCmd.setOwnerType(doorAcc.getOwnerType());
                		syncCmd.setUserId(doorAuth.getUserId());
                		faceRecognitionPhotoService.syncLocalPhotoByUserId(syncCmd);
                    }
            		//---
                    return doorAuth;
                    }
                });
            rlt = ConvertHelper.convert(doorAuthResult, DoorAuthDTO.class);
            rlt.setDoorName(doorAcc.getDisplayNameNotEmpty());
        } else {
            if(cmd.getValidEndMs() == null || cmd.getValidFromMs() == null) {
                throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "Invalid param error");        
            }
            
            doorAuth.setRightOpen((byte)1);
            doorAuth.setApproveUserId(cmd.getApproveUserId());
            doorAuth.setOwnerId(doorAcc.getOwnerId());
            doorAuth.setOwnerType(doorAcc.getOwnerType());
            doorAuth.setOrganization(cmd.getOrganization());
            doorAuth.setDescription(cmd.getDescription());
            doorAuth.setStatus(DoorAuthStatus.VALID.getCode());
            
            //Set the auth driver type
            if(doorAcc.getDoorType().equals(DoorAccessType.ACLINK_LINGLING.getCode())
                    || doorAcc.getDoorType().equals(DoorAccessType.ACLINK_LINGLING_GROUP.getCode())) {
                doorAuth.setDriver(DoorAccessDriverType.LINGLING.getCode());  
            } else if(doorAcc.getDoorType().equals(DoorAccessType.ACLINK_HUARUN_GROUP.getCode())) {
        			doorAuth.setDriver(DoorAccessDriverType.HUARUN_ANGUAN.getCode());
            } else {
                doorAuth.setDriver(DoorAccessDriverType.ZUOLIN.getCode());
            }
            
            if(custom.getPhones() != null && custom.getPhones().size() > 0) {
                doorAuth.setPhone(custom.getPhones().get(0));    
                }
            
            User tmpUser = ConvertHelper.convert(custom, User.class);
            doorAuth.setNickname(getRealName(tmpUser));
            
            doorAuthProvider.createDoorAuth(doorAuth);
            rlt = ConvertHelper.convert(doorAuth, DoorAuthDTO.class);
            rlt.setDoorName(doorAcc.getDisplayNameNotEmpty());
            rlt.setHardwareId(doorAcc.getHardwareId());
        }
        

        // 记录操作日志
        this.createDoorAuthLog(doorAuth);
        
        if(cmd.getRightOpen().equals((byte)0) && cmd.getRightRemote().equals((byte)0) && cmd.getRightVisitor().equals((byte)0)) {
            //如果所有的权限都没有，则不发消息
            return rlt;
        }

        User tmpUser = new User();
        tmpUser.setNickName("admin");
        if (operator != null && operator.getId() != 0) {
            tmpUser.setNickName(getRealName(operator));
            if(cmd.getOperatorOrgId() != null) {
                OrganizationMember om = organizationProvider.findOrganizationMemberByUIdAndOrgId(operator.getId(), cmd.getOperatorOrgId());
                if(om != null && om.getContactName() != null && !om.getContactName().isEmpty()) {
                    tmpUser.setNickName(om.getContactName());
                }
            }
        }

        tmpUser.setId(custom.getId());
        tmpUser.setAccountName(custom.getAccountName());
        //Send messages
        if(doorAcc.getDoorType().equals(DoorAccessType.ACLINK_LINGLING.getCode())
                || (doorAcc.getDoorType().equals(DoorAccessType.ACLINK_LINGLING_GROUP.getCode()))) {
            sendMessageToUser(tmpUser, doorAcc, DoorAccessType.ACLINK_LINGLING_GROUP.getCode()); 
        } else if(doorAcc.getDoorType().equals(DoorAccessType.ACLINK_HUARUN_GROUP.getCode())) {
        		sendMessageToUser(tmpUser, doorAcc, DoorAccessType.ACLINK_HUARUN_GROUP.getCode());
        } else {
            sendMessageToUser(tmpUser, doorAcc, DoorAccessType.ZLACLINK_WIFI.getCode());    
        }

        return rlt;
    }

    private void createDoorAuthLog(DoorAuth doorAuth){
        DoorAuthLog log = new DoorAuthLog();
        log.setDoorId(doorAuth.getDoorId());
        log.setUserId(doorAuth.getUserId());
        log.setRightContent(doorAuth.getRightOpen() + "," + doorAuth.getRightVisitor() + "," + doorAuth.getRightRemote());
        if(UserContext.current().getUser() != null) {
        	log.setCreateUid(UserContext.current().getUser().getId());	
        } else {
        	log.setCreateUid(doorAuth.getUserId());
        }
        
        String discription = "";
        if(doorAuth.getRightOpen() > 0){
            discription += "授权开门权限";
        }else{
            discription += "<font color=\"red\">取消授权开门权限</font>";
        }

        if(doorAuth.getRightVisitor() > 0){
            discription += "<br>授权访客授权权限";
        }else{
            discription += "<br><font color=\"red\">取消授权访客授权权限</font>";
        }

        if(doorAuth.getRightRemote() > 0){
            discription += "<br>授权远程开门权限";
        }else{
            discription += "<br><font color=\"red\">取消授权远程开门权限</font>";
        }

        log.setDiscription(discription);
        doorAuthProvider.createDoorAuthLog(log);
    }
    
    private void createDoorAuthLogBatch(List<DoorAuth> doorAuths){
    	List<DoorAuthLog> logs = new ArrayList<DoorAuthLog>();
    	if(doorAuths != null && doorAuths.size() > 0){
    		for(DoorAuth doorAuth : doorAuths){
    			DoorAuthLog log = new DoorAuthLog();
    			log.setDoorId(doorAuth.getDoorId());
    			log.setUserId(doorAuth.getUserId());
    			log.setRightContent(doorAuth.getRightOpen() + "," + doorAuth.getRightVisitor() + "," + doorAuth.getRightRemote());
    			if(UserContext.current().getUser() != null) {
    				log.setCreateUid(UserContext.current().getUser().getId());	
    			} else {
    				log.setCreateUid(doorAuth.getUserId());
    			}
    			
    			String discription = "";
    			if(doorAuth.getRightOpen() > 0){
    				discription += "授权开门权限";
    			}else{
    				discription += "<font color=\"red\">取消授权开门权限</font>";
    			}
    			
    			if(doorAuth.getRightVisitor() > 0){
    				discription += "<br>授权访客授权权限";
    			}else{
    				discription += "<br><font color=\"red\">取消授权访客授权权限</font>";
    			}
    			
    			if(doorAuth.getRightRemote() > 0){
    				discription += "<br>授权远程开门权限";
    			}else{
    				discription += "<br><font color=\"red\">取消授权远程开门权限</font>";
    			}
    			log.setDiscription(discription);
    			logs.add(log);
    		}
    	}
        doorAuthProvider.createDoorAuthLogBatch(logs);
    }

    
    @Override
    public DoorAuthDTO createDoorAuth(CreateDoorAuthByUser cmd2) {
        User user = UserContext.current().getUser();
        
        User targetUser = userService.findUserByIndentifier(cmd2.getNamespaceId(), cmd2.getPhone());
        if (targetUser == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_NOT_FOUND, "User not found");
        }
        cmd2.setPhone(null);
        cmd2.setNamespaceId(null);
        
        CreateDoorAuthCommand cmd = ConvertHelper.convert(cmd2, CreateDoorAuthCommand.class);
        cmd.setApproveUserId(user.getId());
        cmd.setUserId(targetUser.getId());
        QueryValidDoorAuthForeverCommand qryCmd = ConvertHelper.convert(getLevelQryCmdByUser(user), QueryValidDoorAuthForeverCommand.class);
        qryCmd.setDoorId(cmd.getDoorId());
        DoorAuth auth = doorAuthProvider.queryValidDoorAuthForever(qryCmd);
        if(auth == null || (!auth.getRightVisitor().equals((byte)1))) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, "User not auth");   
        }
        
        //创建临时授权
        cmd.setAuthType(DoorAuthType.TEMPERATE.getCode());
        return createDoorAuth(cmd);
    }
    
    @Override
    public Long deleteDoorAccess(Long doorAccessId) {
        DoorAccess doorAccess = this.dbProvider.execute(new TransactionCallback<DoorAccess>() {
            @Override
            public DoorAccess doInTransaction(TransactionStatus arg0) {
                DoorAccess doorAcc = doorAccessProvider.getDoorAccessById(doorAccessId);
                
                //delete childs
                List<DoorAccess> childs = doorAccessProvider.listDoorAccessByGroupId(doorAccessId, 20);
                if(childs != null && childs.size() > 0) {
                    for(DoorAccess dc : childs) {
                        dc.setStatus(DoorAccessStatus.INVALID.getCode());
                        doorAccessProvider.updateDoorAccess(doorAcc);
                    }
                }
                
                if(doorAcc != null) {
                    doorAcc.setStatus(DoorAccessStatus.INVALID.getCode());
                    doorAccessProvider.updateDoorAccess(doorAcc);
                    return doorAcc;
                }
                
                return null;
            }
        });
        
        if(doorAccess == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "Door not found");
        }
        
        return doorAccess.getId();
    }
    
    @Override
    public Long deleteDoorAuth(Long doorAuthId) {
        this.dbProvider.execute(new TransactionCallback<DoorCommand>() {
            @Override
            public DoorCommand doInTransaction(TransactionStatus arg0) {
                DoorAuth doorAuth = doorAuthProvider.getDoorAuthById(doorAuthId);
                if(doorAuth != null) {
                    AesUserKey aesUserKey1 = aesUserKeyProvider.queryAesUserKeyByAuthId(doorAuth.getDoorId(), doorAuth.getId());
                    
                    AesUserKey aesUserKey2 = aesUserKeyProvider.queryAesUserKeyByDoorId(doorAuth.getDoorId(), doorAuth.getUserId(), doorAuthId);
                    
                    if(doorAuth.getAuthType().equals(DoorAuthType.FOREVER.getCode())) {
                        //Remove right open
                        doorAuth.setRightOpen((byte)0);
                    } else {
                        doorAuth.setStatus(DoorAuthStatus.INVALID.getCode());    
                    }
                    
                    doorAuthProvider.updateDoorAuth(doorAuth);
                    
                    //通知内网服务器同步授权信息(目前是访客,用户与访客的同步待合并)
                    FaceRecognitionPhoto photo = faceRecognitionPhotoProvider.findPhotoByAuthId(doorAuth.getId());
                    if(photo != null){
                    	photo.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                    	faceRecognitionPhotoProvider.updateFacialRecognitionPhoto(photo);
                    	NotifySyncVistorsCommand cmd1 = new NotifySyncVistorsCommand();
                        cmd1.setDoorId(doorAuth.getDoorId());
                        faceRecognitionPhotoService.notifySyncVistorsCommand(cmd1);
                    }
                    
                    if(aesUserKey1 != null) {
                        aesUserKey1.setStatus(AesUserKeyStatus.INVALID.getCode());
                        aesUserKeyProvider.updateAesUserKey(aesUserKey1);    
                    }
                    
                    
                    if(aesUserKey1 == null || aesUserKey2 != null) {
                        return null;
                    }
                    
                    AesServerKey aesServerKey = aesServerKeyService.getCurrentAesServerKey(doorAuth.getDoorId());
                    if(aesServerKey == null) {
                        return null;
                    }
                    
                    if(aesUserKey1.getExpireTimeMs() < System.currentTimeMillis()) {
                    return null;
                    }
                    
                    //generate a DoorCommand, and will send to Device next time
                    DoorCommand cmd = new DoorCommand();
                    cmd.setUserId(doorAuth.getUserId());
                    cmd.setDoorId(doorAuth.getDoorId());
                    cmd.setOwnerId(doorAuth.getOwnerId());
                    cmd.setOwnerType(doorAuth.getOwnerType());
                    cmd.setCmdId(AclinkCommandType.ADD_UNDO_LIST.getCode());
                    cmd.setCmdType((byte)0);
                    cmd.setServerKeyVer(aesServerKey.getSecretVer());
                    cmd.setAclinkKeyVer(aesServerKey.getDeviceVer());
                    cmd.setStatus(DoorCommandStatus.CREATING.getCode());
                    
//                    cmd.setCmdBody(AclinkUtils.packAddUndoList(aesServerKey.getDeviceVer(), aesServerKey.getSecret()
//                            , (int)(aesUserKey1.getExpireTimeMs().longValue()/1000), aesUserKey1.getKeyId().shortValue()));
                    cmd.setCmdBody(aesUserKey1.getId().toString());
                    
                    doorCommandProvider.createDoorCommand(cmd);
                    
                    return cmd;
                    }
                
                return null;
            }
        });
        
        return null;
    }
    
    //获取最新需要更新的数据，包括用户最新的钥匙DoorUserKey，以前锁与服务器交互的钥匙 DoorServerKey。同时可以对上次更新的消息进行确认。
    //urgent 为 true, 则拿最紧急的消息列表。更新到设备之后再尝试开门或其它事情。
    @Override
    public QueryDoorMessageResponse queryDoorMessageByDoorId(QueryDoorMessageCommand cmd) {
        doorCommandProvider.getDoorCommandById(0l);
        DoorAccess doorAccess = doorAccessProvider.queryDoorAccessByHardwareId(cmd.getHardwareId());
        if(doorAccess == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "Door not found");
        }
        
        processIncomeMessageResp(cmd.getInputs());
        QueryDoorMessageResponse resp = new QueryDoorMessageResponse();
        resp.setDoorId(doorAccess.getId());
        resp.setOutputs(generateMessages(doorAccess.getId()));
        return resp;
    }
    
    @Override
    public QueryDoorMessageResponse syncTimerMessage(AclinkSyncTimerCommand cmd) {
    	if(LOGGER.isInfoEnabled()) {
    		LOGGER.info("syncTimer cmd=" + cmd);
    	}
        DoorAccess doorAccess = doorAccessProvider.queryDoorAccessByHardwareId(cmd.getHardwareId());
        if(doorAccess == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "Door not found");
        }
        
    	QueryDoorMessageCommand queryCmd = new QueryDoorMessageCommand();
    	queryCmd.setHardwareId(cmd.getHardwareId());
    	msgGenerator.invalidSyncTimer(doorAccess.getId());
    	
    	return queryDoorMessageByDoorId(queryCmd);
    }
    
    public void processIncomeMessageResp(List<DoorMessage> inputs) {
        if(inputs == null) {
            return;
        }
        
        for (DoorMessage resp : inputs) {
            if(resp.getSeq() <= 0) {
                continue;
                }
            
            aclinkMessageSequence.ackMessage(resp.getSeq());
            DoorCommand doorCommand = doorCommandProvider.getDoorCommandById(resp.getSeq());
            doorCommand.setStatus(DoorCommandStatus.RESPONSE.getCode());
            doorCommandProvider.updateDoorCommand(doorCommand);
            
            //doorCommand.getCmdId()
        } 
    }
    
    @Override
    public DoorMessage activatingDoorAccess(DoorAccessActivingCommand cmd) {        
        User user = UserContext.current().getUser();
        cmd.setHardwareId(cmd.getHardwareId().toUpperCase());
        if(cmd.getDisplayName() == null || cmd.getDisplayName().isEmpty()) {
            cmd.setDisplayName(cmd.getName());
        }
        if(cmd.getDisplayName() == null || cmd.getDisplayName().isEmpty()) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "aclink param error");
        }
        if(cmd.getName() == null || cmd.getName().isEmpty()) {
            String displayName = cmd.getDisplayName();
            String pinyin = PinYinHelper.getPinYin(displayName);
            String[] pinyins = pinyin.split("\\s+");
            pinyin = String.join("", pinyins);
            if(pinyin.length() > 6) {
                pinyin = pinyin.substring(0, 6);
            }
            cmd.setName(pinyin);
        }
        
//        DoorAccess doorAccess = doorAccessProvider.queryDoorAccessByHardwareId(cmd.getHardwareId());
//        if(doorAccess != null && !doorAccess.getStatus().equals(DoorAccessStatus.ACTIVING.getCode())) {
//            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_EXISTS, "DoorAccess exists");
//        }
        
        DoorAccess doorGroup = null;
        if(cmd.getGroupId() != null && (!cmd.getGroupId().equals(0l))) {
            doorGroup = doorAccessProvider.getDoorAccessById(cmd.getGroupId());
            if(doorGroup == null || doorGroup.getStatus().equals(DoorAccessStatus.INVALID.getCode())) {
                throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "Door group not found");
            }
        } else {
            cmd.setGroupId(null);
        }
        final DoorAccess dg = doorGroup;
        
        DoorAccess doorAccess = this.dbProvider.execute(new TransactionCallback<DoorAccess>() {
            @Override
            public DoorAccess doInTransaction(TransactionStatus arg0) {
            	AesServerKey aesServerKey = null;
                DoorAccess doorAcc = doorAccessProvider.queryDoorAccessByHardwareId(cmd.getHardwareId());
                if(doorAcc != null) {
                	if(doorAcc.getStatus().equals(DoorAccessStatus.ACTIVING.getCode())) {
                		return doorAcc;
                	}
                		aesServerKey = aesServerKeyService.getCurrentAesServerKey(doorAcc.getId());
                		doorAcc.setStatus(DoorAccessStatus.INVALID.getCode());
                		doorAccessProvider.updateDoorAccess(doorAcc);
                    }
                
                String aesKey = "";
                if(dg != null) {
                	aesKey = dg.getAesIv();
                	}
                if( (aesKey == null || aesKey.isEmpty()) && aesServerKey != null) {
                	aesKey = aesServerKey.getSecret();
                	}
                if(aesKey == null || aesKey.isEmpty()) {
                	aesKey = AclinkUtils.generateAESKey();
                	}
                
                String aesIv = AclinkUtils.generateAESIV(aesKey);
                
                doorAcc = new DoorAccess();
                doorAcc.setActiveUserId(user.getId());
                doorAcc.setOwnerId(cmd.getOwnerId());
                doorAcc.setOwnerType(cmd.getOwnerType());
                doorAcc.setStatus(DoorAccessStatus.ACTIVING.getCode());
                doorAcc.setDoorType(DoorAccessType.ZLACLINK_WIFI.getCode());
                doorAcc.setHardwareId(cmd.getHardwareId());
                doorAcc.setName(cmd.getName());
                doorAcc.setDescription(cmd.getDescription());
                doorAcc.setAddress(cmd.getAddress());
                doorAcc.setCreatorUserId(user.getId());
                doorAcc.setAesIv(aesIv);
                doorAcc.setGroupid(cmd.getGroupId());
                doorAcc.setDisplayName(cmd.getDisplayName());
                doorAcc.setNamespaceId(UserContext.getCurrentNamespaceId());
                doorAccessProvider.createDoorAccess(doorAcc);
                
                OwnerDoor ownerDoor = new OwnerDoor();
                ownerDoor.setDoorId(doorAcc.getId());
                ownerDoor.setOwnerType(cmd.getOwnerType());
                ownerDoor.setOwnerId(cmd.getOwnerId());
                try {
                    ownerDoorProvider.createOwnerDoor(ownerDoor);
                } catch(Exception ex) {
                    LOGGER.error("createOwnerDoor failed ", ex);
                    return null;
                    }
                
                Aclink aclink = new Aclink();
                aclink.setFirwareVer(cmd.getFirwareVer());
                aclink.setManufacturer(Manufacturer);
                //TODO set active as default
                aclink.setStatus(DoorAccessStatus.ACTIVE.getCode());
                aclink.setDriver(DoorAccessDriverType.ZUOLIN.getCode());
                aclink.setDoorId(doorAcc.getId());
                aclink.setDeviceName(cmd.getName());
                aclinkProvider.createAclink(aclink);
                
                aesServerKey = new AesServerKey();
                aesServerKey.setCreateTimeMs(System.currentTimeMillis());
                aesServerKey.setDoorId(doorAcc.getId());
                aesServerKey.setSecret(aesKey);
                aesServerKey.setDeviceVer(AclinkDeviceVer.VER0.getCode());
                aesServerKeyService.createAesServerKey(aesServerKey);
                
                //create a command event
                DoorCommand cmd = new DoorCommand();
                cmd.setDoorId(doorAcc.getId());
                cmd.setOwnerId(cmd.getOwnerId());
                cmd.setOwnerType(cmd.getOwnerType());
                cmd.setCmdId(AclinkCommandType.INIT_SERVER_KEY.getCode());
                cmd.setCmdType((byte)0);//TODO use enum
                cmd.setServerKeyVer(1l);//Default for AesServerKey
                cmd.setAclinkKeyVer(AclinkDeviceVer.VER0.getCode());
                cmd.setStatus(DoorCommandStatus.SENDING.getCode());
                doorCommandProvider.createDoorCommand(cmd);
                return doorAcc;
            }
        });
        
        if(doorAccess == null || (!doorAccess.getStatus().equals(DoorAccessStatus.ACTIVING.getCode()))) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_ACTIVING_FAILED, "Door Activing failed");
        }
        
        DoorMessage doorMessage = new DoorMessage();
        
        //Generate a single message
        AesServerKey aesServerKey = aesServerKeyService.getCurrentAesServerKey(doorAccess.getId());
        String message = AclinkUtils.packInitServerKey(cmd.getRsaAclinkPub(), aesServerKey.getSecret(), doorAccess.getAesIv(), cmd.getName(),
        		new Date().getTime(), doorAccess.getUuid(), doorMessage);
        
        doorMessage.setDoorId(doorAccess.getId());
        doorMessage.setMessageType(DoorMessageType.NORMAL.getCode());
        AclinkMessage acMsg = new AclinkMessage();
        acMsg.setCmd(AclinkCommandType.INIT_SERVER_KEY.getCode());
        acMsg.setEncrypted(message);
        acMsg.setSecretVersion(aesServerKey.getDeviceVer());
        doorMessage.setBody(acMsg);
        
        return doorMessage;
    }
    
    //激活一个新锁
    @Override
    public QueryDoorMessageResponse activateDoorAccess(DoorAccessActivedCommand cmd) {
        User user = UserContext.current().getUser();
        cmd.setHardwareId(cmd.getHardwareId().toUpperCase());
        
        DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(cmd.getDoorId());
        if(doorAccess != null && doorAccess.getStatus().equals(DoorAccessStatus.ACTIVING.getCode())
                && doorAccess.getCreatorUserId().equals(user.getId())
                && cmd.getHardwareId().equals(doorAccess.getHardwareId())) {
            
            this.dbProvider.execute(new TransactionCallback<DoorCommand>() {
                @Override
                public DoorCommand doInTransaction(TransactionStatus arg0) {
                    doorAccess.setStatus(DoorAccessStatus.ACTIVE.getCode());
                    doorAccessProvider.updateDoorAccess(doorAccess);
                    
                    DoorCommand doorCommand = doorCommandProvider.queryActiveDoorCommand(cmd.getDoorId());
                    if(doorCommand != null) {
                    	doorCommand.setStatus(DoorCommandStatus.PROCESS.getCode());
                     doorCommandProvider.updateDoorCommand(doorCommand);
                     }
                    
                    //Create auth
                    DoorAuth doorAuth = new DoorAuth();
                    doorAuth.setApproveUserId(user.getId());
                    doorAuth.setAuthType(DoorAuthType.TEMPERATE.getCode());
                    doorAuth.setDoorId(cmd.getDoorId());
                    doorAuth.setDescription("");
                    doorAuth.setNickname(user.getNickName());
                    doorAuth.setOwnerId(doorAccess.getOwnerId());
                    doorAuth.setOwnerType(doorAccess.getOwnerType());
                    doorAuth.setValidFromMs(System.currentTimeMillis() -  60*1000);
                    doorAuth.setValidEndMs(System.currentTimeMillis()+ getQrTimeout());
                    doorAuth.setUserId(user.getId());
                    doorAuth.setStatus(DoorAuthStatus.VALID.getCode());
                    UserIdentifier ui = userProvider.findIdentifierById(user.getId());
                    if(ui != null) {
                        doorAuth.setPhone(ui.getIdentifierToken());
                        }
                    doorAuthProvider.createDoorAuth(doorAuth);
                    
                    AesServerKey aesServerKey = aesServerKeyService.getCurrentAesServerKey(doorAccess.getId());
                    
                    AesUserKey aesUserKey = new AesUserKey();
                    aesUserKey.setId(aesUserKeyProvider.prepareForAesUserKeyId());
                    aesUserKey.setKeyId(new Integer((int) (aesUserKey.getId().intValue() % MAX_KEY_ID)));
                    aesUserKey.setAuthId(doorAuth.getId());
                    aesUserKey.setUserId(doorAuth.getUserId());
                    aesUserKey.setCreatorUid(user.getId());
                    aesUserKey.setDoorId(doorAccess.getId());
                    if(doorAuth.getAuthType().equals(DoorAuthType.FOREVER.getCode())) {
                        aesUserKey.setExpireTimeMs(System.currentTimeMillis() + getQrTimeout());
                        aesUserKey.setKeyType(AesUserKeyType.NORMAL.getCode());
                    } else {
                        aesUserKey.setExpireTimeMs(doorAuth.getValidEndMs());
                        aesUserKey.setKeyType(AesUserKeyType.TEMP.getCode());
                            }
                    aesUserKey.setStatus(AesUserKeyStatus.VALID.getCode());
                    aesUserKey.setSecret(AclinkUtils.packAesUserKey(aesServerKey.getSecret(), doorAuth.getUserId(), aesUserKey.getKeyId(), doorAuth.getValidEndMs()));
                    aesUserKeyProvider.createAesUserKey(aesUserKey);
                    
                    //create device name command
//                    doorCommand = new DoorCommand();
//                    doorCommand.setDoorId(doorAccess.getId());
//                    doorCommand.setOwnerId(doorAccess.getOwnerId());
//                    doorCommand.setOwnerType(doorAccess.getOwnerType());
//                    doorCommand.setCmdId(AclinkCommandType.CMD_UPDATE_DEVNAME.getCode());
//                    doorCommand.setCmdType((byte)0);
//                    doorCommand.setServerKeyVer(aesServerKey.getSecretVer());
//                    doorCommand.setAclinkKeyVer(aesServerKey.getDeviceVer());
//                    doorCommand.setStatus(DoorCommandStatus.CREATING.getCode());
                    
                    //Generate a message body for command
//                    doorCommand.setCmdBody(AclinkUtils.packUpdateDeviceName(aesServerKey.getDeviceVer(), aesServerKey.getSecret()
//                            , doorAccess.getAesIv(), doorAccess.getName()));
                    
//                    doorCommandProvider.createDoorCommand(doorCommand);
//                    return doorCommand;
                    return null;
                }
            });
            
        } else {
            if(doorAccess == null) {
                throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "Door Activing failed");    
            }
            
            if(!doorAccess.getStatus().equals(DoorAccessStatus.ACTIVING.getCode())){
                throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_STATE_ERROR, "State error");
            }
            
           throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "Invalid param error");
        }
        
        QueryDoorMessageResponse resp = new QueryDoorMessageResponse();
        resp.setDoorId(cmd.getDoorId());
        resp.setOutputs(generateMessages(cmd.getDoorId()));
        return resp;
    }
    
    //更新锁的详细信息
    @Override
    public void updateDoorAccess(DoorAccessAdminUpdateCommand cmd){
        DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(cmd.getId());
        if(doorAccess == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "DoorAccess not found");
        }
        
        if(cmd.getName() != null) {
            //不再可以修改门禁名称
            if(cmd.getDisplayName() == null) {
                cmd.setDisplayName(cmd.getName());
            }
            cmd.setName(null);
        }
        
        DoorCommand doorCommand = null;
        if(cmd.getName() != null && (!cmd.getName().equals(doorAccess.getName()))) {
            doorAccess.setName(cmd.getName());
            
            //create device name command
            AesServerKey aesServerKey = aesServerKeyService.getCurrentAesServerKey(doorAccess.getId());
            if(aesServerKey != null) {
                doorCommand = new DoorCommand();
                doorCommand.setDoorId(doorAccess.getId());
                doorCommand.setOwnerId(doorAccess.getOwnerId());
                doorCommand.setOwnerType(doorAccess.getOwnerType());
                doorCommand.setCmdId(AclinkCommandType.CMD_UPDATE_DEVNAME.getCode());
                doorCommand.setCmdType((byte)0);
                doorCommand.setServerKeyVer(aesServerKey.getSecretVer());
                doorCommand.setAclinkKeyVer(aesServerKey.getDeviceVer());
                doorCommand.setStatus(DoorCommandStatus.CREATING.getCode());
                //Generate a message body for command
//              doorCommand.setCmdBody(AclinkUtils.packUpdateDeviceName(aesServerKey.getDeviceVer(), aesServerKey.getSecret()
//                      , doorAccess.getAesIv(), doorAccess.getName()));
            }
        }
        if(cmd.getAddress() != null) {
            doorAccess.setAddress(cmd.getAddress());
        }
        if(cmd.getDescription() != null) {
            doorAccess.setDescription(cmd.getDescription());
        }
        if(cmd.getDisplayName() != null && !cmd.getDisplayName().isEmpty() ) {
            doorAccess.setDisplayName(cmd.getDisplayName());
        }
		if (cmd.getEnableAmount() != null) {
			if (cmd.getEnableAmount() == 1) {
				if (doorAccess.getGroupid() != 0 || doorAccess.getDoorType().byteValue() > DoorAccessType.ACLINK_ZL_GROUP.getCode()) {
					throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "not a doorAccessGroup or not a zuolin divice");
				}
			}
        	doorAccess.setEnableAmount(cmd.getEnableAmount());
        }
		if(cmd.getEnableDuration() != null){
			doorAccess.setEnableDuration(cmd.getEnableDuration());
		}

		if(cmd.getServerId() != null){
			if(cmd.getServerId() != 0L){
				if(aclinkServerService.findLocalServerById(cmd.getServerId()) != null){
					doorAccess.setLocalServerId(cmd.getServerId());
				}else{
					throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "关联服务器不存在");
				}
			}else{
				doorAccess.setLocalServerId(0L);
			}
			
		}

        doorAccess.setLatitude(cmd.getLatitude());
        doorAccess.setLongitude(cmd.getLongitude());
        doorAccess.setHasQr(cmd.getHasQr());
        doorAccess.setMaxCount(cmd.getMaxCount());
        doorAccess.setMaxDuration(cmd.getMaxDuration());
        doorAccess.setDefualtInvalidDuration(cmd.getDefualtInvalidDuration());
        doorAccessProvider.updateDoorAccess(doorAccess);
        if(doorCommand != null) {
            doorCommandProvider.createDoorCommand(doorCommand);
        }
    }
    
    //刷新并返回一个新的 DoorServerKey
    public AesServerKey refreshDoorServerKey() {
        return null;
    }
    
    public List<DoorMessage> generateMessages(Long doorId) {
        List<DoorMessage> msgs = msgGenerator.generateMessages(doorId);
        for(DoorMessage dm : msgs) {
            messageSequence.pendingMessage(dm);
        }
        
        return msgs;
    }
    
    @Override
    public void onDoorMessageTimeout(Long cmdId) {
        //message timeout here
        LOGGER.info("timeout for cmdId=", cmdId);
    }
    
        //TODO cache AesUserKey
    List<AesUserKey> listAesUserKeyByUser(User user, ListingLocator locator, Integer count) {
        if(locator == null || locator.getAnchor() == null || locator.getAnchor() == 0){
        	locator = new ListingLocator();
        }
        if(count == null ||count == 0){
        	count = 60;
        }
        
        ListAuthsByLevelandLocationCommand qryCmd = getLevelQryCmdByUser(user);
        qryCmd.setDriver(DoorAccessDriverType.ZUOLIN.getCode());
        List<DoorAuth> auths = uniqueAuths(doorAuthProvider.queryDoorAuthForeverByUserId(locator, qryCmd, null, count));
        
        //TODO when the key is invalid, MUST invalid it and generate a command.
        
        List<AesUserKey> aesUserKeys = new ArrayList<AesUserKey>();
        for(DoorAuth auth : auths) {
            
            if(auth.getAuthType().equals(DoorAuthType.FOREVER.getCode()) && auth.getRightOpen().equals((byte)0)) {
                //Ignore the key which right open is 0
                continue;
            }
            
            AesUserKey aesUserKey = generateAesUserKey(user, auth);
            if(aesUserKey != null) {
                aesUserKeys.add(aesUserKey);    
                }
            }
        
        return aesUserKeys;        
    }
    
    @Override
    public  List<AesUserKey> listAesUserKeyByUserId(Long userId) {
        User user = userProvider.findUserById(userId);
        if(user != null) {
            return listAesUserKeyByUser(user, null, null);
        }
        
        return new ArrayList<AesUserKey>();
        
    }
    
    //list all AesUserKeys for current login user
    @Override
    public ListAesUserKeyByUserResponse listAesUserKeyByUser(ListAesUserKeyByUserCommand cmd) {
    	ListAesUserKeyByUserResponse resp = new ListAesUserKeyByUserResponse();
    	User user = UserContext.current().getUser();
		
    	ListAuthsByLevelandLocationCommand qryCmd = getLevelQryCmdByUser(user);
		qryCmd.setDriver(DoorAccessDriverType.ZUOLIN.getCode());        
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        //避免现网客户端反复取数据,锚点传0就取全部,不返回下一页锚点,待下一版客户端修复后删掉即可  by liuyilin 20180608
        if(cmd.getPageAnchor() != null && cmd.getPageAnchor() == 0){
        	cmd.setPageSize(0);
        }
        //end 20180608

        //1.临时授权也要用蓝牙
        //2.listAesUserKey这个接口只用支持DoorAccessDriverType是左邻的门禁
        //by liuyilin 20180906
        List<DoorAuth> auths = uniqueAuths(doorAuthProvider.queryValidDoorAuthByUserId(locator, qryCmd, cmd.getPageSize() != null && cmd.getPageSize() >0 ?cmd.getPageSize() + 1 : 0));
        //TODO when the key is invalid, MUST invalid it and generate a command.
        List<AesUserKey> aesUserKeys = new ArrayList<AesUserKey>();
        for(DoorAuth auth : auths) {
            AesUserKey aesUserKey = generateAesUserKey(user, auth);
            if(aesUserKey != null) {
                aesUserKeys.add(aesUserKey);    
                }
            }
        
        List<AesUserKeyDTO> dtos = new ArrayList<AesUserKeyDTO>();
        for(AesUserKey key : aesUserKeys) {
            AesUserKeyDTO dto = ConvertHelper.convert(key, AesUserKeyDTO.class);
            DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(dto.getDoorId());
            if(doorAccess != null) {
				dto.setHardwareId(doorAccess.getHardwareId());
				//---clone DoorAccess 20180408 by liuyilin
            	if(doorAccess.getMacCopy() != null && !doorAccess.getMacCopy().isEmpty()){
            		dto.setHardwareId(doorAccess.getMacCopy());
            	}
            	//---
                dto.setDoorName(doorAccess.getDisplayNameNotEmpty());
                dtos.add(dto);    
            }
        }
        Collections.sort(dtos);
        if(cmd.getPageSize() != null && cmd.getPageSize() > 0 && dtos.size() > cmd.getPageSize()){
        	for(DoorAuth auth : auths) {
        		if(dtos.get(dtos.size() - 1).getAuthId().equals(auth.getId())){
        			locator.setAnchor(auth.getCreateTime().getTime());
        		}
                }
        	dtos.remove(dtos.size() - 1);
        }
        resp.setNextPageAnchor(locator.getAnchor());
        resp.setAesUserKeys(dtos);
        
        return resp;
    }
    
    @Override
    //Not used
    public ListAesUserKeyByUserResponse listAdminAesUserKeyByUser() {
        User user = UserContext.current().getUser();
        
        ListAesUserKeyByUserResponse resp = new ListAesUserKeyByUserResponse();
        List<AesUserKey> aesUserKeys = aesUserKeyProvider.queryAdminAesUserKeyByUserId(user.getId(), 20);
        List<AesUserKeyDTO> dtos = new ArrayList<AesUserKeyDTO>();
        for(AesUserKey key : aesUserKeys) {
            AesUserKeyDTO dto = ConvertHelper.convert(key, AesUserKeyDTO.class);
            DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(dto.getDoorId());
            if(doorAccess != null && (!doorAccess.getStatus().equals(DoorAccessStatus.INVALID.getCode()))) {
                dto.setHardwareId(doorAccess.getHardwareId());
                dto.setDoorName(doorAccess.getDisplayNameNotEmpty());
                dtos.add(dto);    
            }
        }
        
        resp.setAesUserKeys(dtos);
        
        return resp;
        
    }
    
    @Override
    public ListAesUserKeyByUserResponse listAdminAesUserKeyByUserAuth(ListAdminAesUserKeyCommand cmd) {
        User user = UserContext.current().getUser();
        
		ListAuthsByLevelandLocationCommand qryCmd = getLevelQryCmdByUser(user);
        cmd.setPageSize(cmd.getPageSize()== null? 0 : cmd.getPageSize());
        ListAesUserKeyByUserResponse resp = new ListAesUserKeyByUserResponse();
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<DoorAuth> auths = doorAuthProvider.queryDoorAuthForeverByUserId(locator, qryCmd, cmd.getRightRemote(), cmd.getPageSize());

        List<AesUserKeyDTO> dtos = new ArrayList<AesUserKeyDTO>();
        for(DoorAuth auth : auths) {
            AesUserKeyDTO dto = new AesUserKeyDTO();
            
            if(auth.getAuthType().equals(DoorAuthType.FOREVER.getCode())) {
            
            	if(auth.getRightOpen().equals((byte)0)) {
            		//Not has right
                	continue;
               } else if(auth.getRightVisitor().equals((byte)1)) {
                        //有访客授权权限
                    dto.setKeyType(AesUserKeyType.ADMIN.getCode());   
               } else {
                    	//普通用户权限
            	   dto.setKeyType(AesUserKeyType.NORMAL.getCode());
                 	}
            	
            } else {
                dto.setKeyType(AesUserKeyType.TEMP.getCode());
                }
            
            dto.setCreateTimeMs(auth.getCreateTime().getTime());
            dto.setCreatorUid(user.getId());
            dto.setDoorId(auth.getDoorId());
            dto.setUserId(auth.getUserId());
            dto.setStatus(AesUserKeyStatus.VALID.getCode());
            dto.setId(auth.getId());
            dto.setAuthId(auth.getId());
            if(auth.getRightRemote() == 1){
            	dto.setRightRemote((byte) 1);
            }

            DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(dto.getDoorId());
            if(doorAccess != null && (!doorAccess.getStatus().equals(DoorAccessStatus.INVALID.getCode()))) {
                dto.setHardwareId(doorAccess.getHardwareId());
                dto.setDoorName(doorAccess.getDisplayNameNotEmpty());
                if(doorAccess.getLocalServerId() != null){
                	if(faceRecognitionPhotoProvider.listFacialRecognitionPhotoByUser(new CrossShardListingLocator(), user.getId(), 0).size() > 0){
                		dto.setRightFaceOpen((byte) 1);
                	}
                }
                dtos.add(dto);
            }
        }
        
        if(cmd.getPageSize() > 0 && dtos.size() > cmd.getPageSize()){
        	resp.setNextPageAnchor(dtos.get(dtos.size() - 1).getCreateTimeMs());
        	dtos.remove(dtos.size() - 1);
        }
        resp.setNextPageAnchor(locator.getAnchor());
        resp.setAesUserKeys(dtos);
        
        return resp;        
    }
    
    private List<DoorAuth> uniqueAuths(List<DoorAuth> auths) {
        Map<String, DoorAuth> map = new HashMap<String, DoorAuth>();
        for(int i = auths.size()-1; i >= 0; i--) {
            DoorAuth auth = auths.get(i);
            String key = "" + auth.getAuthType() + ":" + auth.getDoorId() + ":" + auth.getUserId() + ":" + String.valueOf(auth.getLicenseeType() == null? 0 : auth.getLicenseeType());
            if(map.containsKey(key)) {
                continue;
            } else {
                map.put(key, auth);    
                }
        }
        
        List<DoorAuth> newAuths = new ArrayList<DoorAuth>(); 
        for(Map.Entry<String, DoorAuth> kset : map.entrySet()) {
            newAuths.add(kset.getValue());
        }
        
        return newAuths;
    }
    
    private AesUserKey genarateQrUserKey(User user, DoorAuth doorAuth) {
        DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(doorAuth.getDoorId());
        if(!doorAccess.getStatus().equals(DoorAccessStatus.ACTIVE.getCode())) {
            //The door is delete, set it to invalid
            doorAuth.setStatus(DoorAuthStatus.INVALID.getCode());
            doorAuthProvider.updateDoorAuth(doorAuth);
            return null;
        }
        
    	AesServerKey aesServerKey = aesServerKeyService.getCurrentAesServerKey(doorAccess.getId());
    	if(aesServerKey == null) {
    	    return null;
    	}
    	AesUserKey aesUserKey = new AesUserKey();
		aesUserKey.setId(aesUserKeyProvider.prepareForAesUserKeyId());
		aesUserKey.setKeyId(new Integer((int) (aesUserKey.getId().intValue() % MAX_KEY_ID)));
		aesUserKey.setCreatorUid(user.getId());
		aesUserKey.setUserId(doorAuth.getUserId());
		aesUserKey.setDoorId(doorAccess.getId());
		aesUserKey.setAuthId(doorAuth.getId());
		if(doorAuth.getAuthType().equals(DoorAuthType.FOREVER.getCode())) {
		    aesUserKey.setExpireTimeMs(System.currentTimeMillis() + getQrTimeout());//origin is KEY_TICK_7_DAY
		    aesUserKey.setKeyType(AesUserKeyType.NORMAL.getCode());
		} else {
		    aesUserKey.setExpireTimeMs(doorAuth.getValidEndMs());
		    aesUserKey.setKeyType(AesUserKeyType.TEMP.getCode());
		    }
		
		aesUserKey.setStatus(AesUserKeyStatus.VALID.getCode());
		aesUserKey.setKeyType(AesUserKeyType.NORMAL.getCode());
    	aesUserKey.setSecret(AclinkUtils.packAesUserKey(aesServerKey.getSecret(), doorAuth.getUserId(), aesUserKey.getKeyId(), aesUserKey.getExpireTimeMs()));
    	return aesUserKey;
    }
    
    private AesUserKey generateAesUserKey(User user, DoorAuth doorAuth) {
        DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(doorAuth.getDoorId());
        if(!doorAccess.getStatus().equals(DoorAccessStatus.ACTIVE.getCode())) {
            //The door is delete, set it to invalid
            doorAuth.setStatus(DoorAuthStatus.INVALID.getCode());
            doorAuthProvider.updateDoorAuth(doorAuth);
            
            //throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "DoorAccess not found");
            return null;
        }
        
        AesUserKey aesUserKey = this.dbProvider.execute(new TransactionCallback<AesUserKey>() {

            @Override
            public AesUserKey doInTransaction(TransactionStatus arg0) {
                //Find it if it's already created
                AesUserKey aesUserKey = null;
                Long userId = 1L;//表示访客二维码
                if(!doorAuth.getUserId().equals(0L)) {
                    //Fix for visitor, always create new aes user key
                    aesUserKey = aesUserKeyProvider.queryAesUserKeyByDoorId(doorAuth.getDoorId(), doorAuth.getUserId());
                    userId = doorAuth.getUserId();
                }
                
                if(aesUserKey == null) {
                    AesServerKey aesServerKey = aesServerKeyService.getCurrentAesServerKey(doorAccess.getId());
                    aesUserKey = new AesUserKey();
                    aesUserKey.setId(aesUserKeyProvider.prepareForAesUserKeyId());
                    aesUserKey.setKeyId(new Integer((int) (aesUserKey.getId().intValue() % MAX_KEY_ID)));
                    aesUserKey.setCreatorUid(user.getId());
                    aesUserKey.setUserId(doorAuth.getUserId());
                    aesUserKey.setDoorId(doorAccess.getId());
                    aesUserKey.setAuthId(doorAuth.getId());
                    if(doorAuth.getAuthType().equals(DoorAuthType.FOREVER.getCode())) {
                        aesUserKey.setExpireTimeMs(System.currentTimeMillis() + KEY_TICK_7_DAY);
                        aesUserKey.setKeyType(AesUserKeyType.NORMAL.getCode());
                    } else {
                        aesUserKey.setExpireTimeMs(doorAuth.getValidEndMs());
                        aesUserKey.setKeyType(AesUserKeyType.TEMP.getCode());
                        }
                    
                    aesUserKey.setStatus(AesUserKeyStatus.VALID.getCode());
                    aesUserKey.setKeyType(AesUserKeyType.NORMAL.getCode());
                    aesUserKey.setSecret(AclinkUtils.packAesUserKey(aesServerKey.getSecret(), userId, aesUserKey.getKeyId(), aesUserKey.getExpireTimeMs()));
                    aesUserKeyProvider.createAesUserKey(aesUserKey);
                    }
                return aesUserKey;
            }
            
        });
        
        if(aesUserKey.getSecret().isEmpty()) {
            //TODO log here
            return null;
        }
        
        return aesUserKey;
    }
    
    private boolean checkDoorAccessRole(DoorAccess da) {
        DoorAccessOwnerType t = DoorAccessOwnerType.fromCode(da.getOwnerType());
        Long orgId = -1l;
        
        switch(t) {
        case ENTERPRISE:
            orgId = da.getOwnerId();
            try {
                rolePrivilegeService.checkAuthority(EntityType.ORGANIZATIONS.getCode(), orgId, PrivilegeConstants.AclinkInnerManager);
                return true;
            } catch(Exception ex) {
            }
            break;
        case COMMUNITY:
            List<OrganizationCommunity> orgs = organizationProvider.listOrganizationByCommunityId(da.getOwnerId());
            if(orgs == null || orgs.size() == 0) {
                return false;
            }
            orgId = orgs.get(0).getId();
            try {
                rolePrivilegeService.checkAuthority(EntityType.ORGANIZATIONS.getCode(), orgId, PrivilegeConstants.AclinkManager);
                return true;
            } catch(Exception ex) {
            }
            break;
        case FAMILY:
            return true;
        }
        
        return false;
    }
    
    @Override
    public DoorAccessDTO getDoorAccessDetail(String hardware) {
        DoorAccess da = doorAccessProvider.queryDoorAccessByHardwareId(hardware);
        if(da == null) {
            return  null;
            }
        
        getDoorAccessLastTick(da);
        DoorAccessDTO dto = (DoorAccessDTO)ConvertHelper.convert(da, DoorAccessDTO.class);
        
        if(checkDoorAccessRole(da)) {
            dto.setRole((byte)1);    
        } else {
            dto.setRole((byte)0);
        }
        
        if(dto.getDisplayName() == null) {
            dto.setDisplayName(dto.getName());
        }
        
        dto.setVersion("1.1.0.0");
        
        AclinkFirmware firm = aclinkFirmwareProvider.queryAclinkFirmwareMax();
        if(firm != null) {
            String version = String.format("1.%d.%d.%d", firm.getMajor(), firm.getMinor(), firm.getRevision());
            dto.setVersion(version);
        }
        
        return dto;
    }
    
    @Override
    public ListDoorAuthResponse queryDoorAuthByApproveId(ListDoorAuthCommand cmd) {
        User user = UserContext.current().getUser();
        
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        ListDoorAuthResponse resp = new ListDoorAuthResponse();
        
        List<DoorAuth> auths = doorAuthProvider.queryDoorAuthByApproveId(locator, user.getId(), count);
        List<DoorAuthDTO> dtos = new ArrayList<DoorAuthDTO>();
        List<DoorAuth> invalidAuths = new ArrayList<DoorAuth>();
        for(DoorAuth auth : auths) {
        	//issue-30615 授权过了有效期则置为失效 by liuyilin 20180529
        	long now = DateHelper.currentGMTTime().getTime();
            if(auth.getValidEndMs() < now){
            	auth.setStatus(DoorAuthStatus.INVALID.getCode());
            	invalidAuths.add(auth);
            }
            DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(auth.getDoorId());
            DoorAuthDTO dto = ConvertHelper.convert(auth, DoorAuthDTO.class);
            dto.setHardwareId(doorAccess.getHardwareId());
            dto.setDoorName(doorAccess.getDisplayNameNotEmpty());
//            User u = userProvider.findUserById(auth.getUserId());
//            if(u != null) {
//                if(u.getNickName() != null) {
//                    dto.setUserNickName(u.getNickName());
//                } else {
//                    dto.setUserNickName(u.getAccountName());
//                }
//            }
            dtos.add(dto);
        }
        //issue-30615 授权过了有效期则置为失效 by liuyilin 20180529
        doorAuthProvider.updateDoorAuth(invalidAuths);
        resp.setDtos(dtos);
        resp.setNextPageAnchor(locator.getAnchor());
        return resp;
    }
    
    @Override
    public ListDoorAuthResponse searchDoorAuth(SearchDoorAuthCommand cmd) {
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        ListDoorAuthResponse resp = new ListDoorAuthResponse();
        
        List<DoorAuth> auths = doorAuthProvider.searchDoorAuthByAdmin(locator, cmd.getDoorId(), cmd.getKeyword(), cmd.getStatus(), count);
        List<DoorAuthDTO> dtos = new ArrayList<DoorAuthDTO>();
        for(DoorAuth auth : auths) {
            DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(auth.getDoorId());
            DoorAuthDTO dto = ConvertHelper.convert(auth, DoorAuthDTO.class);
            dto.setHardwareId(doorAccess.getHardwareId());
            dto.setDoorName(doorAccess.getDisplayNameNotEmpty());
            User u = userProvider.findUserById(auth.getApproveUserId());
            if(u != null) {
                dto.setApproveUserName(getRealName(u));
            }
            dtos.add(dto);
        }
        
        resp.setDtos(dtos);
        resp.setNextPageAnchor(locator.getAnchor());
        return resp;        
    }
    
    @Override
    public ListDoorAuthResponse searchVisitorDoorAuth(SearchDoorAuthCommand cmd) {
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        ListDoorAuthResponse resp = new ListDoorAuthResponse();
        
        List<DoorAuth> auths = doorAuthProvider.searchVisitorDoorAuthByAdmin(locator, cmd, count);
        List<DoorAuthDTO> dtos = new ArrayList<DoorAuthDTO>();
        long now = DateHelper.currentGMTTime().getTime();
        for(DoorAuth auth : auths) {
            DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(auth.getDoorId());
            DoorAuthDTO dto = ConvertHelper.convert(auth, DoorAuthDTO.class);
            dto.setGoFloor(auth.getCurrStorey());
            dto.setHardwareId(doorAccess.getHardwareId());
            dto.setDoorName(doorAccess.getDisplayNameNotEmpty());
            dto.setPhone(auth.getPhone());
            dto.setOrganization(auth.getOrganization());
            User u = userProvider.findUserById(auth.getApproveUserId());
            if(u != null) {
                dto.setApproveUserName(getRealName(u));
            }
            
            if(auth.getValidEndMs() < now) {
            	auth.setStatus(DoorAuthStatus.INVALID.getCode());
            }
            
            dtos.add(dto);
        }
        
        resp.setDtos(dtos);
        resp.setNextPageAnchor(locator.getAnchor());
        return resp;        
    }

    @Override
    public ListDoorAuthResponse listTempAuth (SearchDoorAuthCommand cmd) {
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        ListDoorAuthResponse resp = new ListDoorAuthResponse();
        List<DoorAuth> auths = doorAuthProvider.searchTempAuthByAdmin(locator, cmd, count);
        List<DoorAuthDTO> dtos = new ArrayList<DoorAuthDTO>();
        long now = DateHelper.currentGMTTime().getTime();
        for(DoorAuth auth : auths) {
            DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(auth.getDoorId());
            DoorAuthDTO dto = ConvertHelper.convert(auth, DoorAuthDTO.class);
            dto.setGoFloor(auth.getCurrStorey());
            dto.setHardwareId(doorAccess.getHardwareId());
            dto.setDoorName(doorAccess.getDisplayNameNotEmpty());
            dto.setPhone(auth.getPhone());
            dto.setOrganization(auth.getOrganization());
            User u = userProvider.findUserById(auth.getApproveUserId());
            if(u != null) {
                dto.setApproveUserName(getRealName(u));
            }

            if(auth.getValidEndMs() < now) {
                auth.setStatus(DoorAuthStatus.INVALID.getCode());
            }

            dtos.add(dto);
        }

        resp.setDtos(dtos);
        resp.setNextPageAnchor(locator.getAnchor());
        return resp;
    }
    
    private Long getDoorAccessLastTick(DoorAccess doorAccess) {
        Long doorAccId = doorAccess.getId();
        String key = String.format(LAST_TICK, doorAccId);
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        Object v = redisTemplate.opsForValue().get(key);
        
        Long rv ;
        if(v == null) {
            rv = 0l;
        } else {
            rv = Long.valueOf((String)v);    
        }
        
        if((rv.longValue()+TASK_TICK_TIMEOUT) > System.currentTimeMillis()) {
            doorAccess.setLinkStatus(DoorAccessLinkStatus.SUCCESS.getCode());
        } else {
            doorAccess.setLinkStatus(DoorAccessLinkStatus.FAILED.getCode());
            }
        
        return rv;
    }
    
    private Long updateDoorAccessLastTick(Long doorAccId) {
        String key = String.format(LAST_TICK, doorAccId);
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
      
        Object v = redisTemplate.opsForValue().get(key);
        
        Long rv ;
        if(v == null) {
            rv = 0l;
        } else {
            rv = Long.valueOf((String)v);    
        }
        
        redisTemplate.opsForValue().set(key, String.valueOf(System.currentTimeMillis()));
        return rv;
    }
    
    private void cacheTimeout(String keyIn, String value, int seconds) {
        String key = "doorcache:" + keyIn;
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }
    
    private String getCache(String keyIn) {
        String key = "doorcache:" + keyIn;
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        Object o = redisTemplate.opsForValue().get(key);
        if(o == null) {
            return null;
        }
        
        return (String)o;
    }
    
    @Override
    public DoorAccess onDoorAccessConnecting(AclinkConnectingCommand cmd) {
        DoorAccess doorAccess = doorAccessProvider.queryDoorAccessByUuid(cmd.getUuid());
        if(doorAccess == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "Door not found");
        }
        
        AesServerKey aesServerKey = aesServerKeyService.getCurrentAesServerKey(doorAccess.getId());
        if(aesServerKey == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "AesServerKey error");
        }
        
        String base64 = cmd.getEncryptBase64().replace("_", "/").replace("-", "+");
        byte[] key = Base64.decodeBase64(aesServerKey.getSecret());
        byte[] data = Base64.decodeBase64(base64);
        String decodeResult = "";
        try {
            byte[] rb = AESUtil.decrypt(data, key);
            byte[] newArray = Arrays.copyOfRange(rb, 16, 32);
            decodeResult = new String(newArray);
        } catch (Exception e) {
            LOGGER.error("decrypt error", e);
        }
        
        if( (decodeResult == "") || (doorAccess.getUuid().indexOf(decodeResult) != 0) ) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, "Auth error");
        }
        
        updateDoorAccessLastTick(doorAccess.getId());
        doorAccess.setLinkStatus(DoorAccessLinkStatus.SUCCESS.getCode());
        return doorAccess;
    }
    
    @Override
    public void onDoorAcessDisconnected(AclinkDisconnectedCommand cmd) {
        
    }
    
    @Override
    public AclinkWebSocketMessage syncWebSocketMessages(AclinkWebSocketMessage resp) {
        if(resp.getSeq() != null) {
            aclinkMessageSequence.ackMessage(resp.getSeq());
            DoorCommand doorCommand = doorCommandProvider.getDoorCommandById(resp.getSeq());
            if(doorCommand != null) {
                doorCommand.setStatus(DoorCommandStatus.RESPONSE.getCode());
                doorCommandProvider.updateDoorCommand(doorCommand);    
            }
        }

        if(resp.getType() != null && resp.getType() != 1){
        	Long lastTick = updateDoorAccessLastTick(resp.getId());
            //generate a time message
            if( (lastTick+15*1000) < System.currentTimeMillis() ) {
                return msgGenerator.generateTimeMessage(resp.getId());
            }
        }
        
        //return msgGenerator.generateWebSocketMessage(resp.getId());
        
        return null;
    }
    
    @Override
    public AclinkFirmwareDTO createAclinkFirmware(CreateAclinkFirmwareCommand cmd) {
        AclinkFirmware firmware = (AclinkFirmware)ConvertHelper.convert(cmd, AclinkFirmware.class);
        firmware.setFirmwareType(AclinkFirmwareType.ZUOLIN.getCode());
        firmware.setStatus(new Byte((byte)1));
        firmware.setCreatorId(UserContext.current().getUser().getId());
        aclinkFirmwareProvider.createAclinkFirmware(firmware);
        return (AclinkFirmwareDTO)ConvertHelper.convert(firmware, AclinkFirmwareDTO.class);
    }
    
    @Override
    public AclinkUpgradeResponse upgradeFirmware(AclinkUpgradeCommand cmd) {
        DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(cmd.getId());
        if(doorAccess == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "Door not found");
        }
        
        AclinkFirmware firm = aclinkFirmwareProvider.queryAclinkFirmwareMax();
        if(firm == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "Firmware not found");
        }
        
        AesServerKey aesServerKey = aesServerKeyService.getCurrentAesServerKey(doorAccess.getId());
        
        int version = (firm.getMajor().intValue() << 16) + (firm.getMinor().intValue() << 8) + (firm.getRevision().intValue());
        String message = AclinkUtils.packUpgrade(aesServerKey.getDeviceVer(), aesServerKey.getSecret(), version, firm.getChecksum().shortValue(), doorAccess.getUuid());
        
        AclinkUpgradeResponse resp = new AclinkUpgradeResponse();
        resp.setCreatorId(firm.getCreatorId());
        resp.setDownloadUrl(firm.getDownloadUrl());
        resp.setInfoUrl(firm.getInfoUrl());
        resp.setVersion("1" + String.valueOf(firm.getMajor()) + "." + String.valueOf(firm.getMinor()) + "." + String.valueOf(firm.getRevision()));
        resp.setMessage(message);
        
        return resp;
    }
    
    @Override
    public String upgradeVerify(AclinkUpgradeCommand cmd) {
        if(!cmd.getMessage().isEmpty()) {
            return "0";    
        }
        
        return "-1";
    }
    
    @Override
    public DoorAccessCapapilityDTO getDoorAccessCapapility(GetDoorAccessCapapilityCommand cmd) {
        DoorAccessCapapilityDTO dto = new DoorAccessCapapilityDTO();
        dto.setIsSupportQR((byte)1);
        dto.setIsSupportSmart((byte)1);
        if(cmd.getNamespaceId() == null) {
        	cmd.setNamespaceId(UserContext.getCurrentNamespaceId(null));
        }
        
        dto.setQrDriver(this.configProvider.getValue(cmd.getNamespaceId(), AclinkConstant.ACLINK_DRIVER_TYPE, DoorAccessDriverType.ZUOLIN.getCode()));
        dto.setQrDriverExt(this.configProvider.getValue(cmd.getNamespaceId(), AclinkConstant.ACLINK_QR_DRIVER_EXT, DoorAccessDriverType.ZUOLIN.getCode()));
        dto.setSmartDriver(DoorAccessDriverType.ZUOLIN.getCode());
        
        return dto;
    }
    
    private DoorAccessDriverType getQrDriverType(Integer namespaceId) {
        String t = this.configProvider.getValue(namespaceId, AclinkConstant.ACLINK_DRIVER_TYPE, DoorAccessDriverType.ZUOLIN.getCode());
        return DoorAccessDriverType.fromCode(t);
    }
    
    private DoorAccessDriverType getQrDriverExt(Integer namespaceId) {
        String t = this.configProvider.getValue(namespaceId, AclinkConstant.ACLINK_QR_DRIVER_EXT, DoorAccessDriverType.ZUOLIN.getCode());
        return DoorAccessDriverType.fromCode(t);
    }
    
    private DoorAccessDriverType getQrDriverZuolinInner(Integer namespaceId) {
        String t = this.configProvider.getValue(namespaceId, AclinkConstant.ACLINK_QR_DRIVER_ZUOLIN_INNER, DoorAccessDriverType.ZUOLIN.getCode());
        return DoorAccessDriverType.fromCode(t);
    }
    
    private String getLinglingId() {
	    User user = UserContext.current().getUser();
	    if(null == user) {
	    	return aclinkLinglingService.getLinglingId();
	    }
	    UserProfile profile = userActivityProvider.findUserProfileBySpecialKey(user.getId(), UserProfileContstant.LINGLING_ID);
        if(profile == null || null == profile.getItemValue() || profile.getItemValue().isEmpty()) {
        		String linglingId = aclinkLinglingService.getLinglingId();
        		
        		profile = new UserProfile();
        		profile.setItemName(UserProfileContstant.LINGLING_ID);
        		profile.setItemKind((byte)0);
        		profile.setOwnerId(user.getId());
            profile.setItemValue(linglingId);
            userActivityProvider.updateUserProfile(profile);   
        }
        
        return profile.getItemValue();
    }
    
    private void updateLinglingExtraStorey(DoorLinglingExtraKeyDTO extra) {
        List<DoorLinglingAuthStoreyInfo> storeyInfos = new ArrayList<>();
        for(Long storey : extra.getStoreyAuthList()) {
            DoorLinglingAuthStoreyInfo info = new DoorLinglingAuthStoreyInfo();
            info.setStorey(storey);
            if(storey.equals(44l)) {
                info.setDisplayName("45A");
            } else if(storey.equals(57l)) {
                info.setDisplayName("56A");
            } else if(storey.equals(58l)) {
                info.setDisplayName("56B");
            } else if(storey.equals(59l)) {
                info.setDisplayName("57");
            } else if(storey.equals(60l)) {
                info.setDisplayName("58");
            }
            storeyInfos.add(info);
        }
        extra.setStoreyInfos(storeyInfos);
    }
    
    private void doLinglingQRKey(User user, DoorAccess doorAccess, DoorAuth auth, List<DoorAccessQRKeyDTO> qrKeys) {
        DoorAccessQRKeyDTO qr = new DoorAccessQRKeyDTO();
        List<String> hardwares = new ArrayList<String>();
        List<String> sdkKeys = new ArrayList<String>();
        
        //TODO 支持后台配置有效时间
        Long validTime = System.currentTimeMillis() + KEY_TICK_ONE_HOUR;
        int maxCount = 120;
            
        qr.setDoorGroupId(doorAccess.getId());
        auth.setKeyValidTime(validTime);
        
        try {
            if(doorAccess.getDoorType().equals(DoorAccessType.ACLINK_LINGLING_GROUP.getCode())) {
                List<DoorAccess> childs = null;
                
                /*if(doorAccess.isVip()) {
                    childs = doorAccessProvider.listAllDoorAccessLingling(doorAccess.getOwnerId(), doorAccess.getOwnerType(), maxCount);
                } else {
                    childs = doorAccessProvider.listDoorAccessByGroupId(doorAccess.getId(), maxCount);    
                }*/

                if(doorAccess.getId().equals(128l)) {
                    //TODO hardcode must be changed later!!!
                    childs = doorAccessProvider.listDoorAccessByGroupId(doorAccess.getId(), maxCount);
                    if(childs != null) {
                        List<DoorAccess> c2 = doorAccessProvider.listDoorAccessByGroupId(93l, maxCount);
                        if(c2 != null) {
                            childs.addAll(c2);
                        }
                    }
                } else {
                    childs = doorAccessProvider.listDoorAccessByGroupId(doorAccess.getId(), maxCount);
                }

                List<Long> deviceIds = new ArrayList<Long>();
                List<Aclink> aclinks = new ArrayList<Aclink>();
                for(DoorAccess child : childs) {
                    Aclink ca = aclinkProvider.getAclinkByDoorId(child.getId());
                    deviceIds.add(ca.getLinglingDoorId());
                    aclinks.add(ca);
                    hardwares.add(child.getHardwareId());
                    }
                
                AclinkLinglingMakeSdkKey sdkKey = new AclinkLinglingMakeSdkKey();
                sdkKey.setDeviceIds(deviceIds);
                sdkKey.setKeyEffecDay(180l);
                Map<Long, String> keyMap = aclinkLinglingService.makeSdkKey(sdkKey);
                
                for(Aclink ca : aclinks) {
                    String key = keyMap.get(ca.getLinglingDoorId());
                    if(key == null) {
                        LOGGER.error("lingling keyMap failed, linglingDoorId=" + ca.getLinglingDoorId() + " doorId=" + doorAccess.getId());
                        continue;
                    }
                    
                    if(!key.equals(ca.getLinglingSDKKey())) {
                        ca.setLinglingSDKKey(key);
                        aclinkProvider.updateAclink(ca);    
                    }
                    
                    sdkKeys.add(key);
                    }
                
            } else {
                hardwares.add(doorAccess.getHardwareId());
                
                Aclink ca = aclinkProvider.getAclinkByDoorId(doorAccess.getId());
                List<Long> deviceIds = new ArrayList<Long>();
                deviceIds.add(ca.getLinglingDoorId());
                AclinkLinglingMakeSdkKey sdkKey = new AclinkLinglingMakeSdkKey();
                sdkKey.setDeviceIds(deviceIds);
                sdkKey.setKeyEffecDay(180l);
                Map<Long, String> keyMap = aclinkLinglingService.makeSdkKey(sdkKey);
                
                String key = keyMap.get(ca.getLinglingDoorId());
                if(key == null) {
                    LOGGER.error("lingling single failed, linglingDoorId=" + ca.getLinglingDoorId() + " doorId=" + doorAccess.getId());
                    return;
                }
                
                if(!key.equals(ca.getLinglingSDKKey())) {
                    ca.setLinglingSDKKey(key);
                    aclinkProvider.updateAclink(ca);    
                }
                
                sdkKeys.add(key);
            }
            
            auth.setKeyValidTime(validTime);
            auth.setLinglingDoorId(doorAccess.getId());
            doorAuthProvider.updateDoorAuth(auth);
                
        } catch(Exception ex) {
            LOGGER.error("create doorAuth key error", ex);
            return;
        }
        
        qr.setCreateTimeMs(System.currentTimeMillis());
        qr.setCreatorUid(user.getId());
        qr.setDoorGroupId(doorAccess.getId());
        qr.setDoorName(doorAccess.getDisplayNameNotEmpty());
        qr.setExpireTimeMs(validTime-1000*10); //for safe to open door
        
        qr.setHardwares(hardwares);
        qr.setQrDriver(DoorAccessDriverType.LINGLING.getCode());
        qr.setStatus(DoorAccessStatus.ACTIVE.getCode());
        qr.setQrCodeKey("11223344");
        qr.setId(auth.getId());
        
        qr.setDoorOwnerId(doorAccess.getOwnerId());
        qr.setDoorOwnerType(doorAccess.getOwnerType());
        
        DoorLinglingExtraKeyDTO extra = new DoorLinglingExtraKeyDTO();
        
        extra.setAuthLevel(0l);
        if(doorAccess.isVip()) {
            extra.setAuthLevel(1l);
        }
        
        if(auth.getCurrStorey() == null) {
            auth.setCurrStorey(1l);    
        }
        
        if(!auth.getCurrStorey().equals(0l)) {
            extra.setAuthStorey(auth.getCurrStorey());    
        } else {
            extra.setAuthStorey(1l);
        }
        
        List<Long> storeyAuthList = new ArrayList<Long>();
        storeyAuthList.add(1l);
        extra.setStoreyAuthList(storeyAuthList);
        extra.setLinglingId(getLinglingId());//one user one linglingId
        
        try {
            if(checkDoorAccessRole(doorAccess)) {
                extra.setAuthLevel(1l);    
            }            
            storeyAuthList = getDoorListbyUser(user, doorAccess, auth.getCurrStorey());
            if(storeyAuthList != null && storeyAuthList.size() > 0) {
                extra.setAuthStorey(storeyAuthList.get(0));
                extra.setStoreyAuthList(storeyAuthList);
                updateLinglingExtraStorey(extra);
            }
        } catch(Exception ex) {
            LOGGER.error("storeyAuth failed", ex);
        }
        extra.setKeys(sdkKeys);
        
        qr.setExtra(StringHelper.toJsonString(extra));
        
        qrKeys.add(qr);
    }
    
    private void doZuolinQRKey(boolean generate, User user, DoorAccess doorAccess, DoorAuth auth, List<DoorAccessQRKeyDTO> qrKeys) {
        List<String> hardwares = new ArrayList<String>();
        int maxCount = 32;
        
        if(doorAccess.getDoorType().equals(DoorAccessType.ACLINK_ZL_GROUP.getCode())) {
            List<DoorAccess> childs = doorAccessProvider.listDoorAccessByGroupId(doorAccess.getId(), maxCount);
            for(DoorAccess child : childs) {
                hardwares.add(child.getHardwareId());
                }
        } else {
            hardwares.add(doorAccess.getHardwareId());
        }
        
        DoorAccessQRKeyDTO qr = new DoorAccessQRKeyDTO();
        
        AesUserKey aesUserKey = genarateQrUserKey(user, auth);
        if(aesUserKey == null) {
            LOGGER.error("aesUserKey created error");
            return;
        }
        
        qr.setCreateTimeMs(auth.getCreateTime().getTime());
        qr.setCreatorUid(auth.getApproveUserId());
        qr.setDoorGroupId(doorAccess.getId());
        qr.setDoorName(doorAccess.getName());
        qr.setDoorDisplayName(doorAccess.getDisplayNameNotEmpty());
        
        if(auth.getAuthType().equals(DoorAuthType.FOREVER.getCode())) {
        	qr.setExpireTimeMs(System.currentTimeMillis() + this.getQrTimeout());
        } else {
        	qr.setExpireTimeMs(auth.getValidEndMs());	
        }
        
        qr.setHardwares(hardwares);
        qr.setId(auth.getId());
        qr.setQrDriver(this.getQrDriverZuolinInner(UserContext.getCurrentNamespaceId()).getCode());
        qr.setCreateTimeMs(auth.getCreateTime().getTime());
        qr.setCurrentTime(DateHelper.currentGMTTime().getTime());
        qr.setDoorOwnerId(doorAccess.getOwnerId());
        qr.setDoorOwnerType(doorAccess.getOwnerType());
        
        Long qrImageTimeout = this.configProvider.getLongValue(UserContext.getCurrentNamespaceId(), AclinkConstant.ACLINK_QR_IMAGE_TIMEOUTS, 1*60);
        qr.setQrImageTimeout(qrImageTimeout*1000l);
        
        qr.setQrCodeKey(aesUserKey.getSecret());
        
        qr.setWebQRCode(getWebQrByAuthId(UserContext.getCurrentNamespaceId(), user, doorAccess, auth, aesUserKey));
        
        qrKeys.add(qr);
    }

    /**
     * 锁管家的app请求参数拼接
     * @auther wh
     * */ 
    private void doUclbrtQRKey(User user, DoorAccess doorAccess, DoorAuth auth, List<DoorAccessQRKeyDTO> qrKeys) {
    	LOGGER.info("ucl 进入doQRKEY " );
        DoorAccessQRKeyDTO qr = new DoorAccessQRKeyDTO();
        qr.setCreateTimeMs(auth.getCreateTime().getTime());
        qr.setCreatorUid(auth.getApproveUserId());
        qr.setDoorGroupId(doorAccess.getId());
        qr.setDoorName(doorAccess.getName());
        qr.setDoorDisplayName(doorAccess.getDisplayNameNotEmpty());
        qr.setExpireTimeMs(auth.getKeyValidTime());
        qr.setId(auth.getId());
        qr.setQrDriver(DoorAccessDriverType.UCLBRT.getCode());
        qr.setDoorOwnerId(doorAccess.getOwnerId());
        qr.setDoorOwnerType(doorAccess.getOwnerType());
        Aclink ca = aclinkProvider.getAclinkByDoorId(doorAccess.getId());
        if(null !=ca){
        	UclbrtParamsDTO paramsDTO = JSON.parseObject(ca.getUclbrtParams(), UclbrtParamsDTO.class); 
        	UserIdentifier userIdentifier = userProvider.findUserIdentifiersOfUser(UserContext.currentUserId(), UserContext.getCurrentNamespaceId());
        	qr.setQrCodeKey(uclbrtHttpClient.getQrCode(paramsDTO, userIdentifier.getIdentifierToken()));
        	if(null != qr.getQrCodeKey()){
        		qrKeys.add(qr);
        	}
        } 
       
    }
    //do huarun qr
    private void doHuarunQRKey(User user, DoorAccess doorAccess, DoorAuth auth, List<DoorAccessQRKeyDTO> qrKeys) {
        DoorAccessQRKeyDTO qr = new DoorAccessQRKeyDTO();
        qr.setCreateTimeMs(auth.getCreateTime().getTime());
        qr.setCreatorUid(auth.getApproveUserId());
        qr.setDoorGroupId(doorAccess.getId());
        qr.setDoorName(doorAccess.getName());
        qr.setDoorDisplayName(doorAccess.getDisplayNameNotEmpty());
        qr.setExpireTimeMs(auth.getKeyValidTime());
        qr.setId(auth.getId());
        qr.setQrDriver(DoorAccessDriverType.HUARUN_ANGUAN.getCode());
        qr.setDoorOwnerId(doorAccess.getOwnerId());
        qr.setDoorOwnerType(doorAccess.getOwnerType());
      
        AclinkGetSimpleQRCode getCode = new AclinkGetSimpleQRCode();
        UserInfo userInfo = userService.getUserSnapshotInfoWithPhone(user.getId());
        if(userInfo != null && userInfo.getPhones() != null && userInfo.getPhones().size() > 0) {
        	getCode.setPhone(userInfo.getPhones().get(0));
        }
        AclinkGetSimpleQRCodeResp resp = aclinkHuarunService.getSimpleQRCode(getCode);
        if(resp == null) {
        	throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_HUARUN_ERROR, "huarun service error");
        } else {
        	qr.setQrCodeKey(resp.getQrcode());
        }
        
        
        qrKeys.add(qr);
    }
    
    @Override
    public ListDoorAccessQRKeyResponse listDoorAccessQRKey() {
        return listDoorAccessQRKeyAndGenerateQR(null, false);
    }
    
    @Override
    public ListDoorAccessQRKeyResponse listBusAccessQRKey() {
        return listDoorAccessQRKeyAndGenerateQR(DoorAccessDriverType.BUS, false);
    }

    @Override
    public ListDoorAccessQRKeyResponse listDoorAccessQRKeyAndGenerateQR(DoorAccessDriverType driverType, boolean generate) {
    	Long t0 = DateHelper.currentGMTTime().getTime();
        Long t1 = DateHelper.currentGMTTime().getTime();
        LOGGER.info("开始获取auths" );
        
        User user = UserContext.current().getUser();
        ListAuthsByLevelandLocationCommand qryCmd = getLevelQryCmdByUser(user);
		qryCmd.setDriver(driverType != null? driverType.getCode() : null);        
        ListingLocator locator = new ListingLocator();
        //List<DoorAuth> auths = uniqueAuths(doorAuthProvider.queryValidDoorAuthByUserId(locator, user.getId(), DoorAccessDriverType.LINGLING, 60));
        List<DoorAuth> auths = uniqueAuths(doorAuthProvider.queryValidDoorAuthByUserId(locator, qryCmd, 60));
        
        ListDoorAccessQRKeyResponse resp = new ListDoorAccessQRKeyResponse();
        List<DoorAccessQRKeyDTO> qrKeys = new ArrayList<DoorAccessQRKeyDTO>();
        resp.setKeys(qrKeys);
        resp.setQrIntro(this.configProvider.getValue(UserContext.getCurrentNamespaceId(), AclinkConstant.ACLINK_QR_IMAGE_INTRO, AclinkConstant.QR_INTRO_URL));

        Long t2 = DateHelper.currentGMTTime().getTime();
        LOGGER.info("auths 获取 "+(t2-t1));
        for(DoorAuth auth : auths) {
            DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(auth.getDoorId());
            if(!doorAccess.getStatus().equals(DoorAccessStatus.ACTIVE.getCode())) {
                //The door is delete, set it to invalid
                auth.setStatus(DoorAuthStatus.INVALID.getCode());
                doorAuthProvider.updateDoorAuth(auth);
                continue;
            }
            
            //没有二维码读头的门禁不返回二维码,by liuyilin 20180529
            if(doorAccess.getHasQr() != (byte) 1){
            	continue;
            }
            
            
            if(auth.getDriver().equals(DoorAccessDriverType.LINGLING.getCode())) {
            	//Forever + true of rightOpen
                if(!(auth.getAuthType().equals(DoorAuthType.FOREVER.getCode()) && auth.getRightOpen().equals((byte)1))) {
                    continue;
                }
                //这个时间才是有效的二维码超时时间， dto 里面的时间是为了兼容过去的 app 版本。
            	resp.setQrTimeout(this.configProvider.getLongValue(UserContext.getCurrentNamespaceId(), AclinkConstant.ACLINK_QR_TIMEOUTS, 4*24*60));
                doLinglingQRKey(user, doorAccess, auth, qrKeys);
            } else if(DoorAccessDriverType.UCLBRT == DoorAccessDriverType.fromCode(auth.getDriver())){
            	//锁管家是由app远程请求获取二维码的,这里给他组装存在aclinks表里的参数就行
            	//added by wh
            	Long t3 = DateHelper.currentGMTTime().getTime();
                if(!(auth.getAuthType().equals(DoorAuthType.FOREVER.getCode()) && auth.getRightOpen().equals((byte)1))) {
                    continue;
                }
                doUclbrtQRKey(user, doorAccess, auth, qrKeys);

                Long t4 = DateHelper.currentGMTTime().getTime();

                LOGGER.info("拿一个uclbrt 的二维码"+(t4-t3));
            } else if(auth.getDriver().equals(DoorAccessDriverType.HUARUN_ANGUAN.getCode())){
            	//Forever + true of rightOpen
                if(!(auth.getAuthType().equals(DoorAuthType.FOREVER.getCode()) && auth.getRightOpen().equals((byte)1))) {
                    continue;
                }
                
            	doHuarunQRKey(user, doorAccess, auth, qrKeys);
            } else {
            	
            	//rightOpen and more
            	if(!auth.getRightOpen().equals((byte)1)) {
            		continue;
            		}
            	if(!auth.getAuthType().equals(DoorAuthType.FOREVER.getCode())) {
            		Long now = DateHelper.currentGMTTime().getTime();
            		if(auth.getValidEndMs() < now) {
            			//已经失效，删除它
                    auth.setStatus(DoorAuthStatus.INVALID.getCode());
                    doorAuthProvider.updateDoorAuth(auth);
                    continue;
                    }
            		
            		if(auth.getValidFromMs() > now) {
            			continue;
            		}
            	}
            	resp.setQrTimeout(this.getQrTimeout()/1000l);
//            	resp.setQrIntro("http://xxxx.com");
                doZuolinQRKey(generate, user, doorAccess, auth, qrKeys);
                }
           
            }
        LOGGER.debug("总请求时间" + (DateHelper.currentGMTTime().getTime() - t0)); 
        if (resp.getKeys() == null || resp.getKeys().size() ==0 ){
        	LOGGER.info(String.format("user{%d} has no valid auth,phone:%d",user.getId(),user.getIdentityNumberTag()));
        }
        return resp;              
     }
    
    public ListDoorAccessQRKeyResponse listDoorAccessQRKey2() {
        User user = UserContext.current().getUser();
        ListDoorAccessQRKeyResponse resp = new ListDoorAccessQRKeyResponse();
        List<DoorAccessQRKeyDTO> qrKeys = new ArrayList<DoorAccessQRKeyDTO>();
        resp.setKeys(qrKeys);
        
        DoorAccessQRKeyDTO qr = new DoorAccessQRKeyDTO();
        qr.setCreateTimeMs(System.currentTimeMillis());
        qr.setCreatorUid(user.getId());
        qr.setDoorGroupId(1008l);
        qr.setDoorName("testname");
        qr.setExpireTimeMs(System.currentTimeMillis() + 5*60*1000);
        
        List<String> hardwares = new ArrayList<String>();
        hardwares.add("03e80003e8");
        
        qr.setHardwares(hardwares);
        qr.setQrDriver(DoorAccessDriverType.LINGLING.getCode());
        qr.setStatus(DoorAccessStatus.ACTIVE.getCode());
        qr.setQrCodeKey("11223344");
        qr.setId(1008l);
        
        DoorLinglingExtraKeyDTO extra = new DoorLinglingExtraKeyDTO();
        extra.setAuthLevel(0l);
        extra.setAuthStorey(8l);
        
        List<Long> storeyAuthList = new ArrayList<Long>();
        storeyAuthList.add(8l);
        storeyAuthList.add(9l);
        storeyAuthList.add(11l);
        extra.setStoreyAuthList(storeyAuthList);
        
        List<String> sdkKeys = new ArrayList<String>();
        sdkKeys.add("1B70F6FCB1080D2C91B81DFEAFD0B43D26F9F8D70AA08B095B995D55FDEB520223F8642771A5E123EA10C9B8122B77474489");
        sdkKeys.add("CA30CFF4A0C4C46A9AC3DEE75733269C3D1137C00D7301D8BDC12F02D40EA77D16F59B94D86FCA7DC71FBFE48DC7B87B4752");
        extra.setKeys(sdkKeys);
        
        qr.setExtra(StringHelper.toJsonString(extra));
        
        qrKeys.add(qr);
        
        return resp;
        }

    @Override
    public DoorAccessDTO createDoorAccessGroup(CreateDoorAccessGroup cmd) {
    	DoorAccessDriverType driverType = getQrDriverType(UserContext.getCurrentNamespaceId(null));
    	if(driverType == DoorAccessDriverType.LINGLING) {
    		return createDoorAccessGroupLingling(cmd);
    	} else if(driverType == DoorAccessDriverType.HUARUN_ANGUAN) {
    		return createDoorAccessGroupHuarunAnGuan(cmd);
    	} else {
    		return createDoorAccessGroupZuolin(cmd);
    	}
    }

    //安冠 SDK 没有设备或者门禁的概念，它直接通过手机好生成 4 个字节的二维码
    private DoorAccessDTO createDoorAccessGroupHuarunAnGuan(CreateDoorAccessGroup cmd) {
        User user = UserContext.current().getUser();
        
        String uuid = UUID.randomUUID().toString();
        final String groupHardwareId = uuid.replace("-", "");
        
        DoorAccess doorAccess = this.dbProvider.execute(new TransactionCallback<DoorAccess>() {
            @Override
            public DoorAccess doInTransaction(TransactionStatus arg0) {
                DoorAccess doorAcc = new DoorAccess();
                
                doorAcc.setName(cmd.getName());
                doorAcc.setDisplayName(cmd.getName());
                doorAcc.setHardwareId(groupHardwareId);
                doorAcc.setActiveUserId(user.getId());
                doorAcc.setCreatorUserId(user.getId());
                doorAcc.setOwnerId(cmd.getOwnerId());
                doorAcc.setOwnerType(cmd.getOwnerType());
                doorAcc.setAddress(cmd.getAddress());
                doorAcc.setAvatar(cmd.getAddress());
                doorAcc.setStatus(DoorAccessStatus.ACTIVE.getCode());
                doorAcc.setDoorType(DoorAccessType.ACLINK_HUARUN_GROUP.getCode());
                doorAcc.setUuid(groupHardwareId);
                doorAcc.setAesIv("");
                doorAccessProvider.createDoorAccess(doorAcc);
                
                OwnerDoor ownerDoor = new OwnerDoor();
                ownerDoor.setDoorId(doorAcc.getId());
                ownerDoor.setOwnerType(cmd.getOwnerType());
                ownerDoor.setOwnerId(cmd.getOwnerId());
                try {
                    ownerDoorProvider.createOwnerDoor(ownerDoor);
                } catch(Exception ex) {
                    LOGGER.error("createOwnerDoor failed ", ex);
                    return null;
                    }
                
                Aclink aclink = new Aclink();
                aclink.setManufacturer("huarun_anguan");
                aclink.setStatus(DoorAccessStatus.ACTIVE.getCode());
                aclink.setDoorId(doorAcc.getId());
                aclink.setDriver(DoorAccessDriverType.HUARUN_ANGUAN.getCode());
                aclink.setDeviceName(cmd.getName());
                aclink.setFirwareVer("");
                aclinkProvider.createAclink(aclink);
               
                return doorAcc;
            }
        });
        
        return ConvertHelper.convert(doorAccess, DoorAccessDTO.class);    	
    }
    
    private DoorAccessDTO createDoorAccessGroupZuolin(CreateDoorAccessGroup cmd) {
        User user = UserContext.current().getUser();
        
        String uuid = UUID.randomUUID().toString();
        final String groupHardwareId = uuid.replace("-", "");
        
        DoorAccess doorAccess = this.dbProvider.execute(new TransactionCallback<DoorAccess>() {
            @Override
            public DoorAccess doInTransaction(TransactionStatus arg0) {
                DoorAccess doorAcc = new DoorAccess();
                String aesKey = AclinkUtils.generateAESKey();
                
                doorAcc.setName(cmd.getName());
                doorAcc.setDisplayName(cmd.getName());
                doorAcc.setHardwareId(groupHardwareId);
                doorAcc.setActiveUserId(user.getId());
                doorAcc.setCreatorUserId(user.getId());
                doorAcc.setOwnerId(cmd.getOwnerId());
                doorAcc.setOwnerType(cmd.getOwnerType());
                doorAcc.setAddress(cmd.getAddress());
                doorAcc.setAvatar(cmd.getAddress());
                doorAcc.setStatus(DoorAccessStatus.ACTIVE.getCode());
                //edited by liuyilin
                //doorAcc.setDoorType(DoorAccessType.ZLACLINK_WIFI.getCode());
                doorAcc.setDoorType(DoorAccessType.ACLINK_ZL_GROUP.getCode());
                //---
                doorAcc.setUuid(groupHardwareId);
                doorAcc.setAesIv(aesKey);//save the original aesKey, TODO save it to aes server key?
                doorAccessProvider.createDoorAccess(doorAcc);
                
                OwnerDoor ownerDoor = new OwnerDoor();
                ownerDoor.setDoorId(doorAcc.getId());
                ownerDoor.setOwnerType(cmd.getOwnerType());
                ownerDoor.setOwnerId(cmd.getOwnerId());
                try {
                    ownerDoorProvider.createOwnerDoor(ownerDoor);
                } catch(Exception ex) {
                    LOGGER.error("createOwnerDoor failed ", ex);
                    return null;
                    }
                
                Aclink aclink = new Aclink();
                aclink.setManufacturer(Manufacturer);
                aclink.setStatus(DoorAccessStatus.ACTIVE.getCode());
                aclink.setDoorId(doorAcc.getId());
                aclink.setDriver(DoorAccessDriverType.ZUOLIN.getCode());
                aclink.setDeviceName(cmd.getName());
                aclink.setFirwareVer("");
                aclinkProvider.createAclink(aclink);
                
                AesServerKey aesServerKey = new AesServerKey();
                aesServerKey.setCreateTimeMs(System.currentTimeMillis());
                aesServerKey.setDoorId(doorAcc.getId());
                aesServerKey.setSecret(aesKey);
                aesServerKey.setDeviceVer(AclinkDeviceVer.VER0.getCode());
                aesServerKeyService.createAesServerKey(aesServerKey);
               
                return doorAcc;
            }
        });
        
        return ConvertHelper.convert(doorAccess, DoorAccessDTO.class);    	
    }
    
    private DoorAccessDTO createDoorAccessGroupLingling(CreateDoorAccessGroup cmd) {
        User user = UserContext.current().getUser();
        
        String uuid = UUID.randomUUID().toString();
        final String groupHardwareId = uuid.replace("-", "");
        
        if((cmd.getGroupType().byteValue() < DoorAccessType.ACLINK_ZL_GROUP.getCode())
                || (cmd.getGroupType().byteValue() > DoorAccessType.ACLINK_LINGLING_GROUP.getCode())){
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "groupType error");
        }
        
        final String linglingId = aclinkLinglingService.getLinglingId();
        
        DoorAccess doorAccess = this.dbProvider.execute(new TransactionCallback<DoorAccess>() {
            @Override
            public DoorAccess doInTransaction(TransactionStatus arg0) {
                DoorAccess doorAcc = new DoorAccess();
                doorAcc.setName(cmd.getName());
                doorAcc.setDisplayName(cmd.getName());
                doorAcc.setHardwareId(groupHardwareId);
                doorAcc.setActiveUserId(user.getId());
                doorAcc.setCreatorUserId(user.getId());
                doorAcc.setOwnerId(cmd.getOwnerId());
                doorAcc.setOwnerType(cmd.getOwnerType());
                doorAcc.setAddress(cmd.getAddress());
                doorAcc.setAvatar(cmd.getAddress());
                doorAcc.setStatus(DoorAccessStatus.ACTIVE.getCode());
                //edited by liuyilin
//                doorAcc.setDoorType(cmd.getGroupType());
                doorAcc.setDoorType(DoorAccessType.ACLINK_LINGLING_GROUP.getCode());
                //--
                doorAcc.setUuid(groupHardwareId);
                doorAcc.setAesIv("");
                doorAccessProvider.createDoorAccess(doorAcc);
                
                OwnerDoor ownerDoor = new OwnerDoor();
                ownerDoor.setDoorId(doorAcc.getId());
                ownerDoor.setOwnerType(cmd.getOwnerType());
                ownerDoor.setOwnerId(cmd.getOwnerId());
                try {
                    ownerDoorProvider.createOwnerDoor(ownerDoor);
                } catch(Exception ex) {
                    LOGGER.error("createOwnerDoor failed ", ex);
                    return null;
                    }
                
                Aclink aclink = new Aclink();
                aclink.setManufacturer(Manufacturer);
                aclink.setStatus(DoorAccessStatus.ACTIVE.getCode());
                aclink.setDoorId(doorAcc.getId());
                aclink.setDriver(DoorAccessDriverType.LINGLING.getCode());
                aclink.setDeviceName(linglingId);
                aclink.setFirwareVer("");
                aclinkProvider.createAclink(aclink);
               
                return doorAcc;
            }
        });
        
        return ConvertHelper.convert(doorAccess, DoorAccessDTO.class);
    }
    
    @Override
    public DoorAccessDTO createDoorAccessLingLing(CreateDoorAccessLingLing cmd) {
        User user = UserContext.current().getUser();
        
        Long lingDoorId = null;
        
        if(cmd.getExistsId() == null) {
            AclinkLinglingDevice device = new AclinkLinglingDevice();
            device.setDeviceCode(cmd.getHardwareId());
            device.setDeviceName(cmd.getName());
            lingDoorId = aclinkLinglingService.createDevice(device);
            
            if(lingDoorId == null) {
                throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_EXISTS, "Door exists");
            }
        } else {
            lingDoorId = cmd.getExistsId();
        }
        
        final Long lDoorId = lingDoorId;
        
        final String linglingId = aclinkLinglingService.getLinglingId();
        
        DoorAccess doorAccess = this.dbProvider.execute(new TransactionCallback<DoorAccess>() {
            @Override
            public DoorAccess doInTransaction(TransactionStatus arg0) {
                DoorAccess doorAcc = doorAccessProvider.queryDoorAccessByHardwareId(cmd.getHardwareId());
                if(doorAcc != null) {
                    LOGGER.error("door exists! hardwareId=" + cmd.getHardwareId());
                    return null;
                }
                doorAcc = new DoorAccess();
                doorAcc.setName(cmd.getName());
                doorAcc.setDisplayName(cmd.getName());
                doorAcc.setHardwareId(cmd.getHardwareId());
                doorAcc.setActiveUserId(user.getId());
                doorAcc.setCreatorUserId(user.getId());
                doorAcc.setOwnerId(cmd.getOwnerId());
                doorAcc.setOwnerType(cmd.getOwnerType());
                doorAcc.setAddress(cmd.getAddress());
                doorAcc.setAvatar(cmd.getAddress());
                doorAcc.setStatus(DoorAccessStatus.ACTIVE.getCode());
                doorAcc.setDoorType(DoorAccessType.ACLINK_LINGLING.getCode());
                doorAcc.setAesIv("");
                if(cmd.getDoorGroupId() != null) {
                    doorAcc.setGroupid(cmd.getDoorGroupId());
                } else {
                    doorAcc.setGroupid(0l);
                }
                doorAccessProvider.createDoorAccess(doorAcc);
                
                OwnerDoor ownerDoor = new OwnerDoor();
                ownerDoor.setDoorId(doorAcc.getId());
                ownerDoor.setOwnerType(cmd.getOwnerType());
                ownerDoor.setOwnerId(cmd.getOwnerId());
                try {
                    ownerDoorProvider.createOwnerDoor(ownerDoor);
                } catch(Exception ex) {
                    LOGGER.warn("createOwnerDoor failed ", ex);
                    }
                
                Aclink aclink = new Aclink();
                aclink.setManufacturer(Manufacturer);
                aclink.setStatus(DoorAccessStatus.ACTIVE.getCode());
                aclink.setDoorId(doorAcc.getId());
                aclink.setDeviceName(linglingId);
                aclink.setDriver(DoorAccessDriverType.LINGLING.getCode());
                aclink.setIntegralTag1(lDoorId);
                aclink.setFirwareVer("");
                aclinkProvider.createAclink(aclink);
               
                return doorAcc;
            }
        });
        
        if(doorAccess == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_EXISTS, "Door exists");
        }
        
        return ConvertHelper.convert(doorAccess, DoorAccessDTO.class);
    }
    
    private String createZuolinQrV1(String secret) {
        byte[] type = new byte[]{0, 1};
        byte[] cmdArr = new byte[]{8, 0};
        byte[] qrArr = Base64.decodeBase64(secret);
        byte[] lengthArr = DataUtil.shortToByteArray((short) (qrArr.length + cmdArr.length));
        long curTimeMill = System.currentTimeMillis();
        byte[] timeArr = DataUtil.longToByteArray(curTimeMill);
        byte[] resultArr = new byte[cmdArr.length + type.length + lengthArr.length + qrArr.length + timeArr.length];
        System.arraycopy(type, 0, resultArr, 0, type.length);
        System.arraycopy(lengthArr, 0, resultArr, type.length, lengthArr.length);
        System.arraycopy(cmdArr, 0, resultArr, type.length + lengthArr.length, cmdArr.length);
        System.arraycopy(qrArr, 0, resultArr, cmdArr.length + type.length + lengthArr.length, qrArr.length);
        System.arraycopy(timeArr, 0, resultArr, cmdArr.length + type.length + lengthArr.length + qrArr.length, timeArr.length);
        String resultStr = Base64.encodeBase64String(resultArr);
        
        return resultStr;
    }
    
    private String createZuolinQrByCount (Long authId){
		byte[] type = new byte[] { 0, 1 };
		byte[] cmdArr = new byte[] { 0x10, 0 };
		byte[] qrArr = new byte[16];
		byte[] idArr = DataUtil.longToByteArray(authId);
		if (idArr.length > 4) {
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE,
					AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, "id larger than 4 Byte");
		}
		qrArr[0] = 0x1;
		System.arraycopy(idArr, 0, qrArr, 5 - idArr.length, idArr.length);
		byte[] lengthArr = DataUtil.shortToByteArray((short) (qrArr.length + cmdArr.length));
//		long curTimeMill = System.currentTimeMillis();
//		byte[] timeArr = DataUtil.longToByteArray(curTimeMill);
		byte[] resultArr = new byte[cmdArr.length + type.length + lengthArr.length + qrArr.length];// + timeArr.length
		System.arraycopy(type, 0, resultArr, 0, type.length);
		System.arraycopy(lengthArr, 0, resultArr, type.length, lengthArr.length);
		System.arraycopy(cmdArr, 0, resultArr, type.length + lengthArr.length, cmdArr.length);
		System.arraycopy(qrArr, 0, resultArr, cmdArr.length + type.length + lengthArr.length, qrArr.length);
//		System.arraycopy(timeArr, 0, resultArr, cmdArr.length + type.length + lengthArr.length + qrArr.length, timeArr.length);
		String resultStr = Base64.encodeBase64String(resultArr);

		return resultStr;
    }

    //zuolin device qr. normal visitor auth
    private DoorAuthDTO createZuolinDeviceQr(CreateDoorVisitorCommand cmd) {
        User user = UserContext.current().getUser();
        DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(cmd.getDoorId());
        DoorAuth auth = createZuolinQrAuth(user, doorAccess, cmd);
        String nickName = getRealName(user);
        String homeUrl = configProvider.getValue(AclinkConstant.HOME_URL, "");
        List<Tuple<String, Object>> variables = smsProvider.toTupleList(AclinkConstant.SMS_VISITOR_USER, nickName);
        smsProvider.addToTupleList(variables, AclinkConstant.SMS_VISITOR_DOOR, doorAccess.getDisplayNameNotEmpty());

        LocaleTemplate lt = localeTemplateProvider.findLocaleTemplateByScope(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), SmsTemplateCode.SCOPE,
        		SmsTemplateCode.ACLINK_VISITOR_MSG_CODE, user.getLocale());

        if(lt != null && lt.getDescription().indexOf("{link}") >= 0) {
        	smsProvider.addToTupleList(variables, AclinkConstant.SMS_VISITOR_LINK, homeUrl+"/evh");
        }

        smsProvider.addToTupleList(variables, AclinkConstant.SMS_VISITOR_ID, auth.getLinglingUuid());
        String templateLocale = user.getLocale();
        smsProvider.sendSms(cmd.getNamespaceId(), cmd.getPhone(), SmsTemplateCode.SCOPE, SmsTemplateCode.ACLINK_VISITOR_MSG_CODE, templateLocale, variables);

        return ConvertHelper.convert(auth, DoorAuthDTO.class);
    }

    private DoorAuth createZuolinQrAuth(User user, DoorAccess doorAccess, CreateDoorVisitorCommand cmd){
        if(doorAccess == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "DoorAccess not found");
        }
        
        //TODO check for permission
//        DoorAuth authChecked = doorAuthProvider.queryValidDoorAuthForever(cmd.getDoorId(), user.getId());
//        if(authChecked == null || (!authChecked.getRightVisitor().equals((byte)1))) {
//            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, "User not auth");   
//        }
        
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        uuid = uuid.substring(0, 5);
        
        DoorAuth auth = new DoorAuth();
        auth.setApplyUserName(cmd.getUserName());
        auth.setApproveUserId(user.getId());
        auth.setAuthType(DoorAuthType.ZUOLIN_VISITOR.getCode());
        auth.setDescription(cmd.getDescription());
        auth.setDoorId(cmd.getDoorId());
        auth.setDriver(this.getQrDriverZuolinInner(cmd.getNamespaceId()).getCode());
        auth.setOrganization(cmd.getOrganization());
        auth.setPhone(cmd.getPhone());
        auth.setNickname(cmd.getUserName());
        auth.setLinglingUuid(uuid);
        auth.setCurrStorey(cmd.getDoorNumber());
        auth.setRightOpen((byte) 1);
        
        //按次开门只适用于左邻门禁 by liuyilin 20180509
		if (cmd.getAuthRuleType() != null && cmd.getAuthRuleType().equals(DoorAuthRuleType.COUNT.getCode())) {
			//TODO 访客授权,将以前的授权失效
			invalidVistorAuth(cmd.getDoorId(), cmd.getPhone());
			auth.setAuthRuleType(cmd.getAuthRuleType());
			auth.setTotalAuthAmount(cmd.getTotalAuthAmount() == null ? 0 : cmd.getTotalAuthAmount());
			auth.setValidAuthAmount(cmd.getTotalAuthAmount() == null ? 0 : cmd.getTotalAuthAmount());
			auth.setValidFromMs(cmd.getValidFromMs() == null ? System.currentTimeMillis() : cmd.getValidFromMs());
			auth.setKeyValidTime(cmd.getValidFromMs() == null ? System.currentTimeMillis() : cmd.getValidFromMs());
			if(cmd.getValidEndMs() == null){
				auth.setValidEndMs(auth.getValidFromMs() + (doorAccess.getDefualtInvalidDuration() == null ? KEY_TICK_7_DAY : KEY_TICK_ONE_DAY * doorAccess.getDefualtInvalidDuration()));
			}else{
				auth.setValidEndMs(cmd.getValidEndMs());
			}
			
			auth.setRightRemote((byte) 1);
		} else {
			auth.setAuthRuleType(DoorAuthRuleType.DURATION.getCode());
			if (cmd.getValidFromMs() == null) {
				auth.setValidFromMs(System.currentTimeMillis());
			} else {
				auth.setValidFromMs(cmd.getValidFromMs());
			}

			if (cmd.getValidEndMs() == null) {
				auth.setKeyValidTime(System.currentTimeMillis() + KEY_TICK_ONE_DAY);
				auth.setValidEndMs(System.currentTimeMillis() + KEY_TICK_ONE_DAY);
			} else {
				auth.setKeyValidTime(cmd.getValidEndMs());
				auth.setValidEndMs(cmd.getValidEndMs());
			}
		}

        
        auth.setUserId(0l);
        auth.setOwnerType(doorAccess.getOwnerType());
        auth.setOwnerId(doorAccess.getOwnerId());
        auth.setStatus(DoorAuthStatus.VALID.getCode());
        auth.setAuthMethod(cmd.getAuthMethod());
        doorAuthProvider.createDoorAuth(auth);
        
        AesUserKey aesUserKey = generateAesUserKey(user, auth);
        if(aesUserKey == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_STATE_ERROR, "DoorAccess user key error");
        }
        
        auth.setLinglingUuid(uuid + "-" + auth.getId().toString());
        
        //convert to qr, move it to CmdUtil ?
        //edited by liuyilin 20180315
        //String resultStr = createZuolinQrV1(aesUserKey.getSecret());
        String resultStr;
        if(auth.getAuthRuleType() != null && auth.getAuthRuleType().equals(DoorAuthRuleType.COUNT.getCode())){
        	resultStr = createZuolinQrByCount(auth.getId());
        }else{
        	resultStr = createZuolinQrV1(aesUserKey.getSecret());
        }

        
        auth.setQrKey(resultStr);
        doorAuthProvider.updateDoorAuth(auth);
        return auth;
    }
    
    //huarun device qr. normal visitor auth
    private DoorAuthDTO createHuarunDeviceQr(CreateDoorVisitorCommand cmd) {
        User user = UserContext.current().getUser();
        
        DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(cmd.getDoorId());
        if(doorAccess == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "DoorAccess not found");
        }
        
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        uuid = uuid.substring(0, 5);
        
        DoorAuth auth = new DoorAuth();
        auth.setId(doorAuthProvider.getNextDoorAuth());
        auth.setLinglingUuid(uuid + "-" + auth.getId().toString());
        auth.setApplyUserName(cmd.getUserName());
        auth.setApproveUserId(user.getId());
        auth.setAuthType(DoorAuthType.HUARUN_VISITOR.getCode());
        auth.setDescription(cmd.getDescription());
        auth.setDoorId(cmd.getDoorId());
        auth.setDriver(DoorAccessDriverType.HUARUN_ANGUAN.getCode());
        auth.setOrganization(cmd.getOrganization());
        auth.setPhone(cmd.getPhone());
        auth.setNickname(cmd.getUserName());
        auth.setKeyValidTime(System.currentTimeMillis() + KEY_TICK_ONE_DAY);
        auth.setValidFromMs(System.currentTimeMillis());
        auth.setValidEndMs(System.currentTimeMillis() + KEY_TICK_ONE_DAY);
        auth.setUserId(0l);
        auth.setOwnerType(doorAccess.getOwnerType());
        auth.setOwnerId(doorAccess.getOwnerId());
        auth.setStatus(DoorAuthStatus.VALID.getCode());
        auth.setAuthMethod(cmd.getAuthMethod());
        
        AclinkGetSimpleQRCode getCode = new AclinkGetSimpleQRCode();
        getCode.setPhone(cmd.getPhone());
        AclinkSimpleQRCodeInvitation invitation = new AclinkSimpleQRCodeInvitation();
        String dateNow = huarunDateFormatter.format(new Date());
        invitation.setDate(dateNow);
        invitation.setInvitee(cmd.getUserName());
        invitation.setInvitee_tel(cmd.getPhone());
        getCode.setInvitation(invitation);
        AclinkGetSimpleQRCodeResp resp = aclinkHuarunService.getSimpleQRCode(getCode);
        if(resp != null) {
        	auth.setQrKey(resp.getQrcode());	
        } else {
        	throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_HUARUN_ERROR, "huarun service error");
        }
        
        doorAuthProvider.createDoorAuth(auth);
        
        String nickName = getRealName(user);
        String homeUrl = configProvider.getValue(AclinkConstant.HOME_URL, "");
        List<Tuple<String, Object>> variables = smsProvider.toTupleList(AclinkConstant.SMS_VISITOR_USER, nickName);
        smsProvider.addToTupleList(variables, AclinkConstant.SMS_VISITOR_DOOR, doorAccess.getDisplayNameNotEmpty());
        
        LocaleTemplate lt = localeTemplateProvider.findLocaleTemplateByScope(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), SmsTemplateCode.SCOPE,
                SmsTemplateCode.ACLINK_VISITOR_MSG_CODE, user.getLocale());
        
        if(lt != null && lt.getDescription().indexOf("{link}") >= 0) {
            smsProvider.addToTupleList(variables, AclinkConstant.SMS_VISITOR_LINK, homeUrl+"/evh");
        }
        
        smsProvider.addToTupleList(variables, AclinkConstant.SMS_VISITOR_ID, auth.getLinglingUuid());
        String templateLocale = user.getLocale();
        smsProvider.sendSms(cmd.getNamespaceId(), cmd.getPhone(), SmsTemplateCode.SCOPE, SmsTemplateCode.ACLINK_VISITOR_MSG_CODE, templateLocale, variables);
        
        return ConvertHelper.convert(auth, DoorAuthDTO.class);        
    }
    
    //TODO better for implements
    private DoorAuthDTO createZuolinPhoneQr(CreateDoorVisitorCommand cmd) {
        User user = UserContext.current().getUser();
        
        DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(cmd.getDoorId());
        if(doorAccess == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "DoorAccess not found");
        }
        
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        uuid = uuid.substring(0, 5);
        
        DoorAuth auth = new DoorAuth();
        auth.setApplyUserName(user.getNickName());
        auth.setApproveUserId(user.getId());
        auth.setAuthType(DoorAuthType.ZUOLIN_VISITOR.getCode());
        auth.setDescription(cmd.getDescription());
        auth.setDoorId(cmd.getDoorId());
        auth.setDriver(DoorAccessDriverType.PHONE_VISIT.getCode()); //for phone visit implements
        auth.setOrganization(cmd.getOrganization());
        auth.setPhone(cmd.getPhone());
        auth.setNickname(cmd.getUserName());
        if(cmd.getValidEndMs() != null) {
        	auth.setKeyValidTime(cmd.getValidEndMs());
        	auth.setValidEndMs(cmd.getValidEndMs());
        } else {
        	auth.setKeyValidTime(System.currentTimeMillis() + KEY_TICK_ONE_DAY);
        	auth.setValidEndMs(System.currentTimeMillis() + KEY_TICK_ONE_DAY);
        }
        
        if(cmd.getValidFromMs() != null) {
        	auth.setValidFromMs(cmd.getValidFromMs());
        } else {
        	auth.setValidFromMs(System.currentTimeMillis());	
        }
        
        auth.setUserId(0l);
        auth.setOwnerType(doorAccess.getOwnerType());
        auth.setOwnerId(doorAccess.getOwnerId());
        auth.setStatus(DoorAuthStatus.VALID.getCode());
        auth.setAuthMethod(cmd.getAuthMethod());
        
        //TODO valid count
        doorAuthProvider.createDoorAuth(auth);
        
        AesUserKey aesUserKey = generateAesUserKey(user, auth);
        if(aesUserKey == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_STATE_ERROR, "DoorAccess user key error");
        }
        
        auth.setLinglingUuid(uuid + "-" + auth.getId().toString());
        
        //convert to qr, move it to CmdUtil ?
        byte[] type = new byte[]{0, 1};
        byte[] cmdArr = new byte[]{8, 0};
        byte[] qrArr = Base64.decodeBase64(aesUserKey.getSecret());
        byte[] lengthArr = DataUtil.shortToByteArray((short) (qrArr.length + cmdArr.length));
        long curTimeMill = System.currentTimeMillis();
        byte[] timeArr = DataUtil.longToByteArray(curTimeMill);
        byte[] resultArr = new byte[cmdArr.length + type.length + lengthArr.length + qrArr.length + timeArr.length];
        System.arraycopy(type, 0, resultArr, 0, type.length);
        System.arraycopy(lengthArr, 0, resultArr, type.length, lengthArr.length);
        System.arraycopy(cmdArr, 0, resultArr, type.length + lengthArr.length, cmdArr.length);
        System.arraycopy(qrArr, 0, resultArr, cmdArr.length + type.length + lengthArr.length, qrArr.length);
        System.arraycopy(timeArr, 0, resultArr, cmdArr.length + type.length + lengthArr.length + qrArr.length, timeArr.length);
        String resultStr = Base64.encodeBase64String(resultArr);
        
        auth.setQrKey(resultStr);
        doorAuthProvider.updateDoorAuth(auth);
        
        String nickName = getRealName(user);
        String homeUrl = configProvider.getValue(AclinkConstant.HOME_URL, "");
        List<Tuple<String, Object>> variables = smsProvider.toTupleList(AclinkConstant.SMS_VISITOR_USER, nickName);
        smsProvider.addToTupleList(variables, AclinkConstant.SMS_VISITOR_DOOR, doorAccess.getDisplayNameNotEmpty());
        
        //smsProvider.addToTupleList(variables, AclinkConstant.SMS_VISITOR_LINK, homeUrl+"/evh");
        LocaleTemplate lt = localeTemplateProvider.findLocaleTemplateByScope(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()), SmsTemplateCode.SCOPE,
        		SmsTemplateCode.ACLINK_VISITOR_MSG_CODE, user.getLocale());
        
        if(lt != null && lt.getDescription().indexOf("{link}") >= 0) {
        	smsProvider.addToTupleList(variables, AclinkConstant.SMS_VISITOR_LINK, homeUrl+"/evh");
        }
        
        smsProvider.addToTupleList(variables, AclinkConstant.SMS_VISITOR_ID, auth.getLinglingUuid());
        String templateLocale = user.getLocale();
        smsProvider.sendSms(cmd.getNamespaceId(), cmd.getPhone(), SmsTemplateCode.SCOPE, SmsTemplateCode.ACLINK_VISITOR_MSG_CODE, templateLocale, variables);
        
        return ConvertHelper.convert(auth, DoorAuthDTO.class);   
    }
    
    @Override
    public DoorAuthDTO createDoorVisitorAuth(CreateDoorVisitorCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        
        //first check is the phone is in black list
//        this.userService.checkSmsBlackList("doorVisitor", cmd.getPhone());
        
        if(blacklistService.checkUserPrivilege(namespaceId, cmd.getPhone(), PrivilegeConstants.MODULE_ACLINK_VISITOR_DENY)) {
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_SMS_BLACK_LIST,
                    "Hi guys, you are black list user.");    
        }
        
//        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        resolver.checkUserBlacklistAuthority(userId, ownerType, ownerId, PrivilegeConstants.BLACKLIST_NEWS);
        
        //验证操作者权限 by liuyilin 20180927
        User user = UserContext.current().getUser();
        QueryValidDoorAuthForeverCommand qryCmd = ConvertHelper.convert(getLevelQryCmdByUser(user), QueryValidDoorAuthForeverCommand.class);
        qryCmd.setDoorId(cmd.getDoorId());
        qryCmd.setRightVisitor(DoorAuthStatus.VALID.getCode());
        DoorAuth AprovAuth = doorAuthProvider.queryValidDoorAuthForever(qryCmd);
        if(AprovAuth == null || AprovAuth.getRightVisitor() != (byte) 1){
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE,
					AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR,
					localeStringService.getLocalizedString(String.valueOf(AclinkServiceErrorCode.SCOPE),
							String.valueOf(AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR),
							UserContext.current().getUser().getLocale(), "No permisssion"));
        }
        
        DoorAccessDriverType qrDriver = getQrDriverType(namespaceId);
        DoorAccessDriverType qrDriverExt = getQrDriverExt(namespaceId);
        if(qrDriver.equals(DoorAccessDriverType.LINGLING)) {
            return createLinglingVisitorAuth(cmd);
        } else if(qrDriver.equals(DoorAccessDriverType.HUARUN_ANGUAN)) {
        		return createHuarunDeviceQr(cmd);
        } else {
            if(qrDriverExt.equals(DoorAccessDriverType.ZUOLIN)) {
                return createZuolinDeviceQr(cmd);
            } else {
                //phone visitor
                return createZuolinPhoneQr(cmd);
            }
        }
    }
    
    private DoorAuthDTO createLinglingVisitorAuth(CreateDoorVisitorCommand cmd) {
        DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(cmd.getDoorId());
        if(doorAccess == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "DoorAccess not found");
        }
        
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        uuid = uuid.substring(0, 5);
        
        User user = UserContext.current().getUser();
        DoorAuth auth = new DoorAuth();
        auth.setApplyUserName(cmd.getUserName());
        auth.setApproveUserId(user.getId());
        auth.setAuthType(DoorAuthType.LINGLING_VISITOR.getCode());
        auth.setDescription(cmd.getDescription());
        auth.setDoorId(cmd.getDoorId());
        auth.setDriver(DoorAccessDriverType.LINGLING.getCode());
        auth.setOrganization(cmd.getOrganization());
        auth.setPhone(cmd.getPhone());
        auth.setNickname(cmd.getUserName());
        auth.setKeyValidTime(System.currentTimeMillis() + KEY_TICK_ONE_DAY);
        auth.setValidFromMs(
                System.currentTimeMillis()
        );
        auth.setValidEndMs(System.currentTimeMillis() + KEY_TICK_ONE_DAY);
        
        List<DoorAccess> childs = null;
        
        if(doorAccess.getDoorType().equals(DoorAccessType.ACLINK_LINGLING.getCode())) {
            if(!doorAccess.getGroupid().equals(0l)) {
                doorAccess = doorAccessProvider.getDoorAccessById(doorAccess.getGroupid());
            } else {
                childs = new ArrayList<DoorAccess>();
                childs.add(doorAccess);
            }
        }
        
      if(childs == null) {
            childs = doorAccessProvider.listDoorAccessByGroupId(doorAccess.getId(), 32);    
        }
      
      if(childs == null) {
    	  if(LOGGER.isInfoEnabled()) {
    		  LOGGER.info("createLinglingVisitorAuth failed! childs is none");
    	  }
          return null;
      }
        
        List<Long> deviceIds = new ArrayList<Long>();
        for(DoorAccess child : childs) {
            Aclink ca = aclinkProvider.getAclinkByDoorId(child.getId());
            deviceIds.add(ca.getLinglingDoorId());
            }
        
        AclinkLinglingMakeSdkKey sdkKey = new AclinkLinglingMakeSdkKey();
        sdkKey.setDeviceIds(deviceIds);
        sdkKey.setKeyEffecDay(180l);
        Map<Long, String> keyMap = aclinkLinglingService.makeSdkKey(sdkKey);
        
        List<String> keys = new ArrayList<String>();
        for(String key : keyMap.values()) {
            keys.add(key);
        }
        
        AclinkLinglingQrCodeRequest qrRequest = new AclinkLinglingQrCodeRequest();
        qrRequest.setEffecNumber(this.configProvider.getLongValue(AclinkConstant.ACLINK_VISITOR_CNT, 6));
        qrRequest.setEndTime(24*60l);
        qrRequest.setStartTime(Long.toString(System.currentTimeMillis() - 5000));
        
        List<Long> floorIds = new ArrayList<Long>();
        floorIds.add(cmd.getDoorNumber());
        qrRequest.setFloorIds(floorIds);
        qrRequest.setAutoFloorId(cmd.getDoorNumber());
        qrRequest.setBaseFloorId(-3l);
        
        Aclink dAc = aclinkProvider.getAclinkByDoorId(cmd.getDoorId());
        qrRequest.setLingLingId(dAc.getDeviceName());
        
        //Normal user
        qrRequest.setUserType(0l);
        qrRequest.setStrKey("44332211");
        qrRequest.setSdkKeys(keys);
        AclinkLinglingQRCode qrCode = aclinkLinglingService.getQrCode(qrRequest);
        
        auth.setLinglingUuid(uuid + "-" + qrCode.getCodeId().toString());
        auth.setQrKey(qrCode.getQrcodeKey());
        auth.setUserId(0l);
        auth.setOwnerType(doorAccess.getOwnerType());
        auth.setOwnerId(doorAccess.getOwnerId());
        auth.setStatus(DoorAuthStatus.VALID.getCode());
        auth.setCurrStorey(cmd.getDoorNumber());
        doorAuthProvider.createDoorAuth(auth);
        
        if(LOGGER.isInfoEnabled()) {
  		  LOGGER.info("createLinglingVisitorAuth begin send sms");
  	  	}
        
        String nickName = user.getNickName();
        if(nickName == null || nickName.isEmpty()) {
            nickName = user.getAccountName();
        }
        
        String homeUrl = configProvider.getValue(AclinkConstant.HOME_URL, "");
        List<Tuple<String, Object>> variables = smsProvider.toTupleList(AclinkConstant.SMS_VISITOR_USER, nickName);
        smsProvider.addToTupleList(variables, AclinkConstant.SMS_VISITOR_DOOR, doorAccess.getDisplayNameNotEmpty());
        smsProvider.addToTupleList(variables, AclinkConstant.SMS_VISITOR_LINK, homeUrl+"/evh");
        smsProvider.addToTupleList(variables, AclinkConstant.SMS_VISITOR_ID, auth.getLinglingUuid());
        String templateLocale = user.getLocale();
        smsProvider.sendSms(cmd.getNamespaceId(), cmd.getPhone(), SmsTemplateCode.SCOPE, SmsTemplateCode.ACLINK_VISITOR_MSG_CODE, templateLocale, variables);
        
        return ConvertHelper.convert(auth, DoorAuthDTO.class);
    }
    
    private String getWebQrByAuthId(Integer namespaceId, User user, DoorAccess doorAccess, DoorAuth auth, AesUserKey aesUserKey) {
        if(doorAccess.getOwnerType() != null && doorAccess.getDoorType() != null 
                && ( doorAccess.getDoorType().equals(DoorAccessType.ZLACLINK_WIFI.getCode())
                        || doorAccess.getDoorType().equals(DoorAccessType.ZLACLINK_NOWIFI.getCode())
                        || doorAccess.getDoorType().equals(DoorAccessType.ACLINK_ZL_GROUP.getCode())
                        || doorAccess.getDoorType().equals(DoorAccessType.ZLACLINK_WIFI.getCode()) ) ) {
            DoorAccessDriverType driverType = getQrDriverZuolinInner(namespaceId);
            if(driverType == DoorAccessDriverType.ZUOLIN) {
                if(aesUserKey == null) {
                    aesUserKey = generateAesUserKey(user, auth);    
                }
                if(aesUserKey == null) {
                    return null;
                    }
                return createZuolinQrV1(aesUserKey.getSecret());
                
            } else if (driverType == DoorAccessDriverType.ZUOLIN_V2) {
                if(aesUserKey == null) {
                    aesUserKey = generateAesUserKey(user, auth);    
                }
                if(aesUserKey == null) {
                    return null;
                    }
                Long qrImageTimeout = this.configProvider.getLongValue(UserContext.getCurrentNamespaceId(), AclinkConstant.ACLINK_QR_IMAGE_TIMEOUTS, 10*60l);
                byte[] qrArr = Base64.decodeBase64(aesUserKey.getSecret());
                return AclinkUtils.createZlQrCodeForFlapDoor(qrArr, System.currentTimeMillis(), qrImageTimeout*1000l);
            }
            
        }
        
        return null;
    }
    
    private GetVisitorResponse getVisitorByAuth(DoorAuth auth) {
        //获取访客的二维码信息
        GetVisitorResponse resp = new GetVisitorResponse();
        
        DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(auth.getDoorId());
        if(doorAccess != null) {
            resp.setDoorName(doorAccess.getDisplayNameNotEmpty());
        }
        
        resp.setPhone(auth.getPhone());
        if(auth.getValidFromMs() > System.currentTimeMillis() || auth.getValidEndMs() < System.currentTimeMillis() || (auth.getAuthRuleType() != null && (byte) 1 == auth.getAuthRuleType() && auth.getValidAuthAmount() <= 0)) {
            resp.setIsValid((byte)0);    
        } else {
            resp.setIsValid((byte)1);
            //授权有效才传二维码 by liuyilin 20180813
            resp.setQr(auth.getQrKey());
        }
        resp.setDescription(auth.getDescription());
        resp.setValidDay(1l);
        resp.setOrganization(auth.getOrganization());
        resp.setUserName(auth.getNickname());
        resp.setValidEndMs(auth.getValidEndMs());
        resp.setQrIntro(this.configProvider.getValue(UserContext.getCurrentNamespaceId(), AclinkConstant.ACLINK_QR_IMAGE_INTRO, AclinkConstant.QR_INTRO_URL));
        resp.setValidFromMs(auth.getValidFromMs());
        resp.setValidAuthAmount(auth.getValidAuthAmount());
        
        User user = userProvider.findUserById(auth.getApproveUserId());
        if(user != null) {
            resp.setApproveName(user.getNickName());
        }
        
        resp.setCreateTime(auth.getCreateTime().getTime());
        Integer namespaceId = null;
        if(doorAccess.getOwnerType() != null && doorAccess.getDoorType() != null 
                && ( doorAccess.getDoorType().equals(DoorAccessType.ZLACLINK_WIFI.getCode())
                        || doorAccess.getDoorType().equals(DoorAccessType.ZLACLINK_NOWIFI.getCode())
                        || doorAccess.getDoorType().equals(DoorAccessType.ACLINK_ZL_GROUP.getCode())
                        || doorAccess.getDoorType().equals(DoorAccessType.ZLACLINK_WIFI.getCode()) ) ) {
            
            if(doorAccess.getOwnerType().equals(DoorAccessOwnerType.COMMUNITY.getCode())) {
                Community community = communityProvider.findCommunityById(doorAccess.getOwnerId());
                if(community != null) {
                    namespaceId =  community.getNamespaceId();    
                }
                
            } else if(doorAccess.getOwnerType().equals(DoorAccessOwnerType.ENTERPRISE.getCode())) {
                Organization org = organizationProvider.findOrganizationById(doorAccess.getOwnerId());
                if(org != null) {
                    namespaceId = org.getNamespaceId();
                }
            }
            
        }
        
        if(namespaceId != null) {
            DoorAccessDriverType driverType = getQrDriverZuolinInner(namespaceId);
            DoorAccessDriverType qrDriverExt = getQrDriverExt(namespaceId);
            Namespace namespace = namespaceProvider.findNamespaceById(namespaceId);
            if(namespace != null) {
                resp.setNamespaceName(namespace.getName());    
            }
            
            //ZUOLIN_V2按次开门的二维码不转成9号指令,按次二维码防截图后面再做 by liuyilin 20180717
            if(resp.getIsValid() == (byte)1 &&  driverType == DoorAccessDriverType.ZUOLIN_V2 && qrDriverExt == DoorAccessDriverType.ZUOLIN && (auth.getAuthRuleType() == null || auth.getAuthRuleType() == DoorAuthRuleType.DURATION.getCode())) {
                byte[] origin = Base64.decodeBase64(auth.getQrKey());
                byte[] qrLenArr = new byte[2];
                qrLenArr[0] = origin[2];
                qrLenArr[1] = origin[3];
                int qrLen = (int)DataUtil.byteToShort(qrLenArr);
                qrLen -= 2;
                byte[] qrArr = new byte[qrLen];
                System.arraycopy(origin, 6, qrArr, 0, qrLen);
                Long qrImageTimeout = this.configProvider.getLongValue(UserContext.getCurrentNamespaceId(), AclinkConstant.ACLINK_QR_IMAGE_TIMEOUTS, 10*60l);
                resp.setQr(AclinkUtils.createZlQrCodeForFlapDoor(qrArr, System.currentTimeMillis(), qrImageTimeout*1000l));
            }
        }
        
        return resp;
    }
    
    @Override
    public GetVisitorResponse getVisitor(GetVisitorCommand cmd) {
        DoorAuth auth = doorAuthProvider.getLinglingDoorAuthByUuid(cmd.getId());
        if(auth == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, "auth not found");
        }
        
        return getVisitorByAuth(auth);
    }
    
    @Override
    public GetVisitorResponse getVisitorPhone(GetVisitorCommand cmd) {
        GetVisitorResponse resp = new GetVisitorResponse();
        DoorAuth auth = doorAuthProvider.getLinglingDoorAuthByUuid(cmd.getId());
        if(auth == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, "auth not found");
        }
        
        DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(auth.getDoorId());
        if(doorAccess != null) {
            resp.setDoorName(doorAccess.getDisplayNameNotEmpty());
        }
        
        resp.setPhone(auth.getPhone());
        if(auth.getValidEndMs() > System.currentTimeMillis()) {
            resp.setIsValid((byte)1);    
        } else {
            resp.setIsValid((byte)0);
        }
        resp.setDescription(auth.getDescription());
        resp.setValidDay(1l);
        resp.setOrganization(auth.getOrganization());
        resp.setUserName(auth.getNickname());
        resp.setValidEndMs(auth.getValidEndMs());
        
        User user = userProvider.findUserById(auth.getApproveUserId());
        if(user != null) {
            resp.setApproveName(user.getNickName());
        }
        
        //https://core.zuolin.com/evh/aclink/phv?id=10ae5-15016
        String homeUrl = configProvider.getValue(AclinkConstant.HOME_URL, "");
        String url = homeUrl + "/evh/aclink/phv?phvid=" + cmd.getId() + "#sign_suffix";
        
        resp.setCreateTime(auth.getCreateTime().getTime());
        //resp.setQr(Base64.encodeBase64String(url.getBytes()));
        resp.setQr(url);
      
        return resp;
    }
    
    private String getRealName(User user) {
        List<OrganizationSimpleDTO> organizationDTOs = organizationService.listUserRelateOrgs(null, user);
      if (organizationDTOs != null && organizationDTOs.size() > 0 
              && organizationDTOs.get(0).getContactName() != null 
              && !organizationDTOs.get(0).getContactName().isEmpty()) {
          return organizationDTOs.get(0).getContactName();
      }
      
      if(user.getNickName() == null || user.getNickName().isEmpty()) {
          return user.getAccountName();
      }
      
      return user.getNickName();
    }
    
    @Override
    public GetVisitorResponse checkVisitor(GetVisitorCommand cmd) {
        GetVisitorResponse resp = new GetVisitorResponse();
        DoorAuth auth = doorAuthProvider.getLinglingDoorAuthByUuid(cmd.getId());
        if(auth == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, "auth not found");
        }
        
        Integer namespaceId = cmd.getNamespaceId();
        if(namespaceId == null) {
            namespaceId = UserContext.getCurrentNamespaceId();
        }
        
        User user = userProvider.findUserById(auth.getApproveUserId());
        if(user != null) {
            List<OrganizationSimpleDTO> organizationDTOs = organizationService.listUserRelateOrgs(null, user);
//          OrganizationMember om = organizationProvider.findOrganizationMemberByUIdAndOrgId(ui.getId(), ctx.getFlowGraph().getFlow().getOrganizationId());
          if (organizationDTOs != null && organizationDTOs.size() > 0 
                  && organizationDTOs.get(0).getContactName() != null 
                  && !organizationDTOs.get(0).getContactName().isEmpty()) {
              String org = organizationDTOs.get(0).getName();
              String fmt = String.format("%s-%s", org, organizationDTOs.get(0).getContactName());
              user.setNickName(fmt);
              if(LOGGER.isInfoEnabled()) {
                  LOGGER.info("visitor nickname=", fmt);    
              }
              
          } 
          
          resp.setApproveName(user.getNickName());
        }

        DoorUserPermission dp = doorUserPermissionProvider.checkPermission(namespaceId, UserContext.current().getUser().getId()
                , auth.getOwnerId(), auth.getOwnerType());
        if(dp == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, "no permission");
//            resp.setPermissionDeny((byte)1);
        } else {
            resp.setPermissionDeny((byte)0);
        }
        
        DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(auth.getDoorId());
        if(doorAccess != null) {
            resp.setDoorName(doorAccess.getDisplayNameNotEmpty());
        }
        
        resp.setPhone(auth.getPhone());
        if(auth.getValidEndMs() > System.currentTimeMillis()) {
            resp.setIsValid((byte)1);    
        } else {
            resp.setIsValid((byte)0);
        }
        resp.setId(auth.getId());
        resp.setDoorId(auth.getDoorId());
        resp.setDescription(auth.getDescription());
        resp.setValidDay(1l);
        resp.setOrganization(auth.getOrganization());
        resp.setUserName(auth.getNickname());
        resp.setValidEndMs(auth.getValidEndMs());
        
        resp.setCreateTime(auth.getCreateTime().getTime());
      
        return resp;
    }
    
    @Override
    public DoorMessage queryWifiMgmtMessage(AclinkMgmtCommand cmd) {
        DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(cmd.getDoorId());
        if(doorAccess == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "doorAccess not found");
        }
        
        DoorMessage doorMessage = new DoorMessage();
        doorMessage.setDoorId(cmd.getDoorId());
        doorMessage.setSeq(0l);
        doorMessage.setMessageType(DoorMessageType.NORMAL.getCode());
        
        AclinkMessage body = new AclinkMessage();
        //by liuyilin 20180426 设置wifi时设置ip地址
        if(cmd.getServerId() == 0L){
        	//外网服务器,不传serverId
        	String borderUrl = "";

            List<Border> borders = borderProvider.listAllBorders();
            if(borders == null || borders.size() <= 0) {
                return null;
            }
            borderUrl = borders.get(0).getPublicAddress();
            if(borders.get(0).getPublicPort().equals(443)) {
                borderUrl = "wss://" + borderUrl + "/aclink";
            } else {
                borderUrl = "ws://" + borderUrl + ":" + borders.get(0).getPublicPort() + "/aclink";
            }

            AesServerKey aesServerKey = aesServerKeyService.getCurrentAesServerKey(cmd.getDoorId());
            if(aesServerKey == null) {
                throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "doorAccess key error");
            }


            body.setCmd((byte)0xb);
            body.setSecretVersion(aesServerKey.getDeviceVer());
            body.setEncrypted(AclinkUtils.packWifiCmd(aesServerKey.getDeviceVer(), aesServerKey.getSecret(), cmd.getWifiSsid(), cmd.getWifiPwd(), borderUrl));
        }else{
        	//内网服务器,根据serverId查服务器IP地址
        	AclinkServer server = aclinkServerProvider.findServerById(cmd.getServerId());
            if(server == null){
            	throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_SERVER_NOT_FOUND, "server not found");
            }
            if(server.getIpAddress() == null){
            	throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_SERVER_NOT_FOUND, "ip未设置,请同步服务器");
            }


            AesServerKey aesServerKey = aesServerKeyService.getCurrentAesServerKey(cmd.getDoorId());
            if(aesServerKey == null) {
                throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "doorAccess key error");
            }

            body.setCmd((byte)0xb);
            body.setSecretVersion(aesServerKey.getDeviceVer());
            body.setEncrypted(AclinkUtils.packWifiCmd(aesServerKey.getDeviceVer(), aesServerKey.getSecret(), cmd.getWifiSsid(), cmd.getWifiPwd(),"ws://" + server.getIpAddress() + ":8000/aclink"));

            doorMessage.setBody(body);
        }

        
        doorMessage.setBody(body);
        
        return doorMessage;
    }
    
    @Override
    public  ListDoorAccessResponse listDoorAccessGroup(ListDoorAccessGroupCommand cmd) {
        QueryDoorAccessAdminCommand newCmd = (QueryDoorAccessAdminCommand)ConvertHelper.convert(cmd, QueryDoorAccessAdminCommand.class);
        //TODO read from configuration
        //newCmd.setDoorType(DoorAccessType.ACLINK_LINGLING_GROUP.getCode());
        
        //Search only groups
        newCmd.setGroupId(-1l);
        newCmd.setSearch(cmd.getSearch());
        return searchDoorAccessByAdmin(newCmd);
    }
    
    @Override
    public AclinkFirmwareDTO getCurrentFirmware(GetCurrentFirmwareCommand cmd) {
        AclinkFirmware firm = aclinkFirmwareProvider.queryAclinkFirmwareMax();
        return ConvertHelper.convert(firm, AclinkFirmwareDTO.class);
    }
    
    @Override
    public ListDoorAuthResponse createDoorAuthList(AclinkCreateDoorAuthListCommand cmd) {
        ListDoorAuthResponse resp = new ListDoorAuthResponse();
        
        List<DoorAuthDTO> dtos = new ArrayList<DoorAuthDTO>();
        resp.setDtos(dtos);

        for(CreateDoorAuthCommand authCmd: cmd.getAuths()) {
            DoorAuthDTO dto = createDoorAuth(authCmd);
            dtos.add(dto);
        }
        
        return resp;
    }

    @Override
    public String checkAllDoorAuthList(){
        String key = String.format(DOOR_AUTH_ALL_USER, UserContext.getCurrentNamespaceId());
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        Object v = redisTemplate.opsForValue().get(key);
        if(null == v){
            return null;
        }
        return v.toString();
    }

    @Override
    public ListDoorAuthResponse createAllDoorAuthList(AclinkCreateAllDoorAuthListCommand cmd) {
        ListDoorAuthResponse resp = new ListDoorAuthResponse();
        User u = UserContext.current().getUser();
        List<DoorAuthDTO> dtos = new ArrayList<>();
        resp.setDtos(dtos);

        ListAclinkUserCommand userCmd = ConvertHelper.convert(cmd, ListAclinkUserCommand.class);
        userCmd.setPageSize(1000);

        String key = String.format(DOOR_AUTH_ALL_USER, UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        ExecutorUtil.submit(new Runnable() {
            @Override
            public void run() {
                UserContext.setCurrentUser(u);
                String v = "" + System.currentTimeMillis();
                redisTemplate.opsForValue().set(key, v, 30, TimeUnit.MINUTES);
                LOGGER.debug("start door auth. startTime = {}", v);
                while (true){
                    AclinkUserResponse userRes = listAclinkUsers(userCmd);
                    for (AclinkUserDTO user: userRes.getUsers()) {
                        CreateDoorAuthCommand authCmd = ConvertHelper.convert(cmd, CreateDoorAuthCommand.class);
                        authCmd.setUserId(user.getId());
                        authCmd.setApproveUserId(u.getId());
                        DoorAuthDTO dto = createDoorAuth(authCmd);
                        dtos.add(dto);
                    }
                    if(null == userRes.getNextPageAnchor()){
                        break;
                    }
                    userCmd.setPageAnchor(userRes.getNextPageAnchor());
                }
                redisTemplate.delete(key);
                LOGGER.debug("end door auth. endTime = {}", System.currentTimeMillis());
            }
        });

        return resp;
    }
    
    private void remoteOpenDoor(Long doorAuthId, String uuid){
		User user = UserContext.current().getUser();

		DoorAuth doorAuth = doorAuthProvider.getDoorAuthById(doorAuthId);
		if (doorAuth == null /* || !doorAuth.getUserId().equals(user.getId()) */ || !doorAuth.getRightRemote().equals((byte) 1)) {
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE,
					AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, "DoorAuth error");
		}

		DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(doorAuth.getDoorId());
		if (doorAccess == null) {
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE,
					AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "Door not found");
		}

		// DoorAuth auth = doorAuthProvider.queryValidDoorAuthForever(doorId,
		// user.getId(), null, null, (byte)1);
		AesUserKey aesUserKey = generateAesUserKey(user, doorAuth);
		if (aesUserKey == null) {
			LOGGER.error("remote AesUserKey created error");
			return;
		}

		byte[] secret = Base64.decodeBase64(aesUserKey.getSecret());
		LOGGER.error(StringHelper.toHexString(secret));
		byte[] bPayload = CmdUtil.openDoorCmd(secret);

		byte[] bSeq = DataUtil.intToByteArray(0);
		byte[] bLen = DataUtil.intToByteArray(bPayload.length + 6);
		byte[] mBuf = new byte[bPayload.length + 10];

		System.arraycopy(bLen, 0, mBuf, 0, bLen.length);
		System.arraycopy(bSeq, 0, mBuf, 6, bSeq.length);
		System.arraycopy(bPayload, 0, mBuf, 10, bPayload.length);

		AclinkRemotePdu pdu = new AclinkRemotePdu();

		// by liuyilin 20180420 判断门禁是否为内网门禁,消息拼装
		if (doorAccess.getLocalServerId() != null && doorAccess.getLocalServerId() > 0) {
			Base64 base64 = new Base64();
			try {
				String str = "{\"remote_open\":\"" + String.valueOf(doorAccess.getUuid()) + "\",\"msg\":\""
						+ Base64.encodeBase64String(mBuf) + "\"}";
				byte[] textByte;
				textByte = str.getBytes("UTF-8");
				String encodedText = base64.encodeToString(textByte);
				pdu.setBody(encodedText);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			pdu.setType(1);
			pdu.setUuid(aclinkServerService.findLocalServerById(doorAccess.getLocalServerId()).getUuidNum());
		} else {
			pdu.setBody(Base64.encodeBase64String(mBuf));
			pdu.setType(1);
			pdu.setUuid(uuid == null ? doorAccess.getUuid() : uuid);
		}

		long requestId = LocalSequenceGenerator.getNextSequence();
		borderConnectionProvider.broadcastToAllBorders(requestId, pdu);
    }
    
    @Override
    public void remoteOpenDoor(Long doorAuthId) {
        remoteOpenDoor(doorAuthId, null);
    }
    
    @Override
    public void remoteOpenDoor(String hardwareId) {
    	User user = UserContext.current().getUser();
    	
        DoorAccess doorAccess = doorAccessProvider.queryDoorAccessByHardwareId(hardwareId.toUpperCase());
        if(doorAccess == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "Door not found");
        }
        
        DoorAuth doorAuth = doorAuthProvider.queryValidDoorAuthByDoorIdAndUserId(doorAccess.getId(), user.getId(), (byte)1);
        if(doorAuth == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, "DoorAuth error");
        }
        
        remoteOpenDoor(doorAuth.getId());
    }
    
    private List<Long> getDoorListbyUser(User user, DoorAccess doorAccess, Long curr) {
        List<OrganizationDTO> orgs = organizationService.listUserRelateOrganizations(0, user.getId(), OrganizationGroupType.ENTERPRISE);
        List<Long> floors = new ArrayList<Long>();
        Map<Long, Long> floorMap = new HashMap<Long, Long>();
        
        if(doorAccess.isVip()) {
            floorMap.put(3l, 1l);
            floorMap.put(-3l, 1l);
            floorMap.put(1l, 1l);
        }
        
        for(OrganizationDTO dto : orgs) {
            List<OrganizationAddress> addrs = organizationProvider.findOrganizationAddressByOrganizationId(dto.getId());
            for(OrganizationAddress addr  : addrs) {
                Address addr2 = addressProvider.findAddressById(addr.getAddressId());
                
                try {
                    if(addr2.getApartmentFloor() != null) {
                        Long l = Long.parseLong(addr2.getApartmentFloor());
                        floorMap.put(l, 1l);
                    } else {
                        String aname = addr2.getAddress();
                        String[] as = aname.split("-");
                        if(as.length > 1) {
                            aname = as[1];
                        } else {
                            aname = as[0];
                            }
                        
                        Matcher m = npattern.matcher(aname);
                        if(m != null && m.find()) {
                            aname = m.group(0);
                            Long l = Long.parseLong(aname);
                            if(l >= -255 && l <= 255) {
                                floorMap.put(l, 1l);
                                }
                            
                        }
                    }
                    
                } catch(Exception ex) {
                    LOGGER.error("error  for get apartment floor", ex);
                }
            }
        }
        
        if(floorMap.get(curr) != null) {
            floorMap.forEach((key, value) -> {
                if(!key.equals(curr)) {
                    floors.add(key);
                    }
                });
            
            Collections.sort(floors);
         
            //Set to end
            floors.add(0, curr);
            
        } else {
            floorMap.forEach((key, value) -> {
                floors.add(key);
            });
            Collections.sort(floors);
        }
        
        return floors;
    }
    
    @Override
    public ListDoorAccessQRKeyResponse updateAndQueryQR(AclinkUpdateLinglingStoreyCommand cmd) {
        DoorAuth auth = this.doorAuthProvider.getDoorAuthById(cmd.getAuthId());
        if(auth == null) {
            return listDoorAccessQRKey();
        }
        
        if(auth.getCurrStorey() == null || !auth.getCurrStorey().equals(cmd.getNewStorey())) {
            auth.setCurrStorey(cmd.getNewStorey());
            this.doorAuthProvider.updateDoorAuth(auth);
        }
        return listDoorAccessQRKey();
    }
    
    @Override
    public GetShortMessageResponse getShortMessages(GetShortMessageCommand cmd) {
        GetShortMessageResponse resp = new GetShortMessageResponse();
        resp.setMessages(new ArrayList<String>());
        
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        
        String msg = this.configProvider.getValue(namespaceId, AclinkConstant.ACLINK_VISITOR_SHORTS, "");
        String[] msgs = msg.split("\\|");
        for(String m : msgs) {
            resp.getMessages().add(m);
        }
        
        return resp;
    }
    
    @Override
    public AclinkLogListResponse createAclinkLog(AclinkLogCreateCommand cmds) {
        AclinkLogListResponse resp = new AclinkLogListResponse();
        resp.setDtos(new ArrayList<AclinkLogDTO>());
        for(int i = 0; i < cmds.getItems().size(); i++) {
            AclinkLogItem cmd = cmds.getItems().get(i);
            AclinkLog aclinkLog = ConvertHelper.convert(cmd, AclinkLog.class);
            
            try {
                DoorAuth doorAuth = doorAuthProvider.getDoorAuthById(cmd.getAuthId());
                if(cmd.getUserId() == null) {
                    cmd.setUserId(doorAuth.getUserId());
                } 
                Long userId = cmd.getUserId();
                if(userId == null || userId.equals(0l)) {
                	//保安授权方式
                	userId = UserContext.current().getUser().getId();
                	aclinkLog.setUserName(doorAuth.getNickname());
                	aclinkLog.setUserIdentifier(doorAuth.getPhone());
                	aclinkLog.setUserId(userId);
                } else {
                	UserInfo user = userService.getUserInfo(cmd.getUserId());
                	if(user.getPhones() != null && user.getPhones().size() > 0) {
                           aclinkLog.setUserIdentifier(user.getPhones().get(0));    
                       }
                	aclinkLog.setUserName(user.getNickName());
                }
                
                DoorAccess door = doorAccessProvider.getDoorAccessById(doorAuth.getDoorId());
                
                aclinkLog.setDoorName(door.getDisplayNameNotEmpty());
                
                aclinkLog.setHardwareId(door.getHardwareId());
                aclinkLog.setOwnerId(door.getOwnerId());
                aclinkLog.setOwnerType(door.getOwnerType());
                aclinkLog.setDoorType(door.getDoorType());
             
                
                aclinkLogProvider.createAclinkLog(aclinkLog);
                AclinkLogDTO dto = ConvertHelper.convert(aclinkLog, AclinkLogDTO.class);
                if(dto != null) {
                    resp.getDtos().add(dto);
                }
            } catch(Exception ex) {
                LOGGER.error("aclinklog error i=" + i, ex);
            }
        }
        
        return resp;
        
    }
    
    @Override
    public AclinkQueryLogResponse queryLogs(AclinkQueryLogCommand cmd) {
        //添加权限 by liqingyan
//        if (cmd.getOwnerType() != null && cmd.getOwnerType() == 1 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
//            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4112041122L, cmd.getAppId(), null, cmd.getCurrentProjectId());
//        }
        if (cmd.getOwnerType() != null && cmd.getOwnerType() == 0 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4111041112L, cmd.getAppId(), null, cmd.getCurrentProjectId());
        }
        AclinkQueryLogResponse resp = new AclinkQueryLogResponse();
        resp.setDtos(new ArrayList<AclinkLogDTO>());
        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<AclinkLogDTO> objs = aclinkLogProvider.queryAclinkLogDTOsByTime(locator, count, new ListingQueryBuilderCallback() {

            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                if(cmd.getOwnerType() != null) {
                    query.addConditions(Tables.EH_ACLINK_LOGS.OWNER_TYPE.eq(cmd.getOwnerType()));
                }
                if(cmd.getOwnerId() != null) {
                    query.addConditions(Tables.EH_ACLINK_LOGS.OWNER_ID.eq(cmd.getOwnerId()));
                }
                if(cmd.getEventType() != null) {
                    query.addConditions(Tables.EH_ACLINK_LOGS.EVENT_TYPE.eq(cmd.getEventType()));
                }
                if(cmd.getKeyword() != null) {
                    query.addConditions(Tables.EH_ACLINK_LOGS.USER_IDENTIFIER.like(cmd.getKeyword() + "%")
                            .or(Tables.EH_ACLINK_LOGS.USER_NAME.like(cmd.getKeyword()+"%")));
                }
                if(cmd.getDoorId() != null) {
                    query.addConditions(Tables.EH_ACLINK_LOGS.DOOR_ID.eq(cmd.getDoorId()));
                }
                //时间比较
                if(cmd.getStartTime() != null && cmd.getEndTime() != null ) {
                    cmd.setStartTime(DateHelper.parseDataString(DateHelper.getDateDisplayString(TimeZone.getTimeZone("GMT+:08:00"), cmd.getStartTime()),"yyyy-MM-dd").getTime());
                    cmd.setEndTime(DateHelper.parseDataString(DateHelper.getDateDisplayString(TimeZone.getTimeZone("GMT+:08:00"), cmd.getEndTime()),"yyyy-MM-dd").getTime() + 24*60*60*1000);
                    query.addConditions(Tables.EH_ACLINK_LOGS.CREATE_TIME.between(new Timestamp(cmd.getStartTime()), new Timestamp(cmd.getEndTime())));
                }
                return query;
            }
            
        });
        resp.setDtos(objs);
        resp.setNextPageAnchor(locator.getAnchor());
        return resp;
    }
     //add by liqingyan
    @Override
    public DoorStatisticResponse doorStatistic (DoorStatisticCommand cmd){
        //添加权限
//        if (cmd.getOwnerType() != null && cmd.getOwnerType() == 1 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
//            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4112041123L, cmd.getAppId(), null, cmd.getCurrentProjectId());
//        }
        if (cmd.getOwnerType() != null && cmd.getOwnerType() == 0 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4111041113L, cmd.getAppId(), null, cmd.getCurrentProjectId());
        }
        DoorStatisticResponse resp = new DoorStatisticResponse();
        resp.setDtos(new DoorStatisticDTO());
        DoorStatisticDTO objs =aclinkLogProvider.queryDoorStatistic(cmd);
        resp.setDtos(objs);
        return resp;
    }

    //add by liqingyan
    @Override
    public DoorStatisticByTimeResponse doorStatisticByTime (DoorStatisticByTimeCommand cmd){
        //添加权限
//        if (cmd.getOwnerType() != null && cmd.getOwnerType() == 1 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
//            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4112041123L, cmd.getAppId(), null, cmd.getCurrentProjectId());
//        }
        if (cmd.getOwnerType() != null && cmd.getOwnerType() == 0 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4111041113L, cmd.getAppId(), null, cmd.getCurrentProjectId());
        }
        DoorStatisticByTimeResponse resp = new DoorStatisticByTimeResponse();
        resp.setDtos(new ArrayList<DoorStatisticByTimeDTO>());
        List<DoorStatisticByTimeDTO> objs = aclinkLogProvider.queryDoorStatisticByTime(cmd);
        resp.setDtos(objs);
        return resp;
    }

    //add by liqingyan
    @Override
    public TempStatisticByTimeResponse tempStatisticByTime (TempStatisticByTimeCommand cmd){
        //添加权限
//        if (cmd.getOwnerType() != null && cmd.getOwnerType() == 1 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
//            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4112041123L, cmd.getAppId(), null, cmd.getCurrentProjectId());
//        }
        if (cmd.getOwnerType() != null && cmd.getOwnerType() == 0 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4111041113L, cmd.getAppId(), null, cmd.getCurrentProjectId());
        }
        TempStatisticByTimeResponse resp = new TempStatisticByTimeResponse();
        resp.setDtos(new ArrayList<TempStatisticByTimeDTO>());
        List<TempStatisticByTimeDTO> objs = aclinkLogProvider.queryTempStatisticByTime(cmd);
        resp.setDtos(objs);
        return resp;
    } 
      
    @Override
    public DoorAuth getLinglingDoorAuthByUuid(String uuid) {
        return doorAuthProvider.getLinglingDoorAuthByUuid(uuid);
    }
    
    @Override
    public DoorUserPermissionDTO createQRUserPermission(CreateQRUserPermissionCommand cmd) {
        if(cmd.getNamespaceId() == null) {
            cmd.setNamespaceId(0);
        }
        
        User u = UserContext.current().getUser();
        DoorUserPermission dp = new DoorUserPermission();
        dp.setApproveUserId(u.getId());
        dp.setAuthType((byte)0);
        dp.setDescription(cmd.getDescription());
        dp.setUserId(cmd.getUserId());
        dp.setOwnerId(cmd.getOwnerId());
        dp.setOwnerType(cmd.getOwnerType());
        dp.setNamespaceId(cmd.getNamespaceId());
        dp.setStatus((byte)1);
        doorUserPermissionProvider.createDoorUserPermission(dp);
        
        return ConvertHelper.convert(dp, DoorUserPermissionDTO.class);
    }
    
    @Override
    public DoorUserPermissionDTO deleteQRUserPermission(DeleteQRUserPermissionCommand cmd) {
        DoorUserPermission obj = doorUserPermissionProvider.getDoorUserPermissionById(cmd.getId());
        if(obj != null) {
            doorUserPermissionProvider.deleteDoorUserPermission(obj);
            return ConvertHelper.convert(obj, DoorUserPermissionDTO.class);
        }
        
        return null;
        
    }
    
    @Override
    public ListQRUserPermissionResponse listQRUserPermissions(ListQRUserPermissionCommand cmd) {
        ListQRUserPermissionResponse resp = new ListQRUserPermissionResponse();
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        List<DoorUserPermissionDTO> dtos = doorUserPermissionProvider.listDoorUserPermissions(cmd.getNamespaceId()
                , cmd.getOwnerId()
                , cmd.getOwnerType()
                , locator, count);
        resp.setDtos(dtos);
        resp.setNextPageAnchor(locator.getAnchor());
        return resp;
    }
    
    private void deleteAllAuths(Integer namespaceId, Long orgId, Long userId) {
		ListingLocator locator = new ListingLocator();
		int count = 100;
		List<DoorAuth> doorAuths = doorAuthProvider.queryValidDoorAuths(locator, userId, null, null, count);
		if(doorAuths == null || doorAuths.size() == 0) {
			LOGGER.info("deleteAllAuths. doorAuths not found, orgId=" + orgId + " userId=" + userId);
			return;
		}
		
		do {
			List<DoorAuth> dels = new ArrayList<DoorAuth>();
			for(DoorAuth doorAuth : doorAuths) {
				DoorAccessOwnerType ownerType = DoorAccessOwnerType.fromCode(doorAuth.getOwnerType());
				
				if(ownerType == DoorAccessOwnerType.COMMUNITY) {
					Community c = communityProvider.findCommunityById(doorAuth.getOwnerId());
					if(c != null && c.getNamespaceId().equals(namespaceId)) {
						doorAuth.setStatus(DoorAuthStatus.INVALID.getCode());
						dels.add(doorAuth);
					}
				} else if(ownerType == DoorAccessOwnerType.ENTERPRISE) {
					Organization org = organizationProvider.findOrganizationById(doorAuth.getOwnerId());
					if(org != null && org.getNamespaceId().equals(namespaceId)) {
						doorAuth.setStatus(DoorAuthStatus.INVALID.getCode());
						dels.add(doorAuth);
					}
				}
			}
			
			if(dels.size() > 0) {
				doorAuthProvider.updateDoorAuth(dels);
			}
			
			if(locator.getAnchor() != null) {
				doorAuths = doorAuthProvider.queryValidDoorAuths(locator, userId, null, null, count);	
			}
			
		} while (doorAuths != null && doorAuths.size() > 0 && locator.getAnchor() != null);
    }
    
    @Override
    public void deleteAuthWhenLeaveFromOrg(Integer namespaceId, Long orgId, Long userId) {
    	ListUserRelatedOrganizationsCommand cmd = new ListUserRelatedOrganizationsCommand();
    	User user = userProvider.findUserById(userId);
        if(null == user){
            LOGGER.info("user is null orgId=" + orgId + " userId=" + userId);
            return;
        }
    	List<OrganizationSimpleDTO> dtos = organizationService.listUserRelateOrgs(cmd, user);
    	if(dtos.isEmpty()) {
    		deleteAllAuths(namespaceId, orgId, userId);
    		LOGGER.info("delete all auths orgId=" + orgId + " userId=" + userId);
    	} else {
    		ListingLocator locator = new ListingLocator();
    		int count = 100;
    		List<DoorAuth> doorAuths = doorAuthProvider.queryValidDoorAuths(locator, userId, orgId, DoorAccessOwnerType.ENTERPRISE.getCode(), count);
    		if(doorAuths == null || doorAuths.size() == 0) {
    			LOGGER.info("has more orgs. doorAuths not found, orgId=" + orgId + " userId=" + userId);
    			return;
    		}
    		
    		do {
    			for(DoorAuth doorAuth : doorAuths) {
    				doorAuth.setStatus(DoorAuthStatus.INVALID.getCode());
    			}
    			doorAuthProvider.updateDoorAuth(doorAuths);
    		
    			if(locator.getAnchor() != null) {
    				doorAuths = doorAuthProvider.queryValidDoorAuths(locator, userId, orgId, DoorAccessOwnerType.ENTERPRISE.getCode(), count);	
    			}
    			
    		} while (doorAuths != null && doorAuths.size() > 0 && locator.getAnchor() != null);
    	}
    	
    	LOGGER.info("delete all auths ok! orgId=" + orgId + " userId=" + userId);
    }
    
    private long getQrTimeout() {
    	long tick = this.configProvider.getLongValue(UserContext.getCurrentNamespaceId(), AclinkConstant.ACLINK_USERKEY_TIMEOUTS, KEY_TICK_ONE_DAY/1000l);
    	tick = tick * 1000l;
    	if(tick < KEY_TICK_15_MINUTE) {
    		tick = KEY_TICK_15_MINUTE;
    	}
    	
    	return tick;
    }
    
    @Override
    public AclinkGetServerKeyResponse getServerKey(AclinkGetServerKeyCommand cmd) {
    	DoorAccess door = doorAccessProvider.queryDoorAccessByHardwareId(cmd.getHardwareId());
    	if(null == door) {
    		return null;
    	}
    	AclinkGetServerKeyResponse resp = ConvertHelper.convert(door, AclinkGetServerKeyResponse.class);
    	DoorAccessType accessType = DoorAccessType.fromCode(door.getDoorType());
    	if(accessType == DoorAccessType.ACLINK_ZL_GROUP) {
    		resp.setKey0(door.getAesIv());
    	} else if(DoorAccessType.ZLACLINK_WIFI == accessType || DoorAccessType.ZLACLINK_NOWIFI == accessType) {
    		AesServerKey aesServerKey = aesServerKeyService.getCurrentAesServerKey(door.getId());
         if(aesServerKey != null) {
        	 if(aesServerKey.getDeviceVer().equals((byte)0)) {
        		 resp.setKey0(aesServerKey.getSecret()); 
        	 } else {
        		 resp.setKey1(aesServerKey.getSecret());
        	 }
            }
    	}
    	
    	return resp;
    }
    
//    @Override
//    public void test() {
//        Long doorId = 231l;
//        CrossShardListingLocator locator = new CrossShardListingLocator();
//        List<Long> userIds = doorAuthProvider.listDoorAuthByBuildingName2((byte)1, doorId, 240111044331051300l, "A1", locator, 20, 999992);
//        for(Long userId : userIds) {
//            OrganizationMemberDetails detail = organizationProvider.findOrganizationMemberDetailsByTargetId(userId); 
//            DoorAuth auth = doorAuthProvider.queryValidDoorAuthByDoorIdAndUserId(doorId, userId);
//            LOGGER.info("auth=" + auth + " detail=" + detail);
//        }
//        
//    }
    
//    private void testEncrypt() {
//        try {
//            String aesServerKey = "s87SHk+R/IOw6dV7QkX/pA==";
//            String aesUserKey = "mf8eLAiV+bbmo6egNjsCzw==";
//            byte[] serverKey = Base64.decodeBase64(aesServerKey);
//            byte[] userKey = Base64.decodeBase64(aesUserKey);
//            SecretKeySpec skeySpec = new SecretKeySpec(serverKey, "AES");
//            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
//            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
//            byte[] rlt = cipher.doFinal(userKey);
//            
//            byte[] time = new byte[4];
//            System.arraycopy(rlt, 0, time, 0, 4);
//         
//            long timeL = DataUtil.byteArrayToInt(time);
//            LOGGER.info("time=" + timeL);
//            Date dt = new Date(timeL * 1000);
//            Date et = new Date(1476327557913l);
//            Date ct = new Date(1476317477917l);
//            
//            ct = new Date(1476327557913l + KEY_TICK_7_DAY);
////            LOGGER.info("date=" + DateHelper.format(dt) + " create=" + DateHelper.format(ct) + " expire=" + DateHelper.format(et));
//            LOGGER.info(StringHelper.toHexString(rlt));
//            
//        } catch(Exception ex) {
//            
//        }        
//    }

	@Override
	public Action onLocalBusMessage(Object arg0, String arg1, Object arg2,
			String arg3) {
        //Must be
        try {
        	//以前的平台包会在监听到publish之前就执行完update,所以没有问题;更新之后会在事务提交前就监听到,如果另起一个线程,查到的是更新前的数据,授权失败;暂改成同步执行  by liuyilin 20180827
			LOGGER.info("start run.....");
			 Long id = (Long)arg2;
	         if(null == id) {
	              LOGGER.error("None of UserIdentifier");
	         } else {
	        	 if(LOGGER.isDebugEnabled()) {
	        		 LOGGER.debug("newUserAutoAuth id= " + id); 
	        	 }
	              
              try {
            	  newUserAutoAuth(id);
              } catch(Exception exx) {
            	  LOGGER.error("execute promotion error promotionId=" + id, exx);
            	  }

	         }
        } catch(Exception e) {
            LOGGER.error("onLocalBusMessage error ", e);
        } finally{
        	
        }

        return Action.none;
	}
	
	private void newUserAutoAuth(Long identifierId) {
		UserIdentifier identifier = userProvider.findIdentifierById(identifierId);
		if(identifier.getClaimStatus().equals(IdentifierClaimStatus.CLAIMED.getCode())) {
			String mac = this.configProvider.getValue(identifier.getNamespaceId(), AclinkConstant.ACLINK_NEW_USER_AUTO_AUTH, "");
			if(mac == null || mac.isEmpty()) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("identifier not found, mac=" + mac + " claimed=" + identifier);	
				}	
				return;
			}
			DoorAccess doorAccess = doorAccessProvider.queryDoorAccessByHardwareId(mac);
			if(doorAccess == null) {
				LOGGER.warn("aclink auto auth failed mac=" + mac + " claimed=" + identifier);
				return;
			}
			
			CreateDoorAuthCommand cmd = new CreateDoorAuthCommand();
			cmd.setApproveUserId(User.SYSTEM_UID);
			cmd.setAuthMethod(DoorAuthMethodType.ADMIN.getCode());
			cmd.setAuthType(DoorAuthType.FOREVER.getCode());
			cmd.setDescription("new user auto created");
			cmd.setDoorId(doorAccess.getId());
			cmd.setNamespaceId(UserContext.getCurrentNamespaceId());
			cmd.setPhone(identifier.getIdentifierToken());
			cmd.setRightOpen((byte)1);
			cmd.setRightRemote((byte)1);
			cmd.setUserId(identifier.getOwnerUid());
			createDoorAuth(cmd);
		} else {
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("identifier not found, identifierId=" + identifierId + " claimed=" + identifier);	
			}
		}
	}
	
	private void joinCompanyAutoAuthLevel(Integer namespaceId, Long orgId, Long userId) {
	    List<DoorAuthLevel> lvls = doorAuthLevelProvider.findAuthLevels(orgId, DoorAccessOwnerType.ENTERPRISE.getCode());
	    if(lvls != null && lvls.size() > 0) {
	        UserInfo userInfo = userService.getUserSnapshotInfoWithPhone(userId);
	        
	        for(DoorAuthLevel lv : lvls) {
	            if(userInfo != null){
	                CreateDoorAuthCommand cmd = new CreateDoorAuthCommand();
	                cmd.setApproveUserId(User.SYSTEM_UID);
	                cmd.setAuthMethod(DoorAuthMethodType.ADMIN.getCode());
	                cmd.setAuthType(DoorAuthType.FOREVER.getCode());
	                cmd.setDescription("new user auto created");
	                cmd.setDoorId(lv.getDoorId());
	                cmd.setNamespaceId(namespaceId);
	                if(userInfo.getPhones() != null && userInfo.getPhones().size() > 0) {
	                    cmd.setPhone(userInfo.getPhones().get(0));
	                }
	                cmd.setRightOpen((byte)1);
	                cmd.setRightRemote(lv.getRightRemote());
	                cmd.setRightVisitor(lv.getRightVisitor());
	                cmd.setUserId(userId);
	                createDoorAuth(cmd);
	            }
	        }
	    }
	}
	
//	private void joinCompanyAutoAuthLevel(Integer namespaceId, Long userId) {
//		//TODO 查询挪到组织架构
//		List<OrganizationMember> orgs = doorAccessProvider.listOrgsByUserId(namespaceId, userId);
//		if(orgs != null && orgs.size() > 0){
//			for(OrganizationMember org : orgs){
//				joinCompanyAutoAuth(namespaceId,org.getId(),userId);
//			}
//		}
//	}
	
	@Override
	public void joinCompanyAutoAuth(Integer namespaceId, Long orgId, Long userId) {
        //检测userId是否合法
        if(userId == null || userId == 0){
            return;
        }
        
        joinCompanyAutoAuthLevel(namespaceId, orgId, userId);

		String info = this.configProvider.getValue(namespaceId, AclinkConstant.ACLINK_JOIN_COMPANY_AUTO_AUTH, "");
		if(info == null || info.isEmpty()) {
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("join company info not found, info=" + info + " orgId=" + orgId + " userId=" + userId);	
			}	
			return;
		}
		String[] iters = info.split(";");
		for(String s : iters) {
			String[] infos = s.split(",");
			if(infos.length != 3) {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("join company info error, info=" + info + " orgId=" + orgId + " userId=" + userId);	
				}	
				return;
			}
			if(infos[0].equals("building")) {
				Long buildingId = Long.valueOf(infos[1]);
				List<OrganizationAddress> addresses = this.organizationProvider.findOrganizationAddressByOrganizationIdAndBuildingId(orgId, buildingId);
				if(addresses == null || addresses.isEmpty()) {
					if(LOGGER.isInfoEnabled()) {
						LOGGER.info("join company info address not found, info=" + info + " orgId=" + orgId + " userId=" + userId);	
					}
					continue;
				}
				
			} else if(infos[0].equals("company")) {
				if(!orgId.equals(Long.valueOf(infos[1]))) {
					if(LOGGER.isInfoEnabled()) {
						LOGGER.info("join company info company not found, info=" + info + " orgId=" + orgId + " userId=" + userId);	
					}
					continue;
				}
			} else {
				if(LOGGER.isInfoEnabled()) {
					LOGGER.info("join company info type not found, info=" + info + " orgId=" + orgId + " userId=" + userId);	
				}
				continue;
			}
			String mac = infos[2];
			DoorAccess doorAccess = doorAccessProvider.queryDoorAccessByHardwareId(mac);
			if(doorAccess == null) {
				LOGGER.warn("join company info mac not found, info=" + info + " orgId=" + orgId + " userId=" + userId);
				continue;
			}
			
			UserInfo userInfo = userService.getUserSnapshotInfoWithPhone(userId);
            if(userInfo != null){
                CreateDoorAuthCommand cmd = new CreateDoorAuthCommand();
                cmd.setApproveUserId(User.SYSTEM_UID);
                cmd.setAuthMethod(DoorAuthMethodType.ADMIN.getCode());
                cmd.setAuthType(DoorAuthType.FOREVER.getCode());
                cmd.setDescription("new user auto created");
                cmd.setDoorId(doorAccess.getId());
                cmd.setNamespaceId(namespaceId);
                if(userInfo.getPhones() != null && userInfo.getPhones().size() > 0) {
                    cmd.setPhone(userInfo.getPhones().get(0));
                }
                cmd.setRightOpen((byte)1);
                cmd.setRightRemote((byte)1);
                cmd.setUserId(userId);
                createDoorAuth(cmd);
            }else{
                return;
            }
		}
	}

    @Override
    public void exportVisitorDoorAuth(ExportDoorAuthCommand cmd, HttpServletResponse httpResponse) {
        int pageSize = 200;
        if (cmd.getPageSize() != null) {
            pageSize = cmd.getPageSize();
        }

        List<DoorAuth> auths = doorAuthProvider.searchVisitorDoorAuthByAdmin(
                cmd.getDoorId(), cmd.getKeyword(), cmd.getStatus(), pageSize, cmd.getStartTime(), cmd.getEndTime());

        List<DoorAuthExportVo> voList = auths.stream().map(r -> {
            DoorAuthExportVo vo = new DoorAuthExportVo();
            vo.setNickName(r.getNickname());
            vo.setPhone(r.getPhone());
            vo.setOrganization(r.getOrganization());
            vo.setGoDoor(r.getCurrStorey() != null ? String.valueOf(r.getCurrStorey()) : "");
            vo.setDescription(r.getDescription());

            Date d1 = new Date(r.getValidEndMs());
            vo.setAvailableTime(DateUtil.dateToStr(d1, DateUtil.DATE_HM));

            Date d2 = new Date(r.getValidFromMs());
            vo.setAuthTime(DateUtil.dateToStr(d2, DateUtil.DATE_HM));

            User u = userProvider.findUserById(r.getApproveUserId());
            if(u != null) {
                vo.setApproveUserName(getRealName(u));
            }
            return vo;
        }).collect(Collectors.toList());

        String[] propertyNames = {"nickName", "phone", "organization", "goDoor", "description", "availableTime", "approveUserName", "authTime"};
        String[] titleNames = {"姓名", "手机号", "来访单位", "所去楼层", "来访事由", "有效期", "授权人", "授权时间"};
        int[] columnSizes = {20, 20, 20, 20, 20, 20, 20, 20};
        String fileName = String.format("visitor_auth_%s", DateUtil.dateToStr(new Date(), DateUtil.NO_SLASH));
        ExcelUtils excelUtils = new ExcelUtils(httpResponse, fileName, "访客授权");
        excelUtils.writeExcel(propertyNames, titleNames, columnSizes, voList);
    }
    
    @Override
    public void doAlipayRedirect(HttpServletRequest request, HttpServletResponse response) {
        String redirectUrl = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id="+AclinkConstant.ALI_APP_ID+"&scope=auth_user&state="+request.getParameter("state")+"&redirect_uri=";
        String homeUrl = configProvider.getValue(AclinkConstant.HOME_URL, "");
        try {
            redirectUrl += URLEncoder.encode(homeUrl + "/mobile/static/qr_access/qrAliCode.html?", "utf-8");
        } catch (UnsupportedEncodingException e) {
        }
        try {
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
        }
        
    }
    
    @Override
    public GetVisitorResponse getAlipayQR(HttpServletRequest r) {
        String auth_code = r.getParameter("auth_code");
        String state = r.getParameter("state");
        String mac = state;
        String keyIn = "alipay:" + auth_code + ":" + state;
        String phone = getCache(keyIn);
//        phone = "15889660710";
        if(phone == null) {
            AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
            request.setCode(auth_code);
            request.setGrantType("authorization_code");
            try {
                AlipaySystemOauthTokenResponse oauthTokenResponse = getAlipayClient().execute(request);
                LOGGER.info("accessToken=" + oauthTokenResponse.getAccessToken() + " userId=" + oauthTokenResponse.getAlipayUserId() + " body=" + oauthTokenResponse.getBody());
                
                AlipayUserInfoShareRequest requestUser = new AlipayUserInfoShareRequest();
                AlipayUserInfoShareResponse responseUser = getAlipayClient().execute(requestUser, oauthTokenResponse.getAccessToken());
                
                phone = responseUser.getMobile();
                if(phone == null || phone.isEmpty()) {
                    throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_STATE_ERROR, "Phone not found from alipay");
                    }
             } catch (AlipayApiException e) {
                    LOGGER.info("alipay request error", e);
                    throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_STATE_ERROR, "Alipay request error");
                  }
             cacheTimeout(keyIn, phone, 600);
            }
        
            DoorAccess doorAccess = doorAccessProvider.queryDoorAccessByHardwareId(mac.toUpperCase());
            if(doorAccess == null) {
                throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "Door not found");
            }
            DoorAuth auth = doorAuthProvider.queryValidDoorAuthByVisitorPhone(doorAccess.getId(), phone);
            if(auth == null) {
                throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, "auth not found");
            }

            return getVisitorByAuth(auth);
            
    }
    
    //https://docs.open.alipay.com/284/106001/
    public String aliTest2(HttpServletRequest r) {
        return aliTest002(r);
    }
    
    private String aliTest002(HttpServletRequest r) {
        LOGGER.info("" + r.getParameterMap());
        LOGGER.info("auth_code=" + r.getParameter("auth_code"));
        
        String auth_code = r.getParameter("auth_code");
        String state = r.getParameter("state");
        
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setCode(auth_code);
        request.setGrantType("authorization_code");
        try {
            AlipaySystemOauthTokenResponse oauthTokenResponse = getAlipayClient().execute(request);
            LOGGER.info("accessToken=" + oauthTokenResponse.getAccessToken() + " userId=" + oauthTokenResponse.getAlipayUserId() + " body=" + oauthTokenResponse.getBody());
            
            AlipayUserInfoShareRequest requestUser = new AlipayUserInfoShareRequest();
            AlipayUserInfoShareResponse responseUser = getAlipayClient().execute(requestUser, oauthTokenResponse.getAccessToken());
            
            String doorMAC = state;
            String phone = responseUser.getMobile();
            User user = userService.findUserByIndentifier(1000000, phone);
            
            DoorAccess doorAccess = doorAccessProvider.queryDoorAccessByHardwareId(doorMAC.toUpperCase());
            if(doorAccess == null) {
                throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "Door not found");
            }
            
            DoorAuth doorAuth = doorAuthProvider.queryValidDoorAuthByDoorIdAndUserId(doorAccess.getId(), user.getId(), (byte)1);
            if(doorAuth == null) {
                throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, "DoorAuth error");
            }
            
            remoteOpenDoor(doorAuth.getId());
            
            return "door " + doorMAC + " open, userId=" + oauthTokenResponse.getAlipayUserId() + ", userInfo=" + responseUser.getMobile(); 
        } catch (AlipayApiException e) {
            //处理异常
            e.printStackTrace();
        }

        return "failed!";
    }
    
//    private String aliTest001(HttpServletRequest r) {
//        LOGGER.info("" + r.getParameterMap());
//        LOGGER.info("app_auth_code=" + r.getParameter("app_auth_code"));
//        AlipayOpenAuthTokenAppRequest request = new AlipayOpenAuthTokenAppRequest();
//        request.setBizContent(String.format("{\"grant_type\":\"authorization_code\",\"code\":\"%s\"}", r.getParameter("app_auth_code")));
//        try {
//            AlipayOpenAuthTokenAppResponse openAuthResponse = getAlipayClient().execute(request);
//            LOGGER.info("accessToken=" + openAuthResponse.getAppAuthToken() + "refreshToken" + openAuthResponse.getAppRefreshToken());
//            
//            //request.putOtherTextParam("app_auth_token", "201611BB888ae9acd6e44fec9940d09201abfE16");
////            AlipayOpenAuthTokenAppQueryRequest request2 = new AlipayOpenAuthTokenAppQueryRequest();
////            request2.setBizContent("{" +
////            "    \"app_auth_token\":\""+openAuthResponse.getAppAuthToken()+"\"" +
////            "  }");
////            AlipayOpenAuthTokenAppQueryResponse apiListResponse = alipayClient.execute(request2);
////            LOGGER.info("apiListResponse=" + apiListResponse.getBody());
//            
//            AlipayUserInfoShareRequest requestUser = new AlipayUserInfoShareRequest();
//            requestUser.putOtherTextParam("app_auth_token", openAuthResponse.getAppAuthToken());
//            AlipayUserInfoShareResponse responseUser = getAlipayClient().execute(requestUser, null, openAuthResponse.getAppAuthToken());
//
//            return responseUser.getBody();
//            
//        } catch (AlipayApiException e) {
//            //处理异常
//            e.printStackTrace();
//        }
//        
//        return "failed";
//    }
    
//    @Override
//    public String faceTest() {
//        String doorMAC = "DA:28:B1:38:44:57";
//        String phone = "15889660710";
//        User user = userService.findUserByIndentifier(1000000, phone);
//        
//        DoorAccess doorAccess = doorAccessProvider.queryDoorAccessByHardwareId(doorMAC.toUpperCase());
//        if(doorAccess == null) {
//            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "Door not found");
//        }
//        
//        DoorAuth doorAuth = doorAuthProvider.queryValidDoorAuthByDoorIdAndUserId(doorAccess.getId(), user.getId(), (byte)1);
//        if(doorAuth == null) {
//            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, "DoorAuth error");
//        }
//        
//        remoteOpenDoor(doorAuth.getId());
//        
//        return "door " + doorMAC + " open"; 
//    }

    @Override
    public DoorAuthLevelDTO createDoorAuthLevel(CreateDoorAuthLevelCommand cmd) {
        DoorAuthLevel obj = ConvertHelper.convert(cmd, DoorAuthLevel.class);
        DoorAccess da = doorAccessProvider.getDoorAccessById(cmd.getDoorId());
        if (da == null) {
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "door access not found");
        }
        
        if(cmd.getRightOpen() == null) {
            cmd.setRightOpen((byte)0);
        }
        if(cmd.getRightRemote() == null) {
            cmd.setRightRemote((byte)0);
        }
        if(cmd.getRightVisitor() == null) {
            cmd.setRightVisitor((byte)0);
        }
        
        DoorAuthLevel obj2 = doorAuthLevelProvider.findAuthLevel(cmd.getLevelId(), cmd.getLevelType(), cmd.getDoorId());
        if(obj2 != null && 
                cmd.getRightOpen().equals((byte)0) && 
                cmd.getRightRemote().equals((byte)0) &&
                cmd.getRightVisitor().equals((byte)0) ) {
            doorAuthLevelProvider.deleteDoorAuthLevel(obj2);
            obj = obj2;
            cmd.setRightOpen((byte)0);
            cmd.setRightRemote((byte)0);
            cmd.setRightVisitor((byte)0);
        } else if(obj2 != null) {
            obj = obj2;
            obj.setRightOpen((byte)1);
            obj.setRightRemote(cmd.getRightRemote());
            obj.setRightVisitor(cmd.getRightVisitor());
            doorAuthLevelProvider.updateDoorAuthLevel(obj);
        } else {
            obj.setRightOpen((byte)1);
            obj.setOwnerId(da.getOwnerId());
            obj.setOwnerType(da.getOwnerType());
            obj.setNamespaceId(UserContext.getCurrentNamespaceId());
            obj.setOperatorId(UserContext.currentUserId());
            
            doorAuthLevelProvider.createDoorAuthLevel(obj);    
        }
        
        return ConvertHelper.convert(obj, DoorAuthLevelDTO.class);
    }
    
    @Override
    public ListDoorAuthLevelResponse listDoorAuthLevel(ListDoorAuthLevelCommand cmd) {
        //update count if the count is null
        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        cmd.setPageSize(count);
        
        ListDoorAuthLevelResponse resp = null;
        
        if(cmd.getLevelType() == null) {
            return null;
        }
        if(cmd.getLevelType().equals(DoorAccessOwnerType.ENTERPRISE.getCode())) {
            if(cmd.getKeyword() == null || cmd.getKeyword().isEmpty()) {
                resp = doorAuthLevelProvider.findAuthLevels(cmd);
                if(resp.getDtos() != null && resp.getDtos().size() > 0) {
                    for(DoorAuthLevelDTO dto : resp.getDtos()) {
                        Organization org = organizationProvider.findOrganizationById(dto.getLevelId());
                        if(org != null) {
                            dto.setOrgName(org.getName());    
                        } 
                    }      
                }
                
            } else {
                resp = doorAuthLevelProvider.findAuthLevelsWithOrg(cmd);
            }
        } else {
            resp = doorAuthLevelProvider.findAuthLevelsWithBuilding(cmd);
        }
                
        return resp;
    }
    
    @Override
    public void deleteDoorAuthLevel(Long id) {
        DoorAuthLevel lvl = doorAuthLevelProvider.getDoorAuthLevelById(id);
        if(lvl != null) {
            lvl.setStatus(DoorAuthStatus.INVALID.getCode());
            doorAuthLevelProvider.updateDoorAuthLevel(lvl);
        }
    }

	@Override
	public void excuteMessage(AclinkWebSocketMessage cmd) {
		String msg = cmd.getPayload();
		String uuid = cmd.getUuid();
		DoorAccess door = doorAccessProvider.queryDoorAccessByUuid(uuid);
		if(door == null){
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND,
                    "sender not found ");
		}
		byte[] msgArr = Base64.decodeBase64(msg);
		byte cmdNumber = msgArr[0];
		if(cmdNumber == 0x10){
			byte[] testArr = Arrays.copyOfRange(msgArr, 3, 7);
			Long authId =DataUtil.byteArrayToLong(testArr);
			String keyName = "validateDoorAuthCount" + String.valueOf(authId);
			String validResult = getCache(keyName);
			if(!("true").equals(validResult)){
				DoorAuth doorAuth = doorAuthProvider.getDoorAuthById(authId);
				if (doorAuth != null && doorAuth.getStatus().equals(DoorAuthStatus.VALID.getCode())
						&& doorAuth.getValidAuthAmount() > 0 && doorAuth.getValidEndMs() > System.currentTimeMillis()) {
					if(doorAuth.getDoorId().longValue() != door.getId().longValue() && doorAuth.getDoorId().longValue() != door.getGroupid().longValue()){
						throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR,
		                        "sender and msg not pair ");
					}
					doorAuth.setValidAuthAmount(doorAuth.getValidAuthAmount() - 1);
					doorAuthProvider.updateDoorAuth(doorAuth);
					validResult = "true";
					cacheTimeout(keyName, validResult, 60);
				}
			}

			if(("true").equals(validResult)){
				LOGGER.info("validate auth count successed");
				remoteOpenDoor(authId,uuid);
			}else{
				LOGGER.info("validate auth count failed");
			}
		}else if(cmdNumber == 0xf){
//			AclinkLogCreateCommand cmds = new AclinkLogCreateCommand();
//			List<AclinkLogItem> listLogItems = new ArrayList<AclinkLogItem>();
			AclinkLogItem logItem = new AclinkLogItem();
//			logItem.setAuthId(authId);
//			logItem.setKeyId(keyId);
//			logItem.setNamespaceId(namespaceId);
			logItem.setDoorId(door.getId());
			logItem.setUserId(DataUtil.byteArrayToLong(Arrays.copyOfRange(msgArr, 10, 14)));
			logItem.setLogTime(DataUtil.byteArrayToLong(Arrays.copyOfRange(msgArr, 15, 19)) * 1000);
			switch (msgArr[14]){
			case 0x1:
				logItem.setEventType(3L);
				break;
			case 0x2:
				logItem.setEventType(0L);
				break;
			case 0x3:
				logItem.setEventType(1L);
				break;
			case 0x4:
				logItem.setEventType(2L);
				break;
			default:
				break;
			}

			AclinkLog aclinkLog = ConvertHelper.convert(logItem, AclinkLog.class);
			UserInfo user = userService.getUserSnapshotInfo(logItem.getUserId());
			
        	if(user.getPhones() != null && user.getPhones().size() > 0) {
                   aclinkLog.setUserIdentifier(userService.getUserIdentifier(logItem.getUserId()).getIdentifierToken());    
               }
        	aclinkLog.setUserName(logItem.getUserId() == 1L?"访客":user.getNickName());
        	aclinkLog.setDoorName(door.getDisplayNameNotEmpty());
            aclinkLog.setHardwareId(door.getHardwareId());
            aclinkLog.setOwnerId(door.getOwnerId());
            aclinkLog.setOwnerType(door.getOwnerType());
            aclinkLog.setDoorType(door.getDoorType());
            aclinkLogProvider.createAclinkLog(aclinkLog);
		}
	}


    @Override
    public DoorAccessGroupResp listDoorAccessByUser(ListDoorAccessByUserCommand cmd) {
        User user = UserContext.current().getUser();
//      获取用户公司楼层
        List<OrganizationDTO> orgs = organizationService.listUserRelateOrganizations(cmd.getNamespaceId(), user.getId(), OrganizationGroupType.ENTERPRISE);
        List<AddressDTO> userFloors = new ArrayList<>();
        for(OrganizationDTO dto : orgs) {
            List<OrganizationAddress> addrs = organizationProvider.findOrganizationAddressByOrganizationId(dto.getId());
            for(OrganizationAddress addr  : addrs) {
                Address addr2 = addressProvider.findAddressById(addr.getAddressId());
                addr2 = ApartmentFloorHandler(addr2);
                userFloors.add(ConvertHelper.convert(addr2,AddressDTO.class));
            }
        }
//      获取设备
        List<DoorAuth> auths = uniqueAuths(doorAuthProvider.listValidDoorAuthByUser(user.getId(), null)).stream().filter(r -> r.getRightOpen().equals((byte)1)).collect(Collectors.toList());
        DoorAccessGroupResp resp = new DoorAccessGroupResp();
        resp.setUserId(user.getId());
        List<DoorAccessGroupDTO> groups = new ArrayList<>();
        resp.setGroups(groups);
        for(DoorAuth auth : auths){
            DoorAccess access = doorAccessProvider.getDoorAccessById(auth.getDoorId());
            if(null != access && access.getDoorType().equals(DoorAccessType.ACLINK_WANGLONG_GROUP.getCode())){
                DoorAccessGroupDTO group = new DoorAccessGroupDTO();
                groups.add(group);
                group.setId(access.getId());
                group.setGroupName(access.getName());
                group.setKeyU(auth.getKeyU());
                group.setQrDriver(auth.getDriver());
                group.setStatus(access.getStatus());
                group.setDescription(access.getDescription());
                group.setCreateTime(access.getCreateTime());
                List<DoorAccess> devices = doorAccessProvider.listDoorAccessByGroupId(access.getId(),999);
                if(null != devices && devices.size() > 0){
                    group.setDevices(devices.stream().map(r -> {
                        DoorAccessDeviceDTO device = new DoorAccessDeviceDTO();
                        device.setDevUnique(r.getHardwareId());
                        device.setDeviceName(r.getDisplayNameNotEmpty());
                        device.setDeviceType(r.getDoorType());
                        return device;
                    }).collect(Collectors.toList()));
                }
                List<AddressDTO> floors = new ArrayList<>();
                floors.addAll(userFloors);
                if(org.apache.commons.lang.StringUtils.isNotEmpty(access.getFloorId())){
                    JSONObject data = JSON.parseObject(access.getFloorId());
                    JSONArray jsonfloors = data.getJSONArray("floors");
                    if(null != jsonfloors){
                        for (int i = 0;i < jsonfloors.size();i++){
                            String floorId = jsonfloors.getJSONObject(i).getString("id");
                            floors.add(ConvertHelper.convert(ApartmentFloorHandler(addressProvider.findAddressById(Long.valueOf(floorId))),AddressDTO.class));
                        }
                    }
                    if (null != floors & floors.size() > 0) {
                        group.setFloors(floors.stream().distinct().map(r -> {
                            FloorDTO dto = new FloorDTO();
                            dto.setFloor(r.getApartmentFloor());
                            dto.setFloorName(r.getAddress());
                            return dto;
                        }).collect(Collectors.toList()));
                    }
                }
            }
        }

        return resp;
    }

    private Address ApartmentFloorHandler(Address addr){
        if(null == addr.getApartmentFloor()) {
            String aname = addr.getAddress();
            String[] as = aname.split("-");
            if(as.length > 1) {
                aname = as[1];
            } else {
                aname = as[0];
            }
            Matcher m = npattern.matcher(aname);
            if(m != null && m.find()) {
                aname = m.group(0);
                Long l = Long.parseLong(aname);
                if(l >= -255 && l <= 255) {
                    addr.setApartmentFloor(l.toString());
                }
            }
        }
        return addr;
    }


    private String getKeyU(String projectId,String userId) {

        String keyU;
        String secret;
        Map<String,String> params = new HashMap<>();
        params.put("appid",WANGLONG_APP_ID);
        params.put("appsecret",WANGLONG_APP_SECRET);
        params.put("projectId",projectId);
        try {
            String resp = HttpUtils.post(URL_GETAPPINFOS,params,1,"UTF-8");
            JSONObject respObj = JSON.parseObject(resp);
            if(0 != respObj.getInteger("msgCode")){
                LOGGER.error("Http请求失败, msg={}",respObj.getString("msg"));
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "HttpRequest is fail.");
            }
            JSONArray dataArr = respObj.getJSONArray("data");
            if(dataArr.size() <= 0){
                LOGGER.error("Http请求数据为空, data={}",respObj.getString("data"));
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "HttpRequest is fail.");
            }
            JSONObject projectObj = dataArr.getJSONObject(0);
            secret = projectObj.getString("encryptedKey");
            Map<String,String> params1 = new HashMap<>();
            params1.put("appid",WANGLONG_APP_ID);
            params1.put("appsecret",WANGLONG_APP_SECRET);
            params1.put("encryptedKey",secret);
            params1.put("projectId",projectId);
            params1.put("userId",userId);
            String resp1 = HttpUtils.post(URL_GETKEYU,params1,1,"UTF-8");
            JSONObject respObj1 = JSON.parseObject(resp1);
            if(0 != respObj1.getInteger("msgCode")){
                LOGGER.error("Http请求失败, msg={}",respObj1.getString("msg"));
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "HttpRequest is fail.");
            }
            keyU = respObj1.getJSONObject("data").getString("keyU");

        } catch (Exception e) {
            LOGGER.error("Http请求失败, projectId={},userId={}",projectId,userId);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "HttpRequest is fail.");
        }

        return keyU;
    }




	@Override
	public QueryDoorAccessByServerResponse listDoorAccessByServerId(QueryDoorAccessByServerCommand cmd) {
		QueryDoorAccessByServerResponse rsp = new QueryDoorAccessByServerResponse();
		List<DoorAccess> listDoorAccess = doorAccessProvider.listDoorAccessByServerId(cmd.getId(), cmd.getPageSize() == null ? 0 : cmd.getPageSize());
		List<DoorAccessDTO> dtos = new ArrayList<DoorAccessDTO>();
		if(listDoorAccess == null || listDoorAccess.size() == 0){
			return rsp;
		}
		for(DoorAccess da: listDoorAccess){
			DoorAccessDTO dto = ConvertHelper.convert(da, DoorAccessDTO.class);
			if(da.getLocalServerId() != null){
				dto.setServer(aclinkServerService.findLocalServerById(da.getLocalServerId()));
			}
            if(da.getDisplayName() == null) {
                dto.setDisplayName(da.getName());
            }
            dtos.add(dto);
		}
		rsp.setListDoorAccess(dtos);
		if(cmd.getPageSize() != null && dtos.size()>0){
			rsp.setNextPageAnchor(dtos.get(dtos.size() - 1).getId());
		}
		return rsp;
	}

	@Override
	public ListDoorAccessByGroupIdResponse listDoorAccessByGroupId(ListDoorAccessByGroupIdCommand cmd) {
		ListDoorAccessByGroupIdResponse rsp = new ListDoorAccessByGroupIdResponse();
		List<DoorAccess> doors = doorAccessProvider.listDoorAccessByGroupId(cmd.getGroupId(), 0);
		if(doors != null && doors.size() != 0){
			List<DoorAccessDTO> listDtos = new ArrayList<DoorAccessDTO>();
			for (DoorAccess door : doors) {
				listDtos.add(ConvertHelper.convert(door, DoorAccessDTO.class));
			}
			rsp.setListDoorAccess(listDtos);
		}
		return rsp;
	}

	@Override
	public ListFacialRecognitionKeyByUserResponse listFacialAesUserKeyByUser(ListFacialRecognitionKeyByUserCommand cmd) {
		ListFacialRecognitionKeyByUserResponse rsp = new ListFacialRecognitionKeyByUserResponse();
		User user = UserContext.current().getUser();
		ListAuthsByLevelandLocationCommand qryCmd = getLevelQryCmdByUser(user);
		ListingLocator locator = new ListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
        List<DoorAuth> auths = doorAuthProvider.queryDoorAuthForeverByUserId(locator, qryCmd, null, cmd.getPageSize() == null? 0 : cmd.getPageSize());
        List<AesUserKeyDTO> dtos = new ArrayList<AesUserKeyDTO>();
        for(DoorAuth auth : auths) {
            AesUserKeyDTO dto = new AesUserKeyDTO();

            if(auth.getAuthType().equals(DoorAuthType.FOREVER.getCode())) {

            	if(auth.getRightOpen().equals((byte)0)) {
            		//Not has right
                	continue;
               } else if(auth.getRightVisitor().equals((byte)1)) {
                        //有访客授权权限
                    dto.setKeyType(AesUserKeyType.ADMIN.getCode());
               } else {
                    	//普通用户权限
            	   dto.setKeyType(AesUserKeyType.NORMAL.getCode());
                 	}

            } else {
                dto.setKeyType(AesUserKeyType.TEMP.getCode());
                }

            dto.setCreateTimeMs(auth.getCreateTime().getTime());
            dto.setCreatorUid(user.getId());
            dto.setDoorId(auth.getDoorId());
            dto.setUserId(auth.getUserId());
            dto.setStatus(AesUserKeyStatus.VALID.getCode());
            dto.setId(auth.getId());
            dto.setAuthId(auth.getId());

            DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(dto.getDoorId());
            if(doorAccess != null && (!doorAccess.getStatus().equals(DoorAccessStatus.INVALID.getCode()))) {
            	if(doorAccess.getLocalServerId() != null){
            		dto.setRightFaceOpen((byte) 1);//上传了人脸识别照片才能调这个方法,所以不查人脸识别的照片表了
            		dto.setHardwareId(doorAccess.getHardwareId());
            		if(doorAccess.getMacCopy() != null && !doorAccess.getMacCopy().isEmpty()){
            			dto.setHardwareId(doorAccess.getMacCopy());
            		}
                    dto.setDoorName(doorAccess.getDisplayNameNotEmpty());
                    if(auth.getRightRemote() == 1){
                    	dto.setRightRemote((byte) 1);
                    }
                    dtos.add(dto);
            	}
            }
        }
        rsp.setNextPageAnchor(locator.getAnchor());
        rsp.setAesUserKeys(dtos);

		return rsp;
	}

	@Override
	public AesUserKey getAesUserKey(User user, DoorAuth doorAuth){
		return this.generateAesUserKey(user, doorAuth);
	}

	@Override
	public DoorAuthDTO createLocalVisitorAuth(CreateLocalVistorCommand cmd) {
		User user = UserContext.current().getUser();
        DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(cmd.getDoorId());
        DoorAuth auth = createZuolinQrAuth(user, doorAccess, ConvertHelper.convert(cmd, CreateDoorVisitorCommand.class));

        DoorAuthDTO dto = ConvertHelper.convert(auth, DoorAuthDTO.class);
        dto.setQrString(auth.getQrKey());
        if(cmd.getHeadImgUri() != null && !cmd.getHeadImgUri().isEmpty()){
            NotifySyncVistorsCommand cmd1 = new NotifySyncVistorsCommand();
            SetFacialRecognitionPhotoCommand createPhotoCmd = ConvertHelper.convert(cmd, SetFacialRecognitionPhotoCommand.class);
            createPhotoCmd.setUserType((byte) 1);
            createPhotoCmd.setImgUri(cmd.getHeadImgUri());
            createPhotoCmd.setImgUrl(contentServerService.parserUri(createPhotoCmd.getImgUri()));
            createPhotoCmd.setAuthId(auth.getId());
            faceRecognitionPhotoService.setFacialRecognitionPhoto(createPhotoCmd);
            
            cmd1.setDoorId(cmd.getDoorId());
            faceRecognitionPhotoService.notifySyncVistorsCommand(cmd1);
        }
    	return dto;
	}

	/**
	 * 根据电话号码合门禁组的id将授权设置为失效
	 * doorId为门禁组id
	 */
	@Override
	public int invalidVistorAuth(Long doorId, String phone) {
		List<DoorAuth> listAuth = doorAuthProvider.listValidDoorAuthByVisitorPhone(doorId, phone);
		if(listAuth != null && listAuth.size() > 0){
			for(DoorAuth auth : listAuth){
				auth.setStatus(DoorAuthStatus.INVALID.getCode());
			}
			doorAuthProvider.updateDoorAuth(listAuth);
			return 1;
		}
		return 0;
	}

	@Override
	public void updateAccessType(Long doorId, byte doorType) {
		DoorAccess da = doorAccessProvider.getDoorAccessById(doorId);
		if(da == null){
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE,
					AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND,
					localeStringService.getLocalizedString(String.valueOf(AclinkServiceErrorCode.SCOPE),
							String.valueOf(AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND),
							UserContext.current().getUser().getLocale(), "door not found"));
		}
		da.setDoorType(doorType);
		doorAccessProvider.updateDoorAccess(da);
	}

	@Override
	public void deleteAuthByOwner(DeleteAuthByOwnerCommand cmd) {
		if(cmd.getOwnerType() == DoorAccessOwnerType.ENTERPRISE.getCode()){
			deleteAuthWhenLeaveFromOrg(cmd.getNamespaceId(), cmd.getOwnerId(), cmd.getUserId());
			return ;
		}
    	User user = userProvider.findUserById(cmd.getUserId());
        if(null == user){
            LOGGER.info("user is null userId=" + cmd.getUserId());
            return;
        }
		ListingLocator locator = new ListingLocator();
		int count = 100;
		List<DoorAuth> doorAuths = doorAuthProvider.queryValidDoorAuths(locator, cmd.getUserId(), cmd.getOwnerId(), cmd.getOwnerType(), count);
		if(doorAuths == null || doorAuths.size() == 0) {
			LOGGER.info("doorAuths not found, ownerId=" + cmd.getOwnerId() + " userId=" + cmd.getUserId());
			return;
		}
		
		do {
			for(DoorAuth doorAuth : doorAuths) {
				doorAuth.setStatus(DoorAuthStatus.INVALID.getCode());
			}
			doorAuthProvider.updateDoorAuth(doorAuths);
		
			if(locator.getAnchor() != null) {
				doorAuths = doorAuthProvider.queryValidDoorAuths(locator, cmd.getUserId(), cmd.getOwnerId(), cmd.getOwnerType(), count);
			}
			
		} while (doorAuths != null && doorAuths.size() > 0 && locator.getAnchor() != null);
    	
    	LOGGER.info("delete all auths ok! ownerId=" + cmd.getOwnerId() + " userId=" + cmd.getUserId());
	}

    //add by liqingyan
    @Override
    public ListDoorAccessEhResponse listDoorAccessEh(ListDoorAccessEhCommand cmd) {
        ListDoorAccessEhResponse resp = new ListDoorAccessEhResponse();
        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        ListingLocator locator = new ListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        List<DoorAccessNewDTO> dtos = doorAccessProvider.listDoorAccessEh(locator, count, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                SelectQuery<? extends Record> query) {
                if(cmd.getOwnerType() != null) {
                    query.addConditions(Tables.EH_DOOR_ACCESS.OWNER_TYPE.eq(cmd.getOwnerType()));
                }
                if(cmd.getOwnerId() != null) {
                    query.addConditions(Tables.EH_DOOR_ACCESS.OWNER_ID.eq(cmd.getOwnerId()));
                }
                if(cmd.getNamespaceId() != null){
                    query.addConditions(Tables.EH_DOOR_ACCESS.NAMESPACE_ID.eq(cmd.getNamespaceId()));
                }
//                if(cmd.getNamespaceName() != null && !cmd.getNamespaceName().isEmpty()){
//                    query.addConditions(com.everhomes.schema.Tables.EH_NAMESPACES.NAME.eq(cmd.getNamespaceName()));
//                }
//                if(cmd.getDoorType() != null) {
//                    query.addConditions(Tables.EH_DOOR_ACCESS.DOOR_TYPE.eq(cmd.getDoorType()));
//
//                }
                if(cmd.getDeviceId() != null){
                    query.addConditions(Tables.EH_DOOR_ACCESS.DEVICE_ID.eq(cmd.getDeviceId()));
                }
                if(cmd.getDoor() != null && !cmd.getDoor().isEmpty()) {
                    query.addConditions(Tables.EH_DOOR_ACCESS.NAME.like(cmd.getDoor() + "%")
                            .or(Tables.EH_DOOR_ACCESS.DISPLAY_NAME.like(cmd.getDoor()+"%")));
                }
                if(cmd.getBluetoothMAC() != null && !cmd.getBluetoothMAC().isEmpty()){
                    query.addConditions(Tables.EH_DOOR_ACCESS.HARDWARE_ID.like(cmd.getBluetoothMAC()+ "%"));
                }
                return query;
            }
        });
        resp.setDoors(dtos);
        resp.setNextPageAnchor(locator.getAnchor());
        return resp;
    }
    @Override
    public ListDoorTypeResponse listDoorType  (ListDoorTypeCommand cmd){
	    int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = cmd.getPageAnchor() == null? 0 : cmd.getPageAnchor();
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(anchor);
        ListDoorTypeResponse resp = new ListDoorTypeResponse();
        List<AclinkDeviceDTO> dtos = aclinkFirmwareProvider.listFirmwareDevice(locator, count, cmd);
        if(count > 0 && dtos.size() > count) {
            locator.setAnchor(dtos.get(dtos.size() - 1).getId());
            dtos.remove(dtos.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        resp.setDtos(dtos);
        resp.setNextPageAnchor(locator.getAnchor());
        return resp;
    }

    @Override
    public ListFirmwareResponse listFirmware (ListFirmwareCommand cmd){
        int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long anchor = cmd.getPageAnchor() == null? 0 : cmd.getPageAnchor();
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(anchor);
        ListFirmwareResponse resp = new ListFirmwareResponse();
        List<FirmwareNewDTO> dtos = aclinkFirmwareProvider.listFirmwareNew(locator, count, cmd);
        if(count > 0 && dtos.size() > count) {
            locator.setAnchor(dtos.get(dtos.size() - 1).getId());
            dtos.remove(dtos.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        resp.setDtos(dtos);
        resp.setNextPageAnchor(locator.getAnchor());
	    return resp;
    }


    @Override
    public DoorStatisticEhResponse doorStatisticEh (DoorStatisticEhCommand cmd){
        DoorStatisticEhResponse resp = new DoorStatisticEhResponse();
        List<ActiveDoorByPlaceDTO> dto1 = doorAccessProvider.queryDoorAccessByPlaceNew(cmd);
        resp.setDto1(dto1);
        List<ActiveDoorByFirmwareDTO> dto2 = doorAccessProvider.queryDoorAccessByFirmware(cmd);
        resp.setDto2(dto2);
        List<ActiveDoorByEquipmentDTO> dto3 = doorAccessProvider.queryDoorAccessByEquipment(cmd);
        resp.setDto3(dto3);
        List<ActiveDoorByNamespaceDTO> dto4 = doorAccessProvider.queryDoorAccessByNamespaceNew(cmd);
        resp.setDto4(dto4);
        List<AclinkUseByNamespaceDTO> dto5 = null;
        resp.setDto5(dto5);
        return resp;

    }
    @Override
    public void changeDoorName(ChangeDoorNameCommand cmd){
        DoorAccess doorAccess = doorAccessProvider.findDoorAccessById(cmd.getDoorId());
        doorAccess.setDisplayName(cmd.getName());
        doorAccess.setAddress(cmd.getDoorAddress());
        doorAccess.setDescription(cmd.getDoorDescription());
        doorAccessProvider.updateDoorAccessNew(doorAccess);
    }
    @Override
    public void addDoorManagement(AddDoorManagementCommand cmd){

    }
    @Override
    public void deleteDoorManagement (AddDoorManagementCommand cmd){

    }
    @Override
    public ChangeUpdateFirmwareResponse changeUpdateFirmware (ChangeUpdateFirmwareCommand cmd){
        ChangeUpdateFirmwareResponse resp = new ChangeUpdateFirmwareResponse();
	    for(UpdateFirmwareDTO cmds:cmd.getDto()) {
	        if(cmds.getId() != null) {
                AclinkDevice device = aclinkFirmwareProvider.findDeviceById(cmds.getId());
                device.setFirmware(cmds.getFirmware());
                device.setFirmwareId(cmds.getFirmwareId());
                device.setUpdate(cmds.getUpdate());
                aclinkFirmwareProvider.updateAclinkDevice(device);
            }
//            resp.getDtos().add((ConvertHelper.convert(device,AclinkDeviceDTO.class)));
        }
        return null;
    }
    @Override
    public FirmwareNewDTO addFirmware (AddFirmwareCommand cmd){
        AclinkFirmwareNew firmware = (AclinkFirmwareNew) ConvertHelper.convert(cmd,AclinkFirmwareNew.class);
        firmware.setStatus((byte)1);
        aclinkFirmwareProvider.createFirmwareNew(firmware);
        return (FirmwareNewDTO)ConvertHelper.convert(firmware, FirmwareNewDTO.class);
    }
    @Override
    public FirmwareNewDTO deleteFirmware (DeleteFirmwareCommand cmd){
        AclinkFirmwareNew firmware = aclinkFirmwareProvider.findFirmwareById(cmd.getId());
        firmware.setStatus((byte)0);
        aclinkFirmwareProvider.updateFirmwareNew(firmware);
        return (FirmwareNewDTO)ConvertHelper.convert(firmware, FirmwareNewDTO.class);
    }

    @Override
    public ListFirmwarePackageResponse listFirmwarePackage (ListFirmwarePackageCommand cmd){
	    int count = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        Long pageAnchor = cmd.getPageAnchor() == null? 0 : cmd.getPageAnchor();
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(pageAnchor);
        ListFirmwarePackageResponse resp = new ListFirmwarePackageResponse();
        List<FirmwarePackageDTO> dtos = aclinkFirmwareProvider.listFirmwarePackage(locator, count, cmd);
        if(count > 0 && dtos.size() > count) {
            Long anchor = dtos.get(dtos.size() - 1).getId();
            locator.setAnchor(dtos.get(dtos.size() - 1).getId());
            dtos.remove(dtos.size() - 1);
        } else {
            locator.setAnchor(null);
        }
        resp.setDtos(dtos);
        resp.setNextPageAnchor(locator.getAnchor());
	    return resp;
    }

    @Override
    public FirmwarePackageDTO uploadFirmwarePackage(UploadFirmwarePackageCommand cmd){
        AclinkFirmwarePackage pkg = (AclinkFirmwarePackage)ConvertHelper.convert(cmd,AclinkFirmwarePackage.class);
        pkg.setStatus(new Byte((byte)1));
        aclinkFirmwareProvider.createFirmwarePackage(pkg);
        return (FirmwarePackageDTO)ConvertHelper.convert(pkg, FirmwarePackageDTO.class);
    }
    @Override
    public FirmwarePackageDTO deleteFirmwarePackage(DeleteFirmwarePackageCommand cmd){
	    AclinkFirmwarePackage pkg = aclinkFirmwareProvider.findPackageById(cmd.getId());
        pkg.setStatus(((byte)0));
        aclinkFirmwareProvider.updateFirmwarePackage(pkg);
        return (FirmwarePackageDTO)ConvertHelper.convert(pkg, FirmwarePackageDTO.class);
    }
    @Override
    public void uploadWifi(UploadFirmwarePackageCommand cmd){

    }
    @Override
    public void downloadBluetooth (DownloadBluetoothCommand cmd){}

    @Override
    public void downloadWifi(DownloadBluetoothCommand cmd){

    }
    @Override
    public void deleteBluetooth (DeleteBluetoothCommand cmd){}

    @Override
    public void deleteWifi (DeleteBluetoothCommand cmd){}
    //add by liqingyan
    @Override
    public Long deleteDoorAccessEh(Long doorAccessId) {
        DoorAccess doorAccess = doorAccessProvider.findDoorAccessById(doorAccessId);
        doorAccess.setStatus((byte) 2);
        doorAccessProvider.updateDoorAccessNew(doorAccess);
        return doorAccess.getId();
    }

	@Override
    public CheckMobilePrivilegeResponse checkMobilePrivilege (CheckMobilePrivilegeCommand cmd){
        CheckMobilePrivilegeResponse rsp = new CheckMobilePrivilegeResponse();
        //添加权限
        if (cmd.getOwnerType() != null && cmd.getOwnerType() == 1 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4112041124L, cmd.getAppId(), null, cmd.getCurrentProjectId());
            rsp.setPrivilegeType(Boolean.TRUE);
        }
        else if (cmd.getOwnerType() != null && cmd.getOwnerType() == 0 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
            userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4111041114L, cmd.getAppId(), null, cmd.getCurrentProjectId());
            rsp.setPrivilegeType(Boolean.TRUE);
        }
        else rsp.setPrivilegeType(Boolean.FALSE);
        return rsp;
    }

    //add bu liqingyan 添加临时授权
    @Override
    public DoorAuthDTO createTempAuth(CreateTempAuthCommand cmd){
	    return null;
    }


    @Override
	public ListZLDoorAccessResponse listDoorAccessMacByApp() {
		List<String> listMac = new ArrayList<String>();
		ListZLDoorAccessResponse rsp = new ListZLDoorAccessResponse();
		//TODO 上海华润对接调试用,待appSecret与公司关联实现后补完 by liuyilin 20180802
		App app = UserContext.current().getCallerApp();
		if(app == null || app.getName() == null){
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, "auth error");
		}
		Long ownerId = Long.valueOf(app.getName());
		ListDoorAccessGroupCommand cmd = new ListDoorAccessGroupCommand();
		cmd.setOwnerId(ownerId);
		cmd.setOwnerType(DoorAccessOwnerType.ENTERPRISE.getCode());
		List<DoorAccessDTO> das = listDoorAccessGroup(cmd).getDoors();
		if(das != null && das.size() != 0){
			for(DoorAccessDTO da : das){
				listMac.add(da.getHardwareId());
			}
			rsp.setListMac(listMac);
		}
		return rsp;
	}

	@Override
	public GetZLAesUserKeyResponse getAppAesUserKey(GetZLAesUserKeyCommand cmd) {
		DoorAccess da = doorAccessProvider.queryDoorAccessByHardwareId(cmd.getMacAddress());
		if(da == null){
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "Door not found");
		}
		List<AclinkAppUserKeyDTO> userKeys = new ArrayList<AclinkAppUserKeyDTO>();
		for(Long userId: cmd.getUserIds()){
			AclinkAppUserKeyDTO userKey =new AclinkAppUserKeyDTO();
			userKey.setKeySecret(AclinkUtils.packAesUserKey(da.getAesIv(), userId, 0, System.currentTimeMillis() + KEY_TICK_7_DAY));
			userKey.setMac(cmd.getMacAddress());
			userKey.setUserId(userId);
			userKeys.add(userKey);
		}
		GetZLAesUserKeyResponse rsp = new GetZLAesUserKeyResponse();
		rsp.setUserKeys(userKeys);
		
		return rsp;
	}

	@Override
	public void createVisitorBatch(CreateVisitorBatchCommand cmd) {
		if(cmd.getDoorIds() != null && cmd.getDoorIds().size() > 0 && cmd.getAuthList() != null && cmd.getAuthList().size() > 0){
			for(Long doorId : cmd.getDoorIds()){
				if(doorId == null) continue;
				for(CreateLocalVistorCommand itemCmd : cmd.getAuthList()){
					if(itemCmd == null || itemCmd.getNamespaceId() == null) continue;
					itemCmd.setDoorId(doorId);
					itemCmd.setAuthMethod(DoorAuthMethodType.ADMIN.getCode());
					itemCmd.setAuthRuleType(DoorAuthRuleType.DURATION.getCode());
			        if(itemCmd.getValidEndMs() == null) {
			        	if(cmd.getValidEndMs() == null){
			        		itemCmd.setValidEndMs(System.currentTimeMillis() +  KEY_TICK_ONE_DAY);
			        	}else{
			        		itemCmd.setValidEndMs(cmd.getValidEndMs());
			        	}
			        }
			        if(itemCmd.getValidFromMs() == null) {
			        	if(cmd.getValidFromMs() == null){
			        		itemCmd.setValidFromMs(System.currentTimeMillis());	
			        	}else{
			        		itemCmd.setValidFromMs(cmd.getValidFromMs());
			        	}
			        }
					createLocalVisitorAuth(itemCmd);
				}
			}
		}
	}

	@Override
	public CreateZLVisitorQRKeyResponse createZLVisitorQRKey(CreateZLVisitorQRKeyCommand cmd) {
		CreateZLVisitorQRKeyResponse rsp = new CreateZLVisitorQRKeyResponse();
		DoorAccess doorAccess = doorAccessProvider.queryDoorAccessByHardwareId(cmd.getMacAddress());
		if(doorAccess == null){
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "Door not found");
		}
		
		
		//TODO 上海华润对接调试用,待appSecret与公司关联实现后补完 by liuyilin 20180802
		Integer namespaceId = 2;
		User user = UserContext.current().getUser();
		CreateDoorVisitorCommand cmd2 = ConvertHelper.convert(cmd, CreateDoorVisitorCommand.class);
		cmd2.setNamespaceId(namespaceId);
		cmd2.setDoorId(doorAccess.getId());
        DoorAuth auth = createZuolinQrAuth(user, doorAccess, cmd2);
        rsp.setMacAddress(cmd.getMacAddress());
        rsp.setQrCode(auth.getQrKey());
		return rsp;
	}

	@Override
	public ListDoorAccessLiteResponse listDoorAccessByOwnerIdLite(QueryDoorAccessAdminCommand cmd) {
		ListDoorAccessResponse rsp = searchDoorAccessByAdmin(cmd);
		List<DoorAccessDTO> dtos = rsp.getDoors();
		List<DoorAccessLiteDTO> dtosL = new ArrayList<DoorAccessLiteDTO>();
		if(dtos != null && dtos.size() > 0){
			for(DoorAccessDTO dto : dtos){
				dtosL.add(ConvertHelper.convert(dto, DoorAccessLiteDTO.class));
			}
		}
		ListDoorAccessLiteResponse rspL = ConvertHelper.convert(rsp, ListDoorAccessLiteResponse.class);
		rspL.setDoors(dtosL);
		return rspL;
	}

	@Override
	public DoorAccessDTO getDoorAccessById(GetDoorAccessByIdCommand cmd) {
		DoorAccess da = doorAccessProvider.getDoorAccessById(cmd.getDoorId());
		if(da == null){
			return null;
		}
        getDoorAccessLastTick(da);
        
        DoorAccessDTO dto = ConvertHelper.convert(da, DoorAccessDTO.class);
        Aclink aclink = aclinkProvider.getAclinkByDoorId(dto.getId());
        if(aclink != null){
        	dto.setVersion(aclink.getFirwareVer());
        }else{
        	LOGGER.info(String.format("can not find aclink with doorId:%d",dto.getId()));
        }
        
        UserInfo user = userService.getUserSnapshotInfoWithPhone(da.getCreatorUserId());
        if(user != null) {
            String nickName = (user.getNickName() == null ? user.getNickName(): user.getAccountName());
            dto.setCreatorName(nickName);
            String phone = null;
            if(user.getPhones() != null && user.getPhones().size() > 0) {
                phone = user.getPhones().get(0);
                }

            dto.setCreatorPhone(phone);
            }
        
        if(da.getGroupid() != 0) {
            DoorAccess group = doorAccessProvider.getDoorAccessById(da.getGroupid());
            if(group != null) {
                dto.setGroupId(group.getId());
                dto.setGroupName(group.getDisplayNameNotEmpty());
            }
        }
        
        if(dto.getDisplayName() == null) {
            dto.setDisplayName(dto.getName());
        }
        
        if(dto.getEnableAmount() == null){
        	dto.setEnableAmount((byte) 0);
        }

        if(da.getLocalServerId() != null && da.getLocalServerId() != 0L){
        	AclinkServerDTO serverDto = aclinkServerService.findLocalServerById(da.getLocalServerId());
        	if(serverDto != null ){
        		dto.setServer(serverDto);
        	}
        }
        
        List<AclinkServerRelDTO> recDevices = new ArrayList<AclinkServerRelDTO>();
        ListLocalIpadCommand ipadCmd = new ListLocalIpadCommand();
        ipadCmd.setDoorAccessId(dto.getId());
        List<AclinkIPadDTO> ipads = aclinkIpadService.listLocalIpads(new CrossShardListingLocator(), ipadCmd).getAclinkIpads();
        if(ipads != null && ipads.size() > 0){
        	for(AclinkIPadDTO ipad : ipads){
        		AclinkServerRelDTO device = new AclinkServerRelDTO();
        		device.setId(ipad.getId());
        		device.setDeviceName(ipad.getName());
        		device.setEnterStatus(ipad.getEnterStatus());
        		device.setDeviceType((byte) 1);
        		recDevices.add(device);
        	}
        }
        ListLocalCamerasCommand cameraCmd = new ListLocalCamerasCommand();
        cameraCmd.setDoorAccessId(dto.getId());
        List<AclinkCameraDTO> cameras = aclinkCameraService.listLocalCameras(cameraCmd).getAclinkCameras();
        if(cameras != null && cameras.size() > 0){
        	for(AclinkCameraDTO camera : cameras){
        		AclinkServerRelDTO device = new AclinkServerRelDTO();
        		device.setId(camera.getId());
        		device.setDeviceName(camera.getName());
        		device.setEnterStatus(camera.getEnterStatus());
        		device.setDeviceType((byte) 2);
        		recDevices.add(device);
        	}
        }
        dto.setRecDevices(recDevices);
        if(dto.getMaxCount() == null || dto.getMaxCount() == 0 || dto.getMaxDuration() == null || dto.getMaxDuration() == 0){
        	dto.setUseCustomAuthConfig((byte) 0);
        }else{
        	dto.setUseCustomAuthConfig((byte) 1);
        }

		return dto;
	}

	//用户正式授权列表
	@Override
	public ListFormalAuthResponse listFormalAuth(ListFormalAuthCommand cmd) {
        //添加权限 by liqingyan
//      if (cmd.getOwnerType() != null && cmd.getOwnerType() == 1 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
//          userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4102041021L, cmd.getAppId(), null, cmd.getCurrentProjectId());
//      }
      if (cmd.getOwnerType() != null && cmd.getOwnerType() == 0 && cmd.getCurrentPMId() != null && cmd.getAppId() != null && configurationProvider.getBooleanValue("privilege.community.checkflag", true)) {
          userPrivilegeMgr.checkUserPrivilege(UserContext.current().getUser().getId(), cmd.getCurrentPMId(), 4111041111L, cmd.getAppId(), null, cmd.getCurrentProjectId());
      }
		ListFormalAuthResponse rsp = new ListFormalAuthResponse();
		CrossShardListingLocator locator = new CrossShardListingLocator();
		locator.setAnchor(cmd.getPageAnchor());
		cmd.setPageSize(PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize()));
		
		List<AclinkAuthDTO> authDtos = doorAuthProvider.listFormalAuth(locator,cmd.getPageSize(),cmd);
		
		rsp.setAuths(authDtos);
		rsp.setNextPageAnchor(locator.getAnchor());
		return rsp;
	}

	@Override
	public void updateAuthBatch(UpdateAuthBatchCommand cmd) {
		List<DoorAuth> auths = doorAuthProvider.queryDoorAuthAllLicensee(new CrossShardListingLocator(), 0, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
                query.addConditions(Tables.EH_DOOR_AUTH.ID.in(cmd.getAuthIds()));
                return query;
            }
        });
		for(DoorAuth auth : auths){
			if(cmd.getRightRemote() != null){
				auth.setRightRemote(cmd.getRightRemote());
			}
			if(cmd.getRightVisitor() != null){
				auth.setRightVisitor(cmd.getRightVisitor());
			}
			if(cmd.getStatus() != null){
				auth.setStatus(cmd.getStatus());
			}
		}
		doorAuthProvider.updateDoorAuth(auths);
	}

	@Override
	public void createFormalAuthBatch(CreateFormalAuthBatchCommand cmd) {
		User operator = UserContext.current().getUser();
		//TODO 设计文档里没有楼层,先不管
		//TODO 验证id列表
		Timestamp nowTime = new Timestamp(DateHelper.currentGMTTime().getTime());
		List<DoorAuth> authUsers = new ArrayList<DoorAuth>();
		//待优化
		if(cmd.getUserIds() != null && cmd.getUserIds().size() > 0){
			for(Long userId : cmd.getUserIds()){
				if(userId == null) continue;
				DoorAuth auth = new DoorAuth();
				//用户信息不能传id列表? 待优化
		        UserInfo custom = userService.getUserSnapshotInfoWithPhone(userId);//被授权人
		        if(custom == null) {
		        	throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "the input user is not found");
		        }
				auth.setUserId(userId);
				auth.setApproveUserId(operator.getId());
				auth.setAuthMethod(DoorAuthMethodType.ADMIN.getCode());
				auth.setAuthType(DoorAuthType.FOREVER.getCode());
				auth.setStatus(DoorAuthStatus.VALID.getCode());
				auth.setCreateTime(nowTime);
				auth.setLicenseeType(DoorLicenseType.USER.getCode());
				auth.setNickname(getRealName(ConvertHelper.convert(custom, User.class)));
				if(custom.getPhones() != null && custom.getPhones().size() > 0) {
					auth.setPhone(custom.getPhones().get(0));
				}
				if(null != cmd.getKeyU()){
					auth.setKeyU(cmd.getKeyU());
                }
				authUsers.add(auth);
			}
		}
			
		if (cmd.getOrgIds() != null && cmd.getOrgIds().size() > 0) {
			for (Long orgId : cmd.getOrgIds()) {
				if(orgId == null) continue;
				DoorAuth auth = new DoorAuth();
				// 查组织架构信息,不能传ids? 待优化
				OrganizationDTO org = organizationService.getOrganizationById(orgId);
				if (org == null) {
					throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE,
							AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "the input user is not found");
				}
				auth.setUserId(orgId);
				auth.setApproveUserId(operator.getId());
				auth.setAuthMethod(DoorAuthMethodType.ADMIN.getCode());
				auth.setAuthType(DoorAuthType.FOREVER.getCode());
				auth.setStatus(DoorAuthStatus.VALID.getCode());
				auth.setCreateTime(nowTime);
				auth.setLicenseeType(DoorLicenseType.ENTERPRISE.getCode());
				auth.setNickname(org.getName());
				if (null != cmd.getKeyU()) {
					auth.setKeyU(cmd.getKeyU());
				}
				authUsers.add(auth);
			}
		}
				
		if (cmd.getCompanyCommunityIds() != null && cmd.getCompanyCommunityIds().size() > 0) {
			GetCommunitiesByIdsCommand comCmd = new GetCommunitiesByIdsCommand();
			cmd.getCompanyCommunityIds().remove(null);
			comCmd.setIds(cmd.getCompanyCommunityIds());
			List<CommunityDTO> dtos = communityService.getCommunitiesByIds(comCmd);
			if (dtos == null) {
				throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE,
						AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "the input user is not found");
			}
			for (Long comId : cmd.getCompanyCommunityIds()) {
				if(comId == null) continue;
				DoorAuth auth = new DoorAuth();
				auth.setUserId(comId);
				auth.setApproveUserId(operator.getId());
				auth.setAuthMethod(DoorAuthMethodType.ADMIN.getCode());
				auth.setAuthType(DoorAuthType.FOREVER.getCode());
				auth.setStatus(DoorAuthStatus.VALID.getCode());
				auth.setCreateTime(nowTime);
				auth.setLicenseeType(DoorLicenseType.ORG_COMMUNITY.getCode());
				for(CommunityDTO dto : dtos){
					auth.setNickname(dto.getName());
				}
				if (null != cmd.getKeyU()) {
					auth.setKeyU(cmd.getKeyU());
				}
				authUsers.add(auth);
			}
		}
		
		if (cmd.getCompanyBuildingIds() != null && cmd.getCompanyBuildingIds().size() > 0) {
			for (Long comId : cmd.getCompanyBuildingIds()) {
				if(comId == null) continue;
				GetBuildingCommand buildingCmd = new GetBuildingCommand();
				buildingCmd.setBuildingId(comId);
				BuildingDTO dto = communityService.getBuilding(buildingCmd);
				if (dto == null) {
					throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE,
							AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "the input user is not found");
				}
				DoorAuth auth = new DoorAuth();
				auth.setUserId(comId);
				auth.setApproveUserId(operator.getId());
				auth.setAuthMethod(DoorAuthMethodType.ADMIN.getCode());
				auth.setAuthType(DoorAuthType.FOREVER.getCode());
				auth.setStatus(DoorAuthStatus.VALID.getCode());
				auth.setCreateTime(nowTime);
				auth.setLicenseeType(DoorLicenseType.ORG_BUILDING.getCode());
				auth.setNickname(dto.getName());
				if (null != cmd.getKeyU()) {
					auth.setKeyU(cmd.getKeyU());
				}
				authUsers.add(auth);
			}
		}
		
//		cmd.getCompanyAddressIds().removeAll(Collections.singleton(null)); 
		if (cmd.getCompanyAddressIds() != null && cmd.getCompanyAddressIds().size() > 0) {
			Integer namespaceId = UserContext.getCurrentNamespaceId();
			
			List<Address> dtos = addressProvider.listAddressByIds(namespaceId, cmd.getCompanyAddressIds()); 
			if (dtos == null) {
				throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE,
						AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "the input user is not found");
			}
			for (Long comId : cmd.getCompanyAddressIds()) {
				DoorAuth auth = new DoorAuth();
				auth.setUserId(comId);
				auth.setApproveUserId(operator.getId());
				auth.setAuthMethod(DoorAuthMethodType.ADMIN.getCode());
				auth.setAuthType(DoorAuthType.FOREVER.getCode());
				auth.setStatus(DoorAuthStatus.VALID.getCode());
				auth.setCreateTime(nowTime);
				auth.setLicenseeType(DoorLicenseType.ORG_ADDRESS.getCode());
				for(Address dto : dtos){
					auth.setNickname(dto.getAddress());
				}
				if (null != cmd.getKeyU()) {
					auth.setKeyU(cmd.getKeyU());
				}
				authUsers.add(auth);
			}
		}
		
//		cmd.getFamilyCommunityIds().removeAll(Collections.singleton(null)); 
		if (cmd.getFamilyCommunityIds() != null && cmd.getFamilyCommunityIds().size() > 0) {
			GetCommunitiesByIdsCommand comCmd = new GetCommunitiesByIdsCommand();
			comCmd.setIds(cmd.getFamilyCommunityIds());
			List<CommunityDTO> dtos = communityService.getCommunitiesByIds(comCmd);
			if (dtos == null) {
				throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE,
						AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "the input user is not found");
			}
			for (Long comId : cmd.getFamilyCommunityIds()) {
				DoorAuth auth = new DoorAuth();
				auth.setUserId(comId);
				auth.setApproveUserId(operator.getId());
				auth.setAuthMethod(DoorAuthMethodType.ADMIN.getCode());
				auth.setAuthType(DoorAuthType.FOREVER.getCode());
				auth.setStatus(DoorAuthStatus.VALID.getCode());
				auth.setCreateTime(nowTime);
				auth.setLicenseeType(DoorLicenseType.FAMILY_COMMUNITY.getCode());
				for(CommunityDTO dto : dtos){
					auth.setNickname(dto.getName());
				}
				if (null != cmd.getKeyU()) {
					auth.setKeyU(cmd.getKeyU());
				}
				authUsers.add(auth);
			}
		}
		
//		cmd.getFamilyBuildingIds().removeAll(Collections.singleton(null)); 
		if (cmd.getFamilyBuildingIds() != null && cmd.getFamilyBuildingIds().size() > 0) {
			for (Long comId : cmd.getFamilyBuildingIds()) {
				GetBuildingCommand buildingCmd = new GetBuildingCommand();
				buildingCmd.setBuildingId(comId);
				BuildingDTO dto = communityService.getBuilding(buildingCmd);
				if (dto == null) {
					throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE,
							AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "the input user is not found");
				}
				DoorAuth auth = new DoorAuth();
				auth.setUserId(comId);
				auth.setApproveUserId(operator.getId());
				auth.setAuthMethod(DoorAuthMethodType.ADMIN.getCode());
				auth.setAuthType(DoorAuthType.FOREVER.getCode());
				auth.setStatus(DoorAuthStatus.VALID.getCode());
				auth.setCreateTime(nowTime);
				auth.setLicenseeType(DoorLicenseType.FAMILY_BUILDING.getCode());
				auth.setNickname(dto.getName());
				if (null != cmd.getKeyU()) {
					auth.setKeyU(cmd.getKeyU());
				}
				authUsers.add(auth);
			}
		}
		
//		cmd.getFamilyAddressIds().removeAll(Collections.singleton(null)); 
		if (cmd.getFamilyAddressIds() != null && cmd.getFamilyAddressIds().size() > 0) {
			Integer namespaceId = UserContext.getCurrentNamespaceId();
			List<Address> dtos = addressProvider.listAddressByIds(namespaceId, cmd.getFamilyAddressIds()); 
			if (dtos == null) {
				throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE,
						AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "the input user is not found");
			}
			for (Long comId : cmd.getFamilyAddressIds()) {
				
				DoorAuth auth = new DoorAuth();
				auth.setUserId(comId);
				auth.setApproveUserId(operator.getId());
				auth.setAuthMethod(DoorAuthMethodType.ADMIN.getCode());
				auth.setAuthType(DoorAuthType.FOREVER.getCode());
				auth.setStatus(DoorAuthStatus.VALID.getCode());
				auth.setCreateTime(nowTime);
				auth.setLicenseeType(DoorLicenseType.FAMILY_ADDRESS.getCode());
				for(Address dto : dtos){
					auth.setNickname(dto.getAddress());
				}
				if (null != cmd.getKeyU()) {
					auth.setKeyU(cmd.getKeyU());
				}
				authUsers.add(auth);
			}
		}
		//按位置选择的门禁默认只有开门权限
		List<Long> doorIds = new ArrayList<Long>();
		List<Long> groupIds = new ArrayList<Long>();
		List<Long> communityIds = new ArrayList<Long>();
		List<String> buildingIds = new ArrayList<String>();
		List<String> floorIds = new ArrayList<String>();
		List<String> addressIds = new ArrayList<String>();
		//拼门禁查询条件
		if(cmd.getDoorAuthlist() != null && cmd.getDoorAuthlist().size() > 0){
			for(CreateFormalAuthCommand authCmd : cmd.getDoorAuthlist()){
				if(authCmd.getType() != null){
					if(authCmd.getType().equals(AuthGroupType.DOOR_ACCESS.getCode())){
						doorIds.add(Long.valueOf(authCmd.getId()));
					}else if(authCmd.getType().equals(AuthGroupType.DOOR_GROUP.getCode())){
						groupIds.add(Long.valueOf(authCmd.getId()));
					}else if(authCmd.getType().equals(AuthGroupType.COMMUNITY.getCode())){
						communityIds.add(Long.valueOf(authCmd.getId()));
					}else if(authCmd.getType().equals(AuthGroupType.BUILDING.getCode())){
						buildingIds.add(authCmd.getId());
					}else if(authCmd.getType().equals(AuthGroupType.FLOOR.getCode())){
						floorIds.add(authCmd.getId());
					}else if(authCmd.getType().equals(AuthGroupType.ADDRESS.getCode())){
						addressIds.add(authCmd.getId());
					}
				}
			}
		}else{
			throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_PARAM_ERROR, "door list is null");
		}
		
		//查需要授权的门禁列表
		List<DoorAccess> doors = doorAccessProvider.queryDoorAccesss(new CrossShardListingLocator(), 0, new ListingQueryBuilderCallback() {
            @Override
            public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                    SelectQuery<? extends Record> query) {
            	Condition con = Tables.EH_DOOR_ACCESS.ID.isNotNull();
                if(doorIds.size() > 0) {
                	con = con.or(Tables.EH_DOOR_ACCESS.ID.in(doorIds));
                }
                if(groupIds.size() > 0) {
                	con = con.or(Tables.EH_DOOR_ACCESS.GROUPID.in(groupIds));
                }
                if(communityIds.size() > 0) {
                	con = con.or(Tables.EH_DOOR_ACCESS.OWNER_ID.in(communityIds).and(Tables.EH_DOOR_ACCESS.OWNER_TYPE.eq(DoorAccessOwnerType.COMMUNITY.getCode())));
                }
                if(buildingIds.size() > 0) {
                	//公共门禁的ADRESS_DETAIL是楼栋或楼栋+楼层
                	con = con.or(Tables.EH_DOOR_ACCESS.ADRESS_DETAIL.in(buildingIds).and(Tables.EH_DOOR_ACCESS.OWNER_TYPE.eq(DoorAccessOwnerType.COMMUNITY.getCode())));
                }
                if(floorIds.size() > 0) {
                	//公共门禁的ADRESS_DETAIL是楼栋或楼栋_楼层
                	con = con.or(Tables.EH_DOOR_ACCESS.ADRESS_DETAIL.in(floorIds).and(Tables.EH_DOOR_ACCESS.OWNER_TYPE.eq(DoorAccessOwnerType.COMMUNITY.getCode())));
                }
                if(addressIds.size() > 0) {
                	//企业门禁的ADRESS_DETAIL是办公地点
                	con = con.or(Tables.EH_DOOR_ACCESS.ADRESS_DETAIL.in(addressIds).and(Tables.EH_DOOR_ACCESS.OWNER_TYPE.eq(DoorAccessOwnerType.ENTERPRISE.getCode())));
                }
                return query;
            }
            
        });
		
		List<DoorAuth> cAuths = new ArrayList<DoorAuth>();
		List<DoorAuth> uAuths = new ArrayList<DoorAuth>();
		
		for (CreateFormalAuthCommand authCmd : cmd.getDoorAuthlist()) {
			if (authCmd.getType() != null) {
				for (DoorAccess doorAcc : doors) {
					//找到每条cmd里的门禁
					if ((authCmd.getType().equals(AuthGroupType.DOOR_ACCESS.getCode())
							&& doorAcc.getId().equals(Long.valueOf(authCmd.getId())))
						|| (authCmd.getType().equals(AuthGroupType.DOOR_GROUP.getCode())
							&& doorAcc.getGroupid().equals(Long.valueOf(authCmd.getId())))
						|| (authCmd.getType().equals(AuthGroupType.COMMUNITY.getCode())
							&& doorAcc.getOwnerId().equals(Long.valueOf(authCmd.getId()))
							&& doorAcc.getOwnerType().equals(DoorAccessOwnerType.COMMUNITY.getCode()))
						|| ((authCmd.getType().equals(AuthGroupType.BUILDING.getCode())
							|| authCmd.getType().equals(AuthGroupType.FLOOR.getCode()))
							&& authCmd.getId().equals(doorAcc.getAdressDetail())
							&& doorAcc.getOwnerType().equals(DoorAccessOwnerType.COMMUNITY.getCode()))
						|| (authCmd.getType().equals(AuthGroupType.ADDRESS.getCode())
							&& authCmd.getId().equals(doorAcc.getAdressDetail())
							&& doorAcc.getOwnerType().equals(DoorAccessOwnerType.ENTERPRISE.getCode()))) {
						
						//生成单个用户正式授权列表
						for (DoorAuth item : authUsers) {
							DoorAuth auth = ConvertHelper.convert(item, DoorAuth.class);
							auth.setDoorId(doorAcc.getId());
							auth.setOwnerId(doorAcc.getOwnerId());
							auth.setOwnerType(doorAcc.getOwnerType());
							
							//正式授权只针对单个门禁
							auth.setGroupType(AuthGroupType.DOOR_ACCESS.getCode());
							// set driver
							if (doorAcc.getDoorType().equals(DoorAccessType.ACLINK_LINGLING.getCode()) 
									|| doorAcc.getDoorType().equals(DoorAccessType.ACLINK_LINGLING_GROUP.getCode())) {
								auth.setDriver(DoorAccessDriverType.LINGLING.getCode());
							} else if (doorAcc.getDoorType().equals(DoorAccessType.ACLINK_HUARUN_GROUP.getCode())) {
								auth.setDriver(DoorAccessDriverType.HUARUN_ANGUAN.getCode());
							} else if (doorAcc.getDoorType().equals(DoorAccessType.ACLINK_WANGLONG_GROUP.getCode())) {
								auth.setDriver(DoorAccessDriverType.WANG_LONG.getCode());
							} else if (doorAcc.getDoorType().equals(DoorAccessType.ACLINK_BUS.getCode())) {
								auth.setDriver(DoorAccessDriverType.BUS.getCode());
							} else {
								auth.setDriver(DoorAccessDriverType.ZUOLIN.getCode());
							}

							auth.setRightOpen((byte) 1);// 默认开启权限
							auth.setRightRemote(authCmd.getRightRemote() == null?(byte) 0 : authCmd.getRightRemote());
							auth.setRightVisitor(authCmd.getRightVisitor() == null? (byte) 0 : authCmd.getRightVisitor());

							// 正式授权门禁-单个对象
							DoorAuth authEx = doorAuthProvider.queryValidDoorAuthForever(doorAcc.getId(), auth.getUserId(), auth.getLicenseeType());
							if (authEx != null) {
								if (authCmd.getRightOpen() != null && authCmd.getRightOpen().equals((byte) 0)) {
									//关闭权限
									auth.setStatus((byte) 0);
									auth.setRightOpen((byte) 0);
								}
								auth.setId(authEx.getId());
								uAuths.add(auth);
							} else {
								if (authCmd.getRightOpen() != null && authCmd.getRightOpen().equals((byte) 0)) {
									//不要新增一条没有开门权限的记录
									continue;
								}
								cAuths.add(auth);
							}
						}
						
					}
				}
			}
		}
		
		doorAuthProvider.createDoorAuthBatch(cAuths);
		doorAuthProvider.updateDoorAuthBatch(uAuths);
		
        // 记录操作日志
		cAuths.addAll(uAuths);
        this.createDoorAuthLogBatch(cAuths);
        
        User tmpUser = new User();
        tmpUser.setNickName("admin");
        if (operator != null && operator.getId() != 0) {
            tmpUser.setNickName(getRealName(operator));
        }

        //TODO 批量发消息
//        tmpUser.setId(custom.getId());
//        tmpUser.setAccountName(custom.getAccountName());
//        //Send messages
//        if(doorAcc.getDoorType().equals(DoorAccessType.ACLINK_LINGLING.getCode())
//                || (doorAcc.getDoorType().equals(DoorAccessType.ACLINK_LINGLING_GROUP.getCode()))) {
//            sendMessageToUser(tmpUser, doorAcc, DoorAccessType.ACLINK_LINGLING_GROUP.getCode()); 
//        } else if(doorAcc.getDoorType().equals(DoorAccessType.ACLINK_HUARUN_GROUP.getCode())) {
//        		sendMessageToUser(tmpUser, doorAcc, DoorAccessType.ACLINK_HUARUN_GROUP.getCode());
//        } else {
//            sendMessageToUser(tmpUser, doorAcc, DoorAccessType.ZLACLINK_WIFI.getCode());    
//        }

        return ;
		
	}

	@Override
	public ListUserAuthResponse listUserKeys(ListAesUserKeyByUserCommand cmd) {
		// TODO Auto-generated method stub
		ListUserAuthResponse rsp = new ListUserAuthResponse();
		User user = UserContext.current().getUser();
		ListAuthsByLevelandLocationCommand qryCmd = getLevelQryCmdByUser(user);
		
		cmd.setPageSize(cmd.getPageSize() == null? 0 : cmd.getPageSize());
		CrossShardListingLocator locator = new CrossShardListingLocator();
		if(cmd.getPageAnchor() != null){
			locator.setAnchor(cmd.getPageAnchor());
		}
		List<DoorAuthLiteDTO> auths = doorAuthProvider.listAuthsByLevelandLocation(locator, cmd.getPageSize(), qryCmd);
//		List<DoorAuthLiteDTO> authDtos = new ArrayList<DoorAuthLiteDTO>();
//		for(DoorAuth auth : auths){
//			authDtos.add(ConvertHelper.convert(auth, DoorAuthLiteDTO.class));
//		}
		
//        Collections.sort(auths);
//        if(cmd.getPageSize() != null && cmd.getPageSize() > 0 && authDtos.size() > cmd.getPageSize()){
//        	for(DoorAuth auth : auths) {
//        		if(authDtos.get(authDtos.size() - 1).getId().equals(auth.getId())){
//        			locator.setAnchor(auth.getCreateTime().getTime());
//        		}
//                }
//        	authDtos.remove(authDtos.size() - 1);
//        }
		
		rsp.setAuths(auths);
        rsp.setNextPageAnchor(locator.getAnchor());
		return rsp;
	}
	

	@Override
	public GetUserKeyInfoRespnose getUserKeyInfo(GetUserKeyInfoCommand cmd) {
		GetUserKeyInfoRespnose rsp = new GetUserKeyInfoRespnose();
		User user = UserContext.current().getUser();
		
		rsp.setIsSupportRemote(DoorAuthStatus.INVALID.getCode());
		rsp.setIsSupportTempAuth(DoorAuthStatus.INVALID.getCode());
		
		if(cmd.getDoorId() == null && cmd.getAuthId() != null){
			DoorAuth auth = doorAuthProvider.getDoorAuthById(cmd.getAuthId());
			//校验用户
			if(!checkUserAuthByUser(user, auth)){
				throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, String.format("user.id {%d} not pair auth.userId{%d}",user.getId(),auth.getUserId()));
			}
			cmd.setDoorId(auth.getDoorId());
		}
		if(cmd.getDoorId() != null){
	        QueryValidDoorAuthForeverCommand qryCmd = ConvertHelper.convert(getLevelQryCmdByUser(user), QueryValidDoorAuthForeverCommand.class);
	        qryCmd.setDoorId(cmd.getDoorId());
			
			List<DoorAuth> authList = doorAuthProvider.listValidDoorAuthForever(qryCmd);
			if(authList != null && authList.size() > 0){
				DoorAuth auth = authList.get(0);
				for(DoorAuth itemAuth : authList){
					if(itemAuth.getRightRemote().equals(DoorAuthStatus.VALID.getCode())){
						rsp.setAuthId(itemAuth.getId());
						rsp.setIsSupportRemote(DoorAuthStatus.VALID.getCode());
						auth = itemAuth;
					}
					if(itemAuth.getRightVisitor().equals(DoorAuthStatus.VALID.getCode())){
						rsp.setIsSupportTempAuth(DoorAuthStatus.VALID.getCode());
					}
				}
				
				DoorAccess doorAccess = doorAccessProvider.getDoorAccessById(cmd.getDoorId());
				if (doorAccess != null && doorAccess.getStatus().equals(DoorAccessStatus.ACTIVE.getCode())) {
					rsp.setHardwareId(doorAccess.getHardwareId());
					// ---clone DoorAccess 20180408 by liuyilin
					if (doorAccess.getMacCopy() != null && !doorAccess.getMacCopy().isEmpty()) {
						rsp.setHardwareId(doorAccess.getMacCopy());
					}
					// ---
				}else{
					throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, String.format("door.id {%d} not found",cmd.getDoorId()));
				}

				rsp.setAuthId(auth.getId());
				rsp.setIsSupportQR(doorAccess.getHasQr());
				rsp.setIsSupportFaceOpen(doorAccess.getLocalServerId() != null && doorAccess.getLocalServerId() > 0
						? DoorAuthStatus.VALID.getCode() : DoorAuthStatus.INVALID.getCode());
//TODO 临时授权
//				rsp.setOpenRemainCount(auth.getValidAuthAmount());
				
				if(rsp.getIsSupportQR().equals(DoorAuthStatus.VALID.getCode())){
					
					// TODO Auto-generated method stub
			    	if(!auth.getAuthType().equals(DoorAuthType.FOREVER.getCode())) {
			    		Long now = DateHelper.currentGMTTime().getTime();
			    		if(auth.getValidEndMs() < now) {
			    		//已经失效，删除它
			            auth.setStatus(DoorAuthStatus.INVALID.getCode());
			            doorAuthProvider.updateDoorAuth(auth);
			            return null;
			            }
			    		
			    		if(auth.getValidFromMs() > now) {
			    			return null;
			    		}
			    	}
			    	//TODO 生成其他各种类型二维码钥匙
			    	List<DoorAccessQRKeyDTO> qrKeys = new ArrayList<DoorAccessQRKeyDTO>();
			        doZuolinQRKey(false, user, doorAccess, auth, qrKeys);
			        if(qrKeys.size() > 0){
			        	rsp.setQrInfo(qrKeys.get(0));
			        }
				}
				
				if(rsp.getIsSupportTempAuth().equals(DoorAuthStatus.VALID.getCode())){
					rsp.setIsAuthByCount(doorAccess.getEnableAmount());
					rsp.setMaxAuthCount(doorAccess.getMaxCount());
					rsp.setMaxAuthDay(doorAccess.getMaxDuration());
				}
//				rsp.setOpenVailadTime(auth.getc);
		        
		        //BLE
		        AesUserKey aesUserKey = generateAesUserKey(user, auth);
		        rsp.setBlueToothSecret(aesUserKey.getSecret());
			}else{
				throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, String.format("auth with user.id {%d} and door.id {%d} not found",user.getId(),cmd.getDoorId()));
			}
		}
		return rsp;
	}

	@Override
	public ListAccessGroupRelResponse listDoorGroupRel(ListDoorAccessGroupCommand cmd) {
		ListAccessGroupRelResponse rsp = new ListAccessGroupRelResponse();
		CrossShardListingLocator locator = new CrossShardListingLocator();
		if(cmd.getPageAnchor() != null){
			locator.setAnchor(cmd.getPageAnchor());
		}
		Integer count = cmd.getPageSize() == null ? 0 : cmd.getPageSize();
		List<DoorAccessGroupRelDTO> groupRels = doorAccessProvider.listDoorGroupRel(locator,count,cmd);
		rsp.setGroupRels(groupRels);
		if(locator.getAnchor() != null){
			rsp.setNextPageAnchor(locator.getAnchor());
		}
		return rsp;
	}
	
	private ListAuthsByLevelandLocationCommand getLevelQryCmdByUser(User user){
		GetUserDefaultAddressCommand addressCmd = new GetUserDefaultAddressCommand();
		addressCmd.setUserId(user.getId());
		UserAddressDTO addressDTO = businessService.getUserAddress(addressCmd);
		ListAuthsByLevelandLocationCommand qryCmd = new ListAuthsByLevelandLocationCommand();
		List<Long> orgIds = new ArrayList<Long>();
		//TODO 挪到组织架构
		List<OrganizationMember> oms = doorAuthProvider.getOrganizationMemberByUserId(user.getId());
		
		for(OrganizationMember om : oms){
			String[] pathStrs = om.getGroupPath().split("/");
			for(String pathStr : pathStrs){
				if(!pathStr.isEmpty() && orgIds.indexOf(Long.valueOf(pathStr)) < 0){
					orgIds.add(Long.valueOf(pathStr));
				}
			}
		}
		qryCmd.setUserId(user.getId());
		qryCmd.setOrgIds(orgIds);
		qryCmd.setFamilyAddresses(addressDTO.getFamilyAddresses());
		qryCmd.setOrganizationAddresses(addressDTO.getOrganizationAddresses());
		return qryCmd;
	}
	
	private boolean checkUserAuthByUser(User user, DoorAuth auth){
		ListAuthsByLevelandLocationCommand qryCmd = getLevelQryCmdByUser(user);
		Long authUid = auth.getUserId();
		
		List<Long> faIds = new ArrayList<Long>();
		List<Long> fbIds = new ArrayList<Long>();
		List<Long> fcIds = new ArrayList<Long>();
		for (FamilyAddressDTO dto : qryCmd.getFamilyAddresses()) {
			faIds.add(dto.getAdddress().getId());
			fbIds.add(dto.getAdddress().getBuildingId());
			fcIds.add(dto.getAdddress().getCommunityId());
		}
		
		List<Long> oaIds = new ArrayList<Long>();
		List<Long> obIds = new ArrayList<Long>();
		List<Long> ocIds = new ArrayList<Long>();
		for (OrganizationAddressDTO dto : qryCmd.getOrganizationAddresses()) {
			if (dto.getAdddresses() != null && dto.getAdddresses().size() > 0) {
				for (AddressDTO item : dto.getAdddresses()) {
					oaIds.add(item.getId());
					obIds.add(item.getBuildingId());
					ocIds.add(item.getCommunityId());
				}
			}
		}
		
		switch (DoorLicenseType.fromCode(auth.getLicenseeType() == null? 0:auth.getLicenseeType())){
			case USER:
				return authUid.equals(user.getId());
			case ENTERPRISE:
				return qryCmd.getOrgIds().indexOf(authUid) >= 0;
			case ORG_COMMUNITY:
				return qryCmd.getFamilyAddresses().indexOf(ocIds) >= 0;
			case ORG_BUILDING:
				return qryCmd.getFamilyAddresses().indexOf(obIds) >= 0;
			case ORG_ADDRESS:
				return qryCmd.getFamilyAddresses().indexOf(oaIds) >= 0;
			case FAMILY_COMMUNITY:
				return qryCmd.getFamilyAddresses().indexOf(fcIds) >= 0;
			case FAMILY_BUILDING:
				return qryCmd.getFamilyAddresses().indexOf(fbIds) >= 0;
			case FAMILY_ADDRESS:
				return qryCmd.getFamilyAddresses().indexOf(faIds) >= 0;
			default:
				return true;
			
		}
		
	}

}


