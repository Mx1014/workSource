// @formatter:off
package com.everhomes.officecubicle;

import java.util.List;

public interface OfficeCubicleRangeProvider {

	void createOfficeCubicleRange(OfficeCubicleRange officeCubicleRange);

	void updateOfficeCubicleRange(OfficeCubicleRange officeCubicleRange);

	OfficeCubicleRange findOfficeCubicleRangeById(Long rangeId);

	List<OfficeCubicleRange> listOfficeCubicleRange();

    List<OfficeCubicleRange> listRangesBySpaceId(Long id);

	void deleteRangesBySpaceId(Long rangeId);

    OfficeCubicleRange findOfficeCubicleRangeByOwner(Long ownerId, String ownerType, Long spaceId, Integer namespaceId);

	List<OfficeCubicleRange> getOfficeCubicleRangeByOwner(Long ownerId, String ownerType, Integer namespaceId);
}