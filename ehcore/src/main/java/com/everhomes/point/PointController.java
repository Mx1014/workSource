// @formatter:off
package com.everhomes.point;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.point.*;
import com.everhomes.user.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 积分
 * Created by xq.tian on 2017/11/23.
 */
@RestDoc(value="Point controller", site="core")
@RestController
@RequestMapping("/point")
public class PointController extends ControllerBase {

    @Autowired
    private PointService pointService;

    private static final RestResponse SUCCESS = new RestResponse() {
        {
            this.setErrorDescription("OK");
            this.setErrorCode(ErrorCodes.SUCCESS);
        }
    };

    /**
     * <b>URL: /point/getUserPoint</b>
     * <p>获取用户积分</p>
     */
    @RestReturn(PointScoreDTO.class)
    @RequestMapping("getUserPoint")
    public RestResponse getUserPoint(GetUserPointCommand cmd) {
        PointScoreDTO dto = pointService.getUserPoint(cmd);
        return success(dto);
    }

    /**
     * <b>URL: /point/getPointSystem</b>
     * <p>获取积分系统信息</p>
     */
    @RestReturn(PointSystemDTO.class)
    @RequestMapping("getPointSystem")
    public RestResponse getPointSystem(GetPointSystemCommand cmd) {
        PointSystemDTO dto = pointService.getPointSystem(cmd);
        return success(dto);
    }

    /**
     * <b>URL: /point/listPointLogs</b>
     * <p>获取积分记录信息</p>
     */
    @RestReturn(ListPointLogsResponse.class)
    @RequestMapping("listPointLogs")
    public RestResponse listPointLogs(ListPointLogsCommand cmd) {
        if (cmd.getUserId() == null) {
            cmd.setUserId(UserContext.currentUserId());
        }
        ListPointLogsResponse response = pointService.listPointLogs(cmd);
        return success(response);
    }

    /**
     * <b>URL: /point/listPointLogsForMall</b>
     * <p>获取在积分商城滚动的消费记录</p>
     */
    @RestReturn(ListPointLogsResponse.class)
    @RequestMapping("listPointLogsForMall")
    public RestResponse listPointLogsForMall(ListPointLogsForMallCommand cmd) {
        ListPointLogsResponse response = pointService.listPointLogsForMall(cmd);
        return success(response);
    }

    /**
     * <b>URL: /point/listPointGoods</b>
     * <p>获取积分商城的商品</p>
     */
    @RestReturn(ListPointGoodsResponse.class)
    @RequestMapping("listPointGoods")
    public RestResponse listPointGoods(ListPointGoodsCommand cmd) {
        cmd.setStatus(PointCommonStatus.ENABLED.getCode());
        ListPointGoodsResponse response = pointService.listPointGoods(cmd);
        return success(response);
    }

    /**
     * <b>URL: /point/listPointTutorials</b>
     * <p>获取积分指南</p>
     */
    @RestReturn(ListPointTutorialResponse.class)
    @RequestMapping("listPointTutorials")
    public RestResponse listPointTutorials(ListPointTutorialsCommand cmd) {
        ListPointTutorialResponse response = pointService.listPointTutorials(cmd);
        return success(response);
    }

    /**
     * <b>URL: /point/listPointTutorialDetail</b>
     * <p>获取积分指南详情</p>
     */
    @RestReturn(ListPointTutorialDetailResponse.class)
    @RequestMapping("listPointTutorialDetail")
    public RestResponse listPointTutorialDetail(ListPointTutorialDetailCommand cmd) {
        ListPointTutorialDetailResponse response = pointService.listPointTutorialDetail(cmd);
        return success(response);
    }

    /**
     * <b>URL: /point/listPointMallBanners</b>
     * <p>获取积分商城的banner</p>
     */
    @RestReturn(ListPointMallBannersResponse.class)
    @RequestMapping("listPointMallBanners")
    public RestResponse listPointMallBanners(ListPointMallBannersCommand cmd) {
        ListPointMallBannersResponse response = pointService.listPointMallBanners(cmd);
        return success(response);
    }

    private RestResponse success(Object o) {
        RestResponse response = new RestResponse(o);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }
}