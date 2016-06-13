package com.everhomes.contentserver;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.everhomes.contentserver.ConfigResponse;
import com.everhomes.rest.contentserver.WebSocketConstant;
import com.everhomes.rest.rpc.PduFrame;

public class ContentServerHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentServerHandler.class);

    private WebSocketCallback callback;

    private MessageQueue proxy;

    private WebSocketMessageSubscriber subscriber;

    public ContentServerHandler(WebSocketCallback callback) {
        this.callback = callback;
        subscriber = new WebSocketMessageSubscriber();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        LOGGER.info("connect to server,host={}", session.getRemoteAddress());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            proxy=MessageQueue.getInstance();
            proxy.subscriber("contentstorage.", subscriber);
        } catch (Exception e) {
            LOGGER.error("subscriber error");
            return;
        }
        //LOGGER.info("handle text message from content server.payload={}", message.getPayload());
        PduFrame frame = PduFrame.fromJson(message.getPayload());
        if (StringUtils.isEmpty(frame.getName())) {
            LOGGER.info("unknown message type.frame name is empty");
            return;
        }
        if (StringUtils.contains(frame.getName(), WebSocketConstant.CONTENT_STORAGE_REQ)) {
            proxy.publish(session, "contentstorage." + frame.getRequestId(), frame);
            return;
        }
        if (StringUtils.contains(frame.getName(), WebSocketConstant.CONTENT_CONFIG_RSP)) {

            handleResponse(session, frame.getName(), frame);
            return;
        }
        LOGGER.error("handle unknown message.frame={}",
                ToStringBuilder.reflectionToString(frame, ToStringStyle.MULTI_LINE_STYLE));
    }

    private void handleResponse(WebSocketSession session, String message, PduFrame frame) {
        ConfigResponse payLoad = frame.getPayload(ConfigResponse.class);
        proxy.publish(null, "config.contentstorage." + frame.getRequestId(), payLoad);
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        if (LOGGER.isDebugEnabled())
            super.handlePongMessage(session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        LOGGER.error("Handle message error", exception);
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        LOGGER.error("close the connections {}.close status {}", session.getRemoteAddress(), status.getCode());
        callback.onClose(session);
    }

}
