package com.everhomes.flow.nashornfunc;

import com.everhomes.flow.FlowRuntimeScript;
import com.everhomes.flow.NashornEngineService;
import com.everhomes.flow.NashornScript;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.concurrent.atomic.AtomicBoolean;

public class NashornScriptMapping implements NashornScript<ScriptObjectMirror> {

    private final static String FUNCTION_NAME = "mapping";

    private FlowRuntimeScript flowScript;

    private AtomicBoolean finished = new AtomicBoolean(false);

    public NashornScriptMapping(FlowRuntimeScript flowScript) {
        this.flowScript = flowScript;
    }

    @Override
    public void onError(Exception ex) {
        if (finished.get()) {
            return;
        }
        finished.set(true);
    }

    @Override
    public ScriptObjectMirror process(NashornEngineService input) {
        ScriptObjectMirror mirror = input.getScriptObjectMirror(flowScript.getScriptMainId(), flowScript.getScriptVersion(), this);
        if (mirror == null) {
            throw new RuntimeException("Could not found script to eval, scriptMainId = "
                    + flowScript.getScriptMainId() + ", scriptVersion = " + flowScript.getScriptVersion());
        }
        return (ScriptObjectMirror) mirror.getMember(getJSFunc());
    }

    private Object[] getArgs() {
        return new Object[0];
    }

    @Override
    public void onComplete(ScriptObjectMirror out) {
        if (finished.get()) {
            return;
        }
        finished.set(true);
    }

    @Override
    public String getJSFunc() {
        return FUNCTION_NAME;
    }

    @Override
    public String getScript() {
        return flowScript.getScript();
    }
}
