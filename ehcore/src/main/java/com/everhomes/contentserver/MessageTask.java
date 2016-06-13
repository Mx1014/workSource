package com.everhomes.contentserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everhomes.rest.contentserver.WebSocketConstant;

public abstract class MessageTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageTask.class);
    private ContentServerMananger contentServerManager;

    public void setContentServerMannager(ContentServerMananger fileHandlerProvider) {
        this.contentServerManager = fileHandlerProvider;
    }

    @Override
    public void run() {
        try {
            // if (LOGGER.isDebugEnabled())
            //    LOGGER.debug("handle message from content server.do deal with message");
            doRequest(contentServerManager);
            //if (LOGGER.isDebugEnabled())
            //    LOGGER.debug("send reply to content server");
            doResponse("handle message ok", WebSocketConstant.OK_CODE);
        } catch (Throwable e) {
            LOGGER.error("error message", e);
            try {
                doResponse(e.getMessage(), WebSocketConstant.ERR_CODE);
            } catch (Exception e1) {
                LOGGER.error("error message", e);
            }
        }
    }

    // handle request message
    protected abstract void doRequest(ContentServerMananger contentServermanager) throws Exception;

    // send response message
    protected abstract void doResponse(String errMsg, int errCode) throws Exception;

}
