package com.everhomes.point.processor.biz;

import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.SystemEvent;
import com.everhomes.point.*;
import com.everhomes.point.processor.GeneralPointEventProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by xq.tian on 2017/12/7.
 */
@Component
public class BizOrderCancelPointEventProcessor extends GeneralPointEventProcessor implements IPointEventProcessor {

    @Override
    public String[] init() {
        return new String[]{
                SystemEvent.BIZ_ORDER_CANCEL.dft(),
        };
    }

    @Override
    protected Long getPoints(LocalEvent localEvent, PointSystem pointSystem, PointRule rule) {
        PointLog pointLog = pointLogProvider.findByRuleIdAndEntity(
                pointSystem.getNamespaceId(),
                pointSystem.getId(),
                localEvent.getContext().getUid(),
                rule.getBindingRuleId(),
                localEvent.getEntityType(),
                localEvent.getEntityId()
        );

        if (pointLog != null) {
            return pointLog.getPoints();
        }
        return 0L;
    }
}
