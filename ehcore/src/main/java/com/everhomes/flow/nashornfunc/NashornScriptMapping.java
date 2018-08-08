package com.everhomes.flow.nashornfunc;

import com.everhomes.flow.FlowRuntimeScript;
import com.everhomes.scriptengine.nashorn.NashornEngineService;
import com.everhomes.scriptengine.nashorn.NashornScript;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.concurrent.atomic.AtomicBoolean;

public class NashornScriptMapping implements NashornScript<ScriptObjectMirror> {

    private final static String FUNCTION_NAME = "mapping";

    private FlowRuntimeScript script;

    private AtomicBoolean finished = new AtomicBoolean(false);

    public NashornScriptMapping(FlowRuntimeScript script) {
        this.script = script;
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
        String key = String.format("%s:%s", script.getScriptMainId(), script.getScriptVersion());
        ScriptObjectMirror mirror = input.getScriptObjectMirror(key, this);
        if (mirror == null) {
            throw new RuntimeException("Could not found script to eval, scriptMainId = "
                    + script.getScriptMainId() + ", scriptVersion = " + script.getScriptVersion());
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

    public String getJSFunc() {
        return FUNCTION_NAME;
    }

    @Override
    public String getScript() {
        return script.getScript();
    }
}
