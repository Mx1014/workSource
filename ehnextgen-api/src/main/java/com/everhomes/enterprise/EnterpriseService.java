package com.everhomes.enterprise;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.community.CommunityDoc;
import com.everhomes.rest.enterprise.CreateEnterpriseCommand;
import com.everhomes.rest.enterprise.DeleteEnterpriseCommand;
import com.everhomes.rest.enterprise.EnterpriseCommunityDTO;
import com.everhomes.rest.enterprise.EnterpriseDTO;
import com.everhomes.rest.enterprise.GetEnterpriseInfoCommand;
import com.everhomes.rest.enterprise.ImportEnterpriseDataCommand;
import com.everhomes.rest.enterprise.ListEnterpriseByCommunityIdCommand;
import com.everhomes.rest.enterprise.ListEnterpriseCommunityResponse;
import com.everhomes.rest.enterprise.ListEnterpriseResponse;
import com.everhomes.rest.enterprise.ListUserRelatedEnterprisesCommand;
import com.everhomes.rest.enterprise.SearchEnterpriseCommand;
import com.everhomes.rest.enterprise.SearchEnterpriseCommunityCommand;
import com.everhomes.rest.enterprise.SetCurrentEnterpriseCommand;
import com.everhomes.rest.enterprise.UpdateContactorCommand;
import com.everhomes.rest.enterprise.UpdateEnterpriseCommand;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.user.User;

public interface EnterpriseService {
    public ListEnterpriseResponse listEnterpriseByCommunityId(ListEnterpriseByCommunityIdCommand cmd);
    
    //Admin approve enterprise to one community
    void approve(User admin, Long enterpriseId, Long communityId);

    void requestToJoinCommunity(User admin, Long enterpriseId, Long communtyId);

    void reject(User admin, Long enterpriseId, Long communityId);

    void revoke(User admin, Long enterpriseId, Long communityId);
    
    void importToJoinCommunity(List<Enterprise> enterprises, EnterpriseCommunityDTO community);
    List<EnterpriseCommunity> listEnterpriseEnrollCommunties(CrossShardListingLocator locator, Long enterpriseId,
            int pageSize);

    List<Enterprise> listEnterpriseByCommunityId(ListingLocator locator,String enterpriseName, Long communityId, Integer status, int pageSize);

    ListEnterpriseCommunityResponse listEnterpriseEnrollCommunties(GetEnterpriseInfoCommand cmd);

    EnterpriseCommunity getEnterpriseCommunityById(Long id);

    List<EnterpriseDTO> listEnterpriseByPhone(String phone);
    
    List<EnterpriseDTO> listUserRelatedEnterprises(ListUserRelatedEnterprisesCommand cmd);

    List<CommunityDoc> searchCommunities(SearchEnterpriseCommunityCommand cmd);

    ListEnterpriseResponse searchEnterprise(SearchEnterpriseCommand cmd);

    Enterprise getEnterpriseById(Long id);

    void inviteToJoinCommunity(Long enterpriseId, Long communityId);

	public void setCurrentEnterprise(SetCurrentEnterpriseCommand cmd);

	void updateContactor(UpdateContactorCommand cmd);
	
	void deleteEnterprise(DeleteEnterpriseCommand cmd);
	
	ImportDataResponse importEnterpriseData(MultipartFile mfile, Long userId,ImportEnterpriseDataCommand cmd);
	
	EnterpriseDTO findEnterpriseByAddress(Long addressId);
}
