// @formatter:off
package com.everhomes.organization;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.rest.archives.TransferArchivesEmployeesCommand;
import com.everhomes.rest.business.listUsersOfEnterpriseCommand;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.enterprise.*;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.techpark.expansion.ListEnterpriseDetailResponse;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.entity.EntityType;
import com.everhomes.group.GroupMember;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.openapi.Contract;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.acl.admin.AclRoleAssignmentsDTO;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.contract.ContractDTO;
import com.everhomes.rest.enterprise.*;
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
import com.everhomes.rest.organization.*;
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
import com.everhomes.user.User;
import com.everhomes.user.UserIdentifier;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;


public interface OrganizationService {

	ListOrganizationMemberCommandResponse getUserOwningOrganizations();
	void setCurrentOrganization(SetCurrentOrganizationCommand cmd);
	OrganizationDTO getUserCurrentOrganization();

	OrganizationDTO createChildrenOrganization(CreateOrganizationCommand cmd);
	Boolean deleteOrganization(DeleteOrganizationIdCommand cmd);
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

    void createOrganizationCommunity(Long orgId, Long communityId);

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

	SearchTopicsByTypeResponse searchTopicsByType(SearchTopicsByTypeCommand cmd);

	void createDepartment(CreateDepartmentCommand cmd);

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
	ListEnterpriseDetailResponse listEnterprisesAbstract(ListEnterprisesCommand cmd);

	/**
	 * 创建企业
	 * @param cmd
	 * @return
	 */
	OrganizationDTO createEnterprise(CreateEnterpriseCommand cmd);
	void createRoleOrganizationMember(CreateOrganizationMemberCommand cmd);
	void updateChildrenOrganization(UpdateOrganizationsCommand cmd);
	void setAclRoleAssignmentRole(SetAclRoleAssignmentCommand cmd, EntityType entityType);
	void updateEnterprise(UpdateEnterpriseCommand cmd);
	void updateEnterprise(UpdateEnterpriseCommand cmd, boolean updateAttachmentAndAddress);
	void deleteEnterpriseById(DeleteOrganizationIdCommand cmd, Boolean checkAuth);

	ListOrganizationMemberCommandResponse listOrgAuthPersonnels(ListOrganizationContactCommand cmd);
//	ListOrganizationMemberCommandResponse listOrganizationPersonnels(
//			ListOrganizationContactCommand cmd, boolean pinyinFlag);
	ListOrganizationMemberCommandResponse listOrganizationPersonnelsByRoleIds(ListOrganizationPersonnelByRoleIdsCommand cmd);
	void updateOrganizationPersonnel(UpdateOrganizationMemberCommand cmd);
	VerifyPersonnelByPhoneCommandResponse verifyPersonnelByPhone(VerifyPersonnelByPhoneCommand cmd);
	boolean verifyPersonnelByWorkEmail(Long orgId, Long detailId, String workEmail);
	boolean verifyPersonnelByWorkEmail(Long orgId, String contactToken, String workEmail);
	boolean verifyPersonnelByAccount(Long detailId, String account);
	boolean verifyPersonnelByAccount(String contactToken, String account);
	ListOrganizationMemberCommandResponse listParentOrganizationPersonnels(ListOrganizationMemberCommand cmd);
	OrganizationDTO applyForEnterpriseContact(CreateOrganizationMemberCommand cmd);
	OrganizationDTO applyForEnterpriseContactNew(ApplyForEnterpriseContactNewCommand cmd);
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
	OrganizationMemberDTO processUserForMember(Integer namespaceId, String identifierToken, Long ownerId);
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

	/* 认证 */
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


//	List<OrganizationMemberDTO> convertOrganizationMemberDTO(List<OrganizationMember> organizationMembers, Organization org);

	void createChildrenOrganizationJobPosition(CreateOrganizationCommand cmd);

	void createOrganizationJobPosition(CreateOrganizationJobPositionCommand cmd);

	Long updateOrganizationJobPosition(UpdateOrganizationJobPositionCommand cmd);

	Boolean deleteOrganizationJobPosition(DeleteOrganizationIdCommand cmd);

	ListOrganizationJobPositionResponse listOrganizationJobPositions(ListOrganizationJobPositionCommand cmd);

	void updateChildrenOrganizationJobPosition(UpdateOrganizationsCommand cmd);

	Boolean deleteChildrenOrganizationJobPosition(DeleteOrganizationIdCommand cmd);

	void createChildrenOrganizationJobLevel(CreateOrganizationCommand cmd);

	void updateChildrenOrganizationJobLevel(UpdateOrganizationsCommand cmd);

	Boolean deleteChildrenOrganizationJobLevel(DeleteOrganizationIdCommand cmd);

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

	List<OrgAddressDTO> listOrganizationAddresses(ListOrganizationAddressesCommand cmd);
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
	ImportFileTaskDTO importEnterpriseData(ImportEnterpriseDataCommand cmd, MultipartFile multipartFile, Long userId);
	void exportEnterprises(ListEnterprisesCommand cmd, HttpServletResponse response);
	ListEnterprisesCommandResponse listNewEnterprises(ListEnterprisesCommand cmd);

	/**
	 * 查询所有管理公司
	 */
	List<OrganizationDTO> listAllPmOrganizations();

	OrganizationDTO listPmOrganizationsByNamespaceId(Integer namespaceId);

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


	// added by R, for salaryGroup 20170630
	Organization createUniongroupOrganization(Long organizationId, String name, String groupType);

	/**人事管理-离职**/
	void leaveTheJob(LeaveTheJobCommand cmd);

	ListOrganizationMemberCommandResponse syncOrganizationMemberStatus();

	OrganizationDetailDTO getOrganizationDetailById(GetOrganizationDetailByIdCommand cmd);
	OrganizationDetailDTO getOrganizationDetailWithDefaultAttachmentById(GetOrganizationDetailByIdCommand cmd);

	/**创建机构账号，包括注册、把用户添加到公司**/
	OrganizationMember createOrganiztionMemberWithDetailAndUserOrganizationAdmin(Long organizationId, String contactName,
			String contactToken,boolean notSendMsgFlag);

	/**组织架构批量调整**/
	void transferOrganizationPersonels(TransferArchivesEmployeesCommand cmd);

	/**通讯录查询接口**/
	ListOrganizationMemberCommandResponse listOrganizationPersonnelsWithDownStream(ListOrganizationContactCommand cmd);
	ListOrganizationContactCommandResponse listOrganizationContacts(ListOrganizationContactCommand cmd); //	简洁的查询
	ListOrganizationMemberCommandResponse listOrganizationPersonnelsByOrgIds(ListOrganizationPersonnelsByOrgIdsCommand cmd);

	/**根据detailId更新通用信息**/
	Long updateOrganizationMemberInfoByDetailId(Long detailId, String contactToken, String contactName, Byte gender);

	void sortOrganizationsAtSameLevel(SortOrganizationsAtSameLevelCommand cmd);

	FindOrgPersonelCommandResponse findOrgPersonel(FindOrgPersonelCommand cmd);

	/**根据总公司id快速拿到所有公司人员的档案id**/
	List<Long> listDetailIdsByEnterpriseId(Long enterpriseId);

	/**通过机构类型和名称获取机构id**/
	Long getOrganizationNameByNameAndType(String name, String groupType);

	/**批量删除子机构(职级或部门岗位)**/
	Boolean deleteChildrenOrganizationAsList(DeleteChildrenOrganizationAsListCommand cmd);

	/**通过通用岗位和details删除岗位**/
	void deleteOrganizationJobPositionsByPositionIdAndDetails(DeleteOrganizationJobPositionsByPositionIdAndDetailsCommand cmd);

	Integer cleanWrongStatusOrganizationMembers(Integer namespaceId);

	Long modifyPhoneNumberByDetailId(Long detailId, String contactToken);

	// todo 0:本部门；1:上级部门; 2:上上级部门; 3:上上上级部门
	List<OrganizationManagerDTO> getManagerByTargetIdAndOrgId(Long orgId, Long targetId, Integer level);

	List<Organization> listUserOrganizations(Integer namespaceId, Long userId, OrganizationGroupType groupType);
	Byte setOrganizationDetailFlag(SetOrganizationDetailFlagCommand cmd);
	Byte getOrganizationDetailFlag(GetOrganizationDetailFlagCommand cmd);

	void checkOrganizationPrivilege(Long orgId, Long pivilegeId);

	// 根据detailId获取部门
	Long getDepartmentByDetailId(Long detailId);

	ListPMOrganizationsResponse listPMOrganizations(ListPMOrganizationsCommand cmd);

	List<Long> listDetailIdWithEnterpriseExclude(String keywords, Integer namespaceId, Long enterpriseId, Timestamp checkinTimeStart, Timestamp checkinTimeEnd, Timestamp dissmissTimeStart, Timestamp dissmissTimeEnd, CrossShardListingLocator locator, Integer pageSize,List<Long> notinDetails,List<Long> inDetails);

	// 根据detailId获取部门
	Long getDepartmentByDetailIdAndOrgId(Long detailId, Long OrgId);
	void checkNameRepeat(Long organizationId, String name, String groupType, Long groupId);

	/**
	 * 根据域空间id、关键字、企业类型、来查询企业信息
	 * @param cmd
	 * @return
	 */
	ListPMOrganizationsResponse listEnterpriseByNamespaceIds(ListEnterpriseByNamespaceIdCommand cmd);

	OrganizationDTO createStandardEnterprise(CreateEnterpriseStandardCommand cmd);

	/**
	 * 根据organizationId来查询公司详细信息
	 * 表eh_organizations和表eh_organization_details进行联查
	 * @param cmd
	 * @return
	 */
	OrganizationAndDetailDTO getOrganizationDetailByOrgId(FindEnterpriseDetailCommand cmd);

	/**
	 * 编辑单个公司的信息
	 * @param cmd
	 */
	void updateEnterpriseDetail(UpdateEnterpriseDetailCommand cmd);

	/**
	 * 更新办公地点以及其中的楼栋和门牌
	 * @param cmd
	 */
	void insertWorkPlacesAndBuildings(UpdateWorkPlaceCommand cmd);

	/**
	 * 根据用户id来进行更高超级管理员手机号
	 * @param cmd
	 */
	void updateSuperAdmin(UpdateSuperAdminCommand cmd);

	/**
	 * 添加入驻企业（标准版）
	 * @param cmd
	 */
	void createSettledEnterprise(CreateSettledEnterpriseCommand cmd);


	/**
	 * 根据组织ID来删除办公地点
	 * @param cmd
	 */
	void deleteWorkPlacesByOrgId(DeleteWorkPlacesCommand cmd);

	/**
	 * 根据公司ID和域空间ID来删除公司以及相应的信息
	 * @param cmd
	 */
	void destoryOrganizationByOrgId(DestoryOrganizationCommand cmd);

	/**
	 * 根据公司Id、域空间Id、工作台状态 来修改工作台状态
	 * @param cmd
	 */
	void changeWorkBenchFlag(ChangeWorkBenchFlagCommand cmd);

	/**
	 * 根据公司id、办公地点名称、项目id、办公地点名称全称来进行修改办公地点名称
	 * @param cmd
	 */
	void updateWholeAddressName(WholeAddressComamnd cmd);

	void updateCustomerEntryInfo(EnterpriseCustomer customer, OrganizationAddress address);

    OrganizationDTO getAuthOrgByProjectIdAndAppId(GetAuthOrgByProjectIdAndAppIdCommand cmd);

	ListUserOrganizationsResponse listUserOrganizations(ListUserOrganizationsCommand cmd);

	List<Long> getOrganizationProjectIdsByAppId(Long organizationId, Long originAppId);

	List<Long> getProjectIdsByCommunityAndModuleApps(Integer namespaceId, Long communityId, Long moduleId,
			AppInstanceConfigConfigMatchCallBack matchCallback);
	//	物业组所需获取企业员工的唯一标识符
	String getAccountByTargetIdAndOrgId(Long targetId, Long orgId);
	OrganizationMenuResponse openListAllChildrenOrganizations(OpenListAllChildrenOrganizationsCommand cmd);

	//用户认证授权
    UserAuthenticationOrganizationDTO createUserAuthenticationOrganization(CreateUserAuthenticationOrganizationCommand cmd);

    UserAuthenticationOrganizationDTO getUserAuthenticationOrganization(GetUserAuthenticationOrganizationCommand cmd);
}
