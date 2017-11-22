// @formatter:off
package com.everhomes.relocation;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.parking.ParkingService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.parking.*;
import com.everhomes.rest.relocation.*;
import com.everhomes.util.RequireAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestDoc(value="Relocation controller", site="relocation")
@RestController
@RequestMapping("/relocation")
public class RelocationController extends ControllerBase {
    
    @Autowired
    private RelocationService relocationService;

    /**
     * <b>URL: /relocation/searchRelocationRequests</b>
     * <p>搜索搬迁申请</p>
     */
    @RequestMapping("searchRelocationRequests")
    @RestReturn(value=SearchRelocationRequestsResponse.class)
    public RestResponse searchRelocationRequests(SearchRelocationRequestsCommand cmd) {
        
        RestResponse response = new RestResponse(relocationService.searchRelocationRequests(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /relocation/getRelocationRequestDetail</b>
     * <p>获取搬迁申请详情</p>
     */
    @RequestMapping("getRelocationRequestDetail")
    @RestReturn(value=RelocationRequestDTO.class)
    public RestResponse getRelocationRequestDetail(GetRelocationRequestDetailCommand cmd) {

        RestResponse response = new RestResponse(relocationService.getRelocationRequestDetail(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /relocation/getRelocationUserInfo</b>
     * <p>获取搬迁用户信息</p>
     */
    @RequestMapping("getRelocationUserInfo")
    @RestReturn(value=RelocationInfoDTO.class)
    public RestResponse getRelocationUserInfo(GetRelocationUserInfoCommand cmd) {

        RestResponse response = new RestResponse(relocationService.getRelocationUserInfo(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /relocation/requestRelocation</b>
     * <p>申请 搬迁</p>
     */
    @RequestMapping("requestRelocation")
    @RestReturn(value=RelocationRequestDTO.class)
    public RestResponse requestRelocation(RequestRelocationCommand cmd) {

        RestResponse response = new RestResponse(relocationService.requestRelocation(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
