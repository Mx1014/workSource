package com.everhomes.buttscript.engine;

import com.everhomes.bus.LocalEvent;
import com.everhomes.gogs.GogsRepo;
import com.everhomes.rest.user.UserDTO;
import com.everhomes.scriptengine.GogsTransferNashornScript;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.Map;
import java.util.concurrent.LinkedTransferQueue;

/**
 *
 * 同步的脚本调用
 */
public class ButtScriptTransferEngine  extends GogsTransferNashornScript<Object> {

    private final static String FUNCTION_NAME = "main";
    private Map<String, Object> param;

    /**
     * 脚本入参
     */
    private ButtScriptParameter buttparam ;


    public ButtScriptTransferEngine(LinkedTransferQueue<Object> queue, Map<String, Object> param) {
        super(queue);
        this.param = param;
    }

    /**
     *
     * @param queue
     * @param param
     *@param buttparam  脚本入参
     */
    public ButtScriptTransferEngine(LinkedTransferQueue<Object> queue, Map<String, Object> param, ButtScriptParameter buttparam) {
        super(queue);
        this.param = param;
        this.buttparam =  buttparam;
    }
    @Override
    protected Object processInternal(ScriptObjectMirror objectMirror) {
        return objectMirror.callMember(FUNCTION_NAME, getArgs());
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
