// @formatter:off
package com.everhomes.family;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.everhomes.address.Address;
import com.everhomes.group.Group;
import com.everhomes.group.GroupMember;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.user.UserGroup;
import com.everhomes.util.Tuple;

public interface FamilyProvider {
    Family findFamilyByAddressId(long addressId);
    
    void leaveFamilyAtAddress(Address address, UserGroup userGroup);
    Tuple<Integer, List<FamilyDTO>> findFamilByKeyword(String keyword);

    List<GroupMember> listFamilyRequests(Long userId, Long familyId ,Long pageOffset);

    List<FamilyDTO> listWaitApproveFamily(Long communityId, Long offset, Long pageSize);

    List<FamilyDTO> getUserFamiliesByUserId(long userId);
    
    List<Group> listCommunityFamily(Long communityId);
    
    int countUserByCommunityId(long communityId);
    
    int countFamiliesByCommunityId(long communityId);
    
    int countWaitApproveFamily(Long comunityId);
    
    FamilyDTO getFamilyById(Long familyId);
    
    List<GroupMember> listFamilyMembers(ListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback);

    List<GroupMember> listFamilyMembersByCityId(long cityId, int offset, int pageSize);
    
    List<GroupMember> listFamilyMembersByCommunityId(long communityId, int offset, int pageSize);
    
    List<GroupMember> listFamilyMembersByFamilyId(long groupId, int offset, int pageSize);

	List<FamilyBillingTransactions> listFBillTx(int resultCodeId, Long addresssId, int pageSize, long offset);

	FamilyBillingTransactions findFamilyBillTxByOrderId(Long orderId,Long familyId);

	FamilyBillingAccount findFamilyBillingAccountByOwnerId(Long entityId);

	void createFamilyBillingAccount(FamilyBillingAccount fAccount);

	void createFamilyBillingTransaction(FamilyBillingTransactions familyTx);

	void updateFamilyBillingAccount(FamilyBillingAccount fAccount);

	FamilyBillingAccount findFamilyBillingAccountById(Long targetAccountId);

	BigDecimal countFamilyBillTxChargeAmountInYear(Long addressId);

    List<GroupMember> listAllFamilyMembers(int offset, int pageSize);

    void updateFamily(Group group);

	Map<Long, Family> mapFamilyByAddressIds(List<Long> aptIdList);

    List<Family> listFamilByCommunityIdAndUid(Long communityId, Long uid);
    
}
