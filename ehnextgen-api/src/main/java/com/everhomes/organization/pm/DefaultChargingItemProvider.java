package com.everhomes.organization.pm;

import java.util.List;

/**
 * Created by ying.xiong on 2017/10/27.
 */
public interface DefaultChargingItemProvider {
    void createDefaultChargingItem(DefaultChargingItem defaultChargingItem);
    void updateDefaultChargingItem(DefaultChargingItem defaultChargingItem);
    DefaultChargingItem findById(Long id);
    List<DefaultChargingItem> listDefaultChargingItems(Integer namespaceId, Long communityId, String ownerType, Long ownerId);

    void createDefaultChargingItemProperty(DefaultChargingItemProperty property);
    void updateDefaultChargingItemProperty(DefaultChargingItemProperty property);
    List<DefaultChargingItemProperty> findByItemId(Long defaultChargingItemId);
}
