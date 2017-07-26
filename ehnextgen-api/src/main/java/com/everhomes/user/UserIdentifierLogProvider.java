// @formatter:off
package com.everhomes.user;

import java.util.List;

public interface UserIdentifierLogProvider {

	void createUserIdentifierLog(UserIdentifierLog userIdentifierLog);

	void updateUserIdentifierLog(UserIdentifierLog userIdentifierLog);

	UserIdentifierLog findUserIdentifierLogById(Long id);

	List<UserIdentifierLog> listUserIdentifierLog();

    UserIdentifierLog findByUserIdAndIdentifier(Long uid, Integer regionCode, String identifier);

    UserIdentifierLog findByUserId(Long uid);

    UserIdentifierLog findByIdentifier(String identifier);
}