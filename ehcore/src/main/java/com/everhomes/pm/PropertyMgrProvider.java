// @formatter:off
package com.everhomes.pm;

import java.util.List;






import com.everhomes.util.SortOrder;
import com.everhomes.util.Tuple;

@SuppressWarnings("unchecked")
public interface PropertyMgrProvider {

	/**
	 * Query property members by specific <code>targetType</code> and <code>targetId</code>
	 * @param targetType {@link PmTargetType}
	 * @param targetId target user id if target_type is a user
	 * @return property members
	 */
	public List<CommunityPmMember> findPmMemberByTargetTypeAndId(String targetType, long targetId);
	
	/**
	 * Query property members by specific <code>communityId</code> and <code>targetType</code> and <code>targetId</code>
	 * @param communityId the community id where the property member managed
	 * @param targetType {@link PmTargetType}
	 * @param targetId target user id if target_type is a user
	 * @return property members
	 */
	public List<CommunityPmMember> listUserCommunityPmMembers(Long userId);
	public List<CommunityPmMember> findPmMemberByCommunityAndTarget(long communityId, String targetType, long targetId);

	public void createPropMember(CommunityPmMember communityPmMember);
	public void updatePropMember(CommunityPmMember communityPmMember);
	public void deletePropMember(CommunityPmMember communityPmMember);
	public void deletePropMember(long id);
	public CommunityPmMember findPropMemberById(long id);
	public List<CommunityPmMember> listCommunityPmMembers(Long communityId, String contactToken,Integer pageOffset,Integer pageSize);
	
	public void createPropAddressMapping(CommunityAddressMapping communityAddressMapping);
	public void updatePropAddressMapping(CommunityAddressMapping communityAddressMapping);
	public void deletePropAddressMapping(CommunityAddressMapping communityAddressMapping);
	public void deletePropAddressMapping(long id);
	public CommunityAddressMapping findPropAddressMappingById(long id);
	public CommunityAddressMapping findPropAddressMappingByAddressId(Long communityId,Long addressId);
	public List<CommunityAddressMapping> listCommunityAddressMappings(Long communityId, Integer pageOffset,Integer pageSize);
	public List<CommunityAddressMapping> listCommunityAddressMappings(Long communityId);
	public Integer countCommunityAddressMappings(Long communityId);
	
	public void createPropBill(CommunityPmBill communityPmBill);
	public void updatePropBill(CommunityPmBill communityPmBill);
	public void deletePropBill(CommunityPmBill communityPmBill);
	public void deletePropBill(long id);
	public CommunityPmBill findPropBillById(long id);
	public List<CommunityPmBill> listCommunityPmBills(Long communityId, String dateStr,String address, Integer pageOffset,Integer pageSize);
	
	public void createPropBillItem(CommunityPmBillItem communityPmBillItem);
	public void updatePropBillItem(CommunityPmBillItem communityPmBillItem);
	public void deletePropBillItem(CommunityPmBillItem communityPmBillItem);
	public void deletePropBillItem(long id);
	public CommunityPmBillItem findPropBillItemById(long id);
	public List<CommunityPmBillItem> listCommunityPmBillItems(Long billId);
	
	public void createPropOwner(CommunityPmOwner communityPmOwner);
	public void updatePropOwner(CommunityPmOwner communityPmOwner);
	public void deletePropOwner(CommunityPmOwner communityPmOwner);
	public void deletePropOwner(long id);
	public CommunityPmOwner findPropOwnerById(long id);
	public List<CommunityPmOwner> listCommunityPmOwners(Long communityId, String address,String contactToken, Integer pageOffset,Integer pageSize);
    
	public List<CommunityPmTasks> findPmTaskEntityIdAndTargetId(Long communityId, Long entityId, String entityType, 
            Long targetId, String targetType, Byte status);
    public void createPmTask(CommunityPmTasks task);
    public List<PropInvitedUserDTO> listInvitedUsers(Long communityId, String contactToken, Long pageOffset, Long pageSize);

	public List<String> listPropBillDateStr(Long communityId);
}
