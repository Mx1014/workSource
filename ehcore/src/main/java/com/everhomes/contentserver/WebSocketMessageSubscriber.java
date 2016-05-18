package com.everhomes.contentserver;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.everhomes.bus.LocalBusOneshotSubscriber;
import com.everhomes.contentserver.MessageHandleRequest;
import com.everhomes.contentserver.MessageHandleResponse;
import com.everhomes.rest.contentserver.WebSocketConstant;
import com.everhomes.rest.rpc.PduFrame;

public class WebSocketMessageSubscriber implements LocalBusOneshotSubscriber {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketMessageSubscriber.class);

    private WebSocketSession session;

    private MessageQueue taskPool = MessageQueue.getInstance();

    @Override
    public Action onLocalBusMessage(Object paramObject1, String paramString1, Object paramObject2, String paramString2) {
        session = (WebSocketSession) paramObject1;
        PduFrame request = (PduFrame) paramObject2;
        handleRequest(request);
        return null;
    }

    @Override
    public void onLocalBusListeningTimeout() {
        LOGGER.error("handle msg timeout");
    }

    private void handleRequest(PduFrame frame) {
        invokeRequestMessage(session, frame.getName(), frame);
    }

    private void invokeRequestMessage(WebSocketSession session, String message, PduFrame frame) {
        String[] arr = message.split(WebSocketConstant.CONTENT_STORAGE_REQ)[1].split("\\.");
        if (arr.length < 2) {
            throw new IllegalArgumentException("cannot regonize message");
        }
        MessageHandleRequest request = frame.getPayload(MessageHandleRequest.class);
        request.setAccessType(AccessType.fromStringCode(StringUtils.trim(arr[1])));

        // LOGGER.info("Request message={}", request);
        switch (request.getMessageType()) {
        case UPLOADED:
            LOGGER.info("content url(before handleUpload), url=" + request.getUrl());
            handleUpload(session, request, frame);
            LOGGER.info("content url(after handleUpload), url=" + request.getUrl());
            return;
        case LOOKUP:
            // skip
            LOGGER.error("handle message type error");
            return;
        case DELETE:
            handleRemove(session, request, frame);
            return;
        case AUTH:
            handleAuth(session, request, frame);
            return;
        case UNKOWN:
        default:
            return;
        }
    }

    private void handleAuth(WebSocketSession session, MessageHandleRequest request, PduFrame frame) {
        taskPool.submitTask(new MessageTask() {

            @Override
            protected void doRequest(ContentServerMananger contentServerManager) throws Exception {
                contentServerManager.auth(request);
            }

            @Override
            protected void doResponse(String errMsg, int errCode) throws Exception {
                // WebSocket的session不是线程安全的，需要加锁 by lqs20160503
                synchronized(session) {
                    session.sendMessage(new TextMessage(createPduFrame(errCode, errMsg, frame, request).toJson()));
                }
            }
        });
    }

    private void handleUpload(WebSocketSession session, MessageHandleRequest request, PduFrame frame) {
        String objectId = Generator.createRandomKey();
        request.setObjectId(objectId);
        taskPool.submitTask(new MessageTask() {

            @Override
            protected void doRequest(ContentServerMananger contentServerManager) throws Exception {
                LOGGER.info("content url(before handleUpload doRequest), url=" + request.getUrl());
                contentServerManager.upload(request);
                LOGGER.info("content url(after handleUpload doRequest), url=" + request.getUrl());

            }

            @Override
            protected void doResponse(String errMsg, int errCode) throws Exception {
                // if (LOGGER.isDebugEnabled())
                //    LOGGER.debug("send upload success to server");
                //LOGGER.info("content url(before handleUpload doResponse), url=" + request.getUrl());
                PduFrame pdu = createPduFrame(errCode, errMsg, frame, request);
                // WebSocket的session不是线程安全的，需要加锁 by lqs20160503
                synchronized(session) {
                    session.sendMessage(new TextMessage(pdu.toJson()));
                }
                // LOGGER.info("content url(after handleUpload doResponse), url=" + request.getUrl());
                // LOGGER.info("content url(after handleUpload doResponse), pdu=" + pdu.toJson());
                
            }
        });
    }

    private void handleRemove(WebSocketSession session, MessageHandleRequest request, PduFrame frame) {
        taskPool.submitTask(new MessageTask() {

            @Override
            protected void doRequest(ContentServerMananger contentServerManager) throws Exception {
                contentServerManager.delete(request);
            }

            @Override
            protected void doResponse(String errMsg, int errCode) throws Exception {
                // WebSocket的session不是线程安全的，需要加锁 by lqs20160503
                synchronized(session) {
                    session.sendMessage(new TextMessage(createPduFrame(errCode, errMsg, frame, request).toJson()));
                }
            }
        });
    }

    // create pdu frame
    private PduFrame createPduFrame(int errCode, String errMsg, PduFrame oldFrame, MessageHandleRequest request) {
        MessageHandleResponse rsp = new MessageHandleResponse();
        BeanUtils.copyProperties(request, rsp);
        rsp.setErrCode(errCode);
        rsp.setErrMsg(errMsg);
        rsp.setObjectId(request.getObjectId());

        //LOGGER.info("content url(before createPduFrame), url=" + rsp.getUrl());

        //if (LOGGER.isDebugEnabled())
        //    LOGGER.debug("send response message to content server responese={}", rsp);
        PduFrame pdu = new PduFrame();
        pdu.setAppId(oldFrame.getAppId());
        pdu.setName(oldFrame.getName().replace("request", "response"));
        pdu.setVersion(oldFrame.getVersion());
        pdu.setRequestId(oldFrame.getRequestId());
        pdu.setPayload(rsp);
        return pdu;
    }

}
