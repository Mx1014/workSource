package com.everhomes.aclink;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.rest.aclink.*;
import com.everhomes.user.User;

import java.util.List;

public interface DoorAuthProvider {

    Long createDoorAuth(DoorAuth obj);

    Long createDoorAuthLog(DoorAuthLog log);

    void updateDoorAuth(DoorAuth obj);

    void deleteDoorAuth(DoorAuth obj);

    DoorAuth getDoorAuthById(Long id);

//    DoorAuth queryValidDoorAuthByDoorIdAndUserId(Long doorId, Long userId);

    DoorAuth queryValidDoorAuthForever(Long doorId, Long userId);

    List<DoorAuth> queryDoorAuth(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);

    List<DoorAuth> queryDoorAuthByApproveId(ListingLocator locator, Long approveId, int count);

    List<DoorAuth> searchDoorAuthByAdmin(ListingLocator locator, Long doorId, String keyword, Byte status, int count);

    List<DoorAuth> queryDoorAuthForeverByUserId(ListingLocator locator, ListAuthsByLevelandLocationCommand qryCmd, Byte rightRemote, int count);

    DoorAuth getLinglingDoorAuthByUuid(String uuid);

    DoorAuth queryValidDoorAuthForever(Long doorId, Long userId, Byte rightOpen, Byte rightVisitor, Byte rightRemote);
    
    /**
     * 查单个对象的永久有效权限
     */
    DoorAuth queryValidDoorAuthForever(Long doorId, Long userId, Byte licenseeType);

    List<DoorAuth> searchVisitorDoorAuthByAdmin(ListingLocator locator, SearchDoorAuthCommand cmd,
            int count);

    List<DoorAuth> searchTempAuthByAdmin(ListingLocator locator, SearchDoorAuthCommand cmd,
										 int count);
    /**
     * 查用户及其所属组织的所有有效权限
     */
    List<DoorAuth> queryValidDoorAuthByUserId(ListingLocator locator, ListAuthsByLevelandLocationCommand qryCmd, int count);

    AuthVisitorStasticResponse authVistorStatistic(AuthVisitorStatisticCommand cmd);

	void updateDoorAuth(List<DoorAuth> objs);

	/**
	 * 查用户授权,不包括所属组织的授权
	 */
	List<DoorAuth> queryValidDoorAuths(ListingLocator locator, Long userId,
			Long ownerId, Byte ownerType, int count);
	
    List<User> listDoorAuthByOrganizationId(Long organizationId, Byte isOpenAuth, Long doorId, CrossShardListingLocator locator, int pageSize);

    List<User> listDoorAuthByIsAuth(Byte isAuth, Byte isOpenAuth, Long doorId, CrossShardListingLocator locator, int pageSize, Integer namespaceId);

    List<DoorAuthLog> listDoorAuthLogsByUserId(CrossShardListingLocator locator, int pageSize, Long userId, Long doorId);

    Long countDoorAuthUser(Byte isAuth, Byte isOpenAuth, Long doorId, Integer namespaceId, Byte rightType);

	DoorAuth queryValidDoorAuthByDoorIdAndUserId(Long doorId, Long userId, Byte isRemote);

	Long getNextDoorAuth();

    List<DoorAuth> searchVisitorDoorAuthByAdmin(Long doorId, String keyword, Byte status, int pageSize, Long startTime, Long endTime);

    /**
     * 获取小区的楼栋用户
     * @param communityId
     * @param buildingName
     * @param locator
     * @param pageSize
     * @return
     */
    public List<Long> listDoorAuthByBuildingName(Byte isOpenAuth, Long doorId, Long communityId, String buildingName, CrossShardListingLocator locator, int pageSize, Integer namespaceId);

    /**
     * 获取办公区的楼栋用户
     * @param isOpenAuth
     * @param doorId
     * @param communityId
     * @param buildingName
     * @param locator
     * @param pageSize
     * @param namespaceId
     * @return
     */
    List<Long> listDoorAuthByBuildingName2(Byte isOpenAuth, Long doorId,
            Long communityId, String buildingName,
            CrossShardListingLocator locator, int pageSize, Integer namespaceId);

    DoorAuth queryValidDoorAuthByVisitorPhone(Long doorId, String phone);

	List<DoorAuth> listValidDoorAuthByVisitorPhone(Long doorId, String phone, Byte groupType);

	List<User> listCommunityAclinkUsers(Byte isAuth, Byte isOpenAuth, Long doorId, Byte communityType, Long ownerId,
			CrossShardListingLocator locator, int pageSize, Integer namespaceId);

	Long countCommunityDoorAuthUser(Byte isAuth, Byte isOpenAuth, Long doorId, Long communityId, Byte communityType,
			Integer namespaceId, Byte rightType);

    List<DoorAuth> listValidDoorAuthByUser(long userId, String driver);
    
    void createDoorAuthLogBatch(List<DoorAuthLog> logs);

	List<AclinkAuthDTO> listFormalAuth(CrossShardListingLocator locator, Integer pageSize, ListFormalAuthCommand cmd);

	void createDoorAuthBatch(List<DoorAuth> cAuths);

	void updateDoorAuthBatch(List<DoorAuth> uAuths);

	List<OrganizationMember> getOrganizationMemberByUserId(Long id);

	/**
	 * 查用户及其所属组织的权限,及门禁相关信息
	 */
	List<DoorAuthLiteDTO> listAuthsByLevelandLocation(CrossShardListingLocator locator, Integer count, ListAuthsByLevelandLocationCommand qryCmd);

	/**
	 * 查用户及其所属组织的权限
	 */
	List<DoorAuth> queryDoorAuthAllLicensee(ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);
	
	/**
	 * 查用户及其所属组织的有效常规权限
	 */
	DoorAuth queryValidDoorAuthForever(QueryValidDoorAuthForeverCommand qryCmd);

	List<DoorAuth> listValidDoorAuthForever(QueryValidDoorAuthForeverCommand qryCmd);

}
