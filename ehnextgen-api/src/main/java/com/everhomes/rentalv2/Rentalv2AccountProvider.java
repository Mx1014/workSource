package com.everhomes.rentalv2;

import com.everhomes.listing.ListingLocator;
import org.springframework.stereotype.Component;

import java.util.List;

public interface Rentalv2AccountProvider {

    List<Rentalv2PayAccount> listPayAccounts(Integer namespaceId, Long communityId, String resourceType, String sourceType,
                                             Long sourceId, ListingLocator locator, Integer pageSize);

    void deletePayAccount(Long id,Long communityId,String sourceType,Long sourceId);


}
