package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface AclinkFirmwareProvider {

    Long createAclinkFirmware(AclinkFirmware obj);

    void updateAclinkFirmware(AclinkFirmware obj);

    void deleteAclinkFirmware(AclinkFirmware obj);

    AclinkFirmware getAclinkFirmwareById(Long id);

    List<AclinkFirmware> queryAclinkFirmware(ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    AclinkFirmware queryAclinkFirmwareMax();

}
