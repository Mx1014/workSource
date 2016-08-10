// @formatter:off
package com.everhomes.organization;

import java.util.List;
import java.util.Map;




import org.springframework.web.multipart.MultipartFile;

import com.everhomes.entity.EntityType;
import com.everhomes.group.GroupMember;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.acl.admin.AclRoleAssignmentsDTO;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.enterprise.ApproveContactCommand;
import com.everhomes.rest.enterprise.CreateEnterpriseCommand;
import com.everhomes.rest.enterprise.ImportEnterpriseDataCommand;
import com.everhomes.rest.enterprise.LeaveEnterpriseCommand;
import com.everhomes.rest.enterprise.ListUserRelatedEnterprisesCommand;
import com.everhomes.rest.enterprise.RejectContactCommand;
import com.everhomes.rest.enterprise.UpdateEnterpriseCommand;
import com.everhomes.rest.forum.CancelLikeTopicCommand;
import com.everhomes.rest.forum.GetTopicCommand;
import com.everhomes.rest.forum.LikeTopicCommand;
import com.everhomes.rest.forum.ListOrgMixTopicCommand;
import com.everhomes.rest.forum.ListPostCommandResponse;
import com.everhomes.rest.forum.ListTopicCommand;
import com.everhomes.rest.forum.ListTopicCommentCommand;
import com.everhomes.rest.forum.NewCommentCommand;
import com.everhomes.rest.forum.NewTopicCommand;
import com.everhomes.rest.forum.PostDTO;
import com.everhomes.rest.forum.QueryOrganizationTopicCommand;
import com.everhomes.rest.namespace.ListCommunityByNamespaceCommandResponse;
import com.everhomes.rest.organization.AddOrgAddressCommand;
import com.everhomes.rest.organization.AddPersonnelsToGroup;
import com.everhomes.rest.organization.ApplyOrganizationMemberCommand;
import com.everhomes.rest.organization.AssginOrgTopicCommand;
import com.everhomes.rest.organization.CheckOfficalPrivilegeBySceneCommand;
import com.everhomes.rest.organization.CheckOfficalPrivilegeCommand;
import com.everhomes.rest.organization.CheckOfficalPrivilegeResponse;
import com.everhomes.rest.organization.CreateDepartmentCommand;
import com.everhomes.rest.organization.CreateOrganizationAccountCommand;
import com.everhomes.rest.organization.CreateOrganizationByAdminCommand;
import com.everhomes.rest.organization.CreateOrganizationCommand;
import com.everhomes.rest.organization.CreateOrganizationCommunityCommand;
import com.everhomes.rest.organization.CreateOrganizationContactCommand;
import com.everhomes.rest.organization.CreateOrganizationMemberCommand;
import com.everhomes.rest.organization.CreateOrganizationOwnerCommand;
import com.everhomes.rest.organization.CreatePropertyOrganizationCommand;
import com.everhomes.rest.organization.DeleteOrganizationCommunityCommand;
import com.everhomes.rest.organization.DeleteOrganizationIdCommand;
import com.everhomes.rest.organization.DeleteOrganizationOwnerCommand;
import com.everhomes.rest.organization.GetOrgDetailCommand;
import com.everhomes.rest.organization.ImportOrganizationPersonnelDataCommand;
import com.everhomes.rest.organization.ImportOwnerDataCommand;
import com.everhomes.rest.organization.ListAclRoleByUserIdCommand;
import com.everhomes.rest.organization.ListCommunitiesByOrganizationIdCommand;
import com.everhomes.rest.organization.ListDepartmentsCommand;
import com.everhomes.rest.organization.ListDepartmentsCommandResponse;
import com.everhomes.rest.organization.ListEnterprisesCommand;
import com.everhomes.rest.organization.ListEnterprisesCommandResponse;
import com.everhomes.rest.organization.ListOrganizationAdministratorCommand;
import com.everhomes.rest.organization.ListOrganizationCommunityCommand;
import com.everhomes.rest.organization.ListOrganizationCommunityCommandResponse;
import com.everhomes.rest.organization.ListOrganizationCommunityV2CommandResponse;
import com.everhomes.rest.organization.ListOrganizationContactCommand;
import com.everhomes.rest.organization.ListOrganizationContactCommandResponse;
import com.everhomes.rest.organization.ListOrganizationMemberCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.rest.organization.ListOrganizationsByNameCommand;
import com.everhomes.rest.organization.ListOrganizationsByNameResponse;
import com.everhomes.rest.organization.ListOrganizationsCommand;
import com.everhomes.rest.organization.ListOrganizationsCommandResponse;
import com.everhomes.rest.organization.ListPersonnelNotJoinGroupCommand;
import com.everhomes.rest.organization.ListPmManagementComunitesCommand;
import com.everhomes.rest.organization.ListTopicsByTypeCommand;
import com.everhomes.rest.organization.ListTopicsByTypeCommandResponse;
import com.everhomes.rest.organization.ListUserRelatedOrganizationsCommand;
import com.everhomes.rest.organization.ListUserTaskCommand;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.OrganizationDetailDTO;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberCommand;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.organization.OrganizationMenuResponse;
import com.everhomes.rest.organization.OrganizationSimpleDTO;
import com.everhomes.rest.organization.PmManagementCommunityDTO;
import com.everhomes.rest.organization.ProcessOrganizationTaskCommand;
import com.everhomes.rest.organization.RejectOrganizationCommand;
import com.everhomes.rest.organization.SearchOrganizationCommand;
import com.everhomes.rest.organization.SearchOrganizationCommandResponse;
import com.everhomes.rest.organization.SearchTopicsByTypeCommand;
import com.everhomes.rest.organization.SearchTopicsByTypeResponse;
import com.everhomes.rest.organization.SendOrganizationMessageCommand;
import com.everhomes.rest.organization.SetAclRoleAssignmentCommand;
import com.everhomes.rest.organization.SetCurrentOrganizationCommand;
import com.everhomes.rest.organization.SetOrgTopicStatusCommand;
import com.everhomes.rest.organization.UpdateOrganizationContactCommand;
import com.everhomes.rest.organization.UpdateOrganizationMemberCommand;
import com.everhomes.rest.organization.UpdateOrganizationsCommand;
import com.everhomes.rest.organization.UpdatePersonnelsToDepartment;
import com.everhomes.rest.organization.UpdateTopicPrivacyCommand;
import com.everhomes.rest.organization.UserExitOrganizationCommand;
import com.everhomes.rest.organization.UserJoinOrganizationCommand;
import com.everhomes.rest.organization.VerifyPersonnelByPhoneCommand;
import com.everhomes.rest.organization.VerifyPersonnelByPhoneCommandResponse;
import com.everhomes.rest.organization.pm.AddPmBuildingCommand;
import com.everhomes.rest.organization.pm.DeletePmCommunityCommand;
import com.everhomes.rest.organization.pm.ListPmBuildingCommand;
import com.everhomes.rest.organization.pm.ListPmManagementsCommand;
import com.everhomes.rest.organization.pm.PmBuildingDTO;
import com.everhomes.rest.organization.pm.PmManagementsResponse;
import com.everhomes.rest.organization.pm.UnassignedBuildingDTO;
import com.everhomes.rest.organization.pm.UpdateOrganizationMemberByIdsCommand;
import com.everhomes.rest.ui.privilege.GetEntranceByPrivilegeCommand;
import com.everhomes.rest.ui.privilege.GetEntranceByPrivilegeResponse;
import com.everhomes.rest.user.UserTokenCommand;
import com.everhomes.rest.user.UserTokenCommandResponse;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.user.UserIdentifier;



public interface OrganizationService {
	
	ListOrganizationMemberCommandResponse getUserOwningOrganizations();
	void setCurrentOrganization(SetCurrentOrganizationCommand cmd);
	OrganizationDTO getUserCurrentOrganization();

	OrganizationDTO createChildrenOrganization(CreateOrganizationCommand cmd);
	void deleteOrganization(DeleteOrganizationIdCommand cmd);
	void createOrganizationMember(CreateOrganizationMemberCommand cmd);
	void deleteOrganizationMember(DeleteOrganizationIdCommand cmd);
	void createOrganizationCommunity(CreateOrganizationCommunityCommand cmd);
	ListOrganizationsCommandResponse listOrganizations(ListOrganizationsCommand cmd);
	
	void applyOrganizationMember(ApplyOrganizationMemberCommand cmd);
	
	ListOrganizationMemberCommandResponse listOrgMembers(ListOrganizationMemberCommand cmd);
	void createPropertyOrganization(CreatePropertyOrganizationCommand cmd);
	ListOrganizationCommunityCommandResponse listOrganizationCommunities(ListOrganizationCommunityCommand cmd);
	ListOrganizationCommunityV2CommandResponse listOrganizationCommunitiesV2(ListOrganizationCommunityCommand cmd);
	List<Long> getOrganizationCommunityIdById(Long organizationId);
	void deleteOrganizationCommunity(DeleteOrganizationCommunityCommand cmd);
	/**
	 * 获取小区相关的政府机构区域ID；
	 * 对于物业和业委，其region为小区ID；对于居委和公安，其region为机构ID；
	 * @param communityId 小区ID
	 * @return 机构类型与区域ID的映射map
	 */
	Map<String, Long> getOrganizationRegionMap(Long communityId);
	List<Organization> getOrganizationTreeUpToRoot(Long communityId);
	
	
	
	
	//物业帖子相关 
	PostDTO createTopic(NewTopicCommand cmd);
	ListPostCommandResponse queryTopicsByCategory(QueryOrganizationTopicCommand cmd);
	void setOrgTopicStatus(SetOrgTopicStatusCommand cmd);
	ListPostCommandResponse listTopicComments(ListTopicCommentCommand cmd);
	PostDTO createComment(NewCommentCommand cmd);
	void cancelLikeTopic(CancelLikeTopicCommand cmd);
	void likeTopic(LikeTopicCommand cmd);
	PostDTO getTopic(GetTopicCommand cmd);
	void assignOrgTopic(AssginOrgTopicCommand cmd);
	ListPostCommandResponse listTopics(ListTopicCommand cmd);
	ListPostCommandResponse listOrgMixTopics(ListOrgMixTopicCommand cmd);
	UserTokenCommandResponse findUserByIndentifier(UserTokenCommand cmd);
	
	//行政热线
	void createOrgContact(CreateOrganizationContactCommand cmd);
	void updateOrgContact(UpdateOrganizationContactCommand cmd);
	void deleteOrgContact(DeleteOrganizationIdCommand cmd);
	ListOrganizationContactCommandResponse listOrgContact(ListOrganizationContactCommand cmd);
	void sendOrgMessage(SendOrganizationMessageCommand cmd);
	List<OrganizationSimpleDTO> listUserRelateOrgs(ListUserRelatedOrganizationsCommand cmd);
	OrganizationDTO getOrganizationByComunityidAndOrgType(GetOrgDetailCommand cmd);
	int rejectOrganization(RejectOrganizationCommand cmd);
	int userExitOrganization(UserExitOrganizationCommand cmd);
	void approveOrganizationMember(OrganizationMemberCommand cmd);
	void rejectOrganizationMember(OrganizationMemberCommand cmd);
	ListTopicsByTypeCommandResponse listTopicsByType(ListTopicsByTypeCommand cmd);
	void userJoinOrganization(UserJoinOrganizationCommand cmd);
	void deleteOrgMember(OrganizationMemberCommand cmd);
	void updateTopicPrivacy(UpdateTopicPrivacyCommand cmd);
	void createOrganizationCommunityByAdmin(CreateOrganizationCommunityCommand cmd);
	void createOrganizationByAdmin(CreateOrganizationByAdminCommand cmd);
	void addOrgAddress(AddOrgAddressCommand cmd);
	void importOrganization(MultipartFile[] files);
	void importOrgPost(MultipartFile[] files);
	void executeImportOrgPost(String filePath, Long userId);
	void executeImportOrganization(String filePath, Long userId);
	
	
	void addPmBuilding(AddPmBuildingCommand cmd);
	void deletePmCommunity(DeletePmCommunityCommand cmd);
	List<PmBuildingDTO> listPmBuildings(ListPmBuildingCommand cmd);
	List<UnassignedBuildingDTO> listUnassignedBuilding(ListPmBuildingCommand cmd);
	
	PmManagementsResponse listPmManagements(ListPmManagementsCommand cmd);
    
	ListTopicsByTypeCommandResponse listUserTask(ListUserTaskCommand cmd);
//	ListPropTopicStatisticCommandResponse getPMTopicStatistics(ListPropTopicStatisticCommand cmd);
//	void processPartnerOrganizationUser(Long userId, Long partnerId);
	
	SearchTopicsByTypeResponse searchTopicsByType(SearchTopicsByTypeCommand cmd);
	
	void createDepartment(CreateDepartmentCommand cmd);
	ListDepartmentsCommandResponse listDepartments(ListDepartmentsCommand cmd);
	
	
	boolean updateOrganizationMemberByIds(UpdateOrganizationMemberByIdsCommand cmd);
	
	void checkOrganizationPrivilege(long uid, long organizationId, long privilege);
	
	List<Long> getUserResourcePrivilege(long uid, long organizationId);
	
	OrganizationMenuResponse listAllChildrenOrganizationMenus(Long id,
			List<String> groupTypes,Byte naviFlag);
	
	ListOrganizationsCommandResponse listAllChildrenOrganizations(Long id,
			List<String> groupTypes);
	
	ListOrganizationsCommandResponse listChildrenOrganizations(Long id,
			List<String> groupTypes);
	
	
	ListEnterprisesCommandResponse listEnterprises(ListEnterprisesCommand cmd);
	OrganizationDTO createEnterprise(CreateEnterpriseCommand cmd);
	void createRoleOrganizationMember(CreateOrganizationMemberCommand cmd);
	void updateChildrenOrganization(UpdateOrganizationsCommand cmd);
	void setAclRoleAssignmentRole(SetAclRoleAssignmentCommand cmd, EntityType entityType);
	void updateEnterprise(UpdateEnterpriseCommand cmd);
	void deleteEnterpriseById(DeleteOrganizationIdCommand cmd);
	
	ListOrganizationMemberCommandResponse listOrgAuthPersonnels(ListOrganizationContactCommand cmd);
	ListOrganizationMemberCommandResponse listOrganizationPersonnels(
			ListOrganizationContactCommand cmd, boolean pinyinFlag);
	ListOrganizationMemberCommandResponse listOrganizationPersonnelsByRoleIds(ListOrganizationAdministratorCommand cmd);
	void updateOrganizationPersonnel(UpdateOrganizationMemberCommand cmd);
	VerifyPersonnelByPhoneCommandResponse verifyPersonnelByPhone(VerifyPersonnelByPhoneCommand cmd);
	ListOrganizationMemberCommandResponse ListParentOrganizationPersonnels(ListOrganizationMemberCommand cmd);
	OrganizationDTO applyForEnterpriseContact(CreateOrganizationMemberCommand cmd);
	void approveForEnterpriseContact(ApproveContactCommand cmd);
	void leaveForEnterpriseContact(LeaveEnterpriseCommand cmd);
	void updatePersonnelsToDepartment(UpdatePersonnelsToDepartment cmd);
	void addPersonnelsToGroup(AddPersonnelsToGroup cmd);
	void rejectForEnterpriseContact(RejectContactCommand cmd);
	OrganizationMemberDTO createOrganizationPersonnel(CreateOrganizationMemberCommand cmd);
	ListEnterprisesCommandResponse searchEnterprise(SearchOrganizationCommand cmd);
	SearchOrganizationCommandResponse searchOrganization(SearchOrganizationCommand cmd);
	ListCommunityByNamespaceCommandResponse listCommunityByOrganizationId(ListCommunitiesByOrganizationIdCommand cmd);
	void createOrganizationAccount(CreateOrganizationAccountCommand cmd, Long roleId);
	OrganizationMemberDTO processUserForMember(UserIdentifier identifier);
	List<OrganizationDetailDTO> listUserRelateEnterprises(ListUserRelatedEnterprisesCommand cmd);
	List<OrganizationDTO> listUserRelateOrganizations(Integer namespaceId, Long userId, OrganizationGroupType groupType);
	List<Organization> getSyncDatas(CrossShardListingLocator locator);
	List<AclRoleAssignmentsDTO> listAclRoleByUserId(ListAclRoleByUserIdCommand cmd);
	ImportDataResponse importEnterpriseData(MultipartFile mfile,
			Long userId, ImportEnterpriseDataCommand cmd);
	ImportDataResponse importOrganizationPersonnelData(MultipartFile mfile,
			Long userId, ImportOrganizationPersonnelDataCommand cmd);
	
	ListPostCommandResponse listTaskTopicsByType(ListTopicsByTypeCommand cmd);
	
	PostDTO acceptTask(ProcessOrganizationTaskCommand cmd);
	
	PostDTO refuseTask(ProcessOrganizationTaskCommand cmd);
	
	PostDTO grabTask(ProcessOrganizationTaskCommand cmd);
	
	PostDTO processingTask(ProcessOrganizationTaskCommand cmd);
	
	ListPostCommandResponse listAllTaskTopics(ListTopicsByTypeCommand cmd);
	
	ListPostCommandResponse listMyTaskTopics(ListTopicsByTypeCommand cmd);
	
	ListPostCommandResponse listGrabTaskTopics(ListTopicsByTypeCommand cmd);
	
	GetEntranceByPrivilegeResponse getEntranceByPrivilege(GetEntranceByPrivilegeCommand cmd, Long organizationId);
	
	ListOrganizationMemberCommandResponse listPersonnelNotJoinGroups(ListPersonnelNotJoinGroupCommand cmd);
	
	List<PmManagementCommunityDTO> listPmManagementComunites(ListPmManagementComunitesCommand cmd);
	
	void deleteOrganizationOwner(DeleteOrganizationOwnerCommand cmd);
	
	void createOrganizationOwner(CreateOrganizationOwnerCommand cmd);
	
	ImportDataResponse importOwnerData(MultipartFile mfile,ImportOwnerDataCommand cmd);
	
	List<GroupMember> listMessageGroupMembers(Long groupId);

    ListPostCommandResponse  listOrgTopics(QueryOrganizationTopicCommand cmd);
	
	/**
	 * 企业在以下两种情况下与小区关联：1) 企业入驻园区 2) 物业管理公司办公地点本身某个园区、需要使用园区服务
	 * @param organizationId 机构ID
	 * @return 企业所入驻的小区ID
	 */
	Long getOrganizationActiveCommunityId(Long organizationId);
	
	/**
	 * 获取organizationDTO，由于直接用provider拿到的organization没填充其它信息（如小区ID），当需要使用一些额外补充的信息时可使用该接口
	 * @param organizationId 机构ID
	 * @return
	 */
	OrganizationDTO getOrganizationById(Long organizationId);
	
	/**
	 * 根据organizationId，获取所有自己管辖和子集机构的管辖小区
	 * @param organizationId
	 * @return
	 */
	List<CommunityDTO> listAllChildrenOrganizationCoummunities(Long organizationId);
    ListOrganizationsByNameResponse listOrganizationByName(ListOrganizationsByNameCommand cmd);
	CheckOfficalPrivilegeResponse checkOfficalPrivilegeByScene(CheckOfficalPrivilegeBySceneCommand cmd);
	CheckOfficalPrivilegeResponse checkOfficalPrivilege(CheckOfficalPrivilegeCommand cmd);
	List<Long> getOrganizationIdsTreeUpToRoot(Long communityId);
}
