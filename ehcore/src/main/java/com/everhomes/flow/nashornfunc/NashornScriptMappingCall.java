package com.everhomes.flow.nashornfunc;

import com.everhomes.flow.FlowRuntimeScript;
import com.everhomes.scriptengine.nashorn.NashornEngineService;
import com.everhomes.scriptengine.nashorn.NashornScript;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.Undefined;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class NashornScriptMappingCall implements NashornScript<Object> {

    private String funcName;
    private NashornScriptMapping mapping;

    private Map<String, String[]> paramMap;
    private String requestBody;
    private DeferredResult<Object> deferredResult;

    private AtomicBoolean finished = new AtomicBoolean(false);

    public NashornScriptMappingCall(FlowRuntimeScript flowScript, String funcName,
                                    Map<String, String[]> paramMap, String requestBody, DeferredResult<Object> dr) {
        this.mapping = new NashornScriptMapping(flowScript);
        this.funcName = funcName;
        this.paramMap = paramMap;
        this.requestBody = requestBody;
        this.deferredResult = dr;
    }

    @Override
    public void onError(Exception ex) {
        if (finished.getAndSet(true)) {
            return;
        }
        deferredResult.setResult(ex.getMessage());
    }

    @Override
    public Object process(NashornEngineService input) {
        ScriptObjectMirror objectMirror = mapping.process(input);
        if (objectMirror != null) {
            return objectMirror.callMember(getJSFunc(), getArgs());
        }
        return null;
    }

    private Object[] getArgs() {
        return new Object[]{paramMap, requestBody};
    }

    @Override
    public void onComplete(Object out) {
        if (finished.getAndSet(true)) {
            return;
        }
        if (out instanceof Undefined) {
            deferredResult.setResult(null);
        } else {
            deferredResult.setResult(out);
        }
    }

    public String getJSFunc() {
        return funcName;
    }

    @Override
    public String getScript() {
        return mapping.getScript();
    }
}
