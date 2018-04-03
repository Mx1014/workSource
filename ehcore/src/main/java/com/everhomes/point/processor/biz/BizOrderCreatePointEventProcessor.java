package com.everhomes.point.processor.biz;

import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.SystemEvent;
import com.everhomes.point.*;
import com.everhomes.point.processor.GeneralPointEventProcessor;
import com.everhomes.util.StringHelper;
import org.springframework.stereotype.Component;

/**
 * Created by xq.tian on 2017/12/7.
 */
@Component
public class BizOrderCreatePointEventProcessor extends GeneralPointEventProcessor implements IPointEventProcessor {

    @Override
    public String[] init() {
        return new String[]{
                SystemEvent.BIZ_ORDER_CREATE.dft(),
        };
    }

    @Override
    protected Long getPoints(LocalEvent localEvent, PointSystem pointSystem, PointRule rule) {
        String pointsStr = localEvent.getStringParam("score");
        Float aFloat = Float.valueOf(pointsStr);
        return -(aFloat.longValue());
    }

    @Override
    protected void processPointLog(PointLog pointLog, LocalEvent localEvent, PointRule rule, PointSystem pointSystem, PointRuleCategory pointRuleCategory) {
        pointLog.setExtra(StringHelper.toJsonString(localEvent.getParams()));
    }
}
