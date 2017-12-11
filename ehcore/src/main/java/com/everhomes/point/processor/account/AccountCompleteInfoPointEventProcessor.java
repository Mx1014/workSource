package com.everhomes.point.processor.account;

import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.SystemEvent;
import com.everhomes.point.*;
import com.everhomes.point.processor.GeneralPointEventProcessor;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 完善用户信息事件处理器
 * Created by xq.tian on 2017/12/7.
 */
@Component
public class AccountCompleteInfoPointEventProcessor implements PointEventProcessor {

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private GeneralPointEventProcessor generalProcessor;

    @Override
    public String[] init() {
        return new String[]{SystemEvent.ACCOUNT_COMPLETE_INFO.dft()};
    }

    @Override
    public PointEventProcessResult execute(PointEventLog log, PointRule rule, PointSystem pointSystem, PointRuleCategory pointRuleCategory) {
        LocalEvent localEvent = (LocalEvent) StringHelper.fromJsonString(log.getEventJson(), LocalEvent.class);
        if (isValidEvent(localEvent)) {
            return generalProcessor.execute(log, rule, pointSystem, pointRuleCategory);
        }
        return null;
    }

    private boolean isValidEvent(LocalEvent localEvent) {
        User user = userProvider.findUserById(localEvent.getContext().getUid());
        return isValid(user.getAvatar()) && isValid(user.getBirthday()) && isValid(user.getStatusLine()) && isValid(user.getOccupation());
    }

    private boolean isValid(Object o) {
        return o != null && o.toString().trim().length() > 0;
    }

    @Override
    public List<PointResultAction> getResultActions(List<PointAction> pointActions, PointEventLog log, PointRule rule, PointSystem pointSystem, PointRuleCategory category) {
        return generalProcessor.getResultActions(pointActions, log, rule, pointSystem, category);
    }
}
