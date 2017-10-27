package com.everhomes.organization.pm;

import java.util.List;

/**
 * Created by ying.xiong on 2017/10/27.
 */
public interface DefaultChargingItemProvider {

    List<DefaultChargingItemProperty> findByItemId(Long defaultChargingItemId);
}
