// @formatter:off
package com.everhomes.address;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.everhomes.rest.address.ApartmentDTO;
import com.everhomes.rest.address.BuildingDTO;
import com.everhomes.rest.address.ClaimAddressCommand;
import com.everhomes.rest.address.ClaimedAddressInfo;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.address.CommunitySummaryDTO;
import com.everhomes.rest.address.CreateServiceAddressCommand;
import com.everhomes.rest.address.DeleteServiceAddressCommand;
import com.everhomes.rest.address.DisclaimAddressCommand;
import com.everhomes.rest.address.ListAddressByKeywordCommand;
import com.everhomes.rest.address.ListAddressByKeywordCommandResponse;
import com.everhomes.rest.address.ListAddressCommand;
import com.everhomes.rest.address.ListApartmentByBuildingNameCommand;
import com.everhomes.rest.address.ListApartmentByBuildingNameCommandResponse;
import com.everhomes.rest.address.ListBuildingByKeywordCommand;
import com.everhomes.rest.address.ListCommunityByKeywordCommand;
import com.everhomes.rest.address.ListNearbyCommunityCommand;
import com.everhomes.rest.address.ListNearbyMixCommunitiesCommand;
import com.everhomes.rest.address.ListNearbyMixCommunitiesCommandResponse;
import com.everhomes.rest.address.ListPropApartmentsByKeywordCommand;
import com.everhomes.rest.address.SearchCommunityCommand;
import com.everhomes.rest.address.SuggestCommunityCommand;
import com.everhomes.rest.address.SuggestCommunityDTO;
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
	void importCommunityInfos(MultipartFile[] files);
	void importAddressInfos(MultipartFile[] files);
	
	UserServiceAddressDTO createServiceAddress(CreateServiceAddressCommand cmd);
    
    FamilyDTO claimAddressV2(ClaimAddressCommand cmd);
    
    void deleteServiceAddress(DeleteServiceAddressCommand cmd);
    
    List<ApartmentDTO> listUnassignedApartmentsByBuildingName(ListApartmentByBuildingNameCommand cmd);
    
    void importParkAddressData(ImportAddressCommand cmd,MultipartFile[] files);
    
    void importAddressData(MultipartFile[] files);
    
    ListNearbyMixCommunitiesCommandResponse listNearbyMixCommunities(ListNearbyMixCommunitiesCommand cmd);
    
}
