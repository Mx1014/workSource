package com.everhomes.flow;

public interface NashornScript<I, O> {

    String getJSFunc();

    FlowScript getScript();

    O process(I input);

    void onComplete(O out);

    void onError(Exception ex);
}
