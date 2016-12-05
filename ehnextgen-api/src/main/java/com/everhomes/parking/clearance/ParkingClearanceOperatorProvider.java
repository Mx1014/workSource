// @formatter:off
package com.everhomes.parking.clearance;

import java.util.List;

/**
 * Created by xq.tian on 2016/12/2.
 */
public interface ParkingClearanceOperatorProvider {

    /**
     * 创建operator
     */
    long createClearanceOperator(ParkingClearanceOperator operator);

    /**
     * 根据id删除operator
     * 直接删除记录
     */
    void deleteClearanceOperatorById(Integer namespaceId, Long id);

    /**
     * 分页获取operator
     */
    List<ParkingClearanceOperator> listClearanceOperator(Integer namespaceId, Long communityId, Long parkingLotId,
                                                         String operatorType, int pageSize, Long pageAnchor);
}
