package com.everhomes.openapi;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
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
import com.everhomes.rest.user.InitBizInfoCommand;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.user.UserActivityService;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserService;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.util.WebTokenGenerator;

@RestDoc(value="Synch Info Controller", site="synchInfo")
@RestController
@RequestMapping("/openapi")
public class SynchInfoOpenController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(SynchInfoOpenController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private AppProvider appProvider;
	@Autowired
	private UserActivityService userActivityService;


	/**
	 * <b>URL: /openapi/initBizInfo</b>
	 */
	@RequestMapping("initBizInfo")
	@RestReturn(String.class)
	public RestResponse initBizInfo(@Valid InitBizInfoCommand cmd,HttpServletRequest request, HttpServletResponse response) {
		String url = null;
		try {
			this.validateParamIsEmpty(cmd);
			this.validateSign(cmd);
			
			//sync user info
			UserLogin login = this.userService.reSynThridUser(cmd);
			LoginToken token = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), login.getImpersonationId());
			String tokenString = WebTokenGenerator.getInstance().toWebToken(token);
			LOGGER.debug(String.format("Return login info. token: %s, login info: ", tokenString, StringHelper.toJsonString(login)));
			setCookieInResponse("token", tokenString, request, response);
			
			url = userActivityService.getBizUrl();
			
		} catch (Exception ex) {
			LOGGER.error("initBizInfo error", ex);
		}

		RestResponse result =  new RestResponse(url);
		result.setErrorCode(ErrorCodes.SUCCESS);
		result.setErrorDescription("OK");
		return result;
	}

	private void validateParamIsEmpty(InitBizInfoCommand cmd) {
		if(StringUtils.isEmpty(cmd.getLabel())){
			LOGGER.error("label is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"label is null.");
		}
		if(cmd.getNamespaceId() == null) {
			LOGGER.error("namespaceId is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"namespaceId is null.");
		}
		if(cmd.getSubNonce() == null){
			LOGGER.error("subNonce is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"subNonce is null.");
		}
		if(cmd.getSubTimestamp() == null){
			LOGGER.error("subTimestamp is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"subTimestamp is null.");
		}
		if(StringUtils.isEmpty(cmd.getSubKey())){
			LOGGER.error("subKey is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"subKey is null.");
		}
		if(StringUtils.isEmpty(cmd.getSubSign())) {
			LOGGER.error("subSign is null.");
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"subSign is null.");
		}
	}

	private void validateSign(InitBizInfoCommand cmd) {
		App app = appProvider.findAppByKey(cmd.getSubKey());
		if(app == null){
			LOGGER.error("app not found.key=" + cmd.getSubKey());
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
					"app not found.");
		}
		Map<String,String> params = new HashMap<String, String>();
		params.put("label", cmd.getLabel());
		params.put("namespaceId", cmd.getNamespaceId()+"");
		params.put("subNonce", cmd.getSubNonce()+"");
		params.put("subTimestamp", cmd.getSubTimestamp()+"");
		params.put("subKey", cmd.getSubKey());
		if(StringUtils.isNotBlank(cmd.getDetail())) {
			params.put("detail", cmd.getDetail());
		}
		if(StringUtils.isNotBlank(cmd.getMark())) {
			params.put("mark", cmd.getMark());
		}
		if(StringUtils.isNotBlank(cmd.getDescription())) {
			params.put("description", cmd.getDescription());
		}
		String sign = cmd.getSubSign();
		try {
			sign = URLDecoder.decode(cmd.getSubSign(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			sign = URLDecoder.decode(sign);
		}
		String nSign = SignatureHelper.computeSignature(params, app.getSecretKey());
		if(!sign.equals(nSign)){
			LOGGER.error("no oauth to access.nSign="+nSign+",sign="+sign+",key="+cmd.getSubKey());
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

}
