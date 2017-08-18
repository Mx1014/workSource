// @formatter:off
package com.everhomes.address;

import java.util.List;
import java.util.Map;

import com.everhomes.asset.AddressIdAndName;
import org.jooq.Record2;
import org.jooq.Result;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.address.AddressDTO;
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

    /**
     * 根据id列表查询address
     * @param namespaceId
     * @param ids
     * @return
     */
    List<Address> listAddressByIds(Integer namespaceId, List<Long> ids);

    List<AddressDTO> listAddressByBuildingName(Integer namespaceId, Long communityId, String buildingName);
	Address findAddressByBuildingApartmentName(Integer namespaceId, Long communityId, String buildingName, String apartmentName);
	List<Address> listAddressByNamespaceType(Integer namespaceId, Long communityId, String namespaceType);
	Map<Byte, Integer> countApartmentByLivingStatus(Long communityId);
	Integer countApartment(Long communityId);
	void updateOrganizationOwnerAddress(Long id);
	void updateOrganizationAddress(Long id);
	void updateOrganizationAddressMapping(Long id);

    List<AddressIdAndName> findAddressByPossibleName(Integer currentNamespaceId, Long ownerId, String buildingName, String apartmentName);
}
