package com.everhomes.scriptengine.nashorn;

import com.everhomes.scriptengine.Script;

public interface NashornScript<O> extends Script<NashornEngineService, O> {

    String getScript();

    O process(NashornEngineService input);

    void onComplete(O out);

    void onError(Exception ex);
}
