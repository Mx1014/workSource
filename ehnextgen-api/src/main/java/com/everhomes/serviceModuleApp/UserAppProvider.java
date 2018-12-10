// @formatter:off
package com.everhomes.serviceModuleApp;

import java.util.List;


public interface UserAppProvider {

	void createUserApp(UserApp userApp);

	void updateUserApp(UserApp userApp);

	UserApp findUserAppById(Long id);

	List<UserApp> listUserApps(Long userId, Byte locationType, Long locationTargetId);

	List<UserApp> listUserApps(Byte locationType, Long locationTargetId,Long appId);

	Integer getMaxOrder(Long userId, Byte locationType, Long locationTargetId);

	void delete(Long id);

	void delete(List<Long> ids);

	void deleteByUserId(Long userId, Byte locationType, Long locationTargetId);
}