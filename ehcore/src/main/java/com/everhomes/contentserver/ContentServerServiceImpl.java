package com.everhomes.contentserver;

import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.bus.*;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.urlvendor.ContentURLVendors;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.contentserver.*;
import com.everhomes.rest.contentserver.WebSocketConstant;
import com.everhomes.rest.messaging.ImageBody;
import com.everhomes.rest.rpc.PduFrame;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserLogin;
import com.everhomes.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class ContentServerServiceImpl implements ContentServerService, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentServerServiceImpl.class);

    @Autowired
    private ConnectionProvider connectionProvider;

    @Autowired
    private ContentServerProvider contentServerProvider;

    @Autowired
    private BigCollectionProvider bigCollectionProvider;

    @Autowired
    private LocalBusOneshotSubscriberBuilder localBusSubscriberBuilder;

    @Autowired
    private LocalBus localBus;

    @Autowired
    private BusBridgeProvider busBridgeProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    private final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    private CloseableHttpClient httpClient;

    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static final String UPLOAD_FMT = "upload_%s";
    
    // 升级平台包到1.0.1，把@PostConstruct换成ApplicationListener，
    // 因为PostConstruct存在着平台PlatformContext.getComponent()会有空指针问题 by lqs 20180516
    //@PostConstruct
    protected void init() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        this.httpClient = httpClientBuilder.build();
    }
    
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null) {
            init();
        }
    }
    
    @PreDestroy
    protected void clean() {
        if (httpClient != null) {
            try {
                // 关闭流并释放资源
                httpClient.close();
            } catch (Exception e) {
                LOGGER.error("Failed to close the http client", e);
            }
        }
    }

    @Override
    public ContentServer addContentServer(AddContentServerCommand cmd) {
        List<ContentServer> contentServers = contentServerProvider.listContentServers();
        ContentServer findResult = contentServers.stream()
                .filter(r -> ConvertHelper.convert(cmd, ContentServer.class).equals(r)).findAny().orElse(null);
        if (findResult != null)
            return findResult;
        ContentServer contentServer = ConvertHelper.convert(cmd, ContentServer.class);
        try {
            connectionProvider.connect(cmd.getPrivateAddress(), cmd.getPrivatePort());
        } catch (Exception e) {
            LOGGER.error("invalid content server");
            throw RuntimeErrorException.errorWith(ContentServerErrorCode.SCOPE,
                    ContentServerErrorCode.ERROR_INVALID_SERVER, "invalid content server");
        }
        contentServerProvider.addContentServer(contentServer);
        return contentServer;
    }

    @Override
    public void removeContentServer(Long contentServerId) {
        LOGGER.info("Invoke removeContentServer.serverId={}", contentServerId);
        if (contentServerId == null) {
            return;
        }
        ContentServer server = contentServerProvider.findContentServerById(contentServerId);
        if (null == server) {
            LOGGER.info("cannot find server");
            return;
        }
        cleanAll(contentServerId);
        connectionProvider.removeSession(server.getPublicAddress(), server.getPublicPort());
        contentServerProvider.deleteContentServer(server);
    }

    private void cleanAll(long serverId) {
        contentServerProvider.cleanAll(serverId);
    }

    @Override
    public void addConfigItem(AddConfigItemCommand cmd) {
        LOGGER.info("Invoke addConfigItem.Item={}", cmd);
        PduFrame frame = Generator.createPduFrame(
                String.format("%s%s.%s", WebSocketConstant.CONTENT_CONFIG_REQ, cmd.getConfigType().toLowerCase(),
                        cmd.getConfigName()), cmd, 0);
        connectionProvider.configServers(frame);
    }

    @Override
    public void removeConfigItem(String itemName) {
        LOGGER.info("Invoke removeConfigItem.itemName={}", itemName);
    }

    @Override
    public void updateContentServer(UpdateContentServerCommand cmd) {
        contentServerProvider.updateContentServer(ConvertHelper.convert(cmd, ContentServer.class));
    }

    @Override
    public List<ContentServerDTO> listContservers() {
        List<ContentServerDTO> result = contentServerProvider.listContentServers().stream()
                .map(r -> ConvertHelper.convert(r, ContentServerDTO.class)).collect(Collectors.toList());
        return result;
    }

    @Cacheable(value = "selectContentServer", unless = "#result==null")
    @Override
    public ContentServer selectContentServer() throws Exception {
        // LOGGER.info("Enter select content server");
        List<ContentServer> servers = contentServerProvider.listContentServers();
        if (CollectionUtils.isEmpty(servers)) {
            LOGGER.error("cannot find content storage");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "internal error,cannot find contentserver");
        }
        if (servers.size() == 1) {
            ContentServer server = ConvertHelper.convert(servers.get(0), ContentServer.class);
            LOGGER.info("find content server,server id={}", server.getId());
            String address = server.getPrivateAddress();
            int port = server.getPrivatePort();
            if (StringUtils.isEmpty(address)) {
                address = server.getPublicAddress();
                port = server.getPublicPort();
            }
            connectionProvider.connect(address, port);
            return server;
        }
        return connectionProvider.chooseContentServer(servers, 0L);
    }

    @Override
    public List<String> parserUri(List<String> uris, String ownerType, Long ownerId) {
        if (CollectionUtils.isEmpty(uris)) {
            return new ArrayList<>();
        }
        Map<Long, ContentServer> cache = getServersHash();
        final String token[] = new String[1];
        if (UserContext.current().getLogin() != null) {
            token[0] = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
        }
        return uris.stream().map(r -> parserSingleUri(r, cache, ownerType, ownerId, token[0])).collect(Collectors.toList());
    }

    @Override
    public String parserUri(String uri, String ownerType, Long ownerId) {
        Map<Long, ContentServer> cache = getServersHash();
        String token = null;
        if (UserContext.current().getLogin() != null) {
            token = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
        }
        return parserSingleUri(uri, cache, ownerType, ownerId, token);
    }

    @Override
    public String parserUri(String uri) {
        if (uri != null && !uri.isEmpty()) {
            try {
                return parserUri(uri, null, null);
            } catch (Exception ignored) {
                //
            }
        }
        return null;
    }

    @Override
    public String parseSharedUri(String uri) {
        if (uri != null && !uri.isEmpty()) {
            return parserSingleUri(uri, getServersHash(), null, null, null, "Shared");
        }
        return null;
    }

    private Map<Long, ContentServer> getServersHash() {
        List<ContentServer> servers = contentServerProvider.listContentServers();
        Map<Long, ContentServer> cache = new HashMap<>();
        servers.forEach(item -> {
            cache.put(item.getId(), item);
        });
        return cache;
    }

    private String parserSingleUri(String uri, Map<Long, ContentServer> cache, String ownerType, Long ownerId, String token) {
        return parserSingleUri(uri, cache, ownerType, ownerId, token, null);
    }

    private String parserSingleUri(String uri, Map<Long, ContentServer> cache,
                                   String ownerType, Long ownerId, String token, String vendor) {
        if (StringUtils.isEmpty(uri)) {
            return null;
        }
        // added by Janson if UserContext.current().getScheme() == null
        // 如果uri本身已经是以http开头的完整链接，则不需要解释，直接返回（方便用一些测试链接）  by lqs 20160715
        uri = uri.trim();
        if (uri.startsWith(HTTP) || uri.startsWith(HTTPS)) {
            return uri;
        }
        if (!uri.contains("cs://")) {
            return uri;
        }

        uri = uri.replace("cs://", "");
        int position = uri.indexOf("/");
        if (position < 0) {
            LOGGER.error("invalid uri.cannot parser.");
            return "";
        }

        String serverIdStr = uri.substring(0, position);
        Long serverId = 0L;
        try {
            serverId = Long.valueOf(serverIdStr);
        } catch (NumberFormatException e) {
            LOGGER.error("cannot parser");
            return "";
        }
        uri = uri.substring(position + 1, uri.length());

        ContentServer server = cache.get(serverId);
        if (server == null) {
            return uri;
        }

        // https 默认端口443 by sfyan 20161226
        Integer port = server.getPublicPort();
        if (Objects.equals(getScheme(port), HTTPS)) {
            port = 443;
        }

        String scheme = getScheme(port);
        String publicAddress = server.getPublicAddress();

        Tuple<Integer, Integer> widthAndHeight = parseWidthAndHeight(uri);
        int width = widthAndHeight.first();
        int height = widthAndHeight.second();

        Map<String, Object> uriParams = new LinkedHashMap<>();
        uriParams.put("ownerType", ownerType);
        uriParams.put("ownerId", ownerId);
        uriParams.put("token", token);
        uriParams.put("pxw", width);
        uriParams.put("pxh", height);

        return ContentURLVendors.evaluateURL(scheme, publicAddress, port, uri, uriParams, vendor);
    }

    private Tuple<Integer, Integer> parseWidthAndHeight(String uri) {
        int width = 0;
        int height = 0;
        String metaData = null;
        try {
            String resourceId = uri;
            int position = resourceId.indexOf("/");
            if (position != -1) {
                resourceId = resourceId.substring(position + 1);
            }
            resourceId = Generator.decodeUrl(resourceId);
            ContentServerResource resource = contentServerProvider.findByResourceId(resourceId);
            if (resource != null) {
                metaData = resource.getMetadata();
                if (metaData != null && metaData.trim().length() > 0) {
                    HashMap map = (HashMap) StringHelper.fromJsonString(metaData, HashMap.class);
                    Object widthObj = map.get("width");
                    if (widthObj != null) {
                        width = Integer.parseInt(widthObj.toString());
                    }
                    Object heightObj = map.get("height");
                    if (heightObj != null) {
                        height = Integer.parseInt(heightObj.toString());
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to parse the width and height of resources, metaData=" + metaData + ", uri=" + uri, e);
        }
        return new Tuple<>(width, height);
    }

    @Override
    public ContentServerResource findResourceByUri(String uri) {
        if (StringUtils.isEmpty(uri)) {
            return null;
        }
        String result = uri.replace("cs://", "");
        int postion = result.indexOf("/");
        if (postion < 0) {
            LOGGER.error("cannot find any pattern resource");
            return null;
        }
        String[] arr = result.split("/");
        String resourceId = Generator.decodeUrl(arr[arr.length - 1]);
        return contentServerProvider.findByResourceId(resourceId);
    }

    @Override
    public ImageBody parserImageBody(String uri, String ownerType, Long ownerId) {
        if (StringUtils.isEmpty(uri)) {
            LOGGER.error("uri is null.");
            return null;
        }

        ImageBody imageBody = new ImageBody();
        ContentServerResource contentServerResource = this.findResourceByUri(uri);

        if (null == contentServerResource) {
            LOGGER.error("cannot find image uri.");
            return null;
        }
        imageBody.setUri(uri);
        String fileName = contentServerResource.getResourceName();
        imageBody.setFilename(fileName);
        String[] arr = fileName.split(".");
        String format = "image/jpeg";
        if (arr.length > 1) {
            format = "image/" + arr[1];
        }
        imageBody.setFormat(format);
        Integer fileSize = contentServerResource.getResourceSize();
        imageBody.setFileSize(Long.valueOf(fileSize));
        String meta = contentServerResource.getMetadata();
        if (null != meta) {
            Map<String, Object> m = (Map<String, Object>) StringHelper.fromJsonString(meta, Map.class);
            if (null != m) {
                if (null == m.get("height")) {
                    imageBody.setHeight(Integer.valueOf(m.get("height").toString()));
                }

                if (null == m.get("width")) {
                    imageBody.setWidth(Integer.valueOf(m.get("width").toString()));
                }
            }
        }

        String url = this.parserUri(uri, ownerType, ownerId);

        imageBody.setUrl(url);

        return imageBody;
    }

    private String getScheme() {
        return getScheme(null);
    }

    private String getScheme(Integer port) {
        //当后台执行任务的时候UserContext.current().getScheme() 为null，则需要根据数据库content server的配置来确定scheme  by sfyan 20170221
        if (null == UserContext.current().getScheme()) {
            if (null == port) {
                try {
                    ContentServer server = selectContentServer();
                    port = server.getPublicPort();

                } catch (Exception e) {
                    LOGGER.error("Get scheme. Failed to find content server", e);
                    return null;
                }
            }
            if (80 == port || 443 == port) {
                return HTTPS;
            } else {
                return HTTP;
            }
        } else {
            return UserContext.current().getScheme();
        }
    }

    @Override
    public String getContentServer() {
        try {
            ContentServer server = selectContentServer();

            // if (LOGGER.isDebugEnabled()) {
            //     LOGGER.debug("selectContentServer public address is: {}, public port is: {}", server.getPublicPort(), server.getPublicAddress());
            //     LOGGER.debug("current scheme is: {}", UserContext.current().getScheme());
            // }

            // https 默认端口443 by sfyan 20161226
            Integer port = server.getPublicPort();

            String scheme = getScheme(port);
            // if (LOGGER.isDebugEnabled()) {
            //     LOGGER.debug("getContentServer getScheme schema = {}", scheme);
            // }
            if (scheme.equals(HTTPS)) {
                port = 443;
            }
            return String.format("%s:%d", server.getPublicAddress(), port);
        } catch (Exception e) {
            LOGGER.error("Failed to find content server", e);
            return null;
        }
    }

    @Override
    public List<UploadCsFileResponse> uploadFileToContentServer(MultipartFile[] files) {
        String token = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
        List<UploadCsFileResponse> csFileResponseList = new ArrayList<UploadCsFileResponse>();
        for (MultipartFile file : files) {
            UploadCsFileResponse csFileResponse = null;
            InputStream fileStream = null;
            try {
                fileStream = file.getInputStream();
                csFileResponse = uploadFileToContentServer(fileStream, file.getOriginalFilename(), token);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Upload file to content server, contentType={}, fileName={}, orgFileName={}, csFile={}",
                            file.getContentType(), file.getName(), file.getOriginalFilename(), csFileResponse);
                }
            } catch (Exception e) {
                csFileResponse = new UploadCsFileResponse();
                // 错误码是在Content Server里统排，这里定一个特殊的错误码，标识不能处理的错误
                csFileResponse.setErrorCode(999999);
                csFileResponse.setErrorDescription(e.getMessage());
                LOGGER.error("Failed to upload file, contentType={}, fileName={}, orgFileName={}",
                        file.getContentType(), file.getName(), file.getOriginalFilename(), e);
            } finally {
//                Long size = file.getSize();
//                if(size < 1000) {
//                    csFileResponse.setSize(size + "B");
//                } else if(size > 1024 && size < 1024*1024) {
//                    csFileResponse.setSize(size/1024.0 + "KB");
//                } else if(size > 1024*1024 && size < 1024*1024*1024) {
//                    csFileResponse.setSize(size/(1024.0*1024) + "MB");
//                } else if(size > 1024*1024*1024) {
//                    csFileResponse.setSize(size/(1024.0*1024*1024) + "GB");
//                }
                if (fileStream != null) {
                    try {
                        fileStream.close();
                    } catch (Exception e) {
                        LOGGER.error("Failed to close the file stream, contentType={}, fileName={}, orgFileName={}",
                                file.getContentType(), file.getName(), file.getOriginalFilename(), e);
                    }
                }
            }

            csFileResponseList.add(csFileResponse);
        }

        return csFileResponseList;
    }

    @Override
    public UploadCsFileResponse uploadFileToContentServer(InputStream fileStream, String fileName, String token) {
        String fileSuffix = FilenameUtils.getExtension(fileName);

        // 通过文件后缀确定Content server中定义的媒体类型
        String mediaType = ContentMediaHelper.getContentMediaType(fileSuffix);
        if (ContentMediaHelper.UPLOAD_UNKNOW.equals(mediaType)) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Unsupported content media type for [%s], fix your file name extension.", fileName);
        }

        String serverHost = getServerHost();
        String url = String.format("%s://%s/upload/%s?token=%s", HTTP, serverHost, mediaType, token);
        HttpPost httpPost = new HttpPost(url);

        CloseableHttpResponse response = null;
        try {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("upload_file", fileStream,
                    ContentType.APPLICATION_OCTET_STREAM, URLEncoder.encode(fileName, "UTF-8"));
            HttpEntity multipart = builder.build();

            httpPost.setEntity(multipart);

            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity resEntity = response.getEntity();
                String responseBody = EntityUtils.toString(resEntity);

                UploadCsFileResponse csFileResponse = (UploadCsFileResponse) StringHelper.fromJsonString(responseBody, UploadCsFileResponse.class);
                if (csFileResponse != null) {
                    csFileResponse.setOriginalName(fileName);
                }
                return csFileResponse;
            } else {
                LOGGER.error("Failed to upload file, fileSuffix={}, status={}", fileSuffix, response.getStatusLine());
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                        "Failed to upload file");
            }
        } catch (Exception e) {
            LOGGER.error("Failed to upload file, fileSuffix={}", fileSuffix, e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Failed to upload file", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private String getServerHost() {
        String serverHost;
        try {
            ContentServer contentServer = selectContentServer();

            String address = contentServer.getPrivateAddress();
            Integer port = contentServer.getPrivatePort();
            if (org.springframework.util.StringUtils.isEmpty(address)) {
                address = contentServer.getPublicAddress();
                port = contentServer.getPublicPort();
            }
            serverHost = address + ":" + port;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return serverHost;
    }

    private void setUploadFileInfo() {
        String key = String.format("%d", 1);
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);

        Object v = redisTemplate.opsForValue().get(key);

        Long rv;
        if (v == null) {
            rv = 0l;
        } else {
            rv = Long.valueOf((String) v);
        }

        redisTemplate.opsForValue().set(key, String.valueOf(System.currentTimeMillis()));
    }

    @SuppressWarnings("unchecked")
    private boolean checkAndSetUploadId(String uuid) {
        String key = String.format(UPLOAD_FMT, uuid);
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);

        UploadFileInfoDTO dto = new UploadFileInfoDTO();
        dto.setUploadId(uuid);

        Boolean ok = redisTemplate.opsForValue().setIfAbsent(key, StringHelper.toJsonString(dto));

        if (ok) {
            redisTemplate.expire(key, 120l, TimeUnit.SECONDS);
        }

        return ok;
    }

    @Override
    public String newUploadId() {
        for (int i = 0; i < 5; i++) {
            String uuid = UUID.randomUUID().toString();
            uuid = "evhUploader-" + uuid.replace("-", "");
            boolean ok = checkAndSetUploadId(uuid);
            if (ok) {
                return uuid;
            }
        }

        throw RuntimeErrorException.errorWith(ContentServerErrorCode.SCOPE,
                ContentServerErrorCode.ERROR_INVALID_UUID, "invalid uuid");

    }

    private UploadFileInfoDTO getFileDTO(String uploadId) {
        String key = String.format(UPLOAD_FMT, uploadId);
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj == null) {
            return null;
        }
        String str = (String) obj;
        UploadFileInfoDTO dto = (UploadFileInfoDTO) StringHelper.fromJsonString(str, UploadFileInfoDTO.class);
        return dto;
    }

    @Override
    public DeferredResult<RestResponse> waitScan(String uploadId) {
        DeferredResult<RestResponse> deferredResult = new DeferredResult<>();
        RestResponse response = new RestResponse();
        String subject = "upload_scan." + uploadId;
        int scanTimeout = 30 * 1000;
        UploadFileInfoDTO dto = getFileDTO(uploadId);
        if (dto == null) {
            response.setResponseObject("uuid timeout");
            response.setErrorCode(ContentServerErrorCode.ERROR_INVALID_UUID);
            deferredResult.setResult(response);
            return deferredResult;
        }

        //dto 只有一个线程能成功，如果某一个线程失败，则需要重新请求 uuid

        localBusSubscriberBuilder.build(subject, new LocalBusOneshotSubscriber() {
            @Override
            public Action onLocalBusMessage(Object sender, String subject,
                                            Object dtoResp, String path) {
                response.setResponseObject(StringHelper.fromJsonString((String) dtoResp, UploadFileInfoDTO.class));
                deferredResult.setResult(response);
                return null;
            }

            @Override
            public void onLocalBusListeningTimeout() {
                //wait again
                response.setResponseObject("continue");
                response.setErrorCode(200);
                deferredResult.setResult(response);
            }

        }).setTimeout(scanTimeout).create();

        return deferredResult;
    }

    private UploadFileInfoDTO signalFileDTO(UploadFileInfoCommand cmd, Long userId) {
        String key = String.format(UPLOAD_FMT, cmd.getUploadId());
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj == null) {
            return null;
        }
        String str = (String) obj;
        UploadFileInfoDTO dto = (UploadFileInfoDTO) StringHelper.fromJsonString(str, UploadFileInfoDTO.class);

        if (dto.getUserId() != null && !dto.getUserId().equals(userId)) {
            throw RuntimeErrorException.errorWith(ContentServerErrorCode.SCOPE,
                    ContentServerErrorCode.ERROR_INVALID_UUID, "invalid uuid");
        }

        if (dto.getUserId() != null) {
            return dto;
        }

        dto.setContentServer(cmd.getContentServer());
        dto.setFileExtension(cmd.getFileExtension());
        dto.setInfos(cmd.getInfos());
        dto.setLimitCount(cmd.getLimitCount());
        dto.setLimitPerSize(cmd.getLimitPerSize());
        dto.setUserId(userId);
        dto.setUserToken(cmd.getUserToken());
        dto.setTitle(cmd.getTitle());
        dto.setReadOnly(cmd.getReadOnly());

        obj = redisTemplate.opsForValue().getAndSet(key, StringHelper.toJsonString(dto));
        if (obj != null) {
            str = (String) obj;
            UploadFileInfoDTO dto2 = (UploadFileInfoDTO) StringHelper.fromJsonString(str, UploadFileInfoDTO.class);
            if (dto2.getUserId() != null && !dto.getUserId().equals(dto2.getUserId())) {
                //double check, changed by others, throw failed
                throw RuntimeErrorException.errorWith(ContentServerErrorCode.SCOPE,
                        ContentServerErrorCode.ERROR_INVALID_UUID, "invalid uuid");
            }

        }

        //timeout at 10 minutes
        redisTemplate.expire(key, 10 * 60, TimeUnit.SECONDS);

        return dto;
    }

    @Override
    public String signalScanEvent(UploadFileInfoCommand cmd) {
        String subject = "upload_scan." + cmd.getUploadId();
        UploadFileInfoDTO dto = signalFileDTO(cmd, UserContext.currentUserId());

        ExecutorUtil.submit(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LocalBusSubscriber localBusSubscriber = (LocalBusSubscriber) busBridgeProvider;
                    localBusSubscriber.onLocalBusMessage(null, subject, StringHelper.toJsonString(dto), null);
                } catch (Exception e) {
                    LOGGER.error("submit LocalBusSubscriber failed, subject=" + subject, e);
                }

                try {
                    localBus.publish(null, subject, StringHelper.toJsonString(dto));
                } catch (Exception e) {
                    LOGGER.error("submit localBus failed, subject=" + subject, e);
                }

            }
        }, "subscriberUpload"));

        return "ok";
    }

    @Override
    public DeferredResult<RestResponse> waitComplete(String uploadId) {
        DeferredResult<RestResponse> deferredResult = new DeferredResult<>();
        RestResponse response = new RestResponse();
        String subject = "upload_complete." + uploadId;
        int scanTimeout = 30 * 1000;
        UploadFileInfoDTO dto = getFileDTO(uploadId);
        if (dto == null) {
            response.setResponseObject("uuid timeout");
            response.setErrorCode(ContentServerErrorCode.ERROR_INVALID_UUID);
            deferredResult.setResult(response);
            return deferredResult;
        }

        if (dto.getComplete() != null && dto.getComplete() > 0) {
            //already complete
            response.setResponseObject(dto);
            deferredResult.setResult(response);
            return deferredResult;
        }

        localBusSubscriberBuilder.build(subject, new LocalBusOneshotSubscriber() {
            @Override
            public Action onLocalBusMessage(Object sender, String subject,
                                            Object dtoResp, String path) {
                response.setResponseObject(dtoResp);
                deferredResult.setResult(response);
                return null;
            }

            @Override
            public void onLocalBusListeningTimeout() {
                //wait again
                response.setResponseObject("continue");
                response.setErrorCode(200);
                deferredResult.setResult(response);
            }

        }).setTimeout(scanTimeout).create();

        return deferredResult;
    }

    @Override
    public UploadFileInfoDTO queryUploadResult(String uploadId) {
        String subject = "upload_complete." + uploadId;

        //mark as complete
        String key = String.format(UPLOAD_FMT, uploadId);
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj == null) {
            throw RuntimeErrorException.errorWith(ContentServerErrorCode.SCOPE,
                    ContentServerErrorCode.ERROR_INVALID_UUID, "invalid uuid");
        }
        String str = (String) obj;
        UploadFileInfoDTO dto = (UploadFileInfoDTO) StringHelper.fromJsonString(str, UploadFileInfoDTO.class);
        if (dto.getComplete() != null && dto.getComplete() > 0) {
            return dto;
        }

        if (dto.getInfos() != null) {
            for (UploadFileInfo info : dto.getInfos()) {
                if (info.getUri() != null && !info.getUri().isEmpty() && info.getUrl() != null && info.getUrl().isEmpty()) {
                    info.setUrl(this.parserUri(info.getUri()));
                }

            }
        }

        dto.setComplete(1l);
        redisTemplate.opsForValue().set(key, StringHelper.toJsonString(dto), 120, TimeUnit.SECONDS);

        ExecutorUtil.submit(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LocalBusSubscriber localBusSubscriber = (LocalBusSubscriber) busBridgeProvider;
                    localBusSubscriber.onLocalBusMessage(null, subject, "OK", null);
                } catch (Exception e) {
                    LOGGER.error("submit LocalBusSubscriber failed, subject=" + subject, e);
                }

                try {
                    localBus.publish(null, subject, "OK");
                } catch (Exception e) {
                    LOGGER.error("submit localBus failed, subject=" + subject, e);
                }

            }
        }, "subscriberUploadComplete"));

        return dto;
    }

    @Override
    public String updateUploadInfo(UploadFileInfoCommand cmd) {
        Long userId = UserContext.currentUserId();
        String key = String.format(UPLOAD_FMT, cmd.getUploadId());
        Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj == null) {
            throw RuntimeErrorException.errorWith(ContentServerErrorCode.SCOPE,
                    ContentServerErrorCode.ERROR_INVALID_UUID, "invalid uuid");
        }
        String str = (String) obj;
        UploadFileInfoDTO dto = (UploadFileInfoDTO) StringHelper.fromJsonString(str, UploadFileInfoDTO.class);

        if (dto.getUserId() != null && !dto.getUserId().equals(userId)) {
            throw RuntimeErrorException.errorWith(ContentServerErrorCode.SCOPE,
                    ContentServerErrorCode.ERROR_INVALID_UUID, "invalid user");
        }

        if (dto.getComplete() != null && dto.getComplete() > 0) {
            throw RuntimeErrorException.errorWith(ContentServerErrorCode.SCOPE,
                    ContentServerErrorCode.ERROR_INVALID_UUID, "already complete");
        }

//        dto.setContentServer(cmd.getContentServer());
        dto.setFileExtension(cmd.getFileExtension());
        dto.setInfos(cmd.getInfos());
        dto.setLimitCount(cmd.getLimitCount());
        dto.setLimitPerSize(cmd.getLimitPerSize());
        dto.setTitle(cmd.getTitle());
//        dto.setUserId(userId);
//        dto.setUserToken(cmd.getUserToken());

        obj = redisTemplate.opsForValue().getAndSet(key, StringHelper.toJsonString(dto));
        if (obj != null) {
            str = (String) obj;
            UploadFileInfoDTO dto2 = (UploadFileInfoDTO) StringHelper.fromJsonString(str, UploadFileInfoDTO.class);
            if (!dto2.getUserId().equals(dto.getUserId())) {
                //double check, changed by others, throw failed
                throw RuntimeErrorException.errorWith(ContentServerErrorCode.SCOPE,
                        ContentServerErrorCode.ERROR_INVALID_UUID, "invalid uuid");
            }

        }

        //timeout at 10 minutes
        redisTemplate.expire(key, 10 * 60, TimeUnit.SECONDS);

        return "ok";
    }


    @Override
    public CsFileLocationDTO uploadFileByUrl(String fileName, String url) {
        InputStream inputStream = getInputStreamByGet(url);
        if(inputStream == null){
            return null;
        }

        UserLogin userLogin = User.SYSTEM_USER_LOGIN;
        String token = WebTokenGenerator.getInstance().toWebToken(userLogin.getLoginToken());
        UploadCsFileResponse re = uploadFileToContentServer(inputStream, fileName, token);

        if(re != null){
            return re.getResponse();
        }
        return null;
    }


    private InputStream getInputStreamByGet(String url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url)
                    .openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                return inputStream;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
