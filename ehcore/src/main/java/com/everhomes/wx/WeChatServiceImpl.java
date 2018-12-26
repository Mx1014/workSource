package com.everhomes.wx;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.everhomes.rest.contentserver.CsFileLocationDTO;
import com.everhomes.rest.wx.GetContentServerUriCommand;
import com.everhomes.user.User;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.rest.contentserver.UploadCsFileResponse;
import com.everhomes.rest.wx.GetContentServerUrlCommand;
import com.everhomes.rest.wx.GetSignatureCommand;
import com.everhomes.rest.wx.GetSignatureResponse;
import com.everhomes.user.UserContext;
import com.everhomes.util.RuntimeErrorException;
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

    @Autowired
    private ConfigurationProvider configurationProvider;
    
    static final String ACCESS_TOKEN = "wechat.accesstoken";
    static final String JSAPI_TICKENT = "jsapi.ticket";
    
    final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    
    Gson gson = new Gson();
    
	@Override
	public String getAccessToken() {
		Map<String, String> map = makeAccessToken();
		String accessToken = map.get(UserContext.getCurrentNamespaceId() + ACCESS_TOKEN);
		return accessToken;
	}
	
	@Override
	public String getJsapiTicket() {
		Map<String, String> map = makeJsApiTicket();
		String jsapiTicket = map.get(UserContext.getCurrentNamespaceId() + JSAPI_TICKENT);
		return jsapiTicket;
	}
	
	@Override
	public GetSignatureResponse getSignature(GetSignatureCommand cmd) {
		
		String ticket = getJsapiTicket();
		LOGGER.debug("ticket from getJsapiTicket={}", ticket);
		Map<String, String> ret = sign(ticket, cmd.getUrl());
		GetSignatureResponse resp = new GetSignatureResponse();
		resp.setNonceStr(ret.get("nonceStr"));
		resp.setSignature(ret.get("signature"));
		resp.setTimestamp(ret.get("timestamp"));
		// 增加appId，add by yanjun 20170624
		Integer namespaceId = UserContext.getCurrentNamespaceId();
        String appId = this.getAppIdByNamespaceId(namespaceId);
        resp.setAppId(appId);

        //ticket是服务器和微信之间的票据，安全原因不能传给客户端 edit by yanjun 20180711
		//resp.setTicket(ticket);
		return resp;
	}

	@Override
	public String getContentServerUrl(GetContentServerUrlCommand cmd) {
        CsFileLocationDTO csFileLocationDTO = commonGetContentServerResponse(cmd.getMediaId());
        String result = null;
        if (csFileLocationDTO != null) {
            result = csFileLocationDTO.getUrl();
        }
        return result;
	}

    @Override
    public CsFileLocationDTO getContentServerUri(GetContentServerUriCommand cmd) {
        return commonGetContentServerResponse(cmd.getMediaId());
    }

    private CsFileLocationDTO commonGetContentServerResponse(String mediaId) {
        String accessToken = getAccessToken();
        String url = getRestUri(WeChatConstant.GET_MEDIA) + "access_token=" + accessToken + "&media_id=" + mediaId;

        CloseableHttpClient httpclient = null;

        CloseableHttpResponse response = null;
        CsFileLocationDTO result = null;
        try {
            httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            response = httpclient.execute(httpGet);

            int status = response.getStatusLine().getStatusCode();
            if(status != 200){
                LOGGER.error("Failed to get the http result, url={}, status={}", url, response.getStatusLine());
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "Failed to get the http result");
            } else {
                HttpEntity entity =  response.getEntity();
                InputStream is = entity.getContent();

                String fileName = getFileName(response.getLastHeader("Content-Type").getValue());
                String token = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
                UploadCsFileResponse fileResp = contentServerService.uploadFileToContentServer(is, fileName, token);
                if(fileResp.getErrorCode() == 0) {
                    result = fileResp.getResponse();

                    if(LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Get http result, url={}, fileUrl={}", url, result);
                    }

                }

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

            if(httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
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
        Object o = redisTemplate.opsForValue().get(UserContext.getCurrentNamespaceId() + ACCESS_TOKEN);
        
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
            
        keys.put(UserContext.getCurrentNamespaceId() + ACCESS_TOKEN, resp.getAccess_token());
        
        return keys;
    }
	
	//https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
	private String cacheAccessToken(RedisTemplate redisTemplate) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		LOGGER.info("cacheAccessToken :" + namespaceId);
    	String params = "grant_type=" + WeChatConstant.ACCESSTOKEN_GRANTTYPE
    					+ "&appid=" + configProvider.getValue(namespaceId, WeChatConstant.WX_OFFICAL_ACCOUNT_APPID, "")
    					+ "&secret=" + configProvider.getValue(namespaceId, WeChatConstant.WX_OFFICAL_ACCOUNT_SECRET, "");
		
		String body = this.restCall(WeChatConstant.GET_ACCESSTOKEN, null, params);
		
		if(body == "") {
            return null;
        }
        
        //manual cache it to redis
        redisTemplate.opsForValue().set(namespaceId + ACCESS_TOKEN, body, 7000, TimeUnit.SECONDS);
        
		return body;
	}
	
	//accesstoken和jsticket一起缓存
	//https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi
	private String cacheJsapiToken(RedisTemplate redisTemplate) {
		String atBody = cacheAccessToken(redisTemplate);
		AccessTokenResponse resp = (AccessTokenResponse)StringHelper.fromJsonString(atBody, AccessTokenResponse.class);
    	String params = "access_token=" + resp.getAccess_token()
    					+ "&type=" + WeChatConstant.JSAPI_TYPE;

        LOGGER.debug("cacheJsapiToken restCall getticket param body={}", params);
		
		String body = this.restCall(WeChatConstant.GET_JSAPI_TICKET, null, params);

        LOGGER.debug("cacheJsapiToken restCall getticket return body={}", body);

		if(body == "") {
            return null;
        }
        
        //manual cache it to redis
        redisTemplate.opsForValue().set(UserContext.getCurrentNamespaceId() + JSAPI_TICKENT, body, 7000, TimeUnit.SECONDS);
		return body;
	}
	
	private Map<String, String> makeJsApiTicket() {
		
		Accessor acc = this.bigCollectionProvider.getMapAccessor(JSAPI_TICKENT, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        Object o = redisTemplate.opsForValue().get(UserContext.getCurrentNamespaceId() + JSAPI_TICKENT);
        
        String body = "";
        if(o != null) {
            body = (String)o;
        } else {
        	
            body = cacheJsapiToken(redisTemplate);
            if(body == null) {
                return null;
            }
        
        }
        LOGGER.info("body = {}",body);
        JsapiTicketResponse resp = (JsapiTicketResponse)StringHelper.fromJsonString(body, JsapiTicketResponse.class);
        Map<String, String> keys = new HashMap<String, String>();
            
        keys.put(UserContext.getCurrentNamespaceId() + JSAPI_TICKENT, resp.getTicket());

        return keys;
	}
	
	private ListenableFuture<ResponseEntity<String>> restCall(String cmd, Object params, String getParams, ListenableFutureCallback<ResponseEntity<String>> responseCallback) throws UnsupportedEncodingException {

        AsyncRestTemplate template = new AsyncRestTemplate();
        String url = getRestUri(cmd) + getParams;

//        //TODO for test
//        url = "http://members.3322.org/dyndns/getip";
//        //使用测试环境没有固定ip代理访问，从有外网ip的服务器访问微信，从而实现固定id
//        String wechatProxyHost = configurationProvider.getValue("wechat.proxy.host", null);
//        int wechatProxyPort = configurationProvider.getIntValue("wechat.proxy.port", 0);
//        if(wechatProxyHost != null && wechatProxyPort != 0){
//            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//            InetSocketAddress address = new InetSocketAddress(wechatProxyHost,wechatProxyPort);
//            Proxy proxy = new Proxy(Proxy.Type.HTTP,address);
//            factory.setProxy(proxy);
//            template.setAsyncRequestFactory(factory);
//        }

        ListenableFuture<ResponseEntity<String>> future = template.exchange(url, HttpMethod.GET, null, String.class);
        
        if(responseCallback != null) {
            future.addCallback(responseCallback);    
        }
        
        return future;
    }
    
    
    private String restCall(String cmd, Object params, String url) {
//            ListenableFuture<ResponseEntity<String>> future = this.restCall(cmd, params, url, null);
//            return future.get().getBody();
//
        String httpUrl = getRestUri(cmd) + url;
        String s = httpGet(httpUrl, "");


        return s;
    }


    @Override
    public String httpGet(String url, String safeUrl) {
        CloseableHttpClient httpclient = null;

        CloseableHttpResponse response = null;
        String result = null;
        try {
            httpclient = HttpClients.createDefault();

            HttpGet httpGet = new HttpGet(url);

            //与微信交互需要使用ip白名单。测试环境没有固定ip需要代理访问。方法是从有外网ip的服务器访问微信，从而实现固定id   add by yanjun 20180711
            String wechatProxyHost = configurationProvider.getValue("wechat.proxy.host", null);
            int wechatProxyPort = configurationProvider.getIntValue("wechat.proxy.port", 0);
            if(wechatProxyHost != null && wechatProxyPort != 0){
                HttpHost proxy = new HttpHost(wechatProxyHost, wechatProxyPort);
                RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).build();
                httpGet.setConfig(requestConfig);
            }

            response = httpclient.execute(httpGet);

            int status = response.getStatusLine().getStatusCode();
            if(status != 200){
                LOGGER.error("Failed to get the http result, url={}, status={}", safeUrl, response.getStatusLine());
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "Failed to get the http result");
            } else {
                HttpEntity resEntity = response.getEntity();
                String charset = getContentCharSet(resEntity);
                result = EntityUtils.toString(resEntity, charset);
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Get http result, charset={}, url={}, result={}", charset, safeUrl, result);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to get the http result, url={}", safeUrl, e);
        } finally {
            if(response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }


    public static String getContentCharSet(final HttpEntity entity) throws ParseException {
        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        String charset = null;
        if (entity.getContentType() != null) {
            HeaderElement values[] = entity.getContentType().getElements();
            if (values.length > 0) {
                NameValuePair param = values[0].getParameterByName("charset" );
                if (param != null) {
                    charset = param.getValue();
                }
            }
        }

        if(charset == null || charset.length() == 0){
            charset = "UTF-8";
        }

        return charset;
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

    @Override
    public String getAppIdByNamespaceId(Integer namespaceId) {
        String appId = configProvider.getValue(namespaceId, "wx.offical.account.appid", "");

        //增加默认公众号   add by yanjun 20170620
        if(org.springframework.util.StringUtils.isEmpty(appId)){
            appId = configProvider.getValue("wx.offical.account.default.appid", "");
        }
        return appId;
    }
}
