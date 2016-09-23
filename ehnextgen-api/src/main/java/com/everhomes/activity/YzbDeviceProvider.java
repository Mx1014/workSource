package com.everhomes.activity;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface YzbDeviceProvider {

    Long createYzbDevice(YzbDevice obj);

    void updateYzbDevice(YzbDevice obj);

    void deleteYzbDevice(YzbDevice obj);

    YzbDevice getYzbDeviceById(Long id);

    List<YzbDevice> queryYzbDevices(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);

    YzbDevice findYzbDeviceById(String deviceId);

}
