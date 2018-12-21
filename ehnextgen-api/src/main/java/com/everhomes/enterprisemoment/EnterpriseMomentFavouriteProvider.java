// @formatter:off
package com.everhomes.enterprisemoment;

import java.util.List;

public interface EnterpriseMomentFavouriteProvider {

    int createEnterpriseMomentFavourite(EnterpriseMomentFavourite enterpriseMomentFavourite);

    void deleteEnterpriseMomentFavourite(EnterpriseMomentFavourite enterpriseMomentFavourite);

    EnterpriseMomentFavourite findEnterpriseMomentFavouriteById(Integer namespaceId, Long organizationId, Long userId, Long momentId);

	List<EnterpriseMomentFavourite> listEnterpriseMomentFavourite(Long enterpriseMomentId, Integer nameSpaceId, Long organizationId, Long pageAnchor, Integer pageSize);

	List<EnterpriseMomentFavourite> listEnterpriseMomentFavourite(Integer namespaceId, Long organizationId, Long momentId, Integer pageSize);

	Integer countFavourites(Integer namespaceId, Long organizationId, Long momentId);

    List<Long> findFavoiriteUserIds(Integer namespaceId, Long organizationId, Long momentId);

	Integer countFavourites(Integer namespaceId, Long organizationId, Long id, Long userId);
}