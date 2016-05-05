package com.everhomes.contentserver;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import com.everhomes.bus.LocalBusOneshotSubscriber;
import com.everhomes.bus.LocalBusOneshotSubscriberBuilder;
import com.everhomes.contentserver.ConfigResponse;
import com.everhomes.rest.contentserver.ContentServerErrorCode;
import com.everhomes.rest.contentserver.WebSocketConstant;
import com.everhomes.rest.rpc.PduFrame;
import com.everhomes.util.RuntimeErrorException;

@Component
public class ConnectionProviderImpl implements ConnectionProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionProviderImpl.class);

    private Map<String, WebSocketSession> sessionCache;

    private Map<String, WebSocketHandler> handlers;

    @Autowired
    private ContentServerProvider contentServerProvider;

    @Autowired
    private LocalBusOneshotSubscriberBuilder subscriber;

    @PostConstruct
    public void init() {
        sessionCache = new ConcurrentHashMap<String, WebSocketSession>();
        handlers = new ConcurrentHashMap<String, WebSocketHandler>();
        List<ContentServer> result = contentServerProvider.listContentServers();
        result.forEach(item -> {
            try {
                if (StringUtils.isEmpty(item.getPrivateAddress())) {
                    connect(item.getPublicAddress(), item.getPublicPort());
                    return;
                }
                connect(item.getPrivateAddress(), item.getPrivatePort());
            } catch (Exception e) {
                LOGGER.error("handle error message",e);
            }
        });
    }

    @Override
    public void connect(String host, int port) throws Exception {
        String key = String.format("%s:%d", host, port);
        if (sessionCache.containsKey(key)) {
            return;
        }
        handlePing(key);
    }

    private void handlePing(String key) throws Exception {
        LOGGER.info("do handShake with server.host={}", key);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        StandardWebSocketClient wsClient = new StandardWebSocketClient(container);
        WebSocketHttpHeaders wsHeaders = new WebSocketHttpHeaders();
        wsClient.setTaskExecutor(new SimpleAsyncTaskExecutor());

        URI uri = new URI(String.format("ws://%s/ws", key));

        WebSocketHandler handler = null;
        if (!handlers.containsKey(key)) {
            handler = createHandler();
            handlers.put(key, handler);
        }
        // create connection
        WebSocketSession session = wsClient.doHandshake(handlers.get(key), wsHeaders, uri).get();
        sessionCache.put(String.format("%s", key), session);
        LOGGER.info("Conennt to server.......uri={}", uri);
    }

    @Override
    public void sendMessage(String name, Object payLoad, String host, int port) throws Exception {
        String key = String.format("%s:%d", host, port);
        if (null == sessionCache.get(key)) {
            connect(host, port);
            LOGGER.info("call connection before");
        }
        PduFrame frame = Generator.createPduFrame(name, payLoad, 0);
        sessionCache.get(key).sendMessage(new TextMessage(frame.toJson()));

    }

    @Override
    public void sendMessage(WebSocketSession session, String name, Object payLoad) throws IOException {
        PduFrame frame = Generator.createPduFrame(name, payLoad, 0);
        // WebSocket的session不是线程安全的，需要加锁 by lqs20160503
        synchronized(session) {
            session.sendMessage(new TextMessage(frame.toJson()));
        }
    }

    @Override
    public void configServers(PduFrame item) {
        if (sessionCache.isEmpty()) {
            LOGGER.error("cannot find any nodes");
            return;
        }
        sessionCache.values().parallelStream().forEach(
            session -> {
                try {
                    // WebSocket的session不是线程安全的，需要加锁 by lqs20160503
                    synchronized(session) {
                        session.sendMessage(new TextMessage(item.toJson()));
                    }
                } catch (Exception e) {
                    LOGGER.error("send message error", e);
                    throw RuntimeErrorException.errorWith(ContentServerErrorCode.SCOPE,ContentServerErrorCode.ERROR_INVALID_SERVER,"cannot connect to content server");
                }
            });
        List<ConfigResponse> rsp = new ArrayList<>();
        subscriber.build("config.contentstorage." + item.getRequestId(), new LocalBusOneshotSubscriber() {

            @Override
            public Action onLocalBusMessage(Object arg0, String arg1, Object arg2, String arg3) {
                ConfigResponse response = (ConfigResponse) arg2;
                rsp.add(response);
                return null;
            }

            @Override
            public void onLocalBusListeningTimeout() {
                ConfigResponse response = new ConfigResponse();
                response.setErrCode(-1);
                rsp.add(response);
            }
        }).setTimeout(5000).create();
        rsp.forEach(r -> {
            if (r.getErrCode() == -1) {
                LOGGER.error("the server is inavlid");
                throw RuntimeErrorException.errorWith(ContentServerErrorCode.SCOPE,
                        ContentServerErrorCode.ERROR_INVALID_SERVER, "the server in cannot use status");
            }
        });
    }

    @Override
    public void removeSession(String host, int port) {
        String key = String.format("%s:%d", host, port);
        if (sessionCache.containsKey(key)) {
            sessionCache.remove(key);
            handlers.remove(key);
        }
    }

    @Override
    public void cleanAll() {
        sessionCache.values().forEach(session -> {
            try {
                LOGGER.error("destory the connections");
                session.close();
            } catch (Exception e) {
                LOGGER.error("cannot close the connection.skip", e);
            }
        });
        sessionCache.clear();
        handlers.clear();
    }

    private WebSocketHandler createHandler() {
        return new ContentServerHandler(session -> {
            sessionCache.forEach((key, value) -> {
                if (session.equals(value)) {
                    try {
                        value.close();
                    } catch (Exception e) {
                        LOGGER.error("close error");
                    }
                    handlers.remove(key);
                    sessionCache.remove(key);
                }
            });
        });
    }

    @Scheduled(fixedDelay = 5000)
    public void heartbeat() {
        if (sessionCache.isEmpty()) {
            return;
        }
        sessionCache.forEach((key, session) -> {
            try {
                // WebSocket的session不是线程安全的，需要加锁 by lqs20160503
                synchronized(this) {
                    session.sendMessage(new PingMessage(ByteBuffer.allocate(0x9)));
                }
            } catch (Exception e) {
                LOGGER.error("send message error.do handShake again.", e);
                try {
                    handlePing(key);
                } catch (Exception e1) {
                    LOGGER.error("error message", e1);
                    return;
                }
            }
        });
    }

    @Override
    public ContentServer chooseContentServer(List<ContentServer> servers, Long uid) {
        List<QueryCapacityResponse> ret = new ArrayList<>();
        subscriber.build(WebSocketConstant.CONTENT_SERVER_CAPACITY_RSP, new LocalBusOneshotSubscriber() {

            @Override
            public Action onLocalBusMessage(Object paramObject1, String paramString1, Object paramObject2,
                    String paramString2) {
                LOGGER.debug(
                        "handle rsp message from server node.paramObject1={},paramString1={},paramObject2={},paramString2={}",
                        new Object[] { paramObject1, paramString1, paramObject2, paramString2 });
                ret.add((QueryCapacityResponse) paramObject2);
                return null;
            }

            @Override
            public void onLocalBusListeningTimeout() {
                LOGGER.error("handle msg timeout");
            }

        }).setTimeout(5000).create();
        Map<Long, ContentServer> cache = new HashMap<Long, ContentServer>();
        servers.parallelStream().map(node -> 
                        {
                    try {
                        cache.put(node.getId(), node);
                        String address = node.getPrivateAddress();
                        int port = node.getPrivatePort();
                        if (StringUtils.isEmpty(address)) {
                            address = node.getPublicAddress();
                            port = node.getPublicPort();
                        }
                        sendMessage(WebSocketConstant.CONTENT_STORAGE_REQ, new QueryCapacityRequest(), address, port);
                        return null;
                    } catch (Exception e) {
                        LOGGER.error("Handle error message", e);
                        throw RuntimeErrorException.errorWith(ContentServerErrorCode.SCOPE,
                                ContentServerErrorCode.ERROR_INVALID_SESSION, "cannot send message");
                    }
                });
        int maxCapacity = 0;
        QueryCapacityResponse response = null;
        for (QueryCapacityResponse rsp : ret) {
            if (maxCapacity < rsp.getCapacity()) {
                maxCapacity = rsp.getCapacity();
                response = rsp;
            }
        }
        return cache.get(response.getNodeId());
    }
}
