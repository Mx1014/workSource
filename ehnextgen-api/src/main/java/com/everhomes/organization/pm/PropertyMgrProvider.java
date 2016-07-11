// @formatter:off
package com.everhomes.organization.pm;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.jooq.Condition;

import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationTask;
import com.everhomes.rest.organization.pm.ListPropInvitedUserCommandResponse;

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
	public List<CommunityPmMember> listCommunityPmMembers(Long communityId);
	
	public void createPropAddressMapping(CommunityAddressMapping communityAddressMapping);
	public void updatePropAddressMapping(CommunityAddressMapping communityAddressMapping);
	public void deletePropAddressMapping(CommunityAddressMapping communityAddressMapping);
	public void deletePropAddressMapping(long id);
	public CommunityAddressMapping findPropAddressMappingById(long id);
	public CommunityAddressMapping findPropAddressMappingByAddressId(Long communityId,Long addressId);
	public List<CommunityAddressMapping> listCommunityAddressMappings(Long communityId, Integer pageOffset,Integer pageSize);
	public List<CommunityAddressMapping> listCommunityAddressMappings(Long communityId);
	
	public void createPropBill(CommunityPmBill communityPmBill);
	public void updatePropBill(CommunityPmBill communityPmBill);
	public void deletePropBill(CommunityPmBill communityPmBill);
	public void deletePropBill(long id);
	public CommunityPmBill findPropBillById(long id);
	public List<CommunityPmBill> listCommunityPmBills(Long communityId, String dateStr,String address, Integer pageOffset,Integer pageSize);
	public List<CommunityPmBill> listCommunityPmBills(Long communityId, String dateStr);
	
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
	public List<CommunityPmOwner> listCommunityPmOwners(Long communityId, Long addressId);
	
	public List<CommunityPmTasks> listCommunityPmTasks(Long communityId, Long entityId, String entityType, 
            Long targetId, String targetType, String taskType,Byte status,Integer pageOffset,Integer pageSize);
	public List<CommunityPmTasks> findPmTaskEntityIdAndTargetId(Long communityId, Long entityId, String entityType, 
            Long targetId, String targetType, String taskType,Byte status);
    public void createPmTask(CommunityPmTasks task);
    public CommunityPmTasks findPmTaskById(long id);
	public CommunityPmTasks findPmTaskByEntityId(long communityId, long entityId, String entityType);
	public void updatePmTaskListStatus(List<CommunityPmTasks> tasks);
	public void updatePmTask(CommunityPmTasks task);
	
    public ListPropInvitedUserCommandResponse listInvitedUsers(Long communityId, String contactToken, Long pageOffset, Long pageSize);

	public List<String> listPropBillDateStr(Long communityId);
	
	public int countCommunityPmMembers(long communityId, String contactToken);
	public int countCommunityAddressMappings(long communityId,Byte livingStatus);
	public int countCommunityPmBills(long communityId, String dateStr, String address);
	public int countCommunityPmOwners(long communityId, String address, String contactToken);
	public int countCommunityPmTasks(Long communityId, Long entityId, String entityType, 
            Long targetId, String targetType, String taskType,Byte status);
	public int countCommunityPmTasks(Long communityId, String taskType,Byte status,String startTime,String endTime);

	public void createPropContact(CommunityPmContact communityPmContanct);
	public void updatePropContact(CommunityPmContact communityPmContanct);
	public void deletePropContact(CommunityPmContact communityPmContanct);
	public void deletePropContact(long id);
	public CommunityPmContact findPropContactById(long id);
	public List<CommunityPmContact> listCommunityPmContacts(Long communityId);

	public CommunityPmBill findNewestBillByAddressId(Long addressId);

	public List<CommunityPmBill> listNewestPmBillsByOrgId(Long organizationId, String address);

	public BigDecimal countPmYearIncomeByOrganizationId(Long organizationId, Integer resultCodeId);

	public CommunityPmBill findPmBillByAddressAndDate(Long integralTag1,java.sql.Date startDate, java.sql.Date endDate);

	public BigDecimal countFamilyPmBillDueAmountInYear(Long orgId, Long addressId);

	public CommunityPmBill findFamilyFirstPmBillInYear(Long orgId, Long addressId);

	public CommunityPmBill findPmBillByAddressIdAndTime(Long addressId,Date startDate, Date endDate);

	public OrganizationCommunity findPmCommunityByOrgId(Long organizationId);

	public CommunityAddressMapping findAddressMappingByAddressId(Long addressId);

	public List<CommunityAddressMapping> listAddressMappingsByOrgId(Long orgId);

	public List<CommunityPmBill> listPmBillsByOrgId(Long orgId,String address,java.sql.Date startDate, java.sql.Date endDate, long offset, int pageSize);

	public void updateOrganizationAddressMapping(CommunityAddressMapping mapping);

	public List<CommunityPmBillItem> listOrganizationBillItemsByBillId(Long billId);
	
	List<OrganizationTask> communityPmTaskLists(Long organizationId, Long communityId,String taskType,Byte status,String startTime,String endTime);
}
