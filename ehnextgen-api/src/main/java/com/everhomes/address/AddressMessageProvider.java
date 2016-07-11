package com.everhomes.address;

import java.util.List;

import com.everhomes.listing.ListingLocator;

public interface AddressMessageProvider {
    void CreateAddressMessage(AddressMessage adMessage);
    List<AddressMessage> findMessageByAddressId(long addressId, ListingLocator locator, int count);
}
