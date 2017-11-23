// @formatter:off
package com.everhomes.parking.clearance;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.parking.clearance.*;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestDoc(value = "Parking clearance controller", site = "core")
@RestController
@RequestMapping("/clearance")
public class ParkingClearanceController extends ControllerBase {

    @Autowired
    private ParkingClearanceService parkingClearanceService;

    /**
     * <p>新增车辆放行的申请人员</p>
     * <b>URL: /clearance/createClearanceOperator</b>
     */
    @RequestMapping("createClearanceOperator")
    @RestReturn(String.class)
    public RestResponse createClearanceOperator(CreateClearanceOperatorCommand cmd) {
        parkingClearanceService.createClearanceOperator(cmd);
        return success();
    }

    /**
     * <p>删除车辆放行的申请人员</p>
     * <b>URL: /clearance/deleteClearanceOperator</b>
     */
    @RequestMapping("deleteClearanceOperator")
    @RestReturn(String.class)
    public RestResponse deleteClearanceOperator(DeleteClearanceOperatorCommand cmd) {
        parkingClearanceService.deleteClearanceOperator(cmd);
        return success();
    }

    /**
     * <p>车辆放行的申请人员</p>
     * <b>URL: /clearance/listClearanceOperator</b>
     */
    @RequestMapping("listClearanceOperator")
    @RestReturn(ListClearanceOperatorResponse.class)
    public RestResponse listClearanceOperator(ListClearanceOperatorCommand cmd) {
        return response(parkingClearanceService.listClearanceOperator(cmd));
    }

    /**
     * <p>创建车辆放行log</p>
     * <b>URL: /clearance/createClearanceLog</b>
     */
    @RequestMapping("createClearanceLog")
    @RestReturn(ParkingClearanceLogDTO.class)
    public RestResponse createClearanceLog(CreateClearanceLogCommand cmd) {
        return response(parkingClearanceService.createClearanceLog(cmd));
    }

    /**
     * <p>删除车辆放行log</p>
     * <b>URL: /clearance/deleteClearanceLog</b>
     */
    @RequestMapping("deleteClearanceLog")
    @RestReturn(String.class)
    public RestResponse deleteClearanceLog(DeleteClearanceLogCommand cmd) {
        parkingClearanceService.deleteClearanceLog(cmd);
        return success();
    }

    /**
     * <p>搜索车辆放行log</p>
     * <b>URL: /clearance/searchClearanceLog</b>
     */
    @RequestMapping("searchClearanceLog")
    @RestReturn(SearchClearanceLogsResponse.class)
    public RestResponse searchClearanceLog(SearchClearanceLogCommand cmd) {
        return response(parkingClearanceService.searchClearanceLog(cmd));
    }

    /**
     * <p>获取实际来访记录</p>
     * <b>URL: /clearance/getActualClearanceLog</b>
     */
    @RequestMapping("getActualClearanceLog")
    @RestReturn(value = ParkingActualClearanceLogDTO.class, collection = true)
    public RestResponse getActualClearanceLog(GetActualClearanceLogCommand cmd) {
        return response(parkingClearanceService.getActualClearanceLog(cmd));
    }

    /**
     * <p>同步实际来访记录</p>
     * <b>URL: /clearance/sychnLogs</b>
     */
    @RequestMapping("sychnLogs")
    @RestReturn(String.class)
    public RestResponse sychnLogs(SearchClearanceLogCommand cmd) {
        parkingClearanceService.sychnLogs(cmd);
        return new RestResponse();
    }
    
    /**
     * <p>导出车辆放行log</p>
     * <b>URL: /clearance/exportClearanceLog</b>
     */
    @RequestMapping("exportClearanceLog")
    @RestReturn(String.class)
    public RestResponse exportClearanceLog(SearchClearanceLogCommand cmd, HttpServletResponse response) {
    	parkingClearanceService.exportClearanceLog(cmd, response);
    	return new RestResponse();
    }

    /**
     * <p>验证当前用户是否有当前操作的权限</p>
     * <b>URL: /clearance/checkAuthority</b>
     */
    @RequestMapping("checkAuthority")
    @RestReturn(CheckAuthorityResponse.class)
    public RestResponse checkAuthority(CheckAuthorityCommand cmd) {
        return response(parkingClearanceService.checkAuthority(cmd));
    }

    private RestResponse success() {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    private RestResponse response(Object o) {
        RestResponse response = new RestResponse(o);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }
}
