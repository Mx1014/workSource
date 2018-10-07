package com.everhomes.oauth2;

import com.everhomes.app.App;
import com.everhomes.app.AppProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.WebRequestInterceptor;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.namespace.Namespace;
import com.everhomes.openapi.AppNamespaceMapping;
import com.everhomes.openapi.AppNamespaceMappingProvider;
import com.everhomes.rest.oauth2.*;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

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

    @Autowired
    private AppNamespaceMappingProvider appNamespaceMappingProvider;

	@RequestMapping("authorize")
	public Object authorize(
			@RequestParam(value="response_type", required = true) String responseType,
			@RequestParam(value="client_id", required = true) String clientId,
			@RequestParam(value="redirect_uri", required = false) String redirectUri,
			@RequestParam(value="scope", required = false) String scope,
			@RequestParam(value="state", required = false) String state,
			@RequestParam(value="redirect_type", required = false, defaultValue = "http") String redirectType,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) {

		App app = this.appProvider.findAppByKey(clientId);
		if(app == null) {
		    if(LOGGER.isDebugEnabled()) {
		        LOGGER.debug("Invalid request client_id and app not found, clientId={}, redirectJspPath={}", clientId, "oauth2-error-response");
		    }
			model.addAttribute("errorDescription", this.localeStringService.getLocalizedString(
					OAuth2ServiceErrorCode.SCOPE,
					String.valueOf(OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST),
					httpRequest.getLocale().toLanguageTag(),
					"Invalid request client_id or unregistered redirectTo URI"));

			return "oauth2-error-response";
		}

		if(redirectUri == null || redirectUri.isEmpty()) {
			redirectUri = this.oAuth2Service.getDefaultRedirectUri(app.getId());
		}

		if(!this.oAuth2Service.validateRedirectUri(app.getId(), redirectUri)) {
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("Invalid redirect uri, appId={}, redirectUri={}, redirectJspPath={}", app.getId(), redirectUri, "oauth2-error-response");
            }
			model.addAttribute("errorDescription", this.localeStringService.getLocalizedString(
					OAuth2ServiceErrorCode.SCOPE,
					String.valueOf(OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST),
					httpRequest.getLocale().toLanguageTag(),
					"Invalid request client_id or unregistered redirectTo URI"));

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
		if (user != null && user.getId() != User.ANNONYMOUS_UID) {
			URI uri = oAuth2Service.confirmAuthorization(user, cmd);
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("User has logon, will redirect to {}", uri);
            }
			if (uri != null) {
                return redirectTo(uri, redirectType, model);
			}
		}
		//no logon
        model.addAttribute("viewState", WebTokenGenerator.getInstance().toWebToken(cmd));
        oAuth2Service.addAttribute(model, cmd, app);
        
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("User has not logon, will redirect to jsp path {}, headers={}", "oauth2-authorize2", getHeaders(httpRequest));
        }
        
		return "oauth2-authorize2";
	}

    @RequestMapping("confirm")
	public Object confirmAuthorization(
			@RequestParam(value="identifier", required = true) String identifier,
			@RequestParam(value="password", required = true) String password,
			@RequestParam(value="viewState", required = true) String viewState,
            @RequestParam(value="redirect_type", required = false, defaultValue = "http") String redirectType,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) throws URISyntaxException {

		AuthorizationCommand cmd = WebTokenGenerator.getInstance().fromWebToken(viewState, AuthorizationCommand.class);

        // double check in confirmation api to protect against tampering in confirmation callback
        App app = this.appProvider.findAppByKey(cmd.getclient_id());
        if(app == null) {
            model.addAttribute("errorDescription", this.localeStringService.getLocalizedString(
					OAuth2ServiceErrorCode.SCOPE,
					String.valueOf(OAuth2ServiceErrorCode.ERROR_INVALID_REQUEST),
					httpRequest.getLocale().toLanguageTag(),
					"Invalid request client_id or unregistered redirectTo URI"));

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
					"Invalid request client_id or unregistered redirectTo URI"));

            return "oauth2-error-response";
        }

        Integer namespaceId = Namespace.DEFAULT_NAMESPACE;
        AppNamespaceMapping namespaceMapping = appNamespaceMappingProvider.findAppNamespaceMappingByAppKey(app.getAppKey());
        if (namespaceMapping != null) {
            namespaceId = namespaceMapping.getNamespaceId();
        }

        LOGGER.info("Confirm OAuth2 authorization: {}", cmd);
        try {
            ConfirmAuthorizationVO confirmAuthorization = oAuth2Service.confirmAuthorization(namespaceId, identifier, password, cmd);
            if (confirmAuthorization != null) {
                UserLogin login = confirmAuthorization.getUserLogin();
                LoginToken token = new LoginToken(login.getUserId(), login.getLoginId(), login.getLoginInstanceNumber(), login.getImpersonationId());
                String tokenString = WebTokenGenerator.getInstance().toWebToken(token);

                int age = 7 * 24 * 60 * 60; // 一周的有效时间
                WebRequestInterceptor.setCookieInResponse("token", tokenString, httpRequest, httpResponse, age);
                WebRequestInterceptor.setCookieInResponse("namespace_id", String.valueOf(namespaceId), httpRequest, httpResponse, age);

                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setLocation(confirmAuthorization.getUri());
                return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
			} else {
                // 登录失败
                // make sure we always carry the view state back to front end for next round of submission
                String localizedString = localeStringService.getLocalizedString(
                        OAuth2LocalStringCode.SCOPE,
                        OAuth2LocalStringCode.ERROR_LOGIN,
                        Locale.CHINA.toString(),
                        "Invalid identifier or password");

                model.addAttribute("errorDesc", localizedString);
                model.addAttribute("viewState", viewState);
                oAuth2Service.addAttribute(model, cmd, app);
                return "oauth2-authorize2";
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
						"Internal server error"));

				return "oauth2-error-response";
			default : {
				URI uri = new URI(String.format("%s#error=%s&state=%s", redirectUri,
						this.oAuth2Service.getOAuth2AuthorizeError(e.getErrorCode()), cmd.getState()));
                return redirectTo(uri, redirectType, model);
			}
			}
		} catch(Exception e) {
			LOGGER.error("Unexpected exception", e);

			URI uri = new URI(String.format("%s#error=%s&state=%s", redirectUri,
					this.oAuth2Service.getOAuth2AuthorizeError(OAuth2ServiceErrorCode.ERROR_SERVER_ERROR), cmd.getState()));
            return redirectTo(uri, redirectType, model);
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
            @RequestParam(value="redirect_type", required = false, defaultValue = "http") String redirectType,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) throws Exception {
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
            WebRequestInterceptor.setCookieInResponse("token", tokenString, httpRequest, httpResponse);
		}
        return redirectTo(new URI(sourceUrl), redirectType, model);
	}

	@RequireAuthentication(false)
	@RequestMapping("oauth2Logon")
	public Object oauth2Logon(@RequestParam(value="sourceUrl", required = true) String sourceUrl,
                              @RequestParam(value="redirect_type", required = false, defaultValue = "http") String redirectType,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) throws Exception {
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
            return redirectTo(new URI(uri), redirectType, model);
		}
        return redirectTo(new URI(sourceUrl), redirectType, model);
	}

	@RequireAuthentication(false)
	@RequestMapping("redirectUri")
	public Object redirectUri(@RequestParam(value="redirect_type", required = false, defaultValue = "http") String redirectType,
                              HttpServletRequest httpRequest, HttpServletResponse httpResponse, Model model) throws Exception {
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
        WebRequestInterceptor.setCookieInResponse("token", tokenString, httpRequest, httpResponse);
		//返回sourceUrl
		HttpSession session = httpRequest.getSession();
		String sourceUrl = (String) session.getAttribute("sourceUrl");
        return redirectTo(new URI(sourceUrl), redirectType, model);
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

    private Object redirectTo(URI uri, String redirectType, Model model) {
        // 有些浏览器不支持 302 重定向，所以采用 script 类型的跳转
        if (Objects.equals(redirectType, "script")) {
            model.addAttribute("redirectUrl", uri.toString());
            return "oauth2-redirect";
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }
    
    private String getHeaders(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        sb.append("{");

        Enumeration<String> headers = request.getHeaderNames();
        int i = 0;
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();

            if (i > 0)
                sb.append(", ");
            sb.append(header + ": " + request.getHeader(header));
            i++;
        }
        sb.append("}");
        
        return sb.toString();
    }
}
