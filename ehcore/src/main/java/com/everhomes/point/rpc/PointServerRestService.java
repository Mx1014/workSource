package com.everhomes.point.rpc;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.RestResponseBase;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PointServerRestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreServerRestService.class);

    @Autowired
    private RestTemplate template;

    @Autowired
    private ConfigurationProvider configProvider;

    <T extends RestResponseBase> T call(String api, Object param, Class<? extends RestResponseBase> responseType) {
        String body = "";
        if (param != null) {
            Map<String, String> map = new HashMap<>();
            StringHelper.toStringMap(null, param, map);

            String appKey = configProvider.getValue("server.core.appKey", "");
            String secretKey = configProvider.getValue("server.core.secretKey", "");

            map.put("appKey", appKey);
            String signature = SignatureHelper.computeSignature(map, secretKey);
            map.put("signature", signature);
            body = StringHelper.paramMapToQueryString(map);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        RequestEntity<String> requestEntity = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(getRestUri(api)));
        ResponseEntity<? extends RestResponseBase> responseEntity = template.exchange(requestEntity, responseType);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("coreserver rest call, request = {}", requestEntity.toString());
            LOGGER.debug("coreserver rest call, response = {}", responseEntity.toString());
        }
        RestResponseBase responseBase = responseEntity.getBody();

        if (responseBase != null &&
                (responseBase.getErrorCode().intValue() == 200 || responseBase.getErrorCode().intValue() == 201)) {
            return (T) responseBase;
        }
        throw error(api, responseBase);
    }

    private String getRestUri(String relativeUri) {
        String url = configProvider.getValue("server.core.url", "");
        StringBuilder sb = new StringBuilder(url);
        if (!url.endsWith("/"))
            sb.append("/");

        sb.append("evh/");

        if (relativeUri.startsWith("/"))
            sb.append(relativeUri.substring(1));
        else
            sb.append(relativeUri);
        return sb.toString();
    }

    private RuntimeErrorException error(String api, RestResponseBase resp) {
        if (resp.getErrorCode() == 403 && Objects.equals("user", resp.getErrorScope())) {
            return RuntimeErrorException.errorWith("user", 403, resp.getErrorDescription());
        }
        LOGGER.error("call core server failed, api = {}, resp = {}", api, StringHelper.toJsonString(resp));
        return RuntimeErrorException.errorWith("core", 500, "call core server error");
    }
}