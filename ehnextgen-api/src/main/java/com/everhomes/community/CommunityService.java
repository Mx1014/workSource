// @formatter:off
package com.everhomes.community;

import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.community.*;
import com.everhomes.rest.community.admin.*;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.user.admin.ImportDataResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


public interface CommunityService {
    ListCommunitesByStatusCommandResponse listCommunitiesByStatus(ListCommunitesByStatusCommand cmd);
    
    void approveCommuniy(ApproveCommunityAdminCommand cmd);

    void updateCommunity(UpdateCommunityAdminCommand cmd);

    void rejectCommunity(RejectCommunityAdminCommand cmd);

    List<CommunityDTO> getCommunitiesByNameAndCityId(GetCommunitiesByNameAndCityIdCommand cmd);

    List<CommunityDTO> getCommunitiesByIds(GetCommunitiesByIdsCommand cmd);

    void updateCommunityRequestStatus(UpdateCommunityRequestStatusCommand cmd);

    List<CommunityDTO> getNearbyCommunityById(GetNearbyCommunitiesByIdCommand cmd);

    CommunityDTO getCommunityById(GetCommunityByIdCommand cmd);

    CommunityDTO getCommunityByUuid(GetCommunityByUuidCommand cmd);

	ListCommunitiesByKeywordCommandResponse listCommunitiesByKeyword(
			ListComunitiesByKeywordAdminCommand cmd);
	
	ListBuildingCommandResponse listBuildings(ListBuildingCommand cmd);
	BuildingDTO getBuilding(GetBuildingCommand cmd);
	
	BuildingDTO updateBuilding(UpdateBuildingAdminCommand cmd);
	void deleteBuilding(DeleteBuildingAdminCommand cmd);
	
	Boolean verifyBuildingName(VerifyBuildingNameAdminCommand cmd);
	
	List<CommunityManagerDTO> getCommunityManagers(ListCommunityManagersAdminCommand cmd);
	
	List<UserCommunityDTO> getUserCommunities(ListUserCommunitiesCommand cmd);
	
	void approveBuilding(VerifyBuildingAdminCommand cmd);
	
	void rejectBuilding(VerifyBuildingAdminCommand cmd);
	
	ListBuildingsByStatusCommandResponse listBuildingsByStatus(listBuildingsByStatusCommand cmd);
	
	ImportDataResponse importBuildingData(MultipartFile mfile, Long userId);
	
	CommunityUserResponse listUserCommunities(ListCommunityUsersCommand cmd);

	void exportCommunityUsers(ListCommunityUsersCommand cmd, HttpServletResponse response);
	
	CountCommunityUserResponse countCommunityUsers(CountCommunityUsersCommand cmd);
	
	CommunityUserAddressDTO qryCommunityUserAddressByUserId(QryCommunityUserAddressByUserIdCommand cmd);
	
	CommunityUserAddressResponse listUserBycommunityId(ListCommunityUsersCommand cmd);
	
	CommunityUserAddressDTO qryCommunityUserEnterpriseByUserId(QryCommunityUserAddressByUserIdCommand cmd);
	
	CommunityUserAddressResponse listOwnerBycommunityId(ListCommunityUsersCommand cmd);
	
	CommunityAuthUserAddressResponse listCommunityAuthUserAddress(CommunityAuthUserAddressCommand cmd);
	
	CommunityUserAddressResponse listUserByNotJoinedCommunity(ListCommunityUsersCommand cmd);
	
	List<CommunityDTO> listUnassignedCommunitiesByNamespaceId();

	CreateCommunityResponse createCommunity(CreateCommunityCommand cmd);

	void importCommunity(ImportCommunityCommand cmd, MultipartFile[] files);

	ListCommunityByNamespaceIdResponse listCommunityByNamespaceId(ListCommunityByNamespaceIdCommand cmd);

	void communityImportBaseConfig(CommunityImportBaseConfigCommand cmd);

	void communityImportOrganizationConfig(CommunityImportOrganizationConfigCommand cmd);

	ListCommunityAuthPersonnelsResponse listCommunityAuthPersonnels(ListCommunityAuthPersonnelsCommand cmd);

	void updateCommunityUser(UpdateCommunityUserCommand cmd);
	
	/*--------添加修改资源分类 ------*/
	void createResourceCategory(CreateResourceCategoryCommand cmd);
	
	void updateResourceCategory(UpdateResourceCategoryCommand cmd);
	
	void deleteResourceCategory(DeleteResourceCategoryCommand cmd);
	
	void createResourceCategoryAssignment(CreateResourceCategoryAssignmentCommand cmd);
	
	void deleteResourceCategoryAssignment(CreateResourceCategoryAssignmentCommand cmd);
	
	ListCommunitiesByKeywordCommandResponse listCommunitiesByCategory(ListCommunitiesByCategoryCommand cmd);
	
	List<ResourceCategoryDTO> listResourceCategories(ListResourceCategoryCommand cmd);
	
	List<ResourceCategoryDTO> listTreeResourceCategories(ListResourceCategoryCommand cmd);
	
	List<ResourceCategoryDTO> listTreeResourceCategoryAssignments(ListResourceCategoryCommand cmd);

	List<ProjectDTO> listChildProjects(ListChildProjectCommand cmd);

	void createChildProject(CreateChildProjectCommand cmd);

	void updateChildProject(UpdateChildProjectCommand cmd);

	void deleteChildProject(DeleteChildProjectCommand cmd);

	List<ProjectDTO> getTreeProjectCategories(GetTreeProjectCategoriesCommand cmd);

	void updateBuildingOrder(@Valid UpdateBuildingOrderCommand cmd);
	ImportFileTaskDTO importBuildingData(Long communityId, MultipartFile file);

    /**
     * 用户认证的弹窗管理
     */
    CommunityAuthPopupConfigDTO getCommunityAuthPopupConfig(GetCommunityAuthPopupConfigCommand cmd);

    /**
     * 修改用户认证弹窗设置
     */
    CommunityAuthPopupConfigDTO updateCommunityAuthPopupConfig(UpdateCommunityAuthPopupConfigCommand cmd);
}
