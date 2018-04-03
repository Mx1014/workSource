// @formatter:off
package com.everhomes.yellowPage;

import java.util.List;
import java.util.Map;

public interface ServiceAllianceCommentProvider {

	void createServiceAllianceComment(ServiceAllianceComment serviceAllianceComment);

	void updateServiceAllianceComment(ServiceAllianceComment serviceAllianceComment);
	
	void deleteServiceAllianceComment(ServiceAllianceComment serviceAllianceComment);

	ServiceAllianceComment findServiceAllianceCommentById(Long id);

	List<ServiceAllianceComment> listServiceAllianceComment();
	
	List<ServiceAllianceComment> listServiceAllianceCommentByOwner(Integer namespaceId,String ownerType,Long ownerId,
			Long pageAnchor,Integer pageSize);
	
	Map<String,Integer> listServiceAllianceCommentCountByOwner(Integer namespaceId,String ownerType,List<Long> ownerIds);

}