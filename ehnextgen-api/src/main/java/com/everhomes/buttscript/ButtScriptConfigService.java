package com.everhomes.buttscript;

import com.everhomes.rest.buttscript.*;

public interface ButtScriptConfigService {


    ButtScriptConfingResponse findButtScriptConfigByNamespaceId(FindButtScriptConfingCommand cmd);

    void updateButtScriptConfing(UpdateButtScriptConfingCommand cmd ) ;

    ButtScriptConfigDTO crteateButtScriptConfing(AddButtScriptConfingCommand cmd ) ;

    void updateButtScriptConfingStatus(UpdateButtScriptConfingStatusCommand cmd ) ;

}
