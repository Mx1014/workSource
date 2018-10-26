package com.everhomes.gogs;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.util.GsonUtil;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GogsProviderImpl implements GogsProvider, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GogsProviderImpl.class);

    private final static ResponseErrorHandler ERROR_HANDLER = new ErrorHandler();

    private RestTemplate restTemplate;

    {
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(ERROR_HANDLER);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(60000);
        requestFactory.setReadTimeout(60000);

        restTemplate.setRequestFactory(requestFactory);
    }

    private String gogsToken;
    private String gogsAdmin;
    private String gogsServer;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() != null) {
            return;
        }
        init();

        getOrCreateOrganization();
    }

    private void getOrCreateOrganization() {
        try {
            String api = String.format("/orgs/%s", gogsAdmin);
            ResponseEntity<GogsOrganization> response = restCall(HttpMethod.GET, api, null, GogsOrganization.class);
            if (response.getBody() == null) {
                throw new GogsNotExistException(GogsServiceErrorCode.SCOPE, GogsServiceErrorCode.ERROR_NOT_EXIST,
                        "org or repo or file not exist");
            }
        } catch (GogsNotExistException e) {
            // "username": "gogs2",
            // "full_name": "Gogs2",
            // "description": "Gogs is a painless self-hosted Git Service.",
            // "website": "https://gogs.io",
            // "location": "USA"

            Map<String, String> param = new HashMap<>(1);
            param.put("username", gogsAdmin);

            String api = String.format("/admin/users/%s/orgs", "zuolin");
            try {
                restCall(HttpMethod.POST, api, param, GogsOrganization.class);
            } catch (Exception e1) {
                LOGGER.warn("Gogs create organization failed. {}", e.getMessage());
            }
        } catch (Exception e) {
            LOGGER.warn("Gogs get organization failed. {}", e.getMessage());
        }
    }

    @Scheduled(fixedRate = 10 * 60 * 1000L)
    public void init() {
        gogsServer = configurationProvider.getValue("gogs.server.url", "");
        gogsAdmin = configurationProvider.getValue("gogs.admin.name", "");
        gogsToken = configurationProvider.getValue("gogs.admin.token", "");
    }

    // POST /admin/users/:username/repos
    @Override
    public <T> T createRepo(CreateGogsRepoParam param, Class<T> type) {
        if (gogsAdmin.length() == 0) {
            init();
        }
        String api = String.format("/admin/users/%s/repos", gogsAdmin);
        ResponseEntity<String> response = restCall(HttpMethod.POST, api, param, String.class);
        return GsonUtil.fromJson(response.getBody(), type);
    }

    // GET /repos/:username/:reponame/objects/:ref/:path
    @Override
    public <T> List<T> listObjects(String repoName, String path, GogsRawFileParam param, Type type) {
        if (gogsAdmin.length() == 0) {
            init();
        }
        String api = String.format("/repos/%s/%s/objects/%s/%s", gogsAdmin, repoName, "master", path);
        ResponseEntity<String> response = restCall(HttpMethod.GET, api, param, String.class);
        return GsonUtil.fromJson(response.getBody(), type);
    }

    // GET /repos/:username/:reponame/commits/:ref/:path
    @Override
    public <T> List<T> listCommits(String repoName, String path, GogsPaginationParam param, Type type) throws GogsNotExistException {
        if (gogsAdmin.length() == 0) {
            init();
        }
        String api = String.format("/repos/%s/%s/commits/%s/%s", gogsAdmin, repoName, "master", path);
        ResponseEntity<String> response = restCall(HttpMethod.GET, api, param, String.class);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
        return gson.fromJson(response.getBody(), type);
    }

    // POST /repos/:username/:reponame/raw/:ref/:path
    @Override
    public GogsCommit commitFile(String repoName, String path, GogsRawFileParam param) throws GogsConflictException {
        return rawFile(HttpMethod.POST, repoName, path, param, GogsCommit.class);
    }

    // DELETE /repos/:username/:reponame/raw/:ref/:path
    @Override
    public GogsCommit deleteFile(String repoName, String path, GogsRawFileParam param) throws GogsNotExistException {
        return rawFile(HttpMethod.DELETE, repoName, path, param, GogsCommit.class);
    }

    // GET /repos/:username/:reponame/raw/:ref/:path
    @Override
    public byte[] getFile(String repoName, String path, GogsRawFileParam param) throws GogsNotExistException {
        return rawFile(HttpMethod.GET, repoName, path, param, byte[].class);
    }

    private <T> T rawFile(HttpMethod method, String repoName, String path, GogsRawFileParam param, Class<T> type) {
        if (gogsAdmin.length() == 0) {
            init();
        }
        if (param != null && param.getTreePath() == null) {
            param.setTreePath(path);
        }
        String api = String.format("/repos/%s/%s/raw/%s/%s", gogsAdmin, repoName, "master", path);
        ResponseEntity<T> response = restCall(method, api, param, type);
        return response.getBody();
    }

    // GET /repos/:username/:reponame/archive/:ref:format
    @Override
    public byte[] downloadArchive(String repoName) {
        if (gogsAdmin.length() == 0) {
            init();
        }
        String api = String.format("/repos/%s/%s/archive/%s.%s", gogsAdmin, repoName, "master", "zip");
        ResponseEntity<byte[]> response = restCall(HttpMethod.GET, api, null, byte[].class);
        return response.getBody();
    }

    // POST /repos/:username/:reponame/hooks
    @Override
    public <T> T createHook(String repoName, CreateGogsHookParam param, Class<T> type) {
        if (gogsAdmin.length() == 0) {
            init();
        }
        String api = String.format("/repos/%s/%s/hooks", gogsAdmin, repoName);
        ResponseEntity<T> response = restCall(HttpMethod.POST, api, param, type);
        return response.getBody();
    }

    private <T> ResponseEntity<T> restCall(HttpMethod method, String api, Object param, Class<T> responseType) {
        String body = param != null ? StringHelper.toJsonString(param) : "";
        URI uri = URI.create(gogsServer + api);

        if (method == HttpMethod.GET) {
            uri = buildURI(param, uri);
            body = "";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("token " + gogsToken));
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON_UTF8.toString()));

        RequestEntity<String> requestEntity = new RequestEntity<>(body, headers, method, uri);
        ResponseEntity<T> responseEntity = restTemplate.exchange(requestEntity, responseType);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("gogs rest call, request = {}", requestEntity.toString());
            LOGGER.debug("gogs rest call, response = {}", responseEntity.toString());
        }
        return responseEntity;
    }

    private URI buildURI(Object param, URI uri) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(uri);
        introspectorObject(param).forEach((k, v) -> builder.queryParam(humpToUnderline(k), v));
        uri = builder.build().toUri();
        return uri;
    }

    private String humpToUnderline(String text) {
        StringBuilder sb = new StringBuilder(text.length() + text.length() / 5);
        for (char c : text.trim().toCharArray()) {
            if (c >= 65 && c <= 90) {
                sb.append("_");
                c = (char) (c + 32);
            }
            sb.append(c);
        }
        return sb.toString();
    }

    private Map<String, Object> introspectorObject(Object param) {
        if (param == null) {
            return new HashMap<>(0);
        }

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(param.getClass(), Object.class);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            Map<String, Object> values = new HashMap<>(propertyDescriptors.length);
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                Object value = descriptor.getReadMethod().invoke(param);
                if (value != null) {
                    values.put(descriptor.getName(), value);
                }
            }
            return values;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>(0);
    }

    private static class ErrorHandler implements ResponseErrorHandler {
        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR
                    || response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (Exception e) {
                LOGGER.error("Gogs rest call getBody error.", e);
            }
            LOGGER.error("Gogs rest call error: {}", sb.toString());
            switch (response.getStatusCode()) {
                case CONFLICT:
                    throw new GogsConflictException(GogsServiceErrorCode.SCOPE, GogsServiceErrorCode.ERROR_CONFLICT,
                            "conflict");
                case NOT_FOUND:
                    throw new GogsNotExistException(GogsServiceErrorCode.SCOPE, GogsServiceErrorCode.ERROR_NOT_EXIST,
                            "org or repo or file not exist");
                default:
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                            "unexpected error from gogs, error = %s", sb.toString());
            }
        }
    }
}