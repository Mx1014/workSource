// @formatter:off
package com.everhomes.parking.clearance;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.parking.clearance.*;
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
     * <p>新增车辆放行的申请人员或处理人员</p>
     * <b>URL: /clearance/createClearanceOperator</b>
     */
    @RequestMapping("createClearanceOperator")
    @RestReturn(ParkingClearanceOperatorDTO.class)
    public RestResponse createClearanceOperator(CreateClearanceOperatorCommand cmd) {
        return response(parkingClearanceService.createClearanceOperator(cmd));
    }

    /**
     * <p>删除车辆放行的申请人员或处理人员</p>
     * <b>URL: /clearance/deleteClearanceOperator</b>
     */
    @RequestMapping("deleteClearanceOperator")
    @RestReturn(String.class)
    public RestResponse deleteClearanceOperator(DeleteClearanceOperatorCommand cmd) {
        parkingClearanceService.deleteClearanceOperator(cmd);
        return success();
    }

    /**
     * <p>车辆放行的申请人员或处理人员列表</p>
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
     * <p>搜索车辆放行log</p>
     * <b>URL: /clearance/searchClearanceLog</b>
     */
    @RequestMapping("searchClearanceLog")
    @RestReturn(SearchClearanceLogsResponse.class)
    public RestResponse searchClearanceLog(SearchClearanceLogCommand cmd) {
        return response(parkingClearanceService.searchClearanceLog(cmd));
    }

    /**
     * <p>根据申请人id列出车辆放行log</p>
     * <b>URL: /clearance/listClearanceLogByApplicant</b>
     */
    /*@RequestMapping("listClearanceLogByApplicant")
    @RestReturn(value = ParkingClearanceLogDTO.class, collection = true)
    public RestResponse listClearanceLogByApplicant(SearchClearanceLogCommand cmd) {
        return response(parkingClearanceService.searchClearanceLog(cmd));
    }*/

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
