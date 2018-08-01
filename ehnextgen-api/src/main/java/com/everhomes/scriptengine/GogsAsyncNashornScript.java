package com.everhomes.scriptengine;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public abstract class GogsAsyncNashornScript<Void> extends GogsNashornScript<Void> {

    @Override
    protected Void processInternal(ScriptObjectMirror objectMirror) {
        processAsync(objectMirror);
        return null;
    }

    @Override
    public void onComplete(Void out) {

    }

    @Override
    public void onError(Exception ex) {

    }

    abstract protected void processAsync(ScriptObjectMirror objectMirror);
}