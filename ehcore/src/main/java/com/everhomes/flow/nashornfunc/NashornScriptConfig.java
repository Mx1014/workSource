package com.everhomes.flow.nashornfunc;

import com.everhomes.flow.FlowRuntimeScript;
import com.everhomes.scriptengine.nashorn.NashornEngineService;
import com.everhomes.scriptengine.nashorn.NashornScript;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class NashornScriptConfig implements NashornScript<ScriptObjectMirror> {

    private final static String FUNCTION_NAME = "config";

    private FlowRuntimeScript script;

    private Consumer<ScriptObjectMirror> consumer;

    private AtomicBoolean finished = new AtomicBoolean(false);

    public NashornScriptConfig(FlowRuntimeScript script, Consumer<ScriptObjectMirror> consumer) {
        this.script = script;
        this.consumer = consumer;
    }

    @Override
    public void onError(Exception ex) {
        if (consumer != null) {
            consumer.accept(null);
        }
    }

    @Override
    public ScriptObjectMirror process(NashornEngineService input) {
        if (finished.get()) {
            return null;
        }
        String key = String.format("%s:%s", script.getScriptMainId(), script.getScriptVersion());
        ScriptObjectMirror mirror = input.getScriptObjectMirror(key, this);
        if (mirror == null) {
            throw new RuntimeException("Could not found script to eval, scriptMainId = "
                    + script.getScriptMainId() + ", scriptVersion = " + script.getScriptVersion());
        }

        Object member = mirror.getMember(getJSFunc());
        if (member != null) {
            return ((ScriptObjectMirror) member);
        }
        return null;
    }

    private Object[] getArgs() {
        // not use
        return new Object[0];
    }

    @Override
    public void onComplete(ScriptObjectMirror out) {
        if (finished.get()) {
            return;
        }
        finished.set(true);
        if (consumer != null) {
            consumer.accept(out);
        }
    }

    @Override
    public String getScript() {
        return script.getScript();
    }

    public String getJSFunc() {
        return FUNCTION_NAME;
    }
}
