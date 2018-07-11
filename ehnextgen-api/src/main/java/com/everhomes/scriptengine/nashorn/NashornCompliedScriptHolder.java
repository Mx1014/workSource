package com.everhomes.scriptengine.nashorn;

import com.everhomes.flow.FlowScriptCompileException;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.collections4.map.LRUMap;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.Map;

public class NashornCompliedScriptHolder {

    private ScriptEngine scriptEngine;

    private Map<String, ScriptObjectMirror> scriptObjectMirrorMap;

    public NashornCompliedScriptHolder() {
        scriptObjectMirrorMap = new LRUMap<>(1000);
    }

    public ScriptEngine getScriptEngine() {
        return scriptEngine;
    }

    public void compile(String key, String script) {
        try {
            CompiledScript compiledScript = getCompilable().compile(script);

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

    public ScriptObjectMirror getScriptObjectMirror(String key) {
        return scriptObjectMirrorMap.get(key);
    }
}
