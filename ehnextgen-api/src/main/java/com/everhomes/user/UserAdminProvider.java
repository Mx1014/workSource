package com.everhomes.user;

import java.util.List;

import org.jooq.Condition;

import com.everhomes.address.Address;
import com.everhomes.group.Group;
import com.everhomes.group.GroupMember;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.user.UserInfo;

public interface UserAdminProvider {
    List<UserIdentifier> listUserIdentifiers(List<Long> uids);

    List<UserIdentifier> listAllVerifyCode(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback callback);

    List<UserInfo> listRegisterByOrder(CrossShardListingLocator locator, int count, ListingQueryBuilderCallback callback);

    List<UserIdentifier> listUserIdentifiersByOrder(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback callback, Condition... conditons);

    List<UserIdentifier> listVetsByOrder(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback callback);
    
    List<UserIdentifier> listIdentifierTokenByType(String start, Byte type);
    
    Long getRegionIdByCityName(String cityName);
    
    Long getRegionIdByAreaName(Long cityId, String areaName);
    
    Long getCommunitiesIdByName(Long cityId, Long areaId, String name);
    
    List<Address> getAddressByApartments(String buildingName, String apartment_name);
    
    Address createAddress(Address address);

    List<UserIdentifier> listAllVerifyCodeByPhone(
            CrossShardListingLocator locator, String phone, int count,
            ListingQueryBuilderCallback callback);
    
}
