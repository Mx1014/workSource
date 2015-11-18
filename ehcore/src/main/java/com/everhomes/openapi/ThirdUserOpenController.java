package com.everhomes.openapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.everhomes.user.LoginToken;
import com.everhomes.user.SynThridUserCommand;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserService;
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
     * <b>URL: /openapi/user/synCoupon</b>
     */
    @RequestMapping("synCoupon")
    @RestReturn(String.class)
    public RestResponse synCoupon(@Valid SynThridUserCommand cmd,HttpServletRequest request, HttpServletResponse response) {
    	UserLogin login = this.userService.synThridUser(cmd);
    	
		LoginToken token = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber());
		String tokenString = WebTokenGenerator.getInstance().toWebToken(token);

		LOGGER.debug(String.format("Return login info. token: %s, login info: ", tokenString, StringHelper.toJsonString(login)));
		setCookieInResponse("token", tokenString, request, response);
		
    	RestResponse result =  new RestResponse();
    	result.setErrorCode(ErrorCodes.SUCCESS);
    	result.setErrorDescription("OK");
        return result;
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
	@RestReturn(SynThridUserCommand.class)
	public RestResponse testGetThirdSignature(){
    	SynThridUserCommand result = new SynThridUserCommand();
    	String appKey = "d80e06ca-3766-11e5-b18f-b083fe4e159f";
    	App app = appProvider.findAppByKey(appKey);
    	Integer randomNum = 2;
    	String signature = null;
    	String siteUri = "test";
    	String siteUserToken = "testtoken";
    	Long timeStamp = 3l;
    	Map<String,String> map = new HashMap<String, String>();
    	map.put("appKey", appKey);
    	map.put("randomNum", randomNum+"");
    	map.put("siteUri", siteUri);
    	map.put("siteUserToken", siteUserToken);
    	map.put("timestamp", timeStamp+"");
    	signature = SignatureHelper.computeSignature(map, app.getSecretKey());
    	
    	//result.setAppKey(appKey);
    	result.setRandomNum(randomNum);
    	//result.setSignature(signature);
    	result.setSiteUri(siteUri);
    	result.setSiteUserToken(siteUserToken);
    	result.setTimestamp(timeStamp);
		
		RestResponse response =  new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

}
