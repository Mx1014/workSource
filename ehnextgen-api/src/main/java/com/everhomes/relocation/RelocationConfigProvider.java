package com.everhomes.relocation;

public interface RelocationConfigProvider {

    RelocationConfig createRelocationConfig(RelocationConfig bean);
    RelocationConfig updateRelocationConfig(RelocationConfig bean);
    RelocationConfig searchRelocationConfigById(Integer namespaceId, String ownerType, Long ownerId, Long id);
}
