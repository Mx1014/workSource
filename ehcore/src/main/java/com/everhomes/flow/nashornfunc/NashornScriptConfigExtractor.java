package com.everhomes.flow.nashornfunc;

import com.everhomes.flow.FlowRuntimeScript;
import com.everhomes.scriptengine.nashorn.NashornEngineService;
import com.everhomes.scriptengine.nashorn.NashornScript;
import com.everhomes.rest.flow.FlowScriptConfigInfo;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

public class NashornScriptConfigExtractor implements NashornScript<List<FlowScriptConfigInfo>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NashornScriptConfigExtractor.class);

    private NashornScriptConfig scriptConfig;
    private LinkedTransferQueue<List<FlowScriptConfigInfo>> queue;

    public NashornScriptConfigExtractor(FlowRuntimeScript script,
                                        LinkedTransferQueue<List<FlowScriptConfigInfo>> queue) {
        this.scriptConfig = new NashornScriptConfig(script, null);
        this.queue = queue;
    }

    public String getJSFunc() {
        return scriptConfig.getJSFunc();
    }

    @Override
    public String getScript() {
        return scriptConfig.getScript();
    }

    @Override
    public List<FlowScriptConfigInfo> process(NashornEngineService input) {
        ScriptObjectMirror configMirror = scriptConfig.process(input);

        List<FlowScriptConfigInfo> configInfos = new ArrayList<>();
        if (configMirror == null) {
            return configInfos;
        }

        configMirror.forEach((k, v) -> {
            FlowScriptConfigInfo info = new FlowScriptConfigInfo();
            info.setFieldName(k);

            if (v instanceof Map) {
                Map vv = (Map) v;
                info.setFieldDesc(toStr(vv.get("description")));
                info.setFieldValue(toStr(vv.get("default")));
            }
            configInfos.add(info);
        });
        return configInfos;
    }

    private String toStr(Object o) {
        return o != null ? String.valueOf(o) : "";
    }

    @Override
    public void onComplete(List<FlowScriptConfigInfo> out) {
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
                queue.tryTransfer(new ArrayList<>(), 10, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            LOGGER.error("transfer queue error", e);
        }
    }
}
