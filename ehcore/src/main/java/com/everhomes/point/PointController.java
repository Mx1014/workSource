// @formatter:off
package com.everhomes.point;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.point.rpc.PointServiceRPCRest;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.point.*;
import com.everhomes.user.UserContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 积分
 * Created by xq.tian on 2017/11/23.
 */
@RestDoc(value = "Point controller", site = "core")
@RestController
@RequestMapping("/point")
public class PointController extends ControllerBase {

    private static final RestResponse SUCCESS = new RestResponse() {
        {
            this.setErrorDescription("OK");
            this.setErrorCode(ErrorCodes.SUCCESS);
        }
    };

    @Autowired
    private PointService pointService;
    
    @Autowired
    private PointServiceRPCRest pointServiceRPCRest;

    /**
     * <b>URL: /point/getUserPoint</b>
     * <p>获取用户积分</p>
     */
    @RestReturn(PointScoreDTO.class)
    @RequestMapping("getUserPoint")
    public RestResponse getUserPoint(GetUserPointCommand cmd) {
        //PointScoreDTO dto = pointService.getUserPoint(cmd);
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        cmd.setNamespaceId(namespaceId);
        Long uid = cmd.getUid() != null ? cmd.getUid() : UserContext.currentUserId();
        cmd.setUid(uid);
        PointScoreDTO dto = pointServiceRPCRest.getUserPoint(cmd);
        if(dto == null){
            dto = new PointScoreDTO();
            dto.setScore(0L);
        }else if(dto.getScore() == null){
            dto.setScore(0L);
        }

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
        //ListPointLogsResponse response = pointService.listPointLogs(cmd);
        ListPointLogsResponse response = pointServiceRPCRest.getUserPointLogs(cmd);
        if(response == null){
            List<PointLogDTO> logDTOS = new ArrayList<PointLogDTO>();

            response = new ListPointLogsResponse();
            response.setLogs(logDTOS);
        }else if(response.getLogs() == null){
            List<PointLogDTO> logDTOS = new ArrayList<PointLogDTO>();
            response.setLogs(logDTOS);
        }else{
            //页面上会自动帮抵扣的积分加负号,所以这里要把负号去掉
            List<PointLogDTO> logDTOS = response.getLogs() ;
            for(PointLogDTO logs :logDTOS){
                if(logs.getPoints()<0 && logs.getPoints() != null){
                    logs.setPoints(Math.abs(logs.getPoints()));

                }
                logs.setCategoryName(logs.getAppName());
                logs.setDescription(logs.getRuleName());
            }
        }
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
        ListPointGoodsResponse response = pointService.listEnabledPointGoods(cmd);
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
    @RestReturn(ListPointBannersResponse.class)
    @RequestMapping("listPointMallBanners")
    public RestResponse listPointMallBanners(ListPointBannersCommand cmd) {
        cmd.setStatus(PointCommonStatus.ENABLED.getCode());
        ListPointBannersResponse response = pointService.listPointBanners(cmd);
        return success(response);
    }

    private RestResponse success(Object o) {
        RestResponse response = new RestResponse(o);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }
}