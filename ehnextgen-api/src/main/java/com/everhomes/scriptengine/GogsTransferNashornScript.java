package com.everhomes.scriptengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

public abstract class GogsTransferNashornScript<O> extends GogsNashornScript<O> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GogsTransferNashornScript.class);

    private LinkedTransferQueue<O> queue;

    public GogsTransferNashornScript(LinkedTransferQueue<O> queue) {
        this.queue = queue;
    }

    @Override
    public void onComplete(O out) {
        try {
            if (queue != null) {
                queue.tryTransfer(out, 10, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            LOGGER.error("transfer queue error", e);
        }
    }

    @Override
    public void onError(Exception ex) {
        try {
            if (queue != null) {
                queue.tryTransfer(null, 10, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            LOGGER.error("transfer queue error", e);
        }
    }
}