package com.everhomes.point;

import com.everhomes.rest.point.*;
import com.everhomes.rest.user.UserTreasureDTO;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by xq.tian on 2017/11/24.
 */
public interface PointService {

    String getPointSystemUrl(Long systemId);

    GetEnabledPointSystemResponse getEnabledPointSystem(GetEnabledPointSystemCommand cmd);

    PointScoreDTO getUserPoint(GetUserPointCommand cmd);

    PointSystemDTO getPointSystem(GetPointSystemCommand cmd);

    ListPointLogsResponse listPointLogs(ListPointLogsCommand cmd);

    ListPointGoodsResponse listPointGoods(ListPointGoodsCommand cmd);

    ListPointTutorialResponse listPointTutorials(ListPointTutorialsCommand cmd);

    ListPointTutorialDetailResponse listPointTutorialDetail(ListPointTutorialDetailCommand cmd);

    ListPointMallBannersResponse listPointMallBanners(ListPointMallBannersCommand cmd);

    ListPointLogsResponse listPointLogsForMall(ListPointLogsForMallCommand cmd);

    PointSystemDTO createPointSystem(CreatePointSystemCommand cmd);

    PointSystemDTO updatePointSystem(UpdatePointSystemCommand cmd);

    ListPointRuleCategoriesResponse listPointRuleCategories();

    PointLogDTO createPointLog(CreatePointLogCommand cmd);

    ListPointRulesResponse listPointRules(ListPointRulesCommand cmd);

    void exportPointLog(ExportPointLogsCommand cmd, HttpServletResponse response);

    PointGoodDTO updatePointGood(UpdatePointGoodCommand cmd);

    PointTutorialDTO deletePointTutorial(DeletePointTutorialCommand cmd);

    PointTutorialDTO createOrUpdatePointTutorial(CreateOrUpdatePointTutorialCommand cmd);

    ListPointTutorialResponse listPointTutorialsWithMapping(ListPointTutorialsCommand cmd);

    ListPointSystemsResponse listPointSystems(ListPointSystemsCommand cmd);

    PointSystemDTO enablePointSystem(PointSystemIdCommand cmd);

    PointSystemDTO disablePointSystem(PointSystemIdCommand cmd);

    void deletePointSystem(PointSystemIdCommand cmd);

    PointRuleDTO getPointRule(GetPointRuleCommand cmd);

    void restartEventLogScheduler();

    void processUserPoint(UserTreasureDTO point);
}
