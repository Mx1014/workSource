package com.everhomes.contentserver;

import java.util.ArrayList;
import java.util.List;
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
            throw RuntimeErrorException.errorWith(ContentServerErrorCode.SCOPE,ContentServerErrorCode.ERROR_INVALID_SERVER,"invalid content server");
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
    public void addConfigItem(AddConfigItemCommand cmd){
        LOGGER.info("Invoke addConfigItem.Item={}", cmd);
        PduFrame frame = Generator.createPduFrame(
                String.format("%s%s.%s", WebSocketConstant.CONTENT_CONFIG_REQ, cmd.getConfigType().toLowerCase(),
                        cmd.getConfigName()), cmd, 0);
        connectionProvider.configServers(frame);
    }

    @Override
    public void removeConfigItem(String itemName){
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

    @Override
    @Cacheable(value = "selectContentServer", key = "#uid")
    public ContentServer selectContentServer(Long uid) throws Exception {
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
        return connectionProvider.chooseContentServer(servers, uid);
    }

    @Override
    public List<String> rebuildUrl(Long uid, List<String> urls, String configName) {
        List<String> reHashUrl = new ArrayList<String>();
        urls.forEach(url -> {
            reHashUrl.add(String.format("%s/%s", url, configName));
        });
        // http://localhost:30000/image/<objectId>/<configName>?token=<token>
        return reHashUrl;
    }
}
