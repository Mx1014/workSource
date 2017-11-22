// @formatter:off
package com.everhomes.point.admin;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.point.PointService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.point.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 积分
 * Created by xq.tian on 2017/11/23.
 */
@RestDoc(value="Point admin controller", site="core")
@RestController
@RequestMapping("/admin/point")
public class PointAdminController extends ControllerBase {

    @Autowired
    private PointService pointService;

    private static final RestResponse SUCCESS = new RestResponse() {
        {
            this.setErrorDescription("OK");
            this.setErrorCode(ErrorCodes.SUCCESS);
        }
    };

    /**
     * <b>URL: /admin/point/createPointSystem</b>
     * <p>创建积分系统配置信息</p>
     */
    @RestReturn(PointSystemDTO.class)
    @RequestMapping("createPointSystem")
    public RestResponse createPointConfig(CreatePointSystemCommand cmd) {

        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/updatePointSystem</b>
     * <p>修改积分系统配置信息</p>
     */
    @RestReturn(PointSystemDTO.class)
    @RequestMapping("updatePointSystem")
    public RestResponse updatePointConfig(UpdatePointSystemCommand cmd) {

        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/listPointModules</b>
     * <p>获取模块列表</p>
     */
    @RestReturn(ListPointModulesResponse.class)
    @RequestMapping("listPointModules")
    public RestResponse listPointModules() {

        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/createPointLog</b>
     * <p>新增积分记录</p>
     */
    @RestReturn(ListPointModulesResponse.class)
    @RequestMapping("createPointLog")
    public RestResponse createPointLog(CreatePointLogCommand cmd) {

        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/listPointRules</b>
     * <p>获取积分规则列表</p>
     */
    @RestReturn(ListPointRulesResponse.class)
    @RequestMapping("listPointRules")
    public RestResponse listPointRules(ListPointRulesCommand cmd) {

        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/listPointLogs</b>
     * <p>获取积分记录列表</p>
     */
    @RestReturn(ListPointLogsResponse.class)
    @RequestMapping("listPointLogs")
    public RestResponse listPointLogs(ListPointLogsCommand cmd) {

        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/exportPointLogs</b>
     * <p>导出积分记录</p>
     */
    @RestReturn(String.class)
    @RequestMapping("exportPointLogs")
    public RestResponse exportPointLogs(ExportPointLogsCommand cmd) {

        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/listPointGoods</b>
     * <p>获取积分商城的商品</p>
     */
    @RestReturn(ListPointGoodsResponse.class)
    @RequestMapping("listPointGoods")
    public RestResponse listPointGoods(ListPointGoodsCommand cmd) {

        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/updatePointGoods</b>
     * <p>修改积分商城的商品</p>
     */
    @RestReturn(PointGoodDTO.class)
    @RequestMapping("updatePointGoods")
    public RestResponse updatePointGoods(UpdatePointGoodCommand cmd) {

        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/listPointTutorials</b>
     * <p>获取积分指南</p>
     */
    @RestReturn(ListPointTutorialResponse.class)
    @RequestMapping("listPointTutorials")
    public RestResponse listPointTutorials(ListPointTutorialsCommand cmd) {

        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/listPointTutorialDetail</b>
     * <p>获取积分指南详情</p>
     */
    @RestReturn(ListPointTutorialDetailResponse.class)
    @RequestMapping("listPointTutorialDetail")
    public RestResponse listPointTutorialDetail(ListPointTutorialDetailCommand cmd) {

        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/deletePointTutorial</b>
     * <p>删除积分指南</p>
     */
    @RestReturn(PointTutorialDTO.class)
    @RequestMapping("deletePointTutorial")
    public RestResponse deletePointTutorial(DeletePointTutorialCommand cmd) {

        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/createOrUpdatePointTutorial</b>
     * <p>创建或者更新积分指南</p>
     */
    @RestReturn(PointTutorialDTO.class)
    @RequestMapping("createOrUpdatePointTutorial")
    public RestResponse createOrUpdatePointTutorial(CreateOrUpdatePointTutorialCommand cmd) {

        return SUCCESS;
    }

    // 给电商提供的接口

    /**
     * <b>URL: /admin/point/notifySyncPointGoods</b>
     * <p>通知积分系统去读取电商的数据</p>
     */
    @RestReturn(String.class)
    @RequestMapping("notifySyncPointGoods")
    public RestResponse notifySyncPointGoods() {

        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/publishEvent</b>
     * <p>发布事件</p>
     */
    @RestReturn(String.class)
    @RequestMapping("publishEvent")
    public RestResponse publishEvent(PublishEventCommand cmd) {

        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/getUserPoint</b>
     * <p>获取用户积分</p>
     */
    @RestReturn(PointScoreDTO.class)
    @RequestMapping("getUserPoint")
    public RestResponse getUserPoint(GetUserPointCommand cmd) {
        PointScoreDTO dto = pointService.getUserPoint(cmd);
        return response(dto);
    }

    /**
     * <b>URL: /admin/point/getPointRule</b>
     * <p>获取积分规则</p>
     */
    @RestReturn(PointRuleDTO.class)
    @RequestMapping("getPointRule")
    public RestResponse getPointRule(GetPointRuleCommand cmd) {

        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/getEnabledPointSystem</b>
     * <p>获取启用的积分系统配置</p>
     */
    @RestReturn(GetEnabledPointSystemResponse.class)
    @RequestMapping("getEnabledPointSystem")
    public RestResponse getEnabledPointSystem(GetEnabledPointSystemCommand cmd) {
        GetEnabledPointSystemResponse response = pointService.getEnabledPointSystem(cmd);
        return response(response);
    }

    private RestResponse response(Object o) {
        RestResponse response = new RestResponse(o);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }
}