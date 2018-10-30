package com.everhomes.point;

import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.point.pointscores.GetUserPointCommand;
import com.everhomes.point.pointscores.PointScoreDTO;
import com.everhomes.point.sdk.SdkPointService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PointRestTest extends CoreServerTestCase {

    @Autowired
    private SdkPointService sdkPointService;

    @Test
    public void testGetUserPoint() throws InterruptedException {
        Thread.sleep(10*1000);
        GetUserPointCommand cmd = new GetUserPointCommand();
        cmd.setNamespaceId(0);
        cmd.setUid(1L);

        PointScoreDTO userPoint = sdkPointService.getUserPoint(cmd);
        System.out.println(userPoint);
        assertNotNull(userPoint);
    }
}
