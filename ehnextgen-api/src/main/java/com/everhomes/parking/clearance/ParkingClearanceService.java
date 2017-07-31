// @formatter:off
package com.everhomes.parking.clearance;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.parking.clearance.*;

import java.util.List;

/**
 * 车辆放行service
 * Created by xq.tian on 2016/12/2.
 */
public interface ParkingClearanceService {

    /**
     * 新增车辆放行的处理人员或申请人员
     */
    void createClearanceOperator(CreateClearanceOperatorCommand cmd);

    /**
     * 删除车辆放行的申请人员或处理人员
     */
    void deleteClearanceOperator(DeleteClearanceOperatorCommand cmd);

    /**
     * 车辆放行的申请人员或处理人员列表
     */
    ListClearanceOperatorResponse listClearanceOperator(ListClearanceOperatorCommand cmd);

    /**
     * 创建车辆放行log
     */
    ParkingClearanceLogDTO createClearanceLog(CreateClearanceLogCommand cmd);

    /**
     * 搜索车辆放行申请记录
     */
    SearchClearanceLogsResponse searchClearanceLog(SearchClearanceLogCommand cmd);

    /**
     * 检查用户的权限
     */
    CheckAuthorityResponse checkAuthority(CheckAuthorityCommand cmd);

	void exportClearanceLog(SearchClearanceLogCommand cmd, HttpServletResponse response);

	void deleteClearanceLog(DeleteClearanceLogCommand cmd);

    List<ParkingActualClearanceLogDTO> getActualClearanceLog(GetActualClearanceLogCommand cmd);
}
