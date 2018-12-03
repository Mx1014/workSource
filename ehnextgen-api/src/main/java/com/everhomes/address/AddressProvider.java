// @formatter:off
package com.everhomes.address;

import com.everhomes.asset.AddressIdAndName;
import com.everhomes.building.Building;
import com.everhomes.community.Community;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.openapi.ContractBuildingMapping;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.rest.address.ApartmentAbstractDTO;
import com.everhomes.rest.address.ApartmentBriefInfoDTO;
import com.everhomes.rest.address.ApartmentDTO;
import com.everhomes.rest.address.GetApartmentNameByBuildingNameDTO;
import com.everhomes.rest.community.ListApartmentsInCommunityCommand;
import com.everhomes.rest.organization.pm.reportForm.ApartmentReportFormDTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

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

    List<Address> listAddressOnlyByIds(List<Long> ids);

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
    ContractBuildingMapping findContractBuildingMappingByContractId(Long contractId);
	void updateContractBuildingMapping(ContractBuildingMapping contractBuildingMapping);

    String getAddressNameById(Long addressId);

    int changeAddressLivingStatus(Long addressId, Byte status);

  //Byte getAddressLivingStatus(Long addressId);

    Byte getAddressLivingStatus(Long addressId,String addressName);
	int changeAddressLivingStatus(Long id, String address, byte code);
	Address findNotInactiveAddressByBuildingApartmentName(Integer namespaceId, Long communityId, String buildingName,
			String apartmentName);
	long createAddress3(Address address);
	void createAddressArrangement(AddressArrangement arrangement);
	AddressArrangement findActiveAddressArrangementByAddressId(Long addressId);
	void deleteAddressArrangement(Long id);
	AddressArrangement findAddressArrangementById(Long id);
	AddressArrangement findActiveAddressArrangementByTargetId(Long addressId);
	String findApartmentNameById(long addressId);
	Byte findArrangementOperationTypeByAddressId(Long addressId);
	List<AddressArrangement> listActiveAddressArrangementToday(Timestamp today);
//	AddressArrangement findActiveAddressArrangementByOriginalId(Long addressId);
	void updateAddressArrangement(AddressArrangement arrangement);
	List<AddressArrangement> findActiveAddressArrangementByOriginalIdV2(Long id);
	List<AddressArrangement> findActiveAddressArrangementByTargetIdV2(Long id);
	Integer countApartmentNumberByBuildingName(Long communityId, String buildingName);
	Integer countRelatedEnterpriseCustomerNumber(Long communityId,Long buildingId);
	Integer countRelatedOrganizationOwnerNumber(Long communityId, Long buildingId);
	List<Address> findActiveAddressByCommunityId(Long id);
	List<Address> listApartmentsInCommunity(ListApartmentsInCommunityCommand cmd);
	Byte getAddressLivingStatusByAddressId(Long addressId);
	List<Address> findActiveAddressByBuildingNameAndCommunityId(String buildingName, Long communityId);

    List<Long> listThirdPartRelatedAddresses(String code, List<String> addressIds);
    List<Address> findActiveApartmentsByBuildingId(Long buildingId);
    
    /**
     * 根据第三方数据的id列表查询房源
     * @param thirdPartyType，对接第三方的名称，例如：瑞安CM就填“ruian_cm”
     * @param thirdPartyToken，第三方数据id
     * @return
     */ 
    Address findApartmentByThirdPartyId(String thirdPartyType,String thirdPartyToken);
    /**
     * 根据第三方数据的id列表查询楼宇
     * @param thirdPartyType，对接第三方的名称，例如：瑞安CM就填“ruian_cm”
     * @param thirdPartyToken，第三方数据id
     * @return
     */
    Building findBuildingByThirdPartyId(String thirdPartyType,String thirdPartyToken);
    /**
     * 根据第三方数据的id列表查询园区
     * @param thirdPartyType，对接第三方的名称，例如：瑞安CM就填“ruian_cm”
     * @return
     */
    Community findCommunityByThirdPartyId(String thirdPartyType,String thirdPartyToken);
	int getTotalApartmentCount();

	List<ApartmentReportFormDTO> findActiveApartments(int startIndex, int pageSize);
	
	/**
	 * 分页获取地址信息中城市ID不在eh_regions表里的数据，这些数据都是有问题的，需要修复；
	 * @param namespaceId 域空间ID
	 * @param pageAnchor 分页锚点
	 * @param pageSize 每页数量
	 * @return 地址数据
	 */
	List<Address> listAddressesOfInvalidCity(Integer namespaceId, Long pageAnchor, Integer pageSize);
	
	/**
	 * 更新地址信息中的城市ID，为了减少干扰，只更换城市ID
	 * @param addressId 地址ID
	 * @param cityId 城市ID
	 */
	void updateAddressOfCityId(Long addressId, Long cityId);
	List<ApartmentBriefInfoDTO> listApartmentsByMultiStatus(Integer namespaceId, Long communityId, String buildingName,
			String apartment, List<Byte> livingStatus, Long pageAnchor, int pageSize);
	List<Address> findActiveAddress(int startIndex, int pageSize);
}
