// @formatter:off
package com.everhomes.serviceModuleApp;


public interface UserAppFlagProvider {

	void createUserAppFlag(UserAppFlag userAppFlag);

	UserAppFlag findById(Long id);

	UserAppFlag findUserAppFlag(Long userId, Byte locationType, Long locationTargetId);

	void delete(Long id);

	void delete(Long userId, Byte locationType, Long locationTargetId);
}