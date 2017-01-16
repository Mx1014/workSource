package com.everhomes.business;

import java.util.List;

/**
 * Created by xq.tian on 2017/1/10.
 */
public interface BusinessPromotionProvider {

    /**
     * 获取域空间下的电商运营数据
     */
    List<BusinessPromotion> listBusinessPromotion(Integer namespaceId, int pageSize, Long pageAnchor);

    /**
     * 更新电商运营数据
     */
    void updateBusinessPromotion(BusinessPromotion promotion);

    /**
     * 创建电商运营数据
     */
    long createBusinessPromotion(BusinessPromotion promotion);

    /**
     * 根据id查询
     */
    BusinessPromotion findById(Long id);
}
