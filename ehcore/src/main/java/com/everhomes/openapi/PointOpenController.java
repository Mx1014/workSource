package com.everhomes.openapi;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.point.PointService;
import com.everhomes.point.rpc.PointServiceRPCRest;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.point.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestDoc(value = "Point open Controller", site = "core")
@RestController
@RequestMapping("/openapi/point")
public class PointOpenController extends ControllerBase {

    @Autowired
    private PointService pointService;
    
    @Autowired
    private PointServiceRPCRest pointServiceRPCRest;
    

 

    private static final RestResponse SUCCESS = new RestResponse() {
        {
            this.setErrorDescription("OK");
            this.setErrorCode(ErrorCodes.SUCCESS);
        }
    };

    /**
     * <b>URL: /openapi/point/publishEvent</b>
     * <p>发布事件</p>
     */
    @RestReturn(PublishEventResultDTO.class)
    @RequestMapping("publishEvent")
    public RestResponse publishEvent(PublishEventCommand cmd) {
        PublishEventResultDTO dto = pointService.publishEvent(cmd);
        return success(dto);
    }

    /**
     * <b>URL: /openapi/point/getUserPoint</b>
     * <p>获取用户积分</p>
     */
    @RestReturn(PointScoreDTO.class)
    @RequestMapping("getUserPoint")
    public RestResponse getUserPoint(GetUserPointCommand cmd) {
       // PointScoreDTO dto = pointService.getUserPointForOpenAPI(cmd);
        PointScoreDTO dto = pointServiceRPCRest.getUserPoint(cmd);
        return success(dto);
    }

    /**
     * <b>URL: /openapi/point/getEnabledPointSystem</b>
     * <p>获取启用的积分系统</p>
     */
    @RestReturn(PointSystemDTO.class)
    @RequestMapping("getEnabledPointSystem")
    public RestResponse getEnabledPointSystem(GetEnabledPointSystemCommand cmd) {
        GetEnabledPointSystemResponse response = pointService.getEnabledPointSystem(cmd);
        if (response != null && response.getSystems() != null && response.getSystems().size() > 0) {
            return success(response.getSystems().get(0));
        }
        return success(null);
    }

    private RestResponse success(Object o) {
        RestResponse response = new RestResponse(o);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }
}
