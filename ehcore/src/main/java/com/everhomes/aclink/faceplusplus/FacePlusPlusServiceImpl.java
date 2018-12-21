// @formatter:off
package com.everhomes.aclink.faceplusplus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.aclink.*;
import com.everhomes.aclink.huarun.*;
import com.everhomes.aclink.lingling.*;
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
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.locale.LocaleTemplateProvider;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.openapi.AppNamespaceMappingProvider;
import com.everhomes.organization.*;
import com.everhomes.rest.aclink.*;
import com.everhomes.rest.pmtask.PmTaskErrorCode;
import com.everhomes.rest.user.*;
import com.everhomes.server.schema.Tables;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.*;
import com.everhomes.util.*;
import com.everhomes.util.file.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Pattern;

@Component
public class FacePlusPlusServiceImpl implements FacePlusPlusService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FacePlusPlusServiceImpl.class);

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

    //face++对接
    private final static String URL_FACEPLUSPLUS = "http://27.115.13.218:8867";
    private final static String FACEPLUSPLUS_USERNAME = "test@megvii.com";
    private final static String FACEPLUSPLUS_PASSWORD = "123456";

    @Override
    public String login (String ip,String username, String password){
        String url = ip + "/auth/login";
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = null;
        String cookie = null;
        try{
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("user-agent", "Koala Admin");
            httpPost.addHeader("content-type","application/json");

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("username", username);
            jsonParam.put("password", password);
            StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if(status != 200){
                LOGGER.error("Failed to get the http result, url={}, status={}", url, response.getStatusLine());
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "Failed to get the http result");
            }
            HttpEntity resEntity = response.getEntity();
            Header[] setCookie = response.getHeaders("set-cookie");//cookie在响应头里
            cookie = setCookie[0].getValue();
            result = EntityUtils.toString(resEntity);
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
        return cookie;
    }

    @Override
    public JSONObject createUser (String cookie, Integer subjectType, String name, Long start_time, Long end_time,String ip){
        String url = ip + "/subject";
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        JSONObject result = null;
        try{
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("cookie",cookie);
            httpPost.addHeader("content-type","application/json");

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("subject_type", subjectType);
            jsonParam.put("name", name);
            //若为访客设置开始结束时间
            jsonParam.put("start_time", start_time);
            jsonParam.put("end_time", end_time);
            StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8");
//            entity.setContentEncoding("UTF-8");
//            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            response = httpClient.execute(httpPost);
            String json = EntityUtils.toString(response.getEntity(),"utf8");
            int status = response.getStatusLine().getStatusCode();
            if(status != 200){
                LOGGER.error("Failed to get the http result, url={}, status={}", url, response.getStatusLine());
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "Failed to get the http result");
            }
            HttpEntity resEntity = response.getEntity();
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
        return result;
    }
    private List<InputStream> downloadImags(String url){
        List<InputStream> photo = new ArrayList<>();
        if (url!=null && url.length()>0){
            try {
                CloseableHttpClient httpClient = null;
                httpClient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet(url);
                CloseableHttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                InputStream is = httpEntity.getContent();
                photo.add(is);
            }catch (IOException e){
                LOGGER.error("Pmtask request error, param={}", url, e);
                throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_REMOTE_INVOKE_FAIL,
                            "Pmtask download imgs error.");
            }
        }
        return  photo;
    }

    @Override
    public String uploadPhoto (String cookie, String photourl, String subjectId,String ip){
        String url = ip + "/subject/photo";
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = null;
        try{
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("cookie",cookie);
            //httpPost.addHeader("Content-Type","multipart/form-data");
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            ContentType contentType=ContentType.create("text/plain", Charset.forName("UTF-8"));
//            URL photoUrl = new URL(photourl);
//            File file = new File(photoUrl.getFile());

//            File file = null;
//            if(!StringUtils.isEmpty(url)){
//                try {
//                    file = FileUtils.getFileFormUrl(photourl, "name");
//                } catch (Exception e) {}
//            }
//            builder.addPart("photo", new FileBody(file));
            List<InputStream> photo = this.downloadImags(photourl);
            //builder.addBinaryBody()

            ContentType ct = ContentType.create("image/jpeg");
            builder.addBinaryBody("photo", photo.get(0),
                    ct, URLEncoder.encode("img.jpg", "UTF-8"));
            builder.addPart("subject_id",new StringBody(subjectId,contentType) );
            HttpEntity multipart = builder.build();

            httpPost.setEntity(multipart);

            response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            HttpEntity resEntity = response.getEntity();
            result = EntityUtils.toString(response.getEntity(),"utf8");
//            result = JSONObject.parseObject(json);
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
        return result;
    }

    @Override
    public void deleteUser (String cookie, String subjectId, String ip){
        String url = ip + "/subject/" + subjectId;
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        JSONObject result = null;
        try{
            httpClient = HttpClients.createDefault();
            HttpDelete httpDelete = new HttpDelete(url);
            httpDelete.addHeader("cookie",cookie);

            response = httpClient.execute(httpDelete);
            int status = response.getStatusLine().getStatusCode();
            if(status != 200){
                LOGGER.error("Failed to get the http result, url={}, status={}", url, response.getStatusLine());
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "Failed to get the http result");
            }
            HttpEntity resEntity = response.getEntity();
            result = JSON.parseObject(resEntity.toString());
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
    }

    @Override
    public DoorAuth createAuth (DoorAuth doorAuth, CreateDoorAuthCommand cmd,UserInfo custom){
        AclinkFormValues ips = doorAccessProvider.findAclinkFormValues(doorAuth.getOwnerId(),doorAuth.getOwnerType(),AclinkFormValuesType.IP.getCode());
        AclinkFormValues usernames = doorAccessProvider.findAclinkFormValues(doorAuth.getOwnerId(),doorAuth.getOwnerType(),AclinkFormValuesType.USERNAME.getCode());
        AclinkFormValues passwords = doorAccessProvider.findAclinkFormValues(doorAuth.getOwnerId(),doorAuth.getOwnerType(),AclinkFormValuesType.PASSWORD.getCode());
        if(ips == null || usernames == null || passwords == null || ips.getValue().isEmpty() || usernames.getValue().isEmpty() || passwords.getValue().isEmpty()){
            LOGGER.error("unable to find ip, username or password");
            return null;
        }
        String ip = ips.getValue();
        String cookie = this.login(ip,usernames.getValue(),passwords.getValue());
        if(cmd.getRightOpen() == (byte)1){
            String userName = cmd.getUserId().toString();//face++ username 存左邻用户id
            JSONObject response = null;
            if(doorAuth.getAuthType().equals(DoorAuthType.FOREVER.getCode())){
                response = this.createUser(cookie, 0, userName, 0L, 0L,ip);//subjectType 0 员工，1 访客 正式用户不传时间
            }else{
                response = this.createUser(cookie, 1, userName, cmd.getValidFromMs()/1000L, cmd.getValidEndMs()/1000L,ip);//subjectType 0 员工，1 访客 正式用户不传时间
            }
            String subjectId = response.getJSONObject("data").getString("id");//face++用户ID
            if(subjectId != null){
                doorAuth.setStringTag2(subjectId);//face++用户ID存入eh_door_auth.string_tag2
                doorAuthProvider.updateDoorAuth(doorAuth);
            }
            CrossShardListingLocator locator = new CrossShardListingLocator();
            List<FaceRecognitionPhoto> photos = faceRecognitionPhotoProvider. listFacialRecognitionPhotoByUser(locator, custom.getId(), 9999);
            if(!ListUtils.isEmpty(photos)){
                String photoUrl = photos.get(0).getImgUrl();
                //上传照片
                this.uploadPhoto(cookie,photoUrl,subjectId,ip);
            }
        }else if(cmd.getRightOpen() == (byte)0){
            this.deleteUser(cookie, doorAuth.getStringTag2(),ip);
            doorAuth.setStringTag2(null);
            doorAuthProvider.updateDoorAuth(doorAuth);
        }
        return doorAuth;
    }

    @Override
    public void addPhoto(String url,Long authId,Long userId){
        //访客授权记录
        List<DoorAuth> auths = new ArrayList<>();
        if(authId != null){
            DoorAuth auth = doorAuthProvider.getDoorAuthById(authId);
            if(auth != null && auth.getDriver().equals(DoorAccessDriverType.FACEPLUSPLUS.getCode()) && auth.getStringTag2() != null){
                auths.add(auth);
            }else {
                LOGGER.error("unable to find auth");
                return;
            }
        }
        //用户授权记录
        if(userId != null){
            ListingLocator locator = new ListingLocator();
            List<DoorAuth> authsx = doorAuthProvider.queryDoorAuth(locator, 9999, new ListingQueryBuilderCallback() {
                @Override
                public SelectQuery<? extends Record> buildCondition(ListingLocator locator,
                                                                    SelectQuery<? extends Record> query) {
                    query.addConditions(Tables.EH_DOOR_AUTH.USER_ID.eq(userId));
                    query.addConditions(Tables.EH_DOOR_AUTH.DRIVER.eq(DoorAccessDriverType.FACEPLUSPLUS.getCode()));
                    query.addConditions(Tables.EH_DOOR_AUTH.RIGHT_OPEN.eq((byte)1));
                    query.addConditions(Tables.EH_DOOR_AUTH.STRING_TAG2.isNotNull());
                    return query;
                }
            });
            if(ListUtils.isEmpty(authsx)){
                LOGGER.error("unable to find auth");
                return;
            }
            auths.addAll(authsx);
        }
        for(DoorAuth auth: auths){
            AclinkFormValues ips = doorAccessProvider.findAclinkFormValues(auth.getOwnerId(),auth.getOwnerType(),AclinkFormValuesType.IP.getCode());
            AclinkFormValues usernames = doorAccessProvider.findAclinkFormValues(auth.getOwnerId(),auth.getOwnerType(),AclinkFormValuesType.USERNAME.getCode());
            AclinkFormValues passwords = doorAccessProvider.findAclinkFormValues(auth.getOwnerId(),auth.getOwnerType(),AclinkFormValuesType.PASSWORD.getCode());
            if(ips == null || usernames == null || passwords == null || ips.getValue().isEmpty() || usernames.getValue().isEmpty() || passwords.getValue().isEmpty()){
                LOGGER.error("unable to find ip, username or password");
                return;
            }
            String ip = ips.getValue();
            String cookie = this.login(ip,usernames.getValue(),passwords.getValue());
            this.uploadPhoto(cookie,url,auth.getStringTag2(),ip);
        }
    }

    @Override
    public String filetest (String url){
        File file = null;
        if(!StringUtils.isEmpty(url)){
            try {
                file = FileUtils.getFileFormUrl(url, "name");
            } catch (Exception e) {}
        }
        return file.getPath();
    }
}


