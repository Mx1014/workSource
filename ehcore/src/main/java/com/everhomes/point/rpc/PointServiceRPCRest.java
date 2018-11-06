package com.everhomes.point.rpc;

import com.everhomes.point.sdk.SdkPointService;
import com.everhomes.rest.point.*;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointServiceRPCRest {

    @Autowired
    private SdkPointService sdkPointService;

    /**
     * 获取用户积分
     * @param cmd
     * @return
     */
    public PointScoreDTO getUserPoint(GetUserPointCommand cmd) {
        com.everhomes.point.pointscores.GetUserPointCommand ccmd = ConvertHelper.convert(cmd, com.everhomes.point.pointscores.GetUserPointCommand.class);
        return ConvertHelper.convert(sdkPointService.getUserPoint(ccmd), PointScoreDTO.class);
    }

    /**
     * 查询用户积分获取
     * @param cmd
     * @return
     */

    public ListPointLogsResponse getUserPointLogs(ListPointLogsCommand cmd) {
        com.everhomes.point.pointlogs.ListPointLogsCommand ccmd = ConvertHelper.convert(cmd, com.everhomes.point.pointlogs.ListPointLogsCommand.class);
        return ConvertHelper.convert(sdkPointService.getUserPointLogs(ccmd), ListPointLogsResponse.class);
    }

    /**
     * 积分消费
     * @param cmd
     * @return
     */
    public boolean publishPointCostEvent(PublishEventCommand cmd) {
        com.everhomes.point.common.PublishEventCommand ccmd = ConvertHelper.convert(cmd, com.everhomes.point.common.PublishEventCommand.class);
        return sdkPointService.publishPointCostEvent(ccmd);
    }
}
