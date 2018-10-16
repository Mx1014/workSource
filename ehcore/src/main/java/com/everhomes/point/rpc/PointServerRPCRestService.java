package com.everhomes.point.rpc;

import java.net.URI;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.everhomes.util.DateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.RestResponseBase;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.SignatureHelper;
import com.everhomes.util.StringHelper;

public class PointServerRPCRestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PointServerRPCRestService.class);

    RestTemplate  template;
    {
    	SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    	factory.setConnectTimeout(60000);
        factory.setReadTimeout(60000);
        template = new RestTemplate(factory);
    }

  /*  @Autowired
    private RestTemplate template;*/

    @Autowired
    private ConfigurationProvider configProvider;

    <T extends RestResponseBase> T call(String api, Object param, Class<? extends RestResponseBase> responseType) {
        String body = "";
        if (param != null) {
            Map<String, String> map = new HashMap<>();
            StringHelper.toStringMap(null, param, map);

            String appKey = configProvider.getValue("server.point.appKey", "");
            String secretKey = configProvider.getValue("server.point.secretKey", "");

            map.put("appKey", appKey);
            String signature = SignatureHelper.computeSignature(map, secretKey);
            map.put("signature", signature);
            body = StringHelper.paramMapToQueryString(map);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        RequestEntity<String> requestEntity = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(getRestUri(api)));
        Timestamp start = new Timestamp(DateHelper.currentGMTTime().getTime());
        LOGGER.debug("start time, start = {}", start);
        ResponseEntity<? extends RestResponseBase> responseEntity = template.exchange(requestEntity, responseType);
        Timestamp end = new Timestamp(DateHelper.currentGMTTime().getTime());
        LOGGER.debug("end time, end = {}", end);
        LOGGER.debug("use time, use = {}", end.getTime()-start.getTime());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("coreserver rest call, request = {}", requestEntity.toString());
            LOGGER.debug("coreserver rest call, response = {}", responseEntity.toString());
        }
        RestResponseBase responseBase = responseEntity.getBody();
        LOGGER.debug("responseBase = {}", responseBase);
        if (responseBase != null &&
                (responseBase.getErrorCode().intValue() == 200 || responseBase.getErrorCode().intValue() == 201)) {
            return (T) responseBase;
        }
        throw error(api, responseBase);
    }

    private String getRestUri(String relativeUri) {
        String url = configProvider.getValue("server.point.url", "");
        StringBuilder sb = new StringBuilder(url);
        if (!url.endsWith("/"))
            sb.append("/");

        sb.append("point/");

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
        LOGGER.error("call point server failed, api = {}, resp = {}", api, StringHelper.toJsonString(resp));
        return RuntimeErrorException.errorWith("core", 500, "call point server error");
    }
}