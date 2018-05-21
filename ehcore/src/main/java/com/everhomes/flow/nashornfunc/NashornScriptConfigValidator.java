package com.everhomes.flow.nashornfunc;

import com.everhomes.flow.FlowNashornEngineService;
import com.everhomes.flow.FlowScript;
import com.everhomes.flow.NashornScript;
import com.everhomes.rest.flow.FlowScriptConfigInfo;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

public class NashornScriptConfigValidator implements NashornScript<FlowNashornEngineService, Map<String, Boolean>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NashornScriptConfigValidator.class);

    private NashornScriptConfig scriptConfig;
    private List<FlowScriptConfigInfo> configs;

    private LinkedTransferQueue<Map<String, Boolean>> queue;

    public NashornScriptConfigValidator(FlowScript script,
                                        List<FlowScriptConfigInfo> configs,
                                        LinkedTransferQueue<Map<String, Boolean>> queue) {

        this.scriptConfig = new NashornScriptConfig(script, null);
        this.configs = configs;
        this.queue = queue;
    }

    @Override
    public String getJSFunc() {
        return scriptConfig.getJSFunc();
    }

    @Override
    public FlowScript getScript() {
        return scriptConfig.getScript();
    }

    @Override
    public Map<String, Boolean> process(FlowNashornEngineService input) {
        ScriptObjectMirror configMirror = scriptConfig.process(input);

        Map<String, Boolean> resultMap = new HashMap<>();
        for (FlowScriptConfigInfo config : configs) {
            Object field = configMirror.get(config.getFieldName());
            if (field == null) {
                continue;
            }
            resultMap.put(config.getFieldName(), true);

            if (field instanceof ScriptObjectMirror) {
                ScriptObjectMirror fieldMirror = (ScriptObjectMirror) field;
                Object validator = fieldMirror.get("validator");
                if (validator instanceof ScriptObjectMirror) {
                    try {
                        Object ret = ((ScriptObjectMirror) validator).call(validator, config.getFieldValue());
                        resultMap.put(config.getFieldName(), Boolean.valueOf(String.valueOf(ret)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return resultMap;
    }

    @Override
    public void onComplete(Map<String, Boolean> out) {
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
                queue.tryTransfer(new HashMap<>(), 10, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            LOGGER.error("transfer queue error", e);
        }
    }
}
