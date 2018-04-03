// @formatter:off
package com.everhomes.user;

import com.everhomes.listing.ListingLocator;

import java.util.List;

public interface UserAppealLogProvider {

	void createUserAppealLog(UserAppealLog userAppealLog);

	void updateUserAppealLog(UserAppealLog userAppealLog);

	UserAppealLog findUserAppealLogById(Long id);

	List<UserAppealLog> listUserAppealLog(ListingLocator locator, Byte status, int pageSize);

}