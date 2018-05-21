package com.everhomes.flow.nashornfunc;

import com.everhomes.flow.FlowNashornEngineService;
import com.everhomes.flow.FlowScript;
import com.everhomes.flow.NashornScript;
import com.everhomes.util.StringHelper;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.jooq.impl.DSL;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class NashornScriptConfig implements NashornScript<FlowNashornEngineService, ScriptObjectMirror> {

    private final static String FUNCTION_NAME = "config";

    private FlowScript script;

    private Consumer<ScriptObjectMirror> consumer;

    private AtomicBoolean finished = new AtomicBoolean(false);

    public NashornScriptConfig(FlowScript script, Consumer<ScriptObjectMirror> consumer) {
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
    public ScriptObjectMirror process(FlowNashornEngineService input) {
        if (finished.get()) {
            return null;
        }
        ScriptObjectMirror mirror = input.getScriptObjectMirror(script.getScriptMainId(), script.getScriptVersion());
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
    public FlowScript getScript() {
        return script;
    }

    @Override
    public String getJSFunc() {
        return FUNCTION_NAME;
    }
}
