package com.everhomes.oauth2;

import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.oauth2.AuthorizationCommand;
import com.everhomes.rest.oauth2.CommonRestResponse;
import com.everhomes.rest.oauth2.OAuth2AccessTokenResponse;
import com.everhomes.rest.oauth2.OAuth2ServiceErrorCode;
import com.everhomes.rest.user.DeviceIdentifierType;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserService;
import com.everhomes.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequireOAuth2Authentication(OAuth2AuthenticationType.NO_AUTHENTICATION)
@Controller
@RequestMapping("/oauth2")
public class AuthorizationEndpointController extends OAuth2ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationEndpointController.class);
	private static final String ZUOLIN_APP_KEY = "zuolin.appKey";

	@Autowired
	private AppProvider appProvider;

	@Autowired
	private LocaleStringService localeStringService;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@Autowired
	private UserService userService;

	@RequestMapping("authorize")
	public Object authorize(
			@RequestParam(value="response_type", required = true) String responseType,
			@RequestParam(value="client_id", required = true) String clientId,
			@RequestParam(value="redirect_uri", required = false) String redirectUri,
			@RequestParam(value="scope", required = false) String scope,
			@RequestParam(value="state", required = true) String state,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) {

		App app = this.appProvider.findAppByKey(clientId);
		if(app == null) {
			model.addAttribute("errorDescription", this.localeStringService.getLocalizedString(
					OAuth2ServiceErrorCode.SCOPE,
					String.valueOf(OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST),
					httpRequest.getLocale().toLanguageTag(),
					"Invalid request client_id or unregistered redirect URI"));

			return "oauth2-error-response";
		}

		if(redirectUri == null || redirectUri.isEmpty()) {
			redirectUri = this.oAuth2Service.getDefaultRedirectUri(app.getId());
		}

		if(!this.oAuth2Service.validateRedirectUri(app.getId(), redirectUri)) {
			model.addAttribute("errorDescription", this.localeStringService.getLocalizedString(
					OAuth2ServiceErrorCode.SCOPE,
					String.valueOf(OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST),
					httpRequest.getLocale().toLanguageTag(),
					"Invalid request client_id or unregistered redirect URI"));

			return "oauth2-error-response";
		}

		AuthorizationCommand cmd = new AuthorizationCommand();
		cmd.setresponse_type(responseType);
		cmd.setclient_id(clientId);
		cmd.setredirect_uri(redirectUri);
		cmd.setScope(scope);
		cmd.setState(state);
		//has logon
		User user = UserContext.current().getUser();
		if(user!=null&&user.getId()!=User.ANNONYMOUS_UID){
			URI uri = oAuth2Service.confirmAuthorization(user, cmd);
			if (uri != null) {
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setLocation(uri);
				return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
			} 
		}
		//no logon
		model.addAttribute("viewState", WebTokenGenerator.getInstance().toWebToken(cmd));
		return "oauth2-authorize";
	}

	@RequestMapping("confirm")
	public Object confirmAuthorization(
			@RequestParam(value="identifier", required = true) String identifier,
			@RequestParam(value="password", required = true) String password,
			@RequestParam(value="viewState", required = true) String viewState,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) throws URISyntaxException {

		AuthorizationCommand cmd = WebTokenGenerator.getInstance().fromWebToken(viewState, AuthorizationCommand.class);

		// double check in confirmation api to protect against tampering in confirmation callback
		App app = this.appProvider.findAppByKey(cmd.getclient_id());
		if(app == null) {
			model.addAttribute("errorDescription", this.localeStringService.getLocalizedString(
					OAuth2ServiceErrorCode.SCOPE,
					String.valueOf(OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST),
					httpRequest.getLocale().toLanguageTag(),
					"Invalid request client_id or unregistered redirect URI"));

			return "oauth2-error-response";
		}

		String redirectUri = cmd.getredirect_uri();
		if(redirectUri == null || redirectUri.isEmpty()) {
			redirectUri = this.oAuth2Service.getDefaultRedirectUri(app.getId());
		}

		if(!this.oAuth2Service.validateRedirectUri(app.getId(), redirectUri)) {
			model.addAttribute("errorDescription", this.localeStringService.getLocalizedString(
					OAuth2ServiceErrorCode.SCOPE,
					String.valueOf(OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST),
					httpRequest.getLocale().toLanguageTag(),
					"Invalid request client_id or unregistered redirect URI"));

			return "oauth2-error-response";
		}

		LOGGER.info("Confirm OAuth2 authorization: {}", cmd);

		try {
			URI uri = oAuth2Service.confirmAuthorization(identifier, password, cmd);
			if (uri != null) {
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setLocation(uri);
				return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
			} else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("confirmAuthorization return uri is null");
                }
                // make sure we always carry the view state back to front end for next round of submission
				model.addAttribute("viewState", viewState);
				return "oauth2-authorize";
			}
		} catch (RuntimeErrorException e) {
			LOGGER.error("Unexpected exception code = {}", e.getErrorCode());
			LOGGER.error("Unexpected exception", e);

			switch(e.getErrorCode()) {
			case OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST:
				model.addAttribute("errorDescription", this.localeStringService.getLocalizedString(
						OAuth2ServiceErrorCode.SCOPE,
						String.valueOf(OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST),
						httpRequest.getLocale().toLanguageTag(),
						"Invalid request client_id or unregistered redirect URI"));

				return "oauth2-error-response";

			default : {
				URI uri = new URI(String.format("%s#error=%s&state=%s", redirectUri,
						this.oAuth2Service.getOAuth2AuthorizeError(e.getErrorCode()), cmd.getState()));
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setLocation(uri);
				return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
			}
			}
		} catch(Exception e) {
			LOGGER.error("Unexpected exception", e);

			URI uri = new URI(String.format("%s#error=%s&state=%s", redirectUri,
					this.oAuth2Service.getOAuth2AuthorizeError(OAuth2ServiceErrorCode.ERROR_SERVER_ERROR), cmd.getState()));
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setLocation(uri);
			return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
		}
	}

	@RequireAuthentication(false)
	@RequestMapping("signLogon")
	public Object signLogon(
			@RequestParam(value="sourceUrl", required = true) String sourceUrl,
			@RequestParam(value="signature", required = false) String signature,
			@RequestParam(value="id", required = false) Long id,
			@RequestParam(value="name", required = false) String name,
			@RequestParam(value="appKey", required = false) String appKey,
			@RequestParam(value="timeStamp", required = false) Long timeStamp,
			@RequestParam(value="randomNum", required = false) Integer randomNum,
			@RequestParam(value="communityId", required = false) Long communityId,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
		User user = UserContext.current().getUser();
		if(user==null||user.getId()==User.ANNONYMOUS_UID){
			UserInfo userInfo = getBizUserInfo(signature,appKey,String.valueOf(id),name,String.valueOf(randomNum),String.valueOf(timeStamp));
			if(userInfo==null){
				LOGGER.error("userInfo don't get.id="+id);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "userInfo don't get");
			}
			String deviceIdentifier = DeviceIdentifierType.INNER_LOGIN.name();
			String pusherIdentify = null;
			UserLogin login = this.userService.innerLogin(userInfo.getNamespaceId(), userInfo.getId(), deviceIdentifier, pusherIdentify);
			LoginToken logintoken = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), null);
			String tokenString = WebTokenGenerator.getInstance().toWebToken(logintoken);
			setCookieInResponse("token", tokenString, httpRequest, httpResponse);
		}
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(new URI(sourceUrl));
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}

	@RequireAuthentication(false)
	@RequestMapping("oauth2Logon")
	public Object oauth2Logon(@RequestParam(value="sourceUrl", required = true) String sourceUrl,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
		User user = UserContext.current().getUser();
		if(user==null||user.getId()==User.ANNONYMOUS_UID){
			HttpSession session = httpRequest.getSession();
			String state = session.getId();
			if(sourceUrl!=null&&!sourceUrl.equals("")){
				session.setAttribute("sourceUrl", sourceUrl);
			}
			String homeUrl = configurationProvider.getValue("home.url", "https://core.zuolin.com");
			String appKey = configurationProvider.getValue(ZUOLIN_APP_KEY, "f9392ce2-341b-40c1-9c2c-99c702215535");
			String oauthUri = homeUrl+"/evh/oauth2/authorize";
			String redirectUri = homeUrl+"/evh/oauth2/redirectUri";
			String uri = String.format("%s?response_type=code&client_id=%s&redirect_uri=%s&scope=basic&state=%s#oauth2_redirect",
					oauthUri,
					appKey,
					URLEncoder.encode(redirectUri, "UTF-8"),
					state
					);
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setLocation(new URI(uri));
			return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
		}
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(new URI(sourceUrl));
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}

	@RequireAuthentication(false)
	@RequestMapping("redirectUri")
	public Object redirectUri(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
		String code = httpRequest.getParameter("code");
		String state = httpRequest.getParameter("state");
		String homeUrl = configurationProvider.getValue("home.url", "https://core.zuolin.com");
		String redirectUri = homeUrl+"/evh/oauth2/redirectUri";
		String tokenUri = homeUrl+"/evh/oauth2/token";
		String userInfoUri = homeUrl+"/evh/oauth2api/getUserInfo";
		String appKey = configurationProvider.getValue(ZUOLIN_APP_KEY, "f9392ce2-341b-40c1-9c2c-99c702215535");
		App app = appProvider.findAppByKey(appKey);
		if(app==null){
			LOGGER.error("app nou found.appKey="+appKey);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,"app nou found");
		}
		//get token
		Map<String, String> params = new HashMap<String, String>();
		params.put("grant_type", "authorization_code");
		params.put("code", code);
		params.put("redirect_uri", redirectUri);
		params.put("client_id", appKey);
		String credential = String.format("%s:%s", appKey, app.getSecretKey());
		Clients client = new Clients();
		String tokenResponseString = client.restCall("POST", tokenUri, params, "Authorization", String.format("Basic %s", Base64.encodeBase64String(credential.getBytes("UTF-8"))));
		OAuth2AccessTokenResponse tokenResponse = (OAuth2AccessTokenResponse) StringHelper.fromJsonString(tokenResponseString, OAuth2AccessTokenResponse.class);
		String token = tokenResponse.getAccess_token();
		//get user info
		String userInfoResponseString = client.restCall("GET", userInfoUri, null, "Authorization", String.format("Bearer %s", Base64.encodeBase64String(token.getBytes("UTF-8"))));
		Gson gson = new Gson();
		CommonRestResponse<UserInfo> userInfoRestResponse = gson.fromJson(userInfoResponseString, new TypeToken<CommonRestResponse<UserInfo>>(){}.getType());
		UserInfo userInfo = (UserInfo) userInfoRestResponse.getResponse();
		if(userInfo==null){
			LOGGER.error("get user info error.token="+token);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,"get user info error.");
		}
		String deviceIdentifier = DeviceIdentifierType.INNER_LOGIN.name();
		String pusherIdentify = null;
		UserLogin login = this.userService.innerLogin(userInfo.getNamespaceId(), userInfo.getId(), deviceIdentifier, pusherIdentify);
		LoginToken logintoken = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), null);
		String tokenString = WebTokenGenerator.getInstance().toWebToken(logintoken);
		setCookieInResponse("token", tokenString, httpRequest, httpResponse);
		//返回sourceUrl
		HttpSession session = httpRequest.getSession();
		String sourceUrl = (String) session.getAttribute("sourceUrl");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(new URI(sourceUrl));
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}

	private UserInfo getBizUserInfo(String zlSignature,String zlAppKey,String id,String name,String randomNum,String timeStamp) {
		try{
			String homeUrl = configurationProvider.getValue("home.url", "https://core.zuolin.com");
			String getUserInfoUri = homeUrl+"/evh/openapi/getUserInfoById";
			String appKey = configurationProvider.getValue(ZUOLIN_APP_KEY, "f9392ce2-341b-40c1-9c2c-99c702215535");
			App app = appProvider.findAppByKey(appKey);
			if(app==null){
				LOGGER.error("app nou found.appKey="+appKey);
				throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,"app nou found");
			}

			Map<String,String> params = new HashMap<String, String>();
			params.put("appKey", appKey);
			params.put("zlAppKey", zlAppKey);
			params.put("zlSignature", zlSignature);
			params.put("id", id);
			params.put("name", name);
			params.put("randomNum", randomNum);
			params.put("timeStamp", timeStamp);
			String nsignature = SignatureHelper.computeSignature(params, app.getSecretKey());

			String param = String.format("%s?zlSignature=%s&zlAppKey=%s&id=%s&name=%s&randomNum=%s&timeStamp=%s&appKey=%s&signature=%s",
					getUserInfoUri,
					URLEncoder.encode(zlSignature,"UTF-8"),zlAppKey,
					id,URLEncoder.encode(name,"UTF-8"),
					randomNum,timeStamp,
					appKey,URLEncoder.encode(nsignature,"UTF-8"));
			Clients ci = new Clients();
			String responseString = ci.restCall("GET", param, null, null, null);
			if(responseString!=null){
				Gson gson = new Gson();
				CommonRestResponse<UserInfo> userInfoRestResponse = gson.fromJson(responseString, new TypeToken<CommonRestResponse<UserInfo>>(){}.getType());
				UserInfo userInfo = (UserInfo) userInfoRestResponse.getResponse();
				return userInfo;
			}
			return null;
		}
		catch(Exception e){
			LOGGER.error("getUserInfo method error.e="+e.getMessage());
			return null;
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
