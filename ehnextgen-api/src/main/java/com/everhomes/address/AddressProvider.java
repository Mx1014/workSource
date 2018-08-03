// @formatter:off
package com.everhomes.address;

import java.util.List;
import java.util.Map;

import com.everhomes.rest.address.*;

import com.everhomes.asset.AddressIdAndName;

import org.jooq.Record2;
import org.jooq.Result;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractBuildingMapping;

public interface AddressProvider {
    void createAddress(Address address);
    void createAddress2(Address address);
    void updateAddress(Address address);
    void deleteAddress(Address address);
    void deleteAddressById(long id);

    /**
     * 根据addressId来查询eh_addresses表中对应的楼栋和门牌信息
     * @param id
     * @return
     */
    Address findAddressById(long id);

    Address findGroupAddress(Long groupId);

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
	Address findActiveAddressByBuildingApartmentName(Integer namespaceId, Long communityId, String buildingName, String apartmentName);
	List<Address> listAddressByNamespaceType(Integer namespaceId, Long communityId, String namespaceType);
	List<Address> listAddressByNamespaceType(Integer namespaceId, String namespaceType);
	Map<Byte, Integer> countApartmentByLivingStatus(Long communityId);
	Integer countApartment(Long communityId);
	void updateOrganizationOwnerAddress(Long id);
	void updateOrganizationAddress(Long id);
	void updateOrganizationAddressMapping(Long id);

    Address findAddressByNamespaceTypeAndName(String namespaceType, String namespaceToken);

    List<ApartmentAbstractDTO> listAddressByBuildingApartmentName(Integer namespaceId, Long communityId, String buildingName, String apartmentName, Byte livingStatus, CrossShardListingLocator locator, int count);


    List<AddressIdAndName> findAddressByPossibleName(Integer currentNamespaceId, Long ownerId, String buildingName, String apartmentName);


    List<GetApartmentNameByBuildingNameDTO> getApartmentNameByBuildingName(String buildingName, Long communityId, Integer currentNamespaceId);

    void createAddressAttachment(AddressAttachment attachment);
    void updateAddressAttachment(AddressAttachment attachment);
    void deleteApartmentAttachment(Long id);
    AddressAttachment findByAddressAttachmentId(Long id);
    List<AddressAttachment> listAddressAttachments(Long addressId);

    String findLastVersionByNamespace(Integer namespaceId, Long communityId);

    /**
     * 根据门牌地址Id集合addressIds进行批量删除门牌地址
     * @param addressIds
     */
    void betchDisclaimAddress(List<Long> addressIds);

    /**
     * 根据id的集合来批量的查询eh_addresses表中信息
     * @param ids
     * @return
     */
    List<Address> findAddressByIds(List<Long> ids,Integer namespaceId);

    /**
     * 根据buildingName和CommunityId来查询eh_addersses表中的门牌的数量
     * @param buildingName
     * @param communityId
     * @return
     */
    int getApartmentCountByBuildNameAndCommunityId(String buildingName,Long communityId);
    
	List<ContractBuildingMapping> findContractBuildingMappingByAddressId(Long addressId);
	void updateContractBuildingMapping(ContractBuildingMapping contractBuildingMapping);

    String getAddressNameById(Long addressId);

    int changeAddressLivingStatus(Long addressId, Byte status);

  //Byte getAddressLivingStatus(Long addressId);
    Byte getAddressLivingStatus(Long addressId,String addressName);
	int changeAddressLivingStatus(Long id, String address, byte code);
	Address findNotInactiveAddressByBuildingApartmentName(Integer namespaceId, Long communityId, String buildingName,
			String apartmentName);
}
