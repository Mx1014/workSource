package com.everhomes.point.processor.account;

import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.SystemEvent;
import com.everhomes.point.*;
import com.everhomes.point.processor.GeneralPointEventProcessor;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xq.tian on 2017/12/7.
 */
@Component
public class AccountOpenAppPointEventProcessor extends GeneralPointEventProcessor implements IPointEventProcessor {

    @Autowired
    private UserProvider userProvider;

    @Override
    public String[] init() {
        return new String[]{SystemEvent.ACCOUNT_OPEN_APP.dft()};
    }

    @Override
    public PointEventProcessResult execute(LocalEvent localEvent, PointRule rule,
                                           PointSystem pointSystem, PointRuleCategory pointRuleCategory) {
        if (isValid(localEvent)) {
            return super.execute(localEvent, rule, pointSystem, pointRuleCategory);
        }
        return null;
    }

    private boolean isValid(LocalEvent localEvent) {
        User user = userProvider.findUserById(localEvent.getContext().getUid());
        return user != null && isValid(user.getNickName());
    }

    private boolean isValid(Object o) {
        return o != null && o.toString().trim().length() > 0;
    }
}
