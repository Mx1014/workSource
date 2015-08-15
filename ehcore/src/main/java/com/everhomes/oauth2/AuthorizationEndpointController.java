package com.everhomes.oauth2;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.everhomes.locale.LocaleStringService;
import com.everhomes.util.RuntimeErrorException;
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
}
