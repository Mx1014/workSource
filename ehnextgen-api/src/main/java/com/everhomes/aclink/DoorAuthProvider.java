package com.everhomes.aclink;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.aclink.AuthVisitorStasticResponse;
import com.everhomes.rest.aclink.AuthVisitorStatisticCommand;
import com.everhomes.user.User;

import java.util.List;

public interface DoorAuthProvider {

    Long createDoorAuth(DoorAuth obj);

    Long createDoorAuthLog(DoorAuthLog log);

    void updateDoorAuth(DoorAuth obj);

    void deleteDoorAuth(DoorAuth obj);

    DoorAuth getDoorAuthById(Long id);

    DoorAuth queryValidDoorAuthByDoorIdAndUserId(Long doorId, Long userId);

    DoorAuth queryValidDoorAuthForever(Long doorId, Long userId);

    List<DoorAuth> queryDoorAuth(ListingLocator locator, int count, ListingQueryBuilderCallback queryBuilderCallback);

    List<DoorAuth> queryDoorAuthByApproveId(ListingLocator locator, Long approveId, int count);

    List<DoorAuth> searchDoorAuthByAdmin(ListingLocator locator, Long doorId, String keyword, Byte status, int count);

    List<DoorAuth> queryDoorAuthForeverByUserId(ListingLocator locator, Long userId, int count);

    DoorAuth getLinglingDoorAuthByUuid(String uuid);

    DoorAuth queryValidDoorAuthForever(Long doorId, Long userId, Byte rightOpen, Byte rightVisitor, Byte rightRemote);

    List<DoorAuth> searchVisitorDoorAuthByAdmin(ListingLocator locator, Long doorId, String keyword, Byte status,
            int count);

    List<DoorAuth> queryValidDoorAuthByUserId(ListingLocator locator, long userId, String driver, int count);

    AuthVisitorStasticResponse authVistorStatistic(AuthVisitorStatisticCommand cmd);

	void updateDoorAuth(List<DoorAuth> objs);

	List<DoorAuth> queryValidDoorAuths(ListingLocator locator, Long userId,
			Long ownerId, Byte ownerType, int count);

    List<User> listDoorAuthByOrganizationId(Long organizationId, Byte isOpenAuth, Long doorId, CrossShardListingLocator locator, int pageSize);

    List<User> listDoorAuthByIsAuth(Byte isAuth, Byte isOpenAuth, Long doorId, CrossShardListingLocator locator, int pageSize, Integer namespaceId);

    List<DoorAuthLog> listDoorAuthLogsByUserId(CrossShardListingLocator locator, int pageSize, Long userId, Long doorId);

    Long countDoorAuthUser(Byte isAuth, Byte isOpenAuth, Long doorId, Integer namespaceId, Byte rightType);

	DoorAuth queryValidDoorAuthByDoorIdAndUserId(Long doorId, Long userId, Byte isRemote);

	Long getNextDoorAuth();

    List<DoorAuth> searchVisitorDoorAuthByAdmin(Long doorId, String keyword, Byte status, int pageSize, Long startTime, Long endTime);
}
