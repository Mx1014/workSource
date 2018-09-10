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
     * 事件的触发者
     */
    private UserDTO operator ;

    /**
     * 事件对象本身
     */
    private LocalEvent  event ;


    public ButtScriptTransferEngine(LinkedTransferQueue<Object> queue, Map<String, Object> param) {
        super(queue);
        this.param = param;
    }

    /**
     *
     * @param queue
     * @param param
     *@param operator  事件的触发者
     * @param event   事件对象本身
     */
    public ButtScriptTransferEngine(LinkedTransferQueue<Object> queue, Map<String, Object> param, UserDTO operator , LocalEvent event) {
        super(queue);
        this.param = param;
        this.operator =  operator;
        this.event = event;
    }
    @Override
    protected Object processInternal(ScriptObjectMirror objectMirror) {
        return objectMirror.callMember(FUNCTION_NAME, getArgs());
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
