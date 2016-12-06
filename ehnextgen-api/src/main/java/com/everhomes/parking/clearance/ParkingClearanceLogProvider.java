// @formatter:off
package com.everhomes.parking.clearance;

import java.util.List;

/**
 * Created by xq.tian on 2016/12/2.
 */
public interface ParkingClearanceLogProvider {

    /**
     * 创建同行log
     */
    long createClearanceLog(ParkingClearanceLog log);

    /**
     * 搜索放行记录
     * @param qo    查询条件对象
     */
    List<ParkingClearanceLog> searchClearanceLog(ParkingClearanceLogQueryObject qo);

    /**
     * 根据id查找
     */
    ParkingClearanceLog findById(Long id);

    /**
     * 更新log
     */
    void updateClearanceLog(ParkingClearanceLog log);
}
