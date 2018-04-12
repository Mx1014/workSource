package com.everhomes.point;

import com.everhomes.bus.*;
import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.point.ListPointGoodsCommand;
import com.everhomes.rest.point.ListPointGoodsResponse;
import com.everhomes.rest.point.PublishEventCommand;
import com.everhomes.rest.point.PublishEventResultDTO;
import com.everhomes.util.StringHelper;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xq.tian on 2017/12/21.
 */
public class PointServiceImplTest extends CoreServerTestCase {

    @Autowired
    private PointService pointService;

    @Autowired
    private LocalBus localBus;

    @Test
    public void publishEvent() throws Exception {
        PublishEventCommand cmd = new PublishEventCommand();

        LocalEvent localEvent = new LocalEvent();
        localEvent.setEventName(SystemEvent.BIZ_ORDER_CREATE.dft());
        localEvent.setEntityType("EhBiz");
        localEvent.setEntityId(1L);
        localEvent.setCreateTime(System.currentTimeMillis());

        LocalEventContext context = new LocalEventContext();
        context.setUid(350864L);
        context.setNamespaceId(1000000);
        localEvent.setContext(context);
        localEvent.setSyncFlag(TrueOrFalseFlag.TRUE.getCode());

        localEvent.addParam("points", "100");

        cmd.setEventJson(StringHelper.toJsonString(localEvent));

        PublishEventResultDTO dto = pointService.publishEvent(cmd);
        // assertNull(dto);
    }

    @Test
    public void everyYearEndSendMessageSchedule() throws Exception {
        pointService.everyYearEndSendMessageSchedule();
    }

    @Test
    public void everyYearEndClearPointSchedule() throws Exception {
        pointService.everyYearEndClearPointSchedule();
    }

    @Test
    public void testListPointGoods() {
        ListPointGoodsCommand cmd = new ListPointGoodsCommand();
        cmd.setSystemId(89L);
        cmd.setNamespaceId(1000000);
        cmd.setPageSize(3);
        // cmd.setPageAnchor(1L);

        ListPointGoodsResponse response = pointService.listPointGoods(cmd);
        Assert.assertNotNull(response);
    }

    @Test
    public void testListEnabledPointGoods() {
        ListPointGoodsCommand cmd = new ListPointGoodsCommand();
        cmd.setSystemId(89L);
        cmd.setNamespaceId(1000000);
        cmd.setPageSize(4);
        // cmd.setPageAnchor(1L);

        ListPointGoodsResponse response = pointService.listEnabledPointGoods(cmd);
        Assert.assertNotNull(response);
    }

    @Test
    public void eventInEventTest() {
        LocalBusProvider localBus = new LocalBusProvider();

        localBus.subscribe("eventPublic", (o, s, o1, s1) -> {
            // localBus.publish(Thread.currentThread(), "eventInternal", "internal");
            System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhh:::"+Thread.currentThread().getName());
            return LocalBusSubscriber.Action.none;
        });

        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                localBus.publish("111", "eventPublic", "public");
            }).start();
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}