// @formatter:off
package com.everhomes.address;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.address.ApartmentDTO;

public interface AddressProvider {
    void createAddress(Address address);
    void createAddress2(Address address);
    void updateAddress(Address address);
    void deleteAddress(Address address);
    void deleteAddressById(long id);
    Address findAddressById(long id);
    Address findApartmentAddress(Integer namespaceId, long communityId, String buildingName, String apartmentName);
    
    List<Address> queryAddress(CrossShardListingLocator locator, int count, 
        ListingQueryBuilderCallback queryBuilderCallback);
    
    List<ApartmentDTO> listApartmentsByBuildingName(long communityId, String buildingName , int offset , int size);
    
    int countApartmentsByBuildingName(long communityId, String buildingName);
    
    Address findAddressByUuid(String uuid);
    Address findAddressByRegionAndAddress(Long cityId, Long areaId, String address);
	Address findAddressByAddress(String address);
	Address findAddressByCommunityAndAddress(Long cityId, Long areaId, Long communityId, String addressName);
}
