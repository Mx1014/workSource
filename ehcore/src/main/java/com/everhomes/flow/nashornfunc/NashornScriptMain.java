package com.everhomes.flow.nashornfunc;

import com.everhomes.flow.*;
import com.everhomes.rest.flow.FlowCaseBriefDTO;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

public class NashornScriptMain implements NashornScript<FlowNashornEngineService, Void> {

    private final static String FUNCTION_NAME = "main";

    private FlowCaseStateBrief ctx;
    private FlowScript script;

    private AtomicBoolean finished = new AtomicBoolean(false);

    public NashornScriptMain(FlowCaseState state, FlowScript script) {
        this.ctx = new FlowCaseStateBrief();

        ifNotNull(state.getCurrentLane(), o -> ctx.setCurrentLane(o.getFlowLane()));
        ifNotNull(state.getCurrentNode(), o -> ctx.setCurrentNode(o.getFlowNode()));
        ifNotNull(state.getFlowGraph(), o -> ctx.setFlow(o.getFlow()));
        ifNotNull(state.getNextNode(), o -> ctx.setNextNode(o.getFlowNode()));
        ifNotNull(state.getPrefixNode(), o -> ctx.setPrefixNode(o.getFlowNode()));
        ifNotNull(state.getCurrentEvent(), o -> ctx.setSubject(o.getSubject()));

        ctx.setModule(state.getModule());
        ctx.setExtra(state.getExtra());
        ctx.setOperator(state.getOperator());
        ctx.setStepType(state.getStepType());
        ctx.setFlowCase(state.getFlowCase());

        this.script = script;
    }

    private <T> void ifNotNull(T t, Consumer<T> consumer) {
        if (t != null) {
            consumer.accept(t);
        }
    }

    @Override
    public void onError(Exception ex) {

    }

    @Override
    public Void process(FlowNashornEngineService input) {
        if (finished.getAndSet(true)) {
            return null;
        }
        ScriptObjectMirror mirror = input.getScriptObjectMirror(script.getScriptMainId(), script.getScriptVersion());
        if (mirror == null) {
            throw new RuntimeException("Could not found script to eval, scriptMainId = "
                    + script.getScriptMainId() + ", scriptVersion = " + script.getScriptVersion());
        }

        // mirror.put("ctx", ctx);
        mirror.callMember(getJSFunc(), getArgs());
        return null;
    }

    private Object[] getArgs() {
        // not use
        return new Object[]{ctx};
    }

    @Override
    public void onComplete(Void out) {
        if (finished.get()) {
            return;
        }
        finished.set(true);
    }

    @Override
    public String getJSFunc() {
        return FUNCTION_NAME;
    }

    @Override
    public FlowScript getScript() {
        return script;
    }
}
