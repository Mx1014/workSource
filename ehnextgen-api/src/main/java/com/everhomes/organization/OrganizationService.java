// @formatter:off
package com.everhomes.organization;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.business.listUsersOfEnterpriseCommand;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.organization.*;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.entity.EntityType;
import com.everhomes.group.GroupMember;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.openapi.Contract;
import com.everhomes.rest.acl.admin.AclRoleAssignmentsDTO;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.contract.ContractDTO;
import com.everhomes.rest.enterprise.ApproveContactCommand;
import com.everhomes.rest.enterprise.BatchApproveContactCommand;
import com.everhomes.rest.enterprise.BatchRejectContactCommand;
import com.everhomes.rest.enterprise.CreateEnterpriseCommand;
import com.everhomes.rest.enterprise.ImportEnterpriseDataCommand;
import com.everhomes.rest.enterprise.LeaveEnterpriseCommand;
import com.everhomes.rest.enterprise.ListUserRelatedEnterprisesCommand;
import com.everhomes.rest.enterprise.RejectContactCommand;
import com.everhomes.rest.enterprise.UpdateEnterpriseCommand;
import com.everhomes.rest.enterprise.VerifyEnterpriseContactCommand;
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
import com.everhomes.rest.organization.CreateOrganizationOwnerCommand;
import com.everhomes.rest.organization.DeleteOrganizationOwnerCommand;
import com.everhomes.rest.organization.pm.*;
import com.everhomes.rest.ui.privilege.GetEntranceByPrivilegeCommand;
import com.everhomes.rest.ui.privilege.GetEntranceByPrivilegeResponse;
import com.everhomes.rest.user.UserTokenCommand;
import com.everhomes.rest.user.UserTokenCommandResponse;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.user.User;
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

	ListOrganizationsCommandResponse listChildrenOrganizations(Long id, List<String> groupTypes);

	ListOrganizationsCommandResponse listChildrenOrganizations(Long id,
			List<String> groupTypes, String keywords);
	
	
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
	ListOrganizationMemberCommandResponse listOrganizationPersonnelsByRoleIds(ListOrganizationPersonnelByRoleIdsCommand cmd);
	void updateOrganizationPersonnel(UpdateOrganizationMemberCommand cmd);
	VerifyPersonnelByPhoneCommandResponse verifyPersonnelByPhone(VerifyPersonnelByPhoneCommand cmd);
	ListOrganizationMemberCommandResponse listParentOrganizationPersonnels(ListOrganizationMemberCommand cmd);
	OrganizationDTO applyForEnterpriseContact(CreateOrganizationMemberCommand cmd);
	void approveForEnterpriseContact(ApproveContactCommand cmd);
	void leaveForEnterpriseContact(LeaveEnterpriseCommand cmd);
	void updatePersonnelsToDepartment(UpdatePersonnelsToDepartment cmd);
	void addPersonnelsToGroup(AddPersonnelsToGroup cmd);
	void rejectForEnterpriseContact(RejectContactCommand cmd);
	OrganizationMember createOrganizationPersonnel(CreateOrganizationMemberCommand cmd);
	ListEnterprisesCommandResponse searchEnterprise(SearchOrganizationCommand cmd);
	SearchOrganizationCommandResponse searchOrganization(SearchOrganizationCommand cmd);
	ListCommunityByNamespaceCommandResponse listCommunityByOrganizationId(ListCommunitiesByOrganizationIdCommand cmd);
	OrganizationMember createOrganizationAccount(CreateOrganizationAccountCommand cmd, Long roleId);

    OrganizationMemberDTO processUserForMemberWithoutMessage(UserIdentifier identifier);

    OrganizationMemberDTO processUserForMember(UserIdentifier identifier);
	List<OrganizationDetailDTO> listUserRelateEnterprises(ListUserRelatedEnterprisesCommand cmd);
	List<OrganizationDTO> listUserRelateOrganizations(Integer namespaceId, Long userId, OrganizationGroupType groupType);
	List<Organization> getSyncDatas(CrossShardListingLocator locator);
	List<AclRoleAssignmentsDTO> listAclRoleByUserId(ListAclRoleByUserIdCommand cmd);
	ImportFileResponse<ImportEnterpriseDataDTO> importEnterpriseData(MultipartFile mfile,
																	 Long userId, ImportEnterpriseDataCommand cmd);
	ImportFileTaskDTO importOrganizationPersonnelData(MultipartFile mfile,
			Long userId, ImportOrganizationPersonnelDataCommand cmd);
	
	ListPostCommandResponse listTaskTopicsByType(ListTopicsByTypeCommand cmd);

    List<OrganizationManagerDTO> getOrganizationManagers(List<Long> organizationIds);

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
    
    /** 不带menu格式的所有子机构 */
    List<OrganizationDTO> listAllChildrenOrganizationMenusWithoutMenuStyle(Long id,
			List<String> groupTypes,Byte naviFlag);

	CheckOfficalPrivilegeResponse checkOfficalPrivilegeByScene(CheckOfficalPrivilegeBySceneCommand cmd);
	CheckOfficalPrivilegeResponse checkOfficalPrivilege(CheckOfficalPrivilegeCommand cmd);
	
	/**
	 * 获取通讯录的部门或者群组
	 * @param organizationGroupType
	 * @param token
	 * @param orgPath
	 * @return
	 */
	List<OrganizationDTO> getOrganizationMemberGroups(OrganizationGroupType organizationGroupType, String token, String orgPath);
	
	/**
	 * 导出通讯录
	 * @param cmd
	 * @param httpResponse
	 */
	void exportRoleAssignmentPersonnelXls(ExcelOrganizationPersonnelCommand cmd, HttpServletResponse httpResponse);
	
	/**
	 * 删除机构人员 包括子部门
	 * @param cmd
	 */
	void deleteOrganizationPersonnelByContactToken(DeleteOrganizationPersonnelByContactTokenCommand cmd);
	
	/**
	 * 生产excel
	 * @param members
	 * @return
	 */
	XSSFWorkbook createXSSFWorkbook(List<OrganizationMemberDTO> members);

	List<Long> getOrganizationIdsTreeUpToRoot(Long communityId);
	
	void addNewOrganizationInZuolin(AddNewOrganizationInZuolinCommand cmd);
	
	/**
	 * 添加通讯录到多部门或者多群组
	 * @param cmd
	 */
	OrganizationMemberDTO addOrganizationPersonnel(AddOrganizationPersonnelCommand cmd);

	/**
	 * 获取最顶级部门
	 * @param organizationGroupType
	 * @param token
	 * @param organizationId
     * @return
     */
	OrganizationDTO getMemberTopDepartment(List<String> groupTypes, String token, Long organizationId);
	
	/**
	 * 获取机构下面所有人员
	 * @param organizationId
	 * @param groupTypes
	 * @param userName
	 * @return
	 */
	List<OrganizationMemberDTO> listAllChildOrganizationPersonnel(Long organizationId, List<String> groupTypes,String userName);
	Long getTopOrganizationId(Long organizationId);

	/**
	 * 隐藏显示通讯录
	 * @param cmd
     */
	void updateOrganizationContactVisibleFlag(UpdateOrganizationContactVisibleFlagCommand cmd);

	/**
	 * 批量隐藏显示通讯录
	 * @param cmd
     */
	void batchUpdateOrganizationContactVisibleFlag(BatchUpdateOrganizationContactVisibleFlagCommand cmd);

	/**
	 * 树状机构列表
	 * @param cmd
	 * @return
     */
	OrganizationTreeDTO listAllTreeOrganizations(ListAllTreeOrganizationsCommand cmd);

	/**
	 * 简单的通讯录
	 * @param cmd
     * @return
     */
	ListOrganizationContactCommandResponse listOrganizationContacts(ListOrganizationContactCommand cmd);
	void batchApproveForEnterpriseContact(BatchApproveContactCommand cmd);
	void batchRejectForEnterpriseContact(BatchRejectContactCommand cmd);

	/**
	 * 获取用户的最高部门
	 * @param cmd
	 * @return
     */
	OrganizationDTO getContactTopDepartment(GetContactTopDepartmentCommand cmd);
 
	List<OrganizationDTO> listOrganizationsByEmail(ListOrganizationsByEmailCommand cmd);
	void applyForEnterpriseContactByEmail(ApplyForEnterpriseContactByEmailCommand cmd); 
	String verifyEnterpriseContact(VerifyEnterpriseContactCommand cmd);
  
	
	List<OrganizationMemberDTO> convertOrganizationMemberDTO(List<OrganizationMember> organizationMembers, Organization org);

	void createChildrenOrganizationJobPosition(CreateOrganizationCommand cmd);
	
	void createOrganizationJobPosition(CreateOrganizationJobPositionCommand cmd);
	
	void updateOrganizationJobPosition(UpdateOrganizationJobPositionCommand cmd);
	
	void deleteOrganizationJobPosition(DeleteOrganizationIdCommand cmd);
	
	ListOrganizationJobPositionResponse listOrganizationJobPositions(ListOrganizationJobPositionCommand cmd);
	
	void updateChildrenOrganizationJobPosition(UpdateOrganizationsCommand cmd);
	
	void deleteChildrenOrganizationJobPosition(DeleteOrganizationIdCommand cmd);
	
	void createChildrenOrganizationJobLevel(CreateOrganizationCommand cmd);
	
	void updateChildrenOrganizationJobLevel(UpdateOrganizationsCommand cmd);

	void deleteChildrenOrganizationJobLevel(DeleteOrganizationIdCommand cmd);
	
	ListChildrenOrganizationJobLevelResponse listChildrenOrganizationJobLevels(ListAllChildrenOrganizationsCommand cmd);
	
	ListChildrenOrganizationJobPositionResponse listChildrenOrganizationJobPositions(ListAllChildrenOrganizationsCommand cmd);
	List<OrganizationMemberDTO> listOrganizationMemberDTOs(Long orgId,
			List<Long> memberUids);

	List<OrganizationDTO> getOrganizationMemberGroups(OrganizationGroupType organizationGroupType, Long userId, Long organizationId);

    CommunityOrganizationTreeResponse listCommunityOrganizationTree(ListCommunityOrganizationTreeCommand cmd); 
	OrganizationMember createOrganizationAccount(CreateOrganizationAccountCommand cmd, Long roleId,
			Integer namespaceId);
	List<OrganizationContactDTO> getAdmins(Long organizationId);
	OrganizationServiceUser getServiceUser(Long organizationId, Long serviceUserId);
	List<String> getBusinessContactPhone(Long organizationId);
	Set<String> getOrganizationContactPhone(Long organizationId);
	List<String> getAdminPhone(Long organizationId);
	OrganizationServiceUser getServiceUser(Long organizationId);

	List<OrganizationDTO> listOrganizationsByModuleId(ListOrganizationByModuleIdCommand cmd);

	List<OrganizationContactDTO> listOrganizationsContactByModuleId(ListOrganizationByModuleIdCommand cmd);

	List<OrganizationManagerDTO> listOrganizationManagers(ListOrganizationManagersCommand cmd);

	List<OrganizationManagerDTO> listOrganizationAllManagers(ListOrganizationManagersCommand cmd);

	List<OrganizationManagerDTO> listModuleOrganizationManagers(ListOrganizationByModuleIdCommand cmd);


	List<OrganizationContactDTO> listOrganizationContactByJobPositionId(ListOrganizationContactByJobPositionIdCommand cmd);

	List<OrganizationContactDTO> listModuleOrganizationContactByJobPositionId(ListModuleOrganizationContactByJobPositionIdCommand cmd);

	List<OrganizationSimpleDTO> listUserRelateOrgs(
			ListUserRelatedOrganizationsCommand cmd, User user);

	List<OrganizationDTO> getOrganizationMemberGroups(List<String> groupTypes, String token, String orgPath);

	List<OrganizationDTO> getOrganizationMemberGroups(List<String> groupTypes, Long userId, Long organizationId);

    List<OrganizationMember> listOrganizationContactByJobPositionId(List<Long> organizationIds, Long jobPositionId);

	List<OrganizationMember> listOrganizationContactByJobPositionId(Long enterpriseId, Long jobPositionId);

    List<OrgAddressDTO> listUserRelatedOrganizationAddresses(ListUserRelatedOrganizationAddressesCommand cmd);
	ContractDTO processContract(Contract contract, Integer namespaceId);
	ContractDTO processContract(Contract contract);

	ImportFileResponse<ImportOrganizationContactDataDTO> getImportFileResult(GetImportFileResultCommand cmd);

	void exportImportFileFailResultXls(GetImportFileResultCommand cmd, HttpServletResponse httpResponse);
	
	/**
	 * 查询企业下的用户
	 * @param cmd
	 * @return ListOrganizationContactCommandResponse
	 */
	ListOrganizationContactCommandResponse listUsersOfEnterprise(listUsersOfEnterpriseCommand cmd);

    /****** new interface ******/

    ListPersonnelsV2CommandResponse listOrganizationPersonnelsV2(ListPersonnelsV2Command cmd);

    PersonnelsDetailsV2Response getOrganizationPersonnelDetailsV2(GetPersonnelDetailsV2Command cmd);

	OrganizationMemberDTO addOrganizationPersonnelV2(AddOrganizationPersonnelV2Command cmd);

    OrganizationMemberBasicDTO getOrganizationMemberBasicInfo(GetOrganizationMemberInfoCommand cmd);

    void updateOrganizationMemberBackGround(UpdateOrganizationMemberBackGroundCommand cmd);

    OrganizationMemberEducationsDTO addOrganizationMemberEducations(AddOrganizationMemberEducationsCommand cmd);

    List<OrganizationMemberEducationsDTO> listOrganizationMemberEducations(ListOrganizationMemberEducationsCommand cmd);

    void deleteOrganizationMemberEducations(DeleteOrganizationMemberEducationsCommand cmd);

    void updateOrganizationMemberEducations(UpdateOrganizationMemberEducationsCommand cmd);

    OrganizationMemberWorkExperiencesDTO addOrganizationMemberWorkExperiences(AddOrganizationMemberWorkExperiencesCommand cmd);

    List<OrganizationMemberWorkExperiencesDTO> listOrganizationMemberWorkExperiences(ListOrganizationMemberWorkExperiencesCommand cmd);

    void deleteOrganizationMemberWorkExperiences(DeleteOrganizationMemberWorkExperiencesCommand cmd);

    void updateOrganizationMemberWorkExperiences(UpdateOrganizationMemberWorkExperiencesCommand cmd);

    OrganizationMemberInsurancesDTO addOrganizationMemberInsurances(AddOrganizationMemberInsurancesCommand cmd);

    List<OrganizationMemberInsurancesDTO> listOrganizationMemberInsurances(ListOrganizationMemberInsurancesCommand cmd);

    void updateOrganizationMemberInsurances(UpdateOrganizationMemberInsurancesCommand cmd);

    void deleteOrganizationMemberInsurances(DeleteOrganizationMemberInsurancesCommand cmd);

    OrganizationMemberContractsDTO addOrganizationMemberContracts(AddOrganizationMemberContractsCommand cmd);

    List<OrganizationMemberContractsDTO> listOrganizationMemberContracts(ListOrganizationMemberContractsCommand cmd);

    void updateOrganizationMemberContracts (UpdateOrganizationMemberContractsCommand cmd);

    void deleteOrganizationMemberContracts(DeleteOrganizationMemberContractsCommand cmd);

    void updateOrganizationEmployeeStatus(UpdateOrganizationEmployeeStatusCommand cmd);

    List<MemberRecordChangesByJobDTO> listMemberRecordChangesByJob(ListMemberRecordChangesByJobCommand cmd);

    ListMemberProfileRecordsCommandResponse listMemberRecordChangesByProfile(ListMemberProfileRecordsCommand cmd);

    //  暂时舍弃 by R 20170718
//    OrganizationMemberProfileIntegrity getProfileIntegrity(GetProfileIntegrityCommand cmd);

	ImportFileTaskDTO importOrganizationPersonnelFiles(MultipartFile mfile,
													   Long userId, ImportOrganizationPersonnelDataCommand cmd);
    List<Object> getOrganizationMemberIdAndVisibleFlag(String contactToken, Long organizationId);

    void exportOrganizationPersonnelFiles(ExcelOrganizationPersonnelCommand cmd, HttpServletResponse httpResponse);

	ImportFileTaskDTO importEnterpriseData(ImportEnterpriseDataCommand cmd, MultipartFile multipartFile, Long userId);
	void exportEnterprises(ListEnterprisesCommand cmd, HttpServletResponse response);
	ListEnterprisesCommandResponse listNewEnterprises(ListEnterprisesCommand cmd);

	/**
	 * 查询所有管理公司
	 */
	List<OrganizationDTO> listAllPmOrganizations();

	List<Long> getIncludeOrganizationIdsByUserId(Long userId, Long organizationId);

	/**
	 * 最新 获取用户所包含的机构列表，如果用户是某个机构的管理员或者模块管理员或者细化的权限，就拥有这个机构，不一定用户非得在此机构的通讯录
	 * @param cmd
	 * @return
     */
	List<OrganizationSimpleDTO> listUserRelateOrganizations(ListUserRelatedOrganizationsCommand cmd);
	List<OrganizationMember> listOrganizationMemberByOrganizationPathAndUserId(String path,
			Long userId);
	String checkIfLastOnNode(DeleteOrganizationPersonnelByContactTokenCommand cmd);

	/**人事管理-离职**/
	void leaveTheJob(LeaveTheJobCommand cmd);
}
