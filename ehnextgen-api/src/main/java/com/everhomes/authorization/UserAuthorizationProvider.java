// @formatter:off
package com.everhomes.authorization;

import java.util.List;

public interface UserAuthorizationProvider {

	void createUserAuthorization(UserAuthorization userAuthorization);
	
	void updateUserAuthorization(UserAuthorization userAuthorization);

	UserAuthorization findUserAuthorizationById(Long id);

	List<UserAuthorization> listUserAuthorization();
}
