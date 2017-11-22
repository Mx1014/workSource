package com.everhomes.point;

import com.everhomes.rest.point.*;

/**
 * Created by xq.tian on 2017/11/24.
 */
public interface PointService {

    GetEnabledPointSystemResponse getEnabledPointSystem(GetEnabledPointSystemCommand cmd);

    PointScoreDTO getUserPoint(GetUserPointCommand cmd);

    PointSystemDTO getPointSystem(GetPointSystemCommand cmd);

    ListPointLogsResponse listPointLogs(ListPointLogsCommand cmd);

    ListPointGoodsResponse listPointGoods(ListPointGoodsCommand cmd);

    ListPointTutorialResponse listPointTutorials(ListPointTutorialsCommand cmd);

    ListPointTutorialDetailResponse listPointTutorialDetail(ListPointTutorialDetailCommand cmd);

    ListPointMallBannersResponse listPointMallBanners(ListPointMallBannersCommand cmd);

    ListPointLogsResponse listPointLogsForMall(ListPointLogsForMallCommand cmd);
}
