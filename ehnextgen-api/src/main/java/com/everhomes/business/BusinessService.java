package com.everhomes.business;

import com.everhomes.rest.address.*;
import com.everhomes.rest.address.BuildingDTO;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.address.admin.ListBuildingByCommunityIdsCommand;
import com.everhomes.rest.asset.CheckPaymentUserCommand;
import com.everhomes.rest.asset.CheckPaymentUserResponse;
import com.everhomes.rest.business.*;
import com.everhomes.rest.business.admin.*;
import com.everhomes.rest.community.GetCommunitiesByNameAndCityIdCommand;
import com.everhomes.rest.community.GetCommunityByIdCommand;
import com.everhomes.rest.openapi.*;
import com.everhomes.rest.region.ListRegionByKeywordCommand;
import com.everhomes.rest.region.ListRegionCommand;
import com.everhomes.rest.region.RegionDTO;
import com.everhomes.rest.ui.launchpad.FavoriteBusinessesBySceneCommand;
import com.everhomes.rest.ui.user.SearchContentsBySceneCommand;
import com.everhomes.rest.ui.user.SearchContentsBySceneReponse;
import com.everhomes.rest.ui.user.UserProfileDTO;
import com.everhomes.rest.user.GetUserDefaultAddressCommand;
import com.everhomes.rest.user.ListUserCommand;
import com.everhomes.rest.user.UserDtoForBiz;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.util.Tuple;

import javax.validation.Valid;
import java.util.List;

public interface BusinessService {
    void syncBusiness(SyncBusinessCommand cmd);
    void updateBusiness(UpdateBusinessCommand cmd);
    void deleteBusiness(DeleteBusinessCommand cmd);
    BusinessDTO findBusinessById(Long id);
    List<BusinessDTO> getBusinessesByScope(GetBusinessesByScopeCommand cmd);
    GetBusinessesByCategoryCommandResponse getBusinessesByCategory(GetBusinessesByCategoryCommand cmd);
    ListBusinessesByKeywordAdminCommandResponse listBusinessesByKeyword(ListBusinessesByKeywordAdminCommand cmd);
    void recommendBusiness(RecommendBusinessesAdminCommand cmd);
    void syncDeleteBusiness(SyncDeleteBusinessCommand cmd);
    void createBusiness(CreateBusinessAdminCommand cmd);
    void syncUserFavorite(UserFavoriteCommand cmd);
    void syncUserCancelFavorite(UserFavoriteCommand cmd);
    Byte findBusinessFavoriteStatus(UserFavoriteCommand cmd);
    void favoriteBusiness(FavoriteBusinessCommand cmd);
    void cancelFavoriteBusiness(CancelFavoriteBusinessCommand cmd);
    void promoteBusiness(PromoteBusinessAdminCommand cmd);
    void deletePromoteBusiness(DeletePromoteBusinessAdminCommand cmd);
	UserServiceAddressDTO getUserDefaultAddress(GetUserDefaultAddressCommand cmd);
	List<UserDtoForBiz> listUser(ListUserCommand cmd);
	void favoriteBusinesses(FavoriteBusinessesCommand cmd);
	void updateBusinessDistance(UpdateBusinessDistanceCommand cmd);
	BusinessDTO findBusinessById(FindBusinessByIdCommand cmd);
	ListBusinessByKeywordCommandResponse listBusinessByKeyword(ListBusinessByKeywordCommand cmd);
	List<UserInfo> listUserByKeyword(ListUserByKeywordCommand cmd);
	List<String> listBusinessByCommonityId(ListBusinessByCommonityIdCommand cmd);
	List<UserInfo> listUserByIdentifier(ListUserByIdentifierCommand cmd);
	void openBusinessAssignedNamespace(BusinessAsignedNamespaceCommand cmd);
	void closeBusinessAssignedNamespace(BusinessAsignedNamespaceCommand cmd);
	void updateReceivedCouponCount(UpdateReceivedCouponCountCommand cmd);
	UserProfileDTO getReceivedCouponCount(GetReceivedCouponCountCommand cmd);
	void reSyncBusiness(ReSyncBusinessCommand cmd);
	List<BuildingDTO> listBuildingsByKeyword(ListBuildingByCommunityIdsCommand cmd);

	List<BuildingDTO> listBuildingsByKeywordAndNameSpace(@Valid ListBuildingsByKeywordAndNameSpaceCommand cmd);
	Tuple<Integer, List<ApartmentDTO>> listApartmentsByKeyword(ListPropApartmentsByKeywordCommand cmd);
	List<CommunityDTO> getCommunitiesByNameAndCityId(GetCommunitiesByNameAndCityIdCommand cmd);
	CommunityDTO getCommunityById(GetCommunityByIdCommand cmd);
	List<RegionDTO> listRegionByKeyword(ListRegionByKeywordCommand cmd);
	
	void favoriteBusinessesByScene(FavoriteBusinessesBySceneCommand cmd, String baseScene);
	
	List<BusinessDTO> getBusinesses(List<Long> categoryIds,Long communityId);
	Integer findUserCouponCount(UserCouponsCommand cmd);
	void updateUserCouponCount(UpdateUserCouponCountCommand cmd);
	void addUserOrderCount(Long userId);
	void reduceUserOrderCount(Long userId);
	Integer findUserOrderCount(UserCouponsCommand cmd);
	void updateUserOrderCount(UpdateUserOrderCountCommand cmd);
	List<RegionDTO> listRegion(ListRegionCommand cmd);
	UserInfo validateUserPass(ValidateUserPassCommand cmd);
	CreateBusinessGroupResponse createBusinessGroup(CreateBusinessGroupCommand cmd);
	void joinBusinessGroup(JoinBusinessGroupCommand cmd);
	Tuple<Integer, List<ApartmentFloorDTO>> listApartmentFloor(ListApartmentFloorCommand cmd);

    /**
     * 电商运营数据
     */
    ListBusinessPromotionEntitiesReponse listBusinessPromotionEntities(ListBusinessPromotionEntitiesCommand cmd);

    /**
     * 新增或者修改运营数据
     */
    void createBusinessPromotion(CreateBusinessPromotionCommand cmd);

    void switchBusinessPromotionDataSource(SwitchBusinessPromotionDataSourceCommand cmd);
    
    /**
     * 搜索电商信息
     */
    SearchContentsBySceneReponse searchShops(SearchContentsBySceneCommand cmd);

    void testTransaction();

	UserAddressDTO getUserAddress(GetUserDefaultAddressCommand cmd);

	List<OrganizationDTO> getUserOrganizations(GetUserDefaultAddressCommand cmd);

    CheckPaymentUserResponse checkPaymentUser(CheckPaymentUserCommand cmd);
}
