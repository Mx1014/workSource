// @formatter:off
package com.everhomes.aclink.dingxin;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.aclink.*;
import com.everhomes.aclink.huarun.AclinkHuarunService;
import com.everhomes.aclink.lingling.AclinkLinglingService;
import com.everhomes.aclink.uclbrt.UclbrtHttpClient;
import com.everhomes.address.AddressProvider;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.blacklist.BlacklistService;
import com.everhomes.border.BorderConnectionProvider;
import com.everhomes.border.BorderProvider;
import com.everhomes.bus.LocalBus;
import com.everhomes.business.BusinessService;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.CommunityService;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.DbProvider;
import com.everhomes.group.GroupProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateProvider;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.openapi.AppNamespaceMappingProvider;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.aclink.*;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.*;
import com.everhomes.util.DateHelper;
import com.everhomes.util.ListUtils;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.WebTokenGenerator;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class DingxinServiceImpl implements DingxinService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DingxinServiceImpl.class);

    @Autowired
    UclbrtHttpClient uclbrtHttpClient;
    @Autowired
    BigCollectionProvider bigCollectionProvider;
    
    @Autowired
    AppNamespaceMappingProvider appNamespaceMappingProvider;

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
    private AclinkLogService aclinkLogService;

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
    private AclinkIpadProvider aclinkIpadProvider;
    
    @Autowired
    private AclinkCameraService aclinkCameraService;

    @Autowired
    private AclinkCameraProvider aclinkCameraProvider;
    
    @Autowired
    private BusinessService businessService;
    
    @Autowired
    private UserPrivilegeMgr userPrivilegeMgr;

    @Autowired
    private ConfigurationProvider configurationProvider;
    
    @Autowired
    private AclinkMessageSequence aclinkMessageSequence;
    
    AlipayClient alipayClient;
    
    final Pattern npattern = Pattern.compile("\\d+");
    
    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    //鼎芯对接
    private final static String URL_DINGXIN = "http://smart.dchiptech.com/door/api/access/openLockForGWYH";

//    public static void main(String[] args) {
//        String s = WebTokenGenerator.getInstance().toWebToken("01");
//        System.out.println(s);
//        System.out.println(WebTokenGenerator.getInstance().fromWebToken("d3JlYue7yMxO2ZxjwBh9d6ioevgA8uTU1fT52dxEknfm41q_juKua1ohUErO6RzGMtk_gENC6_Ur6_xsrcFKVA", String.class));
//        // gDNryJMR8scqDzAlNRaB0VlQ8hIJgEqL2n9UQN_c2KY
//    }

    @Override
    public String verifyDoorAuth(VerifyDoorAuthCommand cmd){
        List<DoorAuth> auths = new ArrayList<>();
//        if(cmd.getAuthType().equals("user")){
//           auths = doorAuthProvider.listValidDoorAuthByUser(cmd.getUserId(), DoorAccessDriverType.DINGXIN.getCode());
//        } else if(cmd.getAuthType().equals("visitor")){
//            DoorAuth auth = doorAuthProvider.getDoorAuthById(cmd.getUserId());
//            if(auth.getStatus().equals(DoorAuthStatus.VALID.getCode()) && auth.getRightOpen().equals(DoorAuthStatus.VALID.getCode())){
//                auths.add(auth);
//            }
//        }
        //token加密
        String s = WebTokenGenerator.getInstance().fromWebToken(cmd.getUserId(), String.class);
        String[] ss = s.split(",");
        String userType = ss[0];
        String userId = ss[1];
        if(userType.equals("1")){
           auths = doorAuthProvider.listValidDoorAuthByUser(Long.parseLong(userId), DoorAccessDriverType.DINGXIN.getCode());
        } else if(userType.equals("0")){
            DoorAuth auth = doorAuthProvider.getDoorAuthById(Long.parseLong(userId));
            if(auth.getStatus().equals(DoorAuthStatus.VALID.getCode()) && auth.getRightOpen().equals(DoorAuthStatus.VALID.getCode())){
                auths.add(auth);
            }
        }
        List<Long> doorIds = new ArrayList<>();
        if(ListUtils.isEmpty(auths)){
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, "auth is not found");
        } else {
            auths.stream().forEach(n -> doorIds.add(n.getDoorId()));
        }
        DoorAccess door = doorAccessProvider.queryDoorAccessByHardwareId(cmd.getUid());
        String result = null;
        if(door == null){
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_DOOR_NOT_FOUND, "door is not found");
        } else if(!doorIds.contains(door.getId())){
            throw RuntimeErrorException.errorWith(AclinkServiceErrorCode.SCOPE, AclinkServiceErrorCode.ERROR_ACLINK_USER_AUTH_ERROR, "auth is not found");
        } else{
            JSONObject response = this.openDoor(cmd.getUid());
            if(response.getInteger("code").equals(200)){
                //生成日志
                AclinkLogCreateCommand logCmd = new AclinkLogCreateCommand();
                AclinkLogItem item = new AclinkLogItem();
                item.setEventType(1L); //二维码开门
                item.setDoorId(door.getId());
                item.setLogTime(DateHelper.currentGMTTime().getTime());
                item.setNamespaceId(UserContext.getCurrentNamespaceId());
                item.setAuthId(auths.get(0).getId());
                item.setUserId(auths.get(0).getUserId());
                List<AclinkLogItem> items = new ArrayList<>();
                items.add(item);
                logCmd.setItems(items);
                aclinkLogService.createAclinkLog(logCmd);
            }
            result = response.getString("msg");
        }
        return result;
    }

    @Override
    public JSONObject openDoor(String uid){
        String url = URL_DINGXIN;
//        configurationProvider.getValue()
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        JSONObject result = null;
        try{
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("content-type","application/json");
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("uid", uid);
            StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            LOGGER.info("start to connect dingxin");
            response = httpClient.execute(httpPost);
            String json = EntityUtils.toString(response.getEntity(),"utf8");
            int status = response.getStatusLine().getStatusCode();
            if(status != 200){
                LOGGER.error("Failed to get the http result, url={}, status={}", url, response.getStatusLine());
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "Failed to get the http result");
            }
            // http返回转为json格式
            result = JSONObject.parseObject(json);

            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("Get http result, url={}, result={}", url, result);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to get the http result, url={}", url, e);
        } finally {
            if(response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        LOGGER.info("finished");
        return result;
    }
}


