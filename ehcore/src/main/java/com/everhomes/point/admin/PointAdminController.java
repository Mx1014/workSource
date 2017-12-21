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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 积分
 * Created by xq.tian on 2017/11/23.
 */
@RestDoc(value = "Point admin controller", site = "core")
@RestController
@RequestMapping("/admin/point")
public class PointAdminController extends ControllerBase {

    private static final RestResponse SUCCESS = new RestResponse() {
        {
            this.setErrorDescription("OK");
            this.setErrorCode(ErrorCodes.SUCCESS);
        }
    };

    @Autowired
    private PointService pointService;

    /**
     * <b>URL: /admin/point/getPointSystem</b>
     * <p>获取积分系统信息</p>
     */
    @RestReturn(PointSystemDTO.class)
    @RequestMapping("getPointSystem")
    public RestResponse getPointSystem(GetPointSystemCommand cmd) {
        PointSystemDTO dto = pointService.getPointSystem(cmd);
        return success(dto);
    }

    /**
     * <b>URL: /admin/point/listPointSystems</b>
     * <p>积分系统列表</p>
     */
    @RestReturn(ListPointSystemsResponse.class)
    @RequestMapping("listPointSystems")
    public RestResponse listPointSystems(ListPointSystemsCommand cmd) {
        ListPointSystemsResponse response = pointService.listPointSystems(cmd);
        return success(response);
    }

    /**
     * <b>URL: /admin/point/createPointSystem</b>
     * <p>创建积分系统配置信息</p>
     */
    @RestReturn(PointSystemDTO.class)
    @RequestMapping("createPointSystem")
    public RestResponse createPointSystem(CreatePointSystemCommand cmd) {
        PointSystemDTO dto = pointService.createPointSystem(cmd);
        return success(dto);
    }

    /**
     * <b>URL: /admin/point/enablePointSystem</b>
     * <p>开启积分系统</p>
     */
    @RestReturn(PointSystemDTO.class)
    @RequestMapping("enablePointSystem")
    public RestResponse enablePointSystem(PointSystemIdCommand cmd) {
        PointSystemDTO dto = pointService.enablePointSystem(cmd);
        return success(dto);
    }

    /**
     * <b>URL: /admin/point/disablePointSystem</b>
     * <p>关闭积分系统</p>
     */
    @RestReturn(PointSystemDTO.class)
    @RequestMapping("disablePointSystem")
    public RestResponse disablePointSystem(PointSystemIdCommand cmd) {
        PointSystemDTO dto = pointService.disablePointSystem(cmd);
        return success(dto);
    }

    /**
     * <b>URL: /admin/point/deletePointSystem</b>
     * <p>删除积分系统</p>
     */
    @RestReturn(String.class)
    @RequestMapping("deletePointSystem")
    public RestResponse deletePointSystem(PointSystemIdCommand cmd) {
        pointService.deletePointSystem(cmd);
        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/updatePointSystem</b>
     * <p>修改积分系统配置信息</p>
     */
    @RestReturn(PointSystemDTO.class)
    @RequestMapping("updatePointSystem")
    public RestResponse updatePointSystem(UpdatePointSystemCommand cmd) {
        PointSystemDTO dto = pointService.updatePointSystem(cmd);
        return success(dto);
    }

    /**
     * <b>URL: /admin/point/listPointRuleCategories</b>
     * <p>获取积分规则分类列表</p>
     */
    @RestReturn(ListPointRuleCategoriesResponse.class)
    @RequestMapping("listPointRuleCategories")
    public RestResponse listPointRuleCategories() {
        ListPointRuleCategoriesResponse response = pointService.listPointRuleCategories();
        return success(response);
    }

    /**
     * <b>URL: /admin/point/createPointLog</b>
     * <p>新增积分记录</p>
     */
    @RestReturn(PointLogDTO.class)
    @RequestMapping("createPointLog")
    public RestResponse createPointLog(CreatePointLogCommand cmd, HttpServletRequest request) {
        cmd.setSessionId(request.getSession().getId());
        PointLogDTO dto = pointService.createPointLog(cmd);
        return success(dto);
    }

    /**
     * <b>URL: /admin/point/listPointRules</b>
     * <p>获取积分规则列表</p>
     */
    @RestReturn(ListPointRulesResponse.class)
    @RequestMapping("listPointRules")
    public RestResponse listPointRules(ListPointRulesCommand cmd) {
        ListPointRulesResponse response = pointService.listPointRules(cmd);
        return success(response);
    }

    /**
     * <b>URL: /admin/point/updatePointRule</b>
     * <p>获取积分规则列表</p>
     */
    @RestReturn(PointRuleDTO.class)
    @RequestMapping("updatePointRule")
    public RestResponse updatePointRule(UpdatePointRuleCommand cmd) {
        PointRuleDTO dto = pointService.updatePointRule(cmd);
        return success(dto);
    }

    /**
     * <b>URL: /admin/point/listPointLogs</b>
     * <p>获取积分记录列表</p>
     */
    @RestReturn(ListPointLogsResponse.class)
    @RequestMapping("listPointLogs")
    public RestResponse listPointLogs(ListPointLogsCommand cmd) {
        ListPointLogsResponse response = pointService.listPointLogs(cmd);
        return success(response);
    }

    /**
     * <b>URL: /admin/point/listManuallyPointLogs</b>
     * <p>获取手工积分记录列表</p>
     */
    @RestReturn(ListPointLogsResponse.class)
    @RequestMapping("listManuallyPointLogs")
    public RestResponse listManuallyPointLogs(ListPointLogsCommand cmd) {
        ListPointLogsResponse response = pointService.listManuallyPointLogs(cmd);
        return success(response);
    }

    /**
     * <b>URL: /admin/point/exportPointLogs</b>
     * <p>导出积分记录</p>
     */
    @RestReturn(String.class)
    @RequestMapping("exportPointLogs")
    public RestResponse exportPointLogs(ExportPointLogsCommand cmd, HttpServletResponse response) {
        pointService.exportPointLog(cmd, response);
        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/listPointGoods</b>
     * <p>获取积分商城的商品</p>
     */
    @RestReturn(ListPointGoodsResponse.class)
    @RequestMapping("listPointGoods")
    public RestResponse listPointGoods(ListPointGoodsCommand cmd) {
        ListPointGoodsResponse response = pointService.listPointGoods(cmd);
        return success(response);
    }

    /**
     * <b>URL: /admin/point/updatePointGood</b>
     * <p>修改积分商城的商品</p>
     */
    @RestReturn(PointGoodDTO.class)
    @RequestMapping("updatePointGood")
    public RestResponse updatePointGood(UpdatePointGoodCommand cmd) {
        PointGoodDTO dto = pointService.updatePointGood(cmd);
        return success(dto);
    }

    /**
     * <b>URL: /admin/point/listPointTutorials</b>
     * <p>获取积分指南</p>
     */
    @RestReturn(ListPointTutorialResponse.class)
    @RequestMapping("listPointTutorials")
    public RestResponse listPointTutorials(ListPointTutorialsCommand cmd) {
        ListPointTutorialResponse response = pointService.listPointTutorialsWithMapping(cmd);
        return success(response);
    }

    /**
     * <b>URL: /admin/point/deletePointTutorial</b>
     * <p>删除积分指南</p>
     */
    @RestReturn(PointTutorialDTO.class)
    @RequestMapping("deletePointTutorial")
    public RestResponse deletePointTutorial(DeletePointTutorialCommand cmd) {
        PointTutorialDTO dto = pointService.deletePointTutorial(cmd);
        return success(dto);
    }

    /**
     * <b>URL: /admin/point/createOrUpdatePointTutorial</b>
     * <p>创建或者更新积分指南</p>
     */
    @RestReturn(PointTutorialDTO.class)
    @RequestMapping("createOrUpdatePointTutorial")
    public RestResponse createOrUpdatePointTutorial(CreateOrUpdatePointTutorialCommand cmd) {
        PointTutorialDTO dto = pointService.createOrUpdatePointTutorial(cmd);
        return success(dto);
    }

    /**
     * <b>URL: /admin/point/restartEventLogScheduler</b>
     * <p>因为事件处理调度一旦遇到异常就会停止，所以需要手动重启</p>
     */
    @RestReturn(String.class)
    @RequestMapping("restartEventLogScheduler")
    public RestResponse restartEventLogScheduler() {
        pointService.restartEventLogScheduler();
        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/reloadEventMapping</b>
     * <p>重新加载事件mapping</p>
     */
    @RestReturn(String.class)
    @RequestMapping("reloadEventMapping")
    public RestResponse reloadEventMapping() {
        pointService.reloadEventMapping();
        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/createPointRule</b>
     * <p>新增积分规则</p>
     */
    @RestReturn(PointRuleDTO.class)
    @RequestMapping("createPointRule")
    public RestResponse createPointRule(CreatePointRuleCommand cmd) {
        PointRuleDTO dto = pointService.createPointRule(cmd);
        return success(dto);
    }

    /**
     * <b>URL: /admin/point/createPointRuleToEventMapping</b>
     * <p>新增积分规则与事件的映射</p>
     */
    @RestReturn(String.class)
    @RequestMapping("createPointRuleToEventMapping")
    public RestResponse createPointRuleToEventMapping(CreatePointRuleToEventMappingCommand cmd) {
        pointService.createPointRuleToEventMapping(cmd);
        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/createPointBanner</b>
     * <p>创建积分商城的banner</p>
     */
    @RestReturn(PointBannerDTO.class)
    @RequestMapping("createPointBanner")
    public RestResponse createPointBanner(CreatePointBannerCommand cmd) {
        PointBannerDTO dto = pointService.createPointBanner(cmd);
        return success(dto);
    }

    /**
     * <b>URL: /admin/point/updatePointBanner</b>
     * <p>修改积分商城的banner</p>
     */
    @RestReturn(PointBannerDTO.class)
    @RequestMapping("updatePointBanner")
    public RestResponse updatePointBanner(UpdatePointBannerCommand cmd) {
        PointBannerDTO dto = pointService.updatePointBanner(cmd);
        return success(dto);
    }

    /**
     * <b>URL: /admin/point/updatePointBannerStatus</b>
     * <p>修改积分商城的banner开关</p>
     */
    @RestReturn(PointBannerDTO.class)
    @RequestMapping("updatePointBannerStatus")
    public RestResponse updatePointBannerStatus(UpdatePointBannerStatusCommand cmd) {
        PointBannerDTO dto = pointService.updatePointBannerStatus(cmd);
        return success(dto);
    }

    /**
     * <b>URL: /admin/point/listPointBannersBySystemId</b>
     * <p>积分商城的banner列表</p>
     */
    @RestReturn(ListPointBannersResponse.class)
    @RequestMapping("listPointBannersBySystemId")
    public RestResponse listPointBanners(ListPointBannersCommand cmd) {
        ListPointBannersResponse response = pointService.listPointBanners(cmd);
        return success(response);
    }

    /**
     * <b>URL: /admin/point/deletePointBanner</b>
     * <p>删除积分商城的banner</p>
     */
    @RestReturn(String.class)
    @RequestMapping("deletePointBanner")
    public RestResponse deletePointBanner(DeletePointBannerCommand cmd) {
        pointService.deletePointBanner(cmd);
        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/reorderPointBanners</b>
     * <p>积分商城的banner重排序</p>
     */
    @RestReturn(String.class)
    @RequestMapping("reorderPointBanners")
    public RestResponse reorderPointBanners(ReorderPointBannersCommand cmd) {
        pointService.reorderPointBanners(cmd);
        return SUCCESS;
    }

    /**
     * <b>URL: /admin/point/checkUserInfo</b>
     * <p>检查用户</p>
     */
    @RestReturn(CheckUserInfoResponse.class)
    @RequestMapping("checkUserInfo")
    public RestResponse checkUserInfo(CheckUserInfoCommand cmd, HttpServletRequest request) {
        cmd.setSessionId(request.getSession().getId());
        CheckUserInfoResponse response = pointService.checkUserInfo(cmd);
        return success(response);
    }

    private RestResponse success(Object o) {
        RestResponse response = new RestResponse(o);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }
}