package com.everhomes.openapi;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.rest.user.SynThridUserCommand;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserService;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.util.WebTokenGenerator;

@RestDoc(value="ThirdUser open Controller", site="core")
@RestController
@RequestMapping("/openapi/user")
public class ThirdUserOpenController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(ThirdUserOpenController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private AppProvider appProvider;

	/**
	 * <b>URL: /openapi/user/initCoupon</b>
     * <p>初始化用户信息</p>
	 */
	@RequestMapping("initCoupon")
	@RestReturn(String.class)
	public RestResponse initCoupon(@Valid SynThridUserCommand cmd,HttpServletRequest request, HttpServletResponse response) {
		try {
	       this.checkIsNull(cmd);
          this.checkInnerOauth(cmd);

		    UserLogin login = this.userService.synThridUser(cmd);

	        LoginToken token = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), login.getImpersonationId());
	        String tokenString = WebTokenGenerator.getInstance().toWebToken(token);

	        LOGGER.debug(String.format("Return login info. token: %s, login info: ", tokenString, StringHelper.toJsonString(login)));
	        setCookieInResponse("token", tokenString, request, response);
    
		} catch (Exception ex) {
		    LOGGER.error("initCoupon error", ex);
		}
		
		RestResponse result =  new RestResponse();
		result.setErrorCode(ErrorCodes.SUCCESS);
		result.setErrorDescription("OK");
		return result;
	}

	private void checkIsNull(SynThridUserCommand cmd) {
		String key = cmd.getKey();
		Integer namespaceId = cmd.getNamespaceId();
		String namespaceUserToken = cmd.getNamespaceUserToken();
		Integer randomNum = cmd.getRandomNum();
		String sign = cmd.getSign();
		Long timestamp = cmd.getTimestamp();
		if(key == null){
			LOGGER.error("key is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"key is null.");
		}
		if(namespaceId == null){
			LOGGER.error("namespaceId is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"namespaceId is null.");
		}
		if(namespaceUserToken==null){
			LOGGER.error("namespaceUserToken is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"namespaceUserToken is null.");
		}
		if(randomNum==null){
			LOGGER.error("randomNum is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"randomNum is null.");
		}
		if(sign==null){
			LOGGER.error("sign is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"sign is null.");
		}
		if(timestamp==null){
			LOGGER.error("timestamp is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"timestamp is null.");
		}
			
	}

	private void checkInnerOauth(SynThridUserCommand cmd) {
		String namespaceUserToken=cmd.getNamespaceUserToken();
		Integer namespaceId=cmd.getNamespaceId();
		Integer randomNum = cmd.getRandomNum();
		Long timestamp = cmd.getTimestamp();
		String key = cmd.getKey();
		App app = appProvider.findAppByKey(key);
		if(app==null){
			LOGGER.error("app not found.key="+key);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"app not found.");
		}
		Map<String,String> map = new HashMap<String, String>();
		map.put("namespaceUserToken", cmd.getNamespaceUserToken());
		map.put("namespaceId", cmd.getNamespaceId()+"");
		map.put("randomNum", cmd.getRandomNum()+"");
		map.put("timestamp", cmd.getTimestamp()+"");
		map.put("key", cmd.getKey());
		String sign = URLDecoder.decode(cmd.getSign());
		String nSign = SignatureHelper.computeSignature(map, app.getSecretKey());
		if(!sign.equals(nSign)){
			LOGGER.error("no oauth to access.nSign="+nSign+",sign="+sign+",key="+key+",timestamp="+timestamp+",randomNum="+randomNum+",namespaceId="+namespaceId+",namespaceUserToken="+namespaceUserToken);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"no oauth to access.");
		}
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

	@RequestMapping("testGetThirdSignature")
	@RestReturn(String.class)
	public RestResponse testGetThirdSignature(SynThridUserCommand cmd){
		String appKey = "sdr231e5-b1gr24-xfgert-5n32ber-fgrt434gf0sd";
		App app = appProvider.findAppByKey(appKey);
		Map<String,String> map = new HashMap<String, String>();
		map.put("appKey", appKey);
		map.put("namespaceUserToken", cmd.getNamespaceUserToken());
		map.put("namespaceId", cmd.getNamespaceId()+"");
		map.put("randomNum", cmd.getRandomNum()+"");
		map.put("timestamp", cmd.getTimestamp()+"");
		map.put("sign", URLDecoder.decode(cmd.getSign()));
		map.put("key", cmd.getKey());
		map.put("userName", cmd.getUserName());
		map.put("deviceIdentifier", cmd.getDeviceIdentifier());
		String signature = SignatureHelper.computeSignature(map, app.getSecretKey());
		map.put("signature", signature);

		StringBuffer buf = new StringBuffer();
		for(Entry<String, String> r:map.entrySet()){
			if(r.getKey().equals("userName")||r.getKey().equals("deviceIdentifier")||r.getKey().equals("signature")){
				buf.append(r.getKey()+"="+URLEncoder.encode(r.getValue())+"&");
			}
			else
				buf.append(r.getKey()+"="+r.getValue()+"&");
		}

		RestResponse response =  new RestResponse(buf.toString());
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

}
