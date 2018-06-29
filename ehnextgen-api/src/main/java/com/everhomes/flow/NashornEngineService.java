package com.everhomes.flow;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public interface NashornEngineService {

    void start();

    void stop();

    void push(NashornScript<?> script);

    ScriptObjectMirror getScriptObjectMirror(Long scriptMainId, Integer scriptVersion, NashornScript<?> script);
}
