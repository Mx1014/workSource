package com.everhomes.point.processor.biz;

import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.SystemEvent;
import com.everhomes.point.IPointEventProcessor;
import com.everhomes.point.PointRule;
import com.everhomes.point.PointSystem;
import com.everhomes.point.limit.BizPointRuleLimitData;
import com.everhomes.point.processor.GeneralPointEventProcessor;
import com.everhomes.util.StringHelper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Created by xq.tian on 2017/12/7.
 */
@Component
public class BizOrderPayCompletePointEventProcessor
        extends GeneralPointEventProcessor implements IPointEventProcessor {

    private final static Long BIZ_CONTINUOUS_RULE_ID = 20L;// 连续几天消费的规则id
    private final static Long BIZ_CONSUME_RULE_ID = 19L;// 普通消费的规则id

    @Override
    public String[] init() {
        return new String[]{
                SystemEvent.BIZ_ORDER_PAY_COMPLETE.dft(),
        };
    }

    @Override
    protected boolean doLimit(LocalEvent localEvent, PointRule rule, PointSystem pointSystem) {
        Long targetUid = localEvent.getContext().getUid();
        Integer namespaceId = localEvent.getContext().getNamespaceId();

        BizPointRuleLimitData limitData = (BizPointRuleLimitData) StringHelper.fromJsonString(
                rule.getLimitData(), BizPointRuleLimitData.class);

        // 连续几天消费
        if (Objects.equals(rule.getId(), BIZ_CONTINUOUS_RULE_ID)) {
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

            boolean success = true;
            // 连续消费的间隔
            LocalDateTime contTime = dateTime.minusDays(limitData.getContinuous() - 1);
            Integer count = pointLogProvider.countPointLog(
                    namespaceId, pointSystem.getId(), targetUid, BIZ_CONTINUOUS_RULE_ID,
                    Timestamp.valueOf(dateTime), Timestamp.valueOf(contTime));
            if (count > 0) {
                success = false;
            }

            if (success) {
                for (Integer i = 1; i < limitData.getContinuous(); i++) {
                    LocalDateTime startTime = dateTime.minusDays(i);
                    LocalDateTime endTime = dateTime.minusDays(i - 1);

                    count = pointLogProvider.countPointLog(
                            namespaceId, pointSystem.getId(), targetUid, BIZ_CONSUME_RULE_ID,
                            Timestamp.valueOf(startTime), Timestamp.valueOf(endTime));
                    if (count == 0) {
                        success = false;
                        break;
                    }
                }
            }
            return success && super.doLimit(localEvent, rule, pointSystem);
        }
        return super.doLimit(localEvent, rule, pointSystem);
    }
}
