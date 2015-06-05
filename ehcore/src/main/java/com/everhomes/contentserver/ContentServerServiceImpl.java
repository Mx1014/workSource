package com.everhomes.contentserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.contentserver.WebSocketConstant;
import com.everhomes.rpc.PduFrame;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;

@Component
public class ContentServerServiceImpl implements ContentServerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentServerServiceImpl.class);

    @Autowired
    private ConnectionProvider connectionProvider;

    @Autowired
    private ContentServerProvider contentServerProvider;

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
        String token = UserContext.current().getLogin().getLoginToken().getTokenString();
        return uris.stream().map(r -> parserSingleUri(r, cache, ownerType, ownerId, token))
                .collect(Collectors.toList());
    }

    @Override
    public String parserUri(String uri, String ownerType, Long ownerId) {
        Map<Long, ContentServer> cache = getServersHash();
        String token = UserContext.current().getLogin().getLoginToken().getTokenString();
        return parserSingleUri(uri, cache, ownerType, ownerId, token);
    }

    private Map<Long, ContentServer> getServersHash() {
        List<ContentServer> servers = contentServerProvider.listContentServers();
        Map<Long, ContentServer> cache = new HashMap<>();
        servers.forEach(item -> {
            cache.put(item.getId(), item);
        });
        return cache;
    }

    private static String parserSingleUri(String uri, Map<Long, ContentServer> cache, String ownerType, Long ownerId,
            String token) {
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
        if (uri.indexOf("?") != -1) {
            return String.format("http://%s:%d/%s&ownerType=%s&ownerId=%s&token=%s", cache.get(serverId)
                    .getPublicAddress(), cache.get(serverId).getPublicPort(), uri, ownerType, ownerId, token);
        }
        return String.format("http://%s:%d/%s?ownerType=%s&ownerId=%s&token=%s",
                cache.get(serverId).getPublicAddress(), cache.get(serverId).getPublicPort(), uri, ownerType, ownerId,
                token);
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
}
