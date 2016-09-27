package com.everhomes.wx;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.rest.wx.GetContentServerUrlCommand;
import com.everhomes.rest.wx.GetSignatureCommand;
import com.everhomes.rest.wx.GetSignatureResponse;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;
import com.everhomes.util.WebTokenGenerator;
import com.everhomes.wx.WeChatService;
import com.google.gson.Gson;

@Component
public class WeChatServiceImpl implements WeChatService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WeChatServiceImpl.class);

    @Autowired
    private ConfigurationProvider  configProvider;
    
    @Autowired
    private BigCollectionProvider bigCollectionProvider;
    
    @Autowired
    private ContentServerService contentServerService;
    
    static final String ACCESS_TOKEN = "wechat.accesstoken";
    static final String JSAPI_TICKENT = "jsapi.ticket";
    
    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    
    Gson gson = new Gson();
    
	@Override
	public String getAccessToken() {
		Map<String, String> map = makeAccessToken();
		String accessToken = map.get(ACCESS_TOKEN);
		return accessToken;
	}
	
	@Override
	public String getJsapiTicket() {
		Map<String, String> map = makeJsApiTicket();
		String jsapiTicket = map.get(JSAPI_TICKENT);
		return jsapiTicket;
	}
	
	@Override
	public GetSignatureResponse getSignature(GetSignatureCommand cmd) {
		
		String ticket = getJsapiTicket();
		Map<String, String> ret = sign(ticket, cmd.getUrl());
		GetSignatureResponse resp = new GetSignatureResponse();
		resp.setNonceStr(ret.get("nonceStr"));
		resp.setSignature(ret.get("signature"));
		resp.setTimestamp(ret.get("timestamp"));
		resp.setTicket(ticket);
		return resp;
	}

	@Override
	public String getContentServerUrl(GetContentServerUrlCommand cmd) {
		String accessToken = getAccessToken();
		InputStream is = getInputStream(accessToken, cmd);

		String token = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
		UploadCsFileResponse response = contentServerService.uploadFileToContentServer(is, cmd.getFileName(), token);
		if(response.getErrorCode() == 0) {
			String url = response.getResponse().getUrl();
			return url;
		}
		return null;
	}
	
	public static String getFileName(String contentType) {
	    LOGGER.info(" WeChatService contentType : " + contentType);
	    if(!StringUtils.isEmpty(contentType)) {
	    	String[] str = contentType.split("/");
	    	String fileName = "wx_" + System.currentTimeMillis() + "." + str[1];
	    	
	    	return fileName;
	    }
	    
	    return null;
	  }
	
	private InputStream getInputStream(String accessToken, GetContentServerUrlCommand cmd) {
		InputStream is = null;
		HttpURLConnection http = null;
		String url = getRestUri(WeChatConstant.GET_MEDIA) + "access_token=" + accessToken + "&media_id=" + cmd.getMediaId();
		try {
			URL urlGet = new URL(url);
			http = (HttpURLConnection) urlGet
					.openConnection();
			http.setRequestMethod("GET"); 
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
			http.connect();
			// 获取文件转化为byte流
			is = http.getInputStream();
			
			String fileName = getFileName(http.getHeaderField("Content-Type"));
			cmd.setFileName(fileName);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			http.disconnect();
		}
		return is;

	}
	
	private String getRestUri(String methodName) {
        String serverUrl = configProvider.getValue(WeChatConstant.WECHAT_SERVER, "");
        
        StringBuffer sb = new StringBuffer(serverUrl);
        if(!serverUrl.endsWith("/"))
            sb.append("/");
        
        sb.append(methodName);
        sb.append("?");
        
        return sb.toString();
    }
	
	
	private Map<String, String> makeAccessToken() {
        
        
        Accessor acc = this.bigCollectionProvider.getMapAccessor(ACCESS_TOKEN, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        Object o = redisTemplate.opsForValue().get(ACCESS_TOKEN);
        
        String body = "";
        if(o != null) {
            body = (String)o;
        } else {
        	
            body = cacheAccessToken(redisTemplate);
            
            if(body == null) {
                return null;
            }
        
        }

        AccessTokenResponse resp = (AccessTokenResponse)StringHelper.fromJsonString(body, AccessTokenResponse.class);
        Map<String, String> keys = new HashMap<String, String>();
            
        keys.put(ACCESS_TOKEN, resp.getAccess_token());
        
        return keys;
    }
	
	//https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
	private String cacheAccessToken(RedisTemplate redisTemplate) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		LOGGER.info("cacheAccessToken :" + namespaceId);
    	String params = "grant_type=" + WeChatConstant.ACCESSTOKEN_GRANTTYPE
    					+ "&appid=" + configProvider.getValue(namespaceId, WeChatConstant.WECHAT_APPID, "")
    					+ "&secret=" + configProvider.getValue(namespaceId, WeChatConstant.WECHAT_APPSECRET, "");
		
		String body = this.restCall(WeChatConstant.GET_ACCESSTOKEN, null, params);
		
		if(body == "") {
            return null;
        }
        
        //manual cache it to redis
        redisTemplate.opsForValue().set(ACCESS_TOKEN, body, 7000, TimeUnit.SECONDS);
        
		return body;
	}
	
	//accesstoken和jsticket一起缓存
	//https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi
	private String cacheJsapiToken(RedisTemplate redisTemplate) {
		String atBody = cacheAccessToken(redisTemplate);
		AccessTokenResponse resp = (AccessTokenResponse)StringHelper.fromJsonString(atBody, AccessTokenResponse.class);
    	String params = "access_token=" + resp.getAccess_token()
    					+ "&type=" + WeChatConstant.JSAPI_TYPE;
		
		String body = this.restCall(WeChatConstant.GET_JSAPI_TICKET, null, params);
		if(body == "") {
            return null;
        }
        
        //manual cache it to redis
        redisTemplate.opsForValue().set(JSAPI_TICKENT, body, 7000, TimeUnit.SECONDS);
		return body;
	}
	
	private Map<String, String> makeJsApiTicket() {
		
		Accessor acc = this.bigCollectionProvider.getMapAccessor(JSAPI_TICKENT, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        Object o = redisTemplate.opsForValue().get(JSAPI_TICKENT);
        
        String body = "";
        if(o != null) {
            body = (String)o;
        } else {
        	
            body = cacheJsapiToken(redisTemplate);
            if(body == null) {
                return null;
            }
        
        }

        JsapiTicketResponse resp = (JsapiTicketResponse)StringHelper.fromJsonString(body, JsapiTicketResponse.class);
        Map<String, String> keys = new HashMap<String, String>();
            
        keys.put(JSAPI_TICKENT, resp.getTicket());
        
        return keys;
	}
	
	private ListenableFuture<ResponseEntity<String>> restCall(String cmd, Object params, String getParams, ListenableFutureCallback<ResponseEntity<String>> responseCallback) throws UnsupportedEncodingException {
        
        AsyncRestTemplate template = new AsyncRestTemplate();
        String url = getRestUri(cmd) + getParams;
        
        ListenableFuture<ResponseEntity<String>> future = template.exchange(url, HttpMethod.GET, null, String.class);
        
        if(responseCallback != null) {
            future.addCallback(responseCallback);    
        }
        
        return future;
    }
    
    
    private String restCall(String cmd, Object params, String url) {
        try {
            ListenableFuture<ResponseEntity<String>> future = this.restCall(cmd, params, url, null);
            return future.get().getBody();
        } catch (UnsupportedEncodingException | InterruptedException | ExecutionException e) {
            LOGGER.error("request error", e);
            return "";
        }
        
    }

    public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        System.out.println(string1);

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

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

}
