package com.everhomes.scriptengine;

import com.everhomes.gogs.GogsService;
import com.everhomes.scriptengine.nashorn.NashornEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class ScriptEngineServiceImpl implements ScriptEngineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptEngineServiceImpl.class);

    @Autowired
    private NashornEngineService nashornEngineService;

    @Autowired
    private ScriptEngineService scriptEngineService;

    @Autowired
    private GogsService gogsService;

    @Override
    public void push(GogsNashornScript script) {
        Assert.notNull(script, "script should be not null");
        // 目前只支持 js 引擎
        nashornEngineService.push(script);
    }

    @Override
    public <T> T poll(BlockingQueue<T> queue) {
        return poll(queue, 60);
    }

    @Override
    public <T> T poll(BlockingQueue<T> queue, Integer timeoutSeconds) {
        T result = null;
        try {
            result = queue.poll(timeoutSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("poll from queue error", e);
        }
        return result;
    }
}
