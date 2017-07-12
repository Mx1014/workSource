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
     * 删除operator
     * 直接删除记录
     */
    void deleteClearanceOperator(ParkingClearanceOperator operator);

    /**
     * 分页获取operator
     */
    List<ParkingClearanceOperator> listClearanceOperator(Integer namespaceId, Long communityId, Long parkingLotId,
                                                         String operatorType, int pageSize, Long pageAnchor);

    /**
     * 根据id查询
     */
    ParkingClearanceOperator findById(Long id);

    /**
     * 根据停车场和用户查询记录
     * @param parkingLotId
     * @param userId
     * @return
     */
    ParkingClearanceOperator findByParkingLotIdAndUid(Long parkingLotId, Long userId, String operatorType);
}
