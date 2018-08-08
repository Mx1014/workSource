// @formatter:off
package com.everhomes.serviceModuleApp;

import java.util.List;


public interface UserAppProvider {

	void createUserApp(UserApp userApp);

	void updateUserApp(UserApp userApp);

	ServiceModuleApp findUserAppById(Long id);

	List<ServiceModuleApp> listUserApps(Integer namespaceId, Long versionId, Long moduleId);

	void deleteUserApp(Long id);
}