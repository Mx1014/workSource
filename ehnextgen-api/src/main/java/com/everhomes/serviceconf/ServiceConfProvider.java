package com.everhomes.serviceconf;

import java.util.List;

import com.everhomes.server.schema.tables.pojos.EhCommunityServices;

public interface ServiceConfProvider {
	List<CommunityService> listCommunityServices(Integer namespaceId, Long ownerId, String ownerType, Byte scopeCode,
			Long scopeId, Integer pageSize, Long pageAnchor);
}
