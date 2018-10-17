package com.everhomes.aclink;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.aclink.FirmwarePackageDTO;
import com.everhomes.rest.aclink.ListFirmwarePackageCommand;

public interface AclinkFirmwareProvider {

    Long createAclinkFirmware(AclinkFirmware obj);

    void updateAclinkFirmware(AclinkFirmware obj);

    void deleteAclinkFirmware(AclinkFirmware obj);

    AclinkFirmware getAclinkFirmwareById(Long id);

    List<AclinkFirmware> queryAclinkFirmware(ListingLocator locator, int count,
            ListingQueryBuilderCallback queryBuilderCallback);

    AclinkFirmware queryAclinkFirmwareMax();
    //add by liqingyan
    Long createFirmwarePackage (AclinkFirmwarePackage obj);

    Long updateFirmwarePackage (AclinkFirmwarePackage obj);

    AclinkFirmwarePackage findPackageById(Long id);

    List<FirmwarePackageDTO> listFirmwarePackage (CrossShardListingLocator locator, int count, ListFirmwarePackageCommand cmd);

    Long createFirmwareNew(AclinkFirmwareNew obj);

    Long updateFirmwareNew (AclinkFirmwareNew obj);

    AclinkFirmwareNew findFirmwareById(Long id);

}
