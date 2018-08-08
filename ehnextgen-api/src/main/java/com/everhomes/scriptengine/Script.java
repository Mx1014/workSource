package com.everhomes.scriptengine;

public interface Script<I, O> {

    String getScript();

    O process(I input);

    void onComplete(O out);

    void onError(Exception ex);
}
