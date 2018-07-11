package com.everhomes.scriptengine.nashorn;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public interface NashornEngineService {

    void start();

    void stop();

    void push(NashornScript<?> script);

    ScriptObjectMirror getScriptObjectMirror(String key, NashornScript<?> script);
}
