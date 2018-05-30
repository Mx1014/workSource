package com.everhomes.flow;

public interface NashornScript<O> {

    String getJSFunc();

    String getScript();

    O process(NashornEngineService input);

    void onComplete(O out);

    void onError(Exception ex);
}
