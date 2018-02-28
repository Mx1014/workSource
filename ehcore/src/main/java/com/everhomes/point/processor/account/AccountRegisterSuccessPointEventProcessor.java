package com.everhomes.point.processor.account;

import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.SystemEvent;
import com.everhomes.point.IPointEventProcessor;
import com.everhomes.point.processor.GeneralPointEventProcessor;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xq.tian on 2017/12/7.
 */
@Component
public class AccountRegisterSuccessPointEventProcessor extends GeneralPointEventProcessor implements IPointEventProcessor {

    @Autowired
    private UserProvider userProvider;

    @Override
    public String[] init() {
        return new String[]{
                SystemEvent.ACCOUNT_COMPLETE_INFO.dft(),
                SystemEvent.ACCOUNT_REGISTER_SUCCESS.dft()
        };
    }

    @Override
    protected String getEventName(LocalEvent localEvent) {
        User oldUser = localEvent.getObjParam("oldUser", User.class);
        User newUser = localEvent.getObjParam("newUser", User.class);

        if (SystemEvent.ACCOUNT_COMPLETE_INFO.dft().equals(localEvent.getEventName())
                && oldUser != null
                && oldUser.getNickName() == null
                && newUser != null
                && newUser.getNickName() != null) {
            return SystemEvent.ACCOUNT_REGISTER_SUCCESS.dft();
        }
        return "";
    }
}