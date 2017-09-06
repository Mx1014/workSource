// @formatter:off
package com.everhomes.address;

import java.util.List;

import com.everhomes.rest.address.*;

import com.everhomes.rest.enterprise.SearchEnterpriseCommunityCommand;
import org.springframework.web.multipart.MultipartFile;

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

    ListNearbyMixCommunitiesCommandV2Response listNearbyMixCommunitiesV2(ListNearbyMixCommunitiesCommand cmd);

    //获取注册中、已注册、关联最多的社区
    ListNearbyMixCommunitiesCommandV2Response listPopularCommunitiesWithType(SearchEnterpriseCommunityCommand cmd);
}
