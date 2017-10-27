package com.everhomes.organization.pm;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by ying.xiong on 2017/10/27.
 */
@Component
public class DefaultChargingItemProviderImpl implements DefaultChargingItemProvider {
    @Override
    public List<DefaultChargingItemProperty> findByItemId(Long defaultChargingItemId) {
        return null;
    }
}
