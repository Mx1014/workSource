package com.everhomes.scriptengine;

import java.util.concurrent.BlockingQueue;

public interface ScriptEngineService {

    void push(GogsNashornScript script);

    <T> T poll(BlockingQueue<T> queue);

    <T> T poll(BlockingQueue<T> queue, Integer timeoutSeconds);
}
