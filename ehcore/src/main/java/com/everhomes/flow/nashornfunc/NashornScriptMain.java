package com.everhomes.flow.nashornfunc;

import com.everhomes.flow.*;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.function.Consumer;

public class NashornScriptMain implements NashornScript<Void> {

    private final static String FUNCTION_NAME = "main";

    private FlowCaseStateBrief ctx;
    private FlowRuntimeScript script;

    public NashornScriptMain(FlowCaseState state, FlowRuntimeScript script, FlowAction flowAction) {
        this.ctx = new FlowCaseStateBrief();

        ifNotNull(state.getCurrentLane(), o -> ctx.setCurrentLane(o.getFlowLane()));
        ifNotNull(state.getCurrentNode(), o -> ctx.setCurrentNode(o.getFlowNode()));
        ifNotNull(state.getFlowGraph(), o -> ctx.setFlow(o.getFlow()));
        ifNotNull(state.getNextNode(), o -> ctx.setNextNode(o.getFlowNode()));
        ifNotNull(state.getPrefixNode(), o -> ctx.setPrefixNode(o.getFlowNode()));
        ifNotNull(state.getCurrentEvent(), o -> ctx.setSubject(o.getSubject()));
        ifNotNull(state.getCurrentEvent(), o -> ctx.setFiredButtonId(o.getFiredButtonId()));

        ctx.setModule(state.getModule());
        ctx.setExtra(state.getExtra());
        ctx.setOperator(state.getOperator());
        ctx.setStepType(state.getStepType());
        ctx.setFlowCase(state.getFlowCase());
        ctx.setAction(flowAction);

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
    public Void process(NashornEngineService input) {
        ScriptObjectMirror mirror = input.getScriptObjectMirror(script.getScriptMainId(), script.getScriptVersion(), this);
        if (mirror == null) {
            throw new RuntimeException("Could not found script to eval, scriptMainId = "
                    + script.getScriptMainId() + ", scriptVersion = " + script.getScriptVersion());
        }
        mirror.callMember(getJSFunc(), getArgs());
        return null;
    }

    private Object[] getArgs() {
        return new Object[]{ctx};
    }

    @Override
    public void onComplete(Void out) {

    }

    @Override
    public String getJSFunc() {
        return FUNCTION_NAME;
    }

    @Override
    public String getScript() {
        return script.getScript();
    }
}
