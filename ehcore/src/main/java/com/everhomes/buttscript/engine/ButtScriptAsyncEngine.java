package com.everhomes.buttscript.engine;


import com.everhomes.bus.LocalEvent;
import com.everhomes.gogs.GogsRepo;
import com.everhomes.rest.user.UserDTO;
import com.everhomes.scriptengine.GogsAsyncNashornScript;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.Map;

/**
 * 异步的脚本调用
 */
public class ButtScriptAsyncEngine extends GogsAsyncNashornScript {

    private final static String FUNCTION_NAME = "main";
    /**
     * 入参
     */
    private Map<String, Object> param;

    /**
     * 脚本入参
     */
    private ButtScriptParameter buttparam ;



    public ButtScriptAsyncEngine(Map<String, Object> param) {
        this.param = param;
    }

    /**
     *
     * @param param 固定入参
     * @param buttparam  脚本入参
     */
    public ButtScriptAsyncEngine(Map<String, Object> param, ButtScriptParameter buttparam) {
        this.param = param;
        this.buttparam = buttparam ;
    }

    @Override
    protected void processAsync(ScriptObjectMirror objectMirror) {
        objectMirror.callMember(FUNCTION_NAME, getArgs());
    }
    private Object[] getArgs() {
        return new Object[]{buttparam};
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
