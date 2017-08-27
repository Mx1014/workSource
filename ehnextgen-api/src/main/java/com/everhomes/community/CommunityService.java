// @formatter:off
package com.everhomes.community;

import java.util.List;

import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.community.*;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.community.admin.ApproveCommunityAdminCommand;
import com.everhomes.rest.community.admin.CommunityAuthUserAddressCommand;
import com.everhomes.rest.community.admin.CommunityAuthUserAddressResponse;
import com.everhomes.rest.community.admin.CommunityImportBaseConfigCommand;
import com.everhomes.rest.community.admin.CommunityImportOrganizationConfigCommand;
import com.everhomes.rest.community.admin.CommunityManagerDTO;
import com.everhomes.rest.community.admin.CommunityUserAddressDTO;
import com.everhomes.rest.community.admin.CommunityUserAddressResponse;
import com.everhomes.rest.community.admin.CommunityUserResponse;
import com.everhomes.rest.community.admin.CountCommunityUserResponse;
import com.everhomes.rest.community.admin.CountCommunityUsersCommand;
import com.everhomes.rest.community.admin.CreateCommunityCommand;
import com.everhomes.rest.community.admin.CreateCommunityResponse;
import com.everhomes.rest.community.admin.DeleteBuildingAdminCommand;
import com.everhomes.rest.community.admin.DeleteResourceCategoryCommand;
import com.everhomes.rest.community.admin.ImportCommunityCommand;
import com.everhomes.rest.community.admin.ListBuildingsByStatusCommandResponse;
import com.everhomes.rest.community.admin.ListCommunityAuthPersonnelsCommand;
import com.everhomes.rest.community.admin.ListCommunityAuthPersonnelsResponse;
import com.everhomes.rest.community.admin.ListCommunityByNamespaceIdCommand;
import com.everhomes.rest.community.admin.ListCommunityManagersAdminCommand;
import com.everhomes.rest.community.admin.ListCommunityUsersCommand;
import com.everhomes.rest.community.admin.ListComunitiesByKeywordAdminCommand;
import com.everhomes.rest.community.admin.ListUserCommunitiesCommand;
import com.everhomes.rest.community.admin.QryCommunityUserAddressByUserIdCommand;
import com.everhomes.rest.community.admin.RejectCommunityAdminCommand;
import com.everhomes.rest.community.admin.UpdateBuildingAdminCommand;
import com.everhomes.rest.community.admin.UpdateCommunityAdminCommand;
import com.everhomes.rest.community.admin.UpdateCommunityUserCommand;
import com.everhomes.rest.community.admin.UpdateResourceCategoryCommand;
import com.everhomes.rest.community.admin.UserCommunityDTO;
import com.everhomes.rest.community.admin.VerifyBuildingAdminCommand;
import com.everhomes.rest.community.admin.VerifyBuildingNameAdminCommand;
import com.everhomes.rest.community.admin.listBuildingsByStatusCommand;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.community.admin.ListCommunityByNamespaceIdResponse;
import com.everhomes.rest.user.admin.ImportDataResponse;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


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

	ListCommunitiesByOrgIdResponse listCommunitiesByOrgId(ListCommunitiesByOrgIdCommand cmd);
}
