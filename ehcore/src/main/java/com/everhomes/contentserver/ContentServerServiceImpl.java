package com.everhomes.contentserver;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.contentserver.*;
import com.everhomes.rest.contentserver.WebSocketConstant;
import com.everhomes.rest.messaging.ImageBody;
import com.everhomes.rest.rpc.PduFrame;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import com.everhomes.util.WebTokenGenerator;
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
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ContentServerServiceImpl implements ContentServerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentServerServiceImpl.class);

    @Autowired
    private ConnectionProvider connectionProvider;

    @Autowired
    private ContentServerProvider contentServerProvider;
    
    private CloseableHttpClient httpClient;

    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    
    @PostConstruct
    protected void init() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        this.httpClient = httpClientBuilder.build();
    }
    
    @PreDestroy
    protected void clean() {
        if(httpClient != null) {
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

    @Cacheable(value = "selectContentServer",unless="#result==null")
    @Override
    public ContentServer selectContentServer() throws Exception {
        LOGGER.info("Enter select content server");
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
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("rebuild url");
        }
        if (CollectionUtils.isEmpty(uris)) {
            return new ArrayList<String>();
        }
        Map<Long, ContentServer> cache = getServersHash();
        final String token[] = new String[1];
        if(UserContext.current().getLogin() != null) 
            token[0] = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
        return uris.stream().map(r -> parserSingleUri(r, cache, ownerType, ownerId, token[0]))
                .collect(Collectors.toList());
    }

    @Override
    public String parserUri(String uri, String ownerType, Long ownerId) {
        Map<Long, ContentServer> cache = getServersHash();
        String token =null;
        if(UserContext.current().getLogin() != null)
            token = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
        return parserSingleUri(uri, cache, ownerType, ownerId, token);
    }
    
    @Override
    public String parserUri(String uri) {
    	if (uri != null && !uri.isEmpty()) {
			try {
				return parserUri(uri, null, null);
			} catch (Exception e) {
			}
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

    private String parserSingleUri(String uri, Map<Long, ContentServer> cache, String ownerType, Long ownerId,
            String token) {
        if(StringUtils.isEmpty(uri)){
            return null;
        }

        //added by Janson if UserContext.current().getScheme() == null 
        // 如果uri本身已经是以http开头的完整链接，则不需要解释，直接返回（方便用一些测试链接）  by lqs 20160715
        uri = uri.trim();
        if(uri.startsWith(HTTP) || uri.startsWith(HTTPS)) {
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
        String server = uri.substring(0, position);
        Long serverId = 0L;
        try {
            serverId = Long.valueOf(server);
        } catch (NumberFormatException e) {
            LOGGER.error("cannot parser");
            return "";
        }
        uri = uri.substring(position + 1, uri.length());
        if(cache.get(serverId)==null){
            return uri;
        }
//        if (uri.indexOf("?") != -1) {
//            return String.format("http://%s:%d/%s&ownerType=%s&ownerId=%s&token=%s", cache.get(serverId)
//                    .getPublicAddress(), cache.get(serverId).getPublicPort(), uri, ownerType, ownerId, token);
//        }
        
        int width = 0;
        int height = 0;
        String metaData = null;
        try {
            String resourceId = uri;
            position = resourceId.indexOf("/");
            if(position != -1) {
                resourceId = resourceId.substring(position + 1);
            }
            resourceId = Generator.decodeUrl(resourceId);
            if(resourceId != null) {
                ContentServerResource resource = contentServerProvider.findByResourceId(resourceId);
                if(resource != null) {
                    metaData = resource.getMetadata();
                    if(metaData != null && metaData.trim().length() > 0) {
                        HashMap map = (HashMap)StringHelper.fromJsonString(metaData, HashMap.class);
                        Object widthObj = map.get("width");
                        if(widthObj != null) {
                            width = Integer.parseInt(widthObj.toString());
                        }
                        Object heightObj = map.get("height");
                        if(heightObj != null) {
                            height = Integer.parseInt(heightObj.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to parse the width and height of resources, owenerType=" + ownerType
                + ", ownerId=" + ownerId + ", metaData=" + metaData + ", uri=" + uri, e);
        }

        // https 默认端口443 by sfyan 20161226
        Integer port = cache.get(serverId).getPublicPort();
        if(getScheme(port).equals(HTTPS)){
            port = 443;
        }

        return String.format("%s://%s:%d/%s?ownerType=%s&ownerId=%s&token=%s&pxw=%d&pxh=%d",
                getScheme(port), cache.get(serverId).getPublicAddress(), port, uri, ownerType, ownerId,
                token, width, height);
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
    public ImageBody parserImageBody(String uri, String ownerType, Long ownerId){
    	if (StringUtils.isEmpty(uri)) {
    		LOGGER.error("uri is null.");
            return null;
        }
    	
    	ImageBody imageBody = new ImageBody();
		ContentServerResource contentServerResource = this.findResourceByUri(uri);
		
		if(null == contentServerResource){
			LOGGER.error("cannot find image uri.");
			return null;
		}
		imageBody.setUri(uri);
		String fileName = contentServerResource.getResourceName();
		imageBody.setFilename(fileName);
		String[] arr = fileName.split(".");
		String format = "image/jpeg";
		if(arr.length > 1){
			format = "image/" + arr[1];
		}
		imageBody.setFormat(format);
		Integer fileSize = contentServerResource.getResourceSize();
		imageBody.setFileSize(Long.valueOf(fileSize));
		String meta = contentServerResource.getMetadata();
		if(null != meta){
			Map<String, Object> m = (Map<String, Object>) StringHelper.fromJsonString(meta, Map.class);
			if(null != m){
				if(null == m.get("height")){
					imageBody.setHeight(Integer.valueOf(m.get("height").toString()));
				}
				
				if(null == m.get("width")){
					imageBody.setWidth(Integer.valueOf(m.get("width").toString()));
				}
			}
		}
		
		String url = this.parserUri(uri, ownerType, ownerId);
		
		imageBody.setUrl(url);
		
		return imageBody;
    }

    private String getScheme(){
        return getScheme(null);
    }

    private String getScheme(Integer port){
        //当后台执行任务的时候UserContext.current().getScheme() 为null，则需要根据数据库content server的配置来确定scheme  by sfyan 20170221
        if(null == UserContext.current().getScheme()){
            if(null == port){
                try {
                    ContentServer server = selectContentServer();
                    port = server.getPublicPort();

                } catch (Exception e) {
                    LOGGER.error("Get scheme. Failed to find content server", e);
                    return null;
                }
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("getScheme using port = {}", port);
            }
            if(80 == port || 443 == port){
                return HTTPS;
            }else{
                return HTTP;
            }
        }else{
            return UserContext.current().getScheme();
        }
    }

    @Override
    public String getContentServer(){
        try {
            ContentServer server = selectContentServer();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("selectContentServer public address is: {}, public port is: {}", server.getPublicPort(), server.getPublicAddress());
                LOGGER.debug("current scheme is: {}", UserContext.current().getScheme());
            }

            // https 默认端口443 by sfyan 20161226
            Integer port = server.getPublicPort();

            String scheme = getScheme(port);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("getContentServer getScheme schema = {}", scheme);
            }
            if(scheme.equals(HTTPS)){
                port = 443;
            }
            return String.format("%s:%d",server.getPublicAddress(),port);
        } catch (Exception e) {
            LOGGER.error("Failed to find content server", e);
            return null;
        }
    }
    
    @Override
    public List<UploadCsFileResponse> uploadFileToContentServer(MultipartFile[] files) {
        String token = WebTokenGenerator.getInstance().toWebToken(UserContext.current().getLogin().getLoginToken());
        List<UploadCsFileResponse> csFileResponseList = new ArrayList<UploadCsFileResponse>();
        for(MultipartFile file : files) {
            UploadCsFileResponse csFileResponse = null;
            InputStream fileStream = null;
            try {
                fileStream = file.getInputStream();
                csFileResponse = uploadFileToContentServer(fileStream, file.getOriginalFilename(), token);
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Upload file to content server, contentType={}, fileName={}, orgFileName={}, csFile={}", 
                        file.getContentType(), file.getName(), file.getOriginalFilename(), csFileResponse);
                }
            } catch(Exception e) {
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
                if(fileStream != null) {
                    try {
                        fileStream.close();
                    } catch(Exception e) {
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
        String contentServerUri = getContentServer();
        String fileSuffix = FilenameUtils.getExtension(fileName);
        
        // 通过文件后缀确定Content server中定义的媒体类型
        String mediaType = ContentMediaHelper.getContentMediaType(fileSuffix);

        // https 默认端口443 by sfyan 20161226
        String url = String.format("%s://%s/upload/%s?token=%s",getScheme(), contentServerUri, mediaType, token);
        HttpPost httpPost = new HttpPost(url);
        
        CloseableHttpResponse response = null;
        try {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("upload_file", fileStream,
                    ContentType.APPLICATION_OCTET_STREAM, fileSuffix);
            HttpEntity multipart = builder.build();

            httpPost.setEntity(multipart);
            
            response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == 200) {
                HttpEntity resEntity = response.getEntity();
                String responseBody = EntityUtils.toString(resEntity);
                
                UploadCsFileResponse csFileResponse = (UploadCsFileResponse)StringHelper.fromJsonString(responseBody, UploadCsFileResponse.class);
                if(csFileResponse != null) {
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
            if(response != null) {
                try {
                    response.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            
        }
    }
}
