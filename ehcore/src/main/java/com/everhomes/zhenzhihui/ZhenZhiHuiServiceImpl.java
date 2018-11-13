// @formatter:off
package com.everhomes.zhenzhihui;

import com.alibaba.fastjson.JSON;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.launchpad.LaunchPadItemActionDataHandler;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationServiceImpl;
import com.everhomes.point.UserLevel;
import com.everhomes.rest.organization.OrganizationMemberGroupType;
import com.everhomes.rest.organization.OrganizationSimpleDTO;
import com.everhomes.rest.ui.user.SceneDTO;
import com.everhomes.rest.ui.user.SceneTokenDTO;
import com.everhomes.rest.ui.user.SceneType;
import com.everhomes.rest.user.IdentifierClaimStatus;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.rest.user.NamespaceUserType;
import com.everhomes.rest.user.UserCurrentEntityType;
import com.everhomes.rest.user.UserGender;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.UserStatus;
import com.everhomes.rest.zhenzhihui.CreateZhenZhiHuiUserAndEnterpriseInfoCommand;
import com.everhomes.rest.zhenzhihui.CreateZhenZhiHuiUserInfoCommand;
import com.everhomes.rest.zhenzhihui.CreateZhenZhiHuiUserInfoResponse;
import com.everhomes.rest.zhenzhihui.ZhenZhiHuiAffairType;
import com.everhomes.rest.zhenzhihui.ZhenZhiHuiEnterpriseInfoDTO;
import com.everhomes.rest.zhenzhihui.ZhenZhiHuiRedirectCommand;
import com.everhomes.rest.zhenzhihui.ZhenZhiHuiServer;
import com.everhomes.rest.zhenzhihui.ZhenZhiHuiDTO;
import com.everhomes.rest.zhenzhihui.ZhenZhiHuiServiceErrorCode;
import com.everhomes.rest.zhenzhihui.ZhenZhiHuiUserType;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.user.EncryptionUtils;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.WebTokenGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import sun.misc.BASE64Decoder;
import sun.rmi.runtime.Log;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.everhomes.controller.WebRequestInterceptor.setCookieInResponse;

@Component
public class ZhenZhiHuiServiceImpl implements ZhenZhiHuiService{
    private static final String charset       = "UTF-8";
//    private static final String APPENCKEY     = "ece2d40c2badef49";
//    private static final String SSOServiceURL = "http://w1505m3190.iok.la:56535/ZHYQ/restservices/LEAPAuthorize/attributes/query?TICKET=";
    private static final Integer ZHENZHIHUI_NAMESPACE_ID = 999931;
    private static final Long COMMUNITY_ID  = 240111044332063522L;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ZhenZhiHuiServiceImpl.class);

    private static final String REDIRECT_FILL_IN_USER_INFO = "/mobile/static/apply-form/apply_personal_form.html";
    private static final String REDIRECT_FILL_IN_USER_AND_ENTERPRISE_INFO = "/mobile/static/apply-form/apply_co_form.html";

    @Autowired
    private UserService userService;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private OrganizationServiceImpl organizationService;
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    private OrganizationProvider organizationProvider;
    @Autowired
    private ServiceModuleAppService serviceModuleAppService;
    @Autowired
    private ZhenzhihuiUserInfoProvider zhenzhihuiUserInfoProvider;
    @Autowired
    private ZhenzhihuiEnterpriseInfoProvider zhenzhihuiEnterpriseInfoProvider;
    @Override
    public String ssoService(HttpServletRequest request, HttpServletResponse response) {
        String TICKET = request.getParameter("TICKET");
        String serviceUrl = configurationProvider.getValue(ZHENZHIHUI_NAMESPACE_ID, "zhenzhihui.url", "");
        String appkey = configurationProvider.getValue(ZHENZHIHUI_NAMESPACE_ID, "zhenzhihui.appkey", "");
        String homeUrl = configurationProvider.getValue(0, "home.url","");
        if ( TICKET == null ) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "TICKET is null.");
        }

        try {
            //使用独有的身份认证时颁发的密钥解密数据
            TICKET = CBCDecrypt(TICKET, appkey);
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }

        if ( TICKET == null ) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,
                    ErrorCodes.ERROR_INVALID_PARAMETER, "TICKET is null.");
        }

        try {
            //使用attributes接口从SSO服务器获取信息
            String ret = requestService(serviceUrl + TICKET);
            if ( ret != null )
            {
                //使用独有密钥解密数据
                ret = CBCDecrypt(ret, appkey);
                Hashtable<String, String> bean = toJSON(ret);
                if ( bean != null )
                {
                    //目标用户名称
                    String target_user = bean.get("target_user");
                    //方法名
                    String method = bean.get("target_method");
                    //参数
                    String parameter = bean.get("parameter");
                    if(null != parameter){
                        String parameterval = new String( new BASE64Decoder().decodeBuffer(parameter) ,charset);
                        LOGGER.info("target_user = {}, method = {}, parameter = {}, parameterval = {}",target_user,method,parameter,parameterval);
                        ZhenZhiHuiDTO zhenZhiHuiUserInfoDTO = JSON.parseObject(parameterval, ZhenZhiHuiDTO.class);
                        if (zhenZhiHuiUserInfoDTO != null){
                            User user = this.userService.findUserByIndentifier(ZHENZHIHUI_NAMESPACE_ID, zhenZhiHuiUserInfoDTO.getShouji());
                            if (user == null) {
                                user = createUserAndUserIdentifier(zhenZhiHuiUserInfoDTO);
                                LOGGER.info("create user for zhenzhihui, userId = {}",user.getId());
                            }
                            UserLogin login = this.userService.innerLogin(ZHENZHIHUI_NAMESPACE_ID,user.getId(), null, null);
                            LoginToken token = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), login.getImpersonationId());
                            String tokenString = WebTokenGenerator.getInstance().toWebToken(token);
                            setCookieInResponse("token", tokenString, request, response);
                            LOGGER.info("zhenzhihui user login,userId = {}, token = {}", user.getId(), tokenString);
                            SceneTokenDTO sceneTokenDTO = new SceneTokenDTO();
                            sceneTokenDTO.setUserId(user.getId());
                            sceneTokenDTO.setNamespaceId(ZHENZHIHUI_NAMESPACE_ID);

                            List<Long> moduleIds = new ArrayList<>();
                            LOGGER.info("zhenzhihuiDTO = {}", zhenZhiHuiUserInfoDTO);
                            LOGGER.info("code = {}" ,Integer.valueOf(zhenZhiHuiUserInfoDTO.getCode()));
                            ZhenZhiHuiServer zhenZhiHuiServer = ZhenZhiHuiServer.fromStatus(Integer.valueOf(zhenZhiHuiUserInfoDTO.getCode()));
                            LOGGER.info("Server = {}", zhenZhiHuiServer);
                            moduleIds.add(zhenZhiHuiServer.getModule());
                            LOGGER.info("moduleId = {}", moduleIds);
                            List<ServiceModuleApp> serviceModuleApps = serviceModuleAppService.listReleaseServiceModuleAppByModuleIds(ZHENZHIHUI_NAMESPACE_ID, moduleIds);
                            if (CollectionUtils.isEmpty(serviceModuleApps)) {
                                LOGGER.error("APP is null");
                                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "app is null");
                            }
                            LOGGER.info("ServiceModuleApp = {}", serviceModuleApps.get(0));
                            String instanceConfig = "";
                            if (serviceModuleApps.size() == 1) {
                               instanceConfig =  serviceModuleApps.get(0).getInstanceConfig();
                            }else {
                                for (ServiceModuleApp serviceModuleApp : serviceModuleApps) {
                                    if (serviceModuleApp.getName().contains(zhenZhiHuiServer.getName())) {
                                        instanceConfig = serviceModuleApp.getInstanceConfig();
                                    }
                                }
                            }
                            LOGGER.info("instanceConfig = {}",instanceConfig);

                            JSONObject jsonObject = (JSONObject) JSONValue.parse(instanceConfig);

                            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(
                                    homeUrl+ZhenZhiHuiServer.fromStatus(Integer.valueOf(zhenZhiHuiUserInfoDTO.getCode())).getUrl());
                            LOGGER.info("init url = {}",builder.toUriString());
                            StringBuilder urlStr = new StringBuilder().append(builder.toUriString()).append("?");
                            List<OrganizationSimpleDTO> organizationSimpleDTOS = this.organizationService.listUserRelateOrganizations(user.getId());

                            if (CollectionUtils.isEmpty(organizationSimpleDTOS)) {
                                sceneTokenDTO.setScene(SceneType.PARK_TOURIST.getCode());
                                sceneTokenDTO.setEntityType(UserCurrentEntityType.COMMUNITY.getCode());

                                urlStr.append("entityType=").append(UserCurrentEntityType.COMMUNITY.getCode()).append("&");
                                builder.queryParam("entityType",UserCurrentEntityType.COMMUNITY.getCode());
                            }else {
                                sceneTokenDTO.setScene(SceneType.ENTERPRISE.getCode());
                                sceneTokenDTO.setEntityType(UserCurrentEntityType.ORGANIZATION.getCode());
                                sceneTokenDTO.setEntityId(organizationSimpleDTOS.get(0).getId());

                                urlStr.append("organizationId=").append(sceneTokenDTO.getEntityId()).append("&");
                                urlStr.append("entityType=").append(UserCurrentEntityType.ORGANIZATION.getCode()).append("&");
                                builder.queryParam("organizationId", sceneTokenDTO.getEntityId());
                                builder.queryParam("entityType",UserCurrentEntityType.ORGANIZATION.getCode());
                            }
                            LOGGER.info("sceneToken = {}", sceneTokenDTO);
                            String tokenStr = WebTokenGenerator.getInstance().toWebToken(sceneTokenDTO);
                            urlStr.append("ns=").append(ZHENZHIHUI_NAMESPACE_ID).append("&")
                                    .append("namespaceId=").append(ZHENZHIHUI_NAMESPACE_ID).append("&")
                                    .append("userId=").append(user.getId()).append("&")
                                    .append("sceneToken=").append(tokenStr).append("&")
                                    .append("communityId=").append(COMMUNITY_ID).append("&");
                            builder.queryParam("ns", ZHENZHIHUI_NAMESPACE_ID);
                            builder.queryParam("namespaceId", ZHENZHIHUI_NAMESPACE_ID);
                            builder.queryParam("userId", user.getId());
                            builder.queryParam("communityId", COMMUNITY_ID);
                            builder.queryParam("sceneToken",tokenStr);
                            for (Object key : jsonObject.keySet()) {
                                if (!key.toString().equals("url") && !key.toString().equals("categoryDTOList")) {
                                    urlStr.append(key.toString()).append("=").append(jsonObject.get(key)).append("&");
                                    builder.queryParam(key.toString(), jsonObject.get(key));
                                }
                            }
                            String url = urlStr.toString().substring(0,urlStr.toString().length()-1);
                            LOGGER.info("zhenzhihui redirect to zuolin, uri={}" , url);
                            return builder.build().toUriString();
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String zhenzhihuiRedirect(ZhenZhiHuiRedirectCommand cmd) {
        Long userId = UserContext.current().getUser().getId();
        LOGGER.info("user = {}",UserContext.current().getUser());
        if (cmd.getCode().equals(ZhenZhiHuiAffairType.ENTERPRISE.getCode())) {
            List<OrganizationMember> members = this.organizationProvider.listOrganizationMembersByUId(userId);
            if (CollectionUtils.isEmpty(members)) {
                LOGGER.error("no authority access to enterprise affair");
                throw RuntimeErrorException.errorWith(ZhenZhiHuiServiceErrorCode.SCOPE, ZhenZhiHuiServiceErrorCode.ERROR_NO_AUTHORITY, "no authority access to enterprise affair");
            }
            boolean authorityFlag = false;
            for (OrganizationMember member : members) {
                if (OrganizationMemberGroupType.MANAGER.getCode().equals(member.getMemberGroup())) {
                    authorityFlag = true;
                    break;
                }
            }
            if (!authorityFlag) {
                LOGGER.error("no authority access to enterprise affair");
                throw RuntimeErrorException.errorWith(ZhenZhiHuiServiceErrorCode.SCOPE, ZhenZhiHuiServiceErrorCode.ERROR_NO_AUTHORITY, "no authority access to enterprise affair");
            }
        }
        List<ZhenzhihuiUserInfo> zhenzhihuiUserInfoList = this.zhenzhihuiUserInfoProvider.listZhenzhihuiUserInfosByUserId(userId);
        String location = "";
        String homeUrl = this.configurationProvider.getValue(0,"home.url","https://core.zuolin.com");
        if (CollectionUtils.isEmpty(zhenzhihuiUserInfoList)) {
            if (cmd.getCode().equals(ZhenZhiHuiAffairType.ENTERPRISE.getCode())) {
                location = homeUrl + REDIRECT_FILL_IN_USER_AND_ENTERPRISE_INFO;
            }else {
                location = homeUrl + REDIRECT_FILL_IN_USER_INFO;
            }
            LOGGER.info("redirect to fill in location, url={}",location);
            return location;
        }
        List<ZhenzhihuiEnterpriseInfo> zhenZhiHuiEnterpriseInfos  = this.zhenzhihuiEnterpriseInfoProvider.listZhenzhihuiEnterpriseInfoByUserId(userId);
        if (CollectionUtils.isEmpty(zhenZhiHuiEnterpriseInfos) && cmd.getCode().equals(ZhenZhiHuiAffairType.ENTERPRISE.getCode())) {
            location = homeUrl + REDIRECT_FILL_IN_USER_AND_ENTERPRISE_INFO;
            LOGGER.info("redirect to fill in enterprise information, url={}",location);
            return location;
        }
        String zhenzhihuiUrl = this.configurationProvider.getValue(ZHENZHIHUI_NAMESPACE_ID,"zhenzhihui.redirect.url","https://core.zuolin.com?code=");
        location = zhenzhihuiUrl+cmd.getCode();
        LOGGER.info("redirect to zhenzhihui url={}",location);
        return location;
    }

    @Override
    public CreateZhenZhiHuiUserInfoResponse createZhenzhihuiUserInfo(CreateZhenZhiHuiUserInfoCommand cmd) {
        Long userId = UserContext.current().getUser().getId();
        ZhenzhihuiUserInfo zhenzhihuiUserInfo = ConvertHelper.convert(cmd, ZhenzhihuiUserInfo.class);
        zhenzhihuiUserInfo.setUserId(userId);
        this.zhenzhihuiUserInfoProvider.createZhenzhihuiUserInfo(zhenzhihuiUserInfo);
        String location = this.configurationProvider.getValue(ZHENZHIHUI_NAMESPACE_ID,"zhenzhihui.redirect.url","https://core.zuolin.com?code=")+ZhenZhiHuiAffairType.PERSON.getCode();
        LOGGER.info("redirect to zhenzhihui url={}",location);
        CreateZhenZhiHuiUserInfoResponse response = new CreateZhenZhiHuiUserInfoResponse();
        response.setLocation(location);
        return response;
    }

    @Override
    public CreateZhenZhiHuiUserInfoResponse createZhenzhihuiUserAndEnterpriseInfo(CreateZhenZhiHuiUserAndEnterpriseInfoCommand cmd) {
        Long userId = UserContext.current().getUser().getId();
        ZhenzhihuiUserInfo zhenzhihuiUserInfo = ConvertHelper.convert(cmd, ZhenzhihuiUserInfo.class);
        zhenzhihuiUserInfo.setUserId(userId);
        this.zhenzhihuiUserInfoProvider.createZhenzhihuiUserInfo(zhenzhihuiUserInfo);
        ZhenzhihuiEnterpriseInfo zhenzhihuiEnterpriseInfo = new ZhenzhihuiEnterpriseInfo();
        zhenzhihuiEnterpriseInfo.setUserId(userId);
        zhenzhihuiEnterpriseInfo.setCorporationName(cmd.getCorporationName());
        zhenzhihuiEnterpriseInfo.setIdentifyToken(cmd.getCorporationToken());
        zhenzhihuiEnterpriseInfo.setIdentifyType(cmd.getCorporationType());
        zhenzhihuiEnterpriseInfo.setEnterpriseName(cmd.getEnterpriseName());
        zhenzhihuiEnterpriseInfo.setEnterpriseToken(cmd.getEnterpriseToken());
        zhenzhihuiEnterpriseInfo.setEnterpriseType(cmd.getEnterpriseType());
        this.zhenzhihuiEnterpriseInfoProvider.createZhenzhihuiEnterpriseInfo(zhenzhihuiEnterpriseInfo);
        String location = this.configurationProvider.getValue(ZHENZHIHUI_NAMESPACE_ID,"zhenzhihui.redirect.url","https://core.zuolin.com?code=")+ZhenZhiHuiAffairType.ENTERPRISE.getCode();
        LOGGER.info("redirect to zhenzhihui url={}",location);
        CreateZhenZhiHuiUserInfoResponse response = new CreateZhenZhiHuiUserInfoResponse();
        response.setLocation(location);
        return response;
    }

    private User createUserAndUserIdentifier(ZhenZhiHuiDTO zhenZhiHuiUserInfoDTO){
        User user = new User();
        user.setStatus(UserStatus.ACTIVE.getCode());
        user.setNamespaceId(ZHENZHIHUI_NAMESPACE_ID);
        user.setNickName(zhenZhiHuiUserInfoDTO.getMingcheng());
        user.setGender(UserGender.UNDISCLOSURED.getCode());
        user.setNamespaceUserType(NamespaceUserType.ZHENZHIHUI.getCode());
        user.setNamespaceUserToken(zhenZhiHuiUserInfoDTO.getHaoma());
        user.setLevel(UserLevel.L1.getCode());
        user.setAvatar("");
        String salt = EncryptionUtils.createRandomSalt();
        user.setSalt(salt);
        try {
            user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", salt)));
        } catch (Exception e) {
            LOGGER.error("encode password failed");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PASSWORD, "Unable to create password hash");
        }
        UserIdentifier old_userIdentifier = this.userProvider.findClaimedIdentifierByToken(ZHENZHIHUI_NAMESPACE_ID, zhenZhiHuiUserInfoDTO.getShouji());

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
                return old_user;
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
            userIdentifier.setIdentifierToken(zhenZhiHuiUserInfoDTO.getShouji());
            userIdentifier.setNamespaceId(ZHENZHIHUI_NAMESPACE_ID);
            userIdentifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
            userIdentifier.setRegionCode(86);
            userIdentifier.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            userIdentifier.setNotifyTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            this.userProvider.createIdentifier(userIdentifier);
        }
        return user;
    }
    private static String requestService ( String url ) {

        HttpURLConnection httpURLConnection = null;
        InputStream input = null;
        OutputStream output = null;

        try {
            URL urlObj = new URL(url);
            httpURLConnection = (HttpURLConnection) urlObj.openConnection();
            httpURLConnection.setRequestMethod("GET");

            httpURLConnection.setRequestProperty("Accept-Language", "zh-cn");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows XP)");
            httpURLConnection.setRequestProperty("Accept", "*/*");

            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setUseCaches(false);

            httpURLConnection.connect();

            input = new BufferedInputStream(httpURLConnection.getInputStream());
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            output = new BufferedOutputStream(byteStream);

            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = input.read(buffer)) >= 0)
            {
                output.write(buffer, 0, count);
            }

            input.close();
            input = null;
            output.flush();
            output.close();
            output = null;

            return new String(byteStream.toByteArray(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if ( input != null )
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if ( output != null ) {
                try {
                    output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if ( httpURLConnection != null )
                httpURLConnection.disconnect();
        }
        return null;
    }

    private static String CBCDecrypt ( String data , String sourcekey ) throws Exception {
        if ( sourcekey == null || sourcekey.length() != 16 ) {
            throw new Exception("原始KEY必须为16字节字符串");
        }
        byte[] key = sourcekey.substring(0, 8).getBytes(charset);
        byte[] iv = sourcekey.substring(8).getBytes(charset);

        DESKeySpec dks = new DESKeySpec(key);

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(dks);

        // using DES in CBC mode
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        // 若采用NoPadding模式，data长度必须是8的倍数
        // Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");

        // 用密匙初始化Cipher对象
        IvParameterSpec param = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, param);

        // 正式执行解密操作
        byte decryptedData[] = cipher.doFinal(new BASE64Decoder().decodeBuffer(data));

        return new String(decryptedData, charset);
    }

    private static Hashtable<String, String> toJSON ( String str ) {
        BufferedReader sr = null;
        try {
            sr = new BufferedReader(new StringReader(str));
            String line = null;
            sr.readLine();
            Hashtable<String, String> ret = new Hashtable<String, String>();
            while ((line = sr.readLine()) != null) {
                if ( line.length() == 1 )
                    continue;
                int idx = line.indexOf(":");
                String name = line.substring(1, idx - 1);
                String val = null;
                if(line.endsWith(","))
                    val = line.substring(idx + 2, line.length() - 2);
                else val = line.substring(idx + 2, line.length() - 1);

                ret.put(name, val);
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if ( sr != null )
                try
                {
                    sr.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
        }
        return null;
    }

}
