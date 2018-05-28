package com.everhomes.pay.oauth2;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.order.PayProvider;
import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.pay.PayConstants;
import com.everhomes.pay.base.RestClient;
import com.everhomes.pay.oauth.AuthorizationCommand;
import com.everhomes.pay.rest.ApiConstants;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.pay.controller.RegisterBusinessUserRestResponse;
import com.everhomes.server.schema.tables.records.EhNamespacePayMappingsRecord;
import com.everhomes.user.UserContext;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.net.URLEncoder;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class PayOauth2ServiceImpl implements PayOauth2Service, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayOauth2ServiceImpl.class);

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private PayProvider payProvider;

    @Autowired
    private ConfigurationProvider configProvider;

    private RestClient restClient = null;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        String payHomeUrl = configurationProvider.getValue(UserContext.getCurrentNamespaceId(),"pay.v2.home.url", "");
        EhNamespacePayMappingsRecord payMapping = payProvider.getNamespacePayMapping(UserContext.getCurrentNamespaceId());
        if(payMapping == null){
            LOGGER.error("pay account mapping not found for namespaceId={}", UserContext.getCurrentNamespaceId());
            return;
        }
        restClient = new RestClient(payHomeUrl, payMapping.getAppKey(), payMapping.getSecretKey());
    }

    public String authorizeLogon(AuthorizationCommand cmd) {
        if(LOGGER.isDebugEnabled()) {LOGGER.debug("authorize-command=" + GsonUtil.toJson(cmd));}
        RestResponse response = restClient.restCall(
                "POST",
                ApiConstants.MEMBER_REGISTERBUSINESSUSER_URL,
                cmd,
                RegisterBusinessUserRestResponse.class);
        if(LOGGER.isDebugEnabled()) {LOGGER.debug("authorize-response=" + GsonUtil.toJson(response));}
        return response.toString();
    }

    public ResponseEntity redirectLogon(String code, String state) {
        EhNamespacePayMappingsRecord payMapping = payProvider.getNamespacePayMapping(UserContext.getCurrentNamespaceId());
        try {

            URI uri = new URI(String.format("%s?grant_type=authorization_code&code=%s&redirect_uri=%s&app_key=%s",
                    configProvider.getValue(configurationProvider.getValue(UserContext.getCurrentNamespaceId(), "pay.v2.home.url", "") + PayConstants.TOKEN_SERVICE_URL, ""),
                    code,
                    URLEncoder.encode(configProvider.getValue(PayConstants.CORE_SERVER_URL, "") + PayConstants.OAUTH2_REDIRECT_LOGON_URL, "UTF-8"),
                    payMapping.getAppKey()));


            String credential = String.format("%s:%s", payMapping.getAppKey(), payMapping.getSecretKey());

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(uri);
            httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
            httpHeaders.add("Authorization", String.format("Basic %s", Base64.encodeBase64String(credential.getBytes("UTF-8"))));
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } catch (Exception e) {
            LOGGER.error("redirectLogon error: " + e.getMessage());
            return null;
        }
    }
}
