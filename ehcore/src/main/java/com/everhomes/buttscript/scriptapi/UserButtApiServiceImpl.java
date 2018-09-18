package com.everhomes.buttscript.scriptapi;

import com.everhomes.flow.*;
import com.everhomes.rest.flow.FlowEventType;
import com.everhomes.scriptengine.nashorn.NashornModuleApiService;
import com.everhomes.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserButtApiServiceImpl implements NashornModuleApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserButtApiServiceImpl.class);


    @Autowired
    private UserService userService ;

    @Override
    public String name() {
        return "userButtService";
    }


    /**
     * 更新会员等级
     */
    public void updateUserVipLevel(Long userId, Integer vipLevel){
        LOGGER.info("the script call the api updateUserVipLevel . userId:{},vipLevel:{}",userId,vipLevel);
        userService.updateUserVipLevel( userId,  vipLevel);
    }


    public void testCall() {
        LOGGER.debug("this is test api call");
    }
}