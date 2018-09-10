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
     * 事件的触发者
     */
    private UserDTO operator ;

    /**
     * 事件对象本身
     */
    private LocalEvent  event ;

    public ButtScriptAsyncEngine(Map<String, Object> param) {
        this.param = param;
    }

    /**
     *
     * @param param 固定入参
     * @param operator  事件的触发者
     * @param event   事件对象本身
     */
    public ButtScriptAsyncEngine(Map<String, Object> param, UserDTO operator ,LocalEvent event) {
        this.param = param;
        this.operator =  operator;
        this.event = event;
    }

    @Override
    protected void processAsync(ScriptObjectMirror objectMirror) {
        objectMirror.callMember(FUNCTION_NAME, getArgs());
    }
    private Object[] getArgs() {
        return new Object[]{operator,event};
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
