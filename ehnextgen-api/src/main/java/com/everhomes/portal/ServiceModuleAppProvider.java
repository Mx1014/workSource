// @formatter:off
package com.everhomes.portal;

import java.util.List;

public interface ServiceModuleAppProvider {

	void createServiceModuleApp(ServiceModuleApp serviceModuleApp);

	void updateServiceModuleApp(ServiceModuleApp serviceModuleApp);

	ServiceModuleApp findServiceModuleAppById(Long id);

	List<ServiceModuleApp> listServiceModuleApp();

}