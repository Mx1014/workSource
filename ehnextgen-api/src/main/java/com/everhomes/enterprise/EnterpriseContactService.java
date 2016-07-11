package com.everhomes.enterprise;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.everhomes.group.Group;
import com.everhomes.group.GroupMember;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.rest.enterprise.AddContactCommand;
import com.everhomes.rest.enterprise.AddContactGroupCommand;
import com.everhomes.rest.enterprise.ApproveContactCommand;
import com.everhomes.rest.enterprise.CreateContactByUserIdCommand;
import com.everhomes.rest.enterprise.DeleteContactByIdCommand;
import com.everhomes.rest.enterprise.DeleteContactGroupByIdCommand;
import com.everhomes.rest.enterprise.EnterpriseContactDTO;
import com.everhomes.rest.enterprise.GetUserEnterpriseContactCommand;
import com.everhomes.rest.enterprise.LeaveEnterpriseCommand;
import com.everhomes.rest.enterprise.ListContactGroupNamesByEnterpriseIdCommand;
import com.everhomes.rest.enterprise.ListContactGroupNamesByEnterpriseIdCommandResponse;
import com.everhomes.rest.enterprise.ListContactGroupsByEnterpriseIdCommand;
import com.everhomes.rest.enterprise.ListContactGroupsByEnterpriseIdCommandResponse;
import com.everhomes.rest.enterprise.RejectContactCommand;
import com.everhomes.rest.enterprise.UpdateContactCommand;
import com.everhomes.rest.enterprise.importContactsCommand;
import com.everhomes.rest.group.GroupMemberStatus;
import com.everhomes.rest.organization.CreateOrganizationMemberCommand;
import com.everhomes.rest.organization.ListOrganizationContactCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.organization.UpdateOrganizationMemberCommand;
import com.everhomes.rest.organization.UpdatePersonnelsToDepartment;
import com.everhomes.rest.organization.VerifyPersonnelByPhoneCommand;
import com.everhomes.rest.organization.VerifyPersonnelByPhoneCommandResponse;
import com.everhomes.user.User;
import com.everhomes.user.UserIdentifier;

public interface EnterpriseContactService {
    List<EnterpriseContact> queryContactByPhone(String phone);
    List<Enterprise> queryEnterpriseByPhone(String phone);
    //void approveUserToContact(User user, EnterpriseContact contact);
    //void rejectUserFromContact(EnterpriseContact contact);
    EnterpriseContactDTO applyForContact(CreateContactByUserIdCommand cmd);
    List<EnterpriseContactDetail> listContactByEnterpriseId(ListingLocator locator, Long enterpriseId, Integer pageSize,String keyWord);
    EnterpriseContact processUserForContact(UserIdentifier identifier);
    void addContactGroupMember(EnterpriseContactGroupMember member);
    void removeContactGroupMember(EnterpriseContactGroupMember member);
    List<GroupMember> listMessageGroupMembers(Group group, int pageSize);
    void createEnterpriseContact(EnterpriseContact contact);
    void createEnterpriseContactEntry(EnterpriseContactEntry entry);
    List<EnterpriseContactDetail> listContactByStatus(CrossShardListingLocator locator, GroupMemberStatus status,
            Integer pageSize);
    EnterpriseContact queryContactByUserId(Long enterpriseId, Long userId);
    void approveContact(EnterpriseContact contact);
    //void approveByContactId(Long contactId);
	List<EnterpriseContactDetail> listContactsRequestByEnterpriseId(
			ListingLocator locator, Long enterpriseId, Integer pageSize,String keyWord);
	void approveContact(ApproveContactCommand cmd);
	void rejectContact(RejectContactCommand cmd);
	void leaveEnterprise(LeaveEnterpriseCommand cmd);
	void importContacts(importContactsCommand cmd, MultipartFile[] files);
	ListContactGroupsByEnterpriseIdCommandResponse listContactGroupsByEnterpriseId(
			ListContactGroupsByEnterpriseIdCommand cmd);
	ListContactGroupNamesByEnterpriseIdCommandResponse listContactGroupNamesByEnterpriseId(
			ListContactGroupNamesByEnterpriseIdCommand cmd);
	void addContactGroup(AddContactGroupCommand cmd);
	void deleteContactGroupById(DeleteContactGroupByIdCommand cmd);
	void addContact(AddContactCommand cmd);
	void deleteContactById(DeleteContactByIdCommand cmd);
	EnterpriseContactDTO updateContact(UpdateContactCommand cmd);
	EnterpriseContactDTO getUserEnterpriseContact(
			GetUserEnterpriseContactCommand cmd);
	void createOrUpdateUserGroup(EnterpriseContact contact);
	void syncEnterpriseContacts();
	
	ListOrganizationMemberCommandResponse listOrgAuthPersonnels(ListOrganizationContactCommand cmd);
	ListOrganizationMemberCommandResponse listOrganizationPersonnels(
			ListOrganizationContactCommand cmd);
	void updateOrganizationPersonnel(UpdateOrganizationMemberCommand cmd);
	VerifyPersonnelByPhoneCommandResponse verifyPersonnelByPhone(VerifyPersonnelByPhoneCommand cmd);
	ListOrganizationMemberCommandResponse ListParentOrganizationPersonnels(ListOrganizationMemberCommand cmd);
	EnterpriseContactDTO applyForEnterpriseContact(CreateOrganizationMemberCommand cmd);
	void updatePersonnelsToDepartment(UpdatePersonnelsToDepartment cmd);
	void approveForEnterpriseContact(OrganizationMember member);
	OrganizationMemberDTO createOrganizationPersonnel(CreateOrganizationMemberCommand cmd);
}
