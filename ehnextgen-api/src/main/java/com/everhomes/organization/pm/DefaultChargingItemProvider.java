package com.everhomes.organization.pm;

import java.util.List;

/**
 * Created by ying.xiong on 2017/10/27.
 */
public interface DefaultChargingItemProvider {
    void createDefaultChargingItem(DefaultChargingItem defaultChargingItem);
    void updateDefaultChargingItem(DefaultChargingItem defaultChargingItem);
    DefaultChargingItem findById(Long id);
    
    //修复缺陷 #45399 【智富汇】【缴费管理】计价条款异常，加categoryId
    List<DefaultChargingItem> listDefaultChargingItems(Integer namespaceId, Long communityId, String ownerType, Long ownerId, Long categoryId);

    void createDefaultChargingItemProperty(DefaultChargingItemProperty property);
    void updateDefaultChargingItemProperty(DefaultChargingItemProperty property);
    List<DefaultChargingItemProperty> findByItemId(Long defaultChargingItemId);
    List<DefaultChargingItemProperty> findByPropertyId(Byte propertyType, Long propertyId, Byte meterType);
}
