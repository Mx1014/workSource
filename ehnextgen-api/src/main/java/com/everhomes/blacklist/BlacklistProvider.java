package com.everhomes.blacklist;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by sfyan on 2016/12/9.
 */
public interface BlacklistProvider {

    List<UserBlacklist> listUserBlacklists(Integer namespaceId, String scopeType, Long scopeId, Timestamp startTime, Timestamp endTime, String keywords);

    List<UserBlacklist> listUserBlacklists(Integer namespaceId, ListingLocator locator, Integer pageSize, ListingQueryBuilderCallback queryBuilderCallback);

    Long createUserBlacklist(UserBlacklist userBlacklist);

    void updateUserBlacklist(UserBlacklist userBlacklist);

    UserBlacklist findUserBlacklistById(Long id);

    UserBlacklist findUserBlacklistByContactToken(Integer namespaceId, String scopeType, Long scopeId, String contactToken);
}
