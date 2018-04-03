package com.everhomes.rentalv2;

import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author sw on 2017/10/13.
 */
public class RentalUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(RentalUtils.class);


    public static void test(String url, Object requestCmd, Class<?> responseType) {
        RestTemplate template = new RestTemplate();

        ResponseEntity<String> responseEntity = template.postForEntity(url, requestCmd, String.class);

    }

    public static Object restCall(String api, Object o, Class<?> responseType,String host) {
        AsyncRestTemplate template = new AsyncRestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        template.setMessageConverters(messageConverters);
        String[] apis = api.split(" ");
        String method = apis[0];

        String url = host
                + api.substring(method.length() + 1, api.length()).trim();

        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        HttpEntity<String> requestEntity = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Host", host);
        headers.add("charset", "UTF-8");

        ListenableFuture<ResponseEntity<String>> future = null;

        if (method.equalsIgnoreCase("POST")) {
            requestEntity = new HttpEntity<>(StringHelper.toJsonString(o),
                    headers);
            LOGGER.debug("DEBUG: restCall headers: "+requestEntity.toString());
            future = template.exchange(url, HttpMethod.POST, requestEntity,
                    String.class);
        } else {
            Map<String, String> params = new HashMap<String, String>();
            StringHelper.toStringMap("", o, params);
            LOGGER.debug("params is :" + params.toString());

            for (Map.Entry<String, String> entry : params.entrySet()) {
                paramMap.add(entry.getKey().substring(1),
                        URLEncoder.encode(entry.getValue()));
            }

            url = UriComponentsBuilder.fromHttpUrl(url).queryParams(paramMap).build().toUriString();
            requestEntity = new HttpEntity<>(null, headers);
            LOGGER.debug("DEBUG: restCall headers: "+requestEntity.toString());
            future = template.exchange(url, HttpMethod.GET, requestEntity,
                    String.class);
        }

        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = future.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.info("restCall error " + e.getMessage());
            return null;
        }

        if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK) {

            String bodyString = responseEntity.getBody();
            LOGGER.debug(bodyString);
            LOGGER.debug("HEADER" + responseEntity.getHeaders());
//			return bodyString;
            return StringHelper.fromJsonString(bodyString, responseType);

        }

        return null;

    }
}
