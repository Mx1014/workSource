package com.everhomes.wx;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.news.CreateNewsResponse;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserService;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.WebTokenGenerator;

@RestDoc(value = "WX Controller", site = "core")
@RestController
@RequestMapping("/wx")
public class WXController {
	private static final Logger LOGGER = LoggerFactory.getLogger(WXController.class);

	@Autowired
	private UserService userService;
	
	/**
	 * <b>URL: /wx/wxLogin</b>
	 * <p>
	 * 微信login
	 * </p>
	 */
	@RequestMapping("wxLogin")
	@RestReturn(CreateNewsResponse.class)
	@RequireAuthentication(false)
	public void wxLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String sourceUrl = (String) request.getParameter("sourceUrl");
		String mallId = (String) request.getParameter("mallId");
		this.verifyWechatOauth2Command(sourceUrl, mallId);

		LoginToken token = getLoginToken(request);
		if (isValid(token)) {
//			sourceUrl =  "http://beta.zuolin.com/property_service/index.html?hideNavigationBar=1#/my_service";
			LOGGER.info("redirect url : "+sourceUrl );
			response.sendRedirect(sourceUrl);
		} else {
			HttpSession session = request.getSession();
			session.setAttribute("sourceUrl", sourceUrl);
			String sessionId = session.getId();
			// Configuration homeUrlConf =
			// configurationService.getByName(ConfigurationConstant.HOME_URL);
			// Configuration authorizeConf =
			// configurationService.getByName(ConfigurationConstant.WECHAT_OAUTH2_AUTHORIZE_URI);
			// Configuration appidConf =
			// configurationService.getByName(ConfigurationConstant.WECHAT_PUBLIC_ACCOUNT_APPID);
			String redirectUri =  "http://beta.zuolin.com/evh/wx/wxRedirect";
			redirectUri += "?mallId=" + mallId;

			String authorizeUri = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s"
					+ "&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect", "wx58052b72c7579016",
					URLEncoder.encode(redirectUri, "UTF-8"), sessionId); 
			LOGGER.info("redirect url : "+authorizeUri );
			response.sendRedirect(authorizeUri);
			
		}
	}
	/**
	 * <b>URL: /wx/wxRedirect</b>
	 * <p>
	 * 微信login
	 * </p>
	 */
	@RequestMapping("wxRedirect")
	@RestReturn(CreateNewsResponse.class)
	@RequireAuthentication(false)
	public void wxRedirect(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String code = request.getParameter("code");
		String mallIdStr = (String) request.getParameter("mallId");
		String sourceUrl =  "http://beta.zuolin.com/property_service/index.html?hideNavigationBar=1#/my_service";
		LOGGER.info("create a user ");
		response.sendRedirect(sourceUrl); 
	}
	private LoginToken getLoginToken(HttpServletRequest request) {
		Map<String, String[]> paramMap = request.getParameterMap();
		String loginTokenString = null;
		for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
			String value = StringUtils.join(entry.getValue(), ",");
			if (LOGGER.isTraceEnabled())
				LOGGER.trace("HttpRequest param " + entry.getKey() + ": " + value);
			if (entry.getKey().equals("token"))
				loginTokenString = value;
		}

		if (loginTokenString == null) {
			if (request.getCookies() != null) {
				List<Cookie> matchedCookies = new ArrayList<>();

				for (Cookie cookie : request.getCookies()) {
					if (LOGGER.isTraceEnabled())
						LOGGER.trace("HttpRequest cookie " + cookie.getName() + ": " + cookie.getValue() + ", path: "
								+ cookie.getPath());

					if (cookie.getName().equals("token")) {
						matchedCookies.add(cookie);
					}
				}

				if (matchedCookies.size() > 0)
					loginTokenString = matchedCookies.get(matchedCookies.size() - 1).getValue();
			}
		}

		if (loginTokenString != null)
			try {
				return WebTokenGenerator.getInstance().fromWebToken(loginTokenString, LoginToken.class);
			} catch (Exception e) {
				LOGGER.error("Invalid login token.tokenString={}", loginTokenString);
				return null;
			}

		return null;
	}

	private boolean isValid(LoginToken token) {
		if (token == null) {
			User user = UserContext.current().getUser();
			Long userId = -1L;
			if (user != null) {
				userId = user.getId();
			}
			// It's ok when using signature
			// LOGGER.error("Invalid token, token={}, userId={}", token,
			// userId);
			return false;
		}

		return this.userService.isValidLoginToken(token);
	}

	private void verifyWechatOauth2Command(String sourceUrl, String mallId) {
		if (StringUtils.isEmpty(sourceUrl)) {
			LOGGER.error("sourceUrl is null");

		}
		if (StringUtils.isEmpty(mallId)) {
			LOGGER.error("mallId is null");

		}
	}

	private void verifyWechatRedirectCommand(String code, String mallId) {
		if (StringUtils.isEmpty(code)) {
			LOGGER.error("code is null");
		}
		if (StringUtils.isEmpty(mallId)) {
			LOGGER.error("mallId is null");
		}

	}
}
