package com.everhomes.flow.nashornfunc;

import com.everhomes.flow.NashornEngineService;
import com.everhomes.flow.FlowScript;
import com.everhomes.flow.NashornScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

public class NashornScriptValidator implements NashornScript<Boolean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NashornScriptValidator.class);

    private LinkedTransferQueue<Boolean> queue;
    private FlowScript script;

    public NashornScriptValidator(FlowScript script, LinkedTransferQueue<Boolean> queue) {
        this.script = script;
        this.queue = queue;
    }

    @Override
    public String getJSFunc() {
        return "";
    }

    @Override
    public String getScript() {
        return script.getScript();
    }

    @Override
    public Boolean process(NashornEngineService input) {
        // 编译
        input.getCompliedScriptHolder().compile(script.getScriptMainId(), script.getScriptVersion(), script.getScript());
        return Boolean.TRUE;
    }

    @Override
    public void onComplete(Boolean out) {
        try {
            if (queue != null) {
                queue.tryTransfer(Boolean.TRUE, 10, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            LOGGER.error("transfer queue error", e);
        }
    }

    @Override
    public void onError(Exception ex) {
        try {
            if (queue != null) {
                queue.tryTransfer(Boolean.FALSE, 10, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            LOGGER.error("transfer queue error", e);
        }
    }
}
