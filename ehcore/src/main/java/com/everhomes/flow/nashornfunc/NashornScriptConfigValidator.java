package com.everhomes.flow.nashornfunc;

import com.everhomes.flow.FlowRuntimeScript;
import com.everhomes.flow.NashornEngineService;
import com.everhomes.flow.NashornScript;
import com.everhomes.rest.flow.FlowScriptConfigInfo;
import com.everhomes.rest.flow.FlowScriptConfigValidateResult;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

public class NashornScriptConfigValidator implements NashornScript<List<FlowScriptConfigValidateResult>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NashornScriptConfigValidator.class);

    private NashornScriptConfig scriptConfig;
    private List<FlowScriptConfigInfo> configs;

    private LinkedTransferQueue<List<FlowScriptConfigValidateResult>> queue;

    public NashornScriptConfigValidator(FlowRuntimeScript script,
                                        List<FlowScriptConfigInfo> configs,
                                        LinkedTransferQueue<List<FlowScriptConfigValidateResult>> queue) {

        this.scriptConfig = new NashornScriptConfig(script, null);
        this.configs = configs;
        this.queue = queue;
    }

    @Override
    public String getJSFunc() {
        return scriptConfig.getJSFunc();
    }

    @Override
    public String getScript() {
        return scriptConfig.getScript();
    }

    @Override
    public List<FlowScriptConfigValidateResult> process(NashornEngineService input) {
        ScriptObjectMirror configMirror = scriptConfig.process(input);

        List<FlowScriptConfigValidateResult> resultList = new ArrayList<>();

        for (FlowScriptConfigInfo config : configs) {
            Object field = configMirror.get(config.getFieldName());
            if (field == null) {
                continue;
            }

            FlowScriptConfigValidateResult result = new FlowScriptConfigValidateResult();
            result.setPass(Boolean.TRUE);
            result.setFieldName(config.getFieldName());
            resultList.add(result);

            if (field instanceof ScriptObjectMirror) {
                ScriptObjectMirror fieldMirror = (ScriptObjectMirror) field;
                Object validator = fieldMirror.get("validator");
                if (validator instanceof ScriptObjectMirror) {
                    try {
                        Object ret = ((ScriptObjectMirror) validator).call(validator, config.getFieldValue());
                        if (ret != null && !Boolean.TRUE.toString().equals(ret.toString())) {
                            result.setPass(Boolean.FALSE);
                            result.setDescription(ret.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return resultList;
    }

    @Override
    public void onComplete(List<FlowScriptConfigValidateResult> out) {
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
