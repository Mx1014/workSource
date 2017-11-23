package com.everhomes.wanke;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.border.Border;
import com.everhomes.border.BorderProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServer;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.namespace.Namespace;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.organization.ListOrganizationCommunityCommand;
import com.everhomes.rest.organization.ListOrganizationCommunityV2CommandResponse;
import com.everhomes.rest.user.*;
import com.everhomes.rest.wanke.GetSignCommand;
import com.everhomes.rest.wanke.GetSignDTO;
import com.everhomes.rest.wanke.ListCommunityCommand;
import com.everhomes.rest.wanke.ListCommunityResponse;
import com.everhomes.user.*;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.WebTokenGenerator;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component(ServiceConfVendorHandler.SERVICECONF_VENDOR_PREFIX+"wanke")
public class MaShenServiceConfVendorHandler implements ServiceConfVendorHandler{
	private static final Logger LOGGER = LoggerFactory.getLogger(MaShenServiceConfVendorHandler.class);
	private static CloseableHttpClient httpClient = null;
	static{ 
		SSLContext sslcontext = null;
		try {
			sslcontext = SSLContext.getInstance("SSLv3");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	// 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
		X509TrustManager trustManager = new X509TrustManager() {
			@Override
			public void checkClientTrusted(
					java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(
					java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		try {
			sslcontext.init(null, new TrustManager[] { trustManager }, null);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
        httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
	}
	
	@Autowired
	private UserService userService;
	@Autowired
	private DbProvider dbProvider;
	@Autowired
	private UserProvider userProvider;
	@Autowired
	private BorderProvider borderProvider;
	@Autowired
	private ContentServerService contentServerService;
	@Autowired
	private OrganizationProvider organizationProvider;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
    BigCollectionProvider bigCollectionProvider;
	@Autowired
    private ConfigurationProvider configProvider;
	@Autowired
    private CoordinationProvider coordinationProvider;
	
	@Override
	public ListCommunityResponse loginAndGetCommunities(ListCommunityCommand cmd, HttpServletRequest req, HttpServletResponse resp) {
     	ListCommunityResponse listCommunityResponse = new ListCommunityResponse();
     	String userToken = null;
     	String identifierToken = null;
     	String organizationToken = null;
     	String nickName = "";
     	final String initPassword = "123456";
     	//如果debug开启，则用来测试
		boolean isDebugEnabled = configProvider.getBooleanValue("wanke.test.isDebugEnabled", false);
     	if(isDebugEnabled) {
     		userToken = configProvider.getValue("wanke.test.userToken", "");
     		identifierToken = configProvider.getValue("wanke.test.identifierToken", "");
     		organizationToken = configProvider.getValue("wanke.test.organizationToken", "");
     	}else {
     		MashenResponseEntity entity = getUserInfoByToken(cmd.getToken());
    		Map<String, Object> result = entity.getData();
    		userToken = (String) result.get("uid");
    		identifierToken = (String) result.get("mobile");
    		nickName = (String) result.get("name");
    		if(StringUtils.isBlank(userToken) || StringUtils.isBlank(identifierToken)) {
    			LOGGER.error("UserToken or identifierToken from wanke is null, userToken={}, identifierToken={}", userToken, identifierToken);
    			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    					"UserToken or identifierToken from wanke is null");
    		}
    		List organizationList = (List) result.get("orgList");
    		if(null == organizationList || organizationList.isEmpty()){
    			LOGGER.error("User is not in organization, cmd={}, entity={}", cmd, entity);
    			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    					"user is not in organization");
    		}
    		organizationToken = ((Map<String, Object>)organizationList.get(0)).get("orgId").toString();
     	}
     	
		String regIp = getIp(req);
		UserIdentifier identifier = signup(userToken, identifierToken, cmd.getNamespaceId(), regIp, nickName);
		LogonCommandResponse logonCommandResponse = login(identifier, initPassword, req, resp);
		ListOrganizationCommunityCommand listOrganizationCommunityCommand = new ListOrganizationCommunityCommand();
		Organization organization = organizationProvider.findOrganizationByOrganizationToken(organizationToken);
		if(organization == null) {
			LOGGER.error("Organization not found, cmd=" + cmd);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Organization not found");
		}
		listOrganizationCommunityCommand.setOrganizationId(organization.getId());
		ListOrganizationCommunityV2CommandResponse listOrganizationCommunityV2CommandResponse = organizationService
				.listOrganizationCommunitiesV2(listOrganizationCommunityCommand);
		listCommunityResponse.setCommunities(listOrganizationCommunityV2CommandResponse.getCommunities());
		listCommunityResponse.setUserId(identifier.getOwnerUid());
		listCommunityResponse.setContentServer(logonCommandResponse.getContentServer());
		listCommunityResponse.setLoginToken(logonCommandResponse.getLoginToken());
		listCommunityResponse.setAccessPoints(logonCommandResponse.getAccessPoints());
		
		return listCommunityResponse;
	}
	
	private UserIdentifier signup(String userToken, String identifierToken, Integer namespaceId, String regIp, String nickName) {

		final Integer newNamespaceId = (namespaceId == null) ? Namespace.DEFAULT_NAMESPACE : namespaceId;
		
		UserIdentifier identifier = this.userProvider.findClaimedIdentifierByToken(newNamespaceId, identifierToken);
		if(identifier == null) {
			identifier = this.dbProvider.execute((TransactionStatus status) -> {
				User user = new User();
				user.setStatus(UserStatus.ACTIVE.getCode());
				user.setRegIp(regIp);
				user.setRegChannelId(0L);
				user.setNamespaceId(newNamespaceId);
				user.setNamespaceUserToken(userToken);
				// Type类名称太过简单，不易读，改为与数据库字段相匹配的类名，且该类放到user包中供所有模块使用 by lqs 20160922
				user.setNamespaceUserType(NamespaceUserType.WANKE.getCode());
				user.setGender(UserGender.UNDISCLOSURED.getCode());
				user.setNickName(nickName);
				String salt=EncryptionUtils.createRandomSalt();
				user.setSalt(salt);
				try {
					user.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s","123456",salt)));
				} catch (Exception e) {
					LOGGER.error("encode password failed");
					throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PASSWORD, "Unable to create password hash");

				}
				userProvider.createUser(user);

				UserIdentifier newIdentifier = new UserIdentifier();
				newIdentifier.setOwnerUid(user.getId());
				newIdentifier.setIdentifierType(IdentifierType.MOBILE.getCode());
				newIdentifier.setIdentifierToken(identifierToken);
				newIdentifier.setNamespaceId(newNamespaceId);
				newIdentifier.setClaimStatus(IdentifierClaimStatus.CLAIMED.getCode());
				userProvider.createIdentifier(newIdentifier);
				return newIdentifier;
			});
		}
		return identifier;
	}
	
	private LogonCommandResponse login(UserIdentifier identifier, String password, HttpServletRequest req, HttpServletResponse resp){
		UserLogin login = this.userService.logon(identifier.getNamespaceId(), identifier.getRegionCode(), identifier.getIdentifierToken(),
				password, "", "");
		LoginToken token = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), login.getImpersonationId());
		String tokenString = WebTokenGenerator.getInstance().toWebToken(token);

		LOGGER.debug(String.format("Return login info. token: %s, login info: ", tokenString, JSON.toJSONString(login)));
		setCookieInResponse("token", tokenString, req, resp);
        
        // 当从园区版登录时，有指定的namespaceId，需要对这些用户进行特殊处理
        Integer namespaceId = UserContext.getCurrentNamespaceId(identifier.getNamespaceId());
        if(namespaceId != null) {
            setCookieInResponse("namespace_id", String.valueOf(namespaceId), req, resp);
            userService.setDefaultCommunity(login.getUserId(), namespaceId);
        }

        LogonCommandResponse cmdResponse = new LogonCommandResponse(login.getUserId(), tokenString);
		cmdResponse.setAccessPoints(listAllBorderAccessPoints());
		cmdResponse.setContentServer(getContentServer());
		return cmdResponse;
	}
	
	private String getIp(HttpServletRequest request) {
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
		return ip;
	}
	
	private static void setCookieInResponse(String name, String value, HttpServletRequest request,
			HttpServletResponse response) {

		Cookie cookie = findCookieInRequest(name, request);
		if(cookie == null)
			cookie = new Cookie(name, value);
		else
			cookie.setValue(value);
		cookie.setPath("/");
		if(value == null || value.isEmpty())
			cookie.setMaxAge(0);

		response.addCookie(cookie);
	}
	
	private static Cookie findCookieInRequest(String name, HttpServletRequest request) {
		List<Cookie> matchedCookies = new ArrayList<>();

		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals(name)) {
					LOGGER.debug("Found matched cookie with name {} at value: {}, path: {}, version: {}", name,
							cookie.getValue(), cookie.getPath(), cookie.getVersion());
					matchedCookies.add(cookie);
				}
			}
		}

		if(matchedCookies.size() > 0)
			return matchedCookies.get(matchedCookies.size() - 1);
		return null;
	}
	
	private List<String> listAllBorderAccessPoints() {
		List<Border> borders = this.borderProvider.listAllBorders();
		return borders.stream().map((Border border) -> {
			return String.format("%s:%d", border.getPublicAddress(), border.getPublicPort());
		}).collect(Collectors.toList());
	}

	private String getContentServer(){
		try {
			ContentServer server = contentServerService.selectContentServer();
			return String.format("%s:%d",server.getPublicAddress(),server.getPublicPort());
		} catch (Exception e) {
			LOGGER.error("cannot find content server",e);
			return null;
		}
	}
	
	private MashenToken getAccessToken() {
		String appId = configProvider.getValue("wanke.mashen.appId", "");
        String appSecret = configProvider.getValue("wanke.mashen.appSecret", "");
		
    	return this.coordinationProvider.getNamedLock(CoordinationLocks.WANKE_LOGIN.getCode()).enter(()-> {
    		MashenToken ret = getAccessTokenFromCache(appId);
            if(ret == null) {
            	refreshAccessToken(appId, appSecret);
            	ret = getAccessTokenFromCache(appId);
            }
            return ret;
        }).first();
    	
    }
	
	private MashenToken refreshAndGetAccessToken() {
		String appId = configProvider.getValue("wanke.mashen.appId", "");
        String appSecret = configProvider.getValue("wanke.mashen.appSecret", "");
		
    	return this.coordinationProvider.getNamedLock(CoordinationLocks.WANKE_LOGIN.getCode()).enter(()-> {
    		refreshAccessToken(appId, appSecret);
    		MashenToken token = getAccessTokenFromCache(appId);
            return token;
        }).first();
    	
    }
	
	private MashenToken getAccessTokenFromCache(String appId) {
        String key = getAccessTokenKey(appId);
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
      
        Object value = redisTemplate.opsForValue().get(key);
        
        MashenToken ret = null;
        if(value != null) {
        	ret = JSONObject.parseObject(value.toString(), MashenToken.class);    
        }
        if(LOGGER.isDebugEnabled())
        	LOGGER.debug("Get mashen token from cache, key={}, Token={}", key, ret);
        
        return ret;
    }
	
	final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
	private void refreshAccessToken(String appId, String appSecret) {
        String key = getAccessTokenKey(appId);
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        
        Long chnlSn = System.currentTimeMillis();
        //从第三方请求accessToken
        MashenResponseEntity result = loginForAccessToken(appId, appSecret);        
        Map<String, Object> map = result.getData();
        String accessToken = (String) map.get("accessToken");
      //从第三方请求jsApiToken
        MashenResponseEntity jsResult = getJsToken(accessToken);
        Map<String, Object> jsMap = jsResult.getData();
        String jsapiToken = (String) jsMap.get("jsapiToken");
        MashenToken mashenToken = new MashenToken();
        mashenToken.setAccessToken(accessToken);
        mashenToken.setJsapiToken(jsapiToken);
        
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(mashenToken));
        redisTemplate.expire(key, 2, TimeUnit.HOURS);
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Refresh token, key={}, accessToken={}", key, accessToken);
		}
		
    }
	
	private String getAccessTokenKey(String appId) {
        return "wanke-accessToken-" + appId;
    }
	
	private MashenResponseEntity loginForAccessToken(String appId, String appSecret) {
		String param = "/token/get?appId=" + appId + "&appSecret=" + appSecret;
		MashenResponseEntity entity = httpGet(param);
		if(!entity.isSuccess()) {
			LOGGER.error("Response of Mashen, result={}.", entity);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Request of Mashen failed");
		}
        return entity;
	}
	
	private MashenResponseEntity getJsToken(String accessToken) {
		String param = "/token/getJsapiToken?accessToken=" + accessToken;
		MashenResponseEntity entity = httpGet(param);
		if(!entity.isSuccess()) {
			LOGGER.error("Response of Mashen, result={}.", entity);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Request of Mashen failed");
		}
        return entity;
	}
	
	private MashenResponseEntity getUserInfoByToken(String token) {
		MashenToken mashenToken = getAccessToken();
        String param = "/auth/getUserInfoByToken?token=" + token + "&accessToken=" + mashenToken.getAccessToken();
        MashenResponseEntity entity = httpGet(param);
        if(!entity.isSuccess()) {
        	mashenToken = refreshAndGetAccessToken();
        	param = "/auth/getUserInfoByToken?token=" + token + "&accessToken=" + mashenToken.getAccessToken();
        	entity = httpGet(param);
        	if(!entity.isSuccess()){
        		LOGGER.error("Wanke get user info failed, param={}, entity={}", param, entity);
    			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
    					"Wanke get user info failed");
        	}
		}
        return entity;
	}
	
	private MashenResponseEntity httpGet(String param){
		String url = configProvider.getValue("wanke.mashen.url", "");
		if(StringUtils.isBlank(url)) {
			LOGGER.error("Wanke.mashen.url not found, url={}", url);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Wanke.mashen.url not found");
		}
		HttpGet request = new HttpGet(url + param);
		HttpResponse rsp = null;
		try{
			rsp = httpClient.execute(request);
		}catch(Exception e){
			LOGGER.error("HTTP client execute exception, param={}", param, e);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"HTTP client execute exception.");
		}
		
		StatusLine status = rsp.getStatusLine();
		if(status.getStatusCode() == 200){
			String rspText;
			try {
				rspText = EntityUtils.toString(rsp.getEntity(), "UTF-8");
			} catch (Exception e) {
				LOGGER.error("HTTP client EntityUtils toString exception, param={}", param, e);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
						"HTTP client EntityUtils toString exception");
			}
			if(LOGGER.isDebugEnabled())
				LOGGER.debug("Mashen response info :rspText={}", rspText);
			
			//MashenResponseEntity resp = (MashenResponseEntity) StringHelper.fromJsonString(rspText, MashenResponseEntity.class);
			MashenResponseEntity resp = JSON.parseObject(rspText, MashenResponseEntity.class);
			
			return resp;
		}else{
			LOGGER.error("Mashen HTTP request response status is not 200, status, rspText={}.", status, rsp.getEntity());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
					"Mashen HTTP request response status is not 200");
		}
	}
	
	@Override
	public GetSignDTO getSign(GetSignCommand cmd) {
		MashenToken mashenToken = getAccessToken();
		GetSignDTO dto = sign(mashenToken.getJsapiToken(), cmd.getUrl());
		return dto;
	}
	
	 public GetSignDTO sign(String jsapiToken, String url) {
		 GetSignDTO dto = new GetSignDTO();
	     String nonceStr = create_nonce_str();
	     String timestamp = create_timestamp();
	     String string1;
	     String signature = "";
	     string1 = "jsapitoken=" + jsapiToken +
	                  "&noncestr=" + nonceStr +
	                  "&timestamp=" + timestamp +
	                  "&url=" + url;
	        try
	        {
	            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	            crypt.reset();
	            crypt.update(string1.getBytes("UTF-8"));
	            signature = byteToHex(crypt.digest());
	        }
	        catch (NoSuchAlgorithmException e)
	        {
	            e.printStackTrace();
	        }
	        catch (UnsupportedEncodingException e)
	        {
	            e.printStackTrace();
	        }
	        String appId = configProvider.getValue("wanke.mashen.appId", "");
	        dto.setUrl(url);
	        dto.setAppId(appId);
	        dto.setTimestamp(timestamp);
	        dto.setNonceStr(nonceStr);
	        dto.setSignature(signature);

	        return dto;
	  }
	 	
	 private String byteToHex(final byte[] hash) {
		 Formatter formatter = new Formatter();
	        for (byte b : hash)
	        {
	            formatter.format("%02x", b);
	        }
	     String result = formatter.toString();
	     formatter.close();
	     return result;	        
	 }

	 private String create_nonce_str() {
		 return UUID.randomUUID().toString();
	 }

	 private String create_timestamp() {
	     return Long.toString(System.currentTimeMillis());
	 }	    

		
}
