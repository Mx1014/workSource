package com.everhomes.scriptengine.dummy;

import com.everhomes.gogs.GogsRepo;
import com.everhomes.scriptengine.GogsAsyncNashornScript;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.Map;

public class GogsScriptAsyncDummy extends GogsAsyncNashornScript {

    private final static String FUNCTION_NAME = "main";

    private Map<String, Object> param;

    public GogsScriptAsyncDummy(Map<String, Object> param) {
        this.param = param;
    }

    @Override
    protected void processAsync(ScriptObjectMirror objectMirror) {
        objectMirror.callMember(FUNCTION_NAME, getArgs());
    }

    private Object[] getArgs() {
        return new Object[0];
    }

    @Override
    protected GogsRepo getGogsRepo() {
        return (GogsRepo) param.get("gogsRepo");
    }

    @Override
    protected String getScriptPath() {
        return String.valueOf(param.get("scriptPath"));
    }

    @Override
    protected String getLastCommit() {
        return String.valueOf(param.get("lastCommit"));
    }
}
