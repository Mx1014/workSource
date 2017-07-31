package com.everhomes.express;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.discover.RestDoc;
import com.everhomes.namespace.Namespace;
import com.everhomes.openapi.AppNamespaceMapping;
import com.everhomes.openapi.AppNamespaceMappingProvider;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.rest.user.NamespaceUserType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserService;
import com.everhomes.util.RuntimeErrorException;

/**
 *  国贸登录调用
 *
 *  @author:dengs 2017年7月28日
 */
@RestDoc(value = "Express Auth Controller", site = "core")
@RestController
@RequestMapping("/expressauth")
public class ExpressAuthController {// extends ControllerBase
	private static final Logger LOGGER = LoggerFactory.getLogger(ExpressAuthController.class);
    
    private final static String NS = "ns";
    private final static String APPKEY = "appkey";
    private final static String NICK = "nick";
    private final static String MOBILE = "mobile";
    private final static String UID = "uid";
    private final static String TIMESTAMP = "timestamp";
    private final static String CHECKSUM = "checksum";
    private final static String AVATAR = "avatar";
    
    //重定向url
    private final static String REDIRECT_URL = "";
    
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ConfigurationProvider configurationProvider;
    
	@Autowired
	private AppNamespaceMappingProvider appNamespaceMappingProvider;

	@Autowired
	private AppProvider appProvider;
    
    
	/**
	 * <b>URL: /expressauth/authReq</b>
	 * <p>请求国贸授权。</p>
	 */
	@RequestMapping("authReq")
	public void authReq(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    long startTime = System.currentTimeMillis();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process express auth request(req calculate), startTime={}", startTime);
        }
        Map<String, String> params = getRequestParams(request);
        // 域空间，检查登录
        Integer namespaceId = parseNamespace(params.get(NS));
        LoginToken loginToken = userService.getLoginToken(request);
        //根据cookie中的token，如果token验证失败，或者namespace验证失败
        //重新登录
        if(!userService.isValid(loginToken) || !checkUserNamespaceId(namespaceId)) {
        	//验证参数
        	try{
        		vaildParams(params, namespaceId);
        	}catch(Exception e){
        		 response.sendRedirect(REDIRECT_URL);
        		 return ;
        	}
        	//验证通过了，那么如果没有注册，则注册
        	 processUserInfo(namespaceId, params, request, response);
        }
        
        // 登录成功则跳转到原来访问的链接
        LOGGER.info("Process express auth request, loginToken={}", loginToken);
        long endTime = System.currentTimeMillis();
        //重定向到快递的地址。
        response.sendRedirect(REDIRECT_URL);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process weixin auth request(req calculate), elspse={}, endTime={}", (endTime - startTime), endTime);
        }
        return ;
	}
	/**
	 * 参数校验
	 */
	private void vaildParams(Map<String, String> params,  Integer namespaceId) {
		String appkey = params.get(APPKEY);
		//appkey和namespace校验
		App app = appProvider.findAppByKey(appkey);
		if(app == null){
			LOGGER.error("app not found.key = {}", appkey);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invaild appkey");
		}
		//签名校验
		String mobile = params.get(MOBILE);
		String timestamp = params.get(TIMESTAMP);
		String checksum = params.get(CHECKSUM);
		checkSign(mobile, timestamp, checksum, app.getSecretKey());
		
		AppNamespaceMapping mapping = appNamespaceMappingProvider.findAppNamespaceMappingByAppKey(appkey);
		
		if(mapping == null || mapping.getNamespaceId() == null || namespaceId.intValue() != mapping.getNamespaceId().intValue()){
			LOGGER.error("appkey not mapping to namespace, mapping = {}, appKey = {}", mapping, appkey);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "appkey not match ns");
		}
	}

	private Map<String, String> getRequestParams(HttpServletRequest request) {
	    Map<String, String> params = new HashMap<String, String>();
	    Enumeration<String> em = request.getParameterNames();
	    while (em.hasMoreElements()) {
	       String name = em.nextElement();
	       String value = request.getParameter(name);
	       params.put(name, value);
	   }
	    
	    return params;
	}
	
	
    private Integer parseNamespace(Object nsObj) {
        Integer namespaceId = Namespace.DEFAULT_NAMESPACE;
        if(nsObj != null) {
            try {
                namespaceId = Integer.parseInt(nsObj.toString().trim());
            } catch (Exception e) {
                LOGGER.error("Failed to parse the namesapce, ns={}", nsObj, e);
            }
        }
        
        return namespaceId;
    }
    
    private void processUserInfo(Integer namespaceId, Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process express auth request(userinfo calculate), startTime={}", startTime);
        }
        
        User guoMaoUser = new User();
        guoMaoUser.setNamespaceId(namespaceId);
        guoMaoUser.setNamespaceUserType(NamespaceUserType.GUOMAO.getCode());
        guoMaoUser.setNamespaceUserToken(params.get(UID));
        guoMaoUser.setNickName(params.get(NICK));
        guoMaoUser.setAvatar(params.get(AVATAR));
        guoMaoUser.setGender((byte)0);//未知性别
        
        userService.signupByThirdparkUser(guoMaoUser, request);
        userService.logonBythirdPartUser(guoMaoUser.getNamespaceId(), guoMaoUser.getNamespaceUserType(), guoMaoUser.getNamespaceUserToken(), request, response);
        
        long endTime = System.currentTimeMillis();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process express auth request(userinfo calculate), elspse={}, endTime={}", (endTime - startTime), endTime);
        }
    }
    
    //检出当前登录用户域空间和需要登录的域空间是否相同，不同则登出    add by yanjun 20170620
    private boolean checkUserNamespaceId(Integer namespaceId){

	    Integer currentNamespaceId = UserContext.getCurrentNamespaceId();
        LOGGER.info("checkUserNamespaceId, namespaceId={}, currentNamespaceId={}", namespaceId, currentNamespaceId);
        if(currentNamespaceId == null){
            return false;
        }

        if(namespaceId == null || namespaceId.intValue() != currentNamespaceId.intValue()){
            if(UserContext.current() != null){
                UserLogin login = UserContext.current().getLogin();
                if(login != null){
                    userService.logoff(login);
                    LOGGER.info("checkUserNamespaceId, userService.logoff");
                }
            }
            return false;
        }

        return true;


    }
    
	private void checkSign(String mobile, String timestamp, String checksum, String secretkey) {
		Long currentTimestamp = System.currentTimeMillis();
		if(Math.abs(currentTimestamp-Long.parseLong(timestamp))>1000*60*2L){
			// TODO 重定向到登录页面
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "timestamp overtime, timestamp = " + checksum);
		}
		if(checksum == null){
			// TODO 重定向到登录页面
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "checksum = " + checksum);
		}
		String sum = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest((timestamp+mobile+secretkey).getBytes());
			sum = Hex.encodeHexString(bytes);
		} catch (Exception e) {
		}
		if(!checksum.equals(sum)){
			// TODO 重定向到登录页面
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "invaild params ");
		}
	}
	
	public static void main(String[] args) {
		 Map<String, String> params = new HashMap<String, String>();
		 params.put("ns",23456+"");
		 params.put("appkey","de875e40-1c5f-4a0c-94a6-0b37421b8554");
		 params.put("nick","邓爽");
		 params.put("mobile","12345678901");
		 params.put("uid","1234567xxxbbbb");
		 params.put("timestamp",System.currentTimeMillis()+"");
		 params.put("avatar","www.baidu.com");
		 MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 byte[] bytes = md.digest((params.get("timestamp")+params.get("mobile")+"k9+3iUUSlUah1Uggv5ZKbTEktcIuvs7834ZThJS/CmA4eVBR2msOBak9uvut1Io0gZ9tdFJ0LpJ9ELfes8XXZw==").getBytes());
		 params.put("checksum",Hex.encodeHexString(bytes));
		 StringBuffer buffer = new StringBuffer();
		 for (Iterator iterator = params.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			buffer.append("&").append(key).append("=").append(params.get(key));
		}
		System.out.println("http://10.1.110.46:8080/evh/expressauth/authReq?"+buffer.toString().substring(1));
	}
}
