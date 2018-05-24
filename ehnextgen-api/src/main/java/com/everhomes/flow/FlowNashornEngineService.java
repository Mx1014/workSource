package com.everhomes.flow;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public interface FlowNashornEngineService {

    void start();

    void stop();

    void push(NashornScript obj);

    CompliedScriptHolder getCompliedScriptHolder();

    void compileScript(Long scriptMainId, Integer scriptVersion);

    ScriptObjectMirror getScriptObjectMirror(Long scriptMainId, Integer scriptVersion);
}
