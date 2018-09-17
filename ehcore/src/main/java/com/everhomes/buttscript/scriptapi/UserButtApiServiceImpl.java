package com.everhomes.buttscript.scriptapi;

import com.everhomes.flow.*;
import com.everhomes.rest.flow.FlowEventType;
import com.everhomes.scriptengine.nashorn.NashornModuleApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserButtApiServiceImpl implements NashornModuleApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserButtApiServiceImpl.class);


    @Override
    public String name() {
        return "userButtService";
    }



    public void testCall() {
        LOGGER.debug("this is test api call");
    }
}