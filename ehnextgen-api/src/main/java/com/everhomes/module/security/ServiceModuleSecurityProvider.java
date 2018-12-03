package com.everhomes.module.security;

public interface ServiceModuleSecurityProvider {
    ServiceModuleSecurity findServiceModuleSecurity(Integer namespaceId, Long moduleId, Long ownerId, String ownerType, Long userId);

    void createServiceModuleSecurity(ServiceModuleSecurity createServiceModuleSecurity);

    void updateServiceModuleSecurity(ServiceModuleSecurity updateServiceModuleSecurity);
}
