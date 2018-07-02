package com.everhomes.flow;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.collections4.map.LRUMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.*;
import java.util.Map;

public class CompliedScriptHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompliedScriptHolder.class);

    private ScriptEngine scriptEngine;

    // private Map<String, CompiledScript> compiledScriptMap;
    private Map<String, ScriptObjectMirror> scriptObjectMirrorMap;

    public CompliedScriptHolder() {
        // compiledScriptMap = new LRUMap<>(1000);
        scriptObjectMirrorMap = new LRUMap<>(1000);
    }

    public ScriptEngine getScriptEngine() {
        return scriptEngine;
    }

    public void compile(Long scriptMainId, Integer scriptVersion, String script) {
        try {
            String key = String.format("%s:%s", scriptMainId, scriptVersion);

            CompiledScript compiledScript = getCompilable().compile(script);
            // compiledScriptMap.put(key, compiledScript);

            Object eval = compiledScript.eval();
            if (eval == null) {
                return;
            }
            ScriptObjectMirror mirror = (ScriptObjectMirror) eval;
            scriptObjectMirrorMap.put(key, mirror);
        } catch (ScriptException e) {
            throw new FlowScriptCompileException("compile script error", e);
        }
    }

    private Compilable getCompilable() {
        return (Compilable) scriptEngine;
    }

    public void setScriptEngine(ScriptEngine scriptEngine) {
        this.scriptEngine = scriptEngine;
    }

    public ScriptObjectMirror getScriptObjectMirror(Long scriptMainId, Integer scriptVersion) {
        return scriptObjectMirrorMap.get(String.format("%s:%s", scriptMainId, scriptVersion));
    }
}
