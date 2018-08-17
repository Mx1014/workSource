package com.everhomes.scriptengine.dummy;

import com.everhomes.gogs.GogsRepo;
import com.everhomes.scriptengine.GogsTransferNashornScript;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.Map;
import java.util.concurrent.LinkedTransferQueue;

public class GogsScriptTransferDummy extends GogsTransferNashornScript<Object> {

    private final static String FUNCTION_NAME = "main";

    private Map<String, Object> param;

    public GogsScriptTransferDummy(LinkedTransferQueue<Object> queue, Map<String, Object> param) {
        super(queue);
        this.param = param;
    }

    @Override
    protected Object processInternal(ScriptObjectMirror objectMirror) {
        return objectMirror.callMember(FUNCTION_NAME, getArgs());
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
