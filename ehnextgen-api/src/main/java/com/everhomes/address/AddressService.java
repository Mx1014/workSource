// @formatter:off
package com.everhomes.address;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.address.*;
import com.everhomes.rest.community.ListApartmentEnterpriseCustomersCommand;
import com.everhomes.rest.customer.EnterpriseCustomerDTO;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.asset.AddressIdAndName;
import com.everhomes.community.Community;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.address.admin.CorrectAddressAdminCommand;
import com.everhomes.rest.address.admin.ImportAddressCommand;
import com.everhomes.rest.community.CommunityDoc;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.openapi.UserServiceAddressDTO;
import com.everhomes.util.Tuple;

public interface AddressService {
    SuggestCommunityDTO suggestCommunity(SuggestCommunityCommand cmd);
    Tuple<Integer, List<CommunitySummaryDTO>> listSuggestedCommunities();
    Tuple<Integer, List<CommunityDTO>> listNearbyCommunities(ListNearbyCommunityCommand cmd);
    Tuple<Integer, List<CommunityDTO>> listCommunitiesByKeyword(ListCommunityByKeywordCommand cmd);
    Tuple<Integer, List<BuildingDTO>> listBuildingsByKeyword(ListBuildingByKeywordCommand cmd);
    Tuple<Integer, List<ApartmentDTO>> listApartmentsByKeyword(ListPropApartmentsByKeywordCommand cmd);

    Tuple<Integer, List<ApartmentDTO>> listApartmentsByKeywordNew(ListPropApartmentsByKeywordCommand cmd);

    ClaimedAddressInfo claimAddress(ClaimAddressCommand cmd);
    void disclaimAddress(DisclaimAddressCommand cmd);
    
    Tuple<Integer, List<Address>> listAddressByCommunityId(ListAddressCommand cmd);
    
    ListAddressByKeywordCommandResponse listAddressByKeyword(ListAddressByKeywordCommand cmd);
    
    void correctAddress(CorrectAddressAdminCommand cmd);
    
    List<CommunityDoc> searchCommunities(SearchCommunityCommand cmd);
    
    ListApartmentByBuildingNameCommandResponse listApartmentsByBuildingName(ListApartmentByBuildingNameCommand cmd);
    ListApartmentByBuildingNameCommandResponse listCommunityApartmentsByBuildingName(ListApartmentByBuildingNameCommand cmd);
	void importCommunityInfos(MultipartFile[] files);
	void importAddressInfos(MultipartFile[] files);
	
	UserServiceAddressDTO createServiceAddress(CreateServiceAddressCommand cmd);
    
    FamilyDTO claimAddressV2(ClaimAddressCommand cmd);
    
    void deleteServiceAddress(DeleteServiceAddressCommand cmd);
    
    List<ApartmentDTO> listUnassignedApartmentsByBuildingName(ListApartmentByBuildingNameCommand cmd);
    
    void importParkAddressData(ImportAddressCommand cmd,MultipartFile[] files);
    
    void importAddressData(MultipartFile[] files);

    ListNearbyMixCommunitiesCommandResponse listNearbyMixCommunities(ListNearbyMixCommunitiesCommand cmd);

    List<AddressDTO> listAddressByBuildingName(ListApartmentByBuildingNameCommand cmd);
    
    AddressDTO getApartmentByBuildingApartmentName(GetApartmentByBuildingApartmentNameCommand cmd);
	Tuple<Integer, List<ApartmentFloorDTO>> listApartmentFloor(ListApartmentFloorCommand cmd);
	Tuple<Integer, List<BuildingDTO>> listBuildingsByKeywordForBusiness(ListBuildingByKeywordCommand cmd);
	Tuple<Integer, List<ApartmentFloorDTO>> listApartmentFloorForBusiness(ListApartmentFloorCommand cmd);
	Tuple<Integer, List<ApartmentDTO>> listApartmentsByKeywordForBusiness(ListPropApartmentsByKeywordCommand cmd);
	Object importParkAddressData(ImportAddressCommand cmd, MultipartFile file);

    List<AddressIdAndName> findAddressByPossibleName(Integer currentNamespaceId, Long ownerId, String buildingName, String apartmentName);

    List<GetApartmentNameByBuildingNameDTO> getApartmentNameByBuildingName(GetApartmentNameByBuildingNameCommand cmd);


    ListNearbyMixCommunitiesCommandV2Response listNearbyMixCommunitiesV2(ListNearbyMixCommunitiesCommand cmd);

    //获取注册中、已注册、关联最多的社区(园区/xiaoqu)
    ListNearbyMixCommunitiesCommandV2Response listPopularCommunitiesWithType(ListNearbyMixCommunitiesCommand cmd);

    public ListNearbyMixCommunitiesCommandResponse listMixCommunitiesByDistance(ListNearbyMixCommunitiesCommand cmd, ListingLocator locator, int pageSize);

    List<Community> listMixCommunitiesByDistanceWithNamespaceId(ListNearbyMixCommunitiesCommand cmd, ListingLocator locator, int pageSize);

    ApartmentAttachmentDTO uploadApartmentAttachment(UploadApartmentAttachmentCommand cmd);
    void deleteApartmentAttachment(DeleteApartmentAttachmentCommand cmd);
    List<ApartmentAttachmentDTO> listApartmentAttachments(ListApartmentAttachmentsCommand cmd);
    List<EnterpriseCustomerDTO> listApartmentEnterpriseCustomers(ListApartmentEnterpriseCustomersCommand cmd);
    void downloadApartmentAttachment(DownloadApartmentAttachmentCommand cmd);

    /**
     * 根据门牌地址id集合 批量删除门牌地址（标准版）
     * @param cmd
     */
    void betchDisclaimAddress(BetchDisclaimAddressCommand cmd);

	void createAddressArrangement(CreateAddressArrangementCommand cmd);
	AddressArrangementDTO listAddressArrangement(ListAddressArrangementCommand cmd);
	void updateAddressArrangement(UpdateAddressArrangementCommand cmd);
	void deleteAddressArrangement(DeleteAddressArrangementCommand cmd);
	List<HistoryApartmentDTO> getHistoryApartment(GetHistoryApartmentCommand cmd);
	void excuteAddressArrangementOnTime();
	void excuteAddressArrangement(ExcuteAddressArrangementCommand cmd);
	void exportApartmentsInBuilding(ListPropApartmentsByKeywordCommand cmd, HttpServletResponse httpServletResponse);
	
	/**
	 * 在现网中有些地址中的城市ID的ID在region表中找不到（原因未明），故需要进行修复；
	 * @param namespaceId
	 */
	void fixInvalidCityInAddresses(Integer namespaceId);
}
