package com.everhomes.oauth2;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.oauth2.AuthorizationCommand;
import com.everhomes.rest.oauth2.GetUserInfoResultResponse;
import com.everhomes.rest.oauth2.OAuth2AccessTokenResponse;
import com.everhomes.rest.oauth2.OAuth2ServiceErrorCode;
import com.everhomes.rest.user.DeviceIdentifierType;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserLogin;
import com.everhomes.user.UserService;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.WebTokenGenerator;

@RequireOAuth2Authentication(OAuth2AuthenticationType.NO_AUTHENTICATION)
@Controller
@RequestMapping("/oauth2")
public class AuthorizationEndpointController extends OAuth2ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationEndpointController.class);

	@Autowired
	private AppProvider appProvider;

	@Autowired
	private LocaleStringService localeStringService;

	@Autowired
	private AccessTokenManager accessTokenManager;

	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Autowired
	private UserService userService;

	@RequestMapping("authorize")
	public String authorize(
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

		// double check in confirmation call to protect against tampering in confirmation callback
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
				// make sure we always carry the view state back to front end for next round of submission
				model.addAttribute("viewState", viewState);
				return "oauth2-authorize";
			}
		} catch (RuntimeErrorException e) {
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
	@RequestMapping("innerlogin")
	public Object innerlogin(
			@RequestParam(value="sourceUrl", required = true) String sourceUrl,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) throws URISyntaxException {
		HttpSession session = httpRequest.getSession();
		session.setAttribute("sourceUrl", sourceUrl);

		String homeUrl = configurationProvider.getValue("home.url", "https://core.zuolin.com");
		User user = UserContext.current().getUser();
		if(user==null||user.getId()==User.ANNONYMOUS_UID){
			String url =  homeUrl+"/evh/oauth2/oauth";
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setLocation(new URI(url));
			return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
		}else{
			String url =  homeUrl+"/evh/oauth2/result";
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setLocation(new URI(url));
			return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
		}
	}

	@RequireAuthentication(false)
	@RequestMapping("oauth")
	public Object oauth(HttpServletRequest httpRequest,HttpServletRequest request,HttpServletResponse httpResponse, Model model) throws Exception {
		String state = request.getSession().getId();
		HttpSession session = request.getSession();
		String sourceUrl = (String) session.getAttribute("sourceUrl");
		if(sourceUrl!=null&&!sourceUrl.equals("")){
			request.getSession().setAttribute("sourceUrl", sourceUrl);
		}

		String homeUrl = configurationProvider.getValue("home.url", "https://core.zuolin.com");
		String appKey = configurationProvider.getValue("biz.appKey", "d80e06ca-3766-11e5-b18f-b083fe4e159f");

		if(accessTokenManager.getAccessToken(state) == null) {
			String oauthUri = homeUrl+"/evh/oauth2/authorize";
			String gettokenUri = homeUrl+"/evh/oauth2/gettoken";
			String uri;
			uri = String.format("%s?response_type=code&client_id=%s&redirect_uri=%s&scope=basic&state=%s#oauth2_redirect",
					oauthUri,
					appKey,
					URLEncoder.encode(gettokenUri, "UTF-8"),
					state
					);
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setLocation(new URI(uri));
			return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
		}else{
			String resultUri = homeUrl+"/evh/oauth2/result";
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setLocation(new URI(resultUri));
			return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
		}	
	}
	@RequireAuthentication(false)
	@RequestMapping("gettoken")
	public Object gettoken(HttpServletRequest request, HttpServletResponse httpResponse, Model model) throws Exception {
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		String homeUrl = configurationProvider.getValue("home.url", "https://core.zuolin.com");
		String tokenUri = homeUrl+"/evh/oauth2/token";
		String gettokenUri = homeUrl+"/evh/oauth2/gettoken";
		String appKey = configurationProvider.getValue("biz.appKey", "d80e06ca-3766-11e5-b18f-b083fe4e159f");
		App app = appProvider.findAppByKey(appKey);
		if(app==null){
			LOGGER.error("app nou found.appKey="+appKey);
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,"app nou found");
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("grant_type", "authorization_code");
		params.put("code", code);
		params.put("redirect_uri", gettokenUri);
		params.put("client_id", appKey);
		String credential = String.format("%s:%s", appKey, app.getSecretKey());
		
		Clients client = new Clients();
		String responseString = client.restCall("POST", tokenUri, params, "Authorization", String.format("Basic %s", Base64.encodeBase64String(credential.getBytes("UTF-8"))));
		OAuth2AccessTokenResponse tokenResponse = (OAuth2AccessTokenResponse) StringHelper.fromJsonString(responseString, OAuth2AccessTokenResponse.class);
		accessTokenManager.setAccessToken(state, tokenResponse.getAccess_token());

		String url =  homeUrl+"/evh/oauth2/result";
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(new URI(url));
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}

	@RequireAuthentication(false)
	@RequestMapping("result")
	public Object result(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		HttpSession session = request.getSession();

		User user = UserContext.current().getUser();
		if(user==null||user.getId()==User.ANNONYMOUS_UID){
			String sessionId = request.getSession().getId();
			String token = this.accessTokenManager.getAccessToken(sessionId);
			String homeUrl = configurationProvider.getValue("home.url", "https://core.zuolin.com");
			String uri = homeUrl+"/evh/oauth2api/getUserInfo";
			String gettokenUri = homeUrl+"/evh/oauth2/gettoken";
			if(token==null){
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setLocation(new URI(gettokenUri));
				return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
			}
			
			Clients client = new Clients();
			String responseString = client.restCall("GET", uri, null, "Authorization", String.format("Bearer %s", Base64.encodeBase64String(token.getBytes("UTF-8"))));
			GetUserInfoResultResponse userInfoRestResponse = (GetUserInfoResultResponse) StringHelper.fromJsonString(responseString, GetUserInfoResultResponse.class);
			UserInfo userInfo = userInfoRestResponse.getResponse();
			if(userInfo!=null){
				String deviceIdentifier = DeviceIdentifierType.INNER_LOGIN.name();
				String pusherIdentify = null;
				UserLogin login = this.userService.innerLogin(userInfo.getNamespaceId(), userInfo.getId(), deviceIdentifier, pusherIdentify);
				LoginToken logintoken = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber());
				String tokenString = WebTokenGenerator.getInstance().toWebToken(logintoken);
				setCookieInResponse("token", tokenString, request, response);
			}
		}
		String sourceUrl = (String) session.getAttribute("sourceUrl");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(new URI(sourceUrl));
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
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
