package com.everhomes.enterprise;

import java.util.List;

import com.everhomes.rest.enterprise.*;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.community.CommunityDoc;
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

    /**
     * 查询该域空间下不在该项目中的所有企业
     * @param cmd
     * @return
     */
    ListEnterpriseResponse listEnterpriseNoReleaseWithCommunityId(listEnterpriseNoReleaseWithCommunityIdCommand cmd);

    /**
     * 根据项目编号communityId来查询在该项目下的所有公司
     * @param cmd
     * @return
     */
    ListEnterprisesResponse listEnterprisesByCommunityId(ListEnterpriseByCommunityIdCommand cmd);

    /**
     * 根据组织ID和项目Id来删除该项目下面的公司
     * @param cmd
     */
    void deleteEnterpriseByOrgIdAndCommunityId(DeleteEnterpriseCommand cmd);


}
