package com.everhomes.buttscript;

import com.everhomes.rest.buttscript.*;

public interface ButtInfoTypeEventMappingService {

    ButtInfoTypeEventMappingResponse findButtEventMapping(FindButtEventMappingCommand cmd ) ;

    void updateButtEventMapping(UpdateButtEventMappingCommand cmd ) ;

    ButtInfoTypeEventMappingDTO crteateButtEventMapping(AddButtEventMappingCommand cmd ) ;

    void deleteButtEventMapping(DeleteButtEventMappingCommand cmd ) ;
}
