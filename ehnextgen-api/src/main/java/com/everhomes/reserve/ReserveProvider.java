package com.everhomes.reserve;

import java.util.List;

/**
 * @author sw on 2017/12/12.
 */
public interface ReserveProvider {

    ReserveRule findReserveRuleById(Long id);

    void createReserveRule(ReserveRule reserveRule);

    void updateReserveRule(ReserveRule reserveRule);

    ReserveDiscountUser findReserveDiscountUserById(Long id);

    void createReserveDiscountUser(ReserveDiscountUser reserveDiscountUser);

    void updateReserveDiscountUser(ReserveDiscountUser reserveDiscountUser);

    ReserveOrderRule findReserveOrderRuleById(Long id);

    void createReserveOrderRule(ReserveOrderRule reserveOrderRule);

    void updateReserveOrderRule(ReserveOrderRule reserveOrderRule);

    void createReserveCloseDate(ReserveCloseDate reserveCloseDate);

    void deleteReserveCloseDate(ReserveCloseDate reserveCloseDate);

    ReserveOrder findReserveOrderById(Long id);

    void createReserveOrder(ReserveOrder reserveOrder);

    void updateReserveOrder(ReserveOrder reserveOrder);

    List<ReserveOrder> searchReserveOrders(Integer namespaceId, String ownerType, Long ownerId,
                                           String resourceType, Long resourceId, Long startTime, Long endTime,
                                           String spaceNo, String applicantEnterpriseName, String keyword, Byte status,
                                           Long pageAnchor, Integer pageSize);
}
