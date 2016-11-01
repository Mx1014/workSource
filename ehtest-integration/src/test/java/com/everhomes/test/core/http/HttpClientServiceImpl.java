// @formatter:off
package com.everhomes.test.core.http;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.everhomes.test.core.util.GsonHelper;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.test.core.base.UserContext;
import com.everhomes.util.GsonJacksonDateAdapter;
import com.everhomes.util.GsonJacksonTimestampAdapter;
import com.everhomes.util.SimpleConvertHelper;
import com.everhomes.util.StringHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component
@Scope("prototype")
public class HttpClientServiceImpl implements HttpClientService {
    public final String HTTP_METHOD_POST = "POST";
    public final String HTTP_METHOD_GET = "GET";
    
    /** 服务器context路径，即API前缀 */
    @Value("${server.contextPath:}")
    private String serverContextPath;
    
    /**  */
    @Value("${server.address:}")
    private String serverAddress;

    private CloseableHttpClient httpClient;
    
    private HttpContext httpClientContext;
    
    private Gson gson;
    
    public HttpClientServiceImpl() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new GsonJacksonDateAdapter());
        builder.registerTypeAdapter(Timestamp.class, new GsonJacksonTimestampAdapter());
        gson = builder.create();
    }

    @PostConstruct
    private CloseableHttpClient openHttpClient() {
        if (isHttpClientOpen()) {
            return this.httpClient;
        }

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        this.httpClient = HttpClients.custom().setConnectionManager(cm).build();
        this.httpClientContext = HttpClientContext.create();

        if (this.httpClient == null) {
            throw new IllegalStateException("Unable to create HttpClient object");
        }
        
        return this.httpClient;
    }  
    
    public <T extends RestResponseBase> T restPost(String commandRelativeUri, Object cmd, Class<T> responseClz) {
        return restCall(HTTP_METHOD_POST, commandRelativeUri, cmd, responseClz, null);
    }
    
    /**
     * 发HTTP GET请求（用于非登录用户的场合）
     * @param commandRelativeUri API相对URI（不含server context path）
     * @param cmd 请求参数command
     * @param responseClz 响应结果对应的RestReponse子类，使得响应结果可以转化为java对象
     * @param context 含用户token、userAgent等信息的上下文
     * @return HTTP请求结果（RestReponse子类对应的对象）
     */
    public <T extends RestResponseBase> T restGet(String commandRelativeUri, Object cmd, Class<T> responseClz) {
        return restCall(HTTP_METHOD_GET, commandRelativeUri, cmd, responseClz, null);
    }
    
    /**
     * 发HTTP POST请求（含登录用户token、userAgent等信息的上下文）
     * @param commandRelativeUri API相对URI（不含server context path）
     * @param cmd 请求参数command
     * @param responseClz 响应结果对应的RestReponse子类，使得响应结果可以转化为java对象
     * @param context 含用户token、userAgent等信息的上下文
     * @return HTTP请求结果（RestReponse子类对应的对象）
     */
    public <T extends RestResponseBase> T restPost(String commandRelativeUri, Object cmd, Class<T> responseClz, UserContext context) {
        return restCall(HTTP_METHOD_POST, commandRelativeUri, cmd, responseClz, context);
    }
    
    /**
     * 发HTTP GET请求（含登录用户token、userAgent等信息的上下文）
     * @param commandRelativeUri API相对URI（不含server context path）
     * @param cmd 请求参数command
     * @param responseClz 响应结果对应的RestReponse子类，使得响应结果可以转化为java对象
     * @param context 含用户token、userAgent等信息的上下文
     * @return HTTP请求结果（RestReponse子类对应的对象）
     */
    public <T extends RestResponseBase> T restGet(String commandRelativeUri, Object cmd, Class<T> responseClz, UserContext context) {
        return restCall(HTTP_METHOD_GET, commandRelativeUri, cmd, responseClz, context);
    }
    
    public <T extends RestResponseBase> T restCall(String method, String commandRelativeUri, Object cmd, 
            Class<T> responseClz, UserContext context) {
        Map<String, String> params = new HashMap<String, String>();
        StringHelper.toStringMap(null, cmd, params);

        return this.restCall(method, commandRelativeUri, params, responseClz, context);
    }

    public <T extends RestResponseBase> T restCall(String method, String commandRelativeUri, Map<String, String> params, 
        Class<T> responseClz, UserContext context) {
        if(commandRelativeUri == null || commandRelativeUri.trim().length() == 0) {
            throw new IllegalArgumentException("The command relative uri may not be empty");
        }

        String loginToken = null;
        if(context != null) {
            loginToken = context.getLoginToken();
        }

        if (params == null)
            params = new HashMap<String, String>();

        CloseableHttpClient client = openHttpClient();
        String uri = composeFullUri(commandRelativeUri);
        if(context != null) {
            context.setCommandUri(uri);
        }
        
        try {
            CloseableHttpResponse res;
            if (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT")) {
                HttpPost post = new HttpPost(uri);
                
                if(context != null && context.getUserAgent() != null) {
                    post.setHeader(HttpHeaders.USER_AGENT, context.getUserAgent());
                }

                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                if (loginToken != null)
                    formparams.add(new BasicNameValuePair("token", loginToken));

                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
                post.setEntity(entity);

                res = client.execute(post, this.httpClientContext);
            } else {
                RequestBuilder builder = RequestBuilder.create(method.toString()).setUri(uri);
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.addParameter(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                if (loginToken != null)
                    builder.addParameter(new BasicNameValuePair("token", loginToken));

                HttpUriRequest uriRequest = builder.build();
                if(context != null && context.getUserAgent() != null) {
                    uriRequest.setHeader(HttpHeaders.USER_AGENT, context.getUserAgent());
                }

                res = client.execute(uriRequest, this.httpClientContext);
            }

            try {
                HttpEntity resEntity = res.getEntity();
                String responseBody = EntityUtils.toString(resEntity);
                return gson.fromJson(responseBody, responseClz);
            } finally {
                res.close();
            }
        } catch (ClientProtocolException e) {
            return SimpleConvertHelper.convert(new RestResponseBase("HTTP", HttpStatus.SC_BAD_REQUEST, e.getMessage()),
                responseClz);
        } catch (IOException e) {
            return SimpleConvertHelper.convert(new RestResponseBase("HTTP", HttpStatus.SC_BAD_GATEWAY, e.getMessage()),
                responseClz);
        }
    }
    
    public boolean isReponseSuccess(RestResponseBase response) {
        if(response != null ) {
            Integer errCode = response.getErrorCode();
            if(errCode != null && (errCode.equals(200) || errCode.equals(201))) {
                return true;
            }
        }
        
        return false;
    }
    
    public String getServerContextPath() {
        return serverContextPath;
    }

    public void setServerContextPath(String serverContextPath) {
        this.serverContextPath = serverContextPath;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    @PreDestroy
    private void closeHttpClient() {
        if (this.httpClient != null) {
            try {
                this.httpClient.close();

                this.httpClient = null;
                this.httpClientContext = null;
            } catch (IOException e) {
                // ignore the exception;
            }
        }
    }  
    
    private boolean isHttpClientOpen() {
        return this.httpClient != null;
    }

    private String composeFullUri(String commandRelativeUri) {
        StringBuffer sb = new StringBuffer(this.serverAddress);
        if (this.serverAddress.endsWith("/")) {
            sb.deleteCharAt(sb.length() - 1);
        }
        if(!this.serverContextPath.startsWith("/")) {
            sb.append("/");
        }
        sb.append(this.serverContextPath);
        sb.append("/");
        if (commandRelativeUri.startsWith("/"))
            sb.append(commandRelativeUri.substring(1));
        else
            sb.append(commandRelativeUri);
        return sb.toString();
    }
    
    private <T extends RestResponseBase>  T postFile(String commandRelativeUri, Object cmd, Class<T> responseClz, File... files){
    	try {
	    	if(commandRelativeUri == null || commandRelativeUri.trim().length() == 0) {
	            throw new IllegalArgumentException("The command relative uri may not be empty");
	        }
	    	
	        String uri = composeFullUri(commandRelativeUri);
	        
	        Map<String, String> params = new HashMap<String, String>();
	        StringHelper.toStringMap(null, cmd, params);
	        List<Object> list = new ArrayList<>();
	        list.add(Consts.UTF_8);
	        list.addAll(Arrays.asList(files));
			String response = HttpUtils.postFile(uri, params, 30, httpClientContext, list.toArray());
			
			return gson.fromJson(response, responseClz);
		} catch (IOException e) {
			return SimpleConvertHelper.convert(new RestResponseBase("HTTP", HttpStatus.SC_BAD_REQUEST, e.getMessage()),
	                responseClz);
		}
	}
    
    /**
     * 带文件上传的请求
     */
    @Override
    public <T extends RestResponseBase>  T postFile(String commandRelativeUri, Object cmd, File file, Class<T> responseClz){
    	return postFile(commandRelativeUri, cmd, responseClz, file);
    }

	@Override
	public <T extends RestResponseBase> T postFile(String commandRelativeUri,
			Object cmd, File file, Class<T> responseClz, UserContext context) {
		return postFile(commandRelativeUri, cmd, responseClz ,context, file);
	}
	
	private <T extends RestResponseBase>  T postFile(String commandRelativeUri, Object cmd, Class<T> responseClz, UserContext context, File... files){
    	try {
	    	if(commandRelativeUri == null || commandRelativeUri.trim().length() == 0) {
	            throw new IllegalArgumentException("The command relative uri may not be empty");
	        }
	    	
	        String uri = composeFullUri(commandRelativeUri);
	        
	        Map<String, String> params = new HashMap<String, String>();
	        params.put("token", context.getLoginToken());
	        StringHelper.toStringMap(null, cmd, params);
	        List<Object> list = new ArrayList<>();
	        list.add(Consts.UTF_8);
	        list.addAll(Arrays.asList(files));
			String response = HttpUtils.postFile1(uri, params, 30, httpClientContext, list.toArray());
			
			return gson.fromJson(response, responseClz);
		} catch (IOException e) {
			return SimpleConvertHelper.convert(new RestResponseBase("HTTP", HttpStatus.SC_BAD_REQUEST, e.getMessage()),
	                responseClz);
		}
	}
}
