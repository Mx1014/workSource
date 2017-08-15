package com.everhomes.express;

import java.net.URLEncoder;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.discover.RestDoc;
import com.everhomes.namespace.Namespace;
import com.everhomes.openapi.AppNamespaceMapping;
import com.everhomes.openapi.AppNamespaceMappingProvider;
import com.everhomes.rest.RestResponse;
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
public class ExpressThirdCallController {// extends ControllerBase
	private static final Logger LOGGER = LoggerFactory.getLogger(ExpressThirdCallController.class);
    
    private final static String NS = "ns";
    private final static String COMMUNITY = "community";
    private final static String APPKEY = "appkey";
    private final static String NICK = "nick";
    private final static String MOBILE = "mobile";
    private final static String UID = "uid";
    private final static String TIMESTAMP = "timestamp";
    private final static String CHECKSUM = "checksum";
    private final static String AVATAR = "avatar";
    
    //重定向url
    private final static String ERROR_REDIRECT_URL = "/deliver/dist/assets/error.html?error=";
    private final static String SUCCESS_REDIRECT_URL = "/deliver/dist/index.html#/home_page";
    
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ExpressCompanyProvider expressCompanyProvider;
    
    @Autowired
    private ExpressOrderProvider expressOrderProvider;
    
	@Autowired
	private AppNamespaceMappingProvider appNamespaceMappingProvider;
	
	@Autowired
	private CommunityProvider communityProvider;

	@Autowired
	private AppProvider appProvider;
    
    
	/**
	 * <b>URL: /expressauth/authReq</b>
	 * <p>请求国贸授权。</p>
	 */
	@RequestMapping("/expressauth/authReq")
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
        		 response.sendRedirect(ERROR_REDIRECT_URL+URLEncoder.encode(e.getMessage(), "UTF-8"));
        		 return ;
        	}
        	//验证通过了，那么如果没有注册，则注册
        	 processUserInfo(namespaceId, params, request, response);
        }
        
        // 登录成功则跳转到原来访问的链接
        LOGGER.info("Process express auth request, loginToken={}", loginToken);
        long endTime = System.currentTimeMillis();
        //重定向到快递的地址。
        response.sendRedirect(SUCCESS_REDIRECT_URL);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.info("Process weixin auth request(req calculate), elspse={}, endTime={}", (endTime - startTime), endTime);
        }
        return ;
	}
	
	/**
	 * <b>URL: /express/callback</b>
	 * <p>请求国贸授权。</p>
	 */
	@RequestMapping("/express/callback")
	@ResponseBody
	public Map<String,Object> expressCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> resp = new HashMap<String, String>();
		resp.put("success","T");
		try{
			doCallback(request, response);
		}catch (RuntimeErrorException e) {
			resp.put("success","F");
			resp.put("errorMsg",e.getMessage());
			resp.put("errorCode",e.getErrorScope()+" "+e.getErrorCode());
		}
		Map<String,Object> objectMap = new HashMap<String, Object>();
		objectMap.put("response", resp);
		return objectMap;
	}
	
	private void doCallback(HttpServletRequest request, HttpServletResponse response) {
		long startTime = System.currentTimeMillis();
		if(LOGGER.isDebugEnabled()) {
			LOGGER.info("Process express callback request(req calculate), startTime={}", startTime);
		}
		Map<String, String> params = getRequestParams(request);
//		String authorization = params.get("authorization");
		String appKey = params.get("app_key");
		String txLogisticID = params.get("txLogisticID");
		if(appKey == null || appKey.length() == 0 || txLogisticID == null || txLogisticID.length()==0){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"app_key = "+appKey+", txLogisticID = "+txLogisticID);
		}
		ExpressCompany company = expressCompanyProvider.findExpressCompanyByAppKey(appKey);
		if(company == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "unknown app_key = "+appKey);
		}
		ExpressOrder order = expressOrderProvider.findExpressOrderByOrderNo(txLogisticID);
		if(order == null){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "unknown txLogisticID = "+txLogisticID);
		}
		ExpressHandler handler = getExpressHandler(company.getId());
		handler.orderStatusCallback(order, company, params);
	}

	private ExpressHandler getExpressHandler(Long expressCompanyId) {
		ExpressCompany expressCompany = findTopExpressCompany(expressCompanyId);
		return PlatformContext.getComponent(ExpressHandler.EXPRESS_HANDLER_PREFIX+expressCompany.getId());
	}
	
	private ExpressCompany findTopExpressCompany(Long expressCompanyId) {
		ExpressCompany expressCompany = expressCompanyProvider.findExpressCompanyById(expressCompanyId);
		if (expressCompany.getParentId().longValue() != 0L) {
			return findTopExpressCompany(expressCompany.getParentId());
		}
		return expressCompany;
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
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "非法的appkey");
		}
		//签名校验
		String mobile = params.get(MOBILE);
		String timestamp = params.get(TIMESTAMP);
		String checksum = params.get(CHECKSUM);
		checkSign(mobile, timestamp, checksum, app.getSecretKey());
		
		AppNamespaceMapping mapping = appNamespaceMappingProvider.findAppNamespaceMappingByAppKey(appkey);
		String stringCommunityId = params.get(COMMUNITY);
		if(stringCommunityId != null){
			Long communityId = null;
			try{
				communityId = Long.valueOf(stringCommunityId);
			}catch(Exception e){
				LOGGER.error("invaild community = {}", stringCommunityId);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "非法的community");
			}
			Community community = communityProvider.findCommunityById(communityId);
			if(community == null){
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "非法的community");
			}
		}else{
			params.put(COMMUNITY, mapping.getCommunityId()+"");
		}
		
		if(mapping == null || mapping.getNamespaceId() == null || namespaceId.intValue() != mapping.getNamespaceId().intValue()){
			LOGGER.error("appkey not mapping to namespace, mapping = {}, appKey = {}", mapping, appkey);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "appkey不匹配ns");
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
        if(guoMaoUser.getId()!=null){
        	userService.updateUserCurrentCommunityToProfile(guoMaoUser.getId(), Long.valueOf(params.get(COMMUNITY)), guoMaoUser.getNamespaceId());
        }
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
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "时间戳不匹配");
		}
		if(checksum == null){
			// TODO 重定向到登录页面
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "校验和为空" );
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
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "参数校验失败");
		}
	}
	
	public static void main(String[] args) {
		 Map<String, String> params = new HashMap<String, String>();
		 params.put("ns",999901+"");
		 params.put("appkey","de875e40-1c5f-4a0c-94a6-0b37421b8554");
		 params.put("nick","邓爽2");
		 params.put("mobile","12345678902");
		 params.put("uid","1234567xxxx");
		 params.put("timestamp",System.currentTimeMillis()+"");
		 params.put("avatar","core.zuolin.com");
		 params.put("community","240111044331070561");
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
		System.out.println("http://printtest.zuolin.com/evh/expressauth/authReq?"+buffer.toString().substring(1));
		System.out.println("http://10.1.10.90/evh/expressauth/authReq?"+buffer.toString().substring(1));
	}
}
