// @formatter:off
package com.everhomes.aclink.admin;

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
import com.everhomes.aclink.*;
import com.everhomes.aclink.faceplusplus.FacePlusPlusService;
import com.everhomes.aclink.huarun.*;
import com.everhomes.aclink.lingling.*;
import com.everhomes.aclink.uclbrt.EncryptUtil;
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
import com.everhomes.openapi.AppNamespaceMapping;
import com.everhomes.openapi.AppNamespaceMappingProvider;
import com.everhomes.organization.*;
import com.everhomes.payment.util.DownloadUtil;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.aclink.*;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.admin.NamespaceDTO;
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
import com.everhomes.rest.organization.*;
import com.everhomes.rest.rpc.server.AclinkRemotePdu;
import com.everhomes.rest.sms.SmsTemplateCode;
import com.everhomes.rest.user.*;
import com.everhomes.rest.yellowPage.YellowPageServiceErrorCode;
import com.everhomes.sequence.LocalSequenceGenerator;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhUserIdentifiers;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.sms.DateUtil;
import com.everhomes.sms.SmsProvider;
import com.everhomes.user.*;
import com.everhomes.util.*;
import com.everhomes.util.excel.ExcelUtils;
import com.everhomes.util.file.FileUtils;
import com.everhomes.yellowPage.YellowPageUtils;
import com.google.gson.JsonObject;
import com.microsoft.schemas.office.x2006.encryption.CTKeyEncryptor;
import com.sun.jndi.toolkit.url.Uri;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.jasper.tagplugins.jstl.core.Url;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.sql.Timestamp;
import java.text.Format;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private final static String URL_FACEPLUSPLUS = "27.115.23.218";
    private final static String FACEPLUSPLUS_USERNAME = "test@megvii.com";
    private final static String FACEPLUSPLUS_PASSWORD = "123456";

    @Override
    public String login (){
        String url = URL_FACEPLUSPLUS + "/auth/login";
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
            jsonParam.put("username", FACEPLUSPLUS_USERNAME);
            jsonParam.put("password", FACEPLUSPLUS_PASSWORD);
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
    public JSONObject createUser (String cookie, Integer subjectType, String name, Integer start_time, Integer end_time){
        String url = URL_FACEPLUSPLUS + "/subject";
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
            // http返回转为json格式
//            result = (JsonObject)StringHelper.fromJsonString(resEntity.toString(),JSONObject.class);
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
        return result;
    }

    @Override
    public String uploadPhoto (String cookie, String photourl, Integer subjectId){
        String url = URL_FACEPLUSPLUS + "/subject";
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = null;
        try{
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("cookie",cookie);
            httpPost.addHeader("Content-Type","multipart/form-data");
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            URL photoUrl = new URL(photourl);
            File file = new File(photoUrl.getFile());
//            builder.addBinaryBody("upload_file", fileStream,
//                    ContentType.APPLICATION_OCTET_STREAM, URLEncoder.encode(fileName, "UTF-8"));
            HttpEntity multipart = builder.build();

            httpPost.setEntity(multipart);

            response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            HttpEntity resEntity = response.getEntity();
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
        return result;
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


