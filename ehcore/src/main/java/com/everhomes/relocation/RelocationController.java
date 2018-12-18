// @formatter:off
package com.everhomes.relocation;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.relocation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestDoc(value="Relocation controller", site="relocation")
@RestController
@RequestMapping("/relocation")
public class RelocationController extends ControllerBase {
    
    @Autowired
    private RelocationService relocationService;

    @Autowired
    private RelocationConfigService relocationConfigService;

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

    /**
     * <b>URL: /relocation/admin/setRelocationConfig</b>
     * <p>配置物品放行参数</p>
     */
    @RequestMapping("admin/setRelocationConfig")
    @RestReturn(value=RelocationConfigDTO.class)
    public RestResponse setRelocationConfig(SetRelocationConfigCommand cmd) {
        RestResponse response = new RestResponse(relocationConfigService.setRelocationConfig(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /relocation/admin/getRelocationConfig</b>
     * <p>获取物品放行参数</p>
     */
    @RequestMapping("admin/getRelocationConfig")
    @RestReturn(value=RelocationConfigDTO.class)
    public RestResponse getRelocationConfig(GetRelocationConfigCommand cmd) {
        RelocationConfigDTO dto = relocationConfigService.searchRelocationConfigById(cmd);
        RestResponse response = new RestResponse(dto == null ? new RelocationConfigDTO() : dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /relocation/admin/queryRelocationStatistics</b>
     * <p>查询物品放行统计</p>
     */
    @RequestMapping("admin/queryRelocationStatistics")
    @RestReturn(value=QueryRelocationStatisticsResponse.class)
    public RestResponse queryRelocationStatistics(QueryRelocationStatisticsCommand cmd) {
        RelocationConfigDTO dto = relocationConfigService.searchRelocationConfigById(cmd);
        RestResponse response = new RestResponse(dto == null ? new RelocationConfigDTO() : dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
